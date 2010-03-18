package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.base.SSSale;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-30
 * Time: 14:42:47
 */
public class SSOrder extends SSSale {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    // Ert order/avtalsnummer
    private String iYourOrderNumber;
    // Beräknad leverans
    private String iEstimatedDelivery;

    // Faktura nummer
    private Integer iInvoiceNr;
    private Integer iPeriodicInvoiceNr;

    public Integer getPurchaseOrderNr() {
        return iPurchaseOrderNr;
    }

    // Inköpsorder nummer
    private Integer iPurchaseOrderNr;

    //Dölj enhetspris på följesedel
    private boolean iHideUnitprice;
    // Transient sales
    private transient SSInvoice       iInvoice;
    private transient SSPeriodicInvoice iPeriodicInvoice;
    private transient SSPurchaseOrder iPurchaseOrder;

    protected BigDecimal iCurrencyRate;

    ////////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSOrder() {
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        if(iCompany != null){
            setDelayInterest    ( iCompany.getDelayInterest()   );
            setText             ( iCompany.getStandardText( SSStandardText.Saleorder ));
            setTaxRate1         ( iCompany.getTaxRate1()   );
            setTaxRate2         ( iCompany.getTaxRate2()   );
            setTaxRate3         ( iCompany.getTaxRate3()   );
            setDefaultAccounts  ( iCompany.getDefaultAccounts());
            setOurContactPerson ( iCompany.getContactPerson  () );
            setEstimatedDelivery( iCompany.getEstimatedDelivery()  );
            setPaymentTerm      ( iCompany.getPaymentTerm());
            setDeliveryTerm     ( iCompany.getDeliveryTerm());
            setDeliveryWay      ( iCompany.getDeliveryWay());
            setCurrency         ( iCompany.getCurrency());
            iCurrencyRate = iCurrency.getExchangeRate();

        }
    }

    @Override
    public void setCustomer(SSCustomer iCustomer){
        super.setCustomer(iCustomer);
        iCurrencyRate = iCurrency.getExchangeRate();
    }

    /**
     * Copy constructor
     *
     * @param iOrder
     */
    public SSOrder(SSOrder iOrder) {
        copyFrom(iOrder);
    }
    /**
     *  Creates an order based on a tender
     *
     * @param iTender
     */
    public SSOrder(SSTender iTender) {
        copyFrom(iTender);

        iDate         = new Date();
        iCurrencyRate = iTender.getCurrencyRate();

        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();
        for (SSCustomer pCustomer : SSDB.getInstance().getCustomers()) {
            if (pCustomer.getNumber().equals(iCustomerNr)) {
                iHideUnitprice = pCustomer.getHideUnitprice();
            }
        }
        if(iCompany != null){
            iEstimatedDelivery = iCompany.getEstimatedDelivery();
            iText              = iCompany.getStandardText(SSStandardText.Saleorder);
        }
        iNumber = null;
    }


    ////////////////////////////////////////////////////

    /**
     * @param iOrder
     */
    public void copyFrom(SSOrder iOrder) {
        super.copyFrom(iOrder);

        iInvoice           = iOrder.iInvoice;
        iInvoiceNr         = iOrder.iInvoiceNr;
        iPeriodicInvoice   = iOrder.iPeriodicInvoice;
        iPeriodicInvoiceNr = iOrder.iPeriodicInvoiceNr;
        iPurchaseOrder     = iOrder.iPurchaseOrder;
        iPurchaseOrderNr   = iOrder.iPurchaseOrderNr;
        iYourOrderNumber   = iOrder.iYourOrderNumber;
        iEstimatedDelivery = iOrder.iEstimatedDelivery;
        iHideUnitprice     = iOrder.iHideUnitprice;
        iCurrencyRate      = iOrder.iCurrencyRate;
    }

    ////////////////////////////////////////////////////

    /**
     * Sets the number of this sale as the maxinum mumber + 1
     */
    @Override
    public void doAutoIncrecement() {
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        int iNumber = iCompany.getAutoIncrement().getNumber("order");

        List<SSOrder> iOrders = SSDB.getInstance().getOrders();
        for(SSOrder iOrder: iOrders){

            if(iOrder.getNumber() != null && iOrder.getNumber() > iNumber){
                iNumber = iOrder.getNumber();
            }
        }
        setNumber(iNumber + 1);
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
    public String getEstimatedDelivery() {
        return iEstimatedDelivery;
    }

    /**
     *
     * @param iEstimatedDelivery
     */
    public void setEstimatedDelivery(String iEstimatedDelivery) {
        this.iEstimatedDelivery = iEstimatedDelivery;

    }

    ////////////////////////////////////////////////////

    public void setHideUnitprice(boolean iHideUnitprice) {
        this.iHideUnitprice = iHideUnitprice;
    }

    /**
     *
     * @return
     */
    public boolean getHideUnitprice() {
        return iHideUnitprice;
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
        iInvoice        = null;
    }

    public Integer getPeriodicInvoiceNr() {
        return iPeriodicInvoiceNr;
    }

    /**
     *
     * @param iInvoiceNr
     */
    public void setPeriodicInvoiceNr(Integer iInvoiceNr) {
        iPeriodicInvoiceNr = iInvoiceNr;
        iPeriodicInvoice   = null;
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
            if(iCustomer!=null)
            {
                return iCustomer.getInvoiceCurrency().getExchangeRate();
            }
            else
            {
                return new BigDecimal(1.00);
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
    public SSInvoice getInvoice() {
        return getInvoice(SSDB.getInstance().getInvoices());
    }

    /**
     *
     * @param iInvoices
     * @return
     */
    public SSInvoice getInvoice(List<SSInvoice> iInvoices) {
        if(iInvoice == null && iInvoiceNr != null){
            for (SSInvoice iCurrent : iInvoices) {
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
    public void setInvoice(SSInvoice iInvoice) {
        this.iInvoice = iInvoice;
        iInvoiceNr    = iInvoice == null ? null : iInvoice.getNumber();
    }

    public SSPeriodicInvoice getPeriodicInvoice() {
        return getPeriodicInvoice(SSDB.getInstance().getPeriodicInvoices());
    }

    /**
     *
     * @param iPeriodicInvoices
     * @return
     */
    public SSPeriodicInvoice getPeriodicInvoice(List<SSPeriodicInvoice> iPeriodicInvoices) {
        if(iPeriodicInvoice == null && iPeriodicInvoiceNr != null){
            for (SSPeriodicInvoice iCurrent : iPeriodicInvoices) {
                if(iPeriodicInvoiceNr.equals(iCurrent.getNumber())){
                    iPeriodicInvoice = iCurrent;
                }
            }
        }
        return iPeriodicInvoice;
    }

    /**
     *
     * @param iPeriodicInvoice
     */
    public void setPeriodicInvoice(SSPeriodicInvoice iPeriodicInvoice) {
        this.iPeriodicInvoice = iPeriodicInvoice;
        iPeriodicInvoiceNr    = iPeriodicInvoice == null ? null : iPeriodicInvoice.getNumber();
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSPurchaseOrder getPurchaseOrder() {
        return getPurchaseOrder(SSDB.getInstance().getPurchaseOrders());
    }


    /**
     *
     * @param iPurchaseOrders
     * @return
     */
    public SSPurchaseOrder getPurchaseOrder(List<SSPurchaseOrder> iPurchaseOrders) {
        if(iPurchaseOrder == null && iPurchaseOrderNr != null){
            for (SSPurchaseOrder iCurrent : iPurchaseOrders) {
                if(iPurchaseOrderNr.equals(iCurrent.getNumber())){
                    iPurchaseOrder = iCurrent;
                }
            }
        }
        return iPurchaseOrder;
    }

    /**
     *
     * @param iPurchaseOrder
     */
    public void setPurchaseOrder(SSPurchaseOrder iPurchaseOrder) {
        this.iPurchaseOrder = iPurchaseOrder;
        iPurchaseOrderNr    = iPurchaseOrder == null ? null : iPurchaseOrder.getNumber();
    }

    ////////////////////////////////////////////////////


    /**
     * Returns true if this order is assosiated to the specified sales
     *
     * @param iInvoice
     * @return if the order is owner of the sales
     */
    public boolean hasInvoice(SSInvoice iInvoice) {
        return iInvoiceNr != null && iInvoiceNr.equals(iInvoice.getNumber());
    }

    /**
     * Returns if the order has any connected invoice
     *
     * @return if the order has any invoice
     */
    public boolean hasInvoice() {
        return iInvoiceNr != null;
    }

    public boolean hasPeriodicInvoice(SSPeriodicInvoice iPeriodicInvoice) {
        return iPeriodicInvoiceNr != null && iPeriodicInvoiceNr.equals(iPeriodicInvoice.getNumber());
    }

    /**
     * Returns if the order has any connected invoice
     *
     * @return if the order has any invoice
     */
    public boolean hasPeriodicInvoice() {
        return iPeriodicInvoiceNr != null;
    }

    public boolean hasPurchaseOrder(SSPurchaseOrder iPurchaseOrder) {
        return iPurchaseOrderNr != null && iPurchaseOrderNr.equals(iPurchaseOrder.getNumber());
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
        if (!(obj instanceof SSOrder)) {
            return false;
        }
        return iNumber.equals(((SSOrder)obj).getNumber());
    }


}
