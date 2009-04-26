/*
 * @(#)SSNewProjectTableModel.java                v 1.0 2005-sep-20
 *
 * Time-stamp: <2005-sep-20 10:42:26 Hasse>
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
package se.swedsoft.bookkeeping.gui.project.util;

import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSTableEditor;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.SSBookkeeping;

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
    public Class getType() {
        return SSNewProject.class;
    }

    /**
     *
     * @param iTable
     */
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
        public Object getValue(SSNewProject iProject) {
            return iProject.getNumber();
        }

        public void setValue(SSNewProject iProject, Object iValue) {
            iProject.setNumber((String)iValue);

        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 80;
        }
    };

    /**
     *  Nummer
     */
    public static SSTableColumn<SSNewProject> COLUMN_NAME = new SSTableColumn<SSNewProject>(SSBundle.getBundle().getString("projecttable.column.2")) {
        public Object getValue(SSNewProject iProject) {
            return iProject.getName();
        }

        public void setValue(SSNewProject iProject, Object iValue) {
            iProject.setName((String)iValue);

        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 200;
        }
    };

    /**
     *  Beskrivning
     */
    public static SSTableColumn<SSNewProject> COLUMN_DESCRIPTION = new SSTableColumn<SSNewProject>(SSBundle.getBundle().getString("projecttable.column.3")) {
        public Object getValue(SSNewProject iProject) {
            return iProject.getDescription();
        }

        public void setValue(SSNewProject iProject, Object iValue) {
            iProject.setDescription((String)iValue);

        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 400;
        }
    };


    /**
     *  Beskrivning
     */
    public static SSTableColumn<SSNewProject> COLUMN_CONCLUDED = new SSTableColumn<SSNewProject>(SSBundle.getBundle().getString("projecttable.column.4")) {
        public Object getValue(SSNewProject iProject) {
            return iProject.getConcluded();
        }

        public void setValue(SSNewProject iProject, Object iValue) {
            iProject.setConcluded((Boolean)iValue);

        }

        public Class getColumnClass() {
            return Boolean.class;
        }

        public int getDefaultWidth() {
            return 80;
        }
    };



}
