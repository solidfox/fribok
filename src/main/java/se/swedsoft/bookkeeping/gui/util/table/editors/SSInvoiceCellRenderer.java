package se.swedsoft.bookkeeping.gui.util.table.editors;


import se.swedsoft.bookkeeping.data.SSInvoice;

import javax.swing.table.DefaultTableCellRenderer;


/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 13:26:29
 */
public class SSInvoiceCellRenderer extends DefaultTableCellRenderer {

    @Override
    public void setValue(Object value) {
        if (value instanceof SSInvoice) {
            SSInvoice iInvoice = (SSInvoice) value;

            setText(String.valueOf(iInvoice.getNumber()));
        } else {
            setText("");
        }
    }

}
