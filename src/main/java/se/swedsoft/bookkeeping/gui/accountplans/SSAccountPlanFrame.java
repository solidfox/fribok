package se.swedsoft.bookkeeping.gui.accountplans;


import se.swedsoft.bookkeeping.data.SSAccountPlan;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.accountplans.util.SSAccountPlanTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSProgressDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSExcelFileChooser;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.importexport.excel.SSAccountPlanExporter;
import se.swedsoft.bookkeeping.importexport.excel.SSAccountPlanImporter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.print.report.SSAccountPlanPrinter;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


/**
 * Date: 2006-feb-13
 * Time: 12:09:40
 */
public class SSAccountPlanFrame extends SSDefaultTableFrame {

    private static SSAccountPlanFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight) {
        if (cInstance == null || cInstance.isClosed()) {
            cInstance = new SSAccountPlanFrame(pMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
        cInstance.deIconize();
    }

    /**
     *
     * @return The SSNewCompanyFrame
     */
    public static SSAccountPlanFrame getInstance() {
        return cInstance;
    }

    private SSTable iTable;

    private SSAccountPlanTableModel iModel;

    /**
     * Constructor.
     *
     * @param pMainFrame The main frame.
     * @param width     The width of the frame.
     * @param height    The height of the frame.
     */
    private SSAccountPlanFrame(SSMainFrame pMainFrame, int width, int height) {
        super(pMainFrame, SSBundle.getBundle().getString("accountplanframe.title"), width,
                height);
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                updateFrame();
            }
        });

    }

    /**
     * This method should return a toolbar if the sub-class wants one.
     * Otherwise, it may return null.
     *
     * @return A JToolBar or null.
     */
    @Override
    public JToolBar getToolBar() {
        JToolBar iToolBar = new JToolBar();

        // New
        // ***************************
        SSButton iButton = new SSButton("ICON_NEWITEM", "accountplanframe.newbutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateFrame();
                SSAccountPlanDialog.newDialog(getMainFrame(), iModel);
                updateFrame();
            }
        });

        iToolBar.add(iButton);

        // Edit
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "accountplanframe.editbutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editSelectedAccountPlan();
            }
        });
        iTable.addSelectionDependentComponent(iButton);

        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Copy
        // ***************************
        iButton = new SSButton("ICON_COPYITEM", "accountplanframe.copybutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSAccountPlan iSelected = iModel.getSelectedRow(iTable);
                String iName = null;

                if (iSelected != null) {
                    iName = iSelected.getName();
                    // Make sure accountplan is still in database
                    iSelected = getAccountPlan(iSelected);
                }
                updateFrame();
                if (iSelected != null) {
                    SSAccountPlanDialog.copyDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "accountplanframe.accountplangone",
                            iName);
                }
                updateFrame();
            }
        });

        iToolBar.add(iButton);
        iTable.addSelectionDependentComponent(iButton);

        // Delete
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "accountplanframe.deletebutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedAccountPlan();
            }
        });
        iToolBar.add(iButton);
        iToolBar.addSeparator();
        iTable.addSelectionDependentComponent(iButton);

        // Import
        // ***************************
        iButton = new SSButton("ICON_IMPORT", "accountplanframe.importbutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSExcelFileChooser iFilechooser = SSExcelFileChooser.getInstance();

                if (iFilechooser.showOpenDialog(getMainFrame())
                        == JFileChooser.APPROVE_OPTION) {

                    SSAccountPlanImporter iImporter = new SSAccountPlanImporter(
                            iFilechooser.getSelectedFile());

                    try {
                        iImporter.doImport();

                    } catch (IOException ex) {
                        SSErrorDialog.showDialog(getMainFrame(), "",
                                ex.getLocalizedMessage());
                    } catch (SSImportException ex) {
                        SSErrorDialog.showDialog(getMainFrame(), "",
                                ex.getLocalizedMessage());
                    }
                    iModel.fireTableDataChanged();
                    updateFrame();
                }
            }
        });
        iToolBar.add(iButton);

        // Export
        // ***************************
        iButton = new SSButton("ICON_EXPORT", "accountplanframe.exportbutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSAccountPlan iSelected = getSelected();
                String iName = iSelected.getName();

                iSelected = getAccountPlan(iSelected);
                if (iSelected == null) {
                    new SSErrorDialog(getMainFrame(), "accountplanframe.accountplangone",
                            iName);
                    return;
                }
                updateFrame();
                SSExcelFileChooser iFilechooser = SSExcelFileChooser.getInstance();

                iFilechooser.setSelectedFile(new File(iSelected.getName() + ".xls"));

                if (iFilechooser.showSaveDialog(getMainFrame())
                        == JFileChooser.APPROVE_OPTION) {

                    SSAccountPlanExporter iExporter = new SSAccountPlanExporter(
                            iFilechooser.getSelectedFile());

                    try {
                        iExporter.doExport(iSelected);
                    } catch (IOException ex) {
                        SSErrorDialog.showDialog(getMainFrame(), "",
                                ex.getLocalizedMessage());
                    } catch (SSExportException ex) {
                        SSErrorDialog.showDialog(getMainFrame(), "",
                                ex.getLocalizedMessage());
                    }
                }
            }
        });
        iTable.addSelectionDependentComponent(iButton);
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Print
        // ***************************
        iButton = new SSButton("ICON_PRINT", "accountplanframe.printbutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final SSAccountPlan pAccountPlan = getSelected();

                // If nothing selected, return
                if (pAccountPlan == null) {
                    new SSErrorDialog(getMainFrame(), "accountplanframe.selectone");
                    return;
                }
                updateFrame();
                String iName = pAccountPlan.getName();
                final SSAccountPlan iAccountPlan = getAccountPlan(pAccountPlan);

                if (iAccountPlan == null) {
                    new SSErrorDialog(getMainFrame(), "accountplanframe.accountplangone",
                            iName);
                    return;
                }
                SSProgressDialog.runProgress(getMainFrame(),
                        new Runnable() {
                    public void run() {
                        SSAccountPlanPrinter iPrinter = new SSAccountPlanPrinter(
                                iAccountPlan);

                        iPrinter.preview(getMainFrame());
                    }
                });
            }
        });
        iToolBar.add(iButton);
        iTable.addSelectionDependentComponent(iButton);

        return iToolBar;

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

        iModel = new SSAccountPlanTableModel();
        iModel.addColumn(SSAccountPlanTableModel.COLUMN_NAME);
        iModel.addColumn(SSAccountPlanTableModel.COLUMN_TYPE);
        iModel.addColumn(SSAccountPlanTableModel.COLUMN_ASSESSMENTYEAR);
        iModel.setObjects(SSDB.getInstance().getAccountPlans());
        iModel.setupTable(iTable);

        iTable.addDblClickListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editSelectedAccountPlan();
            }
        });

        JPanel iPanel = new JPanel();

        iPanel.setLayout(new BorderLayout());
        iPanel.add(new JScrollPane(iTable), BorderLayout.CENTER);
        iPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        return iPanel;
    }

    /**
     *
     * @return The selected company, if any
     */
    private SSAccountPlan getSelected() {
        int selected = iTable.getSelectedRow();

        if (selected >= 0) {
            return iModel.getObject(selected);
        }
        return null;
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
        return false;
    }

    /**
     * Indicates whether this frame is a year data related frame.
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
    private void editSelectedAccountPlan() {
        SSAccountPlan pAccountPlan = getSelected();

        // If nothing selected, return
        if (pAccountPlan == null) {
            new SSErrorDialog(getMainFrame(), "accountplanframe.selectone");
            return;
        }
        updateFrame();
        String iName = pAccountPlan.getName();

        // Make sure accountplan is still in database
        pAccountPlan = getAccountPlan(pAccountPlan);

        if (pAccountPlan != null) {
            SSAccountPlanDialog.editDialog(getMainFrame(), pAccountPlan, iModel);
        } else {
            new SSErrorDialog(getMainFrame(), "accountplanframe.accountplangone", iName);
        }
        updateFrame();
    }

    /**
     *
     */
    private void deleteSelectedAccountPlan() {
        final SSAccountPlan pAccountPlan = getSelected();

        // If nothing selected, return
        if (pAccountPlan == null) {
            new SSErrorDialog(getMainFrame(), "accountplanframe.selectone");
            return;
        }

        String iName = pAccountPlan.getName();
        final SSAccountPlan iAccountPlan = getAccountPlan(pAccountPlan);

        if (iAccountPlan == null) {
            updateFrame();
            new SSErrorDialog(getMainFrame(), "accountplanframe.accountplangone", iName);
            return;
        }
        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), SSBundle.getBundle(),
                "accountplanframe.delete", iAccountPlan.getName());
        int iResponce = iDialog.getResponce();

        updateFrame();
        if (iResponce != JOptionPane.YES_OPTION) {
            return;
        }
        final String lockString = "accountplan" + iAccountPlan.getId();

        if (SSPostLock.isLocked(lockString)) {
            updateFrame();
            new SSErrorDialog(getMainFrame(), "accountplanframe.accountplanopen",
                    iAccountPlan.getName());
            return;
        }
        SSDB.getInstance().deleteAccountPlan(iAccountPlan);
        iModel.fireTableDataChanged();
        updateFrame();
    }

    public SSAccountPlan getAccountPlan(SSAccountPlan iAccountPlan) {
        return SSDB.getInstance().getAccountPlan(iAccountPlan);
    }

    public void updateFrame() {
        iModel.setObjects(SSDB.getInstance().getAccountPlans());
    }

    public void actionPerformed(ActionEvent e) {
        iTable = null;
        iModel = null;
        cInstance = null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.accountplans.SSAccountPlanFrame");
        sb.append("{iModel=").append(iModel);
        sb.append(", iTable=").append(iTable);
        sb.append('}');
        return sb.toString();
    }
}

