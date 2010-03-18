package se.swedsoft.bookkeeping.gui.company.panel;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSAccountPlan;
import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSSelectionListener;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.model.SSAccountTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Andreas Lago
 * Date: 2006-mar-28
 * Time: 14:11:17
 */
public class SSDefaultAccountPanel extends JPanel {

    private Map<SSDefaultAccount, Integer                   > iDefaultAccounts;

    private Map<SSDefaultAccount, SSTableComboBox<SSAccount>> iComboBoxes;

    private Map<SSDefaultAccount, JTextField                > iTextFields;


    private JPanel iPanel;



    // Kundfodran
    private SSTableComboBox<SSAccount> iCustomerClaim;
    // Inbetalning
    private SSTableComboBox<SSAccount> iInPayment;
    // Utbetalning
    private SSTableComboBox<SSAccount> iOutPayment;
    // Kontant
    private SSTableComboBox<SSAccount> iCash;
    // Leverantörsskuld
    private SSTableComboBox<SSAccount> iSupplierDebt;
    // Moms 1
    private SSTableComboBox<SSAccount> iTax1;
    // Moms 2
    private SSTableComboBox<SSAccount> iTax2;
    // Moms 3
    private SSTableComboBox<SSAccount> iTax3;
    // Försäljning
    private SSTableComboBox<SSAccount> iSales;
    // Inköp
    private SSTableComboBox<SSAccount> iPurchases;
    // Valutakursvinst
    private SSTableComboBox<SSAccount> iCurrencyProfit;
    // Valutakursförlust
    private SSTableComboBox<SSAccount> iCurrencyLoss;
    // Öresavrundning
    private SSTableComboBox<SSAccount> iRounding;
    //Ränteintäkt
    private SSTableComboBox<SSAccount> iInterestProfit;
    // ingående moms
    private SSTableComboBox<SSAccount> iIncommingTax;


    // Kundfodran
    private JTextField iCustomerClaimText;
    // Inbetalning
    private JTextField iInPaymentText;
    // Utbetalning
    private JTextField iOutPaymentText;
    // Kontant
    private JTextField iCashText;
    // Leverantörsskuld
    private JTextField iSupplierDebtText;
    // Moms 1
    private JTextField iTax1Text;
    // Moms 2
    private JTextField iTax2Text;
    // Moms 3
    private JTextField iTax3Text;
    // Försäljning
    private JTextField iSalesText;
    // Inköp
    private JTextField iPurchasesText;
    // Valutakursvinst
    private JTextField iCurrencyProfitText;
    // Valutakursförlust
    private JTextField iCurrencyLossText;
    // Öresavrundning
    private JTextField iRoundingText;
    //Ränteintäkt
    private JTextField iInterestProfitText;
    // ingående moms
    private JTextField iIncommingTaxText;




