package se.swedsoft.bookkeeping.gui.supplierinvoice.panel;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.common.*;
import se.swedsoft.bookkeeping.gui.util.*;
import se.swedsoft.bookkeeping.gui.util.model.SSCurrencyTableModel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSTraversalAction;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSDeleteAction;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.components.SSBigDecimalTextField;
import se.swedsoft.bookkeeping.gui.util.components.SSEditableTableComboBox;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.company.panel.SSDefaultAccountPanel;
import se.swedsoft.bookkeeping.gui.voucher.util.SSVoucherRowTableModelOld;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.supplier.util.SSSupplierTableModel;
import se.swedsoft.bookkeeping.gui.supplierinvoice.util.SSSupplierInvoiceRowTableModel;
import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSPurchaseOrderMath;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import java.awt.event.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.math.BigDecimal;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.text.DecimalFormat;

/**
 * User: Andreas Lago
 * Date: 2006-mar-24
 * Time: 11:21:13
 */
public class SSSupplierInvoicePanel implements ActionListener{

    private SSSupplierInvoice iSupplierInvoice;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;


    private JFormattedTextField iNumber;


    private SSBigDecimalTextField iCurrencyRate;

    private SSDateChooser iDate;

    private SSTableComboBox<SSSupplier> iSupplier;

    private JFormattedTextField iPurchaseOrder;

    private JTextField iReferencenumber;

    private SSEditableTableComboBox<SSCurrency    > iCurrency;

    private SSTable iTable;

    private SSDefaultAccountPanel iDefaultAccounts;


    private SSBigDecimalTextField iNetSum;
    private SSBigDecimalTextField iTotalSum;

    private SSSupplierInvoiceRowTableModel  iModel;

    private SSTable iVoucherTable;

    private SSVoucherRowTableModelOld iVoucherTableModel;

    private SSDateChooser iDueDate;

    private JButton iRefreshVoucher;

    private SSInputVerifier iInputVerifier;

    private JCheckBox iEntered;

    private JCheckBox iBGCEntered;

    private SSBigDecimalTextField iTaxSum;

    private SSBigDecimalTextField iRoundingSum;


    private SSTable iCorrectionTable;

    private SSVoucherRowTableModelOld  iCorrectionTableModel;
    private JCheckBox isStockInfluencing;

    private boolean bNewSupplierInvoice;

    private SSPaymentTerm iPaymentTerm;



