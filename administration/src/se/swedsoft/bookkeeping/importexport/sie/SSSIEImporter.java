package se.swedsoft.bookkeeping.importexport.sie;

import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.SSNewResultUnit;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.gui.util.frame.SSFrameManager;
import se.swedsoft.bookkeeping.importexport.sie.fields.SIEEntry;
import se.swedsoft.bookkeeping.importexport.sie.types.SIEDimension;
import se.swedsoft.bookkeeping.importexport.sie.util.*;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


/**
 * Date: 2006-feb-20
 * Time: 12:52:32
 */
public class SSSIEImporter {

    private List<String> iLines;

    private SIEType iType;

    private List<SIEDimension> iDimensions;

    // Get the importer factory
    private SIEFactory iFactory;

    private File iFile;


    /**
     *
     * @param iFile
     */
    public SSSIEImporter(File iFile) {
        this.iFile = iFile;

        iLines      = new LinkedList<String>();
        iDimensions = SIEDimension.getDefaultDimensions();
        iFactory    = SIEFactory.getImportInstance();
    }

    /**
     *
     * @throws SSImportException
     */
    public void doImport() throws SSImportException {
        SSDB.getInstance().dropTriggers();
        SSNewAccountingYear iAccountingYear = SSDB.getInstance().getCurrentYear();

        // Read the contents of the file
        readFile(iFile);

        if(iLines.isEmpty()) return;

        // Clear all active data

        iAccountingYear.getInBalance  ().clear();
        iAccountingYear.getBudget     ().clear();
        iAccountingYear.getAccountPlan().clear();
        //SSDB.getInstance().updateAccountingYear(iAccountingYear);

        for (SSVoucher iVoucher : SSDB.getInstance().getVouchers()) {
            SSDB.getInstance().deleteVoucher(iVoucher);
        }
        SSDB.getInstance().getVouchers().clear();

        List<SSNewProject> projectsToDelete = new LinkedList<SSNewProject>(SSDB.getInstance().getProjects());
        for (SSNewProject iProject : projectsToDelete) {
            SSDB.getInstance().deleteProject(iProject);
        }
        projectsToDelete = null;
        List<SSNewResultUnit> resultUnitsToDelete = new LinkedList<SSNewResultUnit>(SSDB.getInstance().getResultUnits());
        for (SSNewResultUnit iResultUnit : resultUnitsToDelete) {
            SSDB.getInstance().deleteResultUnit(iResultUnit);
        }
        resultUnitsToDelete = null;
        //System.out.println( iFactory.toString() );

        List<List<String>> iParsedLines = getParsedLines(iLines);
        //iAccountingYear = SSDB.getInstance().getCurrentYear();
        for (List<String> iEntryLines : iParsedLines) {
            SIEReader iReader = new SIEReader(iEntryLines);

            String iLabel = iReader.next();

            SIEEntry iEntry = iFactory.get(iLabel);

            if(iEntry != null) {
                iEntry.importEntry(this, iReader,iAccountingYear);
            } else {
                System.out.println("(SSSIEImporter)Missing reader for: " + iLabel);
            }
        }
        SSDB.getInstance().updateAccountingYear(iAccountingYear);
        SSDB.getInstance().notifyListeners("YEAR", SSDB.getInstance().getCurrentYear(), null);
        setReaded(iFile);

        SSFrameManager.getInstance().close();
        SSDB.getInstance().initYear(true);

        SSDB.getInstance().createTriggers();
    }

    /**
     *
     * @throws SSImportException
     */
    public void doImportVouchers() throws SSImportException {
        // Read the contents of the file
        final String lockString = "voucher"+SSDB.getInstance().getCurrentCompany().getId()+SSDB.getInstance().getCurrentYear().getId();
        readFile(iFile);

        if(iLines.isEmpty()){
            SSPostLock.removeLock(lockString);
            return;
        }

        List<List<String>> iParsedLines = getParsedLines(iLines);
        SSNewAccountingYear iYear = SSDB.getInstance().getCurrentYear();

        for (List<String> iEntryLines : iParsedLines) {
            SIEReader iReader = new SIEReader(iEntryLines);

            String iLabel = iReader.next();

            // Only import verifications
            if(iLabel.equals("#VER") ){
                SIEEntry iEntry = iFactory.get("#VER");

                iEntry.importEntry(this, iReader,iYear);
            }
        }
        setReaded(iFile);
    }

    /**
     *
     * @param pFile
     */
    private void setReaded(File pFile){
        // Set the flag to 1
        iLines.set(0, SIELabel.SIE_FLAGGA + " 1");

        writeFile(pFile);
    }

    /**
     *
     * @param iLines
     * @return
     */
    private static List<List<String>> getParsedLines(List<String> iLines){
        List<List<String>> iParsedLines = new LinkedList<List<String>>();

        for(int iIndex = 0; iIndex < iLines.size(); iIndex++){
            String iLine = iLines.get(iIndex);
            String iNext = (iIndex+1 < iLines.size()) ? iLines.get(iIndex+1) : "";

            // Skip any blank lines
            if(iLine == null || iLine.trim().length() == 0) continue;

            List<String> iEntryLines = new LinkedList<String>();

            iEntryLines.add(iLine);

            if(iNext.equals("{")){
                int iStart = iIndex + 2;

                for(iIndex = iStart; iIndex < iLines.size(); iIndex++){
                    iLine = iLines.get(iIndex);

                    if(iLine.equals("}")){
                        break;
                    }

                    iEntryLines.add(iLine);
                }
            }
            iParsedLines.add(iEntryLines);
        }
        return iParsedLines;
    }



    /**
     *
     * @param pFile
     * @throws SSImportException
     */
    protected void readFile(File pFile) throws SSImportException{
        try{
            iLines = SIEFile.readFile(pFile);
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw new SSImportException(ex.getMessage());
        }
        catch (IOException ex){
            ex.printStackTrace();
            throw new SSImportException(ex.getMessage());
        }
    }


    /**
     *
     * @param pFile
     * @throws SSImportException
     * @throws SSExportException
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
    public List<SIEDimension> getDimensions() {
        return iDimensions;
    }
    /**
     *
     * @param pNumber
     * @return
     */
    public SIEDimension getDimension(int pNumber) {
        return SIEDimension.getDimension(iDimensions, pNumber);
    }

    /**
     *
     * @param pDimensions
     */
    public void setDimensions(List<SIEDimension> pDimensions) {
        iDimensions = pDimensions;
    }

    /**
     *
     * @return
     */
    public SIEFactory getFactory() {
        return iFactory;
    }

    /**
     *
     * @param iFactory
     */
    public void setFactory(SIEFactory iFactory) {
        this.iFactory = iFactory;
    }

    /**
     *
     * @return
     */
    public List<String> getLines() {
        return iLines;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter");
        sb.append("{iDimensions=").append(iDimensions);
        sb.append(", iFactory=").append(iFactory);
        sb.append(", iFile=").append(iFile);
        sb.append(", iLines=").append(iLines);
        sb.append(", iType=").append(iType);
        sb.append('}');
        return sb.toString();
    }
}
