package se.swedsoft.bookkeeping.gui.outdelivery.util;


import se.swedsoft.bookkeeping.calc.math.SSOutdeliveryMath;
import se.swedsoft.bookkeeping.data.SSOutdelivery;
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
public class SSOutdeliveryTableModel extends SSTableModel<SSOutdelivery> {

    /**
     * Default constructor.
     */
    public SSOutdeliveryTableModel() {
        super(SSDB.getInstance().getOutdeliveries());
    }

    /**
     * Default constructor.
     * @param iIndeliveries
     */
    public SSOutdeliveryTableModel(List<SSOutdelivery> iIndeliveries) {
        super(iIndeliveries);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSOutdelivery.class;
    }

    /**
     *  Inventerings nummer
     */
    public static SSTableColumn<SSOutdelivery> COLUMN_NUMBER = new SSTableColumn<SSOutdelivery>(
            SSBundle.getBundle().getString("outdeliverytable.column.1")) {
        @Override
        public Object getValue(SSOutdelivery iIndelivery) {
            return iIndelivery.getNumber();
        }

        @Override
        public void setValue(SSOutdelivery iInvoice, Object iValue) {
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
    public static SSTableColumn<SSOutdelivery> COLUMN_DATE = new SSTableColumn<SSOutdelivery>(
            SSBundle.getBundle().getString("outdeliverytable.column.2")) {
        @Override
        public Object getValue(SSOutdelivery iIndelivery) {
            return iIndelivery.getDate();
        }

        @Override
        public void setValue(SSOutdelivery iIndelivery, Object iValue) {
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
    public static SSTableColumn<SSOutdelivery> COLUMN_TEXT = new SSTableColumn<SSOutdelivery>(
            SSBundle.getBundle().getString("outdeliverytable.column.3")) {
        @Override
        public Object getValue(SSOutdelivery iIndelivery) {
            return iIndelivery.getText();
        }

        @Override
        public void setValue(SSOutdelivery iIndelivery, Object iValue) {
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
    public static SSTableColumn<SSOutdelivery> COLUMN_TOTALCOUNT = new SSTableColumn<SSOutdelivery>(
            SSBundle.getBundle().getString("outdeliverytable.column.4")) {
        @Override
        public Object getValue(SSOutdelivery iIndelivery) {
            return SSOutdeliveryMath.getTotalCount(iIndelivery);
        }

        @Override
        public void setValue(SSOutdelivery iIndelivery, Object iValue) {
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
