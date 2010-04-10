package se.swedsoft.bookkeeping.gui.purchaseorder.util;


import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSPurchaseOrderRow;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import java.math.BigDecimal;


/**
 * User: Andreas Lago
 * Date: 2006-jun-15
 * Time: 13:07:54
 */
public class SSPurchaseOrderRowTableModel extends SSTableModel<SSPurchaseOrderRow> {

    private SSPurchaseOrderRow iEditing;

    /**
     * Default constructor.
     */
    public SSPurchaseOrderRowTableModel() {
        iEditing = new SSPurchaseOrderRow();
    }

    @Override
    public Class getType() {
        return SSPurchaseOrderRow.class;
    }

    @Override
    public int getRowCount() {
        return super.getRowCount() + 1;
    }

    @Override
    public SSPurchaseOrderRow getObject(int row) {
        if (row == super.getRowCount()) {
            return iEditing;
        } else {
            return super.getObject(row);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);

        if (getObject(rowIndex) == iEditing && aValue != null && !"".equals(aValue)) {
            add(iEditing);

            iEditing = new SSPurchaseOrderRow();
        }

    }

    /**
     * Number column
     */
    public static SSTableColumn<SSPurchaseOrderRow> COLUMN_PRODUCT = new SSTableColumn<SSPurchaseOrderRow>(
            "Produkt nr") {
        @Override
        public Object getValue(SSPurchaseOrderRow iObject) {
            SSProduct iProduct = iObject.getProduct(SSDB.getInstance().getProducts());

            return iProduct != null ? iProduct : iObject.getProductNr();

        }

        @Override
        public void setValue(SSPurchaseOrderRow iObject, Object iValue) {
            if (iValue instanceof SSProduct) {
                iObject.setProduct((SSProduct) iValue);
            } else {
                iObject.setProductNr((String) iValue);

                SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

                if (iObject.getUnit() == null) {
                    iObject.setUnit(iCompany.getStandardUnit());
                }
            }

        }

        @Override
        public Class getColumnClass() {
            return SSProduct.class;
        }

        @Override
        public int getDefaultWidth() {
            return 150;
        }
    };

    /**
     * Date column
     */
    public static SSTableColumn<SSPurchaseOrderRow> COLUMN_DESCRIPTION = new SSTableColumn<SSPurchaseOrderRow>(
            "Beskrivning") {
        @Override
        public Object getValue(SSPurchaseOrderRow iObject) {
            return iObject.getDescription();
        }

        @Override
        public void setValue(SSPurchaseOrderRow iObject, Object iValue) {
            iObject.setDescription((String) iValue);

            SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

            if (iObject.getUnit() == null) {
                iObject.setUnit(iCompany.getStandardUnit());
            }
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

    /**
     * Supplier name
     */
    public static SSTableColumn<SSPurchaseOrderRow> COLUMN_SUPPLIER_ARTICLE_NR = new SSTableColumn<SSPurchaseOrderRow>(
            "Lev art nr") {
        @Override
        public Object getValue(SSPurchaseOrderRow iObject) {
            return iObject.getSupplierArticleNr();
        }

        @Override
        public void setValue(SSPurchaseOrderRow iObject, Object iValue) {
            iObject.setSupplierArticleNr((String) iValue);
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

    /**
     * Supplier nr
     */
    public static SSTableColumn<SSPurchaseOrderRow> COLUMN_UNITPRICE = new SSTableColumn<SSPurchaseOrderRow>(
            "Inköpspris") {
        @Override
        public Object getValue(SSPurchaseOrderRow iObject) {
            return iObject.getUnitPrice();
        }

        @Override
        public void setValue(SSPurchaseOrderRow iObject, Object iValue) {
            iObject.setUnitPrice((BigDecimal) iValue);
        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };

    /**
     * Supplier nr
     */
    public static SSTableColumn<SSPurchaseOrderRow> COLUMN_QUANTITY = new SSTableColumn<SSPurchaseOrderRow>(
            "Antal") {
        @Override
        public Object getValue(SSPurchaseOrderRow iObject) {
            return iObject.getQuantity();
        }

        @Override
        public void setValue(SSPurchaseOrderRow iObject, Object iValue) {
            iObject.setQuantity((Integer) iValue);
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
     * Supplier nr
     */
    public static SSTableColumn<SSPurchaseOrderRow> COLUMN_UNIT = new SSTableColumn<SSPurchaseOrderRow>(
            "Enhet") {
        @Override
        public Object getValue(SSPurchaseOrderRow iObject) {
            return iObject.getUnit();
        }

        @Override
        public void setValue(SSPurchaseOrderRow iObject, Object iValue) {
            iObject.setUnit((SSUnit) iValue);
        }

        @Override
        public Class getColumnClass() {
            return SSUnit.class;
        }

        @Override
        public int getDefaultWidth() {
            return 60;
        }
    };

    /**
     * Supplier nr
     */
    public static SSTableColumn<SSPurchaseOrderRow> COLUMN_SUM = new SSTableColumn<SSPurchaseOrderRow>(
            "Summa") {
        @Override
        public Object getValue(SSPurchaseOrderRow iObject) {
            return iObject.getSum();
        }

        @Override
        public void setValue(SSPurchaseOrderRow iObject, Object iValue) {}

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(
                "se.swedsoft.bookkeeping.gui.purchaseorder.util.SSPurchaseOrderRowTableModel");
        sb.append("{iEditing=").append(iEditing);
        sb.append('}');
        return sb.toString();
    }
}
