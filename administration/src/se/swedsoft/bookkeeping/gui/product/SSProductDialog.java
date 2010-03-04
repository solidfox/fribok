package se.swedsoft.bookkeeping.gui.product;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.product.panel.SSProductPanel;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.SSBookkeeping;

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
 * Date: 2006-sep-05
 * Time: 13:45:11
 */
public class SSProductDialog {

    private static Dimension iDialogSize = new Dimension(640, 480);

    private static ResourceBundle bundle = SSBundle.getBundle();

    /**
     *
     * @param iMainFrame
     * @param pModel
     */
    public static void newDialog(final SSMainFrame iMainFrame, final AbstractTableModel pModel) {
        final SSDialog       iDialog = new SSDialog(iMainFrame, bundle.getString("productframe.new.title"));
        final SSProductPanel iPanel  = new SSProductPanel(iDialog, false);

        iPanel.setProduct(new SSProduct());

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSProduct iProduct = iPanel.getProduct();

                List<SSProduct> iProducts = SSDB.getInstance().getProducts();
                for (SSProduct pProduct : iProducts) {
                    if (iProduct.getNumber().equals(pProduct.getNumber())) {
                        new SSErrorDialog(iMainFrame, "productframe.duplicate", iProduct.getNumber());
                        return;
                    }
                }

                SSDB.getInstance().addProduct(iProduct);

                if (pModel != null) pModel.fireTableDataChanged();

                iDialog.closeDialog();
            }
        };
        iPanel.addOkAction(iSaveAction);

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDialog.closeDialog();
            }
        });
        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(!iPanel.isValid()) {
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "productframe.saveonclose") != JOptionPane.OK_OPTION){
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
     * @param iProduct
     * @param pModel
     */
    public static void editDialog(final SSMainFrame iMainFrame, SSProduct iProduct, final AbstractTableModel pModel) {
        final String lockString = "product"+iProduct.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "productframe.productopen", iProduct.getNumber());
            return;
        }
        final SSDialog       iDialog = new SSDialog(iMainFrame, bundle.getString("productframe.edit.title"));
        final SSProductPanel iPanel  = new SSProductPanel(iDialog, true);
        //iPanel.setEditPanel();

        iPanel.setProduct( new SSProduct(iProduct) );

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);


        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSProduct iProduct = iPanel.getProduct();

                SSDB.getInstance().updateProduct(iProduct);

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

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "productframe.saveonclose") != JOptionPane.OK_OPTION) {
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
     * @param iProduct
     * @param pModel
     */
    public static void copyDialog(final SSMainFrame iMainFrame, SSProduct iProduct, final AbstractTableModel pModel) {
        final String lockString = "product"+iProduct.getNumber()+SSDB.getInstance().getCurrentCompany().getId();
        if (SSPostLock.isLocked(lockString)) {
            new SSErrorDialog(iMainFrame, "productframe.productopen", iProduct.getNumber());
            return;
        }
        final SSDialog       iDialog = new SSDialog(iMainFrame, bundle.getString("productframe.copy.title"));
        final SSProductPanel iPanel  = new SSProductPanel(iDialog, false);

        iPanel.setProduct(new SSProduct(iProduct));

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                SSProduct iProduct = iPanel.getProduct();

                List<SSProduct> iProducts = SSDB.getInstance().getProducts();
                for (SSProduct pProduct : iProducts) {
                    if (iProduct.getNumber().equals(pProduct.getNumber())) {
                        new SSErrorDialog(iMainFrame, "productframe.duplicate", iProduct.getNumber());
                        return;
                    }
                }

                SSDB.getInstance().addProduct(iProduct);

                if (pModel != null) pModel.fireTableDataChanged();

                iDialog.closeDialog();
            }
        };
        iPanel.addOkAction(iSaveAction);

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDialog.closeDialog();
            }
        });
        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(!iPanel.isValid()) {
                    return;
                }

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "productframe.saveonclose") != JOptionPane.OK_OPTION) {
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
