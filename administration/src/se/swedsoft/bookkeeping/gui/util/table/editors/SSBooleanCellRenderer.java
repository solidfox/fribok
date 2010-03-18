package se.swedsoft.bookkeeping.gui.util.table.editors;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Date: 2006-feb-14
 * Time: 10:02:50
 */
public class SSBooleanCellRenderer extends JPanel implements TableCellRenderer{


    private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    private JCheckBox iCheckBox;

    public SSBooleanCellRenderer() {
        iCheckBox = new JCheckBox();
        iCheckBox.setHorizontalAlignment(JLabel.CENTER);
        iCheckBox.setBorderPainted(true);
    }

    /**
     * Returns the component used for drawing the cell.  This method is
     * used to configure the renderer appropriately before drawing.
     *
     * @param    table        the <code>JTable</code> that is asking the
     * renderer to draw; can be <code>null</code>
     * @param    value        the value of the cell to be rendered.  It is
     * up to the specific renderer to interpret
     * and draw the value.  For example, if
     * <code>value</code>
     * is the string "true", it could be rendered as a
     * string or it could be rendered as a check
     * box that is checked.  <code>null</code> is a
     * isValid value
     * @param    isSelected    true if the cell is to be rendered with the
     * selection highlighted; otherwise false
     * @param    hasFocus    if true, render cell appropriately.  For
     * example, put a special border on the cell, if
     * the cell can be edited, render in the color used
     * to indicate editing
     * @param    row     the row index of the cell being drawn.  When
     * drawing the header, the value of
     * <code>row</code> is -1
     * @param    column     the column index of the cell being drawn
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setBorder(noFocusBorder);

        if(value != null){
            if (isSelected) {
                iCheckBox.setForeground(table.getSelectionForeground());
                iCheckBox.setBackground(table.getSelectionBackground());
            } else {
                iCheckBox.setForeground(table.getForeground());
                iCheckBox.setBackground(table.getBackground());
            }

            if (hasFocus) {
               iCheckBox.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            } else {
               iCheckBox.setBorder(noFocusBorder);
            }

            iCheckBox.setSelected((Boolean)value);

            return iCheckBox;
        }


        return this;
    }
}