    /**
     *
     * @param iOwner
     */
    public SSSupplierInvoicePanel(final SSDialog iOwner) {
        iNumber.setFormatterFactory( new DefaultFormatterFactory(new NumberFormatter( new DecimalFormat("0") ) ) );
        iNumber.setHorizontalAlignment(JTextField.RIGHT);

        iTable.setColorReadOnly(true);
        iTable.setColumnSortingEnabled(false);
        iTable.setSingleSelect();

        iModel = new SSSupplierInvoiceRowTableModel();
        iModel.addColumn(SSSupplierInvoiceRowTableModel.COLUMN_PRODUCT      , true);
        iModel.addColumn(SSSupplierInvoiceRowTableModel.COLUMN_DESCRIPTION  , true);
        iModel.addColumn(SSSupplierInvoiceRowTableModel.COLUMN_UNITPRICE    , true);
        iModel.addColumn(SSSupplierInvoiceRowTableModel.COLUMN_UNITFREIGHT  , true);
        iModel.addColumn(SSSupplierInvoiceRowTableModel.COLUMN_QUANTITY     , true);
        iModel.addColumn(SSSupplierInvoiceRowTableModel.COLUMN_UNIT         , true);
        iModel.addColumn(SSSupplierInvoiceRowTableModel.COLUMN_SUM          , false);
        iModel.addColumn(SSSupplierInvoiceRowTableModel.COLUMN_ACCOUNT      , true);
        iModel.addColumn(SSSupplierInvoiceRowTableModel.COLUMN_PROJECT      , true);
        iModel.addColumn(SSSupplierInvoiceRowTableModel.COLUMN_RESULTUNIT   , true);

        iModel.setupTable(iTable);


        iModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                updateSumFields();
            }
        });
        iVoucherTableModel = new SSVoucherRowTableModelOld(false, true);

        iVoucherTable.setModel(iVoucherTableModel);

        SSVoucherRowTableModelOld.setupTable(iVoucherTable, iVoucherTableModel);

        iCorrectionTableModel = new SSVoucherRowTableModelOld(false, false){
            @Override
            public int getColumnCount() {
                // Hide the project and result unit columns
                return 4;
            }
        };


        iCorrectionTable.setModel(iCorrectionTableModel);

        SSVoucherRowTableModelOld.setupTable(iCorrectionTable, iCorrectionTableModel);

        new SSDeleteAction(iCorrectionTable){
            @Override
            protected Point doDelete(Point iPosition) {
                SSVoucherRow iSelected = iCorrectionTableModel.getSelectedRow(iCorrectionTable);

                if(iSelected != null) {
                    iCorrectionTableModel.deleteRow(iSelected );
                }
                iCorrectionTableModel.fireTableDataChanged();
                return iPosition;
            }
        };

        new SSTraversalAction(iCorrectionTable){
            @Override
            protected Point doTraversal(Point iPosition) {

                if (iPosition.x == 0) {
                    iPosition.x = iPosition.x + 2;
                } else
                if (iPosition.x < 3) {
                    iPosition.x = iPosition.x + 1;
                } else
                if (iPosition.x == 3) {
                    SSVoucherRow iVoucherRow = iCorrectionTableModel.getObject(iPosition.y);

                    iPosition.y = iPosition.y + 1;
                    iPosition.x = 0;

                    setDifferenceToZero(iVoucherRow);

                }  else {
                    iPosition.y = iPosition.y + 1;
                    iPosition.x = 0;
                }

                return iPosition;
            }
        };

        iSupplier.setModel(  SSSupplierTableModel.getDropDownModel() );
        iSupplier.setSearchColumns(0,1);
        iSupplier.setAllowCustomValues(false);

        iSupplier.addSelectionListener(new SSSelectionListener<SSSupplier>() {
            public void selected(SSSupplier selected) {
                if(selected != null){
                    iSupplierInvoice.setSupplier(selected);
                    iSupplierInvoice.setCurrencyRate(selected.getCurrency() == null ? new BigDecimal(1.0) : selected.getCurrency().getExchangeRate());
                    iPaymentTerm=selected.getPaymentTerm();
                    iSupplierInvoice.setPaymentTerm(iPaymentTerm);
                    iSupplierInvoice.setDueDate();
                    setSupplierInvoice(iSupplierInvoice,bNewSupplierInvoice);
                }
            }
        });

        iDate.addChangeListener(this);

        new SSTraversalAction(iTable){
            @Override
            protected Point doTraversal(Point iPosition) {
                if (iPosition.x <= 5) {
                    iPosition.x = iPosition.x + 1;
                }
                if (iPosition.x == 6) {

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
                SSSupplierInvoiceRow iSelected = iModel.getSelectedRow(iTable);

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



        iCurrencyRate.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                iSupplierInvoice.setCurrencyRate(iCurrencyRate.getValue());
                updateSumFields();
            }
        });


        iRefreshVoucher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSVoucher iVoucher = iSupplierInvoice.generateVoucher();

                iVoucherTableModel.setVoucher(iVoucher);
            }
        });

        iTaxSum.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                iSupplierInvoice.setTaxSum( iTaxSum.getValue() );
                updateSumFields();
            }
        });

        iRoundingSum.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                iSupplierInvoice.setRoundingSum( iRoundingSum.getValue() );
                updateSumFields();
            }
        });


        iInputVerifier = new SSInputVerifier();

        iInputVerifier.add(iSupplier);
        iInputVerifier.addListener(new SSInputVerifier.SSVerifierListener() {
            public void updated(SSInputVerifier iVerifier, boolean iValid) {
                iButtonPanel.getOkButton().setEnabled(iValid);
            }
        });
        iInputVerifier.update();

        addKeyListeners();

    }

    public void actionPerformed(ActionEvent e)
    {
        SSSupplier nSupplier=iSupplierInvoice.getSupplier();
        if(nSupplier!=null)
        {
            Date nDate=iDate.getDate();
            Date nDueDate=iDueDate.getDate();
            iSupplierInvoice.setDate(nDate);
            iPaymentTerm=nSupplier.getPaymentTerm();
            iSupplierInvoice.setPaymentTerm(iPaymentTerm);
            iSupplierInvoice.setDueDate();
            iDueDate.setDate(iSupplierInvoice.getDueDate());
        }
    }

    private void setDifferenceToZero(SSVoucherRow iVoucherRow){
        BigDecimal iDebet = iVoucherRow.getDebet();
        BigDecimal iCredit = iVoucherRow.getCredit();
        if(iDebet == null && iCredit == null){
            BigDecimal iDifference = getDifference();

            if (iDifference.signum() < 0) {
                iVoucherRow.setDebet(iDifference.abs());
            } else {
                iVoucherRow.setCredit(iDifference.abs());
            }
        }
        iCorrectionTableModel.fireTableDataChanged();
    }

    /**
     *
     * @return The debet - credit
     */
    private BigDecimal getDifference() {
        BigDecimal iCreditSum = new BigDecimal(0.0);
        BigDecimal iDebetSum = new BigDecimal(0.0);
        for (SSVoucherRow iRow : iCorrectionTableModel.getObjects()) {
            if (iRow.getDebet() != null) {
                iDebetSum = iDebetSum.add(iRow.getDebet());
            } else if (iRow.getCredit() != null) {
                iCreditSum = iCreditSum.add(iRow.getCredit());
            }
        }
        return iDebetSum.subtract(iCreditSum);

    }


    public void dispose() {

        iSupplier.dispose();
        iSupplier=null;
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
        //if(iPaymentTerm!=null)
            //iPaymentTerm.dispose();

        //iPaymentTerm=null;
        iCurrency.dispose();
        iCurrency=null;
        iDefaultAccounts.dispose();
        iDefaultAccounts=null;
        iTotalSum.removeAll();
        iTotalSum=null;
        iModel=null;
        iInputVerifier=null;
        iCurrencyRate.removeAll();
        iCurrencyRate=null;
        iPurchaseOrder.removeAll();
        iPurchaseOrder=null;
        iReferencenumber.removeAll();
        iReferencenumber=null;
        iNetSum.removeAll();
        iNetSum=null;
        iVoucherTable.dispose();
        iVoucherTable=null;
        iVoucherTableModel=null;
        iDueDate.dispose();
        iDueDate=null;
        ActionListener[] iActionListeners = iRefreshVoucher.getActionListeners();
        for (ActionListener iActionListener : iActionListeners) {
            iRefreshVoucher.removeActionListener(iActionListener);
        }
        iRefreshVoucher=null;
        iInputVerifier=null;
        iEntered=null;
        iBGCEntered=null;
        iTaxSum=null;
        iCorrectionTable.dispose();
        iCorrectionTable=null;
        iCorrectionTableModel=null;
        isStockInfluencing=null;
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
     * @param iSupplierInvoice
     */
    public void setSupplierInvoice(SSSupplierInvoice iSupplierInvoice,boolean iNewSupplierInvoice) {
        bNewSupplierInvoice=iNewSupplierInvoice;
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        this.iSupplierInvoice = iSupplierInvoice;

        List<SSSupplier     > iSuppliers = SSDB.getInstance().getSuppliers();
        List<SSPurchaseOrder> iOrders    = SSDB.getInstance().getPurchaseOrders();

        iVoucherTableModel   .setVoucher(iSupplierInvoice.getVoucher());
        iCorrectionTableModel.setVoucher(iSupplierInvoice.getCorrection());


        iModel.setObjects(this.iSupplierInvoice.getRows());

        // Fakturatnummer
        iNumber.setValue(this.iSupplierInvoice.getNumber());
        // Offertdatum
        
        iDate.setDate(this.iSupplierInvoice.getDate());
        // Kund nummer
        iSupplier.setSelected(this.iSupplierInvoice.getSupplier(iSuppliers) );

        //Betalningsvillkor
        /*if(!bNewSupplierInvoice)
        {
            iPaymentTerm=this.iSupplierInvoice.getSupplier(iSuppliers).getPaymentTerm();
        }
        else
        {

        }*/

        // Ordernummer
        iPurchaseOrder.setValue( getPurchaseOrderNumbers( iSupplierInvoice ) );

        iTaxSum.setValue( iSupplierInvoice.getTaxSum() );

        iRoundingSum.setValue((iSupplierInvoice.getRoundingSum() == null ? new BigDecimal(0) : iSupplierInvoice.getRoundingSum()));
        // OCR/Referens nummer
        iReferencenumber.setText(this.iSupplierInvoice.getReferencenumber());

        // Valuta
        iCurrency.setSelected(this.iSupplierInvoice.getCurrency());
        // Valutakurs
        iCurrencyRate.setValue( this.iSupplierInvoice.getCurrencyRate() );
        // Förfallodag
        iDueDate.setDate(this.iSupplierInvoice.getDueDate());


        // Standard konton
        iDefaultAccounts.setDefaultAccounts( this.iSupplierInvoice.getDefaultAccounts());
        // Bokförd
        iEntered.setSelected( this.iSupplierInvoice.isEntered() );
        // Betald via bankgirocentralen
        iBGCEntered.setSelected( iSupplierInvoice.isBGCEntered() );
        // lagerför
        isStockInfluencing.setSelected( this.iSupplierInvoice.isStockInfluencing() );


        updateSumFields();

        iInputVerifier.update();
    }




    /**
     * @return
     */
    public SSSupplierInvoice getSupplierInvoice() {
        // Offertdatum
        iSupplierInvoice.setDate(iDate.getDate());
        // Kund nummer
        iSupplierInvoice.setSupplier(iSupplier.getSelected());
        // OCR/Referens nummer
        iSupplierInvoice.setReferencenumber(iReferencenumber.getText());
        // Valuta
        iSupplierInvoice.setCurrency(iCurrency.getSelected());
        // Valutakurs
        iSupplierInvoice.setCurrencyRate( iCurrencyRate.getValue() );
        // Förfallodag
        iSupplierInvoice.setDueDate(iDueDate.getDate());

        iSupplierInvoice.setTaxSum(iTaxSum.getValue());

        iSupplierInvoice.setRoundingSum(iRoundingSum.getValue());
        // Standard konton
        iSupplierInvoice.setDefaultAccounts(iDefaultAccounts.getDefaultAccounts());
        // Bokförd
        iSupplierInvoice.setEntered( iEntered.isSelected() );
        // Betald via bankgirocentralen
        iSupplierInvoice.setBGCEntered( iBGCEntered.isSelected() );

        // Lagerför
        iSupplierInvoice.setStockInfluencing( isStockInfluencing.isSelected() );

        // Generera verifikationen
        iSupplierInvoice.generateVoucher();

        return iSupplierInvoice;

    }


    public void addKeyListeners(){

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                iSupplier.getComponent(0).requestFocusInWindow();
            }
        });

        iSupplier.getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iReferencenumber.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iReferencenumber.addKeyListener(new KeyAdapter(){
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
                            iDueDate.getEditor().getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iDueDate.getEditor().getComponent(0).addKeyListener(new KeyAdapter(){
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
                            iTaxSum.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iTaxSum.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iRoundingSum.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iRoundingSum.addKeyListener(new KeyAdapter(){
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
                        iSupplier.getComponent(0).requestFocusInWindow();
                    }
                });
            }
        });

    }

    /**
     *
     * @param iInvoice
     * @return
     */
    private String getPurchaseOrderNumbers(SSSupplierInvoice iInvoice) {
         List<SSPurchaseOrder> iOrders = SSPurchaseOrderMath.getOrdersForInvoice(iInvoice);

        if(iOrders.size() == 0){
            return "Fakturan har inga ordrar";
        }

        Collections.sort(iOrders, new Comparator<SSPurchaseOrder>() {
                   public int compare(SSPurchaseOrder o1, SSPurchaseOrder o2) {
                       return o1.getNumber() - o2.getNumber();
                   }
               });

        StringBuilder sb = new StringBuilder();
         for (SSPurchaseOrder iOrder : iOrders) {

            if(sb.length() > 0) sb.append(", ");

            sb.append(iOrder.getNumber());
        }
        return sb.toString() ;
    }

    public void setOrderNumbers(List<SSPurchaseOrder> iOrders) {
        String iOrdersForInvoice = "";
        for (SSPurchaseOrder iOrder : iOrders) {
            iOrdersForInvoice += iOrder.getNumber() + ", ";
        }
        iOrdersForInvoice = iOrdersForInvoice.substring(0,iOrdersForInvoice.lastIndexOf(", "));
        this.iPurchaseOrder.setText(iOrdersForInvoice);
    }
    /**
     *
     */
    private void updateSumFields(){
        BigDecimal iNetSum   = SSSupplierInvoiceMath.getNetSum  (iSupplierInvoice);
        BigDecimal iTotalSum = SSSupplierInvoiceMath.getTotalSum(iSupplierInvoice);

        this.iNetSum     .setValue(iNetSum );
        this.iTotalSum   .setValue(iTotalSum);
    }




}
