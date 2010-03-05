package se.swedsoft.bookkeeping.calc.math;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSAccountPlan;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.calc.data.SSAccountGroup;
import se.swedsoft.bookkeeping.calc.data.SSAccountSchema;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.math.BigDecimal;

/**
 * Date: 2006-feb-17
 * Time: 10:55:55
 */
public class SSAccountMath {

    /**
     *
     * @param iAccount
     * @param iFrom
     * @param iTo
     * @return
     */
    public static boolean inPeriod( SSAccount iAccount, SSAccount iFrom, SSAccount iTo){
        Integer iNumber     = iAccount.getNumber();
        Integer iFromNumber = iFrom   .getNumber();
        Integer iToNumber   = iTo     .getNumber();

        if(iNumber == null || iFromNumber == null || iToNumber == null) return false;

        return iFromNumber <= iNumber && iNumber <= iToNumber;
    }


    /**
     * Returns if the specified account is a result account
     *
     * @param iAccount
     * @param pYearData
     * @return if the acoount is a result account
     */
    public static boolean isResultAccount(SSAccount iAccount, SSNewAccountingYear pYearData){
        SSAccountSchema iAccountSchema = SSAccountSchema.getAccountSchema(pYearData);

        for(se.swedsoft.bookkeeping.calc.data.SSAccountGroup iAccountGroup : iAccountSchema.getResultGroups() ){
            if( iAccountGroup.getFromAccount() <= iAccount.getNumber() && iAccountGroup.getToAccount() >= iAccount.getNumber() ){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns if the specified account is a balance account
     *
     * @param iAccount
     * @param pYearData
     * @return if the acoount is a balance account
     */
    public static boolean isBalanceAccount(SSAccount iAccount, SSNewAccountingYear pYearData){
        SSAccountSchema iAccountSchema = SSAccountSchema.getAccountSchema(pYearData);


        for(SSAccountGroup iAccountGroup : iAccountSchema.getBalanceGroups() ){
            if( iAccountGroup.getFromAccount() <= iAccount.getNumber() && iAccountGroup.getToAccount() >= iAccount.getNumber() ){
                return true;
            }
        }
        return false;
    }


    /**
     * Gets a account from the current year
     *
     * @param pAccountNr
     * @return the account or null
     */
    public static SSAccount getAccount(Integer pAccountNr) {
       SSAccountPlan iAccountPlan = SSDB.getInstance().getCurrentAccountPlan();

        if(iAccountPlan == null) return null;

        return iAccountPlan.getAccount(pAccountNr);
    }

    /**
     * Returns a List of accounts that are balance accounts.
     *
     * @param pAccountingYear
     * @param pAccounts
     * @return list of balance accounts
     */
    public static List<SSAccount> getBalanceAccounts(SSNewAccountingYear pAccountingYear, List<SSAccount> pAccounts) {
        List<SSAccount> iFiltered = new LinkedList<SSAccount>();

        for(SSAccount iAccount: pAccounts){
            if( isBalanceAccount(iAccount, pAccountingYear) ){
                iFiltered.add(iAccount);
            }
        }
        return iFiltered;
    }

    /**
     * Returns a List of accounts that are result accounts.
     *
     * @param pAccountingYear
     * @param pAccounts
     *
     * @return list of result accounts
     */
    public static List<SSAccount> getResultAccounts(SSNewAccountingYear pAccountingYear, List<SSAccount> pAccounts) {
        List<SSAccount> iFiltered = new LinkedList<SSAccount>();

        for(SSAccount iAccount: pAccounts){
            if( isResultAccount(iAccount, pAccountingYear) ){
                iFiltered.add(iAccount);
            }
        }
        return iFiltered;
    }


    /**
     * Returns the account group.
     *
     * @return The account group of the current account.
     */
    public static int getResultGroup(SSAccount iAccount, SSNewAccountingYear pYearData){
        SSAccountSchema iAccountSchema = SSAccountSchema.getAccountSchema(pYearData);


        for(SSAccountGroup iAccountGroup : iAccountSchema.getBalanceGroups() ){
            if( iAccountGroup.getFromAccount() <= iAccount.getNumber() && iAccountGroup.getToAccount() >= iAccount.getNumber() ){
                return iAccountGroup.getId();
            }
        }
        return -1;
    }


    /**
     * Returns a List of accounts that are balance accounts.
     *
     * @param pAccountingYear
     * @return list of balance accounts
     */
    public static List<SSAccount> getBalanceAccounts(SSNewAccountingYear pAccountingYear) {
        return getBalanceAccounts(pAccountingYear, pAccountingYear.getAccounts());
    }

    /**
     * Returns a List of accounts that are result accounts.
     *
     * @param pAccountingYear
     *
     * @return list of result accounts
     */
    public static List<SSAccount> getResultAccounts(SSNewAccountingYear pAccountingYear) {
        return getResultAccounts(pAccountingYear, pAccountingYear.getAccounts());
    }

    /**
     * Returns the accounts with numbers between (and including) the given accounts.
     *
     * @param pAccounts The list of accounts to filter.
     * @param pFrom     The from account.
     * @param pTo       The to account.
     *
     * @return A list with accounts with number between from and to.
     */
    public static List<SSAccount> getAccounts(List<SSAccount> pAccounts, SSAccount pFrom, SSAccount pTo) {
        List<SSAccount> iFiltered = new LinkedList<SSAccount>();

        for (SSAccount iAccount : pAccounts) {
            if (inPeriod(iAccount, pFrom,  pTo) ) {
                iFiltered.add(iAccount);
            }
        }

        return iFiltered;
    }


    /**
     * Returns the first account in the current accounting year.
     *
     * @return The first account or null if there is none.
     */
    public static SSAccount getFirstAccount(List<SSAccount> pAccounts) {
        SSAccount iFirst = null;

        for (SSAccount iAccount : pAccounts) {
            if (iFirst == null || iAccount.getNumber() < iFirst.getNumber()) {
                iFirst = iAccount;
            }
        }

        return iFirst;
    }

    /**
     * Returns the last account in the current accounting year.
     *
     * @return The last account or null if there is none.
     */
    public static SSAccount getLastAccount(List<SSAccount> pAccounts) {
        SSAccount iLast = null;

        for (SSAccount iAccount : pAccounts) {
            if (iLast == null || iAccount.getNumber() > iLast.getNumber()) {
                iLast = iAccount;
            }
        }
        return iLast;
    }



    // ****************************************************************************************
    // VAT


    /**
     * Returns the accounts with VAT codes specified. <P>
     *
     * @param pAccounts The list of accounts to filter.
     * @param pVatCodes The VAT Codes
     *
     * @return A list with accounts with number between from and to.
     */
    public static List<SSAccount> getAccountsByVATCode(List<SSAccount> pAccounts, String ... pVatCodes ) {
        List <SSAccount> filtered = new LinkedList<SSAccount>();

        for( SSAccount iAccount : pAccounts){

            for(String iVatCode : pVatCodes){
                if( iVatCode.equals( iAccount.getVATCode() ) ) {
                    filtered.add(iAccount);
                    break;
                }
            }
        }
        return filtered;
    }


    /**
     * @param pSums
     * @param pVatCodes
     *
     * @return BigDecimal
     */
    public static BigDecimal getSumByVATCodeForAccounts(Map<SSAccount, BigDecimal> pSums, String ... pVatCodes ) {
        BigDecimal sum = new BigDecimal(0.0);
        for(SSAccount iAccount : pSums.keySet() ){

            for(String iVatCode : pVatCodes){
                if( iVatCode.equals( iAccount.getVATCode() ) ) {
                    sum = sum.add( pSums.get(iAccount) );
                    break;
                }
            }

        }
        return sum;
    }



    /**
     * Returns the number of accounts with VAT codes specified. <P>
     *
     * @param pAccounts The list of accounts to filter.
     * @param pVatCodes The VAT Codes
     *
     * @return The number of codes.
     */
    public static  int getNumAccountsByVatCode(List<SSAccount> pAccounts, String ... pVatCodes ) {
        return getAccountsByVATCode(pAccounts, pVatCodes).size();
    }



    /**
     * Returns the accounts with VAT codes specified.
     *
     * @param pAccounts The list of accounts to filter.
     * @param pVatCode The VAT Code
     * @param pDefaultAccount The account to use if no account is marked with the vatCode
     *
     * @return The account marked with the VAT code if exactly one match, defaultAccount if no match and null if more then one matching account
     */
    public static SSAccount getAccountWithVATCode(List<SSAccount> pAccounts, String pVatCode, SSAccount pDefaultAccount ) {
        // get the accounts marked with the VAT code
        List<SSAccount> theAccounts = getAccountsByVATCode( pAccounts, pVatCode );

        // If more then one account is marked return null to let the caller know that there's inconsistency in the accountplan
        if( theAccounts.size() > 1 ){
            return null;
        }
        //openWarningDialog(iMainFrame, "vatBasis.dialogMoreThenOneAccount");
        // Return the wanter account if it exists, or return the default account
        if(theAccounts.isEmpty()) {
            return pDefaultAccount;
        } else {
            return theAccounts.get(0);
        }
    }





    // ****************************************************************************************
    // SRU




    /**
     * Returns the accounts with SRU codes specified. <P>
     *
     * @param pAccounts The list of accounts to filter.
     * @param pSruCodes The SRU Codes
     *
     * @return A list with accounts with number between from and to.
     */
    public static List<SSAccount> getAccountsBySRUCode(List<SSAccount> pAccounts, String ... pSruCodes ) {
        List <SSAccount> filtered = new LinkedList<SSAccount>();

        for( SSAccount iAccount : pAccounts){

            if(iAccount.getSRUCode() == null) continue;


            for(String iSruCode : pSruCodes){
                if( iSruCode.equals( iAccount.getSRUCode() ) ) {
                    filtered.add(iAccount);
                    break;
                }
            }
        }
        return filtered;
    }

    /**
     * Returns the number of accounts with SRU codes specified.
     *
     * @param pAccounts The list of accounts to filter.
     * @param pSruCodes The SRU Codes
     *
     * @return The number of codes.
     */
    public static  int getNumAccountsBySRUCode(List<SSAccount> pAccounts, String ... pSruCodes ) {
        return getAccountsBySRUCode(pAccounts, pSruCodes).size();
    }



    /**
     * Returns the accounts without any SRU code specified.
     *
     * @param pAccounts The list of accounts to filter.
     *
     * @return A list with accounts with number between from and to.
     */
    public static List<SSAccount> getAccountsWithoutSRUCode(List<SSAccount> pAccounts ) {
        List <SSAccount> filtered = new LinkedList<SSAccount>();

        for( SSAccount iAccount : pAccounts){
            if( (iAccount.getSRUCode() == null) || (iAccount.getSRUCode().length() == 0) ) {
                filtered.add(iAccount);
                break;
            }
        }
        return filtered;
    }
    /**
     * Returns the number of accounts without any SRU code specified. <P>
     *
     * @param pAccounts The list of accounts to filter.
     *
     * @return The number of codes.
     */
    public static  int getNumAccountsWithoutSRUCode(List<SSAccount> pAccounts ) {
        return getAccountsWithoutSRUCode(pAccounts).size();
    }











    /**
     * @param pSums
     * @param pSruCodes
     *
     * @return BigDecimal
     */
    public static BigDecimal getSumBySRUCodeForAccounts(Map<SSAccount, BigDecimal> pSums, String ... pSruCodes ) {

        BigDecimal sum = new BigDecimal(0.0);
        for(SSAccount iAccount : pSums.keySet() ){

            for(String iSruCode : pSruCodes){
                if( iSruCode.equals( iAccount.getSRUCode() ) ) {
                    sum = sum.add( pSums.get(iAccount) );
                    break;
                }
            }

        }
        return sum;
    }

}
