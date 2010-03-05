package se.swedsoft.bookkeeping.calc.data;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Date: 2006-feb-27
 * Time: 16:01:52
 */
public class SSAccountGroup implements Serializable {

    private static ResourceBundle cBundle = SSBundle.getBundle();

    private Integer iId;

    private String iBundle;

    private Integer iFromAccount;

    private Integer iToAccount;

    private List<SSAccountGroup> iGroups;

    /**
     *
     */
    public SSAccountGroup(){
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
    /**
     *
     * @param iFromAccount
     */
    public void setFromAccount(Integer iFromAccount) {
        this.iFromAccount = iFromAccount;
    }


    /**
     *
     * @return
     */
    public Integer getFromAccount() {
        if(iFromAccount == null && iGroups != null){
            iFromAccount = Integer.MAX_VALUE;
            for(SSAccountGroup iAccountGroup : iGroups){
                if(iAccountGroup.getFromAccount() < iFromAccount ){
                    iFromAccount = iAccountGroup.getFromAccount();
                }
            }
        }

        return iFromAccount;
    }
    /**
     *
     * @return
     */
    public Integer getToAccount() {
        if(iToAccount == null && iGroups != null){
            iToAccount = Integer.MIN_VALUE;
            for(SSAccountGroup iAccountGroup : iGroups){
                if(iAccountGroup.getToAccount() > iToAccount ){
                    iToAccount = iAccountGroup.getToAccount();
                }
            }
        }

        return iToAccount;
    }

    /**
     *
     * @param iToAccount
     */
    public void setToAccount(Integer iToAccount) {
        this.iToAccount = iToAccount;
    }

    /**
     *
     * @return
     */
    public String getBundle() {
        return iBundle;
    }

    /***
     *
     * @param iBundle
     */
    public void setBundle(String iBundle) {
        this.iBundle = iBundle;
    }

    /**
     *
     * @return
     */
    public List<SSAccountGroup> getGroups() {
        return iGroups;
    }

    /**
     *
     * @param pAccountGroup
     */
    public void addAccountGroup(SSAccountGroup pAccountGroup){
        if(iGroups == null ){
            iGroups = new LinkedList<SSAccountGroup>();
        }

        iGroups.add(pAccountGroup);
    }


    /**
     *
     * @return The title for this account group
     */
    public String getTitle(){
        return cBundle.getString(iBundle);
    }

    /**
     *
     * @return The sum title for this account group
     */
    public String getSumTitle(){
        return cBundle.getString(iBundle + ".sum");
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
        return toString("");
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
    public String toString(String Seperator) {
        StringBuilder sb = new StringBuilder();

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
        sb.append('\n');
        return sb.toString();
    }

    /**
     *
     * @param iAccounts
     * @return
     */
    public List<SSAccount> getGroupAccounts(List<SSAccount> iAccounts){
        List<SSAccount> iGroupAccounts = new LinkedList<SSAccount>();
        for(SSAccount iAccount: iAccounts){
            if( iAccount.getNumber() >= getFromAccount() && iAccount.getNumber() <= getToAccount()  ){
                iGroupAccounts.add(iAccount);
            }
        }
        return iGroupAccounts;
    }


}


