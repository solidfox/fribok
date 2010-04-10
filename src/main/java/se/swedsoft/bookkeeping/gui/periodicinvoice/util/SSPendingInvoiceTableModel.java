package se.swedsoft.bookkeeping.gui.periodicinvoice.util;

import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSPeriodicInvoice;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

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

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("se.swedsoft.bookkeeping.gui.periodicinvoice.util.SSPendingInvoiceTableModel.Entry");
            sb.append("{iInvoice=").append(iInvoice);
            sb.append(", iPeriodicInvoice=").append(iPeriodicInvoice);
            sb.append(", iSelected=").append(iSelected);
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     *
     * @param iInvoices
     */
    public SSPendingInvoiceTableModel(Map<SSPeriodicInvoice, List<SSInvoice>> iInvoices) {
        List<Entry> iItems = new LinkedList<Entry>();

        for (SSPeriodicInvoice iPeriodicInvoice : iInvoices.keySet()) {

            for (SSInvoice iInvoice : iInvoices.get(iPeriodicInvoice)) {
                Entry iEntry = new Entry();
                iEntry.iPeriodicInvoice = iPeriodicInvoice;
                iEntry.iInvoice         = iInvoice;
                iEntry.iSelected        = false;
                iItems.add(iEntry);
            }
        }
        setObjects(iItems);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
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
     * @return
     */
    public SSTableColumn<Entry> getSelectionColumn(){
        return new SSTableColumn<Entry>(SSBundle.getBundle().getString("periodicinvoicetable.column.5")) {
            @Override
            public Object getValue(Entry iEntry) {
                return iEntry.iSelected;
            }

            @Override
            public void setValue(Entry iEntry, Object iValue) {
                iEntry.iSelected = (Boolean)iValue;
            }

            @Override
            public Class getColumnClass() {
                return Boolean.class;
            }

            @Override
            public int getDefaultWidth() {
                return 60;
            }
        };
    }

    /**
     * Number column
     */
    public static SSTableColumn<Entry> COLUMN_NUMBER = new SSTableColumn<Entry>(SSBundle.getBundle().getString("periodicinvoicetable.column.1")) {
        @Override
        public Object getValue(Entry iEntry) {
            return iEntry.iPeriodicInvoice.getNumber();
        }

        @Override
        public void setValue(Entry iEntry, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return Integer.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Description column
     */
    public static SSTableColumn<Entry> COLUMN_DESCRIPTION = new SSTableColumn<Entry>(SSBundle.getBundle().getString("periodicinvoicetable.column.2")) {
        @Override
        public Object getValue(Entry iEntry) {
            return iEntry.iPeriodicInvoice.getDescription();
        }

        @Override
        public void setValue(Entry iEntry, Object iValue) {
            iEntry.iPeriodicInvoice.setDescription((String)iValue);

        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 250;
        }
    };

    /**
     * Date column
     */
    public static SSTableColumn<Entry> COLUMN_DATE = new SSTableColumn<Entry>(SSBundle.getBundle().getString("invoicetable.column.5")) {
        @Override
        public Object getValue(Entry iEntry) {
            return iEntry.iInvoice.getDate();
        }

        @Override
        public void setValue(Entry iEntry, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return Date.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };


    /**
     *  Kund nummer
     */
    public static SSTableColumn<Entry> COLUMN_CUSTOMER_NR = new SSTableColumn<Entry>(SSBundle.getBundle().getString("invoicetable.column.3")) {
        @Override
        public Object getValue(Entry iEntry) {
            return iEntry.iInvoice.getCustomerNr();
        }

        @Override
        public void setValue(Entry iObject, Object iValue) {
        }

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
     *  Kund namn
     */
    public static SSTableColumn<Entry> COLUMN_CUSTOMER_NAME = new SSTableColumn<Entry>(SSBundle.getBundle().getString("invoicetable.column.4")) {
        @Override
        public Object getValue(Entry iEntry) {
            return iEntry.iInvoice.getCustomerName();
        }

        @Override
        public void setValue(Entry iInvoice, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 150;
        }
    };


}
