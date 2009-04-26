package se.swedsoft.bookkeeping.gui.autodist;

import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.autodist.util.SSAutoDistTableModel;
import se.swedsoft.bookkeeping.data.SSStock;
import se.swedsoft.bookkeeping.data.SSAutoDist;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.util.Vector;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:47:21
 */
public class SSAutoDistFrame extends SSDefaultTableFrame {

    private static SSAutoDistFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( cInstance == null || cInstance.isClosed() ){
            cInstance = new SSAutoDistFrame(pMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
        cInstance.deIconize();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSAutoDistFrame getInstance(){
        return cInstance;
    }


    private SSTable iTable;

    private SSAutoDistTableModel iModel;



    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSAutoDistFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("autodistframe.title"), width, height);
    }


    /**
     * This method should return a toolbar if the sub-class wants one. <P>
     * Otherwise, it may return null.
     *
     * @return A JToolBar or null.
     */
    public JToolBar getToolBar() {
        JToolBar iToolBar = new JToolBar();

        SSButton iButton;


        // New
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "autodistframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSAutoDistDialog.newDialog(getMainFrame(), iModel);
            }
        });
        iToolBar.add(iButton);


        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "autodistframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSAutoDist iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getAutoDist(iSelected);
                }
                if (iSelected != null) {
                    SSAutoDistDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "autodistframe.autodistgone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();



        // Copy
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "autodistframe.copybutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSAutoDist iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getAutoDist(iSelected);
                }
                if(iSelected != null){
                    SSAutoDistDialog.copyDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "autodistframe.autodistgone", iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();


        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "autodistframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSAutoDist> toDelete = iModel.getObjects(selected);
                deleteSelectedAutoDists(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);

        return iToolBar;
    }



    /**
     * This method should return the main content for the frame. <P>
     * Such as an object table.
     *
     * @return The main content for this frame.
     */


    public JComponent getMainContent() {


        iModel = new SSAutoDistTableModel();

        iModel.addColumn( SSAutoDistTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSAutoDistTableModel.COLUMN_DESCRIPTION);


        iTable = new SSTable();

        iModel.setupTable(iTable);

        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSAutoDist iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber;
                if (iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getAutoDist(iSelected);
                } else {
                    return;
                }
                if (iSelected != null) {
                    SSAutoDistDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "autodistframe.autodistgone", iNumber);
                }
            }
        });

        JPanel iPanel = new JPanel();

        iPanel.setLayout(new BorderLayout());
        iPanel.add(new JScrollPane(iTable)/*iTabbedPane*/, BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,4,2));

        return iPanel;
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
     * @return
     */
    public SSAutoDistTableModel getModel() {
        return iModel;
    }


    /**
     *
     */
    private void deleteSelectedAutoDists(java.util.List<SSAutoDist> delete) {
        if (delete.size() == 0) {
            return;
        }

        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "autodistframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSAutoDist iAutoDist : delete) {
                if (SSPostLock.isLocked("autodist" + iAutoDist.getNumber() + SSDB.getInstance().getCurrentCompany().getId())) {
                    new SSErrorDialog(getMainFrame(), "autodistframe.autodistopen", iAutoDist.getNumber());
                } else {
                    SSDB.getInstance().deleteAutoDist(iAutoDist);
                }
            }
        }
    }

    private SSAutoDist getAutoDist(SSAutoDist iAutoDist) {
        return SSDB.getInstance().getAutoDist(iAutoDist);
    }

    private List<SSAutoDist> getAutoDists(List<SSAutoDist> iAutoDists) {
        return SSDB.getInstance().getAutoDists(iAutoDists);
    }

    public void updateFrame() {
        iModel.setObjects(SSDB.getInstance().getAutoDists());
    }
    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        SSAutoDistFrame.cInstance=null;
    }

}
