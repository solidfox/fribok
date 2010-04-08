package se.swedsoft.bookkeeping.data.common;

import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-20
 * Time: 16:00:24
 */
public class SSDeliveryWay implements Serializable, SSTableSearchable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    private String iName;

    private String iDescription;

    /**
     * Constructor.
     */
    public SSDeliveryWay() {
    }

    /**
     * Constructor.
     *
     * @param pName
     * @param pDescription
     */
    public SSDeliveryWay(String pName, String pDescription) {
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
        if(obj instanceof SSDeliveryWay){
            SSDeliveryWay iUnit = (SSDeliveryWay)obj;

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
    public static List<SSDeliveryWay> getDefaultDeliveryWays() {
        List<SSDeliveryWay> iDeliveryWays = new LinkedList<SSDeliveryWay>();
        iDeliveryWays.add( new SSDeliveryWay("P"  , "Post") );
        iDeliveryWays.add( new SSDeliveryWay("HÄM", "Hämtas") );

        return iDeliveryWays;
    }
}
