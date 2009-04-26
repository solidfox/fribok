package se.swedsoft.bookkeeping.gui.backup.util;

import se.swedsoft.bookkeeping.data.backup.SSBackup;
import se.swedsoft.bookkeeping.data.backup.SSBackupDatabase;
import se.swedsoft.bookkeeping.data.backup.util.SSBackupType;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSDateTimeCellRenderer;

import javax.swing.table.TableCellRenderer;
import java.util.Date;
import java.util.List;

/**
 * Date: 2006-mar-03
 * Time: 10:36:35
 */
public class SSBackupTableModel extends SSTableModel<SSBackup> {


    /**
     * Default constructor.
     */
    public SSBackupTableModel(List<SSBackup> iBackups) {
        super( iBackups );
    }


    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSBackup.class;
    }




    /**
     *  Date
     */
    public static SSTableColumn<SSBackup> COLUMN_DATE= new SSTableColumn<SSBackup>(SSBundle.getBundle().getString("backuptable.column.1")) {
        public Object getValue(SSBackup iBackup) {
            return iBackup.getDate();
        }

        public void setValue(SSBackup iBackup, Object iValue) {
        }

        public Class getColumnClass() {
            return Date.class;
        }

        public int getDefaultWidth() {
            return 120;
        }

        /**
         * @return
         */
        public TableCellRenderer getCellRenderer() {
            return new SSDateTimeCellRenderer();
        }

    };



    /**
     *  Date
     */
    public static SSTableColumn<SSBackup> COLUMN_FILENAME = new SSTableColumn<SSBackup>(SSBundle.getBundle().getString("backuptable.column.2")) {
        public Object getValue(SSBackup iBackup) {
            return iBackup.getFilename();
        }

        public void setValue(SSBackup iBackup, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 240;
        }
    };


    /**
     *  Type
     */
    public static SSTableColumn<SSBackup> COLUMN_TYPE = new SSTableColumn<SSBackup>(SSBundle.getBundle().getString("backuptable.column.3")) {
        public Object getValue(SSBackup iBackup) {
            if( iBackup.getType() == SSBackupType.COMPANY  ){
                return null;
            }   else {
                return SSBundle.getBundle().getString("backupframe.allcompanies");
            }
        }

        public void setValue(SSBackup iBackup, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 240;
        }
    };


}
