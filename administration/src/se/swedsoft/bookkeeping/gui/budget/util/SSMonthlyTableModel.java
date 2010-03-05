package se.swedsoft.bookkeeping.gui.budget.util;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSBudget;
import se.swedsoft.bookkeeping.data.SSMonth;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;

import java.math.BigDecimal;

/**
 * Date: 2006-jan-30
 * Time: 09:43:55
 */
public class SSMonthlyTableModel extends SSDefaultTableModel<SSMonth> {
    private SSBudget  iBudget;
    private SSAccount iAccount;

    /**
     * Default constructor. <P>
     */
    public SSMonthlyTableModel(SSBudget pBudget, SSAccount pAccount) {
        super();
        iBudget  = pBudget;
        iAccount = pAccount;
    }

    /**
     *
     * @return Class
     */
    @Override
    public Class<?> getType() {
        return SSMonth.class;
    }
    /**
     *
     * @param columnIndex
     * @return Class
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return BigDecimal.class;
        }
        return super.getColumnClass(columnIndex);
    }


    /**
     *
     * @param rowIndex
     * @param columnIndex
     * @return Object
     */
    public Object getValueAt(int rowIndex, int columnIndex) {

        SSMonth month = getObject(rowIndex);
        Object value = null;

        switch (columnIndex) {
            case 0:
                value = month.toString();
                break;
            case 1:
                value= (iAccount == null) ? new BigDecimal(0.0) : iBudget.getValueForAccountAndMonth(iAccount, month);
                break;
        }

        return value;
    }
    /**
     *
     * @param rowIndex
     * @param columnIndex
     * @return boolean
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (iAccount != null) && (columnIndex == 1);
    }

    /**
     *
     * @param aValue
     * @param rowIndex
     * @param columnIndex
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        SSMonth month = getObject(rowIndex);

        if ((columnIndex == 1) && (iAccount != null)) {
            iBudget.setSaldoForAccountAndMonth(iAccount, month, (BigDecimal)aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     *
     * @param pAccount
     */
    public void setAccount(SSAccount pAccount){
        iAccount = pAccount;
        fireTableDataChanged();
    }


}
