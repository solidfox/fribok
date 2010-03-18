package se.swedsoft.bookkeeping.gui.tender;

import se.swedsoft.bookkeeping.data.SSTender;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSMail;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.order.SSOrderDialog;
import se.swedsoft.bookkeeping.gui.order.SSOrderFrame;
import se.swedsoft.bookkeeping.gui.tender.panel.SSTenderSearchPanel;
import se.swedsoft.bookkeeping.gui.tender.util.SSTenderTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.util.components.SSTabbedPanePanel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSInformationDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.print.SSReportFactory;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:47:21
 */
public class SSTenderFrame extends SSDefaultTableFrame {

    private static SSTenderFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( cInstance == null || cInstance.isClosed() ){
            cInstance = new SSTenderFrame(pMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
        cInstance.deIconize();
        cInstance.updateFrame();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSTenderFrame getInstance(){
        return cInstance;
    }




    private SSTable iTable;

    private SSTenderTableModel iModel;

    private JTabbedPane iTabbedPane;

    private SSTenderSearchPanel iSearchPanel;

    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSTenderFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("tenderframe.title"), width, height);
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

        JMenuItem iMenuItem;
        // New
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "tenderframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSTenderDialog.newDialog(getMainFrame(), iModel);
            }
        });
        iToolBar.add(iButton);



        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "tenderframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSTender iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getTender(iSelected);
                }
                if (iSelected != null) {
                    SSTenderDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "tenderframe.tendergone",iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Copy
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "tenderframe.copybutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSTender iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getTender(iSelected);
                }
                if (iSelected != null) {
                    SSTenderDialog.copyDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "tenderframe.tendergone",iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "tenderframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSTender> toDelete = iModel.getObjects(selected);
                deleteSelectedTender(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();


        // Create order
        // ***************************
        iButton = new SSButton("ICON_INVOICE24", "tenderframe.createorderbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSTender iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getTender(iSelected);
                }
                if (iSelected == null) {
                    new SSErrorDialog(getMainFrame(), "tenderframe.tendergone", iNumber);
                    return;
                }

                // if the selected tender already has an order assosiated we can't create a new one
                if (iSelected.getOrder() != null) {
                    SSInformationDialog.showDialog(getMainFrame(), "tenderframe.tenderhasorder", iSelected.getNumber());
                    return;
                }

                if (SSOrderFrame.getInstance() != null) {
                    SSOrderDialog.newDialog(getMainFrame(), iSelected, SSOrderFrame.getInstance().getModel());
                } else {
                    SSOrderDialog.newDialog(getMainFrame(), iSelected, null);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);


        iToolBar.addSeparator();



        // Print
        // ***************************
        SSMenuButton iButton2 = new SSMenuButton("ICON_PRINT", "tenderframe.printbutton");
        iMenuItem = iButton2.add("tenderframe.print.tenderreport", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                List<SSTender> iSelected = iModel.getSelectedRows(iTable);
                iSelected = getTenders(iSelected);
                if (!iSelected.isEmpty()) {
                    SSReportFactory.TenderReport(getMainFrame(), iSelected);
                }
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);
        iMenuItem = iButton2.add("tenderframe.print.emailtenderreport", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSTender iSelected = iModel.getSelectedRow(iTable);
                iSelected = getTender(iSelected);
                if(iSelected == null) return;

                if (!SSMail.isOk(iSelected.getCustomer())) {
                    return;
                }
                SSReportFactory.EmailTenderReport(getMainFrame(), iSelected);
            }
        });
        iTable.addSelectionDependentComponent(iMenuItem);
        iButton2.addSeparator();
        iButton2.add("tenderframe.print.tenderlistreport", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.TenderListReport( getMainFrame() );
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

        iModel = new SSTenderTableModel();
        iModel.addColumn( SSTenderTableModel.COLUMN_PRINTED);
        iModel.addColumn( SSTenderTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSTenderTableModel.COLUMN_CUSTOMER_NR );
        iModel.addColumn( SSTenderTableModel.COLUMN_CUSTOMER_NAME );
        iModel.addColumn( SSTenderTableModel.COLUMN_DATE );
        iModel.addColumn( SSTenderTableModel.COLUMN_NET_SUM );
        iModel.addColumn( SSTenderTableModel.COLUMN_CURRENCY );
        iModel.addColumn( SSTenderTableModel.COLUMN_ORDER );

        iModel.setupTable(iTable);


        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSTender iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber;
                if (iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getTender(iSelected);
                } else {
                    return;
                }
                if (iSelected != null) {
                    SSTenderDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "tenderframe.tendergone",iNumber);
                }
            }
        });
        iTabbedPane = new JTabbedPane();

        iTabbedPane.add(SSBundle.getBundle().getString("tenderframe.filter.1"), new SSTabbedPanePanel() );
        iTabbedPane.add(SSBundle.getBundle().getString("tenderframe.filter.2"), new SSTabbedPanePanel() );

        iTabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                //setFilterIndex( iTabbedPane.getSelectedIndex() );
                updateFrame();
            }
        });
        //setFilterIndex(0);

        iSearchPanel = new SSTenderSearchPanel(iModel);
        JPanel iPanel = new JPanel();
        //iSearchPanel.ApplyFilter(SSDB.getInstance().getTenders());
        iPanel.setLayout(new BorderLayout());
        iPanel.add(iSearchPanel,BorderLayout.NORTH);
        iPanel.add(iTabbedPane, BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,4,2));

        return iPanel;
    }



    /**
     *
     * @param index
     * @param iList
     */
    public void setFilterIndex(int index,List<SSTender> iList){
        JPanel iPanel = (JPanel)iTabbedPane.getComponentAt( index );

        iPanel.removeAll();
        iPanel.add(new JScrollPane(iTable), BorderLayout.CENTER);

        List<SSTender> iFiltered = Collections.emptyList();


        switch(index){
            // Alla
            case 0:
                iFiltered =  iList;
                break;
                // Ordrar utan faktura
            case 1:
                iFiltered = new LinkedList<SSTender>();
                for(SSTender iTender : iList){
                    if(! iTender.isExpired()){
                        iFiltered.add(iTender);
                    }
                }
                break;
        }

        iModel.setObjects(iFiltered);
        iTabbedPane.repaint();
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

    public JTabbedPane getTabbedPane() {
        return iTabbedPane;
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

    public SSTenderTableModel getModel() {
        return iModel;
    }

    private void deleteSelectedTender(List<SSTender> delete) {
        if (delete.isEmpty()) {
            return;
        }
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "tenderframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSTender iTender : delete) {
                if (SSPostLock.isLocked("tender" + iTender.getNumber() + SSDB.getInstance().getCurrentCompany().getId())){
                    new SSErrorDialog(getMainFrame(), "tenderframe.tenderopen",iTender.getNumber());
                } else {
                    SSDB.getInstance().deleteTender(iTender);
                }
            }
        }
    }

    private SSTender getTender(SSTender iTender) {
        return SSDB.getInstance().getTender(iTender);
    }

    private List<SSTender> getTenders(List<SSTender> iTenders) {
        return SSDB.getInstance().getTenders(iTenders);
    }

    public void updateFrame() {
        iSearchPanel.ApplyFilter(SSDB.getInstance().getTenders());
    }

    public void actionPerformed(ActionEvent e)
    {
        iTable=null;
        iModel=null;
        iTabbedPane=null;
        SSTenderFrame.cInstance=null;
    }



}
