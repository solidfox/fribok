package se.swedsoft.bookkeeping.importexport.sie.fields;

import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.*;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.gui.util.SSBundleString;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.calc.SSBalanceCalculator;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.math.BigDecimal;
import java.util.Map;

/**
 * User: Fredrik Stigsson
 * Date: 2006-feb-23
 * Time: 14:06:33
 */
public class SIEEntryInBalance extends SIEEntry {
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

        if(!iReader.hasFields(STRING, INT, INT, FLOAT )) {
            throw new SSImportException(SSBundleString.getString("sieimport.fielderror", iReader.peekLine()) );
        }

        int iYear          = iReader.nextInteger();
        int iAccountNumber = iReader.nextInteger();

        if( (iYear == 0) && (iCurrentYearData != null) ){
            Map<SSAccount, BigDecimal> iInbalance = iCurrentYearData.getInBalance();

            SSAccount iAccount = iCurrentYearData.getAccountPlan().getAccount(iAccountNumber);

            iInbalance.put(iAccount, iReader.nextBigDecimal() );
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
        SSNewAccountingYear iPreviousYearData = SSDB.getInstance().getPreviousYear();

        boolean iHasData = false;
        if( iPreviousYearData != null ){
            Map<SSAccount, BigDecimal> iInbalance = SSBalanceCalculator.getInBalance(iPreviousYearData);

            for(SSAccount iAccount : iInbalance.keySet() ){
                BigDecimal iValue = iInbalance.get(iAccount);
                if(iValue != null){
                    iWriter.append( SIELabel.SIE_IB);
                    iWriter.append( -1);
                    iWriter.append( iAccount.getNumber());
                    iWriter.append( iValue );
                    iWriter.newLine();
                    iHasData = true;
                }
            }
        }
        if( iCurrentYearData != null ){
            Map<SSAccount, BigDecimal> iInbalance = SSBalanceCalculator.getInBalance(iCurrentYearData);

            for(SSAccount iAccount : iInbalance.keySet() ){
                BigDecimal iValue = iInbalance.get(iAccount);
                if(iValue != null){
                    iWriter.append( SIELabel.SIE_IB);
                    iWriter.append( 0);
                    iWriter.append( iAccount.getNumber());
                    iWriter.append( iValue );
                    iWriter.newLine();
                    iHasData = true;
                }
            }
        }

        return iHasData;
    }
}
