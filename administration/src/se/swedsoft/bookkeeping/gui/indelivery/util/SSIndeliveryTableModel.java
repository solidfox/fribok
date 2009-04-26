package se.swedsoft.bookkeeping.gui.indelivery.util;

import se.swedsoft.bookkeeping.data.SSIndelivery;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.calc.math.SSIndeliveryMath;

import java.util.List;
import java.util.Date;

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
     */
    public SSIndeliveryTableModel(List<SSIndelivery> iIndeliveries) {
        super(iIndeliveries);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSIndelivery.class;
    }



    /**
     *  Inventerings nummer
     */
    public static SSTableColumn<SSIndelivery> COLUMN_NUMBER = new SSTableColumn<SSIndelivery>(SSBundle.getBundle().getString("indeliverytable.column.1")) {
        public Object getValue(SSIndelivery iIndelivery) {
            return iIndelivery.getNumber();
        }

        public void setValue(SSIndelivery iInvoice, Object iValue) {
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
    public static SSTableColumn<SSIndelivery> COLUMN_DATE = new SSTableColumn<SSIndelivery>(SSBundle.getBundle().getString("indeliverytable.column.2")) {
        public Object getValue(SSIndelivery iIndelivery) {
            return iIndelivery.getDate();
        }

        public void setValue(SSIndelivery iIndelivery, Object iValue) {
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
    public static SSTableColumn<SSIndelivery> COLUMN_TEXT = new SSTableColumn<SSIndelivery>(SSBundle.getBundle().getString("indeliverytable.column.3")) {
        public Object getValue(SSIndelivery iIndelivery) {
            return iIndelivery.getText();
        }

        public void setValue(SSIndelivery iIndelivery, Object iValue) {
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
    public static SSTableColumn<SSIndelivery> COLUMN_TOTALCOUNT = new SSTableColumn<SSIndelivery>(SSBundle.getBundle().getString("indeliverytable.column.4")) {
        public Object getValue(SSIndelivery iIndelivery) {
            return SSIndeliveryMath.getTotalCount(iIndelivery);
        }

        public void setValue(SSIndelivery iIndelivery, Object iValue) {
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
