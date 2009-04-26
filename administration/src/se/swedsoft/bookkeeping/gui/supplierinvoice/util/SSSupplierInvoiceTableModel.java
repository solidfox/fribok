package se.swedsoft.bookkeeping.gui.supplierinvoice.util;

import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSSupplierInvoiceTableModel extends SSTableModel<SSSupplierInvoice> {

    /**
     * Default constructor.
     */
    public SSSupplierInvoiceTableModel() {
        super( SSDB.getInstance().getSupplierInvoices() );
    }

    /**
     * Default constructor.
     */
    public SSSupplierInvoiceTableModel(List<SSSupplierInvoice> iSupplierInvoices) {
        super( iSupplierInvoices );
    }

    /**
     * Returns the type of data in this model. <P>
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSSupplierInvoice.class;
    }



    /**
     *
     * @return
     */
    public static SSTableModel<SSSupplierInvoice> getDropDownModel(){

        return getDropDownModel(SSDB.getInstance().getSupplierInvoices());
    }

    /**
     *
     * @param iSupplierInvoices
     * @return
     */
    public static SSTableModel<SSSupplierInvoice> getDropDownModel(List<SSSupplierInvoice> iSupplierInvoices){
        SSSupplierInvoiceTableModel iModel = new SSSupplierInvoiceTableModel( iSupplierInvoices );
        iModel.addColumn(SSSupplierInvoiceTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSSupplierInvoiceTableModel.COLUMN_REFERENCE_NUMBER);
        iModel.addColumn(SSSupplierInvoiceTableModel.COLUMN_SUPPLIER_NUMBER);
        iModel.addColumn(SSSupplierInvoiceTableModel.COLUMN_SUPPLIER_NAME);
        return iModel;
    }




    /**
     * Number
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_NUMBER = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.1")) {
        public Object getValue(SSSupplierInvoice iObject) {
            return iObject.getNumber();
        }

        public void setValue(SSSupplierInvoice iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }
        public int getDefaultWidth() {
            return 70;
        }
    };

    /**
     * Supplier nr
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_REFERENCE_NUMBER = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.10")) {
        public Object getValue(SSSupplierInvoice iObject) {
            return iObject.getReferencenumber();
        }

        public void setValue(SSSupplierInvoice iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }
        public int getDefaultWidth() {
            return 120;
        }
    };

    /**
     * Supplier nr
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_SUPPLIER_NUMBER = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.2")) {
        public Object getValue(SSSupplierInvoice iObject) {
            return iObject.getSupplierNr();
        }

        public void setValue(SSSupplierInvoice iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }
        public int getDefaultWidth() {
            return 150;
        }
    };

    /**
     * Supplier name
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_SUPPLIER_NAME = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.3")) {
        public Object getValue(SSSupplierInvoice iObject) {
            return iObject.getSupplierName();
        }

        public void setValue(SSSupplierInvoice iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }
        public int getDefaultWidth() {
            return 180;
        }
    };


    /**
     * Supplier name
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_DATE = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.4")) {
        public Object getValue(SSSupplierInvoice iObject) {
            return iObject.getDate();
        }

        public void setValue(SSSupplierInvoice iObject, Object iValue) {
            iObject.setDate((Date)iValue);
        }

        public Class getColumnClass() {
            return Date.class;
        }
        public int getDefaultWidth() {
            return 90;
        }
    };

    /**
     * Supplier name
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_DUEDATE = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.5")) {
        public Object getValue(SSSupplierInvoice iObject) {
            return iObject.getDueDate();
        }

        public void setValue(SSSupplierInvoice iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return Date.class;
        }
        public int getDefaultWidth() {
            return 90;
        }
    };

    /**
     *
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_NETSUM = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.6")) {
        public Object getValue(SSSupplierInvoice iObject) {
            return SSSupplierInvoiceMath.getNetSum(iObject);
        }

        public void setValue(SSSupplierInvoice iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }
        public int getDefaultWidth() {
            return 90;
        }
    };


    /**
     *
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_CURRENCY = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.7")) {
        public Object getValue(SSSupplierInvoice iObject) {
            return iObject.getCurrency();
        }

        public void setValue(SSSupplierInvoice iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return SSCurrency.class;
        }
        public int getDefaultWidth() {
            return 50;
        }
    };

    /**
     *
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_TOTALSUM = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.8")) {
        public Object getValue(SSSupplierInvoice iObject) {
            return SSSupplierInvoiceMath.getTotalSum(iObject);
        }

        public void setValue(SSSupplierInvoice iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }
        public int getDefaultWidth() {
            return 90;
        }
    };

    /**
     *
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_SALDO = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.9")) {
        public Object getValue(SSSupplierInvoice iObject) {
            return SSSupplierInvoiceMath.getSaldo(iObject.getNumber());
        }

        public void setValue(SSSupplierInvoice iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }
        public int getDefaultWidth() {
            return 90;
        }
    };



    /**
     * Supplier nr
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_SUPPLIER_BANKGIRO = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.11")) {
        public Object getValue(SSSupplierInvoice iObject) {
            SSSupplier iSupplier = iObject.getSupplier(SSDB.getInstance().getSuppliers() );

            return iSupplier == null ? null: iSupplier.getBankgiro();
        }

        public void setValue(SSSupplierInvoice iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }
        public int getDefaultWidth() {
            return 80;
        }
    };
    /**
     * Supplier nr
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_SUPPLIER_PLUSGIRO = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.12")) {
        public Object getValue(SSSupplierInvoice iObject) {
            SSSupplier iSupplier = iObject.getSupplier(SSDB.getInstance().getSuppliers() );

            return iSupplier == null ? null: iSupplier.getPlusgiro();
        }

        public void setValue(SSSupplierInvoice iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }
        public int getDefaultWidth() {
            return 80;
        }
    };

}
