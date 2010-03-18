package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSPurchaseOrder;
import se.swedsoft.bookkeeping.data.SSPurchaseOrderRow;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSPurchaseOrderListPrinter extends SSPrinter {

    private SSPurchaseOrderListPrinter.SSOrderRowPrinter iPrinter;


    private SSDefaultJasperDataSource iDataSource;

    private List<SSPurchaseOrder> iOrders;

    /**
     *
     */
    public SSPurchaseOrderListPrinter() {
        this(SSDB.getInstance().getPurchaseOrders());
    }

    /**
     *
     * @param iOrders
     */
    public SSPurchaseOrderListPrinter( List<SSPurchaseOrder> iOrders){
        // Get all orders
        this.iOrders = iOrders;

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("purchaseorderlist.jrxml");
        setDetail      ("purchaseorderlist.jrxml");
        setSummary     ("purchaseorderlist.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("purchaseorderlistreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {

        iPrinter = new SSPurchaseOrderListPrinter.SSOrderRowPrinter();
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

        SSDefaultTableModel<SSPurchaseOrder> iModel = new SSDefaultTableModel<SSPurchaseOrder>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            @Override
            public Class getType() {
                return SSAccount.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSPurchaseOrder iOrder = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iOrder.getNumber();
                        break;
                    case 1:
                        value = iOrder.getSupplierNr();
                        break;
                    case 2:
                        value = iOrder.getSupplierName();
                        break;
                    case 3:
                        value = iFormat.format(iOrder.getDate());
                        break;
                    case 4:
                        value = iOrder.getCurrency() == null ? null : iOrder.getCurrency().getName();
                        break;
                    case 5:
                        iPrinter.setOrder(iOrder);

                        iDataSource.reset();

                        value = iDataSource;
                        break;
                    case 6:
                        BigDecimal iSum = new BigDecimal(0);
                        iSum = iSum.add(iOrder.getSum().multiply(iOrder.getCurrencyRate()));
                        value = iSum;
                        break;
                }

                return value;
            }
        };

        iModel.addColumn("order.number");
        iModel.addColumn("order.suppliernr");
        iModel.addColumn("order.suppliername");
        iModel.addColumn("order.date");
        iModel.addColumn("order.currency");
        iModel.addColumn("order.rows");
        iModel.addColumn("order.sum");


        Collections.sort(iOrders, new Comparator<SSPurchaseOrder>() {
            public int compare(SSPurchaseOrder o1, SSPurchaseOrder o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });

        iModel.setObjects(iOrders);


        return iModel;
    }





    private class SSOrderRowPrinter extends SSPrinter {

        private SSDefaultTableModel<SSPurchaseOrderRow> iModel;

        /**
         *
         */
        public SSOrderRowPrinter( ){
            setMargins(0,0,0,0);

            setDetail ("purchaseorderlist.row.jrxml");
            setSummary("purchaseorderlist.row.jrxml");


            iModel = new SSDefaultTableModel<SSPurchaseOrderRow>(  ) {

                @Override
                public Class getType() {
                    return SSSaleRow.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    SSPurchaseOrderRow iRow = getObject(rowIndex);

                    switch (columnIndex) {
                        case 0  :
                            value = iRow.getProductNr();
                            break;
                        case 1:
                            value = iRow.getDescription();
                            break;
                        case 2:
                            value = iRow.getQuantity();
                            break;
                        case 3:
                            value = iRow.getUnit() == null ? null : iRow.getUnit().getName();
                            break;
                        case 4:
                            value = iRow.getUnitPrice();
                            break;
                        case 5:
                            value = iRow.getSupplierArticleNr();
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
            iModel.addColumn("row.supplierartnr");
            iModel.addColumn("row.sum");
        }

        /**
         * Gets the data model for this report
         *
         * @return SSDefaultTableModel
         */
        @Override
        protected SSDefaultTableModel getModel() {
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

        /**
         *
         * @param iOrder
         */
        public void setOrder(SSPurchaseOrder iOrder) {
            iModel.setObjects( iOrder.getRows() );
        }
    }




}
