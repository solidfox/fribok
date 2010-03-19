/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.Serializable;
import java.util.*;

/**
 * @author Roger Björnstedt
 */
public class SSAccountPlan implements Serializable, Cloneable, SSTableSearchable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    private Integer iId;

    // Name of the acoount plan
    private String iName;

    // The name of the plane this is extended from
    private String iBaseName;

    // The assesment year
    private String iAssessementYear;

    // the type of account plan
    private SSAccountPlanType iType;

    // The AccuntPlan this is based on
    //private SSAccountPlan iBasePlan;


    // The acoounts
    private List<SSAccount> iAccounts;



    // Internal list to keep track of witch accounts is active
    private transient List<SSAccount         > iActiveAccounts;

    private transient Map<Integer, SSAccount> iAccountMap;

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Default constructor.
     */
    public SSAccountPlan() {
        iAccounts = new LinkedList<SSAccount>();
        iType     = SSAccountPlanType.get("BAS95");
    }

    /**
     * Copy constructor
     *
     * @param iAccountPlan
     */
    public SSAccountPlan(SSAccountPlan iAccountPlan) {
        copyFrom(iAccountPlan);
    }

    /**
     * Copy constructor
     *
     * @param iAccountPlan
     * @param iBasedOn
     */
    public SSAccountPlan(SSAccountPlan iAccountPlan, boolean iBasedOn) {
        copyFrom(iAccountPlan);
        if(iBasedOn) iBaseName = iAccountPlan.iName;
    }


    /**
     *
     * @param pName
     */
    public SSAccountPlan(String pName) {
        this();
        iName = pName;
    }


    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * Copies all the data from the other account plan
     *
     * @param pAccountPlan
     */
    public void copyFrom(SSAccountPlan pAccountPlan) {
        
        iName            = pAccountPlan.iName;
        iType            = pAccountPlan.iType;
        iBaseName        = pAccountPlan.iBaseName;
        iAssessementYear = pAccountPlan.iAssessementYear;
        iAccounts        = new LinkedList<SSAccount>();

        for (SSAccount iAccount : pAccountPlan.getAccounts()) {
            iAccounts.add( new SSAccount(iAccount) );
        }

        iActiveAccounts  = null;
        iAccountMap      = null;
    }

    ///////////////////////////////////////////////////////////////////////////
    public Integer getId() {
        return iId;
    }

    public void setId(Integer iId) {
        this.iId = iId;
    }
    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return The name
     */
    public String getName() {
        return iName;
    }

    /**
     *
     * @param iName
     */
    public void setName(final String iName) {
        this.iName = iName;
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     *
     * @return
     */
    public String getBaseName() {
        return iBaseName;
    }

    /**
     *
     * @param iBaseName
     */
    public void setBaseName(String iBaseName) {
        this.iBaseName = iBaseName;
    }

    /**
     *
     * @param iAccountPlan
     */
    public void setBasePlan(SSAccountPlan iAccountPlan){
        iBaseName = iAccountPlan == null ? null : iAccountPlan.iName;
    }

    ///////////////////////////////////////////////////////////////////////////


    /**
     *
     * @return The type of account plan
     */
    public SSAccountPlanType getType() {
        if( iType == null) iType = SSAccountPlanType.get("BAS95");
        return iType;
    }

    /**
     *
     * @param iType
     */
    public void setType(SSAccountPlanType iType) {
        this.iType = iType;
    }

    /**
     *
     * @param iType
     */
    public void setType(String iType) {
        this.iType = SSAccountPlanType.get(iType);
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getAssessementYear() {
        return iAssessementYear;
    }

    /**
     *
     * @param iAssessementYear
     */
    public void setAssessementYear(String iAssessementYear) {
        this.iAssessementYear = iAssessementYear;
    }

    ///////////////////////////////////////////////////////////////////////////


    /**
     *
     * @return The acoounts
     */
    public List<SSAccount> getAccounts() {
        if(iAccounts == null) iAccounts = new LinkedList<SSAccount>();

        return iAccounts;
    }

    /**
     *
     * @param iAccounts
     */
    public void setAccounts(List<SSAccount> iAccounts) {
        this.iAccounts = iAccounts;

        Collections.sort(iAccounts, new Comparator<SSAccount>() {
            public int compare(SSAccount o1, SSAccount o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });

        iActiveAccounts = null;
        iAccountMap     = null;
    }

    ///////////////////////////////////////////////////////////////////////////



    /**
     *
     * @param pAccount
     */
    public void addAccount(SSAccount pAccount) {
        iAccounts.add(pAccount);

        if(pAccount.isActive() && iActiveAccounts != null){
            iActiveAccounts.add(pAccount);
        }
        iAccountMap = null;

    }

    /**
     *
     * @param pAccount
     * @return
     */
    public boolean removeAccount(SSAccount pAccount) {
        if(pAccount.isActive() && iActiveAccounts != null){
            iActiveAccounts.remove(pAccount);
        }

        return iAccounts.remove(pAccount);
    }


    /**
     *
     * @param pNumber
     *
     * @return The account
     */
    public SSAccount getAccount(Integer pNumber) {
        return getAccountMap().get(pNumber);
    }



    /**
     *
     */
    public void clear() {
        iAccounts        = new LinkedList<SSAccount>();
        iActiveAccounts  = null;
        iAccountMap      = null;
    }



    /**
     * Copies the sru codes from another account plan
     *
     * @param pAccountPlan
     */
    public void importSRUCodesFrom(SSAccountPlan pAccountPlan){
        Map<Integer, SSAccount> iMap = getAccountMap();

        iAssessementYear = pAccountPlan.iAssessementYear;

        for (SSAccount iFromAccount : pAccountPlan.getAccounts()) {
            SSAccount iAccount = iMap.get( iFromAccount.getNumber() );

            if( iAccount != null) iAccount.setSRUCode(  iFromAccount.getSRUCode() );
        }
    }

    /**
     * Copies the vat codes from another account plan
     *
     * @param pAccountPlan
     */
    public void importVATCodesFrom(SSAccountPlan pAccountPlan){
        Map<Integer, SSAccount> iMap = getAccountMap();

        iAssessementYear = pAccountPlan.iAssessementYear;

        for (SSAccount iFromAccount : pAccountPlan.getAccounts()) {
            SSAccount iAccount = iMap.get( iFromAccount.getNumber() );

            if( iAccount != null) iAccount.setVATCode(  iFromAccount.getVATCode() );
        }
    }



    /**
     * Copies the report codes from another account plan
     *
     * @param pAccountPlan
     */
    public void importReportCodesFrom(SSAccountPlan pAccountPlan){
        Map<Integer, SSAccount> iMap = getAccountMap();

        iAssessementYear = pAccountPlan.iAssessementYear;

        for (SSAccount iFromAccount : pAccountPlan.getAccounts()) {
            SSAccount iAccount = iMap.get( iFromAccount.getNumber() );

            if( iAccount != null) iAccount.setReportCode(  iFromAccount.getReportCode() );
        }
    }




    /**
     * Returns only actove accounts. <P>
     *
     * @return A List of active accounts.
     */
    public List<SSAccount> getActiveAccounts() {
        // If the cache have been cleared for some reason, we have to re-create it.
        if (iActiveAccounts == null) {
            iActiveAccounts = new ArrayList<SSAccount>(iAccounts.size());

            for(SSAccount iCurrent: iAccounts){
                if( iCurrent.isActive() ){
                    iActiveAccounts.add(iCurrent);
                }
            }
        }
        return iActiveAccounts;
    }

    /**
     * Get the map of the acoounts in this account plan
     *
     * @return A map of accounts.
     */
    public Map<Integer, SSAccount> getAccountMap() {
        // If the cache have been cleared for some reason, we have to re-create it.
        if (iAccountMap == null) {
            iAccountMap = new HashMap<Integer, SSAccount>();

            for(SSAccount iCurrent: iAccounts){
                iAccountMap.put( iCurrent.getNumber(), iCurrent );
            }
        }
        return iAccountMap;
    }




    /**
     * @return
     */
    public String toRenderString() {
        return iName;
    }

    
    public String toString() {
        StringBuffer b = new StringBuffer();

        b.append(iName);
        b.append(" - ");
        b.append(getAccounts().size());
        b.append(", base: ");
        b.append(iBaseName);

        return b.toString();
    }


}
