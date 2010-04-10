package se.swedsoft.bookkeeping.gui.util.components;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Date: 2006-feb-03
 * Time: 15:02:18
 */
public class SSCurrencyTextField extends JFormattedTextField {

    /**
     *
     */
    public SSCurrencyTextField() {
        // The action for transferring the focus to the search table.
        Action setValue =  new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                FocusEvent event = new FocusEvent(SSCurrencyTextField.this, FocusEvent.FOCUS_LOST, false, SSCurrencyTextField.this);

                processFocusEvent(event);
                requestFocusInWindow();
            }
        };
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "SETVALUE");

        getActionMap().put("SETVALUE", setValue);


        NumberFormat iFormat = NumberFormat.getNumberInstance();
        iFormat.setMinimumFractionDigits(2);
        iFormat.setMaximumFractionDigits(2);
        NumberFormatter intFormatter = new NumberFormatter(iFormat);
        intFormatter.setFormat(iFormat);

        setFormatterFactory(new DefaultFormatterFactory(intFormatter));
        setHorizontalAlignment(JTextField.TRAILING);
    }

    /**
     * @param value Initial value for the JFormattedTextField
     */
    public SSCurrencyTextField(Object value) {
        this();
        setValue(value);
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
    public BigDecimal getValue() {
        Object iValue = super.getValue();

        if( iValue instanceof Number) {
            return new BigDecimal( ((Number)iValue).doubleValue() );
        }

        if( iValue != null){
            System.out.println("(SSCurrencyTextField) " + iValue.getClass().getName() );
        }

        return null;
    }
}
