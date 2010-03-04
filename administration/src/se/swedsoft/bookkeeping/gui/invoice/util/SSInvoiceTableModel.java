package se.swedsoft.bookkeeping.gui.invoice.util;

import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSDateCellRenderer;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSCurrencyCellRenderer;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellRenderer;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSInvoiceTableModel extends SSTableModel<SSInvoice>{


    /**
     * Default constructor.
     */
    public SSInvoiceTableModel() {
        super(SSDB.getInstance().getInvoices());
    }

    /**
     * Default constructor.
     */
    public SSInvoiceTableModel(List<SSInvoice> iInvoices) {
        super(iInvoices);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSInvoice.class;
    }

    /**
     *
     * @return
     */
    public static SSInvoiceTableModel getDropDownModel(){
        return getDropDownModel(SSDB.getInstance().getInvoices());
    }

    /**
     *
     * @return
     */
    public static SSInvoiceTableModel getDropDownModel(List<SSInvoice> iInvoices){
        SSInvoiceTableModel iModel = new SSInvoiceTableModel(iInvoices);

        iModel.addColumn( SSInvoiceTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSInvoiceTableModel.COLUMN_CUSTOMER_NR   );
        iModel.addColumn( SSInvoiceTableModel.COLUMN_CUSTOMER_NAME   );

        return iModel;
    }




    /**
     *  Utskriven
     */
    public static SSTableColumn<SSInvoice> COLUMN_PRINTED = new SSTableColumn<SSInvoice>("") {
        @Override
        public Object getValue(SSInvoice iInvoice) {
            return iInvoice.isPrinted() ? SSIcon.getIcon("ICON_PROPERTIES16", SSIcon.IconState.NORMAL ) : null;
        }

        @Override
        public void setValue(SSInvoice iInvoice, Object iValue) {
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
     *  Fakturanr
     */
    public static SSTableColumn<SSInvoice> COLUMN_NUMBER = new SSTableColumn<SSInvoice>(SSBundle.getBundle().getString("invoicetable.column.1")) {
        @Override
        public Object getValue(SSInvoice iInvoice) {
            return iInvoice.getNumber();
        }

        @Override
        public void setValue(SSInvoice iInvoice, Object iValue) {
            iInvoice.setNumber((Integer)iValue);

        }

        @Override
        public Class getColumnClass() {
            return Integer.class;
        }

        @Override
        public int getDefaultWidth() {
            return 80;
        }
    };

    /**
     *  OCR Nummer
     */
    public static SSTableColumn<SSInvoice> COLUMN_OCRNUMBER = new SSTableColumn<SSInvoice>(SSBundle.getBundle().getString("invoicetable.column.13")) {
        @Override
        public Object getValue(SSInvoice iInvoice) {
            return iInvoice.getOCRNumber();
        }

        @Override
        public void setValue(SSInvoice iInvoice, Object iValue) {
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
     *  Fakturayp
     */
    public static SSTableColumn<SSInvoice> COLUMN_TYPE = new SSTableColumn<SSInvoice>(SSBundle.getBundle().getString("invoicetable.column.2")) {
        @Override
        public Object getValue(SSInvoice iInvoice) {
            return iInvoice.getType() != null ? iInvoice.getType().getDescription() : "";
        }

        @Override
        public void setValue(SSInvoice iInvoice, Object iValue) {
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
     *  Kund nummer
     */
    public static SSTableColumn<SSInvoice> COLUMN_CUSTOMER_NR = new SSTableColumn<SSInvoice>(SSBundle.getBundle().getString("invoicetable.column.3")) {
        @Override
        public Object getValue(SSInvoice iInvoice) {
            return iInvoice.getCustomerNr();
        }

        @Override
        public void setValue(SSInvoice iInvoice, Object iValue) {
            iInvoice.setCustomerNr((String)iValue);
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
    public static SSTableColumn<SSInvoice> COLUMN_CUSTOMER_NAME = new SSTableColumn<SSInvoice>(SSBundle.getBundle().getString("invoicetable.column.4")) {
        @Override
        public Object getValue(SSInvoice iInvoice) {
            return iInvoice.getCustomerName();
        }

        @Override
        public void setValue(SSInvoice iInvoice, Object iValue) {
            iInvoice.setCustomerName((String)iValue);
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
     * Datum
     */
    public static SSTableColumn<SSInvoice> COLUMN_DATE = new SSTableColumn<SSInvoice>(SSBundle.getBundle().getString("invoicetable.column.5")) {
        @Override
        public Object getValue(SSInvoice iInvoice) {
            return iInvoice.getDate();
        }

        @Override
        public void setValue(SSInvoice iInvoice, Object iValue) {
            iInvoice.setDate((Date)iValue);
        }

        @Override
        public Class getColumnClass() {
            return Date.class;
        }

        @Override
        public int getDefaultWidth() {
            return 90;
        }
    };




    /**
     * Due date
     */
    public static SSTableColumn<SSInvoice> COLUMN_DUEDATE = new SSTableColumn<SSInvoice>(SSBundle.getBundle().getString("invoicetable.column.6")) {
        @Override
        public Object getValue(SSInvoice iInvoice) {
            return iInvoice.getDueDate();
        }

        @Override
        public void setValue(SSInvoice iInvoice, Object iValue) {
            iInvoice.setDueDate((Date)iValue);
        }

        @Override
        public Class getColumnClass() {
            return Date.class;
        }

        @Override
        public int getDefaultWidth() {
            return 90;
        }
    };

    /**
     * Nettosyumma
     */
    public static SSTableColumn<SSInvoice> COLUMN_NET_SUM = new SSTableColumn<SSInvoice>(SSBundle.getBundle().getString("invoicetable.column.7")) {
        @Override
        public Object getValue(SSInvoice iInvoice) {
            return SSInvoiceMath.getNetSum(iInvoice);
        }

        @Override
        public void setValue(SSInvoice iInvoice, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 90;
        }
    };

    /**
     * Valuta
     */
    public static SSTableColumn<SSInvoice> COLUMN_CURRENCY = new SSTableColumn<SSInvoice>(SSBundle.getBundle().getString("invoicetable.column.8")) {
        @Override
        public Object getValue(SSInvoice iInvoice) {
            return iInvoice.getCurrency();
        }

        @Override
        public void setValue(SSInvoice iInvoice, Object iValue) {
            iInvoice.setCurrency((SSCurrency)iValue);
        }

        @Override
        public Class getColumnClass() {
            return SSCurrency.class;
        }

        @Override
        public int getDefaultWidth() {
            return 50;
        }
    };

    /**
     * Valuta kurs
     */
    public static SSTableColumn<SSInvoice> COLUMN_CURRENCY_RATE = new SSTableColumn<SSInvoice>(SSBundle.getBundle().getString("invoicetable.column.9")) {
        @Override
        public Object getValue(SSInvoice iInvoice) {
            return iInvoice.getCurrencyRate();
        }

        @Override
        public void setValue(SSInvoice iInvoice, Object iValue) {
            iInvoice.setCurrencyRate((BigDecimal)iValue);
        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 60;
        }
    };

    /**
     * Total summa
     */
    public static SSTableColumn<SSInvoice> COLUMN_TOTAL_SUM = new SSTableColumn<SSInvoice>(SSBundle.getBundle().getString("invoicetable.column.10")) {
        @Override
        public Object getValue(SSInvoice iInvoice) {
            return SSInvoiceMath.getTotalSum(iInvoice);
        }

        @Override
        public void setValue(SSInvoice iInvoice, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 90;
        }
    };


    /**
     * Saldo
     */
    /*public static SSTableColumn<SSInvoice> COLUMN_SALDO = new SSTableColumn<SSInvoice>(SSBundle.getBundle().getString("invoicetable.column.11")) {
        public Object getValue(SSInvoice iInvoice) {
            return SSInvoiceMath.getSaldo(iInvoice);
        }

        public void setValue(SSInvoice iInvoice, Object iValue) {

        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 90;
        }
    };  */

    public static SSTableColumn<SSInvoice> getSaldoColumn() {
        return new SSTableColumn<SSInvoice>(SSBundle.getBundle().getString("invoicetable.column.11")) {
            @Override
            public Object getValue(SSInvoice iInvoice) {
                return SSInvoiceMath.getSaldo(iInvoice.getNumber());
            }

            @Override
            public void setValue(SSInvoice iObject, Object iValue) {
            }

            @Override
            public Class<?> getColumnClass() {
                return BigDecimal.class;
            }

            @Override
            public int getDefaultWidth() {
                return 90;
            }
        };
    }

    /**
     * Påminnelser
     */
    public static SSTableColumn<SSInvoice> COLUMN_REMINDERS = new SSTableColumn<SSInvoice>(SSBundle.getBundle().getString("invoicetable.column.12")) {
        @Override
        public Object getValue(SSInvoice iInvoice) {
            return iInvoice.getNumReminders();
        }

        @Override
        public void setValue(SSInvoice iInvoice, Object iValue) {
            iInvoice.setNumRemainders((Integer)iValue);
        }

        @Override
        public Class getColumnClass() {
            return Integer.class;
        }

        @Override
        public int getDefaultWidth() {
            return 50;
        }
    };











}
