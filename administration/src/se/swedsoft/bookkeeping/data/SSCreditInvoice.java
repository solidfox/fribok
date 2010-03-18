package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.data.common.SSInvoiceType;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: Andreas Lago
 * Date: 2006-mar-30
 * Time: 12:24:20
 */
public class SSCreditInvoice extends SSInvoice {
    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    // The sales this sales is crediting, only used for credit invoices
    private Integer iCreditingNr;

    // The transient crediting sales
    protected transient SSInvoice iCrediting;


    ////////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSCreditInvoice() {
        super();
        iCurrencyRate = new BigDecimal(1.0);
        iVoucher      = new SSVoucher();
    }

    /**
     *
     * @param iCrediting
     */
    public SSCreditInvoice(SSInvoice iCrediting) {
        copyFrom(iCrediting);

        this.iCrediting   = iCrediting;
        this.iCreditingNr = iCrediting.getNumber();
        this.iDate        = new Date() ;
        this.iEntered     = false ;
        this.iPrinted     = false ;

        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        if(iCompany != null){
            setText( iCompany.getStandardText( SSStandardText.Creditinvoice ));
        }

        generateVoucher();
    }

    ////////////////////////////////////////////////////


    /**
     * Copy constructor
     *
     * @param iInvoice
     */
    public SSCreditInvoice(SSCreditInvoice iInvoice) {
        copyFrom(iInvoice);
    }

    /**
     * Clone constructor
     *
     * @param iInvoice
     * @param iNumber
     */
    public SSCreditInvoice(SSCreditInvoice iInvoice, Integer iNumber) {
        copyFrom(iInvoice);

        this.iNumber = iNumber;
    }

    ////////////////////////////////////////////////////

    /**
     * @param iInvoice
     */
    public void copyFrom(SSCreditInvoice iInvoice) {
        super.copyFrom(iInvoice);

        this.iCreditingNr        = iInvoice.iCreditingNr;
        this.iCurrencyRate       = iInvoice.iCurrencyRate;
        this.iCrediting          = iInvoice.iCrediting;
        this.iVoucher            = new SSVoucher(iInvoice.iVoucher);
    }

    ////////////////////////////////////////////////////

    /**
     * Auto increment the number
     */
    @Override
    public void doAutoIncrecement() {
        List<SSCreditInvoice> iCreditInvoices = SSDB.getInstance().getCreditInvoices();

        int iNumber = SSDB.getInstance().getAutoIncrement().getNumber("creditinvoice");

        for(SSCreditInvoice iCreditInvoice: iCreditInvoices){

            if(iCreditInvoice.getNumber() != null && iCreditInvoice.getNumber() > iNumber){
                iNumber = iCreditInvoice.getNumber();
            }
        }
        this.iNumber = iNumber + 1;
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
        this.iCrediting   = null;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    @Override
    public BigDecimal getCurrencyRate() {
        return iCurrencyRate;
    }

    /**
     *
     * @param iCurrencyRate
     */
    @Override
    public void setCurrencyRate(BigDecimal iCurrencyRate) {
        this.iCurrencyRate = iCurrencyRate;
    }


    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    @Override
    public SSVoucher getVoucher() {
        return iVoucher;
    }

    /**
     *
     * @param iVoucher
     */
    @Override
    public void setVoucher(SSVoucher iVoucher) {
        this.iVoucher = iVoucher;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSInvoice getCrediting() {
        return getCrediting(SSDB.getInstance().getInvoices() );
    }

    /**
     * Get the sales this sales is crediting
     *
     * @param iInvoices
     * @return the sales
     */
    public SSInvoice getCrediting(List<SSInvoice> iInvoices) {
        if(iCrediting == null && iCreditingNr != null){
            for (SSInvoice iCurrent : iInvoices) {
                if(iCreditingNr.equals( iCurrent.getNumber() )){
                    iCrediting = iCurrent;
                }
            }
        }
        return iCrediting;
    }

    /**
     * Set the sales this sales is crediting
     *
     * @param iCrediting
     */
    public void setCrediting(SSInvoice iCrediting) {
        this.iCrediting   = iCrediting;
        this.iCreditingNr = iCrediting != null ? iCrediting.getNumber() : null;
    }

    /**
     * Returns if this credit sales is crediting the specified sales
     *
     * @param iCrediting
     * @return
     */
    public boolean isCrediting(SSInvoice iCrediting){
        boolean answer=false;
        if(iCrediting!=null){
            answer= iCrediting.getNumber().equals(iCreditingNr);
        }
        return answer;
    }

    /**
     * Returns if this credit sales is crediting the specified sales
     *
     * @param iCrediting
     * @return
     */
    public boolean isCrediting(Integer iCrediting){
        return iCrediting != null && iCrediting.equals(iCreditingNr);
    }


    ////////////////////////////////////////////////////


    /**
     *
     */
    @Override
    public SSVoucher generateVoucher(){
       String iDescription = SSBundle.getBundle().getString("creditinvoiceframe.voucherdescription");

        SSAccountPlan iAccountPlan = SSDB.getInstance().getCurrentAccountPlan();


        iVoucher = new SSVoucher();
        iVoucher.setDate       ( new Date() );
        iVoucher.setNumber     ( 0  );
        iVoucher.setDescription( String.format(iDescription, iNumber) );

        // Get the total sum for the sales
        BigDecimal iTotalSum = SSInvoiceMath.getTotalSum(this);
        // Get the tax sums for the sales
        Map<SSTaxCode, BigDecimal> iTaxSum = SSInvoiceMath.getTaxSum(this);
        // Get the required rounding for the sales
        BigDecimal iRoundingSum = SSInvoiceMath.getRounding(this);

        // Add the total sum to the voucher
        if(iType == SSInvoiceType.NORMAL) iVoucher.addVoucherRow(getDefaultAccount(iAccountPlan, SSDefaultAccount.CustomerClaim), null, iTotalSum);
        if(iType == SSInvoiceType.CASH  ) iVoucher.addVoucherRow(getDefaultAccount(iAccountPlan, SSDefaultAccount.Cash         ), null, iTotalSum );

        // Add the rounding
        if(!SSDB.getInstance().getCurrentCompany().isRoundingOff()) iVoucher.addVoucherRow( getDefaultAccount(iAccountPlan, SSDefaultAccount.Rounding), iRoundingSum  );

        // Add the tax if not tax free
        if(! iTaxFree){
            // Add the tax 1
            iVoucher.addVoucherRow( getDefaultAccount(iAccountPlan, SSDefaultAccount.Tax1),  iTaxSum.get(SSTaxCode.TAXRATE_1), null  );
            // Add the tax 2
            iVoucher.addVoucherRow( getDefaultAccount(iAccountPlan, SSDefaultAccount.Tax2),  iTaxSum.get(SSTaxCode.TAXRATE_2), null  );
            // Add the tax 3
            iVoucher.addVoucherRow( getDefaultAccount(iAccountPlan, SSDefaultAccount.Tax3),  iTaxSum.get(SSTaxCode.TAXRATE_3), null  );
        }

        // Add all products
        for(SSSaleRow iRow : iRows){
            SSVoucherRow iVoucherRow = new SSVoucherRow();

            iVoucherRow.setDebet     ( iRow.getSum() );
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

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     *
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof SSCreditInvoice)) {
            return false;
        }
        return iNumber.equals(((SSCreditInvoice)obj).getNumber());
    }


}
