/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.project;


import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.project.util.SSProjectTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSMenuButton;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSProgressDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.print.dialog.SSPeriodSelectionDialog;
import se.swedsoft.bookkeeping.print.report.SSProjectRevenuePrinter;
import se.swedsoft.bookkeeping.print.report.SSProjectsPrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 */
public class SSProjectFrame extends SSDefaultTableFrame {

    private static SSProjectFrame cInstance;

    /**
     *
     * @param pMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame pMainFrame, int pWidth, int pHeight) {
        if (cInstance == null || cInstance.isClosed()) {
            cInstance = new SSProjectFrame(pMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
        cInstance.deIconize();

    }

    /**
     *
     * @return The SSNewProjectFrame
     */
    public static SSProjectFrame getInstance() {
        return cInstance;
    }

    private SSTable iTable;

    private SSProjectTableModel iModel;

    /**
     * Default constructor.
     *
     * @param mainFrame
     * @param width
     * @param height
     */
    private SSProjectFrame(SSMainFrame mainFrame, int width, int height) {
        super(mainFrame, SSBundle.getBundle().getString("projectframe.title"), width,
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
        JToolBar iToolBar = new JToolBar();

        // Nytt projekt
        // ***************************
        SSButton iButton = new SSButton("ICON_NEWITEM", "projectframe.newbutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newProject();
            }
        });

        iToolBar.add(iButton);

