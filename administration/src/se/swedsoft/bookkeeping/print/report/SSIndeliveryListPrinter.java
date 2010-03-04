package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.text.DateFormat;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSIndeliveryListPrinter extends SSPrinter {

    private SSIndeliveryListPrinter.SSInventoryRowPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    private List<SSIndelivery> iIndeliveries;

    /**
     *
     */
    public SSIndeliveryListPrinter() {
        this(SSDB.getInstance().getIndeliveries() );
    }

    /**
     *
     */
    public SSIndeliveryListPrinter( List<SSIndelivery> iIndeliveries){
        super();
        // Get all orders
        this.iIndeliveries = iIndeliveries;

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("indeliverylist.jrxml");
        setDetail      ("indeliverylist.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("indeliverylistreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {

        iPrinter = new SSIndeliveryListPrinter.SSInventoryRowPrinter();
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

        SSDefaultTableModel<SSIndelivery> iModel = new SSDefaultTableModel<SSIndelivery>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            @Override
            public Class getType() {
                return SSInventory.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSIndelivery iIndelivery= getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iIndelivery.getNumber();
                        break;
                    case 1:
                        value = iFormat.format(iIndelivery.getDate());
                        break;
                    case 2:
                        value = iIndelivery.getText();
                        break;
                    case 3:
                        iPrinter.setIndelivery(iIndelivery);

                        iDataSource.reset();

                        value = iDataSource;
                        break;
                }

                return value;
            }
        };

        iModel.addColumn("indelivery.number");
        iModel.addColumn("indelivery.date");
        iModel.addColumn("indelivery.text");
        iModel.addColumn("indelivery.rows");



        Collections.sort(iIndeliveries, new Comparator<SSIndelivery>() {
            public int compare(SSIndelivery o1, SSIndelivery o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });

        iModel.setObjects(iIndeliveries);


        return iModel;
    }


    private class SSInventoryRowPrinter extends SSPrinter {

        private SSDefaultTableModel<SSIndeliveryRow> iModel;

        /**
         *
         */
        public SSInventoryRowPrinter( ){
            setMargins(0,0,0,0);

            setDetail ("indeliverylist.row.jrxml");
            setSummary("indeliverylist.row.jrxml");



            iModel = new SSDefaultTableModel<SSIndeliveryRow>(  ) {

                @Override
                public Class getType() {
                    return SSSaleRow.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    SSIndeliveryRow iRow = getObject(rowIndex);

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

            iModel.addColumn("indeliveryrow.product");
            iModel.addColumn("indeliveryrow.descripion");
            iModel.addColumn("indeliveryrow.change");
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
         * @param iIndelivery
         */
        public void setIndelivery(SSIndelivery iIndelivery) {
            iModel.setObjects( iIndelivery.getRows() );
        }
    }




}
