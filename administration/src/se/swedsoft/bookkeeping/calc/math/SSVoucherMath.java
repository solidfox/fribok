package se.swedsoft.bookkeeping.calc.math;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.math.BigDecimal;
import java.util.*;

/**
 * Date: 2006-feb-15
 * Time: 17:19:30
 */
public class SSVoucherMath {

    private static Calendar cCalendar = Calendar.getInstance();


    /**
     *
     * @param iVoucher
     * @param pFrom
     * @param pTo
     * @return
     */
    public static boolean inPeriod( SSVoucher iVoucher, Date pFrom, Date pTo){
        Date iDate = iVoucher.getDate();
        Date iFrom = SSDateMath.floor(pFrom);
        Date iTo   = SSDateMath.ceil (pTo);

        return (iFrom.getTime() <= iDate.getTime()) && (iDate.getTime() <= iTo.getTime());
    }


    /**
     * Return true if the voucher's date is before the supplied date
     *
     * @param iVoucher
     * @param pTo
     * @return
     */
    public static boolean inPeriod( SSVoucher iVoucher, Date pTo){
        Date iDate = iVoucher.getDate();
        Date iTo   = SSDateMath.ceil (pTo);

        return  (iDate.getTime() <= iTo.getTime());
    }



    /**
     *
     * @param iVoucher
     * @param iMonth
     * @return
     */
    public static boolean inPeriod( SSVoucher iVoucher, SSMonth iMonth){
        return iMonth.getFrom().compareTo(iVoucher.getDate()) <= 0 && iMonth.getTo().compareTo(iVoucher.getDate()) >= 0;
    }

    /**
     *
     * @param iVoucher
     * @param pFrom
     * @param pTo
     * @return
     */
    public static boolean inPeriod( SSVoucher iVoucher, SSVoucher pFrom, SSVoucher pTo){
        return pFrom.getNumber() <= iVoucher.getNumber() && pTo.getNumber() >= iVoucher.getNumber();
    }

    /**
     *
     * @param iVoucher
     * @param pFrom
     * @param pTo
     * @return
     */
    public static boolean inPeriodPrevYear( SSVoucher iVoucher, Date pFrom, Date pTo){
        cCalendar.setTime(pFrom);
        cCalendar.add(Calendar.YEAR, -1);

        Date iPrevYearFrom = cCalendar.getTime();

        cCalendar.setTime(pTo);
        cCalendar.add(Calendar.YEAR, -1);

        Date iPrevYearTo = cCalendar.getTime();

        return inPeriod(iVoucher, iPrevYearFrom, iPrevYearTo);
    }




    /**
     *
     * @param iVoucher
     * @return
     */
    public static BigDecimal getCreditSum(SSVoucher iVoucher){
        BigDecimal sum  = new BigDecimal(0.0);

        for(SSVoucherRow iVoucherRow: iVoucher.getRows()){
            if (iVoucherRow.isValid() && iVoucherRow.getCredit() != null && !iVoucherRow.isCrossed()) {
                sum = sum.add(iVoucherRow.getCredit());
            }
        }
        return sum;
    }

    /**
     *
     * @param iVoucher
     * @return
     */
    public static BigDecimal getDebetSum(SSVoucher iVoucher){
        BigDecimal sum  = new BigDecimal(0.0);

        for(SSVoucherRow iVoucherRow: iVoucher.getRows()){
            if (iVoucherRow.isValid() && iVoucherRow.getDebet() != null && !iVoucherRow.isCrossed()) {
                sum = sum.add(iVoucherRow.getDebet());
            }
        }
        return sum;
    }



    /**
     *
     * @param pVoucher
     * @return
     */
    public static BigDecimal getCreditMinusDebetSum(SSVoucher pVoucher){
        return getCreditSum(pVoucher).subtract( getDebetSum(pVoucher) );
    }

    /**
     *
     * @param pVoucher
     * @return
     */
    public static BigDecimal getDebetMinusCreditSum(SSVoucher pVoucher){
        return getDebetSum(pVoucher).subtract( getCreditSum(pVoucher) );
    }


    /**
     *
     * @param pVouchers
     * @return
     */
    public static Map<SSAccount, BigDecimal>  getCreditMinusDebetSum(List<SSVoucher> pVouchers){
        Map<SSAccount, BigDecimal> iSums = new HashMap<SSAccount, BigDecimal>();

        for(SSVoucher iVoucher: pVouchers){
            for(SSVoucherRow iVoucherRow: iVoucher.getRows()){
                SSAccount iAccount = iVoucherRow.getAccount();

                if (iVoucherRow.isValid() && !iVoucherRow.isCrossed()) {
                    BigDecimal s = iSums.get(iAccount);

                    if(s == null){
                        iSums.put(iAccount,       getCreditMinusDebet(iVoucherRow));
                    } else {
                        iSums.put(iAccount, s.add(getCreditMinusDebet(iVoucherRow)));
                    }
                }
            }
        }
        return iSums;
    }


