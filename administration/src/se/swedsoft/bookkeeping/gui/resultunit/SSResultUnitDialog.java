package se.swedsoft.bookkeeping.gui.resultunit;

import se.swedsoft.bookkeeping.data.SSNewResultUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.resultunit.panel.SSResultUnitPanel;
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

/**
 * User: Andreas Lago
 * Date: 2006-okt-11
 * Time: 09:56:33
 */
public class SSResultUnitDialog {

    /**
     *
     * @param iMainFrame
     * @param model
     */
    public static void newDialog(final SSMainFrame iMainFrame, final AbstractTableModel model) {
        final SSDialog          iDialog = new SSDialog(iMainFrame, SSBundle.getBundle().getString("resultunitframe.new.title"));
        final SSResultUnitPanel iPanel  = new SSResultUnitPanel(false);

        iPanel.setResultUnit(new SSNewResultUnit());

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewResultUnit iResultUnit = iPanel.getResultUnit();

                List<SSNewResultUnit> iResultUnits = SSDB.getInstance().getResultUnits();
                for (SSNewResultUnit pResultUnit : iResultUnits) {
                    if (iResultUnit.getNumber().equals(pResultUnit.getNumber())) {
                        new SSErrorDialog(iMainFrame, "resultunitframe.duplicate",iResultUnit.getNumber());
                        return;
                    }
                }

                SSDB.getInstance().addResultUnit(iResultUnit);

                model.fireTableDataChanged();

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "resultunitframe.saveonclose") != JOptionPane.OK_OPTION) {
                    return;
                }
                iSaveAction.actionPerformed(null);
            }
        });
        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);
        iDialog.pack();
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }

    /**
     *
     * @param iMainFrame
     * @param pResultUnit
     * @param model
     */
    public static void editDialog(final SSMainFrame iMainFrame, SSNewResultUnit pResultUnit, final AbstractTableModel model) {
        final String lockString = "resultunit"+pResultUnit.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "resultunitframe.resultunitopen", pResultUnit.getNumber());
            return;
        }
        final SSDialog          iDialog = new SSDialog(iMainFrame, SSBundle.getBundle().getString("resultunitframe.edit.title"));
        final SSResultUnitPanel iPanel  = new SSResultUnitPanel(true);

        iPanel.setResultUnit(pResultUnit);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                SSNewResultUnit iResultUnit = iPanel.getResultUnit();

                SSDB.getInstance().updateResultUnit(iResultUnit);
                
                model.fireTableDataChanged();
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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "resultunitframe.saveonclose") != JOptionPane.OK_OPTION) {
                    SSPostLock.removeLock(lockString);
                    return;
                }

                iSaveAction.actionPerformed(null);
            }
        });

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);
        iDialog.pack();
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }


}
