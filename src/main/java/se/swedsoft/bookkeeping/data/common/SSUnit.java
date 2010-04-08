package se.swedsoft.bookkeeping.data.common;

import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-20
 * Time: 16:00:24
 */
public class SSUnit implements Serializable, SSTableSearchable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    private String iName;

    private String iDescription;

    /**
     * Constructor.
     */
    public SSUnit() {
    }

    /**
     * Constructor.
     *
     * @param pName
     * @param pDescription
     */
    public SSUnit(String pName, String pDescription) {
        iName        = pName;
        iDescription = pDescription;
    }

    ////////////////////////////////////////////////////

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
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return iName;
    }

    
    public boolean equals(Object obj) {
        if(obj instanceof SSUnit){
            SSUnit iUnit = (SSUnit)obj;

            return iName.equals(iUnit.iName);
        }
        return false;
    }

    
    public String toString() {
        return iName;
    }



    /**
     * Returns the default units
     *
     * @return
     */
    public static List<SSUnit> getDefaultUnits() {
        List<SSUnit> iUnits = new LinkedList<SSUnit>();
        iUnits.add( new SSUnit("st"  , SSBundle.getBundle().getString("ssunit.unit.pcs")));
        iUnits.add( new SSUnit("m"   , SSBundle.getBundle().getString("ssunit.unit.meter")));
        iUnits.add(new SSUnit("H"   , SSBundle.getBundle().getString("ssunit.unit.hours")));
        iUnits.add(new SSUnit("pkt" , SSBundle.getBundle().getString("ssunit.unit.frp")));

        return iUnits;

    }

    /**
     * 
     * @param iValue
     * @return
     */
    public static SSUnit decode(String iValue){
        List<SSUnit> iUnits = SSDB.getInstance().getUnits();

        for (SSUnit iUnit : iUnits) {
            if( iValue.equals(iUnit.iName) || iValue.equals(iUnit.iDescription) ){
                return iUnit;
            }
        }
        return null;
    }

}


