package se.swedsoft.bookkeeping.gui.supplierinvoice;

import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSTabbedPanePanel;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.outpayment.SSOutpaymentDialog;
import se.swedsoft.bookkeeping.gui.outpayment.SSOutpaymentFrame;
import se.swedsoft.bookkeeping.gui.supplierpayments.SSSupplierPaymentDialog;
import se.swedsoft.bookkeeping.gui.suppliercreditinvoice.SSSupplierCreditInvoiceDialog;
import se.swedsoft.bookkeeping.gui.suppliercreditinvoice.SSSupplierCreditInvoiceFrame;
import se.swedsoft.bookkeeping.gui.supplierinvoice.util.SSSupplierInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.supplierinvoice.panel.SSSupplierInvoiceSearchPanel;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSSupplierMath;
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
public class SSSupplierInvoiceFrame extends SSDefaultTableFrame {

    private static SSSupplierInvoiceFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( SSSupplierInvoiceFrame.cInstance == null || SSSupplierInvoiceFrame.cInstance.isClosed() ){
            SSSupplierInvoiceFrame.cInstance = new SSSupplierInvoiceFrame(pMainFrame, pWidth, pHeight);
        }
        SSSupplierInvoiceFrame.cInstance.setVisible(true);
        SSSupplierInvoiceFrame.cInstance.deIconize();
        SSSupplierInvoiceFrame.cInstance.updateFrame();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSSupplierInvoiceFrame getInstance(){
        return cInstance;
    }


    private SSTable iTable;

    private SSSupplierInvoiceTableModel iModel;

    private JTabbedPane iTabbedPane;

