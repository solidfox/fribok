package se.swedsoft.bookkeeping.gui.voucher.util;

import se.swedsoft.bookkeeping.data.SSVoucherRow;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.SSNewResultUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.model.SSEditableTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.voucher.panel.SSVoucherPanel;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.awt.*;

/**
 * User: Andreas Lago
 * Date: 2006-nov-10
 * Time: 11:45:58
 */
public class SSVoucherRowTableModel extends SSEditableTableModel<SSVoucherRow> {


    private int iReadOnlyCount = 0;


    /**
     * Default constructor.
     */
    public SSVoucherRowTableModel() {
        super();
    }

    /**
     *
     * @return
     */
    public SSVoucherRow newObject() {

        if (iReadOnlyCount > 0 && getEditObject() != null) {
            getEditObject().setAdded     (null);
            getEditObject().setEditedDate(new Date());
        }
        return new SSVoucherRow();
    }


    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSVoucherRow.class;
    }

    /**
     * Sets the objects to operate on for this table model.
     *
     * @param iObjects The objects to display.
     */
    public void setObjects(List<SSVoucherRow> iObjects, boolean iEditing ) {
        iReadOnlyCount = iEditing ? iObjects.size() :  0;

        super.setObjects(iObjects);
    }


    /**
     *
     * @param iSelected
     * @return If the row is deletable
     */
    public boolean allowDeletion(SSVoucherRow iSelected){
        return indexOf(iSelected) >= iReadOnlyCount;
    }

    /**
     *
     * @param iSelected
     * @return If the row is deletable
     */
    public boolean allowMarking(SSVoucherRow iSelected){
        int index = indexOf(iSelected);

        return index >= 0 && index < iReadOnlyCount;
    }



    /**
     * @param iTable
     */
    public void setupTable(SSTable iTable, boolean iUsePainter) {
        super.setupTable(iTable);

        // Add the code to be able to paint the crossed and added rows.
        if(iUsePainter) iTable.setCustomPainter( new SSVoucherRowPainter());
    }

    /**
     * Account column
     */
    public static SSTableColumn<SSVoucherRow> COLUMN_ACCOUNT = new SSTableColumn<SSVoucherRow>(SSBundle.getBundle().getString("voucherrowtable.column.1")) {
        public Object getValue(SSVoucherRow iVoucherRow) {
            SSAccount iAccount = iVoucherRow.getAccount(SSDB.getInstance().getAccounts());

            return iAccount != null ? iAccount : iVoucherRow.getAccountNr();
        }

        public void setValue(SSVoucherRow iVoucherRow, Object iValue) {
            if(iValue instanceof SSAccount){
                SSVoucherPanel.iAccountChanged = true;
                iVoucherRow.setAccount((SSAccount)iValue);
            }
        }

        public Class getColumnClass() {
            return SSAccount.class;
        }

        public int getDefaultWidth() {
            return 60;
        }

        public boolean isEditable(int iRow) {
            return iRow >= ((SSVoucherRowTableModel)getModel()).iReadOnlyCount;
        }
    };



    /**
     * Account column
     */
    public static SSTableColumn<SSVoucherRow> COLUMN_DESCRIPTION = new SSTableColumn<SSVoucherRow>(SSBundle.getBundle().getString("voucherrowtable.column.2")) {
        public Object getValue(SSVoucherRow iVoucherRow) {
            SSAccount iAccount = iVoucherRow.getAccount(SSDB.getInstance().getAccounts());

            return  iAccount != null ? iAccount.getDescription() : null;
        }

        public void setValue(SSVoucherRow iVoucherRow, Object iValue) {
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
     * Debet column
     */
    public static SSTableColumn<SSVoucherRow> COLUMN_DEBET = new SSTableColumn<SSVoucherRow>(SSBundle.getBundle().getString("voucherrowtable.column.3")) {
        public Object getValue(SSVoucherRow iVoucherRow) {
            return iVoucherRow.getDebet();
        }

        public void setValue(SSVoucherRow iVoucherRow, Object iValue) {
            SSVoucherPanel.iDebetChanged = true;
            iVoucherRow.setDebet ((BigDecimal)iValue);
            iVoucherRow.setCredit(null);
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 85;
        }

        public boolean isEditable(int iRow) {
            return iRow >= ((SSVoucherRowTableModel)getModel()).iReadOnlyCount;
        }


    };


    /**
     * Credit column
     */
    public static SSTableColumn<SSVoucherRow> COLUMN_CREDIT = new SSTableColumn<SSVoucherRow>(SSBundle.getBundle().getString("voucherrowtable.column.4")) {
        public Object getValue(SSVoucherRow iVoucherRow) {
            return iVoucherRow.getCredit();
        }

        public void setValue(SSVoucherRow iVoucherRow, Object iValue) {
            SSVoucherPanel.iCreditChanged = true;
            iVoucherRow.setDebet (null);
            iVoucherRow.setCredit((BigDecimal)iValue);
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 85;
        }

        public boolean isEditable(int iRow) {
            return iRow >= ((SSVoucherRowTableModel)getModel()).iReadOnlyCount;
        }
    };

    /**
     * Project column
     */
    public static SSTableColumn<SSVoucherRow> COLUMN_PROJECT = new SSTableColumn<SSVoucherRow>(SSBundle.getBundle().getString("voucherrowtable.column.5")) {
        public Object getValue(SSVoucherRow iVoucherRow) {
            return iVoucherRow.getProject();
        }

        public void setValue(SSVoucherRow iVoucherRow, Object iValue) {
            iVoucherRow.setProject((SSNewProject)iValue);
        }

        public Class getColumnClass() {
            return SSNewProject.class;
        }

        public int getDefaultWidth() {
            return 80;
        }

        public boolean isEditable(int iRow) {
            return iRow >= ((SSVoucherRowTableModel)getModel()).iReadOnlyCount;
        }
    };

    /**
     * Result unit column
     */
    public static SSTableColumn<SSVoucherRow> COLUMN_RESULTUNIT = new SSTableColumn<SSVoucherRow>(SSBundle.getBundle().getString("voucherrowtable.column.6")) {
        public Object getValue(SSVoucherRow iVoucherRow) {
            return  iVoucherRow.getResultUnit();
        }

        public void setValue(SSVoucherRow iVoucherRow, Object iValue) {
            iVoucherRow.setResultUnit((SSNewResultUnit)iValue);
        }

        public Class getColumnClass() {
            return SSNewResultUnit.class;
        }

        public int getDefaultWidth() {
            return 80;
        }

        public boolean isEditable(int iRow) {
            return iRow >= ((SSVoucherRowTableModel)getModel()).iReadOnlyCount;
        }
    };


    /**
     * Edited date
     */
    public static SSTableColumn<SSVoucherRow> COLUMN_EDITED_DATE = new SSTableColumn<SSVoucherRow>(SSBundle.getBundle().getString("voucherrowtable.column.7")) {
        public Object getValue(SSVoucherRow iVoucherRow) {
            return  iVoucherRow.getEditedDate();
        }

        public void setValue(SSVoucherRow iVoucherRow, Object iValue) {
        }

        public Class getColumnClass() {
            return Date.class;
        }

        public int getDefaultWidth() {
            return 75;
        }

        public boolean isEditable(int iRow) {
            return false;
        }
    };

    /**
     * Edited signature
     */
    public static SSTableColumn<SSVoucherRow> COLUMN_EDITED_SIGNATURE = new SSTableColumn<SSVoucherRow>(SSBundle.getBundle().getString("voucherrowtable.column.8")) {
        public Object getValue(SSVoucherRow iVoucherRow) {
            return  iVoucherRow.getEditedSignature();
        }

        public void setValue(SSVoucherRow iVoucherRow, Object iValue) {
            iVoucherRow.setEditedSignature((String)iValue);
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 75;
        }

        public boolean isEditable(int iRow) {
            return true;
        }
    };

    //public static Color COLOR_CROSSED          = new Color(255,192,192);
    //public static Color COLOR_CROSSED_SELECTED = new Color(234,171,171);

    // public static Color COLOR_ADDED           = new Color(192,192,255);
    //  public static Color COLOR_ADDED_SELECTED  = new Color(171,171,234);

    public static Color COLOR_CROSSED          = new Color(255,211,211);
    public static Color COLOR_CROSSED_SELECTED = new Color(255,192,192);

    public static Color COLOR_ADDED           = new Color(211,211,255);
    public static Color COLOR_ADDED_SELECTED  = new Color(192,192,255);

    /**
     *
     */
    private class SSVoucherRowPainter implements SSTable.SSCustomPainter{
        public void update(JTable iTable, Component c, int row, int col, boolean selected, boolean editable) {
            SSVoucherRow iRow = getObject(row);

            if (iRow.isCrossed()) {
                if(selected){
                    c.setBackground( COLOR_CROSSED_SELECTED );
                } else {
                    c.setBackground( COLOR_CROSSED );
                }
            } else
            if (iRow.isAdded()) {
                if(selected){
                    c.setBackground( COLOR_ADDED_SELECTED );
                } else {
                    c.setBackground( COLOR_ADDED );
                }
            }
        }
    }


}
