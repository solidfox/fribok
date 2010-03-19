package se.swedsoft.bookkeeping.gui.voucher.util;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.SSNewResultUnit;
import se.swedsoft.bookkeeping.data.SSVoucherRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBoxOld;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;
import se.swedsoft.bookkeeping.gui.util.table.editors.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 * User: Fredrik Stigsson
 * Date: 2006-feb-06
 * Time: 11:03:19
 */
public class SSVoucherEditors {

    public static Color COLOR_CROSSED = new Color(255,192,192);
    public static Color COLOR_ADDED   = new Color(192,192,255);

    private SSVoucherEditors() {
    }


    static class SSDefaultVoucherRowRenderer<T extends SSTableSearchable> extends DefaultTableCellRenderer {

        private SSDefaultTableModel<SSVoucherRow> iModel;

        public SSDefaultVoucherRowRenderer(SSDefaultTableModel<SSVoucherRow> pModel ){
            iModel = pModel;
        }
        /**
         * Returns the default table cell renderer.
         *
         * @param table      the <code>JTable</code>
         * @param value      the value to assign to the cell at
         *                   <code>[row, column]</code>
         * @param isSelected true if cell is selected
         * @param hasFocus   true if cell has focus
         * @param row        the row of the cell to render
         * @param column     the column of the cell to render
         *
         * @return the default table cell renderer
         */
        @Override
        public Component getTableCellRendererComponent(JTable table,  Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            SSVoucherRow iRow =  iModel.getObject(row);

            if (iRow.isCrossed()) {
                c.setBackground( COLOR_CROSSED );
            } else
            if (iRow.isAdded()) {
                c.setBackground( COLOR_ADDED );
            } else
            if (!isSelected) {
                c.setBackground(Color.WHITE);
            }

            return c;
        }

        @Override
        protected void setValue(Object value) {
            if(value instanceof SSTableSearchable){
                setValue( value  );
            } else {
                super.setValue(value);
            }
        }

        protected void setValue(T value) {
            super.setValue(value);
        }

    }


    public static DefaultTableCellRenderer createVoucherRowRenderer(SSDefaultTableModel<SSVoucherRow> pModel){
        return new SSDefaultVoucherRowRenderer(pModel);
    }


    public static DefaultTableCellRenderer createAccountRenderer(SSDefaultTableModel<SSVoucherRow> pModel){
        return new SSDefaultVoucherRowRenderer<SSAccount>(pModel){
            @Override
            public void setValue(SSAccount value) {
                if( value != null){
                    setText( Integer.toString( value.getNumber() ) );
                } else {
                    setText("");
                }

            }
        };
    }



    /*
   public static DefaultTableCellRenderer createAccountRenderer(SSDefaultTableModel<SSVoucherRow> pModel){
        return new SSDefaultVoucherRowRenderer(pModel){
            public void setInvoiceValue(Object value) {

                if( value != null &&  ((SSAccount)value).getNumber() != null){
                    setText( Integer.toString( ((SSAccount)value).getNumber() ) );
                } else {
                    setText("");
                }

            }
        };
    }

    */

    public static DefaultTableCellRenderer createResultUnitRenderer(SSDefaultTableModel<SSVoucherRow> pModel){
        return new SSDefaultVoucherRowRenderer(pModel){
            @Override
            public void setValue(Object value) {
                setText((value == null) ? "" : ((SSNewResultUnit)value).getNumber());
            }
        };
    }

    public static DefaultTableCellRenderer createProjectRenderer(SSDefaultTableModel<SSVoucherRow> pModel){
        return new SSDefaultVoucherRowRenderer(pModel){
            @Override
            public void setValue(Object value) {
                setText((value == null) ? "" : ((SSNewProject)value).getNumber());
            }
        };
    }

    public static DefaultTableCellRenderer createBigDecimalRenderer(SSDefaultTableModel<SSVoucherRow> pModel){
        DefaultTableCellRenderer iEditor = new SSDefaultVoucherRowRenderer(pModel){
            @Override
            protected void setValue(Object value) {
                NumberFormat format = NumberFormat.getNumberInstance();
                format.setMinimumFractionDigits(2);
                format.setMaximumFractionDigits(2);
                format.setGroupingUsed(true);

                setText(value == null ? "" : format.format(value));
            }
        };
        iEditor.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);

