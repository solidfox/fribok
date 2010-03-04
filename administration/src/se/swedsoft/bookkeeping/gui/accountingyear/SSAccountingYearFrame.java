/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.accountingyear;

import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.system.*;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSInformationDialog;
import se.swedsoft.bookkeeping.gui.accountingyear.dialog.SSEditAccountingYearDialog;
import se.swedsoft.bookkeeping.gui.accountingyear.dialog.SSNewAccountingYearDialog;
import se.swedsoft.bookkeeping.gui.accountingyear.util.SSSystemYearDataModel;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.*;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSDateCellRenderer;
import se.swedsoft.bookkeeping.gui.util.frame.*;
import se.swedsoft.bookkeeping.SSVersion;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.util.Date;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;


/**
 * Date: 2006-feb-15
 * Time: 11:17:40
 */
public class SSAccountingYearFrame extends SSDefaultTableFrame {


    private static SSAccountingYearFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( cInstance == null || cInstance.isClosed() ){
            cInstance = new SSAccountingYearFrame(pMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
        cInstance.deIconize();
    }

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(final SSMainFrame pMainFrame, int pWidth, int pHeight, boolean pShowNewDialog){
        if( cInstance == null || cInstance.isClosed() ){
            cInstance = new SSAccountingYearFrame(pMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
        cInstance.deIconize();

        if(pShowNewDialog){
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    SSNewAccountingYearDialog.showDialog(pMainFrame, cInstance.getModel() );
                }
            });
        }

    }

    /**
     *
     * @return The SSNewAccountingYearFrame
     */
    public static SSAccountingYearFrame getInstance(){
        return cInstance;
    }





    private SSTable iTable;

    private SSDefaultTableModel<SSNewAccountingYear> iModel;



