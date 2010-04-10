package se.swedsoft.bookkeeping.print.report;


import se.swedsoft.bookkeeping.calc.SSSalesTaxCalculator;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;

import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import static se.swedsoft.bookkeeping.calc.SSSalesTaxCalculator.SSVATReportGroup;


/**
 * Date: 2006-mar-02
 * Time: 16:45:12
 */
public class SSVATReportPrinter extends SSPrinter {

    private SSNewAccountingYear iAccountingYear;

    private Date iDateFrom;

    private Date iDateTo;

    public SSVATReportPrinter(SSNewAccountingYear pAccountingYear, Date pDateFrom, Date pDateTo) {
        iAccountingYear = pAccountingYear;
        iDateFrom = pDateFrom;
        iDateTo = pDateTo;

        setPageHeader("header_period.jrxml");
        setColumnHeader("vatreport.jrxml");
        setDetail("vatreport.jrxml");
        setSummary("vatreport.jrxml");
    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("vatreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
        addParameter("dateFrom", iDateFrom);
        addParameter("dateTo", iDateTo);

        SSSalesTaxCalculator iCalculator = new SSSalesTaxCalculator(iAccountingYear,
                iDateFrom, iDateTo);

        iCalculator.calculate();

        List<SSVATReportGroup> iReportGroups = iCalculator.getReportGroups();

        SSDefaultTableModel<SSVATReportGroup> iModel = new SSDefaultTableModel<SSVATReportGroup>() {

            @Override
            public Class getType() {
                return SSVATReportGroup.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                SSVATReportGroup iReportGroup = getObject(rowIndex);
                Object value = null;

                switch (columnIndex) {
                case 0:
                    value = iReportGroup.getGroup1();
                    break;

                case 1:
                    value = iReportGroup.getGroup1Description();
                    break;

                case 2:
                    value = iReportGroup.getGroup2();
                    break;

                case 3:
                    value = iReportGroup.getGroup2Description();
                    break;

                case 4:
                    value = iReportGroup.getSum().setScale(0, RoundingMode.DOWN);
                    break;

                }
                return value;
            }
        };

        iModel.addColumn("group.1.number");
        iModel.addColumn("group.1.description");

        iModel.addColumn("group.2.number");
        iModel.addColumn("group.2.description");
        iModel.addColumn("group.2.sum");

        iModel.setObjects(iReportGroups);

        return iModel;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.report.SSVATReportPrinter");
        sb.append("{iAccountingYear=").append(iAccountingYear);
        sb.append(", iDateFrom=").append(iDateFrom);
        sb.append(", iDateTo=").append(iDateTo);
        sb.append('}');
        return sb.toString();
    }
}
