package se.swedsoft.bookkeeping.gui.util.table.model;

import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSTableEditor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.*;

/**
 * User: Andreas Lago
 * Date: 2006-jun-15
 * Time: 10:12:40
 */
public abstract class SSTableModel<T> extends AbstractTableModel {

    // The objects in the table
    private List<T> iObjects;

    // The columns.
    private List<SSTableColumn<T>> iColumns;

    // Editable columns
    private Map<SSTableColumn<T>, Boolean> iEditable;

    //////////////////////////////////////////////////////////////

    /**
     * Default constructor.
     */
    public SSTableModel() {
        iColumns  = new LinkedList<SSTableColumn<T>>();
        iObjects  = new LinkedList<T     >();
        iEditable = new HashMap<SSTableColumn<T>, Boolean>();


    }

    /**
     * Constructor.
     *
     * @param pObjects The data for the table model.
     */
    public SSTableModel(List<T> pObjects) {
        this();
        iObjects = pObjects;
    }

    /**
     * Constructor.
     *
     * @param pObjects The data for the table model.
     */
    public SSTableModel(T ... pObjects) {
        this();

        iObjects.addAll(Arrays.asList(pObjects));
    }

    //////////////////////////////////////////////////////////////

    public SSTableModel<T> getDropdownmodel() {
        return null;
    }

    /**
     * Sets the objects to operate on for this table model.
     *
     * @param pObjects The objects to display.
     */
    public void setObjects(List<T> pObjects) {
        iObjects = pObjects;
        fireTableDataChanged();
    }

