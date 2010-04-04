package se.swedsoft.bookkeeping.gui.company;

import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.system.*;
import se.swedsoft.bookkeeping.data.util.SSConfig;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.accountingyear.SSAccountingYearFrame;
import se.swedsoft.bookkeeping.gui.company.util.SSCompanyTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.frame.SSFrameManager;
import se.swedsoft.bookkeeping.gui.util.frame.SSInternalFrame;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Date: 2006-feb-02
 * Time: 10:40:47
 */
public class SSCompanyFrame extends SSDefaultTableFrame {

    private static SSCompanyFrame cInstance;

    private SSTable iTable;

    private JCheckBox iShowAtStartup;

    private SSDefaultTableModel<SSNewCompany> iModel;


    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( cInstance == null || cInstance.isClosed() ){
            cInstance = new SSCompanyFrame(pMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
        cInstance.deIconize();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSCompanyFrame getInstance(){
        return cInstance;
    }

    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSCompanyFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("companyframe.title"), width, height);
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
        JToolBar iToolBar = new JToolBar();

        // Open
        // ***************************
        SSButton iButton = new SSButton("ICON_OPENITEM", "companyframe.openbutton", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSelectedCompany();
            }
        });
        iToolBar.add(iButton);
        iToolBar.addSeparator();
        iTable.addSelectionDependentComponent(iButton);



        // New
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "companyframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                updateFrame();
                SSCompanyDialog.newDialog(getMainFrame(), iModel);
            }
        });
        iButton.setEnabled(true);
        iToolBar.add(iButton);



        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "companyframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                editSelectedCompany();
            }
        });
        iToolBar.add(iButton);
        iTable.addSelectionDependentComponent(iButton);

        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "companyframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                deleteSelectedCompany();
            }
        });
        iToolBar.add(iButton);
        iTable.addSelectionDependentComponent(iButton);



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
        iModel = new SSCompanyTableModel();
        updateFrame();

        iShowAtStartup = new JCheckBox(SSBundle.getBundle().getString("companyframe.showatstart"));
        iShowAtStartup.setSelected((Boolean) SSConfig.getInstance().get("companyframe.showatstart", true));

        iShowAtStartup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSConfig.getInstance().set("companyframe.showatstart", iShowAtStartup.isSelected());
            }
        });


        iTable = new SSTable();

        iTable.setSingleSelect();
        iTable.setColumnSortingEnabled(false);
        iTable.setModel(iModel);

        iTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        iTable.getColumnModel().getColumn(0).setMaxWidth(70);

        iTable.addDblClickListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSelectedCompany();
            }
        });
        JPanel iPanel = new JPanel();

        iPanel.setLayout(new BorderLayout());
        iPanel.add(new JScrollPane(iTable), BorderLayout.CENTER);
        iPanel.add(iShowAtStartup, BorderLayout.SOUTH);
        iPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        return iPanel;
    }

    /**
     *
     * @return The selected company, if any
     */
    private SSNewCompany getSelected(){
        int selected = iTable.getSelectedRow();

        if (selected >= 0){
            return iModel.getObject(selected);
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
        return false;
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
     * Öppnar det valda företaget
     */
    private void openSelectedCompany(){
        //Hämta markerat företag
        SSNewCompany iNewCompany = getSelected();

        // Kontrollera att ett företag blev valt
        if (iNewCompany == null) {
            //Inget företag markerat. Visa felmeddelande.
            new SSErrorDialog(getMainFrame(), "companyframe.selectonecompany");
            return;
        }
        //Stäng fönstret om företaget redan är öppet.
        if (iNewCompany.equals(SSDB.getInstance().getCurrentCompany())) {
            cInstance.dispose();
            return;
        }
        //Kontrollera att företaget fortfarande finns i databasen
        iNewCompany = SSDB.getInstance().getCompany(iNewCompany);
        if (iNewCompany == null) {
            //Företaget fanns inte kvar i databasen. Visa felmeddelande.
            new SSErrorDialog(getMainFrame(), "companyframe.companygone");
            return;
        }

        //Fråga om företaget ska öppnas.
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), SSBundle.getBundle(), "companyframe.replacecompany", iNewCompany.getName());
        if (iDialog.getResponce() != JOptionPane.YES_OPTION) {
            //Svarade inte ja. Avbryt funktionen
            return;
        }
        //Lås upp det förra företaget
        SSCompanyLock.removeLock(SSDB.getInstance().getCurrentCompany());
        SSYearLock.removeLock(SSDB.getInstance().getCurrentYear());

        //Sätt det valda företaget som nuvarande företag
        SSDB.getInstance().setCurrentCompany(iNewCompany);
        SSDBConfig.setCompanyId(iNewCompany.getId());

        //Lås det nya företaget
        SSCompanyLock.applyLock(iNewCompany);
        SSDB.getInstance().setCurrentYear(null);

        //Stäng alla fönster
        SSFrameManager.getInstance().close();

        //Läs in det förra öppna året för företaget
        SSNewAccountingYear iYear = SSDBConfig.loadCompanySetting(iNewCompany.getId());
        if (iYear == null) {
            //Inget år för företaget sparat. Öppna årsfönstret
            SSDBConfig.setYearId(iNewCompany.getId(), null);
            SSAccountingYearFrame.showFrame(getMainFrame(), 500, 300, false);
        } else {
            //Hittade ett sparat år. Sätt det som nuvarande
            SSDB.getInstance().setCurrentYear(iYear);
            SSYearLock.applyLock(iYear);
            SSDBConfig.setYearId(iNewCompany.getId(), iYear.getId());
        }
        SSDB.getInstance().init(true);
    }

    private void editSelectedCompany(){
        SSNewCompany pCompany = getSelected();
        updateFrame();
        // If nothing selected, return
        if(pCompany == null){
            new SSErrorDialog( getMainFrame(), "companyframe.selectonecompany" );
            return;
        }

        //Kontrollera att företaget fortfarande finns i databasen
        pCompany = SSDB.getInstance().getCompany(pCompany);
        if (pCompany == null) {
            //Företaget fanns inte kvar i databasen. Visa felmeddelande.
            new SSErrorDialog(getMainFrame(), "companyframe.companygone");
            return;
        }
        // Ask the user if he want's to open the selected company to be able to edit it
        if (!pCompany.equals(SSDB.getInstance().getCurrentCompany())) {
            if (SSPostLock.isLocked("sieimport" + pCompany.getId())) {
                updateFrame();
                new SSErrorDialog( getMainFrame(), "companyframe.sieimportopen");
                return;
            }
            // Ask to open the company
            String iCurrent = SSDB.getInstance().getCurrentCompany() == null ? "" : SSDB.getInstance().getCurrentCompany().getName();
            String iNew     = pCompany.getName();

            SSQueryDialog iDialog = new SSQueryDialog( getMainFrame(), SSBundle.getBundle(),  "companyframe.editcompany.openquery", iNew, iCurrent);

            if( iDialog.getResponce() != JOptionPane.YES_OPTION ){
                updateFrame();
                return;
            }

            // Close all company related frames
            SSInternalFrame.closeAllFrames();
            // Select the company

            SSCompanyLock.removeLock(SSDB.getInstance().getCurrentCompany());
            SSYearLock.removeLock(SSDB.getInstance().getCurrentYear());

            //Sätt det valda företaget som nuvarande företag
            SSDB.getInstance().setCurrentCompany(pCompany);
            SSDBConfig.setCompanyId(pCompany.getId());

            //Lås det nya företaget
            SSCompanyLock.applyLock(pCompany);
            SSDB.getInstance().setCurrentYear(null);

            //Stäng alla fönster
            SSFrameManager.getInstance().close();

            //Läs in det förra öppna året för företaget
            SSNewAccountingYear iYear = SSDBConfig.loadCompanySetting(pCompany.getId());
            if (iYear == null) {
                //Inget år för företaget sparat. Öppna årsfönstret
                SSDBConfig.setYearId(pCompany.getId(), null);
                SSAccountingYearFrame.showFrame(getMainFrame(), 500, 300, false);
            } else {
                //Hittade ett sparat år. Sätt det som nuvarande
                SSDB.getInstance().setCurrentYear(iYear);
                SSYearLock.applyLock(iYear);
                SSDBConfig.setYearId(pCompany.getId(), iYear.getId());
            }
            SSDB.getInstance().init(true);
        }
        if(cInstance !=null)
            updateFrame();

        SSCompanyDialog.editDialog(getMainFrame(), SSDB.getInstance().getCurrentCompany(), iModel);
    }

    private void deleteSelectedCompany(){
        SSNewCompany pCompany = getSelected();
        if(pCompany == null){
            new SSErrorDialog( getMainFrame(), "companyframe.selectonecompany" );
            return;
        }
        updateFrame();
        boolean lockRemoved =false;
        if (pCompany.equals(SSDB.getInstance().getCurrentCompany())) {
            SSCompanyLock.removeLock(pCompany);
            lockRemoved = true;
        }

        SSQueryDialog iDialog = new SSQueryDialog( getMainFrame(), SSBundle.getBundle(),  "companyframe.deletecompany", pCompany.getName());

        if( iDialog.getResponce() != JOptionPane.YES_OPTION ){
            if(lockRemoved)
                SSCompanyLock.applyLock(pCompany);
            return;
        }
        updateFrame();

        if(SSCompanyLock.isLocked(pCompany)){
            new SSErrorDialog( getMainFrame(), "companyframe.companyremoveopen");
            if(lockRemoved)
                SSCompanyLock.applyLock(pCompany);
            return;
        }
        boolean iCurrentRemoved = false;
        if(pCompany.equals(SSDB.getInstance().getCurrentCompany())){
            SSYearLock.removeLock(SSDB.getInstance().getCurrentYear());
            SSFrameManager.getInstance().close();
            iCurrentRemoved = true;
        }

        SSDB.getInstance().deleteCompany(pCompany);

        if (iCurrentRemoved) {
            SSDB.getInstance().setCurrentCompany(null);
            SSDB.getInstance().setCurrentYear(null);
        }
        updateFrame();
    }

    public void updateFrame() {
        iModel.setObjects(SSDB.getInstance().getCompanies());
        SSDB.getInstance().notifyListeners("COMPANY", SSDB.getInstance().getCurrentCompany(), null);
    }
    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        iShowAtStartup=null;
        cInstance=null;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.company.SSCompanyFrame");
        sb.append("{iModel=").append(iModel);
        sb.append(", iShowAtStartup=").append(iShowAtStartup);
        sb.append(", iTable=").append(iTable);
        sb.append('}');
        return sb.toString();
    }
}
