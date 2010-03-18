package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.calc.math.SSOutpaymentMath;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * User: Andreas Lago
 * Date: 2006-apr-07
 * Time: 10:43:47
 */
public class SSOutpayment implements SSTableSearchable, Serializable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    // Inbetalningsnummer
    private Integer iNumber;
    // Inbetalningsdatum
    private Date iDate;
    // Text
    private String iText;
    // Rader
    private List<SSOutpaymentRow> iRows;
    // Verifikation
    private SSVoucher iVoucher;
    // Differensverifikation
    private SSVoucher iDifference;
    // Bokförd
    private boolean iEntered;

    // Standard konton
    protected Map<SSDefaultAccount, Integer> iDefaultAccounts;

    ////////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSOutpayment() {
        iRows         = new LinkedList<SSOutpaymentRow>();
        iVoucher      = new SSVoucher();
        iDifference   = new SSVoucher();
        iEntered      = false;
        iDefaultAccounts  = new HashMap<SSDefaultAccount, Integer>();
        iDefaultAccounts.putAll(SSDB.getInstance().getCurrentCompany().getDefaultAccounts());

    }


    /**
     * Copy constructor
     *
     * @param iOutpayment
     */
    public SSOutpayment(SSOutpayment iOutpayment) {
        copyFrom(iOutpayment);
    }


    /**
     * Clone constructor
     *
     * @param iOutpayment
     * @param iNumber
     */
    public SSOutpayment(SSOutpayment iOutpayment, Integer iNumber) {
        copyFrom(iOutpayment);

        this.iNumber = iNumber;
    }
    ////////////////////////////////////////////////////

    /**
     *
     * @param iOutpayment
     */
    public void copyFrom(SSOutpayment iOutpayment) {
        iNumber          = iOutpayment.iNumber;
        iDate            = iOutpayment.iDate;
        iText            = iOutpayment.iText;
        iEntered         = iOutpayment.iEntered;
        iVoucher         = new SSVoucher(iOutpayment.iVoucher);
        iDifference      = new SSVoucher(iOutpayment.iDifference);
        iRows            = new LinkedList<SSOutpaymentRow>();
        iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();

        // Copy all rows
        for(SSOutpaymentRow iRow : iOutpayment.iRows){
            iRows.add( new SSOutpaymentRow(iRow) );
        }

        // Copy all default accounts
        iDefaultAccounts.putAll( iOutpayment.getDefaultAccounts() );
    }

    ////////////////////////////////////////////////////

    /**
     * Sets the number of this voucher as the maxinum mumber + 1
     */
    public void doAutoIncrecement() {
        List<SSOutpayment> iOutpayments = SSDB.getInstance().getOutpayments();

        int iNumber = SSDB.getInstance().getAutoIncrement().getNumber("outpayment");

        for(SSOutpayment iOutpayment: iOutpayments){
            if(iOutpayment.getNumber() != null && iOutpayment.getNumber() > iNumber){
                iNumber = iOutpayment.getNumber();
            }
        }
        setNumber(iNumber + 1);
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Integer getNumber() {
        return iNumber;
    }

    /**
     *
     * @param iNumber
     */
    public void setNumber(Integer iNumber) {
        this.iNumber = iNumber;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Date getDate() {
        return iDate;
    }

    /**
     *
     * @param iDate
     */
    public void setDate(Date iDate) {
        this.iDate = iDate;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getText() {
        return iText;
    }

    /**
     *
     * @param iText
     */
    public void setText(String iText) {
        this.iText = iText;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Map<SSDefaultAccount, Integer> getDefaultAccounts() {
        if(iDefaultAccounts == null)
        {
            SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();
            if(iCompany!=null)
            {
                iDefaultAccounts = iCompany.getDefaultAccounts();
                iCompany=null;
            }
        }
        return iDefaultAccounts;
    }

    /**
     *
     * @param iAccountPlan
     * @param iDefaultAccount
     * @return
     */
    public SSAccount getDefaultAccount(SSAccountPlan iAccountPlan, SSDefaultAccount iDefaultAccount){
        Integer iAccountNumber = getDefaultAccounts().get(iDefaultAccount);

        if(iAccountNumber == null) return null;

        return iAccountPlan.getAccount(iAccountNumber);
    }

    /**
     *
     * @param iDefaultAccount
     * @return
     */
    public Integer getDefaultAccount(SSDefaultAccount iDefaultAccount){
        return iDefaultAccounts.get(iDefaultAccount);
    }

    /**
     *
     * @param iDefaultAccounts
     */
    public void setDefaultAccounts(Map<SSDefaultAccount, Integer> iDefaultAccounts) {
        this.iDefaultAccounts = iDefaultAccounts;
    }


    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSOutpaymentRow> getRows() {
        if(iRows == null) iRows = new LinkedList<SSOutpaymentRow>();
        return iRows;
    }

    /**
     *
     * @param iRows
     */
    public void setRows(List<SSOutpaymentRow> iRows) {
        this.iRows = iRows;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSVoucher getVoucher() {
        return iVoucher;
    }

    /**
     *
     * @param iVoucher
     */
    public void setVoucher(SSVoucher iVoucher) {
        this.iVoucher = iVoucher;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSVoucher getDifference() {
        if(iDifference == null) iDifference = new SSVoucher();

        return iDifference;
    }

    /**
     *
     * @param iDifference
     */
    public void setDifference(SSVoucher iDifference) {
        this.iDifference = iDifference;
    }


    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean isEntered() {
        return iEntered;
    }

    /**
     *
     * @param iEntered
     */
    public void setEntered(boolean iEntered) {
        this.iEntered = iEntered;
    }

    /**
     *
     */
    public void setEntered() {
        iEntered = true;
    }
    ////////////////////////////////////////////////////

    /**
     *
     * @param iSupplierInvoices
     */
    public void addInvoices(List<SSSupplierInvoice> iSupplierInvoices) {
        for (SSSupplierInvoice iSupplierInvoice : iSupplierInvoices) {
            SSOutpaymentRow iRow = new SSOutpaymentRow();

            iRow.setSupplierInvoice( iSupplierInvoice );

            iRows.add(iRow);
        }
    }

    public List<SSSupplierInvoice> getSupplierInvoices() {
        LinkedList<SSSupplierInvoice> iSupplierInvoices = new LinkedList<SSSupplierInvoice>();
        for (SSOutpaymentRow iRow : iRows) {
            if(iRow.getSupplierInvoice(SSDB.getInstance().getSupplierInvoices())!=null)
                iSupplierInvoices.add(iRow.getSupplierInvoice(SSDB.getInstance().getSupplierInvoices()));
        }
        return iSupplierInvoices;
    }


    ////////////////////////////////////////////////////

    /**
     * Returns true if this inpayment contains the sales, false othervise
     *
     * @param iInvoice
     *
     * @return if the inpayment contains the sales
     */
    public boolean isPaying(SSSupplierInvoice iInvoice) {
        for( SSOutpaymentRow iRow: iRows){
            if(iRow.isPaying(iInvoice)) return true;
        }
        return false;
    }



    ////////////////////////////////////////////////////

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
        StringBuilder sb = new StringBuilder();
        sb.append(iNumber);
        sb.append(", ");
        sb.append(iText);
        return sb.toString();
    }

    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return iNumber == null ? null : iNumber.toString();
    }


    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     *
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof SSOutpayment)) {
            return false;
        }
        return iNumber.equals(((SSOutpayment)obj).getNumber());
    }


    /**
     *
     * @return
     */
    public SSVoucher generateVoucher(){
        String iDescription = SSBundle.getBundle().getString("outpaymentframe.voucherdescription");

        SSAccountPlan iAccountPlan = SSDB.getInstance().getCurrentAccountPlan();

        BigDecimal iSum                     = SSOutpaymentMath.getSum(this);
        BigDecimal iCurrencyRateDifference  = SSOutpaymentMath.getCurrencyRateDifference(this);
        BigDecimal iDifferenceSum           = SSVoucherMath.getCreditMinusDebetSum(iDifference);

        iSum = iSum.add( iCurrencyRateDifference );
        iSum = iSum.add( iDifferenceSum          );

        iVoucher = new SSVoucher();
        iVoucher.setDate       ( new Date() );
        iVoucher.setNumber     ( 0  );
        iVoucher.setDescription( String.format(iDescription, iNumber) );

       // Add the sum
        iVoucher.addVoucherRow( getDefaultAccount(iAccountPlan, SSDefaultAccount.SupplierDebt ), iSum );

       // Add the currency change if not zero
        if(iCurrencyRateDifference.signum() > 0 ){
            iVoucher.addVoucherRow( getDefaultAccount(iAccountPlan, SSDefaultAccount.CurrencyProfit ), iCurrencyRateDifference.negate() );
        } else {
            iVoucher.addVoucherRow( getDefaultAccount(iAccountPlan, SSDefaultAccount.CurrencyLoss   ), iCurrencyRateDifference.negate() );
        }

        // Add all rows from the difference voucher
        for(SSVoucherRow iVoucherRow : iDifference.getRows() ){
            iVoucher.addVoucherRow(new SSVoucherRow(iVoucherRow));
        }

       // Add all sales payments
        for(SSOutpaymentRow iRow : iRows){
            SSVoucherRow iVoucherRow = new SSVoucherRow();

            BigDecimal iValue = SSOutpaymentMath.convertToLocal(iRow, iRow.getValue());

            iVoucherRow.setValue  (  iValue.negate() );
            iVoucherRow.setAccount( getDefaultAccount(iAccountPlan, SSDefaultAccount.OutPayment ) );

            iVoucher.addVoucherRow(iVoucherRow);
        }
        iVoucher = SSVoucherMath.compress(iVoucher);

        return iVoucher;
    }


}
