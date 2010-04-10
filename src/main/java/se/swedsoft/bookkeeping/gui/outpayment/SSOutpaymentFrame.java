package se.swedsoft.bookkeeping.gui.outpayment;


import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.data.SSOutpayment;
import se.swedsoft.bookkeeping.data.SSOutpaymentRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.outpayment.panel.SSOutpaymentSearchPanel;
import se.swedsoft.bookkeeping.gui.outpayment.util.SSOutpaymentTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
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
public class SSOutpaymentFrame extends SSDefaultTableFrame {

    private static SSOutpaymentFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight) {
        if (cInstance == null || SSOutpaymentFrame.cInstance.isClosed()) {
            cInstance = new SSOutpaymentFrame(pMainFrame, pWidth, pHeight);
        }
        SSOutpaymentFrame.cInstance.setVisible(true);
        SSOutpaymentFrame.cInstance.deIconize();
        SSOutpaymentFrame.cInstance.updateFrame();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSOutpaymentFrame getInstance() {
        return cInstance;
    }

    private SSTable iTable;

    private SSOutpaymentTableModel iModel;

    private SSOutpaymentSearchPanel iSearchPanel;

    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSOutpaymentFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("outpaymentframe.title"), width,
                height);
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

        // New
        // ***************************
        SSButton iButton = new SSButton("ICON_NEWITEM", "outpaymentframe.newbutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSOutpaymentDialog.newDialog(getMainFrame(), iModel);
            }
        });

        toolBar.add(iButton);

        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "outpaymentframe.editbutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSOutpayment iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber = null;

                if (iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getOutpayment(iSelected);
                }
                if (iSelected != null) {
                    SSOutpaymentDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "outpaymentframe.outpaymentgone",
                            iNumber);
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "outpaymentframe.deletebutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSOutpayment> toDelete = iModel.getObjects(selected);

                deleteSelected(toDelete);
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        toolBar.add(iButton);

        // Print
        // ***************************
        SSMenuButton<SSButton> iMenuButton = new SSMenuButton<SSButton>("ICON_PRINT",
                "outpaymentframe.printbutton");

        iMenuButton.add("outpaymentframe.print.outaymentlist", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSReportFactory.OutpaymentList(getMainFrame());
            }
        });
        toolBar.add(iMenuButton);

        return toolBar;
    }

    /**
     * This method should return the main content for the frame.
     * Such as an object table.
     *
     * @return The main content for this frame.
     */
    @Override
    public JComponent getMainContent() {
        iTable = new SSTable();

        iModel = new SSOutpaymentTableModel();
        iModel.addColumn(SSOutpaymentTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSOutpaymentTableModel.COLUMN_DATE);
        iModel.addColumn(SSOutpaymentTableModel.COLUMN_TEXT);
        iModel.addColumn(SSOutpaymentTableModel.COLUMN_SUM);
        iModel.setupTable(iTable);

        iTable.addDblClickListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSOutpayment iSelected = iModel.getSelectedRow(iTable);
                Integer iNumber;

                if (iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getOutpayment(iSelected);
                } else {
                    return;
                }
                if (iSelected != null) {
                    SSOutpaymentDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "outpaymentframe.outpaymentgone",
                            iNumber);
                }
            }
        });

        JPanel iPanel = new JPanel();

        iSearchPanel = new SSOutpaymentSearchPanel(iModel);
        iPanel.setLayout(new BorderLayout());

        iPanel.add(iSearchPanel, BorderLayout.NORTH);
        iPanel.add(new JScrollPane(iTable), BorderLayout.CENTER);
        iPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 4, 2));

        return iPanel;
    }

    /**
     *
     * @return
     */
    public SSTableModel<SSOutpayment> getModel() {
        return iModel;
    }

    /**
     * This method should return the status bar content, if any.
     *
     * @return The content for the status bar or null if none is wanted.
     */
    @Override
    public JComponent getStatusBar() {
        return null;
    }

    /**
     * Indicates whether this frame is a company data related frame.
     *
     * @return A boolean value.
     */
    @Override
    public boolean isCompanyFrame() {
        return true;
    }

    /**
     * Indicates whether this frame is a year data related frame.
     *
     * @return A boolean value.
     */
    @Override
    public boolean isYearDataFrame() {
        return true;
    }

    /**
     *
     * @param delete
     */
    private void deleteSelected(List<SSOutpayment> delete) {
        if (delete.isEmpty()) {
            return;
        }
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "outpaymentframe.delete");
        int iResponce = iDialog.getResponce();

        if (iResponce == JOptionPane.YES_OPTION) {
            for (SSOutpayment iOutpayment : delete) {
                if (SSPostLock.isLocked(
                        "outpayment" + iOutpayment.getNumber()
                        + SSDB.getInstance().getCurrentCompany().getId())) {
                    new SSErrorDialog(getMainFrame(), "outpaymentframe.outpaymentopen",
                            iOutpayment.getNumber());
                } else {
                    for (SSOutpaymentRow iRow : iOutpayment.getRows()) {
                        if (iRow.getValue() != null && iRow.getInvoiceNr() != null) {
                            if (SSSupplierInvoiceMath.iSaldoMap.containsKey(
                                    iRow.getInvoiceNr())) {
                                SSSupplierInvoiceMath.iSaldoMap.put(iRow.getInvoiceNr(),
                                        SSSupplierInvoiceMath.iSaldoMap.get(iRow.getInvoiceNr()).add(
                                        iRow.getValue()));
                            }
                        }
                    }
                    SSDB.getInstance().deleteOutpayment(iOutpayment);
                }
            }
        }
    }

    private SSOutpayment getOutpayment(SSOutpayment iOutpayment) {
        return SSDB.getInstance().getOutpayment(iOutpayment);
    }

    public void updateFrame() {
        iSearchPanel.ApplyFilter();
    }

    public void actionPerformed(ActionEvent e) {
        iTable = null;
        iModel = null;
        cInstance = null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.outpayment.SSOutpaymentFrame");
        sb.append("{iModel=").append(iModel);
        sb.append(", iSearchPanel=").append(iSearchPanel);
        sb.append(", iTable=").append(iTable);
        sb.append('}');
        return sb.toString();
    }
}
