package se.swedsoft.bookkeeping.gui.customer;

import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.customer.panel.SSCustomerPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

/**
 * User: Andreas Lago
 * Date: 2006-sep-05
 * Time: 13:42:44
 */
public class SSCustomerDialog {

    private static ResourceBundle bundle = SSBundle.getBundle();

    private static Dimension iDialogSize = new Dimension(640, 480);

    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, final AbstractTableModel pModel) {
        final SSDialog        iDialog = new SSDialog(iMainFrame, bundle.getString("customerframe.new.title"));
        final SSCustomerPanel iPanel  = new SSCustomerPanel(iDialog, false);

        iPanel.setCustomer(new SSCustomer());

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSCustomer iCustomer = iPanel.getCustomer();

                if (SSDB.getInstance().getCustomers().contains(iCustomer)) {
                    new SSErrorDialog(iMainFrame, "customerframe.duplicate", iCustomer.getNumber());
                    return;
                }

                SSDB.getInstance().addCustomer(iCustomer);

                if (pModel != null) pModel.fireTableDataChanged();

                iDialog.closeDialog();
            }
        };
        iPanel.addOkAction(iSaveAction);

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDialog.closeDialog();
            }
        });
        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(!iPanel.isValid()) {
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "customerframe.saveonclose") != JOptionPane.OK_OPTION) {
                    return;
                }

                iSaveAction.actionPerformed(null);
            }
        });
        iDialog.setSize(iDialogSize);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }

    /**
     *
     * @param iMainFrame
     * @param iCustomer
     * @param pModel
     */
    public static void editDialog(final SSMainFrame iMainFrame, SSCustomer iCustomer, final AbstractTableModel pModel) {
        final String lockString = "customer"+iCustomer.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "customerframe.customeropen", iCustomer.getNumber());
            return;
        }
        final SSDialog        iDialog = new SSDialog(iMainFrame, bundle.getString("customerframe.edit.title"));
        final SSCustomerPanel iPanel  = new SSCustomerPanel(iDialog, true);


        iPanel.setCustomer(iCustomer);
        //iPanel.setEditPanel(true);
        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSCustomer iCustomer = iPanel.getCustomer();

                SSDB.getInstance().updateCustomer(iCustomer);

                if (pModel != null) pModel.fireTableDataChanged();
                SSPostLock.removeLock(lockString);
                iDialog.closeDialog();
            }
        };
        iPanel.addOkAction(iSaveAction);

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPostLock.removeLock(lockString);
                iDialog.closeDialog();
            }
        });
        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(!iPanel.isValid()) {
                    SSPostLock.removeLock(lockString);
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "customerframe.saveonclose") != JOptionPane.OK_OPTION) {
                    SSPostLock.removeLock(lockString);
                    return;
                }

                iSaveAction.actionPerformed(null);
            }
        });
        iDialog.setSize(iDialogSize);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }


    /**
     *
     * @param iMainFrame
     * @param iCustomer
     * @param pModel
     */
    public static void copyDialog(final SSMainFrame iMainFrame, SSCustomer iCustomer, final AbstractTableModel pModel) {
        final String lockString = "customer"+iCustomer.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if (SSPostLock.isLocked(lockString)) {
            new SSErrorDialog(iMainFrame, "customerframe.customeropen", iCustomer.getNumber());
            return;
        }
        final SSDialog        iDialog = new SSDialog(iMainFrame, bundle.getString("customerframe.copy.title"));
        final SSCustomerPanel iPanel  = new SSCustomerPanel(iDialog, false);

        SSCustomer iNew = new SSCustomer(iCustomer);

        iPanel.setCustomer(iNew);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSCustomer iCustomer = iPanel.getCustomer();

                if (SSDB.getInstance().getCustomers().contains(iCustomer)) {
                    new SSErrorDialog(iMainFrame, "customerframe.duplicate", iCustomer.getNumber());
                    return;
                }
                SSDB.getInstance().addCustomer(iCustomer);

                if (pModel != null) pModel.fireTableDataChanged();

                iDialog.closeDialog();
            }
        };
        iPanel.addOkAction(iSaveAction);

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDialog.closeDialog();
            }
        });
        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(!iPanel.isValid()){
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "customerframe.saveonclose") != JOptionPane.OK_OPTION) {
                    return;
                }

                iSaveAction.actionPerformed(null);
            }
        });
        iDialog.setSize(iDialogSize);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }
}
