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
 * Date: 2006-feb-20
 * Time: 15:30:33
 */
public class SIEEntryFormat implements SIEEntry {

    /**
     * Imports the field
     *
     * @param iReader
     */
    @Override
    public boolean importEntry(SSSIEImporter iImporter, SIEReader iReader, SSNewAccountingYear iYearData) throws SSImportException {
        String iFormat = iReader.next();

        if (!iFormat.equals("PC8")) {
            System.out.println("Unexpected character encoding");
        }
        return true;
    }

    /**
     * Exports the field
     *
     * @param iWriter
     */
    @Override
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iYear) throws SSExportException {
        iWriter.append(SIELabel.SIE_FORMAT);
        iWriter.append("PC8");
        iWriter.newLine();

        return true;
    }

}
