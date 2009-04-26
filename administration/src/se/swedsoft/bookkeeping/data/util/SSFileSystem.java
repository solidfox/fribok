package se.swedsoft.bookkeeping.data.util;

import java.io.File;

/**
 * Date: 2006-mar-03
 * Time: 09:40:17
 */
public class SSFileSystem {

    // Application directory
    private static final String cApplicationDirectory = new File( "" ).getAbsolutePath() + File.separator ;

    // Directory for data
    private static final String cDataDirectory    = cApplicationDirectory + "data" + File.separator;

    // Directory for reports
    private static final String cReportDirectory  = cDataDirectory + File.separator + "report" + File.separator ;

    // Directory for icons
    private static final String cIconDirectory    = cApplicationDirectory + "graphic" + File.separator + "icons" + File.separator ;

    // Directory for images
    private static final String cImageDirectory    = cApplicationDirectory + "graphic" + File.separator;

    // The location of the backup file
    private static final String cBackupDirectory   = cApplicationDirectory;


    /**
     *
     * @return the application directory
     */
    public static String getApplicationDirectory(){
        return cApplicationDirectory;
    }

    /**
     *
     * @return the data directory
     */
    public static String getDataDirectory(){
        return cDataDirectory;
    }

    /**
     *
     * @return the report directory
     */
    public static String getReportDirectory(){
        return cReportDirectory;
    }


    /**
     *
     * @return the icon directory
     */
    public static String getIconDirectory(){
        return cIconDirectory;
    }

    /**
     *
     * @return the icon directory
     */
    public static String getImageDirectory(){
        return cImageDirectory;
    }

    /**
     *
     * @return the backup directory
     */
    public static String getBackupDirectory() {
        return cBackupDirectory;
    }
}
