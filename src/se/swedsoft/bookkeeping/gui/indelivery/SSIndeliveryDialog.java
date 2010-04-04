package se.swedsoft.bookkeeping.gui.indelivery;

import se.swedsoft.bookkeeping.data.SSIndelivery;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.indelivery.panel.SSIndeliveryPanel;
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

/**
 * User: Andreas Lago
 * Date: 2006-sep-26
 * Time: 11:50:08
 */
public class SSIndeliveryDialog {
    private SSIndeliveryDialog() {
    }

    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, final AbstractTableModel pModel) {
        final SSDialog         iDialog = new SSDialog(iMainFrame,  SSBundle.getBundle().getString("indeliveryframe.new.title"));
        final SSIndeliveryPanel iPanel  = new SSIndeliveryPanel(iDialog);

        SSIndelivery iIndelivery = new SSIndelivery();
        iIndelivery.setNumber(null);
        iPanel.setIndelivery(  iIndelivery );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        iPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSIndelivery iIndelivery = iPanel.getIndelivery();
                SSDB.getInstance().addIndelivery(iIndelivery);
                if(pModel != null) pModel.fireTableDataChanged();

                iDialog.closeDialog();
            }
        });

        iPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDialog.closeDialog();
            }
        });

        iDialog.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {

                if (SSQueryDialog.showDialog(iMainFrame, SSBundle.getBundle(), "indeliveryframe.saveonclose") != JOptionPane.OK_OPTION){
                    return;
                }
                SSIndelivery iIndelivery = iPanel.getIndelivery();
                SSDB.getInstance().addIndelivery(iIndelivery);

                if(pModel != null) pModel.fireTableDataChanged();

                iDialog.closeDialog();
            }
        });
        iDialog.setSize    (800, 600);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.showDialog();
    }

    /**
     *
     * @param iMainFrame
     * @param iIndelivery
     * @param pModel
     */
    public static void editDialog(final SSMainFrame iMainFrame, SSIndelivery iIndelivery, final AbstractTableModel pModel) {
        final String lockString = "indelivery" + iIndelivery.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "indeliveryframe.indeliveryopen", iIndelivery.getNumber());
            return;
        }

        final SSDialog         iDialog = new SSDialog(iMainFrame,  SSBundle.getBundle().getString("indeliveryframe.edit.title"));
        final SSIndeliveryPanel iPanel  = new SSIndeliveryPanel(iDialog);

        final SSIndelivery iOriginal = iIndelivery;

        iPanel.setIndelivery(  new SSIndelivery(iIndelivery) );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        iPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSIndelivery iIndelivery = iPanel.getIndelivery();

                SSDB.getInstance().updateIndelivery(iIndelivery);

                if(pModel != null) pModel.fireTableDataChanged();
                SSPostLock.removeLock(lockString);
                iDialog.closeDialog();
            }
        });

        iPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPostLock.removeLock(lockString);
                iDialog.closeDialog();
            }
        });

        iDialog.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {

                if (SSQueryDialog.showDialog(iMainFrame, SSBundle.getBundle(), "indeliveryframe.saveonclose") != JOptionPane.OK_OPTION){
                    SSPostLock.removeLock(lockString);
                    return;
                }
                SSIndelivery iIndelivery = iPanel.getIndelivery();

                SSDB.getInstance().updateIndelivery(iIndelivery);

                if(pModel != null) pModel.fireTableDataChanged();
                SSPostLock.removeLock(lockString);
                iDialog.closeDialog();
            }
        });
        iDialog.setSize    (800, 600);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.showDialog();
    }


    /**
     *
     * @param iMainFrame
     * @param iCopyFrom
     * @param pModel
     */
    public static void copyDialog(final SSMainFrame iMainFrame, SSIndelivery iCopyFrom, final AbstractTableModel pModel) {
        final String lockString = "indelivery" + iCopyFrom.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if (SSPostLock.isLocked(lockString)) {
            new SSErrorDialog(iMainFrame, "indeliveryframe.indeliveryopen", iCopyFrom.getNumber());
            return;
        }
        final SSDialog          iDialog = new SSDialog(iMainFrame,  SSBundle.getBundle().getString("indeliveryframe.copy.title"));
        final SSIndeliveryPanel iPanel  = new SSIndeliveryPanel(iDialog);

        SSIndelivery iIndelivery = new SSIndelivery(iCopyFrom);

        iIndelivery.setNumber(null);

        iPanel.setIndelivery(  iIndelivery );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        iPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSIndelivery iIndelivery = iPanel.getIndelivery();
                SSDB.getInstance().addIndelivery(iIndelivery);

                if(pModel != null) pModel.fireTableDataChanged();

                iDialog.closeDialog();
            }
        });

        iPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDialog.closeDialog();
            }
        });

        iDialog.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {

                if (SSQueryDialog.showDialog(iMainFrame, SSBundle.getBundle(), "indeliveryframe.saveonclose") != JOptionPane.OK_OPTION){
                    return;
                }

                SSIndelivery iIndelivery = iPanel.getIndelivery();
                SSDB.getInstance().addIndelivery(iIndelivery);

                if(pModel != null) pModel.fireTableDataChanged();

                iDialog.closeDialog();
            }
        });
        iDialog.setSize    (800, 600);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.showDialog();
    }
}
