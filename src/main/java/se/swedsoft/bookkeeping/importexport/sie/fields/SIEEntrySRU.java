package se.swedsoft.bookkeeping.importexport.sie.fields;


import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSAccountPlan;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.gui.util.SSBundleString;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;


/**
 * Date: 2006-feb-23
 * Time: 11:11:59
 */
public class SIEEntrySRU implements SIEEntry {

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

        // read the account number
        if (!iReader.hasNextInteger()) {
            throw new SSImportException(
                    SSBundleString.getString("sieimport.fielderror", iReader.peekLine()));
        }

        int iAccountNumber = iReader.nextInteger();

        SSAccount iAccount = iAccountPlan.getAccount(iAccountNumber);

        // read the account description
        if (!iReader.hasNext()) {
            throw new SSImportException(
                    SSBundleString.getString("sieexport.expectedbutfound.value"));
        }

        if (iAccount == null) {
            throw new RuntimeException("Missing account for srucode " + iAccountNumber);
        }

        iAccount.setSRUCode(iReader.next());

        // SSDB.getInstance().updateAccountingYear(iYearData);
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

        boolean iHasData = false;

        for (SSAccount iAccount : iAccountPlan.getAccounts()) {

            if (iAccount.getSRUCode() != null) {

                iWriter.append(SIELabel.SIE_SRU);
                iWriter.append(iAccount.getNumber());
                iWriter.append(iAccount.getSRUCode());
                iWriter.newLine();
                iHasData = true;
            }
        }
        return iHasData;
    }
}
