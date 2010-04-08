package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.data.common.SSPaymentTerm;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * User: Andreas Lago
 * Date: 2006-jun-09
 * Time: 14:06:08
 *
 * Leverantörsfaktura
 */
public class SSSupplierInvoice implements SSTableSearchable, Serializable  {
    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    // Nummer
    protected Integer iNumber;
    // Datum
    protected Date iDate;
    // Förfallodatum
    protected Date iDueDate;
    //Betalningsvillkor
    protected SSPaymentTerm iPaymentTerm;
    // Leverantörsnummer
    private String iSupplierNr;
    // Leverantörsnamn
    private String iSupplierName;
    // OCR/ referensnummer
    private String iReferencenumber;
    // Valuta
    protected SSCurrency iCurrency;
    // Valutakurs
    protected BigDecimal iCurrencyRate;
    // Total Moms
    protected BigDecimal iTaxSum;
    //Öresavrunding
    protected BigDecimal iRoundingSum;
    // Kontering
    protected SSVoucher iVoucher;
    // manuell kontering
    protected SSVoucher iCorrection ;
    // Bokförd
    protected boolean iEntered;
    // Lagerför
    private boolean iStockInfluencing;
    // Om leverantörsfakturan har fakturerats via bangirocentralen
    private boolean iBGCEntered;

    // The rows
    protected List<SSSupplierInvoiceRow> iRows;
    // Standard konton
    protected Map<SSDefaultAccount, Integer> iDefaultAccounts;

    // The supplier
    protected transient SSSupplier iSupplier;


    /**
     *
     */
    public SSSupplierInvoice() {
        iRows = new LinkedList<SSSupplierInvoiceRow>();
        iDate = getLastDate();
        iDueDate = getLastDate();
        iCurrencyRate       = new BigDecimal(1);
        iVoucher            = new SSVoucher();
        iCorrection         = new SSVoucher();
        iTaxSum             = new BigDecimal(0);
        iRoundingSum        = new BigDecimal(0);
        iEntered            = false;
        iStockInfluencing   = true;
        iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();
        iDefaultAccounts.putAll(SSDB.getInstance().getCurrentCompany().getDefaultAccounts());
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();
        if(iCompany != null){
            iCurrency = iCompany.getCurrency();
        }
    }


    /**
     *
     * @param iSupplierInvoice
     */
    public SSSupplierInvoice(SSSupplierInvoice iSupplierInvoice) {
        copyFrom(iSupplierInvoice);
    }



