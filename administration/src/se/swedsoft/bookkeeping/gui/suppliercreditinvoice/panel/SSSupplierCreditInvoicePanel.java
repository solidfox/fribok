package se.swedsoft.bookkeeping.gui.suppliercreditinvoice.panel;

import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.company.panel.SSDefaultAccountPanel;
import se.swedsoft.bookkeeping.gui.supplier.util.SSSupplierTableModel;
import se.swedsoft.bookkeeping.gui.supplierinvoice.util.SSSupplierInvoiceRowTableModel;
import se.swedsoft.bookkeeping.gui.supplierinvoice.util.SSSupplierInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSInputVerifier;
import se.swedsoft.bookkeeping.gui.util.SSSelectionListener;
import se.swedsoft.bookkeeping.gui.util.components.SSBigDecimalTextField;
import se.swedsoft.bookkeeping.gui.util.components.SSEditableTableComboBox;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.model.SSCurrencyTableModel;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSDeleteAction;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSTraversalAction;
import se.swedsoft.bookkeeping.gui.voucher.util.SSVoucherRowTableModelOld;

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
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-24
 * Time: 11:21:13
 */
public class SSSupplierCreditInvoicePanel {

    private SSSupplierCreditInvoice iSupplierCreditInvoice;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;


    private JFormattedTextField iNumber;


    private SSBigDecimalTextField iCurrencyRate;

    private SSDateChooser iDate;

    private SSTableComboBox<SSSupplier> iSupplier;

    private SSTableComboBox<SSSupplierInvoice> iCrediting;

    private JTextField iReferenceNumber;


    private SSEditableTableComboBox<SSCurrency    > iCurrency;

    private SSTable iTable;

    private SSDefaultAccountPanel iDefaultAccounts;

    private SSBigDecimalTextField iNetSum;
    private SSBigDecimalTextField iTotalSum;

    private SSSupplierInvoiceRowTableModel  iModel;

    private SSTable iVoucherTable;

    private SSVoucherRowTableModelOld iVoucherTableModel;

    private JButton iRefreshVoucher;

    private SSInputVerifier iInputVerifier;

    private JCheckBox iEntered;

    private SSBigDecimalTextField iTaxSum;

    private SSBigDecimalTextField iRoundingSum;


    private SSTable iCorrectionTable;

    private SSVoucherRowTableModelOld  iCorrectionTableModel;

    private JCheckBox isStockInfluencing;


