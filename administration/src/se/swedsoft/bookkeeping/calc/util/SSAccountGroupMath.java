package se.swedsoft.bookkeeping.calc.util;

import se.swedsoft.bookkeeping.data.SSAccount;

import java.util.LinkedList;
import java.util.List;

/**
 * Date: 2006-feb-15
 * Time: 17:12:40
 */
public class SSAccountGroupMath {


    /**
     *
     * @param iAccount
     * @return boolean
     */
    @Deprecated
    public static boolean isBalanceAccount(SSAccount iAccount){
        return (iAccount.getNumber() >= 1000 && iAccount.getNumber() <= 2999);
    }

    /**
     *
     * @param iAccount
     * @return
     */
    @Deprecated
    public static boolean isResultAccount(SSAccount iAccount){
        return iAccount.getNumber() > 3000;
    }


    /**
     *
     * @param iAccount
     * @return
     */
    @Deprecated
    public static int getResultGroup(SSAccount iAccount) {
        int iNumber = iAccount.getNumber();

        if (iNumber >= 3000 && iNumber <= 3799) {
            return 1;
        }
        else if (iNumber >= 4900 && iNumber <= 4999) {
            return 2;
        }
        else if (iNumber >= 3800 && iNumber <= 3899) {
            return 3;
        }
        else if (iNumber >= 3900 && iNumber <= 3999) {
            return 4;
        }
        else if (iNumber >= 4000 && iNumber <= 4899) {
            return 5;
        }
        else if (iNumber >= 5000 && iNumber <= 6999) {
            return 6;
        }
        else if (iNumber >= 7000 && iNumber <= 7699) {
            return 7;
        }
        else if (iNumber >= 7700 && iNumber <= 7899) {
            return 8;
        }
        else if (iNumber >= 7900 && iNumber <= 7999) {
            return 9;
        }
        else if (iNumber >= 8000 && iNumber <= 8699) {
            return 10;
        }
        else if (iNumber >= 8700 && iNumber <= 8799) {
            return 11;
        }
        else if (iNumber >= 8800 && iNumber <= 8899) {
            return 12;
        }
        else if (iNumber >= 8900 && iNumber <= 8999) {
            return 13;
        }

        return 0;
    }


    /**
     * Returns a List of accounts that are result accounts.
     *
     * @return A List.
     */
    @Deprecated
    public static List<SSAccount> getResultGroupAccounts(List<SSAccount> pAccounts, int pGroup) {
        List<SSAccount> accounts = new LinkedList<SSAccount>();
        for(SSAccount iAccount: pAccounts){
            if( getResultGroup(iAccount) == pGroup ){
                accounts.add(iAccount);
            }
        }
        return accounts;
    }

     /**
     * Returns a List of accounts that are result accounts.
     *
     * @return A List.
     */
     @Deprecated
    public static List<SSAccount> getResultAccounts(List<SSAccount> iAccounts) {
        List<SSAccount> accounts = new LinkedList<SSAccount>();
        for(SSAccount iAccount: iAccounts){
            if( isResultAccount(iAccount) ){
                accounts.add(iAccount);
            }
        }
        return accounts;
    }
}
