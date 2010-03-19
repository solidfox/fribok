package se.swedsoft.bookkeeping.gui.tender.panel;

import se.swedsoft.bookkeeping.calc.math.SSProductMath;
import se.swedsoft.bookkeeping.calc.math.SSTenderMath;
import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSTender;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.common.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.company.panel.SSAdressPanel;
import se.swedsoft.bookkeeping.gui.company.panel.SSDefaultAccountPanel;
import se.swedsoft.bookkeeping.gui.customer.util.SSCustomerTableModel;
import se.swedsoft.bookkeeping.gui.invoice.util.SSInvoiceRowTableModel;
import se.swedsoft.bookkeeping.gui.tender.dialog.SSExchangeRateDialog;
import se.swedsoft.bookkeeping.gui.util.*;
import se.swedsoft.bookkeeping.gui.util.components.SSBigDecimalTextField;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSEditableTableComboBox;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSInformationDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.model.SSCurrencyTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSDeliveryTermTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSDeliveryWayTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSPaymentTermTableModel;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSDeleteAction;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSTraversalAction;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSTaxCodeCellEditor;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSTaxCodeCellRenderer;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;

/**
 * User: Andreas Lago
 * Date: 2006-mar-24
 * Time: 11:21:13
 */
public class SSTenderPanel{

    private SSTender iTender;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JFormattedTextField iNumber;

    private SSDateChooser iDate;

    private SSDateChooser iExpires;

    private SSTableComboBox<SSCustomer> iCustomer;
//   private SSTableComboBoxOld<SSCustomer> iCustomerNr;

    private JTextField iCustomerName;

    private JCheckBox iTaxFree;

    private SSBigDecimalTextField iDelayInterest;

    private SSAdressPanel iDeliveryAddress;

    private SSAdressPanel iInvoiceAddress;

    private JTextArea iText;

    private JTextField iYourContactPerson;

    private JTextField iOurContactPerson;

    private SSBigDecimalTextField iTaxRate1;

    private SSBigDecimalTextField iTaxRate2;

    private SSBigDecimalTextField iTaxRate3;

    private JCheckBox iEuSaleCommodity;

    private JCheckBox iEuSaleYhirdPartCommodity;

    private SSEditableTableComboBox<SSDeliveryWay > iDeliveryWay;

    private SSEditableTableComboBox<SSDeliveryTerm> iDeliveryTerm;

    private SSEditableTableComboBox<SSPaymentTerm > iPaymentTerm;

    private SSEditableTableComboBox<SSCurrency    > iCurrency;

    private SSTable iTable;

    private SSDefaultAccountPanel iDefaultAccounts;

    private SSButton iCurrencyCalculatorButton;

    private SSBigDecimalTextField iNetSum;
    private SSBigDecimalTextField iTaxSum1;
    private SSBigDecimalTextField iTaxSum2;
    private SSBigDecimalTextField iTaxSum3;
    private SSBigDecimalTextField iRoundingSum;
    private SSBigDecimalTextField iTotalSum;

    private JLabel iTaxLabel1;
    private JLabel iTaxLabel2;
    private JLabel iTaxLabel3;

    private JCheckBox iSavecustomerandproducts;

    private SSInvoiceRowTableModel  iModel;

    private SSInputVerifier iInputVerifier;
    private JCheckBox iUseInvoiceForDelivery;
    private SSBigDecimalTextField iCurrencyRate;

