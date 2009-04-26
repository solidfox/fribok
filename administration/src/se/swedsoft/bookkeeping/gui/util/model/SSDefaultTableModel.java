/*
 * @(#)SSDefaultTableModel.java                v 1.0 2005-jul-06
 *
 * Time-stamp: <2005-jul-06 22:17:32 Hasse>
 *
 * Copyright (c) Trade Extensions TradeExt AB, Sweden.
 * www.tradeextensions.com, info@tradeextensions.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Trade Extensions ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Trade Extensions.
 */
package se.swedsoft.bookkeeping.gui.util.model;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSBundleString;

import javax.swing.table.AbstractTableModel;
import javax.swing.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


/**
 * The default table model to use in this application.
 *
 * @author Roger Björnstedt
 */
public abstract class SSDefaultTableModel<T> extends AbstractTableModel {


    /** The objects in the table. */
    private List<T> iObjects;

    /** The columns. */
    private List<String> iColumns;


    /**
     * Default constructor.
     */
    public SSDefaultTableModel() {
        super();
        iColumns = new LinkedList<String>();
        iObjects = new LinkedList<T     >();
    }

    /**
     * Constructor.
     *
     * @param pObjects The data for the table model.
     */
    public SSDefaultTableModel(List<T> pObjects) {
        super();
        iColumns = new LinkedList<String>();
        iObjects = pObjects;
    }

    /**
     * Constructor.
     *
     * @param pObjects The data for the table model.
     */
    public SSDefaultTableModel(T ... pObjects) {
        super();
        iColumns = new LinkedList<String>();
        iObjects = new LinkedList<T     >();

        for(T iObject : pObjects){
            iObjects.add(iObject);
        }
    }

    /**
     * Copy constructor.
     *
     * @param pModel The data for the table model.
     */
    public SSDefaultTableModel(SSDefaultTableModel<T> pModel) {
        super();
        iColumns = new ArrayList<String>(pModel.iColumns);
        iObjects = new ArrayList<T     >(pModel.iObjects);
    }



    /**
     * Returns a List of Strings with the column names. <P>
     *
     * @return A List of String objects.
     */
    public List<String> getColumnNames() {
        return iColumns;
    }

    /**
     * Sets the objects to operate on for this table model. <P>
     *
     * @param pObjects The objects to display.
     */
    public void setObjects(List<T> pObjects) {
        iObjects = pObjects;
        fireTableDataChanged();
    }

    /**
     * Sets the objects to operate on for this table model. <P>
     *
     * @param pObjects The objects to display.
     */
    public void setObjects(T ... pObjects) {
        iObjects = new LinkedList<T>();
        for(T iObject: pObjects){
            iObjects.add(iObject);
        }
    }

    /**
     * Adds a column to the table model.
     *
     * @param iTitle The column title.
     */
    public void addColumn(String iTitle) {
        iColumns.add(iTitle);
    }

    /**
     * Adds a column to the table model.
     *
     * @param iString The column title.
     */
    public void addColumn(SSBundleString iString) {
        iColumns.add( iString.getString() );
    }


    /**
     * Returns a  name for the column
     *
     * @param column the column being queried
     *
     * @return a string containing the default name of <code>column</code>
     */
    public String getColumnName(int column) {
        return column < iColumns.size() ? iColumns.get(column) : super.getColumnName(column);
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
     * Returns if a row is the last row in the model.
     *
     * @param row
     * @return if the row is the last one in the model
     */
    public boolean isLastRow(int row) {
        return row + 1 == iObjects.size();
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
     *
     * @return The columns
     */
    public List<String> getColumns() {
        return iColumns;
    }


    /**
     * Adds the object to the table model. <P>
     *
     * @param pObject The object to add.
     */
    public void add(T pObject) {
        iObjects.add(pObject);

        fireTableRowsInserted(iObjects.size() - 1, iObjects.size() - 1);
    }

    /**
     * Adds all objects to the table model. <P>
     *
     * @param pObjects The objects to add.
     */
    public void addAll(List<T> pObjects) {
        iObjects.addAll(pObjects);

        fireTableRowsInserted(iObjects.size() - pObjects.size(), iObjects.size());
    }

    /**
     * Returns the object at the given row index. <P>
     *
     * @param row The row to get the object from.
     *
     * @return An Object.
     */
    public T getObject(int row) {
        return iObjects.get(row);
    }

    /**
     * Deletes the object from the table model. <P>
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
     * Deletes the data at the given index if the index i within the limits of
     * the underlaying structure. <P>
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
     * Returns the List in the background of this table model. <P>
     * Be careful to modify this.
     *
     * @return The internal list of objects.
     */
    public List<T> getObjects() {
        return iObjects;
    }

    /**
     * Returns a List of objects for the given indices. <P>
     *
     * @param indices The indices of the objects to return.
     *
     * @return A List of objects. If the index array is empty, then all objects
     *         in this model is returned.
     */
    public List<T> getObjects(int[] indices) {
        List<T> objects;

        if (indices.length == 0) {
            objects = getObjects();
        } else {
            objects = new ArrayList<T>(indices.length);
            for (int i : indices) {
                objects.add(getObject(i));
            }
        }
        return objects;
    }

    /**
     * Returns a bundle that can be used for getting locale specific texts. <P>
     *
     * @return A ResourceBundle.
     */
    protected ResourceBundle getBundle() {
        return SSBundle.getBundle();
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
     *
     * @param iTable
     * @return
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

        if (selected.length >= 0){
            return getObjects(selected);
        }
        return null;
    }


    /**
     * Returns the index in this list of the first occurrence of the specified
     * element, or -1 if this list does not contain this element.
     * More formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     *
     * @param o
     * @return the index
     */
    public int indexOf(T o) {
        return iObjects.indexOf(o);
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
}
