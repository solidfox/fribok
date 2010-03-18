package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.calc.SSResultCalculator;
import se.swedsoft.bookkeeping.calc.data.SSAccountGroup;
import se.swedsoft.bookkeeping.calc.data.SSAccountSchema;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;

import java.math.BigDecimal;
import java.util.*;

/**
 * Date: 2006-feb-17
 * Time: 09:25:44
 */
public class SSResultPrinter extends SSPrinter {

    private static ResourceBundle bundle =  SSBundle.getBundle();

    SSNewAccountingYear iYearData;

    SSAccountSchema iAccountSchema;

    Date iDateFrom;

    Date iDateTo;

    Boolean iShowBudget;

    Boolean iShowPrevYear;

    /**
     *
     * @param pFrom
     * @param pTo
     * @param pShowBudget
     * @param pShowPrevYear
     */
    public SSResultPrinter(Date pFrom, Date pTo, Boolean pShowBudget, Boolean pShowPrevYear){
        this( SSDB.getInstance().getCurrentYear(), pFrom, pTo, pShowBudget, pShowPrevYear );
    }

    /**
     *
     * @param pYearData The year
     * @param pFrom
     * @param pTo
     * @param pShowBudget
     * @param pShowPrevYear
     */
    public SSResultPrinter(SSNewAccountingYear pYearData, Date pFrom, Date pTo, Boolean pShowBudget, Boolean pShowPrevYear){
        iYearData     = pYearData;
        iDateFrom     = pFrom;
        iDateTo       = pTo;
        iShowBudget   = pShowBudget;
        iShowPrevYear = pShowPrevYear;

        iAccountSchema = SSAccountSchema.getAccountSchema(pYearData);

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("resultcolumns.jrxml");
        setDetail      ("result.jrxml");
        setSummary     ("result.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return bundle.getString("resultreport.title");
    }

    private int iLastVisibleSummaryGroup;

    /**
     *
     * @param iRow
     * @param iLastVisibleRow
     * @return
     */
    private int getSummaryGroup(ResultRow iRow, ResultRow iLastVisibleRow){
        int iSummaryGroup = -1;

        if(iRow.getLevelGroup(1) >= 1  &&  iRow.getLevelGroup(1) <= 5  ) iSummaryGroup = 1;
        if(iRow.getLevelGroup(1) >= 6  &&  iRow.getLevelGroup(1) <= 7  ) iSummaryGroup = 2;
        if(iRow.getLevelGroup(1) >= 8  &&  iRow.getLevelGroup(1) <= 9  ) iSummaryGroup = 3;
        if(iRow.getLevelGroup(1) >= 10 &&  iRow.getLevelGroup(1) <= 10 ) iSummaryGroup = 4;
        if(iRow.getLevelGroup(1) >= 11 &&  iRow.getLevelGroup(1) <= 11 ) iSummaryGroup = 5;
        if(iRow.getLevelGroup(1) >= 12 &&  iRow.getLevelGroup(1) <= 12 ) iSummaryGroup = 6;

        if(iLastVisibleSummaryGroup != iSummaryGroup){
            if(iLastVisibleRow != null) iLastVisibleRow.iSummaryGroup = iLastVisibleSummaryGroup;

            iLastVisibleSummaryGroup = iSummaryGroup;

            return -1;
        } else {
            return -1;
        }
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {

        addParameter("dateFrom", iDateFrom );
        addParameter("dateTo"  , iDateTo);

        SSResultCalculator iCalculator = getCalculator();

        // Calculate all fields
        iCalculator.calculate();

        // Get the columns, this differes depending on what we are printing
        getColumns(iCalculator);

        List<SSAccountGroup> iResultGroups = iAccountSchema.getResultGroups();

        List<SSAccount> iAccounts = iYearData.getAccounts();

        List<ResultRow> iRows = new LinkedList<ResultRow>();


        iLastVisibleSummaryGroup = 1;

        ResultRow iLastVisibleRow = null;

        for(SSAccountGroup iResultGroup: iResultGroups){
            List<ResultRow> iCurrentRows = getRows(iResultGroup,  iAccounts,  0);
            for(ResultRow iRow: iCurrentRows){
                SSAccount iAccount = iRow.iAccount;


                boolean isColumn1 = (iColumn1 != null && iColumn1.containsKey(iAccount));
                boolean isColumn2 = (iColumn2 != null && iColumn2.containsKey(iAccount));
                boolean isColumn3 = (iColumn3 != null && iColumn3.containsKey(iAccount));

                // Only add the row if any field from the result report is not null
                if(isColumn1 || isColumn2 || isColumn3 ){
                    iRows.add( iRow );

                   // if( iRow.iSummaryGroup != -1) iLastVisibleSummaryGroup = iRow.iSummaryGroup;

                    iRow.iSummaryGroup = getSummaryGroup(iRow, iLastVisibleRow);

                    iLastVisibleRow = iRow;
                }
            }
        }

        SSDefaultTableModel<ResultRow> iModel = new SSDefaultTableModel<ResultRow>(){

            @Override
            public Class getType() {
                return ResultRow.class;

            }
            public Object getValueAt(int rowIndex, int columnIndex) {
                ResultRow iRow    = getObject(rowIndex);

                SSAccount iAccount = iRow.iAccount;

                Object value = null;

                switch(columnIndex){
                    case 0:
                        // account.number
                        value = iAccount.getNumber();
                        break;
                    case 1:
                        // account.description
                        value = iAccount.getDescription();
                        break;
                    case 2:
                        // account.group.1
                        value = iRow.getLevelGroup(0);
                        break;
                    case 3:
                        // account.group.2
                        value = iRow.getLevelGroup(1);
                        break;

                    case 4:
                        // account.value.1
                        value = (iColumn1 == null) ? null : iColumn1.get(iAccount);
                        break;

                    case 5:
                        // account.value.2
                        value = (iColumn2 == null) ? null : iColumn2.get(iAccount);
                        break;
                    case 6:
                        //account.value.3
                        value = (iColumn3 == null) ? null : iColumn3.get(iAccount);
                        break;

                    case 7:
                        // group.1.title
                        value =  iRow.iLevelGroups[0] != null ? iRow.iLevelGroups[0].getTitle() : "";
                        break;
                    case 8:
                        // group.2.title
                        value =  iRow.iLevelGroups[1] != null ? iRow.iLevelGroups[1].getTitle() : "";
                        break;

                    case 9:
                        //  group.1.sumtitle
                        value =  iRow.iLevelGroups[0] != null ? iRow.iLevelGroups[0].getSumTitle() : "";
                        break;
                    case 10:
                        // group.2.sumtitle
                        value =  iRow.iLevelGroups[1] != null ? iRow.iLevelGroups[1].getSumTitle() : "";
                        break;
                    case 11:
                        //summary.index
                        value =  iRow.iSummaryGroup;
                        break;
                    case 12:
                        //summary.title
                        value = bundle.getString("resultreport.summary." + iRow.getLevelGroup(1) );
                        break;
                }

                return value;
            }

        };

        iModel.addColumn( "account.number" );
        iModel.addColumn( "account.description" );
        iModel.addColumn( "account.group.1" );
        iModel.addColumn( "account.group.2" );

        iModel.addColumn( "account.value.1");
        iModel.addColumn( "account.value.2");
        iModel.addColumn( "account.value.3");

        iModel.addColumn( "group.1.title");
        iModel.addColumn( "group.2.title");

        iModel.addColumn( "group.1.sumtitle");
        iModel.addColumn( "group.2.sumtitle");

        iModel.addColumn( "summary.index");
        iModel.addColumn( "summary.title");

        iModel.setObjects( iRows );

        return iModel;
    }

    /**
     *
     * @return iCalculator
     */
    protected SSResultCalculator getCalculator() {
        return new SSResultCalculator(iYearData, iDateFrom, iDateTo, null, null );
    }


    protected Map<SSAccount, BigDecimal> iColumn1;
    protected Map<SSAccount, BigDecimal> iColumn2;
    protected Map<SSAccount, BigDecimal> iColumn3;

    /**
     *
     * @param iCalculator
     */
    protected void getColumns( SSResultCalculator iCalculator){

        if(iShowBudget){
            addParameter("column.text.1", bundle.getString("resultreport.column.4") ); // Budgetr
            addParameter("column.text.2", bundle.getString("resultreport.column.1") ); // Perioden
            addParameter("column.text.3", bundle.getString("resultreport.column.5") );

            iColumn1 = iCalculator.getChangeBudget();
            iColumn2 = iCalculator.getChangePeriod();
            iColumn3 = iCalculator.getDeviation(iColumn1, iColumn2);

        } else
        if(iShowPrevYear){
            addParameter("column.text.1", bundle.getString("resultreport.column.6") );  // Föregående år
            addParameter("column.text.2", bundle.getString("resultreport.column.1") );  // Perioden
            addParameter("column.text.3", bundle.getString("resultreport.column.5") );  // Avvikelse

            iColumn1 = iCalculator.getChangeLastYear();
            iColumn2 = iCalculator.getChangePeriod();
            iColumn3 = iCalculator.getDeviation(iColumn1, iColumn2);
        } else {
            addParameter("column.text.2", bundle.getString("resultreport.column.1") ); // Perioden
            addParameter("column.text.3", bundle.getString("resultreport.column.2") ); // Hela året

            iColumn1 = null;
            iColumn2 = iCalculator.getChangePeriod();
            iColumn3 = iCalculator.getChange();
        }

    }



    /**
     *
     * @param iGroup
     * @param iAccounts
     * @param iLevel
     * @return
     */
    private List<ResultRow> getRows(SSAccountGroup iGroup, List<SSAccount> iAccounts, int iLevel){
        List<SSAccount> iGroupAccounts = iGroup.getGroupAccounts(iAccounts);

        List<ResultRow> iRows = new LinkedList<ResultRow>();

        // This is a leaf node, add this node's groups to the list
        if( iGroup.getGroups() == null ){

            for(SSAccount iAccount: iGroupAccounts){
                ResultRow iRow = new ResultRow();

                iRow.iAccount = iAccount;
                iRow.iLevelGroups[iLevel] = iGroup;

                iRows.add(iRow);
            }
        } else {
            for(SSAccountGroup iBalanceGroup: iGroup.getGroups() ){
                iRows.addAll( getRows(iBalanceGroup,  iAccounts,  iLevel+1) );
            }
            for(ResultRow iRow: iRows){
                iRow.iLevelGroups[iLevel] = iGroup;
            }

        }
        return iRows;
    }


    private class ResultRow{

        SSAccount iAccount;

        SSAccountGroup [] iLevelGroups = new SSAccountGroup[2];

        Integer iSummaryGroup;

        public int getLevelGroup(int iLevel){
            if(iLevelGroups[iLevel] == null) return -1;

            return iLevelGroups[iLevel].getId();
        }
    }
}


