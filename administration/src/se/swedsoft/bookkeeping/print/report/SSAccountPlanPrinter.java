package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSAccountPlan;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.SSBookkeeping;


/**
 * Date: 2006-feb-16
 * Time: 16:47:37
 */
public class SSAccountPlanPrinter extends SSPrinter {


    SSAccountPlan iAccountPlan;

    /**
     *
     */
    public SSAccountPlanPrinter(  ){
        this( SSDB.getInstance().getCurrentYear().getAccountPlan()  );
    }

    /**
     *
     * @param pAccountPlan The accountplan
     */
    public SSAccountPlanPrinter(SSAccountPlan pAccountPlan ){
        super();
        iAccountPlan = pAccountPlan;

        setPageHeader  ("header.jrxml");
        setColumnHeader("accountplan.jrxml");
        setDetail      ("accountplan.jrxml");
    }



    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("accountplanreport.title");
    }

    /**
     * Gets the sub title  for this report
     *
     * @return The sub title
     */
    @Override
    protected  String getSubTitle(){
        return iAccountPlan.getName();
    }



    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {

        SSDefaultTableModel<SSAccount> iModel = new SSDefaultTableModel<SSAccount>() {

            @Override
            public Class getType() {
                return SSAccount.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                SSAccount iAccount = getObject(rowIndex);
                Object value = null;

                switch (columnIndex) {
                    case 0:
                        value = iAccount.getNumber();
                        break;
                    case 1:
                        value = iAccount.getDescription();
                        break;
                    case 2:
                        value = iAccount.getVATCode();
                        break;
                    case 3:
                        value = iAccount.getSRUCode();
                        break;
                    case 4:
                        value = iAccount.getReportCode();
                        break;
                }

                return value;
            }
        };
        iModel.addColumn("account.number");
        iModel.addColumn("account.description");
        iModel.addColumn("account.vat");
        iModel.addColumn("account.sru");
        iModel.addColumn("account.report");
        iModel.setObjects( iAccountPlan.getActiveAccounts() );

        return iModel;
    }



}
