package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.calc.SSResultCalculator;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSNewProject;

import java.util.Date;
import java.util.ResourceBundle;

/**
 * Date: 2006-feb-28
 * Time: 11:44:29
 */
public class SSProjectResultPrinter extends SSResultPrinter {

    private static ResourceBundle bundle =  SSBundle.getBundle();

    SSNewProject iProject;

    /**
     *
     */
    public SSProjectResultPrinter(Date pFrom, Date pTo,  SSNewProject pProject) {
        super(pFrom, pTo, false, false);
        iProject = pProject;

        addParameter("periodTitle",                                        SSBundle.getBundle().getString("resultreport.projectperiod")) ;
        addParameter("periodText", iProject != null ? iProject.getName() : SSBundle.getBundle().getString("resultreport.projectperiod.all"));
    }

    /**
     * @param pYearData The year
     */
    public SSProjectResultPrinter(SSNewAccountingYear pYearData, Date pFrom, Date pTo,  SSNewProject pProject) {
        super(pYearData, pFrom, pTo, false, false);
        iProject = pProject;

        addParameter("periodTitle",                                        SSBundle.getBundle().getString("resultreport.projectperiod")) ;
        addParameter("periodText", iProject != null ? iProject.getName() : SSBundle.getBundle().getString("resultreport.projectperiod.all"));
    }


    /**
     *
     * @return
     */
    @Override
    protected SSResultCalculator getCalculator() {
        return new SSResultCalculator(iYearData, iDateFrom, iDateTo, iProject , null );
    }

    /**
     * @param iCalculator
     */
    @Override
    protected void getColumns(SSResultCalculator iCalculator) {
        addParameter("column.text.2", bundle.getString("resultreport.column.1") ); // Perioden
        addParameter("column.text.3", bundle.getString("resultreport.column.7") ); // Alla år

        iColumn1 = null;
        iColumn2 = iCalculator.getProjectChangePeriod();
        iColumn3 = iCalculator.getProjectChange();
    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("resultreport.project.title");
    }


}
