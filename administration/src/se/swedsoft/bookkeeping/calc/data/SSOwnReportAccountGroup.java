package se.swedsoft.bookkeeping.calc.data;

import se.swedsoft.bookkeeping.data.SSAccount;

import java.util.LinkedList;
import java.util.List;

/**
 * User: Johan Gunnarsson
 * Date: 2007-nov-29
 * Time: 08:51:44
 */
public class SSOwnReportAccountGroup {

    private Integer iId;

    private List<SSAccount> iAccounts;

    private List<SSOwnReportAccountGroup> iGroups;

    private String iName;

    private String iSumTitle;
    /**
     *
     */
    public SSOwnReportAccountGroup(){
        iGroups = null;
        iId     = -1;
    }

    /**
     *
     * @return
     */
    public Integer getId() {
        return iId;
    }

    /**
     *
     * @param iId
     */
    public void setId(Integer iId) {
        this.iId = iId;
    }

    public void setAccounts(List<SSAccount> iAccounts) {
        this.iAccounts = iAccounts;
    }


    /**
     *
     * @return
     */
    public List<SSAccount> getAccounts() {
        return iAccounts;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return iName;
    }

    /***
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
    public List<SSOwnReportAccountGroup> getGroups() {
        return iGroups;
    }

    public void setGroups(List<SSOwnReportAccountGroup> iGroups){
        this.iGroups = iGroups;
    }

    /**
     *
     * @param pAccountGroup
     */
    public void addAccountGroup(SSOwnReportAccountGroup pAccountGroup){
        if(iGroups == null ){
            iGroups = new LinkedList<SSOwnReportAccountGroup>();
        }

        iGroups.add(pAccountGroup);
    }

    public void setSumTitle(String iSumTitle){
        this.iSumTitle = iSumTitle;
    }
    public String getSumTitle(){
        return iSumTitle;
    }

    /**
     *
     * @param iAccounts
     * @return
     */
    /*public List<SSAccount> getGroupAccounts(List<SSAccount> iAccounts){
        List<SSAccount> iGroupAccounts = new LinkedList<SSAccount>();
        for(SSAccount iAccount: iAccounts){
            if( iAccount.getNumber() >= getFromAccount() && iAccount.getNumber() <= getToAccount()  ){
                iGroupAccounts.add(iAccount);
            }
        }
        return iGroupAccounts;
    } */

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.calc.data.SSOwnReportAccountGroup");
        sb.append("{iAccounts=").append(iAccounts);
        sb.append(", iGroups=").append(iGroups);
        sb.append(", iId=").append(iId);
        sb.append(", iName='").append(iName).append('\'');
        sb.append(", iSumTitle='").append(iSumTitle).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
