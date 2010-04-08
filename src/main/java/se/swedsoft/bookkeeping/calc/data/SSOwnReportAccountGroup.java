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





    
    public String toString() {
        return toString("");
    }

    
    public String toString(String Seperator) {
        /*StringBuilder sb = new StringBuilder();

        sb.append(Seperator);
        sb.append( iId );
        sb.append(": ");
        sb.append( getTitle() );
        sb.append(", ");
        sb.append( iFromAccount );
        sb.append(" - ");
        sb.append( iToAccount );

        if(iGroups  != null){
            sb.append("{\n");
            for(SSAccountGroup iGroup : iGroups ){
                sb.append(iGroup.toString(Seperator + "  ") );
            }
            sb.append("  }\n");
        }
        sb.append("\n");
        return sb.toString();*/
        return iName;
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
}
