/*
 * @(#)SSVoucherTemplate.java                v 1.0 2005-sep-01
 *
 * Time-stamp: <2005-sep-01 10:39:23 Hasse>
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

import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.io.Serializable;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.util.Date;
import java.math.BigDecimal;

// Trade Extensions specific imports

// Java specific imports

/**
 * @author Roger Björnstedt
 */
public class SSVoucherTemplate implements Serializable, SSTableSearchable {


    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    //
    private String iDescription;

    // The modified date
    private Date iDate;

    //
    private List<SSVoucherTemplateRow> iRows;


    ////////////////////////////////////////////////////////////////////


    /**
     * Default constructor.
     */
    public SSVoucherTemplate() {
        this.iRows        = new LinkedList<SSVoucherTemplateRow>();
        this.iDescription = null;
        this.iDate        = new Date();
    }


    /**
     * Create a new voucher template from the voucher
     *
     * @param pVoucher
     */
    public SSVoucherTemplate(SSVoucher pVoucher) {
        this.iDescription = pVoucher.getDescription();
        this.iDate        = new Date();

        iRows = new LinkedList<SSVoucherTemplateRow>();

        for(SSVoucherRow iVoucherRow: pVoucher.getRows()){
            SSVoucherTemplateRow iTemplateRow = new SSVoucherTemplateRow();

            iTemplateRow.setAccount( iVoucherRow.getAccount()  );
            iTemplateRow.setDebet  ( iVoucherRow.getDebet  () );
            iTemplateRow.setCredit ( iVoucherRow.getCredit  () );

            iRows.add(iTemplateRow);

        }
    }

    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @param pVoucher
     */
    public void addToVoucher(SSVoucher pVoucher) {
        pVoucher.setDescription(iDescription);



        for(SSVoucherTemplateRow iTemplateRow: iRows){
            SSVoucherRow iVoucherRow = new SSVoucherRow();

            // Dont add the row if the account is in the voucher
            if( SSVoucherMath.hasAccount(pVoucher, iTemplateRow.getAccount() ) ) continue;

            iVoucherRow.setAccount(  iTemplateRow.getAccount() );
            iVoucherRow.setDebet  (  iTemplateRow.getDebet() );
            iVoucherRow.setCredit (  iTemplateRow.getCredit() );

            pVoucher.addVoucherRow(iVoucherRow);
        }
    }

    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getDescription() {
        return iDescription;
    }

    /**
     *
     * @param iDescription
     */
    public void setDescription(String iDescription) {
        this.iDescription = iDescription;
    }
    ////////////////////////////////////////////////////////////////////

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
    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public List<SSVoucherTemplateRow> getRows() {
        return iRows;
    }

    /**
     *
     * @param iRows
     */
    public void setRows(List<SSVoucherTemplateRow> iRows) {
        this.iRows = iRows;
    }

    ////////////////////////////////////////////////////////////////////

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object.
     */
    public String toString() {

        StringBuffer b = new StringBuffer();
        b.append(iDescription);
        b.append("\n");

        return b.toString();
    }


    /**
     * @return
     */
    public String toRenderString() {
        return iDescription;
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
        if (!(obj instanceof SSVoucherTemplate)) {
            return false;
        }
        return iDescription.equals(((SSVoucherTemplate)obj).getDescription());
    }





    /**
     *
     */
    public static final class SSVoucherTemplateRow implements Serializable {

        // Constant for serialization versioning.
        static final long serialVersionUID = 1L;


        private Integer iAccountNr;

        private Boolean iDebet;

        private SSAccount iAccount;

        /**
         *
         */
        public SSVoucherTemplateRow() {
            iAccount   = null;
            iDebet     = false;
            iAccountNr = null;
        }


        ////////////////////////////////////////////////////////////////////

        /**
         *
         * @return
         */
        public Integer getAccountNr() {
            return iAccountNr;
        }

        /**
         *
         * @param iAccountNr
         */
        public void setAccountNr(Integer iAccountNr) {
            this.iAccountNr = iAccountNr;
            this.iAccount   = null;
        }


        ////////////////////////////////////////////////////////////////////

        /**
         *
         * @return
         */
        public BigDecimal getDebet() {
            return iDebet ? new BigDecimal(0.0) : null;
        }

        /**
         *
         * @return
         */
        public BigDecimal getCredit() {
            return iDebet ?  null: new BigDecimal(0.0);
        }

        ////////////////////////////////////////////////////////////////////

        /**
         *
         * @param iDebet
         */
        public void setDebet(BigDecimal iDebet) {
            this.iDebet = (iDebet != null);
        }

        /**
         *
         * @param iCredit
         */
        public void setCredit(BigDecimal iCredit) {
            this.iDebet = (iCredit == null);
        }


        ////////////////////////////////////////////////////////////////////

        /**
         *
         * @return
         */
        public SSAccount getAccount() {
            return getAccount(SSDB.getInstance().getAccounts());
        }

        /**
         *
         * @return
         */
        public SSAccount getAccount(List<SSAccount> iAccounts ) {
            if(iAccount == null && iAccountNr != null){
                for (SSAccount iCurrent : iAccounts) {
                    if(iAccountNr.equals(iCurrent.getNumber() )){
                        iAccount = iCurrent;
                        break;
                    }
                }
            }
            return iAccount;
        }

        /**
         *
         * @param iAccount
         */
        public void setAccount(SSAccount iAccount) {
            this.iAccount   = iAccount;
            this.iAccountNr = iAccount == null ? null : iAccount.getNumber();
        }


        ////////////////////////////////////////////////////////////////////

        /**
         *
         * @param out
         * @throws java.io.IOException
         */
        private void writeObject(java.io.ObjectOutputStream out) throws IOException {
            iAccount    = null;
            out.defaultWriteObject();
        }

        /**
         *
         * @param in
         * @throws IOException
         * @throws ClassNotFoundException
         */
        private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException{
            in.defaultReadObject();
            if(iAccount    != null) iAccountNr    = iAccount   .getNumber();

            iAccount    = null;
        }


    }


}
