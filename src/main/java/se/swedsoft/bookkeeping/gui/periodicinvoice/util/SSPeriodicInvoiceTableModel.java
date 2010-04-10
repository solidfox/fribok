package se.swedsoft.bookkeeping.gui.periodicinvoice.util;


import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.SSPeriodicInvoice;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;


/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSPeriodicInvoiceTableModel extends SSTableModel<SSPeriodicInvoice> {

    /**
     * Default constructor.
     */
    public SSPeriodicInvoiceTableModel() {
        super(SSDB.getInstance().getPeriodicInvoices());
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSPeriodicInvoice.class;
    }

    /**
     * Number column
     */
    public static SSTableColumn<SSPeriodicInvoice> COLUMN_NUMBER = new SSTableColumn<SSPeriodicInvoice>(
            SSBundle.getBundle().getString("periodicinvoicetable.column.1")) {
        @Override
        public Object getValue(SSPeriodicInvoice iObject) {
            return iObject.getNumber();
        }

        @Override
        public void setValue(SSPeriodicInvoice iObject, Object iValue) {
            iObject.setNumber((Integer) iValue);

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
     * Description column
     */
    public static SSTableColumn<SSPeriodicInvoice> COLUMN_DESCRIPTION = new SSTableColumn<SSPeriodicInvoice>(
            SSBundle.getBundle().getString("periodicinvoicetable.column.2")) {
        @Override
        public Object getValue(SSPeriodicInvoice iObject) {
            return iObject.getDescription();
        }

        @Override
        public void setValue(SSPeriodicInvoice iObject, Object iValue) {
            iObject.setDescription((String) iValue);

        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 250;
        }
    };

    /**
     * Date column
     */
    public static SSTableColumn<SSPeriodicInvoice> COLUMN_DATE = new SSTableColumn<SSPeriodicInvoice>(
            SSBundle.getBundle().getString("periodicinvoicetable.column.3")) {
        @Override
        public Object getValue(SSPeriodicInvoice iObject) {
            return iObject.getDate();
        }

        @Override
        public void setValue(SSPeriodicInvoice iObject, Object iValue) {
            iObject.setDate((Date) iValue);

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
    public static SSTableColumn<SSPeriodicInvoice> COLUMN_NEXT = new SSTableColumn<SSPeriodicInvoice>(
            SSBundle.getBundle().getString("periodicinvoicetable.column.4")) {
        @Override
        public Object getValue(SSPeriodicInvoice iObject) {
            Date iNext = iObject.getNextDate();

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            return iNext != null
                    ? iFormat.format(iNext)
                    : SSBundle.getBundle().getString("periodicinvoiceframe.concluded");
        }

        @Override
        public void setValue(SSPeriodicInvoice iObject, Object iValue) {}

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     *  Kund nummer
     */
    public static SSTableColumn<SSPeriodicInvoice> COLUMN_CUSTOMER_NR = new SSTableColumn<SSPeriodicInvoice>(
            SSBundle.getBundle().getString("invoicetable.column.3")) {
        @Override
        public Object getValue(SSPeriodicInvoice iInvoice) {
            return iInvoice.getTemplate().getCustomerNr();
        }

        @Override
        public void setValue(SSPeriodicInvoice iInvoice, Object iValue) {}

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     *  Kund namn
     */
    public static SSTableColumn<SSPeriodicInvoice> COLUMN_CUSTOMER_NAME = new SSTableColumn<SSPeriodicInvoice>(
            SSBundle.getBundle().getString("invoicetable.column.4")) {
        @Override
        public Object getValue(SSPeriodicInvoice iInvoice) {
            return iInvoice.getTemplate().getCustomerName();
        }

        @Override
        public void setValue(SSPeriodicInvoice iInvoice, Object iValue) {}

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 150;
        }
    };

    /**
     * Total summa
     */
    public static SSTableColumn<SSPeriodicInvoice> COLUMN_TOTAL_SUM = new SSTableColumn<SSPeriodicInvoice>(
            SSBundle.getBundle().getString("invoicetable.column.10")) {
        @Override
        public Object getValue(SSPeriodicInvoice iInvoice) {
            return SSInvoiceMath.getTotalSum(iInvoice.getTemplate());
        }

        @Override
        public void setValue(SSPeriodicInvoice iInvoice, Object iValue) {}

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 90;
        }
    };

    /**
     * Valuta
     */
    public static SSTableColumn<SSPeriodicInvoice> COLUMN_CURRENCY = new SSTableColumn<SSPeriodicInvoice>(
            SSBundle.getBundle().getString("invoicetable.column.8")) {
        @Override
        public Object getValue(SSPeriodicInvoice iInvoice) {
            return iInvoice.getTemplate().getCurrency();
        }

        @Override
        public void setValue(SSPeriodicInvoice iInvoice, Object iValue) {}

        @Override
        public Class getColumnClass() {
            return SSCurrency.class;
        }

        @Override
        public int getDefaultWidth() {
            return 50;
        }
    };

}
