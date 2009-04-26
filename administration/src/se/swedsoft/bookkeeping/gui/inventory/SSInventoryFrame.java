package se.swedsoft.bookkeeping.gui.inventory;

import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.inventory.util.SSInventoryTableModel;
import se.swedsoft.bookkeeping.data.SSInventory;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.print.SSReportFactory;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-sep-18
 * Time: 11:34:17
 */
public class SSInventoryFrame extends SSDefaultTableFrame {


    private static SSInventoryFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( cInstance == null || cInstance.isClosed() ){
            cInstance = new SSInventoryFrame(pMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
        cInstance.deIconize();
        cInstance.updateFrame();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSInventoryFrame getInstance(){
        return cInstance;
    }


    private SSTable iTable;

    private SSInventoryTableModel iModel;



    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSInventoryFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("inventortyframe.title"), width, height);
    }


    /**
     * This method should return a toolbar if the sub-class wants one. <P>
     * Otherwise, it may return null.
     *
     * @return A JToolBar or null.
     */
    public JToolBar getToolBar() {
        JToolBar toolBar = new JToolBar();

        SSButton iButton;

        SSMenuButton<SSButton> iMenuButton;

        JMenuItem iMenuItem;

        // New
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "inventortyframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSInventoryDialog.newDialog(getMainFrame(), iModel);
            }
        });
        toolBar.add(iButton);

        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "inventortyframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSInventory iInventory = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iInventory != null){
                    iNumber = iInventory.getNumber();
                    iInventory = getInventory(iInventory);
                }
                if (iInventory != null) {
                    SSInventoryDialog.editDialog(getMainFrame(), iInventory, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "inventoryframe.inventorygone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

            /*
        // Copy
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "inventortyframe.copybutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSInventory iInventory = iModel.getSelectedRow(iTable);

                if(iInventory != null) SSInventoryDialog.copyDialog(getMainFrame(), iInventory, iModel);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator(); */

        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "inventortyframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSInventory> toDelete = iModel.getObjects(selected);
                deleteSelected(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();


          // Print
        // ***************************
        iMenuButton = new SSMenuButton<SSButton>("ICON_PRINT", "inventortyframe.printbutton");
        iMenuButton.add("inventortyframe.print.list", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.InventoryList(getMainFrame());
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
    public JComponent getMainContent() {
        iTable = new SSTable();

        iModel = new SSInventoryTableModel();
        iModel.addColumn(SSInventoryTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSInventoryTableModel.COLUMN_DATE);
        iModel.addColumn(SSInventoryTableModel.COLUMN_TEXT);

        iModel.setupTable(iTable);

        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSInventory iInventory = iModel.getSelectedRow(iTable);
                Integer iNumber;
                if (iInventory != null) {
                    iNumber = iInventory.getNumber();
                    iInventory = getInventory(iInventory);
                } else {
                    return;
                }
                if (iInventory != null) {
                    SSInventoryDialog.editDialog(getMainFrame(), iInventory, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "inventoryframe.inventorygone", iNumber);
                }
            }
        });


        JPanel iPanel = new JPanel();

        iPanel.setLayout(new BorderLayout());
        iPanel.add(new JScrollPane(iTable), BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,4,2));

        return iPanel;
    }

    /**
     *
     * @return
     */
    public SSInventoryTableModel getModel() {
        return iModel;
    }

    /**
     * This method should return the status bar content, if any. <P>
     *
     * @return The content for the status bar or null if none is wanted.
     */
    public JComponent getStatusBar() {
        return null;
    }

    /**
     * Indicates whether this frame is a company data related frame. <P>
     *
     * @return A boolean value.
     */
    public boolean isCompanyFrame() {
        return true;
    }

    /**
     * Indicates whether this frame is a year data related frame. <P>
     *
     * @return A boolean value.
     */
    public boolean isYearDataFrame() {
        return false;
    }



    /**
     *
     */
    private void deleteSelected(List<SSInventory> delete) {
        if (delete.size() == 0) {
            return;
        }
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "inventoryframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSInventory iInventory : delete) {
                if (SSPostLock.isLocked("inventory" + iInventory.getNumber() + SSDB.getInstance().getCurrentCompany().getId())){
                    new SSErrorDialog(getMainFrame(), "inventoryframe.inventoryopen",iInventory.getNumber());
                } else {
                    SSDB.getInstance().deleteInventory(iInventory);
                }
            }
        }
    }

    private SSInventory getInventory(SSInventory iInventory) {
        return SSDB.getInstance().getInventory(iInventory);
    }

    public void updateFrame() {
        iModel.setObjects(SSDB.getInstance().getInventories());
    }
    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        SSInventoryFrame.cInstance=null;
    }
}
