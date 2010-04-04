package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Date: 2006-feb-15
 * Time: 10:26:20
 */
public class SSAccountPlanType implements SSTableSearchable, Serializable  {

    static final long serialVersionUID = 1L;

    private String iName;

    private String iSchema;

    /**
     *
     * @param pName
     * @param pSchema
     */
    private SSAccountPlanType(String pName, String pSchema){
        iName   = pName;
        iSchema = pSchema;

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
     * @param iName
     */
    public void setName(String iName) {
        this.iName = iName;
    }

    /**
     *
     * @return
     */
    public String getSchema() {
        if( iSchema == null) iSchema = "BAS95.xml";
        return iSchema;
    }

    /**
     *
     * @param iSchema
     */
    public void setSchema(String iSchema) {
        this.iSchema = iSchema;
    }
    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return iName;
    }

    
    public int hashCode() {
        return iName.hashCode();
    }

    
    public String toString() {
        return iName;
    }







    private static final Map<String, SSAccountPlanType> iAccountPlanTypes = new HashMap<String, SSAccountPlanType>();

    // TODO: Load this from file
    static{
        iAccountPlanTypes.put("BAS95"  , new SSAccountPlanType("BAS95"  , "BAS95.xml"  ));
        iAccountPlanTypes.put("BAS96"  , new SSAccountPlanType("BAS96"  , "BAS96.xml"  ));
        iAccountPlanTypes.put("EUBAS97", new SSAccountPlanType("EUBAS97", "EUBAS97.xml"));
    }



    /**
     * Retuns a list of all account plan types
     *
     * @return List of types
     */
    public static List<SSAccountPlanType> getAccountPlanTypes(){
        return new LinkedList<SSAccountPlanType>( iAccountPlanTypes.values() );
    }


    /**
     * Gets the accountplantype by its name
     *
     * @param pName
     * @return The accountplantype
     */
    public static SSAccountPlanType get(String pName){
        return iAccountPlanTypes.get(pName);
    }

}
