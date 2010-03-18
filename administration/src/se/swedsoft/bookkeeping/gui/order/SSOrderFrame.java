package se.swedsoft.bookkeeping.gui.order;

import se.swedsoft.bookkeeping.SSVersion;
import se.swedsoft.bookkeeping.calc.math.SSOrderMath;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSMail;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.order.panel.SSOrderSearchPanel;
import se.swedsoft.bookkeeping.gui.order.util.SSOrderTableModel;
import se.swedsoft.bookkeeping.gui.purchaseorder.SSPurchaseOrderDialog;
import se.swedsoft.bookkeeping.gui.purchaseorder.SSPurchaseOrderFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.util.components.SSTabbedPanePanel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSInformationDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSInitDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSDefaultFileChooser;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSXMLFileChooser;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.importexport.xml.SSOrderExporter;
import se.swedsoft.bookkeeping.importexport.xml.SSOrderImporter;
import se.swedsoft.bookkeeping.print.SSReportFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:47:21
 */
public class SSOrderFrame extends SSDefaultTableFrame {

    private static SSOrderFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( cInstance == null || SSOrderFrame.cInstance.isClosed() ){
            cInstance = new SSOrderFrame(pMainFrame, pWidth, pHeight);
        }
        SSOrderFrame.cInstance.setVisible(true);
        SSOrderFrame.cInstance.deIconize();
        SSOrderFrame.cInstance.updateFrame();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSOrderFrame getInstance(){
        return cInstance;
    }


    private JTabbedPane iTabbedPane;

    private SSTable iTable;

    private SSOrderTableModel iModel;

