package se.swedsoft.bookkeeping.gui.purchasesuggestion.dialog;

import se.swedsoft.bookkeeping.gui.periodicinvoice.util.SSPendingInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSInformationDialog;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSSupplierCellEditor;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.purchaseorder.SSPurchaseOrderFrame;
import se.swedsoft.bookkeeping.gui.purchasesuggestion.util.SSPurchaseSuggestionTableModel;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.calc.math.SSProductMath;
import se.swedsoft.bookkeeping.calc.math.SSSupplierMath;
import se.swedsoft.bookkeeping.calc.math.SSPurchaseOrderMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;

/**
 * User: Andreas Lago
 * Date: 2006-aug-14
 * Time: 12:19:27
 */
public class SSPurchaseSuggestionDialog extends SSDialog {

    private SSPurchaseSuggestionTableModel iModel;

    private SSButtonPanel iButtonPanel;

    private JPanel iPanel;

    private SSTable iTable;

    private List<SSProduct> iProducts;

    /**
     *
     * @param iMainFrame
     */
    public SSPurchaseSuggestionDialog(SSMainFrame iMainFrame){
        super(iMainFrame, SSBundle.getBundle().getString("purchasesuggestiondialog.title"));

        iProducts = getProductsForPurchaseSuggestion();

        iModel = new SSPurchaseSuggestionTableModel(iProducts);
        iModel.addColumn(iModel.getSelectionColumn(), true);
        iModel.addColumn(SSPurchaseSuggestionTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSPurchaseSuggestionTableModel.COLUMN_DESCRIPTION);
        iModel.addColumn(SSPurchaseSuggestionTableModel.COLUMN_ORDERPOINT);
        iModel.addColumn(SSPurchaseSuggestionTableModel.COLUMN_STOCK_QUANTITY);
        iModel.addColumn(SSPurchaseSuggestionTableModel.COLUMN_INCOMMING);
        iModel.addColumn(SSPurchaseSuggestionTableModel.COLUMN_ORDER_VOLUME, true);
        iModel.addColumn(SSPurchaseSuggestionTableModel.COLUMN_SUPPLIER, true);

        iModel.setupTable(iTable);
        iModel.selectAll();

        iTable.setDefaultEditor(SSSupplier.class, new SSSupplierCellEditor());

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.CANCEL_OPTION);
                setVisible(false);
            }
        });

        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(iModel.getSelected() == null){
                    new SSErrorDialog( SSMainFrame.getInstance(), "purchasesuggestiondialog.nosupplier");
                    return;
                }
                setModalResult(JOptionPane.OK_OPTION);
                setVisible(false);
            }
        });
        iButtonPanel.getOkButton().setEnabled( iProducts.size() > 0 );

        setPanel(iPanel);
    }

    /**
     * Returnar de produkter som ska finnas med i inköpsförslaget. Detta inkluderar
     * produkter med lagerantal mindre än beställningspunkten förutsatt att det är en
     * lagervara samt inte en paketprodukt.
     *
     * @return iProducts
     */
    private List<SSProduct> getProductsForPurchaseSuggestion() {
        List<SSProduct> iProducts = new LinkedList<SSProduct>();
        SSStock iStock = new SSStock(true);
        for (SSProduct iProduct : SSDB.getInstance().getProducts()) {
            if (!iProduct.isParcel() && iProduct.isStockProduct() ) {
                Integer iStockQuantity = iStock.getQuantity(iProduct) == null ? 0 : iStock.getQuantity(iProduct);
                Integer iOrderPoint = iProduct.getOrderpoint() == null ? 0 : iProduct.getOrderpoint();
                Integer iIncomming = SSPurchaseOrderMath.getNumberOfIncommingProducts(iProduct);
                if(iStockQuantity+iIncomming < iOrderPoint) iProducts.add(iProduct);
            }
        }
        return iProducts;
    }
    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }

    /**
     *
     * @param l
     */
    public void addOkActionListener(ActionListener l) {
        iButtonPanel.addOkActionListener(l);
    }

    /**
     *
     * @param l
     */
    public void addCancelActionListener(ActionListener l) {
        iButtonPanel.addCancelActionListener(l);
    }


    /**
     *
     * @param iMainFrame
     */
    public static void showDialog(SSMainFrame iMainFrame){
        String lockString = "purchasesuggestion"+SSDB.getInstance().getCurrentCompany().getId();
        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "purchasesuggestion.iscreated");
            return;
        }
        SSPurchaseSuggestionDialog iDialog = new SSPurchaseSuggestionDialog(iMainFrame);
        iDialog.setSize(800, 600);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible(true);

        if(iDialog.getModalResult() != JOptionPane.OK_OPTION ) {
            SSPostLock.removeLock(lockString);
            return;
        }
        List<SSProduct> iProducts = iDialog.iModel.getSelected();
        List<SSProduct> iUseForPurchaseOrder = new LinkedList<SSProduct>();
        String iAddedOrders ="";
        for (SSSupplier iSupplier : SSDB.getInstance().getSuppliers()) {
            iUseForPurchaseOrder.clear();
            for (SSProduct iProduct : iProducts) {
                if(iProduct.getOrdercount() == null) continue;

                if (iSupplier.getNumber().equals(iProduct.getSupplierNr()) && iProduct.getOrdercount() > 0) {
                    iUseForPurchaseOrder.add(iProduct);
                }
            }
            if(iUseForPurchaseOrder.size()>0){
                if(iAddedOrders.length() != 0) iAddedOrders += ", ";
                SSPurchaseOrder iPurchaseOrder = new SSPurchaseOrder(iUseForPurchaseOrder, iSupplier);
                SSDB.getInstance().addPurchaseOrder(iPurchaseOrder);
                iAddedOrders += iPurchaseOrder.getNumber();
            }
        }

        SSPostLock.removeLock(lockString);
        if (iAddedOrders.length() > 0) {
            SSInformationDialog.showDialog(SSMainFrame.getInstance(), "purchasesuggestiondialog.success",iAddedOrders);
        } else {
            SSInformationDialog.showDialog(SSMainFrame.getInstance(), "purchasesuggestiondialog.noordersadded");
        }

    }



}
