package se.swedsoft.bookkeeping.gui.inpayment;

import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.invoice.SSInvoiceFrame;
import se.swedsoft.bookkeeping.gui.inpayment.panel.SSInpaymentPanel;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.data.SSInpayment;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.data.system.SSDBConfig;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.server.UID;
import java.util.*;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-aug-01
 * Time: 09:23:40
 */
public class SSInpaymentDialog {
    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void editDialog(final SSMainFrame iMainFrame, final SSInpayment pInpayment, final SSDefaultTableModel<SSInpayment> pModel) {
        final String lockString = "inpayment" + pInpayment.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog( iMainFrame, "inpaymentframe.inpaymentopen",pInpayment.getNumber());
            return;
        }

        final SSDialog       iDialog = new SSDialog(iMainFrame, SSBundle.getBundle().getString("inpaymentframe.edit.title"));
        final SSInpaymentPanel iPanel  = new SSInpaymentPanel(iDialog);

        iPanel.setInpayment( new SSInpayment(pInpayment));

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSInpayment iInpayment = iPanel.getInpayment();

                SSDB.getInstance().updateInpayment(iInpayment);

                if (pModel != null) pModel.fireTableDataChanged();

                if (SSInvoiceFrame.getInstance() != null) {
                    SSInvoiceFrame.getInstance().updateFrame();
                }
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
                if (SSQueryDialog.showDialog(iMainFrame, SSBundle.getBundle(), "inpaymentframe.saveonclose") != JOptionPane.OK_OPTION){
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
    public static void newDialog(final SSMainFrame iMainFrame, final SSDefaultTableModel<SSInpayment> pModel) {
        final SSDialog         iDialog = new SSDialog(iMainFrame, SSBundle.getBundle().getString("inpaymentframe.new.title"));
        final SSInpaymentPanel iPanel  = new SSInpaymentPanel(iDialog);

        SSInpayment iInpayment = new SSInpayment();
        iPanel.setInpayment( iInpayment);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSInpayment iInpayment = iPanel.getInpayment();
                SSDB.getInstance().addInpayment(iInpayment);
                SSInvoiceFrame.fireTableDataChanged();

                if (pModel != null) pModel.fireTableDataChanged();

                if (SSInvoiceFrame.getInstance() != null) {
                    SSInvoiceFrame.getInstance().updateFrame();
                }

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
                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "inpaymentframe.saveonclose") != JOptionPane.OK_OPTION){
                    return;
                }

                iSaveAction.actionPerformed(null);
            }
        });

        iDialog.setSize    (800, 600);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();

    }

    /**
     *
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, SSInpayment iInpayment , final SSDefaultTableModel<SSInpayment> pModel) {
        final SSDialog         iDialog = new SSDialog(iMainFrame, SSBundle.getBundle().getString("inpaymentframe.new.title"));
        final SSInpaymentPanel iPanel  = new SSInpaymentPanel(iDialog);

        iPanel.setInpayment( iInpayment);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSInpayment iInpayment = iPanel.getInpayment();
                SSDB.getInstance().addInpayment(iInpayment);

                SSInvoiceFrame.fireTableDataChanged();

                if (SSInvoiceFrame.getInstance() != null) {
                    SSInvoiceFrame.getInstance().updateFrame();
                }

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
                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "inpaymentframe.saveonclose") != JOptionPane.OK_OPTION){
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
