package se.swedsoft.bookkeeping.print.report;


import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;

import java.math.BigDecimal;
import java.util.*;


/**
 * Date: 2006-mar-02
 * Time: 16:45:12
 *
 * Förenklat årsbokslut
 */
public class SSSimpleStatementPrinter extends SSPrinter {

    private Date iDateFrom;

    private Date iDateTo;

    private GroupPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    private List<SSAccount> iAccounts;

    private Map<String, List<SSAccount>> iAccountsByReportCode;

    private Map<SSAccount, BigDecimal> iCreditMinusDebetSum;

    private Map<SSAccount, BigDecimal> iDebetMinusCreditSum;

    /**
     *
     * @param iDateFrom
     * @param iDateTo
     */
    public SSSimpleStatementPrinter(Date iDateFrom, Date iDateTo) {
        this(SSDB.getInstance().getAccounts(), iDateFrom, iDateTo);
    }

    /**
     *
     * @param iAccounts
     * @param iDateFrom
     * @param iDateTo
     */
    public SSSimpleStatementPrinter(List<SSAccount> iAccounts, Date iDateFrom, Date iDateTo) {
        this.iDateFrom = iDateFrom;
        this.iDateTo = iDateTo;
        this.iAccounts = iAccounts;

        setPageHeader("simplestatement.jrxml");
        setDetail("simplestatement.jrxml");
        setPageFooter("simplestatement.jrxml");

        setColumnCount(2);
        setColumnWidth(275);
        setColumnSpacing(5);

        calculate();
    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("simplestatement.title");
    }

    /**
     *
     */
    private void calculate() {
        // Get all vouchers
        List<SSVoucher> iVouchers = SSVoucherMath.getVouchers(
                SSDB.getInstance().getVouchers(), iDateFrom, iDateTo);

        iCreditMinusDebetSum = SSVoucherMath.getCreditMinusDebetSum(iVouchers);
        iDebetMinusCreditSum = SSVoucherMath.getDebetMinusCreditSum(iVouchers);

        iAccountsByReportCode = new HashMap<String, List<SSAccount>>();

        for (SSAccount iAccount : iAccounts) {
            String iReportCode = iAccount.getReportCode();

            List<SSAccount> iAccountsForReportCode = iAccountsByReportCode.get(iReportCode);

            if (iAccountsForReportCode == null) {
                iAccountsForReportCode = new LinkedList<SSAccount>();

                iAccountsByReportCode.put(iReportCode, iAccountsForReportCode);
            }
            iAccountsForReportCode.add(iAccount);
        }
    }

    /**
     *
     * @param group
     * @param number
     * @return
     */
    private BigDecimal getValueForGroup(String group, int number) {
        Map<SSAccount, BigDecimal> iValues;

        switch (number) {
        case  1:
            iValues = iDebetMinusCreditSum;
            break;

        case  2:
            iValues = iDebetMinusCreditSum;
            break;

        case  3:
            iValues = iCreditMinusDebetSum;
            break;

        case  4:
            iValues = iCreditMinusDebetSum;
            break;

        case  5:
            iValues = iCreditMinusDebetSum;
            break;

        case  6:
            iValues = iCreditMinusDebetSum;
            break;

        case  7:
            iValues = iCreditMinusDebetSum;
            break;

        case  8:
            iValues = iCreditMinusDebetSum;
            break;

        case  9:
            iValues = iCreditMinusDebetSum;
            break;

        case 10:
            iValues = iDebetMinusCreditSum;
            break;

        case 11:
            iValues = iCreditMinusDebetSum;
            break;

        default:
            return new BigDecimal(0);
        }

        if (group.equals("B11")) {
            return null;
        }
        if (group.equals("B12")) {
            return null;
        }

        if (group.equals("B10")) {
            BigDecimal iSumForAccounts = getSumForAccounts(iValues, group);

            iSumForAccounts = iSumForAccounts.add(
                    getSumForAccounts(iValues, "U1", "U2", "U3", "U4"));
            return iSumForAccounts;
        } else {
            return getSumForAccounts(iValues, group);
        }
    }

