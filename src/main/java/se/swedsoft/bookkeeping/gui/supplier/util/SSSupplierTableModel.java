package se.swedsoft.bookkeeping.gui.supplier.util;

import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSSupplierMath;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSSupplierTableModel extends SSTableModel<SSSupplier> {


    /**
     * Default constructor.
     */
    public SSSupplierTableModel() {
        super(SSDB.getInstance().getSuppliers() );
    }

    /**
     * Constructor.
     *
     * @param pObjects The data for the table model.
     */
    public SSSupplierTableModel(List<SSSupplier> pObjects) {
        super(pObjects);
    }


    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSSupplier.class;
    }

    /**
     *
     * @return
     */
    public static SSTableModel<SSSupplier> getDropDownModel() {
        SSSupplierTableModel iModel = new SSSupplierTableModel();
        iModel.addColumn(COLUMN_NUMBER);
        iModel.addColumn(COLUMN_NAME);
        return iModel;

    }



    /**
     * Number column
     */
    public static SSTableColumn<SSSupplier> COLUMN_NUMBER = new SSTableColumn<SSSupplier>(SSBundle.getBundle().getString("suppliertable.column.1")) {
        @Override
        public Object getValue(SSSupplier iObject) {
            return iObject.getNumber();
        }

        @Override
        public void setValue(SSSupplier iObject, Object iValue) {
        }

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
     * Name
     */
    public static SSTableColumn<SSSupplier> COLUMN_NAME = new SSTableColumn<SSSupplier>(SSBundle.getBundle().getString("suppliertable.column.2")) {
        @Override
        public Object getValue(SSSupplier iObject) {
            return iObject.getName();
        }

        @Override
        public void setValue(SSSupplier iObject, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }
        @Override
        public int getDefaultWidth() {
            return 142;
        }
    };

    /**
     * Your contact
     */
    public static SSTableColumn<SSSupplier> COLUMN_YOUR_CONTACT = new SSTableColumn<SSSupplier>(SSBundle.getBundle().getString("suppliertable.column.3")) {
        @Override
        public Object getValue(SSSupplier iObject) {
            return iObject.getYourContact();
        }

        @Override
        public void setValue(SSSupplier iObject, Object iValue) {
            iObject.setYourContact((String)iValue);
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }
        @Override
        public int getDefaultWidth() {
            return 142;
        }
    };


    /**
     * Date column
     */
    public static SSTableColumn<SSSupplier> COLUMN_PHONE = new SSTableColumn<SSSupplier>(SSBundle.getBundle().getString("suppliertable.column.4")) {
        @Override
        public Object getValue(SSSupplier iObject) {
            return iObject.getPhone1();
        }

        @Override
        public void setValue(SSSupplier iObject, Object iValue) {
            iObject.setPhone1((String)iValue);
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 110;
        }

    };


    /**
     * Our customer nr
     */
    public static SSTableColumn<SSSupplier> COLUMN_OUR_CUSTOMER_NR = new SSTableColumn<SSSupplier>(SSBundle.getBundle().getString("suppliertable.column.5")) {
        @Override
        public Object getValue(SSSupplier iObject) {
            return iObject.getOurCustomerNr();
        }

        @Override
        public void setValue(SSSupplier iObject, Object iValue) {
            iObject.setOurCustomerNr((String)iValue);
        }

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
     * Supplier debt
     */
    public static SSTableColumn<SSSupplier> COLUMN_SUPPLIERDEBT = new SSTableColumn<SSSupplier>(SSBundle.getBundle().getString("suppliertable.column.6")) {
        @Override
        public Object getValue(SSSupplier iObject) {
            List<SSSupplierInvoice> iInvoices = SSSupplierMath.iInvoicesForSuppliers.get(iObject.getNumber());
            if(iInvoices == null) return new BigDecimal(0);
            BigDecimal iTotalSum = new BigDecimal(0);
            for(SSSupplierInvoice iInvoice:iInvoices){
                if(SSSupplierInvoiceMath.iSaldoMap.containsKey(iInvoice.getNumber())){
                    BigDecimal iSupplierInvoiceSaldo = SSSupplierInvoiceMath.iSaldoMap.get(iInvoice.getNumber());
                    iTotalSum = iTotalSum.add(SSSupplierInvoiceMath.convertToLocal(iInvoice,iSupplierInvoiceSaldo));
                }
            }
            return iTotalSum;
        }

        @Override
        public void setValue(SSSupplier iObject, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }

    };



}