    /**
     * Sets the objects to operate on for this table model.
     *
     * @param pObjects The objects to display.
     */
    public void setObjects(T ... pObjects) {
        iObjects = new LinkedList<T>();
        iObjects.addAll(Arrays.asList(pObjects));
        fireTableDataChanged();
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     *
     * @see #getRowCount
     */
    public int getColumnCount() {
        return iColumns.size();
    }

    /**
     * Adds a column to the table model.
     *
     * @param iColumn The column.
     */
    public void addColumn(SSTableColumn<T> iColumn) {
        addColumn(iColumn, false);
    }

    /**
     *
     * @param iColumn
     * @param iEditable
     */
    public void addColumn(SSTableColumn<T> iColumn, boolean iEditable) {
        iColumns.add(iColumn);

        this.iEditable.put(iColumn,  iEditable);
        fireTableDataChanged();
    }

    /**
     *
     * @param iColumn
     */
    public void removeColumn(SSTableColumn<T> iColumn) {
        iColumns.remove(iColumn);

        this.iEditable.remove(iColumn);

        fireTableDataChanged();
    }



    /**
     *
     * @return The columns
     */
    public List<SSTableColumn<T>> getColumns() {
        return iColumns;
    }

    /**
     * Returns a  name for the column
     *
     * @param column the column being queried
     *
     * @return a string containing the default name of <code>column</code>
     */
    @Override
    public String getColumnName(int column) {
        // Inside the bounds ?
        if(column >= 0 && column < iColumns.size() ) {

            SSTableColumn iColumn =  iColumns.get(column);

            return iColumn.getName();
        }
        return super.getColumnName(column);
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param    rowIndex    the row whose value is to be queried
     * @param    columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        SSTableColumn<T> iColumn = iColumns.get(columnIndex);

        iColumn.setModel(this);

        T iObject = getObject(rowIndex);

        return iColumn.getValue(iObject);
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
        SSTableColumn<T> iColumn = iColumns.get(columnIndex);

        T iObject = getObject(rowIndex);

        iColumn.setModel(this);
        iColumn.setValue(iObject, aValue);

        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    /**
     * Returns false.  This is the default implementation for all cells.
     *
     * @param rowIndex    the row being queried
     * @param columnIndex the column being queried
     * @return false
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        SSTableColumn<T> iColumn = iColumns.get(columnIndex);

        iColumn.setModel(this);

        return iEditable.get(iColumn) && iColumn.isEditable(rowIndex);
    }


    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     *
     * @see #getColumnCount
     */
    public int getRowCount() {
        return iObjects.size();
    }

    /**
     * Adds the object to the table model.
     *
     * @param pObject The object to add.
     */
    public void add(T pObject) {
        iObjects.add(pObject);

        fireTableRowsInserted(iObjects.size() - 1, iObjects.size() - 1);
    }

    /**
     * Adds all objects to the table model.
     *
     * @param pObjects The objects to add.
     */
    public void addAll(List<T> pObjects) {
        iObjects.addAll(pObjects);

        fireTableRowsInserted(iObjects.size() - pObjects.size(), iObjects.size());
    }

    /**
     * Returns the object at the given row index.
     *
     * @param row The row to get the object from.
     *
     * @return An Object.
     */
    public T getObject(int row) {
        return iObjects.get(row);
    }

    /**
     * Deletes the object from the table model.
     *
     * @param pObject The object to delete.
     */
    public void delete(T pObject) {
        int index = iObjects.indexOf(pObject);

        if (index >= 0) {
            iObjects.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }

    /**
     * Deletes the object from the table model.
     *
     * @param iObjects The object to delete.
     */
    public void delete(List<T> iObjects) {
        this.iObjects.removeAll(iObjects);

        fireTableDataChanged();
    }



    /**
     * Deletes the data at the given index if the index i within the limits of
     * the underlaying structure.
     *
     * @param index The index of the object to remove.
     */
    public void delete(int index) {
        if (index >= 0 && index < iObjects.size()) {
            iObjects.remove(index);
            fireTableRowsDeleted(index, index);
        }
    }

    /**
     * Deletes the data at the given indices. <P>
     *
     * @param indices The indices for the rows to remove.
     */
    public void delete(int[] indices) {
        if (indices.length > 0) {
            for (int i = indices.length - 1; i >= 0; i--) {
                iObjects.remove(indices[i]);
            }
            fireTableRowsDeleted(indices[0], indices[indices.length - 1]);
        }
    }

    /**
     * Returns the type of data in this model. <P>
     *
     * @return The current data type.
     */
    public abstract Class getType();

    /**
     * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     * @param columnIndex the column being queried
     * @return the Object.class
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        SSTableColumn iColumn = iColumns.get(columnIndex);

        return iColumn.getColumnClass();
    }

    /**
     * Returns the List in the background of this table model.
     *
     * @return The internal list of objects.
     */
    public List<T> getObjects() {
        return iObjects;
    }

    /**
     * Returns a List of objects for the given indices.
     *
     * @param iIndices The indices of the objects to return.
     *
     * @return A List of objects. If the index array is empty, then all objects
     *         in this model is returned.
     */
    public List<T> getObjects(int[] iIndices) {
        List<T> iFiltered;

        if (iIndices.length == 0) {
            iFiltered = getObjects();
        } else {
            iFiltered = new ArrayList<T>(iIndices.length);
            for (int i : iIndices) {
                iFiltered.add(getObject(i));
            }
        }
        return iFiltered;
    }


    /**
     *
     * @param iObject
     */
    public void deleteRow(T iObject) {
        iObjects.remove(iObject);
        fireTableDataChanged();
    }




    /**
     * Returns the index in this list of the first occurrence of the specified
     * element, or -1 if this list does not contain this element.
     * More formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     *
     * @param iObject
     * @return the index
     */
    public int indexOf(T iObject) {
        return iObjects.indexOf(iObject);
    }



    /**
     *
     * @param iTable
     * @return the selected row
     */
    public T getSelectedRow(JTable iTable){
        int selected = iTable.getSelectedRow();

        if (selected >= 0){
            return getObject(selected);
        }
        return null;

    }

    /**
     *
     * @param iTable
     * @return
     */
    public List<T> getSelectedRows(JTable iTable){
        int [] selected = iTable.getSelectedRows();

        if (selected.length > 0){
            return getObjects(selected);
        }
        return null;
    }

    /**
     *
     * @param iTable
     */
    public void setupTable(SSTable iTable) {
        iTable.setModel(this);
        iTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        iTable.tableChanged(null);

        SSTableEditor.setDefaultEditors(iTable);

        int iIndex = 0;
        for (SSTableColumn<T> iColumn : iColumns) {
            int iWidth                  = iColumn.getDefaultWidth();
            TableCellEditor   iEditor   = iColumn.getCellEditor();
            TableCellRenderer iRenderer = iColumn.getCellRenderer();

            iTable.getColumnModel().getColumn(iIndex).setPreferredWidth(iWidth);
            iTable.getColumnModel().getColumn(iIndex).setCellEditor(iEditor);
            iTable.getColumnModel().getColumn(iIndex).setCellRenderer(iRenderer);

            iIndex++;

        }
    }


    /**
     *
     * @param iTable
     */
    public void addDeleteAction(final JTable iTable){

        // The delete action.
        Action iDelete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int col = iTable.getSelectedColumn();
                int row = iTable.getSelectedRow();

                if ( iTable.isEditing() ) {
                    return;
                }
                T iSelected = getSelectedRow(iTable);

                if(iSelected != null) {
                    deleteRow(iSelected );

                    iTable.changeSelection(row-1, col, false, false);
                }
            }
        };
        iTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "DELETE_ROW");
        iTable.getActionMap().put("DELETE_ROW"     , iDelete);
    }

/*    public void dispose()
    {
        TableModelListener[] iTableModelListeners = this.getTableModelListeners();
        for(int i=0;i<iTableModelListeners.length;i++)
        {
            this.removeTableModelListener(iTableModelListeners[i]);
        }
        if(iObjects!=null)
            iObjects.removeAll(iObjects);

        iObjects=null;
        if(iColumns!=null)
            iColumns.removeAll(iColumns);

        iColumns=null;
        if(iEditable!=null)
            iEditable.clear();

        iEditable=null;

    }
*/

}
