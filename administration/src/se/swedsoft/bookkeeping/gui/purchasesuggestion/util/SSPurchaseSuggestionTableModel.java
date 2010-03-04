package se.swedsoft.bookkeeping.gui.purchasesuggestion.util;

import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.calc.math.SSPurchaseOrderMath;

import java.util.*;

/**
 * User: Andreas Lago
 * Date: 2006-aug-14
 * Time: 12:14:25
 */
public class SSPurchaseSuggestionTableModel extends SSTableModel<SSPurchaseSuggestionTableModel.Entry> {

    // private Map<SSPeriodInvoice, Boolean> iSelected;

    public static class Entry {
        SSProduct iProduct;
        Integer iOrderVolume;
        SSSupplier iSupplier;
        Boolean iSelected;
    }

    private static SSStock iStock;
    /**
     *
     * @param iProducts
     */
    public SSPurchaseSuggestionTableModel(List<SSProduct> iProducts) {
        super();
        List<Entry> iItems = new LinkedList<Entry>();

        Entry iEntry;
        iStock = new SSStock(true);
        for (SSProduct iProduct : iProducts) {
            iEntry = new Entry();
            iEntry.iProduct = iProduct;
            iEntry.iSelected = false;
            iEntry.iOrderVolume = iProduct.getOrdercount();
            iEntry.iSupplier = iProduct.getSupplier(SSDB.getInstance().getSuppliers());
            iItems.add(iEntry);
        }
        setObjects(iItems);
    }

    /**
     * Returns the type of data in this model. <P>
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
    public List<SSProduct> getSelected() {
        List<SSProduct> iSelected = new LinkedList<SSProduct>();

        for (Entry iEntry : getObjects()) {

            if(iEntry.iSelected) {
                if (iEntry.iSupplier == null) {
                    return null;
                }
                SSProduct iProduct = new SSProduct(iEntry.iProduct);
                iProduct.setSupplier(iEntry.iSupplier);
                iProduct.setOrdercount(iEntry.iOrderVolume);
                iSelected.add(iProduct);
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
        return new SSTableColumn<Entry>(SSBundle.getBundle().getString("purchasesuggestiontable.column.1")) {
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
    public static SSTableColumn<Entry> COLUMN_NUMBER = new SSTableColumn<Entry>(SSBundle.getBundle().getString("purchasesuggestiontable.column.2")) {
        @Override
        public Object getValue(Entry iEntry) {
            return iEntry.iProduct.getNumber();
        }

        @Override
        public void setValue(Entry iEntry, Object iValue) {
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
     * Description column
     */
    public static SSTableColumn<Entry> COLUMN_DESCRIPTION = new SSTableColumn<Entry>(SSBundle.getBundle().getString("purchasesuggestiontable.column.3")) {
        @Override
        public Object getValue(Entry iEntry) {
            return iEntry.iProduct.getDescription();
        }

        @Override
        public void setValue(Entry iEntry, Object iValue) {
            iEntry.iProduct.setDescription((String)iValue);

        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 275;
        }
    };

    /**
     * Beställningspunktkolumn
     */
    public static SSTableColumn<Entry> COLUMN_ORDERPOINT = new SSTableColumn<Entry>(SSBundle.getBundle().getString("purchasesuggestiontable.column.4")) {
        @Override
        public Object getValue(Entry iEntry) {
            return iEntry.iProduct.getOrderpoint() == null ? 0 : iEntry.iProduct.getOrderpoint();
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
            return 120;
        }
    };


    /**
     *  Lagerantal
     */
    public static SSTableColumn<Entry> COLUMN_STOCK_QUANTITY = new SSTableColumn<Entry>(SSBundle.getBundle().getString("purchasesuggestiontable.column.5")) {
        @Override
        public Object getValue(Entry iEntry) {

            return iStock.getQuantity(iEntry.iProduct) == null ? 0 : iStock.getQuantity(iEntry.iProduct);
        }

        @Override
        public void setValue(Entry iObject, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return Integer.class;
        }

        @Override
        public int getDefaultWidth() {
            return 90;
        }
    };

    /**
     *  Väntas
     */
    public static SSTableColumn<Entry> COLUMN_INCOMMING = new SSTableColumn<Entry>(SSBundle.getBundle().getString("purchasesuggestiontable.column.6")) {
        @Override
        public Object getValue(Entry iEntry) {
            return SSPurchaseOrderMath.getNumberOfIncommingProducts(iEntry.iProduct);
        }

        @Override
        public void setValue(Entry iObject, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return Integer.class;
        }

        @Override
        public int getDefaultWidth() {
            return 90;
        }
    };
    /**
     *  Beställningsantal
     */
    public static SSTableColumn<Entry> COLUMN_ORDER_VOLUME = new SSTableColumn<Entry>(SSBundle.getBundle().getString("purchasesuggestiontable.column.7")) {
        @Override
        public Object getValue(Entry iEntry) {
            return iEntry.iOrderVolume == null ? 0 : iEntry.iOrderVolume;
        }

        @Override
        public void setValue(Entry iEntry, Object iValue) {
            iEntry.iOrderVolume = (Integer) iValue;
        }

        @Override
        public Class getColumnClass() {
            return Integer.class;
        }

        @Override
        public int getDefaultWidth() {
            return 120;
        }
    };

    /**
     *  Leverantör
     */
    public static SSTableColumn<Entry> COLUMN_SUPPLIER = new SSTableColumn<Entry>(SSBundle.getBundle().getString("purchasesuggestiontable.column.8")) {
        @Override
        public Object getValue(Entry iEntry) {
            return iEntry.iSupplier;
        }

        @Override
        public void setValue(Entry iEntry, Object iValue) {
            if(iValue instanceof SSSupplier){
                iEntry.iSupplier = (SSSupplier) iValue;
            }
        }

        @Override
        public Class getColumnClass() {
            return SSSupplier.class;
        }

        @Override
        public int getDefaultWidth() {
            return 200;
        }

        @Override
        public boolean isEditable(int iRow) {
            return true;
        }
    };


}
