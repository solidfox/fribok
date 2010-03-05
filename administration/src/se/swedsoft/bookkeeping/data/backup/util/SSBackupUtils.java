package se.swedsoft.bookkeeping.data.backup.util;

import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSSystemCompany;
import se.swedsoft.bookkeeping.data.system.SSSystemYear;

import java.util.List;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static se.swedsoft.bookkeeping.data.backup.util.SSBackupZip.ArchiveFile;

/**
 * Date: 2006-mar-03
 * Time: 13:00:54
 */
public class SSBackupUtils {


    /**
     *
     * @return
     */
    public static List<ArchiveFile> getFiles(){

        List<ArchiveFile> iFiles = new LinkedList<SSBackupZip.ArchiveFile>();

        if(!SSDB.getInstance().getLocking()){
            // Add the database files
            iFiles.add( new ArchiveFile( new File("db"+File.separator+"JFSDB.properties")) );
            iFiles.add( new ArchiveFile( new File("db"+File.separator+"JFSDB.script")) );
            iFiles.add( new ArchiveFile( new File("db"+File.separator+"JFSDB.data")) );
            iFiles.add( new ArchiveFile( new File("db"+File.separator+"JFSDB.backup")) );
            iFiles.add( new ArchiveFile( new File("db"+File.separator+"JFSDB.log")) );
        }

        return iFiles;
    }


    /**
     *
     * @param pCompany
     * @return
     */
    public static List<ArchiveFile> getFiles(SSSystemCompany pCompany){
        List<ArchiveFile> iFiles = new LinkedList<ArchiveFile>();

        // Add the company
        //iFiles.add(  new ArchiveFile( SSDB.getInstance().getFile(pCompany.getId())) );

        // Loop through all years
        for(SSSystemYear iYear: pCompany.getYears() ){
            // Add the year
            //iFiles.add(  new ArchiveFile( SSDB.getInstance().getFile(iYear.getId())) );
        }

        return iFiles;
    }

    /**
     *
     * @param pFilename
     * @param iDirectory
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<ArchiveFile> getFiles(String pFilename, String iDirectory) throws FileNotFoundException, IOException {

        List<ArchiveFile> iFiles = new LinkedList<ArchiveFile>();

        // Get the names of the files in the zip file
        for(String iName: SSBackupZip.getFiles(pFilename) ){

            // Don't extract the info file
            if(iName.equals("backup.info")) continue;

            File iFile = new File(iDirectory + iName);

            iFiles.add( new ArchiveFile(iFile, iName) );
        }
        return iFiles;
    }
}
