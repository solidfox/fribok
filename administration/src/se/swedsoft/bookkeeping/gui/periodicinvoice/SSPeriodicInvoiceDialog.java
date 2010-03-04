package se.swedsoft.bookkeeping.gui.periodicinvoice;

import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.invoice.SSInvoiceFrame;
import se.swedsoft.bookkeeping.gui.periodicinvoice.panel.SSPeriodicInvoicePanel;
import se.swedsoft.bookkeeping.gui.periodicinvoice.dialog.SSPendingPeriodicInvoiceDialog;
import se.swedsoft.bookkeeping.gui.periodicinvoice.util.SSPeriodicInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.data.SSPeriodicInvoice;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSOrder;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSPeriodicInvoiceMath;

import javax.swing.table.AbstractTableModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-jul-31
 * Time: 15:01:54
 */
public class SSPeriodicInvoiceDialog {

    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, final AbstractTableModel pModel) {
        final SSDialog               iDialog = new SSDialog(iMainFrame,  SSBundle.getBundle().getString("periodicinvoiceframe.new.title"));
        final SSPeriodicInvoicePanel iPanel  = new SSPeriodicInvoicePanel(iDialog);
        SSPeriodicInvoice iPeriodicInvoice = new SSPeriodicInvoice();
        iPeriodicInvoice.setNumber(null);
        iPanel.setPeriodicInvoice( iPeriodicInvoice );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPeriodicInvoice iPeriodicInvoice = iPanel.getPeriodicInvoice();
                SSDB.getInstance().addPeriodicInvoice(iPeriodicInvoice);

                if (iPanel.doSaveCustomerAndProducts())
                    SSInvoiceMath.addCustomerAndProducts(iPeriodicInvoice.getTemplate());

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "periodicinvoiceframe.saveonclose") != JOptionPane.OK_OPTION) {
                    return;
                }

                iSaveAction.actionPerformed(null);
            }
        });
        iDialog.setSize    (800, 600);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.showDialog();
    }

    public static void newDialog(final SSMainFrame iMainFrame, final AbstractTableModel pModel, SSPeriodicInvoice iPeriodicInvoice, final List<SSOrder> iOrders) {
        final SSDialog               iDialog = new SSDialog(iMainFrame,  SSBundle.getBundle().getString("periodicinvoiceframe.new.title"));
        final SSPeriodicInvoicePanel iPanel  = new SSPeriodicInvoicePanel(iDialog);
        //SSPeriodicInvoice iPeriodicInvoice = new SSPeriodicInvoice();
        iPeriodicInvoice.setNumber(null);

        iPanel.setPeriodicInvoice( iPeriodicInvoice );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPeriodicInvoice iPeriodicInvoice = iPanel.getPeriodicInvoice();
                SSDB.getInstance().addPeriodicInvoice(iPeriodicInvoice);

                for (SSOrder iOrder : iOrders) {
                    // Set the invoice for the order
                    if(SSDB.getInstance().getOrders().contains(iOrder)){
                        iOrder.setPeriodicInvoice(iPeriodicInvoice);
                        SSDB.getInstance().updateOrder(iOrder);
                    }
                }

                if (iPanel.doSaveCustomerAndProducts())
                    SSInvoiceMath.addCustomerAndProducts(iPeriodicInvoice.getTemplate());

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "periodicinvoiceframe.saveonclose") != JOptionPane.OK_OPTION) {
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
     * @param pModel
     */
    public static void editDialog(final SSMainFrame iMainFrame, final SSPeriodicInvoice iEditInvoice, final AbstractTableModel pModel) {
        final String lockString = "periodicinvoice" + iEditInvoice.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if(!SSPostLock.applyLock(lockString)){
            new SSErrorDialog( iMainFrame, "periodicinvoiceframe.invoiceopen",iEditInvoice.getNumber());
            return;
        }
        final SSDialog               iDialog = new SSDialog(iMainFrame, SSBundle.getBundle().getString("periodicinvoiceframe.edit.title"));
        final SSPeriodicInvoicePanel iPanel  = new SSPeriodicInvoicePanel(iDialog);

        iPanel.setPeriodicInvoice( new SSPeriodicInvoice(iEditInvoice) );
        iPanel.setSavecustomerandproductsSelected(false);
        int iAddedInvoices = 0;
        List<SSInvoice> iInvoices = iEditInvoice.getInvoices();
        for(SSInvoice iTempInvoice : iInvoices){
            if(iEditInvoice.isAdded(iTempInvoice)){
                iAddedInvoices++;
            }
        }
        final int iInvoiceCount = iAddedInvoices;
        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPeriodicInvoice iPeriodicInvoice = iPanel.getPeriodicInvoice();

                if(iPeriodicInvoice.getCount() < iInvoiceCount){
                    SSErrorDialog.showDialog(iMainFrame, "För få fakturor", "<html>" + iInvoiceCount + " fakturor är redan fakturerade för den här periodfakturan.<br>Du kan inte ange ett lägre antal än " + iInvoiceCount + ".");
                    return;
                }
                for(int i=0; i<iInvoiceCount; i++){
                    iPeriodicInvoice.setAdded(iPeriodicInvoice.getInvoices().get(i));
                }
                
                SSDB.getInstance().updatePeriodicInvoice(iPeriodicInvoice);

                if (iPanel.doSaveCustomerAndProducts())
                    SSInvoiceMath.addCustomerAndProducts(iPeriodicInvoice.getTemplate());

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "periodicinvoiceframe.saveonclose") != JOptionPane.OK_OPTION) {
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
    public static void copyDialog(final SSMainFrame iMainFrame, SSPeriodicInvoice iPeriodicInvoice, final SSPeriodicInvoiceTableModel pModel) {
        final String lockString = "periodicinvoice" + iPeriodicInvoice.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if(SSPostLock.isLocked(lockString)){
            new SSErrorDialog( iMainFrame, "peridodicinvoiceframe.invoiceopen",iPeriodicInvoice.getNumber());
            return;
        }
        final SSDialog       iDialog = new SSDialog(iMainFrame, SSBundle.getBundle().getString("periodicinvoiceframe.copy.title"));
        final SSPeriodicInvoicePanel iPanel  = new SSPeriodicInvoicePanel(iDialog);
        SSPeriodicInvoice iNew = new SSPeriodicInvoice(iPeriodicInvoice);

        iNew.setNumber(null);
        iNew.setDate(new Date() );
        for (SSInvoice iInvoice : iNew.getInvoices()) {
            iNew.setNotAdded(iInvoice);
        }
        iPanel.setPeriodicInvoice( iNew );


        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPeriodicInvoice iPeriodicInvoice = iPanel.getPeriodicInvoice();

                SSDB.getInstance().addPeriodicInvoice(iPeriodicInvoice);

                if (iPanel.doSaveCustomerAndProducts()) SSInvoiceMath.addCustomerAndProducts(iPeriodicInvoice.getTemplate());

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "periodicinvoiceframe.saveonclose") != JOptionPane.OK_OPTION) {
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
     */
    public static boolean pendingPeriodicInvoicesDialog(SSMainFrame iMainFrame) {
        final String lockString = "periodicinvoicepending"+SSDB.getInstance().getCurrentCompany().getId();
        if(!SSPostLock.applyLock(lockString)){
            new SSErrorDialog( iMainFrame, "periodicinvoiceframe.periodicinvoice");
            return false;
        }

        Map<SSPeriodicInvoice,List<SSInvoice>> iPeriodicInvoices = SSPeriodicInvoiceMath.getPeriodicInvoices();
        Map<SSPeriodicInvoice,List<SSInvoice>> iSelected = SSPendingPeriodicInvoiceDialog.showDialog(iMainFrame, iPeriodicInvoices);
        if( iSelected == null) {
            return true;
        }
        HashMap<SSPeriodicInvoice,List<SSInvoice>> iTemp = new HashMap<SSPeriodicInvoice,List<SSInvoice>>(iSelected);
        for (SSPeriodicInvoice iPeriodicInvoice : iSelected.keySet()) {

            if(SSPostLock.isLocked("periodicinvoice"+iPeriodicInvoice.getNumber()+SSDB.getInstance().getCurrentCompany().getId())){
                iTemp.remove(iPeriodicInvoice);
                new SSErrorDialog(new JFrame(), "periodicinvoiceframe.invoiceopen", iPeriodicInvoice.getNumber());
                continue;
            }
            if(SSDB.getInstance().getPeriodicInvoice(iPeriodicInvoice) == null){
                iTemp.remove(iPeriodicInvoice);
                new SSErrorDialog(new JFrame(), "periodicinvoiceframe.invoicegone", iPeriodicInvoice.getNumber());
                continue;
            }

            for (SSInvoice iInvoice : iTemp.get(iPeriodicInvoice)) {

                iPeriodicInvoice.setAdded( iInvoice );
                SSDB.getInstance().updatePeriodicInvoice(iPeriodicInvoice);

                iInvoice = new SSInvoice(iInvoice);
                iInvoice.setDate(new Date() );
                iInvoice.setDueDate();

                SSDB.getInstance().addInvoice(iInvoice);
            }
        }

        SSPeriodicInvoiceFrame.fireTableDataChanged();
        SSInvoiceFrame.fireTableDataChanged();

        return true;
    }


}
