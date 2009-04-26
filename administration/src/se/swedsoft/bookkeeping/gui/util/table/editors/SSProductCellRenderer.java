package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.SSProduct;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 13:26:29
 */
public class SSProductCellRenderer extends DefaultTableCellRenderer {


    public void setValue(Object value) {
        if(value instanceof SSProduct){
            SSProduct iProduct = (SSProduct) value;

            setText( iProduct.getNumber()  );
        }  else {
            if(value != null){
                setText( value.toString() );
            } else {
                setText( "" );
            }

        }
    }


}
