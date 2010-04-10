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
 * Date: 2006-feb-20
 * Time: 12:40:17
 */
public class SIEEntryFlagga implements SIEEntry {

    /**
     * Imports the field
     *
     * @param iReader
     */
    @Override
    public boolean importEntry(SSSIEImporter iImporter, SIEReader iReader, SSNewAccountingYear iYearData) throws SSImportException {
        if (!iReader.hasNextInteger()) {
            throw new SSImportException(SIELabel.SIE_FLAGGA + " Missing parameter");
        }

        if (iReader.nextInteger() == 1) {
            System.out.println(
                    "(SIEEntryFlagga) The file has already been imported, ignored.");
        }
        return true;
    }

    /**
     * Exports the field
     *
     * @param iWriter
     */
    @Override
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iCurrentYearData) throws SSExportException {
        iWriter.append(SIELabel.SIE_FLAGGA);
        iWriter.append(0);
        iWriter.newLine();

        return true;
    }
}
