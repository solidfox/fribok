package se.swedsoft.bookkeeping.gui.customer;

import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.customer.panel.SSCustomerSearchPanel;
import se.swedsoft.bookkeeping.gui.customer.util.SSCustomerTableModel;
import se.swedsoft.bookkeeping.gui.exportbgcadmission.SSExportBGCAdmissionDialog;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSInitDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSProgressDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSDefaultFileChooser;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSExcelFileChooser;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSXMLFileChooser;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.importexport.excel.SSCustomerExporter;
import se.swedsoft.bookkeeping.importexport.excel.SSCustomerImporter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.print.dialog.SSPeriodSelectionDialog;
import se.swedsoft.bookkeeping.print.report.SSCustomerListPrinter;
import se.swedsoft.bookkeeping.print.report.SSCustomerRevenuePrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:47:21
 */
public class SSCustomerFrame extends SSDefaultTableFrame {

    private static SSCustomerFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( SSCustomerFrame.cInstance == null || SSCustomerFrame.cInstance.isClosed() ){
            SSCustomerFrame.cInstance = new SSCustomerFrame(pMainFrame, pWidth, pHeight);
        }
        SSCustomerFrame.cInstance.setVisible(true);
        SSCustomerFrame.cInstance.deIconize();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSCustomerFrame getInstance(){
        return SSCustomerFrame.cInstance;
    }


    private SSTable iTable;

    private SSCustomerTableModel iModel;

