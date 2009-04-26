package se.swedsoft.bookkeeping.gui.periodicinvoice.util;

import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSPeriodicInvoice;

import java.util.*;

/**
 * User: Andreas Lago
 * Date: 2006-aug-14
 * Time: 12:14:25
 */
public class SSPendingInvoiceTableModel extends SSTableModel<SSPendingInvoiceTableModel.Entry> {

    // private Map<SSPeriodInvoice, Boolean> iSelected;

    public static class Entry {
        SSPeriodicInvoice iPeriodicInvoice;

        SSInvoice iInvoice;

        Boolean iSelected;
    }

    /**
     *
     * @param iInvoices
     */
    public SSPendingInvoiceTableModel(Map<SSPeriodicInvoice, List<SSInvoice>> iInvoices) {
        super();
        List<Entry> iItems = new LinkedList<Entry>();

        Entry iEntry;
        for (SSPeriodicInvoice iPeriodicInvoice : iInvoices.keySet()) {

            for (SSInvoice iInvoice : iInvoices.get(iPeriodicInvoice)) {
                iEntry = new Entry();
                iEntry.iPeriodicInvoice = iPeriodicInvoice;
                iEntry.iInvoice         = iInvoice;
                iEntry.iSelected        = false;
                iItems.add(iEntry);
            }
        }
        setObjects(iItems);
    }

    /**
     * Returns the type of data in this model. <P>
     *
     * @return The current data type.
     */
    public Class getType() {
        return Entry.class;
    }


    /**
     *
     * @return
     */
    public Map<SSPeriodicInvoice,List<SSInvoice>> getSelected() {
        Map<SSPeriodicInvoice,List<SSInvoice>> iSelected = new HashMap<SSPeriodicInvoice, List<SSInvoice>>();

        for (Entry iEntry : getObjects()) {

            List<SSInvoice> iInvoices = iSelected.get(iEntry.iPeriodicInvoice);

            if(iInvoices == null) {
                iInvoices = new LinkedList<SSInvoice>();
                iSelected.put(iEntry.iPeriodicInvoice, iInvoices);
            }

            if(iEntry.iSelected){
                iInvoices.add(iEntry.iInvoice);
            }
        }
        return iSelected;
    }


    /**
     *
     */
    public void selectAll(){
        for (Entry entry : getObjects()) {
            entry.iSelected = true;
        }
        fireTableDataChanged();
    }

    /**
     *  Vald
     */
    public SSTableColumn<Entry> getSelectionColumn(){
        return new SSTableColumn<Entry>(SSBundle.getBundle().getString("periodicinvoicetable.column.5")) {
            public Object getValue(Entry iEntry) {
                return iEntry.iSelected;
            }

            public void setValue(Entry iEntry, Object iValue) {
                iEntry.iSelected = (Boolean)iValue;
            }

            public Class getColumnClass() {
                return Boolean.class;
            }

            public int getDefaultWidth() {
                return 60;
            }
        };
    }

    /**
     * Number column
     */
    public static SSTableColumn<Entry> COLUMN_NUMBER = new SSTableColumn<Entry>(SSBundle.getBundle().getString("periodicinvoicetable.column.1")) {
        public Object getValue(Entry iEntry) {
            return iEntry.iPeriodicInvoice.getNumber();
        }

        public void setValue(Entry iEntry, Object iValue) {
        }

        public Class getColumnClass() {
            return Integer.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Description column
     */
    public static SSTableColumn<Entry> COLUMN_DESCRIPTION = new SSTableColumn<Entry>(SSBundle.getBundle().getString("periodicinvoicetable.column.2")) {
        public Object getValue(Entry iEntry) {
            return iEntry.iPeriodicInvoice.getDescription();
        }

        public void setValue(Entry iEntry, Object iValue) {
            iEntry.iPeriodicInvoice.setDescription((String)iValue);

        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 250;
        }
    };

    /**
     * Date column
     */
    public static SSTableColumn<Entry> COLUMN_DATE = new SSTableColumn<Entry>(SSBundle.getBundle().getString("invoicetable.column.5")) {
        public Object getValue(Entry iEntry) {
            return iEntry.iInvoice.getDate();
        }

        public void setValue(Entry iEntry, Object iValue) {
        }

        public Class getColumnClass() {
            return Date.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };


    /**
     *  Kund nummer
     */
    public static SSTableColumn<Entry> COLUMN_CUSTOMER_NR = new SSTableColumn<Entry>(SSBundle.getBundle().getString("invoicetable.column.3")) {
        public Object getValue(Entry iEntry) {
            return iEntry.iInvoice.getCustomerNr();
        }

        public void setValue(Entry iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };
    /**
     *  Kund namn
     */
    public static SSTableColumn<Entry> COLUMN_CUSTOMER_NAME = new SSTableColumn<Entry>(SSBundle.getBundle().getString("invoicetable.column.4")) {
        public Object getValue(Entry iEntry) {
            return iEntry.iInvoice.getCustomerName();
        }

        public void setValue(Entry iInvoice, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 150;
        }
    };


}
