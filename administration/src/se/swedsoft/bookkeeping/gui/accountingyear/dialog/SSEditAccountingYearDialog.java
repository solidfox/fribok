/*
 * 2006-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.accountingyear.dialog;

import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.accountingyear.panel.SSAccountingYearPanel;

import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

public class SSEditAccountingYearDialog {

    private static ResourceBundle bundle = SSBundle.getBundle();


    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void showDialog(final SSMainFrame iMainFrame, final SSNewAccountingYear pAccountingYear, final SSDefaultTableModel<SSNewAccountingYear> pModel) {
        if(SSPostLock.isLocked(pAccountingYear.getId())){
            new SSErrorDialog( iMainFrame, "postlock.accountingyear");
            return;
        }
        SSPostLock.applyLock(pAccountingYear.getId());

        final SSDialog              iDialog = new SSDialog(iMainFrame, bundle.getString("accountingyearframe.edit.title"));
        final SSAccountingYearPanel iPanel = new SSAccountingYearPanel();

        iPanel.setShowAccountPlanPanel(false);
        iPanel.setAccountingYear( pAccountingYear );

        iPanel.addOkAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewAccountingYear iYear = iPanel.getAccountingYear();
                iYear.setId(pAccountingYear.getId());

                SSDB.getInstance().updateAccountingYear(iYear);

                SSPostLock.removeLock(pAccountingYear.getId());

                iDialog.closeDialog();
            }
        });

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPostLock.removeLock(pAccountingYear.getId());
                iDialog.closeDialog();
            }
        });

        iDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                SSPostLock.removeLock(pAccountingYear.getId());
            }
        });
        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);
        iDialog.pack();
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }



}
