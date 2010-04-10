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

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if( value instanceof Date){
           setDate( (Date) value );
        } else {
           setDate( new Date() );
        }

        return iPanel;
    }

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.util.table.editors.SSDateCellEditor");
        sb.append("{iButton=").append(iButton);
        sb.append(", iDate=").append(iDate);
        sb.append(", iDateChooser=").append(iDateChooser);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iTextField=").append(iTextField);
        sb.append('}');
        return sb.toString();
    }
}
