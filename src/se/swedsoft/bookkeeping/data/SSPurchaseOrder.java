package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.common.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * User: Andreas Lago
 * Date: 2006-jun-15
 * Time: 10:07:35
 *
 * Inköpsorder
 */
public class SSPurchaseOrder implements SSTableSearchable, Serializable {

    private static final long serialVersionUID = 6529008747056659550L;




    // Nummer
    protected Integer iNumber;
    // Faktura nummer
    private Integer iInvoiceNr;
    // Datum
    protected Date iDate;
    // Leverantörsnummer
    protected String iSupplierNr;
    // Leverantörsnamn
    protected String iSupplierName;
    // Väntat leveransdatum
    protected Date iEstimatedDelivery;
    // Betalningsvilkor
    protected SSPaymentTerm iPaymentTerm;
    // Leveransvilkor
    protected SSDeliveryTerm iDeliveryTerm;
    // Leveranssätt
    protected SSDeliveryWay iDeliveryWay;
    // Vår kontakt
    protected String iOurContact;
    // Er kontakt
    protected String iYourContact;
    // Valuta
    protected SSCurrency iCurrency;
    //Valutakurs
    protected BigDecimal iCurrencyRate;

    // Vår leveransaddress
    protected SSAddress iDeliveryAddress;
    // Leverantörens address
    protected SSAddress iSupplierAddress;
    // Frivillig text
    private String iText;
    // Utskriven
    protected boolean iPrinted;

    private boolean iStockInfluencing;

    protected List<SSPurchaseOrderRow> iRows;
    // Standardkonton
    protected Map<SSDefaultAccount, Integer> iDefaultAccounts;


    // The transient supplier
    protected transient SSSupplier iSupplier;

    // Transient sales
    private transient SSSupplierInvoice iInvoice;



