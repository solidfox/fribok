package se.swedsoft.bookkeeping.data.common;


import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


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
    public SSPaymentTerm() {}

    /**
     * Constructor.
     *
     * @param pName
     * @param pDescription
     */
    public SSPaymentTerm(String pName, String pDescription) {
        iName = pName;
        iDescription = pDescription;
    }

    // //////////////////////////////////////////////////
    public void dispose() {
        iName = null;
        iDescription = null;
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

    // //////////////////////////////////////////////////

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

    // //////////////////////////////////////////////////

    /**
     * Decodes the name as integer
     *
     * @return
     */
    public Integer decodeValue() {
        try {
            return Integer.decode(iName);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     *
     * @param iDate
     * @return
     */
    public Date addDaysToDate(Date iDate) {
        int iDays = decodeValue();

        Calendar iCalendar = Calendar.getInstance();

        iCalendar.add(Calendar.DAY_OF_MONTH, iDays);

        return iCalendar.getTime();
    }

    // //////////////////////////////////////////////////

    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return iName;
    }

    public boolean equals(Object obj) {
        if (obj instanceof SSPaymentTerm) {
            SSPaymentTerm iUnit = (SSPaymentTerm) obj;

            return iName.equals(iUnit.iName);
        }
        return false;
    }

    public String toString() {
        return iDescription;
    }

    /**
     *
     * @return
     */
    public static List<SSPaymentTerm> getDefaultPaymentTerms() {
        List<SSPaymentTerm> iPaymentTerms = new LinkedList<SSPaymentTerm>();

        iPaymentTerms.add(new SSPaymentTerm("K", "Kontant"));
        iPaymentTerms.add(new SSPaymentTerm("PF", "Postförskott"));
        iPaymentTerms.add(new SSPaymentTerm("30", "30 dagar netto"));
        iPaymentTerms.add(new SSPaymentTerm("52", "10 dagar netto"));
        iPaymentTerms.add(new SSPaymentTerm("10", "10 dagar netto"));

        return iPaymentTerms;
    }
}
