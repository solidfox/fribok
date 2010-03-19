/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
        iRows        = new LinkedList<SSVoucherTemplateRow>();
        iDescription = null;
        iDate        = new Date();
    }


    /**
     * Create a new voucher template from the voucher
     *
     * @param pVoucher
     */
    public SSVoucherTemplate(SSVoucher pVoucher) {
        iDescription = pVoucher.getDescription();
        iDate        = new Date();

        iRows        = new LinkedList<SSVoucherTemplateRow>();

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


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.data.SSVoucherTemplate");
        sb.append("{iDate=").append(iDate);
        sb.append(", iDescription='").append(iDescription).append('\'');
        sb.append(", iRows=").append(iRows);
        sb.append('}');
        return sb.toString();
    }


    /**
     * @return
     */
    public String toRenderString() {
        return iDescription;
    }

    
    public boolean equals(Object obj) {
        if (!(obj instanceof SSVoucherTemplate)) {
            return false;
        }
        return iDescription.equals(((SSVoucherTemplate) obj).iDescription);
    }





    /**
     *
     */
    public static final class SSVoucherTemplateRow implements Serializable {

        // Constant for serialization versioning.
        static final long serialVersionUID = 1L;


        private Integer iAccountNr;

        private boolean iDebet;

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
            iAccount        = null;
        }


        ////////////////////////////////////////////////////////////////////

        /**
         *
         * @return
         */
        public BigDecimal getDebet() {
            return iDebet ? new BigDecimal(0) : null;
        }

        /**
         *
         * @return
         */
        public BigDecimal getCredit() {
            return iDebet ?  null: new BigDecimal(0);
        }

        ////////////////////////////////////////////////////////////////////

        /**
         *
         * @param iDebet
         */
        public void setDebet(BigDecimal iDebet) {
            this.iDebet = iDebet != null;
        }

        /**
         *
         * @param iCredit
         */
        public void setCredit(BigDecimal iCredit) {
            iDebet = iCredit == null;
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
         * @param iAccounts
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
            iAccountNr = iAccount == null ? null : iAccount.getNumber();
        }


        ////////////////////////////////////////////////////////////////////

        /**
         *
         * @param out
         * @throws IOException
         */
        private void writeObject(ObjectOutputStream out) throws IOException {
            iAccount    = null;
            out.defaultWriteObject();
        }

        /**
         *
         * @param in
         * @throws IOException
         * @throws ClassNotFoundException
         */
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
            in.defaultReadObject();
            if(iAccount    != null) iAccountNr    = iAccount   .getNumber();

            iAccount    = null;
        }


        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("se.swedsoft.bookkeeping.data.SSVoucherTemplate.SSVoucherTemplateRow");
            sb.append("{iAccount=").append(iAccount);
            sb.append(", iAccountNr=").append(iAccountNr);
            sb.append(", iDebet=").append(iDebet);
            sb.append('}');
            return sb.toString();
        }
    }


}
