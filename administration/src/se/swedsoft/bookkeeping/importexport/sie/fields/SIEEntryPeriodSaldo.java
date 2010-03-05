package se.swedsoft.bookkeeping.importexport.sie.fields;

import se.swedsoft.bookkeeping.calc.SSSaldoCalculator;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEType;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Date: 2006-feb-23
 * Time: 15:03:23
 */
public class SIEEntryPeriodSaldo extends SIEEntry {
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

        List<SSNewResultUnit> iResultUnits = SSDB.getInstance().getResultUnits();
        List<SSNewProject   > iProjects    = SSDB.getInstance().getProjects();


        Map<SSMonth, Map<SSAccount, BigDecimal>> iMonths;

        boolean iHasData = false;
        if( iPreviousYearData != null ){

            iMonths = SSSaldoCalculator.getSaldo(iPreviousYearData);

            for(SSMonth iMonth: iMonths.keySet() ){
                Map<SSAccount, BigDecimal> iAccounts = iMonths.get(iMonth);
                for(SSAccount iAccount: iAccounts.keySet() ){
                    BigDecimal iSaldo = iAccounts.get(iAccount);

                    iWriter.append( SIELabel.SIE_PSALDO);
                    iWriter.append( -1);
                    iWriter.append( iMonth );
                    iWriter.append( iAccount.getNumber());
                    iWriter.append( "{}");
                    iWriter.append( iSaldo  );
                    iWriter.newLine();
                    iHasData = true;
                }
            }

            if(iExporter.getType() == SIEType.SIE_3 ){

                // Resultatenhet, #DIM 1
                for(SSNewResultUnit iResultUnit: iResultUnits){

                    iMonths = SSSaldoCalculator.getSaldo(iPreviousYearData, iResultUnit);

                    for(SSMonth iMonth: iMonths.keySet() ){
                        Map<SSAccount, BigDecimal> iAccounts = iMonths.get(iMonth);
                        for(SSAccount iAccount: iAccounts.keySet() ){
                            BigDecimal iSaldo = iAccounts.get(iAccount);

                            iWriter.append( SIELabel.SIE_PSALDO);
                            iWriter.append( -1);
                            iWriter.append( iMonth );
                            iWriter.append( iAccount.getNumber());
                            iWriter.append( 1, iResultUnit.getNumber() );
                            iWriter.append( iSaldo  );
                            iWriter.newLine();
                            iHasData = true;
                        }
                    }
                }
                // Projekt, #DIM 6
                for(SSNewProject iProject: iProjects){

                    iMonths = SSSaldoCalculator.getSaldo(iPreviousYearData, iProject);

                    for(SSMonth iMonth: iMonths.keySet() ){
                        Map<SSAccount, BigDecimal> iAccounts = iMonths.get(iMonth);
                        for(SSAccount iAccount: iAccounts.keySet() ){
                            BigDecimal iSaldo = iAccounts.get(iAccount);

                            iWriter.append( SIELabel.SIE_PSALDO);
                            iWriter.append( -1);
                            iWriter.append( iMonth );
                            iWriter.append( iAccount.getNumber());
                            iWriter.append( 6, iProject.getNumber() );
                            iWriter.append( iSaldo  );
                            iWriter.newLine();
                            iHasData = true;
                        }
                    }
                }
            }
        }

        if( iCurrentYearData != null ){
            iMonths = SSSaldoCalculator.getSaldo(iCurrentYearData);

            for(SSMonth iMonth: iMonths.keySet() ){
                Map<SSAccount, BigDecimal> iAccounts = iMonths.get(iMonth);
                for(SSAccount iAccount: iAccounts.keySet() ){
                    BigDecimal iSaldo = iAccounts.get(iAccount);

                    iWriter.append( SIELabel.SIE_PSALDO);
                    iWriter.append( 0);
                    iWriter.append( iMonth );
                    iWriter.append( iAccount.getNumber());
                    iWriter.append( "{}");
                    iWriter.append( iSaldo  );
                    iWriter.newLine();
                    iHasData = true;
                }

            }

            if(iExporter.getType() == SIEType.SIE_3 ){

                // Resultatenhet, #DIM 1
                for(SSNewResultUnit iResultUnit: iResultUnits){
                    iMonths = SSSaldoCalculator.getSaldo(iCurrentYearData, iResultUnit);

                    for(SSMonth iMonth: iMonths.keySet() ){
                        Map<SSAccount, BigDecimal> iAccounts = iMonths.get(iMonth);
                        for(SSAccount iAccount: iAccounts.keySet() ){
                            BigDecimal iSaldo = iAccounts.get(iAccount);

                            iWriter.append( SIELabel.SIE_PSALDO);
                            iWriter.append( 0);
                            iWriter.append( iMonth );
                            iWriter.append( iAccount.getNumber());
                            iWriter.append( 1, iResultUnit.getNumber() );
                            iWriter.append( iSaldo  );
                            iWriter.newLine();
                            iHasData = true;
                        }
                    }
                }
                // Projekt, #DIM 6
                for(SSNewProject iProject: iProjects){
                    iMonths = SSSaldoCalculator.getSaldo(iCurrentYearData, iProject);

                    for(SSMonth iMonth: iMonths.keySet() ){
                        Map<SSAccount, BigDecimal> iAccounts = iMonths.get(iMonth);
                        for(SSAccount iAccount: iAccounts.keySet() ){
                            BigDecimal iSaldo = iAccounts.get(iAccount);

                            iWriter.append( SIELabel.SIE_PSALDO);
                            iWriter.append( 0);
                            iWriter.append( iMonth );
                            iWriter.append( iAccount.getNumber());
                            iWriter.append( 6, iProject.getNumber() );
                            iWriter.append( iSaldo  );
                            iWriter.newLine();
                            iHasData = true;
                        }
                    }
                }
            }
        }
        return iHasData;
    }
}
