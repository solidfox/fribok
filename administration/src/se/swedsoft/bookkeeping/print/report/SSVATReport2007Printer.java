package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Date: 2006-mar-02
 * Time: 16:45:12
 */
public class SSVATReport2007Printer extends SSPrinter {

    private SSNewAccountingYear iAccountingYear;

    private Date iDateFrom;

    private Date iDateTo;



    private SSVATReportRowPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;


    private List<SSAccount> iAccounts;

    private Map<String, List<SSAccount>> iAccountsByVatCode;

    private Map<SSAccount, BigDecimal> iCreditMinusDebetSum;

    private Map<SSAccount, BigDecimal> iDebetMinusCreditSum;

    /**
     *
     * @param iAccountingYear
     * @param iDateFrom
     * @param iDateTo
     */
    public SSVATReport2007Printer(SSNewAccountingYear iAccountingYear, Date iDateFrom, Date iDateTo ){
        this.iAccountingYear   = iAccountingYear;
        this.iDateFrom       = iDateFrom;
        this.iDateTo         = iDateTo;
        this.iAccounts       = iAccountingYear.getAccounts();

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("vatreport2007.jrxml");
        setDetail      ("vatreport2007.jrxml");

        calculate();
    }



    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("vatreport2007.title");
    }

    /**
     *
     */
    private void calculate(){
        // Get all vouchers
        List<SSVoucher> iVouchers = SSVoucherMath.getVouchers(iAccountingYear.getVouchers(), iDateFrom, iDateTo);

        iCreditMinusDebetSum = SSVoucherMath.getCreditMinusDebetSum(iVouchers);
        iDebetMinusCreditSum = SSVoucherMath.getDebetMinusCreditSum(iVouchers);

        iAccountsByVatCode   = new HashMap<String, List<SSAccount>>();

        for (SSAccount iAccount : iAccounts) {
            String iVATCode = iAccount.getVATCode();

            List<SSAccount> iAccountsForVatCode = iAccountsByVatCode.get(iVATCode);
            if(iAccountsForVatCode == null){
                iAccountsForVatCode = new LinkedList<SSAccount>();

                iAccountsByVatCode.put(iVATCode, iAccountsForVatCode);
            }
            iAccountsForVatCode.add(iAccount);
        }
    }



    /**
     *
     * @param group
     * @return
     */
    private String getVATCodesForGroup(Integer group){
        switch(group){
            //  A. Momspliktig försäljning eller utag exklusive moms
            case 5: return "MP1, MP2, MP3, PTOG";
            case 6: return "MU1, MU2, MU3";
            case 7: return "MBBU";
            case 8: return "MPFF";

            // B. Utgående moms på försäljning eller uttag i ruta 5-8
            case 10: return "U1, UVL";
            case 11: return "U2";
            case 12: return "U3";

            // C. Momspliktiga inköp där köparen är skalleskylding.
            case 20: return "VFEU";
            case 21: return "TFEU";
            case 22: return "TFFU";
            case 23: return "IVIS";
            case 24: return "ITIS";

            // D. Utgående moms på inköp i ruta 20 - 24
            case 30: return "U1MI, UEU, UTFU";
            case 31: return "U2MI";
            case 32: return "U3MI";

            // E. Försäljning m.m. som är undantagen från moms.
            case 35: return "VTEU, ÖVEU";
            case 36: return "E";
            case 37: return "3VEU";
            case 38: return "3FEU";
            case 39: return "FTEU";
            case 40: return "OTTU";
            case 41: return "OMSS";
            case 42: return "MF";

            // F. Ingående moms
            case 48: return "I, IVL";

            // G. Moms att betala eller få tillbaka.
            case 49: return "R1, R2";
        }
        return null;
    }

    /**
     *
     * @param group
     * @return
     */
    private BigDecimal getValueForGroup(Integer group){

        switch(group){
            //  A. Momspliktig försäljning eller utag exklusive moms
            case 5: return getSumForAccounts(iCreditMinusDebetSum, "MP1", "MP2", "MP3", "PTOG");
            case 6: return getSumForAccounts(iCreditMinusDebetSum, "MU1", "MU2", "MU3");
            case 7: return getSumForAccounts(iCreditMinusDebetSum, "MBBU");
            case 8: return getSumForAccounts(iCreditMinusDebetSum, "MPFF");

                // B. Utgående moms på försäljning eller uttag i ruta 5-8

            case 10: return getSumForAccounts(iCreditMinusDebetSum, "U1", "UVL");
            case 11: return getSumForAccounts(iCreditMinusDebetSum, "U2");
            case 12: return getSumForAccounts(iCreditMinusDebetSum, "U3");

                // C. Momspliktiga inköp där köparen är skatteskyldig.

            case 20: return getSumForAccounts(iDebetMinusCreditSum, "VFEU");
            case 21: return getSumForAccounts(iDebetMinusCreditSum, "TFEU");
            case 22: return getSumForAccounts(iDebetMinusCreditSum, "TFFU");
            case 23: return getSumForAccounts(iDebetMinusCreditSum, "IVIS");
            case 24: return getSumForAccounts(iDebetMinusCreditSum, "ITIS");

                // D. Utgående moms på inköp i ruta 20 - 24
            case 30: return getSumForAccounts(iCreditMinusDebetSum, "U1MI", "UEU", "UTFU");
            case 31: return getSumForAccounts(iCreditMinusDebetSum, "U2MI");
            case 32: return getSumForAccounts(iCreditMinusDebetSum, "U3MI");

                // E. Försäljning m.m. som är undantagen från moms.
            case 35: return getSumForAccounts(iCreditMinusDebetSum, "VTEU", "ÖVEU");
            case 36: return getSumForAccounts(iCreditMinusDebetSum, "E");
            case 37: return getSumForAccounts(iDebetMinusCreditSum, "3VEU");
            case 38: return getSumForAccounts(iCreditMinusDebetSum, "3FEU");
            case 39: return getSumForAccounts(iCreditMinusDebetSum, "FTEU");
            case 40: return getSumForAccounts(iCreditMinusDebetSum, "OTTU");
            case 41: return getSumForAccounts(iCreditMinusDebetSum, "OMSS");
            case 42: return getSumForAccounts(iCreditMinusDebetSum, "MF");

                // F. Ingående moms
            case 48: return getSumForAccounts(iDebetMinusCreditSum, "I", "IVL");

                // G. Moms att betala eller få tillbaka.
            case 49:
                BigDecimal iSum = new  BigDecimal(0);

                iSum = iSum.add(   getValueForGroup(10) );
                iSum = iSum.add(   getValueForGroup(11) );
                iSum = iSum.add(   getValueForGroup(12) );

                iSum = iSum.add(   getValueForGroup(30) );
                iSum = iSum.add(   getValueForGroup(31) );
                iSum = iSum.add(   getValueForGroup(32) );

                iSum = iSum.subtract(   getValueForGroup(48) );

                return iSum;

        }
        return new BigDecimal(0);
    }



    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
        addParameter("dateFrom", iDateFrom );
        addParameter("dateTo"  , iDateTo);

        iPrinter = new SSVATReportRowPrinter();
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());


        SSDefaultTableModel<String> iModel = new SSDefaultTableModel<String>() {

            @Override
            public Class getType() {
                return String.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                String iNumber = getObject(rowIndex);

                Object value = null;

                switch (columnIndex) {
                    case 0:
                        value = iNumber;
                        break;
                    case 1:
                        value = SSBundle.getBundle().getString("vatreport2007.group." + iNumber);
                        break;
                    case 2:
                        iPrinter.setGroup( iNumber.charAt(0) );

                        iDataSource.reset();

                        value = iDataSource;
                        break;

                }
                return value;
            }
        };
        iModel.addColumn("group.number");
        iModel.addColumn("group.description");
        iModel.addColumn("group.rows");

        iModel.add("A");
        iModel.add("B");
        iModel.add("C");
        iModel.add("D");
        iModel.add("E");
        iModel.add("F");
        iModel.add("G");

        return iModel;
    }


    private class SSVATReportRowPrinter extends SSPrinter {

        private SSDefaultTableModel<Integer> iModel;

        /**
         *
         */
        public SSVATReportRowPrinter( ){
            setMargins(0,0,0,0);

            setDetail ("vatreport2007.rows.jrxml");

            iModel = new SSDefaultTableModel<Integer>(  ) {

                @Override
                public Class getType() {
                    return Integer.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    Integer iNumber = getObject(rowIndex);

                    switch (columnIndex) {
                        case 0:
                            value = iNumber;
                            break;
                        case 1:
                            value = SSBundle.getBundle().getString("vatreport2007.group." + iNumber);
                            break;
                        case 2:
                            value = getVATCodesForGroup(iNumber);
                            break;
                        case 3:
                            value = getValueForGroup(iNumber).setScale(0, RoundingMode.DOWN);
                            break;
                    }

                    return value;
                }
            };

            iModel.addColumn("group.number");
            iModel.addColumn("group.description");
            iModel.addColumn("group.vatcodes");
            iModel.addColumn("group.value");
        }

        /**
         * Gets the data model for this report
         *
         * @return SSDefaultTableModel
         */
        @Override
        protected SSDefaultTableModel getModel() {
            return iModel;
        }

        /**
         * Gets the title for this report
         *
         * @return The title
         */
        @Override
        public String getTitle() {
            return null;
        }

        /**
         *
         * @param iNumber
         */
        public void setGroup(char iNumber){
            List<Integer> iObjects = new LinkedList<Integer>();
            switch(iNumber){
                case 'A':
                    iObjects.add(5);
                    iObjects.add(6);
                    iObjects.add(7);
                    iObjects.add(8);
                    break;
                case 'B':
                    iObjects.add(10);
                    iObjects.add(11);
                    iObjects.add(12);
                    break;
                case 'C':
                    iObjects.add(20);
                    iObjects.add(21);
                    iObjects.add(22);
                    iObjects.add(23);
                    iObjects.add(24);
                    break;
                case 'D':
                    iObjects.add(30);
                    iObjects.add(31);
                    iObjects.add(32);
                    break;
                case 'E':
                    iObjects.add(35);
                    iObjects.add(36);
                    iObjects.add(37);
                    iObjects.add(38);
                    iObjects.add(39);
                    iObjects.add(40);
                    iObjects.add(41);
                    iObjects.add(42);
                    break;
                case 'F':
                    iObjects.add(48);
                    break;
                case 'G':
                    iObjects.add(49);
                    break;
            }
            iModel.setObjects( iObjects );
        }
    }




    /**
     *
     * @param iSums
     * @param iVATCodes
     * @return
     */
    private BigDecimal getSumForAccounts(Map<SSAccount, BigDecimal> iSums, String ... iVATCodes){
        BigDecimal iSum = new BigDecimal(0);
        for (SSAccount iAccount : iAccounts) {

            if( hasVATCode(iAccount,  iVATCodes) ) {
                BigDecimal iSumForAccount = iSums.get(iAccount);

                if(iSumForAccount != null) iSum = iSum.add(iSumForAccount.setScale(0,RoundingMode.DOWN));
            }


        }
        return iSum;
    }

    /**
     *
     * @param iAccount
     * @param iVATCodes
     * @return
     */
    private boolean hasVATCode(SSAccount iAccount, String ... iVATCodes ){

        String iVATCodeForAccount = iAccount.getVATCode();

        for (String iVATCode : iVATCodes) {

            if( iVATCode.equals( iVATCodeForAccount )){

                return true;
            }
        }
        return false;
    }






}
