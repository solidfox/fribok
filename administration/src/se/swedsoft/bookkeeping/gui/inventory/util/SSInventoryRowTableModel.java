package se.swedsoft.bookkeeping.gui.inventory.util;

import se.swedsoft.bookkeeping.data.SSInventory;
import se.swedsoft.bookkeeping.data.SSInventoryRow;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSStock;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSEditableTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSInventoryRowTableModel extends SSEditableTableModel<SSInventoryRow> {

    private static SSStock iStock;

    /**
     * Default constructor.
     */
    public SSInventoryRowTableModel(SSStock iStock) {
        super();

        SSInventoryRowTableModel.iStock = iStock;
    }


    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSInventory.class;
    }

    /**
     * @return
     */
    @Override
    public SSInventoryRow newObject() {
        return new SSInventoryRow();
    }

    /**
     * Product
     */
    public static SSTableColumn<SSInventoryRow> COLUMN_PRODUCT = new SSTableColumn<SSInventoryRow>(SSBundle.getBundle().getString("inventoryrowtable.column.1")) {
        @Override
        public Object getValue(SSInventoryRow iRow) {
            return iRow.getProduct();
        }

        @Override
        public void setValue(SSInventoryRow iRow, Object iValue) {
            SSProduct iProduct = (SSProduct)iValue;

            iRow.setProduct(iProduct);

            if( iStock == null || iProduct == null ) return;

            iRow.setStockQuantity( iStock.getQuantity(iProduct) );
            iRow.setChange(0);
        }

        @Override
        public Class getColumnClass() {
            return SSProduct.class;
        }

        @Override
        public int getDefaultWidth() {
            return 90;
        }
    };

    /**
     * Product beskrivning
     */
    public static SSTableColumn<SSInventoryRow> COLUMN_DESCRIPTION = new SSTableColumn<SSInventoryRow>(SSBundle.getBundle().getString("inventoryrowtable.column.2")) {
        @Override
        public Object getValue(SSInventoryRow iRow) {
            SSProduct iProduct = iRow.getProduct();

            return iProduct == null ? null : iProduct.getDescription();
        }

        @Override
        public void setValue(SSInventoryRow iRow, Object iValue) {

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
     * Inventerat antal
     */
    public static SSTableColumn<SSInventoryRow> COLUMN_INVENTORYQUANTITY = new SSTableColumn<SSInventoryRow>(SSBundle.getBundle().getString("inventoryrowtable.column.3")) {
        @Override
        public Object getValue(SSInventoryRow iRow) {
            return iRow.getInventoryQuantity();
        }

        @Override
        public void setValue(SSInventoryRow iRow, Object iValue) {
            iRow.setInventoryQuantity((Integer)iValue);
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
     * Antal i lager
     */
    public static SSTableColumn<SSInventoryRow> COLUMN_STOCKQUANTITY = new SSTableColumn<SSInventoryRow>(SSBundle.getBundle().getString("inventoryrowtable.column.4")) {
        @Override
        public Object getValue(SSInventoryRow iRow) {
            Integer   iQuantity = iRow.getStockQuantity();
            SSProduct iProduct  = iRow.getProduct();

            if(iQuantity == null && iProduct != null && iStock != null){
                iQuantity = iStock.getQuantity(iProduct);

                iRow.setStockQuantity( iQuantity );
            }

            return iQuantity;
        }

        @Override
        public void setValue(SSInventoryRow iRow, Object iValue) {
          // read only
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
     * Skillnad
     */
    public static SSTableColumn<SSInventoryRow> COLUMN_CHANGE = new SSTableColumn<SSInventoryRow>(SSBundle.getBundle().getString("inventoryrowtable.column.5")) {
        @Override
        public Object getValue(SSInventoryRow iRow) {
            return iRow.getChange();
        }

        @Override
        public void setValue(SSInventoryRow iRow, Object iValue) {
            iRow.setChange((Integer)iValue);
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


    public static SSTableColumn<SSInventoryRow> COLUMN_WAREHOUSELOCATION = new SSTableColumn<SSInventoryRow>(SSBundle.getBundle().getString("inventoryrowtable.column.6")) {
        @Override
        public Object getValue(SSInventoryRow iRow) {
            SSProduct iProduct  = iRow.getProduct();
            return iProduct == null ? "" : (iProduct.getWarehouseLocation() == null ? "" : iProduct.getWarehouseLocation());
        }

        @Override
        public void setValue(SSInventoryRow iRow, Object iValue) {
          // read only
        }

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
     * Product nummer

     public static SSTableColumn<SSProduct> COLUMN_NUMBER = new SSTableColumn<SSProduct>(SSBundle.getBundle().getString("producttable.column.1")) {
     public Object getValue(SSProduct iInventory) {
     return iInventory.getNumber();
     }

     public void setValue(SSProduct iInventory, Object iValue) {
     iInventory.setNumber((String)iValue);
     }

     public Class getColumnClass() {
     return String.class;
     }

     public int getDefaultWidth() {
     return 90;
     }
     };

     public static SSTableColumn<SSProduct> COLUMN_DESCRIPTION = new SSTableColumn<SSProduct>(SSBundle.getBundle().getString("producttable.column.2")) {
     public Object getValue(SSProduct iProduct) {
     return iProduct.getDescription();
     }

     public void setValue(SSProduct iProduct, Object iValue) {
     iProduct.setDescription((String)iValue);
     }

     public Class getColumnClass() {
     return String.class;
     }

     public int getDefaultWidth() {
     return 400;
     }
     };

     */

}
