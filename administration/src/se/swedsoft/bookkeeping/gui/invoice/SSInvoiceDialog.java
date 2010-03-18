package se.swedsoft.bookkeeping.gui.invoice;

import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSOrder;
import se.swedsoft.bookkeeping.data.common.SSInvoiceType;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.invoice.dialog.SSInvoiceTypeDialog;
import se.swedsoft.bookkeeping.gui.invoice.panel.SSInvoicePanel;
import se.swedsoft.bookkeeping.gui.invoice.util.SSInvoiceTableModel;
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
import java.util.Date;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-jul-31
 * Time: 15:01:54
 */
public class SSInvoiceDialog {

    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, final SSInvoiceTableModel pModel) {
        final SSDialog       iDialog = new SSDialog(iMainFrame,  SSBundle.getBundle().getString("invoiceframe.new.title"));
        SSInvoiceType iInvoiceType = SSInvoiceTypeDialog.showDialog(iMainFrame);

        if(iInvoiceType == null) return;

        final SSInvoicePanel iPanel  = new SSInvoicePanel(iDialog);
        iPanel.setInvoice( new SSInvoice(iInvoiceType) );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSInvoice iInvoice = iPanel.getInvoice();
                SSDB.getInstance().addInvoice(iInvoice);


                if (iPanel.doSaveCustomerAndProducts()) SSInvoiceMath.addCustomerAndProducts(iInvoice);

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "invoiceframe.saveonclose") != JOptionPane.OK_OPTION) {
                    return;
                }

                iSaveAction.actionPerformed(null);
            }
        });
        iDialog.setSize    (800, 600);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.showDialog();
    }

    /**
     *
     * @param iMainFrame
     * @param iInvoice
     * @param pModel
     */
    public static void editDialog(final SSMainFrame iMainFrame, SSInvoice iInvoice, final SSInvoiceTableModel pModel) {
        final String lockString = "invoice" + iInvoice.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if(!SSPostLock.applyLock(lockString)){
            new SSErrorDialog( iMainFrame, "invoiceframe.invoiceopen",iInvoice.getNumber());
            return;
        }
        final SSDialog       iDialog = new SSDialog(iMainFrame, SSBundle.getBundle().getString("invoiceframe.edit.title"));
        final SSInvoicePanel iPanel  = new SSInvoicePanel(iDialog);
        iPanel.setInvoice( new SSInvoice(iInvoice) );
        iPanel.setSavecustomerandproductsSelected(false);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSInvoice iInvoice = iPanel.getInvoice();

                SSDB.getInstance().updateInvoice(iInvoice);
                if (iPanel.doSaveCustomerAndProducts()) SSInvoiceMath.addCustomerAndProducts(iInvoice);

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
                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "invoiceframe.saveonclose") != JOptionPane.OK_OPTION) {
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
     * @param iInvoice
     * @param pModel
     */
    public static void copyDialog(final SSMainFrame iMainFrame, SSInvoice iInvoice, final SSInvoiceTableModel pModel) {
        final String lockString = "invoice" + iInvoice.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if(SSPostLock.isLocked(lockString)){
            new SSErrorDialog( iMainFrame, "invoiceframe.invoiceopen",iInvoice.getNumber());
            return;
        }
        final SSDialog       iDialog = new SSDialog(iMainFrame, SSBundle.getBundle().getString("invoiceframe.copy.title"));
        final SSInvoicePanel iPanel  = new SSInvoicePanel(iDialog);
        SSInvoice iNew = new SSInvoice(iInvoice);

        iNew.setNumber(null);
        iNew.setDate(new Date() );
        iNew.setDueDate() ;
        iNew.setEntered(false);
        iNew.setPrinted(false);
        iNew.setInterestInvoiced(false);
        iNew.setOCRNumber(null);
        iNew.setNumRemainders(0);

        iPanel.setInvoice( iNew );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSInvoice iInvoice = iPanel.getInvoice();
                SSDB.getInstance().addInvoice(iInvoice);

                if (iPanel.doSaveCustomerAndProducts()) SSInvoiceMath.addCustomerAndProducts(iInvoice);

                if (pModel != null) pModel.fireTableDataChanged();

                iPanel.dispose();
                iDialog.closeDialog();
            }
        };
        iPanel.addOkAction(iSaveAction);

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //SSInvoice iInvoice = iPanel.getInvoice();
                // remove all references for the sales
                //SSOrderMath.removeReference(iInvoice);

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "invoiceframe.saveonclose") != JOptionPane.OK_OPTION) {
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
     * @param iInvoice
     * @param iOrders
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, SSInvoice iInvoice, final List<SSOrder> iOrders, final AbstractTableModel pModel) {
        final String lockString = "ordertoinvoice"+SSDB.getInstance().getCurrentCompany().getId();
        if(!SSPostLock.applyLock(lockString)){
            new SSErrorDialog( iMainFrame, "invoiceframe.ordertoinvoiceopen",iInvoice.getNumber());
            return;
        }
        final SSDialog       iDialog = new SSDialog(iMainFrame, SSBundle.getBundle().getString("invoiceframe.new.title"));
        final SSInvoicePanel iPanel  = new SSInvoicePanel(iDialog);

        //iInvoice.setPaymentTerm(iInvoice.getCustomer() == null ? null : iInvoice.getCustomer().getPaymentTerm());

        iInvoice.setDueDate();
        iInvoice.setEntered(false);
        iInvoice.setPrinted(false);



        iPanel.setInvoice( new SSInvoice(iInvoice) );
        //iPanel.getInvoice().doAutoIncrecement();
        //iPanel.getInvoice().setEntered(false);
        //iPanel.setOrderNumbers(iOrders);
        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSInvoice iInvoice = iPanel.getInvoice();

                SSDB.getInstance().addInvoice(iInvoice);

                for (SSOrder iOrder : iOrders) {
                    // Set the invoice for the order
                    if(SSDB.getInstance().getOrders().contains(iOrder)){
                        iOrder.setInvoice(iInvoice);
                        SSDB.getInstance().updateOrder(iOrder);
                    }
                }

                if (iPanel.doSaveCustomerAndProducts()) SSInvoiceMath.addCustomerAndProducts(iInvoice);

                //if (pModel != null) pModel.fireTableDataChanged();

                iPanel.dispose();
                SSPostLock.removeLock(lockString);
                iDialog.closeDialog();
            }
        };
        iPanel.addOkAction(iSaveAction);

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDialog.closeDialog();
                //SSInvoice iInvoice = iPanel.getInvoice();
                // remove all references for the sales
                //SSOrderMath.removeReference(iInvoice);

                SSPostLock.removeLock(lockString);

                iPanel.dispose();
            }
        });
        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(! iPanel.isValid() ) {
                    SSPostLock.removeLock(lockString);
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "invoiceframe.saveonclose") != JOptionPane.OK_OPTION) {
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
}
