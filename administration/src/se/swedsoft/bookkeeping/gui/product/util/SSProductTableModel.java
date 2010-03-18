package se.swedsoft.bookkeeping.gui.product.util;

import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSStock;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.math.BigDecimal;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSProductTableModel extends SSTableModel<SSProduct> {


    /**
     * Default constructor.
     */
    public SSProductTableModel() {
        super(SSDB.getInstance().getProducts());
    }

    /**
     * Default constructor.
     * @param iProducts
     */
    public SSProductTableModel(List<SSProduct> iProducts) {
        super(iProducts);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSProduct.class;
    }


    @Override
    public void setupTable(SSTable iTable) {
        iTable.setModel(this);
        iTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        iTable.tableChanged(null);

        int iIndex = 0;
        for (SSTableColumn<SSProduct> iColumn : getColumns()) {
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
    public static SSProductTableModel getDropDownModel(){
        return getDropDownModel(SSDB.getInstance().getProducts());
    }


    /**
     *
     * @param iProducts
     * @return
     */
    public static SSProductTableModel getDropDownModel(List<SSProduct> iProducts){
        SSProductTableModel iModel = new SSProductTableModel(iProducts);

        iModel.addColumn( SSProductTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSProductTableModel.COLUMN_DESCRIPTION   );

        return iModel;

    }



    /**
     *  Utskriven
     */
    public static SSTableColumn<SSProduct> COLUMN_PARCEL = new SSTableColumn<SSProduct>("") {
        @Override
        public Object getValue(SSProduct iProduct) {
            return iProduct.isParcel() ? SSIcon.getIcon("ICON_PARCEL16", SSIcon.IconState.NORMAL ) : null;
        }

        @Override
        public void setValue(SSProduct iProduct, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return ImageIcon.class;
        }

        @Override
        public int getDefaultWidth() {
            return 20;
        }
    };

    /**
     *  Produkt nr
     */
    public static SSTableColumn<SSProduct> COLUMN_NUMBER = new SSTableColumn<SSProduct>(SSBundle.getBundle().getString("producttable.column.1")) {
        @Override
        public Object getValue(SSProduct iProduct) {
            return iProduct.getNumber();
        }

        @Override
        public void setValue(SSProduct iProduct, Object iValue) {
            iProduct.setNumber((String)iValue);

        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 125;
        }
    };


    /**
     *  Produkt nr
     */
    public static SSTableColumn<SSProduct> COLUMN_DESCRIPTION = new SSTableColumn<SSProduct>(SSBundle.getBundle().getString("producttable.column.2")) {
        @Override
        public Object getValue(SSProduct iProduct) {
            return iProduct.getDescription();
        }

        @Override
        public void setValue(SSProduct iProduct, Object iValue) {
            iProduct.setDescription((String)iValue);

        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 225;
        }
    };



    /**
     *  Enhetspris
     */
    public static SSTableColumn<SSProduct> COLUMN_PRICE = new SSTableColumn<SSProduct>(SSBundle.getBundle().getString("producttable.column.3")) {
        @Override
        public Object getValue(SSProduct iProduct) {
            return iProduct.getSellingPrice();
        }

        @Override
        public void setValue(SSProduct iProduct, Object iValue) {
            iProduct.setSellingPrice((BigDecimal)iValue);

        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 70;
        }
    };


    /**
     *  Enhet
     */
    public static SSTableColumn<SSProduct> COLUMN_UNIT = new SSTableColumn<SSProduct>(SSBundle.getBundle().getString("producttable.column.4")) {
        @Override
        public Object getValue(SSProduct iProduct) {
            return iProduct.getUnit();
        }

        @Override
        public void setValue(SSProduct iProduct, Object iValue) {
            iProduct.setUnit((SSUnit)iValue);

        }

        @Override
        public Class getColumnClass() {
            return SSUnit.class;
        }

        @Override
        public int getDefaultWidth() {
            return 70;
        }
    };


    /**
     *  Enhet
     */
    public static SSTableColumn<SSProduct> COLUMN_WAREHOUSE_LOCATION = new SSTableColumn<SSProduct>(SSBundle.getBundle().getString("producttable.column.5")) {
        @Override
        public Object getValue(SSProduct iProduct) {
            return iProduct.getWarehouseLocation();
        }

        @Override
        public void setValue(SSProduct iProduct, Object iValue) {
            iProduct.setWarehouseLocation((String)iValue);

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
     *
     * @param iStock
     * @return
     */
    public static SSTableColumn<SSProduct> getStockQuantityColumn(final SSStock iStock) {
        return new SSTableColumn<SSProduct>(SSBundle.getBundle().getString("producttable.column.6")) {
            @Override
            public Object getValue(SSProduct iProduct) {

                if(iProduct.isParcel()) return null;

                return iStock.getQuantity(iProduct);
            }

            @Override
            public void setValue(SSProduct iObject, Object iValue) {
            }

            @Override
            public Class<?> getColumnClass() {
                return Integer.class;
            }

            @Override
            public int getDefaultWidth() {
                return 90;
            }
        };
    }

    public static SSTableColumn<SSProduct> getStockAvaiableColumn(final SSStock iStock) {
        return new SSTableColumn<SSProduct>(SSBundle.getBundle().getString("producttable.column.7")) {
            @Override
            public Object getValue(SSProduct iProduct) {
                if(iProduct.isParcel()) return null;

                return iStock.getAvaiable(iProduct);
            }

            @Override
            public void setValue(SSProduct iObject, Object iValue) {
            }

            @Override
            public Class<?> getColumnClass() {
                return Integer.class;
            }

            @Override
            public int getDefaultWidth() {
                return 90;
            }
        };
    }



}
