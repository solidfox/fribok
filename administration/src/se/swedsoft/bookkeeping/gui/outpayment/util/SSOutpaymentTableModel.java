package se.swedsoft.bookkeeping.gui.outpayment.util;

import se.swedsoft.bookkeeping.data.SSOutpayment;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.calc.math.SSOutpaymentMath;

import java.math.BigDecimal;
import java.util.Date;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSOutpaymentTableModel extends SSTableModel<SSOutpayment> {


    /**
     * Default constructor.
     */
    public SSOutpaymentTableModel() {
        super(SSDB.getInstance().getOutpayments());
    }


    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSOutpayment.class;
    }


    /**
     * Number column
     */
    public static SSTableColumn<SSOutpayment> COLUMN_NUMBER = new SSTableColumn<SSOutpayment>(SSBundle.getBundle().getString("outpaymenttable.column.1")) {
        @Override
        public Object getValue(SSOutpayment iObject) {
            return iObject.getNumber();
        }

        @Override
        public void setValue(SSOutpayment iObject, Object iValue) {
            iObject.setNumber((Integer)iValue);

        }

        @Override
        public Class getColumnClass() {
            return Integer.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Date column
     */
    public static SSTableColumn<SSOutpayment> COLUMN_DATE = new SSTableColumn<SSOutpayment>(SSBundle.getBundle().getString("outpaymenttable.column.2")) {
        @Override
        public Object getValue(SSOutpayment iObject) {
            return iObject.getDate();
        }

        @Override
        public void setValue(SSOutpayment iObject, Object iValue) {
            iObject.setDate((Date)iValue);

        }

        @Override
        public Class getColumnClass() {
            return Date.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Date column
     */
    public static SSTableColumn<SSOutpayment> COLUMN_TEXT = new SSTableColumn<SSOutpayment>(SSBundle.getBundle().getString("outpaymenttable.column.3")) {
        @Override
        public Object getValue(SSOutpayment iObject) {
            return iObject.getText();
        }

        @Override
        public void setValue(SSOutpayment iObject, Object iValue) {
            iObject.setText((String)iValue);

        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 450;
        }
    };

    /**
     * Sum column
     */
    public static SSTableColumn<SSOutpayment> COLUMN_SUM = new SSTableColumn<SSOutpayment>(SSBundle.getBundle().getString("outpaymenttable.column.4")) {
        @Override
        public Object getValue(SSOutpayment iObject) {
            return SSOutpaymentMath.getSum(iObject);
        }

        @Override
        public void setValue(SSOutpayment iObject, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 120;
        }
    };


}
