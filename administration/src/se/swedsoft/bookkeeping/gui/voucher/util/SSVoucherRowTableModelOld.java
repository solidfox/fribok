package se.swedsoft.bookkeeping.gui.voucher.util;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.editors.*;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.data.*;

import javax.swing.*;
import java.util.Date;
import java.util.LinkedList;
import java.math.BigDecimal;
import java.awt.*;

/**
 * Date: 2006-feb-06
 * Time: 10:34:44
 */

//@Deprecated
public class SSVoucherRowTableModelOld extends SSDefaultTableModel<SSVoucherRow> {

    public static final int COL_ACCOUNT             = 0;
    public static final int COL_DESCRIPTION         = 1;
    public static final int COL_DEBET               = 2;
    public static final int COL_CREDIT              = 3;
    public static final int COL_PROJECT             = 4;
    public static final int COL_RESULTUNIT          = 5;
    public static final int COL_EDITED_DATE         = 6;
    public static final int COL_EDITED_SIGNATURE    = 7;



    // toggles if were are editing a existing voucher
    private boolean iEdit;

    // Is the model read-only
    private boolean iReadOnly;
    // The number of read-only rows in editing mode
    private int     iReadOnlyRowCount;
    // The current row
    private SSVoucherRow iEditingRow;



    /**
     * Default constructor.
     */
    public SSVoucherRowTableModelOld(boolean pEdit, boolean pReadOnly){
        super();
        iEdit                   = pEdit;
        iReadOnly               = pReadOnly;
        iReadOnlyRowCount       = 0;
        iEditingRow             = new SSVoucherRow();

        addColumn(SSBundle.getBundle().getString("voucherrowtable.column.1"));
        addColumn(SSBundle.getBundle().getString("voucherrowtable.column.2"));
        addColumn(SSBundle.getBundle().getString("voucherrowtable.column.3"));
        addColumn(SSBundle.getBundle().getString("voucherrowtable.column.4"));
        addColumn(SSBundle.getBundle().getString("voucherrowtable.column.5"));
        addColumn(SSBundle.getBundle().getString("voucherrowtable.column.6"));

        if(pEdit){
            addColumn(SSBundle.getBundle().getString("voucherrowtable.column.7"));
            addColumn(SSBundle.getBundle().getString("voucherrowtable.column.8"));
        }
        setObjects( new LinkedList<SSVoucherRow>() );
    }