    /**
     *
     * @param pVouchers
     * @return
     */
    public static Map<SSAccount, BigDecimal>  getDebetMinusCreditSum(List<SSVoucher> pVouchers){
        Map<SSAccount, BigDecimal> iSums = new HashMap<SSAccount, BigDecimal>();

        for(SSVoucher iVoucher: pVouchers){
            for(SSVoucherRow iVoucherRow: iVoucher.getRows()){
                SSAccount iAccount = iVoucherRow.getAccount();

                if (iVoucherRow.isValid() && !iVoucherRow.isCrossed()) {
                    BigDecimal s = iSums.get(iAccount);

                    if(s == null){
                        iSums.put(iAccount,       getDebetMinusCredit(iVoucherRow));
                    } else {
                        iSums.put(iAccount, s.add(getDebetMinusCredit(iVoucherRow)));
                    }
                }
            }
        }
        return iSums;
    }





    /**
     *
     * @param pAccountingYear
     * @param pVouchers
     * @return
     */
    public static Map<SSAccount, BigDecimal>  getCreditMinusDebetSumPlusIB(SSNewAccountingYear pAccountingYear, List<SSVoucher> pVouchers){
        Map<SSAccount, BigDecimal> iSums      = getCreditMinusDebetSum(pVouchers);
        Map<SSAccount, BigDecimal> iInBalance = pAccountingYear.getInBalance();

        for(Map.Entry<SSAccount, BigDecimal> ssAccountBigDecimalEntry : iInBalance.entrySet()){
            BigDecimal s = iSums.get(ssAccountBigDecimalEntry.getKey());

            if(s == null){
                iSums.put(ssAccountBigDecimalEntry.getKey(), ssAccountBigDecimalEntry.getValue());
            } else {
                iSums.put(ssAccountBigDecimalEntry.getKey(), s.add(ssAccountBigDecimalEntry.getValue()));
            }
        }
        return iSums;
    }


    /**
     *
     * @param pAccountingYear
     * @param pVouchers
     * @return
     */
    public static Map<SSAccount, BigDecimal> getDebetMinusCreditSumPlusIB(SSNewAccountingYear pAccountingYear, List<SSVoucher> pVouchers){
        Map<SSAccount, BigDecimal> iSums     = getDebetMinusCreditSum(pVouchers);
        Map<SSAccount, BigDecimal> iInBalance = pAccountingYear.getInBalance();

        for(Map.Entry<SSAccount, BigDecimal> ssAccountBigDecimalEntry : iInBalance.entrySet()){
            BigDecimal s = iSums.get(ssAccountBigDecimalEntry.getKey());

            if(s == null){
                iSums.put(ssAccountBigDecimalEntry.getKey(), ssAccountBigDecimalEntry.getValue());
            } else {
                iSums.put(ssAccountBigDecimalEntry.getKey(), s.add(ssAccountBigDecimalEntry.getValue()));
            }
        }
        return iSums;
    }



    /**
     *
     * @param iVoucher
     * @param iAccount
     * @return
     */
    public static BigDecimal getDebetSumForAccount(SSVoucher iVoucher, SSAccount iAccount){
        BigDecimal sum  = new BigDecimal(0.0);

        for(SSVoucherRow iVoucherRow: iVoucher.getRows()){
            if (iVoucherRow.isValid() && iVoucherRow.getDebet() != null && !iVoucherRow.isCrossed() && iVoucherRow.getAccount().equals(iAccount) )  {
                sum = sum.add(iVoucherRow.getDebet());
            }
        }
        return sum;
    }


    /**
     *
     * @param iVoucher
     * @param iAccount
     * @return
     */
    public static BigDecimal getCreditSumForAccount(SSVoucher iVoucher, SSAccount iAccount){
        BigDecimal sum  = new BigDecimal(0.0);

        for(SSVoucherRow iVoucherRow: iVoucher.getRows()){
            if (iVoucherRow.isValid() && iVoucherRow.getCredit() != null && !iVoucherRow.isCrossed() && iVoucherRow.getAccount().equals(iAccount) )  {
                sum = sum.add(iVoucherRow.getCredit());
            }
        }
        return sum;
    }


    /**
     *
     *
     * @return
     */
    public static int getMaxNumber() {
        return SSDB.getInstance().getLastVoucherNumber();
    }