    /**
     * Create a supplierinvoice based on a purchase order
     *
     * @param iPurchaseOrder
     */
    public SSSupplierInvoice(SSPurchaseOrder iPurchaseOrder) {
        iDate            = getLastDate();
        iSupplier        = iPurchaseOrder.iSupplier;
        iNumber          = iPurchaseOrder.iNumber;
        iSupplier        = iPurchaseOrder.iSupplier;
        iSupplierNr      = iPurchaseOrder.iSupplierNr;
        iSupplierName    = iPurchaseOrder.iSupplierName;
        iCurrency        = iPurchaseOrder.iCurrency;
        iCurrencyRate    = iPurchaseOrder.getCurrencyRate();
        iVoucher         = new SSVoucher();
        iCorrection      = new SSVoucher();
        iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();
        iRows            = new LinkedList<SSSupplierInvoiceRow>();
        iRoundingSum     = new BigDecimal(0);

        // Copy all default accounts
        for (SSDefaultAccount iDefaultAccount : iPurchaseOrder.getDefaultAccounts().keySet()) {
            iDefaultAccounts.put(iDefaultAccount, iPurchaseOrder.getDefaultAccounts().get(iDefaultAccount));
        }


        generateVoucher();
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @param iSupplierInvoice
     */
    public void copyFrom(SSSupplierInvoice iSupplierInvoice) {
        iNumber           = iSupplierInvoice.iNumber;
        iDate             = iSupplierInvoice.iDate;
        iPaymentTerm      = iSupplierInvoice.iPaymentTerm;
        iDueDate          = iSupplierInvoice.iDueDate;
        iSupplierNr       = iSupplierInvoice.iSupplierNr;
        iSupplierName     = iSupplierInvoice.iSupplierName;
        iReferencenumber  = iSupplierInvoice.iReferencenumber;
        iCurrency         = iSupplierInvoice.iCurrency;
        iCurrencyRate     = iSupplierInvoice.iCurrencyRate;
        iTaxSum           = iSupplierInvoice.iTaxSum;
        iRoundingSum      = iSupplierInvoice.iRoundingSum;
        iStockInfluencing = iSupplierInvoice.iStockInfluencing;
        iEntered          = iSupplierInvoice.iEntered;
        iBGCEntered       = iSupplierInvoice.iBGCEntered;

        iVoucher          = new SSVoucher(iSupplierInvoice.iVoucher);
        iCorrection       = new SSVoucher(iSupplierInvoice.iCorrection);

        iSupplier         = iSupplierInvoice.iSupplier;
        iDefaultAccounts  = new HashMap<SSDefaultAccount, Integer>();

        // Copy all default accounts
        for (SSDefaultAccount iDefaultAccount : iSupplierInvoice.getDefaultAccounts().keySet()) {
            iDefaultAccounts.put(iDefaultAccount, iSupplierInvoice.getDefaultAccounts().get(iDefaultAccount));
        }

        iRows = new LinkedList<SSSupplierInvoiceRow>();
        for (SSSupplierInvoiceRow iRow : iSupplierInvoice.iRows) {
            iRows.add( new SSSupplierInvoiceRow(iRow) );
        }
    }
    ////////////////////////////////////////////////////

    /**
     *
     */
    public void doAutoIncrecement() {
        List<SSSupplierInvoice> iInvoices = SSDB.getInstance().getSupplierInvoices();

        int iMax = SSDB.getInstance().getAutoIncrement().getNumber("supplierinvoice");
        for (SSSupplierInvoice iSupplierInvoice : iInvoices) {
            if(iSupplierInvoice.iNumber > iMax){
                iMax = iSupplierInvoice.iNumber;
            }
        }

        iNumber = iMax + 1;
    }


    public Date getLastDate(){
        List<SSSupplierInvoice> iSupplierInvoices = SSDB.getInstance().getSupplierInvoices();
        Date iMax = null;
        if(!iSupplierInvoices.isEmpty()){
            iMax = iSupplierInvoices.get(0).iDate;
            for(SSSupplierInvoice iInvoice : iSupplierInvoices){
                if(iInvoice.iDate.after(iMax)){
                    iMax = iInvoice.iDate;
                }
            }
        }
        return iMax;
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
    public Date getDueDate() {
        return iDueDate;
    }

    /**
     *
     * @param iDueDate
     */
    public void setDueDate(Date iDueDate) {
        this.iDueDate = iDueDate;
    }

    public void setDueDate() {
        Calendar iCalendar = Calendar.getInstance();

        if(iPaymentTerm != null){
            if(iDate==null){
                 iDate=new Date();
            }
            iCalendar.setTime( iDate );
            iCalendar.add(Calendar.DAY_OF_MONTH, iPaymentTerm.decodeValue());

            iDueDate = iCalendar.getTime() ;
        } else {
            iDueDate = iDate;
        }
    }

    public void setPaymentTerm(SSPaymentTerm iPaymentTerm)
    {
        this.iPaymentTerm=iPaymentTerm;
    }

    ////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public String getSupplierNr() {
        return iSupplierNr;
    }

    /**
     *
     * @param iSupplierNr
     */
    public void setSupplierNr(String iSupplierNr) {
        this.iSupplierNr = iSupplierNr;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getSupplierName() {
        return iSupplierName;
    }

    /**
     *
     * @param iSupplierName
     */
    public void setSupplierName(String iSupplierName) {
        this.iSupplierName = iSupplierName;
    }

    ////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public String getReferencenumber() {
        return iReferencenumber;
    }

    /**
     *
     * @param iReferencenumber
     */
    public void setReferencenumber(String iReferencenumber) {
        this.iReferencenumber = iReferencenumber;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSCurrency getCurrency() {
        return iCurrency;
    }

    /**
     *
     * @param iCurrency
     */
    public void setCurrency(SSCurrency iCurrency) {
        this.iCurrency = iCurrency;
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
    public BigDecimal getTaxSum() {
        if(iTaxSum == null) iTaxSum = new BigDecimal(0);

        return iTaxSum;
    }

    /**
     *
     * @param iTaxSum
     */
    public void setTaxSum(BigDecimal iTaxSum) {
        this.iTaxSum = iTaxSum;
    }


    public BigDecimal getRoundingSum() {
        if(iRoundingSum == null) iRoundingSum = new BigDecimal(0);

        return iRoundingSum;
    }

    /**
     *
     * @param iRoundingSum
     */
    public void setRoundingSum(BigDecimal iRoundingSum) {
        this.iRoundingSum = iRoundingSum;
    }




    ////////////////////////////////////////////////////

    /**
     *
     * @param iSuppliers
     * @return
     */
    public SSSupplier getSupplier(List<SSSupplier> iSuppliers) {
        if(iSupplier == null){
            for (SSSupplier iCurrent : iSuppliers) {
                if(iCurrent.getNumber().equals(iSupplierNr)){
                    iSupplier = iCurrent;
                }
            }
        }
        return iSupplier;
    }

    public SSSupplier getSupplier()
    {
        return iSupplier;
    }

    /**
     *
     * @param iSupplier
     */
    public void setSupplier(SSSupplier iSupplier) {
        this.iSupplier   = iSupplier;
        iSupplierNr = iSupplier == null ? null : iSupplier.getNumber();

        if(iSupplier != null){
            iSupplierName     = iSupplier.getName();
            iCurrency         = iSupplier.getCurrency();
        }
    }
    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSSupplierInvoiceRow> getRows() {
        return iRows;
    }

    /**
     *
     * @param iRows
     */
    public void setRows(List<SSSupplierInvoiceRow> iRows) {
        this.iRows = iRows;
    }

    ////////////////////////////////////////////////////

    /**
     * Adds the rows from an supplierinvoice to this supplierinvoice
     *
     * @param iSupplierInvoice
     */
    public void append(SSSupplierInvoice iSupplierInvoice) {
        for (SSSupplierInvoiceRow iRow : iSupplierInvoice.iRows) {
            iRows.add( new SSSupplierInvoiceRow(iRow) );
        }
    }

    /**
     * Adds the rows from an purchaseorder to this supplierinvoice
     *
     * @param iPurchaseOrder
     */
    public void append(SSPurchaseOrder iPurchaseOrder) {
        for (SSPurchaseOrderRow iRow : iPurchaseOrder.getRows()) {

            SSSupplierInvoiceRow iMatchingRow = SSSupplierInvoiceMath.getMatchingRow(this, iRow);

            if( iMatchingRow != null){
                Integer iQuantity = iMatchingRow.getQuantity();

                if(iQuantity != null){
                    iMatchingRow.setQuantity( iQuantity + iRow.getQuantity() );
                } else {
                    iMatchingRow.setQuantity(  iRow.getQuantity() );
                }
            } else {
                iRows.add( new SSSupplierInvoiceRow(iRow) );
            }
        }
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
        Integer iAccountNumber = iDefaultAccounts.get(iDefaultAccount);

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
    public SSVoucher getCorrection() {
        return iCorrection;
    }

    /**
     *
     * @param iCorrection
     */
    public void setCorrection(SSVoucher iCorrection) {
        this.iCorrection = iCorrection;
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
     * @return
     */
    public boolean isBGCEntered() {
        return iBGCEntered;
    }

    /**
     *
     * @param iBGCEntered
     */
    public void setBGCEntered(boolean iBGCEntered) {
        this.iBGCEntered = iBGCEntered;
    }

    /**
     *
     */
    public void setBGCEntered() {
        iBGCEntered = true;
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
    public void setStockInfluencing(boolean iStockInfluencing){
        this.iStockInfluencing = iStockInfluencing;
    }


    ////////////////////////////////////////////////////

    
    public boolean equals(Object obj) {

        if (iNumber == null) {
            return false;
        }

        if (obj instanceof SSSupplierInvoice) {
            SSSupplierInvoice iSale = (SSSupplierInvoice) obj;

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
    public String toRenderString() {
        return iNumber == null ? "" : iNumber.toString();
    }

    /**
     *
     * @param iSupplier
     * @return
     */
    public boolean hasSupplier(SSSupplier iSupplier) {
        return (iSupplierNr != null) && iSupplierNr.equals(iSupplier.getNumber());
    }


    /**
     *
     * @param iProduct
     * @return
     */
    public boolean hasProduct(SSProduct iProduct) {
        for (SSSupplierInvoiceRow iRow : iRows) {
            if(iRow.hasProduct(iProduct)) return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
    public SSVoucher generateVoucher() {
        iVoucher = new SSVoucher();
        String iDescription = SSBundle.getBundle().getString("supplierinvoiceframe.voucherdescription");

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
        iVoucher.addVoucherRow(getDefaultAccount(iAccountPlan, SSDefaultAccount.SupplierDebt), null , iTotalSum);

        // Add roundingsum
        iVoucher.addVoucherRow( getDefaultAccount(iAccountPlan, SSDefaultAccount.Rounding), iRoundingSum);

        // Add the tax 1
        iVoucher.addVoucherRow( getDefaultAccount(iAccountPlan, SSDefaultAccount.IncommingTax), iTaxSum, null   );

        // Add all rows from the correction voucher
        for(SSVoucherRow iVoucherRow : iCorrection.getRows() ){
            iVoucher.addVoucherRow(new SSVoucherRow(iVoucherRow));
        }


        // Add all products
        for(SSSupplierInvoiceRow iRow : iRows){
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
                if(iRow.getDebet().compareTo(new BigDecimal(0)) == -1){
                    iRow.setCredit(iRow.getDebet().negate());
                    iRow.setDebet(null);
                }
            }
            else {
                if(iRow.getCredit().compareTo(new BigDecimal(0)) == -1){
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
