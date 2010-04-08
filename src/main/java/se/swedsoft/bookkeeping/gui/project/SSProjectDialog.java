package se.swedsoft.bookkeeping.gui.project;

import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.project.panel.SSProjectPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-okt-11
 * Time: 09:46:10
 */
public class SSProjectDialog {
    static final SSBundle bundle = SSBundle.getBundle();

    private SSProjectDialog() {
    }

    /**
     *
     * @param iMainFrame
     * @param iModel
     */
    public static void newDialog(final SSMainFrame iMainFrame,final AbstractTableModel iModel) {
        final SSDialog       iDialog = new SSDialog(iMainFrame, bundle.getString("projectframe.new.title"));
        final SSProjectPanel iPanel = new SSProjectPanel(false);

        iPanel.setProject( new SSNewProject() );

        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewProject iProject = iPanel.getProject();
                List<SSNewProject> iProjects = SSDB.getInstance().getProjects();
                for (SSNewProject pProject : iProjects) {
                    if (iProject.getNumber().equals(pProject.getNumber())) {
                        new SSErrorDialog(iMainFrame, "projectframe.duplicate",iProject.getNumber());
                        return;
                    }
                }
                SSDB.getInstance().addProject(iProject);


                if (iModel != null) iModel.fireTableDataChanged();

                iDialog.closeDialog();
            }
        };
        iPanel.addOkAction(iSaveAction);

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDialog.closeDialog();
            }
        });
        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "projectframe.saveonclose") != JOptionPane.OK_OPTION) {
                    return;
                }

                iSaveAction.actionPerformed(null);
            }
        });

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);
        iDialog.pack();
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }

    /**
     *
     * @param iMainFrame
     * @param pProject
     * @param iModel
     */
    public static void editDialog(final SSMainFrame iMainFrame, SSNewProject pProject, final AbstractTableModel iModel) {
        Integer iCompanyId = SSDB.getInstance().getCurrentCompany().getId();
        final String lockString = "project"+pProject.getNumber()+iCompanyId;
        if (!SSPostLock.applyLock(lockString)) {
            new SSErrorDialog(iMainFrame, "projectframe.projectopen", pProject.getNumber());
            return;
        }
        final SSDialog       iDialog = new SSDialog(iMainFrame, bundle.getString("projectframe.edit.title"));
        final SSProjectPanel iPanel = new SSProjectPanel(true);

        iPanel.setProject(pProject);
        final ActionListener iSaveAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSNewProject iProject = iPanel.getProject();

                SSDB.getInstance().updateProject(iProject);

                if (iModel != null) iModel.fireTableDataChanged();
                SSPostLock.removeLock(lockString);
                iDialog.closeDialog();
            }
        };
        iPanel.addOkAction(iSaveAction);

        iPanel.addCancelAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPostLock.removeLock(lockString);
                iDialog.closeDialog();
            }
        });
        iDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                if( SSQueryDialog.showDialog(iMainFrame,SSBundle.getBundle(), "projectframe.saveonclose") != JOptionPane.OK_OPTION) {
                    SSPostLock.removeLock(lockString);
                    return;
                }

                iSaveAction.actionPerformed(null);
            }
        });
        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);
        iDialog.pack();
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }
}