        return iEditor;
    }

    public static DefaultTableCellRenderer createDateRenderer(SSDefaultTableModel<SSVoucherRow> pModel){

        return new SSDefaultVoucherRowRenderer(pModel){
            @Override
            protected void setValue(Object value) {
                DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);

                setText(value != null ? format.format((Date)value) : "");
            }
        };
    }



    public static TableCellEditor createAccountEditor() {
        SSDefaultTableModel<SSAccount> model = new SSDefaultTableModel<SSAccount>( SSDB.getInstance().getCurrentYear().getActiveAccounts() ) {
            @Override
            public Class getType() {
                return SSAccount.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                SSAccount account = getObject(rowIndex);
                Object value = null;
                switch (columnIndex) {
                    case 0:
                        value = account.getNumber();
                        break;
                    case 1:
                        value = account.getDescription();
                        break;
                }

                return value;
            }
        };

        model.addColumn(SSBundle.getBundle().getString("accounttable.column.1"));
        model.addColumn(SSBundle.getBundle().getString("accounttable.column.2"));


        SSTableComboBoxOld.CellEditor iEditor = SSTableComboBoxOld.createAsCellEditor(model);
        iEditor.setSearchColumns(0, 1);
        iEditor.setPopupSize   (360,200);
        iEditor.setColumnWidths(60,300);
        return iEditor;

    }

    public static TableCellEditor createProjectEditor() {

        SSDefaultTableModel<SSNewProject> model = new SSDefaultTableModel<SSNewProject>(SSDB.getInstance().getProjects()) {

            @Override
            public Class getType() {
                return SSNewProject.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                SSNewProject project = getObject(rowIndex);
                Object value = null;
                switch (columnIndex) {
                    case 0:
                        value = project.getNumber();
                        break;
                    case 1:
                        value = project.getName();
                        break;
                    case 2:
                        value = project.getDescription();
                        break;
                }

                return value;
            }
        };
        model.addColumn(SSBundle.getBundle().getString("projecttable.column.1"));
        model.addColumn(SSBundle.getBundle().getString("projecttable.column.2"));
        model.addColumn(SSBundle.getBundle().getString("projecttable.column.3"));

        SSTableComboBoxOld.CellEditor iEditor = SSTableComboBoxOld.createAsCellEditor(model);
        iEditor.setSearchColumns(0);
        iEditor.setPopupSize   (360,200);
        iEditor.setColumnWidths(60,100,200);
        return iEditor;
    }

    public static TableCellEditor createResultUnitEditor() {
        SSDefaultTableModel<SSNewResultUnit> model = new SSDefaultTableModel<SSNewResultUnit>(SSDB.getInstance().getResultUnits()) {
            @Override
            public Class getType() {
                return SSNewResultUnit.class;
            }
            public Object getValueAt(int rowIndex, int columnIndex) {
                SSNewResultUnit resultUnit = getObject(rowIndex);
                Object value = null;
                switch (columnIndex) {
                    case 0:
                        value = resultUnit.getNumber();
                        break;
                    case 1:
                        value= resultUnit.getName();
                        break;
                }

                return value;
            }
        };
        model.addColumn(SSBundle.getBundle().getString("resultunittable.column.1"));
        model.addColumn(SSBundle.getBundle().getString("resultunittable.column.2"));

        SSTableComboBoxOld.CellEditor iEditor = SSTableComboBoxOld.createAsCellEditor(model);
        iEditor.setSearchColumns(0);
        iEditor.setPopupSize   (360,200);
        iEditor.setColumnWidths(60,300);
        return iEditor;
    }



    public static void setupVoucherRowTable(final SSTable pTable, final SSDefaultTableModel<SSVoucherRow> pModel) {
        // Show the lines.
        pTable.setShowHorizontalLines(true);
        pTable.setShowVerticalLines(true);
        pTable.setColumnSortingEnabled(false);



        // Disallow the reordering of the table headers.
        pTable.getTableHeader().setReorderingAllowed(false);

        pTable.setDefaultRenderer(SSAccount.class, new SSAccountCellRenderer() );
        pTable.setDefaultEditor  (SSAccount.class, new SSAccountCellEditor()  );

        // Set appropriate renderers and editors.
   //     pTable.setDefaultRenderer(String.class, new SSDefaultVoucherRowRenderer(pModel));

        //   pTable.setDefaultRenderer(SSAccount.class, createAccountRenderer(pModel) );
        //   pTable.setDefaultEditor  (SSAccount.class, createAccountEditor()  );

        //    pTable.setDefaultRenderer(SSNewProject.class, createProjectRenderer(pModel));
        //  pTable.setDefaultEditor  (SSNewProject.class, createProjectEditor() );

        pTable.setDefaultRenderer(SSNewProject.class, new SSProjectCellRenderer());
        pTable.setDefaultEditor  (SSNewProject.class, new SSProjectCellEditor() );

        // pTable.setDefaultRenderer(SSNewResultUnit.class, createResultUnitRenderer(pModel));
        //Table.setDefaultEditor  (SSNewResultUnit.class, createResultUnitEditor() );

        pTable.setDefaultRenderer(SSNewResultUnit.class, new SSResultUnitCellRenderer());
        pTable.setDefaultEditor  (SSNewResultUnit.class, new SSResultUnitCellEditor() );

        // Set the default renderer for the date cells.
       // pTable.setDefaultRenderer(Date.class      , createDateRenderer(pModel));
         pTable.setDefaultRenderer(Date.class      , new SSDateCellRenderer());

        //pTable.setDefaultRenderer(BigDecimal.class, createBigDecimalRenderer(pModel) );
        pTable.setDefaultRenderer(BigDecimal.class, new SSBigDecimalCellRenderer(2) );
        pTable.setDefaultEditor  (BigDecimal.class, new SSBigDecimalCellEditor  (2));

        pTable.setModel( pModel );


        try{

            pTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_ACCOUNT     ).setPreferredWidth(65);
            pTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_DESCRIPTION ).setPreferredWidth(240);
            pTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_DEBET       ).setPreferredWidth(85);
            pTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_CREDIT      ).setPreferredWidth(85);
            pTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_PROJECT     ).setPreferredWidth(85);
            pTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_RESULTUNIT  ).setPreferredWidth(85);

            if( pTable.getColumnModel().getColumnCount() == 8 ){
                pTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_EDITED_DATE     ).setPreferredWidth(70);
                pTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_EDITED_SIGNATURE).setPreferredWidth(65);
            } else {
                pTable.getColumnModel().getColumn(SSVoucherRowTableModelOld.COL_DESCRIPTION ).setPreferredWidth(376);
            }
        } catch (Exception ignored) {}
    }
}


