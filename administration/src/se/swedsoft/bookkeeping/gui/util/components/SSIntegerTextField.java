package se.swedsoft.bookkeeping.gui.util.components;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.text.NumberFormat;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

/**
 * Date: 2006-feb-03
 * Time: 15:02:18
 */
public class SSIntegerTextField extends JFormattedTextField {

    NumberFormat iFormat = NumberFormat.getNumberInstance();

    /**
     *
     */
    public SSIntegerTextField() {
        super();
        // The action for transferring the focus to the search table.
          Action iSetValue =  new AbstractAction() {
              public void actionPerformed(ActionEvent e) {
                  FocusEvent event = new FocusEvent(SSIntegerTextField.this, FocusEvent.FOCUS_LOST, false, SSIntegerTextField.this);

                  SSIntegerTextField.this.processFocusEvent(event);
                  SSIntegerTextField.this.requestFocus();
              }
          };
          getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "SETVALUE");

          getActionMap().put("SETVALUE", iSetValue);

        iFormat = NumberFormat.getNumberInstance();
        iFormat.setMinimumFractionDigits(0);
        iFormat.setMaximumFractionDigits(0);

        NumberFormatter iFormatter = new NumberFormatter(iFormat);
        iFormatter.setFormat(iFormat);

        setFormatterFactory(new DefaultFormatterFactory(iFormatter));

        setHorizontalAlignment(JTextField.TRAILING);
    }

    /**
     * @param value Initial value for the JFormattedTextField
     */
    public SSIntegerTextField(Object value) {
        this();
        setValue(value);
    }

    public void dispose()
    {
        iFormat=null;
        super.removeAll();
    }



    /**
     * Returns the last isValid value. Based on the editing policy of
     * the <code>AbstractFormatter</code> this may not return the current
     * value. The currently edited value can be obtained by invoking
     * <code>commitEdit</code> followed by <code>getInvoiceValue</code>.
     *
     * @return Last isValid value
     */
    @Override
    public Integer getValue() {
        Object iValue = super.getValue();

        if( iValue instanceof Integer) {
            return(Integer)iValue;
        }

        if( iValue instanceof Number) {
            return new Integer( ((Number)iValue).intValue() );
        }

        return null;


    }
}
