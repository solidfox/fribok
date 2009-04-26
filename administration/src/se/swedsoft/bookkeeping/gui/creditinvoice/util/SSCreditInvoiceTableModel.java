package se.swedsoft.bookkeeping.gui.creditinvoice.util;

import se.swedsoft.bookkeeping.data.SSCreditInvoice;
import se.swedsoft.bookkeeping.data.SSCreditInvoice;
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
import se.swedsoft.bookkeeping.calc.math.SSCreditInvoiceMath;

import javax.swing.*;
import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSCreditInvoiceTableModel  extends SSTableModel<SSCreditInvoice> {


    /**
     * Default constructor.
     */
    public SSCreditInvoiceTableModel() {
        super(SSDB.getInstance().getCreditInvoices());
    }

    /**
     * Default constructor.
     */
    public SSCreditInvoiceTableModel(List<SSCreditInvoice> iCreditInvoices) {
        super(iCreditInvoices);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSCreditInvoice.class;
    }

    /**
     *
     * @return
     */
    public static SSCreditInvoiceTableModel getDropDownModel(){
        SSCreditInvoiceTableModel iModel = new SSCreditInvoiceTableModel();

        iModel.addColumn( SSCreditInvoiceTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSCreditInvoiceTableModel.COLUMN_CUSTOMER_NR   );
        iModel.addColumn( SSCreditInvoiceTableModel.COLUMN_CUSTOMER_NAME   );

        return iModel;
    }



    /**
     *  Utskriven
     */
    public static SSTableColumn<SSCreditInvoice> COLUMN_PRINTED = new SSTableColumn<SSCreditInvoice>("") {
        public Object getValue(SSCreditInvoice iCreditInvoice) {
            return iCreditInvoice.isPrinted() ? SSIcon.getIcon("ICON_PROPERTIES16", SSIcon.IconState.NORMAL ) : null;
        }

        public void setValue(SSCreditInvoice iInvoice, Object iValue) {
        }

        public Class getColumnClass() {
            return ImageIcon.class;
        }

        public int getDefaultWidth() {
            return 20;
        }
    };

    /**
     *  Fakturanr
     */
    public static SSTableColumn<SSCreditInvoice> COLUMN_NUMBER = new SSTableColumn<SSCreditInvoice>(SSBundle.getBundle().getString("creditinvoicetable.column.1")) {
        public Object getValue(SSCreditInvoice iCreditInvoice) {
            return iCreditInvoice.getNumber();
        }

        public void setValue(SSCreditInvoice iCreditInvoice, Object iValue) {
            iCreditInvoice.setNumber((Integer)iValue);

        }

        public Class getColumnClass() {
            return Integer.class;
        }

        public int getDefaultWidth() {
            return 80;
        }
    };


    /**
     *  Kund nummer
     */
    public static SSTableColumn<SSCreditInvoice> COLUMN_CUSTOMER_NR = new SSTableColumn<SSCreditInvoice>(SSBundle.getBundle().getString("creditinvoicetable.column.2")) {
        public Object getValue(SSCreditInvoice iCreditInvoice) {
            return iCreditInvoice.getCustomerNr();
        }

        public void setValue(SSCreditInvoice iCreditInvoice, Object iValue) {
            iCreditInvoice.setCustomerNr((String)iValue);
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };
    /**
     *  Kund namn
     */
    public static SSTableColumn<SSCreditInvoice> COLUMN_CUSTOMER_NAME = new SSTableColumn<SSCreditInvoice>(SSBundle.getBundle().getString("creditinvoicetable.column.3")) {
        public Object getValue(SSCreditInvoice iCreditInvoice) {
            return iCreditInvoice.getCustomerName();
        }

        public void setValue(SSCreditInvoice iCreditInvoice, Object iValue) {
            iCreditInvoice.setCustomerName((String)iValue);
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 150;
        }
    };



    /**
     * Datum
     */
    public static SSTableColumn<SSCreditInvoice> COLUMN_DATE = new SSTableColumn<SSCreditInvoice>(SSBundle.getBundle().getString("creditinvoicetable.column.4")) {
        public Object getValue(SSCreditInvoice iCreditInvoice) {
            return iCreditInvoice.getDate();
        }

        public void setValue(SSCreditInvoice iCreditInvoice, Object iValue) {
            iCreditInvoice.setDate((Date)iValue);
        }

        public Class getColumnClass() {
            return Date.class;
        }

        public int getDefaultWidth() {
            return 90;
        }
    };


    /**
     * krediterar faktura
     */
    public static SSTableColumn<SSCreditInvoice> COLUMN_CREDITING = new SSTableColumn<SSCreditInvoice>(SSBundle.getBundle().getString("creditinvoicetable.column.5")) {
        public Object getValue(SSCreditInvoice iCreditInvoice) {
            return iCreditInvoice.getCreditingNr();
        }

        public void setValue(SSCreditInvoice iCreditInvoice, Object iValue) {

        }

        public Class getColumnClass() {
            return Integer.class;
        }

        public int getDefaultWidth() {
            return 115;
        }
    };


    /**
     * Nettosuumma
     */
    public static SSTableColumn<SSCreditInvoice> COLUMN_NET_SUM = new SSTableColumn<SSCreditInvoice>(SSBundle.getBundle().getString("creditinvoicetable.column.6")) {
        public Object getValue(SSCreditInvoice iCreditInvoice) {
            return SSCreditInvoiceMath.getNetSum(iCreditInvoice);
        }

        public void setValue(SSCreditInvoice iCreditInvoice, Object iValue) {
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 90;
        }
    };

    /**
     * Valuta
     */
    public static SSTableColumn<SSCreditInvoice> COLUMN_CURRENCY = new SSTableColumn<SSCreditInvoice>(SSBundle.getBundle().getString("creditinvoicetable.column.7")) {
        public Object getValue(SSCreditInvoice iCreditInvoice) {
            return iCreditInvoice.getCurrency();
        }

        public void setValue(SSCreditInvoice iCreditInvoice, Object iValue) {
            iCreditInvoice.setCurrency((SSCurrency)iValue);
        }

        public Class getColumnClass() {
            return SSCurrency.class;
        }

        public int getDefaultWidth() {
            return 50;
        }
    };


    /**
     * Total summa
     */
    public static SSTableColumn<SSCreditInvoice> COLUMN_TOTAL_SUM = new SSTableColumn<SSCreditInvoice>(SSBundle.getBundle().getString("creditinvoicetable.column.8")) {
        public Object getValue(SSCreditInvoice iCreditInvoice) {
            return SSCreditInvoiceMath.getTotalSum(iCreditInvoice);
        }

        public void setValue(SSCreditInvoice iCreditInvoice, Object iValue) {
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 90;
        }
    };



}
