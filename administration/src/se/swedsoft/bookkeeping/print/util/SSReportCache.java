package se.swedsoft.bookkeeping.print.util;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import se.swedsoft.bookkeeping.app.SSPath;
import se.swedsoft.bookkeeping.util.SSException;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2006-feb-14
 * Time: 17:01:15
 * @version $Id$
 */
public class SSReportCache {
    private static final File REPORT_DIR   = new File(SSPath.get(SSPath.APP_DATA), "report");
    private static final File COMPILED_DIR = new File(REPORT_DIR, "compiled");

    // The report cache with compiled report definitions.
    private Map<String, JasperReport> iReportCache;

    // our instance
    private static SSReportCache cInstance;

    /**
     * Get the instance of this class
     * @return The instance
     */
    public static SSReportCache getInstance(){
        if (cInstance == null) {
            cInstance = new SSReportCache();
        }
        return cInstance;
    }

    /**
     *
     */
    private SSReportCache(){
        iReportCache =  new HashMap<String, JasperReport>();
    }

    /**
     * This function will load a report, either from the runtime cache, a
     * precompiled version or from the report source.
     *
     * @param pReportName The name of the report to load, ie vatcontrol.jrxml.
     *
     * @return The JasperReport object
     * @throws se.swedsoft.bookkeeping.util.SSException
     */
    public JasperReport getReport(String pReportName) throws SSException{
        // Try to get the report from cache
        JasperReport pReport = iReportCache.get(pReportName);
        if (pReport == null) {
            try {
                pReport = loadReport(pReportName);
            } catch(FileNotFoundException ex) {
                throw new SSException( ex.getLocalizedMessage() );
            }
            iReportCache.put(pReportName, pReport);
        }
        return pReport;
    }

    /**
     * 
     * @param pReportName
     * @return
     * @throws java.io.FileNotFoundException
     */
    private JasperReport loadReport(String pReportName) throws FileNotFoundException {
        File iReportFile   = new File(REPORT_DIR,   pReportName);
        File iCompiledFile = new File(COMPILED_DIR, pReportName.replace(".jrxml", ".jasperreport"));

        try {
            // If the report exists on disk, load it...
            if (iCompiledFile.exists()) {
                Date iReportDate   =  new Date(iReportFile  .lastModified());
                Date iCompiledDate =  new Date(iCompiledFile.lastModified());

                // if the report file hasnt been changes since the last compile,
                // load the compiled file, else fall through to the compile code
                if (iReportDate.compareTo( iCompiledDate ) <= 0) {
                    System.out.printf("Loading precompiled report %s from disk...\n", iCompiledFile);

                    return loadCompiledReport(iCompiledFile);
                }

                System.out.println("Precompiled report exists, but report is changed..." );
            }

            // .. we need to recompile the report
            System.out.printf("Compiling and saving report %s to disk...\n", iReportFile);

            FileInputStream iFileInputStream = new FileInputStream(iReportFile);

            JasperReport iReport = JasperCompileManager.compileReport(new BufferedInputStream(iFileInputStream));

            // Make the output directory
            iCompiledFile.getParentFile().mkdirs();

            return saveCompiledReport(iCompiledFile, iReport);
        } catch (JRException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param pCompiledFile
     *
     * @return The report
     */
    private JasperReport loadCompiledReport(File pCompiledFile) {
        try{
            FileInputStream iFileInputStream = new FileInputStream(pCompiledFile);

            ObjectInputStream iObjectInputStream = new ObjectInputStream(new BufferedInputStream(iFileInputStream));

            return (JasperReport)iObjectInputStream.readObject();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param pCompiledFile
     * @param pReport
     *
     * @return The report
     */
    private JasperReport saveCompiledReport(File pCompiledFile, JasperReport pReport) {
        try{
            FileOutputStream iFileOutputStream = new FileOutputStream(pCompiledFile);

            ObjectOutputStream iObjectOutputStream = new ObjectOutputStream(new BufferedOutputStream(iFileOutputStream) );

            iObjectOutputStream.writeObject(pReport);
            iObjectOutputStream.flush();

            return pReport;
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
}