    /**
     *
     * @param iOwner
     */
    public SSTenderPanel(final SSDialog iOwner) {
        iNumber.setFormatterFactory( new DefaultFormatterFactory(new NumberFormatter( new DecimalFormat("0") ) ) );
        iNumber.setHorizontalAlignment(JTextField.RIGHT);

        iTable.setColorReadOnly(true);
        iTable.setColumnSortingEnabled(false);
        iTable.setSingleSelect();

        iModel = new SSInvoiceRowTableModel();
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_PRODUCT      , true);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_DESCRIPTION  , true);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_UNITPRICE    , true);
        iModel.addColumn(new ContributionRateColumn()               , false);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_QUANTITY     , true);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_UNIT         , true);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_DISCOUNT     , true);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_SUM          , false);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_TAX          , true);
        iModel.setupTable(iTable);

        iModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                updateSumFields();
            }
        });


        iUseInvoiceForDelivery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDeliveryAddress.setEnabled( !iUseInvoiceForDelivery.isSelected());

                if(!iUseInvoiceForDelivery.isSelected())
                    iDeliveryAddress.setFocus();
                else
                    iInvoiceAddress.setFocus();
            }
        });

        iCustomer.setModel(SSCustomerTableModel.getDropDownModel());
        iCustomer.setSearchColumns(0,1);
        iCustomer.setAllowCustomValues(true);
        iCustomer.addSelectionListener(new SSSelectionListener<SSCustomer>() {
            public void selected(SSCustomer selected) {
                if(selected != null){
                    iModel.setCustomer(selected);
                    iTender.setCustomer(selected);
                    iTender.setCurrencyRate(selected.getInvoiceCurrency() == null ? new BigDecimal(1) : selected.getInvoiceCurrency().getExchangeRate());
                    if(selected.getDiscount() != null){
                        for (SSSaleRow iRow : iModel.getObjects()) {
                            iRow.setDiscount(selected.getDiscount().doubleValue() == new BigDecimal(0).doubleValue() ? null : selected.getDiscount());
                        }
                    }
                    setTender(iTender);
                }
            }
        });

        new SSTraversalAction(iTable){
            @Override
            protected Point doTraversal(Point iPosition) {
                if (iPosition.x <= 4) {
                    iPosition.x = iPosition.x + 1;
                }
                if (iPosition.x == 5) {

                    iPosition.y = iPosition.y + 1;
                    iPosition.x = 0;

                    if(iPosition.y == iModel.getRowCount()) {
                        iButtonPanel.getOkButton().requestFocus();
                        return null;
                    }

                }
                return iPosition;
            }
        };

        new SSDeleteAction(iTable){
            @Override
            protected Point doDelete(Point iPosition) {
                SSSaleRow iSelected = iModel.getSelectedRow(iTable);

                if(iSelected != null) {

                    SSQueryDialog dialog = new SSQueryDialog(SSMainFrame.getInstance(), SSBundle.getBundle(), "tenderframe.deleterow", iSelected.toString() );

                    if( dialog.getResponce() != JOptionPane.YES_OPTION ) return null;

                    iModel.deleteRow(iSelected );
                }
                return iPosition;
            }
        };


        iCurrency.getComboBox().setModel( SSCurrencyTableModel.getDropDownModel() );
        iCurrency.getComboBox().setSearchColumns(0);
        iCurrency.setEditingFactory( SSCurrencyTableModel.getEditingFactory(iOwner) );

        iCurrency.getComboBox().addSelectionListener(new SSSelectionListener<SSCurrency>() {
            public void selected(SSCurrency selected) {
                if(selected != null) iCurrencyRate.setValue(selected.getExchangeRate());

                iModel.fireTableDataChanged();
            }
        });

        iDeliveryWay.getComboBox().setModel( SSDeliveryWayTableModel.getDropDownModel() );
        iDeliveryWay.getComboBox().setSearchColumns(0);
        iDeliveryWay.setEditingFactory( SSDeliveryWayTableModel.getEditingFactory(iOwner) );

        iDeliveryTerm.getComboBox().setModel( SSDeliveryTermTableModel.getDropDownModel() );
        iDeliveryTerm.getComboBox().setSearchColumns(0);
        iDeliveryTerm.setEditingFactory( SSDeliveryTermTableModel.getEditingFactory(iOwner) );

        iPaymentTerm.getComboBox().setModel( SSPaymentTermTableModel.getDropDownModel() );
        iPaymentTerm.getComboBox().setSearchColumns(0);
        iPaymentTerm.setEditingFactory( SSPaymentTermTableModel.getEditingFactory(iOwner) );

        iTaxRate1.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // Momssats 1
                iTender.setTaxRate1(iTaxRate1.getValue());
                updateTaxTexts();
                updateSumFields();
            }
        });
        iTaxRate2.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // Momssats 2
                iTender.setTaxRate2(iTaxRate2.getValue());
                updateTaxTexts();
                updateSumFields();
            }
        });
        iTaxRate3.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // Momssats
                iTender.setTaxRate3(iTaxRate3.getValue());
                updateTaxTexts();
                updateSumFields();
            }
        });

        /**
         *
         */
        iTaxFree.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Momsfritt
                iTender.setTaxFree(iTaxFree.isSelected());
                updateSumFields();
            }
        });

        iCurrencyCalculatorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                SSCurrency iCompanyCurrency = SSDB.getInstance().getCurrentCompany().getCurrency();
                SSCurrency iCurrentCurrency = iCurrency.getSelected();

                if( iCompanyCurrency == null || iCurrentCurrency == null) {
                    new SSInformationDialog(iOwner, "tenderframe.selectcurrency");
                    return;
                }
                SSExchangeRateDialog.showDialog(iOwner, iTender, iCompanyCurrency, iCurrentCurrency , iModel);
            }
        });


        SSButtonGroup iGroup = new SSButtonGroup(true);
        iGroup.add( iEuSaleCommodity );
        iGroup.add( iEuSaleYhirdPartCommodity );

        iInputVerifier = new SSInputVerifier();

        iInputVerifier.add(iCustomer);
        iInputVerifier.add(iCustomerName);
        iInputVerifier.addListener(new SSInputVerifier.SSVerifierListener() {
            public void updated(SSInputVerifier iVerifier, boolean iValid) {
                iButtonPanel.getOkButton().setEnabled(iValid);
            }
        });

        iSavecustomerandproducts.setSelected(true);

        addKeyListeners();


    }

    /**
     *
     * @return
     */
    public boolean isValid() {
        return iInputVerifier.isValid();
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
     * @param pActionListener
     */

    public void addOkAction(ActionListener pActionListener) {
        iButtonPanel.addOkActionListener(pActionListener);
    }

    /**
     *
     * @param pActionListener
     */
    public void addCancelAction(ActionListener pActionListener) {
        iButtonPanel.addCancelActionListener(pActionListener);
    }


    /**
     * @param pTender
     */
    public void setTender(SSTender pTender) {
        iTender = pTender;


        iModel.setObjects(pTender.getRows());

        // Offfertnummer
        iNumber.setValue(iTender.getNumber());
        // Offertdatum
        iDate.setDate(iTender.getDate());
        // Giltig tom
        iExpires.setDate(iTender.getExpires());

        // Kund nummer
        iCustomer.setText(iTender.getCustomerNr());
        // Kund namn
        iCustomerName.setText(iTender.getCustomerName());
        // Vår kontaktperson:
        iOurContactPerson.setText(iTender.getOurContactPerson());
        // Er kontaktperson:
        iYourContactPerson.setText(iTender.getYourContactPerson());
        // Dröjsmålsränta
        iDelayInterest.setValue(iTender.getDelayInterest());

        // Valuta
        iCurrency.setSelected(iTender.getCurrency());
        // Valutakurs
        iCurrencyRate.setValue( iTender.getCurrencyRate() );
        // Betalningsvilkor
        iPaymentTerm.setSelected(iTender.getPaymentTerm());
        // Leveransvilkor
        iDeliveryTerm.setSelected(iTender.getDeliveryTerm());
        // Leveranssätt
        iDeliveryWay.setSelected(iTender.getDeliveryWay());

        for (SSCustomer pCustomer : SSDB.getInstance().getCustomers()) {
            if (pCustomer.getNumber().equals(iCustomer.getText())) {
                iModel.setCustomer(pCustomer);
            }
        }

        // Momsfritt
        iTaxFree.setSelected(iTender.getTaxFree());
        // Momssats 1
        iTaxRate1.setValue( iTender.getTaxRate1() );
        // Momssats 2
        iTaxRate2.setValue( iTender.getTaxRate2() );
        // Momssats 3
        iTaxRate3.setValue( iTender.getTaxRate3() );
        // EU-försäljning varor
        iEuSaleCommodity            .setSelected(iTender.getEuSaleCommodity());
        // EU-försäljning trepart varor
        iEuSaleYhirdPartCommodity   .setSelected(iTender.getEuSaleThirdPartCommodity());


        // Offerttext
        iText.setText(iTender.getText());

        // Fakturaadress
        iInvoiceAddress.setAdress(iTender.getInvoiceAddress());
        // Leveransadress
        iDeliveryAddress.setAdress(iTender.getDeliveryAddress());
        // Standard konton
        iDefaultAccounts.setDefaultAccounts( iTender.getDefaultAccounts());

        updateSumFields();
        updateTaxTexts();

        iUseInvoiceForDelivery.setSelected(  iTender.getDeliveryAddress().equals(  iTender.getInvoiceAddress() ) );

        iDeliveryAddress.setEnabled( !iUseInvoiceForDelivery.isSelected());

        iInputVerifier.update();
    }

    /**
     * @return
     */
    public SSTender getTender() {

        // Offertdatum
        iTender.setDate(iDate.getDate());
        // Giltig tom
        iTender.setExpires(iExpires.getDate());
        // Kund nummer
        iTender.setCustomerNr(iCustomer.getText());
        // Kund namn
        iTender.setCustomerName(iCustomerName.getText());
        // Vår kontaktperson:
        iTender.setOurContactPerson(iOurContactPerson.getText());
        // Er kontaktperson:
        iTender.setYourContactPerson(iYourContactPerson.getText());
        // Dröjsmålsränta
        iTender.setDelayInterest(iDelayInterest.getValue());

        // Valuta
        iTender.setCurrency(iCurrency.getSelected());
        // Valutakurs
        iTender.setCurrencyRate( iCurrencyRate.getValue() );
        // Betalningsvilkor
        iTender.setPaymentTerm(iPaymentTerm.getSelected());
        // Leveransvilkor
        iTender.setDeliveryTerm(iDeliveryTerm.getSelected());
        // Leveranssätt
        iTender.setDeliveryWay(iDeliveryWay.getSelected());
        // EU-försäljning varor
        iTender.setEuSaleCommodity(iEuSaleCommodity.isSelected());
        // EU-försäljning trepart varor
        iTender.setEuSaleYhirdPartCommodity(iEuSaleYhirdPartCommodity.isSelected());

        // Momsfritt
        iTender.setTaxFree(iTaxFree.isSelected());
        // Momssats 1
        iTender.setTaxRate1(iTaxRate1.getValue());
        // Momssats 2
        iTender.setTaxRate2(iTaxRate2.getValue());
        // Momssats
        iTender.setTaxRate3(iTaxRate3.getValue());

        // Offerttext
        iTender.setText(iText.getText());

        // Standard konton
        iTender.setDefaultAccounts(iDefaultAccounts.getDefaultAccounts());

        // Fakturaadress
        iTender.setInvoiceAddress(iInvoiceAddress.getAddress());
        if (iUseInvoiceForDelivery.isSelected()) {
            // Leveransadress
            iTender.setDeliveryAddress(iInvoiceAddress.getAddressCloned());
        } else {
            // Leveransadress
            iTender.setDeliveryAddress(iDeliveryAddress.getAddress());
        }

        return iTender;

    }

    /**
     *
     * @return
     */
    public boolean doSaveCustomerAndProducts() {
        return iSavecustomerandproducts.isSelected();
    }


    /**
     *
     */
    private void updateTaxTexts(){
        SSTaxCodeCellEditor   iEditor   = new SSTaxCodeCellEditor();
        SSTaxCodeCellRenderer iRenderer = new SSTaxCodeCellRenderer();

        String iTaxText = SSBundle.getBundle().getString("tenderframe.tax");

        BigDecimal iTaxRate1 = this.iTaxRate1.getValue() == null ? new BigDecimal(25) : this.iTaxRate1.getValue();
        BigDecimal iTaxRate2 = this.iTaxRate2.getValue() == null ? new BigDecimal(12) : this.iTaxRate2.getValue();
        BigDecimal iTaxRate3 = this.iTaxRate3.getValue() == null ? new BigDecimal( 6) : this.iTaxRate3.getValue();

        iEditor.setValue(SSTaxCode.TAXRATE_0,  new BigDecimal(0));
        iEditor.setValue(SSTaxCode.TAXRATE_1, iTaxRate1);
        iEditor.setValue(SSTaxCode.TAXRATE_2, iTaxRate2);
        iEditor.setValue(SSTaxCode.TAXRATE_3, iTaxRate3);

        iRenderer.setValue(SSTaxCode.TAXRATE_0,  new BigDecimal(0));
        iRenderer.setValue(SSTaxCode.TAXRATE_1, iTaxRate1);
        iRenderer.setValue(SSTaxCode.TAXRATE_2, iTaxRate2);
        iRenderer.setValue(SSTaxCode.TAXRATE_3, iTaxRate3);

        NumberFormat iFormat = NumberFormat.getNumberInstance();

        iTaxLabel1.setText( iTaxText + ' ' + iFormat.format(iTaxRate1) + '%');
        iTaxLabel2.setText( iTaxText + ' ' + iFormat.format(iTaxRate2) + '%');
        iTaxLabel3.setText( iTaxText + ' ' + iFormat.format(iTaxRate3) + '%');

        iTable.setDefaultEditor  (SSTaxCode.class, iEditor);
        iTable.setDefaultRenderer(SSTaxCode.class, iRenderer);

        iModel.fireTableDataChanged();
    }

    /**
     *
     */
    private void updateSumFields(){
        BigDecimal                 iNetSum   = SSTenderMath.getNetSum    (iTender);
        Map<SSTaxCode, BigDecimal> iTaxSum   = SSTenderMath.getTaxSum    (iTender);
        BigDecimal                 iTotalSum = SSTenderMath.getTotalSum  (iTender);
        BigDecimal                 iRounding = SSTenderMath.getRounding  (iTender);

        this.iNetSum     .setValue(iNetSum );
        iTaxSum1.setValue(iTaxSum.get(SSTaxCode.TAXRATE_1));
        iTaxSum2.setValue(iTaxSum.get(SSTaxCode.TAXRATE_2));
        iTaxSum3.setValue(iTaxSum.get(SSTaxCode.TAXRATE_3));
        if(!SSDB.getInstance().getCurrentCompany().isRoundingOff()) iRoundingSum.setValue(iRounding);
        this.iTotalSum   .setValue(iTotalSum);
    }

    /**
     *
     * @param iSelected
     */
    public void setSavecustomerandproductsSelected(boolean iSelected){
        iSavecustomerandproducts.setSelected(iSelected);

    }

    /**
     * Used to clean up references making sure the garbage collector
     * is able to clean up the object.
     */
    public void dispose() {
        iModel.setCustomer(null);
        iPanel.removeAll();
        iPanel=null;
        iButtonPanel.dispose();
        iButtonPanel=null;
        iTable.dispose();
        iTable=null;
        iNumber.removeAll();
        iNumber=null;
        iDate.dispose();
        iDate=null;
        iExpires.dispose();
        iExpires=null;
        iCustomer.dispose();
        iCustomer=null;
        iCustomerName=null;

        iTaxFree=null;
        iDelayInterest=null;
        iDeliveryAddress=null;
        iInvoiceAddress=null;
        iText.removeAll();
        iText=null;
        iYourContactPerson.removeAll();
        iYourContactPerson=null;
        iOurContactPerson.removeAll();
        iOurContactPerson=null;
        PropertyChangeListener[] iPropertyChangeListeners=iTaxRate1.getPropertyChangeListeners();
        for (PropertyChangeListener iPropertyChangeListener : iPropertyChangeListeners) {
            iTaxRate1.removePropertyChangeListener(iPropertyChangeListener);
        }
        iTaxRate1.removeAll();
        iTaxRate1=null;
        iPropertyChangeListeners=iTaxRate2.getPropertyChangeListeners();
        for (PropertyChangeListener iPropertyChangeListener : iPropertyChangeListeners) {
            iTaxRate2.removePropertyChangeListener(iPropertyChangeListener);
        }
        iTaxRate2.removeAll();
        iTaxRate2=null;
        iPropertyChangeListeners=iTaxRate3.getPropertyChangeListeners();
        for (PropertyChangeListener iPropertyChangeListener : iPropertyChangeListeners) {
            iTaxRate3.removePropertyChangeListener(iPropertyChangeListener);
        }
        iTaxRate3.removeAll();
        iTaxRate3=null;
        iEuSaleCommodity.removeAll();
        iEuSaleCommodity=null;
        iEuSaleYhirdPartCommodity.removeAll();
        iEuSaleYhirdPartCommodity=null;
        iDeliveryWay.dispose();
        iDeliveryWay=null;
        iDeliveryTerm.dispose();
        iDeliveryTerm=null;
        iPaymentTerm.dispose();
        iPaymentTerm=null;
        iCurrency.dispose();
        iCurrency=null;

        iDefaultAccounts.dispose();
        iDefaultAccounts=null;

        iCurrencyCalculatorButton.dispose();
        iCurrencyCalculatorButton=null;

        iNetSum.removeAll();
        iNetSum=null;
        iTaxSum1.removeAll();
        iTaxSum1=null;
        iTaxSum2.removeAll();
        iTaxSum2=null;
        iTaxSum3.removeAll();
        iTaxSum3=null;
        iRoundingSum.removeAll();
        iRoundingSum=null;
        iTotalSum.removeAll();
        iTotalSum=null;

        iTaxLabel1=null;
        iTaxLabel2=null;
        iTaxLabel3=null;

        iSavecustomerandproducts=null;

        iModel=null;

        iInputVerifier=null;
        iUseInvoiceForDelivery=null;

        iCurrencyRate.removeAll();
        iCurrencyRate=null;


    }

    public void addKeyListeners(){

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                iCustomer.getComponent(0).requestFocusInWindow();
            }
        });

        iCustomer.getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iCustomerName.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iCustomerName.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iOurContactPerson.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iOurContactPerson.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iYourContactPerson.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iYourContactPerson.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iDate.getEditor().getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iDate.getEditor().getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iExpires.getEditor().getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iExpires.getEditor().getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iPaymentTerm.getComboBox().getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iPaymentTerm.getComboBox().getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iDeliveryTerm.getComboBox().getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iDeliveryTerm.getComboBox().getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iDeliveryWay.getComboBox().getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iDeliveryWay.getComboBox().getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iCurrency.getComboBox().getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iCurrency.getComboBox().getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iCurrencyRate.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iCurrencyRate.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iTable.requestFocusInWindow();
                            iTable.changeSelection(0,0,false,false);
                        }
                    });
                }
            }
        });

        iButtonPanel.getOkButton().addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iButtonPanel.getCancelButton().requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iButtonPanel.getCancelButton().addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iButtonPanel.getOkButton().requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iNumber.addFocusListener(new FocusAdapter(){
            @Override
            public void focusGained(FocusEvent e){
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        iCustomer.getComponent(0).requestFocusInWindow();
                    }
                });
            }
        });

        iInvoiceAddress.addKeyListeners();
        iDeliveryAddress.addKeyListeners();

        iUseInvoiceForDelivery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(!iUseInvoiceForDelivery.isSelected())
                    iDeliveryAddress.setFocus();
                else
                    iInvoiceAddress.setFocus();
            }
        });
    }

    /**
     * Contribution Rate
     */
    private class ContributionRateColumn extends SSTableColumn<SSSaleRow>{

        /**
         *
         */
        public ContributionRateColumn() {
            super(SSBundle.getBundle().getString("salerowtable.column.12") );
        }

        private BigDecimal getExchangeRate(){
            BigDecimal iValue = iCurrencyRate.getValue();

            if(iValue != null){
                return iValue;
            }

            SSCurrency iSelected = iCurrency.getSelected();

            if(iSelected != null){
                return iSelected.getExchangeRate();
            }
            return null;
        }

        /**
         *
         * @param iRow
         * @return the value
         */
        @Override
        public Object getValue(SSSaleRow iRow) {
            SSProduct iProduct   = iRow.getProduct();

            if(iProduct == null) return null;

            BigDecimal iInprice       = SSProductMath.getInprice(iProduct, iDate.getDate());
            BigDecimal iUnitprice     = iRow.getUnitprice();
            BigDecimal iExchangeRate  = getExchangeRate();

            if(iInprice == null || iUnitprice == null ) return null;

            if(iExchangeRate != null) {
                return iUnitprice.multiply(iExchangeRate).subtract(iInprice);
            } else {
                return iUnitprice.subtract(iInprice);
            }
        }

        @Override
        public void setValue(SSSaleRow iObject, Object iValue) {

        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.tender.panel.SSTenderPanel");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iCurrency=").append(iCurrency);
        sb.append(", iCurrencyCalculatorButton=").append(iCurrencyCalculatorButton);
        sb.append(", iCurrencyRate=").append(iCurrencyRate);
        sb.append(", iCustomer=").append(iCustomer);
        sb.append(", iCustomerName=").append(iCustomerName);
        sb.append(", iDate=").append(iDate);
        sb.append(", iDefaultAccounts=").append(iDefaultAccounts);
        sb.append(", iDelayInterest=").append(iDelayInterest);
        sb.append(", iDeliveryAddress=").append(iDeliveryAddress);
        sb.append(", iDeliveryTerm=").append(iDeliveryTerm);
        sb.append(", iDeliveryWay=").append(iDeliveryWay);
        sb.append(", iEuSaleCommodity=").append(iEuSaleCommodity);
        sb.append(", iEuSaleYhirdPartCommodity=").append(iEuSaleYhirdPartCommodity);
        sb.append(", iExpires=").append(iExpires);
        sb.append(", iInputVerifier=").append(iInputVerifier);
        sb.append(", iInvoiceAddress=").append(iInvoiceAddress);
        sb.append(", iModel=").append(iModel);
        sb.append(", iNetSum=").append(iNetSum);
        sb.append(", iNumber=").append(iNumber);
        sb.append(", iOurContactPerson=").append(iOurContactPerson);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iPaymentTerm=").append(iPaymentTerm);
        sb.append(", iRoundingSum=").append(iRoundingSum);
        sb.append(", iSavecustomerandproducts=").append(iSavecustomerandproducts);
        sb.append(", iTable=").append(iTable);
        sb.append(", iTaxFree=").append(iTaxFree);
        sb.append(", iTaxLabel1=").append(iTaxLabel1);
        sb.append(", iTaxLabel2=").append(iTaxLabel2);
        sb.append(", iTaxLabel3=").append(iTaxLabel3);
        sb.append(", iTaxRate1=").append(iTaxRate1);
        sb.append(", iTaxRate2=").append(iTaxRate2);
        sb.append(", iTaxRate3=").append(iTaxRate3);
        sb.append(", iTaxSum1=").append(iTaxSum1);
        sb.append(", iTaxSum2=").append(iTaxSum2);
        sb.append(", iTaxSum3=").append(iTaxSum3);
        sb.append(", iTender=").append(iTender);
        sb.append(", iText=").append(iText);
        sb.append(", iTotalSum=").append(iTotalSum);
        sb.append(", iUseInvoiceForDelivery=").append(iUseInvoiceForDelivery);
        sb.append(", iYourContactPerson=").append(iYourContactPerson);
        sb.append('}');
        return sb.toString();
    }
}
