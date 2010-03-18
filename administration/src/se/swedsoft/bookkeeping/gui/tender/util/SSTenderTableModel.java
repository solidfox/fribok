package se.swedsoft.bookkeeping.gui.tender.util;

import se.swedsoft.bookkeeping.calc.math.SSTenderMath;
import se.swedsoft.bookkeeping.data.SSTender;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSTenderTableModel  extends SSTableModel<SSTender> {


    /**
     * Default constructor.
     */
    public SSTenderTableModel() {
        super(SSDB.getInstance().getTenders() );
    }

    /**
     * Default constructor.
     * @param iTenders
     */
    public SSTenderTableModel(List<SSTender> iTenders) {
        super(iTenders);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSTender.class;
    }




    /**
     *  Utskriven
     */
    public static SSTableColumn<SSTender> COLUMN_PRINTED = new SSTableColumn<SSTender>("") {
        @Override
        public Object getValue(SSTender iTender) {
            return iTender.isPrinted() ? SSIcon.getIcon("ICON_PROPERTIES16", SSIcon.IconState.NORMAL ) : null;
        }

        @Override
        public void setValue(SSTender iTender, Object iValue) {
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
     *  Ordernummer
     */
    public static SSTableColumn<SSTender> COLUMN_NUMBER = new SSTableColumn<SSTender>(SSBundle.getBundle().getString("tendertable.column.1")) {
        @Override
        public Object getValue(SSTender iTender) {
            return iTender.getNumber();
        }

        @Override
        public void setValue(SSTender iTender, Object iValue) {
            iTender.setNumber((Integer)iValue);

        }

        @Override
        public Class getColumnClass() {
            return Integer.class;
        }

        @Override
        public int getDefaultWidth() {
            return 70;
        }
    };



    /**
     *  Kund nummer
     */
    public static SSTableColumn<SSTender> COLUMN_CUSTOMER_NR = new SSTableColumn<SSTender>(SSBundle.getBundle().getString("tendertable.column.2")) {
        @Override
        public Object getValue(SSTender iInvoice) {
            return iInvoice.getCustomerNr();
        }

        @Override
        public void setValue(SSTender iInvoice, Object iValue) {
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
    public static SSTableColumn<SSTender> COLUMN_CUSTOMER_NAME = new SSTableColumn<SSTender>(SSBundle.getBundle().getString("tendertable.column.3")) {
        @Override
        public Object getValue(SSTender iInvoice) {
            return iInvoice.getCustomerName();
        }

        @Override
        public void setValue(SSTender iInvoice, Object iValue) {
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
    public static SSTableColumn<SSTender> COLUMN_DATE = new SSTableColumn<SSTender>(SSBundle.getBundle().getString("tendertable.column.4")) {
        @Override
        public Object getValue(SSTender iTender) {
            return iTender.getDate();
        }

        @Override
        public void setValue(SSTender iTender, Object iValue) {
            iTender.setDate((Date)iValue);
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
    public static SSTableColumn<SSTender> COLUMN_NET_SUM = new SSTableColumn<SSTender>(SSBundle.getBundle().getString("tendertable.column.5")) {
        @Override
        public Object getValue(SSTender iTender) {
            return SSTenderMath.getNetSum(iTender);
        }

        @Override
        public void setValue(SSTender iTender, Object iValue) {
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
    public static SSTableColumn<SSTender> COLUMN_CURRENCY = new SSTableColumn<SSTender>(SSBundle.getBundle().getString("tendertable.column.6")) {
        @Override
        public Object getValue(SSTender iTender) {
            return iTender.getCurrency();
        }

        @Override
        public void setValue(SSTender iTender, Object iValue) {
            iTender.setCurrency((SSCurrency)iValue);
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
     * Faktura
     */
    public static SSTableColumn<SSTender> COLUMN_ORDER = new SSTableColumn<SSTender>(SSBundle.getBundle().getString("tendertable.column.7")) {
        @Override
        public Object getValue(SSTender iTender) {
            return iTender.getOrderNr();//iTender.getOrder( SSDB.getInstance().getOrders() );
        }

        @Override
        public void setValue(SSTender iTender, Object iValue) {
            //iTender.setOrder((SSOrder)iValue);
        }

        @Override
        public Class getColumnClass() {
            return Integer.class;//SSOrder.class;
        }

        @Override
        public int getDefaultWidth() {
            return 90;
        }
    };

}