        // Ändra projekt
        // ***************************
        iButton = new SSButton("ICON_EDITITEM", "projectframe.editbutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewProject iSelected = getSelected();
                String iNumber = null;

                if (iSelected != null) {
                    iNumber = iSelected.getNumber();
                    iSelected = getProject(iSelected);
                }
                if (iSelected != null) {
                    SSProjectDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "projectframe.projectgone", iNumber);
                }
            }
        });
        iToolBar.add(iButton);
        iToolBar.addSeparator();
        iTable.addSelectionDependentComponent(iButton);

        // Ta bort projekt
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "projectframe.deletebutton",
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSNewProject> toDelete = iModel.getObjects(selected);

                deleteSelectedProjects(toDelete);
            }
        });
        iToolBar.add(iButton);
        iToolBar.addSeparator();
        iTable.addSelectionDependentComponent(iButton);

        // Skriv ut projekt
        // ***************************
        SSMenuButton iButton2 = new SSMenuButton("ICON_PRINT", "projectframe.printbutton");

        iButton2.add("projectframe.print.projectrevenue", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ProjectRevenueReport();
            }
        });
        iButton2.add("projectframe.print.projectlist", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printProjects();
            }
        });
        iToolBar.add(iButton2);

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

        iModel = new SSProjectTableModel();
        iModel.addColumn(SSProjectTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSProjectTableModel.COLUMN_NAME);
        iModel.addColumn(SSProjectTableModel.COLUMN_CONCLUDED);

        iModel.setupTable(iTable);

        iTable.addDblClickListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewProject iSelected = getSelected();

                if (iSelected == null) {
                    return;
                }

                String iNumber = iSelected.getNumber();

                iSelected = getProject(iSelected);
                if (iSelected != null) {
                    SSProjectDialog.editDialog(getMainFrame(), iSelected, iModel);
                } else {
                    new SSErrorDialog(getMainFrame(), "projectframe.projectgone", iNumber);
                }
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
     * @return
     */
    private SSNewProject getSelected() {
        int selected = iTable.getSelectedRow();

        if (selected >= 0) {
            return  iModel.getObject(selected);
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
        return true;
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
    private void newProject() {
        SSProjectDialog.newDialog(getMainFrame(), iModel);
    }

    /**
     *
     * @param delete
     */
    private void deleteSelectedProjects(List<SSNewProject> delete) {
        if (delete.isEmpty()) {
            return;
        }

        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "projectframe.delete");
        int iResponce = iDialog.getResponce();

        if (iResponce == JOptionPane.YES_OPTION) {
            for (SSNewProject iProject : delete) {
                if (SSPostLock.isLocked(
                        "project" + iProject.getNumber()
                        + SSDB.getInstance().getCurrentCompany().getId())) {
                    new SSErrorDialog(getMainFrame(), "projectframe.projectopen",
                            iProject.getNumber());
                } else {
                    SSDB.getInstance().deleteProject(iProject);
                }
            }
        }
    }

    private SSNewProject getProject(SSNewProject iProject) {
        return SSDB.getInstance().getProject(iProject);
    }

    private List<SSNewProject> getProjects(List<SSNewProject> iProjects) {
        return SSDB.getInstance().getProjects(iProjects);
    }

    /**
     *
     */

    private void ProjectRevenueReport() {
        List<SSNewProject> iProjects;

        if (iTable.getSelectedRowCount() > 0) {

            int iOption = SSQueryDialog.showDialog(getMainFrame(),
                    JOptionPane.YES_NO_CANCEL_OPTION, "projectframe.printallorselected");

            switch (iOption) {
            case JOptionPane.YES_OPTION:
                iProjects = iModel.getObjects(iTable.getSelectedRows());
                iProjects = getProjects(iProjects);
                break;

            case JOptionPane.NO_OPTION:
                iProjects = SSDB.getInstance().getProjects();
                break;

            default:
                return;
            }
        } else {
            iProjects = SSDB.getInstance().getProjects();
        }

        SSPeriodSelectionDialog iDialog = new SSPeriodSelectionDialog(getMainFrame(),
                SSBundle.getBundle().getString("projectrevenue.perioddialog.title"));

        if (SSDB.getInstance().getCurrentYear() != null) {
            iDialog.setFrom(SSDB.getInstance().getCurrentYear().getFrom());
            iDialog.setTo(SSDB.getInstance().getCurrentYear().getTo());
        } else {
            Calendar iCal = Calendar.getInstance();

            iDialog.setFrom(iCal.getTime());
            iCal.add(Calendar.MONTH, 1);
            iDialog.setTo(iCal.getTime());
        }
        iDialog.setLocationRelativeTo(getMainFrame());

        if (iDialog.showDialog() != JOptionPane.OK_OPTION) {
            return;
        }

        final Date iFrom = iDialog.getFrom();
        final Date iTo = iDialog.getTo();

        final SSProjectRevenuePrinter iPrinter = new SSProjectRevenuePrinter(iProjects,
                iFrom, iTo);

        SSProgressDialog.runProgress(getMainFrame(), new Runnable() {
            public void run() {
                iPrinter.preview(getMainFrame());
            }
        });
    }

    private void printProjects() {
        final SSProjectsPrinter iPrinter;
        List<SSNewProject> iProjects;

        if (iTable.getSelectedRowCount() > 0) {

            SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(),
                    JOptionPane.YES_NO_CANCEL_OPTION, "projectframe.print");
            int iResponce = iDialog.getResponce();

            switch (iResponce) {
            case JOptionPane.YES_OPTION:
                iProjects = getProjects(iModel.getObjects(iTable.getSelectedRows()));
                iPrinter = new SSProjectsPrinter(iProjects);
                break;

            case JOptionPane.NO_OPTION:
                iProjects = getProjects(iModel.getObjects());
                iPrinter = new SSProjectsPrinter(iProjects);
                break;

            default:
                return;
            }
        } else {
            iProjects = getProjects(iModel.getObjects(iTable.getSelectedRows()));
            iPrinter = new SSProjectsPrinter(iProjects);
        }

        SSProgressDialog.runProgress(getMainFrame(), new Runnable() {
            public void run() {
                iPrinter.preview(getMainFrame());
            }
        });
    }

    public void updateFrame() {
        iModel.setObjects(SSDB.getInstance().getProjects());
    }

    public void actionPerformed(ActionEvent e) {
        iTable = null;
        iModel = null;
        cInstance = null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.project.SSProjectFrame");
        sb.append("{iModel=").append(iModel);
        sb.append(", iTable=").append(iTable);
        sb.append('}');
        return sb.toString();
    }
}
