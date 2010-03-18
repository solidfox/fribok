package se.swedsoft.bookkeeping.gui.supplierinvoice.util;

import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
     * @param iSupplierInvoices
     */
    public SSSupplierInvoiceTableModel(List<SSSupplierInvoice> iSupplierInvoices) {
        super( iSupplierInvoices );
    }

    /**
     * Returns the type of data in this model. <P>
     *
     * @return The current data type.
     */
    @Override
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
        @Override
        public Object getValue(SSSupplierInvoice iObject) {
            return iObject.getNumber();
        }

        @Override
        public void setValue(SSSupplierInvoice iObject, Object iValue) {
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
    public static SSTableColumn<SSSupplierInvoice> COLUMN_REFERENCE_NUMBER = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.10")) {
        @Override
        public Object getValue(SSSupplierInvoice iObject) {
            return iObject.getReferencenumber();
        }

        @Override
        public void setValue(SSSupplierInvoice iObject, Object iValue) {
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

    /**
     * Supplier nr
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_SUPPLIER_NUMBER = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.2")) {
        @Override
        public Object getValue(SSSupplierInvoice iObject) {
            return iObject.getSupplierNr();
        }

        @Override
        public void setValue(SSSupplierInvoice iObject, Object iValue) {
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
    public static SSTableColumn<SSSupplierInvoice> COLUMN_SUPPLIER_NAME = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.3")) {
        @Override
        public Object getValue(SSSupplierInvoice iObject) {
            return iObject.getSupplierName();
        }

        @Override
        public void setValue(SSSupplierInvoice iObject, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }
        @Override
        public int getDefaultWidth() {
            return 180;
        }
    };


    /**
     * Supplier name
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_DATE = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.4")) {
        @Override
        public Object getValue(SSSupplierInvoice iObject) {
            return iObject.getDate();
        }

        @Override
        public void setValue(SSSupplierInvoice iObject, Object iValue) {
            iObject.setDate((Date)iValue);
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
    public static SSTableColumn<SSSupplierInvoice> COLUMN_DUEDATE = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.5")) {
        @Override
        public Object getValue(SSSupplierInvoice iObject) {
            return iObject.getDueDate();
        }

        @Override
        public void setValue(SSSupplierInvoice iObject, Object iValue) {
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
    public static SSTableColumn<SSSupplierInvoice> COLUMN_NETSUM = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.6")) {
        @Override
        public Object getValue(SSSupplierInvoice iObject) {
            return SSSupplierInvoiceMath.getNetSum(iObject);
        }

        @Override
        public void setValue(SSSupplierInvoice iObject, Object iValue) {
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
    public static SSTableColumn<SSSupplierInvoice> COLUMN_CURRENCY = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.7")) {
        @Override
        public Object getValue(SSSupplierInvoice iObject) {
            return iObject.getCurrency();
        }

        @Override
        public void setValue(SSSupplierInvoice iObject, Object iValue) {
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
    public static SSTableColumn<SSSupplierInvoice> COLUMN_TOTALSUM = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.8")) {
        @Override
        public Object getValue(SSSupplierInvoice iObject) {
            return SSSupplierInvoiceMath.getTotalSum(iObject);
        }

        @Override
        public void setValue(SSSupplierInvoice iObject, Object iValue) {
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
    public static SSTableColumn<SSSupplierInvoice> COLUMN_SALDO = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.9")) {
        @Override
        public Object getValue(SSSupplierInvoice iObject) {
            return SSSupplierInvoiceMath.getSaldo(iObject.getNumber());
        }

        @Override
        public void setValue(SSSupplierInvoice iObject, Object iValue) {
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
     * Supplier nr
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_SUPPLIER_BANKGIRO = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.11")) {
        @Override
        public Object getValue(SSSupplierInvoice iObject) {
            SSSupplier iSupplier = iObject.getSupplier(SSDB.getInstance().getSuppliers() );

            return iSupplier == null ? null: iSupplier.getBankgiro();
        }

        @Override
        public void setValue(SSSupplierInvoice iObject, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }
        @Override
        public int getDefaultWidth() {
            return 80;
        }
    };
    /**
     * Supplier nr
     */
    public static SSTableColumn<SSSupplierInvoice> COLUMN_SUPPLIER_PLUSGIRO = new SSTableColumn<SSSupplierInvoice>(SSBundle.getBundle().getString("supplierinvoicetable.column.12")) {
        @Override
        public Object getValue(SSSupplierInvoice iObject) {
            SSSupplier iSupplier = iObject.getSupplier(SSDB.getInstance().getSuppliers() );

            return iSupplier == null ? null: iSupplier.getPlusgiro();
        }

        @Override
        public void setValue(SSSupplierInvoice iObject, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }
        @Override
        public int getDefaultWidth() {
            return 80;
        }
    };

}
