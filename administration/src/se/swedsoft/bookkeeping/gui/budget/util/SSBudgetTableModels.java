package se.swedsoft.bookkeeping.gui.budget.util;

import se.swedsoft.bookkeeping.data.SSBudget;
import se.swedsoft.bookkeeping.data.SSMonth;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.math.BigDecimal;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

/**
 * Date: 2006-jan-27
 * Time: 16:23:32
 */
public class SSBudgetTableModels {

    /**
     *
     * @param pBudget
     * @return SSDefaultTableModel
     */
    public static  SSDefaultTableModel<SSAccount> createBudgetTableModel(final SSBudget pBudget) {
        List<SSAccount> iAccounts = pBudget.getAccounts();

        SSDefaultTableModel<SSAccount> iTableModel =  new SSDefaultTableModel<SSAccount>() {
            /**
             *
             * @return Class
             */
            public Class getType() {
                return SSAccount.class;
            }

            /**
             *
             * @param columnIndex
             * @return Class
             */
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    case 2:
                        return BigDecimal.class;
                }
                return super.getColumnClass(columnIndex);
            }

            /**
             *
             * @param rowIndex
             * @param columnIndex
             * @return
             */
            public Object getValueAt(int rowIndex, int columnIndex) {

                SSAccount account = getObject(rowIndex);
                Object value = null;

                switch (columnIndex) {
                    case 0:
                        value = account.getNumber();
                        break;
                    case 1:
                        value = account.getDescription();
                        break;
                    case 2:
                        value = pBudget.getSumForAccount(account);
                        break;
                }

                return value;
            }

            /**
             *
             * @param aValue
             * @param rowIndex
             * @param columnIndex
             */
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                SSAccount account = getObject(rowIndex);

                if (columnIndex == 2) {
                    pBudget.setSumForAccount(account, (BigDecimal)aValue);
                }

                fireTableCellUpdated(rowIndex, columnIndex);
            }

            /**
             *
             * @param rowIndex
             * @param columnIndex
             * @return
             */
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnIndex == 2;
            }

        };
        iTableModel.addColumn(SSBundle.getBundle().getString("budgetframe.budgettable.column.1"));
        iTableModel.addColumn(SSBundle.getBundle().getString("budgetframe.budgettable.column.2"));
        iTableModel.addColumn(SSBundle.getBundle().getString("budgetframe.budgettable.column.3"));

        iTableModel.setObjects(iAccounts);

        return iTableModel;
    }

    /**
     *
     * @param pBudget
     * @return SSDefaultTableModel
     */
    public static SSMonthlyTableModel createMonthlyTableModel(final SSBudget pBudget, final SSAccount pAccount) {
        List<SSMonth> iMonths = pBudget.getMonths();

        SSMonthlyTableModel iTableModel = new SSMonthlyTableModel(pBudget, pAccount);
        iTableModel.addColumn(SSBundle.getBundle().getString("budgetframe.monthlytable.column.1"));
        iTableModel.addColumn(SSBundle.getBundle().getString("budgetframe.monthlytable.column.2"));

        Collections.sort(iMonths, new Comparator<SSMonth>() {
            public int compare(SSMonth o1, SSMonth o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });


        iTableModel.setObjects(iMonths);
        return iTableModel;
    }


}
