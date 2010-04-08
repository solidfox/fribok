package se.swedsoft.bookkeeping.importexport.sie.fields;

import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundleString;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import java.util.Date;

import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.INT;
import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.STRING;

/**
 * Date: 2006-feb-22
 * Time: 15:10:21
 */
public class SIEEntryRAR implements SIEEntry {


    /**
     * Imports the entry
     *
     * @param iImporter
     * @param iReader
     * @return If anything was imported
     * @throws SSImportException
     *
     */
    @Override
    public boolean importEntry(SSSIEImporter iImporter, SIEReader iReader, SSNewAccountingYear iCurrentYearData) throws SSImportException {

        // #RAR årsnr från till
        if(!iReader.hasFields(STRING, INT, INT, INT)) {
            throw new SSImportException(SSBundleString.getString("sieimport.fielderror", iReader.peekLine()) );
        }

        int        iYear          = iReader.nextInteger();
        Date       iFrom          = iReader.nextDate();
        Date       iTo            = iReader.nextDate();

        if( iYear == 0 && iCurrentYearData != null){
            iCurrentYearData.setFrom(iFrom);
            iCurrentYearData.setTo  (iTo);
            //SSDB.getInstance().updateAccountingYear(iCurrentYearData);
        }

        return true;
    }

    /**
     * Exports the entry
     *
     * @param iExporter
     * @param iWriter
     * @return If anything was exported
     * @throws SSExportException
     *
     */
    @Override
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iCurrentYearData) throws SSExportException {
        SSNewAccountingYear iPreviousYearData = SSDB.getInstance().getPreviousYear();

        
        if( iPreviousYearData != null){
            iWriter.append(SIELabel.SIE_RAR);
            iWriter.append("-1");
            iWriter.append( iPreviousYearData.getFrom() );
            iWriter.append( iPreviousYearData.getTo() );
            iWriter.newLine();
        }

        if( iCurrentYearData != null){
            iWriter.append(SIELabel.SIE_RAR);
            iWriter.append("0");
            iWriter.append( iCurrentYearData.getFrom() );
            iWriter.append( iCurrentYearData.getTo() );
            iWriter.newLine();
        }

        return iPreviousYearData != null || iCurrentYearData != null;
    }


}
