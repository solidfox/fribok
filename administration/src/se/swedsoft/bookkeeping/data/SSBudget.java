package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.calc.math.SSAccountMath;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.*;

/**
 * User: Fredrik Stigsson
 * Date: 2006-jan-27
 * Time: 10:58:42
 */
public class SSBudget implements Serializable  {
    /**
     * Constant for serialization versioning.
     */
    static final long serialVersionUID = 1L;


    private static Calendar cCalendar = Calendar.getInstance();


    private transient SSNewAccountingYear iAccountingYear;

    private Date iFrom;

    private Date iTo;

    private Map<SSMonth, Map<SSAccount, BigDecimal>> iBudget;




    /**
     * Default constructor
     */
    public SSBudget(){
        iFrom           = new Date();
        iTo             = new Date();
        iAccountingYear = null;
        iBudget         = new HashMap<SSMonth, Map<SSAccount, BigDecimal>>();
    }

    /**
     * Copy constructor
     * @param pSource
     */
    public SSBudget(SSBudget pSource){
        iFrom           = pSource.iFrom;
        iTo             = pSource.iTo;
        iAccountingYear = pSource.iAccountingYear;
        iBudget         = new HashMap<SSMonth, Map<SSAccount, BigDecimal>>();

        Map<SSMonth, Map<SSAccount, BigDecimal>> iSource =  pSource.getBudget();

        for(Map.Entry<SSMonth, Map<SSAccount, BigDecimal>> ssMonthMapEntry : iSource.entrySet()){
            Map<SSAccount, BigDecimal> iMonthlyBudget = new HashMap<SSAccount, BigDecimal>();

            iMonthlyBudget.putAll(ssMonthMapEntry.getValue());

            iBudget.put(ssMonthMapEntry.getKey(), iMonthlyBudget);
        }

    }




    /**
     *
     */
    public void clear() {
        iBudget = createBudgetForYear();
    }


    /**
     *
     * @return the from date
     */
    public Date getFrom() {
        return iFrom;
    }


    /**
     *
     * @return the to date
     */
    public Date getTo() {
        return iTo;
    }

    /**
     * Returns the accounting year for this budget
     *
     * @return the accounting year
     */
    public SSNewAccountingYear getAccountingYear(){
        return iAccountingYear;
    }


    /**
     * @return The accounts
     */
    public List<SSAccount> getAccounts() {
        if(iAccountingYear != null){
            return SSAccountMath.getResultAccounts(iAccountingYear);
        }
        return Collections.emptyList();
    }


