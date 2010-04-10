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
public class SSBigDecimalTextField extends JFormattedTextField {

    NumberFormat iFormat = NumberFormat.getNumberInstance();

    private String iAppendText;

    /**
     *
     */
    public SSBigDecimalTextField() {
        // The action for transferring the focus to the search table.
        Action iSetValue =  new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                FocusEvent iEvent = new FocusEvent(SSBigDecimalTextField.this, FocusEvent.FOCUS_LOST, false, SSBigDecimalTextField.this);

                processFocusEvent(iEvent);
                requestFocusInWindow();
            }
        };
        // The action for transferring the focus to the search table.
        Action iSetNull =  new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                setValue(null);
            }
        };

        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER , 0), "SETVALUE");
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "SETNULL");

        getActionMap().put("SETVALUE", iSetValue);
        getActionMap().put("SETNULL" , iSetNull);

        iFormat = NumberFormat.getNumberInstance();
        iFormat.setMinimumFractionDigits(2);
        iFormat.setMaximumFractionDigits(2);

        NumberFormatter iFormatter = new NumberFormatter(iFormat);
        iFormatter.setFormat(iFormat);

        setFormatterFactory(new DefaultFormatterFactory(iFormatter));

        setHorizontalAlignment(JTextField.TRAILING);
    }

    /**
     * @param value Initial value for the JFormattedTextField
     */
    public SSBigDecimalTextField(Object value) {
        this();
        setValue(value);
    }

    /**
     * 
     * @return
     */
    public String getAppendText() {
        return iAppendText;
    }

    /**
     *
     * @param iAppendText
     */
    public void setAppendText(String iAppendText) {
        this.iAppendText = iAppendText;
    }

    /**
     *
     * @param iFractionDigits
     */
    public void setFractionDigits(int iFractionDigits){
        iFormat.setMinimumFractionDigits(iFractionDigits);
        iFormat.setMaximumFractionDigits(iFractionDigits);
    }

    /**
     *
     * @return
     */
    public int getFractionDigits() {
        return iFormat.getMaximumFractionDigits();
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

        if( iValue instanceof BigDecimal) {
            return(BigDecimal)iValue;
        }

        if( iValue instanceof Number) {
            return new BigDecimal( ((Number)iValue).doubleValue() );
        }

        return null;


    }

    @Override
    public void setText(String t) {
        if(iAppendText != null){
            super.setText(t + ' ' + iAppendText);
        } else {
            super.setText(t);
        }

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.util.components.SSBigDecimalTextField");
        sb.append("{iAppendText='").append(iAppendText).append('\'');
        sb.append(", iFormat=").append(iFormat);
        sb.append('}');
        return sb.toString();
    }
}
