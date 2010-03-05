package se.swedsoft.bookkeeping.data.backup.util;

import se.swedsoft.bookkeeping.data.backup.SSBackup;
import se.swedsoft.bookkeeping.data.system.SSSystemCompany;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.util.Date;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;

import se.swedsoft.bookkeeping.util.SSException;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.frame.SSInternalFrame;

import static se.swedsoft.bookkeeping.data.backup.util.SSBackupZip.ArchiveFile;
import se.swedsoft.bookkeeping.data.SSNewCompany;

/**
 * Date: 2006-mar-03
 * Time: 11:14:09
 */
public class SSBackupFactory {

    /**
     *
     * @return
     */
    public static String getDefaultFileName(){
        Date iDate = new Date();

        DateFormat iDateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        DateFormat iTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

        String iFileName = "backup." + iDateFormat.format(iDate) + "." + iTimeFormat.format(iDate)+".zip";

        iFileName = iFileName.replace(":", "");
        iFileName = iFileName.replace("-", "");

        return iFileName;
    }

    /**
     * 
     * @param iCompany
     * @return
     */
    public static String getDefaultFileName(SSNewCompany iCompany) {
        Date iDate = new Date();

        DateFormat iDateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        DateFormat iTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);

        String iFileName = "backup." + iCompany.getName() + "." + iDateFormat.format(iDate) + "." + iTimeFormat.format(iDate)+".zip";

        iFileName = iFileName.replace(":", "");
        iFileName = iFileName.replace("-", "");

        return iFileName;
    }


    /**
     * Creates a full backup
     *
     * @param pFilename
     *
     * @return the backup
     */
    public static SSBackup createBackup(String pFilename){
        SSBackup iBackup = new SSBackup(SSBackupType.FULL);

        iBackup.setDate    ( new Date() );
        iBackup.setFilename(pFilename   );


        // Get the database files
        List<ArchiveFile> iFiles = SSBackupUtils.getFiles();

        File iBackupFile;
        try {
            // Create a new temp file
            iBackupFile = File.createTempFile("backup", null);

            SSBackup.storeBackup(iBackupFile, iBackup);

            iFiles.add( new ArchiveFile(iBackupFile, "backup.info" ) );
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        System.out.println("Creating backup, adding files {");
        printFiles(iFiles);
        System.out.println("}");

        try {
            SSBackupZip.compressFiles(pFilename, iFiles);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Delete the temporary backupfile
        //iBackupFile.delete();

        return iBackup;
    }

    /**
     * Creates a backup for the supplied company
     *
     * @param pFilename
     * @param pCompany
     *
     * @return the backup
     */
    public static SSBackup createBackup(String pFilename, SSSystemCompany pCompany){


        return null;
    }

    /**
     *
     * @param pFilename
     */
    public static void restoreBackup(String pFilename) throws SSException{
        File iBackupFile;
        try {
            // Create a new temp file
            iBackupFile = File.createTempFile("backup", null);


            // Read the backup file, if exists
            if( ! SSBackupZip.extractFile(pFilename, new ArchiveFile(iBackupFile, "backup.info"))){
                throw new SSException(SSBundle.getBundle(), "backupframe.importbackup.invalid");
            }

            // Load the backup
            SSBackup iBackup = SSBackup.loadBackup(iBackupFile);

            if(iBackup.getType() == SSBackupType.FULL){
                restoreBackup(pFilename, iBackup);
            }


            iBackupFile.delete();


        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }


    /**
     * Restores a full backup
     *
     * @param pFilename
     * @param iBackup
     */
    private static void restoreBackup(String pFilename, SSBackup iBackup) throws IOException {
        SSInternalFrame.closeAllFrames();

        // Get the database directory
        String iDirectory = "db"+File.separator;

        // Delete all old files
        SSDB.getInstance().delete();

        List<ArchiveFile> iFiles = SSBackupUtils.getFiles(pFilename, iDirectory);

        // Extract all files
        SSBackupZip.extractFiles(pFilename, iFiles);


        try {
            SSDB.getInstance().loadLocalDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Restores a company backup
     *
     * @param pFilename
     * @param iBackup
     * @param iRestoredCompany
     */
    private static void restoreBackup(String pFilename, SSBackup iBackup, SSSystemCompany iRestoredCompany) {
        SSInternalFrame.closeAllFrames();

        // Get the database directory
        //String iDirectory = SSDB.getInstance().getDirectory();

        //SSSystemCompany iCompany = SSDB.getInstance().getCompany(iRestoredCompany);
        // Test if the company exists in the database
        /*if(iCompany != null ){
            iRestoredCompany.setCurrent( iCompany.isCurrent() );

            // Delete the company
            SSDB.getInstance().deleteCompany(iCompany);
        } else {
            iRestoredCompany.setCurrent( false );
        } */

        //List<ArchiveFile> iFiles = SSBackupUtils.getFiles(pFilename, iDirectory);

        // Extract all files
        //SSBackupZip.extractFiles(pFilename, iFiles);

        // Add the company to the database
        //SSDB.getInstance().getSystemCompanies().add(iRestoredCompany);

        if(iRestoredCompany.isCurrent()) {
            //SSDB.getInstance().setCurrentCompany(iRestoredCompany);
        }
    }


    private static void printFiles(List<ArchiveFile> iFiles){

        for(ArchiveFile iFile: iFiles){
            System.out.println("  "  + iFile );
        }


    }


}