    public SSDefaultAccountPanel() {
        setLayout(new BorderLayout() );

        add(iPanel, BorderLayout.CENTER);

        iDefaultAccounts = new HashMap<SSDefaultAccount, Integer>();

        iComboBoxes = new HashMap<SSDefaultAccount, SSTableComboBox<SSAccount>>();

        // Kundfodran
        iComboBoxes.put(SSDefaultAccount.CustomerClaim, iCustomerClaim);
        // Inbetalning
        iComboBoxes.put(SSDefaultAccount.InPayment, iInPayment);
        // Utbetalning
        iComboBoxes.put(SSDefaultAccount.OutPayment, iOutPayment);
        // Kontant
        iComboBoxes.put(SSDefaultAccount.Cash, iCash);
        // Leverantörsskuld
        iComboBoxes.put(SSDefaultAccount.SupplierDebt, iSupplierDebt);
        // Moms 1
        iComboBoxes.put(SSDefaultAccount.Tax1, iTax1);
        // Moms 2
        iComboBoxes.put(SSDefaultAccount.Tax2, iTax2);
        // Moms 3
        iComboBoxes.put(SSDefaultAccount.Tax3, iTax3);
        // Försäljning
        iComboBoxes.put(SSDefaultAccount.Sales, iSales);
        // Inköp
        iComboBoxes.put(SSDefaultAccount.Purchases, iPurchases);
        // Valutakursvinst
        iComboBoxes.put(SSDefaultAccount.CurrencyProfit, iCurrencyProfit);
        // Valutakursförlust
        iComboBoxes.put(SSDefaultAccount.CurrencyLoss, iCurrencyLoss);
        // Öresavrundning
        iComboBoxes.put(SSDefaultAccount.Rounding, iRounding);
        //Ränteintäkt
        iComboBoxes.put(SSDefaultAccount.InterestProfit, iInterestProfit);
        //Ingående moms
        iComboBoxes.put(SSDefaultAccount.IncommingTax, iIncommingTax);



        iTextFields = new HashMap<SSDefaultAccount, JTextField>();

        // Kundfodran
        iTextFields.put(SSDefaultAccount.CustomerClaim, iCustomerClaimText);
        // Inbetalning
        iTextFields.put(SSDefaultAccount.InPayment, iInPaymentText);
        // Utbetalning
        iTextFields.put(SSDefaultAccount.OutPayment, iOutPaymentText);
        // Kontant
        iTextFields.put(SSDefaultAccount.Cash, iCashText);
        // Leverantörsskuld
        iTextFields.put(SSDefaultAccount.SupplierDebt, iSupplierDebtText);
        // Moms 1
        iTextFields.put(SSDefaultAccount.Tax1, iTax1Text);
        // Moms 2
        iTextFields.put(SSDefaultAccount.Tax2, iTax2Text);
        // Moms 3
        iTextFields.put(SSDefaultAccount.Tax3, iTax3Text);
        // Försäljning
        iTextFields.put(SSDefaultAccount.Sales, iSalesText);
        // Inköp
        iTextFields.put(SSDefaultAccount.Purchases, iPurchasesText);
        // Valutakursvinst
        iTextFields.put(SSDefaultAccount.CurrencyProfit, iCurrencyProfitText);
        // Valutakursförlust
        iTextFields.put(SSDefaultAccount.CurrencyLoss, iCurrencyLossText);
        // Öresavrundning
        iTextFields.put(SSDefaultAccount.Rounding, iRoundingText);
        //Ränteintäkt
        iTextFields.put(SSDefaultAccount.InterestProfit, iInterestProfitText);
        //Ingående moms
        iTextFields.put(SSDefaultAccount.IncommingTax, iIncommingTaxText);


        for (int i = 0; i < SSDefaultAccount.values().length; i++) {
            SSDefaultAccount iDefaultAccount = SSDefaultAccount.values()[i];
            SSTableComboBox<SSAccount> iComboBox = iComboBoxes.get(iDefaultAccount);

            iComboBox.setModel(SSAccountTableModel.getDropDownModel());
            iComboBox.setSearchColumns(0);
            iComboBox.setAllowCustomValues(true);
            iComboBox.addSelectionListener(new SelectionListener(iDefaultAccount));
        }

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
     * @param iDefaultAccounts
     */
    public void setDefaultAccounts(Map<SSDefaultAccount, Integer> iDefaultAccounts) {
        this.iDefaultAccounts = iDefaultAccounts;

        SSAccountPlan iAccountPlan = SSDB.getInstance().getCurrentAccountPlan();

        for(SSDefaultAccount iCurrent : SSDefaultAccount.values()){
            SSTableComboBox<SSAccount> iComboBox = iComboBoxes.get(iCurrent);

            Integer iAccountNumber = iDefaultAccounts.get(iCurrent);

            if(iAccountNumber == null) iAccountNumber = iCurrent.getDefaultAccountNumber();

            if(iAccountPlan != null){
                iComboBox.setSelected( iAccountPlan.getAccount( iAccountNumber ), true );
            } else {
                iComboBox.setText    ( iAccountNumber.toString() );
            }
        }

    }


    /**
     *
     * @return
     */
    public Map<SSDefaultAccount, Integer> getDefaultAccounts() {

        for(SSDefaultAccount iCurrent : SSDefaultAccount.values()){
            SSTableComboBox<SSAccount> iComboBox = iComboBoxes.get(iCurrent);

            Integer iAccountNumber;
            if(iComboBox.getSelected() != null){
                iAccountNumber = iComboBox.getSelected().getNumber();
            }  else {

                try {
                    iAccountNumber =  Integer.decode( iComboBox.getText() );
                } catch (NumberFormatException e) {
                    iAccountNumber = iCurrent.getDefaultAccountNumber();
                }
            }
            iDefaultAccounts.put(iCurrent, iAccountNumber);
        }
        return iDefaultAccounts;
    }

    /**
     * Used to clean up references making sure the garbage collector
     * is able to clean up the object.
     */
    public void dispose() {

        //iDefaultAccounts.clear();
        //iDefaultAccounts=null;

        iComboBoxes.clear();
        iComboBoxes=null;

        iTextFields.clear();
        iTextFields=null;

        iPanel.removeAll();
        iPanel=null;

        iCustomerClaim.dispose();
        iCustomerClaim=null;

        iInPayment.dispose();
        iInPayment =null;
        iOutPayment.dispose();
        iOutPayment =null;
        iCash.dispose();
        iCash=null;
        iSupplierDebt.dispose();
        iSupplierDebt=null;
        iTax1.dispose();
        iTax1=null;
        iTax2.dispose();
        iTax2=null;
        iTax3.dispose();
        iTax3=null;
        iSales.dispose();
        iSales=null;
        iPurchases.dispose();
        iPurchases=null;
        iCurrencyProfit.dispose();
        iCurrencyProfit=null;
        iCurrencyLoss.dispose();
        iCurrencyLoss=null;
        iRounding.dispose();
        iRounding=null;
        iInterestProfit.dispose();
        iInterestProfit=null;
        iIncommingTax.dispose();
        iIncommingTax=null;
        iCustomerClaimText.removeAll();
        iCustomerClaimText=null;
        iInPaymentText.removeAll();
        iInPaymentText=null;
        iOutPaymentText.removeAll();
        iOutPaymentText=null;
        iCashText.removeAll();
        iCashText=null;
        iSupplierDebtText.removeAll();
        iSupplierDebtText=null;
        iTax1Text.removeAll();
        iTax1Text=null;
        iTax2Text.removeAll();
        iTax2Text=null;
        iTax3Text.removeAll();
        iTax3Text=null;
        iSalesText.removeAll();
        iSalesText=null;
        iPurchasesText.removeAll();
        iPurchasesText=null;
        iCurrencyProfitText.removeAll();
        iCurrencyProfitText=null;
        iCurrencyLossText.removeAll();
        iCurrencyLossText=null;
        iRoundingText.removeAll();
        iRoundingText=null;
        iInterestProfitText.removeAll();
        iInterestProfitText=null;
        iIncommingTaxText.removeAll();
        iIncommingTaxText=null;
    }


    private class SelectionListener implements SSSelectionListener<SSAccount>{
        private SSDefaultAccount iDefaultAccount;

        public SelectionListener(SSDefaultAccount iDefaultAccount) {
            this.iDefaultAccount = iDefaultAccount;
        }

        public void selected(SSAccount selected) {
            JTextField iTextField = iTextFields.get(iDefaultAccount);

            if(selected != null) iDefaultAccounts.put(iDefaultAccount, selected.getNumber());

            if(iTextField != null){

                if(selected != null){
                   iTextField.setText( selected.getDescription() );
                } else {
                    iTextField.setText( "" );
                }

            }

        }
    }

}
