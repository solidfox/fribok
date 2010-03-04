package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.SSNewResultUnit;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 13:26:29
 */
public class SSResultUnitCellRenderer extends DefaultTableCellRenderer {

    /**
     * 
     * @param value
     */
    @Override
    public void setValue(Object value) {
        if(value instanceof SSNewResultUnit){
            SSNewResultUnit iResultUnit = (SSNewResultUnit) value;

             setText( "" + iResultUnit.getNumber()  );
        }  else {
            setText( "" );
        }
    }


}
