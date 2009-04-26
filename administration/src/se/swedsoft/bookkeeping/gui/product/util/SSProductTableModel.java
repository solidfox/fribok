package se.swedsoft.bookkeeping.gui.product.util;

import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSStock;
import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.List;
import java.math.BigDecimal;

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
     */
    public SSProductTableModel(List<SSProduct> iProducts) {
        super(iProducts);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSProduct.class;
    }


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
        public Object getValue(SSProduct iProduct) {
            return iProduct.isParcel() ? SSIcon.getIcon("ICON_PARCEL16", SSIcon.IconState.NORMAL ) : null;
        }

        public void setValue(SSProduct iProduct, Object iValue) {
        }

        public Class getColumnClass() {
            return ImageIcon.class;
        }

        public int getDefaultWidth() {
            return 20;
        }
    };

    /**
     *  Produkt nr
     */
    public static SSTableColumn<SSProduct> COLUMN_NUMBER = new SSTableColumn<SSProduct>(SSBundle.getBundle().getString("producttable.column.1")) {
        public Object getValue(SSProduct iProduct) {
            return iProduct.getNumber();
        }

        public void setValue(SSProduct iProduct, Object iValue) {
            iProduct.setNumber((String)iValue);

        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 125;
        }
    };


    /**
     *  Produkt nr
     */
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
            return 225;
        }
    };



    /**
     *  Enhetspris
     */
    public static SSTableColumn<SSProduct> COLUMN_PRICE = new SSTableColumn<SSProduct>(SSBundle.getBundle().getString("producttable.column.3")) {
        public Object getValue(SSProduct iProduct) {
            return iProduct.getSellingPrice();
        }

        public void setValue(SSProduct iProduct, Object iValue) {
            iProduct.setSellingPrice((BigDecimal)iValue);

        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 70;
        }
    };


    /**
     *  Enhet
     */
    public static SSTableColumn<SSProduct> COLUMN_UNIT = new SSTableColumn<SSProduct>(SSBundle.getBundle().getString("producttable.column.4")) {
        public Object getValue(SSProduct iProduct) {
            return iProduct.getUnit();
        }

        public void setValue(SSProduct iProduct, Object iValue) {
            iProduct.setUnit((SSUnit)iValue);

        }

        public Class getColumnClass() {
            return SSUnit.class;
        }

        public int getDefaultWidth() {
            return 70;
        }
    };


    /**
     *  Enhet
     */
    public static SSTableColumn<SSProduct> COLUMN_WAREHOUSE_LOCATION = new SSTableColumn<SSProduct>(SSBundle.getBundle().getString("producttable.column.5")) {
        public Object getValue(SSProduct iProduct) {
            return iProduct.getWarehouseLocation();
        }

        public void setValue(SSProduct iProduct, Object iValue) {
            iProduct.setWarehouseLocation((String)iValue);

        }

        public Class getColumnClass() {
            return String.class;
        }

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
            public Object getValue(SSProduct iProduct) {

                if(iProduct.isParcel()) return null;

                return iStock.getQuantity(iProduct);
            }

            public void setValue(SSProduct iObject, Object iValue) {
            }

            public Class<?> getColumnClass() {
                return Integer.class;
            }

            public int getDefaultWidth() {
                return 90;
            }
        };
    }

    public static SSTableColumn<SSProduct> getStockAvaiableColumn(final SSStock iStock) {
        return new SSTableColumn<SSProduct>(SSBundle.getBundle().getString("producttable.column.7")) {
            public Object getValue(SSProduct iProduct) {
                if(iProduct.isParcel()) return null;

                return iStock.getAvaiable(iProduct);
            }

            public void setValue(SSProduct iObject, Object iValue) {
            }

            public Class<?> getColumnClass() {
                return Integer.class;
            }

            public int getDefaultWidth() {
                return 90;
            }
        };
    }



}
