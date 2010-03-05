package se.swedsoft.bookkeeping.importexport.sie;

import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.importexport.sie.util.*;
import se.swedsoft.bookkeeping.importexport.sie.fields.SIEEntry;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEFile;
import se.swedsoft.bookkeeping.gui.util.SSBundleString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

/**
 * Date: 2006-feb-20
 * Time: 14:16:55
 */
public class SSSIEExporter {


    private List<String> iLines;

    private SIEType iType;

    private String iComment;


    /**
     *
     * @param pType
     */
    public SSSIEExporter(SIEType pType) {
        iLines   = new LinkedList<String>();
        iType    = pType;
        iComment = null;
    }

    /**
     *
     * @param pType
     * @param pComment
     */
    public SSSIEExporter(SIEType pType, String pComment) {
        iLines   = new LinkedList<String>();
        iType    = pType;
        iComment = pComment;
    }
    /**
     *
     * @param pFile
     */
    public void exportSIE(File pFile) throws SSExportException {
        SSNewAccountingYear iYearData = SSDB.getInstance().getCurrentYear();

        // Test so we have an active year
        if(iYearData == null) {
            throw new SSExportException( SSBundleString.getString("sieexport.noyear") );
        }

        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        // Test so we have an active company
        if(iCompany == null) {
            throw new SSExportException( SSBundleString.getString("sieexport.nocompany") );
        }

        // Get the exporter factory
        SIEFactory iFactory = SIEFactory.getExportInstance(iType);

        //System.out.println( iFactory.toString() );

        for(SIELabel iLabel : iFactory.getLabels()){
            SIEEntry iEntry = iLabel.getEntry();

            if(iEntry == null) continue;

            SIEWriter iWriter = new SIEWriter();

            if( iEntry.exportEntry(this, iWriter, iYearData) ){

                if( iWriter.getLines().size() == 0 ){
                    throw new RuntimeException("Entry reported data but no lines found: " + iEntry);
                }

                iLines.addAll(iWriter.getLines());
            }
        }
        writeFile(pFile);
    }


    /**
     *
     * @param pFile
     * @throws SSImportException
     */
    protected void readFile(File pFile) throws SSExportException{
        try{
            iLines = SIEFile.readFile(pFile);
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw new SSExportException(ex.getMessage());
        }
        catch (IOException ex){
            ex.printStackTrace();
            throw new SSExportException(ex.getMessage());
        }
    }
    /**
     *
     * @param pFile
     * @throws SSImportException
     */
    private void writeFile(File pFile) throws SSExportException{
        try{
            SIEFile.writeFile(pFile, iLines);
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw new SSExportException(ex.getMessage());
        }
        catch (IOException ex){
            ex.printStackTrace();
            throw new SSExportException(ex.getMessage());
        }
    }




    /**
     *
     * @param pType
     */
    public void setType(SIEType pType) {
        iType = pType;

    }

    /**
     *
     * @return
     */
    public SIEType getType() {
        return iType;
    }

    /**
     *
     * @return
     */
    public String getComment() {
        return iComment;
    }

    /**
     *
     * @return
     */
    public List<String> getLines() {
        return iLines;
    }




}
