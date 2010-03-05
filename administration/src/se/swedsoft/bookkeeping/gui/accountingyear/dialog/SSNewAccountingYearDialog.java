/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.accountingyear.dialog;

import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSDBConfig;
import se.swedsoft.bookkeeping.data.system.SSYearLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.accountingyear.panel.SSAccountingYearPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.frame.SSFrameManager;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class SSNewAccountingYearDialog {

    private static ResourceBundle bundle = SSBundle.getBundle();


    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void showDialog(final SSMainFrame iMainFrame, final SSDefaultTableModel<SSNewAccountingYear> pModel) {

        final SSDialog              iDialog = new SSDialog(iMainFrame, bundle.getString("accountingyearframe.new.title"));
        final SSAccountingYearPanel iPanel  = new SSAccountingYearPanel();

        iPanel.setAccountingYear( new SSNewAccountingYear() );
        iPanel.setYearFromAndTo();


        iPanel.addOkAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewAccountingYear iAccountingYear = iPanel.getAccountingYear();
                SSDB.getInstance().addAccountingYear(iAccountingYear);

                int iResponce = SSQueryDialog.showDialog(iMainFrame, SSBundle.getBundle(), "accountingyearframe.replaceyear", iAccountingYear.toRenderString());
                if(iResponce == JOptionPane.YES_OPTION){

                    SSDB.getInstance().setCurrentYear(iAccountingYear);
                    SSDB.getInstance().initYear(true);
                    SSYearLock.applyLock(iAccountingYear);
                    SSDBConfig.setYearId(SSDB.getInstance().getCurrentCompany().getId(),iAccountingYear.getId());
                    // Close all year related frames
                    SSFrameManager.getInstance().close();
                }

                iDialog.closeDialog();

            }
        });

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDialog.closeDialog();
            }
        });
        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);
        iDialog.pack();
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }






}
