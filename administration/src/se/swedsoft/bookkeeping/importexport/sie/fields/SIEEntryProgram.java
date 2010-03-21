package se.swedsoft.bookkeeping.importexport.sie.fields;

import se.swedsoft.bookkeeping.app.Version;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

/**
 * Date: 2006-feb-20
 * Time: 15:59:24
 */
public class SIEEntryProgram implements SIEEntry {

     /**
     * Imports the field
     *
     * @param iReader
     */
    @Override
    public boolean importEntry(SSSIEImporter iImporter, SIEReader iReader, SSNewAccountingYear iYearData) throws SSImportException {
        return false;
    }

    /**
     * Exports the field
     *
     * @param iWriter
     */
    @Override
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iCurrentYearData) throws SSExportException {
        iWriter.append(SIELabel.SIE_PROGRAM);
        iWriter.append(Version.APP_TITLE);
        iWriter.append(Version.APP_VERSION);
        iWriter.newLine();
        return true;
    }

}
