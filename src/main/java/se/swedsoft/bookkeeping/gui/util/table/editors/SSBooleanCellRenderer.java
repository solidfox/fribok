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
public class SSBooleanCellRenderer extends JPanel implements TableCellRenderer {

    private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    private JCheckBox iCheckBox;

    public SSBooleanCellRenderer() {
        iCheckBox = new JCheckBox();
        iCheckBox.setHorizontalAlignment(JLabel.CENTER);
        iCheckBox.setBorderPainted(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setBorder(noFocusBorder);

        if (value != null) {
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

            iCheckBox.setSelected((Boolean) value);

            return iCheckBox;
        }

        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.util.table.editors.SSBooleanCellRenderer");
        sb.append("{iCheckBox=").append(iCheckBox);
        sb.append('}');
        return sb.toString();
    }
}

