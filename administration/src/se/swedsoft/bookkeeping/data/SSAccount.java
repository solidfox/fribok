/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.util.Hashtable;


/**
 * Accounts
 */
public class SSAccount implements Serializable, Cloneable, SSTableSearchable {

    /// Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    private int iNumber;

    private String iDescription;

    private String iSRUCode;

    private String iVATCode;

    private String iReportCode;

    private boolean iActive;

    private boolean iProjectRequired;

    private boolean iResultUnitRequired;


    /**
     * Default constructor.
     */
    public SSAccount() {
        iNumber = -1;
        iActive = true;
        iProjectRequired = false;
        iResultUnitRequired = false;
    }


    /**
     * @param iNumber
     */
    public SSAccount(int iNumber) {
        this.iNumber = iNumber;
        iActive = true;
        iProjectRequired = false;
        iResultUnitRequired = false;
    }

    /**
     * Copy contructor.
     *
     * @param pAccount The account to copy.
     */
    public SSAccount(SSAccount pAccount) {
        copyFrom(pAccount);
    }

    ////////////////////////////////////////////////////////////////////////


    /**
     *
     * @param pAccount
     */
    public void copyFrom(SSAccount pAccount) {
        iNumber             = pAccount.iNumber;
        iDescription        = pAccount.iDescription;
        iSRUCode            = pAccount.iSRUCode;
        iVATCode            = pAccount.iVATCode;
        iReportCode         = pAccount.iReportCode;
        iActive             = pAccount.iActive;
        iProjectRequired    = pAccount.iProjectRequired;
        iResultUnitRequired = pAccount.iResultUnitRequired;
    }


    ////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Integer getNumber() {
        return iNumber < 1 ? null : iNumber;
    }

    /**
     *
     * @param iNumber
     */
    public void setNumber(Integer iNumber) {
        this.iNumber =  iNumber == null ? -1 : iNumber;
    }

    ////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getSRUCode() {
        return iSRUCode;
    }

    /**
     *
     * @param pSRUCode
     */
    public void setSRUCode(String pSRUCode) {
        iSRUCode = pSRUCode;
    }

    ////////////////////////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public String getVATCode() {
        return iVATCode;
    }

    /**
     *
     * @param pVATCode
     */
    public void setVATCode(String pVATCode) {
        iVATCode = pVATCode;
    }

    ////////////////////////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public String getReportCode() {
        return iReportCode;
    }

    /**
     *
     * @param pReportCode
     */
    public void setReportCode(String pReportCode) {
        iReportCode = pReportCode;
    }

    ////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean isActive() {
        return iActive;
    }

    /**
     *
     * @param pActive
     */
    public void setActive(boolean pActive) {
        iActive = pActive;
    }

    ////////////////////////////////////////////////////////////////////////

    public boolean isProjectRequired() {
        return iProjectRequired;
    }

    public void setProjectRequired(boolean iProjectRequired) {
        this.iProjectRequired = iProjectRequired;
    }

    public boolean isResultUnitRequired() {
        return iResultUnitRequired;
    }

    public void setResultUnitRequired(boolean iResultUnitRequired) {
        this.iResultUnitRequired = iResultUnitRequired;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.data.SSAccount");
        sb.append("{iActive=").append(iActive);
        sb.append(", iDescription='").append(iDescription).append('\'');
        sb.append(", iNumber=").append(iNumber);
        sb.append(", iProjectRequired=").append(iProjectRequired);
        sb.append(", iReportCode='").append(iReportCode).append('\'');
        sb.append(", iResultUnitRequired=").append(iResultUnitRequired);
        sb.append(", iSRUCode='").append(iSRUCode).append('\'');
        sb.append(", iVATCode='").append(iVATCode).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return String.valueOf(iNumber);
    }



    
    public boolean equals(Object obj) {
        if(obj instanceof SSAccount ){
            SSAccount iAccount = (SSAccount) obj;

            return iNumber == iAccount.iNumber;

        }
        return false;
    }

    /**
     * We need to override this as account is saved as instances for the various classes like SSVoucherRow
     *
     * This enshures that the same account from different years will be treated as the same account over
     * different years. The account number shall qualify as a good enought hash key anyway.
     *
     * @return the hashcode
     */
    public int hashCode() {
        //if(iNumber != null){
            return iNumber;
       // } else {
        //    return super.hashCode();
       // }

    }



}