    /**
     *
     * @param iVoucher
     */
    public void setVoucher(SSVoucher iVoucher){

        setObjects( iVoucher.getRows() );

        iReadOnlyRowCount = super.getRowCount();

        fireTableDataChanged();
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
    @Override
    public int getRowCount() {
        if(iReadOnly){
            return super.getRowCount();
        } else {
            return super.getRowCount() + 1;
        }
    }

    /**
     * Returns the object at the given row index.
     *
     * @param row The row to get the object from.
     * @return An Object.
     */
    @Override
    public SSVoucherRow getObject(int row) {
        if( row >= super.getRowCount() ){
            return iEditingRow;
        } else {
            return super.getObject(row);
        }
    }

    /**
     * Returns false.  This is the default implementation for all cells.
     *
     * @param rowIndex    the row being queried
     * @param columnIndex the column being queried
     *
     * @return false
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        SSVoucherRow iRow = getObject(rowIndex);

        if(iReadOnly) return false;

        // We can never edit the description or editdate
        if( columnIndex == COL_DESCRIPTION || columnIndex == COL_EDITED_DATE) return false;

        // If editing a voucher we can only change data for newly added rows
        if (iEdit && (rowIndex < iReadOnlyRowCount) ) return false;

        // We can always edit the accout field
        if( columnIndex == COL_ACCOUNT) return true;

        if( iRow != null){
            // We can edit all rows if the account is set
            if( iRow.getAccount() != null )  return true;
        }

        return false;
    }

    /**
     * Returns the type of data in this model. <P>
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSVoucherRow.class;
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * voucherrowtable.column.1=Konto
     * voucherrowtable.column.2=Benämning
     * voucherrowtable.column.3=Debet
     * voucherrowtable.column.4=Kredit
     * voucherrowtable.column.5=Projekt
     * voucherrowtable.column.6=Resultatenhet
     * voucherrowtable.column.7=Ändrad
     * voucherrowtable.column.8=Signatur

     *
     *
     * @param    rowIndex    the row whose value is to be queried
     * @param    columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        SSVoucherRow iVoucherRow = getObject(rowIndex);
        SSAccount    iAccount    = iVoucherRow.getAccount();

        Object value = null;

        switch (columnIndex) {
            case COL_ACCOUNT:
                value = iAccount;
                break;
            case COL_DESCRIPTION:
                value = iAccount != null ? iAccount.getDescription() : null;
                break;
            case COL_DEBET:
                value = iVoucherRow.getDebet();
                break;
            case COL_CREDIT:
                value = iVoucherRow.getCredit();
                break;
            case COL_PROJECT:
                value = iVoucherRow.getProject();
                break;
            case COL_RESULTUNIT:
                value = iVoucherRow.getResultUnit();
                break;
            case COL_EDITED_DATE:
                value = iVoucherRow.getEditedDate();
                break;
            case COL_EDITED_SIGNATURE:
                value = iVoucherRow.getEditedSignature();
                break;
        }

        return value;
    }


    /**
     * This empty implementation is provided so users don't have to implement
     * this method if their data model is not editable.
     *
     * @param aValue      value to assign to cell
     * @param rowIndex    row of cell
     * @param columnIndex column of cell
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        SSVoucherRow iVoucherRow = getObject(rowIndex);

        switch (columnIndex) {
            case COL_ACCOUNT:
                if(aValue instanceof SSAccount) iVoucherRow.setAccount((SSAccount)aValue);


                /*
           if (iEdit && iVoucherRow != iEditingRow) {
               iVoucherRow.setAdded     (null);
               iVoucherRow.setEditedDate(new Date());
           }     */
                break;
            case COL_DEBET:
                iVoucherRow.setDebet ((BigDecimal)aValue);
                iVoucherRow.setCredit(null);
                fireTableCellUpdated(rowIndex, COL_CREDIT);
                break;
            case COL_CREDIT:
                iVoucherRow.setCredit((BigDecimal)aValue);
                iVoucherRow.setDebet (null);
                fireTableCellUpdated(rowIndex, COL_DEBET);
                break;
            case COL_PROJECT:
                iVoucherRow.setProject((SSNewProject)aValue);
                break;
            case COL_RESULTUNIT:
                iVoucherRow.setResultUnit((SSNewResultUnit)aValue);
                break;
            case COL_EDITED_SIGNATURE:
                iVoucherRow.setEditedSignature((String)aValue);
                break;
        }

        fireTableCellUpdated(rowIndex, columnIndex);

        if( (iVoucherRow == iEditingRow) && (aValue != null) && (!"".equals(aValue)) ) {

            if (iEdit) {
                iEditingRow.setAdded     (null);
                iEditingRow.setEditedDate(new Date());
            }
            add(iEditingRow);

            iEditingRow = new SSVoucherRow();

            fireTableDataChanged();
        }
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    @Override
    public int getColumnCount() {
        int count = super.getColumnCount();

        return (iEdit) ? count : Math.min(count, 6 );
    }

