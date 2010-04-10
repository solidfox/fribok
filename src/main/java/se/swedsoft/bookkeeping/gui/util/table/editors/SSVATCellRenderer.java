package se.swedsoft.bookkeeping.gui.util.table.editors;


import se.swedsoft.bookkeeping.data.common.SSVATCode;

import javax.swing.table.DefaultTableCellRenderer;


/**
 * Date: 2006-mar-01
 * Time: 09:52:18
 */
public class SSVATCellRenderer extends DefaultTableCellRenderer {

    @Override
    public void setValue(Object value) {
        if (value instanceof SSVATCode) {
            SSVATCode iVATCode = (SSVATCode) value;

            setText(iVATCode.getName());
        } else {
            if (value != null) {
                setText(value.toString());
            } else {
                setText("");
            }
        }
    }

}
