package se.swedsoft.bookkeeping.gui.vouchertemplate.util;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.data.SSVoucherTemplate;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.util.List;
import java.util.Date;

/**
 * Date: 2006-feb-06
 * Time: 10:12:58
 */
public class SSVoucherTemplateTableModel extends SSTableModel<SSVoucherTemplate> {


    /**
     * Default constructor.
     */
    public SSVoucherTemplateTableModel() {
        super(SSDB.getInstance().getVoucherTemplates());
    }

    /**
     * Default constructor.
     */
    public SSVoucherTemplateTableModel(List<SSVoucherTemplate> iVouchers) {
        super(iVouchers);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSVoucherTemplate.class;
    }



    /**
     *
     * @return
     */
    public static SSVoucherTemplateTableModel getDropDownModel(){
        SSVoucherTemplateTableModel iModel = new SSVoucherTemplateTableModel();

        iModel.addColumn( SSVoucherTemplateTableModel.COLUMN_DESCRIPTION );

        return iModel;
    }




    /**
    *  Beskrivning
    */
   public static SSTableColumn<SSVoucherTemplate> COLUMN_DESCRIPTION = new SSTableColumn<SSVoucherTemplate>(SSBundle.getBundle().getString("vouchertable.column.3")) {
       @Override
       public Object getValue(SSVoucherTemplate iVoucher) {
           return iVoucher.getDescription();
       }

       @Override
       public void setValue(SSVoucherTemplate iVoucher, Object iValue) {
       }

       @Override
       public Class getColumnClass() {
           return String.class;
       }

       @Override
       public int getDefaultWidth() {
           return 400;
       }
   };


    /**
    *  Date
    */
   public static SSTableColumn<SSVoucherTemplate> COLUMN_DATE = new SSTableColumn<SSVoucherTemplate>(SSBundle.getBundle().getString("vouchertable.column.2")) {
       @Override
       public Object getValue(SSVoucherTemplate iVoucher) {
           return iVoucher.getDate();
       }

       @Override
       public void setValue(SSVoucherTemplate iVoucher, Object iValue) {
           iVoucher.setDate((Date)iValue);
       }

       @Override
       public Class getColumnClass() {
           return Date.class;
       }

       @Override
       public int getDefaultWidth() {
           return 80;
       }
   };

}



