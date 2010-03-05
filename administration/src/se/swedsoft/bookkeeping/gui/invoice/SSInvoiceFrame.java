package se.swedsoft.bookkeeping.gui.invoice;

import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSConfirmDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSTabbedPanePanel;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.periodicinvoice.SSPeriodicInvoiceDialog;
import se.swedsoft.bookkeeping.gui.creditinvoice.SSCreditInvoiceFrame;
import se.swedsoft.bookkeeping.gui.creditinvoice.SSCreditInvoiceDialog;
import se.swedsoft.bookkeeping.gui.inpayment.SSInpaymentFrame;
import se.swedsoft.bookkeeping.gui.inpayment.SSInpaymentDialog;
import se.swedsoft.bookkeeping.gui.invoice.util.SSInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.invoice.dialog.SSInterestInvoiceDialog;
import se.swedsoft.bookkeeping.gui.invoice.panel.SSInvoiceSearchPanel;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.data.system.SSMail;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.*;
import se.swedsoft.bookkeeping.print.SSReportFactory;

import javax.swing.*;
import javax.swing.event.*;
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
public class SSInvoiceFrame extends SSDefaultTableFrame{

    private static SSInvoiceFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( SSInvoiceFrame.cInstance == null || SSInvoiceFrame.cInstance.isClosed() ){
            SSInvoiceFrame.cInstance = new SSInvoiceFrame(pMainFrame, pWidth, pHeight);
        }
        SSInvoiceFrame.cInstance.setVisible(true);
        SSInvoiceFrame.cInstance.deIconize();
        SSInvoiceFrame.cInstance.updateFrame();

        if( SSPeriodicInvoiceMath.hasPendingPeriodicInvoices() && new SSConfirmDialog("periodicinvoiceframe.pendingperiodicinvoices").openDialog(pMainFrame) == JOptionPane.OK_OPTION ){
            if(SSPeriodicInvoiceDialog.pendingPeriodicInvoicesDialog( pMainFrame )){
                SSPostLock.removeLock("periodicinvoicepending"+SSDB.getInstance().getCurrentCompany().getId());
            }
        }
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSInvoiceFrame getInstance(){
        return SSInvoiceFrame.cInstance;
    }

    private JTabbedPane iTabbedPane;

    private SSTable iTable;

    private SSInvoiceTableModel iModel;

