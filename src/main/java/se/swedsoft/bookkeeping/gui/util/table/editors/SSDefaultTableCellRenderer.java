package se.swedsoft.bookkeeping.gui.util.table.editors;


import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 15:04:24
 */
public abstract class SSDefaultTableCellRenderer <T extends SSTableSearchable> extends DefaultTableCellRenderer {

    @Override
    protected void setValue(Object value) {
        if (value instanceof SSTableSearchable) {
            setValue((T) value);
        } else {
            super.setValue(value);
        }
    }

    /**
     * Sets the {@code String} object for the cell being rendered to
     * {@code value}.
     *
     * @param value the string value for this cell; if value is {@code null} it
     *              sets the text value to an empty string
     * @see JLabel#setText
     */
    protected void setValue(T value) {
        super.setValue(value);
    }

}