    /**
     * @return The months
     */
    public List<SSMonth> getMonths() {
        if(iBudget == null){
            iBudget = createBudgetForYear();
        }
        List<SSMonth> iMonths = new LinkedList<SSMonth>();

        for(SSMonth iMonth: iBudget.keySet()){
            iMonths.add(iMonth);
        }

        Collections.sort(iMonths, new Comparator<SSMonth>() {
            public int compare(SSMonth o1, SSMonth o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        return iMonths;
    }

    /**
     * @param pMonth
     * @return The months
     */
    public SSMonth getMonth(  SSMonth pMonth) {
        for(SSMonth iMonth: iBudget.keySet()){
            if(iMonth.equals(pMonth)){
                return iMonth;
            }
        }
        return null;
    }


    /**
     * Sets the current year, if the from and to dates differes from the internal current the montly distribution
     * will be lost.
     *
     * @param pAccountingYear The year
     */
    public void setYear(SSNewAccountingYear pAccountingYear){
        iAccountingYear = pAccountingYear;

        if( iAccountingYear.getFrom().compareTo(iFrom) != 0 ||  iAccountingYear.getTo().compareTo(iTo) != 0   ){
            iFrom = iAccountingYear.getFrom();
            iTo   = iAccountingYear.getTo  ();

            iBudget = createBudgetForYear();
        }
    }


    /**
     *
     * @return
     */
    public Map<SSMonth, Map<SSAccount, BigDecimal>> getBudget(){
        if(iBudget == null){
            iBudget = createBudgetForYear();
        }
        return iBudget;
    }

    /**
     *
     * @param pMonth
     * @return the budget for a month
     */
    public Map<SSAccount, BigDecimal> getBudget(SSMonth pMonth){
        if(iBudget == null){
            iBudget = createBudgetForYear();
        }
        return iBudget.get(pMonth);
    }


    /**
     * Sets the budget sum for an account. This will be spread over the year
     *
     * @param pAccount The account to set the sum to.
     * @param pValue The value
     */
    public void setSumForAccount(SSAccount pAccount, BigDecimal pValue){
        List<SSMonth> iMonths = getMonths();

        if(pValue == null || pValue.signum() == 0){

            // Delete the value for each month
            for(SSMonth iMonth : iMonths){
                iBudget.get(iMonth).remove(pAccount );
            }
            return;
        }

        // If we have no months we cannot set any sum
        if(iMonths.isEmpty()){
            return;
        }
        // Get the number of months as a bigdecimal for our calculations
        BigDecimal numMonths = new BigDecimal( iMonths.size() );

        // Make shure we have 2 decimals for the sum, else the accuracy of the divission will be of
        pValue = pValue.setScale(2, BigDecimal.ROUND_HALF_UP);
        // Get the sum to be added per month
        BigDecimal sumPerMonth = pValue.divide( numMonths, BigDecimal.ROUND_FLOOR  );
        // Get the last few ören that differs from the total sum
        BigDecimal remainder   = pValue.subtract( sumPerMonth.multiply( numMonths ) )  ;


        // Set the value for each month to the wanted one
        for(SSMonth iMonth : iMonths){
            iBudget.get(iMonth).put(pAccount, sumPerMonth );
        }
        // Add the remainder to the last month
        addValueToMonth( iMonths.get( iMonths.size()-1 ), pAccount, remainder);
    }

    /**
     * Add a value to a month
     *
     * @param pMonth The month
     * @param pAccount
     * @param pValue The value
     */
    public void addValueToMonth(SSMonth pMonth, SSAccount pAccount, BigDecimal pValue){
        BigDecimal current = iBudget.get(pMonth).get(pAccount);

        if(current != null){
            iBudget.get(pMonth).put(pAccount, current.add(pValue));
        } else {
            iBudget.get(pMonth).put(pAccount, pValue);
        }

    }


    /**
     * Get the budget sum for an account.
     *
     * @param pAccount The account to get the sum from.
     *
     * @return The sum
     */
    public BigDecimal getSumForAccount(SSAccount pAccount){
        BigDecimal iSum = new BigDecimal(0.0);

        for(Map.Entry<SSMonth, Map<SSAccount, BigDecimal>> ssMonthMapEntry : iBudget.entrySet()){
            BigDecimal iValue = ssMonthMapEntry.getValue().get(pAccount);

            if(iValue == null) continue;

            iSum = iSum.add(iValue);
        }
        return iSum.signum() == 0 ? null : iSum;
    }

    /**
     * Get the budget sum for an account.
     *
     * @param pAccount The account to get the sum from.
     * @param pFrom
     * @param pTo
     *
     * @return The sum
     */
    public BigDecimal getSumForAccount(SSAccount pAccount, Date pFrom, Date pTo){
        BigDecimal iSum = new BigDecimal(0.0);

        for(Map.Entry<SSMonth, Map<SSAccount, BigDecimal>> ssMonthMapEntry : iBudget.entrySet()){
            BigDecimal iValue = ssMonthMapEntry.getValue().get(pAccount);

            if(iValue == null || !ssMonthMapEntry.getKey().isBetween(pFrom, pTo)) continue;

            iSum = iSum.add(iValue);
        }
        return iSum.signum() == 0 ? null : iSum;
    }

    /**
     * Get the budget sum for all accounts.
     *
     * @return The sum
     */
    public Map<SSAccount, BigDecimal> getSumForAccounts(){
        HashMap<SSAccount, BigDecimal> sum = new HashMap<SSAccount, BigDecimal>();

        for(SSAccount account: getAccounts() ){
            sum.put(account, getSumForAccount(account) );
        }
        return sum;
    }

    /**
     * Get the budget sum for all accounts.
     * @param pFrom
     * @param pTo
     *
     * @return The sum
     */
    public Map<SSAccount, BigDecimal> getSumForAccounts(Date pFrom, Date pTo){
        HashMap<SSAccount, BigDecimal> sum = new HashMap<SSAccount, BigDecimal>();

        for(SSAccount account: getAccounts() ){
            sum.put(account, getSumForAccount(account, pFrom, pTo) );
        }
        return sum;
    }

    /**
     * Sets the budget value for an account and month.
     *
     * @param pAccount The account to set the value to.
     * @param pMonth The month to set the value to.
     * @param pValue The value
     */
    public void setSaldoForAccountAndMonth(SSAccount pAccount, SSMonth pMonth, BigDecimal pValue){
        Map<SSAccount, BigDecimal> iMonthlyBudget = iBudget.get(pMonth);

        if( iMonthlyBudget != null){

            if(pValue == null || pValue.signum() == 0){
                iMonthlyBudget.put(pAccount, null) ;
            } else{
                iMonthlyBudget.put(pAccount, pValue) ;
            }
        }
    }

    /**
     * Get the budget value for an account and month.
     *
     * @param pAccount The account to get value sum from.
     * @param pMonth The month to set the value to.
     *
     * @return The value
     */
    public BigDecimal getValueForAccountAndMonth(SSAccount pAccount, SSMonth pMonth){

        Map<SSAccount, BigDecimal> iMonthlyBudget = iBudget.get(pMonth);

        if( iMonthlyBudget != null){
            return iMonthlyBudget.get(pAccount) ;
        }
        return null;
    }


    /**
     * Breaks a accounting year into it's months
     *
     * @return the new map
     */
    private Map <SSMonth, Map<SSAccount, BigDecimal>> createBudgetForYear(){
        Map<SSMonth, Map<SSAccount, BigDecimal>> iNewBudget = new HashMap<SSMonth, Map<SSAccount, BigDecimal>>();

        Map<SSAccount, BigDecimal> iSum = getSumForAccounts();

        List<SSMonth> iMonths = SSMonth.splitYearIntoMonths(iAccountingYear);

        for(SSMonth iMonth: iMonths){
            Map<SSAccount, BigDecimal> iMontlyBudget = new HashMap<SSAccount, BigDecimal>();

            iNewBudget.put(iMonth, iMontlyBudget);
        }

        // Set the sums
        for(Map.Entry<SSAccount, BigDecimal> ssAccountBigDecimalEntry : iSum.entrySet()){
            setSumForAccount(ssAccountBigDecimalEntry.getKey(), ssAccountBigDecimalEntry.getValue());
        }
        return iNewBudget;
    }

    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     *
     * @return a string representation of the object.
     */
    public String toString() {
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);

        StringBuffer b = new StringBuffer();

        b.append("Budget for " );
        b.append(format.format(iFrom) );
        b.append(" to" );
        b.append(format.format(iTo) );

        for(SSMonth iMonth: getMonths()){
            b.append("Month: " );
            b.append( iMonth );
            b.append("{\n");

            for(SSAccount iAccount: iBudget.get(iMonth).keySet()){
                b.append("  Account: \n" );
                b.append("  ");
                b.append(iAccount);
                b.append("    Sum:" );
                b.append("    ");
                b.append( iBudget.get(iMonth).get(iAccount) );
                b.append('\n');
            }
            b.append("}\n");
        }

        return b.toString();
    }

    /**
     *
     * @param iObjectInputStream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream iObjectInputStream)  throws IOException, ClassNotFoundException{
        iObjectInputStream.defaultReadObject();

        if(iBudget == null){
            iBudget = new HashMap<SSMonth, Map<SSAccount, BigDecimal>>();
        }

        if( iBudget.isEmpty() && !iFrom.equals(iTo)){
            iFrom = new Date();
            iTo   = new Date();

        }
    }




}

