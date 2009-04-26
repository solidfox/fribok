package se.swedsoft.bookkeeping.importexport.sie.fields;

import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;

import java.text.DateFormat;
import java.util.Date;

/**
 * Date: 2006-feb-22
 * Time: 12:26:37
 */
public class SIEEntryGenererat extends SIEEntry {

    /**
     * Imports the field
     *
     * @param iReader
     */
    public boolean importEntry(SSSIEImporter iImporter, SIEReader iReader, SSNewAccountingYear iYearData) throws SSImportException {
        return true;
    }

    /**
     * Exports the field
     *
     * @param iWriter
     */
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iYear) throws SSExportException {
        iWriter.append(SIELabel.SIE_GEN);
        iWriter.append( new Date() );
        iWriter.newLine();

         return true;
    }


}
