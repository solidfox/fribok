package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-apr-07
 * Time: 10:57:04
 */
public class SSOutpaymentRow implements SSTableSearchable, Serializable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    // Fakturanummer
    private Integer iInvoiceNr;
    // Fakturans valuta
    private SSCurrency iInvoiceCurrency;
    // Fakturans kurs
    private BigDecimal iInvoiceCurrencyRate;

    // Inbetalt belopp
    private BigDecimal iValue;
    // Valutakursen för inbetalt belopp
    private BigDecimal iCurrencyRate;


    // The transistient supplier invoice
    private transient SSSupplierInvoice iInvoice;


    ////////////////////////////////////////////////////

    /**
     * Default constructor
     */
    public SSOutpaymentRow() {
    }

    /**
     * Default constructor
     * @param iInvoice
     */
    public SSOutpaymentRow(SSSupplierInvoice iInvoice) {
        setSupplierInvoice(iInvoice);
    }

    /**
     * Copy constructor
     *
     * @param iRow
     */
    public SSOutpaymentRow(SSOutpaymentRow iRow) {
        copyFrom(iRow);
    }

    ////////////////////////////////////////////////////


    /**
     *
     * @param iInpaymentRow
     */
    public void copyFrom(SSOutpaymentRow iInpaymentRow) {
        iInvoiceNr           = iInpaymentRow.iInvoiceNr;
        iInvoiceCurrency     = iInpaymentRow.iInvoiceCurrency;
        iInvoiceCurrencyRate = iInpaymentRow.iInvoiceCurrencyRate;
        iValue               = iInpaymentRow.iValue;
        iCurrencyRate        = iInpaymentRow.iCurrencyRate;

        iInvoice             = iInpaymentRow.iInvoice;
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
    public SSCurrency getInvoiceCurrency() {
        return iInvoiceCurrency;
    }

    /**
     *
     * @param iInvoiceCurrency
     */
    public void setInvoiceCurrency(SSCurrency iInvoiceCurrency) {
        this.iInvoiceCurrency = iInvoiceCurrency;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getInvoiceCurrencyRate() {
        return iInvoiceCurrencyRate;
    }

    /**
     *
     * @param iInvoiceExchangerate
     */
    public void setInvoiceCurrencyRate(BigDecimal iInvoiceExchangerate) {
        iInvoiceCurrencyRate = iInvoiceExchangerate;
    }

    ////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getValue() {
        return iValue;
    }

    /**
     *
     * @param iValue
     */
    public void setValue(BigDecimal iValue) {
        this.iValue = iValue;
    }

    ////////////////////////////////////////////////////



    /**
     *
     * @param iCurrencyRate
     */
    public void setCurrencyRate(BigDecimal iCurrencyRate) {
        this.iCurrencyRate = iCurrencyRate;
    }

    /**
     *
     * @return
     */
    public BigDecimal getCurrencyRate() {
        return iCurrencyRate;
    }

    ////////////////////////////////////////////////////


    /**
     * Returns the value in the company currency as
     *
     * iValue * iCurrencyRate
     *
     * @return
     */
    public BigDecimal getLocalValue() {

        if( iCurrencyRate == null || iValue == null) return null;

        return iCurrencyRate.multiply(iValue);
    }

    /**
     * Set value in the company currency such as
     *
     * iValue * iCurrencyRate = iLocalValue
     *
     * @param iLocalValue
     */
    public void setLocalValue(BigDecimal iLocalValue) {
        if( iCurrencyRate == null || iValue == null) return;

        iCurrencyRate = iLocalValue.divide(iValue, 5, RoundingMode.HALF_UP );
    }

    ////////////////////////////////////////////////////


    /**
     *
     * @param iInvoices
     * @return
     */
    public SSSupplierInvoice getSupplierInvoice(List<SSSupplierInvoice> iInvoices) {
        if(iInvoice == null && iInvoiceNr != null){
            for(SSSupplierInvoice iCurrent: iInvoices){
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
    public void setSupplierInvoice(SSSupplierInvoice iInvoice) {
        this.iInvoice = iInvoice;
        iInvoiceNr    = iInvoice == null ? null : iInvoice.getNumber();

        if( iInvoice != null){
            BigDecimal iSaldo    = SSSupplierInvoiceMath.getSaldo(iInvoice.getNumber());

            iInvoiceCurrency     = iInvoice.getCurrency();
            iInvoiceCurrencyRate = iInvoice.getCurrencyRate();
            iValue               = iSaldo;
            iCurrencyRate        = iInvoice.getCurrencyRate();
        }
    }

    ////////////////////////////////////////////////////


    /**
     * Returns if this row is paying the selected sales
     *
     * @param iInvoice
     * @return if the row is paying the sales
     */
    public boolean isPaying(SSSupplierInvoice iInvoice){
        boolean answer=false;
        if(iInvoice!=null){
            answer= iInvoice.getNumber().equals(iInvoiceNr);
        }
        return answer;
    }

    /**
     * Returns if this row is paying the selected sales
     *
     * @param iInvoice
     * @return if the row is paying the sales
     */
    public boolean isPaying(Integer iInvoice){
        return iInvoice != null && iInvoice.equals(iInvoiceNr);
    }


    ////////////////////////////////////////////////////





    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return String.valueOf(iInvoiceNr);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.data.SSOutpaymentRow");
        sb.append("{iCurrencyRate=").append(iCurrencyRate);
        sb.append(", iInvoice=").append(iInvoice);
        sb.append(", iInvoiceCurrency=").append(iInvoiceCurrency);
        sb.append(", iInvoiceCurrencyRate=").append(iInvoiceCurrencyRate);
        sb.append(", iInvoiceNr=").append(iInvoiceNr);
        sb.append(", iValue=").append(iValue);
        sb.append('}');
        return sb.toString();
    }
}
