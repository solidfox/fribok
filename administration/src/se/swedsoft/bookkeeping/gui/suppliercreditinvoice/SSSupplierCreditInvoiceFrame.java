package se.swedsoft.bookkeeping.gui.suppliercreditinvoice;

import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.data.SSSupplierCreditInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.suppliercreditinvoice.panel.SSSupplierCreditInvoiceSearchPanel;
import se.swedsoft.bookkeeping.gui.suppliercreditinvoice.util.SSSupplierCreditinvoiceTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.print.SSReportFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:47:21
 */
public class SSSupplierCreditInvoiceFrame extends SSDefaultTableFrame {

    private static SSSupplierCreditInvoiceFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( cInstance == null || SSSupplierCreditInvoiceFrame.cInstance.isClosed() ){
            cInstance = new SSSupplierCreditInvoiceFrame(pMainFrame, pWidth, pHeight);
        }
        SSSupplierCreditInvoiceFrame.cInstance.setVisible(true);
        SSSupplierCreditInvoiceFrame.cInstance.deIconize();
        SSSupplierCreditInvoiceFrame.cInstance.updateFrame();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSSupplierCreditInvoiceFrame getInstance(){
        return cInstance;
    }


    private SSTable iTable;

    private SSTableModel<SSSupplierCreditInvoice> iModel;

    SSSupplierCreditInvoiceSearchPanel iSearchPanel;
    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSSupplierCreditInvoiceFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("suppliercreditinvoiceframe.title"), width, height);
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

        SSMenuButton iMenuButton;

        // New
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "suppliercreditinvoiceframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSSupplierCreditInvoiceDialog.newDialog(getMainFrame(), iModel);
            }
        });
        toolBar.add(iButton);



        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "suppliercreditinvoiceframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSSupplierCreditInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getSupplierCreditInvoice(iSelected);
                }
                if (iSelected != null) {
                    SSSupplierCreditInvoiceDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "suppliercreditinvoiceframe.suppliercreditinvoicegone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Copy
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "suppliercreditinvoiceframe.copybutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSSupplierCreditInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getSupplierCreditInvoice(iSelected);
                }
                if (iSelected != null) {
                    SSSupplierCreditInvoiceDialog.copyDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "suppliercreditinvoiceframe.suppliercreditinvoicegone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "suppliercreditinvoiceframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSSupplierCreditInvoice> toDelete = iModel.getObjects(selected);
                deleteSelected(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);


        // Print
        // ***************************
        iMenuButton = new SSMenuButton("ICON_PRINT", "suppliercreditinvoiceframe.printbutton");

        iMenuButton.add("suppliercreditinvoiceframe.print.list", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.SupplierCreditInvoiceListReport( getMainFrame() );
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
        iModel = new SSSupplierCreditinvoiceTableModel();
        iModel.addColumn(SSSupplierCreditinvoiceTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSSupplierCreditinvoiceTableModel.COLUMN_CREDITNING);
        iModel.addColumn(SSSupplierCreditinvoiceTableModel.COLUMN_SUPPLIER_NUMBER);
        iModel.addColumn(SSSupplierCreditinvoiceTableModel.COLUMN_SUPPLIER_NAME);
        iModel.addColumn(SSSupplierCreditinvoiceTableModel.COLUMN_DATE);
        //iModel.addColumn(SSSupplierCreditinvoiceTableModel.COLUMN_DUEDATE);
        iModel.addColumn(SSSupplierCreditinvoiceTableModel.COLUMN_NETSUM);
        iModel.addColumn(SSSupplierCreditinvoiceTableModel.COLUMN_CURRENCY);
        iModel.addColumn(SSSupplierCreditinvoiceTableModel.COLUMN_TOTALSUM);



        iTable = new SSTable();

        iModel.setupTable(iTable);

        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSSupplierCreditInvoice iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber;
                if (iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getSupplierCreditInvoice(iSelected);
                } else {
                    return;
                }
                if (iSelected != null) {
                    SSSupplierCreditInvoiceDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "suppliercreditinvoiceframe.suppliercreditinvoicegone", iNumber);
                }
            }
        });

        iSearchPanel = new SSSupplierCreditInvoiceSearchPanel(iModel);
        JPanel iPanel = new JPanel();

        iPanel.setLayout(new BorderLayout());
        iPanel.add(iSearchPanel, BorderLayout.NORTH);
        iPanel.add( new JScrollPane(iTable), BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,4,2));

        return iPanel;
    }


    /**
     *
     * @return
     */
    public SSTableModel<SSSupplierCreditInvoice> getModel(){
        return iModel;
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
     * @param delete
     */
    private void deleteSelected(List<SSSupplierCreditInvoice> delete) {
        if (delete.isEmpty()) {
            return;
        }
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "suppliercreditinvoiceframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSSupplierCreditInvoice iSupplierCreditInvoice : delete) {
                if (SSPostLock.isLocked("suppliercreditinvoice" + iSupplierCreditInvoice.getNumber() + SSDB.getInstance().getCurrentCompany().getId())){
                    new SSErrorDialog(getMainFrame(), "suppliercreditinvoiceframe.suppliercreditinvoiceopen",iSupplierCreditInvoice.getNumber());
                } else {
                    if(SSSupplierInvoiceMath.iSaldoMap.containsKey(iSupplierCreditInvoice.getCreditingNr())){
                        SSSupplierInvoiceMath.iSaldoMap.put(iSupplierCreditInvoice.getCreditingNr(),SSSupplierInvoiceMath.iSaldoMap.get(iSupplierCreditInvoice.getCreditingNr()).add(SSSupplierInvoiceMath.getTotalSum(iSupplierCreditInvoice)));
                    }
                    SSDB.getInstance().deleteSupplierCreditInvoice(iSupplierCreditInvoice);
                }
            }
        }
    }

    private SSSupplierCreditInvoice getSupplierCreditInvoice(SSSupplierCreditInvoice iSupplierCreditInvoice) {
        return SSDB.getInstance().getSupplierCreditInvoice(iSupplierCreditInvoice);
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
