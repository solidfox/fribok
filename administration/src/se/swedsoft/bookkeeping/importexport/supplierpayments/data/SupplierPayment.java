package se.swedsoft.bookkeeping.importexport.supplierpayments.data;

import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.SSAddress;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import java.util.LinkedList;

/**
 * User: Andreas Lago
 * Date: 2006-aug-28
 * Time: 11:58:59
 */
public class SupplierPayment {

    private SSSupplierInvoice iInvoice;

    private BigDecimal iValue;

    // betalningskonto
    private String iAccount;

    private PaymentMethod iPaymentMethod;

    private Date iDate;

    private String iCurrency;


    /**
     *
     * @param iInvoice
     */
    public SupplierPayment(SSSupplierInvoice iInvoice) {
        this.iInvoice       = iInvoice;
        this.iPaymentMethod = PaymentMethod.BANKGIRO;
        this.iValue         = SSSupplierInvoiceMath.getSaldo(iInvoice.getNumber());
        this.iAccount       = getBankGiro();

        this.iDate          = iInvoice.getDueDate () == null ? new Date() : iInvoice.getDueDate ();
        this.iCurrency      = iInvoice.getCurrency() == null ? "SEK"      : iInvoice.getCurrency().getName();

    }
    ////////////////////////////////////////////////////////////////////////////////////////



    /**
     *
     * @return
     */
    public SSSupplierInvoice getSupplierInvoice() {
        return iInvoice;
    }

    /**
     *
     * @param iInvoice
     */
    public void setSupplierInvoice(SSSupplierInvoice iInvoice) {
        this.iInvoice = iInvoice;
    }

    ////////////////////////////////////////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public BigDecimal getValue() {
        if( iValue== null) iValue = new BigDecimal(0);
        return iValue;
    }

    /**
     *
     * @param iValue
     */
    public void setValue(BigDecimal iValue) {
        this.iValue = iValue;
    }
    ////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getAccount() {
        return iAccount;
    }

    /**
     *
     * @param iAccount
     */
    public void setAccount(String iAccount) {
        this.iAccount = iAccount;
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public PaymentMethod getPaymentMethod() {
        return iPaymentMethod;
    }

    /**
     *
     * @param iMethod
     */
    public void setPaymentMethod(PaymentMethod iMethod) {
        this.iPaymentMethod = iMethod;
    }
    ////////////////////////////////////////////////////////////////////////////////////////


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


    ////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Integer getNumber() {
        return iInvoice.getNumber();
    }

    /**
     *
     * @return
     */
    public String getReference() {
        return iInvoice.getReferencenumber();
    }


    /**
     *
     * @return
     */
    public String getCurrency() {
        return iCurrency;
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSSupplier getSupplier(){
        return iInvoice.getSupplier( SSDB.getInstance().getSuppliers() );
    }

    /**
     *
     * @return
     */
    public String getBankGiro() {
        SSSupplier iSupplier = iInvoice.getSupplier(SSDB.getInstance().getSuppliers() );

        return iSupplier == null ? null: iSupplier.getBankgiro();
    }

    /**
     *
     * @return
     */
    public String getPlusGiro() {
        SSSupplier iSupplier = iInvoice.getSupplier(SSDB.getInstance().getSuppliers() );

        return iSupplier == null ? null: iSupplier.getPlusgiro();
    }



    /**
     *
     * @return
     */
    public Integer getOutpaymentNumber() {
        SSSupplier iSupplier = iInvoice.getSupplier(SSDB.getInstance().getSuppliers() );

        return iSupplier == null ? null : iSupplier.getOutpaymentNumber();
    }

    /**
     *
     * @return
     */
    public SSAddress getAddress() {
        SSSupplier iSupplier = iInvoice.getSupplier(SSDB.getInstance().getSuppliers() );

        return iSupplier == null ? null: iSupplier.getAddress();
    }


    /**
     *
     * @param iPayments
     * @param iCurrency
     * @return
     */
    public static List<SupplierPayment> getPayments(List<SupplierPayment> iPayments, String iCurrency) {
        List<SupplierPayment> iFiltered = new LinkedList<SupplierPayment>();
        for (SupplierPayment iPayment : iPayments) {
            if( iCurrency.equals( iPayment.getCurrency() ) ){
                iFiltered.add(iPayment);
            }
        }
        return iFiltered;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p/>
     * The <code>equals</code> method implements an equivalence relation
     * on non-null object references:
     *
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     * @see #hashCode()
     */
    public boolean equals(Object obj) {
        if(obj instanceof SupplierPayment){
            SupplierPayment iPayment = (SupplierPayment) obj;
            return iPayment.getSupplierInvoice().equals(iInvoice);
        }
        return false;
    }
}
