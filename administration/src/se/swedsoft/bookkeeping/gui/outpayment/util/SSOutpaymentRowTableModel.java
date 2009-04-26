package se.swedsoft.bookkeeping.gui.outpayment.util;

import se.swedsoft.bookkeeping.data.SSOutpaymentRow;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellEditor;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellRenderer;
import se.swedsoft.bookkeeping.calc.math.SSOutpaymentMath;

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


    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSOutpaymentRow.class;
    }


    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    public int getRowCount() {
        return super.getRowCount() + 1;
    }


    /**
     * Returns the object at the given row index.
     *
     * @param row The row to get the object from.
     * @return An Object.
     */
    public SSOutpaymentRow getObject(int row) {
        if( row == super.getRowCount()){
            return iEditing;
        } else {
            return super.getObject(row);
        }
    }


    /**
     * This empty implementation is provided so users don't have to implement
     * this method if their data model is not editable.
     *
     * @param aValue      value to assign to cell
     * @param rowIndex    row of cell
     * @param columnIndex column of cell
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);

        if(getObject(rowIndex) == iEditing && aValue != null && !"".equals(aValue)){
            add(iEditing);

            iEditing = new SSOutpaymentRow();
        }

    }

    /**
     * Supplier invoice column
     */
    public static SSTableColumn<SSOutpaymentRow> COLUMN_SUPPLIERINVOICE = new SSTableColumn<SSOutpaymentRow>(SSBundle.getBundle().getString("outpaymentrowtable.column.1")) {
        public Object getValue(SSOutpaymentRow iObject) {
            return iObject.getSupplierInvoice(SSDB.getInstance().getSupplierInvoices());
        }

        public void setValue(SSOutpaymentRow iObject, Object iValue) {
            iObject.setSupplierInvoice((SSSupplierInvoice)iValue);

        }

        public Class getColumnClass() {
            return SSSupplierInvoice.class;
        }

        public int getDefaultWidth() {
            return 80;
        }
    };

    /**
     * Currency column
     */
    public static SSTableColumn<SSOutpaymentRow> COLUMN_CURRENCY = new SSTableColumn<SSOutpaymentRow>(SSBundle.getBundle().getString("outpaymentrowtable.column.2")) {
        public Object getValue(SSOutpaymentRow iObject) {
            return iObject.getInvoiceCurrency();
        }

        public void setValue(SSOutpaymentRow iObject, Object iValue) {
            iObject.setInvoiceCurrency((SSCurrency)iValue);

        }

        public Class getColumnClass() {
            return SSCurrency.class;
        }

        public int getDefaultWidth() {
            return 80;
        }
    };


    /**
     * Currencyrate column
     */
    public static SSTableColumn<SSOutpaymentRow> COLUMN_INVOICE_CURRENCYRATE = new SSTableColumn<SSOutpaymentRow>(SSBundle.getBundle().getString("outpaymentrowtable.column.3")) {
        public Object getValue(SSOutpaymentRow iObject) {
            return iObject.getInvoiceCurrencyRate();
        }

        public void setValue(SSOutpaymentRow iObject, Object iValue) {
            iObject.setCurrencyRate((BigDecimal)iValue);

        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 80;
        }

        /**
         * @return
         */
        public TableCellEditor getCellEditor() {
            return new SSBigDecimalCellEditor(5);
        }

        /**
         * @return
         */
        public TableCellRenderer getCellRenderer() {
            return  new SSBigDecimalCellRenderer(5);
        }
    };

    /**
     * Value column
     */
    public static SSTableColumn<SSOutpaymentRow> COLUMN_VALUE = new SSTableColumn<SSOutpaymentRow>(SSBundle.getBundle().getString("outpaymentrowtable.column.4")) {
        public Object getValue(SSOutpaymentRow iObject) {
            return iObject.getValue();
        }

        public void setValue(SSOutpaymentRow iOutpaymentRow, Object iValue) {
            iOutpaymentRow.setValue((BigDecimal)iValue);
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 130;
        }
    };

    /**
     * Value column
     */
    public static SSTableColumn<SSOutpaymentRow> COLUMN_CURRENCYRATE = new SSTableColumn<SSOutpaymentRow>(SSBundle.getBundle().getString("outpaymentrowtable.column.5")) {
        public Object getValue(SSOutpaymentRow iObject) {
            return iObject.getCurrencyRate();
        }

        public void setValue(SSOutpaymentRow iOutpaymentRow, Object iCurrencyRate) {
            iOutpaymentRow.setCurrencyRate((BigDecimal)iCurrencyRate);
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 80;
        }

        /**
         * @return
         */
        public TableCellEditor getCellEditor() {
            return new SSBigDecimalCellEditor(5);
        }

        /**
         * @return
         */
        public TableCellRenderer getCellRenderer() {
            return  new SSBigDecimalCellRenderer(5);
        }
    };


    /**
     * Payed column
     */
    public static SSTableColumn<SSOutpaymentRow> COLUMN_PAYED = new SSTableColumn<SSOutpaymentRow>(SSBundle.getBundle().getString("outpaymentrowtable.column.6")) {
        public Object getValue(SSOutpaymentRow iObject) {
            return iObject.getLocalValue();
        }

        public void setValue(SSOutpaymentRow iObject, Object iValue) {
            iObject.setLocalValue((BigDecimal)iValue);

        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 130;
        }
    };

    /**
     * Value column
     */
    public static SSTableColumn<SSOutpaymentRow> COLUMN_CURRENCYRATEDIFFERENCE = new SSTableColumn<SSOutpaymentRow>(SSBundle.getBundle().getString("outpaymentrowtable.column.7")) {
        public Object getValue(SSOutpaymentRow iObject) {
            return SSOutpaymentMath.getCurrencyRateDifference(iObject);
        }

        public void setValue(SSOutpaymentRow iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };



}
