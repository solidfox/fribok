/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.util.table.editors;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.DateFormat;

// Trade Extensions specific imports

// Java specific imports

/**
 * This class implements a cell renderer that renders a Date using a DateFormat
 * with format DateFormat.SHORT. This will only display year-month-day. <P>
 *
 * @author Roger Björnstedt
 */
public class SSDateCellRenderer extends DefaultTableCellRenderer {


    // The formatter to use.
    private DateFormat iFormat;


    /**
     * Default constructor.
     */
    public SSDateCellRenderer() {
        setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
    }


    /**
     * Sets the value for the cell.
     *
     * @param value The value to format.
     */
    @Override
    public void setValue(Object value) {
        if (iFormat == null) {
            iFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        }
        setText((value == null) ? "" : iFormat.format(value));
    }

}
