package se.swedsoft.bookkeeping.importexport.sie.fields;

import se.swedsoft.bookkeeping.calc.SSResultCalculator;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Date: 2006-feb-23
 * Time: 14:59:28
 */
public class SIEEntryResult extends SIEEntry {
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
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iYear) throws SSExportException {
        SSNewAccountingYear iPreviousYearData = SSDB.getInstance().getPreviousYear();

        boolean iHasData = false;
        if( iPreviousYearData != null ){
            Map<SSAccount, BigDecimal> iResult = SSResultCalculator.getResult(iPreviousYearData);

            for(SSAccount iAccount : iResult.keySet() ){
                iWriter.append( SIELabel.SIE_RES);
                iWriter.append( -1);
                iWriter.append( iAccount.getNumber());
                iWriter.append( iResult.get(iAccount)  );
                iWriter.newLine();
                iHasData = true;
            }
        }
        if( iYear != null ){
            Map<SSAccount, BigDecimal> iResult = SSResultCalculator.getResult((iYear));

            for(SSAccount iAccount : iResult.keySet() ){
                iWriter.append( SIELabel.SIE_RES);
                iWriter.append( 0);
                iWriter.append( iAccount.getNumber());
                iWriter.append( iResult.get(iAccount)  );
                iWriter.newLine();
                iHasData = true;
            }
        }

        return iHasData;
    }
}
