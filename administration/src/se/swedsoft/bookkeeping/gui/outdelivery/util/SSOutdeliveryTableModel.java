package se.swedsoft.bookkeeping.gui.outdelivery.util;

import se.swedsoft.bookkeeping.data.SSOutdelivery;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.calc.math.SSOutdeliveryMath;

import java.util.List;
import java.util.Date;

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
     */
    public SSOutdeliveryTableModel(List<SSOutdelivery> iIndeliveries) {
        super(iIndeliveries);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSOutdelivery.class;
    }



    /**
     *  Inventerings nummer
     */
    public static SSTableColumn<SSOutdelivery> COLUMN_NUMBER = new SSTableColumn<SSOutdelivery>(SSBundle.getBundle().getString("outdeliverytable.column.1")) {
        public Object getValue(SSOutdelivery iIndelivery) {
            return iIndelivery.getNumber();
        }

        public void setValue(SSOutdelivery iInvoice, Object iValue) {
            iInvoice.setNumber((Integer)iValue);
        }

        public Class getColumnClass() {
            return Integer.class;
        }

        public int getDefaultWidth() {
            return 70;
        }
    };

    /**
     * Datum
     */
    public static SSTableColumn<SSOutdelivery> COLUMN_DATE = new SSTableColumn<SSOutdelivery>(SSBundle.getBundle().getString("outdeliverytable.column.2")) {
        public Object getValue(SSOutdelivery iIndelivery) {
            return iIndelivery.getDate();
        }

        public void setValue(SSOutdelivery iIndelivery, Object iValue) {
            iIndelivery.setDate((Date)iValue);
        }

        public Class getColumnClass() {
            return Date.class;
        }

        public int getDefaultWidth() {
            return 90;
        }
    };

    /**
     * Text
     */
    public static SSTableColumn<SSOutdelivery> COLUMN_TEXT = new SSTableColumn<SSOutdelivery>(SSBundle.getBundle().getString("outdeliverytable.column.3")) {
        public Object getValue(SSOutdelivery iIndelivery) {
            return iIndelivery.getText();
        }

        public void setValue(SSOutdelivery iIndelivery, Object iValue) {
            iIndelivery.setText((String)iValue);
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 400;
        }
    };



    /**
     * Totalt antal
     */
    public static SSTableColumn<SSOutdelivery> COLUMN_TOTALCOUNT = new SSTableColumn<SSOutdelivery>(SSBundle.getBundle().getString("outdeliverytable.column.4")) {
        public Object getValue(SSOutdelivery iIndelivery) {
            return SSOutdeliveryMath.getTotalCount(iIndelivery);
        }

        public void setValue(SSOutdelivery iIndelivery, Object iValue) {
            iIndelivery.setText((String)iValue);
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 120;
        }
    };










}
