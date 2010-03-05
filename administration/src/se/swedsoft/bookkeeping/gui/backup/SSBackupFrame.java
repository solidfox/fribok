/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.backup;

import se.swedsoft.bookkeeping.data.backup.SSBackup;
import se.swedsoft.bookkeeping.data.backup.SSBackupDatabase;
import se.swedsoft.bookkeeping.data.backup.util.SSBackupFactory;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.backup.util.SSBackupTableModel;
import se.swedsoft.bookkeeping.gui.company.SSCompanyFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSBackupFileChooser;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.frame.SSFrameManager;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.util.SSException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 */
public class SSBackupFrame extends SSDefaultTableFrame {

    private static SSBackupFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( cInstance == null || cInstance.isClosed() ){
            cInstance = new SSBackupFrame(pMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
        cInstance.deIconize();
    }

    /**
     *
     */
    public static void hideFrame(){
        if( cInstance != null  ){
            cInstance.setVisible(true);
        }
    }

    /**
     *
     * @return The SSNewAccountingYearFrame
     */
    public static SSBackupFrame getInstance(){
        return cInstance;
    }


    private SSBackupDatabase iDatabase;

    private SSTable iTable;

    private SSTableModel<SSBackup> iModel;


    /**
     * Default constructor.
     */
    private SSBackupFrame(SSMainFrame pMainFrame, int pWidth, int pHeight) {
        super(pMainFrame, SSBundle.getBundle().getString("backupframe.title"), pWidth, pHeight);
    }


    /**
     * This method should return a toolbar if the sub-class wants one.
     * Otherwise, it may return null.
     *
     * @return A JToolBar or null.
     */
    @Override
    public JToolBar getToolBar() {
        JToolBar toolBar = new JToolBar();

        SSButton iButton;

        // Restore from selected backup
        // ***************************
        iButton = new SSButton("ICON_IMPORT", "backupframe.restorebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSBackup iBackup = getSelected();

                // If nothing selected, return
                if(iBackup == null){
                    new SSErrorDialog( getMainFrame(), "backupframe.selectone" );
                    return;
                }

                if(! iBackup.exists() ){
                    iDatabase.getBackups().remove(iBackup);
                    iDatabase.notifyUpdated();

                    new SSErrorDialog(getMainFrame(), "backupframe.missingfile");

                    return;
                }
                try{
                    SSBackupFactory.restoreBackup(iBackup.getFilename());

                    SSDB.getInstance().setCurrentCompany(null);
                    SSDB.getInstance().setCurrentYear(null);
                    iModel.fireTableDataChanged();
                    SSFrameManager.getInstance().close();

                    SSCompanyFrame.showFrame(getMainFrame(),500,300);
                } catch (SSException ex) {
                    new SSErrorDialog(getMainFrame(), "exceptiondialog", ex.getLocalizedMessage());
                }
            }
        });
        toolBar.add(iButton);
        toolBar.addSeparator();
        iTable.addSelectionDependentComponent(iButton);


        // Restore from external backup
        // ***************************
        iButton = new SSButton("ICON_OPENITEM", "backupframe.openbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSBackupFileChooser iFileChooser = SSBackupFileChooser.getInstance();

                if(iFileChooser.showOpenDialog( getMainFrame() ) != JOptionPane.OK_OPTION ) return;

                String iFileName = iFileChooser.getSelectedFile().getAbsolutePath();

                try {
                    SSBackupFactory.restoreBackup(iFileName );

                    SSDB.getInstance().setCurrentCompany(null);
                    SSDB.getInstance().setCurrentYear(null);

                    iModel.fireTableDataChanged();
                    
                    SSCompanyFrame.showFrame(getMainFrame(),500,300);
                } catch (SSException ex) {
                    new SSErrorDialog(getMainFrame(), "exceptiondialog", ex.getLocalizedMessage());
                }
            }
        });
        toolBar.add(iButton);

        // Ta bort backup
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "backupframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                deleteSelectedBackup();

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
        iDatabase = SSBackupDatabase.getInstance();

        List<SSBackup> iBackups = iDatabase.getBackups();

        Collections.sort(iBackups, new Comparator<SSBackup>() {
            public int compare(SSBackup o1, SSBackup o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        iTable = new SSTable();

        iModel = new SSBackupTableModel(iBackups);
        iModel.addColumn( SSBackupTableModel.COLUMN_DATE );
        iModel.addColumn( SSBackupTableModel.COLUMN_FILENAME );
        iModel.addColumn( SSBackupTableModel.COLUMN_TYPE );
        iModel.setupTable(iTable);

        JPanel iPanel = new JPanel();
        iPanel.setLayout( new BorderLayout() );
        iPanel.add( new JScrollPane(iTable), BorderLayout.CENTER);
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
     * @return The selected backup, if any
     */
    private SSBackup getSelected(){
        int selected = iTable.getSelectedRow();

        if (selected >= 0){
            return iModel.getObject(selected);
        }
        return null;
    }



    /**
     *
     */
    private void deleteSelectedBackup(){
        final SSBackup iBackup = getSelected();

        // If nothing selected, return
        if(iBackup == null){
            new SSErrorDialog( getMainFrame(), "backupframe.selectone" );
            return;
        }

        SSQueryDialog iDialog = new SSQueryDialog( getMainFrame(), SSBundle.getBundle(),  "backupframe.delete", iBackup.getFilename());

        if( iDialog.getResponce() != JOptionPane.YES_OPTION ){
            return;
        }
        try {
            iBackup.delete();

            iDatabase.getBackups().remove(iBackup);

            iModel.fireTableDataChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    /**
     *
     * @return boolean
     */
    @Override
    public boolean isCompanyFrame() {
        return false;
    }

    /**
     *
     * @return boolean
     */
    @Override
    public boolean isYearDataFrame() {
        return false;
    }
    
    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        SSBackupFrame.cInstance=null;
    }
}