    private SSInvoiceSearchPanel iSearchPanel;

    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSInvoiceFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("invoiceframe.title"), width, height);
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

        SSMenuButton<SSButton> iMenuButton;

        JMenuItem iMenuItem;

        // New
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "invoiceframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSInvoiceDialog.newDialog(getMainFrame(), iModel);
            }
        });
        toolBar.add(iButton);



        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "invoiceframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getInvoice(iSelected);
                }
                if (iSelected != null) {
                    SSInvoiceDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "invoiceframe.invoicegone",iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Copy
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "invoiceframe.copybutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getInvoice(iSelected);
                }
                if (iSelected != null) {
                    SSInvoiceDialog.copyDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "invoiceframe.invoicegone",iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "invoiceframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSInvoice> toDelete = iModel.getObjects(selected);
                deleteSelectedInvoice(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();



        // Create inpayment for sales
        // ***************************
        iButton = new SSButton("ICON_COINS24", "invoiceframe.inpaymentbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(iTable.getSelectedRowCount() > 0 ){
                    List<SSInvoice> iSelected = iModel.getObjects(iTable.getSelectedRows());
                    iSelected = getInvoices(iSelected);
                    SSInpayment iInpayment  = new SSInpayment();
                    if (!iSelected.isEmpty()) {
                        iInpayment.addInvoices(iSelected);

                        if (SSInpaymentFrame.getInstance() != null) {
                            SSInpaymentDialog.newDialog(getMainFrame(), iInpayment, SSInpaymentFrame.getInstance().getModel());
                        } else {
                            SSInpaymentDialog.newDialog(getMainFrame(), iInpayment, null);
                        }
                    }
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);

        // Create creditinvoice for sales
        // ***************************
        iButton = new SSButton("ICON_CREATECHANGE", "invoiceframe.creditinvoicebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getInvoice(iSelected);
                }
                if (iSelected != null) {

                    if (SSCreditInvoiceFrame.getInstance() != null) {
                        SSCreditInvoiceDialog.newDialog(getMainFrame(), iSelected, SSCreditInvoiceFrame.getInstance().getModel());
                    } else {
                        SSCreditInvoiceDialog.newDialog(getMainFrame(), iSelected, null);
                    }
                } else {
                    new SSErrorDialog( getMainFrame(), "invoiceframe.invoicegone",iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);

        // Skriv ut påminellse för valda fakturor
        // ***************************
        iButton = new SSButton("ICON_EXCLAMATION24", "invoiceframe.reminderbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSInvoice> iSelected = iModel.getObjects(iTable.getSelectedRows());
                iSelected = getInvoices(iSelected);
                if (!iSelected.isEmpty()) {
                    SSReportFactory.ReminderReport(getMainFrame(), iSelected);
                    //updateFrame();
                }
                //iModel.fireTableDataChanged();
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);


        // Skapa räntefakturor
        // ***************************
        iButton = new SSButton("ICON_INVOICE24", "invoiceframe.interestinvoicebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSInterestInvoiceDialog.showDialog(getMainFrame(), iModel);
            }
        });
        toolBar.add(iButton);

        toolBar.addSeparator();


        // Print
        // ***************************
        iMenuButton = new SSMenuButton<SSButton>("ICON_PRINT", "invoiceframe.printbutton");
        iMenuItem = iMenuButton.add("invoiceframe.print.invoicereport", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSInvoice> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getInvoices(iSelected);
                if (!iSelected.isEmpty()) {
                    SSReportFactory.InvoiceReport(getMainFrame(), iSelected);
                }
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);
        iMenuItem = iMenuButton.add("invoiceframe.print.emailinvoicereport", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSInvoice iSelected = iModel.getSelectedRow(iTable);
                iSelected = getInvoice(iSelected);
                if(iSelected == null) return;
                if (!SSMail.isOk(iSelected.getCustomer())) {
                    return;
                }
                SSReportFactory.EmailInvoiceReport(getMainFrame(), iSelected);
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);
        iMenuItem = iMenuButton.add("invoiceframe.print.reminder", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSInvoice> iSelected = iModel.getObjects(iTable.getSelectedRows());
                iSelected = getInvoices(iSelected);
                if (!iSelected.isEmpty()) {
                    SSReportFactory.ReminderReport(getMainFrame(), iSelected);
                }
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);

        iMenuButton.addSeparator();
        iMenuItem = iMenuButton.add("invoiceframe.print.ocrinvoicereport", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSInvoice> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getInvoices(iSelected);
                if (!iSelected.isEmpty()) {
                    SSReportFactory.OCRInvoiceReport(getMainFrame(), iSelected);
                }
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);


        iMenuButton.addSeparator();
        iMenuButton.add("invoiceframe.print.invoicelistreport", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.InvoiceListReport( getMainFrame() );
            }
        });
        toolBar.add(iMenuButton);


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

        iModel = new SSInvoiceTableModel();
        iModel.addColumn(SSInvoiceTableModel.COLUMN_PRINTED);
        iModel.addColumn(SSInvoiceTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSInvoiceTableModel.COLUMN_TYPE);
        iModel.addColumn(SSInvoiceTableModel.COLUMN_CUSTOMER_NR);
        iModel.addColumn(SSInvoiceTableModel.COLUMN_CUSTOMER_NAME);
        iModel.addColumn(SSInvoiceTableModel.COLUMN_DATE);
        iModel.addColumn(SSInvoiceTableModel.COLUMN_DUEDATE);
        iModel.addColumn(SSInvoiceTableModel.COLUMN_NET_SUM);
        iModel.addColumn(SSInvoiceTableModel.COLUMN_CURRENCY);
        iModel.addColumn(SSInvoiceTableModel.COLUMN_CURRENCY_RATE);
        iModel.addColumn(SSInvoiceTableModel.COLUMN_TOTAL_SUM);
        //iModel.addColumn(SSInvoiceTableModel.COLUMN_SALDO);
        iModel.addColumn(SSInvoiceTableModel.getSaldoColumn());
        iModel.addColumn(SSInvoiceTableModel.COLUMN_REMINDERS);

       iModel.setupTable(iTable);

        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber;
                if (iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getInvoice(iSelected);
                } else {
                    return;
                }
                if (iSelected != null) {
                    SSInvoiceDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "invoiceframe.invoicegone",iNumber);
                }
            }
        });

        iTabbedPane = new JTabbedPane();

        iTabbedPane.add(SSBundle.getBundle().getString("invoiceframe.filter.1"), new SSTabbedPanePanel() );
        iTabbedPane.add(SSBundle.getBundle().getString("invoiceframe.filter.2"), new SSTabbedPanePanel() );
        iTabbedPane.add(SSBundle.getBundle().getString("invoiceframe.filter.3"), new SSTabbedPanePanel() );

        iTabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iSearchPanel.ApplyFilter(SSDB.getInstance().getInvoices());
            }
        });
        //setFilterIndex(0);


        JPanel iPanel = new JPanel();
        iSearchPanel = new SSInvoiceSearchPanel(iModel);
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
    public void setFilterIndex(int index,List<SSInvoice> iInvoices){
        JPanel iPanel = (JPanel)iTabbedPane.getComponentAt( index );

        iPanel.removeAll();
        iPanel.add(new JScrollPane(iTable), BorderLayout.CENTER);


        List<SSInvoice> iFiltered = Collections.emptyList();
        //


        switch(index){
            // Alla
            case 0:
                iFiltered = iInvoices;
                break;
                // Obetalda
            case 1:
                iFiltered = new LinkedList<SSInvoice>();
                for (SSInvoice iInvoice : iInvoices) {
                    /*if( SSInvoiceMath.getSaldo(iInvoice).signum() != 0){
                        iFiltered.add(iInvoice);
                    }*/
                    if(SSInvoiceMath.iSaldoMap.containsKey(iInvoice.getNumber())){
                        if(SSInvoiceMath.iSaldoMap.get(iInvoice.getNumber()).signum() != 0)
                            iFiltered.add(iInvoice);
                    }
                }
                break;
                // Förfallna
            case 2:
                iFiltered = new LinkedList<SSInvoice>();
                for (SSInvoice iInvoice : iInvoices) {
                    if(SSInvoiceMath.iSaldoMap.containsKey(iInvoice.getNumber())){
                        if(SSInvoiceMath.iSaldoMap.get(iInvoice.getNumber()).signum() != 0 && SSInvoiceMath.expired(iInvoice) ){
                            iFiltered.add(iInvoice);
                        }
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
    public SSInvoiceTableModel getModel() {
        return iModel;
    }

    public JTabbedPane getTabbedPane() {
        return iTabbedPane;
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
     */
    private void deleteSelectedInvoice(List<SSInvoice> delete) {
        if (delete.isEmpty()) {
            return;
        }

        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "invoiceframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSInvoice iInvoice : delete) {
                if (SSPostLock.isLocked("invoice" + iInvoice.getNumber() + SSDB.getInstance().getCurrentCompany().getId())) {
                    new SSErrorDialog(getMainFrame(), "invoiceframe.invoiceopen", iInvoice.getNumber());
                } else {
                    List<SSOrder> iOrdersToUpdate = new LinkedList<SSOrder>();
                    for (SSOrder iOrder : SSDB.getInstance().getOrders()) {
                        if (iOrder.hasInvoice(iInvoice)) {
                            iOrder.setInvoice(null);
                            iOrdersToUpdate.add(iOrder);
                        }
                    }
                    for(SSOrder iOrder : iOrdersToUpdate){
                        SSDB.getInstance().updateOrder(iOrder);
                    }
                    iOrdersToUpdate = null;
                    int iIndex = SSCustomerMath.iInvoicesForCustomers.get(iInvoice.getCustomerNr()).indexOf(iInvoice);
                    if(iIndex != -1){
                        SSCustomerMath.iInvoicesForCustomers.get(iInvoice.getCustomerNr()).remove(iIndex);
                    }
                    SSDB.getInstance().deleteInvoice(iInvoice);
                }
            }
        }
    }

    private SSInvoice getInvoice(SSInvoice iInvoice) {
        return SSDB.getInstance().getInvoice(iInvoice);
    }

    private List<SSInvoice> getInvoices(List<SSInvoice> iInvoices) {
        return SSDB.getInstance().getInvoices(iInvoices);
    }

    /**
     *
     */
    public static void fireTableDataChanged() {
        if(cInstance != null) cInstance.getModel().fireTableDataChanged();
    }

    public void updateFrame() {
        iSearchPanel.ApplyFilter(SSDB.getInstance().getInvoices());
    }

    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        SSInvoiceFrame.cInstance=null;
    }
}
