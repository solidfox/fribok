package se.swedsoft.bookkeeping.data.common;

import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.util.List;
import java.util.LinkedList;
import java.util.Date;
import java.util.Calendar;

/**
 * User: Andreas Lago
 * Date: 2006-mar-20
 * Time: 16:00:24
 */
public class SSPaymentTerm implements Serializable, SSTableSearchable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    private String iName;

    private String iDescription;

    /**
     * Constructor.
     */
    public SSPaymentTerm() {
    }

    /**
     * Constructor.
     *
     * @param pName
     * @param pDescription
     */
    public SSPaymentTerm(String pName, String pDescription) {
        iName        = pName;
        iDescription = pDescription;
    }

    ////////////////////////////////////////////////////
    public void dispose()
    {
        iName=null;
        iDescription=null;
    }
    /**
     *
     * @return the name
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

    ////////////////////////////////////////////////////


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
    ////////////////////////////////////////////////////

    /**
     * Decodes the name as integer
     * 
     * @return
     */
    public Integer decodeValue(){
        try {
            return Integer.decode(iName);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     *
     * @param iDate
     */
    public Date addDaysToDate(Date iDate) {
        int iDays = decodeValue();

        Calendar iCalendar = Calendar.getInstance();

        iCalendar.add(Calendar.DAY_OF_MONTH, iDays);

        return iCalendar.getTime();
    }

    ////////////////////////////////////////////////////

    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return iName;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     *
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     * @see #hashCode()
     * @see java.util.Hashtable
     */
    public boolean equals(Object obj) {
        if(obj instanceof SSPaymentTerm){
            SSPaymentTerm iUnit = (SSPaymentTerm)obj;

            return iName.equals(iUnit.getName());
        }
        return false;
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
        return iDescription;
    }


    /**
     *
     * @return
     */
    public static List<SSPaymentTerm> getDefaultPaymentTerms() {
        List<SSPaymentTerm> iPaymentTerms = new LinkedList<SSPaymentTerm>();

        iPaymentTerms.add( new SSPaymentTerm("K" , "Kontant"));
        iPaymentTerms.add( new SSPaymentTerm("PF", "Postförskott"));
        iPaymentTerms.add( new SSPaymentTerm("30", "30 dagar netto"));
        iPaymentTerms.add( new SSPaymentTerm("52", "10 dagar netto"));
        iPaymentTerms.add( new SSPaymentTerm("10", "10 dagar netto"));

        return iPaymentTerms;
    }
}
