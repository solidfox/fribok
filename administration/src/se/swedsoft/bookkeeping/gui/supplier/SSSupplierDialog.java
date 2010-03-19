package se.swedsoft.bookkeeping.gui.supplier;

import se.swedsoft.bookkeeping.calc.math.SSSupplierMath;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.supplier.panel.SSSupplierPanel;
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
import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-aug-31
 * Time: 11:04:42
 */
public class SSSupplierDialog {


    private static Dimension iDialogSize = new Dimension(640, 480);

    private SSSupplierDialog() {
    }

    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, final AbstractTableModel pModel) {
        if(!SSPostLock.applyLock("importsupplier")){
            new SSErrorDialog(iMainFrame, "supplierframe.import.locked");
            return;
        }
        final SSDialog        iDialog = new SSDialog(iMainFrame, SSBundle.getBundle().getString("supplierframe.new.title"));
        final SSSupplierPanel iPanel  = new SSSupplierPanel(iDialog, false);
        SSSupplier iSupplier = new SSSupplier();
        iSupplier.setOutpaymentNumber(null);
        iPanel.setSupplier(iSupplier,true);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSSupplier iSupplier = iPanel.getSupplier();
                if(iSupplier.getOutpaymentNumber() == null)
                    iSupplier.setOutpaymentNumber(SSSupplierMath.getOutpaymentNumber());

                List<SSSupplier> iSuppliers = new LinkedList<SSSupplier>(SSDB.getInstance().getSuppliers());
                for(SSSupplier iTSupplier : iSuppliers){
                    if(iSupplier.equals(iTSupplier)){
                        new SSErrorDialog(iMainFrame, "supplierframe.duplicate", iSupplier.getNumber());
                        SSPostLock.removeLock("importsupplier");
                        return;
                    }

                    if(iSupplier.getOutpaymentNumber().equals(iTSupplier.getOutpaymentNumber())){
                        new SSErrorDialog(iMainFrame, "supplierframe.dupopnr", iSupplier.getOutpaymentNumber());
                        SSPostLock.removeLock("importsupplier");
                        return;
                    }
                }

                SSDB.getInstance().addSupplier(iSupplier);

                if (pModel != null) pModel.fireTableDataChanged();
                SSPostLock.removeLock("importsupplier");
                iDialog.closeDialog();
            }
        };
        iPanel.addOkAction(iSaveAction);

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPostLock.removeLock("importsupplier");
                iDialog.closeDialog();
            }
        });
        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(!iPanel.isValid()) {
                    SSPostLock.removeLock("importsupplier");
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "supplierframe.saveonclose") != JOptionPane.OK_OPTION) {
                    SSPostLock.removeLock("importsupplier");
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
     * @param pSupplier
     * @param pModel
     */
    public static void editDialog(final SSMainFrame iMainFrame, SSSupplier pSupplier, final AbstractTableModel pModel) {
        final String lockString = "supplier"+pSupplier.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "supplierframe.supplieropen", pSupplier.getNumber());
            return;
        }

        final SSDialog        iDialog = new SSDialog(iMainFrame, SSBundle.getBundle().getString("supplierframe.edit.title"));
        final SSSupplierPanel iPanel  = new SSSupplierPanel(iDialog, true);
        final Integer iOriginalOutPaymentNumber = pSupplier.getOutpaymentNumber();
        iPanel.setSupplier(pSupplier,false);
        //iPanel.setEditPanel(true);
        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSSupplier iSupplier = iPanel.getSupplier();
                if(iSupplier.getOutpaymentNumber() == null)
                    iSupplier.setOutpaymentNumber(SSSupplierMath.getOutpaymentNumber());

                if(!iSupplier.getOutpaymentNumber().equals(iOriginalOutPaymentNumber) ){
                    List<SSSupplier> iSuppliers = new LinkedList<SSSupplier>(SSDB.getInstance().getSuppliers());
                    for(SSSupplier iTSupplier : iSuppliers){
                        if(iSupplier.getOutpaymentNumber().equals(iTSupplier.getOutpaymentNumber())){
                            new SSErrorDialog(iMainFrame, "supplierframe.dupopnr", iSupplier.getOutpaymentNumber());
                            SSPostLock.removeLock("importsupplier");
                            return;
                        }
                    }
                }
                SSDB.getInstance().updateSupplier(iSupplier);

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "supplierframe.saveonclose") != JOptionPane.OK_OPTION) {
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
     * @param pModel
     * @param iSupplier
     */
    public static void copyDialog(final SSMainFrame iMainFrame, SSSupplier iSupplier, final AbstractTableModel pModel) {
        final String lockString = "supplier"+iSupplier.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if (SSPostLock.isLocked(lockString)) {
            new SSErrorDialog(iMainFrame, "supplierframe.supplieropen", iSupplier.getNumber());
            return;
        }
         if(!SSPostLock.applyLock("importsupplier")){
            new SSErrorDialog(iMainFrame, "supplierframe.import.locked");
            return;
        }
        final SSDialog        iDialog = new SSDialog(iMainFrame, SSBundle.getBundle().getString("supplierframe.copy.title"));
        final SSSupplierPanel iPanel  = new SSSupplierPanel(iDialog, false);

         SSSupplier iNew = new SSSupplier(iSupplier);

        iPanel.setSupplier(iNew,true);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSSupplier iSupplier = iPanel.getSupplier();
                if(iSupplier.getOutpaymentNumber() == null)
                    iSupplier.setOutpaymentNumber(SSSupplierMath.getOutpaymentNumber());

                List<SSSupplier> iSuppliers = new LinkedList<SSSupplier>(SSDB.getInstance().getSuppliers());
                for(SSSupplier iTSupplier : iSuppliers){
                    if(iSupplier.equals(iTSupplier)){
                        new SSErrorDialog(iMainFrame, "supplierframe.duplicate", iSupplier.getNumber());
                        SSPostLock.removeLock("importsupplier");
                        return;
                    }

                    if(iSupplier.getOutpaymentNumber().equals(iTSupplier.getOutpaymentNumber())){
                        new SSErrorDialog(iMainFrame, "supplierframe.dupopnr", iSupplier.getOutpaymentNumber());
                        SSPostLock.removeLock("importsupplier");
                        return;
                    }
                }

                SSDB.getInstance().addSupplier(iSupplier);

                if (pModel != null) pModel.fireTableDataChanged();
                SSPostLock.removeLock("importsupplier");
                iDialog.closeDialog();
            }
        };
        iPanel.addOkAction(iSaveAction);

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPostLock.removeLock("importsupplier");
                iDialog.closeDialog();
            }
        });
        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(!iPanel.isValid()){
                    SSPostLock.removeLock("importsupplier");
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "supplierframe.saveonclose") != JOptionPane.OK_OPTION) {
                    SSPostLock.removeLock("importsupplier");
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