    /**
     * Retuns if the database contains a voucher with the specified number
     *
     * @param iNumber
     * @return <code>true</code> if a voucher with the specified number exists, <code>false</code> othervise.
     */
    public static boolean hasVoucher(Integer iNumber) {
        SSVoucher iVoucher = new SSVoucher(iNumber);
        return SSDB.getInstance().getVoucher(iVoucher) != null;
    }

    /**
     * Retuns if the database contains a voucher with the specified number
     *
     * @param iVouchers
     * @param iNumber
     * @return <code>true</code> if a voucher with the specified number exists, <code>false</code> othervise.
     */
    public static boolean hasVoucher(List<SSVoucher> iVouchers, Integer iNumber) {
        for(SSVoucher iVoucher:  iVouchers){
            if(iVoucher.getNumber() == iNumber){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param iVouchers
     * @return
     */
    public static SSVoucher getFirst(List<SSVoucher> iVouchers){

        if(iVouchers.isEmpty()) return null;

        SSVoucher iCurrent = iVouchers.get(0);

        for(SSVoucher iVoucher : iVouchers){
            if( iVoucher.getNumber() < iCurrent.getNumber() ){
                iCurrent = iVoucher;
            }
        }

        return iCurrent;
    }

    /**
     *
     * @param iVouchers
     * @return
     */
    public static SSVoucher getLast(List<SSVoucher> iVouchers){

        if(iVouchers.isEmpty()) return null;

        SSVoucher iCurrent = iVouchers.get(0);

        for(SSVoucher iVoucher : iVouchers){
            if( iVoucher.getNumber() > iCurrent.getNumber() ){
                iCurrent = iVoucher;
            }
        }

        return iCurrent;
    }

    /**
     *
     * @param pVouchers
     * @param pFrom
     * @param pTo
     * @return
     */
    public static List<SSVoucher> getVouchers(List<SSVoucher> pVouchers, Date pFrom, Date pTo) {

        List<SSVoucher> iFiltered = new LinkedList<SSVoucher>();

        for(SSVoucher iVoucher : pVouchers) {
            if(inPeriod(iVoucher, pFrom, pTo)){
                iFiltered.add(iVoucher);
            }
        }


        return iFiltered;
    }





    /**
     *
     * @param iVoucherRow
     * @return
     */
    public static BigDecimal getCreditMinusDebet(SSVoucherRow iVoucherRow){
        BigDecimal debet  = iVoucherRow.getDebet () ;
        BigDecimal credit = iVoucherRow.getCredit();

        if(debet  == null) debet  = new BigDecimal(0.0);
        if(credit == null) credit = new BigDecimal(0.0);

        return credit.subtract(debet);
    }

    /**
     *
     * @param iVoucherRow
     * @return
     */
    public static BigDecimal getDebetMinusCredit(SSVoucherRow iVoucherRow){
        BigDecimal debet  = iVoucherRow.getDebet () ;
        BigDecimal credit = iVoucherRow.getCredit();

        if(debet  == null) debet  = new BigDecimal(0.0);
        if(credit == null) credit = new BigDecimal(0.0);

        return debet.subtract(credit);
    }


    /**
     * Set the debet - credit for the row such as:
     *
     * if value > 0
     *      debet = value
     * else
     *      credit = value
     *
     * @param iVoucherRow
     * @param iValue
     */
    public static void setDebetMinusCredit(SSVoucherRow iVoucherRow, BigDecimal iValue) {
        if( iValue == null ) return;

        if( iValue.signum() > 0){
            iVoucherRow.setDebet(iValue);
        }   else {
            iVoucherRow.setCredit(iValue.negate());
        }
    }


    /**
     * Set the credit - debey for the row such as:
     *
     * if value > 0
     *      credit = value
     * else
     *      debet = value
     *
     * @param iVoucherRow
     * @param iValue
     */
    public static void setCreditMinusDebet(SSVoucherRow iVoucherRow, BigDecimal iValue) {
        if( iValue == null ) return;

        if( iValue.signum() > 0){
            iVoucherRow.setCredit(iValue);
        }   else {
            iVoucherRow.setDebet(iValue.negate());
        }
    }


    /**
     * Returns true if the voucher contains the supplied account
     *
     * @param pVoucher
     * @param pAccount
     * @return if the voucher has the account
     */
    public static boolean hasAccount(SSVoucher pVoucher, SSAccount pAccount) {

        for(SSVoucherRow iVoucherRow : pVoucher.getRows()){
            SSAccount iAccount = iVoucherRow.getAccount();
            if(iAccount != null && iAccount.equals(pAccount)){
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param iVoucher
     * @param iOperand
     */
    public static void multiplyRowsBy(SSVoucher iVoucher, BigDecimal iOperand) {

        for(SSVoucherRow iVoucherRow : iVoucher.getRows()){

            BigDecimal iDebet  = iVoucherRow.getDebet();
            BigDecimal iCredit = iVoucherRow.getCredit();

            if(iDebet  != null) iDebet  = iDebet .multiply(iOperand);
            if(iCredit != null) iCredit = iCredit.multiply(iOperand);

            iVoucherRow.setDebet (iDebet);
            iVoucherRow.setCredit(iCredit);
        }

    }


    /**
     *
     * @param iVoucher
     * @param iAccount
     * @param iProject
     * @param iResultunit
     * @return
     */
    public static SSVoucherRow findRow(SSVoucher iVoucher, SSAccount iAccount, SSNewProject iProject, SSNewResultUnit iResultunit){

        for (SSVoucherRow iRow : iVoucher.getRows()) {
            SSAccount    iRowAccount    = iRow.getAccount();
            SSNewProject    iRowProject    = iRow.getProject();
            SSNewResultUnit iRowResultUnit = iRow.getResultUnit();

            if( (iAccount    == iRowAccount    || (iAccount    != null && iAccount   .equals(iRowAccount   ))) &&
                (iProject    == iRowProject    || (iProject    != null && iProject   .equals(iRowProject   ))) &&
                (iResultunit == iRowResultUnit || (iResultunit != null && iResultunit.equals(iRowResultUnit)))  ){
                return iRow;
            }
       }
        return null;
    }

    /**
     *
     * @param iVoucher
     * @return
     */
    public static SSVoucher compress(SSVoucher iVoucher){
        SSVoucher iNew = new SSVoucher();

        iNew.setNumber      ( iVoucher.getNumber()      );
        iNew.setDescription ( iVoucher.getDescription() );
        iNew.setDate        ( iVoucher.getDate()        );
        iNew.setCorrects    ( iVoucher.getCorrects()    );
        iNew.setCorrectedBy ( iVoucher.getCorrectedBy() );

        for (SSVoucherRow iRow : iVoucher.getRows()) {
            SSVoucherRow iCurrent = findRow(iNew, iRow.getAccount(), iRow.getProject(), iRow.getResultUnit());

            if(iCurrent == null){
                iNew.addVoucherRow(new SSVoucherRow(iRow) );
            } else {
                BigDecimal iCredit = iRow.getCredit();
                BigDecimal iDebet  = iRow.getDebet();

                BigDecimal iValue = iCurrent.getValue();

                if(iDebet   != null) iCurrent.setValue( iValue.add     (iDebet)  );
                if(iCredit  != null) iCurrent.setValue( iValue.subtract(iCredit)  );
            }

        }

        return iNew;
    }

    /**
     * Returns the latest voucher if any, otherwise null.
     *
     * @return The previous voucher.
     */
    public static Date getNextVoucherDate() {
        List<SSVoucher> iVouchers = new LinkedList<SSVoucher>(SSDB.getInstance().getVouchers());

        Collections.sort(iVouchers, new Comparator<SSVoucher>() {
            public int compare(SSVoucher iVoucher1, SSVoucher iVoucher2) {
                Date iDate1 = iVoucher1.getDate();
                Date iDate2 = iVoucher2.getDate();

                if(iDate1 == null || iDate2 == null) return 0;

                return iDate2.compareTo(iDate1);
            }
        });
        if(!iVouchers.isEmpty()){
            Date iDate = iVouchers.get(0).getDate();
            if(iDate != null) return iDate;
        }

        SSNewAccountingYear iAccountingYear = SSDB.getInstance().getCurrentYear();

        if( iAccountingYear != null && iAccountingYear.getFrom() != null) return iAccountingYear.getFrom();

        return new Date();
    }




    /**
     *
     * @param iFrom
     * @param iTo
     * @param iReverse
     */
    public static void copyRows(SSVoucher iFrom, SSVoucher iTo, boolean iReverse) {
        List<SSVoucherRow> iRows = iFrom.getRows();

        List<SSVoucherRow> iNew = new LinkedList<SSVoucherRow>();
        for (SSVoucherRow iRow : iRows) {

            if(iRow.isCrossed() ) continue;

            SSVoucherRow iNewRow = new SSVoucherRow(iRow, iReverse);

            iNewRow.setAdded(false);
            iNewRow.setEditedSignature(null);
            iNewRow.setEditedDate(null);

            iNew.add( iNewRow );
        }
        iTo.setVoucherRows(iNew);
    }
}





