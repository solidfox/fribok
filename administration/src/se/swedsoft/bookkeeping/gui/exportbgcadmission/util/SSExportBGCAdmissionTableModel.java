package se.swedsoft.bookkeeping.gui.exportbgcadmission.util;

import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.supplier.SSSupplierDialog;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.importexport.supplierpayments.data.PaymentMethod;
import se.swedsoft.bookkeeping.importexport.supplierpayments.data.SupplierPayment;

import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

/**
 * User: Andreas Lago
 * Date: 2006-aug-28
 * Time: 15:00:30
 */
public class SSExportBGCAdmissionTableModel extends SSTableModel<SSCustomer> {



    /**
     * Constructor.
     *
     */
    public SSExportBGCAdmissionTableModel(List<SSCustomer> iCustomers) {
        super();
        setObjects(iCustomers);
    }

    /**
     * Returns the type of data in this model. <P>
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSCustomer.class;
    }

    /**
     * customernumber
     */
    public static SSTableColumn<SSCustomer> COLUMN_NUMBER = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("exportbgcadmission.column.1")) {
        public Object getValue(SSCustomer iCustomer) {
            return iCustomer.getNumber();
        }

        public void setValue(SSCustomer iCustomer, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }
        public int getDefaultWidth() {
            return 75;
        }
    };

    /**
     * Supplier nr
     */
    public static SSTableColumn<SSCustomer> COLUMN_NAME = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("exportbgcadmission.column.2")) {
        public Object getValue(SSCustomer iCustomer) {
            return iCustomer.getName();
        }

        public void setValue(SSCustomer iCustomer, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }
        public int getDefaultWidth() {
            return 200;
        }
    };

    /**
     * Bankgiro
     */
    public static SSTableColumn<SSCustomer> COLUMN_BANKGIRO = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("exportbgcadmission.column.3")) {
        public Object getValue(SSCustomer iCustomer) {
            return iCustomer.getBankgiro();
        }

        public void setValue(SSCustomer iCustomer, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }
        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Clearingnummer
     */
    public static SSTableColumn<SSCustomer> COLUMN_CLEARINGNUMBER = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("exportbgcadmission.column.4")) {
        public Object getValue(SSCustomer iCustomer) {
            return iCustomer.getClearingNumber();
        }

        public void setValue(SSCustomer iCustomer, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }
        public int getDefaultWidth() {
            return 75;
        }
    };

    /**
     * Kontonummer
     */
    public static SSTableColumn<SSCustomer> COLUMN_ACCOUNTNR = new SSTableColumn<SSCustomer>(SSBundle.getBundle().getString("exportbgcadmission.column.5")) {
        public Object getValue(SSCustomer iCustomer) {
            return iCustomer.getBankgiro();
        }

        public void setValue(SSCustomer iCustomer, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }
        public int getDefaultWidth() {
            return 120;
        }
    };

    /**
     * @param iTable
     */
    public void setupTable(SSTable iTable) {
        super.setupTable(iTable);
    }
}
