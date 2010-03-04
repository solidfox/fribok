package se.swedsoft.bookkeeping.gui.creditinvoice.panel;

import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.data.SSCreditInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.common.*;
import se.swedsoft.bookkeeping.gui.util.components.*;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSTaxCodeCellEditor;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSTaxCodeCellRenderer;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSTraversalAction;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSDeleteAction;
import se.swedsoft.bookkeeping.gui.util.SSInputVerifier;
import se.swedsoft.bookkeeping.gui.util.SSSelectionListener;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.model.SSCurrencyTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSDeliveryWayTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSDeliveryTermTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSPaymentTermTableModel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.company.panel.SSAdressPanel;
import se.swedsoft.bookkeeping.gui.company.panel.SSDefaultAccountPanel;
import se.swedsoft.bookkeeping.gui.invoice.util.SSInvoiceRowTableModel;
import se.swedsoft.bookkeeping.gui.voucher.util.SSVoucherRowTableModelOld;
import se.swedsoft.bookkeeping.gui.customer.util.SSCustomerTableModel;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import java.util.Map;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.DecimalFormat;

/**
 * User: Andreas Lago
 * Date: 2006-mar-24
 * Time: 11:21:13
 */
public class SSCreditInvoicePanel {

    private SSCreditInvoice iCreditInvoice;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JFormattedTextField iNumber;

    private JFormattedTextField iCrediting;

    private SSBigDecimalTextField iCurrencyRate;

    private SSDateChooser iDate;

    private SSTableComboBox<SSCustomer> iCustomer;

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

    private JCheckBox iEntered;


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

    private SSTable iVoucherTable;

    private SSVoucherRowTableModelOld iVoucherTableModel;

    private JButton iRefreshVoucher;

    private SSInputVerifier iInputVerifier;
    private JCheckBox isStockInfluencing;


