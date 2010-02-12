/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.system.SSDB;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 */
public class SSVoucherRow implements Serializable, Cloneable {


    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    private Integer iAccountNr;

    private String iProjectNumber;
    private Integer iProjectNr;

    private String iResultUnitNumber;
    private Integer iResultUnitNr;

    private BigDecimal iDebet;

    private BigDecimal iCredit;

    private Date iEditedDate;

    private String iEditedSignature;

    private boolean iCrossed;

    private boolean iAdded;


    private /*transient*/ SSAccount iAccount;

    private /*transient*/ SSNewProject iProject;

    private /*transient*/ SSNewResultUnit iResultUnit;


    /**
     * Default constructor.
     */
    public SSVoucherRow() {

    }


    /**
     *
     * @param pAccount
     * @param pDebet
     * @param pCredit
     */
    public SSVoucherRow(SSAccount pAccount, BigDecimal pDebet, BigDecimal pCredit) {
        iAccount = pAccount;
        iDebet   = pDebet;
        iCredit  = pCredit;
    }

    /**
     *
     * @param pAccount
     * @param pDebet
     * @param pCredit
     * @param pProject
     * @param pResultUnit
     */
    public SSVoucherRow(SSAccount pAccount, BigDecimal pDebet, BigDecimal pCredit, SSNewProject pProject, SSNewResultUnit pResultUnit) {
        iAccount    = pAccount;
        if(iAccount != null) iAccountNr  = pAccount.getNumber();
        iDebet      = pDebet;
        iCredit     = pCredit;
        iProject    = pProject;
        if(iProject != null) iProjectNumber  = pProject.getNumber();
        iResultUnit = pResultUnit;
        if(iResultUnit != null) iResultUnitNumber = pResultUnit.getNumber();
    }

    /**
     * Copy constructor.
     *
     * @param pVoucherRow
     */
    public SSVoucherRow(SSVoucherRow pVoucherRow) {
        copyFrom(pVoucherRow);
    }


    /**
     * Copy constructor.
     *
     * @param pVoucherRow
     */
    public SSVoucherRow(SSVoucherRow pVoucherRow, boolean iReverse) {
        copyFrom(pVoucherRow);
        if(iReverse){
            iDebet           = pVoucherRow.iCredit;
            iCredit          = pVoucherRow.iDebet;
        }
    }

    ////////////////////////////////////////////////////////////////////

    /**
     *
     *
     * @param pVoucherRow The row to copy.
     */
    public void copyFrom(SSVoucherRow pVoucherRow) {
        iAccountNr       = pVoucherRow.iAccountNr;
        //iProjectNr       = pVoucherRow.iProjectNr;
        iProjectNumber   = pVoucherRow.iProjectNumber;
        //iResultUnitNr    = pVoucherRow.iResultUnitNr;
        iResultUnitNumber= pVoucherRow.iResultUnitNumber;
        iDebet           = pVoucherRow.iDebet;
        iCredit          = pVoucherRow.iCredit;
        iEditedDate      = pVoucherRow.iEditedDate;
        iEditedSignature = pVoucherRow.iEditedSignature;
        iCrossed         = pVoucherRow.iCrossed;
        iAdded           = pVoucherRow.iAdded;

        iProject         = pVoucherRow.iProject;
        iResultUnit      = pVoucherRow.iResultUnit;
        iAccount         = pVoucherRow.iAccount;
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
    public String getResultUnitNr() {
        return iResultUnitNumber;
    }

    /**
     *
     * @param iResultUnitNr
     */
    public void setResultUnitNr(String iResultUnitNr) {
        this.iResultUnitNumber = iResultUnitNr;
        this.iResultUnit   = null;
    }

    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getProjectNr() {
        return iProjectNumber;
    }

    /**
     *
     * @param iProjectNr
     */
    public void setProjectNr(String iProjectNr) {
        this.iProjectNumber = iProjectNr;
        this.iProject   = null;
    }

    public void fixResultUnitAndProject() {
        if (iResultUnitNr != null && iResultUnitNumber == null) {
            iResultUnitNumber = iResultUnitNr.toString();
        }
        if (iProjectNr != null && iProjectNumber == null) {
            iProjectNumber = iProjectNr.toString();
        }
    }
    ////////////////////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public BigDecimal getDebet() {
        return iDebet;
    }

    /**
     *
     * @param pDebet
     */
    public void setDebet(BigDecimal pDebet) {
        iDebet = pDebet;
    }

    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getCredit() {
        return iCredit;
    }

    /**
     *
     * @param pCredit
     */
    public void setCredit(BigDecimal pCredit) {
        iCredit = pCredit;
    }


    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Date getEditedDate() {
        return iEditedDate;
    }

    /**
     *
     * @param pEditedDate
     */
    public void setEditedDate(Date pEditedDate) {
        iEditedDate = pEditedDate;
    }

    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getEditedSignature() {
        return iEditedSignature;
    }

    /**
     *
     * @param pEditedSignature
     */
    public void setEditedSignature(String pEditedSignature) {
        iEditedSignature = pEditedSignature;
    }

    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean isCrossed() {
        return iCrossed;
    }
     /**
     *
     * @param iCrossed
     */
    public void setCrossed(boolean iCrossed) {
        this.iCrossed         = iCrossed;
    }

