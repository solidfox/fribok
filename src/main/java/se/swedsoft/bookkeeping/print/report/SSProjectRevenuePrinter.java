package se.swedsoft.bookkeeping.print.report;


import se.swedsoft.bookkeeping.calc.math.SSCreditInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSDateMath;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.SSCreditInvoice;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSMonth;
import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;

import java.math.BigDecimal;
import java.util.*;


/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSProjectRevenuePrinter extends SSPrinter {

    private SSMonthlyDistributionPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    private List<SSNewProject> iProjects;

    private Date  iDateFrom;

    private Date  iDateTo;

    Map<String, Map<SSMonth, BigDecimal>> iProjectRevenue;

    /**
     *
     * @param pProjects
     * @param pFrom
     * @param pTo
     */
    public SSProjectRevenuePrinter(List<SSNewProject> pProjects, Date pFrom, Date pTo) {
        iProjects = pProjects;
        iDateFrom = SSDateMath.floor(pFrom);
        iDateTo = SSDateMath.ceil(pTo);
        calculate();
        setPageHeader("header_period.jrxml");
        setColumnHeader("projectrevenue.jrxml");
        setDetail("projectrevenue.jrxml");
    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("projectrevenue.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
        addParameter("dateFrom", iDateFrom);
        addParameter("dateTo", iDateTo);

        iPrinter = new SSMonthlyDistributionPrinter(iDateFrom, iDateTo);
        iPrinter.generateReport();

        addParameter("Report", iPrinter.getReport());
        addParameter("Parameters", iPrinter.getParameters());

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

        SSDefaultTableModel<SSNewProject> iModel = new SSDefaultTableModel<SSNewProject>() {
            @Override
            public Class getType() {
                return SSNewProject.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSNewProject iProject = getObject(rowIndex);

                switch (columnIndex) {
                case 0:
                    value = iProject.getNumber();
                    break;

                case 1:
                    value = iProject.getName();
                    break;

                case 2:
                    iPrinter.setProject(iProject,
                            iProjectRevenue.get(iProject.getNumber()));
                    iDataSource.reset();
                    value = iDataSource;
                    break;
                }

                return value;
            }
        };

        iModel.addColumn("project.number");
        iModel.addColumn("project.description");
        iModel.addColumn("month.data");

        iModel.setObjects(iProjects);

        return iModel;
    }

    private void calculate() {
        iProjectRevenue = new HashMap<String, Map<SSMonth, BigDecimal>>();
        List<SSInvoice> iInvoices = SSDB.getInstance().getInvoices();

        for (SSInvoice iInvoice : iInvoices) {
            if (iInvoice.getDate().after(iDateFrom) && iInvoice.getDate().before(iDateTo)) {
                Calendar iCal = Calendar.getInstance();

                iCal.setTime(iInvoice.getDate());
                iCal.set(Calendar.DAY_OF_MONTH, 1);
                Date iFrom = iCal.getTime();

                iCal.set(Calendar.DAY_OF_MONTH,
                        iCal.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date iTo = iCal.getTime();
                SSMonth iMonth = new SSMonth(iFrom, iTo);

                for (SSSaleRow iRow : iInvoice.getRows()) {
                    if (iRow.getProjectNr() != null && iRow.getSum() != null) {
                        BigDecimal iSum = SSInvoiceMath.convertToLocal(iInvoice,
                                iRow.getSum());
                        Map<SSMonth, BigDecimal> iRevenueInMonth;

                        if (iProjectRevenue.containsKey(iRow.getProjectNr())) {
                            iRevenueInMonth = iProjectRevenue.get(iRow.getProjectNr());
                            if (iRevenueInMonth.containsKey(iMonth)) {
                                iRevenueInMonth.put(iMonth,
                                        iRevenueInMonth.get(iMonth).add(iSum));
                            } else {
                                iRevenueInMonth.put(iMonth, iSum);
                            }
                        } else {
                            iRevenueInMonth = new HashMap<SSMonth, BigDecimal>();
                            iRevenueInMonth.put(iMonth, iSum);
                        }
                        iProjectRevenue.put(iRow.getProjectNr(), iRevenueInMonth);
                    }
                }
            }
        }

        List<SSCreditInvoice> iCreditInvoices = SSDB.getInstance().getCreditInvoices();

        for (SSCreditInvoice iCreditInvoice : iCreditInvoices) {
            if (iCreditInvoice.getDate().after(iDateFrom)
                    && iCreditInvoice.getDate().before(iDateTo)) {
                Calendar iCal = Calendar.getInstance();

                iCal.setTime(iCreditInvoice.getDate());
                iCal.set(Calendar.DAY_OF_MONTH, 1);
                Date iFrom = iCal.getTime();

                iCal.set(Calendar.DAY_OF_MONTH,
                        iCal.getActualMaximum(Calendar.DAY_OF_MONTH));
                Date iTo = iCal.getTime();
                SSMonth iMonth = new SSMonth(iFrom, iTo);

                for (SSSaleRow iRow : iCreditInvoice.getRows()) {
                    if (iRow.getProjectNr() != null && iRow.getSum() != null) {
                        BigDecimal iSum = SSCreditInvoiceMath.convertToLocal(
                                iCreditInvoice, iRow.getSum());
                        Map<SSMonth, BigDecimal> iRevenueInMonth;

                        if (iProjectRevenue.containsKey(iRow.getProjectNr())) {
                            iRevenueInMonth = iProjectRevenue.get(iRow.getProjectNr());
                            if (iRevenueInMonth.containsKey(iMonth)) {
                                iRevenueInMonth.put(iMonth,
                                        iRevenueInMonth.get(iMonth).subtract(iSum));
                            } else {
                                iRevenueInMonth.put(iMonth, iSum.negate());
                            }
                        } else {
                            iRevenueInMonth = new HashMap<SSMonth, BigDecimal>();
                            iRevenueInMonth.put(iMonth, iSum.negate());
                        }
                        iProjectRevenue.put(iRow.getProjectNr(), iRevenueInMonth);
                    }
                }
            }
        }
    }

    private class SSMonthlyDistributionPrinter extends SSPrinter {

        private SSDefaultTableModel<SSMonth> iModel;

        private SSNewProject iProject;
        private Date  iFrom;

        private Date  iTo;

        private Map<SSMonth, BigDecimal> iRevenue;

        /**
         *
         * @param pFrom
         * @param pTo
         */
        public SSMonthlyDistributionPrinter(Date pFrom, Date pTo) {
            iFrom = pFrom;
            iTo = pTo;
            setMargins(0, 0, 0, 0);

            setDetail("projectrevenue.monthly.jrxml");
            setSummary("projectrevenue.monthly.jrxml");

            iModel = new SSDefaultTableModel<SSMonth>(
                    SSMonth.splitYearIntoMonths(iFrom, iTo)) {

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
                        if (iProject != null && iRevenue.containsKey(iMonth)) {
                            value = iRevenue.get(iMonth);
                        } else {
                            value = new BigDecimal(0);
                        }
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
         * @param pProject
         * @param iMap
         */
        public void setProject(SSNewProject pProject, Map<SSMonth, BigDecimal> iMap) {
            iProject = pProject;
            iRevenue = iMap;
            if (iRevenue == null) {
                iRevenue = new HashMap<SSMonth, BigDecimal>();
            }
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();

            sb.append(
                    "se.swedsoft.bookkeeping.print.report.SSProjectRevenuePrinter.SSMonthlyDistributionPrinter");
            sb.append("{iFrom=").append(iFrom);
            sb.append(", iModel=").append(iModel);
            sb.append(", iProject=").append(iProject);
            sb.append(", iRevenue=").append(iRevenue);
            sb.append(", iTo=").append(iTo);
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.report.SSProjectRevenuePrinter");
        sb.append("{iDataSource=").append(iDataSource);
        sb.append(", iDateFrom=").append(iDateFrom);
        sb.append(", iDateTo=").append(iDateTo);
        sb.append(", iPrinter=").append(iPrinter);
        sb.append(", iProjectRevenue=").append(iProjectRevenue);
        sb.append(", iProjects=").append(iProjects);
        sb.append('}');
        return sb.toString();
    }
}
