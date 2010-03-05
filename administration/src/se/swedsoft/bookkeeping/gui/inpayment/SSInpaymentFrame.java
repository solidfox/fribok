package se.swedsoft.bookkeeping.gui.inpayment;

import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.SSInpayment;
import se.swedsoft.bookkeeping.data.SSInpaymentRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.inpayment.panel.SSInpaymentSearchPanel;
import se.swedsoft.bookkeeping.gui.inpayment.util.SSInpaymentTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.print.SSReportFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:47:21
 */
public class SSInpaymentFrame extends SSDefaultTableFrame {

    private static SSInpaymentFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight){
        if( SSInpaymentFrame.cInstance == null || SSInpaymentFrame.cInstance.isClosed() ){
            SSInpaymentFrame.cInstance = new SSInpaymentFrame(pMainFrame, pWidth, pHeight);
        }
        SSInpaymentFrame.cInstance.setVisible(true);
        SSInpaymentFrame.cInstance.deIconize();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSInpaymentFrame getInstance(){
        return SSInpaymentFrame.cInstance;
    }


    private SSTable iTable;

    private SSInpaymentTableModel iModel;

    private SSInpaymentSearchPanel iSearchPanel;

    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSInpaymentFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("inpaymentframe.title"), width, height);
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

        // New
        // ***************************
        iButton = new SSButton("ICON_NEWITEM", "inpaymentframe.newbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSInpaymentDialog.newDialog(getMainFrame(), iModel);
            }
        });
        toolBar.add(iButton);



        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "inpaymentframe.editbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSInpayment iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;
                if(iSelected != null){
                    iNumber = iSelected.getNumber();
                    iSelected = getInpayment(iSelected);
                }
                if (iSelected != null) {
                    SSInpaymentDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "inpaymentframe.inpaymentgone",iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "inpaymentframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {

                int[] selected = iTable.getSelectedRows();
                List<SSInpayment> toDelete = iModel.getObjects(selected);
                deleteSelectedInpayment(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Print
        // ***************************
        iMenuButton = new SSMenuButton<SSButton>("ICON_PRINT", "inpaymentframe.printbutton");
        iMenuButton.add("inpaymentframe.print.inpaymentlist", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.InpaymentList(getMainFrame());
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

        iModel = new SSInpaymentTableModel();

        iTable = new SSTable();

        iTable.setModel(iModel);

        SSInpaymentTableModel.setupTable(iTable);


        iTable.addDblClickListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSInpayment iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber;
                if (iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getInpayment(iSelected);
                } else {
                    return;
                }
                if (iSelected != null) {
                    SSInpaymentDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog( getMainFrame(), "inpaymentframe.inpaymentgone",iNumber);
                }
            }
        });

        iSearchPanel = new SSInpaymentSearchPanel(iModel);
        JPanel iPanel = new JPanel();

        iPanel.setLayout(new BorderLayout());
        iPanel.add(iSearchPanel, BorderLayout.NORTH);
        iPanel.add(new JScrollPane(iTable), BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,4,2));

        return iPanel;
    }

    /**
     * 
     * @return
     */
    public SSDefaultTableModel<SSInpayment> getModel() {
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
    private void deleteSelectedInpayment(List<SSInpayment> delete) {
        if (delete.isEmpty()) {
            return;
        }
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "inpaymentframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSInpayment iInpayment : delete) {
                if (SSPostLock.isLocked("inpayment" + iInpayment.getNumber() + SSDB.getInstance().getCurrentCompany().getId())){
                    new SSErrorDialog(getMainFrame(), "inpaymentframe.inpaymentopen",iInpayment.getNumber());
                } else {
                    for(SSInpaymentRow iRow : iInpayment.getRows()){
                        if(iRow.getValue() != null && iRow.getInvoiceNr() != null){
                            if(SSInvoiceMath.iSaldoMap.containsKey(iRow.getInvoiceNr())){
                                SSInvoiceMath.iSaldoMap.put(iRow.getInvoiceNr(), SSInvoiceMath.iSaldoMap.get(iRow.getInvoiceNr()).add(iRow.getValue()));
                            }
                        }
                    }
                    SSDB.getInstance().deleteInpayment(iInpayment);
                }
            }
        }
    }

    private SSInpayment getInpayment(SSInpayment iInpayment) {
        return SSDB.getInstance().getInpayment(iInpayment);
    }

    public void updateFrame() {
        iSearchPanel.ApplyFilter();
    }





}
