package se.swedsoft.bookkeeping.print.report.sales;


import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.SSCreditInvoice;
import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSCreditinvoicePrinter extends SSPrinter {

    private SSCreditInvoice iCreditInvoice;

    private Locale iLocale;

    /**
     *
     * @param iInvoice
     * @param iLocale
     */
    public SSCreditinvoicePrinter(SSCreditInvoice iInvoice, Locale iLocale) {
        iCreditInvoice = iInvoice;
        this.iLocale = iLocale;

        ResourceBundle iBundle = ResourceBundle.getBundle("reports.creditinvoicereport",
                iLocale);

        setBundle(iBundle);
        setLocale(iLocale);

        setMargins(0, 0, 0, 0);

        setPageHeader("sales/sale.header.jrxml");
        setPageFooter("sales/sale.footer.jrxml");

        setDetail("sales/creditinvoice.jrxml");
        setColumnHeader("sales/creditinvoice.jrxml");

        addParameters();

    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        addParameter("title.date", iBundle.getString("creditinvoicereport.title.date"));
        addParameter("title.number", iBundle.getString("creditinvoicereport.title.number"));

        return iBundle.getString("creditinvoicereport.title");
    }

    /**
     *
     */
    private void addParameters() {
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        SSCustomer iCustomer = iCreditInvoice.getCustomer(
                SSDB.getInstance().getCustomers());

        // Company parameters
        SSSalePrinterUtils.addParametersForCompany(iCompany, this);

        // Sale parameters
        addParameter("number", iCreditInvoice.getNumber());
        addParameter("date", iCreditInvoice.getDate());
        addParameter("text", iCreditInvoice.getText());

        addParameter("creditinvoice.hasdiscount",
                SSInvoiceMath.hasDiscount(iCreditInvoice));

        addParameter("creditinvoice.deliveryadress.name",
                iCreditInvoice.getDeliveryAddress().getName());
        addParameter("creditinvoice.deliveryadress.address1",
                iCreditInvoice.getDeliveryAddress().getAddress1());
        addParameter("creditinvoice.deliveryadress.address2",
                iCreditInvoice.getDeliveryAddress().getAddress2());
        addParameter("creditinvoice.deliveryadress.zipcode",
                iCreditInvoice.getDeliveryAddress().getZipCode());
        addParameter("creditinvoice.deliveryadress.city",
                iCreditInvoice.getDeliveryAddress().getCity());
        addParameter("creditinvoice.deliveryadress.country",
                iCreditInvoice.getDeliveryAddress().getCountry());

        addParameter("creditinvoice.invoiceadress.name",
                iCreditInvoice.getInvoiceAddress().getName());
        addParameter("creditinvoice.invoiceadress.address1",
                iCreditInvoice.getInvoiceAddress().getAddress1());
        addParameter("creditinvoice.invoiceadress.address2",
                iCreditInvoice.getInvoiceAddress().getAddress2());
        addParameter("creditinvoice.invoiceadress.zipcode",
                iCreditInvoice.getInvoiceAddress().getZipCode());
        addParameter("creditinvoice.invoiceadress.city",
                iCreditInvoice.getInvoiceAddress().getCity());
        addParameter("creditinvoice.invoiceadress.country",
                iCreditInvoice.getInvoiceAddress().getCountry());

        SSInvoice iInvoice = iCreditInvoice.getCrediting();

        addParameter("creditinvoice.ourcontact", iCreditInvoice.getOurContactPerson());
        addParameter("creditinvoice.deliveryterm", iCreditInvoice.getDeliveryTerm(), true);
        addParameter("creditinvoice.deliveryway", iCreditInvoice.getDeliveryWay(), true);
        addParameter("creditinvoice.paymentterm", iCreditInvoice.getPaymentTerm(), true);
        addParameter("creditinvoice.delayinterest", iCreditInvoice.getDelayInterest(),
                true);
        addParameter("creditinvoice.currency", iCreditInvoice.getCurrency(), true);
        addParameter("creditinvoice.customernr", iCreditInvoice.getCustomerNr());
        addParameter("creditinvoice.yourcontact", iCreditInvoice.getYourContactPerson());
        addParameter("creditinvoice.yourordernumber", iCreditInvoice.getYourOrderNumber());
        addParameter("creditinvoice.paymentday", iCreditInvoice.getDueDate());

        if (iInvoice != null && iInvoice.getOCRNumber() != null) {
            addParameter("creditinvoice.invoicenumber", iInvoice.getOCRNumber());
        } else {
            addParameter("creditinvoice.invoicenumber", iCreditInvoice.getCreditingNr(),
                    true);
        }

        addParameter("creditinvoice.taxrate1", iCreditInvoice.getTaxRate1().toString());
        addParameter("creditinvoice.taxrate2", iCreditInvoice.getTaxRate2().toString());
        addParameter("creditinvoice.taxrate3", iCreditInvoice.getTaxRate3().toString());

        if (iCustomer != null) {
            addParameter("creditinvoice.vatnumber", iCustomer.getVATNumber());
        }

        // Calculate the sum fields...
        BigDecimal                 iNetSum = SSInvoiceMath.getNetSum(iCreditInvoice);
        Map<SSTaxCode, BigDecimal> iTaxSum = SSInvoiceMath.getTaxSum(iCreditInvoice);
        BigDecimal                 iTotalSum = SSInvoiceMath.getTotalSum(iCreditInvoice);
        BigDecimal                 iRounding = SSInvoiceMath.getRounding(iCreditInvoice);

        // ... add them to the report
        addParameter("creditinvoice.netsum", iNetSum);
        addParameter("creditinvoice.taxsum1", iTaxSum.get(SSTaxCode.TAXRATE_1));
        addParameter("creditinvoice.taxsum2", iTaxSum.get(SSTaxCode.TAXRATE_2));
        addParameter("creditinvoice.taxsum3", iTaxSum.get(SSTaxCode.TAXRATE_3));
        addParameter("creditinvoice.rounding", iRounding);
        addParameter("creditinvoice.totalsum", iTotalSum);

        // QR-code 
        DateFormat iFormat = new SimpleDateFormat("yyyyMMdd");
        final StringBuilder uqrData = new StringBuilder();

        uqrData.append("{\"uqr\": 2, \"tp\": 2, ");
        uqrData.append("\"nme\": \"");
        uqrData.append(iCompany.getName());
        // fixme! - cc: iCompany.getAddress().getCountry() -> CountyCode
        uqrData.append("\", \"cc\": \"SE\""); 
        uqrData.append(", \"cid\": \"");
        uqrData.append(iCompany.getCorporateID());
        uqrData.append("\", \"iref\": \"");
        uqrData.append(iCreditInvoice.hasOCRNumber() ? iCreditInvoice.getOCRNumber() : iCreditInvoice.getNumber());
        uqrData.append("\", \"cr\": \"");
        uqrData.append(iCreditInvoice.getCreditingNr());
        uqrData.append("\", \"idt\": \"");
        uqrData.append(iFormat.format(iCreditInvoice.getDate()));
        uqrData.append("\", \"ddt\": \"");
        uqrData.append(iFormat.format(iCreditInvoice.getDueDate()));
        uqrData.append("\", \"due\": ");
        uqrData.append(iTotalSum.negate());
        uqrData.append(", \"vat\": ");
        uqrData.append(iTotalSum.subtract(iNetSum).subtract(iRounding).setScale(2, RoundingMode.HALF_UP).negate());
        uqrData.append(", \"vh\": ");
        uqrData.append(iTaxSum.get(SSTaxCode.TAXRATE_1).setScale(2, RoundingMode.HALF_UP).negate());
        uqrData.append(", \"vm\": ");
        uqrData.append(iTaxSum.get(SSTaxCode.TAXRATE_2).setScale(2, RoundingMode.HALF_UP).negate());
        uqrData.append(", \"vl\": ");
        uqrData.append(iTaxSum.get(SSTaxCode.TAXRATE_3).setScale(2, RoundingMode.HALF_UP).negate());
        uqrData.append(", \"cur\": \"");
        uqrData.append(iCreditInvoice.getCurrency()); 
        uqrData.append("\", ");
        uqrData.append("\"adr\": \"");
        uqrData.append(iCompany.getAddress().getZipCode());
        uqrData.append(" ");
        uqrData.append(iCompany.getAddress().getCity());
        uqrData.append("\"");
        uqrData.append("}");

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

                return iPrinter.getDataSource();
            }
        };

        iModel.addColumn("subreport.datasource");

        iModel.setObjects(iCreditInvoice);

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

            setColumnHeader("sales/creditinvoice.rows.jrxml");
            setDetail("sales/creditinvoice.rows.jrxml");

            setPageFooter("sales/creditinvoice.rows.jrxml");
            setLastPageFooter("sales/creditinvoice.rows.jrxml");
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

            iModel.setObjects(iCreditInvoice.getRows());

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

        sb.append("se.swedsoft.bookkeeping.print.report.sales.SSCreditinvoicePrinter");
        sb.append("{iCreditInvoice=").append(iCreditInvoice);
        sb.append(", iLocale=").append(iLocale);
        sb.append('}');
        return sb.toString();
    }
}
