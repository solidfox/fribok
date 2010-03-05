package se.swedsoft.bookkeeping.calc;

import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.calc.util.SSCalculatorException;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.ownreport.util.SSOwnReportAccountRow;

import java.math.BigDecimal;
import java.util.*;

public class SSOwnReportCalculator {


    public static class SSResultBudgetRow {

        private Integer    iGroup;

        private SSAccount  iAccount;

        public Integer getGroup() {
            return iGroup;
        }

        public SSAccount getAccount() {
            return iAccount;
        }
    }

    private SSOwnReport iOwnReport;

    private Date         iFrom;
    private Date         iTo;
    private SSNewProject    iProject;
    private SSNewResultUnit iResultUnit;

    private Map<SSAccount, BigDecimal> iChangePeriod;

    private Map<SSAccount, BigDecimal> iChangeBudget;




    public SSOwnReportCalculator(Date pFrom, Date pTo, SSOwnReport pOwnReport, SSNewProject pProject, SSNewResultUnit pResultUnit){
        iFrom       = pFrom;
        iTo         = pTo;
        iOwnReport  = pOwnReport;

        iProject = pProject;
        iResultUnit = pResultUnit;

        iChangePeriod          = new HashMap<SSAccount, BigDecimal>();
        iChangeBudget          = new HashMap<SSAccount, BigDecimal>();

    }




    /**
     *
     *
     * @throws SSCalculatorException
     */
    public void calculate() throws SSCalculatorException {

        // Get all years
        List<SSNewAccountingYear> iAllYearData = SSDB.getInstance().getYears();

        // All vouchers
        List<SSVoucher> iVouchers    = new LinkedList<SSVoucher>();

        // Add the vouchers for all years to the list
        for(SSNewAccountingYear iCurrent : iAllYearData ){
            iVouchers.addAll(  iCurrent.getVouchers()  );
        }

        // Loop through all vouchers
        for(SSVoucher iVoucher: iVouchers ){

            // If the date of the oucher is in between the start and end date, add to PeriodChange
            boolean inPeriod =  SSVoucherMath.inPeriod(iVoucher, iFrom, iTo);

            // Loop through all the voucher rows
            for(SSVoucherRow iRow: iVoucher.getRows()){
                SSAccount    iRowAccount    = iRow.getAccount();
                SSNewProject    iRowProject    = iRow.getProject();
                SSNewResultUnit iRowResultUnit = iRow.getResultUnit();

                // Skip the row is crossed or invalid
                if( !iRow.isValid() || iRow.isCrossed()  ) continue;

                BigDecimal iRowSum = SSVoucherMath.getCreditMinusDebet(iRow);

                if(inPeriod){

                    if(iProject != null){
                        if(inProject(iRowProject, iProject))
                            addValueToMap(iChangePeriod, iRowAccount, iRowSum);
                        else
                            addValueToMap(iChangePeriod, iRowAccount, new BigDecimal(0.0));
                    }
                    else if(iResultUnit != null){
                        if(inResultUnit(iRowResultUnit, iResultUnit))
                            addValueToMap(iChangePeriod, iRowAccount, iRowSum);
                        else
                            addValueToMap(iChangePeriod, iRowAccount, new BigDecimal(0.0));
                    }
                    else{
                        addValueToMap(iChangePeriod, iRowAccount, iRowSum);
                    }
                }

            }
        }

        // Fill the budget map
        for(SSOwnReportRow iOwnReportRow : iOwnReport.getHeadings()){
            for(SSOwnReportAccountRow iAccountRow : iOwnReportRow.getAccountRows()){
                BigDecimal iSum = iAccountRow.getSumForMonths(iFrom, iTo);
                if(iSum != null) addValueToMap(iChangeBudget, iAccountRow.getAccount(), iSum);
            }
        }
    }

    /**
     *
     * @param pRowProject
     * @param pProject
     * @return
     */
    private boolean inProject(SSNewProject pRowProject, SSNewProject pProject){
        return pRowProject != null && !pRowProject.isConcluded( iTo ) && pRowProject.equals(pProject);
    }

    /**
     *
     * @param pRowResultUnit
     * @param pResultUnit
     * @return
     */
    private boolean inResultUnit(SSNewResultUnit pRowResultUnit, SSNewResultUnit pResultUnit){
        return pRowResultUnit != null && pRowResultUnit.equals(pResultUnit);
    }



    /**
     * Change for all accounts over the year
     *
     * @return
     */

    /**
     * Change for all accounts over the period
     *
     * @return
     */
    public Map<SSAccount, BigDecimal> getChangePeriod() {
        return iChangePeriod;
    }

    /**
     * Change for the period according to budget
     *
     * @return
     */
    public Map<SSAccount, BigDecimal> getChangeBudget() {
        return iChangeBudget;
    }

    /**
     * b - a
     *
     * @return
     */
    public Map<SSAccount, BigDecimal> getDeviation(Map<SSAccount, BigDecimal> a, Map<SSAccount, BigDecimal> b) {

        Map<SSAccount, BigDecimal> iResult = new HashMap<SSAccount, BigDecimal>();

        for(Map.Entry<SSAccount, BigDecimal> ssAccountBigDecimalEntry1 : b.entrySet()){
            iResult.put(ssAccountBigDecimalEntry1.getKey(), ssAccountBigDecimalEntry1.getValue());
        }

        for(Map.Entry<SSAccount, BigDecimal> ssAccountBigDecimalEntry : a.entrySet()){
            subtractValueToMap(iResult, ssAccountBigDecimalEntry.getKey(), ssAccountBigDecimalEntry.getValue());
        }

        return iResult;
    }



    /**
     *
     * @param iMap
     * @param iAccount
     * @param iValue
     */
    private static void addValueToMap(Map<SSAccount, BigDecimal> iMap, SSAccount iAccount, BigDecimal iValue){
        // Add the row sum to the total sum
        BigDecimal s = iMap.get(iAccount);
        if(s == null) {
            iMap.put(iAccount, iValue );
        }   else {
            iMap.put(iAccount, s.add(iValue) );
        }
    }


    /**
     *
     * @param iMap
     * @param iAccount
     * @param iValue
     */
    private static void subtractValueToMap(Map<SSAccount, BigDecimal> iMap, SSAccount iAccount, BigDecimal iValue){
        // Add the row sum to the total sum
        BigDecimal s = iMap.get(iAccount);
        if(s == null) {
            iMap.put(iAccount, iValue.negate() );
        }   else {
            iMap.put(iAccount, s.subtract(iValue) );
        }
    }
}
