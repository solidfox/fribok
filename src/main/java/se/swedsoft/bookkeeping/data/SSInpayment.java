package se.swedsoft.bookkeeping.data;


import se.swedsoft.bookkeeping.calc.math.SSInpaymentMath;
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
public class SSInpayment implements SSTableSearchable, Serializable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    // Inbetalningsnummer
    private Integer iNumber;
    // Inbetalningsdatum
    private Date iDate;
    // Text
    private String iText;
    // Rader
    private List<SSInpaymentRow> iRows;
    // Verifikation
    private SSVoucher iVoucher;
    // Differensverifikation
    private SSVoucher iDifference;
    // Bokförd
    private boolean iEntered;

    // Standard konton
    protected Map<SSDefaultAccount, Integer> iDefaultAccounts;

    // //////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSInpayment() {
        iRows = new LinkedList<SSInpaymentRow>();
        iVoucher = new SSVoucher();
        iDifference = new SSVoucher();
        iEntered = false;
        iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();
        iDefaultAccounts.putAll(
                SSDB.getInstance().getCurrentCompany().getDefaultAccounts());

        // doAutoIncrecement();
    }

    /**
     * Copy constructor
     *
     * @param iInpayment
     */
    public SSInpayment(SSInpayment iInpayment) {
        copyFrom(iInpayment);
    }

    /**
     * Clone constructor
     *
     * @param iInpayment
     * @param iNumber
     */
    public SSInpayment(SSInpayment iInpayment, Integer iNumber) {
        copyFrom(iInpayment);

        this.iNumber = iNumber;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @param iInpayment
     */
    public void copyFrom(SSInpayment iInpayment) {
        iNumber = iInpayment.iNumber;
        iDate = iInpayment.iDate;
        iText = iInpayment.iText;
        iEntered = iInpayment.iEntered;
        iVoucher = new SSVoucher(iInpayment.iVoucher);
        iDifference = new SSVoucher(iInpayment.iDifference);
        iRows = new LinkedList<SSInpaymentRow>();
        iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();

        // Copy all rows
        for (SSInpaymentRow iRow : iInpayment.iRows) {
            iRows.add(new SSInpaymentRow(iRow));
        }

        // Copy all default accounts
        iDefaultAccounts.putAll(iInpayment.getDefaultAccounts());
    }

    // //////////////////////////////////////////////////

    /**
     * Sets the number of this voucher as the maxinum mumber + 1
     */
    public void doAutoIncrecement() {
        List<SSInpayment> iInpayments = SSDB.getInstance().getInpayments();

        int iNumber = SSDB.getInstance().getAutoIncrement().getNumber("inpayment");

        for (SSInpayment iInpayment: iInpayments) {
            if (iInpayment.iNumber > iNumber) {
                iNumber = iInpayment.iNumber;
            }
        }
        this.iNumber = iNumber + 1;

    }

    // //////////////////////////////////////////////////
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

    // //////////////////////////////////////////////////

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

    // //////////////////////////////////////////////////

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

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Map<SSDefaultAccount, Integer> getDefaultAccounts() {

        if (iDefaultAccounts == null) {
            SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

            if (iCompany != null) {
                iDefaultAccounts = iCompany.getDefaultAccounts();
                iCompany = null;
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
    public SSAccount getDefaultAccount(SSAccountPlan iAccountPlan, SSDefaultAccount iDefaultAccount) {
        Integer iAccountNumber = iDefaultAccounts.get(iDefaultAccount);

        if (iAccountNumber == null) {
            return null;
        }

        return iAccountPlan.getAccount(iAccountNumber);
    }

    /**
     *
     * @param iDefaultAccount
     * @return
     */
    public Integer getDefaultAccount(SSDefaultAccount iDefaultAccount) {
        return iDefaultAccounts.get(iDefaultAccount);
    }

    /**
     *
     * @param iDefaultAccounts
     */
    public void setDefaultAccounts(Map<SSDefaultAccount, Integer> iDefaultAccounts) {
        this.iDefaultAccounts = iDefaultAccounts;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSInpaymentRow> getRows() {
        if (iRows == null) {
            iRows = new LinkedList<SSInpaymentRow>();
        }
        return iRows;
    }

    /**
     *
     * @param iRows
     */
    public void setRows(List<SSInpaymentRow> iRows) {
        this.iRows = iRows;
    }

    // //////////////////////////////////////////////////

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

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSVoucher getDifference() {
        if (iDifference == null) {
            iDifference = new SSVoucher();
        }

        return iDifference;
    }

    /**
     *
     * @param iDifference
     */
    public void setDifference(SSVoucher iDifference) {
        this.iDifference = iDifference;
    }

    // //////////////////////////////////////////////////

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

    // //////////////////////////////////////////////////

    /**
     *
     * @param iInvoices
     */
    public void addInvoices(List<SSInvoice> iInvoices) {
        for (SSInvoice iInvoice : iInvoices) {
            iRows.add(new SSInpaymentRow(iInvoice));
        }
    }

    public List<SSInvoice> getInvoices() {
        List<SSInvoice> iInvoices = new LinkedList<SSInvoice>();

        for (SSInpaymentRow iRow : iRows) {
            if (iRow.getInvoice(SSDB.getInstance().getInvoices()) != null) {
                iInvoices.add(iRow.getInvoice(SSDB.getInstance().getInvoices()));
            }
        }
        return iInvoices;
    }

    // //////////////////////////////////////////////////

    /**
     * Returns true if this inpayment contains the sales, false othervise
     *
     * @param iInvoice
     *
     * @return if the inpayment contains the sales
     */
    public boolean isPaying(SSInvoice iInvoice) {
        for (SSInpaymentRow iRow: iRows) {
            if (iRow.isPaying(iInvoice)) {
                return true;
            }
        }
        return false;
    }

    // //////////////////////////////////////////////////

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

    public boolean equals(Object obj) {
        if (!(obj instanceof SSInpayment)) {
            return false;
        }
        return iNumber.equals(((SSInpayment) obj).iNumber);
    }

    /**
     *
     * @return
     */
    public SSVoucher generateVoucher() {
        String iDescription = SSBundle.getBundle().getString(
                "inpaymentframe.voucherdescription");

        SSAccountPlan iAccountPlan = SSDB.getInstance().getCurrentAccountPlan();

        BigDecimal iSum = SSInpaymentMath.getSum(this);
        BigDecimal iCurrencyRateDifference = SSInpaymentMath.getCurrencyRateDifference(
                this);
        BigDecimal iDifferenceSum = SSVoucherMath.getCreditMinusDebetSum(iDifference);

        iSum = iSum.subtract(iCurrencyRateDifference);
        iSum = iSum.subtract(iDifferenceSum);

        iVoucher = new SSVoucher();
        iVoucher.setDate(new Date());
        iVoucher.setNumber(0);
        iVoucher.setDescription(String.format(iDescription, iNumber));

        // Add the sum
        iVoucher.addVoucherRow(
                getDefaultAccount(iAccountPlan, SSDefaultAccount.CustomerClaim),
                iSum.negate());

        // Add the currency change if not zero
        if (iCurrencyRateDifference.signum() > 0) {
            iVoucher.addVoucherRow(
                    getDefaultAccount(iAccountPlan, SSDefaultAccount.CurrencyProfit),
                    iCurrencyRateDifference.negate());
        } else {
            iVoucher.addVoucherRow(
                    getDefaultAccount(iAccountPlan, SSDefaultAccount.CurrencyLoss),
                    iCurrencyRateDifference.negate());
        }

        // Add all rows from the difference voucher
        for (SSVoucherRow iVoucherRow : iDifference.getRows()) {
            iVoucher.addVoucherRow(new SSVoucherRow(iVoucherRow));
        }

        // Add all sales payments
        for (SSInpaymentRow iRow : iRows) {
            SSVoucherRow iVoucherRow = new SSVoucherRow();

            BigDecimal iValue = SSInpaymentMath.convertToLocal(iRow, iRow.getValue());

            iVoucherRow.setValue(iValue);
            iVoucherRow.setAccount(
                    getDefaultAccount(iAccountPlan, SSDefaultAccount.InPayment));

            iVoucher.addVoucherRow(iVoucherRow);
        }
        iVoucher = SSVoucherMath.compress(iVoucher);

        return iVoucher;
    }

}
