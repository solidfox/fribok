package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.common.SSCurrency;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 13:26:29
 */
public class SSCurrencyCellRenderer extends DefaultTableCellRenderer {


    @Override
    public void setValue(Object value) {
        if(value instanceof SSCurrency){
            SSCurrency iCurrency = (SSCurrency) value;

            setText( iCurrency.getName()  );
        }  else {
            setText( "" );
        }
    }


}
