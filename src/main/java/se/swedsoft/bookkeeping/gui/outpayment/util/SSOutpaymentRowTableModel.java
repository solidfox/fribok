package se.swedsoft.bookkeeping.gui.outpayment.util;


import se.swedsoft.bookkeeping.calc.math.SSOutpaymentMath;
import se.swedsoft.bookkeeping.data.SSOutpaymentRow;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellEditor;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellRenderer;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.math.BigDecimal;


/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSOutpaymentRowTableModel extends SSTableModel<SSOutpaymentRow> {

    private SSOutpaymentRow iEditing;

    /**
     * Default constructor.
     */
    public SSOutpaymentRowTableModel() {
        iEditing = new SSOutpaymentRow();
    }

    @Override
    public Class getType() {
        return SSOutpaymentRow.class;
    }

    @Override
    public int getRowCount() {
        return super.getRowCount() + 1;
    }

    @Override
    public SSOutpaymentRow getObject(int row) {
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

            iEditing = new SSOutpaymentRow();
        }

    }

    /**
     * Supplier invoice column
     */
    public static SSTableColumn<SSOutpaymentRow> COLUMN_SUPPLIERINVOICE = new SSTableColumn<SSOutpaymentRow>(
            SSBundle.getBundle().getString("outpaymentrowtable.column.1")) {
        @Override
        public Object getValue(SSOutpaymentRow iObject) {
            return iObject.getSupplierInvoice(SSDB.getInstance().getSupplierInvoices());
        }

        @Override
        public void setValue(SSOutpaymentRow iObject, Object iValue) {
            iObject.setSupplierInvoice((SSSupplierInvoice) iValue);

        }

        @Override
        public Class getColumnClass() {
            return SSSupplierInvoice.class;
        }

        @Override
        public int getDefaultWidth() {
            return 80;
        }
    };

    /**
     * Currency column
     */
    public static SSTableColumn<SSOutpaymentRow> COLUMN_CURRENCY = new SSTableColumn<SSOutpaymentRow>(
            SSBundle.getBundle().getString("outpaymentrowtable.column.2")) {
        @Override
        public Object getValue(SSOutpaymentRow iObject) {
            return iObject.getInvoiceCurrency();
        }

        @Override
        public void setValue(SSOutpaymentRow iObject, Object iValue) {
            iObject.setInvoiceCurrency((SSCurrency) iValue);

        }

        @Override
        public Class getColumnClass() {
            return SSCurrency.class;
        }

        @Override
        public int getDefaultWidth() {
            return 80;
        }
    };

    /**
     * Currencyrate column
     */
    public static SSTableColumn<SSOutpaymentRow> COLUMN_INVOICE_CURRENCYRATE = new SSTableColumn<SSOutpaymentRow>(
            SSBundle.getBundle().getString("outpaymentrowtable.column.3")) {
        @Override
        public Object getValue(SSOutpaymentRow iObject) {
            return iObject.getInvoiceCurrencyRate();
        }

        @Override
        public void setValue(SSOutpaymentRow iObject, Object iValue) {
            iObject.setCurrencyRate((BigDecimal) iValue);

        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 80;
        }

        /**
         * @return
         */
        @Override
        public TableCellEditor getCellEditor() {
            return new SSBigDecimalCellEditor(5);
        }

        /**
         * @return
         */
        @Override
        public TableCellRenderer getCellRenderer() {
            return  new SSBigDecimalCellRenderer(5);
        }
    };

    /**
     * Value column
     */
    public static SSTableColumn<SSOutpaymentRow> COLUMN_VALUE = new SSTableColumn<SSOutpaymentRow>(
            SSBundle.getBundle().getString("outpaymentrowtable.column.4")) {
        @Override
        public Object getValue(SSOutpaymentRow iObject) {
            return iObject.getValue();
        }

        @Override
        public void setValue(SSOutpaymentRow iOutpaymentRow, Object iValue) {
            iOutpaymentRow.setValue((BigDecimal) iValue);
        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 130;
        }
    };

    /**
     * Value column
     */
    public static SSTableColumn<SSOutpaymentRow> COLUMN_CURRENCYRATE = new SSTableColumn<SSOutpaymentRow>(
            SSBundle.getBundle().getString("outpaymentrowtable.column.5")) {
        @Override
        public Object getValue(SSOutpaymentRow iObject) {
            return iObject.getCurrencyRate();
        }

        @Override
        public void setValue(SSOutpaymentRow iOutpaymentRow, Object iCurrencyRate) {
            iOutpaymentRow.setCurrencyRate((BigDecimal) iCurrencyRate);
        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 80;
        }

        /**
         * @return
         */
        @Override
        public TableCellEditor getCellEditor() {
            return new SSBigDecimalCellEditor(5);
        }

        /**
         * @return
         */
        @Override
        public TableCellRenderer getCellRenderer() {
            return  new SSBigDecimalCellRenderer(5);
        }
    };

    /**
     * Payed column
     */
    public static SSTableColumn<SSOutpaymentRow> COLUMN_PAYED = new SSTableColumn<SSOutpaymentRow>(
            SSBundle.getBundle().getString("outpaymentrowtable.column.6")) {
        @Override
        public Object getValue(SSOutpaymentRow iObject) {
            return iObject.getLocalValue();
        }

        @Override
        public void setValue(SSOutpaymentRow iObject, Object iValue) {
            iObject.setLocalValue((BigDecimal) iValue);

        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 130;
        }
    };

    /**
     * Value column
     */
    public static SSTableColumn<SSOutpaymentRow> COLUMN_CURRENCYRATEDIFFERENCE = new SSTableColumn<SSOutpaymentRow>(
            SSBundle.getBundle().getString("outpaymentrowtable.column.7")) {
        @Override
        public Object getValue(SSOutpaymentRow iObject) {
            return SSOutpaymentMath.getCurrencyRateDifference(iObject);
        }

        @Override
        public void setValue(SSOutpaymentRow iObject, Object iValue) {}

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

        sb.append("se.swedsoft.bookkeeping.gui.outpayment.util.SSOutpaymentRowTableModel");
        sb.append("{iEditing=").append(iEditing);
        sb.append('}');
        return sb.toString();
    }
}
