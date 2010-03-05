package se.swedsoft.bookkeeping.importexport.bgmax.dialog;

import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.gui.invoice.util.SSInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.components.SSCurrencyTextField;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxBetalning;
import se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxReferens;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Andreas Lago
 * Date: 2006-aug-24
 * Time: 09:36:38
 */
public class BgMaxSelectInvoiceDialog extends SSDialog {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JTextField iReference;

    private SSCurrencyTextField iBelopp;

    private SSTableComboBox<SSInvoice> iInvoices;
    private JTextField iCurrency;
    private JTextPane iInformationsText;
    private JTextField iBetalarensNamn;
    private JTextField iBetalarensOrt;
    private JTextField iBetalarensPostnummer;
    private JTextField iBetalarensAdress;
    private JTextField iBetalarensLand;
    private JTextField iBetalarensOrganisationsnr;
    private JList list1;
    private JTextPane textPane1;

    /**
     * @param iFrame
     */
    public BgMaxSelectInvoiceDialog(JFrame iFrame) {
        super(iFrame, SSBundle.getBundle().getString("bgmaximport.selectinvoice.title"));

        SSInvoiceTableModel iModel = new SSInvoiceTableModel( SSInvoiceMath.getNonPayedOrCreditedInvoices() );
        iModel.addColumn( SSInvoiceTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSInvoiceTableModel.COLUMN_OCRNUMBER );
        iModel.addColumn( SSInvoiceTableModel.COLUMN_CUSTOMER_NR );
        iModel.addColumn( SSInvoiceTableModel.COLUMN_CUSTOMER_NAME );
        iModel.addColumn( SSInvoiceTableModel.COLUMN_DATE );
        iModel.addColumn( SSInvoiceTableModel.COLUMN_TOTAL_SUM );
        iModel.addColumn(SSInvoiceTableModel.getSaldoColumn());
        iModel.addColumn( SSInvoiceTableModel.COLUMN_CURRENCY );

        setPanel(iPanel);

        iInvoices.setModel(iModel);

        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(JOptionPane.OK_OPTION);
            }
        });

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(JOptionPane.CANCEL_OPTION);
            }
        });
    }

    /**
     *
     * @return
     */
    public SSInvoice getInvoice() {
        return iInvoices.getSelected();
    }


    /**
     *
     * @param iBetalning
     * @return
     */
    public int showDialog(BgMaxBetalning iBetalning) {
        iInvoices.setSelected(null);

        iCurrency .setText( iBetalning.iAvsnitt.iValuta );

        iInformationsText.setText( iBetalning.iInformationsText );


        iReference.setText( getReferenceText(iBetalning)  );

        iBelopp          .setValue( iBetalning.getBelopp()   );

        iBetalarensNamn           .setText(  iBetalning.iBetalarensNamn);
        iBetalarensOrt            .setText(  iBetalning.iBetalarensOrt);
        iBetalarensPostnummer     .setText(  iBetalning.iBetalarensPostnummer);
        iBetalarensAdress         .setText(  iBetalning.iBetalarensAdress);
        iBetalarensLand           .setText(  iBetalning.iBetalarensLand);
        iBetalarensOrganisationsnr.setText(  iBetalning.iBetalarensOrganisationsnr);

        pack();

        return super.showDialog();
    }

    /**
     *
     * @param iBetalning
     * @return
     */
    private String getReferenceText(BgMaxBetalning iBetalning){
        StringBuilder sb = new StringBuilder();


        String iText = iBetalning.iReferens;

        if(iText != null && iText.trim().length() > 0) sb.append( iText ).append('\n');

        for (BgMaxReferens iReferens : iBetalning.iReferenser) {
            iText = iReferens.iReferens;

            if(iText != null && iText.trim().length() > 0) sb.append( iText ).append('\n');
        }
       return sb.toString();
    }

}
