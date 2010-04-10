package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Date: 2006-feb-15
 * @version $Id$
 */
public class SSAccountPlanType implements SSTableSearchable, Serializable  {

    static final long serialVersionUID = 1L;
    private static final Map<String, SSAccountPlanType> iAccountPlanTypes = new HashMap<String, SSAccountPlanType>();

    // TODO: Load this from file
    static {
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
     * @param name
     * @return The accountplantype
     */
    public static SSAccountPlanType get(String name){
        return iAccountPlanTypes.get(name);
    }
    
    // non-static below

    private String name;
    private String schema;

    /**
     * 
     * @param name
     * @param schema
     */
    private SSAccountPlanType(String name, String schema) {
        this.name   = name;
        this.schema = schema;
    }

    /**
     *
     * @return the name of this schema
     */
    public String getName() {
        return name;
    }

    /**
     * Rename this schema
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     */
    public String getSchema() {
        if (schema == null)
            schema = "BAS95.xml";
        return schema;
    }

    /**
     *
     * @param schema
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return name;
    }
    
    public int hashCode() {
        return name.hashCode();
    }
    
    public String toString() {
        return name;
    }
}
