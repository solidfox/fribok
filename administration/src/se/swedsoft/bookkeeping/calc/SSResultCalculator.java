package se.swedsoft.bookkeeping.calc;

import se.swedsoft.bookkeeping.calc.math.SSAccountMath;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.calc.util.SSCalculatorException;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.math.BigDecimal;
import java.util.*;

/**
 * Date: 2006-feb-21
 * Time: 10:33:13
 */
public class SSResultCalculator {


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



    private SSNewAccountingYear   iYearData;

    private Date         iFrom;
    private Date         iTo;
    private SSNewProject    iProject;
    private SSNewResultUnit iResultUnit;


    private Map<SSAccount, BigDecimal> iChange;

    private Map<SSAccount, BigDecimal> iChangePeriod;

    private Map<SSAccount, BigDecimal> iChangePrevYear;

    private Map<SSAccount, BigDecimal> iChangeBudget;

    private Map<SSAccount, BigDecimal> iProjectChange;

    private Map<SSAccount, BigDecimal> iProjectChangePeriod;



    private Map<SSAccount, BigDecimal> iResultunitChange;

    private Map<SSAccount, BigDecimal> iResultunitChangePeriod;



    public SSResultCalculator(SSNewAccountingYear pYearData, Date pFrom, Date pTo, SSNewProject pProject, SSNewResultUnit pResultUnit){
        iYearData   = pYearData;
        iFrom       = pFrom;
        iTo         = pTo;
        iProject    = pProject;
        iResultUnit = pResultUnit;

        iChange                = new HashMap<SSAccount, BigDecimal>();
        iChangePeriod          = new HashMap<SSAccount, BigDecimal>();

        iChangePrevYear        = new HashMap<SSAccount, BigDecimal>();
        iChangeBudget          = new HashMap<SSAccount, BigDecimal>();

        iProjectChange         = new HashMap<SSAccount, BigDecimal>();
        iProjectChangePeriod   = new HashMap<SSAccount, BigDecimal>();

        iResultunitChange      = new HashMap<SSAccount, BigDecimal>();
        iResultunitChangePeriod= new HashMap<SSAccount, BigDecimal>();
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

            boolean inYear   =  SSVoucherMath.inPeriod(iVoucher, iYearData.getFrom(), iYearData.getTo());

            boolean inPrevYear = SSVoucherMath.inPeriodPrevYear(iVoucher, iFrom, iTo);


            // Loop through all the voucher rows
            for(SSVoucherRow iRow: iVoucher.getRows()){
                SSAccount    iRowAccount    = iRow.getAccount();
                SSNewProject    iRowProject    = iRow.getProject();
                SSNewResultUnit iRowResultUnit = iRow.getResultUnit();

                // Skip the row is crossed or invalid
                if( !iRow.isValid() || iRow.isCrossed()  ) continue;

                BigDecimal iRowSum = SSVoucherMath.getCreditMinusDebet(iRow);


                if(inYear){
                    addValueToMap(iChange, iRowAccount, iRowSum);
                }
                if(inPeriod){
                    addValueToMap(iChangePeriod, iRowAccount, iRowSum);
                }
                if(inPrevYear){
                    addValueToMap(iChangePrevYear, iRowAccount, iRowSum);
                }
                if( inProject(iRowProject, iProject) )  {

                    addValueToMap(iProjectChange, iRowAccount, iRowSum);

                    if(inPeriod){
                        addValueToMap(iProjectChangePeriod, iRowAccount, iRowSum);
                    }
                }
                if( inResultUnit(iRowResultUnit, iResultUnit)   )  {

                    addValueToMap(iResultunitChange, iRowAccount, iRowSum);

                    if(inPeriod){
                        addValueToMap(iResultunitChangePeriod, iRowAccount, iRowSum);
                    }


                }
            }
        }

        SSBudget       iBudget = iYearData.getBudget();
        // Fill the budget map
        for(SSAccount iAccount: iBudget.getAccounts()){
            BigDecimal iSum = iBudget.getSumForAccount(iAccount, iFrom, iTo);

            if( iSum != null && iSum.signum() != 0) addValueToMap(iChangeBudget, iAccount, iSum);
        }


    }

    /**
     *
     * @param pRowProject
     * @param pProject
     * @return
     */
    private boolean inProject(SSNewProject pRowProject, SSNewProject pProject){
        return (pRowProject != null) && !pRowProject.isConcluded( iTo ) && ((pProject == null) || (pRowProject.equals(pProject)));
    }

    /**
     *
     * @param pRowResultUnit
     * @param pResultUnit
     * @return
     */
    private boolean inResultUnit(SSNewResultUnit pRowResultUnit, SSNewResultUnit pResultUnit){
        return (pRowResultUnit != null) && ((pResultUnit == null) || (pRowResultUnit.equals(pResultUnit)));
    }



    /**
     * Change for all accounts over the year
     *
     * @return
     */
    public Map<SSAccount, BigDecimal> getChange() {
        return iChange;
    }

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
     * Change for the same period last year
     *
     * @return
     */
    public Map<SSAccount, BigDecimal> getChangeLastYear() {
        return iChangePrevYear;
    }

    /**
     * Change for the projects over all years
     *
     * @return
     */
    public Map<SSAccount, BigDecimal> getProjectChange() {
        return iProjectChange;
    }

    /**
     * Change for projects over the period
     *
     * @return
     */
    public Map<SSAccount, BigDecimal> getProjectChangePeriod() {
        return iProjectChangePeriod;
    }

    /**
     * Change for the resultunits over all years
     *
     * @return
     */
    public Map<SSAccount, BigDecimal> getResultunitChange() {
        return iResultunitChange;
    }

    /**
     * Change for resultunits over the period
     *
     * @return
     */
    public Map<SSAccount, BigDecimal> getResultunitChangePeriod() {
        return iResultunitChangePeriod;
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

    /**
     * Get the result for the year as
     *
     * debet - credit
     *
     * @param pYearData
     * @return The result for the year
     */
    public static Map<SSAccount, BigDecimal> getResult(SSNewAccountingYear pYearData){
        List<SSVoucher> iVouchers  = pYearData.getVouchers();

        Map<SSAccount, BigDecimal> iResult = new HashMap<SSAccount, BigDecimal>();

        // Loop through all vouchers
        for(SSVoucher iVoucher: iVouchers ){

            // Loop through all the voucher rows
            for(SSVoucherRow iRow: iVoucher.getRows()){
                SSAccount iRowAccount = iRow.getAccount();

                // Skip the row is crossed or invalid
                if( !iRow.isValid() || iRow.isCrossed()  ) continue;

                // Only calculate result accoubts
                if( SSAccountMath.isResultAccount(iRowAccount, pYearData) ){
                    BigDecimal iRowSum = SSVoucherMath.getDebetMinusCredit(iRow);

                    addValueToMap(iResult, iRowAccount, iRowSum);
                }
            }

        }
        return iResult;
    }

}
