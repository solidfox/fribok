package se.swedsoft.bookkeeping.gui.accountingyear.util;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;

import java.math.BigDecimal;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Date: 2006-feb-15
 * Time: 16:28:34
 */
public class SSStartingAmountTableModel  extends SSDefaultTableModel<SSAccount> {

    private static ResourceBundle cBunbdle = SSBundle.getBundle();

    private Map<SSAccount, BigDecimal> iInBalance;

    /**
     *
     * @param pInBalance
     */
    public SSStartingAmountTableModel( Map<SSAccount, BigDecimal> pInBalance){
        iInBalance = pInBalance;
    
        addColumn(  cBunbdle.getString("startingammounttable.column.1"));
        addColumn(  cBunbdle.getString("startingammounttable.column.2"));
        addColumn(  cBunbdle.getString("startingammounttable.column.3"));
    }

    /**
     * Returns the type of data in this model. <P>
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSAccount.class;
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
        SSAccount iAccount = getObject(rowIndex);

        Object value = null;

        switch (columnIndex) {
            case 0:
                value = iAccount.getNumber();
                break;
            case 1:
                value = iAccount.getDescription();
                break;
            case 2:
                value = iInBalance.get(iAccount);
                if(value == null) value = new BigDecimal(0.0);
                break;
        }

        return value;
    }


    /**
     * This empty implementation is provided so users don't have to implement
     * this method if their data model is not editable.
     *
     * @param aValue      value to assign to cell
     * @param rowIndex    row of cell
     * @param columnIndex column of cell
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        SSAccount iAccount = getObject(rowIndex);

        switch (columnIndex) {
            case 2:
                BigDecimal iVal = (BigDecimal)aValue;
                iInBalance.put(iAccount, iVal);
                break;
        }

        fireTableCellUpdated(rowIndex, columnIndex);
    }


    /**
     * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     * @param columnIndex the column being queried
     * @return the Object.class
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {

        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return BigDecimal.class;
        }

        return super.getColumnClass(columnIndex);
    }

    /**
     * Returns false.  This is the default implementation for all cells.
     *
     * @param rowIndex    the row being queried
     * @param columnIndex the column being queried
     * @return false
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2;
    }
}
