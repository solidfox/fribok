package se.swedsoft.bookkeeping.print.report;


import se.swedsoft.bookkeeping.calc.SSResultCalculator;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSNewResultUnit;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.util.Date;
import java.util.ResourceBundle;


/**
 * Date: 2006-feb-28
 * Time: 11:51:36
 */
public class SSResultUnitResultPrinter extends SSResultPrinter {

    private static ResourceBundle bundle = SSBundle.getBundle();

    SSNewResultUnit iResultUnit;

    /**
     *
     * @param pFrom
     * @param pTo
     * @param pResultUnit
     */
    public SSResultUnitResultPrinter(Date pFrom, Date pTo, SSNewResultUnit pResultUnit) {
        super(pFrom, pTo, false, false);
        iResultUnit = pResultUnit;

        addParameter("periodTitle",
                SSBundle.getBundle().getString("resultreport.resultunitperiod"));
        addParameter("periodText",
                iResultUnit != null
                ? iResultUnit.getName()
                : SSBundle.getBundle().getString("resultreport.resultunitperiod.all"));
    }

    /**
     * @param pYearData The year
     * @param pFrom
     * @param pTo
     * @param pResultUnit
     */
    public SSResultUnitResultPrinter(SSNewAccountingYear pYearData, Date pFrom, Date pTo, SSNewResultUnit pResultUnit) {
        super(pYearData, pFrom, pTo, false, false);
        iResultUnit = pResultUnit;

        addParameter("periodTitle",
                SSBundle.getBundle().getString("resultreport.resultunitperiod"));
        addParameter("periodText",
                iResultUnit != null
                ? iResultUnit.getName()
                : SSBundle.getBundle().getString("resultreport.resultunitperiod.all"));
    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("resultreport.resultunit.title");
    }

    /**
     *
     * @return
     */
    @Override
    protected SSResultCalculator getCalculator() {
        return new SSResultCalculator(iYearData, iDateFrom, iDateTo, null, iResultUnit);
    }

    /**
     * @param iCalculator
     */
    @Override
    protected void getColumns(SSResultCalculator iCalculator) {
        addParameter("column.text.2", bundle.getString("resultreport.column.1")); // Perioden
        addParameter("column.text.3", bundle.getString("resultreport.column.7")); // Hela året

        iColumn1 = null;
        iColumn2 = iCalculator.getResultunitChangePeriod();
        iColumn3 = iCalculator.getResultunitChange();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.report.SSResultUnitResultPrinter");
        sb.append("{iResultUnit=").append(iResultUnit);
        sb.append('}');
        return sb.toString();
    }
}
