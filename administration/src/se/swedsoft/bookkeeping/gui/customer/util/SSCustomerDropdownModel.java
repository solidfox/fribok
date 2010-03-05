package se.swedsoft.bookkeeping.gui.customer.util;

import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSCustomerDropdownModel extends SSDefaultTableModel<SSCustomer> {

    /**
     * Default constructor.
     */
    public SSCustomerDropdownModel() {
        this( SSDB.getInstance().getCustomers() );
    }

    /**
     * Constructor.
     *
     * @param pObjects The data for the table model.
     */
    public SSCustomerDropdownModel(List<SSCustomer> pObjects) {
        super(pObjects);
        addColumn(SSBundle.getBundle().getString("customertable.column.1"));
        addColumn(SSBundle.getBundle().getString("customertable.column.2"));
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
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param    rowIndex    the row whose value is to be queried
     * @param    columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        SSCustomer iCustomer = getObject(rowIndex);

        Object value = null;
        switch(columnIndex){
            case 0:
                value = iCustomer.getNumber();
                break;
            case 1:
                value = iCustomer.getName();
                break;
        }

        return value;
    }

    /**
     * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     * @param columnIndex the column being queried
     * @return the Object.class
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex){
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:

        }
        return super.getColumnClass(columnIndex);
    }


}
