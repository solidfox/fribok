package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.data.SSVoucherRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.calc.math.SSAccountMath;

import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;

/**
 * Date: 2006-mar-02
 * Time: 16:45:12
 */
public class SSVATControl2007Printer extends SSPrinter {

    private SSNewAccountingYear iAccountingYear;

    private Date iDateFrom;

    private Date iDateTo;



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
    public SSVATControl2007Printer(SSNewAccountingYear iAccountingYear, Date iDateFrom, Date iDateTo ){
        super();
        this.iAccountingYear   = iAccountingYear;
        this.iDateFrom       = iDateFrom;
        this.iDateTo         = iDateTo;
        this.iAccounts       = iAccountingYear.getAccounts();

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("vatcontrol2007.jrxml");
        setDetail      ("vatcontrol2007.jrxml");
        setSummary     ("vatcontrol2007.jrxml");

        calculate();
    }



    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("vatcontrol2007.title");
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
     * @param iAccountR1
     * @param iAccountR2
     * @param iAccountA
     * @return
     */
    public SSVoucher getVoucher(SSAccount iAccountR1, SSAccount iAccountR2, SSAccount iAccountA){
        //  DateFormat iFormat =
        DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        String iDescription = String.format(SSBundle.getBundle().getString("vatreport2007.voucherdescription"), iFormat.format(iDateFrom), iFormat.format(iDateTo));

        List<SSAccount> iAccounts = SSAccountMath.getAccountsByVATCode( SSDB.getInstance().getAccounts(), "U1", "U2", "U3", "UVL", "UEU", "UTFU", "U1MI", "U2MI", "U3MI", "I", "IVL");

        SSVoucher    iVoucher = new SSVoucher();
        SSVoucherRow iRow;

        iVoucher.doAutoIncrecement();
        iVoucher.setDescription( iDescription );
        iVoucher.setDate( iDateTo );

        BigDecimal iSum        = new BigDecimal(0);
        BigDecimal iRoundedSum = new BigDecimal(0);
        for (SSAccount iAccount : iAccounts) {

            BigDecimal iValue = iCreditMinusDebetSum.get(iAccount);

            if(iValue == null || iValue.signum() == 0) continue;

            iRow = new SSVoucherRow();
            iRow.setAccount(iAccount);
            iRow.setValue(iValue);

            iVoucher.addVoucherRow(iRow);

            iSum  = iSum.add(iValue);
            iRoundedSum = iRoundedSum.add(iValue.setScale(0,RoundingMode.DOWN));
        }

        if(iSum.signum() != 0){
            iRow = new SSVoucherRow();

            //BigDecimal iRounded = iSum.setScale(0, RoundingMode.DOWN);

            if(iRoundedSum.signum() > 0){
                iRow.setAccount( iAccountR2);
                iRow.setCredit ( iRoundedSum   );
            }  else {
                iRow.setAccount( iAccountR1);
                iRow.setDebet  ( iRoundedSum.abs() );
            }

            iVoucher.addVoucherRow(iRow);

            if(iRoundedSum.subtract(iSum).signum() != 0){
                iRow = new SSVoucherRow();
                iRow.setAccount(iAccountA);
                iRow.setValue( iRoundedSum.subtract(iSum) );

                iVoucher.addVoucherRow(iRow);
            }
        }




        return iVoucher;
    }


    /**
     *
     * @param group
     */
    private BigDecimal getValueForGroup(Integer group){

        switch(group){
            case 1: return getSumForAccounts(iCreditMinusDebetSum, "MP1", "MPFF", "MBBU", "MU1");
            case 2: return getSumForAccounts(iCreditMinusDebetSum, "MP2", "MU2");
            case 3: return getSumForAccounts(iCreditMinusDebetSum, "MP3", "MU3");
        }

        return new BigDecimal(0);
    }

    /**
     *
     * @param group
     */
    private BigDecimal getTaxForGroup(Integer group){
        BigDecimal iValue = getValueForGroup(group);

        switch(group){
            case 1: return iValue.multiply( new BigDecimal(0.25));
            case 2: return iValue.multiply( new BigDecimal(0.12));
            case 3: return iValue.multiply( new BigDecimal(0.06));
        }

        return new BigDecimal(0);
    }

    /**
     *
     * @param group
     */
    private BigDecimal getReportedTaxForGroup(Integer group){

        switch(group){
            case 1: return getSumForAccounts(iCreditMinusDebetSum, "U1", "UVL");
            case 2: return getSumForAccounts(iCreditMinusDebetSum, "U2");
            case 3: return getSumForAccounts(iCreditMinusDebetSum, "U3");
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

        SSDefaultTableModel<Integer> iModel = new SSDefaultTableModel<Integer>() {

            @Override
            public Class getType() {
                return String.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Integer iNumber = getObject(rowIndex);

                Object value = null;

                switch (columnIndex) {
                    case 0:
                        value = iNumber;
                        break;
                    case 1:
                        value = SSBundle.getBundle().getString("vatcontrol2007.group." + iNumber);
                        break;
                    case 2:
                        value = getValueForGroup(iNumber);
                        break;
                    case 3:
                        value = getTaxForGroup(iNumber);
                        break;
                    case 4:
                        value = getReportedTaxForGroup(iNumber);
                        break;
                }
                return value;
            }
        };

        iModel.addColumn("group.number");
        iModel.addColumn("group.description");
        iModel.addColumn("group.turnover");
        iModel.addColumn("group.calculated");
        iModel.addColumn("group.reported");

        iModel.add(1);
        iModel.add(2);
        iModel.add(3);


        return iModel;
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

                if(iSumForAccount != null) iSum = iSum.add(iSumForAccount);
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
