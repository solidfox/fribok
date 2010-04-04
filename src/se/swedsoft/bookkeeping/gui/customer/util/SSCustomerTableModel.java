package se.swedsoft.bookkeeping.gui.customer.util;

import se.swedsoft.bookkeeping.calc.math.SSCustomerMath;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSInvoice;
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
public class SSCustomerTableModel extends SSTableModel<SSCustomer> {


    /**
     * Default constructor.
     */
    public SSCustomerTableModel() {
        super(SSDB.getInstance().getCustomers() );
    }

    /**
     * Constructor.
     *
     * @param pObjects The data for the table model.
     */
    public SSCustomerTableModel(List<SSCustomer> pObjects) {
        super(pObjects);
    }


    /**
     * Returns the type of data in this model. <P>
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSCustomer.class;
    }


    /**
     * Number column
     */
    public static SSTableColumn<SSCustomer> COLUMN_NUMBER = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("customertable.column.1")) {
        @Override
        public Object getValue(SSCustomer iObject) {
            return iObject.getNumber();
        }

        @Override
        public void setValue(SSCustomer iObject, Object iValue) {
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
    public static SSTableColumn<SSCustomer> COLUMN_NAME = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("customertable.column.2")) {
        @Override
        public Object getValue(SSCustomer iObject) {
            return iObject.getName();
        }

        @Override
        public void setValue(SSCustomer iObject, Object iValue) {
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
     * Your contact
     */
    public static SSTableColumn<SSCustomer> COLUMN_YOUR_CONTACT = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("customertable.column.3")) {
        @Override
        public Object getValue(SSCustomer iObject) {
            return iObject.getYourContactPerson();
        }

        @Override
        public void setValue(SSCustomer iObject, Object iValue) {
            iObject.setYourContactPerson((String)iValue);
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
    public static SSTableColumn<SSCustomer> COLUMN_REGISTRATION_NUMBER= new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("customertable.column.4")) {
        @Override
        public Object getValue(SSCustomer iObject) {
            return iObject.getRegistrationNumber();
        }

        @Override
        public void setValue(SSCustomer iObject, Object iValue) {
            iObject.setPhone1((String)iValue);
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
     * Our customer nr
     */
    public static SSTableColumn<SSCustomer> COLUMN_PHONE = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("customertable.column.5")) {
        @Override
        public Object getValue(SSCustomer iObject) {
            return iObject.getPhone1();
        }

        @Override
        public void setValue(SSCustomer iObject, Object iValue) {
            iObject.setPhone1((String)iValue);
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
     * Customer claim
     */
    public static SSTableColumn<SSCustomer> COLUMN_CUSTOMER_CLAIM = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("customertable.column.6")) {
        @Override
        public Object getValue(SSCustomer iCustomer) {
            List<SSInvoice> iInvoices = SSCustomerMath.iInvoicesForCustomers.get(iCustomer.getNumber());
            if(iInvoices == null) return new BigDecimal(0);
            BigDecimal iTotalSum = new BigDecimal(0);
            for(SSInvoice iInvoice:iInvoices){
                if(SSInvoiceMath.iSaldoMap.containsKey(iInvoice.getNumber())){
                    BigDecimal iInvoiceSaldo = SSInvoiceMath.iSaldoMap.get(iInvoice.getNumber());
                    iTotalSum = iTotalSum.add(SSInvoiceMath.convertToLocal(iInvoice,iInvoiceSaldo));
                }
            }
            return iTotalSum;
        }

        @Override
        public void setValue(SSCustomer iObject, Object iValue) {
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


    /**
     * Supplier debt
     */
    public static SSTableColumn<SSCustomer> COLUMN_CREDIT_LIMIT = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("customertable.column.7")) {
        @Override
        public Object getValue(SSCustomer iCustomer) {
            return iCustomer.getCreditLimit();
        }

        @Override
        public void setValue(SSCustomer iObject, Object iValue) {
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

    /**
     *
     * @return
     */
    public static SSCustomerTableModel getDropDownModel(){
        SSCustomerTableModel iModel = new SSCustomerTableModel();

        iModel.addColumn( COLUMN_NUMBER );
        iModel.addColumn( COLUMN_NAME   );

        return iModel;

    }




}