    /**
     *
     * @param pSignature
     */
    public void setCrossed(String pSignature) {
        iCrossed         = true;
        iEditedDate      = new Date();
        iEditedSignature = pSignature;
    }

    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean isAdded() {
        return iAdded;
    }

     /**
     *
     * @param iAdded
     */
    public void setAdded(boolean iAdded) {
        this.iAdded           = iAdded;
    }

    /**
     *
     * @param pSignature
     */
    public void setAdded(String pSignature) {
        iAdded           = true;
        iEditedDate      = new Date();
        iEditedSignature = pSignature;
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
     * @return
     */
    public SSNewProject getProject() {
        return getProject( SSDB.getInstance().getProjects() );
    }


    /**
     *
     * @return
     */
    public SSNewProject getProject(List<SSNewProject> iProjects) {
        if(iProject == null && iProjectNumber != null){
            for (SSNewProject iCurrent : iProjects) {
                if(iProjectNumber.equals(iCurrent.getNumber() )){
                    iProject = iCurrent;
                    break;
                }
            }
        }
        return iProject;
    }


    /**
     *
     * @param iProject
     */
    public void setProject(SSNewProject iProject) {
        this.iProject   = iProject;
        this.iProjectNumber = iProject == null ? null : iProject.getNumber();
    }

    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSNewResultUnit getResultUnit() {
        return getResultUnit(SSDB.getInstance().getResultUnits());

    }
    /**
     *
     * @return
     */
    public SSNewResultUnit getResultUnit(List<SSNewResultUnit> iResultUnits) {
        if(iResultUnit == null && iResultUnitNumber != null){
            for (SSNewResultUnit iCurrent : iResultUnits) {
                if(iResultUnitNumber.equals(iCurrent.getNumber() )){
                    iResultUnit = iCurrent;
                    break;
                }
            }
        }
        return iResultUnit;
    }

    /**
     *
     * @param iResultUnit
     */
    public void setResultUnit(SSNewResultUnit iResultUnit) {
        this.iResultUnit   = iResultUnit;
        this.iResultUnitNumber = iResultUnit == null ? null : iResultUnit.getNumber();

    }

    /**
     *
     * @param out
     * @throws IOException
     */
    private void writeObject(java.io.ObjectOutputStream out) throws IOException{
        iAccount    = null;
        iProject    = null;
        iResultUnit = null;
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
        if(iProject    != null) iProjectNumber    = iProject   .getNumber();
        if(iResultUnit != null) iResultUnitNumber = iResultUnit.getNumber();

        iAccount    = null;
        iProject    = null;
        iResultUnit = null;
    }

    ////////////////////////////////////////////////////////////////////

    /**
     * Set the value of this row, such as
     *
     * if( value > 0)
     *  debet = value
     * else
     *  credet value
     *
     *
     * @param iValue
     */
    public void setValue(BigDecimal iValue) {
        // Dont add a empty row
        if(iValue == null) return;

        // Add the value
        if( iValue.signum() > 0){
            setDebet  ( iValue      );
            setCredit ( null);
        } else {
            setDebet  ( null   );
            setCredit ( iValue.abs()  );
        }
    }

    /**
     * Get the value of the row, such as a debet is positive and a credit is negative
     *
     * @return
     */
    public BigDecimal getValue(){
        if( iDebet != null ){
            return iDebet;
        }
        if( iCredit != null){
            return iCredit.negate();
        }
        return new BigDecimal(0.00);
    }


    ////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean isDebet() {
        return iDebet != null;
    }

    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p/>
     * The <code>toString</code> method for class <code>Object</code>
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `<code>@</code>', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    public String toString() {

        StringBuffer b = new StringBuffer();
        if( iCrossed ){
            b.append("-");
        }
        b.append(iAccountNr);
        b.append(", D=");
        b.append(iDebet);
        b.append(", C=");
        b.append(iCredit);

        if( iProjectNumber != null ){
            b.append(" (Project ");
            b.append(iProjectNumber );
            b.append(" )");
        }

        if( iResultUnitNumber != null ){
            b.append(" (Resultunit ");
            b.append(iResultUnitNumber);
            b.append(" )");
        }


        return b.toString();
    }



    /**
     * Creates a new voucher row that is the inverse of this one
     *
     * @return A row that is the reverse of this one in debet and credit.

    public SSVoucherRow createReversedRow() {
    return new SSVoucherRow(getAccount(), getCredit(), getDebet(), getProject(), getResultUnit());
    }
     */
    /**
     * Indicates that this row has an account and one value in one of credit or debet.
     * Also, empty rows are isValid rows.
     *
     * @return A boolean value.
     */
    public boolean isValid() {
        return (getAccount() != null) && (iDebet != null || iCredit != null);
    }

    /**
     * Indicates whether both the account and debet and credit values are null.
     *
     * @return A boolean value.
     */
    public boolean isEmpty() {
        return (iAccountNr == null) && (iDebet == null) && (iCredit == null);
    }

    /**
     *
     * @param iAccount
     */
    public boolean hasAccount(SSAccount iAccount) {
        return (iAccountNr != null) && iAccountNr.equals( iAccount.getNumber() );
    }





}
