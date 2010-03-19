/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import javax.swing.*;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.*;


/**
 */
public class SSNewAccountingYear implements Serializable, SSTableSearchable {

    /// Constant for serialization versioning.
    static final long serialVersionUID = 1L;



    private Integer iId;

    private Date iFrom;

    private Date iTo;

    private SSAccountPlan iPlan;

    private Map<SSAccount, BigDecimal> iInBalance;

    private SSBudget iBudget;



    /**
     * Default constructor.
     */
    public SSNewAccountingYear() {
        iId        = 0;
        iFrom      = new Date();
        iTo        = new Date();
        iInBalance = new HashMap<SSAccount, BigDecimal>();
        iBudget    = new SSBudget();
    }

    /**
     *
     * @param pFrom
     * @param pTo
     */
    public SSNewAccountingYear(Date pFrom, Date pTo) {
        this();
        iFrom = pFrom;
        iTo   = pTo;
    }

    /**
     *
     * @param pAccountingYear
     */
    public SSNewAccountingYear(SSNewAccountingYear pAccountingYear) {
        this();
        setData(pAccountingYear);
    }

    public SSNewAccountingYear(SSAccountingYear iOldYear) {
        iFrom = iOldYear.getFrom();
        iTo = iOldYear.getTo();
        iPlan = iOldYear.getAccountPlan();
        iInBalance = iOldYear.getInBalance();
        iBudget = iOldYear.getBudget();
    }
    /**
     * Sets the data of the accountingyear to the same as the parameter
     *
     * Note that the data aren't copied
     *
     * @param pAccountingYear
     */
    public void setData(SSNewAccountingYear pAccountingYear){
        iId        = pAccountingYear.iId;
        iFrom      = pAccountingYear.iFrom;
        iTo        = pAccountingYear.iTo;
        iInBalance = pAccountingYear.iInBalance;
        iBudget    = pAccountingYear.iBudget;
        iPlan      = pAccountingYear.iPlan;
    }

    /**
     *
     * @return the id
     */
    public Integer getId() {
        return iId;
    }

    public void setId(Integer pId) {
        iId = pId;
    }
    /**
     *
     * @return the from date
     */
    public Date getFrom() {
        return iFrom;
    }

    /**
     *
     * @param pFrom
     */
    public void setFrom(Date pFrom) {
        iFrom = pFrom;
    }

    /**
     *
     * @return the todate
     */
    public Date getTo() {
        return iTo;
    }

    /**
     *
     * @param pTo
     */
    public void setTo(Date pTo) {
        iTo = pTo;
    }

    /**
     *
     * @return the account plan
     */
    public SSAccountPlan getAccountPlan() {
        if(iPlan == null) iPlan = new SSAccountPlan();
        
        return iPlan;
    }

    /**
     *
     * @param pAccountPlan
     */
    public void setAccountPlan(SSAccountPlan pAccountPlan) {
        iPlan = pAccountPlan;
    }



    /**
     *
     * @return the budget for the year
     */
    public SSBudget getBudget() {
        // Make shure the budget know that we are the owning year
        iBudget.setYear( this );

        return iBudget;
    }

    /**
     *
     * @param iBudget
     */
    public void setBudget(SSBudget iBudget) {
        this.iBudget = iBudget;
    }



    /**
     *
     * @return the in balance
     */
    public Map<SSAccount, BigDecimal> getInBalance() {
        return iInBalance;
    }

    /**
     *
     * @param pInBalance
     */
    public void setInBalance(Map<SSAccount, BigDecimal> pInBalance) {
        iInBalance = pInBalance;
    }


    /**
     * Returns the vouchers for the year
     *
     * @return the vouchers
     */
    public List<SSVoucher> getVouchers() {
        return SSDB.getInstance().getVouchers(this);
    }

    /**
     * Returns the accounts in the current acccountplan.
     *
     * @return A List of the current accounts or null.
     */
    public List<SSAccount> getAccounts() {
        if (iPlan != null) {
            return iPlan.getAccounts();
        }
        return Collections.emptyList();
    }


    /**
     * Returns the active accounts in the current acccountplan.
     *
     * @return A List of the active accounts or null.
     */
    public List<SSAccount> getActiveAccounts() {
        if (iPlan != null) {
            return iPlan.getActiveAccounts();
        }
        return Collections.emptyList();
    }









    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        return iFormat.format(iFrom) + " - "+ iFormat.format(iTo);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.data.SSNewAccountingYear");
        sb.append("{iBudget=").append(iBudget);
        sb.append(", iFrom=").append(iFrom);
        sb.append(", iId=").append(iId);
        sb.append(", iInBalance=").append(iInBalance);
        sb.append(", iPlan=").append(iPlan);
        sb.append(", iTo=").append(iTo);
        sb.append('}');
        return sb.toString();
    }




    /**
     *
     * @param pAccount
     * @param pAmount
     */
    public void setInBalance(SSAccount pAccount, BigDecimal pAmount) {
        if (iInBalance == null) {
            iInBalance = new HashMap<SSAccount, BigDecimal>();
        }
        iInBalance.put(pAccount, pAmount);
    }



    /**
     *
     * @param pAccount
     *
     * @return
     */
    public BigDecimal getInBalance(SSAccount pAccount) {
        if (iInBalance == null) {
            iInBalance = new HashMap<SSAccount, BigDecimal>();
        }
        BigDecimal amount = iInBalance.get(pAccount);

        if (amount == null) {
            amount = new BigDecimal(0);
        }
        return amount;
    }



    /**
     *
     * @param iObjectInputStream
     * @throws IOException
     * @throws ClassNotFoundException

    private void readObject(ObjectInputStream iObjectInputStream) throws IOException, ClassNotFoundException{
        iObjectInputStream.defaultReadObject();

        SSNewCompany        iCompany        = SSDB.getInstance().getCurrentCompany();
        SSNewAccountingYear iAccountingYear = this;

        for(SSVoucher iVoucher:  iVouchers){
            for(SSVoucherRow iVoucherRow: iVoucher.getVoucherRows())
                // @TODO: This is a hack
                iVoucherRow.updateReferences(iCompany, iAccountingYear);
        }
        notifyListeners(iCompany, iAccountingYear);
    }
   */

    /**
     *
     * @param iMainFrame The main frame
     */
    public static void openWarningDialogNoYearData(SSMainFrame iMainFrame){
        String message = SSBundle.getBundle().getString("accountingYear.no.year.message");
        String title   = SSBundle.getBundle().getString("accountingYear.no.year.title");
        JOptionPane.showMessageDialog(iMainFrame, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    ////////////////////////////////////////////////////////////////////
             /*
    public static interface SSNewAccountingYearListener{
        public void yearLoaded(SSNewCompany iCompany, SSNewAccountingYear iAccountingYear);
    }

    private static List<SSNewAccountingYearListener> iListeners = new LinkedList<SSNewAccountingYearListener>();


    public void addListener(SSNewAccountingYearListener iListener){
        iListeners.add(iListener);
    }



    private void notifyListeners(SSNewCompany iCompany, SSNewAccountingYear iAccountingYear){
        for(SSNewAccountingYearListener iListener: iListeners){
            iListener.yearLoaded(iCompany, iAccountingYear);
        }
    }  */

    
    public boolean equals(Object obj) {
        if (!(obj instanceof SSNewAccountingYear)) {
            return false;
        }
        return iId.equals(((SSNewAccountingYear) obj).iId);
    }

}
