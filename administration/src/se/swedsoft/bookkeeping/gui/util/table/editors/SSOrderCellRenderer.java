package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.invoice.util.SSInvoiceTableModel;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSOrder;

import javax.swing.table.DefaultTableCellRenderer;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 11:37:41
 */
public class SSOrderCellRenderer extends DefaultTableCellRenderer {


    public void setValue(Object value) {
        if(value instanceof SSOrder){
            SSOrder iOrder = (SSOrder) value;

            setText( "" +  iOrder.getNumber()  );
        }  else {
            setText( "" );
        }
    }


}

