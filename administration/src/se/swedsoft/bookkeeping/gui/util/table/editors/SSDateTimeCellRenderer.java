/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.util.table.editors;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.DateFormat;


/**
 * This class implements a cell renderer that renders a Date and time
 *
 */
public class SSDateTimeCellRenderer extends DefaultTableCellRenderer {


    // The formatter to use.
    private DateFormat iDateFormat;
    private DateFormat iTimeFormat;


    /**
     * Default constructor.
     */
    public SSDateTimeCellRenderer() {
        iDateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        iTimeFormat = DateFormat.getTimeInstance(DateFormat.SHORT);
    }


    /**
     * Sets the value for the cell.
     *
     * @param value The value to format.
     */
    @Override
    public void setValue(Object value) {
        if(value == null){
            setText("");
        } else {
            setText( iDateFormat.format(value) + ' ' + iTimeFormat.format(value));
        }
    }

}
