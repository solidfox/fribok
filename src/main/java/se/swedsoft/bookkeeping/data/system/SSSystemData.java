package se.swedsoft.bookkeeping.data.system;


import se.swedsoft.bookkeeping.data.SSAccountPlan;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * Date: 2006-feb-24
 * Time: 15:58:36
 */
public class SSSystemData implements Serializable {

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;

    // The list of companies
    private List<SSSystemCompany> iCompanies;

    // List of available account plans.
    private List<SSAccountPlan> iAccountPlans;

    /**
     *
     */
    public SSSystemData() {
        iCompanies = new LinkedList<SSSystemCompany>();
        iAccountPlans = new LinkedList<SSAccountPlan>();

    }

    /**
     * Returns all account plans
     *
     * @return list of account plans
     */
    public List<SSAccountPlan> getAccountPlans() {
        return iAccountPlans;
    }

    /**
     * Sets the accountplans
     *
     * @param pAccountPlans
     */
    public void setAccountPlans(List<SSAccountPlan> pAccountPlans) {
        iAccountPlans = pAccountPlans;
    }

    /**
     * Returns all companys in the database
     *
     * @return the companys
     */
    public List<SSSystemCompany> getSystemCompanies() {
        return iCompanies;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.data.system.SSSystemData");
        sb.append("{iAccountPlans=").append(iAccountPlans);
        sb.append(", iCompanies=").append(iCompanies);
        sb.append('}');
        return sb.toString();
    }
}

