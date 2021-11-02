package se.swedsoft.bookkeeping.print.report.sales;

import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.common.SSInvoiceType;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 *
 * $Id$
 */
public class SSInvoicePrinter extends SSPrinter {

    protected SSInvoice iInvoice;

    protected Locale iLocale;

    /**
     *
     */
    protected SSInvoicePrinter() {}

    /**
     *
     * @param iInvoice
     * @param iLocale
     */
    public SSInvoicePrinter(SSInvoice iInvoice, Locale iLocale) {
        this.iInvoice = iInvoice;
        this.iLocale = iLocale;

        ResourceBundle iBundle = ResourceBundle.getBundle("reports.invoicereport", iLocale);

        setBundle(iBundle);
        setLocale(iLocale);

        setMargins(0, 0, 0, 0);

        setPageHeader("sales/sale.header.jrxml");
        setPageFooter("sales/sale.footer.jrxml");

        setDetail("sales/invoice.jrxml");
        setColumnHeader("sales/invoice.jrxml");

        addParameters();
    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        addParameter("title.date", iBundle.getString("invoicereport.title.date"));
        addParameter("title.number", iBundle.getString("invoicereport.title.number"));

        if (iInvoice.getType() == SSInvoiceType.NORMAL) {
            addParameter("invoicereport.totalsum",
                    iBundle.getString("invoicereport.totalsum.normal"));

            return iBundle.getString("invoicereport.title.normal");
        } else {
            addParameter("invoicereport.totalsum",
                    iBundle.getString("invoicereport.totalsum.cash"));

            return iBundle.getString("invoicereport.title.cash");
        }
    }

    /**
     *
     */
    protected void addParameters() {
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        SSCustomer iCustomer = iInvoice.getCustomer(SSDB.getInstance().getCustomers());

        SSSalePrinterUtils.addParametersForCompany(iCompany, this);

        // Sale parameters
        addParameter("number", iInvoice.getNumber());
        addParameter("date", iInvoice.getDate());
        addParameter("text", iInvoice.getText());

        addParameter("invoice.hasdiscount", SSInvoiceMath.hasDiscount(iInvoice));

        addParameter("invoice.deliveryadress.name",
                iInvoice.getDeliveryAddress().getName());
        addParameter("invoice.deliveryadress.address1",
                iInvoice.getDeliveryAddress().getAddress1());
        addParameter("invoice.deliveryadress.address2",
                iInvoice.getDeliveryAddress().getAddress2());
        addParameter("invoice.deliveryadress.zipcode",
                iInvoice.getDeliveryAddress().getZipCode());
        addParameter("invoice.deliveryadress.city",
                iInvoice.getDeliveryAddress().getCity());
        addParameter("invoice.deliveryadress.country",
                iInvoice.getDeliveryAddress().getCountry());

        addParameter("invoice.invoiceadress.name", iInvoice.getInvoiceAddress().getName());
        addParameter("invoice.invoiceadress.address1",
                iInvoice.getInvoiceAddress().getAddress1());
        addParameter("invoice.invoiceadress.address2",
                iInvoice.getInvoiceAddress().getAddress2());
        addParameter("invoice.invoiceadress.zipcode",
                iInvoice.getInvoiceAddress().getZipCode());
        addParameter("invoice.invoiceadress.city", iInvoice.getInvoiceAddress().getCity());
        addParameter("invoice.invoiceadress.country",
                iInvoice.getInvoiceAddress().getCountry());

        addParameter("invoice.ourcontact", iInvoice.getOurContactPerson());
        addParameter("invoice.deliveryterm", iInvoice.getDeliveryTerm(), true);
        addParameter("invoice.deliveryway", iInvoice.getDeliveryWay(), true);
        addParameter("invoice.paymentterm", iInvoice.getPaymentTerm(), true);
        addParameter("invoice.delayinterest", iInvoice.getDelayInterest(), true);
        addParameter("invoice.currency", iInvoice.getCurrency(), true);

        addParameter("invoice.customernr", iInvoice.getCustomerNr());
        addParameter("invoice.yourcontact", iInvoice.getYourContactPerson());
        addParameter("invoice.yourordernumber", iInvoice.getYourOrderNumber());
        addParameter("invoice.paymentday", iInvoice.getDueDate());

        addParameter("invoice.taxrate1", iInvoice.getTaxRate1().toString());
        addParameter("invoice.taxrate2", iInvoice.getTaxRate2().toString());
        addParameter("invoice.taxrate3", iInvoice.getTaxRate3().toString());

        if (iCustomer != null) {
            addParameter("invoice.vatnumber", iCustomer.getVATNumber());
        }

        // Calculate the sum fields...
        BigDecimal                 iNetSum = SSInvoiceMath.getNetSum(iInvoice);
        Map<SSTaxCode, BigDecimal> iTaxSum = SSInvoiceMath.getTaxSum(iInvoice);
        BigDecimal                 iTotalSum = SSInvoiceMath.getTotalSum(iInvoice);
        BigDecimal                 iRounding = SSInvoiceMath.getRounding(iInvoice);

        // ... add them to the report
        addParameter("invoice.netsum", iNetSum);
        addParameter("invoice.taxsum1", iTaxSum.get(SSTaxCode.TAXRATE_1));
        addParameter("invoice.taxsum2", iTaxSum.get(SSTaxCode.TAXRATE_2));
        addParameter("invoice.taxsum3", iTaxSum.get(SSTaxCode.TAXRATE_3));
        addParameter("invoice.rounding", iRounding);
        addParameter("invoice.totalsum", iTotalSum);


        // QR-code 
        DateFormat iFormat = new SimpleDateFormat("yyyyMMdd");
        final StringBuilder uqrData = new StringBuilder();

        uqrData.append("{\"uqr\": 1, \"tp\": 1, ");
        uqrData.append("\"nme\": \""); 
        uqrData.append(iCompany.getName());
        uqrData.append("\", \"cc\": \"SE\""); //iCompany.getAddress().getCountry()
        uqrData.append(", \"cid\": \"");
        uqrData.append(iCompany.getCorporateID());
        uqrData.append("\", \"iref\": \"");
        uqrData.append(iInvoice.getNumber());
        uqrData.append("\", \"idt\": \"");
        uqrData.append(iFormat.format(iInvoice.getDate()));
        uqrData.append("\", \"ddt\": \"");
        uqrData.append(iFormat.format(iInvoice.getDueDate()));
        uqrData.append("\", \"due\": ");
        uqrData.append(iTotalSum);
        uqrData.append(", \"vat\": ");
        uqrData.append((iTotalSum.subtract(iNetSum)));
        uqrData.append(", \"cur\": \"");
        uqrData.append(iInvoice.getCurrency()); 
        uqrData.append("\", \"pt\": \"");
        uqrData.append(iCompany.getBankGiroNumber() != ""? "BG": "");  
        uqrData.append("\", \"acc\": \"");
        uqrData.append(iCompany.getBankGiroNumber());  //iCompany.getPlusGiroNumber(), iCompany.getBIC(), iCompany.getIBAN() 
        uqrData.append("\"}");

        SSSalePrinterUtils.addParameterForQRCode(uqrData.toString(), this);

    }

