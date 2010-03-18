package se.swedsoft.bookkeeping.gui.supplierinvoice;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.supplierinvoice.panel.SSSupplierInvoicePanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

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
 * Date: 2006-mar-21
 * Time: 10:57:02
 */
public class SSSupplierInvoiceDialog {


    private static ResourceBundle bundle = SSBundle.getBundle();



    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, final SSTableModel<SSSupplierInvoice> pModel) {
        final SSDialog               iDialog = new SSDialog(iMainFrame, SSSupplierInvoiceDialog.bundle.getString("supplierinvoiceframe.new.title"));
        final SSSupplierInvoicePanel iPanel  = new SSSupplierInvoicePanel(iDialog);

        SSSupplierInvoice iSupplierInvoice = new SSSupplierInvoice();
        iSupplierInvoice.setNumber(null);
        iPanel.setSupplierInvoice( iSupplierInvoice,true);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSSupplierInvoice iSupplierInvoice = iPanel.getSupplierInvoice();
                SSDB.getInstance().addSupplierInvoice(iSupplierInvoice);

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "supplierinvoiceframe.saveonclose") != JOptionPane.OK_OPTION){
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
     * @param iSupplierInvoice
     * @param pModel
     */
    public static void editDialog(final SSMainFrame iMainFrame, SSSupplierInvoice iSupplierInvoice, final SSTableModel<SSSupplierInvoice> pModel) {
        final String lockString = "supplierinvoice" + iSupplierInvoice.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if(!SSPostLock.applyLock(lockString)){
            new SSErrorDialog(iMainFrame, "supplierinvoiceframe.supplierinvoiceopen", iSupplierInvoice.getNumber());
            return;
        }
        final SSDialog               iDialog = new SSDialog(iMainFrame, SSSupplierInvoiceDialog.bundle.getString("supplierinvoiceframe.edit.title"));
        final SSSupplierInvoicePanel iPanel  = new SSSupplierInvoicePanel(iDialog);

        iPanel.setSupplierInvoice( new SSSupplierInvoice(iSupplierInvoice),false);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSSupplierInvoice iSupplierInvoice = iPanel.getSupplierInvoice();

                SSDB.getInstance().updateSupplierInvoice(iSupplierInvoice);

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "supplierinvoiceframe.saveonclose") != JOptionPane.OK_OPTION) {
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

    /**
     *
     * @param iMainFrame
     * @param iCopyFrom
     * @param pModel
     */
    public static void copyDialog(final SSMainFrame iMainFrame, SSSupplierInvoice iCopyFrom, final AbstractTableModel pModel) {
        final String lockString = "supplierinvoice" + iCopyFrom.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if(SSPostLock.isLocked(lockString)){
            new SSErrorDialog(iMainFrame, "supplierinvoiceframe.supplierinvoiceopen", iCopyFrom.getNumber());
            return;
        }
        final SSDialog               iDialog = new SSDialog(iMainFrame, SSSupplierInvoiceDialog.bundle.getString("supplierinvoiceframe.copy.title"));
        final SSSupplierInvoicePanel iPanel  = new SSSupplierInvoicePanel(iDialog);

        SSSupplierInvoice iNew = new SSSupplierInvoice(iCopyFrom);

        iNew.setNumber(null);
        iNew.setDate(iNew.getLastDate());
        iNew.setEntered(false);
        iNew.setBGCEntered(false);

        for(SSSupplier iSupplier : SSDB.getInstance().getSuppliers()) {
            if (iCopyFrom.getSupplierNr().equals(iSupplier.getNumber())) {
                iNew.setPaymentTerm(iSupplier.getPaymentTerm());
            }
        }  
        iNew.setDueDate();

        iPanel.setSupplierInvoice( iNew,false );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSSupplierInvoice iSupplierInvoice = iPanel.getSupplierInvoice();
                SSDB.getInstance().addSupplierInvoice(iSupplierInvoice);

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
                if(! iPanel.isValid() ){
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "supplierinvoiceframe.saveonclose") != JOptionPane.OK_OPTION) {
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
     * @param iInvoice
     * @param iOrders
     * @param iModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, SSSupplierInvoice iInvoice, final List<SSPurchaseOrder> iOrders, final SSTableModel iModel) {
        final String lockString = "purchaseordertosupplierinvoice"+SSDB.getInstance().getCurrentCompany().getId();
        if(!SSPostLock.applyLock(lockString)){
            new SSErrorDialog(iMainFrame, "supplierinvoiceframe.purchaseordertosupplierinvoice");
            return;
        }
        final SSDialog               iDialog = new SSDialog(iMainFrame, SSSupplierInvoiceDialog.bundle.getString("supplierinvoiceframe.new.title"));
        final SSSupplierInvoicePanel iPanel  = new SSSupplierInvoicePanel(iDialog);

        SSSupplierInvoice iSupplierInvoice = new SSSupplierInvoice(iInvoice);

        for (SSSupplierInvoiceRow iRow : iSupplierInvoice.getRows()) {
            if (iRow.getProductNr() != null) {
                SSProduct iProduct = iRow.getProduct(SSDB.getInstance().getProducts());

                iRow.setUnitFreight(iProduct == null ? null : iProduct.getUnitFreight());
                iRow.setProject(iProduct == null ? null : iProduct.getProject(iProduct.getProjectNr()));
                iRow.setResultUnit(iProduct == null ? null : iProduct.getResultUnit(iProduct.getResultUnitNr()));
            }
        }
        iSupplierInvoice.setNumber(null);

        iPanel.setSupplierInvoice( iSupplierInvoice,true );

        iPanel.setOrderNumbers(iOrders);
        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSSupplierInvoice iSupplierInvoice = iPanel.getSupplierInvoice();

                SSDB.getInstance().addSupplierInvoice(iSupplierInvoice);


                for (SSPurchaseOrder iPurchaseOrder : iOrders) {
                    // Set the sales for the selected order to the new one
                    iPurchaseOrder.setInvoice(iSupplierInvoice);
                    SSDB.getInstance().updatePurchaseOrder(iPurchaseOrder);
                }

                if (iModel != null) iModel.fireTableDataChanged();

                iPanel.dispose();
                SSPostLock.removeLock(lockString);
                iDialog.closeDialog();
            }
        };
        iPanel.addOkAction(iSaveAction);

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iPanel.dispose();
                SSPostLock.removeLock(lockString);
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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "supplierinvoiceframe.saveonclose") != JOptionPane.OK_OPTION){
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
