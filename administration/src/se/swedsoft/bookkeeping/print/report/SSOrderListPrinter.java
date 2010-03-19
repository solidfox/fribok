package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.calc.math.SSOrderMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSOrder;
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
public class SSOrderListPrinter extends SSPrinter {

    private SSOrderRowPrinter iPrinter;


    private SSDefaultJasperDataSource iDataSource;

    private List<SSOrder> iOrders;

    /**
     *
     */
    public SSOrderListPrinter() {
        this(SSDB.getInstance().getOrders());
    }

    /**
     *
     * @param iOrders
     */
    public SSOrderListPrinter( List<SSOrder> iOrders){
        // Get all orders
        this.iOrders = iOrders;

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("orderlist.jrxml");
        setDetail      ("orderlist.jrxml");
        setSummary     ("orderlist.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("orderlistreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {

        iPrinter = new SSOrderRowPrinter();
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

        SSDefaultTableModel<SSOrder> iModel = new SSDefaultTableModel<SSOrder>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            @Override
            public Class getType() {
                return SSAccount.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSOrder iOrder = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iOrder.getNumber();
                        break;
                    case 1:
                        value = iOrder.getCustomerNr();
                        break;
                    case 2:
                        value = iOrder.getCustomerName();
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
                        iSum = iSum.add(SSOrderMath.getNetSum(iOrder).multiply(iOrder.getCurrencyRate()));
                        value = iSum;
                        break;
                }

                return value;
            }
        };

        iModel.addColumn("order.number");
        iModel.addColumn("order.customernr");
        iModel.addColumn("order.customername");
        iModel.addColumn("order.date");
        iModel.addColumn("order.currency");
        iModel.addColumn("order.rows");
        iModel.addColumn("order.sum");


        Collections.sort(iOrders, new Comparator<SSOrder>() {
            public int compare(SSOrder o1, SSOrder o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });

        iModel.setObjects(iOrders);


        return iModel;
    }





    private class SSOrderRowPrinter extends SSPrinter {

        private SSDefaultTableModel<SSSaleRow> iModel;



        /**
         *
         */
        public SSOrderRowPrinter( ){
            setMargins(0,0,0,0);

            setDetail ("orderlist.row.jrxml");
            setSummary("orderlist.row.jrxml");


            iModel = new SSDefaultTableModel<SSSaleRow>(  ) {

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
                            value = iRow.getDescription();
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
        public void setOrder(SSOrder iOrder) {
            iModel.setObjects( iOrder.getRows() );
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("se.swedsoft.bookkeeping.print.report.SSOrderListPrinter.SSOrderRowPrinter");
            sb.append("{iModel=").append(iModel);
            sb.append('}');
            return sb.toString();
        }
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.print.report.SSOrderListPrinter");
        sb.append("{iDataSource=").append(iDataSource);
        sb.append(", iOrders=").append(iOrders);
        sb.append(", iPrinter=").append(iPrinter);
        sb.append('}');
        return sb.toString();
    }
}
