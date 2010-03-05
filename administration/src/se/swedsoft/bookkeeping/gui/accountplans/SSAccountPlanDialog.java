package se.swedsoft.bookkeeping.gui.accountplans;

import se.swedsoft.bookkeeping.data.SSAccountPlan;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.accountplans.panel.SSAccountPlanPanel;
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
import java.util.List;
import java.util.ResourceBundle;


/**
 * User: Andreas Lago
 * Date: 2006-okt-13
 * Time: 11:18:45
 */
public class SSAccountPlanDialog {

    private static ResourceBundle bundle = SSBundle.getBundle();

    /**
     *
     * @param iMainFrame
     * @param iModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, final AbstractTableModel iModel) {
        final SSDialog           iDialog = new SSDialog(iMainFrame, bundle.getString("accountplanframe.new.title"));
        final SSAccountPlanPanel iPanel  = new SSAccountPlanPanel(iMainFrame);

        iPanel.setAccountPlan( new SSAccountPlan() );
        iPanel.setShowBase(false);
 //       iPanel.setShowImport(false);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSAccountPlan iAccountPlan = iPanel.getAccountPlan();

                List<SSAccountPlan> iPlans = SSDB.getInstance().getAccountPlans();
                for (SSAccountPlan pAccountPlan : iPlans) {
                    if (iAccountPlan.getName().equals(pAccountPlan.getName())){
                        new SSErrorDialog(iMainFrame, "accountplanframe.duplicate", iAccountPlan.getName());
                        return;
                    }
                }


                SSDB.getInstance().addAccountPlan(iAccountPlan);

                if (iModel != null) iModel.fireTableDataChanged();

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
                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "accountplanframe.saveonclose") != JOptionPane.OK_OPTION){
                    return;
                }

                iSaveAction.actionPerformed(null);
            }
        });

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);
        iDialog.setSize(600, 450);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }

    /**
     *
     * @param iMainFrame
     * @param iModel
     */
    public static void editDialog(final SSMainFrame iMainFrame, SSAccountPlan iAccountPlan, final AbstractTableModel iModel) {
        final String lockString = "accountplan"+iAccountPlan.getId();
        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "accountplanframe.accountplanopen", iAccountPlan.getName());
            return;
        }
        final String iName = iAccountPlan.getName();
        final SSDialog           iDialog = new SSDialog(iMainFrame, bundle.getString("accountplanframe.edit.title"));
        final SSAccountPlanPanel iPanel  = new SSAccountPlanPanel(iMainFrame);

        final SSAccountPlan iOriginal = iAccountPlan;

        iPanel.setAccountPlan( new SSAccountPlan( iAccountPlan ) );
        iPanel.setShowBase(false);
    //    iPanel.setShowImport(false);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSAccountPlan iAccountPlan = iPanel.getAccountPlan();

                List<SSAccountPlan> iPlans = SSDB.getInstance().getAccountPlans();
                for (SSAccountPlan pAccountPlan : iPlans) {
                    if (iAccountPlan.getName().equals(pAccountPlan.getName()) && !iAccountPlan.getName().equals(iName)){
                        new SSErrorDialog(iMainFrame, "accountplanframe.duplicate", iAccountPlan.getName());
                        return;
                    }
                }

                iOriginal.copyFrom(iAccountPlan);
                SSDB.getInstance().updateAccountPlan(iOriginal);

                if (iModel != null) iModel.fireTableDataChanged();
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
                if(!iPanel.isValid()){
                    SSPostLock.removeLock(lockString);
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "accountplanframe.saveonclose") != JOptionPane.OK_OPTION) {
                    SSPostLock.removeLock(lockString);
                    return;
                }

                iSaveAction.actionPerformed(null);
            }
        });

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);
        iDialog.setSize(600,450);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }


    /**
     *
     * @param iMainFrame
     * @param iModel
     */
    public static void copyDialog(final SSMainFrame iMainFrame, SSAccountPlan iAccountPlan, final AbstractTableModel iModel) {
        final String lockString = "accountplan"+iAccountPlan.getId();
        if (SSPostLock.isLocked(lockString)) {
            new SSErrorDialog(iMainFrame, "accountplanframe.accountplanopen", iAccountPlan.getName());
            return;
        }
        final SSDialog           iDialog = new SSDialog(iMainFrame, bundle.getString("accountplanframe.copy.title"));
        final SSAccountPlanPanel iPanel  = new SSAccountPlanPanel(iMainFrame);

        iPanel.setAccountPlan( new SSAccountPlan( iAccountPlan ) );
        iPanel.setShowBase(false);
      //  iPanel.setShowImport(false);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSAccountPlan iAccountPlan = iPanel.getAccountPlan();

                List<SSAccountPlan> iPlans = SSDB.getInstance().getAccountPlans();
                for (SSAccountPlan pAccountPlan : iPlans) {
                    if (iAccountPlan.getName().equals(pAccountPlan.getName())){
                        new SSErrorDialog(iMainFrame, "accountplanframe.duplicate", iAccountPlan.getName());
                        return;
                    }
                }

                SSDB.getInstance().addAccountPlan(iAccountPlan);


                if (iModel != null) iModel.fireTableDataChanged();

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "accountplanframe.saveonclose") != JOptionPane.OK_OPTION) {
                    return;
                }

                iSaveAction.actionPerformed(null);
            }
        });

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);
        iDialog.setSize(600,450);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }


    /**
     *
     * @param iMainFrame
     * @param iAccountPlan
     */
    public static void editCurrentDialog(final SSMainFrame iMainFrame, SSAccountPlan iAccountPlan, final AbstractTableModel iModel) {
        final String lockString = "accountplan"+iAccountPlan.getName()+SSDB.getInstance().getCurrentYear().getId();
        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog( iMainFrame, "accountplanframe.accountplanopen",iAccountPlan.getName());
            return;
        }
        final SSDialog           iDialog = new SSDialog(iMainFrame, bundle.getString("accountplanframe.editcurrent.title"));
        final SSAccountPlanPanel iPanel  = new SSAccountPlanPanel(iMainFrame);

        iPanel.setAccountPlan( new SSAccountPlan(iAccountPlan) );
        iPanel.setShowBase(true);
     //   iPanel.setShowImport(true);


        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSAccountPlan iAccountPlan = iPanel.getAccountPlan();


                SSNewAccountingYear iCurrentYear = SSDB.getInstance().getCurrentYear();
                iCurrentYear.setAccountPlan(iAccountPlan);
                SSDB.getInstance().updateAccountingYear(iCurrentYear);

                if (iModel != null) iModel.fireTableDataChanged();
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
                if(!iPanel.isValid()){
                    SSPostLock.removeLock(lockString);
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "accountplanframe.saveonclose") != JOptionPane.OK_OPTION){
                    SSPostLock.removeLock(lockString);
                    return;
                }

                iSaveAction.actionPerformed(null);
            }
        });

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);
        iDialog.setSize(600,450);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }
}
