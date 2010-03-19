package se.swedsoft.bookkeeping.importexport.sie.util;

import se.swedsoft.bookkeeping.importexport.sie.fields.SIEEntry;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Date: 2006-feb-20
 * Time: 12:39:02
 */
public class SIEFactory {


    /**
     *
     * @return
     */
    public static SIEFactory getImportInstance(){
        return new SIEFactory();
    }

    /**
     *
     * @param iType
     * @return
     */
    public static SIEFactory getExportInstance(SIEType iType){
        return new SIEFactory(iType);
    }


    private Map<String  , SIEEntry> iEntries;

    private List<SIELabel> iLabels;

    /**
     *
     */
    private SIEFactory(){
        iEntries = new HashMap<String, SIEEntry>();
        iLabels  = new LinkedList<SIELabel>();

        for(SIELabel iLabel : SIELabel.values()){
            put(iLabel, iLabel.getEntry() );
        }
    }

    /**
     *
     * @param iType
     */
    private SIEFactory(SIEType iType){
        iEntries = new HashMap<String, SIEEntry>();
        iLabels  = new LinkedList<SIELabel>();

        for(SIELabel iLabel : SIELabel.values(iType)){
            put(iLabel, iLabel.getEntry() );
        }
    }


    /**
     *
     * @param pLabel
     * @return
     */
    public SIEEntry get(String pLabel){
        return iEntries.get( pLabel );
    }
    /**
     *
     * @param pLabel
     * @return
     */
    public SIEEntry get(SIELabel pLabel){
        return iEntries.get( pLabel.getName() );
    }


    /**
     *
     * @param pLabel
     */
    public void put(SIELabel pLabel){
        iLabels.add(pLabel);

        iEntries.put(pLabel.getName(), pLabel.getEntry() );
    }

    /**
     *
     * @param pLabel
     * @param pField
     */
    public void put(SIELabel pLabel, SIEEntry pField){
        iLabels.add(pLabel);

        iEntries.put(pLabel.getName(), pField);
    }

    /**
     *
     * @return
     */
    public List<SIELabel> getLabels(){
        return iLabels;

    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.importexport.sie.util.SIEFactory");
        sb.append("{iEntries=").append(iEntries);
        sb.append(", iLabels=").append(iLabels);
        sb.append('}');
        return sb.toString();
    }

}
