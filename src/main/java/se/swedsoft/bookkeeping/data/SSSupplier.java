package se.swedsoft.bookkeeping.data;


import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.common.SSDeliveryTerm;
import se.swedsoft.bookkeeping.data.common.SSDeliveryWay;
import se.swedsoft.bookkeeping.data.common.SSPaymentTerm;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * Date: 2006-mar-20
 * Time: 15:59:06
 */
public class SSSupplier implements Serializable, SSTableSearchable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    // Leverantörsnummer
    private String iNumber;

    // Namn
    private String iName;
    // Telefon
    private String iPhone;
    // Telefon 2
    private String iPhone2;
    // Fax nummer
    private String iTeleFax;

    // E-Post
    private String iEmail;
    // Hemsida
    private String iHomepage;
    // Orginisationsnummer
    private String iRegistrationNumber;
    // Er kontaktperson
    private String iYourContact;
    // Vår kontaktperson
    private String iOurContact;
    // Vårt kund nummer
    private String iOurCustomerNr;

    // Bankgiro nummer:
    private String      iBankAccountNumber;
    // Plusgiro nummer:
    private String      iPlusAccountNumber;
    // utbetalningsnummer
    private Integer iOutpaymentNumber;

    // Valuta
    private SSCurrency     iCurrency;
    // Betalningsvilkor
    private SSPaymentTerm  iPaymentTerm;
    // Leveransvilkor
    private SSDeliveryTerm iDeliveryTerm;
    // Leveranssätt
    private SSDeliveryWay iDeliveryWay;

    // Address
    private SSAddress iAddress;

    private String iComment;

    // ////////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSSupplier() {
        iAddress = new SSAddress();

        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        if (iCompany != null) {
            iOurContact = iCompany.getContactPerson();
            iCurrency = iCompany.getCurrency();
            iPaymentTerm = iCompany.getPaymentTerm();
            iDeliveryTerm = iCompany.getDeliveryTerm();
            iDeliveryWay = iCompany.getDeliveryWay();
        }
    }

    /**
     * Copy constructor
     *
     * @param iSupplier
     */
    public SSSupplier(SSSupplier iSupplier) {
        iNumber = iSupplier.iNumber;
        iName = iSupplier.iName;
        iPhone = iSupplier.iPhone;
        iPhone2 = iSupplier.iPhone2;
        iTeleFax = iSupplier.iTeleFax;
        iEmail = iSupplier.iEmail;
        iHomepage = iSupplier.iHomepage;
        iRegistrationNumber = iSupplier.iRegistrationNumber;
        iYourContact = iSupplier.iYourContact;
        iOurContact = iSupplier.iOurContact;
        iOurCustomerNr = iSupplier.iOurCustomerNr;
        iBankAccountNumber = iSupplier.iBankAccountNumber;
        iPlusAccountNumber = iSupplier.iPlusAccountNumber;
        iCurrency = iSupplier.iCurrency;
        iPaymentTerm = iSupplier.iPaymentTerm;
        iDeliveryTerm = iSupplier.iDeliveryTerm;
        iDeliveryWay = iSupplier.iDeliveryWay;
        iAddress = new SSAddress(iSupplier.iAddress);
    }

    // ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getNumber() {
        return iNumber;
    }

    /**
     *
     * @param iSupplierNr
     */
    public void setNumber(String iSupplierNr) {
        iNumber = iSupplierNr;
    }

    // ////////////////////////////////////////////////////

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

    // ////////////////////////////////////////////////////

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

    // ////////////////////////////////////////////////////

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

    // ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getTelefax() {
        return iTeleFax;
    }

    /**
     *
     * @param iTelefax
     */
    public void setTelefax(String iTelefax) {
        iTeleFax = iTelefax;
    }

    // ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getEMail() {
        return iEmail;
    }

    /**
     *
     * @param iEmail
     */
    public void setEMail(String iEmail) {
        this.iEmail = iEmail;
    }

    // ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getHomepage() {
        return iHomepage;
    }

    /**
     *
     * @param iHomepage
     */
    public void setHomepage(String iHomepage) {
        this.iHomepage = iHomepage;
    }

    // ////////////////////////////////////////////////////

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

    // ////////////////////////////////////////////////////

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

    // ////////////////////////////////////////////////////

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

    // ////////////////////////////////////////////////////

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
    public void setBankGiro(String iBankAccountNumber) {
        this.iBankAccountNumber = iBankAccountNumber;
    }

    // ////////////////////////////////////////////////////

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
    public void setPlusGiro(String iPlusAccountNumber) {
        this.iPlusAccountNumber = iPlusAccountNumber;
    }

    // ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Integer getOutpaymentNumber() {
        return iOutpaymentNumber;
    }

    /**
     *
     * @param iOutpaymentNumber
     */
    public void setOutpaymentNumber(Integer iOutpaymentNumber) {
        this.iOutpaymentNumber = iOutpaymentNumber;
    }

    // ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSCurrency getCurrency() {
        return SSDB.getInstance().getCurrency(iCurrency);
    }

    /**
     *
     * @param iCurrency
     */
    public void setCurrency(SSCurrency iCurrency) {
        this.iCurrency = iCurrency;
    }

    // ////////////////////////////////////////////////////

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

    // ////////////////////////////////////////////////////

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

    // ////////////////////////////////////////////////////

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

    // ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSAddress getAddress() {
        return iAddress;
    }

    /**
     *
     * @param iAddress
     */
    public void setAddress(SSAddress iAddress) {
        this.iAddress = iAddress;
    }

    // ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getOurCustomerNr() {
        return iOurCustomerNr;
    }

    /**
     *
     * @param iOurCustomerNr
     */
    public void setOurCustomerNr(String iOurCustomerNr) {
        this.iOurCustomerNr = iOurCustomerNr;
    }

    public String getComment() {
        return iComment;
    }

    public void setComment(String iComment) {
        this.iComment = iComment;
    }

    // ////////////////////////////////////////////////////

    public boolean equals(Object obj) {
        if (iNumber == null) {
            return super.equals(obj);
        }

        if (obj instanceof SSSupplier) {
            SSSupplier iSupplier = (SSSupplier) obj;

            return iNumber.equals(iSupplier.iNumber);
        }
        return false;
    }

    public int hashCode() {
        if (iNumber != null) {
            return iNumber.hashCode();
        }
        return super.hashCode();
    }

    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return iNumber;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(iNumber);
        sb.append(", ");
        sb.append(iName);

        return sb.toString();
    }

    public BigDecimal getSupplierRevenueForMonth(SSMonth iMonth) {
        Double iSupplierInvoiceSum = 0.0;

        for (SSSupplierInvoice iSupplierInvoice : SSDB.getInstance().getSupplierInvoices()) {
            if (iMonth.isDateInMonth(iSupplierInvoice.getDate())) {
                if (iSupplierInvoice.getSupplierNr() != null) {
                    if (iSupplierInvoice.getSupplierNr().equals(iNumber)) {
                        iSupplierInvoiceSum += SSSupplierInvoiceMath.getNetSum(iSupplierInvoice).doubleValue()
                                * iSupplierInvoice.getCurrencyRate().doubleValue();
                    }
                }
            }
        }

        Double iSupplierCreditInvoiceSum = 0.0;

        for (SSSupplierCreditInvoice iSupplierCreditInvoice : SSDB.getInstance().getSupplierCreditInvoices()) {
            if (iMonth.isDateInMonth(iSupplierCreditInvoice.getDate())) {
                if (iSupplierCreditInvoice.getSupplierNr() != null) {
                    if (iSupplierCreditInvoice.getSupplierNr().equals(iNumber)) {
                        iSupplierCreditInvoiceSum += SSSupplierInvoiceMath.getNetSum(iSupplierCreditInvoice).doubleValue()
                                * iSupplierCreditInvoice.getCurrencyRate().doubleValue();
                    }
                }
            }
        }
        return new BigDecimal(iSupplierInvoiceSum - iSupplierCreditInvoiceSum);
    }

}
