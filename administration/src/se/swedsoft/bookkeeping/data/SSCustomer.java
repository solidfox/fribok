package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.common.*;
import se.swedsoft.bookkeeping.data.SSAddress;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;
import se.swedsoft.bookkeeping.SSBookkeeping;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * User: Andreas Lago
 * Date: 2006-mar-22
 * Time: 15:07:47
 */
public class SSCustomer implements Serializable, SSTableSearchable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    // Kund nr:
    private String      iCustomerNr;
    // Namn
    private String      iName;
    // E-post
    private String      iEMail;
    // Telefon
    private String      iPhone;
    // Telefon 2
    private String      iPhone2;
    // Fax
    private String      iTelefax;
    // Orginisationsnummer
    private String      iRegistrationNumber;
    // Vår kontaktperson:
    private String      iOurContactPerson;
    // Er kontaktperson:
    private String      iYourContactPerson;


    // VAT nr:
    private String      iVATNumber;
    // Bankgiro nummer:
    private String      iBankAccountNumber;
    // Plusgiro nummer:
    private String      iPlusAccountNumber;
    // Kontonummer:
    private String      iAccountNumber;
    // Clearingnummer:
    private String      iClearingNumber;

    // EU-försäljning varor
    private boolean        iEuSaleCommodity;
    // EU-försäljning trepart varor
    private boolean        iEuSaleYhirdPartCommodity;
    // Momsfri försäljning
    private boolean        iVatFreeSale;
    //Göm enhetspris på följesedel
    private boolean        iHideUnitprice;

    // Fakureringsvaluta
    private SSCurrency     iInvoiceCurrency;
    // Betalningsvilkor
    private SSPaymentTerm  iPaymentTerm;
    // Leveransvilkor
    private SSDeliveryTerm iDeliveryTerm;
    // Leveranssätt
    private SSDeliveryWay  iDeliveryWay;

    // Kreditgräns
    private BigDecimal iCreditLimit;
    // Rabatt
    private BigDecimal iDiscount;

    // Fakturaadress
    private SSAddress  iInvoiceAddress;
    // Leveransadress
    private SSAddress  iDeliveryAddress;

    private String iComment;


    //////////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSCustomer() {
        iInvoiceAddress  = new SSAddress();
        iDeliveryAddress = new SSAddress();

        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();
        if(iCompany != null){
            iOurContactPerson = iCompany.getContactPerson();
            iInvoiceCurrency  = iCompany.getCurrency();
            iPaymentTerm = iCompany.getPaymentTerm();
            iDeliveryTerm = iCompany.getDeliveryTerm();
            iDeliveryWay = iCompany.getDeliveryWay();
        //     setCurrency         ( iCompany.getCurrency()  );
        }
    }

    /**
     * Copy constructor
     *
     * @param iCustomer
     */
    public SSCustomer(SSCustomer iCustomer) {
        this.iCustomerNr                = iCustomer.iCustomerNr;
        this.iName                      = iCustomer.iName;
        this.iEMail                     = iCustomer.iEMail;
        this.iPhone                     = iCustomer.iPhone;
        this.iPhone2                    = iCustomer.iPhone2;
        this.iTelefax                   = iCustomer.iTelefax;
        this.iRegistrationNumber        = iCustomer.iRegistrationNumber;
        this.iOurContactPerson          = iCustomer.iOurContactPerson;
        this.iYourContactPerson         = iCustomer.iYourContactPerson;
        this.iVATNumber                 = iCustomer.iVATNumber;
        this.iBankAccountNumber         = iCustomer.iBankAccountNumber;
        this.iPlusAccountNumber         = iCustomer.iPlusAccountNumber;
        this.iAccountNumber             = iCustomer.iAccountNumber;
        this.iClearingNumber            = iCustomer.iClearingNumber;
        this.iEuSaleCommodity           = iCustomer.iEuSaleCommodity;
        this.iEuSaleYhirdPartCommodity  = iCustomer.iEuSaleYhirdPartCommodity;
        this.iVatFreeSale               = iCustomer.iVatFreeSale;
        this.iHideUnitprice             = iCustomer.iHideUnitprice;
        this.iInvoiceCurrency           = iCustomer.iInvoiceCurrency;
        this.iPaymentTerm               = iCustomer.iPaymentTerm;
        this.iDeliveryTerm              = iCustomer.iDeliveryTerm;
        this.iDeliveryWay               = iCustomer.iDeliveryWay;
        this.iCreditLimit               = iCustomer.iCreditLimit;
        this.iDiscount                  = iCustomer.iDiscount;
        this.iInvoiceAddress            = new SSAddress(iCustomer.iInvoiceAddress );
        this.iDeliveryAddress           = new SSAddress(iCustomer.iDeliveryAddress);
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getNumber() {
        return iCustomerNr;
    }

    /**
     *
     * @param iNumber
     */
    public void setNumber(String iNumber) {
        this.iCustomerNr = iNumber;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getName() {
        return iName;
    }

    /**
     *
     * @param iName
     */
    public void setName(String iName) {
        this.iName = iName;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getEMail() {
        return iEMail;
    }

    /**
     *
     * @param iEMail
     */
    public void setEMail(String iEMail) {
        this.iEMail = iEMail;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getPhone1() {
        return iPhone;
    }

    /**
     *
     * @param iPhone
     */
    public void setPhone1(String iPhone) {
        this.iPhone = iPhone;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getPhone2() {
        return iPhone2;
    }

    /**
     *
     * @param iPhone2
     */
    public void setPhone2(String iPhone2) {
        this.iPhone2 = iPhone2;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getTelefax() {
        return iTelefax;
    }

    /**
     *
     * @param iTelefax
     */
    public void setTelefax(String iTelefax) {
        this.iTelefax = iTelefax;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getRegistrationNumber() {
        return iRegistrationNumber;
    }

    /**
     *
     * @param iRegistrationNumber
     */
    public void setRegistrationNumber(String iRegistrationNumber) {
        this.iRegistrationNumber = iRegistrationNumber;
    }

    //////////////////////////////////////////////////////

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

    //////////////////////////////////////////////////////

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

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getVATNumber() {
        return iVATNumber;
    }

    /**
     *
     * @param iVATNumber
     */
    public void setVATNumber(String iVATNumber) {
        this.iVATNumber = iVATNumber;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getBankgiro() {
        return iBankAccountNumber;
    }

    /**
     *
     * @param iBankAccountNumber
     */
    public void setBankgiro(String iBankAccountNumber) {
        this.iBankAccountNumber = iBankAccountNumber;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getPlusgiro() {
        return iPlusAccountNumber;
    }

    /**
     *
     * @param iPlusAccountNumber
     */
    public void setPlusgiro(String iPlusAccountNumber) {
        this.iPlusAccountNumber = iPlusAccountNumber;
    }

    //////////////////////////////////////////////////////

    public String getAccountNumber() {
        return iAccountNumber;
    }

    public void setAccountNumber(String iAccountNumber) {
        this.iAccountNumber = iAccountNumber;
    }

    public String getClearingNumber() {
        return iClearingNumber;
    }

    public void setClearingNumber(String iClearingNumber) {
        this.iClearingNumber = iClearingNumber;
    }

    //////////////////////////////////////////////////////

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

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean getEuSaleYhirdPartCommodity() {
        return iEuSaleYhirdPartCommodity;
    }

    /**
     *
     * @param iEuSaleYhirdPartCommodity
     */
    public void setEuSaleYhirdPartCommodity(boolean iEuSaleYhirdPartCommodity) {
        this.iEuSaleYhirdPartCommodity = iEuSaleYhirdPartCommodity;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean getHideUnitprice() {
        return iHideUnitprice;
    }

    /**
     * 
     * @param iHideUnitprice
     */
    public void setHideUnitprice(boolean iHideUnitprice) {
        this.iHideUnitprice = iHideUnitprice;
    }

    /**
     *
     * @return
     */
    public boolean getTaxFree() {
        return iVatFreeSale;
    }

    /**
     *
     * @param iTaxFree
     */
    public void setTaxFree(boolean iTaxFree) {
        this.iVatFreeSale = iTaxFree;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSCurrency getInvoiceCurrency() {
        return SSDB.getInstance().getCurrency(iInvoiceCurrency);
    }

    /**
     *
     * @param iInvoiceCurrency
     */
    public void setInvoiceCurrency(SSCurrency iInvoiceCurrency) {
        this.iInvoiceCurrency = iInvoiceCurrency;
    }

    //////////////////////////////////////////////////////

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

    //////////////////////////////////////////////////////

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

    //////////////////////////////////////////////////////

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

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getCreditLimit() {
        return iCreditLimit;
    }

    /**
     *
     * @param iCreditLimit
     */
    public void setCreditLimit(BigDecimal iCreditLimit) {
        this.iCreditLimit = iCreditLimit;
    }

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getDiscount() {
        return iDiscount;
    }

    /**
     *
     * @param iDiscount
     */
    public void setDiscount(BigDecimal iDiscount) {
        this.iDiscount = iDiscount;
    }

    //////////////////////////////////////////////////////

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

    //////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSAddress getDeliveryAddress() {
        return iDeliveryAddress;
    }

    public String getComment() {
        return iComment;
    }

    public void setComment(String iComment) {
        this.iComment = iComment;
    }

    /**
     *
     * @param iDeliveryAddress
     */
    public void setDeliveryAddress(SSAddress iDeliveryAddress) {
        this.iDeliveryAddress = iDeliveryAddress;
    }


    //////////////////////////////////////////////////////

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
        if(iCustomerNr == null){
            return false;
        }
        if(obj instanceof SSCustomer){
            SSCustomer iCustomer = (SSCustomer)obj;

            return iCustomerNr.equals(iCustomer.iCustomerNr);
        }
        return false;
    }

    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return iCustomerNr;
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
        return iCustomerNr == null ? super.hashCode() : iCustomerNr.hashCode();
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

        sb.append(iCustomerNr);
        sb.append(", ");
        sb.append(iName);

        return sb.toString();
    }


    public BigDecimal getCustomerRevenueForMonth(SSMonth iMonth) {
        Double iInvoiceSum = 0.0;
        Double iCreditInvoiceSum = 0.0;

        for (SSInvoice iInvoice : SSDB.getInstance().getInvoices()) {
            if(iMonth.isDateInMonth(iInvoice.getDate())){
                if(iInvoice.getCustomerNr() != null){
                    if(iInvoice.getCustomerNr().equals(iCustomerNr)){
                        iInvoiceSum += SSInvoiceMath.getNetSum(iInvoice).doubleValue()*iInvoice.getCurrencyRate().doubleValue();
                    }
                }

            }
        }

        for (SSCreditInvoice iCreditInvoice : SSDB.getInstance().getCreditInvoices()) {
            if(iMonth.isDateInMonth(iCreditInvoice.getDate())){
                if(iCreditInvoice.getCustomerNr() != null){
                    if(iCreditInvoice.getCustomerNr().equals(iCustomerNr)){
                        iCreditInvoiceSum += SSInvoiceMath.getNetSum(iCreditInvoice).doubleValue()*iCreditInvoice.getCurrencyRate().doubleValue();
                    }
                }
            }
        }
        return(new BigDecimal(iInvoiceSum-iCreditInvoiceSum));
    }


}
