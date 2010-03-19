package se.swedsoft.bookkeeping.data.system;


import se.swedsoft.bookkeeping.data.SSNewCompany;

import java.io.Serializable;
import java.rmi.server.UID;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 2006-feb-24
 * Time: 16:02:26
 *
 * Contains the information for each company
 *
 */
public class SSSystemCompany implements Serializable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    // The id of the company
    private UID iID;

    // The name of the company
    private String iName;

    // We are the current (active) company
    private boolean iCurrent;

    // The years in the company
    private List<SSSystemYear> iYears;


    // Our placehoder for the company, if loaded
    private transient SSNewCompany iCompany;


    /**
     * Creates a new system company
     */
    public SSSystemCompany(){
        iID      = new UID();
        iName    = "";
        iCompany = null;
        iYears   = new LinkedList<SSSystemYear>();
        iCurrent = false;
    }

    /**
     *
     * @param pCompany
     */
    public SSSystemCompany(SSNewCompany pCompany){
        iCompany = pCompany;
        iCurrent = false;
        iID      = new UID();
        iName    = pCompany.getName();
        iYears   = new LinkedList<SSSystemYear>();
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     * returns the unique id for this company
     * @return
     */
    public UID getId() {
        return iID;
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getName() {
        return iName;
    }

    /**
     *
     * @param iName
     */
    public void setName(String iName) {
        this.iName = iName;
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return if the company is the current year
     */
    public boolean isCurrent() {
        return iCurrent;
    }

    /**
     * Set if the company shall be the current one, loads data if true, unloads if false
     *
     * @param pCurrent if the company shall be current
     */
    public void setCurrent(boolean pCurrent) {
        iCurrent = pCurrent;

    }
    //////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public SSNewCompany getData() {
        return iCompany;
    }

    /**
     *
     * @param iCompany
     */
    public void setData(SSNewCompany iCompany) {
        if(iCompany != null)
            iName = iCompany.getName();
        
        this.iCompany = iCompany;
    }

    //////////////////////////////////////////////////////////////////////////////

    /**
     * The years for the company
     *
     * @return list of years
     */
    public List<SSSystemYear> getYears() {
        return iYears;
    }

    public void setYears(List<SSSystemYear> pYears) {
        iYears = pYears;
    }

    /**
     * The current year for the company
     *
     * @return list of years
     */
    public SSSystemYear getCurrentYear() {

        for (SSSystemYear iYear : iYears) {
            if (iYear.isCurrent()) {
                return iYear;
            }
        }
        return null;
    }

    public void setCurrentyear(SSSystemYear iNew) {
        if(iNew == null)
            return;
        
        for(SSSystemYear iYear: iYears) {
            if (iYear.isCurrent()) {
                iYear.setCurrent(false);
            }
            if (iYear.getId().equals(iNew.getId())) {
                iYear.setData(iNew.getData());
                iYear.setCurrent(true);
            }
        }
    }
    //////////////////////////////////////////////////////////////////////////////


    
    public boolean equals(Object other) {
        if(other instanceof SSSystemCompany){
            SSSystemCompany iSystemCompany = (SSSystemCompany) other;

            return iID.equals( iSystemCompany.iID );
        }
        if(other instanceof SSNewCompany){
            SSNewCompany iCompany = (SSNewCompany) other;

            return iID.equals( iCompany.getId() );
        }
        return false;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.data.system.SSSystemCompany");
        sb.append("{iCompany=").append(iCompany);
        sb.append(", iCurrent=").append(iCurrent);
        sb.append(", iID=").append(iID);
        sb.append(", iName='").append(iName).append('\'');
        sb.append(", iYears=").append(iYears);
        sb.append('}');
        return sb.toString();
    }



}
