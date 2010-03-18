package se.swedsoft.bookkeeping.gui.outdelivery;

import se.swedsoft.bookkeeping.data.SSOutdelivery;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.outdelivery.util.SSOutdeliveryTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.print.SSReportFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-sep-18
 * Time: 11:34:17
 */
public class SSOutdeliveryFrame extends SSDefaultTableFrame {


    private static SSOutdeliveryFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( SSOutdeliveryFrame.cInstance == null || SSOutdeliveryFrame.cInstance.isClosed() ){
            SSOutdeliveryFrame.cInstance = new SSOutdeliveryFrame(pMainFrame, pWidth, pHeight);
        }
        SSOutdeliveryFrame.cInstance.setVisible(true);
        SSOutdeliveryFrame.cInstance.deIconize();
        SSOutdeliveryFrame.cInstance.updateFrame();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSOutdeliveryFrame getInstance(){
        return SSOutdeliveryFrame.cInstance;
    }


    private SSTable iTable;

    private SSOutdeliveryTableModel iModel;



    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSOutdeliveryFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("outdeliveryframe.title"), width, height);
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
        iButton = new SSButton("ICON_NEWITEM", "outdeliveryframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSOutdeliveryDialog.newDialog(getMainFrame(), iModel);
            }
        });
        toolBar.add(iButton);



        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "outdeliveryframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSOutdelivery iOutdelivery = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iOutdelivery != null){
                    iNumber = iOutdelivery.getNumber();
                    iOutdelivery = getOutdelivery(iOutdelivery);
                }
                if (iOutdelivery != null) {
                    SSOutdeliveryDialog.editDialog(getMainFrame(), iOutdelivery, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "outdeliveryframe.outdeliverygone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Copy
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "outdeliveryframe.copybutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSOutdelivery iOutdelivery = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iOutdelivery != null){
                    iNumber = iOutdelivery.getNumber();
                    iOutdelivery = getOutdelivery(iOutdelivery);
                }
                if (iOutdelivery != null) {
                    SSOutdeliveryDialog.copyDialog(getMainFrame(), iOutdelivery, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "outdeliveryframe.outdeliverygone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "outdeliveryframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSOutdelivery> toDelete = iModel.getObjects(selected);
                deleteSelected(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

         // Print
        // ***************************
        iMenuButton = new SSMenuButton<SSButton>("ICON_PRINT", "outdeliveryframe.printbutton");
        iMenuButton.add("outdeliveryframe.print.list", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.OutdeliveryList(getMainFrame());
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

        iModel = new SSOutdeliveryTableModel();
        iModel.addColumn(SSOutdeliveryTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSOutdeliveryTableModel.COLUMN_DATE);
        iModel.addColumn(SSOutdeliveryTableModel.COLUMN_TEXT);
        iModel.addColumn(SSOutdeliveryTableModel.COLUMN_TOTALCOUNT);

        iModel.setupTable(iTable);

        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSOutdelivery iOutdelivery = iModel.getSelectedRow(iTable);
                Integer iNumber;
                if (iOutdelivery != null) {
                    iNumber = iOutdelivery.getNumber();
                    iOutdelivery = getOutdelivery(iOutdelivery);
                } else {
                    return;
                }
                if (iOutdelivery != null) {
                    SSOutdeliveryDialog.editDialog(getMainFrame(), iOutdelivery, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "outdeliveryframe.outdeliverygone", iNumber);
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
    public SSOutdeliveryTableModel getModel() {
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
     * @param delete
     */
    private void deleteSelected(List<SSOutdelivery> delete) {
        if (delete.isEmpty()) {
            return;
        }
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "outdeliveryframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSOutdelivery iOutdelivery : delete) {
                if (SSPostLock.isLocked("outdelivery" + iOutdelivery.getNumber() + SSDB.getInstance().getCurrentCompany().getId())){
                    new SSErrorDialog(getMainFrame(), "outdeliveryframe.outdeliveryopen",iOutdelivery.getNumber());
                } else {
                    SSDB.getInstance().deleteOutdelivery(iOutdelivery);
                }
            }
        }
    }

    private SSOutdelivery getOutdelivery(SSOutdelivery iOutdelivery) {
        return SSDB.getInstance().getOutdelivery(iOutdelivery);
    }

    public void updateFrame() {
        iModel.setObjects(SSDB.getInstance().getOutdeliveries());
    }
    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        SSOutdeliveryFrame.cInstance=null;
    }
}
