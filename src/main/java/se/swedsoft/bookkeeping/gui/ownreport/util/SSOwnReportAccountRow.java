package se.swedsoft.bookkeeping.gui.ownreport.util;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSMonth;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Johan Gunnarsson
 * Date: 2007-nov-26
 * Time: 16:36:45
 */


public class SSOwnReportAccountRow implements Serializable {

    static final long serialVersionUID = 1L;
    
    private SSAccount iAccount;

    private Map<SSMonth, BigDecimal> iBudget;

    public SSOwnReportAccountRow(){
        iAccount = new SSAccount();
        iBudget = new HashMap<SSMonth, BigDecimal>();

        SSNewAccountingYear iYear = SSDB.getInstance().getCurrentYear();
        List<SSMonth> iMonths = SSMonth.splitYearIntoMonths(iYear);
        for(SSMonth iMonth : iMonths){
            iBudget.put(iMonth, new BigDecimal(0));
        }
    }

    public SSAccount getAccount() {
        return iAccount;
    }

    public void setAccount(SSAccount iAccount) {
        this.iAccount = iAccount;
    }

    public void setAccount(String iAccountNr){
        Integer iNumber = Integer.parseInt(iAccountNr);
        for(SSAccount pAccount : SSDB.getInstance().getCurrentYear().getAccounts()){
            if(pAccount.getNumber().equals(iNumber)){
                iAccount = pAccount;
            }
        }
    }

    public Map<SSMonth, BigDecimal> getBudget() {
        return iBudget;
    }

    public void setBudget(Map<SSMonth, BigDecimal> iBudget) {
        this.iBudget = iBudget;
    }

    public void setYearBudget(BigDecimal iValue){
        SSNewAccountingYear iYear = SSDB.getInstance().getCurrentYear();
        if(iYear == null) return;

        BigDecimal iMonthly = iValue.divide(new BigDecimal(iBudget.keySet().size()),new MathContext(100));

        for(SSMonth iMonth : iBudget.keySet()){
            if(iBudget.containsKey(iMonth)){
                iBudget.put(iMonth, iMonthly);
            }
        }
    }

    public BigDecimal getSum(){
        BigDecimal iSum = new BigDecimal(0);
        for(Map.Entry<SSMonth, BigDecimal> ssMonthBigDecimalEntry : iBudget.entrySet()){
            iSum = iSum.add(ssMonthBigDecimalEntry.getValue());
        }
        return iSum;
    }

    public BigDecimal getSumForMonths(Date iFrom, Date iTo){
        BigDecimal iSum = new BigDecimal(0);
        List<SSMonth> iMonths = SSMonth.splitYearIntoMonths(iFrom, iTo);
        for(SSMonth iMonth : iMonths){
            if(iBudget.containsKey(iMonth)){
                iSum = iSum.add(iBudget.get(iMonth));
            }
        }
        return iSum;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.ownreport.util.SSOwnReportAccountRow");
        sb.append("{iAccount=").append(iAccount);
        sb.append(", iBudget=").append(iBudget);
        sb.append('}');
        return sb.toString();
    }
}
