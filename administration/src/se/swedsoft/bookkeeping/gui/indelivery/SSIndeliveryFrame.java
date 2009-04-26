package se.swedsoft.bookkeeping.gui.indelivery;

import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.indelivery.util.SSIndeliveryTableModel;
import se.swedsoft.bookkeeping.data.SSIndelivery;
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
public class SSIndeliveryFrame extends SSDefaultTableFrame {


    private static SSIndeliveryFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( SSIndeliveryFrame.cInstance == null || SSIndeliveryFrame.cInstance.isClosed() ){
            SSIndeliveryFrame.cInstance = new SSIndeliveryFrame(pMainFrame, pWidth, pHeight);
        }
        SSIndeliveryFrame.cInstance.setVisible(true);
        SSIndeliveryFrame.cInstance.deIconize();
        SSIndeliveryFrame.cInstance.updateFrame();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSIndeliveryFrame getInstance(){
        return SSIndeliveryFrame.cInstance;
    }


    private SSTable iTable;

    private SSIndeliveryTableModel iModel;



    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSIndeliveryFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("indeliveryframe.title"), width, height);
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
        iButton = new SSButton("ICON_NEWITEM", "indeliveryframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSIndeliveryDialog.newDialog(getMainFrame(), iModel);
            }
        });
        toolBar.add(iButton);



        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "indeliveryframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSIndelivery iIndelivery = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iIndelivery!=null){
                    iNumber = iIndelivery.getNumber();
                    iIndelivery = getIndelivery(iIndelivery);
                }
                if (iIndelivery != null) {
                    SSIndeliveryDialog.editDialog(getMainFrame(), iIndelivery, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "indeliveryframe.indeliverygone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Copy
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "indeliveryframe.copybutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSIndelivery iIndelivery = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iIndelivery!=null){
                    iNumber = iIndelivery.getNumber();
                    iIndelivery = getIndelivery(iIndelivery);
                }
                if (iIndelivery != null) {
                    SSIndeliveryDialog.copyDialog(getMainFrame(), iIndelivery, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "indeliveryframe.indeliverygone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "indeliveryframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSIndelivery> toDelete = iModel.getObjects(selected);
                deleteSelected(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

         // Print
        // ***************************
        iMenuButton = new SSMenuButton<SSButton>("ICON_PRINT", "indeliveryframe.printbutton");
        iMenuButton.add("indeliveryframe.print.list", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.IndeliveryList(getMainFrame());
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

        iModel = new SSIndeliveryTableModel();
        iModel.addColumn(SSIndeliveryTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSIndeliveryTableModel.COLUMN_DATE);
        iModel.addColumn(SSIndeliveryTableModel.COLUMN_TEXT);
        iModel.addColumn(SSIndeliveryTableModel.COLUMN_TOTALCOUNT);

        iModel.setupTable(iTable);

        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSIndelivery iIndelivery = iModel.getSelectedRow(iTable);
                Integer iNumber;
                if (iIndelivery != null) {
                    iNumber = iIndelivery.getNumber();
                    iIndelivery = getIndelivery(iIndelivery);
                } else {
                    return;
                }
                if (iIndelivery != null) {
                    SSIndeliveryDialog.editDialog(getMainFrame(), iIndelivery, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "indeliveryframe.indeliverygone", iNumber);
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
    public SSIndeliveryTableModel getModel() {
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
    private void deleteSelected(List<SSIndelivery> delete) {
        if (delete.size() == 0) {
            return;
        }
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "indeliveryframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSIndelivery iIndelivery : delete) {
                if (SSPostLock.isLocked("indelivery" + iIndelivery.getNumber() + SSDB.getInstance().getCurrentCompany().getId())){
                    new SSErrorDialog(getMainFrame(), "indeliveryframe.indeliveryopen",iIndelivery.getNumber());
                } else {
                    SSDB.getInstance().deleteIndelivery(iIndelivery);
                }
            }
        }
    }

    private SSIndelivery getIndelivery(SSIndelivery iIndelivery) {
        return SSDB.getInstance().getIndelivery(iIndelivery);
    }

    public void updateFrame() {
        iModel.setObjects(SSDB.getInstance().getIndeliveries());
    }
    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        SSIndeliveryFrame.cInstance=null;
    }
}
