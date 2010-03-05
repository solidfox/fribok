package se.swedsoft.bookkeeping.gui.voucher.dialogs;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.common.SSVATCode;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSInputVerifier;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSConfirmDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.model.SSVATCodeTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;


/**
 * User: Fredrik Stigsson
 * Date: 2006-feb-07
 * Time: 10:49:10
 */
public class SSAddAccountDialog extends SSDialog {


    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;
    private JTextField iAccountNr;
    private JTextField iDescription;
    private JTextField iSruCode;
    private JTextField iReportCode;
    private JCheckBox iProjectRequired;
    private JCheckBox iResultUnitRequired;
    private SSTableComboBox<SSVATCode> iVatCode;

    private SSInputVerifier iInputVerifier;



    public SSAddAccountDialog(final SSMainFrame iDialog){
        super(iDialog, "Lägg till konto" );

        SSVATCodeTableModel iModel = new SSVATCodeTableModel();
        iModel.addColumn(SSVATCodeTableModel.COLUMN_NAME);
        iModel.addColumn(SSVATCodeTableModel.COLUMN_DESCRIPTION);
        iVatCode.setModel(iModel);


        add(iPanel, BorderLayout.CENTER);
        pack();
        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSAccount iAccount = new SSAccount();
                try{
                    iAccount.setNumber(Integer.parseInt(iAccountNr.getText()));
                }
                catch(NumberFormatException ex){
                    SSErrorDialog.showDialog(iDialog, "Felaktigt kontonummer", "Kontonummret får bara innehålla siffror.");
                    return;
                }

                SSNewAccountingYear iCurrentYear = SSDB.getInstance().getCurrentYear();
                List<SSAccount> iExistingAccounts = new LinkedList<SSAccount>(iCurrentYear.getAccounts());
                if(iExistingAccounts.contains(iAccount)){
                    iAccount = iExistingAccounts.get(iExistingAccounts.indexOf(iAccount));
                    if(iAccount.isActive()){
                        SSErrorDialog.showDialog(iDialog, "Dubblett", "Kontot " + iAccount.getNumber() + " existerar redan.");
                        return;
                    }
                    else{
                        int result = SSConfirmDialog.showDialog(iDialog, "Kontot inaktivt", "Kontot " + iAccount.getNumber() + " är inaktivt. Vill du aktivera det?");
                        if(result == JOptionPane.YES_OPTION){
                            iAccount.setActive(true);
                            SSDB.getInstance().updateAccountingYear(iCurrentYear);
                        }
                        else{
                            return;
                        }

                    }
                }
                else{
                    iAccount.setDescription(iDescription.getText());
                    iAccount.setVATCode(iVatCode.getSelected() == null ? null : iVatCode.getSelected().getName());
                    iAccount.setSRUCode(iSruCode.getText());
                    iAccount.setReportCode(iReportCode.getText());
                    iAccount.setProjectRequired(iProjectRequired.isSelected());
                    iAccount.setResultUnitRequired(iResultUnitRequired.isSelected());

                    iCurrentYear.getAccountPlan().addAccount(iAccount);
                    iCurrentYear.getAccountPlan().setAccounts(iCurrentYear.getAccountPlan().getAccounts());

                    SSDB.getInstance().updateAccountingYear(iCurrentYear);
                }

                closeDialog(JOptionPane.OK_OPTION);
            }
        });

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(JOptionPane.CANCEL_OPTION);
            }
        });

        iInputVerifier = new SSInputVerifier();

        iInputVerifier.add(iAccountNr);
        iInputVerifier.add(iDescription);
        iInputVerifier.addListener(new SSInputVerifier.SSVerifierListener() {
            public void updated(SSInputVerifier iVerifier, boolean iValid) {
                //JComponent iCurrent = iVerifier.getCurrentComponent();
                iButtonPanel.getOkButton().setEnabled(iValid);
            }
        });
    }

    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }


    /**
     *
     * @param iMainFrame
     */
    public static void showDialog(final SSMainFrame iMainFrame) {
        SSAddAccountDialog iDialog = new SSAddAccountDialog(iMainFrame);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.showDialog();
    }

}


