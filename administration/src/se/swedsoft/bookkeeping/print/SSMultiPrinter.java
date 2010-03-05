package se.swedsoft.bookkeeping.print;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperReport;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Date: 2006-mar-02
 * Time: 14:27:30
 */
public class SSMultiPrinter extends SSPrinter {

    private class SSSubReport{

        private String iName;

        private JasperReport iReport;

        private JRDataSource iDataSource;

        private Map<String, Object> iParameters;

        private ResourceBundle iBundle;

    }

    private List<SSSubReport> iSubReports;



    /**
     *
     */
    public SSMultiPrinter(  ){
        super();
        iSubReports  = new LinkedList<SSSubReport>();

        setMargins(0,0,0,0);
        setDetail ("multireport.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
       if(!iSubReports.isEmpty()){
           SSSubReport iSubReport  = iSubReports.get(0);

           return iSubReport.iName;
       }

        return "";
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {

        SSDefaultTableModel <SSSubReport>iModel = new SSDefaultTableModel<SSSubReport>() {
            @Override
            public Class getType() {
                return String.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {

                SSSubReport iSubReport = getObject(rowIndex);

                Object value = null;

                switch (columnIndex) {
                    case 0: // raport.name
                        value = iSubReport.iName;
                        break;
                    case 1: // raport.report
                        value = iSubReport.iReport;
                        break;
                    case 2: // raport.datasource
                        value = iSubReport.iDataSource;
                        break;
                    case 3: // raport.parameters
                        value = iSubReport.iParameters;
                        break;
                    case 4: // raport.bundle
                        value = iSubReport.iBundle;
                        break;

                }
                return value;
            }
        };
        iModel.addColumn("report.name");

        iModel.addColumn("report.report");
        iModel.addColumn("report.datasource");
        iModel.addColumn("report.parameters");
        iModel.addColumn("report.bundle");

        iModel.setObjects(iSubReports);


        return iModel;
    }

    /**
     *
     * @param pReport
     */
    public void addReport(SSPrinter pReport ){
        SSSubReport iSubReport = new SSSubReport();

        pReport.generateReport();

        iSubReport.iName       = pReport.getTitle();
        iSubReport.iReport     = pReport.getReport();
        iSubReport.iDataSource = new SSDefaultJasperDataSource(pReport.getModel());
        iSubReport.iParameters = pReport.getParameters();
        iSubReport.iBundle     = pReport.getBundle();

        iSubReports.add(iSubReport);
    }

}
