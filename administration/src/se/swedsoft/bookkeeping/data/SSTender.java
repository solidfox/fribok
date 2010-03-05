package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.base.SSSale;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Tender
 * Date: 2006-mar-24
 * Time: 15:51:50
 */
public class SSTender extends SSSale {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    // Giltig tom
    private Date iExpires;

    public Integer getOrderNr() {
        return iOrderNr;
    }

    // Order nummer
    private Integer iOrderNr;

    //Valutakurs
    protected BigDecimal iCurrencyRate;

    // Ordern
    private transient SSOrder iOrder;


    ////////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSTender() {
        super();

    }

    /**
     * Copy constructor
     *
     * @param iTender
     */
    public SSTender(SSTender iTender) {
        copyFrom(iTender);
    }


    ////////////////////////////////////////////////////

    /**
     *
     * @param iTender
     */
    public void copyFrom(SSTender iTender) {
        super.copyFrom(iTender);

        this.iOrder        = iTender.iOrder;
        this.iOrderNr      = iTender.iOrderNr;
        this.iExpires      = iTender.iExpires;
        this.iCurrencyRate = iTender.iCurrencyRate;
    }


    ////////////////////////////////////////////////////

    /**
     * Sets the number of this sale as the maxinum mumber + 1
     */
    @Override
    public void doAutoIncrecement() {
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        int iNumber = iCompany.getAutoIncrement().getNumber("tender");

        for(SSTender iTender:  SSDB.getInstance().getTenders()){

            if(iTender.getNumber() != null && iTender.getNumber() > iNumber){
                iNumber = iTender.getNumber();
            }
        }
        setNumber(iNumber + 1);
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Date getExpires() {
        return iExpires;
    }

    /**
     *
     * @param iExpires
     */
    public void setExpires(Date iExpires) {
        this.iExpires = iExpires;
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
    public SSOrder getOrder() {
        return getOrder(SSDB.getInstance().getOrders());
    }
    /**
     *
     * @param iOrders
     * @return
     */
    public SSOrder getOrder(List<SSOrder> iOrders) {
        if(iOrder == null && iOrderNr != null){
            for (SSOrder iCurrent : iOrders) {
                if(iOrderNr.equals(iCurrent.getNumber())){
                    iOrder = iCurrent;
                }
            }
        }
        return iOrder;
    }
    

    /**
     *
     * @param iOrder
     */
    public void setOrder(SSOrder iOrder) {
        this.iOrder   = iOrder;
        this.iOrderNr = iOrder == null ? null : iOrder.getNumber();
    }

    public boolean hasOrder(SSOrder iOrder) {
        return iOrderNr != null && iOrderNr.equals(iOrder.getNumber());
    }

    ////////////////////////////////////////////////////

    /**
     * Returns if the tender is expires, ie new Date().after(iExpires)
     *
     * @return if the tender is expired
     */
    public boolean isExpired() {
        // Never expires
        if(iExpires == null) return false;

        return new Date().after(iExpires);
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
        if (!(obj instanceof SSTender)) {
            return false;
        }
        return iNumber.equals(((SSTender)obj).getNumber());
    }
}


