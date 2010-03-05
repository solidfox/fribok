package se.swedsoft.bookkeeping.gui.purchaseorder;

import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.util.components.SSTabbedPanePanel;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.purchaseorder.util.SSPurchaseOrderTableModel;
import se.swedsoft.bookkeeping.gui.purchaseorder.panel.SSPurchaseOrderSearchPanel;
import se.swedsoft.bookkeeping.data.SSPurchaseOrder;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.SSOrder;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.data.system.SSMail;
import se.swedsoft.bookkeeping.print.SSReportFactory;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:47:21
 */
public class SSPurchaseOrderFrame extends SSDefaultTableFrame {

    private static SSPurchaseOrderFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( SSPurchaseOrderFrame.cInstance == null || SSPurchaseOrderFrame.cInstance.isClosed() ){
            SSPurchaseOrderFrame.cInstance = new SSPurchaseOrderFrame(pMainFrame, pWidth, pHeight);
        }
        SSPurchaseOrderFrame.cInstance.setVisible(true);
        SSPurchaseOrderFrame.cInstance.deIconize();
        SSPurchaseOrderFrame.cInstance.updateFrame();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSPurchaseOrderFrame getInstance(){
        return SSPurchaseOrderFrame.cInstance;
    }


    private JTabbedPane iTabbedPane;

    private SSTable iTable;

    private SSPurchaseOrderTableModel iModel;

