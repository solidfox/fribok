package se.swedsoft.bookkeeping.gui.periodicinvoice.panel;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.common.*;
import se.swedsoft.bookkeeping.gui.util.*;
import se.swedsoft.bookkeeping.gui.util.model.SSCurrencyTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSDeliveryWayTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSDeliveryTermTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSPaymentTermTableModel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSInformationDialog;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSTaxCodeCellEditor;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSTaxCodeCellRenderer;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSTraversalAction;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSDeleteAction;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.components.*;
import se.swedsoft.bookkeeping.gui.company.panel.SSAdressPanel;
import se.swedsoft.bookkeeping.gui.company.panel.SSDefaultAccountPanel;
import se.swedsoft.bookkeeping.gui.invoice.util.SSInvoiceRowTableModel;
import se.swedsoft.bookkeeping.gui.voucher.util.SSVoucherRowTableModelOld;
import se.swedsoft.bookkeeping.gui.customer.util.SSCustomerTableModel;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.tender.dialog.SSExchangeRateDialog;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSDateMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * User: Andreas Lago
 * Date: 2006-mar-24
 * Time: 11:21:13
 */
public class SSPeriodicInvoicePanel {

    private SSPeriodicInvoice iPeriodicInvoice;

    private SSInvoice iInvoice;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;


    private JFormattedTextField iNumber;

    private SSBigDecimalTextField iCurrencyRate;

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

    private JTextField iYourOrderNumber;

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

    private SSTable iVoucherTable;

    private SSVoucherRowTableModelOld iVoucherTableModel;


    private JButton iRefreshVoucher;

    private SSInputVerifier iInputVerifier;

    // Periodfaktura fält
    private SSDateChooser iDate;

    private SSIntegerTextField iCount;

    private SSIntegerTextField iPeriod;

    private JTextField iDescription;

    private JCheckBox iAppendPeriod;

    private SSDateChooser iPeriodStart;

    private SSDateChooser iPeriodEnd;

    private SSTable iInvoices;
    private JTabbedPane iTabbedPane;
    private JCheckBox isStockInfluencing;

    private JCheckBox iAddInfo;
    private JTextField iInformation;
    private JTextField textField1;


