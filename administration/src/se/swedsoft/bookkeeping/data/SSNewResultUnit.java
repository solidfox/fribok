/*
 * @(#)SSNewResultUnit.java                v 1.0 2005-nov-08
 *
 * Time-stamp: <2005-nov-08 20:05:02 Hasse>
 *
 * Copyright (c) Trade Extensions TradeExt AB, Sweden.
 * www.tradeextensions.com, info@tradeextensions.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Trade Extensions ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Trade Extensions.
 */
package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.util.List;
import java.text.DateFormat;
import java.math.BigDecimal;

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
        iNumber      = "";
        iName        = "";
        iDescription = "";
    }

    /**
     * Default constructor.
     */
    public SSNewResultUnit(String number, String name) {
        iNumber      = number;
        iName        = name;
        iDescription = "";
    }


    public SSNewResultUnit(SSResultUnit iOld) {
        iNumber = ""+iOld.getNumber();
        iName = iOld.getName();
        iDescription = iOld.getDescription();
    }
    ///////////////////////////////////////////////////////////////////////////



    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getNumber() {
        return iNumber;
    }

    ///////////////////////////////////////////////////////////////////////////

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
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if(obj instanceof SSNewResultUnit)
            return (((SSNewResultUnit)obj).iNumber).equals(iNumber);

        return super.equals(obj);
    }

    /**
     * @return
     */
    public String toRenderString() {
        return iNumber;
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

        sb.append( iNumber );
        sb.append( " - " );
        sb.append( iName);
        sb.append( ", " );
        sb.append( iDescription );
        return sb.toString();
    }

    public BigDecimal getResultUnitRevenueForMonth(SSMonth iMonth) {
        Double iInvoiceSum = 0.0;
        Double iCreditInvoiceSum = 0.0;
        List<SSInvoice> iInvoices = SSDB.getInstance().getInvoices();
        for (SSInvoice iInvoice : iInvoices) {
            if(iMonth.isDateInMonth(iInvoice.getDate())){
                for (SSSaleRow iRow : iInvoice.getRows()) {
                    if(iRow.getResultUnitNr() != null){
                        if (iRow.getResultUnitNr().equals(iNumber) && iRow.getSum()!=null) {
                            iInvoiceSum += iRow.getSum().doubleValue()*iInvoice.getCurrencyRate().doubleValue();
                        }
                    }
                }
            }
        }

        List<SSCreditInvoice> iCreditInvoices = SSDB.getInstance().getCreditInvoices();
        for (SSCreditInvoice iCreditInvoice : iCreditInvoices) {
            if(iMonth.isDateInMonth(iCreditInvoice.getDate())){
                for (SSSaleRow iRow : iCreditInvoice.getRows()) {
                    if(iRow.getResultUnitNr() != null) {
                        if (iRow.getResultUnitNr().equals(iNumber) && iRow.getSum()!=null) {
                            iCreditInvoiceSum += iRow.getSum().doubleValue()*iCreditInvoice.getCurrencyRate().doubleValue();
                        }
                    }
                }
            }
        }
        return(new BigDecimal(iInvoiceSum-iCreditInvoiceSum));
    }


} // End of class SSNewResultUnit