    /**
     * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     * @param columnIndex the column being queried
     *
     * @return the Object.class
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {

        switch (columnIndex) {
            case COL_ACCOUNT:
                return SSAccount.class;
            case COL_DESCRIPTION:
                return String.class;
            case COL_DEBET:
                return BigDecimal.class;
            case COL_CREDIT:
                return BigDecimal.class;
            case COL_PROJECT:
                return SSNewProject.class;
            case COL_RESULTUNIT:
                return SSNewResultUnit.class;
            case COL_EDITED_DATE:
                return Date.class;
            case COL_EDITED_SIGNATURE:
                return String.class;
        }

        return super.getColumnClass(columnIndex);
    }

    /**
     *
     * @param iSelected
     */
    @Override
    public void deleteRow(SSVoucherRow iSelected){
        // Dont do anything here
        if(iReadOnly) return;
        //   super.delete();

        // If we wan't to delete the editing row, just skip it
        if(iSelected == iEditingRow ){
            iEditingRow = new SSVoucherRow();
        }
        // We can't delete the first rows in edit mode
        if(iEdit &&  indexOf(iSelected) < iReadOnlyRowCount  ) return;

        // Delete the row from the model
        super.delete(iSelected);
    }


    /**
     *
     * @param iSelected
     * @return If the row is deletable
     */
    public boolean canDeleteRow(SSVoucherRow iSelected){
        return !iReadOnly && !(iEdit &&  indexOf(iSelected) < iReadOnlyRowCount  ) || (iSelected == iEditingRow );
    }






    public static Color COLOR_CROSSED = new Color(255,192,192);
    public static Color COLOR_ADDED   = new Color(192,192,255);

    /**
     *
     * @param iTable
     */
    public static void setupTable(SSTable iTable, final SSDefaultTableModel<SSVoucherRow> iModel) {
        //
        iTable.setColumnSortingEnabled(false);
        // Disallow the reordering of the table headers.
        iTable.getTableHeader().setReorderingAllowed(false);

        // Add the code to be able to paint the crossed and added rows.
        iTable.setCustomPainter( new SSTable.SSCustomPainter() {
            public void update(JTable iTable, Component c, int row, int col, boolean selected, boolean editable) {
                SSVoucherRow iRow = iModel.getObject(row);

                if (iRow.isCrossed()) {
                    c.setBackground( COLOR_CROSSED );
                } else
                if (iRow.isAdded()) {
                    c.setBackground( COLOR_ADDED );
                } else {

                    if(selected){
                        c.setBackground( iTable.getSelectionBackground() );
                    } else {
                        c.setBackground( iTable.getBackground() );
                    }


                }
            }
        });

        iTable.setDefaultRenderer(SSAccount.class, new SSAccountCellRenderer() );
        iTable.setDefaultEditor  (SSAccount.class, new SSAccountCellEditor()  );

        iTable.setDefaultRenderer(SSNewProject.class, new SSProjectCellRenderer());
        iTable.setDefaultEditor  (SSNewProject.class, new SSProjectCellEditor() );

        iTable.setDefaultRenderer(SSNewResultUnit.class, new SSResultUnitCellRenderer());
        iTable.setDefaultEditor  (SSNewResultUnit.class, new SSResultUnitCellEditor() );

        // Set the default renderer for the date cells.
        iTable.setDefaultRenderer(Date.class      , new SSDateCellRenderer());

        iTable.setDefaultRenderer(BigDecimal.class, new SSBigDecimalCellRenderer(2) );
        iTable.setDefaultEditor  (BigDecimal.class, new SSBigDecimalCellEditor  (2));

        try{
            iTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_ACCOUNT     ).setPreferredWidth(57);
            iTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_DESCRIPTION ).setPreferredWidth(240);
            iTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_DEBET       ).setPreferredWidth(85);
            iTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_CREDIT      ).setPreferredWidth(85);
            iTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_PROJECT     ).setPreferredWidth(85);
            iTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_RESULTUNIT  ).setPreferredWidth(85);

            if( iTable.getColumnModel().getColumnCount() == 8 ){
                iTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_EDITED_DATE     ).setPreferredWidth(70);
                iTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_EDITED_SIGNATURE).setPreferredWidth(65);
            } else {
                iTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_DESCRIPTION ).setPreferredWidth(376);
            }
        } catch(Exception ignored){

        }
    }

}


