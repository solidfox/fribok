package se.swedsoft.bookkeeping.gui.exportbgcadmission.util;


import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import java.util.List;


/**
 * User: Andreas Lago
 * Date: 2006-aug-28
 * Time: 15:00:30
 */
public class SSExportBGCAdmissionTableModel extends SSTableModel<SSCustomer> {

    /**
     * Constructor.
     *
     * @param iCustomers
     */
    public SSExportBGCAdmissionTableModel(List<SSCustomer> iCustomers) {
        setObjects(iCustomers);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSCustomer.class;
    }

    /**
     * customernumber
     */
    public static SSTableColumn<SSCustomer> COLUMN_NUMBER = new SSTableColumn<SSCustomer>(
            SSBundle.getBundle().getString("exportbgcadmission.column.1")) {
        @Override
        public Object getValue(SSCustomer iCustomer) {
            return iCustomer.getNumber();
        }

        @Override
        public void setValue(SSCustomer iCustomer, Object iValue) {}

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 75;
        }
    };

    /**
     * Supplier nr
     */
    public static SSTableColumn<SSCustomer> COLUMN_NAME = new SSTableColumn<SSCustomer>(
            SSBundle.getBundle().getString("exportbgcadmission.column.2")) {
        @Override
        public Object getValue(SSCustomer iCustomer) {
            return iCustomer.getName();
        }

        @Override
        public void setValue(SSCustomer iCustomer, Object iValue) {}

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
     * Bankgiro
     */
    public static SSTableColumn<SSCustomer> COLUMN_BANKGIRO = new SSTableColumn<SSCustomer>(
            SSBundle.getBundle().getString("exportbgcadmission.column.3")) {
        @Override
        public Object getValue(SSCustomer iCustomer) {
            return iCustomer.getBankgiro();
        }

        @Override
        public void setValue(SSCustomer iCustomer, Object iValue) {}

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Clearingnummer
     */
    public static SSTableColumn<SSCustomer> COLUMN_CLEARINGNUMBER = new SSTableColumn<SSCustomer>(
            SSBundle.getBundle().getString("exportbgcadmission.column.4")) {
        @Override
        public Object getValue(SSCustomer iCustomer) {
            return iCustomer.getClearingNumber();
        }

        @Override
        public void setValue(SSCustomer iCustomer, Object iValue) {}

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 75;
        }
    };

    /**
     * Kontonummer
     */
    public static SSTableColumn<SSCustomer> COLUMN_ACCOUNTNR = new SSTableColumn<SSCustomer>(
            SSBundle.getBundle().getString("exportbgcadmission.column.5")) {
        @Override
        public Object getValue(SSCustomer iCustomer) {
            return iCustomer.getBankgiro();
        }

        @Override
        public void setValue(SSCustomer iCustomer, Object iValue) {}

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 120;
        }
    };

    /**
     * @param iTable
     */
    @Override
    public void setupTable(SSTable iTable) {
        super.setupTable(iTable);
    }
}
