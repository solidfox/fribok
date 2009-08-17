package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.util.*;
import java.math.BigDecimal;

/**
 * User: Andreas Lago
 * Date: 2006-jun-09
 * Time: 14:06:08
 *
 * Leverantörsfaktura
 */
public class SSSupplierCreditInvoice extends SSSupplierInvoice  {
    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    // Nummret för leverantörsfakturan denna fakturan krediterar
    protected Integer iCreditingNr;

    // The supplier
    protected transient SSSupplierInvoice iCrediting;

    /**
     *
     */
    public SSSupplierCreditInvoice() {
        super();
    }

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
        this.iDate         = new Date();
        this.iCreditingNr  = iSupplierInvoice.getNumber();
        this.iCrediting    = iSupplierInvoice;

    }

    ////////////////////////////////////////////////////


    /**
     *
     */
    public void doAutoIncrecement() {
        List<SSSupplierCreditInvoice> iInvoices = SSDB.getInstance().getSupplierCreditInvoices();

        int iNumber = SSDB.getInstance().getAutoIncrement().getNumber("suppliercreditinvoice");

        for (SSSupplierCreditInvoice iSupplierInvoice : iInvoices) {
            if(iSupplierInvoice.getNumber() > iNumber){
                iNumber = iSupplierInvoice.getNumber();
            }
        }

        this.iNumber = iNumber + 1;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @param iSSSupplierCreditInvoice
     */
    public void copyFrom(SSSupplierCreditInvoice iSSSupplierCreditInvoice) {
        super.copyFrom(iSSSupplierCreditInvoice);

        this.iCreditingNr  = iSSSupplierCreditInvoice.iCreditingNr;
        this.iCrediting    = iSSSupplierCreditInvoice.iCrediting;
    }


    ////////////////////////////////////////////////////

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


    ////////////////////////////////////////////////////

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
        if(iCrediting == null){
            for (SSSupplierInvoice iCurrent : iSupplierInvoices) {
                if(iCurrent.getNumber().equals(iCreditingNr)){
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
        this.iCrediting   = iCrediting;
        if (iCrediting == null) {
            this.iCreditingNr = null;
        } else {
            this.iCreditingNr = iCrediting.getNumber();
        }
    }

    ////////////////////////////////////////////////////

    /**
     * Returns if this credit supplier invoice is crediting the specified supplier invoice
     *
     * @param iCrediting
     * @return
     */
    public boolean isCrediting(SSSupplierInvoice iCrediting){
        boolean answer=false;
        if(iCrediting!=null){
            answer= iCrediting.getNumber().equals(iCreditingNr);
        }
        return answer;
    }



    ////////////////////////////////////////////////////

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     * @see #hashCode()
     * @see java.util.Hashtable
     */
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
        sb.append(iDate);

        return sb.toString();
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hashtables such as those provided by
     * <code>java.util.Hashtable</code>.
     * <p/>
     * The general contract of <code>hashCode</code> is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during
     * an execution of a Java application, the <tt>hashCode</tt> method
     * must consistently return the same integer, provided no information
     * used in <tt>equals</tt> comparisons on the object is modified.
     * This integer need not remain consistent from one execution of an
     * application to another execution of the same application.
     * <li>If two objects are equal according to the <tt>equals(Object)</tt>
     * method, then calling the <code>hashCode</code> method on each of
     * the two objects must produce the same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal
     * according to the {@link Object#equals(Object)}
     * method, then calling the <tt>hashCode</tt> method on each of the
     * two objects must produce distinct integer results.  However, the
     * programmer should be aware that producing distinct integer results
     * for unequal objects may improve the performance of hashtables.
     * </ul>
     * <p/>
     * As much as is reasonably practical, the hashCode method defined by
     * class <tt>Object</tt> does return distinct integers for distinct
     * objects. (This is typically implemented by converting the internal
     * address of the object into an integer, but this implementation
     * technique is not required by the
     * Java<font size="-2"><sup>TM</sup></font> programming language.)
     *
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see java.util.Hashtable
     */
    public int hashCode() {
        return iNumber;
    }

    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return iNumber == null ? "" : iNumber.toString();
    }

    /**
     *
     * @return
     */
    public SSVoucher generateVoucher() {
        iVoucher = new SSVoucher();
        String iDescription = SSBundle.getBundle().getString("suppliercreditinvoiceframe.voucherdescription");

        SSNewCompany     iCompany     = SSDB.getInstance().getCurrentCompany();

        SSAccountPlan iAccountPlan = SSDB.getInstance().getCurrentAccountPlan();


        iVoucher = new SSVoucher();
        iVoucher.setDate       ( new Date() );
        iVoucher.setNumber     ( 0  );
        iVoucher.setDescription( String.format(iDescription, iNumber) );

        // Get the total sum for the sales
        BigDecimal iTotalSum         = SSSupplierInvoiceMath.getTotalSum(this);
        BigDecimal iCorrectionSum    = SSVoucherMath.getCreditMinusDebetSum(iCorrection);

        iTotalSum = iTotalSum.subtract( iCorrectionSum    );

        // Add the total sum to the voucher
        iVoucher.addVoucherRow(getDefaultAccount(iAccountPlan, SSDefaultAccount.SupplierDebt),  iTotalSum, null);

        // Add roundingsum
        iVoucher.addVoucherRow( getDefaultAccount(iAccountPlan, SSDefaultAccount.Rounding), iRoundingSum.negate());

        // Add the tax 1
        iVoucher.addVoucherRow( getDefaultAccount(iAccountPlan, SSDefaultAccount.IncommingTax), null, iTaxSum );

        // Add all rows from the correction voucher
        for(SSVoucherRow iVoucherRow : iCorrection.getRows() ){
            iVoucher.addVoucherRow(new SSVoucherRow(iVoucherRow));
        }


        // Add all products
        for(SSSupplierInvoiceRow iRow : iRows){
            SSVoucherRow iVoucherRow = new SSVoucherRow();

            iVoucherRow.setCredit    ( iRow.getSum() );
            iVoucherRow.setAccount   ( iRow.getAccount   ( iAccountPlan.getAccounts() ) );
            iVoucherRow.setProject   ( iRow.getProject   ( SSDB.getInstance().getProjects()));
            iVoucherRow.setResultUnit( iRow.getResultUnit( SSDB.getInstance().getResultUnits()));

            if(iVoucherRow.getAccountNr()!=null)
            {
                iVoucher.addVoucherRow(iVoucherRow);
            }
        }
        for(SSVoucherRow iRow : iVoucher.getRows()){
            if(iRow.isDebet()){
                if(iRow.getDebet().compareTo(new BigDecimal(0.0)) == -1){
                    iRow.setCredit(iRow.getDebet().negate());
                    iRow.setDebet(null);
                }
            }
            else {
                if(iRow.getCredit().compareTo(new BigDecimal(0.0)) == -1){
                    iRow.setDebet(iRow.getCredit().negate());
                    iRow.setCredit(null);
                }
            }
        }
        // Convert all rows to the local currency
        if(iCurrencyRate != null)  SSVoucherMath.multiplyRowsBy(iVoucher, iCurrencyRate);

        iVoucher = SSVoucherMath.compress(iVoucher);

        return iVoucher;
    }

}
