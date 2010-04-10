/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.voucher.util;


import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Date: 2006-feb-07
 * Time: 09:14:14
 */

public class SSVoucherTableModel extends SSTableModel<SSVoucher> {

    /**
     * Default constructor.
     */
    public SSVoucherTableModel() {
        super(SSDB.getInstance().getVouchers());
    }

    /**
     * Default constructor.
     * @param iVouchers
     */
    public SSVoucherTableModel(List<SSVoucher> iVouchers) {
        super(iVouchers);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSVoucher.class;
    }

    /**
     *
     * @return
     */
    public static SSVoucherTableModel getDropDownModel() {
        SSVoucherTableModel iModel = new SSVoucherTableModel();

        iModel.addColumn(COLUMN_NUMBER);
        iModel.addColumn(COLUMN_DESCRIPTION);

        return iModel;
    }

    /**
     *  Fakturanr
     */
    public static SSTableColumn<SSVoucher> COLUMN_NUMBER = new SSTableColumn<SSVoucher>(
            SSBundle.getBundle().getString("vouchertable.column.1")) {
        @Override
        public Object getValue(SSVoucher iVoucher) {
            return iVoucher.getNumber();
        }

        @Override
        public void setValue(SSVoucher iVoucher, Object iValue) {
            iVoucher.setNumber((Integer) iValue);

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
     *  Fakturanr
     */
    public static SSTableColumn<SSVoucher> COLUMN_DATE = new SSTableColumn<SSVoucher>(
            SSBundle.getBundle().getString("vouchertable.column.2")) {
        @Override
        public Object getValue(SSVoucher iVoucher) {
            return iVoucher.getDate();
        }

        @Override
        public void setValue(SSVoucher iVoucher, Object iValue) {
            iVoucher.setDate((Date) iValue);

        }

        @Override
        public Class getColumnClass() {
            return Date.class;
        }

        @Override
        public int getDefaultWidth() {
            return 85;
        }
    };

    /**
     *  Fakturanr
     */
    public static SSTableColumn<SSVoucher> COLUMN_DESCRIPTION = new SSTableColumn<SSVoucher>(
            SSBundle.getBundle().getString("vouchertable.column.3")) {
        @Override
        public Object getValue(SSVoucher iVoucher) {
            return iVoucher.getDescription();
        }

        @Override
        public void setValue(SSVoucher iVoucher, Object iValue) {
            iVoucher.setDescription((String) iValue);

        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 300;
        }
    };

    /**
     *  Fakturanr
     */
    public static SSTableColumn<SSVoucher> COLUMN_CORRECTS = new SSTableColumn<SSVoucher>(
            SSBundle.getBundle().getString("vouchertable.column.4")) {
        @Override
        public Object getValue(SSVoucher iVoucher) {
            return iVoucher.getCorrects();
        }

        @Override
        public void setValue(SSVoucher iVoucher, Object iValue) {
            iVoucher.setCorrects((SSVoucher) iValue);

        }

        @Override
        public Class getColumnClass() {
            return SSVoucher.class;
        }

        @Override
        public int getDefaultWidth() {
            return 80;
        }
    };

    /**
     *  Fakturanr
     */
    public static SSTableColumn<SSVoucher> COLUMN_CORRECTEDBY = new SSTableColumn<SSVoucher>(
            SSBundle.getBundle().getString("vouchertable.column.5")) {
        @Override
        public Object getValue(SSVoucher iVoucher) {
            return iVoucher.getCorrectedBy();
        }

        @Override
        public void setValue(SSVoucher iVoucher, Object iValue) {
            iVoucher.setCorrectedBy((SSVoucher) iValue);

        }

        @Override
        public Class getColumnClass() {
            return SSVoucher.class;
        }

        @Override
        public int getDefaultWidth() {
            return 80;
        }
    };

    /**
     *  Omslutning
     */
    public static SSTableColumn<SSVoucher> COLUMN_SUM = new SSTableColumn<SSVoucher>(
            SSBundle.getBundle().getString("vouchertable.column.6")) {
        @Override
        public Object getValue(SSVoucher iVoucher) {
            return SSVoucherMath.getCreditSum(iVoucher);
        }

        @Override
        public void setValue(SSVoucher iVoucher, Object iValue) {
            iVoucher.setCorrectedBy((SSVoucher) iValue);

        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 120;
        }
    };

}
