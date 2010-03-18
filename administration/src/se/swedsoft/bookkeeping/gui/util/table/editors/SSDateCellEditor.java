package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Date;
import java.util.EventObject;

/**
 *
 */
public class SSDateCellEditor extends AbstractCellEditor implements TableCellEditor {

    private JPanel iPanel;

    private JFormattedTextField iTextField;

    private JButton iButton;

    private SSDateChooser iDateChooser;

    private Date iDate;

    /**
     * Default constructor.
     */
    public SSDateCellEditor() {
        DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        iDateChooser = new SSDateChooser();
        iDateChooser.addChangeListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setDate( iDateChooser.getDate() );
            }
        }); 

        iTextField = new JFormattedTextField(iFormat);
        iTextField.setHorizontalAlignment(JTextField.TRAILING);
        iTextField.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                setDate( (Date)iTextField.getValue() );
            }
        });

        iButton = new SSButton("ICON_CALENDAR16");
        iButton.setToolTipText( SSBundle.getBundle().getString("date.tooltip"));
        iButton.setPreferredSize(new Dimension(20, 20));
        iButton.setMaximumSize  (new Dimension(20, 20));
        iButton.setMinimumSize  (new Dimension(20, 20));

        iButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDateChooser.setDate( iDate );
                iDateChooser.show(iButton, 0, iButton.getHeight());
            }
        });


        iPanel = new JPanel();
        iPanel.setLayout(new BorderLayout());
        iPanel.add(iTextField, BorderLayout.CENTER);
        iPanel.add(iButton, BorderLayout.EAST);
    }

    /**
     * Returns the value contained in the editor.
     *
     * @return the value contained in the editor
     */
    public Object getCellEditorValue() {
        return iDate;
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
        if( value instanceof Date){
           setDate( (Date) value );
        } else {
           setDate( new Date() );
        }

        return iPanel;
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


    /**
     *
     * @return
     */
    public Date getDate() {
        return iDate;
    }

    /**
     *
     * @param iDate
     */
    public void setDate(Date iDate) {
        this.iDate = iDate;

        iTextField  .setValue(iDate);
        iDateChooser.setDate (iDate);
    }
}
