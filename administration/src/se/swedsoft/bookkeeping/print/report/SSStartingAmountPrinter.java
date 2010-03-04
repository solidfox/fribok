package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.util.*;
import java.math.BigDecimal;

/**
 * Date: 2006-feb-16
 * Time: 10:34:40
 */
public class SSStartingAmountPrinter extends SSPrinter {

    private Map<SSAccount, BigDecimal> iInBalance;
    private Date iFrom;
    private Date iTo;

    /**
     *
     */
    public SSStartingAmountPrinter(  ){
      this( SSDB.getInstance().getCurrentYear() );
    }

    /**
     *
     * @param iAccountingYear
     */
    public SSStartingAmountPrinter(SSNewAccountingYear iAccountingYear ){
      this( iAccountingYear.getInBalance(), iAccountingYear.getFrom(), iAccountingYear.getTo() );
    }

    /**
     *
     * @param iInBalance
     * @param iFrom
     * @param iTo
     */
    public SSStartingAmountPrinter(Map<SSAccount, BigDecimal> iInBalance, Date iFrom, Date iTo) {
        this.iInBalance = iInBalance;
        this.iFrom = iFrom;
        this.iTo = iTo;
                
        setPageHeader  ("header.jrxml");
        setColumnHeader("startingammount.jrxml");
        setDetail      ("startingammount.jrxml");
        setSummary     ("startingammount.jrxml");
     }



    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("inbalancereport.title");
    }


    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
        addParameter("dateFrom", iFrom );
        addParameter("dateTo"  , iTo);

        List<SSAccount> iAccounts = new LinkedList<SSAccount>();

        // Extract all accounts
        for(SSAccount iAccount: iInBalance.keySet()){
            iAccounts.add(iAccount);
        }

        SSDefaultTableModel<SSAccount> iModel = new SSDefaultTableModel<SSAccount>(){

            @Override
            public Class getType() {
                return SSAccount.class;

            }
            public Object getValueAt(int rowIndex, int columnIndex) {
                SSAccount iAccount = getObject(rowIndex);

                Object value = null;

                switch(columnIndex){
                    case 0:
                        value = iAccount.getNumber();
                        break;
                    case 1:
                        value = iAccount.getDescription();
                        break;
                    case 2:
                        value = iInBalance.get(iAccount);
                        break;
                }

                return value;
            }

        };
        iModel.addColumn( "account" );
        iModel.addColumn( "description" );
        iModel.addColumn( "inbalance" );

        Collections.sort(iAccounts, new Comparator<SSAccount>() {
            public int compare(SSAccount o1, SSAccount o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });

        iModel.setObjects( iAccounts );


        return iModel;
    }






}
