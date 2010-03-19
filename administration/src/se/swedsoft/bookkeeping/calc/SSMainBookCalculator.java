package se.swedsoft.bookkeeping.calc;

import se.swedsoft.bookkeeping.calc.math.SSAccountMath;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.calc.util.SSCalculatorException;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.math.BigDecimal;
import java.util.*;

/**
 * Date: 2006-feb-17
 * Time: 10:34:32
 */
public class SSMainBookCalculator {



    public static class SSMainBookRow {

        private Boolean      iHasdata;

        private SSAccount    iAccount;

        private Integer      iNumber;

        private String       iDescription;

        private Date         iDate;

        private Boolean      iCrossed;

        private Boolean      iAdded;

        private BigDecimal   iDebet;

        private BigDecimal   iCredit;

        private BigDecimal   iSum;



        public Boolean getHasdata() {
            return iHasdata;
        }

        public SSAccount getAccount() {
            return iAccount;
        }

        public Integer getNumber() {
            return iNumber;
        }

        public String getDescription() {
            return iDescription;
        }

        public Date getDate() {
            return iDate;
        }


        public BigDecimal getDebet() {
            return iDebet;
        }

        public BigDecimal getCredit() {
            return iCredit;
        }

        public BigDecimal getSum() {
            return iSum;
        }

        public Boolean isAdded() {
            return iAdded;
        }

        public void setAdded(Boolean iAdded) {
            this.iAdded = iAdded;
        }

        public Boolean isCrossed() {
            return iCrossed;
        }

        public void setCrossed(Boolean iCrossed) {
            this.iCrossed = iCrossed;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("se.swedsoft.bookkeeping.calc.SSMainBookCalculator.SSMainBookRow");
            sb.append("{iAccount=").append(iAccount);
            sb.append(", iAdded=").append(iAdded);
            sb.append(", iCredit=").append(iCredit);
            sb.append(", iCrossed=").append(iCrossed);
            sb.append(", iDate=").append(iDate);
            sb.append(", iDebet=").append(iDebet);
            sb.append(", iDescription='").append(iDescription).append('\'');
            sb.append(", iHasdata=").append(iHasdata);
            sb.append(", iNumber=").append(iNumber);
            sb.append(", iSum=").append(iSum);
            sb.append('}');
            return sb.toString();
        }
    }



    private SSNewAccountingYear iYearData;

    private  SSAccount iAccountFrom;
    private  SSAccount iAccountTo;

    private Date iDateFrom;
    private Date iDateTo;

    private SSNewProject iProject;
    private SSNewResultUnit iResultUnit;

    private List<SSMainBookRow> iRows;

    private Map<SSAccount, BigDecimal> iInBalance;

    private Map<SSAccount, BigDecimal> iInSaldo;


    /**
     *
     * @param pAccountFrom
     * @param pAccountTo
     * @param pDateFrom
     * @param pDateTo
     * @param iProject
     * @param iResultUnit
     */
    public SSMainBookCalculator(SSAccount pAccountFrom, SSAccount pAccountTo, Date pDateFrom, Date pDateTo, SSNewProject iProject, SSNewResultUnit iResultUnit){
        this( SSDB.getInstance().getCurrentYear(), pAccountFrom, pAccountTo, pDateFrom, pDateTo, iProject, iResultUnit );
    }

    /**
     *
     * @param pYearData
     * @param pAccountFrom
     * @param pAccountTo
     * @param pDateFrom
     * @param pDateTo
     * @param iProject
     * @param iResultUnit
     */
    public SSMainBookCalculator(SSNewAccountingYear pYearData, SSAccount pAccountFrom, SSAccount pAccountTo, Date pDateFrom, Date pDateTo, SSNewProject iProject, SSNewResultUnit iResultUnit){
        iYearData  = pYearData;
        iAccountFrom    = pAccountFrom;
        iAccountTo      = pAccountTo;
        iDateFrom       = pDateFrom;
        iDateTo         = pDateTo;
        this.iProject    = iProject;
        this.iResultUnit = iResultUnit;

        iRows         = new LinkedList<SSMainBookRow>();
        iInBalance    = new HashMap<SSAccount, BigDecimal>();
        iInSaldo      = new HashMap<SSAccount, BigDecimal>();
    }



