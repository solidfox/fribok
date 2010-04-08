package se.swedsoft.bookkeeping.gui.ownreport.util;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSMonth;
import se.swedsoft.bookkeeping.data.SSOwnReportRow;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Date: 2006-jan-30
 * Time: 09:43:55
 */
public class SSOwnReportMonthlyTableModel extends SSDefaultTableModel<SSMonth> {
    private SSAccount iAccount;
    List<SSOwnReportRow> iRows;
    /**
     * Default constructor. <P>
     * @param pAccount
     * @param pRows
     */
    public SSOwnReportMonthlyTableModel(SSAccount pAccount, List<SSOwnReportRow> pRows) {
        iAccount = pAccount;
        iRows = pRows;
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
                value= (iAccount == null) ? new BigDecimal(0) : getValueForAccountAndMonth(iAccount, month).setScale(2, RoundingMode.HALF_UP);
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
            setValueForAccountAndMonth(iAccount, month, (BigDecimal)aValue);
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

    public BigDecimal getValueForAccountAndMonth(SSAccount iAccount, SSMonth iMonth){
        for(SSOwnReportRow iOwnReportRow : iRows){
            for(SSOwnReportAccountRow iRow : iOwnReportRow.getAccountRows()){
                if(iRow.getAccount() != null && iAccount.equals(iRow.getAccount())) return iRow.getBudget().get(iMonth);
            }
        }
        return new BigDecimal(0);
    }

    public void setValueForAccountAndMonth(SSAccount iAccount, SSMonth iMonth, BigDecimal iValue){
        for(SSOwnReportRow iOwnReportRow : iRows){
            for(SSOwnReportAccountRow iRow : iOwnReportRow.getAccountRows()){
                if(iRow.getAccount() != null && iAccount.equals(iRow.getAccount())) iRow.getBudget().put(iMonth, iValue);
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.ownreport.util.SSOwnReportMonthlyTableModel");
        sb.append("{iAccount=").append(iAccount);
        sb.append(", iRows=").append(iRows);
        sb.append('}');
        return sb.toString();
    }
}

