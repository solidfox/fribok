package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 13:26:29
 */
public class SSSupplierInvoiceCellRenderer extends DefaultTableCellRenderer {


    @Override
    public void setValue(Object value) {
        if(value instanceof SSSupplierInvoice){
            SSSupplierInvoice iSupplierInvoice = (SSSupplierInvoice) value;

            setText( "" + iSupplierInvoice.getNumber()  );
        }  else {
            setText( "" );
        }
    }


}
