/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.data;


import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


// Trade Extensions specific imports

// Java specific imports

/**
 * @author Roger Björnstedt
 */
public class SSNewResultUnit implements Serializable, SSTableSearchable {

    /**
     * Constant for serialization versioning.
     */
    static final long serialVersionUID = 1L;

    /** */
    private String iNumber;

    /** */
    private String iName;

    /** */
    private String iDescription;

    /**
     * Default constructor.
     */
    public SSNewResultUnit() {
        iNumber = "";
        iName = "";
        iDescription = "";
    }

    /**
     * Default constructor.
     * @param number
     * @param name
     */
    public SSNewResultUnit(String number, String name) {
        iNumber = number;
        iName = name;
        iDescription = "";
    }

    public SSNewResultUnit(SSResultUnit iOld) {
        iNumber = String.valueOf(iOld.getNumber());
        iName = iOld.getName();
        iDescription = iOld.getDescription();
    }

    // /////////////////////////////////////////////////////////////////////////

    // /////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getNumber() {
        return iNumber;
    }

    // /////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param pNumber
     */
    public void setNumber(String pNumber) {
        iNumber = pNumber;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return iName;
    }

    /**
     *
     * @param pName
     */
    public void setName(String pName) {
        iName = pName;
    }

    // /////////////////////////////////////////////////////////////////////////

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

    // /////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if (obj instanceof SSNewResultUnit) {
            return ((SSNewResultUnit) obj).iNumber.equals(iNumber);
        }

        return super.equals(obj);
    }

    /**
     * @return
     */
    public String toRenderString() {
        return iNumber;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(iNumber);
        sb.append(" - ");
        sb.append(iName);
        sb.append(", ");
        sb.append(iDescription);
        return sb.toString();
    }

    public BigDecimal getResultUnitRevenueForMonth(SSMonth iMonth) {
        Double iInvoiceSum = 0.0;
        List<SSInvoice> iInvoices = SSDB.getInstance().getInvoices();

        for (SSInvoice iInvoice : iInvoices) {
            if (iMonth.isDateInMonth(iInvoice.getDate())) {
                for (SSSaleRow iRow : iInvoice.getRows()) {
                    if (iRow.getResultUnitNr() != null) {
                        if (iRow.getResultUnitNr().equals(iNumber)
                                && iRow.getSum() != null) {
                            iInvoiceSum += iRow.getSum().doubleValue()
                                    * iInvoice.getCurrencyRate().doubleValue();
                        }
                    }
                }
            }
        }

        List<SSCreditInvoice> iCreditInvoices = SSDB.getInstance().getCreditInvoices();
        Double iCreditInvoiceSum = 0.0;

        for (SSCreditInvoice iCreditInvoice : iCreditInvoices) {
            if (iMonth.isDateInMonth(iCreditInvoice.getDate())) {
                for (SSSaleRow iRow : iCreditInvoice.getRows()) {
                    if (iRow.getResultUnitNr() != null) {
                        if (iRow.getResultUnitNr().equals(iNumber)
                                && iRow.getSum() != null) {
                            iCreditInvoiceSum += iRow.getSum().doubleValue()
                                    * iCreditInvoice.getCurrencyRate().doubleValue();
                        }
                    }
                }
            }
        }
        return new BigDecimal(iInvoiceSum - iCreditInvoiceSum);
    }

} // End of class SSNewResultUnit
