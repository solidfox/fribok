package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.base.SSSale;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.data.common.SSInvoiceType;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.math.BigDecimal;
import java.util.*;

/**
 * User: Andreas Lago
 * Date: 2006-mar-30
 * Time: 12:24:20
 */
public class SSInvoice extends SSSale {
    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    // Typ av faktura
    protected SSInvoiceType iType;
    // Valutakurs
    protected BigDecimal iCurrencyRate;
    // Förfallodag
    protected Date iPaymentDay;
    // Kontering
    protected SSVoucher iVoucher;
    // Ert order/avtalsnummer
    protected String iYourOrderNumber;
    // OCR nummret om fakturan är utskriven som ocr faktura
    protected String iOCRNumber;

    // Bokförd
    protected boolean iEntered;
    // Antal påminnelser
    protected int iNumReminders;
    // Räntefakturerad
    protected boolean iInterestInvoiced;
    // Lagerför
    private boolean iStockInfluencing;

    private String iOrderNumbers;

    ////////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSInvoice() {
        super();
        iType              = SSInvoiceType.NORMAL;
        iCurrencyRate      = new BigDecimal(1.0);
        iVoucher           = new SSVoucher();
        iOCRNumber         = null;
        iOrderNumbers      = "Fakturan har inga ordrar";
        iPrinted           = false;
        iInterestInvoiced  = false;
        iStockInfluencing  = true;
        iNumReminders      = 0;
        if(iPaymentTerm != null)
            iPaymentDay = iPaymentTerm.addDaysToDate(new Date());
    }

    /**
     * Copy constructor
     *
     * @param iInvoice
     */
    public SSInvoice(SSInvoice iInvoice) {
        copyFrom(iInvoice);
    }



    /**
     * Creates a new sales with the number as the lastest sales number + 1
     *
     */
    public SSInvoice(SSInvoiceType iInvoiceType ){
        this();
        
        setType(iInvoiceType);

        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        if(iCompany != null){
            setDelayInterest    ( iCompany.getDelayInterest()   );

            setText( iCompany.getStandardText( SSStandardText.Customerinvoice ));

            setTaxRate1         ( iCompany.getTaxRate1()   );
            setTaxRate2         ( iCompany.getTaxRate2()   );
            setTaxRate3         ( iCompany.getTaxRate3()   );
            setDefaultAccounts  ( iCompany.getDefaultAccounts());
            setOurContactPerson ( iCompany.getContactPerson  () );
        }
        //doAutoIncrecement();
    }

    /**
     *  Creates an sales based on a order
     *
     * @param iOrder
     */
    public SSInvoice(SSOrder iOrder) {
        this();

        copyFrom(iOrder);

        this.iCurrencyRate = iOrder.getCurrencyRate();
        this.iVoucher      = new SSVoucher();
        this.iDate         = new Date();
        this.iRows         = new LinkedList<SSSaleRow>();

        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        if(iCompany != null){
            iText = iCompany.getStandardText(SSStandardText.Customerinvoice);
        }
    }


    ////////////////////////////////////////////////////

    /**
     * @param iInvoice
     */
    public void copyFrom(SSInvoice iInvoice) {
        super.copyFrom(iInvoice);

        //this.iCurrencyRate       = iInvoice.getCustomer().getInvoiceCurrency().getExchangeRate();
        this.iCurrencyRate       = iInvoice.getCurrencyRate();
        this.iPaymentDay         = iInvoice.iPaymentDay;
        this.iYourOrderNumber    = iInvoice.iYourOrderNumber;
        this.iType               = iInvoice.iType;
        this.iEntered            = iInvoice.iEntered;
        this.iStockInfluencing   = iInvoice.iStockInfluencing;
        this.iInterestInvoiced   = iInvoice.iInterestInvoiced;
        this.iNumReminders       = iInvoice.iNumReminders;
        this.iOCRNumber          = iInvoice.iOCRNumber;
        this.iOrderNumbers       = iInvoice.iOrderNumbers; 
        this.iVoucher            = new SSVoucher(iInvoice.iVoucher);
    }

    ////////////////////////////////////////////////////

    /**
     * Auto increment the sales number
     */
    @Override
    public void doAutoIncrecement(){
        int iNumber =  SSDB.getInstance().getAutoIncrement().getNumber("invoice");

        for(SSInvoice iInvoice: SSDB.getInstance().getInvoices()){

            if(iInvoice.getNumber() != null && iInvoice.getNumber() > iNumber){
                iNumber = iInvoice.getNumber();
            }
        }
        setNumber(iNumber + 1);
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSInvoiceType getType() {
        if(iType == null) iType = SSInvoiceType.NORMAL;
        return iType;
    }

    /**
     *
     * @param iType
     */
    public void setType(SSInvoiceType iType) {
        this.iType = iType;
    }

    ////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public BigDecimal getCurrencyRate() {
        return iCurrencyRate;
    }

    /**
     *
     * @param iCurrencyRate
     */
    public void setCurrencyRate(BigDecimal iCurrencyRate) {
        this.iCurrencyRate = iCurrencyRate;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Date getDueDate() {
        return iPaymentDay;
    }

    /**
     *
     * @param iPaymentDay
     */
    public void setDueDate(Date iPaymentDay) {
        this.iPaymentDay = iPaymentDay;
    }

    /**
     * Set the duedate depending on the invoice date and the payment term
     */
    public void setDueDate() {
        Calendar iCalendar = Calendar.getInstance();

        if(iPaymentTerm != null){
            iCalendar.setTime( iDate );
            iCalendar.add(Calendar.DAY_OF_MONTH, iPaymentTerm.decodeValue());

            iPaymentDay = iCalendar.getTime() ;
        } else {
            iPaymentDay = iDate;
        }
    }


    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getYourOrderNumber() {
        return iYourOrderNumber;
    }

    /**
     *
     * @param iYourOrderNumber
     */
    public void setYourOrderNumber(String iYourOrderNumber) {
        this.iYourOrderNumber = iYourOrderNumber;
    }

    ////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public SSVoucher getVoucher() {
        if(iVoucher == null) generateVoucher();
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
    public boolean hasOCRNumber(){
        return iOCRNumber != null;
    }

    /**
     *
     * @return
     */
    public String getOCRNumber() {
        return iOCRNumber;
    }

    /**
     *
     * @param iOCRNumber
     */
    public void setOCRNumber(String iOCRNumber){
        this.iOCRNumber = iOCRNumber;
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
        this.iEntered = true;
    }

   

    ////////////////////////////////////////////////////

    public String getOrderNumbers() {
        return iOrderNumbers;
    }

    public void setOrderNumbers(String iOrderNumbers) {
        this.iOrderNumbers = iOrderNumbers;
    }

    public void setOrderNumers(List<SSOrder> iOrders){
        String iOrdersForInvoice = "";
        for (SSOrder iOrder : iOrders) {
            iOrdersForInvoice += iOrder.getNumber() + ", ";
        }
        iOrdersForInvoice = iOrdersForInvoice.substring(0,iOrdersForInvoice.lastIndexOf(", "));
        this.iOrderNumbers = iOrdersForInvoice;
    }
    /**
     *
     * @return
     */
    public boolean isInterestInvoiced() {
        return iInterestInvoiced;
    }

    /**
     *
     * @param iInterestInvoiced
     */
    public void setInterestInvoiced(boolean iInterestInvoiced) {
        this.iInterestInvoiced = iInterestInvoiced;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public int getNumReminders() {
        return iNumReminders;
    }

    /**
     *
     * @param iNumReminders
     */
    public void setNumRemainders(int iNumReminders) {
        this.iNumReminders = iNumReminders;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean isStockInfluencing() {
        return iStockInfluencing;
    }

    /**
     *
     * @param iStockInfluencing
     */
    public void setStockInfluencing(boolean iStockInfluencing) {
        this.iStockInfluencing = iStockInfluencing;
    }

    ////////////////////////////////////////////////////

    /**
     * Adds the rows from an order to this invoice
     *
     * @param iOrder
     */
    public void append(SSOrder iOrder) {
        for (SSSaleRow iRow : iOrder.getRows()) {

            SSSaleRow iMatchingRow = SSInvoiceMath.getMatchingRow(this, iRow);

            if( iMatchingRow != null){
                Integer iQuantity = iMatchingRow.getQuantity();

                if(iQuantity != null){
                    iMatchingRow.setQuantity( iQuantity + iRow.getQuantity() );
                } else {
                    iMatchingRow.setQuantity(  iRow.getQuantity() );

                }
            } else {
                iRows.add( new SSSaleRow(iRow) );
            }
        }
    }


    /**
     * Returns true if sales customer is the same customer as the supplied
     *
     * @param iCustomer
     * @return
     */
    public boolean hasCustomer(SSCustomer iCustomer) {
        return (iCustomerNr != null) && iCustomerNr.equals(iCustomer.getNumber());
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hashtables such as those provided by
     * <code>java.util.Hashtable</code>.
     *
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see java.util.Hashtable
     */
    public int hashCode() {
        if(iNumber != null) return iNumber;

        return super.hashCode();
    }


    /**
     *
     */
    public SSVoucher generateVoucher(){
        String iDescription = SSBundle.getBundle().getString("invoiceframe.voucherdescription");

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
        if(iType == SSInvoiceType.NORMAL) iVoucher.addVoucherRow(getDefaultAccount(iAccountPlan, SSDefaultAccount.CustomerClaim), iTotalSum, null);
        if(iType == SSInvoiceType.CASH  ) iVoucher.addVoucherRow(getDefaultAccount(iAccountPlan, SSDefaultAccount.Cash         ), iTotalSum, null);

        // Add the rounding
        if(!SSDB.getInstance().getCurrentCompany().isRoundingOff()) iVoucher.addVoucherRow( getDefaultAccount(iAccountPlan, SSDefaultAccount.Rounding), iRoundingSum.negate());

        // Add the tax if not tax free
        if(! iTaxFree){
            // Add the tax 1
            iVoucher.addVoucherRow( getDefaultAccount(iAccountPlan, SSDefaultAccount.Tax1), null, iTaxSum.get(SSTaxCode.TAXRATE_1)  );
            // Add the tax 2
            iVoucher.addVoucherRow( getDefaultAccount(iAccountPlan, SSDefaultAccount.Tax2), null, iTaxSum.get(SSTaxCode.TAXRATE_2)  );
            // Add the tax 3
            iVoucher.addVoucherRow( getDefaultAccount(iAccountPlan, SSDefaultAccount.Tax3), null, iTaxSum.get(SSTaxCode.TAXRATE_3)  );
        }

        // Add all products
        for(SSSaleRow iRow : iRows){
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

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     *
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof SSInvoice)) {
            return false;
        }
        return iNumber.equals(((SSInvoice)obj).getNumber());
    }

}
