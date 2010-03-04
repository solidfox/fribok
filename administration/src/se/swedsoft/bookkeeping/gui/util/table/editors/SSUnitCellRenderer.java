package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.common.SSUnit;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 13:26:29
 */
public class SSUnitCellRenderer extends DefaultTableCellRenderer {


    @Override
    public void setValue(Object value) {
        if(value != null && value instanceof SSUnit){
            setText( ((SSUnit)value).getName()  );
        }  else {
            setText( "" );
        }
    }


}
