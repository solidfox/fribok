package se.swedsoft.bookkeeping.gui.autodist.util;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import java.math.BigDecimal;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSAutoDistRowTableModel extends SSTableModel<SSAutoDistRow> {


    private SSAutoDistRow iEditing;


    /**
     * Default constructor.
     */
    public SSAutoDistRowTableModel() {
        iEditing = new SSAutoDistRow();
    }


    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSAutoDistRow.class;
    }


    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    public int getRowCount() {
        return super.getRowCount() + 1;
    }


    /**
     * Returns the object at the given row index.
     *
     * @param row The row to get the object from.
     * @return An Object.
     */
    public SSAutoDistRow getObject(int row) {
        if( row == super.getRowCount()){
            return iEditing;
        } else {
            return super.getObject(row);
        }
    }


    /**
     * This empty implementation is provided so users don't have to implement
     * this method if their data model is not editable.
     *
     * @param aValue      value to assign to cell
     * @param rowIndex    row of cell
     * @param columnIndex column of cell
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);

        if(getObject(rowIndex) == iEditing && aValue != null && !"".equals(aValue)){
            add(iEditing);

            iEditing = new SSAutoDistRow();
        }

    }

    /**
     * Account column
     */
    public static SSTableColumn<SSAutoDistRow> COLUMN_ACCOUNT = new SSTableColumn<SSAutoDistRow>(SSBundle.getBundle().getString("autodistrowtable.column.1")) {
        public Object getValue(SSAutoDistRow iAutoDistRow) {
            SSAccount iAccount = iAutoDistRow.getAccount(SSDB.getInstance().getAccounts());

            return iAccount != null ? iAccount : iAutoDistRow.getAccountNr();
        }

        public void setValue(SSAutoDistRow iAutoDistRow, Object iValue) {
            if(iValue instanceof SSAccount){
                iAutoDistRow.setAccount((SSAccount)iValue);
            }
        }

        public Class getColumnClass() {
            return SSAccount.class;
        }

        public int getDefaultWidth() {
            return 60;
        }

        public boolean isEditable(int iRow) {
            return true;
        }
    };



    /**
     * Account column
     */
    public static SSTableColumn<SSAutoDistRow> COLUMN_DESCRIPTION = new SSTableColumn<SSAutoDistRow>(SSBundle.getBundle().getString("autodistrowtable.column.2")) {
        public Object getValue(SSAutoDistRow iAutoDistRow) {
            SSAccount iAccount = iAutoDistRow.getAccount(SSDB.getInstance().getAccounts());

            return  iAccount != null ? iAccount.getDescription() : null;
        }

        public void setValue(SSAutoDistRow iAutoDistRow, Object iValue) {
            // Read only
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 260;
        }

        public boolean isEditable(int iRow) {
            return false;
        }

    };

    /**
     * % column
     */
    public static SSTableColumn<SSAutoDistRow> COLUMN_PERCENTAGE = new SSTableColumn<SSAutoDistRow>(SSBundle.getBundle().getString("autodistrowtable.column.3")) {
        public Object getValue(SSAutoDistRow iAutoDistRow) {
            return  iAutoDistRow.getPercentage();
        }

        public void setValue(SSAutoDistRow iAutoDistRow, Object iValue) {
            iAutoDistRow.setPercentage((BigDecimal)iValue);
            iAutoDistRow.setDebet(null);
            iAutoDistRow.setCredit(null);
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 85;
        }

        public boolean isEditable(int iRow) {
            return true;
        }

    };

    /**
     * Debet column
     */
    public static SSTableColumn<SSAutoDistRow> COLUMN_DEBET = new SSTableColumn<SSAutoDistRow>(SSBundle.getBundle().getString("autodistrowtable.column.4")) {
        public Object getValue(SSAutoDistRow iAutoDistRow) {
            return iAutoDistRow.getDebet();
        }

        public void setValue(SSAutoDistRow iAutoDistRow, Object iValue) {
            iAutoDistRow.setDebet ((BigDecimal)iValue);
            iAutoDistRow.setCredit(null);
            iAutoDistRow.setPercentage(null);
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 85;
        }

        public boolean isEditable(int iRow) {
            return true;
        }
    };


    /**
     * Credit column
     */
    public static SSTableColumn<SSAutoDistRow> COLUMN_CREDIT = new SSTableColumn<SSAutoDistRow>(SSBundle.getBundle().getString("autodistrowtable.column.5")) {
        public Object getValue(SSAutoDistRow iAutoDistRow) {
            return iAutoDistRow.getCredit();
        }

        public void setValue(SSAutoDistRow iAutoDistRow, Object iValue) {
            iAutoDistRow.setCredit((BigDecimal)iValue);
            iAutoDistRow.setDebet(null);
            iAutoDistRow.setPercentage(null);
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 85;
        }

        public boolean isEditable(int iRow) {
            return true;
        }
    };

    /**
     * Project column
     */
    public static SSTableColumn<SSAutoDistRow> COLUMN_PROJECT = new SSTableColumn<SSAutoDistRow>(SSBundle.getBundle().getString("autodistrowtable.column.6")) {
        public Object getValue(SSAutoDistRow iAutoDistRow) {
            return iAutoDistRow.getProject();
        }

        public void setValue(SSAutoDistRow iAutoDistRow, Object iValue) {
            if(iValue instanceof SSNewProject){
                iAutoDistRow.setProject((SSNewProject)iValue);
            }
        }

        public Class getColumnClass() {
            return SSNewProject.class;
        }

        public int getDefaultWidth() {
            return 80;
        }

        public boolean isEditable(int iRow) {
            return true;
        }
    };

    /**
     * Result unit column
     */
    public static SSTableColumn<SSAutoDistRow> COLUMN_RESULTUNIT = new SSTableColumn<SSAutoDistRow>(SSBundle.getBundle().getString("autodistrowtable.column.7")) {
        public Object getValue(SSAutoDistRow iAutoDistRow) {
            return  iAutoDistRow.getResultUnit();
        }

        public void setValue(SSAutoDistRow iAutoDistRow, Object iValue) {
            if(iValue instanceof SSNewResultUnit){
                iAutoDistRow.setResultUnit((SSNewResultUnit)iValue);
            }
        }

        public Class getColumnClass() {
            return SSNewResultUnit.class;
        }

        public int getDefaultWidth() {
            return 80;
        }

        public boolean isEditable(int iRow) {
            return true;
        }
    };
}


