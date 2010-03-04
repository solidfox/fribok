package se.swedsoft.bookkeeping.gui.order.util;

import se.swedsoft.bookkeeping.data.SSOrder;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSPurchaseOrder;
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
import se.swedsoft.bookkeeping.calc.math.SSTenderMath;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSOrderMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import java.util.List;
import java.util.Date;
import java.util.Map;
import java.math.BigDecimal;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSOrderTableModel extends SSTableModel<SSOrder> {


    /**
     * Default constructor.
     */
    public SSOrderTableModel() {
        super(SSDB.getInstance().getOrders());
    }

    /**
     * Default constructor.
     */
    public SSOrderTableModel(List<SSOrder> iOrders) {
        super(iOrders);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSOrder.class;
    }




    /**
     *  Utskriven
     */
    public static SSTableColumn<SSOrder> COLUMN_PRINTED = new SSTableColumn<SSOrder>("") {
        @Override
        public Object getValue(SSOrder iOrder) {
            return iOrder.isPrinted() ? SSIcon.getIcon("ICON_PROPERTIES16", SSIcon.IconState.NORMAL ) : null;
        }

        @Override
        public void setValue(SSOrder iInvoice, Object iValue) {
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
    public static SSTableColumn<SSOrder> COLUMN_NUMBER = new SSTableColumn<SSOrder>(SSBundle.getBundle().getString("ordertable.column.1")) {
        @Override
        public Object getValue(SSOrder iOrder) {
            return iOrder.getNumber();
        }

        @Override
        public void setValue(SSOrder iOrder, Object iValue) {
            iOrder.setNumber((Integer)iValue);

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
    public static SSTableColumn<SSOrder> COLUMN_CUSTOMER_NR = new SSTableColumn<SSOrder>(SSBundle.getBundle().getString("ordertable.column.2")) {
        @Override
        public Object getValue(SSOrder iInvoice) {
            return iInvoice.getCustomerNr();
        }

        @Override
        public void setValue(SSOrder iInvoice, Object iValue) {
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
    public static SSTableColumn<SSOrder> COLUMN_CUSTOMER_NAME = new SSTableColumn<SSOrder>(SSBundle.getBundle().getString("ordertable.column.3")) {
        @Override
        public Object getValue(SSOrder iInvoice) {
            return iInvoice.getCustomerName();
        }

        @Override
        public void setValue(SSOrder iInvoice, Object iValue) {
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
    public static SSTableColumn<SSOrder> COLUMN_DATE = new SSTableColumn<SSOrder>(SSBundle.getBundle().getString("ordertable.column.4")) {
        @Override
        public Object getValue(SSOrder iOrder) {
            return iOrder.getDate();
        }

        @Override
        public void setValue(SSOrder iOrder, Object iValue) {
            iOrder.setDate((Date)iValue);
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
    public static SSTableColumn<SSOrder> COLUMN_NET_SUM = new SSTableColumn<SSOrder>(SSBundle.getBundle().getString("ordertable.column.5")) {
        @Override
        public Object getValue(SSOrder iOrder) {
            return SSOrderMath.getNetSum(iOrder);
        }

        @Override
        public void setValue(SSOrder iOrder, Object iValue) {
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
    public static SSTableColumn<SSOrder> COLUMN_CURRENCY = new SSTableColumn<SSOrder>(SSBundle.getBundle().getString("ordertable.column.6")) {
        @Override
        public Object getValue(SSOrder iOrder) {
            return iOrder.getCurrency();
        }

        @Override
        public void setValue(SSOrder iOrder, Object iValue) {
            iOrder.setCurrency((SSCurrency)iValue);
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
     * Beräknad leverans
     */
    public static SSTableColumn<SSOrder> COLUMN_ESTIMATED_DELIVERY = new SSTableColumn<SSOrder>(SSBundle.getBundle().getString("ordertable.column.7")) {
        @Override
        public Object getValue(SSOrder iOrder) {
            return iOrder.getEstimatedDelivery();
        }

        @Override
        public void setValue(SSOrder iOrder, Object iValue) {
            iOrder.setEstimatedDelivery((String)iValue);
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
     * Faktura
     */
    public static SSTableColumn<SSOrder> COLUMN_INVOICE = new SSTableColumn<SSOrder>(SSBundle.getBundle().getString("ordertable.column.8")) {
        @Override
        public Object getValue(SSOrder iOrder) {
            if(iOrder.getInvoiceNr() != null)
                return iOrder.getInvoiceNr().toString();
            else if(iOrder.getPeriodicInvoiceNr() != null)
                return "P" + iOrder.getPeriodicInvoiceNr().toString();
            else
                return null;
        }

        @Override
        public void setValue(SSOrder iOrder, Object iValue) {
            //iOrder.setInvoice((SSInvoice)iValue);
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 90;
        }
    };


    /**
     * Inköpsorder
     */
    public static SSTableColumn<SSOrder> COLUMN_PURCHASEORDER = new SSTableColumn<SSOrder>(SSBundle.getBundle().getString("ordertable.column.9")) {
        @Override
        public Object getValue(SSOrder iOrder) {
            return iOrder.getPurchaseOrderNr();
        }

        @Override
        public void setValue(SSOrder iOrder, Object iValue) {
            //iOrder.setPurchaseOrder((SSPurchaseOrder)iValue);
        }

        @Override
        public Class getColumnClass() {
            return Integer.class;
        }

        @Override
        public int getDefaultWidth() {
            return 90;
        }
    };

    /**
     *
     * @param iOrdersWithoutInvoice
     * @return
     */
    public static SSOrderTableModel getDropdownModel(boolean iOrdersWithoutInvoice){
        List<SSOrder> iOrders = SSDB.getInstance().getOrders();
        if(iOrdersWithoutInvoice){
            iOrders = SSOrderMath.getOrdersWithoutInvoice(iOrders);
        }

        SSOrderTableModel iModel = new SSOrderTableModel(iOrders);

        iModel.addColumn( COLUMN_NUMBER );
        iModel.addColumn( COLUMN_CUSTOMER_NR );
        iModel.addColumn( COLUMN_CUSTOMER_NAME );

        return iModel;
    }

    /**
     *
     * @return
     */
    public static SSOrderTableModel getDropdownModel(){
        SSOrderTableModel iModel = new SSOrderTableModel();

        iModel.addColumn( COLUMN_NUMBER );
        iModel.addColumn( COLUMN_CUSTOMER_NR );
        iModel.addColumn( COLUMN_CUSTOMER_NAME );

        return iModel;
    }

}
