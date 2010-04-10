package se.swedsoft.bookkeeping.importexport.sie.fields;


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

import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.STRING;


/**
 * Date: 2006-feb-22
 * Time: 15:49:56
 */
public class SIEEntryKontoplanTyp implements SIEEntry {

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

        // #KPTYP typ
        if (!iReader.hasFields(STRING, STRING)) {
            throw new SSImportException(
                    SSBundleString.getString("sieimport.fielderror", iReader.peekLine()));
        }

        String iType = iReader.nextString();

        SSAccountPlan iAccountPlan = iCurrentYearData.getAccountPlan();

        if (iAccountPlan != null) {
            iAccountPlan.setType(iType);
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
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iCurrentYearData) throws SSExportException {

        SSAccountPlan iAccountPlan = iCurrentYearData.getAccountPlan();

        if (iAccountPlan != null) {
            // #KPTYP typ
            iWriter.append(SIELabel.SIE_KPTYP);
            iWriter.append(iAccountPlan.getType());
            iWriter.newLine();

        }

        return iAccountPlan != null;

    }
}
