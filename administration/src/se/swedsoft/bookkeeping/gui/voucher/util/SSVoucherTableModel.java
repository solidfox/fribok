/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.voucher.util;

import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;

import java.util.*;
import java.math.BigDecimal;


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
     */
    public SSVoucherTableModel(List<SSVoucher> iVouchers) {
        super(iVouchers);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSVoucher.class;
    }

    /**
     *
     * @return
     */
    public static SSVoucherTableModel getDropDownModel(){
        SSVoucherTableModel iModel = new SSVoucherTableModel();

        iModel.addColumn( SSVoucherTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSVoucherTableModel.COLUMN_DESCRIPTION );

        return iModel;
    }


     /**
     *  Fakturanr
     */
    public static SSTableColumn<SSVoucher> COLUMN_NUMBER = new SSTableColumn<SSVoucher>(SSBundle.getBundle().getString("vouchertable.column.1")) {
        public Object getValue(SSVoucher iVoucher) {
            return iVoucher.getNumber();
        }

        public void setValue(SSVoucher iVoucher, Object iValue) {
            iVoucher.setNumber((Integer)iValue);

        }

        public Class getColumnClass() {
            return Integer.class;
        }

        public int getDefaultWidth() {
            return 80;
        }
    };


    /**
    *  Fakturanr
    */
   public static SSTableColumn<SSVoucher> COLUMN_DATE = new SSTableColumn<SSVoucher>(SSBundle.getBundle().getString("vouchertable.column.2")) {
       public Object getValue(SSVoucher iVoucher) {
           return iVoucher.getDate();
       }

       public void setValue(SSVoucher iVoucher, Object iValue) {
           iVoucher.setDate((Date)iValue);

       }

       public Class getColumnClass() {
           return Date.class;
       }

       public int getDefaultWidth() {
           return 85;
       }
   };




    /**
    *  Fakturanr
    */
   public static SSTableColumn<SSVoucher> COLUMN_DESCRIPTION = new SSTableColumn<SSVoucher>(SSBundle.getBundle().getString("vouchertable.column.3")) {
       public Object getValue(SSVoucher iVoucher) {
           return iVoucher.getDescription();
       }

       public void setValue(SSVoucher iVoucher, Object iValue) {
           iVoucher.setDescription((String)iValue);

       }

       public Class getColumnClass() {
           return String.class;
       }

       public int getDefaultWidth() {
           return 300;
       }
   };




    /**
    *  Fakturanr
    */
   public static SSTableColumn<SSVoucher> COLUMN_CORRECTS = new SSTableColumn<SSVoucher>(SSBundle.getBundle().getString("vouchertable.column.4")) {
       public Object getValue(SSVoucher iVoucher) {
           return iVoucher.getCorrects();
       }

       public void setValue(SSVoucher iVoucher, Object iValue) {
           iVoucher.setCorrects((SSVoucher)iValue);

       }

       public Class getColumnClass() {
           return SSVoucher.class;
       }

       public int getDefaultWidth() {
           return 80;
       }
   };


    /**
    *  Fakturanr
    */
   public static SSTableColumn<SSVoucher> COLUMN_CORRECTEDBY = new SSTableColumn<SSVoucher>(SSBundle.getBundle().getString("vouchertable.column.5")) {
       public Object getValue(SSVoucher iVoucher) {
           return iVoucher.getCorrectedBy();
       }

       public void setValue(SSVoucher iVoucher, Object iValue) {
           iVoucher.setCorrectedBy((SSVoucher)iValue);

       }

       public Class getColumnClass() {
           return SSVoucher.class;
       }

       public int getDefaultWidth() {
           return 80;
       }
   };



    /**
    *  Omslutning
    */
   public static SSTableColumn<SSVoucher> COLUMN_SUM = new SSTableColumn<SSVoucher>(SSBundle.getBundle().getString("vouchertable.column.6")) {
       public Object getValue(SSVoucher iVoucher) {
           return SSVoucherMath.getCreditSum(iVoucher);
       }

       public void setValue(SSVoucher iVoucher, Object iValue) {
           iVoucher.setCorrectedBy((SSVoucher)iValue);

       }

       public Class getColumnClass() {
           return BigDecimal.class;
       }

       public int getDefaultWidth() {
           return 120;
       }
   };



}
