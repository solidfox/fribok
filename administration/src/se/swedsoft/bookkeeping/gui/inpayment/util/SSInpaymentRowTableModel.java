package se.swedsoft.bookkeeping.gui.inpayment.util;

import se.swedsoft.bookkeeping.calc.math.SSInpaymentMath;
import se.swedsoft.bookkeeping.data.SSInpaymentRow;
import se.swedsoft.bookkeeping.data.SSInvoice;
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
public class SSInpaymentRowTableModel extends SSTableModel<SSInpaymentRow> {


    private SSInpaymentRow iEditing;


    /**
     * Default constructor.
     */
    public SSInpaymentRowTableModel() {
        iEditing = new SSInpaymentRow();
    }


    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSInpaymentRow.class;
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
    @Override
    public int getRowCount() {
        return super.getRowCount() + 1;
    }


    /**
     * Returns the object at the given row index.
     *
     * @param row The row to get the object from.
     * @return An Object.
     */
    @Override
    public SSInpaymentRow getObject(int row) {
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
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);

        if(getObject(rowIndex) == iEditing && aValue != null && !"".equals(aValue)){
            add(iEditing);

            iEditing = new SSInpaymentRow();
        }

    }

    /**
     * Supplier invoice column
     */
    public static SSTableColumn<SSInpaymentRow> COLUMN_INVOICE = new SSTableColumn<SSInpaymentRow>(SSBundle.getBundle().getString("outpaymentrowtable.column.1")) {
        @Override
        public Object getValue(SSInpaymentRow iObject) {
            return iObject.getInvoice(SSDB.getInstance().getInvoices());
        }

        @Override
        public void setValue(SSInpaymentRow iObject, Object iValue) {
            iObject.setInvoice((SSInvoice)iValue);
        }

        @Override
        public Class getColumnClass() {
            return SSInvoice.class;
        }

        @Override
        public int getDefaultWidth() {
            return 80;
        }
    };

    /**
     * Currency column
     */
    public static SSTableColumn<SSInpaymentRow> COLUMN_CURRENCY = new SSTableColumn<SSInpaymentRow>(SSBundle.getBundle().getString("outpaymentrowtable.column.2")) {
        @Override
        public Object getValue(SSInpaymentRow iObject) {
            return iObject.getInvoiceCurrency();
        }

        @Override
        public void setValue(SSInpaymentRow iObject, Object iValue) {
            iObject.setInvoiceCurrency((SSCurrency)iValue);
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
    public static SSTableColumn<SSInpaymentRow> COLUMN_INVOICE_CURRENCYRATE = new SSTableColumn<SSInpaymentRow>(SSBundle.getBundle().getString("outpaymentrowtable.column.3")) {
        @Override
        public Object getValue(SSInpaymentRow iObject) {
            return iObject.getInvoiceCurrencyRate();
        }

        @Override
        public void setValue(SSInpaymentRow iObject, Object iValue) {
            iObject.setCurrencyRate((BigDecimal)iValue);

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
    public static SSTableColumn<SSInpaymentRow> COLUMN_VALUE = new SSTableColumn<SSInpaymentRow>(SSBundle.getBundle().getString("outpaymentrowtable.column.4")) {
        @Override
        public Object getValue(SSInpaymentRow iObject) {
            return iObject.getValue();
        }

        @Override
        public void setValue(SSInpaymentRow iOutpaymentRow, Object iValue) {
            iOutpaymentRow.setValue((BigDecimal)iValue);
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
    public static SSTableColumn<SSInpaymentRow> COLUMN_CURRENCYRATE = new SSTableColumn<SSInpaymentRow>(SSBundle.getBundle().getString("outpaymentrowtable.column.5")) {
        @Override
        public Object getValue(SSInpaymentRow iObject) {
            return iObject.getCurrencyRate();
        }

        @Override
        public void setValue(SSInpaymentRow iOutpaymentRow, Object iCurrencyRate) {
            iOutpaymentRow.setCurrencyRate((BigDecimal)iCurrencyRate);
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
    public static SSTableColumn<SSInpaymentRow> COLUMN_PAYED = new SSTableColumn<SSInpaymentRow>(SSBundle.getBundle().getString("outpaymentrowtable.column.6")) {
        @Override
        public Object getValue(SSInpaymentRow iObject) {
            return iObject.getLocalValue();
        }

        @Override
        public void setValue(SSInpaymentRow iObject, Object iValue) {
            iObject.setLocalValue((BigDecimal)iValue);

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
    public static SSTableColumn<SSInpaymentRow> COLUMN_CURRENCYRATEDIFFERENCE = new SSTableColumn<SSInpaymentRow>(SSBundle.getBundle().getString("outpaymentrowtable.column.7")) {
        @Override
        public Object getValue(SSInpaymentRow iObject) {
            return SSInpaymentMath.getCurrencyRateDifference(iObject);
        }

        @Override
        public void setValue(SSInpaymentRow iObject, Object iValue) {
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








}
