package se.swedsoft.bookkeeping.gui.supplier;

import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSExcelFileChooser;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSProgressDialog;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.supplier.util.SSSupplierTableModel;
import se.swedsoft.bookkeeping.gui.supplier.panel.SSSupplierSearchPanel;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.importexport.excel.SSSupplierExporter;
import se.swedsoft.bookkeeping.importexport.excel.SSSupplierImporter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.print.report.SSSupplierListPrinter;
import se.swedsoft.bookkeeping.print.report.SSSupplierRevenuePrinter;
import se.swedsoft.bookkeeping.print.dialog.SSPeriodSelectionDialog;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:47:21
 */
public class SSSupplierFrame extends SSDefaultTableFrame {

    private static SSSupplierFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( SSSupplierFrame.cInstance == null || SSSupplierFrame.cInstance.isClosed() ){
            SSSupplierFrame.cInstance = new SSSupplierFrame(pMainFrame, pWidth, pHeight);
        }
        SSSupplierFrame.cInstance.setVisible(true);
        SSSupplierFrame.cInstance.deIconize();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSSupplierFrame getInstance(){
        return SSSupplierFrame.cInstance;
    }


    private SSTable iTable;

    private SSSupplierTableModel iModel;

    private SSSupplierSearchPanel iSearchPanel;


    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSSupplierFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("supplierframe.title"), width, height);
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
        iButton = new SSButton("ICON_NEWITEM", "supplierframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSSupplierDialog.newDialog(getMainFrame(), iModel);
            }
        });
        iToolBar.add(iButton);



        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "supplierframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSSupplier iSelected = iModel.getSelectedRow(iTable);
                String iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getSupplier(iSelected);
                }
                if (iSelected != null) {
                    SSSupplierDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "supplierframe.suppliergone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Copy
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "supplierframe.copybutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSSupplier iSelected = iModel.getSelectedRow(iTable);
                String iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getSupplier(iSelected);
                }
                if (iSelected != null) {
                    SSSupplierDialog.copyDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "supplierframe.suppliergone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "supplierframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSSupplier> toDelete = iModel.getObjects(selected);
                deleteSelectedSuppliers(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Importera
        // ***************************
        iButton = new SSButton("ICON_IMPORT", "supplierframe.importbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSExcelFileChooser iFilechooser = SSExcelFileChooser.getInstance();

                if( iFilechooser.showOpenDialog( getMainFrame() ) == JFileChooser.APPROVE_OPTION  ){
                    SSSupplierImporter iImporter = new SSSupplierImporter( iFilechooser.getSelectedFile() );
                    if(!SSPostLock.applyLock("importsupplier")){
                        new SSErrorDialog(getMainFrame(), "supplierframe.import.locked");
                        return;
                    }
                    try {
                        iImporter.Import();

                    } catch (IOException ex) {
                        SSErrorDialog.showDialog( getMainFrame(), "", ex.getLocalizedMessage() );
                        SSPostLock.removeLock("importsupplier");
                    } catch (SSImportException ex) {
                        SSErrorDialog.showDialog( getMainFrame(), "", ex.getLocalizedMessage() );
                        SSPostLock.removeLock("importsupplier");
                    }
                    iModel.fireTableDataChanged();
                    SSPostLock.removeLock("importsupplier");
                }
            }
        });
        iToolBar.add(iButton);
        // Exportera
        // ***************************
        iButton = new SSButton("ICON_EXPORT", "supplierframe.exportbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSExcelFileChooser iFilechooser = SSExcelFileChooser.getInstance();

                List<SSSupplier> iItems;
                List<SSSupplier> iSelected = iModel.getSelectedRows(iTable);

                if( iSelected != null) {
                    int select = SSQueryDialog.showDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, getTitle(), SSBundle.getBundle().getString("supplierframe.import.allorselected"));
                    switch (select) {
                        case JOptionPane.YES_OPTION:
                            iItems = getSuppliers(iSelected);
                            break;
                        case JOptionPane.NO_OPTION :
                            iItems = SSDB.getInstance().getSuppliers();
                            break;
                        default:
                            return;
                    }
                } else {
                    iItems = SSDB.getInstance().getSuppliers();
                }

                iFilechooser.setSelectedFile(new File("Leverantörslista.xls"));

                if( iFilechooser.showSaveDialog( getMainFrame() ) == JFileChooser.APPROVE_OPTION  ){

                    try {
                        SSSupplierExporter iExporter = new SSSupplierExporter( iFilechooser.getSelectedFile(), iItems );

                        iExporter.export();
                    } catch (IOException ex) {
                        SSErrorDialog.showDialog( getMainFrame(), "", ex.getLocalizedMessage() );
                    } catch (SSExportException ex) {
                        SSErrorDialog.showDialog( getMainFrame(), "", ex.getLocalizedMessage() );
                    }
                }
            }
        });
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Print
        // ***************************
        SSMenuButton iButton2 = new SSMenuButton("ICON_PRINT", "supplierframe.printbutton");
        iButton2.add("supplierframe.print.supplierrevenue", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SupplierRevenueReport();
            }
        });
        iButton2.add("supplierframe.print.supplierlist", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SupplierListReport();
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
        iModel = new SSSupplierTableModel();
        iModel.addColumn( SSSupplierTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSSupplierTableModel.COLUMN_NAME );
        iModel.addColumn( SSSupplierTableModel.COLUMN_PHONE );
        iModel.addColumn( SSSupplierTableModel.COLUMN_OUR_CUSTOMER_NR );
        iModel.addColumn( SSSupplierTableModel.COLUMN_YOUR_CONTACT );
        iModel.addColumn( SSSupplierTableModel.COLUMN_SUPPLIERDEBT );

        iTable = new SSTable();

        iModel.setupTable(iTable);


        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSSupplier iSelected = iModel.getSelectedRow(iTable);
                String iNumber;
                if (iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getSupplier(iSelected);
                } else {
                    return;
                }
                if (iSelected != null) {
                    SSSupplierDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "supplierframe.suppliergone", iNumber);
                }
            }
        });
        iSearchPanel = new SSSupplierSearchPanel(iModel);

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
     */
    private void deleteSelectedSuppliers(java.util.List<SSSupplier> delete) {
        if (delete.size() == 0) {
            return;
        }

        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "supplierframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSSupplier iSupplier : delete) {
                if (SSPostLock.isLocked("supplier" + iSupplier.getNumber() + SSDB.getInstance().getCurrentCompany().getId())) {
                    new SSErrorDialog(getMainFrame(), "supplierframe.supplieropen", iSupplier.getNumber());
                } else {
                    SSDB.getInstance().deleteSupplier(iSupplier);
                }
            }
        }
    }


    private void SupplierRevenueReport() {
        final SSSupplierRevenuePrinter iPrinter;
        List<SSSupplier> iSuppliers;
        if (iTable.getSelectedRowCount() > 0) {

            int iOption = SSQueryDialog.showDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, "supplierframe.printallorselected");
            switch(iOption ){
                case JOptionPane.YES_OPTION:
                    iSuppliers = iModel.getObjects(iTable.getSelectedRows());
                    iSuppliers = getSuppliers(iSuppliers);
                    break;
                case JOptionPane.NO_OPTION :
                    iSuppliers = SSDB.getInstance().getSuppliers();
                    break;
                default:
                    return;
            }
        } else {
            iSuppliers = SSDB.getInstance().getSuppliers();
        }

        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(getMainFrame(), SSBundle.getBundle().getString("supplierrevenue.perioddialog.title"));
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

        iPrinter = new SSSupplierRevenuePrinter(iSuppliers,iFrom,iTo);

        SSProgressDialog.runProgress(getMainFrame(), new Runnable(){
            public void run() {
                iPrinter.preview(getMainFrame());
            }
        });
    }
    /**
     *
     */
        private void SupplierListReport() {
        final SSSupplierListPrinter iPrinter;
        List<SSSupplier> iSuppliers;
        if (iTable.getSelectedRowCount() > 0) {

            int iOption = SSQueryDialog.showDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, "supplierframe.printallorselected");
            switch (iOption) {
                case JOptionPane.YES_OPTION:
                    iSuppliers = iModel.getObjects(iTable.getSelectedRows());
                    iSuppliers = getSuppliers(iSuppliers);
                    iPrinter = new SSSupplierListPrinter(iSuppliers);
                    break;
                case JOptionPane.NO_OPTION :
                    iSuppliers = iModel.getObjects();
                    iSuppliers = getSuppliers(iSuppliers);
                    iPrinter = new SSSupplierListPrinter(iSuppliers);
                    break;
                default:
                    return;
            }
        } else {
            iSuppliers = iModel.getObjects();
            iSuppliers = getSuppliers(iSuppliers);
            iPrinter = new SSSupplierListPrinter(iSuppliers);
        }

        SSProgressDialog.runProgress(getMainFrame(), new Runnable(){
            public void run() {
                iPrinter.preview(getMainFrame());
            }
        });


    }

    private SSSupplier getSupplier(SSSupplier iSupplier) {
        return SSDB.getInstance().getSupplier(iSupplier);
    }

    private java.util.List<SSSupplier> getSuppliers(java.util.List<SSSupplier> iSuppliers) {
        return SSDB.getInstance().getSuppliers(iSuppliers);
    }

    public void updateFrame() {
        iModel.setObjects(SSDB.getInstance().getSuppliers());
        iSearchPanel.ApplyFilter();
    }

    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        iSearchPanel=null;
        SSSupplierFrame.cInstance=null;
    }

}
