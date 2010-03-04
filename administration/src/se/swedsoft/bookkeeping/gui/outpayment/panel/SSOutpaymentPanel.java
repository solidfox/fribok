package se.swedsoft.bookkeeping.gui.outpayment.panel;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.components.SSBigDecimalTextField;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSSupplierInvoiceCellEditor;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSTraversalAction;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSDeleteAction;
import se.swedsoft.bookkeeping.gui.company.panel.SSDefaultAccountPanel;
import se.swedsoft.bookkeeping.gui.voucher.util.SSVoucherRowTableModelOld;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.outpayment.util.SSOutpaymentRowTableModel;
import se.swedsoft.bookkeeping.calc.math.SSOutpaymentMath;
import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

/**
 * User: Andreas Lago
 * Date: 2006-apr-07
 * Time: 11:24:03
 */
public class SSOutpaymentPanel {

    private SSOutpayment iOutpayment;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;


    private SSTable iTable;

    private JFormattedTextField iNumber;

    private SSDateChooser iDate;

    private JTextField iText;

    private JCheckBox iEntered;

    private SSDefaultAccountPanel iDefaultAccounts;

    private SSTable iVoucherTable;

    private JButton iRefreshVoucher;

    private SSBigDecimalTextField iSum;

    private SSTable iDifferenceTable;


    private SSOutpaymentRowTableModel iModel;

    private SSVoucherRowTableModelOld iVoucherTableModel;

    private SSVoucherRowTableModelOld iDifferenceTableModel;