    ////////////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSPurchaseOrder() {
        iRows              = new LinkedList<SSPurchaseOrderRow>();
        iDeliveryAddress   = new SSAddress();
        iSupplierAddress   = new SSAddress();
        iDefaultAccounts   = new HashMap<SSDefaultAccount, Integer>();
        iEstimatedDelivery = new Date();
        iStockInfluencing  = true;

        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();
        if(iCompany != null){
            setDefaultAccounts(iCompany.getDefaultAccounts());
            iDeliveryAddress = new SSAddress(iCompany.getDeliveryAddress());
            iText = iCompany.getStandardText(SSStandardText.Purchaseorder);
        }
        if(iSupplier != null)
        {
            iCurrency = iSupplier.getCurrency();
            iCurrencyRate = iCurrency.getExchangeRate();
        }
    }

    /**
     * Copy constructor
     *
     * @param iPurchaseOrder
     */
    public SSPurchaseOrder(SSPurchaseOrder iPurchaseOrder) {
        iNumber            = iPurchaseOrder.iNumber;
        iDate              = iPurchaseOrder.iDate;
        iSupplierNr        = iPurchaseOrder.iSupplierNr;
        iSupplierName      = iPurchaseOrder.iSupplierName;
        iEstimatedDelivery = iPurchaseOrder.iEstimatedDelivery;
        iPaymentTerm       = iPurchaseOrder.iPaymentTerm;
        iDeliveryTerm      = iPurchaseOrder.iDeliveryTerm;
        iDeliveryWay       = iPurchaseOrder.iDeliveryWay;
        iOurContact        = iPurchaseOrder.iOurContact;
        iYourContact       = iPurchaseOrder.iYourContact;
        iCurrency          = iPurchaseOrder.iCurrency;
        iSupplier          = iPurchaseOrder.iSupplier;
        iText              = iPurchaseOrder.iText;
        iInvoice           = iPurchaseOrder.iInvoice;
        iInvoiceNr         = iPurchaseOrder.iInvoiceNr;
        iPrinted           = iPurchaseOrder.iPrinted;
        iCurrencyRate      = iPurchaseOrder.iCurrencyRate;
        iDeliveryAddress   = new SSAddress(iPurchaseOrder.iDeliveryAddress);
        iSupplierAddress   = new SSAddress(iPurchaseOrder.iSupplierAddress);
        iRows              = new LinkedList<SSPurchaseOrderRow>();

        for (SSPurchaseOrderRow iRow : iPurchaseOrder.iRows) {
            iRows.add(new SSPurchaseOrderRow(iRow) );
        }
        // Copy all default accounts
        iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();
        iDefaultAccounts.putAll( iPurchaseOrder.getDefaultAccounts() );

    }

    public SSPurchaseOrder(List<SSProduct> iProducts, SSSupplier iSupplier) {
        setSupplier(iSupplier);
        iRows                 = new LinkedList<SSPurchaseOrderRow>();
        iDefaultAccounts      = new HashMap<SSDefaultAccount, Integer>();
        iStockInfluencing     = true;
        iEstimatedDelivery    = Calendar.getInstance().getTime();
        iDate                 = Calendar.getInstance().getTime();
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();
        if(iCompany != null){
            setDefaultAccounts(iCompany.getDefaultAccounts());
            iDeliveryAddress = new SSAddress(iCompany.getDeliveryAddress());
            iText            = iCompany.getStandardText(SSStandardText.Purchaseorder);
        }
        if(this.iSupplier != null){
            iCurrency     = this.iSupplier.getCurrency();
            iCurrencyRate = iCurrency == null ? null : iCurrency.getExchangeRate();
        }

        for (SSProduct iProduct : iProducts) {
            SSProduct iOriginal = SSDB.getInstance().getProduct(iProduct);
            if(iOriginal != null){
                SSPurchaseOrderRow iRow = new SSPurchaseOrderRow();
                iRow.setProduct(iOriginal);
                iRow.setQuantity(iProduct.getOrdercount());
                iRows.add(iRow);
            }
        }
    }

    ////////////////////////////////////////////////////////


    /**
     * Sets the number of this purchase order as the maxinum mumber + 1
     */
    public void doAutoIncrecement() {
        List<SSPurchaseOrder> iPurchaseOrders = SSDB.getInstance().getPurchaseOrders();

        int iNumber = SSDB.getInstance().getAutoIncrement().getNumber("purchaseorder");

        for (SSPurchaseOrder iPurchaseOrder : iPurchaseOrders) {
            if(iPurchaseOrder.iNumber > iNumber ){
                iNumber  = iPurchaseOrder.iNumber;
            }
        }
        this.iNumber = iNumber + 1;
    }

    ////////////////////////////////////////////////////////
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


    /**
     *
     * @return
     */
    public Date getEstimatedDelivery() {
        return iEstimatedDelivery;
    }

    /**
     *
     * @param iEstimatedDelivery
     */
    public void setEstimatedDelivery(Date iEstimatedDelivery) {
        this.iEstimatedDelivery = iEstimatedDelivery;
    }

    /**
     *
     * @return
     */
    public SSPaymentTerm getPaymentTerm() {
        return iPaymentTerm;
    }

    /**
     *
     * @param iPaymentTerm
     */
    public void setPaymentTerm(SSPaymentTerm iPaymentTerm) {
        this.iPaymentTerm = iPaymentTerm;
    }

    /**
     *
     * @return
     */
    public SSDeliveryTerm getDeliveryTerm() {
        return iDeliveryTerm;
    }

    /**
     *
     * @param iDeliveryTerm
     */
    public void setDeliveryTerm(SSDeliveryTerm iDeliveryTerm) {
        this.iDeliveryTerm = iDeliveryTerm;
    }

    /**
     *
     * @return
     */
    public SSDeliveryWay getDeliveryWay() {
        return iDeliveryWay;
    }

    /**
     *
     * @param iDeliveryWay
     */
    public void setDeliveryWay(SSDeliveryWay iDeliveryWay) {
        this.iDeliveryWay = iDeliveryWay;
    }

    /**
     *
     * @return
     */
    public String getOurContact() {
        return iOurContact;
    }

    /**
     *
     * @param iOurContact
     */
    public void setOurContact(String iOurContact) {
        this.iOurContact = iOurContact;
    }

    /**
     *
     * @return
     */
    public String getYourContact() {
        return iYourContact;
    }

    /**
     *
     * @param iYourContact
     */
    public void setYourContact(String iYourContact) {
        this.iYourContact = iYourContact;
    }

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
        if(iCurrencyRate!=null)
        {
            return iCurrencyRate;
        }
        else
        {
            if(iSupplier!=null && iSupplier.getCurrency() != null)
            {
                return iSupplier.getCurrency().getExchangeRate();
            }
            else
            {
                return new BigDecimal(1);
            }
        }
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
    public SSAddress getDeliveryAddress() {
        return iDeliveryAddress;
    }

    /**
     *
     * @param iDeliveryAddress
     */
    public void setDeliveryAddress(SSAddress iDeliveryAddress) {
        this.iDeliveryAddress = iDeliveryAddress;
    }

    /**
     *
     * @return
     */
    public SSAddress getSupplierAddress() {
        return iSupplierAddress;
    }

    /**
     *
     * @param iSupplierAddress
     */
    public void setSupplierAddress(SSAddress iSupplierAddress) {
        this.iSupplierAddress = iSupplierAddress;
    }


    /**
     *
     * @param iText
     */
    public void setText(String iText){
        this.iText = iText;
    }

    /**
     *
     * @return
     */
    public String getText() {
        return iText;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean isPrinted() {
        return iPrinted;
    }

    /**
     *
     * @param iPrinted
     */
    public void setPrinted(boolean iPrinted) {
        this.iPrinted = iPrinted;
    }

    /**
     *
     */
    public void setPrinted() {
        iPrinted = true;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSPurchaseOrderRow> getRows() {
        return iRows;
    }

    /**
     *
     * @param iRows
     */
    public void setRows(List<SSPurchaseOrderRow> iRows) {
        this.iRows = iRows;
    }

    /**
     *
     * @return
     */
    public SSSupplier getSupplier() {
        return getSupplier(SSDB.getInstance().getSuppliers());
    }

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

    /**
     *
     * @param iSupplier
     */
    public void setSupplier(SSSupplier iSupplier) {
        this.iSupplier   = iSupplier;
        iSupplierNr = iSupplier == null ? null : iSupplier.getNumber();

        if(iSupplier != null){
            iSupplierName     = iSupplier.getName();
            iSupplierAddress  = iSupplier.getAddress().clone();
            iOurContact       = iSupplier.getOurContact();
            iYourContact      = iSupplier.getYourContact();
            iPaymentTerm      = iSupplier.getPaymentTerm();
            iDeliveryTerm     = iSupplier.getDeliveryTerm();
            iDeliveryWay      = iSupplier.getDeliveryWay();
            iCurrency         = iSupplier.getCurrency();
        }
    }

    /**
     *
     * @return
     */
    public Map<SSDefaultAccount, Integer> getDefaultAccounts(){
        if(iDefaultAccounts == null) iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();
        return iDefaultAccounts;
    }

    /**
     * 
     * @param iDefaultAccounts
     */
    public void setDefaultAccounts(Map<SSDefaultAccount, Integer> iDefaultAccounts){
        this.iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();
        this.iDefaultAccounts.putAll(iDefaultAccounts);
    }

    ////////////////////////////////////////////////////
    /**
     *
     * @return
     */
    public Integer getInvoiceNr() {
        return iInvoiceNr;
    }

    /**
     *
     * @param iInvoiceNr
     */
    public void setInvoiceNr(Integer iInvoiceNr) {
        this.iInvoiceNr = iInvoiceNr;
        iInvoice = null;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSSupplierInvoice getInvoice() {
        return getInvoice(SSDB.getInstance().getSupplierInvoices());
    }

    /**
     *
     * @param iInvoices
     * @return
     */
    public SSSupplierInvoice getInvoice(List<SSSupplierInvoice> iInvoices) {
        if(iInvoice == null && iInvoiceNr != null){
            for (SSSupplierInvoice iCurrent : iInvoices) {
                if(iInvoiceNr.equals(iCurrent.getNumber())){
                    iInvoice = iCurrent;
                }
            }
        }
        return iInvoice;
    }

    /**
     *
     * @param iInvoice
     */
    public void setInvoice(SSSupplierInvoice iInvoice) {
        this.iInvoice   = iInvoice;
        iInvoiceNr = iInvoice == null ? null : iInvoice.getNumber();
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

    ////////////////////////////////////////////////////////

    /**
     * Returns the sum of the <code>SSPurchaseOrderRow.getSum()</code> for all rows.
     *
     * @return the sum
     */
    public BigDecimal getSum(){
        BigDecimal iSum = new BigDecimal(0);

        for (SSPurchaseOrderRow iRow : iRows) {
            BigDecimal iRowSum = iRow.getSum();

            if(iRowSum != null) iSum = iSum.add( iRowSum );
        }
        return iSum;
    }

    ////////////////////////////////////////////////////////

    
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(iNumber);

        return sb.toString();
    }


    
    public boolean equals(Object obj) {
        if(obj instanceof SSPurchaseOrder){
            SSPurchaseOrder iPurchaseOrder = (SSPurchaseOrder) obj;

            return iPurchaseOrder.iNumber.equals(iNumber);
        }
        return false;
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
        return String.valueOf(iNumber);
    }

    /**
     * Returns true if this order is has any invoice
     *
     * @return if the order is owner of the sales
     */
    public boolean hasInvoice() {
        return iInvoiceNr != null;
    }


    /**
     * Returns true if this order is assosiated to the specified invoice
     *
     * @param iInvoice
     * @return if the order is owner of the sales
     */
    public boolean hasInvoice(SSSupplierInvoice iInvoice) {
        return iInvoiceNr != null && iInvoiceNr.equals(iInvoice.getNumber());
    }

    /**
     *  Returns true of this order has the specified supplier
     * @param iSupplier
     * @return
     */
    public boolean hasSupplier(SSSupplier iSupplier) {
        return iSupplierNr != null && iSupplierNr.equals(iSupplier.getNumber());
    }



}
