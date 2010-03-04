package se.swedsoft.bookkeeping.data.base;

import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;
import se.swedsoft.bookkeeping.data.common.*;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.io.Serializable;
import java.util.*;
import java.math.BigDecimal;

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
     */
    public SSSale(SSSale iSale, Integer iNumber) {
        copyFrom(iSale);

        setNumber(iNumber);
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @param iSale
     */
    public void copyFrom(SSSale iSale) {
        this.iNumber                    = iSale.iNumber;
        this.iDate                      = iSale.iDate;
        this.iCustomerNr                = iSale.iCustomerNr;
        this.iCustomerName              = iSale.iCustomerName;
        this.iOurContactPerson          = iSale.iOurContactPerson;
        this.iYourContactPerson         = iSale.iYourContactPerson;
        this.iDelayInterest             = iSale.iDelayInterest;
        this.iCurrency                  = iSale.iCurrency;
        this.iPaymentTerm               = iSale.iPaymentTerm;
        this.iDeliveryTerm              = iSale.iDeliveryTerm;
        this.iDeliveryWay               = iSale.iDeliveryWay;
        this.iTaxFree                   = iSale.iTaxFree;
        this.iText                      = iSale.iText;
        this.iTaxRate1                  = iSale.iTaxRate1;
        this.iTaxRate2                  = iSale.iTaxRate2;
        this.iTaxRate3                  = iSale.iTaxRate3;
        this.iEuSaleCommodity           = iSale.iEuSaleCommodity;
        this.iEuSaleYhirdPartCommodity  = iSale.iEuSaleYhirdPartCommodity;
        this.iPrinted                   = iSale.iPrinted;
        this.iCustomer                  = null;
        this.iInvoiceAddress            = new SSAddress(iSale.iInvoiceAddress );
        this.iDeliveryAddress           = new SSAddress(iSale.iDeliveryAddress);
        this.iRows                      = new LinkedList<SSSaleRow>();
        this.iDefaultAccounts           = new HashMap<SSDefaultAccount, Integer>();

        // Copy all rows
        for(SSSaleRow iRow : iSale.iRows){
            this.iRows.add( new SSSaleRow(iRow) );
        }

        // Copy all default accounts
        for (SSDefaultAccount iDefaultAccount : iSale.getDefaultAccounts().keySet()) {
            this.iDefaultAccounts.put(iDefaultAccount, iSale.getDefaultAccounts().get(iDefaultAccount));
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
        if(iTaxRate1 == null) iTaxRate1 = new BigDecimal(25.0);
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
        if(iTaxRate2 == null) iTaxRate2 = new BigDecimal(12.0);
        this.iTaxRate2 = iTaxRate2;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getTaxRate3() {
        if(iTaxRate3 == null) iTaxRate3 = new BigDecimal(6.0);
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
        this.iText = iTenderText;
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
        this.iPrinted = true;
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
        this.iCustomer                  = iCustomer;
        this.iCustomerName              = iCustomer.getName();
        this.iCustomerNr                = iCustomer.getNumber();
        this.iYourContactPerson         = iCustomer.getYourContactPerson();
        this.iOurContactPerson          = iCustomer.getOurContactPerson();
        this.iPaymentTerm               = iCustomer.getPaymentTerm();
        this.iDeliveryTerm              = iCustomer.getDeliveryTerm();
        this.iDeliveryWay               = iCustomer.getDeliveryWay();
        this.iCurrency                  = iCustomer.getInvoiceCurrency();
        
        //this.iCurrency.setExchangeRate(iCustomer.getInvoiceCurrency() == null ? null : iCustomer.getInvoiceCurrency().getExchangeRate());
        this.iTaxFree                   = iCustomer.getTaxFree();
        this.iEuSaleCommodity           = iCustomer.getEuSaleCommodity();
        this.iEuSaleYhirdPartCommodity  = iCustomer.getEuSaleYhirdPartCommodity();


        this.iInvoiceAddress  = new SSAddress(iCustomer.getInvoiceAddress () );
        this.iDeliveryAddress = new SSAddress(iCustomer.getDeliveryAddress() );
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return the normalized tax rate 1
     */
    public BigDecimal getNormalizedTaxRate1() {
        return iTaxRate1 == null ? new BigDecimal(0.25) : iTaxRate1.scaleByPowerOfTen(-2);

    }

    /**
     *
     * @return the normalized tax rate 2
     */
    public BigDecimal getNormalizedTaxRate2() {
        return iTaxRate2 == null ? new BigDecimal(0.12) : iTaxRate2.scaleByPowerOfTen(-2);

    }

    /**
     *
     * @return the normalized tax rate 3
     */
    public BigDecimal getNormalizedTaxRate3() {
        return iTaxRate3 == null ? new BigDecimal(0.06) : iTaxRate3.scaleByPowerOfTen(-2);

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

        if (obj instanceof SSSale) {
            SSSale iSale = (SSSale) obj;

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
        sb.append(iCustomerNr);
        sb.append(", ");
        sb.append(iCustomerName);
        sb.append(") {\n");
        for(SSSaleRow iRow : iRows){
            sb.append("  ");
            sb.append(iRow);
            sb.append("\n");
        }
        sb.append("}");

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







}
