package se.swedsoft.bookkeeping.gui.outdelivery.util;

import se.swedsoft.bookkeeping.data.SSInventory;
import se.swedsoft.bookkeeping.data.SSOutdeliveryRow;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSEditableTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSOutdeliveryRowTableModel extends SSEditableTableModel<SSOutdeliveryRow> {


    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSInventory.class;
    }

    /**
     * @return
     */
    @Override
    public SSOutdeliveryRow newObject() {
        return new SSOutdeliveryRow();
    }

    /**
     * Product
     */
    public static SSTableColumn<SSOutdeliveryRow> COLUMN_PRODUCT = new SSTableColumn<SSOutdeliveryRow>(SSBundle.getBundle().getString("outdeliveryrowtable.column.1")) {
        @Override
        public Object getValue(SSOutdeliveryRow iRow) {
            return iRow.getProduct();
        }

        @Override
        public void setValue(SSOutdeliveryRow iRow, Object iValue) {
            if(iValue instanceof SSProduct) iRow.setProduct((SSProduct)iValue);
        }

        @Override
        public Class getColumnClass() {
            return SSProduct.class;
        }

        @Override
        public int getDefaultWidth() {
            return 90;
        }
    };

    /**
     * Product beskrivning
     */
    public static SSTableColumn<SSOutdeliveryRow> COLUMN_DESCRIPTION = new SSTableColumn<SSOutdeliveryRow>(SSBundle.getBundle().getString("outdeliveryrowtable.column.2")) {
        @Override
        public Object getValue(SSOutdeliveryRow iRow) {
            SSProduct iProduct = iRow.getProduct();

            return iProduct == null ? null : iProduct.getDescription();
        }

        @Override
        public void setValue(SSOutdeliveryRow iRow, Object iValue) {

        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 400;
        }
    };

    /**
     * Skillnad
     */
    public static SSTableColumn<SSOutdeliveryRow> COLUMN_CHANGE = new SSTableColumn<SSOutdeliveryRow>(SSBundle.getBundle().getString("outdeliveryrowtable.column.3")) {
        @Override
        public Object getValue(SSOutdeliveryRow iRow) {
            return iRow.getChange();
        }

        @Override
        public void setValue(SSOutdeliveryRow iRow, Object iValue) {
            iRow.setChange((Integer)iValue);
        }

        @Override
        public Class getColumnClass() {
            return Integer.class;
        }

        @Override
        public int getDefaultWidth() {
            return 120;
        }
    };


}
