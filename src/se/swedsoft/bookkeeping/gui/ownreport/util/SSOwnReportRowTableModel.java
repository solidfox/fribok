package se.swedsoft.bookkeeping.gui.ownreport.util;

import se.swedsoft.bookkeeping.data.SSOwnReportRow;
import se.swedsoft.bookkeeping.data.common.SSHeadingType;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSHeadingTypeCellEditor;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSHeadingTypeCellRenderer;
import se.swedsoft.bookkeeping.gui.util.table.model.SSEditableTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * User: Johan Gunnarsson
 * Date: 2007-nov-26
 * Time: 10:35:14
 */
public class SSOwnReportRowTableModel extends SSEditableTableModel<SSOwnReportRow> {

    /**
     *
     * @return
     */
    @Override
    public SSOwnReportRow newObject() {
        return  new SSOwnReportRow();
    }

    public static SSTableModel<SSOwnReportRow> getDropDownModel(){
        SSOwnReportRowTableModel iModel = new SSOwnReportRowTableModel();
        iModel.addColumn( COLUMN_HEADING, false   );
        return iModel;
    }
    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSOwnReportRow.class;
    }

    public static SSTableColumn<SSOwnReportRow> COLUMN_TYPE = new SSTableColumn<SSOwnReportRow>(SSBundle.getBundle().getString("ownreport.headingtable.column.1")) {
        @Override
        public Object getValue(SSOwnReportRow iObject) {
            return iObject.getType();
        }

        @Override
        public void setValue(SSOwnReportRow iObject, Object iValue) {
            if(iValue instanceof SSHeadingType){
                iObject.setType((SSHeadingType)iValue);
            }
        }

        @Override
        public Class getColumnClass() {
            return SSHeadingType.class;
        }

        @Override
        public int getDefaultWidth() {
            return 75;
        }

        @Override
        public TableCellEditor getCellEditor() {
            return new SSHeadingTypeCellEditor();
        }

        /**
         * @return
         */
        @Override
        public TableCellRenderer getCellRenderer() {
            return new SSHeadingTypeCellRenderer();
        }
    };

    public static SSTableColumn<SSOwnReportRow> COLUMN_HEADING = new SSTableColumn<SSOwnReportRow>(SSBundle.getBundle().getString("ownreport.headingtable.column.2")) {
        @Override
        public Object getValue(SSOwnReportRow iObject) {
            return iObject.getHeading();
        }

        @Override
        public void setValue(SSOwnReportRow iObject, Object iValue) {
            iObject.setHeading((String)iValue);
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
}