    /**
     *
     * @throws SSCalculatorException
     */
    public void calculate() throws SSCalculatorException {

        List<SSVoucher> iVouchers = iYearData.getVouchers();
        //      List<SSAccount> iAccounts = iYearData.getAccounts();

        iInBalance = iYearData.getInBalance();

        // Add the inbalance to the insaldo
        for(Map.Entry<SSAccount, BigDecimal> ssAccountBigDecimalEntry : iInBalance.entrySet()){
            iInSaldo.put(ssAccountBigDecimalEntry.getKey(), ssAccountBigDecimalEntry.getValue());
        }


        for(SSVoucher iVoucher: iVouchers){

            // If the date of the voucer is before the start date, add to InSaldo
            boolean inSaldo  = iDateFrom.compareTo(iVoucher.getDate()) > 0;

            // If the date of the oucher is in between the start and end date, add to PeriodChange
            boolean inPeriod =  SSVoucherMath.inPeriod(iVoucher, iDateFrom, iDateTo);

            // Loop through all the rows
            for(SSVoucherRow iVoucherRow: iVoucher.getRows()){
                SSAccount    iAccount       = iVoucherRow.getAccount();

                SSNewProject    iRowProject    = iVoucherRow.getProject();
                SSNewResultUnit iRowResultUnit = iVoucherRow.getResultUnit();

                // Skip the row if invalid
                if( !iVoucherRow.isValid()  ) continue;


                if( iProject    != null && !iProject.equals(iRowProject)) continue;
                if( iResultUnit != null && !iResultUnit.equals(iRowResultUnit)) continue;

                BigDecimal iRowSaldo = SSVoucherMath.getDebetMinusCredit(iVoucherRow);

                // Add the row sum to the inSaldo, if in range
                if (inSaldo && !iVoucherRow.isCrossed()){
                    addValueToMap(iInSaldo , iAccount, iRowSaldo);
                }

                // Add the row if the account and date is in the period
                if(SSAccountMath.inPeriod(iAccount, iAccountFrom, iAccountTo) && inPeriod ){
                    SSMainBookRow iMainBookRow = new SSMainBookRow();

                    iMainBookRow.iHasdata = true;
                    iMainBookRow.iAccount = iAccount;

                    iMainBookRow.iNumber      = iVoucher.getNumber();
                    iMainBookRow.iDescription = iVoucher.getDescription();
                    iMainBookRow.iDate        = iVoucher.getDate();

                    iMainBookRow.iAdded       = iVoucherRow.isAdded();
                    iMainBookRow.iCrossed     = iVoucherRow.isCrossed();
                    iMainBookRow.iDebet       = iVoucherRow.getDebet();
                    iMainBookRow.iCredit      = iVoucherRow.getCredit();
                    iMainBookRow.iSum        =  iRowSaldo;
                    iRows.add( iMainBookRow );

                }
            }
        }

        for(SSAccount iAccount : iInSaldo.keySet() ){
            if( (iInSaldo.containsKey(iAccount) &&  iInSaldo.get(iAccount).signum() != 0) || (iInBalance.containsKey(iAccount) &&  iInBalance.get(iAccount).signum() != 0) ){

                if(SSAccountMath.inPeriod(iAccount, iAccountFrom, iAccountTo) ){
                    SSMainBookRow iMainBookRow = new SSMainBookRow();

                    iMainBookRow.iHasdata = false;
                    iMainBookRow.iAccount = iAccount;

                    iMainBookRow.iAdded   = false;
                    iMainBookRow.iCrossed = false;
                    iMainBookRow.iDebet   = new BigDecimal(0);
                    iMainBookRow.iCredit  = new BigDecimal(0);
                    iMainBookRow.iSum     = new BigDecimal(0);

                    iRows.add( iMainBookRow );
                }
            }
        }

    }



    /**
     *
     * @param iMap
     * @param iAccount
     * @param iValue
     */
    private void addValueToMap(Map<SSAccount, BigDecimal> iMap, SSAccount iAccount, BigDecimal iValue){
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
     * @return
     */
    public List<SSMainBookRow> getRows() {
        return iRows;
    }

    /**
     *
     * @return
     */
    public Map<SSAccount, BigDecimal> getInBalance() {
        return iInBalance;
    }

    /**
     *
     * @return
     */
    public Map<SSAccount, BigDecimal> getInSaldo() {
        return iInSaldo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.calc.SSMainBookCalculator");
        sb.append("{iAccountFrom=").append(iAccountFrom);
        sb.append(", iAccountTo=").append(iAccountTo);
        sb.append(", iDateFrom=").append(iDateFrom);
        sb.append(", iDateTo=").append(iDateTo);
        sb.append(", iInBalance=").append(iInBalance);
        sb.append(", iInSaldo=").append(iInSaldo);
        sb.append(", iProject=").append(iProject);
        sb.append(", iResultUnit=").append(iResultUnit);
        sb.append(", iRows=").append(iRows);
        sb.append(", iYearData=").append(iYearData);
        sb.append('}');
        return sb.toString();
    }
}
