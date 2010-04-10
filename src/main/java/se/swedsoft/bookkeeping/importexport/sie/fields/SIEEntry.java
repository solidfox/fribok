package se.swedsoft.bookkeeping.importexport.sie.fields;


import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;


/**
 * Date: 2006-feb-20
 * Time: 12:32:14
 */
public interface SIEEntry {

    /**
     * Imports the entry
     *
     * @param iImporter
     * @param iReader
     * @param iCurrentYear
     * @return If anything was imported
     *
     * @throws SSImportException
     */
    boolean importEntry(SSSIEImporter iImporter, SIEReader iReader, SSNewAccountingYear iCurrentYear) throws SSImportException;

    /**
     * Exports the entry
     *
     * @param iExporter
     * @param iWriter
     * @param iCurrentYear
     * @return If anything was exported
     *
     * @throws SSExportException
     */
    boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iCurrentYear) throws SSExportException;

}
