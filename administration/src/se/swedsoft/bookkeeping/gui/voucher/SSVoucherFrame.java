package se.swedsoft.bookkeeping.gui.voucher;

import se.swedsoft.bookkeeping.SSVersion;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSProgressDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSExcelFileChooser;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSSIEFileChooser;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.voucher.util.SSVoucherTableModel;
import se.swedsoft.bookkeeping.importexport.excel.SSVoucherExporter;
import se.swedsoft.bookkeeping.importexport.excel.SSVoucherImporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.print.report.SSVoucherListPrinter;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.EventObject;
import java.util.List;

/**
 * Date: 2006-feb-07
 * Time: 08:50:35
 */
public class SSVoucherFrame extends SSDefaultTableFrame {

    private static SSVoucherFrame cInstance;


    /**
     *
     * @return
     */
    public static SSVoucherFrame getInstance() {
        return cInstance;
    }

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( cInstance == null || cInstance.isClosed() ){
            cInstance = new SSVoucherFrame(pMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
        cInstance.deIconize();
        cInstance.updateFrame();
    }

    /**
     * 
     */
    public static void closeFrame(){
        if( cInstance != null){
            cInstance.setVisible(false);
        }

    }

    private SSTable iTable;

    private SSVoucherTableModel iModel;

    /**
     * Default constructor.
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    private SSVoucherFrame(SSMainFrame pMainFrame, int pWidth, int pHeight) {
        super(pMainFrame, SSBundle.getBundle().getString("voucherframe.title"), pWidth, pHeight);
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

        // Ny verifikation
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "voucherframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSVoucherDialog.newDialog(getMainFrame(), iModel);
            }
        });
        iToolBar.add(iButton);

        // Ändra verifikation
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "voucherframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSVoucher iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected!=null){
                    iNumber = iSelected.getNumber();
                    iSelected = getVoucher(iSelected);
                }
                if (iSelected != null) {
                    SSVoucherDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "voucherframe.vouchergone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Skapa ny ändringsverifikation
        // ***************************
        iButton = new SSButton("ICON_CREATECHANGE", "voucherframe.createchangebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSVoucher iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected!=null){
                    iNumber = iSelected.getNumber();
                    iSelected = getVoucher(iSelected);
                }
                if (iSelected != null) {
                    SSVoucherDialog.copyDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "voucherframe.vouchergone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();


        if(SSVersion.iAllowVoucherDeletion){
            // Ta bort verifikation
            // ***************************
            iButton = new SSButton("ICON_DELETEITEM", "voucherframe.deletebutton", new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    int[] selected = iTable.getSelectedRows();
                    List<SSVoucher> toDelete = iModel.getObjects(selected);
                    deleteSelectedVouchers(toDelete);
                }
            });
            iTable.addSelectionDependentComponent(iButton);

            iToolBar.add(iButton);
            iToolBar.addSeparator();
        }


        // Importera
        // ***************************
        iButton = new SSButton("ICON_IMPORT", "voucherframe.importbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                final String lockString = "voucher"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId();
                if (!SSPostLock.applyLock(lockString)) {
                    new SSErrorDialog( getMainFrame(), "voucheriscreated");
                    return;
                }

                SSExcelFileChooser iFilechooser = SSExcelFileChooser.getInstance();
                int iResponce = iFilechooser.showOpenDialog(getMainFrame());
                if ( iResponce == JFileChooser.APPROVE_OPTION) {
                    SSVoucherImporter iImporter = new SSVoucherImporter(iFilechooser.getSelectedFile());

                    try {
                        iImporter.Import();

                    } catch (IOException ex) {
                        SSPostLock.removeLock(lockString);
                        SSErrorDialog.showDialog(getMainFrame(), "", ex.getLocalizedMessage());
                    } catch (SSImportException ex) {
                        SSPostLock.removeLock(lockString);
                        SSErrorDialog.showDialog(getMainFrame(), "", ex.getLocalizedMessage());
                    }
                    iModel.fireTableDataChanged();
                } else {
                    SSPostLock.removeLock(lockString);
                }
            }
        });
        iToolBar.add(iButton);

        // Exportera
        // ***************************
        iButton = new SSButton("ICON_EXPORT", "voucherframe.exportbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSExcelFileChooser iFilechooser = SSExcelFileChooser.getInstance();

                List<SSVoucher> iItems;
                List<SSVoucher> iSelected = iModel.getSelectedRows(iTable);
                if( iSelected != null) {
                    int select = SSQueryDialog.showDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, getTitle(), SSBundle.getBundle().getString("voucherframe.import.allorselected"));
                    switch(select){
                        case JOptionPane.YES_OPTION:
                            iItems = getVouchers(iSelected);
                            break;
                        case JOptionPane.NO_OPTION :
                            iItems = SSDB.getInstance().getVouchers();
                            break;
                        default:
                            return;
                    }
                } else {
                    iItems = SSDB.getInstance().getVouchers();
                }
                iFilechooser.setSelectedFile(new File("Verifikationer.xls"));

                if( iFilechooser.showSaveDialog( getMainFrame() ) == JFileChooser.APPROVE_OPTION  ){

                    SSVoucherExporter iExporter = new SSVoucherExporter( iFilechooser.getSelectedFile(), iItems );

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
        iToolBar.add(iButton);

        iToolBar.addSeparator();

        // Importera verifikationer från sie
        // ***************************
        iButton = new SSButton("Task List 24", "voucherframe.importsiebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                final String lockString = "voucher"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId();
                if (!SSPostLock.applyLock(lockString)) {
                    new SSErrorDialog( getMainFrame(), "voucheriscreated");
                    return;
                }
                SSSIEFileChooser iFileChooser = SSSIEFileChooser.getInstance();
                int iResponce = iFileChooser.showOpenDialog(getMainFrame());
                if (iResponce == JFileChooser.APPROVE_OPTION) {
                    SSSIEImporter iImporter = new SSSIEImporter(iFileChooser.getSelectedFile());

                    try {
                        iImporter.doImportVouchers();
                        SSPostLock.removeLock(lockString);
                    } catch (SSImportException ex) {
                        SSPostLock.removeLock(lockString);
                        SSErrorDialog.showDialog(getMainFrame(), "", ex.getLocalizedMessage());
                    }
                    iModel.fireTableDataChanged();
                } else {
                    SSPostLock.removeLock(lockString);
                }
            }
        });
        iToolBar.add(iButton);

        iToolBar.addSeparator();

        // Skriv ut verifikation(er)
        // ***************************
        iButton = new SSButton("ICON_PRINT", "voucherframe.printbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                printVouchers();
            }
        });
        iToolBar.add(iButton);

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
        iTable = new SSTable();

        iModel = new SSVoucherTableModel();
        iModel.addColumn(SSVoucherTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSVoucherTableModel.COLUMN_DATE);
        iModel.addColumn(SSVoucherTableModel.COLUMN_DESCRIPTION);
        iModel.addColumn(SSVoucherTableModel.COLUMN_SUM);
        iModel.addColumn(SSVoucherTableModel.COLUMN_CORRECTS);
        iModel.addColumn(SSVoucherTableModel.COLUMN_CORRECTEDBY);


        iModel.setupTable(iTable);

        iTable.setDefaultRenderer(SSVoucher.class, new SSVoucherCellRenderer());
        iTable.setDefaultEditor  (SSVoucher.class, new SSVoucherCellEditor  ());

        iTable.addDblClickListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSVoucher iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber;
                if (iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getVoucher(iSelected);
                } else {
                    return;
                }
                if (iSelected != null) {
                    SSVoucherDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "voucherframe.vouchergone", iNumber);
                }
            }

        });

        JPanel iPanel = new JPanel();
        iPanel.setLayout( new BorderLayout() );
        iPanel.add( new JScrollPane(iTable), BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,2,2));
        return iPanel;
    }

    public void updateFrame() {
        iModel.setObjects(SSDB.getInstance().getVouchers());
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
    public SSVoucherTableModel getModel() {
        return iModel;
    }


    /**
     *
     * @param delete
     */
    private void deleteSelectedVouchers(List<SSVoucher> delete) {
        if (delete.isEmpty()) {
            return;
        }
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "voucherframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSVoucher iVoucher : delete) {
                if (SSPostLock.isLocked("voucher" + iVoucher.getNumber() + SSDB.getInstance().getCurrentCompany().getId())){
                    new SSErrorDialog(getMainFrame(), "voucherframe.voucheropen",iVoucher.getNumber());
                } else {
                    SSDB.getInstance().deleteVoucher(iVoucher);
                }
            }
        }
    }
    private SSVoucher getVoucher(SSVoucher iVoucher) {
        return SSDB.getInstance().getVoucher(iVoucher);
    }

    private List<SSVoucher> getVouchers(List<SSVoucher> iVouchers) {
        return SSDB.getInstance().getVouchers(iVouchers);
    }

    /**
     *
     */
    private void printVouchers() {
        final SSVoucherListPrinter iPrinter;
        List<SSVoucher> iVouchers;
        if (iTable.getSelectedRowCount() > 0) {

            SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, "voucherframe.print");

            switch(iDialog.getResponce() ){
                case JOptionPane.YES_OPTION:
                    iVouchers = getVouchers(iModel.getObjects(iTable.getSelectedRows()));
                    iPrinter = new SSVoucherListPrinter(iVouchers);
                    break;
                case JOptionPane.NO_OPTION :
                    iPrinter = new SSVoucherListPrinter(SSDB.getInstance().getVouchers());
                    break;
                default:
                    return;
            }
        } else {
            iPrinter = new SSVoucherListPrinter(SSDB.getInstance().getVouchers());
            //iPrinter = new SSVoucherListPrinter(iModel.getObjects(iTable.getSelectedRows()));
        }

        SSProgressDialog.runProgress(getMainFrame(), new Runnable(){
            public void run() {
                iPrinter.preview(getMainFrame());
            }
        });


    }









    private class SSVoucherCellEditor extends DefaultCellEditor {

        private SSVoucherCellRenderer iRenderer;


        public SSVoucherCellEditor() {
            super(new JTextField());
            iRenderer = new SSVoucherCellRenderer();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if(value != null){
                final SSVoucher iVoucher   = (SSVoucher)value;

                Component c = iRenderer.getTableCellRendererComponent(table, value, isSelected, true, row, column);

                JButton iButton = new JButton("...");
                iButton.setMaximumSize  ( new Dimension(18,18));
                iButton.setPreferredSize( new Dimension(18,18));
                iButton.setSize         ( new Dimension(18,18));
                iButton.setToolTipText  ( SSBundle.getBundle().getString("voucherframe.gotovoucher.tooltip") );

                iButton.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        int index = SSVoucherFrame.this.iModel.indexOf(iVoucher );

                        SSVoucherFrame.this.iTable.editCellAt(-1, -1);
                        if(index >= 0){
                            SSVoucherFrame.this.iTable.setRowSelectionInterval(index, index);
                            SSVoucherFrame.this.iTable.scrollRectToVisible(SSVoucherFrame.this.iTable.getCellRect(index, 0, true));
                        }
                    }
                });
                JPanel iPanel = new JPanel();

                iPanel.setLayout( new BorderLayout() );
                iPanel.add(c      , BorderLayout.CENTER);
                iPanel.add(iButton, BorderLayout.EAST);

                return iPanel;

            }
            return super.getTableCellEditorComponent(table, value, isSelected, row, column);
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }
    }


    private static class SSVoucherCellRenderer extends DefaultTableCellRenderer {

        public SSVoucherCellRenderer() {
        }

        @Override
        protected void setValue(Object value) {
            if(value != null){
                SSVoucher iVoucher = (SSVoucher)value;
                setText( Integer.toString( iVoucher.getNumber() ) );
            } else {

                setText("");
            }
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        cInstance=null;
    }
}


