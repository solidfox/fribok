package se.swedsoft.bookkeeping.gui.outpayment;

import se.swedsoft.bookkeeping.data.SSOutpayment;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.outpayment.panel.SSOutpaymentPanel;
import se.swedsoft.bookkeeping.gui.supplierinvoice.SSSupplierInvoiceFrame;
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
 * Date: 2006-mar-21
 * Time: 10:57:02
 */
public class SSOutpaymentDialog {


    private static ResourceBundle bundle = SSBundle.getBundle();

    private SSOutpaymentDialog() {
    }


    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, final AbstractTableModel pModel) {
        final SSDialog          iDialog = new SSDialog(iMainFrame, SSOutpaymentDialog.bundle.getString("outpaymentframe.new.title"));
        final SSOutpaymentPanel iPanel  = new SSOutpaymentPanel(iDialog);

        SSOutpayment iOutpayment = new SSOutpayment();
        iOutpayment.setNumber(null);
        iPanel.setOutpayment(iOutpayment);
        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSOutpayment iOutpayment = iPanel.getOutpayment();

                SSDB.getInstance().addOutpayment(iOutpayment);

                if (pModel != null) pModel.fireTableDataChanged();
                if (SSSupplierInvoiceFrame.getInstance() != null) {
                    SSSupplierInvoiceFrame.getInstance().updateFrame();
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
                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "outpaymentframe.saveonclose") != JOptionPane.OK_OPTION) {
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
     * @param iMainFrame
     * @param iOutpayment
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, SSOutpayment iOutpayment ,final AbstractTableModel pModel) {
        final SSDialog          iDialog = new SSDialog(iMainFrame, SSOutpaymentDialog.bundle.getString("outpaymentframe.new.title"));
        final SSOutpaymentPanel iPanel  = new SSOutpaymentPanel(iDialog);

        iOutpayment.setNumber(null);
        iPanel.setOutpayment( iOutpayment);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSOutpayment iOutpayment = iPanel.getOutpayment();

                SSDB.getInstance().addOutpayment(iOutpayment);

                if (pModel != null) pModel.fireTableDataChanged();
                if (SSSupplierInvoiceFrame.getInstance() != null) {
                    SSSupplierInvoiceFrame.getInstance().updateFrame();
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
                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "outpaymentframe.saveonclose") != JOptionPane.OK_OPTION){
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
     * @param iMainFrame
     * @param iSelected
     * @param pModel
     */
    public static void editDialog(final SSMainFrame iMainFrame, SSOutpayment iSelected, final AbstractTableModel pModel) {
        final String lockString = "outpayment" + iSelected.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if(!SSPostLock.applyLock(lockString)){
            new SSErrorDialog( iMainFrame, "outpaymentframe.outpaymentopen",iSelected.getNumber());
            return;
        }
        final SSDialog          iDialog = new SSDialog(iMainFrame, SSOutpaymentDialog.bundle.getString("outpaymentframe.edit.title"));
        final SSOutpaymentPanel iPanel  = new SSOutpaymentPanel(iDialog);

        iPanel.setOutpayment( new SSOutpayment(iSelected));

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSOutpayment iOutpayment = iPanel.getOutpayment();

                SSDB.getInstance().updateOutpayment(iOutpayment);

                if (pModel != null) pModel.fireTableDataChanged();

                SSPostLock.removeLock(lockString);

                if (SSSupplierInvoiceFrame.getInstance() != null) {
                    SSSupplierInvoiceFrame.getInstance().updateFrame();
                }
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
                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "outpaymentframe.saveonclose") != JOptionPane.OK_OPTION) {
                    SSPostLock.removeLock(lockString);
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
