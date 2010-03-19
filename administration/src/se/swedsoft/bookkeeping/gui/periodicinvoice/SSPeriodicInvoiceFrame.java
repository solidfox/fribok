package se.swedsoft.bookkeeping.gui.periodicinvoice;

import se.swedsoft.bookkeeping.data.SSOrder;
import se.swedsoft.bookkeeping.data.SSPeriodicInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.periodicinvoice.panel.SSListInvoicesPanel;
import se.swedsoft.bookkeeping.gui.periodicinvoice.panel.SSPeriodicInvoiceSearchPanel;
import se.swedsoft.bookkeeping.gui.periodicinvoice.util.SSPeriodicInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;


/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:47:21
 */
public class SSPeriodicInvoiceFrame extends SSDefaultTableFrame {

    private static SSPeriodicInvoiceFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( cInstance == null || SSPeriodicInvoiceFrame.cInstance.isClosed() ){
            cInstance = new SSPeriodicInvoiceFrame(pMainFrame, pWidth, pHeight);
        }
        SSPeriodicInvoiceFrame.cInstance.setVisible(true);
        SSPeriodicInvoiceFrame.cInstance.deIconize();
        SSPeriodicInvoiceFrame.cInstance.updateFrame();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSPeriodicInvoiceFrame getInstance(){
        return cInstance;
    }

    private SSTable iTable;

    private SSPeriodicInvoiceTableModel iModel;

