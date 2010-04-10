package se.swedsoft.bookkeeping.print.report;


import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;

import java.text.DateFormat;
import java.util.List;


/**
 * Date: 2006-mar-01
 * Time: 10:50:45
 */
public class SSProjectsPrinter extends SSPrinter {

    List<SSNewProject> iProjects;

    /**
     *
     */
    public SSProjectsPrinter() {
        this(SSDB.getInstance().getProjects());
    }

    /**
     *
     * @param pProjects The accountplan
     */
    public SSProjectsPrinter(List<SSNewProject> pProjects) {
        iProjects = pProjects;

        setPageHeader("header.jrxml");
        setColumnHeader("projects.jrxml");
        setDetail("projects.jrxml");
    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("projectreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {

        final DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        SSDefaultTableModel<SSNewProject> iModel = new SSDefaultTableModel<SSNewProject>() {

            @Override
            public Class getType() {
                return SSNewProject.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                SSNewProject iProject = getObject(rowIndex);
                Object value = null;

                switch (columnIndex) {
                case 0:
                    value = iProject.getNumber();
                    break;

                case 1:
                    value = iProject.getName();
                    break;

                case 2:
                    value = iProject.getDescription();
                    break;

                case 3:
                    value = iProject.getConcluded()
                            ? iFormat.format(iProject.getConcludedDate())
                            : null;
                    break;
                }

                return value;
            }
        };

        iModel.addColumn("project.number");
        iModel.addColumn("project.name");
        iModel.addColumn("project.description");
        iModel.addColumn("project.concluded");

        iModel.setObjects(iProjects);

        return iModel;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.report.SSProjectsPrinter");
        sb.append("{iProjects=").append(iProjects);
        sb.append('}');
        return sb.toString();
    }
}
