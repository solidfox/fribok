package se.swedsoft.bookkeeping.importexport.sie.fields;

import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

/**
 * User: Fredrik Stigsson
 * Date: 2006-feb-22
 * Time: 13:02:30
 */
public class SIEEntryProsa implements SIEEntry {


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
        String iComment = iExporter.getComment();

        if(iComment != null){
            iWriter.append(SIELabel.SIE_PROSA);
            iWriter.append(iComment);
            iWriter.newLine();
            return true;
        }

        return false;
    }


}
