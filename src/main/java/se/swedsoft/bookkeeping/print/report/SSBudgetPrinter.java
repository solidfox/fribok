package se.swedsoft.bookkeeping.print.report;


import se.swedsoft.bookkeeping.calc.math.SSAccountMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSBudget;
import se.swedsoft.bookkeeping.data.SSMonth;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSBudgetPrinter extends SSPrinter {

    private SSMonthlyDistributionPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    private SSNewAccountingYear iAccountingYear;

    private Date  iDateFrom;

    private Date  iDateTo;

    /**
     *
     */
    public SSBudgetPrinter() {
        this(SSDB.getInstance().getCurrentYear());
    }

    /**
     *
     * @param pFrom
     * @param pTo
     */
    public SSBudgetPrinter(Date pFrom, Date pTo) {
        this(SSDB.getInstance().getCurrentYear(), pFrom, pTo);
    }

    /**
     *
     * @param pAccountingYear The accountingyear
     */
    public SSBudgetPrinter(SSNewAccountingYear pAccountingYear) {
        this(pAccountingYear, pAccountingYear.getFrom(), pAccountingYear.getTo());
    }

    /**
     *
     * @param pAccountingYear The accountingyear
     * @param pFrom
     * @param pTo
     */
    public SSBudgetPrinter(SSNewAccountingYear pAccountingYear, Date pFrom, Date pTo) {
        iAccountingYear = pAccountingYear;
        iDateFrom = pFrom;
        iDateTo = pTo;

        setPageHeader("header_period.jrxml");
        setColumnHeader("budget.jrxml");
        setDetail("budget.jrxml");
    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("budgetreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
        addParameter("dateFrom", iDateFrom);
        addParameter("dateTo", iDateTo);

        iPrinter = new SSMonthlyDistributionPrinter(iAccountingYear, iDateFrom, iDateTo);
        iPrinter.generateReport();

        addParameter("Report", iPrinter.getReport());
        addParameter("Parameters", iPrinter.getParameters());

        final SSBudget        iBudget = iAccountingYear.getBudget();
        final List<SSAccount> iAccounts = SSAccountMath.getResultAccounts(iAccountingYear,
                iAccountingYear.getAccounts());

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

        SSDefaultTableModel<SSAccount> iModel = new SSDefaultTableModel<SSAccount>() {
            @Override
            public Class getType() {
                return SSAccount.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSAccount iAccount = getObject(rowIndex);

                switch (columnIndex) {
                case 0:
                    value = iAccount.getNumber();
                    break;

                case 1:
                    value = iAccount.getDescription();
                    break;

                case 2:
                    value = iBudget.getSumForAccount(iAccount) != null;
                    break;

                case 3:
                    iPrinter.setAccount(iAccount);

                    iDataSource.reset();

                    value = iDataSource;
                    break;
                }

                return value;
            }
        };

        iModel.addColumn("account.number");
        iModel.addColumn("account.description");
        iModel.addColumn("account.visible");
        iModel.addColumn("month.data");

        Collections.sort(iAccounts,
                new Comparator<SSAccount>() {
            public int compare(SSAccount o1, SSAccount o2) {
                return SSAccountMath.getResultGroup(o1, iAccountingYear)
                        - SSAccountMath.getResultGroup(o2, iAccountingYear);
            }
        });

        iModel.setObjects(iAccounts);

        return iModel;
    }

    private class SSMonthlyDistributionPrinter extends SSPrinter {

        private SSDefaultTableModel<SSMonth> iModel;

        private SSAccount iAccount;

        private Date  iFrom;

        private Date  iTo;

        /**
         *
         * @param pAccountingYear
         * @param pFrom
         * @param pTo
         */
        public SSMonthlyDistributionPrinter(SSNewAccountingYear pAccountingYear, Date pFrom, Date pTo) {
            iFrom = pFrom;
            iTo = pTo;
            setMargins(0, 0, 0, 0);

            setDetail("budget.monthly.jrxml");
            setSummary("budget.monthly.jrxml");

            final SSBudget iBudget = pAccountingYear.getBudget();

            iModel = new SSDefaultTableModel<SSMonth>(iBudget.getMonths()) {

                @Override
                public Class getType() {
                    return SSMonth.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    SSMonth iMonth = getObject(rowIndex);

                    switch (columnIndex) {
                    case 0:
                        value = iMonth.toString();
                        break;

                    case 1:
                        value = iMonth.getName();
                        break;

                    case 2:
                        value = iBudget.getValueForAccountAndMonth(iAccount, iMonth);
                        break;

                    case 3:
                        value = iMonth.isBetween(iFrom, iTo);
                        break;
                    }

                    return value;
                }
            };

            iModel.addColumn("month.date");
            iModel.addColumn("month.description");
            iModel.addColumn("month.value");
            iModel.addColumn("month.visible");
        }

        /**
         * Gets the data model for this report
         *
         * @return SSDefaultTableModel
         */
        @Override
        protected SSDefaultTableModel getModel() {
            return iModel;
        }

        /**
         * Gets the title for this report
         *
         * @return The title
         */
        @Override
        public String getTitle() {
            return null;
        }

        /**
         *
         * @param pAccount
         */
        public void setAccount(SSAccount pAccount) {
            iAccount = pAccount;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();

            sb.append(
                    "se.swedsoft.bookkeeping.print.report.SSBudgetPrinter.SSMonthlyDistributionPrinter");
            sb.append("{iAccount=").append(iAccount);
            sb.append(", iFrom=").append(iFrom);
            sb.append(", iModel=").append(iModel);
            sb.append(", iTo=").append(iTo);
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.report.SSBudgetPrinter");
        sb.append("{iAccountingYear=").append(iAccountingYear);
        sb.append(", iDataSource=").append(iDataSource);
        sb.append(", iDateFrom=").append(iDateFrom);
        sb.append(", iDateTo=").append(iDateTo);
        sb.append(", iPrinter=").append(iPrinter);
        sb.append('}');
        return sb.toString();
    }
}
