package se.swedsoft.bookkeeping.importexport.sie.util;

import java.util.List;
import java.util.LinkedList;
import java.io.*;

/**
 * Date: 2006-feb-20
 * Time: 12:59:15
 */
public class SIEFile {


    /**
     *
     * @param pFile
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<String> readFile(File pFile) throws FileNotFoundException, IOException {
        List<String> iLines = new LinkedList<String>();

        BufferedReader iReader = null;
        try{
            iReader = new BufferedReader( new InputStreamReader( new FileInputStream(pFile) , "IBM-437") );

            String iLine;
            while (( iLine = iReader.readLine()) != null){
                iLines.add(iLine);
            }
        }
        finally {
            if (iReader!= null) iReader.close();
        }
        return iLines;

    }

    /**
     *
     * @param pFile
     * @param iLines
     * @throws IOException
     */
    public static  void writeFile(File pFile, List<String> iLines) throws IOException {
        BufferedWriter iWriter = null;
        try {
            iWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(pFile) , "IBM-437") );

            for(String iLine: iLines){
                iWriter.write( iLine );
                iWriter.newLine();
            }
        }
        finally {
            if (iWriter != null) {
                iWriter.flush();
                iWriter.close();
            }
        }
    }
}
