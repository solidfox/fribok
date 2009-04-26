package se.swedsoft.bookkeeping.gui.customer.util;

import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.calc.math.SSCustomerMath;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.util.List;
import java.math.BigDecimal;

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
    public Class getType() {
        return SSCustomer.class;
    }


    /**
     * Number column
     */
    public static SSTableColumn<SSCustomer> COLUMN_NUMBER = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("customertable.column.1")) {
        public Object getValue(SSCustomer iObject) {
            return iObject.getNumber();
        }

        public void setValue(SSCustomer iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Name
     */
    public static SSTableColumn<SSCustomer> COLUMN_NAME = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("customertable.column.2")) {
        public Object getValue(SSCustomer iObject) {
            return iObject.getName();
        }

        public void setValue(SSCustomer iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }
        public int getDefaultWidth() {
            return 180;
        }
    };

    /**
     * Your contact
     */
    public static SSTableColumn<SSCustomer> COLUMN_YOUR_CONTACT = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("customertable.column.3")) {
        public Object getValue(SSCustomer iObject) {
            return iObject.getYourContactPerson();
        }

        public void setValue(SSCustomer iObject, Object iValue) {
            iObject.setYourContactPerson((String)iValue);
        }

        public Class getColumnClass() {
            return String.class;
        }
        public int getDefaultWidth() {
            return 142;
        }
    };


    /**
     * Date column
     */
    public static SSTableColumn<SSCustomer> COLUMN_REGISTRATION_NUMBER= new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("customertable.column.4")) {
        public Object getValue(SSCustomer iObject) {
            return iObject.getRegistrationNumber();
        }

        public void setValue(SSCustomer iObject, Object iValue) {
            iObject.setPhone1((String)iValue);
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 142;
        }

    };


    /**
     * Our customer nr
     */
    public static SSTableColumn<SSCustomer> COLUMN_PHONE = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("customertable.column.5")) {
        public Object getValue(SSCustomer iObject) {
            return iObject.getPhone1();
        }

        public void setValue(SSCustomer iObject, Object iValue) {
            iObject.setPhone1((String)iValue);
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 142;
        }

    };

    /**
     * Customer claim
     */
    public static SSTableColumn<SSCustomer> COLUMN_CUSTOMER_CLAIM = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("customertable.column.6")) {
        public Object getValue(SSCustomer iCustomer) {
            List<SSInvoice> iInvoices = SSCustomerMath.iInvoicesForCustomers.get(iCustomer.getNumber());
            if(iInvoices == null) return new BigDecimal(0.00);
            BigDecimal iTotalSum = new BigDecimal(0.00);
            for(SSInvoice iInvoice:iInvoices){
                if(SSInvoiceMath.iSaldoMap.containsKey(iInvoice.getNumber())){
                    BigDecimal iInvoiceSaldo = SSInvoiceMath.iSaldoMap.get(iInvoice.getNumber());
                    iTotalSum = iTotalSum.add(SSInvoiceMath.convertToLocal(iInvoice,iInvoiceSaldo));
                }
            }
            return iTotalSum;
        }

        public void setValue(SSCustomer iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 100;
        }

    };


    /**
     * Supplier debt
     */
    public static SSTableColumn<SSCustomer> COLUMN_CREDIT_LIMIT = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("customertable.column.7")) {
        public Object getValue(SSCustomer iCustomer) {
            return iCustomer.getCreditLimit();
        }

        public void setValue(SSCustomer iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

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

        iModel.addColumn( SSCustomerTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSCustomerTableModel.COLUMN_NAME   );

        return iModel;

    }




}