    private SSPeriodicInvoiceSearchPanel iSearchPanel;

    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSPeriodicInvoiceFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("periodicinvoiceframe.title"), width, height);
    }


    /**
     * This method should return a toolbar if the sub-class wants one. <P>
     * Otherwise, it may return null.
     *
     * @return A JToolBar or null.
     */
    @Override
    public JToolBar getToolBar() {
        JToolBar toolBar = new JToolBar();

        SSMenuButton<SSButton> iMenuButton;

        JMenuItem iMenuItem;

        // New
        // ***************************
        SSButton iButton = new SSButton("ICON_NEWITEM", "periodicinvoiceframe.newbutton", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPeriodicInvoiceDialog.newDialog(getMainFrame(), iModel);
            }
        });
        toolBar.add(iButton);



        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "periodicinvoiceframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSPeriodicInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getPeriodicInvoice(iSelected);
                }
                if (iSelected != null) {
                    SSPeriodicInvoiceDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "periodicinvoiceframe.invoicegone",iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);

        // Copy
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "periodicinvoiceframe.copybutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSPeriodicInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getPeriodicInvoice(iSelected);
                }
                if (iSelected != null) {
                    SSPeriodicInvoiceDialog.copyDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "periodicinvoiceframe.invoicegone",iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "periodicinvoiceframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSPeriodicInvoice> toDelete = iModel.getObjects(selected);
                deleteSelectedInvoice(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();


        // List the invoices for this periodic invoice
        // ***************************
        iButton = new SSButton("Task List 24", "periodicinvoiceframe.invoicelistbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSPeriodicInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getPeriodicInvoice(iSelected);
                }
                if (iSelected != null) {
                    SSListInvoicesPanel.showDialog(getMainFrame(), iSelected);
                } else {
                    new SSErrorDialog( getMainFrame(), "periodicinvoiceframe.invoicegone",iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();


        // Create PeriodicInvoice for sales
        // ***************************
        iButton = new SSButton("ICON_CREATECHANGE", "periodicinvoiceframe.invoicebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(SSPeriodicInvoiceDialog.pendingPeriodicInvoicesDialog( getMainFrame() )){
                    SSPostLock.removeLock("periodicinvoicepending"+SSDB.getInstance().getCurrentCompany().getId());
                }
            }
        });
        toolBar.add(iButton);

        return toolBar;
    }



    /**
     * This method should return the main content for the frame. <P>
     * Such as an object table.
     *
     * @return The main content for this frame.
     */
    @Override
    public JComponent getMainContent() {
        iTable = new SSTable();

        iModel = new SSPeriodicInvoiceTableModel();
        iModel.addColumn(SSPeriodicInvoiceTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSPeriodicInvoiceTableModel.COLUMN_DESCRIPTION);
        iModel.addColumn(SSPeriodicInvoiceTableModel.COLUMN_CUSTOMER_NR);
        iModel.addColumn(SSPeriodicInvoiceTableModel.COLUMN_CUSTOMER_NAME);
        iModel.addColumn(SSPeriodicInvoiceTableModel.COLUMN_DATE);
        iModel.addColumn(SSPeriodicInvoiceTableModel.COLUMN_NEXT);
        iModel.addColumn(SSPeriodicInvoiceTableModel.COLUMN_TOTAL_SUM);
        iModel.addColumn(SSPeriodicInvoiceTableModel.COLUMN_CURRENCY);

        iModel.setupTable(iTable);


        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSPeriodicInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getPeriodicInvoice(iSelected);
                } else{
                    return;
                }
                if (iSelected != null) {
                    SSPeriodicInvoiceDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "periodicinvoiceframe.invoicegone",iNumber);
                }
            }
        });

        JPanel iPanel = new JPanel();
        iSearchPanel = new SSPeriodicInvoiceSearchPanel(iModel);
        iPanel.setLayout(new BorderLayout());
        iPanel.add(iSearchPanel,BorderLayout.NORTH);
        iPanel.add(new JScrollPane(iTable), BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,2,2));

        return iPanel;
    }

    /*
    *
    * @return
    */
    public SSTableModel<SSPeriodicInvoice> getModel() {
        return iModel;
    }

    /**
     * This method should return the status bar content, if any. <P>
     *
     * @return The content for the status bar or null if none is wanted.
     */
    @Override
    public JComponent getStatusBar() {
        return null;
    }

    /**
     * Indicates whether this frame is a company data related frame. <P>
     *
     * @return A boolean value.
     */
    @Override
    public boolean isCompanyFrame() {
        return true;
    }

    /**
     * Indicates whether this frame is a year data related frame. <P>
     *
     * @return A boolean value.
     */
    @Override
    public boolean isYearDataFrame() {
        return false;
    }




    /**
     *
     * @param delete
     */
    private void deleteSelectedInvoice(List<SSPeriodicInvoice> delete) {
        if (delete.isEmpty()) {
            return;
        }

        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "periodicinvoiceframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSPeriodicInvoice iPeriodicInvoice : delete) {
                if (SSPostLock.isLocked("periodicinvoice" + iPeriodicInvoice.getNumber() + SSDB.getInstance().getCurrentCompany().getId())) {
                    new SSErrorDialog(getMainFrame(), "periodicinvoiceframe.periodicinvoiceopen", iPeriodicInvoice.getNumber());
                } else {
                    List<SSOrder> iOrdersToUpdate = new LinkedList<SSOrder>();
                    for (SSOrder iOrder : SSDB.getInstance().getOrders()) {
                        if (iOrder.hasPeriodicInvoice(iPeriodicInvoice)) {
                            iOrder.setPeriodicInvoice(null);
                            iOrdersToUpdate.add(iOrder);
                        }
                    }
                    for(SSOrder iOrder : iOrdersToUpdate){
                        SSDB.getInstance().updateOrder(iOrder);
                    }
                    iOrdersToUpdate = null;
                    SSDB.getInstance().deletePeriodicInvoice(iPeriodicInvoice);
                }
            }
        }
    }

    /**
     *
     */
    public static void fireTableDataChanged() {
        if(cInstance != null) cInstance.getModel().fireTableDataChanged();
    }

    private SSPeriodicInvoice getPeriodicInvoice(SSPeriodicInvoice iPeriodicInvoice) {
        return SSDB.getInstance().getPeriodicInvoice(iPeriodicInvoice);
    }

    public void updateFrame() {
        iSearchPanel.ApplyFilter();
    }
    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        cInstance=null;
    }

}
