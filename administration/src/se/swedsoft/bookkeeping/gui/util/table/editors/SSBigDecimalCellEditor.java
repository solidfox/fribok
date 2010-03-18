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
        super();
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

    /**
     * Returns the value contained in the editor.
     *
     * @return the value contained in the editor
     */
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



    /**
     * Sets an initial <code>value</code> for the editor.  This will cause
     * the editor to <code>stopEditing</code> and lose any partially
     * edited value if the editor is editing when this method is called. <p>
     * <p/>
     * Returns the component that should be added to the client's
     * <code>Component</code> hierarchy.  Once installed in the client's
     * hierarchy this component will then be able to draw and receive
     * user input.
     *
     * @param    table        the <code>JTable</code> that is asking the
     * editor to edit; can be <code>null</code>
     * @param    value        the value of the cell to be edited; it is
     * up to the specific editor to interpret
     * and draw the value.  For example, if value is
     * the string "true", it could be rendered as a
     * string or it could be rendered as a check
     * box that is checked.  <code>null</code>
     * is a valid value
     * @param    isSelected    true if the cell is to be rendered with
     * highlighting
     * @param    row the row of the cell being edited
     * @param    column the column of the cell being edited
     * @return the component for editing
     */
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

    /**
     * Returns true.
     *
     * @param e an event object
     * @return true
     */
    @Override
    public boolean isCellEditable(EventObject e) {
         if (e instanceof MouseEvent) {
             MouseEvent iMouseEvent = (MouseEvent) e;

             return iMouseEvent.getClickCount() >= 2;
        }
        return super.isCellEditable(e);
    }

} // End of class SSIntegerCellEditor