    /**
     *
     * @param iOwner
     */
    public SSCreditInvoicePanel(final SSDialog iOwner) {
        iNumber.setFormatterFactory( new DefaultFormatterFactory(new NumberFormatter( new DecimalFormat("0") ) ) );
        iNumber.setHorizontalAlignment(JTextField.RIGHT);

        iTable.setColorReadOnly(true);
        iTable.setColumnSortingEnabled(false);
        iTable.setSingleSelect();

        iModel = new SSInvoiceRowTableModel();
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_PRODUCT      , true);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_DESCRIPTION  , true);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_UNITPRICE    , true);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_QUANTITY     , true);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_UNIT         , true);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_DISCOUNT     , true);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_SUM          , false);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_TAX          , true);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_ACCOUNT      , true);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_PROJECT      , true);
        iModel.addColumn(SSInvoiceRowTableModel.COLUMN_RESULTUNIT   , true);
        iModel.setupTable(iTable);

        iModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                updateSumFields();
            }
        });
        iVoucherTableModel = new SSVoucherRowTableModelOld(false, true);

        iVoucherTable.setModel(iVoucherTableModel);

        SSVoucherRowTableModelOld.setupTable(iVoucherTable, iVoucherTableModel);


        iCustomer.setModel(SSCustomerTableModel.getDropDownModel());
        iCustomer.setSearchColumns(0,1);
        iCustomer.setAllowCustomValues(true);

        iCustomer.addSelectionListener(new SSSelectionListener<SSCustomer>() {
            public void selected(SSCustomer selected) {
                if(selected != null){
                    iModel.setCustomer(selected);
                    iCreditInvoice.setCustomer(selected);
                    if(selected.getDiscount() != null){
                        for (SSSaleRow iRow : iModel.getObjects()) {
                            iRow.setDiscount(selected.getDiscount().doubleValue() == (new BigDecimal(0.0)).doubleValue() ? null : selected.getDiscount());
                        }
                    }
                    setCreditInvoice(iCreditInvoice);
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
                iCreditInvoice.setTaxRate1(iTaxRate1.getValue());

                updateTaxTexts();
                updateSumFields();
            }
        });
        iTaxRate2.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // Momssats 2
                iCreditInvoice.setTaxRate2(iTaxRate2.getValue());

                updateTaxTexts();
                updateSumFields();
            }
        });
        iTaxRate3.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // Momssats
                iCreditInvoice.setTaxRate3(iTaxRate3.getValue());

                updateTaxTexts();
                updateSumFields();
            }
        });
        iTaxFree.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Momsfritt
                iCreditInvoice.setTaxFree(iTaxFree.isSelected());

                updateSumFields();
            }
        });

        iRefreshVoucher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSVoucher iVoucher = iCreditInvoice.generateVoucher();

                iVoucherTableModel.setVoucher(iVoucher);
            }
        });


        iInputVerifier = new SSInputVerifier();

        iInputVerifier.add(iCustomer);
        iInputVerifier.add(iCustomerName);
        iInputVerifier.addListener(new SSInputVerifier.SSVerifierListener() {
            public void updated(SSInputVerifier iVerifier, boolean iValid) {
                JComponent iCurrent = iVerifier.getCurrentComponent();

                if(iCurrent == iCustomer);

                iButtonPanel.getOkButton().setEnabled(iValid);
            }
        });

        addKeyListeners();


    }


    public void dispose() {
        iModel.setCustomer(null);
        iPanel.removeAll();
        iPanel=null;
        iButtonPanel.dispose();
        iButtonPanel=null;
        iCrediting.removeAll();
        iCrediting=null;
        iTable.dispose();
        iTable=null;
        iNumber.removeAll();
        iNumber=null;
        iCurrencyRate.removeAll();
        iCurrencyRate=null;
        iDate.dispose();
        iDate=null;
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
        iVoucherTable.dispose();
        iVoucherTable=null;
        ActionListener[] iActionListeners = iRefreshVoucher.getActionListeners();
        for (ActionListener iActionListener : iActionListeners) {
            iRefreshVoucher.removeActionListener(iActionListener);
        }
        iRefreshVoucher.removeAll();
        iRefreshVoucher=null;


        //iModel.dispose();
        iModel=null;
        iVoucherTableModel=null;

        iEntered=null;
        isStockInfluencing=null;
        iInputVerifier=null;
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
     * @param pOrder
     */
    public void setCreditInvoice(SSCreditInvoice pOrder) {
        this.iCreditInvoice = pOrder;

        iVoucherTableModel.setVoucher(iCreditInvoice.getVoucher());

        iModel.setObjects(iCreditInvoice.getRows());

        // Fakturatnummer
        iNumber.setValue(iCreditInvoice.getNumber());
        // Ordernummer
        iCrediting.setValue(iCreditInvoice.getCreditingNr());
        // iCrediting.setSelected(iCreditInvoice.getCrediting( SSDB.getInstance().getInvoices() ) );
        // Offertdatum
        iDate.setDate(iCreditInvoice.getDate());
        // Kund nummer
        iCustomer.setText(iCreditInvoice.getCustomerNr());
        for (SSCustomer pCustomer : SSDB.getInstance().getCustomers()) {
            if (pCustomer.getNumber().equals(iCustomer.getText())) {
                iModel.setCustomer(pCustomer);
            }
        }
        // Kund namn
        iCustomerName.setText(iCreditInvoice.getCustomerName());
        // Vår kontaktperson:
        iOurContactPerson.setText(iCreditInvoice.getOurContactPerson());
        // Er kontaktperson:
        iYourContactPerson.setText(iCreditInvoice.getYourContactPerson());
        // Dröjsmålsränta
        iDelayInterest.setValue(iCreditInvoice.getDelayInterest());

        // Valuta
        iCurrency.setSelected(iCreditInvoice.getCurrency());
        // Valutakurs
        iCurrencyRate.setValue( iCreditInvoice.getCurrencyRate() );
        // Betalningsvilkor
        iPaymentTerm.setSelected(iCreditInvoice.getPaymentTerm(), true);
        // Leveransvilkor
        iDeliveryTerm.setSelected(iCreditInvoice.getDeliveryTerm());
        // Leveranssätt
        iDeliveryWay.setSelected(iCreditInvoice.getDeliveryWay());

        // Bokförd
        iEntered.setSelected( iCreditInvoice.isEntered() );
        // Lagerför
        isStockInfluencing.setSelected( iCreditInvoice.isStockInfluencing() );

        // Momsfritt
        iTaxFree.setSelected(iCreditInvoice.getTaxFree());
        // Momssats 1
        iTaxRate1.setValue( iCreditInvoice.getTaxRate1() );
        // Momssats 2
        iTaxRate2.setValue( iCreditInvoice.getTaxRate2() );
        // Momssats 3
        iTaxRate3.setValue( iCreditInvoice.getTaxRate3() );
        // EU-försäljning varor
        iEuSaleCommodity.setSelected(iCreditInvoice.getEuSaleCommodity());
        // EU-försäljning trepart varor
        iEuSaleYhirdPartCommodity.setSelected(iCreditInvoice.getEuSaleThirdPartCommodity());


        // Offerttext
        iText.setText(iCreditInvoice.getText());

        // Fakturaadress
        iInvoiceAddress.setAdress(iCreditInvoice.getInvoiceAddress());
        // Leveransadress
        iDeliveryAddress.setAdress(iCreditInvoice.getDeliveryAddress());
        // Standard konton
        iDefaultAccounts.setDefaultAccounts( iCreditInvoice.getDefaultAccounts());

        updateSumFields();
        updateTaxTexts();

        iSavecustomerandproducts.setSelected(true);

        iInputVerifier.update();
    }

    /**
     * @return
     */
    public SSCreditInvoice getCreditInvoice() {
        // Offertdatum
        iCreditInvoice.setDate(iDate.getDate());
        // Kund nummer
        iCreditInvoice.setCustomerNr(iCustomer.getText());
        // Kund namn
        iCreditInvoice.setCustomerName(iCustomerName.getText());
        // Vår kontaktperson:
        iCreditInvoice.setOurContactPerson(iOurContactPerson.getText());
        // Er kontaktperson:
        iCreditInvoice.setYourContactPerson(iYourContactPerson.getText());
        // Dröjsmålsränta
        iCreditInvoice.setDelayInterest(iDelayInterest.getValue());
        // Valuta
        iCreditInvoice.setCurrency(iCurrency.getSelected());
        // Valutakurs
        iCreditInvoice.setCurrencyRate( iCurrencyRate.getValue() );
        // Betalningsvilkor
        iCreditInvoice.setPaymentTerm(iPaymentTerm.getSelected());
        // Leveransvilkor
        iCreditInvoice.setDeliveryTerm(iDeliveryTerm.getSelected());
        // Leveranssätt
        iCreditInvoice.setDeliveryWay(iDeliveryWay.getSelected());
        // Momsfritt
        iCreditInvoice.setTaxFree(iTaxFree.isSelected());

        // Momssats 1
        iCreditInvoice.setTaxRate1(iTaxRate1.getValue());
        // Momssats 2
        iCreditInvoice.setTaxRate2(iTaxRate2.getValue());
        // Momssats
        iCreditInvoice.setTaxRate3(iTaxRate3.getValue());
        // Bokförd
        iCreditInvoice.setEntered( iEntered.isSelected() );
        // Lagerför
        iCreditInvoice.setStockInfluencing( isStockInfluencing.isSelected() );

        // Offerttext
        iCreditInvoice.setText(iText.getText());

        // Fakturaadress
        iCreditInvoice.setInvoiceAddress(iInvoiceAddress.getAddress());
        // Leveransadress
        iCreditInvoice.setDeliveryAddress(iDeliveryAddress.getAddress());
        // Standard konton
        iCreditInvoice.setDefaultAccounts(iDefaultAccounts.getDefaultAccounts());

        // Generera verifikationen
        iCreditInvoice.generateVoucher();

        return iCreditInvoice;

    }

    public void addKeyListeners(){

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                iCustomerName.requestFocusInWindow();
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
                        iCustomerName.requestFocusInWindow();
                    }
                });
            }
        });

        iInvoiceAddress.addKeyListeners();
        iDeliveryAddress.addKeyListeners();
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


        NumberFormat iFormat = NumberFormat.getNumberInstance();

        iEditor.setValue(SSTaxCode.TAXRATE_0,  new BigDecimal(0));
        iEditor.setValue(SSTaxCode.TAXRATE_1, iTaxRate1);
        iEditor.setValue(SSTaxCode.TAXRATE_2, iTaxRate2);
        iEditor.setValue(SSTaxCode.TAXRATE_3, iTaxRate3);

        iRenderer.setValue(SSTaxCode.TAXRATE_0,  new BigDecimal(0));
        iRenderer.setValue(SSTaxCode.TAXRATE_1, iTaxRate1);
        iRenderer.setValue(SSTaxCode.TAXRATE_2, iTaxRate2);
        iRenderer.setValue(SSTaxCode.TAXRATE_3, iTaxRate3);

        iTaxLabel1.setText( iTaxText + " " + iFormat.format(iTaxRate1) + "%" );
        iTaxLabel2.setText( iTaxText + " " + iFormat.format(iTaxRate2) + "%" );
        iTaxLabel3.setText( iTaxText + " " + iFormat.format(iTaxRate3) + "%" );

        iTable.setDefaultEditor  (SSTaxCode.class, iEditor);
        iTable.setDefaultRenderer(SSTaxCode.class, iRenderer);

        iModel.fireTableDataChanged();
    }

    /**
     *
     */
    private void updateSumFields(){
        BigDecimal                 iNetSum   = SSInvoiceMath.getNetSum    (iCreditInvoice);
        Map<SSTaxCode, BigDecimal> iTaxSum   = SSInvoiceMath.getTaxSum    (iCreditInvoice);
        BigDecimal                 iTotalSum = SSInvoiceMath.getTotalSum  (iCreditInvoice);
        BigDecimal                 iRounding = SSInvoiceMath.getRounding  (iCreditInvoice);

        this.iNetSum     .setValue(iNetSum );
        this.iTaxSum1    .setValue(iTaxSum.get(SSTaxCode.TAXRATE_1));
        this.iTaxSum2    .setValue(iTaxSum.get(SSTaxCode.TAXRATE_2));
        this.iTaxSum3    .setValue(iTaxSum.get(SSTaxCode.TAXRATE_3));
        if(!SSDB.getInstance().getCurrentCompany().isRoundingOff()) this.iRoundingSum.setValue(iRounding);
        this.iTotalSum   .setValue(iTotalSum);
    }

    /**
     *
     * @param iSelected
     */
    public void setSavecustomerandproductsSelected(boolean iSelected){
        iSavecustomerandproducts.setSelected(iSelected);

    }


}
