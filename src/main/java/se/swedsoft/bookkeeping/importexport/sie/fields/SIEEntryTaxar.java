package se.swedsoft.bookkeeping.importexport.sie.fields;


import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.gui.util.SSBundleString;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.INT;
import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.STRING;


/**
 * Date: 2006-feb-22
 * Time: 15:28:21
 */
public class SIEEntryTaxar implements SIEEntry {

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
    public boolean importEntry(SSSIEImporter iImporter, SIEReader iReader, SSNewAccountingYear iCurrentYearData) throws SSImportException {

        // #TAXAR taxeringsår
        if (!iReader.hasFields(STRING, INT)) {
            throw new SSImportException(
                    SSBundleString.getString("sieimport.fielderror", iReader.peekLine()));
        }

        String  iYear = iReader.nextString();

        if (iCurrentYearData != null && iCurrentYearData.getAccountPlan() != null) {

            iCurrentYearData.getAccountPlan().setAssessementYear(iYear);

            // SSDB.getInstance().updateAccountingYear(iCurrentYearData);
            return true;
        }
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
    @Override
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iCurrentYearData) throws SSExportException {

        if (iCurrentYearData != null && iCurrentYearData.getAccountPlan() != null) {
            String iAssessementYear = iCurrentYearData.getAccountPlan().getAssessementYear();

            if (iAssessementYear == null) {
                throw new SSExportException(
                        SSBundleString.getString("sieexport.noassesmentyear"));
            }

            iWriter.append(SIELabel.SIE_TAXAR);
            iWriter.append(iAssessementYear);
            iWriter.append("BFL");
            iWriter.newLine();

            return true;
        }
        return false;
    }

}
