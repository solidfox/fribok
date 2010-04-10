package se.swedsoft.bookkeeping.gui.backup;


import se.swedsoft.bookkeeping.data.backup.SSBackup;
import se.swedsoft.bookkeeping.data.backup.SSBackupDatabase;
import se.swedsoft.bookkeeping.data.backup.util.SSBackupFactory;

import javax.swing.*;
import java.io.File;


/**
 * Show the backup dialog.
 * 2006
 * @author Andreas Lago
 * @version $Id$
 */
public class SSBackupDialog {
    private final JFrame parent;
    private final JFileChooser fileChooser;
    private final SSBackupDatabase backupDatabase;

    /**
     * Creates a new instance.
     * @param parent the parent of the dialog
     * @param fileChooser the file chooser object to use
     * @param backupDatabase the backup database instance
     */
    public SSBackupDialog(JFrame parent, JFileChooser fileChooser,
            SSBackupDatabase backupDatabase) {
        this.parent = parent;
        this.fileChooser = fileChooser;
        this.backupDatabase = backupDatabase;
    }

    /**
     * Show the dialog and run the backup.
     * @return true on success, false on error or if the user cancelled the action
     */
    public boolean show() {
        String defaultFileName = SSBackupFactory.getDefaultFileName();

        fileChooser.setSelectedFile(new File(defaultFileName));

        int result = fileChooser.showSaveDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            SSBackup backup = SSBackupFactory.createBackup(filename);

            backupDatabase.add(backup);
            backupDatabase.notifyUpdated();
            return true;
        }
        return false;
    }
}
