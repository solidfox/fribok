package se.swedsoft.bookkeeping.data.common;

import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 2006-feb-13
 * Time: 09:04:18
 */
public class SSCurrency implements Serializable, SSTableSearchable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    private String iCode;

    private String iDescription;

    private BigDecimal iExchangeRate;


    /**
     *
     */
    public SSCurrency(){
        iCode         = null;
        iDescription  = null;
        iExchangeRate = new BigDecimal(1.00);
    }

    /**
     *
     * @param iCode
     * @param iDescription
     */
    public SSCurrency( String iCode, String iDescription){
        this.iCode        = iCode;
        this.iDescription = iDescription;
        iExchangeRate     = new BigDecimal(1.00);
    }

    /**
     *
     * @param iCurrency
     */
    public SSCurrency(SSCurrency iCurrency) {
        copyFrom(iCurrency);
    }

    ///////////////////////////////////////////////////////////////////////////



    /**
     *
     * @param iCurrency
     */
    public void copyFrom(SSCurrency iCurrency) {
        iCode         = iCurrency.iCode;
        iDescription  = iCurrency.iDescription;
        iExchangeRate = iCurrency.iExchangeRate;
    }


    /**
     *
     * @return
     */
    public String getName() {
        return iCode;
    }

    /**
     *
     * @param pCode
     */
    public void setName(String pCode) {
        iCode = pCode;
    }

    ///////////////////////////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public String getDescription() {
        return iDescription;
    }

    /**
     *
     * @param pDescription
     */
    public void setDescription(String pDescription) {
        iDescription = pDescription;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getExchangeRate() {
        if(iExchangeRate == null) iExchangeRate = new BigDecimal(1.00);
        return iExchangeRate;
    }

    /**
     *
     * @param iExchangeRate
     */
    public void setExchangeRate(BigDecimal iExchangeRate) {
        this.iExchangeRate = iExchangeRate;
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * @return
     */
    public String toRenderString() {
        return iCode ;
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
        return iCode;
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
        if (!(obj instanceof SSCurrency)) {
            return false;
        }
        return iCode.equals(((SSCurrency) obj).iCode);
    }


    /**
     * Returns the default currencies to our db (EUR, SEK, USD);
     *
     * @return the default currenices
     */
    public static List<SSCurrency> getDefaultCurrencies() {
        List<SSCurrency> iCurrencies = new LinkedList<SSCurrency>();

        iCurrencies.add( new SSCurrency("EUR", "Euro") );
        iCurrencies.add( new SSCurrency("SEK", "Svenska kronor") );
        iCurrencies.add( new SSCurrency("USD", "US Dollar") );

        return iCurrencies;
    }


}