    /**
     *
     * @param iOwner
     */
    public SSPeriodicInvoicePanel(final SSDialog iOwner) {
        
        iTable.setColorReadOnly(true);
        iTable.setColumnSortingEnabled(false);

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
                    iInvoice.setCustomer(selected);
                    iInvoice.setCurrencyRate(selected.getInvoiceCurrency() == null ? new BigDecimal(1.0) : selected.getInvoiceCurrency().getExchangeRate());
                    if(selected.getDiscount() != null){
                        for (SSSaleRow iRow : iModel.getObjects()) {
                            iRow.setDiscount(selected.getDiscount().doubleValue() == (new BigDecimal(0.0)).doubleValue() ? null : selected.getDiscount());
                        }
                    }
                    setPeriodicInvoice(iPeriodicInvoice);
                }
            }
        });
        new SSTraversalAction(iTable){
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

        iCurrencyRate.addPropertyChangeListener("value", new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent e){
                iInvoice.setCurrencyRate(iCurrencyRate.getValue());
                updateSumFields();
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
                iInvoice.setTaxRate1(iTaxRate1.getValue());

                updateTaxTexts();
                updateSumFields();
            }
        });
        iTaxRate2.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // Momssats 2
                iInvoice.setTaxRate2(iTaxRate2.getValue());

                updateTaxTexts();
                updateSumFields();
            }
        });
        iTaxRate3.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                // Momssats
                iInvoice.setTaxRate3(iTaxRate3.getValue());

                updateTaxTexts();
                updateSumFields();
            }
        });
        iTaxFree.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Momsfritt
                iInvoice.setTaxFree(iTaxFree.isSelected());

                updateSumFields();
            }
        });


        iCurrencyCalculatorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSCurrency iCompanyCurrency = SSDB.getInstance().getCurrentCompany().getCurrency();
                SSCurrency iCurrentCurrency = iCurrency.getSelected();

                if( iCompanyCurrency == null || iCurrentCurrency == null) {
                    new SSInformationDialog(iOwner, "invoiceframe.selectcurrency");
                    return;
                }
                SSExchangeRateDialog.showDialog(iOwner, iInvoice, iCompanyCurrency, iCurrentCurrency , iModel);
            }
        });



        iRefreshVoucher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSVoucher iVoucher = iInvoice.generateVoucher();

                iVoucherTableModel.setVoucher(iVoucher);
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
                // JComponent iCurrent = iVerifier.getCurrentComponent();

                iButtonPanel.getOkButton().setEnabled(iValid);
            }
        });


        iDate.addChangeListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Date    iDate   = SSPeriodicInvoicePanel.this.iDate  .getDate();
                Integer iPeriod = SSPeriodicInvoicePanel.this.iPeriod.getValue();

                if(iPeriod == null || iDate == null) return;

                Date iPeriodStart = SSDateMath.getFirstDayInMonth(iDate );
                Date iPeriodEnd   = SSDateMath.addMonths(iPeriodStart, iPeriod );

                SSPeriodicInvoicePanel.this.iPeriodStart.setDate( iPeriodStart );
                SSPeriodicInvoicePanel.this.iPeriodEnd  .setDate( iPeriodEnd );
            }
        });

        iCount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Date    iDate   = SSPeriodicInvoicePanel.this.iDate  .getDate();
                Integer iPeriod = SSPeriodicInvoicePanel.this.iPeriod.getValue();

                if(iPeriod == null || iDate == null) return;

                Date iPeriodStart = SSDateMath.getFirstDayInMonth(iDate );
                Date iPeriodEnd   = SSDateMath.addMonths(iPeriodStart, iPeriod );

                SSPeriodicInvoicePanel.this.iPeriodStart.setDate( iPeriodStart );
                SSPeriodicInvoicePanel.this.iPeriodEnd  .setDate( iPeriodEnd );
            }
        });

        iSavecustomerandproducts.setSelected(true);

        addKeyListeners();
    }

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
        iYourOrderNumber=null;
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
        iVoucherTable.dispose();
        iVoucherTable=null;
        ActionListener[] iActionListeners = iRefreshVoucher.getActionListeners();
        for (ActionListener iActionListener : iActionListeners) {
            iRefreshVoucher.removeActionListener(iActionListener);
        }
        iRefreshVoucher.removeAll();
        iRefreshVoucher=null;
        iVoucherTableModel=null;
        isStockInfluencing=null;
        iCount.dispose();
        iCount=null;
        iPeriod.dispose();
        iPeriod=null;
        iAppendPeriod.removeAll();
        iAppendPeriod=null;
        iPeriodStart.dispose();
        iPeriodStart=null;
        iPeriodEnd.dispose();
        iPeriodEnd=null;
        iInputVerifier=null;
        iTabbedPane.removeAll();
        iTabbedPane=null;
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
     * @param iPeriodicInvoice
     */
    public void setPeriodicInvoice(SSPeriodicInvoice iPeriodicInvoice) {
        this.iPeriodicInvoice = iPeriodicInvoice;
        this.iInvoice         = iPeriodicInvoice.getTemplate();

        // Första faktueringsdatum
        iDate.setDate( iPeriodicInvoice.getDate() );
        // Antal fakturor
        iCount.setValue( iPeriodicInvoice.getCount() );
        // Periodtid i månader
        iPeriod.setValue( iPeriodicInvoice.getPeriod() );
        // Beskrivning
        iDescription.setText( iPeriodicInvoice.getDescription() );
        // Lägg till perioden som en rad i fakturan
        iAppendPeriod .setSelected( iPeriodicInvoice.getAppendPeriod() );
        // Första datum i perioden:
        iPeriodStart.setDate(iPeriodicInvoice.getPeriodStart() );
        // Sista datum i perioden:
        iPeriodEnd.setDate( iPeriodicInvoice.getPeriodEnd() );

        iAddInfo.setSelected(iPeriodicInvoice.isAppendInformation());

        iInformation.setText(iPeriodicInvoice.getInformation());

        iVoucherTableModel.setVoucher(iInvoice.getVoucher());


        iModel.setObjects(iInvoice.getRows());

        // Fakturatnummer
        iNumber.setValue(iPeriodicInvoice.getNumber());

        // Erat ordernummer
        iYourOrderNumber.setText( iInvoice.getYourOrderNumber() );
        // Kund nummer
        iCustomer.setText(iInvoice.getCustomerNr());
        for (SSCustomer pCustomer : SSDB.getInstance().getCustomers()) {
            if (pCustomer.getNumber().equals(iCustomer.getText())) {
                iModel.setCustomer(pCustomer);
            }
        }
        // Kund namn
        iCustomerName.setText(iInvoice.getCustomerName());
        // Vår kontaktperson:
        iOurContactPerson.setText(iInvoice.getOurContactPerson());
        // Er kontaktperson:
        iYourContactPerson.setText(iInvoice.getYourContactPerson());
        // Dröjsmålsränta
        iDelayInterest.setValue(iInvoice.getDelayInterest());

        // Valuta
        iCurrency.setSelected(iInvoice.getCurrency());
        // Valutakurs
        iCurrencyRate.setValue( iInvoice.getCurrencyRate() );
        // Betalningsvilkor
        iPaymentTerm.setSelected(iInvoice.getPaymentTerm(), true);
        // Leveransvilkor
        iDeliveryTerm.setSelected(iInvoice.getDeliveryTerm());
        // Leveranssätt
        iDeliveryWay.setSelected(iInvoice.getDeliveryWay());


        // Momsfritt
        iTaxFree.setSelected(iInvoice.getTaxFree());
        // Momssats 1
        iTaxRate1.setValue( iInvoice.getTaxRate1() );
        // Momssats 2
        iTaxRate2.setValue( iInvoice.getTaxRate2() );
        // Momssats 3
        iTaxRate3.setValue( iInvoice.getTaxRate3() );
        // EU-försäljning varor
        iEuSaleCommodity.setSelected(iInvoice.getEuSaleCommodity());
        // EU-försäljning trepart varor
        iEuSaleYhirdPartCommodity.setSelected(iInvoice.getEuSaleThirdPartCommodity());
        // Offerttext
        iText.setText(iInvoice.getText());

        // Fakturaadress
        iInvoiceAddress.setAdress(iInvoice.getInvoiceAddress());
        // Leveransadress
        iDeliveryAddress.setAdress(iInvoice.getDeliveryAddress());
        // Standard konton
        iDefaultAccounts.setDefaultAccounts( iInvoice.getDefaultAccounts());

        isStockInfluencing.setSelected( iInvoice.isStockInfluencing() );

        updateSumFields();
        updateTaxTexts();

        iInputVerifier.update();


    }



    /**
     * @return
     */
    public SSPeriodicInvoice getPeriodicInvoice() {
        // Första faktueringsdatum
        iPeriodicInvoice.setDate(iDate.getDate() );
        // Antal fakturor
        iPeriodicInvoice.setCount(iCount.getValue() );
        // Periodtid i månader
        iPeriodicInvoice.setPeriod(iPeriod.getValue() );
        // Beskrivning
        iPeriodicInvoice.setDescription( iDescription.getText() );
        //Lägg till perioden som en rad i fakturan
        iPeriodicInvoice.setAppendPeriod( iAppendPeriod.isSelected() );
        // Första datum i perioden:
        iPeriodicInvoice.setPeriodStart(iPeriodStart.getDate() );
        // Sista datum i perioden:
        iPeriodicInvoice.setPeriodEnd( iPeriodEnd.getDate() );

        iPeriodicInvoice.setAppendInformation(iAddInfo.isSelected());

        iPeriodicInvoice.setInformation(iInformation.getText());
        // Kund nummer
        iInvoice.setCustomerNr(iCustomer.getText());
        // Kund namn
        iInvoice.setCustomerName(iCustomerName.getText());
        // Vår kontaktperson:
        iInvoice.setOurContactPerson(iOurContactPerson.getText());
        // Er kontaktperson:
        iInvoice.setYourContactPerson(iYourContactPerson.getText());
        // Dröjsmålsränta
        iInvoice.setDelayInterest(iDelayInterest.getValue());
        // Erat ordernummer
        iInvoice.setYourOrderNumber( iYourOrderNumber.getText());
        // Valuta
        iInvoice.setCurrency(iCurrency.getSelected());
        // Valutakurs
        iInvoice.setCurrencyRate( iCurrencyRate.getValue() );
        // Betalningsvilkor
        iInvoice.setPaymentTerm(iPaymentTerm.getSelected());
        // Leveransvilkor
        iInvoice.setDeliveryTerm(iDeliveryTerm.getSelected());
        // Leveranssätt
        iInvoice.setDeliveryWay(iDeliveryWay.getSelected());
        // Momsfritt
        iInvoice.setTaxFree(iTaxFree.isSelected());
        // EU-försäljning varor
        iInvoice.setEuSaleCommodity(iEuSaleCommodity.isSelected());
        // EU-försäljning trepart varor
        iInvoice.setEuSaleYhirdPartCommodity(iEuSaleYhirdPartCommodity.isSelected());

        // Momssats 1
        iInvoice.setTaxRate1(iTaxRate1.getValue());
        // Momssats 2
        iInvoice.setTaxRate2(iTaxRate2.getValue());
        // Momssats
        iInvoice.setTaxRate3(iTaxRate3.getValue());


        // Offerttext
        iInvoice.setText(iText.getText());

        // Fakturaadress
        iInvoice.setInvoiceAddress(iInvoiceAddress.getAddress());
        // Leveransadress
        iInvoice.setDeliveryAddress(iDeliveryAddress.getAddress());
        // Standard konton
        iInvoice.setDefaultAccounts(iDefaultAccounts.getDefaultAccounts());
        // Bokförd
        iInvoice.setEntered( false );
        // Utskriven
        iInvoice.setPrinted( false );
        // Lagerför
        iInvoice.setStockInfluencing( isStockInfluencing.isSelected() );
        // Räntefakturerad
        iInvoice.setInterestInvoiced( false );

        // Generera verifikationen
        iInvoice.generateVoucher();

        iPeriodicInvoice.setTemplate(iInvoice);

        iPeriodicInvoice.createInvoices();

        return iPeriodicInvoice;

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
        BigDecimal                 iNetSum   = SSInvoiceMath.getNetSum    (iInvoice);
        Map<SSTaxCode, BigDecimal> iTaxSum   = SSInvoiceMath.getTaxSum    (iInvoice);
        BigDecimal                 iTotalSum = SSInvoiceMath.getTotalSum  (iInvoice);
        BigDecimal                 iRounding = SSInvoiceMath.getRounding  (iInvoice);

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



    public void addKeyListeners(){

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                iCustomer.getComponent(0).requestFocusInWindow();
            }
        });

        iCustomer.getComponent(0).addKeyListener(new KeyAdapter(){
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
            public void focusGained(FocusEvent e){
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        iCustomer.getComponent(0).requestFocusInWindow();
                    }
                });
            }
        });

        iDate.getEditor().getComponent(0).addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iDescription.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iDescription.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iPeriod.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iPeriod.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iCount.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iCount.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iAppendPeriod.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iAppendPeriod.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iPeriodStart.getEditor().getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iPeriodStart.getEditor().getComponent(0).addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iPeriodEnd.getEditor().getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iPeriodEnd.getEditor().getComponent(0).addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iAddInfo.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iAddInfo.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iInformation.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iInvoiceAddress.addKeyListeners();
        iDeliveryAddress.addKeyListeners();

    }


}