    private SSOrderSearchPanel iSearchPanel;

    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSOrderFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("orderframe.title"), width, height);
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

        SSButton iButton;

        JMenuItem iMenuItem;
        // New
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "orderframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSOrderDialog.newDialog(getMainFrame(), iModel);
            }
        });
        toolBar.add(iButton);



        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "orderframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSOrder iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getOrder(iSelected);
                }
                if (iSelected != null) {
                    SSOrderDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "orderframe.ordergone",iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Copy
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "orderframe.copybutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSOrder iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getOrder(iSelected);
                }
                if (iSelected != null) {
                    SSOrderDialog.copyDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "orderframe.ordergone", iNumber);
                }

            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();


        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "orderframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSOrder> toDelete = iModel.getObjects(selected);
                deleteSelectedOrder(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();


        // Create invoice
        // ***************************

        SSMenuButton iButton2 = new SSMenuButton("ICON_INVOICE24", "orderframe.createinvoicebutton");
        iButton2.add("orderframe.customerinvoice", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSOrder> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getOrders(iSelected);
                if (!iSelected.isEmpty()) {
                    SSOrderDialog.invoiceDialog(getMainFrame(), iSelected, iModel);
                }
            }
        });
        iButton2.add("orderframe.cashreceipt", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSOrder> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getOrders(iSelected);
                if (!iSelected.isEmpty()) {
                    SSOrderDialog.cashReceiptDialog(getMainFrame(), iSelected, iModel);
                }
            }
        });
        iButton2.add("orderframe.periodicinvoice", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSOrder> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getOrders(iSelected);
                if (!iSelected.isEmpty()) {
                    SSOrderDialog.periodicInvoiceDialog(getMainFrame(), iSelected, iModel);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton2);
        toolBar.add(iButton2);

        if(!SSVersion.app_title.contains("JFS Fakturering")){
            // Create purchase order from this order
            // ***************************
            iButton = new SSButton("ICON_INVOICE24", "orderframe.createspurchaseorderbutton", new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    List<SSOrder> iSelected = iModel.getSelectedRows(iTable);
                    iSelected = getOrders(iSelected);
                    if(iSelected == null){
                        return;
                    }

                    for (SSOrder iOrder : iSelected) {
                        // if the selected order already has a purchase order assosiated we can't create a new one
                        if(iOrder.getPurchaseOrderNr() != null){
                            SSInformationDialog.showDialog(getMainFrame(), "orderframe.orderhaspurchaseorder", iOrder.getNumber());
                            return;
                        }
                    }

                    Map<SSProduct, Integer> iProductCount = null;
                    try {
                        iProductCount = SSOrderMath.getProductCount(iSelected);
                    } catch (NullPointerException e1) {
                        new SSErrorDialog( getMainFrame(), "orderframe.orderwrong");
                    }

                    SSPurchaseOrder iPurchaseOrder = new SSPurchaseOrder();

                    for (Map.Entry<SSProduct, Integer> ssProductIntegerEntry : iProductCount.entrySet()) {

                        if(ssProductIntegerEntry.getKey().isParcel()) {
                            for (SSProductRow iParcelRow : ssProductIntegerEntry.getKey().getParcelRows()) {
                                SSPurchaseOrderRow iRow = new SSPurchaseOrderRow();

                                iRow.setProduct ( iParcelRow.getProduct(SSDB.getInstance().getProducts()));
                                iRow.setQuantity(ssProductIntegerEntry.getValue() * iParcelRow.getQuantity()  );

                                iPurchaseOrder.getRows().add(iRow);
                            }

                        } else {
                            SSPurchaseOrderRow iRow = new SSPurchaseOrderRow();

                            iRow.setProduct (ssProductIntegerEntry.getKey());
                            iRow.setQuantity(ssProductIntegerEntry.getValue());

                            iPurchaseOrder.getRows().add(iRow);
                        }
                    }


                    if( SSPurchaseOrderFrame.getInstance() != null){
                        SSPurchaseOrderDialog.newDialog(getMainFrame(), iPurchaseOrder, iSelected, SSPurchaseOrderFrame.getInstance().getModel() );
                    } else {
                        SSPurchaseOrderDialog.newDialog(getMainFrame(), iPurchaseOrder, iSelected, null );
                    }
                }


            });
            iTable.addSelectionDependentComponent(iButton);
            toolBar.add(iButton);
            toolBar.addSeparator();
        }
        // Importera
        // ***************************
        iButton2 = new SSMenuButton("ICON_IMPORT", "orderframe.importbutton");
        iButton2.add("orderframe.importxml", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSXMLFileChooser iFilechooser = SSXMLFileChooser.getInstance();
                iFilechooser.setSelectedFile(new File("Orderlista.xml"));

                if( iFilechooser.showOpenDialog( getMainFrame() ) == JFileChooser.APPROVE_OPTION  ){
                    final SSOrderImporter iImporter = new SSOrderImporter(iFilechooser.getSelectedFile());
                    try {
                        SSInitDialog.runProgress(getMainFrame(), "Importerar ordrar", new Runnable(){
                            public void run(){
                                iImporter.doImport();
                            }
                        });
                    } catch (SSImportException e1) {
                        SSErrorDialog.showDialog(getMainFrame(), "", e1.getLocalizedMessage());
                    } catch (Exception e2){
                       e2.printStackTrace();
                    }
                }
            }
        });
        iButton2.add("orderframe.importebutik", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSDefaultFileChooser iFilechooser = new SSDefaultFileChooser();
                //iFilechooser.setSelectedFile(new File("Orderlista.xml"));

                if( iFilechooser.showOpenDialog( getMainFrame() ) == JFileChooser.APPROVE_OPTION  ){
                    final SSOrderImporter iImporter = new SSOrderImporter(iFilechooser.getSelectedFile());
                    try {
                        SSInitDialog.runProgress(getMainFrame(), "Importerar ordrar", new Runnable(){
                            public void run(){
                                iImporter.doEbutikImport();
                            }
                        });
                    } catch (SSImportException e1) {
                        SSErrorDialog.showDialog(getMainFrame(), "", e1.getLocalizedMessage());
                    } catch (Exception e2){
                       e2.printStackTrace();
                    }
                }
            }
        });
        toolBar.add(iButton2);
        // Exportera
        // ***************************
        iButton = new SSButton("ICON_EXPORT", "orderframe.exportbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSOrder> iItems;
                List<SSOrder> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getOrders(iSelected);
                if( iSelected != null) {
                    int select = SSQueryDialog.showDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, getTitle(), SSBundle.getBundle().getString("orderframe.exportallorselected"));
                    switch(select){
                        case JOptionPane.YES_OPTION:
                            iItems = iSelected;
                            break;
                        case JOptionPane.NO_OPTION :
                            iItems = SSDB.getInstance().getOrders();
                            break;
                        default:
                            return;
                    }
                } else {
                    iItems = SSDB.getInstance().getOrders();
                }
                if (!iItems.isEmpty()) {

                    SSXMLFileChooser iFilechooser = SSXMLFileChooser.getInstance();
                    iFilechooser.setSelectedFile(new File("Orderlista.xml"));

                    if( iFilechooser.showSaveDialog( getMainFrame() ) == JFileChooser.APPROVE_OPTION  ){
                        SSOrderExporter iExporter = new SSOrderExporter(iFilechooser.getSelectedFile(), iItems);
                        iExporter.doExport();  
                    }
                }
            }
        });
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Print
        // ***************************
        iButton2 = new SSMenuButton("ICON_PRINT", "orderframe.printbutton");
        iMenuItem = iButton2.add("orderframe.print.orderreport", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSOrder> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getOrders(iSelected);
                if (!iSelected.isEmpty()) {
                    SSReportFactory.OrderReport(getMainFrame(), iSelected);
                }
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);
        iMenuItem = iButton2.add("orderframe.print.emailorderreport", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSOrder iSelected = iModel.getSelectedRow(iTable);
                iSelected = getOrder(iSelected);
                if(iSelected == null) return;
                if (!SSMail.isOk(iSelected.getCustomer())) {
                    return;
                }
                SSReportFactory.EmailOrderReport(getMainFrame(), iSelected);
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);
        iMenuItem = iButton2.add("orderframe.print.deliverynote", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSOrder> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getOrders(iSelected);
                if (!iSelected.isEmpty()) {
                    SSReportFactory.DeliverynoteReport(getMainFrame(), iSelected);
                }
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);
        iMenuItem = iButton2.add("orderframe.print.pickingslip", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSOrder> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getOrders(iSelected);
                if (!iSelected.isEmpty()) {
                    SSReportFactory.PickingslipReport(getMainFrame(), iSelected);
                }
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);
        iButton2.addSeparator();
        iButton2.add("orderframe.print.orderlistreport", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.OrderListReport( getMainFrame() );
            }
        });
        toolBar.add(iButton2);




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
        iModel = new SSOrderTableModel();
        iModel.addColumn( SSOrderTableModel.COLUMN_PRINTED);
        iModel.addColumn( SSOrderTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSOrderTableModel.COLUMN_CUSTOMER_NR );
        iModel.addColumn( SSOrderTableModel.COLUMN_CUSTOMER_NAME );
        iModel.addColumn( SSOrderTableModel.COLUMN_DATE );
        iModel.addColumn( SSOrderTableModel.COLUMN_NET_SUM );
        iModel.addColumn( SSOrderTableModel.COLUMN_CURRENCY );
        iModel.addColumn( SSOrderTableModel.COLUMN_ESTIMATED_DELIVERY );
        iModel.addColumn( SSOrderTableModel.COLUMN_INVOICE );
        if(!SSVersion.app_title.contains("JFS Fakturering"))
            iModel.addColumn( SSOrderTableModel.COLUMN_PURCHASEORDER );


        iModel.setupTable(iTable);


        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSOrder iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber;
                if (iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getOrder(iSelected);
                } else {
                    return;
                }
                if (iSelected != null) {
                    SSOrderDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "orderframe.ordergone",iNumber);
                }
            }
        });
        iTabbedPane = new JTabbedPane();

        iTabbedPane.add(SSBundle.getBundle().getString("orderframe.filter.1"), new SSTabbedPanePanel() );
        iTabbedPane.add(SSBundle.getBundle().getString("orderframe.filter.2"), new SSTabbedPanePanel() );

        iTabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iSearchPanel.ApplyFilter(SSDB.getInstance().getOrders());
            }
        });
        //setFilterIndex(0);

        //iModel.setObjects(SSDB.getInstance().getOrders());
        JPanel iPanel = new JPanel();
        iSearchPanel = new SSOrderSearchPanel(iModel);
        iPanel.setLayout(new BorderLayout());
        iPanel.add(iSearchPanel,BorderLayout.NORTH);
        iPanel.add(iTabbedPane, BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,4,2));

        return iPanel;
    }

    /**
     *
     * @param index
     * @param iOrders
     */
    public void setFilterIndex(int index,List<SSOrder> iOrders){
        JPanel iPanel = (JPanel)iTabbedPane.getComponentAt( index );

        iPanel.removeAll();
        iPanel.add(new JScrollPane(iTable), BorderLayout.CENTER);

        List<SSOrder> iFiltered = Collections.emptyList();

        List<SSInvoice> iInvoices = SSDB.getInstance().getInvoices();

        switch(index){
            // Alla
            case 0:
                iFiltered = iOrders;
                break;
                // Ordrar utan faktura
            case 1:
                iFiltered = new LinkedList<SSOrder>();
                for(SSOrder iOrder : iOrders){

                    if(iOrder.getInvoiceNr() == null && iOrder.getPeriodicInvoiceNr() == null){
                        iFiltered.add(iOrder);
                    }
                }
                break;
        }
        iModel.setObjects(iFiltered);
        iTabbedPane.repaint();
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
        return true;
    }

    /**
     *
     * @return
     */
    public SSOrderTableModel getModel() {
        return iModel;
    }

    public JTabbedPane getTabbedPane() {
        return iTabbedPane;
    }

    /**
     *
     * @param delete
     */
    private void deleteSelectedOrder(List<SSOrder> delete) {
        if (delete.isEmpty()) {
            return;
        }

        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "orderframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSOrder iOrder : delete) {
                if (SSPostLock.isLocked("order" + iOrder.getNumber() + SSDB.getInstance().getCurrentCompany().getId())) {
                    new SSErrorDialog(getMainFrame(), "orderframe.orderopen", iOrder.getNumber());
                } else {
                    List<SSTender> iTenders = new LinkedList<SSTender>(SSDB.getInstance().getTenders());
                    for (SSTender iTender : iTenders) {
                        if (iTender.hasOrder(iOrder)) {
                            iTender.setOrder(null);
                            SSDB.getInstance().updateTender(iTender);
                        }
                    }
                    iTenders = null;
                    SSDB.getInstance().deleteOrder(iOrder);
                }
            }
        }
    }

    private SSOrder getOrder(SSOrder iOrder) {
        return SSDB.getInstance().getOrder(iOrder);
    }

    private List<SSOrder> getOrders(List<SSOrder> iOrders) {
        return SSDB.getInstance().getOrders(iOrders);
    }

    public void updateFrame() {
        iSearchPanel.ApplyFilter(SSDB.getInstance().getOrders());
    }
    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        iTabbedPane=null;
        cInstance=null;
    }


}
