package se.swedsoft.bookkeeping.gui.ownreport;

import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.ownreport.util.SSOwnReportTableModel;

import se.swedsoft.bookkeeping.data.SSOwnReport;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.print.SSReportFactory;

import java.util.List;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:47:21
 */
public class SSOwnReportFrame extends SSDefaultTableFrame {

    private static SSOwnReportFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( SSOwnReportFrame.cInstance == null || SSOwnReportFrame.cInstance.isClosed() ){
            SSOwnReportFrame.cInstance = new SSOwnReportFrame(pMainFrame, pWidth, pHeight);
        }
        SSOwnReportFrame.cInstance.setVisible(true);
        SSOwnReportFrame.cInstance.deIconize();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSOwnReportFrame getInstance(){
        return SSOwnReportFrame.cInstance;
    }


    private SSTable iTable;

    private SSOwnReportTableModel iModel;

    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSOwnReportFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("ownreportframe.title"), width, height);
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

        // New
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "ownreportframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSOwnReportDialog.newDialog(getMainFrame());
            }
        });
        toolBar.add(iButton);



        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "ownreportframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSOwnReport iSelected = iModel.getSelectedRow(iTable);
                String iName = null;
                if(iSelected != null){
                    iName = iSelected.getName();
                    iSelected = getOwnReport(iSelected);
                }
                if (iSelected != null) {
                    SSOwnReportDialog.editDialog(getMainFrame(), iSelected);
                } else {
                    new SSErrorDialog( getMainFrame(), "ownreportframe.ownreportgone",iName);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "ownreportframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {

                int[] selected = iTable.getSelectedRows();
                List<SSOwnReport> toDelete = iModel.getObjects(selected);
                deleteSelectedOwnReport(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Print
        // ***************************
        iButton = new SSButton("ICON_PRINT", "ownreportframe.printbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSOwnReport iSelected = iModel.getSelectedRow(iTable);
                String iName = null;
                if(iSelected != null){
                    iName = iSelected.getName();
                    iSelected = getOwnReport(iSelected);
                }
                if (iSelected != null) {
                    SSReportFactory.buildOwnReport(getMainFrame(), iSelected);
                } else {
                    new SSErrorDialog( getMainFrame(), "ownreportframe.ownreportgone",iName);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
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
        iModel = new SSOwnReportTableModel();
        iModel.addColumn( SSOwnReportTableModel.COLUMN_NAME);
        iModel.addColumn( SSOwnReportTableModel.COLUMN_PROJECT);
        iModel.addColumn( SSOwnReportTableModel.COLUMN_RESULTUNIT);

        iModel.setupTable(iTable);


        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSOwnReport iSelected = iModel.getSelectedRow(iTable);
                String iName;
                if (iSelected != null) {
                    iName = iSelected.getName();
                    iSelected = getOwnReport(iSelected);
                } else {
                    return;
                }
                if (iSelected != null) {
                    SSOwnReportDialog.editDialog(getMainFrame(), iSelected);
                } else {
                    new SSErrorDialog( getMainFrame(), "ownreportframe.ownreportgone",iName);
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
    public SSTableModel<SSOwnReport> getModel() {
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
        return true;
    }


    /**
     *
     */
    private void deleteSelectedOwnReport(List<SSOwnReport> delete) {
        if (delete.isEmpty()) {
            return;
        }
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "ownreportframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSOwnReport iOwnReport : delete) {
                if (SSPostLock.isLocked("ownreport" + iOwnReport.getId() + SSDB.getInstance().getCurrentCompany().getId())){
                    new SSErrorDialog(getMainFrame(), "ownreportframe.ownreportopen",iOwnReport.getName());
                } else {
                    SSDB.getInstance().deleteOwnReport(iOwnReport);
                }
            }
        }
    }

    private SSOwnReport getOwnReport(SSOwnReport iOwnReport) {
        return SSDB.getInstance().getOwnReport(iOwnReport);
    }

    public void updateFrame() {
        iModel.setObjects(SSDB.getInstance().getOwnReports());
    }





}
