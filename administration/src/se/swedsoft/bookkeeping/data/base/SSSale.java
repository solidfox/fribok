package se.swedsoft.bookkeeping.data.base;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.common.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

/**
 * Base class that tender, orders and invouces inherit from
 * Date: 2006-mar-24
 * Time: 15:51:50
 */
public abstract class SSSale implements SSTableSearchable, Serializable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    // Nummer
    protected Integer iNumber;
    // Datum
    protected Date iDate;

    // Kund nummer
    protected String iCustomerNr;
    // Kund namn
    protected String iCustomerName;
    // Vår kontaktperson:
    protected String iOurContactPerson;
    // Er kontaktperson:
    protected String iYourContactPerson;
    // Dröjsmålsränta
    protected BigDecimal iDelayInterest;
    // Valuta
    protected SSCurrency iCurrency;
    // Betalningsvilkor
    protected SSPaymentTerm iPaymentTerm;
    // Leveransvilkor
    protected SSDeliveryTerm iDeliveryTerm;
    // Leveranssätt
    protected SSDeliveryWay iDeliveryWay;
    // Momsfritt
    protected boolean iTaxFree;
    // Offerttext
    protected String iText;
    // Moms 1
    protected BigDecimal iTaxRate1;
    // Moms 2
    protected BigDecimal iTaxRate2;
    // Moms 3
    protected BigDecimal iTaxRate3;
    // EU-försäljning varor
    private boolean iEuSaleCommodity;
    // EU-försäljning trepart varor
    private boolean iEuSaleYhirdPartCommodity;
    // Fakturaadress
    protected SSAddress iInvoiceAddress;
    // Leveransadress
    protected SSAddress iDeliveryAddress;
    // Utskriven
    protected boolean iPrinted;


    // Standard konton
    protected Map<SSDefaultAccount, Integer> iDefaultAccounts;


    // Rader
    protected List<SSSaleRow> iRows;

    // Transient reference to the customer.
    protected transient SSCustomer iCustomer;

    ////////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSSale() {
        iDate            = new Date();
        iInvoiceAddress  = new SSAddress();
        iDeliveryAddress = new SSAddress();
        iRows            = new LinkedList<SSSaleRow>();
        iPrinted         = false;
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();
        if (iCompany != null) {
            iPaymentTerm = iCompany.getPaymentTerm();
            
            iDeliveryTerm = iCompany.getDeliveryTerm();
            iDeliveryWay = iCompany.getDeliveryWay();
            iCurrency    = iCompany.getCurrency();
        }

    }

    /**
     * Copy constructor
     *
     * @param iSale
     */
    public SSSale(SSSale iSale) {
        copyFrom(iSale);
    }

    /**
     * Clone constructor
     *
     * @param iSale
     * @param iNumber
     */
    public SSSale(SSSale iSale, Integer iNumber) {
        copyFrom(iSale);

        this.iNumber = iNumber;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @param iSale
     */
    public void copyFrom(SSSale iSale) {
        iNumber                   = iSale.iNumber;
        iDate                     = iSale.iDate;
        iCustomerNr               = iSale.iCustomerNr;
        iCustomerName             = iSale.iCustomerName;
        iOurContactPerson         = iSale.iOurContactPerson;
        iYourContactPerson        = iSale.iYourContactPerson;
        iDelayInterest            = iSale.iDelayInterest;
        iCurrency                 = iSale.iCurrency;
        iPaymentTerm              = iSale.iPaymentTerm;
        iDeliveryTerm             = iSale.iDeliveryTerm;
        iDeliveryWay              = iSale.iDeliveryWay;
        iTaxFree                  = iSale.iTaxFree;
        iText                     = iSale.iText;
        iTaxRate1                 = iSale.iTaxRate1;
        iTaxRate2                 = iSale.iTaxRate2;
        iTaxRate3                 = iSale.iTaxRate3;
        iEuSaleCommodity          = iSale.iEuSaleCommodity;
        iEuSaleYhirdPartCommodity = iSale.iEuSaleYhirdPartCommodity;
        iPrinted                  = iSale.iPrinted;
        iCustomer                 = null;
        iInvoiceAddress           = new SSAddress(iSale.iInvoiceAddress );
        iDeliveryAddress          = new SSAddress(iSale.iDeliveryAddress);
        iRows                     = new LinkedList<SSSaleRow>();
        iDefaultAccounts          = new HashMap<SSDefaultAccount, Integer>();

        // Copy all rows
        for(SSSaleRow iRow : iSale.iRows){
            iRows.add( new SSSaleRow(iRow) );
        }

        // Copy all default accounts
        for (SSDefaultAccount iDefaultAccount : iSale.getDefaultAccounts().keySet()) {
            iDefaultAccounts.put(iDefaultAccount, iSale.getDefaultAccounts().get(iDefaultAccount));
        }
    }

    ////////////////////////////////////////////////////

    /**
     * Sets the number of this sale as the maxinum mumber + 1
     */
    public abstract void doAutoIncrecement();

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
    public String getCustomerNr() {
        return iCustomerNr;
    }

    /**
     *
     * @param iCustomerNr
     */
    public void setCustomerNr(String iCustomerNr) {
        this.iCustomerNr = iCustomerNr;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getCustomerName() {
        return iCustomerName;
    }

    /**
     *
     * @param iCustomerName
     */
    public void setCustomerName(String iCustomerName) {
        this.iCustomerName = iCustomerName;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getOurContactPerson() {
        return iOurContactPerson;
    }

    /**
     *
     * @param iOurContactPerson
     */
    public void setOurContactPerson(String iOurContactPerson) {
        this.iOurContactPerson = iOurContactPerson;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getYourContactPerson() {
        return iYourContactPerson;
    }

    /**
     *
     * @param iYourContactPerson
     */
    public void setYourContactPerson(String iYourContactPerson) {
        this.iYourContactPerson = iYourContactPerson;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getDelayInterest() {
        if(iDelayInterest == null) iDelayInterest = new BigDecimal(0);
        return iDelayInterest;
    }

    /**
     *
     * @param iDelayInterest
     */
    public void setDelayInterest(BigDecimal iDelayInterest) {
        this.iDelayInterest = iDelayInterest;
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

    ////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean getTaxFree() {
        return iTaxFree;
    }

    /**
     *
     * @param iTaxFree
     */
    public void setTaxFree(boolean iTaxFree) {
        this.iTaxFree = iTaxFree;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getTaxRate1() {
        if(iTaxRate1 == null) iTaxRate1 = new BigDecimal(25);
        return iTaxRate1;
    }

    /**
     *
     * @param iTaxRate1
     */
    public void setTaxRate1(BigDecimal iTaxRate1) {
        this.iTaxRate1 = iTaxRate1;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getTaxRate2() {
        return iTaxRate2;
    }

    /**
     *
     * @param iTaxRate2
     */
    public void setTaxRate2(BigDecimal iTaxRate2) {
        if(iTaxRate2 == null) iTaxRate2 = new BigDecimal(12);
        this.iTaxRate2 = iTaxRate2;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getTaxRate3() {
        if(iTaxRate3 == null) iTaxRate3 = new BigDecimal(6);
        return iTaxRate3;
    }

    /**
     *
     * @param iTaxRate3
     */
    public void setTaxRate3(BigDecimal iTaxRate3) {
        this.iTaxRate3 = iTaxRate3;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean getEuSaleCommodity() {
        return iEuSaleCommodity;
    }

    /**
     *
     * @param iEuSaleCommodity
     */
    public void setEuSaleCommodity(boolean iEuSaleCommodity) {
        this.iEuSaleCommodity = iEuSaleCommodity;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean getEuSaleThirdPartCommodity() {
        return iEuSaleYhirdPartCommodity;
    }

    /**
     *
     * @param iEuSaleYhirdPartCommodity
     */
    public void setEuSaleYhirdPartCommodity(boolean iEuSaleYhirdPartCommodity) {
        this.iEuSaleYhirdPartCommodity = iEuSaleYhirdPartCommodity;
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
     * @param iTenderText
     */
    public void setText(String iTenderText) {
        iText = iTenderText;
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
    public SSAddress getInvoiceAddress() {
        return iInvoiceAddress;
    }

    /**
     *
     * @param iInvoiceAddress
     */
    public void setInvoiceAddress(SSAddress iInvoiceAddress) {
        this.iInvoiceAddress = iInvoiceAddress;
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

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Map<SSDefaultAccount, Integer> getDefaultAccounts() {
        if(iDefaultAccounts == null) iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();
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
        this.iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();
        this.iDefaultAccounts.putAll(iDefaultAccounts);
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSSaleRow> getRows() {
        if(iRows == null) iRows = new LinkedList<SSSaleRow>();
        return iRows;
    }

    /**
     *
     * @param iRows
     */
    public void setRows(List<SSSaleRow> iRows) {
        this.iRows = iRows;
    }
    /**
     *
     * @param iRows
     */
    public void setRows(SSSaleRow ... iRows) {
        this.iRows = new LinkedList<SSSaleRow>();
        this.iRows.addAll(Arrays.asList(iRows));
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSCustomer getCustomer() {
        return getCustomer(SSDB.getInstance().getCustomers());
    }

    /**
     *
     * @param iCustomers
     * @return
     */
    public SSCustomer getCustomer(List<SSCustomer> iCustomers) {
        if(iCustomer == null){
            for(SSCustomer iCurrent: iCustomers){
                String iNumber = iCurrent.getNumber();

                if(iNumber != null && iNumber.equals(iCustomerNr)){
                    iCustomer = iCurrent;
                    break;
                }
            }
        }
        return iCustomer;
    }

    /**
     * Set the customer and copies the default information from the customer
     *
     * @param iCustomer
     */
    public void setCustomer(SSCustomer iCustomer) {
        this.iCustomer                                                  = iCustomer;
        iCustomerName                                                   = iCustomer.getName();
        iCustomerNr                                                     = iCustomer.getNumber();
        iYourContactPerson                                              = iCustomer.getYourContactPerson();
        iOurContactPerson                                               = iCustomer.getOurContactPerson();
        iPaymentTerm                                                    = iCustomer.getPaymentTerm();
        iDeliveryTerm                                                   = iCustomer.getDeliveryTerm();
        iDeliveryWay                                                    = iCustomer.getDeliveryWay();
        iCurrency                                                       = iCustomer.getInvoiceCurrency();
        
        //this.iCurrency.setExchangeRate(iCustomer.getInvoiceCurrency() == null ? null : iCustomer.getInvoiceCurrency().getExchangeRate());
        iTaxFree                                                        = iCustomer.getTaxFree();
        iEuSaleCommodity                                                = iCustomer.getEuSaleCommodity();
        iEuSaleYhirdPartCommodity                                       = iCustomer.getEuSaleYhirdPartCommodity();

        iInvoiceAddress                                                 = new SSAddress(iCustomer.getInvoiceAddress () );
        iDeliveryAddress                                                = new SSAddress(iCustomer.getDeliveryAddress() );
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return the normalized tax rate 1
     */
    public BigDecimal getNormalizedTaxRate1() {
        return iTaxRate1 == null ? new BigDecimal("0.25") : iTaxRate1.scaleByPowerOfTen(-2);

    }

    /**
     *
     * @return the normalized tax rate 2
     */
    public BigDecimal getNormalizedTaxRate2() {
        return iTaxRate2 == null ? new BigDecimal("0.12") : iTaxRate2.scaleByPowerOfTen(-2);

    }

    /**
     *
     * @return the normalized tax rate 3
     */
    public BigDecimal getNormalizedTaxRate3() {
        return iTaxRate3 == null ? new BigDecimal("0.06") : iTaxRate3.scaleByPowerOfTen(-2);

    }

    ////////////////////////////////////////////////////

    
    public boolean equals(Object obj) {

        if (iNumber == null) {
            return false;
        }

        if (obj instanceof SSSale) {
            SSSale iSale = (SSSale) obj;

            return iNumber.equals(iSale.iNumber);
        }
        return false;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.data.base.SSSale");
        sb.append("{iCurrency=").append(iCurrency);
        sb.append(", iCustomer=").append(iCustomer);
        sb.append(", iCustomerName='").append(iCustomerName).append('\'');
        sb.append(", iCustomerNr='").append(iCustomerNr).append('\'');
        sb.append(", iDate=").append(iDate);
        sb.append(", iDefaultAccounts=").append(iDefaultAccounts);
        sb.append(", iDelayInterest=").append(iDelayInterest);
        sb.append(", iDeliveryAddress=").append(iDeliveryAddress);
        sb.append(", iDeliveryTerm=").append(iDeliveryTerm);
        sb.append(", iDeliveryWay=").append(iDeliveryWay);
        sb.append(", iEuSaleCommodity=").append(iEuSaleCommodity);
        sb.append(", iEuSaleYhirdPartCommodity=").append(iEuSaleYhirdPartCommodity);
        sb.append(", iInvoiceAddress=").append(iInvoiceAddress);
        sb.append(", iNumber=").append(iNumber);
        sb.append(", iOurContactPerson='").append(iOurContactPerson).append('\'');
        sb.append(", iPaymentTerm=").append(iPaymentTerm);
        sb.append(", iPrinted=").append(iPrinted);
        sb.append(", iRows=").append(iRows);
        sb.append(", iTaxFree=").append(iTaxFree);
        sb.append(", iTaxRate1=").append(iTaxRate1);
        sb.append(", iTaxRate2=").append(iTaxRate2);
        sb.append(", iTaxRate3=").append(iTaxRate3);
        sb.append(", iText='").append(iText).append('\'');
        sb.append(", iYourContactPerson='").append(iYourContactPerson).append('\'');
        sb.append('}');
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







}
