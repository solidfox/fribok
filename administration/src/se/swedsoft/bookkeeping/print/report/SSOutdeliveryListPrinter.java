package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.data.SSInventory;
import se.swedsoft.bookkeeping.data.SSOutdelivery;
import se.swedsoft.bookkeeping.data.SSOutdeliveryRow;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSOutdeliveryListPrinter extends SSPrinter {

    private SSOutdeliveryListPrinter.SSInventoryRowPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    private List<SSOutdelivery> iOutdeliveries;

    /**
     *
     */
    public SSOutdeliveryListPrinter() {
        this(SSDB.getInstance().getOutdeliveries() );
    }

    /**
     *
     */
    public SSOutdeliveryListPrinter( List<SSOutdelivery> iOutdeliveries){
        super();
        // Get all orders
        this.iOutdeliveries = iOutdeliveries;

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("outdeliverylist.jrxml");
        setDetail      ("outdeliverylist.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("outdeliverylistreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {

        iPrinter = new SSOutdeliveryListPrinter.SSInventoryRowPrinter();
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

        SSDefaultTableModel<SSOutdelivery> iModel = new SSDefaultTableModel<SSOutdelivery>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            @Override
            public Class getType() {
                return SSInventory.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSOutdelivery iOutdelivery = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iOutdelivery.getNumber();
                        break;
                    case 1:
                        value = iFormat.format(iOutdelivery.getDate());
                        break;
                    case 2:
                        value = iOutdelivery.getText();
                        break;
                    case 3:
                        iPrinter.setOutdelivery(iOutdelivery);

                        iDataSource.reset();

                        value = iDataSource;
                        break;
                }

                return value;
            }
        };

        iModel.addColumn("outdelivery.number");
        iModel.addColumn("outdelivery.date");
        iModel.addColumn("outdelivery.text");
        iModel.addColumn("outdelivery.rows");



        Collections.sort(iOutdeliveries, new Comparator<SSOutdelivery>() {
            public int compare(SSOutdelivery o1, SSOutdelivery o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });

        iModel.setObjects(iOutdeliveries);


        return iModel;
    }


    private class SSInventoryRowPrinter extends SSPrinter {

        private SSDefaultTableModel<SSOutdeliveryRow> iModel;

        /**
         *
         */
        public SSInventoryRowPrinter( ){
            setMargins(0,0,0,0);

            setDetail ("outdeliverylist.row.jrxml");
            setSummary("outdeliverylist.row.jrxml");



            iModel = new SSDefaultTableModel<SSOutdeliveryRow>(  ) {

                @Override
                public Class getType() {
                    return SSSaleRow.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    SSOutdeliveryRow iRow = getObject(rowIndex);

                    SSProduct iProduct = iRow.getProduct();

                    switch (columnIndex) {
                        case 0  :
                            value = iRow.getProductNr();
                            break;
                        case 1:
                            value = iProduct == null ? null : iProduct.getDescription();
                            break;
                        case 2:
                            value = iRow.getChange();
                            break;

                    }

                    return value;
                }
            };

            iModel.addColumn("outdeliveryrow.product");
            iModel.addColumn("outdeliveryrow.descripion");
            iModel.addColumn("outdeliveryrow.change");
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
         * @param iOutdelivery
         */
        public void setOutdelivery(SSOutdelivery iOutdelivery ) {
            iModel.setObjects( iOutdelivery.getRows() );
        }
    }




}
