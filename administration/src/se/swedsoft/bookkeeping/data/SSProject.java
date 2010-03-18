/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;


/**
 */
public class SSProject implements Serializable, SSTableSearchable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    private int iNumber;

    private String iName;

    private String iDescription;

    private boolean iConcluded;

    private Date iConcludedDate;


    /**
     * Default constructor.
     */
    public SSProject() {

    }

    /**
     *
     * @param pNumber
     * @param pName
     * @param pDescription
     */
    public SSProject(int pNumber, String pName, String pDescription) {
        iNumber        = pNumber;
        iName          = pName;
        iDescription   = pDescription;
        iConcluded     = false;
        iConcludedDate = null;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     */


    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public int getNumber() {
        return iNumber;
    }

    /**
     *
     * @param number
     */
    public void setNumber(int number) {
        iNumber = number;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getName() {
        return iName;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        iName = name;
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
     * @param description
     */
    public void setDescription(String description) {
        iDescription = description;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public boolean getConcluded() {
        return iConcluded;
    }

    /**
     *
     * @param pConcluded
     */
    public void setConcluded(boolean pConcluded) {
        iConcluded = pConcluded;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public Date getConcludedDate() {
        return iConcludedDate;
    }

    /**
     *
     * @param pConcluded
     */
    public void setConcludedDate(Date pConcluded) {
        iConcludedDate = pConcluded;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param iDate
     * @return
     */
    public boolean isConcluded(Date iDate){
        return iConcluded && (iConcludedDate != null) &&   iConcludedDate.getTime() <= iDate.getTime();
    }

    ///////////////////////////////////////////////////////////////////////////


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
        DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        StringBuilder sb = new StringBuilder();

        sb.append( iNumber );
        sb.append( " - " );
        sb.append( iName);
        sb.append( ", " );
        sb.append( iDescription );
        if(iConcluded){
            sb.append( "(Concluded " );
            sb.append( iFormat.format( iConcludedDate ) );
            sb.append( ") " );
        }
        return sb.toString();
    }

    /**
     *
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if(obj instanceof SSProject)
            return ((SSProject)obj).iNumber == iNumber;

        return false;
    }

    /**
     * @return
     */
    public String toRenderString() {
        return Integer.toString(iNumber);
    }




}
