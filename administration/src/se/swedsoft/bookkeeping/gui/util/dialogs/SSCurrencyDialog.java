package se.swedsoft.bookkeeping.gui.util.dialogs;

import se.swedsoft.bookkeeping.gui.util.components.SSBigDecimalTextField;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;

/**
 * User: Andreas Lago
 * Date: 2006-mar-23
 * Time: 14:25:51
 */
public class SSCurrencyDialog extends SSDialog{

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JTextField iDescription;

    private JTextField iName;

    private SSBigDecimalTextField iExchangeRate;

    /**
     * @param iFrame
     */
    public SSCurrencyDialog(JFrame iFrame) {
        super(iFrame, SSBundle.getBundle().getString("currencydialog.title"));

        setPanel(iPanel);

        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSCurrencyDialog.this.closeDialog(JOptionPane.OK_OPTION);
            }
        });

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSCurrencyDialog.this.closeDialog(JOptionPane.CANCEL_OPTION);
            }
        });
        setLocationRelativeTo(iFrame);
    }

    /**
     * @param iDialog
     */
    public SSCurrencyDialog(JDialog iDialog) {
        super(iDialog, SSBundle.getBundle().getString("currencydialog.title"));

        setPanel(iPanel);

        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSCurrencyDialog.this.closeDialog(JOptionPane.OK_OPTION);
            }
        });

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSCurrencyDialog.this.closeDialog(JOptionPane.CANCEL_OPTION);
            }
        });
        setLocationRelativeTo(iDialog);
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getName() {
        return iName.getText();
    }

    /**
     *
     * @param iName
     */
    public void setName(String iName) {
        this.iName.setText(iName);
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getDescription() {
        return iDescription.getText();
    }

    /**
     *
     * @param iDescription
     */
    public void setDescription(String iDescription) {
        this.iDescription.setText(iDescription);
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public BigDecimal getExchangeRate() {
        return iExchangeRate.getValue();
    }

    /**
     *
     * @param iValue
     */
    public void setExchangeRate(BigDecimal iValue) {
        iExchangeRate.setValue(iValue);
    }


}
