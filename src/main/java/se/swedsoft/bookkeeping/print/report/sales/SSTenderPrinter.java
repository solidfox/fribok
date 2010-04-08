package se.swedsoft.bookkeeping.print.report.sales;

import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSSaleMath;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.SSTender;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSTenderPrinter extends SSPrinter {

    private SSTender iTender;

    private Locale iLocale;

    /**
     *
     * @param iTender
     * @param iLocale
     */
    public SSTenderPrinter(SSTender iTender, Locale iLocale){
        this.iTender  = iTender;
        this.iLocale = iLocale;

        ResourceBundle iBundle = ResourceBundle.getBundle("reports.tenderreport", iLocale);

        setBundle( iBundle );
        setLocale( iLocale );

        setMargins(0,0,0,0);

        setPageHeader  ("sales/sale.header.jrxml");
        setPageFooter  ("sales/sale.footer.jrxml");

        setDetail      ("sales/tender.jrxml");
        setColumnHeader("sales/tender.jrxml");
        //setBackground  ("sales/tender.jrxml");

        addParameters();


    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        addParameter("title.date"     , iBundle.getString("tenderreport.title.date") );
        addParameter("title.number"   , iBundle.getString("tenderreport.title.number") );

        return iBundle.getString("tenderreport.title");
    }



    /**
     *
     */
    private void addParameters(){
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        SSSalePrinterUtils.addParametersForCompany(iCompany, this);

        // Sale parameters
        addParameter("number"   , iTender.getNumber() );
        addParameter("date"     , iTender.getDate() );
        addParameter("text"     , iTender.getText() );

        addParameter("tender.hasdiscount"   , SSSaleMath.hasDiscount(iTender));

        addParameter("tender.deliveryadress.name"    , iTender.getDeliveryAddress().getName() );
        addParameter("tender.deliveryadress.address1", iTender.getDeliveryAddress().getAddress1() );
        addParameter("tender.deliveryadress.address2", iTender.getDeliveryAddress().getAddress2() );
        addParameter("tender.deliveryadress.zipcode" , iTender.getDeliveryAddress().getZipCode() );
        addParameter("tender.deliveryadress.city"    , iTender.getDeliveryAddress().getCity() );
        addParameter("tender.deliveryadress.country" , iTender.getDeliveryAddress().getCountry() );

        addParameter("tender.invoiceadress.name"    , iTender.getInvoiceAddress().getName() );
        addParameter("tender.invoiceadress.address1", iTender.getInvoiceAddress().getAddress1() );
        addParameter("tender.invoiceadress.address2", iTender.getInvoiceAddress().getAddress2() );
        addParameter("tender.invoiceadress.zipcode" , iTender.getInvoiceAddress().getZipCode() );
        addParameter("tender.invoiceadress.city"    , iTender.getInvoiceAddress().getCity() );
        addParameter("tender.invoiceadress.country" , iTender.getInvoiceAddress().getCountry() );


        addParameter("tender.ourcontact"               , iTender.getOurContactPerson() );
        addParameter("tender.deliveryterm"             , iTender.getDeliveryTerm() , true);
        addParameter("tender.deliveryway"              , iTender.getDeliveryWay () , true);
        addParameter("tender.paymentterm"              , iTender.getPaymentTerm () , true);
        addParameter("tender.delayinterest"            , iTender.getDelayInterest () , true);
        addParameter("tender.currency"                 , iTender.getCurrency() , true);

        addParameter("tender.customernr"               , iTender.getCustomerNr() );
        addParameter("tender.yourcontact"              , iTender.getYourContactPerson() );

        addParameter("tender.taxrate1"               , iTender.getTaxRate1().toString() );
        addParameter("tender.taxrate2"               , iTender.getTaxRate2().toString() );
        addParameter("tender.taxrate3"               , iTender.getTaxRate3().toString() );


        // Calculate the sum fields...
        BigDecimal                 iNetSum   = SSInvoiceMath.getNetSum    (iTender);
        Map<SSTaxCode, BigDecimal> iTaxSum   = SSInvoiceMath.getTaxSum    (iTender);
        BigDecimal                 iTotalSum = SSInvoiceMath.getTotalSum  (iTender);
        BigDecimal                 iRounding = SSInvoiceMath.getRounding  (iTender);

        // ... add them to the report
        addParameter("tender.netsum"            , iNetSum );
        addParameter("tender.taxsum1"           , iTaxSum.get(SSTaxCode.TAXRATE_1) );
        addParameter("tender.taxsum2"           , iTaxSum.get(SSTaxCode.TAXRATE_2) );
        addParameter("tender.taxsum3"           , iTaxSum.get(SSTaxCode.TAXRATE_3) );
        addParameter("tender.rounding"          , iRounding );
        addParameter("tender.totalsum"          , iTotalSum );
    }


    /**
     *
     * @return
     */
    @Override
    protected SSDefaultTableModel getModel() {
        final SSPrinter iPrinter = new SSRowReport(  );

        iPrinter.setBundle(iBundle);
        iPrinter.setLocale(iLocale);
        iPrinter.generateReport();

        addParameter("subreport.report"      , iPrinter.getReport());
        addParameter("subreport.parameters"  , iPrinter.getParameters() );
        addParameter("subreport.datasource"  , iPrinter.getDataSource() );

        SSDefaultTableModel<SSTender> iModel = new SSDefaultTableModel<SSTender>(  ) {

            @Override
            public Class getType() {
                return SSTender.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                iPrinter.getDataSource().reset();

                return iPrinter.getDataSource();
            }
        };

        iModel.addColumn("subreport.datasource");

        iModel.setObjects(iTender);

        return iModel;
    }




    /**
     *
     */
    private class SSRowReport extends SSPrinter {

        /**
         *
         */
        public SSRowReport( ){
            setMargins(0,0,0,0);

            setColumnHeader("sales/tender.rows.jrxml");
            setDetail      ("sales/tender.rows.jrxml");

            setPageFooter    ("sales/tender.rows.jrxml");
            setLastPageFooter("sales/tender.rows.jrxml");

        }

        /**
         * Gets the data model for this report
         *
         * @return SSDefaultTableModel
         */
        @Override
        protected SSDefaultTableModel getModel() {
            SSDefaultTableModel<SSSaleRow> iModel = new SSDefaultTableModel<SSSaleRow>(  ) {

                @Override
                public Class getType() {
                    return SSSaleRow.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    SSSaleRow iRow = getObject(rowIndex);

                    switch (columnIndex) {
                        case 0  :
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

            iModel.setObjects( iTender.getRows() );

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
        sb.append("se.swedsoft.bookkeeping.print.report.sales.SSTenderPrinter");
        sb.append("{iLocale=").append(iLocale);
        sb.append(", iTender=").append(iTender);
        sb.append('}');
        return sb.toString();
    }
}
