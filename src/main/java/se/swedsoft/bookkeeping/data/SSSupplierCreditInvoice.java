package se.swedsoft.bookkeeping.data;


import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * User: Andreas Lago
 * Date: 2006-jun-09
 * Time: 14:06:08
 *
 * Leverantörsfaktura
 */
public class SSSupplierCreditInvoice extends SSSupplierInvoice {
    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    // Nummret för leverantörsfakturan denna fakturan krediterar
    protected Integer iCreditingNr;

    // The supplier
    protected transient SSSupplierInvoice iCrediting;

    /**
     *
     */
    public SSSupplierCreditInvoice() {}

    /**
     *
     * @param iSSSupplierCreditInvoice
     */
    public SSSupplierCreditInvoice(SSSupplierCreditInvoice iSSSupplierCreditInvoice) {
        copyFrom(iSSSupplierCreditInvoice);
    }

    /**
     * Create a suppliercrditinvoice based supplierinvoice
     *
     * @param iSupplierInvoice
     */
    public SSSupplierCreditInvoice(SSSupplierInvoice iSupplierInvoice) {
        super(iSupplierInvoice);
        iDate = new Date();
        iCreditingNr = iSupplierInvoice.getNumber();
        iCrediting = iSupplierInvoice;

    }

    // //////////////////////////////////////////////////

    /**
     *
     */
    @Override
    public void doAutoIncrecement() {
        List<SSSupplierCreditInvoice> iInvoices = SSDB.getInstance().getSupplierCreditInvoices();

        int iNumber = SSDB.getInstance().getAutoIncrement().getNumber(
                "suppliercreditinvoice");

        for (SSSupplierCreditInvoice iSupplierInvoice : iInvoices) {
            if (iSupplierInvoice.getNumber() > iNumber) {
                iNumber = iSupplierInvoice.getNumber();
            }
        }

        this.iNumber = iNumber + 1;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @param iSSSupplierCreditInvoice
     */
    public void copyFrom(SSSupplierCreditInvoice iSSSupplierCreditInvoice) {
        super.copyFrom(iSSSupplierCreditInvoice);

        iCreditingNr = iSSSupplierCreditInvoice.iCreditingNr;
        iCrediting = iSSSupplierCreditInvoice.iCrediting;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Integer getCreditingNr() {
        return iCreditingNr;
    }

    /**
     *
     * @param iCreditingNr
     */
    public void setCreditingNr(Integer iCreditingNr) {
        this.iCreditingNr = iCreditingNr;
    }

    // //////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSSupplierInvoice getCrediting() {
        return getCrediting(SSDB.getInstance().getSupplierInvoices());
    }

    /**
     *
     * @param iSupplierInvoices
     * @return
     */
    public SSSupplierInvoice getCrediting(List<SSSupplierInvoice> iSupplierInvoices) {
        if (iCrediting == null) {
            for (SSSupplierInvoice iCurrent : iSupplierInvoices) {
                if (iCurrent.getNumber().equals(iCreditingNr)) {
                    iCrediting = iCurrent;
                }
            }
        }
        return iCrediting;
    }

    /**
     *
     * @param iCrediting
     */
    public void setCrediting(SSSupplierInvoice iCrediting) {
        this.iCrediting = iCrediting;
        if (iCrediting == null) {
            iCreditingNr = null;
        } else {
            iCreditingNr = iCrediting.getNumber();
        }
    }

    // //////////////////////////////////////////////////

    /**
     * Returns if this credit supplier invoice is crediting the specified supplier invoice
     *
     * @param iCrediting
     * @return
     */
    public boolean isCrediting(SSSupplierInvoice iCrediting) {
        boolean answer = false;

        if (iCrediting != null) {
            answer = iCrediting.getNumber().equals(iCreditingNr);
        }
        return answer;
    }

    // //////////////////////////////////////////////////

    public boolean equals(Object obj) {

        if (iNumber == null) {
            return false;
        }

        if (obj instanceof SSSupplierCreditInvoice) {
            SSSupplierCreditInvoice iSale = (SSSupplierCreditInvoice) obj;

            return iNumber.equals(iSale.iNumber);
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(iNumber);
        sb.append(", ");
        sb.append(iDate);

        return sb.toString();
    }

    public int hashCode() {
        return iNumber;
    }

    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    @Override
    public String toRenderString() {
        return iNumber == null ? "" : iNumber.toString();
    }

    /**
     *
     * @return
     */
    @Override
    public SSVoucher generateVoucher() {
        iVoucher = new SSVoucher();
        String iDescription = SSBundle.getBundle().getString(
                "suppliercreditinvoiceframe.voucherdescription");

        SSNewCompany     iCompany = SSDB.getInstance().getCurrentCompany();

        SSAccountPlan iAccountPlan = SSDB.getInstance().getCurrentAccountPlan();

        iVoucher = new SSVoucher();
        iVoucher.setDate(new Date());
        iVoucher.setNumber(0);
        iVoucher.setDescription(String.format(iDescription, iNumber));

        // Get the total sum for the sales
        BigDecimal iTotalSum = SSSupplierInvoiceMath.getTotalSum(this);
        BigDecimal iCorrectionSum = SSVoucherMath.getCreditMinusDebetSum(iCorrection);

        iTotalSum = iTotalSum.subtract(iCorrectionSum);

        // Add the total sum to the voucher
        iVoucher.addVoucherRow(
                getDefaultAccount(iAccountPlan, SSDefaultAccount.SupplierDebt), iTotalSum,
                null);

        // Add roundingsum
        iVoucher.addVoucherRow(getDefaultAccount(iAccountPlan, SSDefaultAccount.Rounding),
                iRoundingSum.negate());

        // Add the tax 1
        iVoucher.addVoucherRow(
                getDefaultAccount(iAccountPlan, SSDefaultAccount.IncommingTax), null,
                iTaxSum);

        // Add all rows from the correction voucher
        for (SSVoucherRow iVoucherRow : iCorrection.getRows()) {
            iVoucher.addVoucherRow(new SSVoucherRow(iVoucherRow));
        }

        // Add all products
        for (SSSupplierInvoiceRow iRow : iRows) {
            SSVoucherRow iVoucherRow = new SSVoucherRow();

            iVoucherRow.setCredit(iRow.getSum());
            iVoucherRow.setAccount(iRow.getAccount(iAccountPlan.getAccounts()));
            iVoucherRow.setProject(iRow.getProject(SSDB.getInstance().getProjects()));
            iVoucherRow.setResultUnit(
                    iRow.getResultUnit(SSDB.getInstance().getResultUnits()));

            if (iVoucherRow.getAccountNr() != null) {
                iVoucher.addVoucherRow(iVoucherRow);
            }
        }
        for (SSVoucherRow iRow : iVoucher.getRows()) {
            if (iRow.isDebet()) {
                if (iRow.getDebet().compareTo(new BigDecimal(0)) == -1) {
                    iRow.setCredit(iRow.getDebet().negate());
                    iRow.setDebet(null);
                }
            } else {
                if (iRow.getCredit().compareTo(new BigDecimal(0)) == -1) {
                    iRow.setDebet(iRow.getCredit().negate());
                    iRow.setCredit(null);
                }
            }
        }
        // Convert all rows to the local currency
        if (iCurrencyRate != null) {
            SSVoucherMath.multiplyRowsBy(iVoucher, iCurrencyRate);
        }

        iVoucher = SSVoucherMath.compress(iVoucher);

        return iVoucher;
    }

}
