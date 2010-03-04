package se.swedsoft.bookkeeping.gui.accountplans.util;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.common.SSVATCode;
import se.swedsoft.bookkeeping.gui.util.table.model.SSEditableTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSVATCellEditor;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSVATCellRenderer;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * Date: 2006-feb-13
 * Time: 14:16:01
 */
public class SSAccountPlanRowTableModel extends SSEditableTableModel<SSAccount> {
    /**
     *
     * @return
     */
    @Override
    public SSAccount newObject() {
        return new SSAccount();
    }


    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSAccount.class;
    }



    /**
     * Aktivt
     */
    public static SSTableColumn<SSAccount> COLUMN_ACTIVE = new SSTableColumn<SSAccount>(SSBundle.getBundle().getString("accounttable.column.3")) {
        @Override
        public Object getValue(SSAccount iAccount) {
            return iAccount.getNumber() != null && iAccount.isActive();
        }

        @Override
        public void setValue(SSAccount iAccount, Object iValue) {
           iAccount.setActive((Boolean)iValue);
        }

        @Override
        public Class getColumnClass() {
            return Boolean.class;
        }

        @Override
        public int getDefaultWidth() {
            return 55;
        }
    };


    /**
     * Nummer
     */
    public static SSTableColumn<SSAccount> COLUMN_NUMBER = new SSTableColumn<SSAccount>(SSBundle.getBundle().getString("accounttable.column.1")) {
        @Override
        public Object getValue(SSAccount iAccount) {
            return iAccount.getNumber();
        }

        @Override
        public void setValue(SSAccount iAccount, Object iValue) {
            iAccount.setNumber((Integer)iValue);
        }

        @Override
        public Class getColumnClass() {
            return Integer.class;
        }

        @Override
        public int getDefaultWidth() {
            return 60;
        }
    };

    /**
     * Beskrivning
     */
    public static SSTableColumn<SSAccount> COLUMN_DESCRIPTION = new SSTableColumn<SSAccount>(SSBundle.getBundle().getString("accounttable.column.2")) {
        @Override
        public Object getValue(SSAccount iAccount) {
            return iAccount.getDescription();
        }

        @Override
        public void setValue(SSAccount iAccount, Object iValue) {
            iAccount.setDescription((String)iValue);
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 230;
        }
    };

    /**
     * Beskrivning
     */
    public static SSTableColumn<SSAccount> COLUMN_VATCODE = new SSTableColumn<SSAccount>(SSBundle.getBundle().getString("accounttable.column.4")) {
        @Override
        public Object getValue(SSAccount iAccount) {

            SSVATCode iVATCode = SSVATCode.decode(iAccount.getVATCode());

            if( iVATCode != null){
                return iVATCode.getName();
            } else {
                return iAccount.getVATCode();
            }
        }

        @Override
        public void setValue(SSAccount iAccount, Object iValue) {
            if( iValue instanceof SSVATCode){
                SSVATCode iVATCode = (SSVATCode) iValue;

                 iAccount.setVATCode( iVATCode.getName() );
            } else {
                iAccount.setVATCode( (String)iValue );
            }
        }

        /**
         *
         * @return
         */
        @Override
        public Class getColumnClass() {
            return String.class;
        }

        /**
         *
         * @return
         */
        @Override
        public int getDefaultWidth() {
            return 70;
        }

        /**
         * @return
         */
        @Override
        public TableCellEditor getCellEditor() {
            return new SSVATCellEditor();
        }

        /**
         * @return
         */
        @Override
        public TableCellRenderer getCellRenderer() {
            return new SSVATCellRenderer();
        }
    };


    /**
     * Beskrivning
     */
    public static SSTableColumn<SSAccount> COLUMN_SRUCODE = new SSTableColumn<SSAccount>(SSBundle.getBundle().getString("accounttable.column.5")) {
        @Override
        public Object getValue(SSAccount iAccount) {
            return iAccount.getSRUCode();
        }

        @Override
        public void setValue(SSAccount iAccount, Object iValue) {
            iAccount.setSRUCode((String)iValue);
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 70;
        }
    };


    /**
     * Rapportkod
     */
    public static SSTableColumn<SSAccount> COLUMN_REPORTCODE = new SSTableColumn<SSAccount>(SSBundle.getBundle().getString("accounttable.column.6")) {
        @Override
        public Object getValue(SSAccount iAccount) {
            return iAccount.getReportCode();
        }

        @Override
        public void setValue(SSAccount iAccount, Object iValue) {
            iAccount.setReportCode((String)iValue);
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 70;
        }
    };

    /**
     * Projekt krävs
     */

    public static SSTableColumn<SSAccount> COLUMN_PROJECT = new SSTableColumn<SSAccount>(SSBundle.getBundle().getString("accounttable.column.7")) {
        @Override
        public Object getValue(SSAccount iAccount) {
            return iAccount.getNumber() != null && iAccount.isProjectRequired();
        }

        @Override
        public void setValue(SSAccount iAccount, Object iValue) {
           iAccount.setProjectRequired((Boolean)iValue);
        }

        @Override
        public Class getColumnClass() {
            return Boolean.class;
        }

        @Override
        public int getDefaultWidth() {
            return 55;
        }
    };

    /**
     * Resultatenhet krävs
     */

    public static SSTableColumn<SSAccount> COLUMN_RESULTUNIT = new SSTableColumn<SSAccount>(SSBundle.getBundle().getString("accounttable.column.8")) {
        @Override
        public Object getValue(SSAccount iAccount) {
            return iAccount.getNumber() != null && iAccount.isResultUnitRequired();
        }

        @Override
        public void setValue(SSAccount iAccount, Object iValue) {
           iAccount.setResultUnitRequired((Boolean)iValue);
        }

        @Override
        public Class getColumnClass() {
            return Boolean.class;
        }

        @Override
        public int getDefaultWidth() {
            return 90;
        }
    };




}

