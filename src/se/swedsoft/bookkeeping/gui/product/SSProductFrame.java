package se.swedsoft.bookkeeping.gui.product;

import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSStock;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.product.panel.SSProductSearchPanel;
import se.swedsoft.bookkeeping.gui.product.util.SSProductTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSProgressDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSExcelFileChooser;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSXMLFileChooser;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.importexport.excel.SSProductExporter;
import se.swedsoft.bookkeeping.importexport.excel.SSProductImporter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.print.dialog.SSPeriodSelectionDialog;
import se.swedsoft.bookkeeping.print.report.SSProductListPrinter;
import se.swedsoft.bookkeeping.print.report.SSProductRevenuePrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:47:21
 */
public class SSProductFrame extends SSDefaultTableFrame {

    private static SSProductFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( cInstance == null || cInstance.isClosed() ){
            cInstance = new SSProductFrame(pMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
        cInstance.deIconize();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSProductFrame getInstance(){
        return cInstance;
    }


    private SSTable iTable;

    private SSProductTableModel iModel;

    private SSProductSearchPanel iSearchPanel;

    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSProductFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("productframe.title"), width, height);
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


        // New
        // ***************************
        SSButton iButton = new SSButton("ICON_NEWITEM", "productframe.newbutton", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSProductDialog.newDialog(getMainFrame(), iModel);
            }
        });
        iToolBar.add(iButton);


        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "productframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSProduct iSelected = iModel.getSelectedRow(iTable);
                String iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getProduct(iSelected);
                }
                if (iSelected != null) {
                    SSProductDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "productframe.productgone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();



        // Copy
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "productframe.copybutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSProduct iSelected = iModel.getSelectedRow(iTable);

                String iNumber = iSelected.getNumber();
                iSelected = getProduct(iSelected);
                if(iSelected != null){
                    SSProductDialog.copyDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "productframe.productgone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();


        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "productframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSProduct> toDelete = iModel.getObjects(selected);
                deleteSelectedProducts(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Importera
        // ***************************
        SSMenuButton iButton2 = new SSMenuButton("ICON_IMPORT", "productframe.importbutton");
        iButton2.add("productframe.import.excel", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSExcelFileChooser iFilechooser = SSExcelFileChooser.getInstance();

                if( iFilechooser.showOpenDialog( getMainFrame() ) == JFileChooser.APPROVE_OPTION  ){
                    try {
                        SSProductImporter iImporter = new SSProductImporter( iFilechooser.getSelectedFile()  );

                        iImporter.doImport();
                    } catch (IOException ex) {
                        SSErrorDialog.showDialog( getMainFrame(), "", ex.getLocalizedMessage() );
                    } catch (SSImportException ex) {
                        SSErrorDialog.showDialog( getMainFrame(), "", ex.getLocalizedMessage() );
                    }
                    iModel.fireTableDataChanged();
                }
            }
        });
        iButton2.add("productframe.import.xml", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSXMLFileChooser iFilechooser = SSXMLFileChooser.getInstance();
                iFilechooser.setSelectedFile(new File("Produktlista.xml"));

                if( iFilechooser.showOpenDialog( getMainFrame() ) == JFileChooser.APPROVE_OPTION  ){
                    SSProductImporter iImporter = new SSProductImporter(iFilechooser.getSelectedFile());
                    try {
                        iImporter.doXMLImport();
                    } catch (SSImportException e1){
                        SSErrorDialog.showDialog( getMainFrame(), "", e1.getLocalizedMessage() );
                    }

                }
            }
        });
        iToolBar.add(iButton2);

        // Exportera
        // ***************************
        iButton2 = new SSMenuButton("ICON_EXPORT", "productframe.exportbutton");
        iButton2.add("productframe.export.excel", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSExcelFileChooser iFilechooser = SSExcelFileChooser.getInstance();
                List<SSProduct> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getProducts(iSelected);
                List<SSProduct> iItems;
                if( iSelected != null) {
                    int select = SSQueryDialog.showDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, getTitle(), SSBundle.getBundle().getString("productframe.import.allorselected"));
                    switch(select){
                        case JOptionPane.YES_OPTION:
                            iItems = iSelected;
                            break;
                        case JOptionPane.NO_OPTION :
                            iItems = SSDB.getInstance().getProducts();
                            break;
                        default:
                            return;
                    }
                } else {
                    iItems = SSDB.getInstance().getProducts();
                }
                iFilechooser.setSelectedFile(new File("Produktlista.xls"));

                if( iFilechooser.showSaveDialog( getMainFrame() ) == JFileChooser.APPROVE_OPTION  ){
                    iItems = getProducts(iItems);
                    try {
                        SSProductExporter iExporter = new SSProductExporter( iFilechooser.getSelectedFile(), iItems );

                        iExporter.doExport();
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
                List<SSProduct> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getProducts(iSelected);
                List<SSProduct> iItems;
                if( iSelected != null) {
                    int select = SSQueryDialog.showDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, getTitle(), SSBundle.getBundle().getString("productframe.import.allorselected"));
                    switch(select){
                        case JOptionPane.YES_OPTION:
                            iItems = iSelected;
                            break;
                        case JOptionPane.NO_OPTION :
                            iItems = SSDB.getInstance().getProducts();
                            break;
                        default:
                            return;
                    }
                } else {
                    iItems = SSDB.getInstance().getProducts();
                }
                if (!iItems.isEmpty()) {

                    SSXMLFileChooser iFilechooser = SSXMLFileChooser.getInstance();
                    iFilechooser.setSelectedFile(new File("Produktlista.xml"));

                    if( iFilechooser.showSaveDialog( getMainFrame() ) == JFileChooser.APPROVE_OPTION  ){
                        SSProductExporter iExporter = new SSProductExporter(iFilechooser.getSelectedFile(), iItems);
                        iExporter.doXMLExport();
                    }
                }
            }

        });
        iToolBar.add(iButton2);
        iToolBar.addSeparator();

        // Print
        // ***************************
        iButton2 = new SSMenuButton("ICON_PRINT", "productframe.printbutton");
        iButton2.add("productframe.print.productrevenue", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                ProductRevenueReport();
            }
        });
        iButton2.add("productframe.print.productlist", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                ProductListReport();
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


        SSStock iStock = new SSStock(true);

        iModel = new SSProductTableModel();
        iModel.addColumn( SSProductTableModel.COLUMN_PARCEL );
        iModel.addColumn( SSProductTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSProductTableModel.COLUMN_DESCRIPTION );
        iModel.addColumn( SSProductTableModel.COLUMN_PRICE);
        iModel.addColumn( SSProductTableModel.COLUMN_UNIT );
        iModel.addColumn( SSProductTableModel.COLUMN_WAREHOUSE_LOCATION );
        //if(!SSVersion.app_title.contains("JFS Fakturering")) {
        iModel.addColumn( SSProductTableModel.getStockQuantityColumn(iStock) );
        iModel.addColumn( SSProductTableModel.getStockAvaiableColumn(iStock) );
        //}

        iTable = new SSTable();

        iModel.setupTable(iTable);

        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSProduct iSelected = iModel.getSelectedRow(iTable);
                if (iSelected == null)
                    return;

                String iNumber = iSelected.getNumber();
                iSelected = getProduct(iSelected);

                if (iSelected != null) {
                    SSProductDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "productframe.productgone", iNumber);
                }
            }
        });


         iSearchPanel = new SSProductSearchPanel(iModel);

        JPanel iPanel = new JPanel();

        iPanel.setLayout(new BorderLayout());
        iPanel.add(iSearchPanel, BorderLayout.NORTH );
        iPanel.add(new JScrollPane(iTable)/*iTabbedPane*/, BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,4,2));

        return iPanel;
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
     * @return
     */
    public SSProductTableModel getModel() {
        return iModel;
    }


    /**
     *
     * @param delete
     */
    private void deleteSelectedProducts(List<SSProduct> delete) {
        if (delete.isEmpty()) {
            return;
        }

        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "productframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSProduct iProduct : delete) {
                if (SSPostLock.isLocked("product" + iProduct.getNumber() + SSDB.getInstance().getCurrentCompany().getId())) {
                    new SSErrorDialog(getMainFrame(), "productframe.productopen", iProduct.getNumber());
                } else {
                    SSDB.getInstance().deleteProduct(iProduct);
                }
            }
        }
    }

    private void ProductRevenueReport() {
        List<SSProduct> iProducts;
        if (iTable.getSelectedRowCount() > 0) {

            int iOption = SSQueryDialog.showDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, "productframe.printallorselected");

            switch(iOption ){
                case JOptionPane.YES_OPTION:
                    iProducts = iModel.getObjects(iTable.getSelectedRows());
                    iProducts = getProducts(iProducts);
                    break;
                case JOptionPane.NO_OPTION :
                    iProducts = SSDB.getInstance().getProducts();
                    break;
                default:
                    return;
            }
        } else {
            iProducts = SSDB.getInstance().getProducts();
        }

        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(getMainFrame(), SSBundle.getBundle().getString("productrevenue.perioddialog.title"));
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

        final SSProductRevenuePrinter iPrinter = new SSProductRevenuePrinter(iProducts, iFrom, iTo);

        SSProgressDialog.runProgress(getMainFrame(), new Runnable(){
            public void run() {
                iPrinter.preview(getMainFrame());
            }
        });
    }

    /**
     *
     */
    private void ProductListReport() {
        final SSProductListPrinter iPrinter;
        List<SSProduct> iProducts;
        if (iTable.getSelectedRowCount() > 0) {

            int iOption = SSQueryDialog.showDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, "productframe.printallorselected");
            switch(iOption ){
                case JOptionPane.YES_OPTION:
                    iProducts = iModel.getObjects(iTable.getSelectedRows());
                    iProducts = getProducts(iProducts);
                    iPrinter = new SSProductListPrinter(iProducts);
                    break;
                case JOptionPane.NO_OPTION :
                    iProducts = SSDB.getInstance().getProducts();
                    iPrinter = new SSProductListPrinter(iProducts);
                    break;
                default:
                    return;
            }
        } else {
            iProducts = SSDB.getInstance().getProducts();
            iPrinter = new SSProductListPrinter(iProducts);
        }

        SSProgressDialog.runProgress(getMainFrame(), new Runnable(){
            public void run() {
                iPrinter.preview(getMainFrame());
            }
        });
    }

    private SSProduct getProduct(SSProduct iProduct) {
        return SSDB.getInstance().getProduct(iProduct);
    }

    private List<SSProduct> getProducts(List<SSProduct> iProducts) {
        return SSDB.getInstance().getProducts(iProducts);
    }

    public void updateFrame() {
        iSearchPanel.ApplyFilter();
    }
    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        iSearchPanel=null;
        cInstance=null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.product.SSProductFrame");
        sb.append("{iModel=").append(iModel);
        sb.append(", iSearchPanel=").append(iSearchPanel);
        sb.append(", iTable=").append(iTable);
        sb.append('}');
        return sb.toString();
    }
}
