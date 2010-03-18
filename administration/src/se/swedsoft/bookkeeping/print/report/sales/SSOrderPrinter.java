package se.swedsoft.bookkeeping.print.report.sales;

import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSSaleMath;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.SSOrder;
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
public class SSOrderPrinter extends SSPrinter {

    private SSOrder iOrder;

    private Locale iLocale;

    /**
     *
     * @param iOrder
     * @param iLocale
     */
    public SSOrderPrinter(SSOrder iOrder, Locale iLocale){
        super();
        this.iOrder  = iOrder;
        this.iLocale = iLocale;

        ResourceBundle iBundle = ResourceBundle.getBundle("se.swedsoft.bookkeeping.resources.orderreport", iLocale);

        setBundle( iBundle );
        setLocale( iLocale );

        setMargins(0,0,0,0);

        setPageHeader  ("sales/sale.header.jrxml");
        setPageFooter  ("sales/sale.footer.jrxml");

        setDetail      ("sales/order.jrxml");
        setColumnHeader("sales/order.jrxml");

        addParameters();


    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        addParameter("title.date"     , iBundle.getString("orderreport.title.date") );
        addParameter("title.number"   , iBundle.getString("orderreport.title.number") );

        return iBundle.getString("orderreport.title");
    }



    /**
     *
     */
    private void addParameters(){
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        SSSalePrinterUtils.addParametersForCompany(iCompany, this);

        // Sale parameters
        addParameter("number"   , iOrder.getNumber() );
        addParameter("date"     , iOrder.getDate() );
        addParameter("text"     , iOrder.getText() );

        addParameter("order.hasdiscount"   , SSSaleMath.hasDiscount(iOrder));

        addParameter("order.deliveryadress.name"    , iOrder.getDeliveryAddress().getName() );
        addParameter("order.deliveryadress.address1", iOrder.getDeliveryAddress().getAddress1() );
        addParameter("order.deliveryadress.address2", iOrder.getDeliveryAddress().getAddress2() );
        addParameter("order.deliveryadress.zipcode" , iOrder.getDeliveryAddress().getZipCode() );
        addParameter("order.deliveryadress.city"    , iOrder.getDeliveryAddress().getCity() );
        addParameter("order.deliveryadress.country" , iOrder.getDeliveryAddress().getCountry() );

        addParameter("order.invoiceadress.name"    , iOrder.getInvoiceAddress().getName() );
        addParameter("order.invoiceadress.address1", iOrder.getInvoiceAddress().getAddress1() );
        addParameter("order.invoiceadress.address2", iOrder.getInvoiceAddress().getAddress2() );
        addParameter("order.invoiceadress.zipcode" , iOrder.getInvoiceAddress().getZipCode() );
        addParameter("order.invoiceadress.city"    , iOrder.getInvoiceAddress().getCity() );
        addParameter("order.invoiceadress.country" , iOrder.getInvoiceAddress().getCountry() );


        addParameter("order.ourcontact"               , iOrder.getOurContactPerson() );
        addParameter("order.deliveryterm"             , iOrder.getDeliveryTerm() , true);
        addParameter("order.deliveryway"              , iOrder.getDeliveryWay () , true);
        addParameter("order.paymentterm"              , iOrder.getPaymentTerm () , true);
        addParameter("order.delayinterest"            , iOrder.getDelayInterest () , true);
        addParameter("order.currency"                 , iOrder.getCurrency() , true);
        addParameter("order.estimateddelivery"        , iOrder.getEstimatedDelivery () , true);

        addParameter("order.customernr"               , iOrder.getCustomerNr() );
        addParameter("order.yourcontact"              , iOrder.getYourContactPerson() );
        addParameter("order.yourordernumber"          , iOrder.getYourOrderNumber() );

        addParameter("order.taxrate1"               , iOrder.getTaxRate1().toString() );
        addParameter("order.taxrate2"               , iOrder.getTaxRate2().toString() );
        addParameter("order.taxrate3"               , iOrder.getTaxRate3().toString() );


        // Calculate the sum fields...
        BigDecimal                 iNetSum   = SSInvoiceMath.getNetSum    (iOrder);
        Map<SSTaxCode, BigDecimal> iTaxSum   = SSInvoiceMath.getTaxSum    (iOrder);
        BigDecimal                 iTotalSum = SSInvoiceMath.getTotalSum  (iOrder);
        BigDecimal                 iRounding = SSInvoiceMath.getRounding  (iOrder);

        // ... add them to the report
        addParameter("order.netsum"            , iNetSum );
        addParameter("order.taxsum1"           , iTaxSum.get(SSTaxCode.TAXRATE_1) );
        addParameter("order.taxsum2"           , iTaxSum.get(SSTaxCode.TAXRATE_2) );
        addParameter("order.taxsum3"           , iTaxSum.get(SSTaxCode.TAXRATE_3) );
        addParameter("order.rounding"          , iRounding );
        addParameter("order.totalsum"          , iTotalSum );
    }


    /**
     *
     * @return
     */
    @Override
    protected SSDefaultTableModel getModel() {
        final SSPrinter iPrinter = new SSOrderPrinter.SSRowReport(  );

        iPrinter.setBundle(iBundle);
        iPrinter.setLocale(iLocale);
        iPrinter.generateReport();

        addParameter("subreport.report"      , iPrinter.getReport());
        addParameter("subreport.parameters"  , iPrinter.getParameters() );
        addParameter("subreport.datasource"  , iPrinter.getDataSource() );

        SSDefaultTableModel<SSOrder> iModel = new SSDefaultTableModel<SSOrder>(  ) {

            @Override
            public Class getType() {
                return SSOrder.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                iPrinter.getDataSource().reset();

                return iPrinter.getDataSource();
            }
        };

        iModel.addColumn("subreport.datasource");

        iModel.setObjects(iOrder);

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
            super();
            setMargins(0,0,0,0);

            setColumnHeader  ("sales/order.rows.jrxml");
            setDetail        ("sales/order.rows.jrxml");
            setPageFooter    ("sales/order.rows.jrxml");
            setLastPageFooter("sales/order.rows.jrxml");
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

            iModel.setObjects( iOrder.getRows() );

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



}