    /**
     *
     * @param group
     * @param number
     * @return
     */
    private BigDecimal getSumForGroup(String group, int number) {

        if (group.equals("B9")) {
            return getSumForAccounts(iDebetMinusCreditSum, "B1", "B2", "B3", "B4", "B5",
                    "B6", "B7", "B8", "B9");
        }
        if (group.equals("B16")) {
            return getSumForAccounts(iCreditMinusDebetSum, "B10", "B13", "B14", "B15",
                    "B16", "U1", "U2", "U3", "U4");
        }

        return null;
    }

    /**
     *
     * @param group
     * @param number
     * @return
     */
    private boolean getShowSeparatorForGroup(String group, int number) {
        return !(group.equals("B5") || group.equals("B10") || group.equals("B11")
                || group.equals("B12") || group.equals("R4") || group.equals("R8")
                || group.equals("R10") || group.equals("R11") || group.equals("U4"));
    }

    /**
     *
     * @param group
     * @return
     */
    private int getSection(int group) {
        switch (group) {
        case  1:
            return 1;

        case  2:
            return 1;

        case  3:
            return 3;

        case  4:
            return 3;

        case  5:
            return 3;

        case  6:
            return 3;

        case  7:
            return 2;

        case  8:
            return 2;

        case  9:
            return 4;

        case 10:
            return 4;

        case 11:
            return 4;
        }
        return 0;
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
        addParameter("date.from", iDateFrom);
        addParameter("date.to", iDateTo);

        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        if (iCompany != null) {
            addParameter("company.name", iCompany.getName());
            addParameter("company.corporateid", iCompany.getCorporateID());
        }

        iPrinter = new GroupPrinter();
        iPrinter.generateReport();

        addParameter("Report", iPrinter.getReport());
        addParameter("Parameters", iPrinter.getParameters());

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

        SSDefaultTableModel<Integer> iModel = new SSDefaultTableModel<Integer>() {

            @Override
            public Class getType() {
                return String.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Integer iNumber = getObject(rowIndex);

                Object value = null;

                switch (columnIndex) {
                case 0:
                    value = iNumber;
                    break;

                case 1:
                    value = getSection(iNumber);
                    break;

                case 2:
                    value = SSBundle.getBundle().getString(
                            "simplestatement.group." + iNumber);
                    break;

                case 3:
                    iPrinter.setGroup(iNumber);

                    iDataSource.reset();

                    value = iDataSource;
                    break;

                }
                return value;
            }
        };

        iModel.addColumn("group.number");
        iModel.addColumn("group.section");
        iModel.addColumn("group.description");
        iModel.addColumn("group.rows");

        iModel.add(1);
        iModel.add(2);
        iModel.add(7);
        iModel.add(8);
        iModel.add(3);
        iModel.add(4);
        iModel.add(5);
        iModel.add(6);
        iModel.add(9);
        iModel.add(10);
        iModel.add(11);

        return iModel;
    }

    private class GroupPrinter extends SSPrinter {

        private SSDefaultTableModel<String> iModel;

        private Integer iGroup;

