/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.util.table.editors;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.EventObject;


public class SSBigDecimalCellEditor extends AbstractCellEditor implements TableCellEditor {

    private JFormattedTextField iTextField;

    /**
     * Default constructor.
     * @param maxFractionDigits
     */
    public SSBigDecimalCellEditor(int maxFractionDigits) {
        NumberFormat iFormat = NumberFormat.getNumberInstance();
        iFormat.setMinimumFractionDigits(maxFractionDigits);
        iFormat.setMaximumFractionDigits(maxFractionDigits);

        iTextField = new JFormattedTextField(iFormat);

        iTextField.setHorizontalAlignment(JTextField.TRAILING);
        iTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);

        iTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "commintchanges");

        iTextField.getActionMap().put("commintchanges", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
        iTextField.addMouseListener(new MouseInputAdapter() {
            // implements java.awt.event.MouseListener
            @Override
            public void mouseClicked(MouseEvent e) {
                  iTextField.selectAll();
            }
        });

    }

    @Override
    public Object getCellEditorValue() {
        try {
            iTextField.commitEdit();
        } catch (ParseException e1) {
            return null;
        }
        Object iValue = iTextField.getValue();

        if( iValue instanceof BigDecimal){
            return iValue;
        }
        if( iValue instanceof Number){
            Number iNumber = (Number) iValue;

             return new BigDecimal( iNumber.doubleValue() );
        }
        return null;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        if( value instanceof Number){
            Number iValue = (Number) value;

            iTextField.setValue(iValue);
            iTextField.selectAll();
        } else {
            iTextField.setValue(null);
            iTextField.selectAll();
        }
        return iTextField;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
         if (e instanceof MouseEvent) {
             MouseEvent iMouseEvent = (MouseEvent) e;

             return iMouseEvent.getClickCount() >= 2;
        }
        return super.isCellEditable(e);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellEditor");
        sb.append("{iTextField=").append(iTextField);
        sb.append('}');
        return sb.toString();
    }
}
