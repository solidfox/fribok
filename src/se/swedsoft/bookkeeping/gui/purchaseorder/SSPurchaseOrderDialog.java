package se.swedsoft.bookkeeping.gui.purchaseorder;

import se.swedsoft.bookkeeping.calc.math.SSPurchaseOrderMath;
import se.swedsoft.bookkeeping.data.SSOrder;
import se.swedsoft.bookkeeping.data.SSPurchaseOrder;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.purchaseorder.panel.SSPurchaseOrderPanel;
import se.swedsoft.bookkeeping.gui.supplierinvoice.SSSupplierInvoiceDialog;
import se.swedsoft.bookkeeping.gui.supplierinvoice.SSSupplierInvoiceFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSInformationDialog;
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
import java.util.ResourceBundle;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:57:02
 */
public class SSPurchaseOrderDialog {


    private static ResourceBundle bundle = SSBundle.getBundle();

    private SSPurchaseOrderDialog() {
    }


    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, final AbstractTableModel pModel) {
        final SSDialog              iDialog = new SSDialog(iMainFrame, SSPurchaseOrderDialog.bundle.getString("purchaseorderframe.new.title"));
        final SSPurchaseOrderPanel  iPanel  = new SSPurchaseOrderPanel(iDialog);

        iPanel.setOrder( new SSPurchaseOrder() );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPurchaseOrder iOrder = iPanel.getOrder();
                SSDB.getInstance().addPurchaseOrder(iOrder);

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "purchaseorderframe.saveonclose") != JOptionPane.OK_OPTION) {
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
     * @param iPurchaseOrder
     * @param iSelected
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, SSPurchaseOrder iPurchaseOrder, final List<SSOrder> iSelected, final AbstractTableModel pModel) {

        final String lockString = "ordertopurchaseorder"+SSDB.getInstance().getCurrentCompany().getId();
        if(!SSPostLock.applyLock(lockString)){
            new SSErrorDialog( iMainFrame, "purchaseorderframe.ordertopurchaseorder");
            return;
        }

        final SSDialog              iDialog = new SSDialog(iMainFrame, SSPurchaseOrderDialog.bundle.getString("purchaseorderframe.new.title"));
        final SSPurchaseOrderPanel  iPanel  = new SSPurchaseOrderPanel(iDialog);


        iPanel.setOrder( iPurchaseOrder );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPurchaseOrder iPurchaseOrder = iPanel.getOrder();
                SSDB.getInstance().addPurchaseOrder(iPurchaseOrder);

                for (SSOrder iOrder : iSelected) {
                    if(SSDB.getInstance().getOrders().contains(iOrder)){
                        iOrder.setPurchaseOrder(iPurchaseOrder);
                        SSDB.getInstance().updateOrder(iOrder);
                    }
                }

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
                if(! iPanel.isValid() ){
                    SSPostLock.removeLock(lockString);
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "purchaseorderframe.saveonclose") != JOptionPane.OK_OPTION) {
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
     * @param iPurchaseOrder
     * @param pModel
     */
    public static void editDialog(final SSMainFrame iMainFrame, SSPurchaseOrder iPurchaseOrder, final AbstractTableModel pModel) {
        final String lockString = "purchaseorder" + iPurchaseOrder.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if(!SSPostLock.applyLock(lockString)){
            new SSErrorDialog(iMainFrame, "purchaseorderframe.purchaseorderopen", iPurchaseOrder.getNumber());
            return;
        }
        final SSDialog              iDialog = new SSDialog(iMainFrame, SSPurchaseOrderDialog.bundle.getString("purchaseorderframe.edit.title"));
        final SSPurchaseOrderPanel  iPanel  = new SSPurchaseOrderPanel(iDialog);

        iPanel.setOrder( iPurchaseOrder );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPurchaseOrder iOrder = iPanel.getOrder();

                SSDB.getInstance().updatePurchaseOrder(iOrder);

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
                    SSPostLock.removeLock(lockString);
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "purchaseorderframe.saveonclose") != JOptionPane.OK_OPTION) {
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
     * @param iPurchaseOrder
     * @param pModel
     */
    public static void copyDialog(final SSMainFrame iMainFrame, SSPurchaseOrder iPurchaseOrder, final AbstractTableModel pModel) {
        final String lockString = "purchaseorder" + iPurchaseOrder.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if(SSPostLock.isLocked(lockString)){
            new SSErrorDialog(iMainFrame, "purchaseorderframe.purchaseorderopen", iPurchaseOrder.getNumber());
            return;
        }
        final SSDialog              iDialog = new SSDialog(iMainFrame, SSPurchaseOrderDialog.bundle.getString("purchaseorderframe.copy.title"));
        final SSPurchaseOrderPanel  iPanel  = new SSPurchaseOrderPanel(iDialog);

        SSPurchaseOrder iNew = new SSPurchaseOrder(iPurchaseOrder);
        iNew.setNumber(null);
        iNew.setDate(new Date() );
        iNew.setInvoice(null);
        iNew.setPrinted(false);
        iNew.setEstimatedDelivery( new Date() );

        iPanel.setOrder( iNew );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPurchaseOrder iOrder = iPanel.getOrder();

                SSDB.getInstance().addPurchaseOrder(iOrder);

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "purchaseorderframe.saveonclose") != JOptionPane.OK_OPTION){
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
     * @param iPurchaseOrders
     * @param iModel
     */
    public static void createInvoiceDialog(final SSMainFrame iMainFrame, List<SSPurchaseOrder> iPurchaseOrders, final AbstractTableModel iModel) {
        // Empty, exit
        if(iPurchaseOrders.isEmpty()) return;

        // Get the supplier from the first selected purchase order
        SSSupplier iSupplier = iPurchaseOrders.get(0).getSupplier();

        // We have selected more then one purchase order, do we want to create one supplierinvoice ?
        if(iPurchaseOrders.size() > 1) {
            if (SSQueryDialog.showDialog(iMainFrame, SSBundle.getBundle(), "purchaseorderframe.createcollectioninvoice", iSupplier.getNumber()) != JOptionPane.OK_OPTION)
                return;

            iPurchaseOrders = SSPurchaseOrderMath.getOrdersBySupplierNr(iPurchaseOrders, iSupplier.getNumber());
        }
        // Create the new supplier invoice
        SSSupplierInvoice iSupplierInvoice = new SSSupplierInvoice(iPurchaseOrders.get(0));

        iSupplierInvoice.setNumber(null);
        iSupplierInvoice.setStockInfluencing(true);
        //iSupplierInvoice.setDate(new Date());


        for (SSPurchaseOrder iCurrent : iPurchaseOrders) {

            // if the selected order already has a sales assosiated we can't create a new one
            if(iCurrent.getInvoice() != null){
                SSInformationDialog.showDialog(iMainFrame, "purchaseorderframe.orderhasinvoice", iCurrent.getNumber());
                return;
            }
            // Append the rows from the purchase order to the supplier invoice
            iSupplierInvoice.append(iCurrent);
        }

        if(SSSupplierInvoiceFrame.getInstance() != null){
            SSSupplierInvoiceDialog.newDialog(iMainFrame, iSupplierInvoice, iPurchaseOrders, SSSupplierInvoiceFrame.getInstance().getModel());
        } else {
            SSSupplierInvoiceDialog.newDialog(iMainFrame, iSupplierInvoice, iPurchaseOrders, null);
        }
        if(iModel != null) iModel.fireTableDataChanged();




/*



SSPurchaseOrder iOrder = iOrders.get(0);

// if the selected order already has a sales assosiated we can't create a new one
if(iOrder.getInvoice(SSDB.getInstance().getSupplierInvoices()) != null){
SSInformationDialog.showDialog(iMainFrame, "orderframe.orderhasinvoice", iOrder.getNumber());
return;
}

SSSupplierInvoice iInvoice = new SSSupplierInvoice(iOrder);

iInvoice.doAutoIncrecement();

// Set the sales for the selected order to the new one
iOrder.setInvoice( iInvoice );


if(iOrders.size() > 1 && SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "purchaseorderframe.createcollectioninvoice", iOrder.getSupplierNr()) == JOptionPane.OK_OPTION) {

// Get all the orders with the same customer nr as the first selected
List<SSPurchaseOrder> iFiltered = SSPurchaseOrderMath.getOrdersBySupplierNr(iOrders, iOrder.getSupplierNr());

// Remove the first selected order so we don't add it to the sales twice
iFiltered.remove(iOrder);

for (SSPurchaseOrder iCurrent : iFiltered) {

// if the selected order already has a sales assosiated we can't create a new one
if(iCurrent.getInvoice(SSDB.getInstance().getSupplierInvoices()) != null){
  SSInformationDialog.showDialog(iMainFrame, "purchaseorderframe.orderhasinvoice", iCurrent.getNumber());
  return;
}
// Append the rows from the order to the sales
iInvoice.append(iCurrent);
}

}


if(SSSupplierInvoiceFrame.getInstance() != null){
SSSupplierInvoiceDialog.newDialog(iMainFrame, iInvoice, iFiltered, SSSupplierInvoiceFrame.getInstance().getModel());
} else {
SSSupplierInvoiceDialog.newDialog(iMainFrame, iInvoice, iFiltered, null);
}

*/
        //iModel.fireTableDataChanged();

        // SSDB.getInstance().notifyYearUpdated();
    }


}
