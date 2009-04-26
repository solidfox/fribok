package se.swedsoft.bookkeeping.calc;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.calc.util.SSCalculatorException;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.calc.math.SSAccountMath;

import java.util.*;
import java.math.BigDecimal;

/**
 * Date: 2006-feb-15
 * Time: 17:05:08
 */
public class SSBalanceCalculator {


    private SSNewAccountingYear iYearData;


    private Map<SSAccount, BigDecimal> iInBalance;

    private Map<SSAccount, BigDecimal> iInSaldo;

    private Map<SSAccount, BigDecimal> iChange;

    private Map<SSAccount, BigDecimal> iChangePeriod;

    private Map<SSAccount, BigDecimal> iOutSaldo;



    public SSBalanceCalculator(SSNewAccountingYear pYearData){
        iYearData  = pYearData;

        iInBalance    = new HashMap<SSAccount, BigDecimal>();
        iInSaldo      = new HashMap<SSAccount, BigDecimal>();
        iChange       = new HashMap<SSAccount, BigDecimal>();
        iChangePeriod = new HashMap<SSAccount, BigDecimal>();
        iOutSaldo     = new HashMap<SSAccount, BigDecimal>();
    }

    /**
     *
     * @throws SSCalculatorException
     */
    public void calculate() throws SSCalculatorException {
        calculate(iYearData.getFrom(), iYearData.getTo());
    }

    /**
     *
     * @param pFrom
     * @param pTo
     * @throws SSCalculatorException
     */
    public void calculate(Date pFrom, Date pTo) throws SSCalculatorException {
        List<SSVoucher> iVouchers  = iYearData.getVouchers();

        iInBalance = iYearData.getInBalance();


        // Add the inbalance to the insaldo
        for(SSAccount iAccount: iInBalance.keySet()) {
            iInSaldo.put(iAccount, iInBalance.get(iAccount)  );
        }

        // Loop through all vouchers
        for(SSVoucher iVoucher: iVouchers ){

            // If the date of the voucer is before the start date, add to InSaldo
            boolean inSaldo  = pFrom.compareTo(iVoucher.getDate()) > 0;

            // If the date of the oucher is in between the start and end date, add to PeriodChange
            boolean inPeriod =  SSVoucherMath.inPeriod(iVoucher, pFrom, pTo);

            // Loop through all the voucher rows
            for(SSVoucherRow iRow: iVoucher.getRows()){
                SSAccount iRowAccount = iRow.getAccount();

                // Skip the row is crossed or invalid
                if( !iRow.isValid() || iRow.isCrossed()  ) continue;

                BigDecimal iRowSum = SSVoucherMath.getDebetMinusCredit(iRow);


                // Add the row sum to the periodChange, if in range
                if (inPeriod){
                    // Add the row sum to the total sum
                    addValueToMap(iChange, iRowAccount, iRowSum);

                    addValueToMap(iChangePeriod, iRowAccount, iRowSum);
                }
                // Add the row sum to the inSaldo, if in range
                if (inSaldo ){
                    addValueToMap(iInSaldo     , iRowAccount, iRowSum);
                }
            }

        }
        // Add the period change to the sum
        for(SSAccount iAccount: iChangePeriod.keySet()) {
            addValueToMap(iOutSaldo, iAccount, iChangePeriod.get(iAccount) );
        }

        // Add the insaldo to the sum
        for(SSAccount iAccount: iInSaldo.keySet()) {
            addValueToMap(iOutSaldo, iAccount, iInSaldo.get(iAccount) );
        }

    }


    /**
     *
     * @return Returns all influencing accounts
     */
    public List<SSAccount> getAccounts() {
        List<SSAccount> iList = new LinkedList<SSAccount>();
        for(SSAccount iAccount : iOutSaldo.keySet()){
            iList.add(iAccount);
        }

        return iList;
    }

    /**
     *
     * @return The in balance
     */
    public Map<SSAccount, BigDecimal> getInBalance() {
        return iInBalance;
    }

    /**
     * Returns the inSaldo as:
     *
     * Inbalance + The change up to the From date
     *
     *
     * @return The in saldo
     */
    public Map<SSAccount, BigDecimal> getInSaldo() {
        return iInSaldo;
    }

    /**
     *
     * @return The change over the whole year
     */
    public Map<SSAccount, BigDecimal> getChange() {
        return iChange;
    }

    /**
     *
     * @return The change for the period
     */
    public Map<SSAccount, BigDecimal> getPeriodChange() {
        return iChangePeriod;
    }

    /**
     * Returns the sum as:
     *
     * InSaldo + PeriodChange
     *
     * @return The sum
     */
    public Map<SSAccount, BigDecimal> getOutSaldo() {
        return iOutSaldo;
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
     * @param pYearData
     * @return The inbalance for the year
     */
    public static Map<SSAccount, BigDecimal> getInBalance(SSNewAccountingYear pYearData){
        return pYearData.getInBalance();
    }

    /**
     *
     * @param pYearData
     * @return The outbalance for the year
     */
    public static Map<SSAccount, BigDecimal> getOutBalance(SSNewAccountingYear pYearData){
        List<SSVoucher> iVouchers  = pYearData.getVouchers();

        Map<SSAccount, BigDecimal> iOutBalance = new HashMap<SSAccount, BigDecimal>();
        Map<SSAccount, BigDecimal> iInBalance  = pYearData.getInBalance();

        // Add the inbalance to the out balance
        for(SSAccount iAccount: iInBalance.keySet()) {
            if( SSAccountMath.isBalanceAccount(iAccount, pYearData) ){
                iOutBalance.put(iAccount, iInBalance.get(iAccount)  );
            }
        }

        // Loop through all vouchers
        for(SSVoucher iVoucher: iVouchers ){

            // Loop through all the voucher rows
            for(SSVoucherRow iRow: iVoucher.getRows()){
                SSAccount iRowAccount = iRow.getAccount();

                // Skip the row is crossed or invalid
                if( !iRow.isValid() || iRow.isCrossed()  ) continue;

                // Only calculate balance accoubts
                if( SSAccountMath.isBalanceAccount(iRowAccount, pYearData) ){
                    BigDecimal iRowSum = SSVoucherMath.getDebetMinusCredit(iRow);

                    addValueToMap(iOutBalance, iRowAccount, iRowSum);
                }
            }

        }
        return iOutBalance;
    }

}
