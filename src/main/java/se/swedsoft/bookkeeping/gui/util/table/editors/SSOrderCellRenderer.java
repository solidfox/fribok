package se.swedsoft.bookkeeping.gui.util.table.editors;


import se.swedsoft.bookkeeping.data.SSOrder;

import javax.swing.table.DefaultTableCellRenderer;


/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 11:37:41
 */
public class SSOrderCellRenderer extends DefaultTableCellRenderer {

    @Override
    public void setValue(Object value) {
        if (value instanceof SSOrder) {
            SSOrder iOrder = (SSOrder) value;

            setText(String.valueOf(iOrder.getNumber()));
        } else {
            setText("");
        }
    }

}

