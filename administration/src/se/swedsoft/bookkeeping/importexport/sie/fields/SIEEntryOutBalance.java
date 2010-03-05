package se.swedsoft.bookkeeping.importexport.sie.fields;

import se.swedsoft.bookkeeping.calc.SSBalanceCalculator;
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
 * User: Fredrik Stigsson
 * Date: 2006-feb-23
 * Time: 14:26:58
 */
public class SIEEntryOutBalance extends SIEEntry {
    /**
     * Imports the entry
     *
     * @param iImporter
     * @param iReader
     * @return If anything was imported
     * @throws se.swedsoft.bookkeeping.importexport.util.SSImportException
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
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iCurrentYearData) throws SSExportException {
        SSNewAccountingYear iPreviousYearData = SSDB.getInstance().getPreviousYear();

        boolean iHasData = false;
        if( iPreviousYearData != null ){
            Map<SSAccount, BigDecimal> iOutBalance = SSBalanceCalculator.getOutBalance(iPreviousYearData);

            for(Map.Entry<SSAccount, BigDecimal> ssAccountBigDecimalEntry : iOutBalance.entrySet()){
                iWriter.append( SIELabel.SIE_UB);
                iWriter.append( -1);
                iWriter.append(ssAccountBigDecimalEntry.getKey().getNumber());
                iWriter.append(ssAccountBigDecimalEntry.getValue());
                iWriter.newLine();
                iHasData = true;
            }
        }
        if( iCurrentYearData != null ){
            Map<SSAccount, BigDecimal> iOutBalance = SSBalanceCalculator.getOutBalance(iCurrentYearData);

            for(Map.Entry<SSAccount, BigDecimal> ssAccountBigDecimalEntry : iOutBalance.entrySet()){
                iWriter.append( SIELabel.SIE_UB);
                iWriter.append( 0);
                iWriter.append(ssAccountBigDecimalEntry.getKey().getNumber());
                iWriter.append(ssAccountBigDecimalEntry.getValue());
                iWriter.newLine();
                iHasData = true;
            }
        }

        return iHasData;
    }



}