    private SSCustomerSearchPanel iSearchPanel;

    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSCustomerFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("customerframe.title"), width, height);
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


        // New
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "customerframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSCustomerDialog.newDialog(getMainFrame(), iModel);
            }
        });
        iToolBar.add(iButton);

        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "customerframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSCustomer iSelected = iModel.getSelectedRow(iTable);
                String iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getCustomer(iSelected);
                }
                if (iSelected != null) {
                    SSCustomerDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "customerframe.customergone", iNumber);
                }
            }
        });
        iToolBar.add(iButton);
        iToolBar.addSeparator();
        iTable.addSelectionDependentComponent(iButton);


        // Copy
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "customerframe.copybutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSCustomer iSelected = iModel.getSelectedRow(iTable);
                String iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getCustomer(iSelected);
                }
                if(iSelected != null){
                    SSCustomerDialog.copyDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "customerframe.customergone", iNumber);
                }
            }
        });
        iToolBar.add(iButton);
        iToolBar.addSeparator();
        iTable.addSelectionDependentComponent(iButton);


        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "customerframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSCustomer> toDelete = iModel.getObjects(selected);
                deleteSelectedCustomers(toDelete);
            }
        });
        iToolBar.add(iButton);
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.addSeparator();


        // Importera
        // ***************************
        SSMenuButton iButton2 = new SSMenuButton("ICON_IMPORT", "customerframe.importbutton");
        iButton2.add("customerframe.import.excel", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSExcelFileChooser iFilechooser = SSExcelFileChooser.getInstance();

                if( iFilechooser.showOpenDialog( getMainFrame() ) == JFileChooser.APPROVE_OPTION  ){
                    final SSCustomerImporter iImporter = new SSCustomerImporter( iFilechooser.getSelectedFile() );

                    try {
                        SSInitDialog.runProgress(getMainFrame(), "Importerar kunder", new Runnable(){
                            public void run(){
                                iImporter.Import();
                            }
                        });
                    } catch (SSImportException ex) {
                        SSErrorDialog.showDialog( getMainFrame(), "", ex.getLocalizedMessage() );
                    }
                    iModel.fireTableDataChanged();
                }

            }
        });
        iButton2.add("customerframe.import.xml", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSXMLFileChooser iFilechooser = SSXMLFileChooser.getInstance();
                iFilechooser.setSelectedFile(new File("Kundlista.xml"));

                if( iFilechooser.showOpenDialog( getMainFrame() ) == JFileChooser.APPROVE_OPTION  ){
                    final SSCustomerImporter iImporter = new SSCustomerImporter(iFilechooser.getSelectedFile());
                    try {
                        SSInitDialog.runProgress(getMainFrame(), "Importerar kunder", new Runnable(){
                            public void run(){
                                iImporter.doImport();
                            }
                        });
                    } catch (SSImportException e1){
                        SSErrorDialog.showDialog( getMainFrame(), "", e1.getLocalizedMessage() );
                    }

                }
            }
        });
        iButton2.add("customerframe.import.ebutik", new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SSDefaultFileChooser iFileChooser = new SSDefaultFileChooser();

                if(iFileChooser.showOpenDialog(getMainFrame()) == JFileChooser.APPROVE_OPTION){
                    final SSCustomerImporter iImporter = new SSCustomerImporter(iFileChooser.getSelectedFile());
                    try{
                        SSInitDialog.runProgress(getMainFrame(), "Importerar kunder", new Runnable(){
                            public void run(){
                                iImporter.doEbutikImport();
                            }
                        });
                    } catch (SSImportException e1){
                        SSErrorDialog.showDialog(getMainFrame(), "" , e1.getLocalizedMessage());
                    }
                }
            }
        });
        iToolBar.add(iButton2);
        // Exportera
        // ***************************
        iButton2 = new SSMenuButton("ICON_EXPORT", "customerframe.exportbutton");
        iButton2.add("customerframe.export.excel", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSExcelFileChooser iFilechooser = SSExcelFileChooser.getInstance();

                List<SSCustomer> iItems;
                List<SSCustomer> iSelected = iModel.getSelectedRows(iTable);

                if( iSelected != null) {
                    int select = SSQueryDialog.showDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, getTitle(), SSBundle.getBundle().getString("customerframe.import.allorselected"));
                    switch(select){
                        case JOptionPane.YES_OPTION:
                            iItems = iSelected;
                            iItems = getCustomers(iItems);
                            break;
                        case JOptionPane.NO_OPTION :
                            iItems = SSDB.getInstance().getCustomers();
                            break;
                        default:
                            return;
                    }
                } else {
                    iItems = SSDB.getInstance().getCustomers();
                }

                iFilechooser.setSelectedFile(new File("Kundlista.xls"));

                if( iFilechooser.showSaveDialog( getMainFrame() ) == JFileChooser.APPROVE_OPTION  ){
                    SSCustomerExporter iExporter = new SSCustomerExporter( iFilechooser.getSelectedFile(), iItems );

                    try {
                        iExporter.export();
                    } catch (IOException ex) {
                        SSErrorDialog.showDialog( getMainFrame(), "", ex.getLocalizedMessage() );
                    } catch (SSExportException ex) {
                        SSErrorDialog.showDialog( getMainFrame(), "", ex.getLocalizedMessage() );
                    }
                }
            }
        });
        iButton2.add("customerframe.export.xml", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSCustomer> iItems;
                List<SSCustomer> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getCustomers(iSelected);
                if( iSelected != null) {
                    int select = SSQueryDialog.showDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, getTitle(), SSBundle.getBundle().getString("customerframe.import.allorselected"));
                    switch(select){
                        case JOptionPane.YES_OPTION:
                            iItems = iSelected;
                            break;
                        case JOptionPane.NO_OPTION :
                            iItems = SSDB.getInstance().getCustomers();
                            break;
                        default:
                            return;
                    }
                } else {
                    iItems = SSDB.getInstance().getCustomers();
                }
                if (!iItems.isEmpty()) {

                    SSXMLFileChooser iFilechooser = SSXMLFileChooser.getInstance();
                    iFilechooser.setSelectedFile(new File("Kundlista.xml"));

                    if( iFilechooser.showSaveDialog( getMainFrame() ) == JFileChooser.APPROVE_OPTION  ){
                        SSCustomerExporter iExporter = new SSCustomerExporter(iFilechooser.getSelectedFile(), iItems);
                        iExporter.doXMLExport();
                    }
                }
            }

        });
        iToolBar.add(iButton2);
        iToolBar.addSeparator();

        // Print
        // ***************************
        iButton2 = new SSMenuButton("ICON_PRINT", "customerframe.printbutton");
        iButton2.add("customerframe.print.customerrevenue", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                CustomerRevenueReport();
            }
        });
        iButton2.add("customerframe.print.customerlist", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                CustomerListReport();
            }
        });
        iToolBar.add(iButton2);
        /*iToolBar.addSeparator();
        iButton2 = new SSMenuButton("ICON_INVOICE24", "customerframe.bgcadmissionbutton");
        JMenuItem iMenuItem = iButton2.add("customerframe.bgcadmissionexport", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                exportBGCAdmissions();
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);
        iButton2.add("customerframe.bgcadmissionimport", new ActionListener(){
            public void actionPerformed(ActionEvent e) {

            }
        });
        iToolBar.add(iButton2);*/
        return iToolBar;
    }

    public void exportBGCAdmissions() {
        final String lockString = "bgcadmission"+SSDB.getInstance().getCurrentCompany().getId();
        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog( getMainFrame(), "customerframe.bgcadmission");
            return;
        }
        List<SSCustomer> iSelected = iModel.getSelectedRows(iTable);
        iSelected = getCustomers(iSelected);
        if(iSelected.isEmpty()) {
            SSPostLock.removeLock(lockString);
            return;
        }

        List<SSCustomer> iFiltered = new LinkedList<SSCustomer>();

        for (SSCustomer iCustomer : iSelected) {

        }
        SSExportBGCAdmissionDialog iDialog = new SSExportBGCAdmissionDialog(getMainFrame(), iSelected);

        iDialog.setSize(800, 600);
        iDialog.setLocationRelativeTo(getMainFrame());
        iDialog.showDialog();
    }

    /**
     * This method should return the main content for the frame. <P>
     * Such as an object table.
     *
     * @return The main content for this frame.
     */
    @Override
    public JComponent getMainContent() {
        iModel = new SSCustomerTableModel();
        iModel.addColumn( SSCustomerTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSCustomerTableModel.COLUMN_NAME );
        iModel.addColumn( SSCustomerTableModel.COLUMN_YOUR_CONTACT );
        iModel.addColumn( SSCustomerTableModel.COLUMN_REGISTRATION_NUMBER );
        iModel.addColumn( SSCustomerTableModel.COLUMN_PHONE );
        iModel.addColumn( SSCustomerTableModel.COLUMN_CUSTOMER_CLAIM );
        iModel.addColumn( SSCustomerTableModel.COLUMN_CREDIT_LIMIT );

        iTable = new SSTable();

        iModel.setupTable(iTable);

        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSCustomer iSelected = iModel.getSelectedRow(iTable);
                String iNumber = null;
                if (iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getCustomer(iSelected);
                } else {
                    return;
                }
                if (iSelected != null) {
                    SSCustomerDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "customerframe.customergone", iNumber);
                }
            }
        });
        iSearchPanel = new SSCustomerSearchPanel(iModel);

        JPanel iPanel = new JPanel();

        iPanel.setLayout(new BorderLayout());
        iPanel.add(iSearchPanel, BorderLayout.NORTH);
        iPanel.add(new JScrollPane(iTable), BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,4,2));

        return iPanel;
    }




    /**
     * This method should return the status bar content, if any.
     *
     * @return The content for the status bar or null if none is wanted.
     */
    @Override
    public JComponent getStatusBar() {
        return null;
    }

    /**
     * Indicates whether this frame is a company data related frame.
     *
     * @return A boolean value.
     */
    @Override
    public boolean isCompanyFrame() {
        return true;
    }

    /**
     * Indicates whether this frame is a year data related frame.
     *
     * @return A boolean value.
     */
    @Override
    public boolean isYearDataFrame() {
        return false;
    }



    /**
     *
     * @return the model
     */
    public SSCustomerTableModel getModel() {
        return iModel;
    }

    /**
     *
     * @param delete
     */
    private void deleteSelectedCustomers(List<SSCustomer> delete) {
        if (delete.isEmpty()) {
            return;
        }

        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "customerframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSCustomer iCustomer : delete) {
                if (SSPostLock.isLocked("customer" + iCustomer.getNumber() + SSDB.getInstance().getCurrentCompany().getId())) {
                    new SSErrorDialog(getMainFrame(), "customerframe.customeropen", iCustomer.getNumber());
                } else {
                    SSDB.getInstance().deleteCustomer(iCustomer);
                }
            }
        }
    }



    /**
     *
     */
    private void CustomerListReport() {
        final SSCustomerListPrinter iPrinter;
        List<SSCustomer> iCustomers;
        if (iTable.getSelectedRowCount() > 0) {

            int iOption = SSQueryDialog.showDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, "customerframe.printallorselected");
            switch (iOption) {
                case JOptionPane.YES_OPTION:
                    iCustomers = iModel.getObjects(iTable.getSelectedRows());
                    iCustomers = getCustomers(iCustomers);
                    iPrinter = new SSCustomerListPrinter(iCustomers);
                    break;
                case JOptionPane.NO_OPTION :
                    iCustomers = iModel.getObjects();
                    iCustomers = getCustomers(iCustomers);
                    iPrinter = new SSCustomerListPrinter(iCustomers);
                    break;
                default:
                    return;
            }
        } else {
            iCustomers = iModel.getObjects(iTable.getSelectedRows());
            iCustomers = getCustomers(iCustomers);
            iPrinter = new SSCustomerListPrinter(iCustomers);
        }

        SSProgressDialog.runProgress(getMainFrame(), new Runnable(){
            public void run() {
                iPrinter.preview(getMainFrame());
            }
        });


    }

    private void CustomerRevenueReport() {
        final SSCustomerRevenuePrinter iPrinter;
        List<SSCustomer> iCustomers;
        if (iTable.getSelectedRowCount() > 0) {

            int iOption = SSQueryDialog.showDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, "customerframe.printallorselected");
            switch(iOption ){
                case JOptionPane.YES_OPTION:
                    iCustomers = iModel.getObjects(iTable.getSelectedRows());
                    iCustomers = getCustomers(iCustomers);
                    break;
                case JOptionPane.NO_OPTION :
                    iCustomers = SSDB.getInstance().getCustomers();
                    break;
                default:
                    return;
            }
        } else {
            iCustomers = SSDB.getInstance().getCustomers();
        }

        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(getMainFrame(), SSBundle.getBundle().getString("customerrevenue.perioddialog.title"));
        if (SSDB.getInstance().getCurrentYear() != null) {
            iDialog.setFrom(SSDB.getInstance().getCurrentYear().getFrom());
            iDialog.setTo(SSDB.getInstance().getCurrentYear().getTo());
        } else {
            Calendar iCal = Calendar.getInstance();
            iDialog.setFrom(iCal.getTime());
            iCal.add(Calendar.MONTH,1);
            iDialog.setTo(iCal.getTime());
        }
        iDialog.setLocationRelativeTo(getMainFrame());
        if( iDialog.showDialog() != JOptionPane.OK_OPTION) return;

        final Date iFrom = iDialog.getFrom();
        final Date iTo   = iDialog.getTo();

        iPrinter = new SSCustomerRevenuePrinter(iCustomers,iFrom,iTo);

        SSProgressDialog.runProgress(getMainFrame(), new Runnable(){
            public void run() {
                iPrinter.preview(getMainFrame());
            }
        });
    }

    private SSCustomer getCustomer(SSCustomer iCustomer) {
        return SSDB.getInstance().getCustomer(iCustomer);
    }

    private List<SSCustomer> getCustomers(List<SSCustomer> iCustomers) {
        return SSDB.getInstance().getCustomers(iCustomers);
    }

    public void updateFrame() {
        iModel.setObjects(SSDB.getInstance().getCustomers());
        iSearchPanel.ApplyFilter();

    }

    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        iSearchPanel=null;
        SSCustomerFrame.cInstance=null;
    }

}
