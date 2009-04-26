package se.swedsoft.bookkeeping.gui.indelivery.util;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.gui.util.table.model.SSEditableTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSIndeliveryRowTableModel extends SSEditableTableModel<SSIndeliveryRow> {


    /**
     * Default constructor.
     */
    public SSIndeliveryRowTableModel() {
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
    public SSIndeliveryRow newObject() {
        return new SSIndeliveryRow();
    }

    /**
     * Product
     */
    public static SSTableColumn<SSIndeliveryRow> COLUMN_PRODUCT = new SSTableColumn<SSIndeliveryRow>(SSBundle.getBundle().getString("indeliveryrowtable.column.1")) {
        public Object getValue(SSIndeliveryRow iRow) {
            return iRow.getProduct();
        }

        public void setValue(SSIndeliveryRow iRow, Object iValue) {
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
    public static SSTableColumn<SSIndeliveryRow> COLUMN_DESCRIPTION = new SSTableColumn<SSIndeliveryRow>(SSBundle.getBundle().getString("indeliveryrowtable.column.2")) {
        public Object getValue(SSIndeliveryRow iRow) {
            SSProduct iProduct = iRow.getProduct();

            return iProduct == null ? null : iProduct.getDescription();
        }

        public void setValue(SSIndeliveryRow iRow, Object iValue) {

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
    public static SSTableColumn<SSIndeliveryRow> COLUMN_CHANGE = new SSTableColumn<SSIndeliveryRow>(SSBundle.getBundle().getString("indeliveryrowtable.column.3")) {
        public Object getValue(SSIndeliveryRow iRow) {
            return iRow.getChange();
        }

        public void setValue(SSIndeliveryRow iRow, Object iValue) {
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
