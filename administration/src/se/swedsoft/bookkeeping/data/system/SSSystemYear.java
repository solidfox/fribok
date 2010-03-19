package se.swedsoft.bookkeeping.data.system;


import se.swedsoft.bookkeeping.data.SSAccountPlan;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.rmi.server.UID;
import java.text.DateFormat;
import java.util.Date;

/**
 * Date: 2006-feb-24
 *
 *  Virual yeardata
 */
public class SSSystemYear implements Serializable, SSTableSearchable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    private UID iID;

    private Date iDateFrom;

    private Date iDateTo;

    private String iAccountPlan;

    private boolean iCurrent;

    private transient SSNewAccountingYear iYear;

    /**
     * Creates a new systemyear
     */
    public SSSystemYear(){
        iID          = new UID();
        iDateFrom    = new Date();
        iDateTo      = new Date();
        iCurrent     = false;
        iAccountPlan = null;
    }
    /**
     *
     * @param pYear
     */
    public SSSystemYear(SSNewAccountingYear pYear){
        iYear        = pYear;
        iCurrent     = false;
        iID          = new UID();
        iDateFrom    = pYear.getFrom();
        iDateTo      = pYear.getTo  ();
        iAccountPlan = pYear.getAccountPlan() != null ? pYear.getAccountPlan().getName() : null;

      //  unloadYear();
    }

    /**
     * Returns the id for the year
     *
     * @return the id
     */
    public UID getId() {
        return iID;
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the start date of the accounting year
     *
     * @return the start date
     */
    public Date getFrom() {
        return iDateFrom;
    }

    public void setFrom(Date iDateFrom) {
        this.iDateFrom = iDateFrom;
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the stop date of the acoounting year
     *
     * @return the stop date
     */
    public Date getTo() {
        return iDateTo;
    }

    /**
     *
     * @param iDateTo
     */
    public void setTo(Date iDateTo) {
        this.iDateTo = iDateTo;
    }


    //////////////////////////////////////////////////////////////////////////////

    /**
     * Return the accoutn plans name for the year
     *
     * @return the accountplan
     */
    public String getAccountPlan(){
        return iAccountPlan;
    }

    /**
     *
     * @param iAccountPlan
     */
    public void setAccountPlan(String iAccountPlan) {
        this.iAccountPlan = iAccountPlan;
    }


    /**
     *
     * @param iAccountPlan
     */
    public void setAccountPlan(SSAccountPlan iAccountPlan) {
        this.iAccountPlan = iAccountPlan == null ? null : iAccountPlan.getName();
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSNewAccountingYear getData() {
        return iYear;
    }

    /**
     *
     * @param iAccountingYear
     */
    public void setData(SSNewAccountingYear iAccountingYear) {

        iYear = iAccountingYear;
    }

    //////////////////////////////////////////////////////////////////////////////

    public void setCurrentYear(SSNewAccountingYear iAccountingYear) {
        iDateFrom    = iAccountingYear.getFrom();
        iDateTo      = iAccountingYear.getTo();
        iCurrent     = true;
        iAccountPlan = iAccountingYear.getAccountPlan() != null ? iAccountingYear.getAccountPlan().getName() : null;
        iYear = iAccountingYear;
    }
    /**
     *
     * @return if the year is the current year
     */
    public boolean isCurrent() {
        return iCurrent;
    }





    /**
     * Set if the year shall be the current one, loads data if true, unloads if false
     *
     * @param pCurrent if the year shall be current
     */
    public void setCurrent(boolean pCurrent){
        iCurrent = pCurrent;

    }


    
    public boolean equals(Object other) {
        if(other instanceof SSSystemYear){
            return iID.equals(((SSSystemYear) other).iID);
        }
        if(other instanceof SSNewAccountingYear){
            return iID.equals( ((SSNewAccountingYear)other).getId() );
        }
        return false;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.data.system.SSSystemYear");
        sb.append("{iAccountPlan='").append(iAccountPlan).append('\'');
        sb.append(", iCurrent=").append(iCurrent);
        sb.append(", iDateFrom=").append(iDateFrom);
        sb.append(", iDateTo=").append(iDateTo);
        sb.append(", iID=").append(iID);
        sb.append(", iYear=").append(iYear);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);

        return format.format(iDateFrom) + " - " + format.format(iDateTo);
    }


}
