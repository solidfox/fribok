package se.swedsoft.bookkeeping.gui.indelivery.util;


import se.swedsoft.bookkeeping.calc.math.SSIndeliveryMath;
import se.swedsoft.bookkeeping.data.SSIndelivery;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import java.util.Date;
import java.util.List;


/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSIndeliveryTableModel extends SSTableModel<SSIndelivery> {

    /**
     * Default constructor.
     */
    public SSIndeliveryTableModel() {
        super(SSDB.getInstance().getIndeliveries());
    }

    /**
     * Default constructor.
     * @param iIndeliveries
     */
    public SSIndeliveryTableModel(List<SSIndelivery> iIndeliveries) {
        super(iIndeliveries);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSIndelivery.class;
    }

    /**
     *  Inventerings nummer
     */
    public static SSTableColumn<SSIndelivery> COLUMN_NUMBER = new SSTableColumn<SSIndelivery>(
            SSBundle.getBundle().getString("indeliverytable.column.1")) {
        @Override
        public Object getValue(SSIndelivery iIndelivery) {
            return iIndelivery.getNumber();
        }

        @Override
        public void setValue(SSIndelivery iInvoice, Object iValue) {
            iInvoice.setNumber((Integer) iValue);
        }

        @Override
        public Class getColumnClass() {
            return Integer.class;
        }

        @Override
        public int getDefaultWidth() {
            return 70;
        }
    };

    /**
     * Datum
     */
    public static SSTableColumn<SSIndelivery> COLUMN_DATE = new SSTableColumn<SSIndelivery>(
            SSBundle.getBundle().getString("indeliverytable.column.2")) {
        @Override
        public Object getValue(SSIndelivery iIndelivery) {
            return iIndelivery.getDate();
        }

        @Override
        public void setValue(SSIndelivery iIndelivery, Object iValue) {
            iIndelivery.setDate((Date) iValue);
        }

        @Override
        public Class getColumnClass() {
            return Date.class;
        }

        @Override
        public int getDefaultWidth() {
            return 90;
        }
    };

    /**
     * Text
     */
    public static SSTableColumn<SSIndelivery> COLUMN_TEXT = new SSTableColumn<SSIndelivery>(
            SSBundle.getBundle().getString("indeliverytable.column.3")) {
        @Override
        public Object getValue(SSIndelivery iIndelivery) {
            return iIndelivery.getText();
        }

        @Override
        public void setValue(SSIndelivery iIndelivery, Object iValue) {
            iIndelivery.setText((String) iValue);
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
     * Totalt antal
     */
    public static SSTableColumn<SSIndelivery> COLUMN_TOTALCOUNT = new SSTableColumn<SSIndelivery>(
            SSBundle.getBundle().getString("indeliverytable.column.4")) {
        @Override
        public Object getValue(SSIndelivery iIndelivery) {
            return SSIndeliveryMath.getTotalCount(iIndelivery);
        }

        @Override
        public void setValue(SSIndelivery iIndelivery, Object iValue) {
            iIndelivery.setText((String) iValue);
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 120;
        }
    };

}
