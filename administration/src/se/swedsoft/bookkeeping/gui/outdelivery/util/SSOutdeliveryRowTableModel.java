package se.swedsoft.bookkeeping.gui.outdelivery.util;

import se.swedsoft.bookkeeping.data.SSOutdeliveryRow;
import se.swedsoft.bookkeeping.data.SSInventory;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.gui.util.table.model.SSEditableTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSOutdeliveryRowTableModel extends SSEditableTableModel<SSOutdeliveryRow> {


    /**
     * Default constructor.
     */
    public SSOutdeliveryRowTableModel() {
        super();
    }


    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSInventory.class;
    }

    /**
     * @return
     */
    public SSOutdeliveryRow newObject() {
        return new SSOutdeliveryRow();
    }

    /**
     * Product
     */
    public static SSTableColumn<SSOutdeliveryRow> COLUMN_PRODUCT = new SSTableColumn<SSOutdeliveryRow>(SSBundle.getBundle().getString("outdeliveryrowtable.column.1")) {
        public Object getValue(SSOutdeliveryRow iRow) {
            return iRow.getProduct();
        }

        public void setValue(SSOutdeliveryRow iRow, Object iValue) {
            if(iValue instanceof SSProduct) iRow.setProduct((SSProduct)iValue);
        }

        public Class getColumnClass() {
            return SSProduct.class;
        }

        public int getDefaultWidth() {
            return 90;
        }
    };

    /**
     * Product beskrivning
     */
    public static SSTableColumn<SSOutdeliveryRow> COLUMN_DESCRIPTION = new SSTableColumn<SSOutdeliveryRow>(SSBundle.getBundle().getString("outdeliveryrowtable.column.2")) {
        public Object getValue(SSOutdeliveryRow iRow) {
            SSProduct iProduct = iRow.getProduct();

            return iProduct == null ? null : iProduct.getDescription();
        }

        public void setValue(SSOutdeliveryRow iRow, Object iValue) {

        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 400;
        }
    };

    /**
     * Skillnad
     */
    public static SSTableColumn<SSOutdeliveryRow> COLUMN_CHANGE = new SSTableColumn<SSOutdeliveryRow>(SSBundle.getBundle().getString("outdeliveryrowtable.column.3")) {
        public Object getValue(SSOutdeliveryRow iRow) {
            return iRow.getChange();
        }

        public void setValue(SSOutdeliveryRow iRow, Object iValue) {
            iRow.setChange((Integer)iValue);
        }

        public Class getColumnClass() {
            return Integer.class;
        }

        public int getDefaultWidth() {
            return 120;
        }
    };


}
