package se.swedsoft.bookkeeping.gui.creditinvoice;

import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.creditinvoice.util.SSCreditInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.creditinvoice.panel.SSCreditInvoiceSearchPanel;
import se.swedsoft.bookkeeping.data.SSCreditInvoice;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.data.system.SSMail;
import se.swedsoft.bookkeeping.print.SSReportFactory;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSCreditInvoiceMath;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:47:21
 */
public class SSCreditInvoiceFrame extends SSDefaultTableFrame {

    private static SSCreditInvoiceFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( SSCreditInvoiceFrame.cInstance == null || SSCreditInvoiceFrame.cInstance.isClosed() ){
            SSCreditInvoiceFrame.cInstance = new SSCreditInvoiceFrame(pMainFrame, pWidth, pHeight);
        }
        SSCreditInvoiceFrame.cInstance.setVisible(true);
        SSCreditInvoiceFrame.cInstance.deIconize();
        SSCreditInvoiceFrame.cInstance.updateFrame();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSCreditInvoiceFrame getInstance(){
        return SSCreditInvoiceFrame.cInstance;
    }

    private SSTable iTable;

    private SSCreditInvoiceTableModel iModel;

    private SSCreditInvoiceSearchPanel iSearchPanel;

    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSCreditInvoiceFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("creditinvoiceframe.title"), width, height);
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

        JMenuItem iMenuItem;
        // New
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "creditinvoiceframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSCreditInvoiceDialog.newDialog(getMainFrame(), iModel);
            }
        });
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "creditinvoiceframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSCreditInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getCreditInvoice(iSelected);
                }
                if (iSelected != null) {
                    SSCreditInvoiceDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "creditinvoiceframe.creditinvoicegone",iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();


        // Kopiera
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "creditinvoiceframe.copybutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSCreditInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getCreditInvoice(iSelected);
                }
                if (iSelected != null) {
                    SSCreditInvoiceDialog.copyDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "creditinvoiceframe.creditinvoicegone",iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "creditinvoiceframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSCreditInvoice> toDelete = iModel.getObjects(selected);
                deleteSelectedInvoice(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Print
        // ***************************
        SSMenuButton iButton2 = new SSMenuButton("ICON_PRINT", "creditinvoiceframe.printbutton");
        iMenuItem = iButton2.add("creditinvoiceframe.print.creditinvoice", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSCreditInvoice> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getCreditInvoices(iSelected);
                if (iSelected.size() > 0) {
                    SSReportFactory.CreditInvoiceReport(getMainFrame(), iSelected);
                }
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);
        iMenuItem = iButton2.add("creditinvoiceframe.print.emailcreditinvoicereport", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSCreditInvoice iSelected = iModel.getSelectedRow(iTable);
                iSelected = getCreditInvoice(iSelected);
                if(iSelected == null) return;
                if (!SSMail.isOk(iSelected.getCustomer())) {
                    return;
                }
                SSReportFactory.EmailCreditInvoiceReport(getMainFrame(), iSelected);
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);
        iButton2.addSeparator();
        iButton2.add("creditinvoiceframe.print.creditinvoicelist", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.CreditInvoiceListReport( getMainFrame() );

            }
        });
        iToolBar.add(iButton2);


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
        iModel = new SSCreditInvoiceTableModel( );
        iModel.addColumn(SSCreditInvoiceTableModel.COLUMN_PRINTED);
        iModel.addColumn(SSCreditInvoiceTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSCreditInvoiceTableModel.COLUMN_CUSTOMER_NR);
        iModel.addColumn(SSCreditInvoiceTableModel.COLUMN_CUSTOMER_NAME);
        iModel.addColumn(SSCreditInvoiceTableModel.COLUMN_DATE);
        iModel.addColumn(SSCreditInvoiceTableModel.COLUMN_NET_SUM);
        iModel.addColumn(SSCreditInvoiceTableModel.COLUMN_CURRENCY);
        iModel.addColumn(SSCreditInvoiceTableModel.COLUMN_TOTAL_SUM);
        iModel.addColumn(SSCreditInvoiceTableModel.COLUMN_CREDITING);

        iTable = new SSTable();
        iTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        iModel.setupTable(iTable);


        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSCreditInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber;
                if (iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getCreditInvoice(iSelected);
                } else {
                    return;
                }
                if (iSelected != null) {
                    SSCreditInvoiceDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "creditinvoiceframe.creditinvoicegone",iNumber);
                }
             }
        });

        iSearchPanel = new SSCreditInvoiceSearchPanel(iModel);
        JPanel iPanel = new JPanel();

        iPanel.setLayout(new BorderLayout());
        iPanel.add(iSearchPanel,BorderLayout.NORTH);
        iPanel.add(new JScrollPane(iTable), BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,4,2));

        return iPanel;
    }


    /**
     *
     * @return
     */
    public SSCreditInvoiceTableModel getModel() {
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
     */
    /*private void deleteSelectedInvoice() {
        int[] selected = iTable.getSelectedRows();

        if (selected.length == 0) return;

        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "creditinvoiceframe.delete");

        if(iDialog.getResponce()== JOptionPane.YES_OPTION) {
            iModel.delete(selected);
            SSDB.getInstance().notifyUpdated(SSBookkeeping.iCompany);
        }
    }*/

    private void deleteSelectedInvoice(List<SSCreditInvoice> delete) {
        if (delete.size() == 0) {
            return;
        }

        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "creditinvoiceframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSCreditInvoice iCreditInvoice : delete) {
                if (SSPostLock.isLocked("creditinvoice" + iCreditInvoice.getNumber() + SSDB.getInstance().getCurrentCompany().getId())) {
                    new SSErrorDialog(getMainFrame(), "creditinvoiceframe.creditinvoiceopen", iCreditInvoice.getNumber());
                } else {
                    if(SSInvoiceMath.iSaldoMap.containsKey(iCreditInvoice.getCreditingNr())){
                        SSInvoiceMath.iSaldoMap.put(iCreditInvoice.getCreditingNr(),SSInvoiceMath.iSaldoMap.get(iCreditInvoice.getCreditingNr()).add(SSCreditInvoiceMath.getTotalSum(iCreditInvoice)));
                    }
                    SSDB.getInstance().deleteCreditInvoice(iCreditInvoice);
                }
            }
        }

    }

    private SSCreditInvoice getCreditInvoice(SSCreditInvoice iCreditInvoice) {
        return SSDB.getInstance().getCreditInvoice(iCreditInvoice);
    }

    private List<SSCreditInvoice> getCreditInvoices(List<SSCreditInvoice> iCreditInvoices) {
        return SSDB.getInstance().getCreditInvoices(iCreditInvoices);
    }

    public void updateFrame() {
        iSearchPanel.ApplyFilter();
    }

    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        SSCreditInvoiceFrame.cInstance=null;
    }

}
