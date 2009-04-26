package se.swedsoft.bookkeeping.importexport.sie.fields;

import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.SSAddress;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.SSBookkeeping;

/**
 * User: Fredrik Stigsson
 * Date: 2006-feb-23
 * Time: 10:04:25
 */
public class SIEEntryForetagsAdress extends SIEEntry {
    /**
     * Imports the entry
     *
     * @param iImporter
     * @param iReader
     * @return If anything was imported
     * @throws SSImportException
     *
     */
    public boolean importEntry(SSSIEImporter iImporter, SIEReader iReader, SSNewAccountingYear iYearData) throws SSImportException {
        return false;
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
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iYear) throws SSExportException {
         SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        SSAddress iAddress = iCompany.getAddress();

        iWriter.append(SIELabel.SIE_ADRESS);
        iWriter.append( iAddress.getName() );
        iWriter.append( iAddress.getAddress1()  );
        iWriter.append( iAddress.getPostalAddress()  );
        iWriter.append( iCompany.getPhone() );
        iWriter.newLine();

        return true;
    }
}