    /**
     * Constructor
     * @param pMainFrame
     * @param width
     * @param height
     */
    private SSAccountingYearFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("accountingyearframe.title"), width, height);
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e){
                updateFrame();
            }
        });

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

        // Open
        // ***************************
        iButton = new SSButton("ICON_OPENITEM", "accountingyearframe.openbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                openSelectedAccountingYear();
            }
        });
        toolBar.add(iButton);
        toolBar.addSeparator();
        iTable.addSelectionDependentComponent(iButton);


        // New
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "accountingyearframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                updateFrame();
                SSNewAccountingYearDialog.showDialog(getMainFrame(), iModel);
                updateFrame();
            }
        });
        iButton.setEnabled(true);
        toolBar.add(iButton);
        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "accountingyearframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                editSelectedAccountingYear();
            }
        });
        toolBar.add(iButton);
        iTable.addSelectionDependentComponent(iButton);


        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "accountingyearframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                deleteSelectedAccountingYear();
            }
        });
        toolBar.add(iButton);
        iTable.addSelectionDependentComponent(iButton);



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
        iModel = new SSSystemYearDataModel();

        // Get the objects.
        List<SSNewAccountingYear> iYears = SSDB.getInstance().getYears();


        Collections.sort(iYears, new Comparator<SSNewAccountingYear>() {
            public int compare(SSNewAccountingYear o1, SSNewAccountingYear o2) {
                return o1.getFrom().compareTo(o2.getFrom());
            }
        });
        iModel.setObjects(iYears);


        iTable = new SSTable();
        iTable.setDefaultRenderer(Date.class, new SSDateCellRenderer());

        iTable.setSingleSelect();
        iTable.setColumnSortingEnabled(false);
        iTable.setModel(iModel);

        iTable.getColumnModel().getColumn(0).setPreferredWidth(55);
        iTable.getColumnModel().getColumn(1).setPreferredWidth(70);
        iTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        iTable.getColumnModel().getColumn(3).setPreferredWidth(200);

        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSAccountingYearFrame.this.openSelectedAccountingYear();
            }
        });


        JPanel iPanel = new JPanel();

        iPanel.setLayout(new BorderLayout());
        iPanel.add(new JScrollPane(iTable), BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,2,2));

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
     *
     * @return The selected yeardata, if any
     */
    private SSNewAccountingYear getSelected(){
        int selected = iTable.getSelectedRow();

        if (selected >= 0){
            return iModel.getObject(selected);
        }
        return null;
    }

    /**
     *
     * @return
     */
    public SSDefaultTableModel<SSNewAccountingYear> getModel() {
        return iModel;
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
     */
    private void openSelectedAccountingYear() {
        //Hämta markerat år
        SSNewAccountingYear iNewYear = getSelected();

        // Kontrollera att ett år blev valt
        if (iNewYear == null) {
            //Inget år markerat. Visa felmeddelande.
            new SSErrorDialog(getMainFrame(), "accountingyearframe.selectone");
            return;
        }
        //Stäng fönstret om året är öppet
        if (iNewYear.equals(SSDB.getInstance().getCurrentYear())) {
            SSAccountingYearFrame.getInstance().dispose();
            return;
        }
        //Kontrollera att året fortfarande finns i databasen
        iNewYear = SSDB.getInstance().getAccountingYear(iNewYear);
        if (iNewYear == null) {
            //Året fanns inte kvar i databasen. Visa felmeddelande.
            new SSErrorDialog(getMainFrame(), "accountingyearframe.yeargone");
            return;
        }

        //Fråga om året ska öppnas.
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), SSBundle.getBundle(), "accountingyearframe.replaceyear", iNewYear.toRenderString());
        if (iDialog.getResponce() != JOptionPane.YES_OPTION) {
            //Svarade inte ja. Avbryt funktionen
            return;
        }
        //Lås upp förra året
        SSYearLock.removeLock(SSDB.getInstance().getCurrentYear());
        //Sätt det valda året som nuvarande år
        SSDB.getInstance().setCurrentYear(iNewYear);
        SSDB.getInstance().initYear(true);
        SSDBConfig.setYearId(SSDB.getInstance().getCurrentCompany().getId(),iNewYear.getId());

        //Lås nya året
        SSYearLock.applyLock(iNewYear);
        //Stäng alla fönster
        SSFrameManager.getInstance().close();
    }


    /**
     *
     */
    private void editSelectedAccountingYear(){
        SSNewAccountingYear iAccountingYear = getSelected();
        updateFrame();
        if(iAccountingYear == null){
            new SSErrorDialog( getMainFrame(), "accountingyearframe.selectone" );
            return;
        }

        //Kontrollera att året fortfarande finns i databasen
        iAccountingYear = SSDB.getInstance().getAccountingYear(iAccountingYear);
        if (iAccountingYear == null) {
            //Året fanns inte kvar i databasen. Visa felmeddelande.
            new SSErrorDialog(getMainFrame(), "accountingyearframe.yeargone");
            return;
        }

        // Ask the user if he want's to open the selected year to be able to edit it
        if (!iAccountingYear.equals(SSDB.getInstance().getCurrentYear())) {

            // Ask to open the year
            String iCurrent = SSDB.getInstance().getCurrentYear() == null ? "" : SSDB.getInstance().getCurrentYear().toRenderString();
            String iNew = iAccountingYear.toRenderString();

            SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), SSBundle.getBundle(), "accountingyearframe.openyear", iNew, iCurrent);

            if (iDialog.getResponce() != JOptionPane.YES_OPTION) {
                return;
            }

            //Lås upp förra året
            SSYearLock.removeLock(SSDB.getInstance().getCurrentYear());
            //Sätt det valda året som nuvarande år
            SSDB.getInstance().setCurrentYear(iAccountingYear);
            SSDB.getInstance().initYear(true);
            SSDBConfig.setYearId(SSDB.getInstance().getCurrentCompany().getId(),iAccountingYear.getId());

            //Lås nya året
            SSYearLock.applyLock(iAccountingYear);
            //Stäng alla fönster
            SSFrameManager.getInstance().close();
        }

        SSNewAccountingYear iYearData = SSDB.getInstance().getCurrentYear();

        SSEditAccountingYearDialog.showDialog(getMainFrame(), iYearData, iModel);
        updateFrame();
    }


    /**
     *
     */
    private void deleteSelectedAccountingYear(){
        SSNewAccountingYear pAccountingYear = getSelected();
        if(pAccountingYear == null){
            new SSErrorDialog( getMainFrame(), "accountingyearframe.selectone" );
            return;
        }
        updateFrame();
        boolean lockRemoved =false;
        if (pAccountingYear.equals(SSDB.getInstance().getCurrentYear())) {
            SSYearLock.removeLock(pAccountingYear);
            lockRemoved = true;
        }

        SSQueryDialog iDialog = new SSQueryDialog( getMainFrame(), SSBundle.getBundle(),  "accountingyearframe.delete", pAccountingYear.toRenderString());

        if( iDialog.getResponce() != JOptionPane.YES_OPTION ){
            if(lockRemoved)
                SSYearLock.applyLock(pAccountingYear);
            return;
        }
        updateFrame();

        if(SSYearLock.isLocked(pAccountingYear)){
            new SSErrorDialog( getMainFrame(), "accountingyearframe.yearopen");
            if(lockRemoved)
                SSYearLock.applyLock(pAccountingYear);
            return;
        }
        if(pAccountingYear.equals(SSDB.getInstance().getCurrentYear())){
            SSYearLock.removeLock(SSDB.getInstance().getCurrentYear());
            SSDBConfig.setYearId(SSDB.getInstance().getCurrentCompany().getId(), null);
            SSDB.getInstance().setCurrentYear(null);
        }

        SSDB.getInstance().deleteAccountingYear(pAccountingYear);
        updateFrame();
    }

    public void updateFrame() {
        iModel.setObjects(SSDB.getInstance().getYears());
        SSDB.getInstance().notifyListeners("YEAR", SSDB.getInstance().getCurrentYear(), null);
    }
    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        SSAccountingYearFrame.cInstance=null;
    }
}




