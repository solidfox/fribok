package se.swedsoft.bookkeeping.importexport.sie.fields;

import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.STRING;
import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.INT;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSAccountPlan;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.gui.util.SSBundleString;

/**
 * User: Fredrik Stigsson
 * Date: 2006-feb-23
 * Time: 10:15:47
 */
public class SIEEntryKonto extends SIEEntry {


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
    public boolean importEntry(SSSIEImporter iImporter, SIEReader iReader, SSNewAccountingYear iYearData) throws SSImportException {

        SSAccountPlan iAccountPlan = iYearData.getAccountPlan();

        if(!iReader.hasFields(STRING, INT, STRING )) {
            throw new SSImportException(SSBundleString.getString("sieimport.fielderror", iReader.peekLine()) );
        }
        SSAccount iAccount = new SSAccount();

        iAccount.setNumber     ( iReader.nextInteger() );
        iAccount.setDescription( iReader.next() );

        if(!iAccountPlan.getAccounts().contains(iAccount)){
            // add the acccount
            iAccountPlan.addAccount(iAccount);
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
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iYearData) throws SSExportException {

        SSAccountPlan iAccountPlan = iYearData.getAccountPlan();

        for(SSAccount iAccount : iAccountPlan.getAccounts()){
            iWriter.append(SIELabel.SIE_KONTO);
            iWriter.append( iAccount.getNumber() );
            iWriter.append( iAccount.getDescription() );
            iWriter.newLine();
        }

        return !iAccountPlan.getAccounts().isEmpty();
    }
}
