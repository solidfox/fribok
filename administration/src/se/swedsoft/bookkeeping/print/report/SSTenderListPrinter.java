package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.calc.math.SSTenderMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSTender;
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
public class SSTenderListPrinter extends SSPrinter {

    private SSTenderRowPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    private List<SSTender> iTenders;

    /**
     *
     */
    public SSTenderListPrinter() {
        this(SSDB.getInstance().getTenders());
    }

    /**
     *
     * @param iTenders
     */
    public SSTenderListPrinter( List<SSTender> iTenders){
        super();
        // Get all orders
        this.iTenders = iTenders;

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("tenderlist.jrxml");
        setDetail      ("tenderlist.jrxml");
        setSummary     ("tenderlist.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("tenderlistreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {

        iPrinter = new SSTenderRowPrinter();
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

        SSDefaultTableModel<SSTender> iModel = new SSDefaultTableModel<SSTender>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            @Override
            public Class getType() {
                return SSAccount.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSTender iTender = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iTender.getNumber();
                        break;
                    case 1:
                        value = iTender.getCustomerNr();
                        break;
                    case 2:
                        value = iTender.getCustomerName();
                        break;
                    case 3:
                        value = iFormat.format(iTender.getDate());
                        break;
                    case 4:
                        value = iTender.getCurrency() == null ? null : iTender.getCurrency().getName();
                        break;
                    case 5:
                        iPrinter.setTender(iTender);

                        iDataSource.reset();

                        value = iDataSource;
                        break;
                    case 6:
                        BigDecimal iSum = new BigDecimal(0);
                        iSum = iSum.add(SSTenderMath.getNetSum(iTender).multiply(iTender.getCurrencyRate()));
                        value = iSum;
                        break;
                }

                return value;
            }
        };

        iModel.addColumn("tender.number");
        iModel.addColumn("tender.customernr");
        iModel.addColumn("tender.customername");
        iModel.addColumn("tender.date");
        iModel.addColumn("tender.currency");
        iModel.addColumn("tender.rows");
        iModel.addColumn("tender.sum");


        Collections.sort(iTenders, new Comparator<SSTender>() {
            public int compare(SSTender o1, SSTender o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });

        iModel.setObjects(iTenders);


        return iModel;
    }


    private class SSTenderRowPrinter extends SSPrinter {

        private SSDefaultTableModel<SSSaleRow> iModel;

        /**
         *
         */
        public SSTenderRowPrinter( ){
            setMargins(0,0,0,0);

            setDetail ("tenderlist.row.jrxml");
            setSummary("tenderlist.row.jrxml");


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
         * @param iTender
         */
        public void setTender(SSTender iTender) {
            iModel.setObjects( iTender.getRows() );
        }
    }




}
