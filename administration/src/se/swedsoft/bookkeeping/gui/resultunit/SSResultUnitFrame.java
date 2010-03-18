/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.resultunit;

import se.swedsoft.bookkeeping.data.SSNewResultUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.resultunit.util.SSResultUnitTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSProgressDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.print.dialog.SSPeriodSelectionDialog;
import se.swedsoft.bookkeeping.print.report.SSResultUnitPrinter;
import se.swedsoft.bookkeeping.print.report.SSResultUnitRevenuePrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class SSResultUnitFrame extends SSDefaultTableFrame {

    private static SSResultUnitFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( cInstance == null || cInstance.isClosed() ){
            cInstance = new SSResultUnitFrame(pMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
        cInstance.deIconize();

    }

    /**
     *
     * @return The SSNewProjectFrame
     */
    public static SSResultUnitFrame getInstance(){
        return cInstance;
    }



    private SSResultUnitTableModel iModel;

    private SSTable iTable;


    /**
     * Default constructor. <P>
     * @param frame
     * @param width
     * @param height
     */
    private SSResultUnitFrame(SSMainFrame frame, int width, int height) {
        super(frame, SSBundle.getBundle().getString("resultunitframe.title"), width, height);
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

        // Ny resultatenhet
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "resultunitframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSResultUnitDialog.newDialog(getMainFrame(), iModel);
            }
        });
        iToolBar.add(iButton);

        // Ändra resultatenhet
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "resultunitframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSNewResultUnit iSelected = getSelected();
                String iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getResultUnit(iSelected);
                }
                if (iSelected != null) {
                    SSResultUnitDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "resultunitframe.resultunitgone", iNumber);
                }
            }
        });
        iToolBar.add(iButton);
        iToolBar.addSeparator();
        iTable.addSelectionDependentComponent(iButton);

        // Ta bort resultatenhet
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "resultunitframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSNewResultUnit> toDelete = iModel.getObjects(selected);
                deleteSelectedResultUnits(toDelete);
            }
        });
        iToolBar.add(iButton);
        iToolBar.addSeparator();
        iTable.addSelectionDependentComponent(iButton);

        // Skriv ut resultatenhet
        // ***************************
        SSMenuButton iButton2 = new SSMenuButton("ICON_PRINT", "resultunitframe.printbutton");
        iButton2.add("resultunitframe.print.resultunitrevenue", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                ResultUnitRevenueReport();
            }
        });
        iButton2.add("resultunitframe.print.resultunitlist", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                printResultUnits();
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
        iTable = new SSTable();

        iModel = new SSResultUnitTableModel();
        iModel.addColumn(SSResultUnitTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSResultUnitTableModel.COLUMN_NAME);


        iModel.setupTable(iTable);

        iTable.addDblClickListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewResultUnit iSelected = getSelected();
                if(iSelected == null)
                    return;

                String iNumber = iSelected.getNumber();
                iSelected = getResultUnit(iSelected);
                if (iSelected != null) {
                    SSResultUnitDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "resultunitframe.resultunitgone", iNumber);
                }
            }
        });


        JPanel iPanel = new JPanel();

        iPanel.setLayout(new BorderLayout());
        iPanel.add(new JScrollPane(iTable), BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,2,2));

        return iPanel;
    }

    /**
     *
     * @return
     */
    private SSNewResultUnit getSelected(){
        int selected = iTable.getSelectedRow();

        if (selected >= 0) {
            return  iModel.getObject(selected);
        }
        return null;
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
    private void deleteSelectedResultUnits(List<SSNewResultUnit> delete) {
        if (delete.isEmpty()) {
            return;
        }
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "resultunitframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSNewResultUnit iResultUnit : delete) {
                if (SSPostLock.isLocked("resultunit" + iResultUnit.getNumber() + SSDB.getInstance().getCurrentCompany().getId())) {
                    new SSErrorDialog(getMainFrame(), "resultunitframe.resultunitopen", iResultUnit.getNumber());
                } else {
                    SSDB.getInstance().deleteResultUnit(iResultUnit);
                }
            }
        }
    }

    private void ResultUnitRevenueReport() {
        final SSResultUnitRevenuePrinter iPrinter;
        List<SSNewResultUnit> iResultUnits;
        if (iTable.getSelectedRowCount() > 0) {

            int iOption = SSQueryDialog.showDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, "resultunitframe.printallorselected");

            switch(iOption ){
                case JOptionPane.YES_OPTION:
                    iResultUnits = iModel.getObjects(iTable.getSelectedRows());
                    iResultUnits = getResultUnits(iResultUnits);
                    break;
                case JOptionPane.NO_OPTION :
                    iResultUnits = SSDB.getInstance().getResultUnits();
                    break;
                default:
                    return;
            }
        } else {
            iResultUnits = SSDB.getInstance().getResultUnits();
        }

        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(getMainFrame(), SSBundle.getBundle().getString("resultunitrevenue.perioddialog.title"));
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

        iPrinter = new SSResultUnitRevenuePrinter(iResultUnits,iFrom,iTo);

        SSProgressDialog.runProgress(getMainFrame(), new Runnable(){
            public void run() {
                iPrinter.preview(getMainFrame());
            }
        });
    }

    /**
     *
     */
    private void printResultUnits() {
        final SSResultUnitPrinter iPrinter;
        List<SSNewResultUnit> iResultUnits;
        if (iTable.getSelectedRowCount() > 0) {

            SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, "resultunitframe.print");
            int iResponce = iDialog.getResponce();

            switch (iResponce) {
                case JOptionPane.YES_OPTION:
                    iResultUnits = getResultUnits(iModel.getObjects(iTable.getSelectedRows()));
                    iPrinter = new SSResultUnitPrinter(iResultUnits);
                    break;
                case JOptionPane.NO_OPTION :
                    iResultUnits = getResultUnits(iModel.getObjects());
                    iPrinter = new SSResultUnitPrinter(iResultUnits);
                    break;
                default:
                    return;
            }
        } else {
            iResultUnits = getResultUnits(iModel.getObjects(iTable.getSelectedRows()));
            iPrinter = new SSResultUnitPrinter(iResultUnits);
        }
        SSProgressDialog.runProgress(getMainFrame(), new Runnable() {
            public void run() {
                iPrinter.preview( getMainFrame() );
            }
        });


    }

    private SSNewResultUnit getResultUnit(SSNewResultUnit iResultUnit) {
        return SSDB.getInstance().getResultUnit(iResultUnit);
    }

    private List<SSNewResultUnit> getResultUnits(List<SSNewResultUnit> iResultUnits) {
        return SSDB.getInstance().getResultUnits(iResultUnits);
    }

    public void updateFrame() {
        iModel.setObjects(SSDB.getInstance().getResultUnits());
    }
    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        SSResultUnitFrame.cInstance=null;
    }
}