    /**
     *
     * @param iDialog
     */
    public SSOutpaymentPanel(SSDialog iDialog) {
        iModel = new SSOutpaymentRowTableModel(   );
        iModel.addColumn(SSOutpaymentRowTableModel.COLUMN_SUPPLIERINVOICE, true);
        iModel.addColumn(SSOutpaymentRowTableModel.COLUMN_CURRENCY);
        iModel.addColumn(SSOutpaymentRowTableModel.COLUMN_INVOICE_CURRENCYRATE);
        iModel.addColumn(SSOutpaymentRowTableModel.COLUMN_VALUE, true);
        iModel.addColumn(SSOutpaymentRowTableModel.COLUMN_CURRENCYRATE, true);
        iModel.addColumn(SSOutpaymentRowTableModel.COLUMN_PAYED, true);
        iModel.addColumn(SSOutpaymentRowTableModel.COLUMN_CURRENCYRATEDIFFERENCE);
        iModel.setupTable(iTable);

        iTable.setDefaultEditor(SSSupplierInvoice.class, new SSSupplierInvoiceCellEditor( SSSupplierInvoiceMath.getNonPayedOrCreditedInvoices() )  );


        iTable.setColorReadOnly(true);
        iTable.setSingleSelect();

        iVoucherTableModel = new SSVoucherRowTableModelOld(false, true){
            @Override
            public int getColumnCount() {
                // Hide the project and result unit columns
                return 4;
            }
        };

        iVoucherTable.setModel(iVoucherTableModel);

        SSVoucherRowTableModelOld.setupTable(iVoucherTable, iVoucherTableModel);

        iDifferenceTableModel = new SSVoucherRowTableModelOld(false, false){
            @Override
            public int getColumnCount() {
                // Hide the project and result unit columns
                return 4;
            }
        };
        iDifferenceTable.setModel(iDifferenceTableModel);

        SSVoucherRowTableModelOld.setupTable(iDifferenceTable, iDifferenceTableModel);

        new SSTraversalAction(iTable){
            @Override
            protected Point doTraversal(Point iPosition) {
                if (iPosition.x <= 2) {
                    iPosition.x = 3;
                } else
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
                SSOutpaymentRow iSelected = iModel.getSelectedRow(iTable);

                if(iSelected != null) {

                    SSQueryDialog dialog = new SSQueryDialog(SSMainFrame.getInstance(),SSBundle.getBundle(), "outpaymentframe.deleterow", iSelected.toString() );

                    if( dialog.getResponce() != JOptionPane.YES_OPTION ) return null;

                    iModel.deleteRow(iSelected );
                }
                return iPosition;
            }
        };

        new SSDeleteAction(iDifferenceTable){
            @Override
            protected Point doDelete(Point iPosition) {
                SSVoucherRow iSelected = iDifferenceTableModel.getSelectedRow(iDifferenceTable);

                if(iSelected != null) {
                    iDifferenceTableModel.deleteRow(iSelected );
                }
                iDifferenceTableModel.fireTableDataChanged();
                return iPosition;
            }
        };

        new SSTraversalAction(iDifferenceTable){
            @Override
            protected Point doTraversal(Point iPosition) {

                if (iPosition.x == 0) {
                    iPosition.x = iPosition.x + 2;
                } else
                if (iPosition.x < 3) {
                    iPosition.x = iPosition.x + 1;
                } else
                if (iPosition.x == 3) {
                    SSVoucherRow iVoucherRow = iDifferenceTableModel.getObject(iPosition.y);

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

        iRefreshVoucher.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSVoucher iVoucher = iOutpayment.generateVoucher();

                iVoucherTableModel.setVoucher(iVoucher);
            }
        });

        iModel.addTableModelListener( new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                updateSumFields();
            }


        });

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                iDate.getEditor().getComponent(0).requestFocusInWindow();
            }
        });

        iDate.getEditor().getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iText.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iText.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
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
        iDifferenceTableModel.fireTableDataChanged();
    }

    /**
     *
     * @return The debet - credit
     */
    private BigDecimal getDifference() {
        BigDecimal iCreditSum = new BigDecimal(0.0);
        BigDecimal iDebetSum = new BigDecimal(0.0);
        for (SSVoucherRow iRow : iDifferenceTableModel.getObjects()) {
            if (iRow.getDebet() != null) {
                iDebetSum = iDebetSum.add(iRow.getDebet());
            } else if (iRow.getCredit() != null) {
                iCreditSum = iCreditSum.add(iRow.getCredit());
            }
        }
        return iDebetSum.subtract(iCreditSum);

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
     *
     * @return
     */
    public SSOutpayment getOutpayment() {
        // Inbetalningsdatum
        iOutpayment.setDate(iDate.getDate());
        // Text
        iOutpayment.setText( iText.getText() );
        // Standardkonton
        iOutpayment.setDefaultAccounts( iDefaultAccounts.getDefaultAccounts());
        // Bokförd
        iOutpayment.setEntered(iEntered.isSelected());

        // Generate the voucher
        iOutpayment.generateVoucher();

        return iOutpayment;
    }

    /**
     *
     * @param iOutpayment
     */
    public void setOutpayment(SSOutpayment iOutpayment) {
        this.iOutpayment = iOutpayment;

        // Inbetalningsnummer
        iNumber.setValue( iOutpayment.getNumber() );
        // Inbetalningsdatum
        iDate.setDate( iOutpayment.getDate() );
        // Text
        iText.setText( iOutpayment.getText());
        // Bokförd
        iEntered.setSelected(iOutpayment.isEntered());
        // Standardkonton
        iDefaultAccounts.setDefaultAccounts( iOutpayment.getDefaultAccounts() );
        // Kontering
        iVoucherTableModel.setVoucher( iOutpayment.getVoucher() );
        // Differens verifikation
        iDifferenceTableModel.setVoucher( iOutpayment.getDifference());

        iModel.setObjects(iOutpayment.getRows());

        updateSumFields();
    }

    /**
     *
     *
     */
    private void updateSumFields() {
        BigDecimal iSum = SSOutpaymentMath.getSum(iOutpayment);

        this.iSum.setValue(iSum);
    }

    public void dispose() {

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
        iText.removeAll();
        iText=null;
        iEntered.removeAll();
        iEntered=null;
        iDefaultAccounts.dispose();
        iDefaultAccounts=null;
        iVoucherTable.dispose();
        iVoucherTable=null;
        ActionListener[] iActionListeners = iRefreshVoucher.getActionListeners();
        for (ActionListener iActionListener : iActionListeners) {
            iRefreshVoucher.removeActionListener(iActionListener);
        }
        iRefreshVoucher.removeAll();
        iRefreshVoucher=null;
        iSum=null;
        iDifferenceTable.dispose();
        iDifferenceTable=null;

        //iModel.dispose();
        iModel=null;
        iVoucherTableModel=null;
        iDifferenceTableModel=null;
    }

}
