package se.swedsoft.bookkeeping.importexport.sie.fields;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSBudget;
import se.swedsoft.bookkeeping.data.SSMonth;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundleString;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import java.math.BigDecimal;
import java.util.Map;

import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.*;

/**
 * Date: 2006-feb-23
 * Time: 16:26:38
 */
public class SIEEntryPeriodBudget implements SIEEntry {
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

        // #PBUDGET årsnr period konto {dimensionsnr objectkod} saldo [kvantitet]
        if(!iReader.hasFields(STRING, INT, INT, INT, ARRAY, FLOAT)) {
            throw new SSImportException(SSBundleString.getString("sieimport.fielderror", iReader.peekLine()) );
        }

        int        iYear          = iReader.nextInteger();
        SSMonth    iMonth         = iReader.nextMonth();
        int        iAccountNumber = iReader.nextInteger();
        iReader.nextArray(); // We don't support result budget for objects
        BigDecimal iSaldo         = iReader.nextBigDecimal();

        if( (iYear == 0) && (iCurrentYearData != null) ){
            SSBudget iBudget = iCurrentYearData.getBudget();

            SSAccount iAccount = iCurrentYearData.getAccountPlan().getAccount(iAccountNumber);
            iMonth             = iBudget.getMonth(iMonth);

            if(iSaldo == null) iSaldo = new BigDecimal(0);

            iBudget.setSaldoForAccountAndMonth(iAccount, iMonth, iSaldo.negate());
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

        if(iPreviousYearData != null){
            SSBudget iBudget = iPreviousYearData.getBudget();

            Map<SSMonth, Map<SSAccount, BigDecimal>> iBudgetData = iBudget.getBudget();

            for(Map.Entry<SSMonth, Map<SSAccount, BigDecimal>> ssMonthMapEntry : iBudgetData.entrySet()){
                Map<SSAccount, BigDecimal> iAccounts = ssMonthMapEntry.getValue();
                for(Map.Entry<SSAccount, BigDecimal> ssAccountBigDecimalEntry : iAccounts.entrySet()){
                    BigDecimal iSaldo = ssAccountBigDecimalEntry.getValue();

                    if(iSaldo == null) continue;
                    
                    iWriter.append( SIELabel.SIE_PBUDGET);
                    iWriter.append( -1);
                    iWriter.append(ssMonthMapEntry.getKey());
                    iWriter.append(ssAccountBigDecimalEntry.getKey().getNumber());
                    iWriter.append( "{}");
                    iWriter.append( iSaldo.negate()  );
                    iWriter.newLine();

                    iHasData = true;
                }
            }
        }

        if(iCurrentYearData != null){
            SSBudget iBudget = iCurrentYearData.getBudget();

            Map<SSMonth, Map<SSAccount, BigDecimal>> iBudgetData = iBudget.getBudget();

            for(Map.Entry<SSMonth, Map<SSAccount, BigDecimal>> ssMonthMapEntry : iBudgetData.entrySet()){
                Map<SSAccount, BigDecimal> iAccounts = ssMonthMapEntry.getValue();
                for(Map.Entry<SSAccount, BigDecimal> ssAccountBigDecimalEntry : iAccounts.entrySet()){
                    BigDecimal iSaldo = ssAccountBigDecimalEntry.getValue();

                    if(iSaldo == null) continue;

                    iWriter.append( SIELabel.SIE_PBUDGET);
                    iWriter.append( 0);
                    iWriter.append(ssMonthMapEntry.getKey());
                    iWriter.append(ssAccountBigDecimalEntry.getKey().getNumber());
                    iWriter.append( "{}");
                    iWriter.append( iSaldo.negate()  );
                    iWriter.newLine();

                    iHasData = true;
                }
            }
        }

        return iHasData;
    }
}
