package se.swedsoft.bookkeeping.print.report.sales;

import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.SSOrder;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;

import java.math.BigDecimal;
import java.util.*;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSPickingslipPrinter extends SSPrinter {

    private SSOrder iOrder;

    private Locale iLocale;

    /**
     *
     */
    public SSPickingslipPrinter(SSOrder iOrder, Locale iLocale){
        super();
        this.iOrder  = iOrder;
        this.iLocale = iLocale;

        ResourceBundle iBundle = ResourceBundle.getBundle("se.swedsoft.bookkeeping.resources.pickingslipreport", iLocale);

        setBundle( iBundle );
        setLocale( iLocale );

        setMargins(0,0,0,0);

        setPageHeader  ("sales/sale.header.jrxml");
        setPageFooter  ("sales/sale.footer.jrxml");

        setDetail      ("sales/pickingslip.jrxml");
        setColumnHeader("sales/pickingslip.jrxml");

        addParameters();


    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        addParameter("title.date"     , iBundle.getString("pickingslipreport.title.date") );
        addParameter("title.number"   , iBundle.getString("pickingslipreport.title.number") );

        return iBundle.getString("pickingslipreport.title");
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
    }


    /**
     *
     * @return
     */
    @Override
    protected SSDefaultTableModel getModel() {
        final SSPrinter iPrinter = new SSPickingslipPrinter.SSRowReport(  );

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

            setColumnHeader("sales/pickingslip.rows.jrxml");
            setDetail      ("sales/pickingslip.rows.jrxml");

            setPageFooter    ("sales/pickingslip.rows.jrxml");
            setLastPageFooter("sales/pickingslip.rows.jrxml");
        }

        /**
         * Gets the data model for this report
         *
         * @return SSDefaultTableModel
         */
        @Override
        protected SSDefaultTableModel getModel() {
            final List<SSProduct> iProducts = SSDB.getInstance().getProducts();

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
                    }
                    SSProduct iProduct = iRow.getProduct(iProducts);

                    if(iProduct == null) return value;

                    switch (columnIndex) {
                        case 3:
                            value = iProduct.getWarehouseLocation();
                            break;
                        case 4:
                            value = iProduct.getUnit() == null  ? null : iProduct.getUnit().getName();
                            break;
                        case 5:
                            value = iProduct.getWeight() == null ? new BigDecimal(0.0) : iProduct.getWeight();
                            break;
                        case 6:
                            value = iProduct.getVolume() == null ? new BigDecimal(0.0) : iProduct.getVolume();
                            break;
                    }

                    return value;
                }
            };

            iModel.addColumn("product.number");
            iModel.addColumn("product.description");
            iModel.addColumn("product.count");
            iModel.addColumn("product.warehouselocation");
            iModel.addColumn("product.unit");
            iModel.addColumn("product.weight");
            iModel.addColumn("product.volume");

            List<SSSaleRow> iRows = new LinkedList<SSSaleRow>(iOrder.getRows());
            Collections.sort(iRows, new Comparator(){
                public int compare(Object iRow1, Object iRow2){
                    SSSaleRow iSaleRow1 = (SSSaleRow) iRow1;
                    SSSaleRow iSaleRow2 = (SSSaleRow) iRow2;

                    SSProduct iProduct1 = SSDB.getInstance().getProduct(iSaleRow1.getProductNr());
                    SSProduct iProduct2 = SSDB.getInstance().getProduct(iSaleRow2.getProductNr());

                    if(iProduct1 != null && iProduct2 != null){
                        if(iProduct1.getWarehouseLocation() == null && iProduct2.getWarehouseLocation() == null) return 0;
                        else if(iProduct1.getWarehouseLocation() != null && iProduct2.getWarehouseLocation() == null) return 1;
                        else if(iProduct1.getWarehouseLocation() == null && iProduct2.getWarehouseLocation() != null) return -1;
                        else return iProduct1.getWarehouseLocation().compareTo(iProduct2.getWarehouseLocation());
                    }
                    else if(iProduct1 == null && iProduct2 == null){
                        return 0;
                    }
                    else if(iProduct1 != null){
                        return -1;
                    }
                    else{
                        return 1;
                    }
                }
            });
            iModel.setObjects( iRows );

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
