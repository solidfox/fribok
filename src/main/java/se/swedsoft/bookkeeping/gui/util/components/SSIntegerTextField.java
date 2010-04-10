package se.swedsoft.bookkeeping.gui.util.components;


import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;


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
        // The action for transferring the focus to the search table.
        Action iSetValue = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                FocusEvent event = new FocusEvent(SSIntegerTextField.this,
                        FocusEvent.FOCUS_LOST, false, SSIntegerTextField.this);

                processFocusEvent(event);
                requestFocus();
            }
        };

        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                "SETVALUE");

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

    public void dispose() {
        iFormat = null;
        super.removeAll();
    }

    /**
     * Returns the last isValid value. Based on the editing policy of
     * the {@code AbstractFormatter} this may not return the current
     * value. The currently edited value can be obtained by invoking
     * {@code commitEdit} followed by {@code getInvoiceValue}.
     *
     * @return Last isValid value
     */
    @Override
    public Integer getValue() {
        Object iValue = super.getValue();

        if (iValue instanceof Integer) {
            return(Integer) iValue;
        }

        if (iValue instanceof Number) {
            return ((Number) iValue).intValue();
        }

        return null;

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.util.components.SSIntegerTextField");
        sb.append("{iFormat=").append(iFormat);
        sb.append('}');
        return sb.toString();
    }
}