    private SSSupplierInvoiceSearchPanel iSearchPanel;
    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSSupplierInvoiceFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("supplierinvoiceframe.title"), width, height);
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



        // New
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "supplierinvoiceframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSSupplierInvoiceDialog.newDialog(getMainFrame(), iModel);
            }
        });
        iToolBar.add(iButton);

        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "supplierinvoiceframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSSupplierInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected!= null){
                    iNumber = iSelected.getNumber();
                    iSelected = getSupplierInvoice(iSelected);
                }
                if (iSelected != null) {
                    SSSupplierInvoiceDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "supplierinvoiceframe.supplierinvoicegone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Copy
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "supplierinvoiceframe.copybutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSSupplierInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected!= null){
                    iNumber = iSelected.getNumber();
                    iSelected = getSupplierInvoice(iSelected);
                }
                if(iSelected != null){
                    SSSupplierInvoiceDialog.copyDialog(getMainFrame(), iSelected, iModel);
                }
                else {
                    new SSErrorDialog(getMainFrame(), "supplierinvoiceframe.supplierinvoicegone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();
        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "supplierinvoiceframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSSupplierInvoice> toDelete = iModel.getObjects(selected);
                deleteSelected(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Create credit invoice
        // ***************************
        iButton = new SSButton("ICON_INVOICE24", "supplierinvoiceframe.suppliercreditinvoicebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSSupplierInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected!= null){
                    iNumber = iSelected.getNumber();
                    iSelected = getSupplierInvoice(iSelected);
                }
                if (iSelected != null) {

                    if (SSSupplierCreditInvoiceFrame.getInstance() != null) {
                        SSSupplierCreditInvoiceDialog.newDialog(getMainFrame(), iSelected, SSSupplierCreditInvoiceFrame.getInstance().getModel() );
                    } else {
                        SSSupplierCreditInvoiceDialog.newDialog(getMainFrame(), iSelected, null );
                    }
                } else {
                    new SSErrorDialog(getMainFrame(), "supplierinvoiceframe.supplierinvoicegone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);

        // Skapa utbetalning
        // ***************************
        iButton = new SSButton("ICON_COINS24", "supplierinvoiceframe.outpaymentbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(iTable.getSelectedRowCount() > 0 ){
                    List<SSSupplierInvoice> iSelected = iModel.getObjects(iTable.getSelectedRows());
                    iSelected = getSupplierInvoices(iSelected);
                    SSOutpayment iOutpayment  = new SSOutpayment();
                    if (iSelected.size() > 0) {
                        iOutpayment.addInvoices(iSelected);

                        if (SSOutpaymentFrame.getInstance() != null) {
                            SSOutpaymentDialog.newDialog(getMainFrame(), iOutpayment, SSOutpaymentFrame.getInstance().getModel() );
                        } else {
                            SSOutpaymentDialog.newDialog(getMainFrame(), iOutpayment, null );
                        }
                    }
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Skapa leverantörsbetalningar
        // ***************************
        iButton = new SSButton("Task List 24", "supplierinvoiceframe.createsupplierpayment", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                final String lockString = "supplierpayment"+SSDB.getInstance().getCurrentCompany().getId();
                if (!SSPostLock.applyLock(lockString)) {
                    new SSErrorDialog( getMainFrame(), "supplierinvoiceframe.supplierpayment");
                    return;
                }

                List<SSSupplierInvoice> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getSupplierInvoices(iSelected);
                if(iSelected.size() == 0) {
                    SSPostLock.removeLock(lockString);
                    return;
                }
                // Filter by currency, only SEK and EUR is supported
                List<SSSupplierInvoice> iFiltered = new LinkedList<SSSupplierInvoice>();

                for (SSSupplierInvoice iSupplierInvoice : iSelected) {

                    if(iSupplierInvoice.isBGCEntered()) continue;

                    SSCurrency iCurrency = iSupplierInvoice.getCurrency();

                    if( iCurrency != null && ( "SEK".equals(iCurrency.getName()) ||  "EUR".equals(iCurrency.getName())) ){
                        iFiltered.add(iSupplierInvoice);
                    }
                }
                SSSupplierPaymentDialog iDialog = new SSSupplierPaymentDialog(getMainFrame(), iFiltered);

                iDialog.setSize(800, 600);
                iDialog.setLocationRelativeTo(getMainFrame());
                iDialog.showDialog();

            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();



        // Print
        // ***************************
        iMenuButton = new SSMenuButton("ICON_PRINT", "supplierinvoiceframe.printbutton");

        iMenuButton.add("supplierinvoiceframe.print.list", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.SupplierInvoiceListReport( getMainFrame() );
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
        iModel = new SSSupplierInvoiceTableModel();
        iModel.addColumn(SSSupplierInvoiceTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSSupplierInvoiceTableModel.COLUMN_REFERENCE_NUMBER);
        iModel.addColumn(SSSupplierInvoiceTableModel.COLUMN_SUPPLIER_NUMBER);
        iModel.addColumn(SSSupplierInvoiceTableModel.COLUMN_SUPPLIER_NAME);
        iModel.addColumn(SSSupplierInvoiceTableModel.COLUMN_DATE);
        iModel.addColumn(SSSupplierInvoiceTableModel.COLUMN_DUEDATE);
        iModel.addColumn(SSSupplierInvoiceTableModel.COLUMN_NETSUM);
        iModel.addColumn(SSSupplierInvoiceTableModel.COLUMN_CURRENCY);
        iModel.addColumn(SSSupplierInvoiceTableModel.COLUMN_TOTALSUM);
        iModel.addColumn(SSSupplierInvoiceTableModel.COLUMN_SALDO);



        iTable = new SSTable();

        iModel.setupTable(iTable);

        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSSupplierInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber;
                if (iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getSupplierInvoice(iSelected);
                } else {
                    return;
                }
                if (iSelected != null) {
                    SSSupplierInvoiceDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "supplierinvoiceframe.supplierinvoicegone", iNumber);
                }
            }
        });


        iTabbedPane = new JTabbedPane();

        iTabbedPane.add(SSBundle.getBundle().getString("supplierinvoiceframe.filter.1"), new SSTabbedPanePanel() );
        iTabbedPane.add(SSBundle.getBundle().getString("supplierinvoiceframe.filter.2"), new SSTabbedPanePanel() );
        iTabbedPane.add(SSBundle.getBundle().getString("supplierinvoiceframe.filter.3"), new SSTabbedPanePanel() );

        iTabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateFrame();
            }
        });
        //setFilterIndex(0);

        JPanel iPanel = new JPanel();
        iSearchPanel = new SSSupplierInvoiceSearchPanel(iModel);
        iPanel.setLayout(new BorderLayout());
        iPanel.add(iSearchPanel,BorderLayout.NORTH);
        iPanel.add( iTabbedPane, BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,4,2));

        return iPanel;
    }
    /**
     *
     * @param index
     */
    public void setFilterIndex(int index,List<SSSupplierInvoice> iInvoices){
        JPanel iPanel = (JPanel)iTabbedPane.getComponentAt( index );

        iPanel.removeAll();
        iPanel.add(new JScrollPane(iTable), BorderLayout.CENTER);

        List<SSSupplierInvoice> iFiltered = Collections.emptyList();

        //

        switch(index){
            // Alla
            case 0:
                iFiltered = iInvoices;
                break;
                // Obetalda
            case 1:
                iFiltered = new LinkedList<SSSupplierInvoice>();
                for (SSSupplierInvoice iInvoice : iInvoices) {
                    if( SSSupplierInvoiceMath.getSaldo(iInvoice.getNumber()).signum() != 0){
                        iFiltered.add(iInvoice);
                    }
                }
                break;
                // Förfallna
            case 2:
                iFiltered = new LinkedList<SSSupplierInvoice>();
                for (SSSupplierInvoice iInvoice : iInvoices) {
                    if( SSSupplierInvoiceMath.getSaldo(iInvoice.getNumber()).signum() != 0 && SSSupplierInvoiceMath.expired(iInvoice) ){


                        iFiltered.add(iInvoice);
                    }
                }
                break;
        }
        iModel.setObjects(iFiltered);
        iTabbedPane.repaint();
    }


    /**
     *
     * @return
     */
    public SSTableModel getModel(){
        return iModel;
    }

    public JTabbedPane getTabbedPane() {
        return iTabbedPane;
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
     */
    private void deleteSelected(List<SSSupplierInvoice> delete) {
        if (delete.size() == 0) {
            return;
        }
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "supplierinvoiceframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSSupplierInvoice iSupplierInvoice : delete) {
                if (SSPostLock.isLocked("supplierinvoice" + iSupplierInvoice.getNumber() + SSDB.getInstance().getCurrentCompany().getId())){
                    new SSErrorDialog(getMainFrame(), "supplierinvoiceframe.supplierinvoiceopen",iSupplierInvoice.getNumber());
                } else {
                    for (SSPurchaseOrder iPurchaseOrder : SSDB.getInstance().getPurchaseOrders()) {
                        if (iPurchaseOrder.hasInvoice(iSupplierInvoice)) {
                            iPurchaseOrder.setInvoice(null);
                            SSDB.getInstance().updatePurchaseOrder(iPurchaseOrder);
                        }
                    }
                    int iIndex = SSSupplierMath.iInvoicesForSuppliers.get(iSupplierInvoice.getSupplierNr()).indexOf(iSupplierInvoice);
                    if(iIndex != -1){
                        SSSupplierMath.iInvoicesForSuppliers.get(iSupplierInvoice.getSupplierNr()).remove(iIndex);
                    }
                    SSDB.getInstance().deleteSupplierInvoice(iSupplierInvoice);
                }
            }
        }
    }

    private SSSupplierInvoice getSupplierInvoice(SSSupplierInvoice iSupplierInvoice) {
        return SSDB.getInstance().getSupplierInvoice(iSupplierInvoice);
    }

    private List<SSSupplierInvoice> getSupplierInvoices(List<SSSupplierInvoice> iSupplierInvoices) {
        return SSDB.getInstance().getSupplierInvoices(iSupplierInvoices);
    }

    public void updateFrame() {
        iSearchPanel.ApplyFilter(SSDB.getInstance().getSupplierInvoices());
    }

    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        iTabbedPane=null;
        SSSupplierInvoiceFrame.cInstance=null;
    }


}
