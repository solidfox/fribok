/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.project.util;

import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.List;

public class SSProjectTableModel extends SSTableModel<SSNewProject> {


    /**
     * Default constructor.
     */
    public SSProjectTableModel() {
        super(SSDB.getInstance().getProjects());
    }

    /**
     * Default constructor.
     */
    public SSProjectTableModel(List<SSNewProject> iProjects) {
        super(iProjects);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSNewProject.class;
    }

    /**
     *
     * @param iTable
     */
    @Override
    public void setupTable(SSTable iTable) {
        iTable.setModel(this);
        iTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        iTable.tableChanged(null);

        int iIndex = 0;
        for (SSTableColumn<SSNewProject> iColumn : getColumns()) {
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
     * @return
     */
    public static SSProjectTableModel getDropDownModel(){
        SSProjectTableModel iModel = new SSProjectTableModel();


        iModel.addColumn( SSProjectTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSProjectTableModel.COLUMN_NAME   );

        return iModel;
    }

    /**
     *
     * @return
     */
    public static SSProjectTableModel getDropDownModel(List<SSNewProject> iProjects){
        SSProjectTableModel iModel = new SSProjectTableModel(iProjects);

        iModel.addColumn( SSProjectTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSProjectTableModel.COLUMN_NAME   );

        return iModel;
    }



    /**
     *  Nummer
     */
    public static SSTableColumn<SSNewProject> COLUMN_NUMBER = new SSTableColumn<SSNewProject>(SSBundle.getBundle().getString("projecttable.column.1")) {
        @Override
        public Object getValue(SSNewProject iProject) {
            return iProject.getNumber();
        }

        @Override
        public void setValue(SSNewProject iProject, Object iValue) {
            iProject.setNumber((String)iValue);

        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 80;
        }
    };

    /**
     *  Nummer
     */
    public static SSTableColumn<SSNewProject> COLUMN_NAME = new SSTableColumn<SSNewProject>(SSBundle.getBundle().getString("projecttable.column.2")) {
        @Override
        public Object getValue(SSNewProject iProject) {
            return iProject.getName();
        }

        @Override
        public void setValue(SSNewProject iProject, Object iValue) {
            iProject.setName((String)iValue);

        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 200;
        }
    };

    /**
     *  Beskrivning
     */
    public static SSTableColumn<SSNewProject> COLUMN_DESCRIPTION = new SSTableColumn<SSNewProject>(SSBundle.getBundle().getString("projecttable.column.3")) {
        @Override
        public Object getValue(SSNewProject iProject) {
            return iProject.getDescription();
        }

        @Override
        public void setValue(SSNewProject iProject, Object iValue) {
            iProject.setDescription((String)iValue);

        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 400;
        }
    };


    /**
     *  Beskrivning
     */
    public static SSTableColumn<SSNewProject> COLUMN_CONCLUDED = new SSTableColumn<SSNewProject>(SSBundle.getBundle().getString("projecttable.column.4")) {
        @Override
        public Object getValue(SSNewProject iProject) {
            return iProject.getConcluded();
        }

        @Override
        public void setValue(SSNewProject iProject, Object iValue) {
            iProject.setConcluded((Boolean)iValue);

        }

        @Override
        public Class getColumnClass() {
            return Boolean.class;
        }

        @Override
        public int getDefaultWidth() {
            return 80;
        }
    };



}
