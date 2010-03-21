/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import javax.swing.*;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.server.UID;
import java.text.DateFormat;
import java.util.*;


/**
 */
public class SSAccountingYear implements Serializable, SSTableSearchable {

    /// Constant for serialization versioning.
    static final long serialVersionUID = 1L;



    private UID iId;

    private Date iFrom;

    private Date iTo;

    private SSAccountPlan iPlan;

    private Map<SSAccount, BigDecimal> iInBalance;

    private List<SSVoucher> iVouchers;

    private SSBudget iBudget;



    /**
     * Default constructor.
     */
    public SSAccountingYear() {
        iId        = new UID();
        iFrom      = new Date();
        iTo        = new Date();
        iInBalance = new HashMap<SSAccount, BigDecimal>();
        iVouchers  = new LinkedList<SSVoucher>();
        iBudget    = new SSBudget();
    }

    /**
     *
     * @param pFrom
     * @param pTo
     */
    public SSAccountingYear(Date pFrom, Date pTo) {
        this();
        iFrom = pFrom;
        iTo   = pTo;
    }

    /**
     *
     * @param pAccountingYear
     */
    public SSAccountingYear(SSAccountingYear pAccountingYear) {
        this();
        setData(pAccountingYear);
    }

    /**
     * Sets the data of the accountingyear to the same as the parameter
     *
     * Note that the data aren't copied
     *
     * @param pAccountingYear
     */
    public void setData(SSAccountingYear pAccountingYear){
        iId        = pAccountingYear.iId;
        iFrom      = pAccountingYear.iFrom;
        iTo        = pAccountingYear.iTo;
        iInBalance = pAccountingYear.iInBalance;
        iVouchers  = pAccountingYear.iVouchers;
        iBudget    = pAccountingYear.iBudget;
        iPlan      = pAccountingYear.iPlan;
    }

    /**
     *
     * @return the id
     */
    public UID getId() {
        return iId;
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
        //iBudget.setYear( this );

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
        return iVouchers;
    }

    /**
     * Set the vouchers for the year
     *
     * @param pVouchers the vouchers
     */
    public void setVouchers( List<SSVoucher> pVouchers) {
        iVouchers = pVouchers;
    }

    public void updateVoucher(SSVoucher pVoucher) {
        for (int i = 0; i<iVouchers.size();i++) {
            SSVoucher iVoucher = iVouchers.get(i);
            if (iVoucher.getNumber()==pVoucher.getNumber()) {
                iVouchers.remove(i);
                iVouchers.add(i,pVoucher);
            }
        }
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

    
    public String toString() {
        DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        StringBuilder sb = new StringBuilder();

        sb.append(iFormat.format(iFrom));
        sb.append(' ');
        sb.append( SSBundle.getBundle().getString("date.seperator") );
        sb.append(' ');
        sb.append(iFormat.format(iTo));

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

        SSCompany        iCompany        = SSDB.getInstance().getCurrentCompany();
        SSAccountingYear iAccountingYear = this;

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
    public static interface SSAccountingYearListener{
        public void yearLoaded(SSCompany iCompany, SSAccountingYear iAccountingYear);
    }

    private static List<SSAccountingYearListener> iListeners = new LinkedList<SSAccountingYearListener>();


    public void addListener(SSAccountingYearListener iListener){
        iListeners.add(iListener);
    }



    private void notifyListeners(SSCompany iCompany, SSAccountingYear iAccountingYear){
        for(SSAccountingYearListener iListener: iListeners){
            iListener.yearLoaded(iCompany, iAccountingYear);
        }
    }  */


}
