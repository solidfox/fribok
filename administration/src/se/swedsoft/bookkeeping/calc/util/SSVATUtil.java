package se.swedsoft.bookkeeping.calc.util;

import se.swedsoft.bookkeeping.calc.math.SSAccountMath;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.data.SSVoucherRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Date: 2006-jan-26
 * Time: 08:40:41
 */
public class SSVATUtil {



    /**
     * @param creditMinusDebetSum The map of the summirised vouchers for the account
     *
     * @return The result
     */
    public static BigDecimal getVatToPayOrRetrieve(Map<SSAccount, BigDecimal> creditMinusDebetSum){
        return SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "U1", "UVL", "U2", "U3", "UEU", "UTFU", "I", "IVL");
    }

    /**
     * @param creditMinusDebetSum The map of the summirised vouchers for the account
     *
     * @return The rounded result
     */
    public static BigDecimal getVatToPayOrRetrieveRounded(Map<SSAccount, BigDecimal> creditMinusDebetSum){
        return getVatToPayOrRetrieve( creditMinusDebetSum ).setScale(0, RoundingMode.DOWN);
    }

    /**
     * Adds a new account to the required lists
     */
    private static void addAccount(SSAccount account, List<SSAccount>accounts, Map<SSAccount, BigDecimal> creditMinusDebetSum, Map<SSAccount, BigDecimal> debetMinusCreditSum){
        accounts.add(account);
        creditMinusDebetSum.put(account, new BigDecimal(0.0));
        debetMinusCreditSum.put(account, new BigDecimal(0.0));
    }


    /**
     * @param name
     * @param iDateFrom
     * @param iDateTo
     * @param iAccountR1 The account with the VAT code R1
     * @param iAccountR2 The account with the VAT code R2
     * @param iAccountA The account with the VAT code A
     *
     * @return The voucher
     */
    public static SSVoucher generateVATVoucher(String name, Date iDateFrom, Date iDateTo, SSAccount iAccountR1, SSAccount iAccountR2, SSAccount iAccountA){

        DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        String iDescription = String.format(SSBundle.getBundle().getString("vatreport2007.voucherdescription"), iFormat.format(iDateFrom), iFormat.format(iDateTo));

        List<SSAccount> iAccounts = SSAccountMath.getAccountsByVATCode( SSDB.getInstance().getAccounts(), "U1", "U2", "U3", "UVL", "UEU", "UTFU", "I", "IVL");
        List<SSVoucher> iVouchers = SSVoucherMath.getVouchers         ( SSDB.getInstance().getVouchers(), iDateFrom, iDateTo);

        Map<SSAccount, BigDecimal> iCreditMinusDebetSum  = SSVoucherMath.getCreditMinusDebetSum(iVouchers);
        Map<SSAccount, BigDecimal> debetMinusCreditSum = SSVoucherMath.getDebetMinusCreditSum(iVouchers);


        SSVoucher    iVoucher = new SSVoucher();
        SSVoucherRow iRow;

        iVoucher.doAutoIncrecement();
        iVoucher.setDescription( iDescription );
        iVoucher.setDate(iDateTo);

        BigDecimal iSum        = new BigDecimal(0);
        for (SSAccount iAccount : iAccounts) {

            BigDecimal iValue = iCreditMinusDebetSum.get(iAccount);

            if(iValue == null || iValue.signum() == 0) continue;

            iRow = new SSVoucherRow();
            iRow.setAccount(iAccount);
            iRow.setValue(iValue);

            iVoucher.addVoucherRow(iRow);

            iSum  = iSum.add(iValue);
        }

        if(iSum.signum() != 0){
            iRow = new SSVoucherRow();

            BigDecimal iRounded = iSum.setScale(0, RoundingMode.DOWN);

            if(iRounded.signum() > 0){
                iRow.setAccount( iAccountR2);
                iRow.setCredit ( iRounded   );
            }  else {
                iRow.setAccount( iAccountR1);
                iRow.setDebet  ( iRounded.abs() );
            }

            iVoucher.addVoucherRow(iRow);

            if(iRounded.subtract(iSum).signum() != 0){
                iRow = new SSVoucherRow();
                iRow.setAccount(iAccountA);
                iRow.setValue( iRounded.subtract(iSum) );

                iVoucher.addVoucherRow(iRow);
            }
        }




        return iVoucher;
                     /*
        SSNewAccountingYear iAccountingYear = SSDB.getInstance().getCurrentYear();

        List<SSAccount> accounts = SSAccountMath.getAccountsByVATCode( SSDB.getInstance().getAccounts(), "U1", "U2", "U3", "UVL", "UEU", "UTFU", "I", "IVL");


        BigDecimal vatToPayOrRetrieve        = SSVATUtil.getVatToPayOrRetrieve       (debetMinusCreditSum);
        BigDecimal vatToPayOrRetrieveRounded = SSVATUtil.getVatToPayOrRetrieveRounded(debetMinusCreditSum);

        // Add the account for Öresutjämmning
        addAccount(accountA, accounts, creditMinusDebetSum, debetMinusCreditSum);

        // Check so the sum arent zero
        if(vatToPayOrRetrieve.signum() != 0 ){

            // debet - credit < 0 ==> Credit > Debet
            if(vatToPayOrRetrieve.signum() < 0  ){
                addAccount(accountR2, accounts, creditMinusDebetSum, debetMinusCreditSum);

                vatToPayOrRetrieve        = vatToPayOrRetrieve       .negate();
                vatToPayOrRetrieveRounded = vatToPayOrRetrieveRounded.negate();

                debetMinusCreditSum.put(accountR2, vatToPayOrRetrieveRounded );
                creditMinusDebetSum.put(accountA , vatToPayOrRetrieveRounded.subtract(vatToPayOrRetrieve));
            }
            // debet - credit > 0 ==> Debet > Credit
            else {
                addAccount(accountR1, accounts, creditMinusDebetSum, debetMinusCreditSum);

                creditMinusDebetSum.put(accountR1, vatToPayOrRetrieveRounded );
                creditMinusDebetSum.put(accountA , vatToPayOrRetrieve.subtract(vatToPayOrRetrieveRounded));
            }
        }
        SSVoucher voucher = SSVoucher.newVoucher();

        voucher.setDescription(name);
        voucher.setDate  ( to );
        for(SSAccount account: accounts ){


            BigDecimal debet  = creditMinusDebetSum.get(account);
            BigDecimal credit = debetMinusCreditSum.get(account);

            if( debet  != null && debet.signum()  <= 0) debet  = null;
            if( credit != null && credit.signum() <= 0) credit = null;

            if( debet != null || credit != null){
                SSVoucherRow iVoucherRow = new SSVoucherRow();
                iVoucherRow.setAccount(account);
                iVoucherRow.setDebet  (debet);
                iVoucherRow.setCredit (credit);

                voucher.addVoucherRow(iVoucherRow);
            }
        }

        return voucher;    */
    }


}