    /**
     *
     * @param iOwner
     */
    public SSSupplierCreditInvoicePanel(final JDialog iOwner) {
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
        
        iSupplier.setModel( SSSupplierTableModel.getDropDownModel() );
        iSupplier.setSearchColumns(0,1);
        iSupplier.setAllowCustomValues(false);

        iSupplier.addSelectionListener(new SSSelectionListener<SSSupplier>() {
            public void selected(SSSupplier selected) {
                if(selected != null){
                    iSupplierCreditInvoice.setSupplier(selected);

                    setCreditSupplierInvoice(iSupplierCreditInvoice);
                }
            }
        });

        iCrediting.setModel(SSSupplierInvoiceTableModel.getDropDownModel() );
        iCrediting.setSearchColumns(0);
        iCrediting.setAllowCustomValues(false);



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

        iCurrencyRate.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                iSupplierCreditInvoice.setCurrencyRate(iCurrencyRate.getValue());
                updateSumFields();
            }
        });


        iRefreshVoucher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSVoucher iVoucher = iSupplierCreditInvoice.generateVoucher();

                iVoucherTableModel.setVoucher(iVoucher);
            }
        });

        iTaxSum.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                iSupplierCreditInvoice.setTaxSum( iTaxSum.getValue() );
                updateSumFields();
            }
        });

        iRoundingSum.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                iSupplierCreditInvoice.setRoundingSum( iRoundingSum.getValue() );
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

        addKeyListeners();


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
        BigDecimal iCreditSum = new BigDecimal(0);
        BigDecimal iDebetSum = new BigDecimal(0);
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
        iNetSum.removeAll();
        iNetSum=null;
        iVoucherTable.dispose();
        iVoucherTable=null;
        iVoucherTableModel=null;
        ActionListener[] iActionListeners = iRefreshVoucher.getActionListeners();
        for (ActionListener iActionListener : iActionListeners) {
            iRefreshVoucher.removeActionListener(iActionListener);
        }
        iRefreshVoucher=null;
        iInputVerifier=null;
        iEntered=null;
        iTaxSum=null;
        iCorrectionTable.dispose();
        iCorrectionTable=null;
        iCorrectionTableModel=null;
        isStockInfluencing=null;
        iCrediting.dispose();
        iCrediting=null;
        iReferenceNumber=null;
    }

    /**
     *
     * @return
     */
    public boolean isValid() {
        return iInputVerifier.isValid();
    }

    public void addKeyListeners(){

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                iReferenceNumber.requestFocusInWindow();
            }
        });

        iReferenceNumber.addKeyListener(new KeyAdapter(){
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
                        iReferenceNumber.requestFocusInWindow();
                    }
                });
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
     * @param iSSSupplierInvoice
     */
    public void setCreditSupplierInvoice(SSSupplierCreditInvoice iSSSupplierInvoice) {
        iSupplierCreditInvoice = iSSSupplierInvoice;

        List<SSSupplier> iSuppliers = SSDB.getInstance().getSuppliers();
        List<SSSupplierInvoice   > iSupplierInvoices    = SSDB.getInstance().getSupplierInvoices();

        iVoucherTableModel   .setVoucher(iSSSupplierInvoice.getVoucher());
        iCorrectionTableModel.setVoucher(iSSSupplierInvoice.getCorrection());


        iModel.setObjects(iSupplierCreditInvoice.getRows());


        // Fakturatnummer
        iNumber.setValue(iSupplierCreditInvoice.getNumber());
        // Offertdatum
        iDate.setDate(iSupplierCreditInvoice.getDate());
        // Kund nummer
        iSupplier.setSelected(iSupplierCreditInvoice.getSupplier(iSuppliers) );

        iCrediting.setSelected(iSupplierCreditInvoice.getCrediting (iSupplierInvoices) );

        iTaxSum.setValue( iSSSupplierInvoice.getTaxSum() );

        iRoundingSum.setValue(iSupplierCreditInvoice.getRoundingSum());
        // OCR/Referensnummer
        iReferenceNumber.setText(iSupplierCreditInvoice.getReferencenumber());

        // Valuta
        iCurrency.setSelected(iSupplierCreditInvoice.getCurrency());
        // Valutakurs
        iCurrencyRate.setValue(iSupplierCreditInvoice.getCurrencyRate() );


        // Standard konton
        iDefaultAccounts.setDefaultAccounts(iSupplierCreditInvoice.getDefaultAccounts());
        // Bokförd
        iEntered.setSelected(iSupplierCreditInvoice.isEntered() );
        // Lagerför
        isStockInfluencing.setSelected(iSupplierCreditInvoice.isStockInfluencing() );

        updateSumFields();

        iInputVerifier.update();
    }



    /**
     * @return
     */
    public SSSupplierCreditInvoice getSupplierCreditInvoice() {
        // Offertdatum
        iSupplierCreditInvoice.setDate(iDate.getDate());
        // Kund nummer
        iSupplierCreditInvoice.setSupplier(iSupplier.getSelected());
        // Kund nummer
        iSupplierCreditInvoice.setCrediting(iCrediting.getSelected());
        // OCR/Referens nummer
        iSupplierCreditInvoice.setReferencenumber(iReferenceNumber.getText());
        // Valuta
        iSupplierCreditInvoice.setCurrency(iCurrency.getSelected());
        // Valutakurs
        iSupplierCreditInvoice.setCurrencyRate( iCurrencyRate.getValue() );

        iSupplierCreditInvoice.setTaxSum(iTaxSum.getValue());

        iSupplierCreditInvoice.setRoundingSum(iRoundingSum.getValue());
        // Standard konton
        iSupplierCreditInvoice.setDefaultAccounts(iDefaultAccounts.getDefaultAccounts());
        // Bokförd
        iSupplierCreditInvoice.setEntered( iEntered.isSelected() );
        // Lagerför
        iSupplierCreditInvoice.setStockInfluencing( isStockInfluencing.isSelected() );


        // Generera verifikationen
        iSupplierCreditInvoice.generateVoucher();

        return iSupplierCreditInvoice;

    }




    /**
     *
     */
    private void updateSumFields(){
        BigDecimal iNetSum   = SSSupplierInvoiceMath.getNetSum  (iSupplierCreditInvoice);
        BigDecimal iTotalSum = SSSupplierInvoiceMath.getTotalSum(iSupplierCreditInvoice);

        this.iNetSum     .setValue(iNetSum );
        this.iTotalSum   .setValue(iTotalSum);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.suppliercreditinvoice.panel.SSSupplierCreditInvoicePanel");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iCorrectionTable=").append(iCorrectionTable);
        sb.append(", iCorrectionTableModel=").append(iCorrectionTableModel);
        sb.append(", iCrediting=").append(iCrediting);
        sb.append(", iCurrency=").append(iCurrency);
        sb.append(", iCurrencyRate=").append(iCurrencyRate);
        sb.append(", iDate=").append(iDate);
        sb.append(", iDefaultAccounts=").append(iDefaultAccounts);
        sb.append(", iEntered=").append(iEntered);
        sb.append(", iInputVerifier=").append(iInputVerifier);
        sb.append(", iModel=").append(iModel);
        sb.append(", iNetSum=").append(iNetSum);
        sb.append(", iNumber=").append(iNumber);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iReferenceNumber=").append(iReferenceNumber);
        sb.append(", iRefreshVoucher=").append(iRefreshVoucher);
        sb.append(", iRoundingSum=").append(iRoundingSum);
        sb.append(", isStockInfluencing=").append(isStockInfluencing);
        sb.append(", iSupplier=").append(iSupplier);
        sb.append(", iSupplierCreditInvoice=").append(iSupplierCreditInvoice);
        sb.append(", iTable=").append(iTable);
        sb.append(", iTaxSum=").append(iTaxSum);
        sb.append(", iTotalSum=").append(iTotalSum);
        sb.append(", iVoucherTable=").append(iVoucherTable);
        sb.append(", iVoucherTableModel=").append(iVoucherTableModel);
        sb.append('}');
        return sb.toString();
    }
}
