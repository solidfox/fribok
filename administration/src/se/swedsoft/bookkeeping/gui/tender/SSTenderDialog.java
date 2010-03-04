package se.swedsoft.bookkeeping.gui.tender;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.inpayment.SSInpaymentFrame;
import se.swedsoft.bookkeeping.gui.invoice.SSInvoiceFrame;
import se.swedsoft.bookkeeping.gui.tender.panel.SSTenderPanel;
import se.swedsoft.bookkeeping.gui.tender.util.SSTenderTableModel;
import se.swedsoft.bookkeeping.data.SSTender;
import se.swedsoft.bookkeeping.data.SSInpayment;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.calc.math.SSTenderMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.table.AbstractTableModel;
import javax.swing.*;
import java.util.ResourceBundle;
import java.util.Date;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * User: Andreas Lago
 * Date: 2006-sep-21
 * Time: 11:56:22
 */
public class SSTenderDialog {

    private static ResourceBundle bundle = SSBundle.getBundle();

    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void copyDialog(final SSMainFrame iMainFrame, SSTender iTender, final SSTenderTableModel pModel) {
        final String lockString = "tender" + iTender.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if(SSPostLock.isLocked(lockString)){
            new SSErrorDialog( iMainFrame, "tenderframe.tenderopen",iTender.getNumber());
            return;
        }
        final SSDialog      iDialog = new SSDialog(iMainFrame, bundle.getString("tenderframe.copy.title"));
        final SSTenderPanel iPanel  = new SSTenderPanel(iDialog);
        SSTender iNew = new SSTender(iTender);

        iNew.setNumber(null);
        iNew.setDate(new Date() );
        iNew.setOrder(null);
        iNew.setPrinted(false);

        iPanel.setTender( iNew );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSTender iTender = iPanel.getTender();

                SSDB.getInstance().addTender(iTender);

                if (iPanel.doSaveCustomerAndProducts()) SSTenderMath.addCustomerAndProducts(iTender);

                if (pModel != null) pModel.fireTableDataChanged();
                iPanel.dispose();
                iDialog.closeDialog();
            }
        };
        iPanel.addOkAction(iSaveAction);

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iPanel.dispose();
                iDialog.closeDialog();
            }
        });

        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(! iPanel.isValid() ) {
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "tenderframe.saveonclose") != JOptionPane.OK_OPTION){
                    return;
                }

                iSaveAction.actionPerformed(null);
            }
        });
        iDialog.setSize(800, 600);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }

    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void editDialog(final SSMainFrame iMainFrame, SSTender iTender, final SSTenderTableModel pModel) {
        final String lockString = "tender" + iTender.getNumber()+SSDB.getInstance().getCurrentCompany().getId();

        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog( iMainFrame, "tenderframe.tenderopen",iTender.getNumber());
            return;
        }
        final SSDialog      iDialog = new SSDialog(iMainFrame, bundle.getString("tenderframe.edit.title"));
        final SSTenderPanel iPanel  = new SSTenderPanel(iDialog);
        iPanel.setTender( new SSTender(iTender) );
        iPanel.setSavecustomerandproductsSelected(false);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSTender iTender = iPanel.getTender();

                SSDB.getInstance().updateTender(iTender);
                if (iPanel.doSaveCustomerAndProducts()) SSTenderMath.addCustomerAndProducts(iTender);
                if (pModel != null) pModel.fireTableDataChanged();
                SSPostLock.removeLock(lockString);

                iPanel.dispose();
                iDialog.closeDialog();
            }
        };
        iPanel.addOkAction(iSaveAction);

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPostLock.removeLock(lockString);
                iPanel.dispose();
                iDialog.closeDialog();
            }
        });
        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(! iPanel.isValid() ) {
                    SSPostLock.removeLock(lockString);
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "tenderframe.saveonclose") != JOptionPane.OK_OPTION) {
                    SSPostLock.removeLock(lockString);
                    return;
                }

                iSaveAction.actionPerformed(null);
            }
        });
        iDialog.setSize(800, 600);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }

    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, final SSTenderTableModel pModel) {
        final SSDialog      iDialog = new SSDialog(iMainFrame, bundle.getString("tenderframe.new.title"));
        final SSTenderPanel iPanel  = new SSTenderPanel(iDialog);
        iPanel.setTender( SSTenderMath.newTender() );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSTender iTender = iPanel.getTender();

                SSDB.getInstance().addTender(iTender);

                if (iPanel.doSaveCustomerAndProducts()) SSTenderMath.addCustomerAndProducts(iTender);

                iPanel.dispose();
                iDialog.closeDialog();
            }
        };
        iPanel.addOkAction(iSaveAction);

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iPanel.dispose();
                iDialog.closeDialog();
            }
        });
        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(! iPanel.isValid() ){
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "tenderframe.saveonclose") != JOptionPane.OK_OPTION){
                    return;
                }
                iSaveAction.actionPerformed(null);
            }
        });
        iDialog.setSize    (800, 600);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }
}
