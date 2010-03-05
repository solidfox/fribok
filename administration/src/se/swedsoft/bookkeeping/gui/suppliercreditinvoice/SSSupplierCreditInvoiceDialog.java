package se.swedsoft.bookkeeping.gui.suppliercreditinvoice;

import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSSupplierCreditInvoice;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.SSSupplierInvoiceRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.suppliercreditinvoice.dialog.SSSelectSupplierInvoiceDialog;
import se.swedsoft.bookkeeping.gui.suppliercreditinvoice.panel.SSSupplierCreditInvoicePanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:57:02
 */
public class SSSupplierCreditInvoiceDialog {


    private static ResourceBundle bundle = SSBundle.getBundle();


    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, final SSTableModel<SSSupplierCreditInvoice> pModel) {
        final SSDialog                    iDialog = new SSDialog(iMainFrame, SSSupplierCreditInvoiceDialog.bundle.getString("suppliercreditinvoiceframe.new.title"));


        SSSupplierInvoice iSupplierInvoice = SSSelectSupplierInvoiceDialog.showDialog(iMainFrame);
        final SSSupplierCreditInvoicePanel iPanel  = new SSSupplierCreditInvoicePanel(iDialog);
        if( iSupplierInvoice == null) {
            new SSErrorDialog(iMainFrame, "suppliercreditinvoiceframe.suppliercreditinvoicenoinvoice");
            return;
        } else if (iSupplierInvoice.getNumber() == -1) {
            return;
        }

        SSSupplierCreditInvoice iSupplierCreditInvoice = new SSSupplierCreditInvoice(iSupplierInvoice);
        iSupplierCreditInvoice.setEntered(false);
        iSupplierCreditInvoice.setNumber(null);
        iPanel.setCreditSupplierInvoice( iSupplierCreditInvoice);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSSupplierCreditInvoice iSupplierCreditInvoice = iPanel.getSupplierCreditInvoice();
                SSDB.getInstance().addSupplierCreditInvoice(iSupplierCreditInvoice);

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "suppliercreditinvoiceframe.saveonclose") != JOptionPane.OK_OPTION) {
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
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, SSSupplierInvoice iSupplierInvoice, final SSTableModel<SSSupplierCreditInvoice> pModel) {
        final SSDialog iDialog = new SSDialog(iMainFrame, SSSupplierCreditInvoiceDialog.bundle.getString("suppliercreditinvoiceframe.new.title"));
        final SSSupplierCreditInvoicePanel iPanel = new SSSupplierCreditInvoicePanel(iDialog);

        SSSupplierCreditInvoice iSupplierCreditInvoice = new SSSupplierCreditInvoice(iSupplierInvoice);
        iSupplierCreditInvoice.setEntered(false);
        iSupplierCreditInvoice.setNumber(null);
        for (SSSupplierInvoiceRow iRow : iSupplierInvoice.getRows()) {
            if (iRow.getProductNr() != null) {
                SSProduct iProduct = iRow.getProduct(SSDB.getInstance().getProducts());

                iRow.setProject(iProduct == null ? null : iProduct.getProject(iProduct.getProjectNr()));
                iRow.setResultUnit(iProduct == null ? null : iProduct.getResultUnit(iProduct.getResultUnitNr()));
            }
        }
        iPanel.setCreditSupplierInvoice(iSupplierCreditInvoice);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSSupplierCreditInvoice iSupplierCreditInvoice = iPanel.getSupplierCreditInvoice();
                SSDB.getInstance().addSupplierCreditInvoice(iSupplierCreditInvoice);

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
                if (! iPanel.isValid()) {
                    return;
                }

                if (SSQueryDialog.showDialog(iMainFrame, SSBundle.getBundle(), "suppliercreditinvoiceframe.saveonclose") != JOptionPane.OK_OPTION){
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
    public static void editDialog(final SSMainFrame iMainFrame, SSSupplierCreditInvoice iSupplierCreditInvoice, final SSTableModel<SSSupplierCreditInvoice> pModel) {
        final String lockString = "suppliercreditinvoice" + iSupplierCreditInvoice.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "suppliercreditinvoiceframe.suppliercreditinvoiceopen", iSupplierCreditInvoice.getNumber());
            return;
        }
        final SSDialog                     iDialog = new SSDialog(iMainFrame, SSSupplierCreditInvoiceDialog.bundle.getString("suppliercreditinvoiceframe.edit.title"));
        final SSSupplierCreditInvoicePanel iPanel  = new SSSupplierCreditInvoicePanel(iDialog);

        iPanel.setCreditSupplierInvoice( iSupplierCreditInvoice);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSSupplierCreditInvoice iSupplierCreditInvoice = iPanel.getSupplierCreditInvoice();

                SSDB.getInstance().updateSupplierCreditInvoice(iSupplierCreditInvoice);

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
                if(! iPanel.isValid() ){
                    return;
                }

                if (SSQueryDialog.showDialog(iMainFrame, SSBundle.getBundle(), "suppliercreditinvoiceframe.saveonclose") != JOptionPane.OK_OPTION){
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
     * @param pModel
     */
    public static void copyDialog(final SSMainFrame iMainFrame, SSSupplierCreditInvoice iSupplierCreditInvoice, final SSTableModel<SSSupplierCreditInvoice> pModel) {
        final String lockString = "suppliercreditinvoice" + iSupplierCreditInvoice.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if (SSPostLock.isLocked(lockString)) {
            new SSErrorDialog(iMainFrame, "suppliercreditinvoiceframe.suppliercreditinvoiceopen", iSupplierCreditInvoice.getNumber());
            return;
        }
        final SSDialog                    iDialog = new SSDialog(iMainFrame, SSSupplierCreditInvoiceDialog.bundle.getString("suppliercreditinvoiceframe.copy.title"));
        final SSSupplierCreditInvoicePanel iPanel  = new SSSupplierCreditInvoicePanel(iDialog);

        SSSupplierCreditInvoice iNew = new SSSupplierCreditInvoice(iSupplierCreditInvoice);

        iNew.setNumber(null);
        iNew.setEntered(false);
        iNew.setDate(new Date() );
        if (iSupplierCreditInvoice.getCrediting() == null) {
            new SSErrorDialog(iMainFrame, "suppliercreditinvoiceframe.suppliercreditinvoicenoinvoice");
            return;
        }
        iPanel.setCreditSupplierInvoice( iNew);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSSupplierCreditInvoice iSupplierCreditInvoice = iPanel.getSupplierCreditInvoice();
                SSDB.getInstance().addSupplierCreditInvoice(iSupplierCreditInvoice);

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "suppliercreditinvoiceframe.saveonclose") != JOptionPane.OK_OPTION){
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
