/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;

// Trade Extensions specific imports

// Java specific imports

/**
 * @author Roger Björnstedt
 */
public class SSResultUnit implements Serializable, SSTableSearchable {


    /**
     * Constant for serialization versioning.
     */
    static final long serialVersionUID = 1L;


    /** */
    private int iNumber;

    /** */
    private String iName;

    /** */
    private String iDescription;

    /**
     * Default constructor.
     */
    public SSResultUnit() {
        iNumber      = 0;
        iName        = "";
        iDescription = "";
    }

    /**
     * Default constructor.
     */
    public SSResultUnit(int number, String name) {
        iNumber      = number;
        iName        = name;
        iDescription = "";
    }

    ///////////////////////////////////////////////////////////////////////////



    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public int getNumber() {
        return iNumber;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param pNumber
     */
    public void setNumber(int pNumber) {
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
        if(obj instanceof SSResultUnit)
            return ((SSResultUnit)obj).iNumber == iNumber;

        return super.equals(obj);
    }

    /**
     * @return
     */
    public String toRenderString() {
        return Integer.toString(iNumber);
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







} // End of class SSNewResultUnit
