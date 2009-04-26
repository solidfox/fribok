package se.swedsoft.bookkeeping.importexport.sie.fields;

import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.SSVersion;

import java.util.Date;

/**
 * Date: 2006-feb-20
 * Time: 15:59:24
 */
public class SIEEntryProgram extends SIEEntry {

     /**
     * Imports the field
     *
     * @param iReader
     */
    public boolean importEntry(SSSIEImporter iImporter, SIEReader iReader, SSNewAccountingYear iYearData) throws SSImportException {
        return false;
    }

    /**
     * Exports the field
     *
     * @param iWriter
     */
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iCurrentYearData) throws SSExportException {
        iWriter.append(SIELabel.SIE_PROGRAM);
        iWriter.append(SSVersion.app_title);
        iWriter.append(SSVersion.app_version);
        iWriter.newLine();
        return true;
    }

}