    private SSPurchaseOrderSearchPanel iSearchPanel;


    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSPurchaseOrderFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("purchaseorderframe.title"), width, height);
    }


    /**
     * This method should return a toolbar if the sub-class wants one. <P>
     * Otherwise, it may return null.
     *
     * @return A JToolBar or null.
     */
    @Override
    public JToolBar getToolBar() {
        JToolBar iToolBar = new JToolBar();

        SSButton iButton;

        SSMenuButton iMenuButton;

        JMenuItem iMenuItem;



        // New
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "purchaseorderframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSPurchaseOrderDialog.newDialog(getMainFrame(), iModel);
            }
        });
        iToolBar.add(iButton);



        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "purchaseorderframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSPurchaseOrder iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getPurchaseOrder(iSelected);
                }
                if (iSelected != null) {
                    SSPurchaseOrderDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "purchaseorderframe.purchaseordergone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Copy
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "purchaseorderframe.copybutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSPurchaseOrder iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getPurchaseOrder(iSelected);
                }
                if (iSelected != null) {
                    SSPurchaseOrderDialog.copyDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "purchaseorderframe.purchaseordergone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "purchaseorderframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSPurchaseOrder> toDelete = iModel.getObjects(selected);
                deleteSelected(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();



        // Skapa leverantörsfaktura för inköpsorder
        // ***************************
        iButton = new SSButton("ICON_INVOICE24", "purchaseorderframe.createorderbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSPurchaseOrder> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getPurchaseOrders(iSelected);
                if (!iSelected.isEmpty()) {
                    SSPurchaseOrderDialog.createInvoiceDialog(getMainFrame(), iSelected, iModel);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);


        iToolBar.addSeparator();


        // Print
        // ***************************
        iMenuButton = new SSMenuButton("ICON_PRINT", "purchaseorderframe.printbutton");
        iMenuItem = iMenuButton.add("purchaseorderframe.print.purchaseorder", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSPurchaseOrder> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getPurchaseOrders(iSelected);
                if (!iSelected.isEmpty()) {
                    SSReportFactory.PurchaseOrderReport(getMainFrame(), iSelected);
                }
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);
        iMenuItem = iMenuButton.add("purchaseorderframe.print.emailpurchaseorderreport", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSPurchaseOrder iSelected = iModel.getSelectedRow(iTable);
                iSelected = getPurchaseOrder(iSelected);
                if(iSelected == null) return;
                if (!SSMail.isOk(iSelected.getSupplier())) {
                    return;
                }
                SSReportFactory.EmailPurchaseOrderReport(getMainFrame(), iSelected);
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);
        iMenuItem = iMenuButton.add("purchaseorderframe.print.inquiry", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSPurchaseOrder> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getPurchaseOrders(iSelected);
                if (!iSelected.isEmpty()) {
                    SSReportFactory.InquiryReport(getMainFrame(), iSelected);
                }
            }
        });

        Double.parseDouble("12.20");
        iTable.addSelectionDependentComponent(iMenuItem);
        iMenuItem = iMenuButton.add("purchaseorderframe.print.emailinquiry", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSPurchaseOrder iSelected = iModel.getSelectedRow(iTable);
                iSelected = getPurchaseOrder(iSelected);
                if(iSelected == null) return;
                if (!SSMail.isOk(iSelected.getSupplier())) {
                    return;
                }
                SSReportFactory.EmailInquiryReport(getMainFrame(), iSelected);
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);
        iMenuButton.addSeparator();
        iMenuButton.add("purchaseorderframe.print.purchaseorderlist", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.PurchaseOrderListReport( getMainFrame() );
            }
        });
        iToolBar.add(iMenuButton);


        return iToolBar;
    }



    /**
     * This method should return the main content for the frame. <P>
     * Such as an object table.
     *
     * @return The main content for this frame.
     */
    @Override
    public JComponent getMainContent() {
        iModel = new SSPurchaseOrderTableModel();

        iModel.addColumn(SSPurchaseOrderTableModel.COLUMN_PRINTED );
        iModel.addColumn(SSPurchaseOrderTableModel.COLUMN_NUMBER );
        iModel.addColumn(SSPurchaseOrderTableModel.COLUMN_SUPPLIER_NR  );
        iModel.addColumn(SSPurchaseOrderTableModel.COLUMN_SUPPLIER_NAME   );
        iModel.addColumn(SSPurchaseOrderTableModel.COLUMN_DATE   );
        iModel.addColumn(SSPurchaseOrderTableModel.COLUMN_ESTIMATED_DELIVERY  );
        iModel.addColumn(SSPurchaseOrderTableModel.COLUMN_SUM   );
        iModel.addColumn(SSPurchaseOrderTableModel.COLUMN_CURRENCY  );
        iModel.addColumn(SSPurchaseOrderTableModel.COLUMN_INVOICE  );

        iTable = new SSTable();

        iModel.setupTable(iTable);

        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSPurchaseOrder iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber;
                if (iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getPurchaseOrder(iSelected);
                } else {
                    return;
                }
                if (iSelected != null) {
                    SSPurchaseOrderDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "purchaseorderframe.purchaseordergone", iNumber);
                }
            }
        });


        iTabbedPane = new JTabbedPane();

        iTabbedPane.add(SSBundle.getBundle().getString("orderframe.filter.1"), new SSTabbedPanePanel() );
        iTabbedPane.add(SSBundle.getBundle().getString("orderframe.filter.2"), new SSTabbedPanePanel() );

        iTabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iSearchPanel.ApplyFilter(SSDB.getInstance().getPurchaseOrders());
            }
        });
        //setFilterIndex(0);


        JPanel iPanel = new JPanel();
        iSearchPanel = new SSPurchaseOrderSearchPanel(iModel);
        iPanel.setLayout(new BorderLayout());
        iPanel.add(iSearchPanel,BorderLayout.NORTH);
        iPanel.add(iTabbedPane, BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,4,2));

        return iPanel;
    }

    /**
     *
     * @param index
     */
    public void setFilterIndex(int index, List<SSPurchaseOrder> iOrders){
        JPanel iPanel = (JPanel)iTabbedPane.getComponentAt( index );

        iPanel.removeAll();
        iPanel.add(new JScrollPane(iTable), BorderLayout.CENTER);

        List<SSPurchaseOrder> iFiltered = Collections.emptyList();

        List<SSSupplierInvoice> iInvoices = SSDB.getInstance().getSupplierInvoices();

        switch(index){
            // Alla
            case 0:
                iFiltered =  iOrders;
                break;
                // Ordrar utan faktura
            case 1:
                iFiltered = new LinkedList<SSPurchaseOrder>();
                for(SSPurchaseOrder iOrder : iOrders){

                    if(iOrder.getInvoiceNr() == null){
                        iFiltered.add(iOrder);
                    }
                }

                break;
        }
        iModel.setObjects(iFiltered);
        iTabbedPane.repaint();
    }



    /**

     /**
     * This method should return the status bar content, if any. <P>
     *
     * @return The content for the status bar or null if none is wanted.
     */
    @Override
    public JComponent getStatusBar() {
        return null;
    }

    public JTabbedPane getTabbedPane() {
        return iTabbedPane;
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
        return true;
    }




    private void deleteSelected(List<SSPurchaseOrder> delete) {
        if (delete.isEmpty()) {
            return;
        }
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "purchaseorderframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSPurchaseOrder iPurchaseOrder : delete) {
                if (SSPostLock.isLocked("purchaseorder" + iPurchaseOrder.getNumber() + SSDB.getInstance().getCurrentCompany().getId())){
                    new SSErrorDialog(getMainFrame(), "purchaseorderframe.purchaseorderopen",iPurchaseOrder.getNumber());
                } else {
                    for (SSOrder iOrder : SSDB.getInstance().getOrders()) {
                        if (iOrder.hasPurchaseOrder(iPurchaseOrder)) {
                            iOrder.setPurchaseOrder(null);
                            SSDB.getInstance().updateOrder(iOrder);
                        }
                    }
                    SSDB.getInstance().deletePurchaseOrder(iPurchaseOrder);
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public SSPurchaseOrderTableModel getModel() {
        return iModel;
    }

    private SSPurchaseOrder getPurchaseOrder(SSPurchaseOrder iPurchaseOrder) {
        return SSDB.getInstance().getPurchaseOrder(iPurchaseOrder);
    }

    private List<SSPurchaseOrder> getPurchaseOrders(List<SSPurchaseOrder> iPurchaseOrders) {
        return SSDB.getInstance().getPurchaseOrders(iPurchaseOrders);
    }

    public void updateFrame() {
        iSearchPanel.ApplyFilter(SSDB.getInstance().getPurchaseOrders());
    }
    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        iTabbedPane=null;
        SSPurchaseOrderFrame.cInstance=null;
    }
}