    /**
     *
     * @return
     */
    @Override
    protected SSDefaultTableModel getModel() {
        final SSPrinter iPrinter = new SSRowReport();

        iPrinter.setBundle(iBundle);
        iPrinter.setLocale(iLocale);
        iPrinter.generateReport();
        iPrinter.getDataSource().reset();

        addParameter("subreport.report", iPrinter.getReport());
        addParameter("subreport.parameters", iPrinter.getParameters());
        addParameter("subreport.datasource", iPrinter.getDataSource());

        SSDefaultTableModel<SSInvoice> iModel = new SSDefaultTableModel<SSInvoice>() {

            @Override
            public Class getType() {
                return SSInvoice.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                iPrinter.getDataSource().reset();

                return getObject(rowIndex);
            }
        };

        iModel.setObjects(iInvoice);

        return iModel;
    }

    /**
     *
     */
    private class SSRowReport extends SSPrinter {

        /**
         *
         */
        public SSRowReport() {
            setMargins(0, 0, 0, 0);

            setColumnHeader("sales/invoice.rows.jrxml");
            setDetail("sales/invoice.rows.jrxml");

            setPageFooter("sales/invoice.rows.jrxml");
            setLastPageFooter("sales/invoice.rows.jrxml");

        }

        /**
         * Gets the data model for this report
         *
         * @return SSDefaultTableModel
         */
        @Override
        protected SSDefaultTableModel getModel() {
            SSDefaultTableModel<SSSaleRow> iModel = new SSDefaultTableModel<SSSaleRow>() {

                @Override
                public Class getType() {
                    return SSSaleRow.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    SSSaleRow iRow = getObject(rowIndex);

                    switch (columnIndex) {
                    case 0:
                        value = iRow.getProductNr();
                        break;

                    case 1:
                        value = iRow.getDescription(iLocale);
                        break;

                    case 2:
                        value = iRow.getQuantity();
                        break;

                    case 3:
                        value = iRow.getUnit() == null ? null : iRow.getUnit().getName();
                        break;

                    case 4:
                        value = iRow.getUnitprice();
                        break;

                    case 5:
                        value = iRow.getDiscount();
                        break;

                    case 6:
                        value = iRow.getSum();
                        break;
                    }

                    return value;
                }
            };

            iModel.addColumn("row.number");
            iModel.addColumn("row.description");
            iModel.addColumn("row.count");
            iModel.addColumn("row.unit");
            iModel.addColumn("row.unitprice");
            iModel.addColumn("row.discount");
            iModel.addColumn("row.sum");

            List<SSSaleRow> iRows = new LinkedList<SSSaleRow>(iInvoice.getRows());

            if (iInvoice.getOrderNumbers() != null
                    && iInvoice.getOrderNumbers().length() != 0
                    && !iInvoice.getOrderNumbers().equals("Fakturan har inga ordrar")) {
                SSSaleRow iRow = new SSSaleRow();

                iRow.setProductNr(
                        iLocale.getLanguage().equals("en")
                                ? "Contains order:"
                                : "Avser order:");
                iRow.setDescription(iInvoice.getOrderNumbers());
                iRows.add(iRow);
            }
            iModel.setObjects(iRows);

            return iModel;
        }

        /**
         * Gets the title for this report
         *
         * @return The title
         */
        @Override
        public String getTitle() {
            return null;
        }

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.report.sales.SSInvoicePrinter");
        sb.append("{iInvoice=").append(iInvoice);
        sb.append(", iLocale=").append(iLocale);
        sb.append('}');
        return sb.toString();
    }
}
