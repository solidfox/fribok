package se.swedsoft.bookkeeping.data.backup;


import org.fribok.bookkeeping.app.Path;

import java.io.*;
import java.util.List;


/**
 * Date: 2006-mar-03
 * Time: 09:39:32
 */
public class SSBackupDatabase {
    private static final File iFile = new File(Path.get(Path.USER_DATA), "backup.history");

    private static SSBackupDatabase cInstance;

    /**
     *
     * @return
     */
    public static SSBackupDatabase getInstance() {
        if (cInstance == null) {
            cInstance = new SSBackupDatabase();
        }
        return cInstance;
    }

    private SSBackupData iData;

    /**
     *
     */
    private SSBackupDatabase() {
        if (iFile.exists()) {
            loadDatabase();
        } else {
            newDatabase();
        }
    }

    /**
     * Notify that the database has been changed and need to be stored to file
     */
    public void notifyUpdated() {
        storeDatabase();
    }

    // ///////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new backupdatabase
     */
    private void newDatabase() {
        iData = new SSBackupData();
    }

    /**
     * Loads the backupdatabase
     *
     */
    private void loadDatabase() {
        ObjectInputStream iObjectInputStream = null;

        try {
            iObjectInputStream = new ObjectInputStream(
                    new BufferedInputStream(new FileInputStream(iFile)));
            iData = (SSBackupData) iObjectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (iObjectInputStream != null) {
                try {
                    iObjectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Store the backupdatabase
     *
     */
    private void storeDatabase() {
        try {
            ObjectOutputStream iObjectOutputStream = new ObjectOutputStream(
                    new BufferedOutputStream(new FileOutputStream(iFile)));

            iObjectOutputStream.writeObject(iData);
            iObjectOutputStream.flush();
            iObjectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ///////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns a list of all known backups
     *
     * @return list of backups
     */
    public List<SSBackup> getBackups() {
        if (iData == null) {
            throw new RuntimeException("Backupdatabase not loaded");
        }
        return iData.getBackups();
    }

    public void add(SSBackup backup) {
        iData.getBackups().add(backup);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.data.backup.SSBackupDatabase");
        sb.append("{iData=").append(iData);
        sb.append('}');
        return sb.toString();
    }
}
