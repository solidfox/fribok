package se.swedsoft.bookkeeping.gui.sie.dialog;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSSIEFileChooser;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.sie.panel.SSExportSIEPanel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;

import java.util.ResourceBundle;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import se.swedsoft.bookkeeping.importexport.sie.util.SIEType;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.SSBookkeeping;
import se.swedsoft.bookkeeping.data.system.SSDB;

import javax.swing.*;

/**
 * Date: 2006-feb-13
 * Time: 16:40:28
 */
public class SSExportSIEDialog {

    private static ResourceBundle bundle = SSBundle.getBundle();

    /**
     *
     * @param iMainFrame
     */
    public static void showDialog(final SSMainFrame iMainFrame) {
        final SSDialog         iDialog = new SSDialog(iMainFrame, bundle.getString("sieframe.export.title"));
        final SSExportSIEPanel iPanel  = new SSExportSIEPanel();

        SSSIEFileChooser iFileChooser = SSSIEFileChooser.getInstance();

        iFileChooser.setDefaultFileName();

        if(iFileChooser.showSaveDialog(iMainFrame) != JFileChooser.APPROVE_OPTION) return;

        final File iFile = iFileChooser.getSelectedFile();

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        iPanel.addOkAction( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String iComment = iPanel.getComment();
                SIEType iType = iPanel.getType();

                try {
                    SSSIEExporter iExporter = new SSSIEExporter(iType, iComment);

                    iExporter.exportSIE(iFile);
                } catch (SSExportException ex) {
                    new SSErrorDialog(iMainFrame, "exportexceptiondialog", ex.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                iDialog.closeDialog();
            }
        });
        iPanel.addCancelAction(  new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDialog.closeDialog();
            }
        });

        iDialog.pack();
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();

    }
}
