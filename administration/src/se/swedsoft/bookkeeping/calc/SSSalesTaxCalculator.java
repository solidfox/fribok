package se.swedsoft.bookkeeping.calc;

import se.swedsoft.bookkeeping.calc.math.SSAccountMath;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.calc.util.SSCalculatorException;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Date: 2006-mar-09
 * Time: 10:35:03
 */
public class SSSalesTaxCalculator {


    private SSNewAccountingYear iAccountingYear;

    private Date iDateFrom;

    private Date iDateTo;

    private List<SSVATReportGroup> iReportGroups;

    private  List<SSVATControlGroup> iControlGroups;



    /**
     *
     * @param pAccountingYear
     * @param pDateFrom
     * @param pDateTo
     */
    public SSSalesTaxCalculator(SSNewAccountingYear pAccountingYear, Date pDateFrom, Date pDateTo) {
        iAccountingYear = pAccountingYear;
        iDateFrom       = pDateFrom;
        iDateTo         = pDateTo;
    }


    /**
     *
     * @throws SSCalculatorException
     */
    public void calculate() throws SSCalculatorException {
        iReportGroups   = createReportGroups();
        iControlGroups  = createControlGroups();

        // Get all vouchers
        List<SSVoucher> iVouchers = SSVoucherMath.getVouchers(iAccountingYear.getVouchers(), iDateFrom, iDateTo);

       // System.out.println("Momsrapport, verifikationer:");
       // for(SSVoucher iVoucher : iVouchers){
        //    System.out.println(iVoucher);
        //}

        Map<SSAccount, BigDecimal> creditMinusDebetSum = SSVoucherMath.getCreditMinusDebetSum(iVouchers);
        Map<SSAccount, BigDecimal> debetMinusCreditSum = SSVoucherMath.getDebetMinusCreditSum(iVouchers);


        for(SSVATReportGroup iGroup : iReportGroups){
            BigDecimal iSum = null;

            switch( iGroup.getGroup2() ){

                case 11: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "MP1", "MP2", "MP3").add( SSAccountMath.getSumByVATCodeForAccounts(debetMinusCreditSum, "TFEU") ); break;
                case 12: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "MF"); break;
                case 13: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "MPFF"); break;
                case 14: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "MBVT"); break;
                case 15: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "MBBU"); break;

                case 21: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "E"); break;
                case 22: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "OTTU"); break;
                case 23: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "VTEU"); break;
                case 24: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "ÖVEU"); break;
                case 25: iSum = SSAccountMath.getSumByVATCodeForAccounts(debetMinusCreditSum, "VFEU"); break;
                case 26: iSum = SSAccountMath.getSumByVATCodeForAccounts(debetMinusCreditSum, "3VEU"); break;

                case 30: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "UVL"); break;
                case 31: iSum = SSAccountMath.getSumByVATCodeForAccounts(debetMinusCreditSum, "IVL"); break;

                case 32: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "U1", "UVL"); break;
                case 33: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "U2"); break;
                case 34: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "U3"); break;
                case 35: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "UEU"); break;
                case 36: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "UTFU"); break;
                case 37: iSum = SSAccountMath.getSumByVATCodeForAccounts(debetMinusCreditSum, "I", "IVL"); break;
            }
            iGroup.setSum(iSum);
        }


        for(SSVATControlGroup iGroup : iControlGroups){
            BigDecimal iSum      = null;


            switch( iGroup.getGroup1() ){
                case 1: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "MP1", "MPFF"); break;
                case 2: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "MP2"); break;
                case 3: iSum = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "MP3"); break;
            }

            BigDecimal iReported = null;
            switch( iGroup.getGroup1() ){
                case 1: iReported = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "U1", "UVL"); break;
                case 2: iReported = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "U2"); break;
                case 3: iReported = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "U3"); break;
            }

            iGroup.setSum(iSum);
            iGroup.setReported(iReported);
        }



    }




    /**
     *
     * @return
     */
    public List<SSVATReportGroup> getReportGroups() {
        return iReportGroups;
    }

    /**
     *
     * @return
     */
    public List<SSVATControlGroup> getControlGroups(){
        return iControlGroups;
    }



    /**
     *
     * @return
     */
    private List<SSVATReportGroup> createReportGroups(){
        List<SSVATReportGroup> iReportGroups = new LinkedList<SSVATReportGroup>();

        // Add the groups
        iReportGroups.add( new SSVATReportGroup(1, 11) );
        iReportGroups.add( new SSVATReportGroup(1, 12) );
        iReportGroups.add( new SSVATReportGroup(1, 13) );
        iReportGroups.add( new SSVATReportGroup(1, 14) );
        iReportGroups.add( new SSVATReportGroup(1, 15) );

        iReportGroups.add( new SSVATReportGroup(2, 21) );
        iReportGroups.add( new SSVATReportGroup(2, 22) );
        iReportGroups.add( new SSVATReportGroup(2, 23) );
        iReportGroups.add( new SSVATReportGroup(2, 24) );
        iReportGroups.add( new SSVATReportGroup(2, 25) );
        iReportGroups.add( new SSVATReportGroup(2, 26) );

        iReportGroups.add( new SSVATReportGroup(3, 30) );
        iReportGroups.add( new SSVATReportGroup(3, 31) );

        iReportGroups.add( new SSVATReportGroup(4, 32) );
        iReportGroups.add( new SSVATReportGroup(4, 33) );
        iReportGroups.add( new SSVATReportGroup(4, 34) );
        iReportGroups.add( new SSVATReportGroup(4, 35) );
        iReportGroups.add( new SSVATReportGroup(4, 36) );
        iReportGroups.add( new SSVATReportGroup(4, 37) );

        return iReportGroups;
    }

    /**
     *
     * @return
     */
    private List<SSVATControlGroup> createControlGroups(){
        List<SSVATControlGroup> iControlGroups = new LinkedList<SSVATControlGroup>();

// Add the three groups
        iControlGroups.add( new SSVATControlGroup(1) );
        iControlGroups.add( new SSVATControlGroup(2) );
        iControlGroups.add( new SSVATControlGroup(3) );

        return iControlGroups;
    }




    /**
     *
     */
    public static class SSVATReportGroup{

        private int iGroup1;

        private int iGroup2;

        private BigDecimal iSum;



        /**
         * Default constructor.
         * @param pGroup1
         * @param pGroup2
         */
        public SSVATReportGroup(int pGroup1, int pGroup2){
            iGroup1 = pGroup1;
            iGroup2 = pGroup2;
            iSum    = null;
        }


        public int getGroup1() {
            return iGroup1;
        }

        public void setGroup1(int iGroup1) {
            this.iGroup1 = iGroup1;
        }

        public int getGroup2() {
            return iGroup2;
        }

        public void setGroup2(int iGroup2) {
            this.iGroup2 = iGroup2;
        }



        /**
         *
         * @return
         */
        public BigDecimal getSum() {
            return iSum;
        }

        /**
         *
         * @param iSum
         */
        public void setSum(BigDecimal iSum) {
            this.iSum = iSum;
        }

        /**
         * @return String
         */
        public String getGroup1Description(){
            return SSBundle.getBundle().getString("vatreport.group.1." + iGroup1 );
        }

        /**
         * @return String
         */
        public String getGroup2Description(){
            return SSBundle.getBundle().getString("vatreport.group.2." + iGroup2 );
        }
    }




    public static class SSVATControlGroup{

        private BigDecimal iSum;

        private BigDecimal iReported;

        private int iGroup1;


        /**
         * Default constructor.
         * @param pGroup1
         */
        public SSVATControlGroup(int pGroup1){
            iGroup1 = pGroup1;
            iSum    = null;
        }

        /**
         * @return
         */
        public int getGroup1(){
            return iGroup1;
        }

        /**
         *
         * @param iGroup1
         */
        public void setGroup1(int iGroup1) {
            this.iGroup1 = iGroup1;
        }

        /**
         *
         * @return
         */
        public BigDecimal getSum() {
            return iSum;
        }

        /**
         *
         * @param iSum
         */
        public void setSum(BigDecimal iSum) {
            this.iSum = iSum;
        }

        public BigDecimal getReported() {
            return iReported;
        }

        public void setReported(BigDecimal iReported) {
            this.iReported = iReported;
        }

        /**
         * @return String
         */
        public String getDescription(){
            return SSBundle.getBundle().getString("vatcontrolreport.group.1." + iGroup1 );
        }
    }


}
