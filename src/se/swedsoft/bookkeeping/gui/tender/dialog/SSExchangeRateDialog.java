package se.swedsoft.bookkeeping.gui.tender.dialog;

import se.swedsoft.bookkeeping.data.base.SSSale;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.components.SSBigDecimalTextField;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * User: Andreas Lago
 * Date: 2006-mar-28
 * Time: 17:10:03
 */
public class SSExchangeRateDialog extends SSDialog{


    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JLabel iLabel1;
    private JLabel iLabel2;

    private SSBigDecimalTextField iExchangeRate;


    /**
     * @param iDialog
     */
    public SSExchangeRateDialog(JDialog iDialog) {
        super(iDialog, SSBundle.getBundle().getString("tenderframe.currencycalculator"));

        setPanel(iPanel);

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(JOptionPane.CANCEL_OPTION);
            }
        });
        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(JOptionPane.OK_OPTION);


            }
        });

        iExchangeRate.setValue(new BigDecimal(1));
    }


    /**
     *
     * @return
     */
    public BigDecimal getExchangeRate() {
        return iExchangeRate.getValue();
    }

    /**
     *
     * @param iExchangeRate
     */
    public void setExchangeRate(BigDecimal iExchangeRate) {
        this.iExchangeRate.setValue(iExchangeRate);
    }


    /**
     *
     * @param iOwner
     * @param iSale
     * @param iCompanyCurrency
     * @param iCurrentCurrency
     * @param pModel
     */
    public static void showDialog(final JDialog iOwner, final SSSale iSale, SSCurrency iCompanyCurrency, SSCurrency iCurrentCurrency, final AbstractTableModel pModel) {
        final SSExchangeRateDialog iDialog = new SSExchangeRateDialog(iOwner);

        BigDecimal iExchangeRate = iCurrentCurrency.getExchangeRate();

        iDialog.iLabel2.setText( String.format("%s"    , iCompanyCurrency.getName() ) );
        iDialog.iLabel1.setText( String.format("1 %s =", iCurrentCurrency.getName() )  );

        if(iExchangeRate != null) iDialog.iExchangeRate.setValue(iExchangeRate);

        iDialog.setLocationRelativeTo(iOwner);

        if(iDialog.showDialog() != JOptionPane.OK_OPTION) return;

        iExchangeRate = iDialog.getExchangeRate();

        for(SSSaleRow iRow: iSale.getRows()){
            BigDecimal iUnitPrice = iRow.getUnitprice();

            if(iUnitPrice != null) iRow.setUnitprice( iUnitPrice.divide(iExchangeRate, 2, RoundingMode.HALF_UP));
        }

        if(pModel != null) pModel.fireTableDataChanged();

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.tender.dialog.SSExchangeRateDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iExchangeRate=").append(iExchangeRate);
        sb.append(", iLabel1=").append(iLabel1);
        sb.append(", iLabel2=").append(iLabel2);
        sb.append(", iPanel=").append(iPanel);
        sb.append('}');
        return sb.toString();
    }
}
