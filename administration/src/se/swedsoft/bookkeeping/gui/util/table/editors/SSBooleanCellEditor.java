/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.util.table.editors;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;


/**
 */
public class SSBooleanCellEditor extends DefaultCellEditor {

    private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    // Our panel
    private JPanel iPanel;


    /**
     * Default constructor.
     */
    public SSBooleanCellEditor() {
        super(new JCheckBox());
        iPanel = new JPanel();

        JCheckBox checkBox = (JCheckBox)getComponent();
        checkBox.setHorizontalAlignment(JCheckBox.CENTER);
    }


    /**
     * Implements the <code>TableCellEditor</code> interface.
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Component c =  super.getTableCellEditorComponent(table, value, isSelected, row, column);

        if(value == null){
            if (isSelected) {
                iPanel.setForeground(table.getSelectionForeground());
                iPanel.setBackground(table.getSelectionBackground());
            } else {
                iPanel.setForeground(table.getForeground());
                iPanel.setBackground(table.getBackground());
            }

            iPanel.setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));

            return iPanel;
        }

        return c;

    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.util.table.editors.SSBooleanCellEditor");
        sb.append("{iPanel=").append(iPanel);
        sb.append('}');
        return sb.toString();
    }
}
