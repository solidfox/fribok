package se.swedsoft.bookkeeping.gui.backup;

import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSBackupFileChooser;
import se.swedsoft.bookkeeping.data.backup.util.SSBackupFactory;
import se.swedsoft.bookkeeping.data.backup.SSBackup;
import se.swedsoft.bookkeeping.data.backup.SSBackupDatabase;
import se.swedsoft.bookkeeping.data.system.SSSystemCompany;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import java.io.File;

/**
 * User: Andreas Lago
 * Date: 2006-okt-05
 * Time: 10:16:17
 */
public class SSBackupDialog {
    /**
     *
     * @param iMainFrame
     */
    public static boolean backupFullDialog(final SSMainFrame iMainFrame) {
        SSBackupFrame.hideFrame();

        SSBackupFileChooser iFileChooser = SSBackupFileChooser.getInstance();

        String iDefaultFileName = SSBackupFactory.getDefaultFileName();

        iFileChooser.setSelectedFile( new File(iDefaultFileName) );

        if( iFileChooser.showSaveDialog(iMainFrame) == JFileChooser.APPROVE_OPTION) {
            String iFilename = iFileChooser.getSelectedFile().getAbsolutePath();

            SSBackup iBackup = SSBackupFactory.createBackup(iFilename);

            SSBackupDatabase.getInstance().getBackups().add(iBackup);
            SSBackupDatabase.getInstance().notifyUpdated();
            return true;
        }
        return false;

    }

    /**
     *
     * @param iMainFrame
     */
    public static void backupCurrentDialog(final SSMainFrame iMainFrame) {
        SSBackupFrame.hideFrame();

        SSBackupFileChooser iFileChooser = SSBackupFileChooser.getInstance();

        String iDefaultFileName = SSBackupFactory.getDefaultFileName(SSDB.getInstance().getCurrentCompany());

        iFileChooser.setSelectedFile( new File(iDefaultFileName) );

        if( iFileChooser.showSaveDialog(iMainFrame) == JFileChooser.APPROVE_OPTION) {
            String iFilename = iFileChooser.getSelectedFile().getAbsolutePath();

            //SSSystemCompany iCompany = SSDB.getInstance().getCurrentSystemCompany();

            //SSBackup iBackup = SSBackupFactory.createBackup(iFilename, iCompany);

            //SSBackupDatabase.getInstance().getBackups().add(iBackup);
            SSBackupDatabase.getInstance().notifyUpdated();

        }

    }
}
