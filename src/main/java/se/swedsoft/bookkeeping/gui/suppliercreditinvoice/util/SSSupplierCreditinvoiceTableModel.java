package se.swedsoft.bookkeeping.gui.suppliercreditinvoice.util;

import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.data.SSSupplierCreditInvoice;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSSupplierCreditinvoiceTableModel extends SSTableModel<SSSupplierCreditInvoice> {

    /**
     * Default constructor.
     */
    public SSSupplierCreditinvoiceTableModel() {
        super( SSDB.getInstance().getSupplierCreditInvoices() );
    }

    /**
     * Returns the type of data in this model. <P>
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSSupplierCreditInvoice.class;
    }


    /**
     * Supplier nr
     */
    public static SSTableColumn<SSSupplierCreditInvoice> COLUMN_NUMBER = new SSTableColumn<SSSupplierCreditInvoice>(SSBundle.getBundle().getString("suppliercreditinvoicetable.column.1")) {
        @Override
        public Object getValue(SSSupplierCreditInvoice iObject) {
            return iObject.getNumber();
        }

        @Override
        public void setValue(SSSupplierCreditInvoice iObject, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }
        @Override
        public int getDefaultWidth() {
            return 70;
        }
    };

    /**
     * Crediting
     */
    public static SSTableColumn<SSSupplierCreditInvoice> COLUMN_CREDITNING = new SSTableColumn<SSSupplierCreditInvoice>(SSBundle.getBundle().getString("suppliercreditinvoicetable.column.9")) {
        @Override
        public Object getValue(SSSupplierCreditInvoice iObject) {
            return iObject.getCreditingNr();
        }

        @Override
        public void setValue(SSSupplierCreditInvoice iObject, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }
        @Override
        public int getDefaultWidth() {
            return 70;
        }
    };

    /**
     * Supplier nr
     */
    public static SSTableColumn<SSSupplierCreditInvoice> COLUMN_SUPPLIER_NUMBER = new SSTableColumn<SSSupplierCreditInvoice>(SSBundle.getBundle().getString("suppliercreditinvoicetable.column.2")) {
        @Override
        public Object getValue(SSSupplierCreditInvoice iObject) {
            return iObject.getSupplierNr();
        }

        @Override
        public void setValue(SSSupplierCreditInvoice iObject, Object iValue) {
        }

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
     * Supplier name
     */
    public static SSTableColumn<SSSupplierCreditInvoice> COLUMN_SUPPLIER_NAME = new SSTableColumn<SSSupplierCreditInvoice>(SSBundle.getBundle().getString("suppliercreditinvoicetable.column.3")) {
        @Override
        public Object getValue(SSSupplierCreditInvoice iObject) {
            return iObject.getSupplierName();
        }

        @Override
        public void setValue(SSSupplierCreditInvoice iObject, Object iValue) {
        }

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
     * Supplier name
     */
    public static SSTableColumn<SSSupplierCreditInvoice> COLUMN_DATE = new SSTableColumn<SSSupplierCreditInvoice>(SSBundle.getBundle().getString("suppliercreditinvoicetable.column.4")) {
        @Override
        public Object getValue(SSSupplierCreditInvoice iObject) {
            return iObject.getDate();
        }

        @Override
        public void setValue(SSSupplierCreditInvoice iObject, Object iValue) {
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
     * Supplier name
     */
    public static SSTableColumn<SSSupplierCreditInvoice> COLUMN_DUEDATE = new SSTableColumn<SSSupplierCreditInvoice>(SSBundle.getBundle().getString("suppliercreditinvoicetable.column.5")) {
        @Override
        public Object getValue(SSSupplierCreditInvoice iObject) {
            return iObject.getDueDate();
        }

        @Override
        public void setValue(SSSupplierCreditInvoice iObject, Object iValue) {
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
     *
     */
    public static SSTableColumn<SSSupplierCreditInvoice> COLUMN_NETSUM = new SSTableColumn<SSSupplierCreditInvoice>(SSBundle.getBundle().getString("suppliercreditinvoicetable.column.6")) {
        @Override
        public Object getValue(SSSupplierCreditInvoice iObject) {
            return SSSupplierInvoiceMath.getNetSum(iObject);
        }

        @Override
        public void setValue(SSSupplierCreditInvoice iObject, Object iValue) {
        }

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
     *
     */
    public static SSTableColumn<SSSupplierCreditInvoice> COLUMN_CURRENCY = new SSTableColumn<SSSupplierCreditInvoice>(SSBundle.getBundle().getString("suppliercreditinvoicetable.column.7")) {
        @Override
        public Object getValue(SSSupplierCreditInvoice iObject) {
            return iObject.getCurrency();
        }

        @Override
        public void setValue(SSSupplierCreditInvoice iObject, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return SSCurrency.class;
        }
        @Override
        public int getDefaultWidth() {
            return 50;
        }
    };

    /**
     *
     */
    public static SSTableColumn<SSSupplierCreditInvoice> COLUMN_TOTALSUM = new SSTableColumn<SSSupplierCreditInvoice>(SSBundle.getBundle().getString("suppliercreditinvoicetable.column.8")) {
        @Override
        public Object getValue(SSSupplierCreditInvoice iObject) {
            return SSSupplierInvoiceMath.getTotalSum(iObject);
        }

        @Override
        public void setValue(SSSupplierCreditInvoice iObject, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }
        @Override
        public int getDefaultWidth() {
            return 90;
        }
    };



}
