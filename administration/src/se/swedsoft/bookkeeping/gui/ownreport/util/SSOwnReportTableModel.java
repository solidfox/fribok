package se.swedsoft.bookkeeping.gui.ownreport.util;

import se.swedsoft.bookkeeping.data.SSOwnReport;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSOwnReportTableModel extends SSTableModel<SSOwnReport> {

    /**
     * Default constructor.
     */
    public SSOwnReportTableModel() {
        this( SSDB.getInstance().getOwnReports() );

    }

    /**
     * Constructor.
     *
     * @param pObjects The data for the table model.
     */
    public SSOwnReportTableModel(List<SSOwnReport> pObjects) {
        super(pObjects);
    }

    /**
     * Returns the type of data in this model. <P>
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSOwnReport.class;
    }

    public static SSTableColumn<SSOwnReport> COLUMN_NAME = new SSTableColumn<SSOwnReport>(SSBundle.getBundle().getString("ownreporttable.column.1")) {
        @Override
        public Object getValue(SSOwnReport iOwnReport) {
            return iOwnReport.getName();
        }

        @Override
        public void setValue(SSOwnReport iOwnReport, Object iValue) {
            iOwnReport.setName((String)iValue);
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 300;
        }
    };

    public static SSTableColumn<SSOwnReport> COLUMN_PROJECT = new SSTableColumn<SSOwnReport>(SSBundle.getBundle().getString("ownreporttable.column.2")) {
        @Override
        public Object getValue(SSOwnReport iOwnReport) {
            return iOwnReport.getProjectNr();
        }

        @Override
        public void setValue(SSOwnReport iOwnReport, Object iValue) {
            iOwnReport.setProjectNr((String)iValue);
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 110;
        }
    };

    public static SSTableColumn<SSOwnReport> COLUMN_RESULTUNIT = new SSTableColumn<SSOwnReport>(SSBundle.getBundle().getString("ownreporttable.column.3")) {
        @Override
        public Object getValue(SSOwnReport iOwnReport) {
            return iOwnReport.getResultUnitNr();
        }

        @Override
        public void setValue(SSOwnReport iOwnReport, Object iValue) {
            iOwnReport.setResultUnitNr((String)iValue);
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 110;
        }
    };

}
