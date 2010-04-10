package se.swedsoft.bookkeeping.gui.product.util;


import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSProductRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBoxOld;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellRenderer;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSDefaultTableCellRenderer;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;


/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 12:44:47
 */
public class SSProductRowTableModel extends SSDefaultTableModel<SSProductRow> {

    private SSProductRow iEditingRow;

    /**
     * Default constructor.
     */
    public SSProductRowTableModel() {
        this(new LinkedList<SSProductRow>());
    }

    /**
     * Constructor.
     *
     * @param pObjects The data for the table model.
     */
    public SSProductRowTableModel(List<SSProductRow> pObjects) {
        super(pObjects);
        iEditingRow = new SSProductRow();

        addColumn(SSBundle.getBundle().getString("productrowtable.column.1"));
        addColumn(SSBundle.getBundle().getString("productrowtable.column.2"));
        addColumn(SSBundle.getBundle().getString("productrowtable.column.3"));
        addColumn(SSBundle.getBundle().getString("productrowtable.column.4"));

    }

    @Override
    public Class getType() {
        return SSProduct.class;
    }

    /**
     * Returns the value for the cell at {@code columnIndex} and
     * {@code rowIndex}.
     *    producttable.column.1=Produkt nr
     *    producttable.column.2=Beskrivning
     *    producttable.column.3=Enhets pris
     *    producttable.column.4=Lagerplats
     *    producttable.column.5=I lager
     *    producttable.column.6=Disponibelt
     *
     * @param    rowIndex    the row whose value is to be queried
     * @param    columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        SSProductRow iRow = getObject(rowIndex);
        SSProduct    iProduct = iRow.getProduct(SSDB.getInstance().getProducts());

        Object value = null;

        switch (columnIndex) {
        case 0:
            value = iProduct;
            break;

        case 1:
            value = iRow.getDescription();
            break;

        case 2:
            value = iRow.getQuantity();
            break;

        case 3:
            value = iProduct == null ? null : iProduct.getUnit();
            break;
        }

        return value;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        SSProductRow iRow = getObject(rowIndex);

        switch (columnIndex) {
        case 0:
            iRow.setProduct((SSProduct) aValue);
            break;

        case 1:
            iRow.setDescription((String) aValue);
            break;

        case 2:
            iRow.setQuantity((Integer) aValue);
            break;

        case 3:
            break;
        }

        if (iRow == iEditingRow && aValue != null && !"".equals(aValue)) {
            add(iEditingRow);
            iEditingRow = new SSProductRow();
        }

    }

    @Override
    public int getRowCount() {
        return super.getRowCount() + 1;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
        case 0:
            return SSProduct.class;

        case 1:
            return String.class;

        case 2:
            return Integer.class;

        case 3:
            return String.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        SSProduct iProduct = getObject(rowIndex).getProduct(
                SSDB.getInstance().getProducts());

        return (columnIndex == 0) || (iProduct != null && (columnIndex != 3));
    }

    @Override
    public SSProductRow getObject(int row) {
        if (row == super.getRowCount()) {
            return iEditingRow;
        } else {
            return super.getObject(row);
        }
    }

    /**
     * @param iObject
     */
    @Override
    public void deleteRow(SSProductRow iObject) {
        if (iObject == iEditingRow) {
            iEditingRow = new SSProductRow();

            fireTableDataChanged();
        } else {
            super.deleteRow(iObject);
        }
    }

    /**
     *
     * @param iTable
     * @param iProduct
     */
    public static void setupTable(final SSTable iTable, SSProduct iProduct) {
        iTable.setColumnSortingEnabled(false);

        // iTable.setSingleSelect();
        iTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        iTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        iTable.getColumnModel().getColumn(2).setPreferredWidth(70);
        iTable.getColumnModel().getColumn(3).setPreferredWidth(70);

        iTable.setDefaultRenderer(BigDecimal.class, new SSBigDecimalCellRenderer(2));

        iTable.setDefaultEditor(SSProduct.class,
                new SSProductEditor(SSDB.getInstance().getProducts(), iProduct));
        iTable.setDefaultRenderer(SSProduct.class, new SSProductRenderer());
    }

    private static class SSProductRenderer extends SSDefaultTableCellRenderer<SSProduct> {

        /**
         * Sets the {@code String} object for the cell being rendered to
         * {@code value}.
         *
         * @param value the string value for this cell; if value is
         *              {@code null} it sets the text value to an empty string
         * @see JLabel#setText
         */
        @Override
        protected void setValue(SSProduct value) {
            if (value != null) {
                super.setValue(value.getNumber());
            } else {
                super.setValue("");
            }
        }

    }


    private static class SSProductEditor extends SSTableComboBoxOld.CellEditor<SSProduct> {

        /**
         *
         * @param iProducts
         * @param iExcluded
         */
        public SSProductEditor(List<SSProduct> iProducts, SSProduct iExcluded) {
            List<SSProduct> iFiltered = new LinkedList<SSProduct>();

            for (SSProduct iProduct : iProducts) {

                if (iExcluded != iProduct) {
                    iFiltered.add(iProduct);
                }

            }

            SSDefaultTableModel< SSProduct> model = new SSDefaultTableModel<SSProduct>(
                    iFiltered) {

                @Override
                public Class getType() {
                    return SSProduct.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    SSProduct iProduct = getObject(rowIndex);

                    Object value = null;

                    switch (columnIndex) {
                    case 0:
                        value = iProduct.getNumber();
                        break;

                    case 1:
                        value = iProduct.getDescription();
                        break;
                    }

                    return value;
                }
            };

            model.addColumn(SSBundle.getBundle().getString("producttable.column.1"));
            model.addColumn(SSBundle.getBundle().getString("producttable.column.2"));

            setModel(model);
            setSearchColumns(0, 1);
            setPopupSize(360, 200);
            setColumnWidths(60, 300);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.product.util.SSProductRowTableModel");
        sb.append("{iEditingRow=").append(iEditingRow);
        sb.append('}');
        return sb.toString();
    }
}