        /**
         *
         */
        public GroupPrinter() {
            setMargins(0, 0, 0, 0);

            setSize(250, 842);
            setColumnWidth(250);
            setDetail("simplestatement.group.jrxml");
            setSummary("simplestatement.group.jrxml");

            iModel = new SSDefaultTableModel<String>() {

                @Override
                public Class getType() {
                    return Integer.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    String iNumber = getObject(rowIndex);

                    switch (columnIndex) {
                    case 0:
                        value = iNumber;
                        break;

                    case 1:
                        value = SSBundle.getBundle().getString(
                                "simplestatement.group." + iNumber);
                        break;

                    case 2:
                        value = getValueForGroup(iNumber, iGroup);
                        break;

                    case 3:
                        value = SSBundle.getBundle().getString(
                                "simplestatement.group." + iNumber + ".sum");
                        break;

                    case 4:
                        value = getSumForGroup(iNumber, iGroup);
                        break;

                    case 5:
                        value = getShowSeparatorForGroup(iNumber, iGroup);
                        break;
                    }

                    return value;
                }
            };

            iModel.addColumn("group.number");
            iModel.addColumn("group.description");
            iModel.addColumn("group.value");

            iModel.addColumn("group.sumtext");
            iModel.addColumn("group.sum");

            iModel.addColumn("group.showseparator");

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
         * @param iNumber
         */
        public void setGroup(Integer iNumber) {
            iGroup = iNumber;

            List<String> iObjects = new LinkedList<String>();

            switch (iNumber) {
            case 1:
                iObjects.add("B1");
                iObjects.add("B2");
                iObjects.add("B3");
                iObjects.add("B4");
                iObjects.add("B5");
                break;

            case 2:
                iObjects.add("B6");
                iObjects.add("B7");
                iObjects.add("B8");
                iObjects.add("B9");
                break;

            case 3:
                iObjects.add("B10");
                break;

            case 4:
                iObjects.add("B11");
                break;

            case 5:
                iObjects.add("B12");
                break;

            case 6:
                iObjects.add("B13");
                iObjects.add("B14");
                iObjects.add("B15");
                iObjects.add("B16");
                break;

            case 7:
                iObjects.add("R1");
                iObjects.add("R2");
                iObjects.add("R3");
                iObjects.add("R4");
                break;

            case 8:
                iObjects.add("R5");
                iObjects.add("R6");
                iObjects.add("R7");
                iObjects.add("R8");
                break;

            case 9:
                iObjects.add("R9");
                iObjects.add("R10");
                break;

            case 10:
                iObjects.add("R11");
                break;

            case 11:
                iObjects.add("U1");
                iObjects.add("U2");
                iObjects.add("U3");
                iObjects.add("U4");
                break;
            }
            iModel.setObjects(iObjects);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();

            sb.append(
                    "se.swedsoft.bookkeeping.print.report.SSSimpleStatementPrinter.GroupPrinter");
            sb.append("{iGroup=").append(iGroup);
            sb.append(", iModel=").append(iModel);
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     *
     * @param iSums
     * @param iReportCodes
     * @return
     */
    private BigDecimal getSumForAccounts(Map<SSAccount, BigDecimal> iSums, String... iReportCodes) {
        BigDecimal iSum = new BigDecimal(0);
        SSNewAccountingYear iYear = SSDB.getInstance().getCurrentYear();

        for (SSAccount iAccount : iAccounts) {
            if (hasReportCode(iAccount, iReportCodes)) {
                BigDecimal iSumForAccount = iSums.get(iAccount);
                BigDecimal iInbalanceForAccount = iYear.getInBalance(iAccount);

                if (iSumForAccount != null) {
                    iSum = iSum.add(iSumForAccount);
                }
                if (iInbalanceForAccount != null) {
                    if (hasReportCode(iAccount, "B10", "B13", "B14", "B15", "B16", "U1",
                            "U2", "U3", "U4")) {
                        iSum = iSum.add(iInbalanceForAccount.multiply(new BigDecimal(-1)));
                    } else {
                        iSum = iSum.add(iInbalanceForAccount);
                    }
                }
            }
        }
        return iSum;
    }

    /**
     *
     * @param iAccount
     * @param iReportCodes
     * @return
     */
    private boolean hasReportCode(SSAccount iAccount, String... iReportCodes) {

        String iReportCodeForAccount = iAccount.getReportCode();

        for (String iReportCode : iReportCodes) {

            if (iReportCode.equals(iReportCodeForAccount)) {

                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.report.SSSimpleStatementPrinter");
        sb.append("{iAccounts=").append(iAccounts);
        sb.append(", iAccountsByReportCode=").append(iAccountsByReportCode);
        sb.append(", iCreditMinusDebetSum=").append(iCreditMinusDebetSum);
        sb.append(", iDataSource=").append(iDataSource);
        sb.append(", iDateFrom=").append(iDateFrom);
        sb.append(", iDateTo=").append(iDateTo);
        sb.append(", iDebetMinusCreditSum=").append(iDebetMinusCreditSum);
        sb.append(", iPrinter=").append(iPrinter);
        sb.append('}');
        return sb.toString();
    }
}
