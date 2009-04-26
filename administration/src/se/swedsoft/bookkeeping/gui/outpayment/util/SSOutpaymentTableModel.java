package se.swedsoft.bookkeeping.gui.outpayment.util;

import se.swedsoft.bookkeeping.data.SSOutpaymentRow;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.SSOutpayment;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
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
    public Class getType() {
        return SSOutpayment.class;
    }


    /**
     * Number column
     */
    public static SSTableColumn<SSOutpayment> COLUMN_NUMBER = new SSTableColumn<SSOutpayment>(SSBundle.getBundle().getString("outpaymenttable.column.1")) {
        public Object getValue(SSOutpayment iObject) {
            return iObject.getNumber();
        }

        public void setValue(SSOutpayment iObject, Object iValue) {
            iObject.setNumber((Integer)iValue);

        }

        public Class getColumnClass() {
            return Integer.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Date column
     */
    public static SSTableColumn<SSOutpayment> COLUMN_DATE = new SSTableColumn<SSOutpayment>(SSBundle.getBundle().getString("outpaymenttable.column.2")) {
        public Object getValue(SSOutpayment iObject) {
            return iObject.getDate();
        }

        public void setValue(SSOutpayment iObject, Object iValue) {
            iObject.setDate((Date)iValue);

        }

        public Class getColumnClass() {
            return Date.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Date column
     */
    public static SSTableColumn<SSOutpayment> COLUMN_TEXT = new SSTableColumn<SSOutpayment>(SSBundle.getBundle().getString("outpaymenttable.column.3")) {
        public Object getValue(SSOutpayment iObject) {
            return iObject.getText();
        }

        public void setValue(SSOutpayment iObject, Object iValue) {
            iObject.setText((String)iValue);

        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 450;
        }
    };

    /**
     * Sum column
     */
    public static SSTableColumn<SSOutpayment> COLUMN_SUM = new SSTableColumn<SSOutpayment>(SSBundle.getBundle().getString("outpaymenttable.column.4")) {
        public Object getValue(SSOutpayment iObject) {
            return SSOutpaymentMath.getSum(iObject);
        }

        public void setValue(SSOutpayment iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 120;
        }
    };


}
