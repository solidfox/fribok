package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.common.SSDeliveryTerm;
import se.swedsoft.bookkeeping.data.common.SSDeliveryWay;
import se.swedsoft.bookkeeping.data.common.SSPaymentTerm;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Hashtable;

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
            iPaymentTerm      = iCompany.getPaymentTerm();
            iDeliveryTerm     = iCompany.getDeliveryTerm();
            iDeliveryWay      = iCompany.getDeliveryWay();
        //     setCurrency         ( iCompany.getCurrency()  );
        }
    }

    /**
     * Copy constructor
     *
     * @param iCustomer
     */
    public SSCustomer(SSCustomer iCustomer) {
        iCustomerNr               = iCustomer.iCustomerNr;
        iName                     = iCustomer.iName;
        iEMail                    = iCustomer.iEMail;
        iPhone                    = iCustomer.iPhone;
        iPhone2                   = iCustomer.iPhone2;
        iTelefax                  = iCustomer.iTelefax;
        iRegistrationNumber       = iCustomer.iRegistrationNumber;
        iOurContactPerson         = iCustomer.iOurContactPerson;
        iYourContactPerson        = iCustomer.iYourContactPerson;
        iVATNumber                = iCustomer.iVATNumber;
        iBankAccountNumber        = iCustomer.iBankAccountNumber;
        iPlusAccountNumber        = iCustomer.iPlusAccountNumber;
        iAccountNumber            = iCustomer.iAccountNumber;
        iClearingNumber           = iCustomer.iClearingNumber;
        iEuSaleCommodity          = iCustomer.iEuSaleCommodity;
        iEuSaleYhirdPartCommodity = iCustomer.iEuSaleYhirdPartCommodity;
        iVatFreeSale              = iCustomer.iVatFreeSale;
        iHideUnitprice            = iCustomer.iHideUnitprice;
        iInvoiceCurrency          = iCustomer.iInvoiceCurrency;
        iPaymentTerm              = iCustomer.iPaymentTerm;
        iDeliveryTerm             = iCustomer.iDeliveryTerm;
        iDeliveryWay              = iCustomer.iDeliveryWay;
        iCreditLimit              = iCustomer.iCreditLimit;
        iDiscount                 = iCustomer.iDiscount;
        iInvoiceAddress           = new SSAddress(iCustomer.iInvoiceAddress );
        iDeliveryAddress          = new SSAddress(iCustomer.iDeliveryAddress);
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
        iCustomerNr = iNumber;
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
        iVatFreeSale = iTaxFree;
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

    
    public int hashCode() {
        return iCustomerNr == null ? super.hashCode() : iCustomerNr.hashCode();
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.data.SSCustomer");
        sb.append("{iAccountNumber='").append(iAccountNumber).append('\'');
        sb.append(", iBankAccountNumber='").append(iBankAccountNumber).append('\'');
        sb.append(", iClearingNumber='").append(iClearingNumber).append('\'');
        sb.append(", iComment='").append(iComment).append('\'');
        sb.append(", iCreditLimit=").append(iCreditLimit);
        sb.append(", iCustomerNr='").append(iCustomerNr).append('\'');
        sb.append(", iDeliveryAddress=").append(iDeliveryAddress);
        sb.append(", iDeliveryTerm=").append(iDeliveryTerm);
        sb.append(", iDeliveryWay=").append(iDeliveryWay);
        sb.append(", iDiscount=").append(iDiscount);
        sb.append(", iEMail='").append(iEMail).append('\'');
        sb.append(", iEuSaleCommodity=").append(iEuSaleCommodity);
        sb.append(", iEuSaleYhirdPartCommodity=").append(iEuSaleYhirdPartCommodity);
        sb.append(", iHideUnitprice=").append(iHideUnitprice);
        sb.append(", iInvoiceAddress=").append(iInvoiceAddress);
        sb.append(", iInvoiceCurrency=").append(iInvoiceCurrency);
        sb.append(", iName='").append(iName).append('\'');
        sb.append(", iOurContactPerson='").append(iOurContactPerson).append('\'');
        sb.append(", iPaymentTerm=").append(iPaymentTerm);
        sb.append(", iPhone='").append(iPhone).append('\'');
        sb.append(", iPhone2='").append(iPhone2).append('\'');
        sb.append(", iPlusAccountNumber='").append(iPlusAccountNumber).append('\'');
        sb.append(", iRegistrationNumber='").append(iRegistrationNumber).append('\'');
        sb.append(", iTelefax='").append(iTelefax).append('\'');
        sb.append(", iVatFreeSale=").append(iVatFreeSale);
        sb.append(", iVATNumber='").append(iVATNumber).append('\'');
        sb.append(", iYourContactPerson='").append(iYourContactPerson).append('\'');
        sb.append('}');
        return sb.toString();
    }


    public BigDecimal getCustomerRevenueForMonth(SSMonth iMonth) {
        Double iInvoiceSum = 0.0;

        for (SSInvoice iInvoice : SSDB.getInstance().getInvoices()) {
            if(iMonth.isDateInMonth(iInvoice.getDate())){
                if(iInvoice.getCustomerNr() != null){
                    if(iInvoice.getCustomerNr().equals(iCustomerNr)){
                        iInvoiceSum += SSInvoiceMath.getNetSum(iInvoice).doubleValue()*iInvoice.getCurrencyRate().doubleValue();
                    }
                }

            }
        }

        Double iCreditInvoiceSum = 0.0;
        for (SSCreditInvoice iCreditInvoice : SSDB.getInstance().getCreditInvoices()) {
            if(iMonth.isDateInMonth(iCreditInvoice.getDate())){
                if(iCreditInvoice.getCustomerNr() != null){
                    if(iCreditInvoice.getCustomerNr().equals(iCustomerNr)){
                        iCreditInvoiceSum += SSInvoiceMath.getNetSum(iCreditInvoice).doubleValue()*iCreditInvoice.getCurrencyRate().doubleValue();
                    }
                }
            }
        }
        return new BigDecimal(iInvoiceSum-iCreditInvoiceSum);
    }


}
