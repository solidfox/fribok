/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.budget.panel;


import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSBudget;
import se.swedsoft.bookkeeping.gui.budget.util.SSBudgetTableModels;
import se.swedsoft.bookkeeping.gui.budget.util.SSMonthlyTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSSelectionListener;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBoxOld;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellEditor;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellRenderer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;


/**
 *
 */
public class SSBudgetMainPanel {

    private JPanel iPanel;

    private SSDefaultTableModel<SSAccount> iBudgetTableModel;

    private SSMonthlyTableModel            iMonthlyTableModel;

    private SSTable iBudgetTable;

    private SSTable iMonthlyTable;

    private JTextField iAccountDescription;

    private JTabbedPane iTabbedPane;

    private SSAccount iCurrentAccount;

    private SSBudget  iBudget;

    private SSTableComboBoxOld<SSAccount>iAccountNumber;

    private JTextField iSumTextField;

    /**
     * Default constructor.
     * @param pBudget
     */
    public SSBudgetMainPanel(SSBudget pBudget) {
        iBudget = pBudget;
        iBudgetTableModel = SSBudgetTableModels.createBudgetTableModel(pBudget);
        iMonthlyTableModel = SSBudgetTableModels.createMonthlyTableModel(pBudget, null);

        final List<SSAccount> iAccounts = pBudget.getAccounts();

        // Get the available accounts.
        SSDefaultTableModel<SSAccount> iModel = new SSDefaultTableModel<SSAccount>(
                iAccounts) {

            @Override
            public Class getType() {
                return SSAccount.class;
            }

            @Override
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
                }
                return value;
            }
        };

        iModel.addColumn(SSBundle.getBundle().getString("accounttable.column.1"));
        iModel.addColumn(SSBundle.getBundle().getString("accounttable.column.2"));

        iAccountNumber.setModel(iModel);
        iAccountNumber.setSearchColumns(0);
        iAccountNumber.setColumnWidths(60, 270);

        iAccountNumber.addSelectionListener(new SSSelectionListener<SSAccount>() {
            public void selected(SSAccount selected) {
                iMonthlyTable.removeEditor();

                setCurrentAccount(selected);
            }
        });

        iBudgetTable.setModel(iBudgetTableModel);

        iBudgetTable.getColumnModel().getColumn(0).setPreferredWidth(60);
        iBudgetTable.getColumnModel().getColumn(1).setPreferredWidth(600);
        iBudgetTable.getColumnModel().getColumn(2).setPreferredWidth(120);

        iBudgetTable.getColumnModel().getColumn(2).setCellRenderer(
                new SSBigDecimalCellRenderer(2, true));
        iBudgetTable.getColumnModel().getColumn(2).setCellEditor(
                new SSBigDecimalCellEditor(2));

        iMonthlyTable.setDefaultEditor(BigDecimal.class, new SSBigDecimalCellEditor(2));
        iMonthlyTable.setModel(iMonthlyTableModel);

        iMonthlyTable.getColumnModel().getColumn(0).setMaxWidth(60);
        iMonthlyTable.getColumnModel().getColumn(1).setCellRenderer(
                new SSBigDecimalCellRenderer(2, true));
        iMonthlyTable.getColumnModel().getColumn(1).setCellEditor(
                new SSBigDecimalCellEditor(2));

        iBudgetTable.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {

                if (iBudgetTable.getSelectedRowCount() > 0) {

                    SSAccount account = iBudgetTableModel.getObject(
                            iBudgetTable.getSelectedRow());

                    if (account != null) {
                        setCurrentAccount(account);
                    }
                }
            }
        });

        iBudgetTable.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && iBudgetTable.getSelectedColumn() != 2) {
                    iTabbedPane.setSelectedIndex(1);
                }
            }

            public void mousePressed(MouseEvent e) {}

            public void mouseReleased(MouseEvent e) {}

            public void mouseEntered(MouseEvent e) {}

            public void mouseExited(MouseEvent e) {}
        });

        iBudgetTableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                updateSumLabel();
            }
        });
        iMonthlyTableModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                updateSumLabel();
            }
        });
        updateSumLabel();
    }

    /**
     *
     * @param pAccount
     */
    private void setCurrentAccount(SSAccount pAccount) {
        iCurrentAccount = pAccount;

        iMonthlyTableModel.setAccount(pAccount);

        updateSumLabel();

        iAccountDescription.setText(pAccount.getDescription());

        iAccountNumber.setSelected(pAccount);
    }

    /**
     *
     */
    private void updateSumLabel() {
        NumberFormat format = NumberFormat.getNumberInstance();

        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        format.setGroupingUsed(true);

        if (iCurrentAccount == null) {
            iSumTextField.setText(format.format(0));
        } else {
            BigDecimal value = iBudget.getSumForAccount(iCurrentAccount);

            if (value == null) {
                iSumTextField.setText(format.format(0));
            } else {
                iSumTextField.setText(format.format(value));
            }
        }
    }

    /**
     * Returns the panel.
     *
     * @return The main panel.
     */
    public JPanel getPanel() {
        return iPanel;
    }

    /**
     *
     * @return the budget
     */
    public SSBudget getBudget() {
        return iBudget;
    }

    public void setBudget(SSBudget pBudget) {
        iBudget = pBudget;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.budget.panel.SSBudgetMainPanel");
        sb.append("{iAccountDescription=").append(iAccountDescription);
        sb.append(", iAccountNumber=").append(iAccountNumber);
        sb.append(", iBudget=").append(iBudget);
        sb.append(", iBudgetTable=").append(iBudgetTable);
        sb.append(", iBudgetTableModel=").append(iBudgetTableModel);
        sb.append(", iCurrentAccount=").append(iCurrentAccount);
        sb.append(", iMonthlyTable=").append(iMonthlyTable);
        sb.append(", iMonthlyTableModel=").append(iMonthlyTableModel);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iSumTextField=").append(iSumTextField);
        sb.append(", iTabbedPane=").append(iTabbedPane);
        sb.append('}');
        return sb.toString();
    }
}
