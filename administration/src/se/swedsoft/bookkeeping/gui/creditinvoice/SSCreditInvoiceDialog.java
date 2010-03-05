package se.swedsoft.bookkeeping.gui.creditinvoice;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.invoice.SSInvoiceFrame;
import se.swedsoft.bookkeeping.gui.creditinvoice.panel.SSCreditInvoicePanel;
import se.swedsoft.bookkeeping.gui.creditinvoice.dialog.SSSelectInvoiceDialog;
import se.swedsoft.bookkeeping.data.SSCreditInvoice;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.calc.math.SSOrderMath;
import se.swedsoft.bookkeeping.calc.math.SSTenderMath;

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
 * Date: 2006-okt-10
 * Time: 13:22:12
 */
public class SSCreditInvoiceDialog {

    private static ResourceBundle bundle = SSBundle.getBundle();

    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void editDialog(final SSMainFrame iMainFrame, SSCreditInvoice iInvoice, final AbstractTableModel pModel) {
        final String lockString = "creditinvoice" + iInvoice.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if(!SSPostLock.applyLock(lockString)){
            new SSErrorDialog( iMainFrame, "creditinvoiceframe.creditinvoiceopen",iInvoice.getNumber());
            return;
        }
        final SSDialog             iDialog = new SSDialog(iMainFrame, bundle.getString("creditinvoiceframe.edit.title"));
        final SSCreditInvoicePanel iPanel  = new SSCreditInvoicePanel(iDialog);

        iPanel.setCreditInvoice( new SSCreditInvoice(iInvoice) );
        iPanel.setSavecustomerandproductsSelected(false);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSCreditInvoice iInvoice = iPanel.getCreditInvoice();

                SSDB.getInstance().updateCreditInvoice(iInvoice);

                if (iPanel.doSaveCustomerAndProducts()) SSOrderMath.addCustomerAndProducts(iInvoice);

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
                if(! iPanel.isValid() ) {
                    SSPostLock.removeLock(lockString);
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "creditinvoiceframe.saveonclose") != JOptionPane.OK_OPTION) {
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
    public static void newDialog(final SSMainFrame iMainFrame, final AbstractTableModel pModel) {
        // Get the sales to credit

        SSInvoice iCrediting = SSSelectInvoiceDialog.showDialog(iMainFrame);

        if(iCrediting == null) {
            new SSErrorDialog( iMainFrame, "creditinvoiceframe.creditinvoicenoinvoice");
            return;
        } else if (iCrediting.getNumber() == -1) {
            return;
        }

        newDialog(iMainFrame, iCrediting, pModel);
    }

    /**
     *
     * @param iMainFrame
     * @param iCrediting
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, SSInvoice iCrediting, final AbstractTableModel pModel) {
        final SSDialog             iDialog = new SSDialog(iMainFrame, bundle.getString("creditinvoiceframe.new.title"));
        final SSCreditInvoicePanel iPanel  = new SSCreditInvoicePanel(iDialog);
        SSCreditInvoice iCreditInvoice = new SSCreditInvoice( iCrediting );
        iCreditInvoice.setNumber(null);

        iPanel.setCreditInvoice(iCreditInvoice);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSCreditInvoice iInvoice = iPanel.getCreditInvoice();
                SSDB.getInstance().addCreditInvoice(iInvoice);

                if (iPanel.doSaveCustomerAndProducts()) SSTenderMath.addCustomerAndProducts(iInvoice);

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
                if(! iPanel.isValid() ) {
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "creditinvoiceframe.saveonclose") != JOptionPane.OK_OPTION) {
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
    public static void copyDialog(final SSMainFrame iMainFrame, SSCreditInvoice iCreditInvoice, final AbstractTableModel pModel) {
        final String lockString = "creditinvoice" + iCreditInvoice.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if(SSPostLock.isLocked(lockString)){
            new SSErrorDialog( iMainFrame, "creditinvoiceframe.creditinvoiceopen",iCreditInvoice.getNumber());
            return;
        }
        final SSDialog             iDialog = new SSDialog(iMainFrame, bundle.getString("creditinvoiceframe.copy.title"));
        final SSCreditInvoicePanel iPanel  = new SSCreditInvoicePanel(iDialog);

        SSCreditInvoice iNew = new SSCreditInvoice(iCreditInvoice);

        iNew.setDate(new Date() );
        iNew.setNumber(null);
        iNew.setEntered(false);
        iNew.setPrinted(false);
        if (iCreditInvoice.getCrediting() == null) {
            new SSErrorDialog( iMainFrame, "creditinvoiceframe.creditinvoicenoinvoice");
            return;
        }
        iPanel.setCreditInvoice(iNew );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSCreditInvoice iInvoice = iPanel.getCreditInvoice();

                SSDB.getInstance().addCreditInvoice(iInvoice);

                if (iPanel.doSaveCustomerAndProducts()) SSTenderMath.addCustomerAndProducts(iInvoice);

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
                if(! iPanel.isValid() ){
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "creditinvoiceframe.saveonclose") != JOptionPane.OK_OPTION) {
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
