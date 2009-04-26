package se.swedsoft.bookkeeping.print.report.sales;

import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.data.SSPurchaseOrder;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.SSPurchaseOrderRow;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSInquiryPrinter extends SSPrinter {

    private SSPurchaseOrder iPurchaseOrder;

    private Locale iLocale;

    /**
     *
     */
    public SSInquiryPrinter(SSPurchaseOrder iPurchaseOrder, Locale iLocale){
        super();
        this.iPurchaseOrder = iPurchaseOrder;
        this.iLocale        = iLocale;

        ResourceBundle iBundle = ResourceBundle.getBundle("se.swedsoft.bookkeeping.resources.inquiryreport", iLocale);

        setBundle( iBundle );
        setLocale( iLocale );

        setMargins(0,0,0,0);


        setPageHeader  ("sales/sale.header.jrxml");
        setPageFooter  ("sales/sale.footer.jrxml");

        setDetail      ("sales/inquiry.jrxml");
        setColumnHeader("sales/inquiry.jrxml");
        setBackground  ("sales/inquiry.jrxml");

        addParameters();

    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    public String getTitle() {
        addParameter("title.date"     , iBundle.getString("inquiryreport.title.date") );
        addParameter("title.number"   , iBundle.getString("inquiryreport.title.number") );

        return iBundle.getString("inquiryreport.title");
    }



    /**
     *
     */
    private void addParameters(){
       SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        SSSalePrinterUtils.addParametersForCompany(iCompany, this);

        // Sale parameters
        addParameter("number"   , iPurchaseOrder.getNumber() );
        addParameter("date"     , iPurchaseOrder.getDate() );
        addParameter("text"     , iPurchaseOrder.getText() );

        addParameter("purchaseorder.deliveryadress.name"    , iPurchaseOrder.getDeliveryAddress().getName() );
        addParameter("purchaseorder.deliveryadress.address1", iPurchaseOrder.getDeliveryAddress().getAddress1() );
        addParameter("purchaseorder.deliveryadress.address2", iPurchaseOrder.getDeliveryAddress().getAddress2() );
        addParameter("purchaseorder.deliveryadress.zipcode" , iPurchaseOrder.getDeliveryAddress().getZipCode() );
        addParameter("purchaseorder.deliveryadress.city"    , iPurchaseOrder.getDeliveryAddress().getCity() );
        addParameter("purchaseorder.deliveryadress.country" , iPurchaseOrder.getDeliveryAddress().getCountry() );

        addParameter("purchaseorder.supplieraddress.name"    , iPurchaseOrder.getSupplierAddress().getName() );
        addParameter("purchaseorder.supplieraddress.address1", iPurchaseOrder.getSupplierAddress().getAddress1() );
        addParameter("purchaseorder.supplieraddress.address2", iPurchaseOrder.getSupplierAddress().getAddress2() );
        addParameter("purchaseorder.supplieraddress.zipcode" , iPurchaseOrder.getSupplierAddress().getZipCode() );
        addParameter("purchaseorder.supplieraddress.city"    , iPurchaseOrder.getSupplierAddress().getCity() );
        addParameter("purchaseorder.supplieraddress.country" , iPurchaseOrder.getSupplierAddress().getCountry() );


        addParameter("purchaseorder.ourcontact"               , iPurchaseOrder.getOurContact() );
        addParameter("purchaseorder.yourcontact"              , iPurchaseOrder.getYourContact() );
        addParameter("purchaseorder.deliveryterm"             , iPurchaseOrder.getDeliveryTerm() , true);
        addParameter("purchaseorder.deliveryway"              , iPurchaseOrder.getDeliveryWay () , true);
        addParameter("purchaseorder.paymentterm"              , iPurchaseOrder.getPaymentTerm () , true);
        addParameter("purchaseorder.currency"                 , iPurchaseOrder.getCurrency() , true);


        addParameter("purchaseorder.totalsum"            , iPurchaseOrder.getSum() );
    }


    /**
     *
     * @return
     */
    protected SSDefaultTableModel getModel() {
        final SSPrinter iPrinter = new SSInquiryPrinter.SSRowReport(  );

        iPrinter.setBundle(iBundle);
        iPrinter.setLocale(iLocale);
        iPrinter.generateReport();

        addParameter("subreport.report"      , iPrinter.getReport());
        addParameter("subreport.parameters"  , iPrinter.getParameters() );
        addParameter("subreport.datasource"  , iPrinter.getDataSource() );

        SSDefaultTableModel<SSPurchaseOrder> iModel = new SSDefaultTableModel<SSPurchaseOrder>(  ) {

            public Class getType() {
                return SSPurchaseOrder.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                iPrinter.getDataSource().reset();

                return iPrinter.getDataSource();
            }
        };

        iModel.addColumn("subreport.datasource");

        iModel.setObjects(iPurchaseOrder);

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

            setColumnHeader("sales/inquiry.rows.jrxml");
            setDetail      ("sales/inquiry.rows.jrxml");

            setPageFooter    ("sales/inquiry.rows.jrxml");
            setLastPageFooter("sales/inquiry.rows.jrxml");
        }

        /**
         * Gets the data model for this report
         *
         * @return SSDefaultTableModel
         */
        protected SSDefaultTableModel getModel() {
            SSDefaultTableModel<SSPurchaseOrderRow> iModel = new SSDefaultTableModel<SSPurchaseOrderRow>(  ) {

                public Class getType() {
                    return SSSaleRow.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    SSPurchaseOrderRow iRow = getObject(rowIndex);

                    switch (columnIndex) {
                        case 0:
                            value = iRow.getSupplierArticleNr();
                            break;
                        case 1  :
                            value = iRow.getProductNr();
                            break;
                        case 2:
                            value = iRow.getDescription(iLocale);
                            break;
                        case 3:
                            value = iRow.getQuantity();
                            break;
                        case 4:
                            value = iRow.getUnit() == null ? null : iRow.getUnit().getName();
                            break;
                        case 5:
                            value = iRow.getUnitPrice();
                            break;
                        case 6:
                            value = iRow.getSum();
                            break;
                    }

                    return value;
                }
            };

            iModel.addColumn("row.supplierarticlenr");
            iModel.addColumn("row.number");
            iModel.addColumn("row.description");
            iModel.addColumn("row.quantity");
            iModel.addColumn("row.unit");
            iModel.addColumn("row.unitprice");
            iModel.addColumn("row.sum");

            iModel.setObjects( iPurchaseOrder.getRows() );

            return iModel;
        }

        /**
         * Gets the title for this report
         *
         * @return The title
         */
        public String getTitle() {
            return null;
        }


    }
}
