package se.swedsoft.bookkeeping.gui.purchaseorder.panel;

import se.swedsoft.bookkeeping.data.SSPurchaseOrder;
import se.swedsoft.bookkeeping.data.SSPurchaseOrderRow;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.common.SSDeliveryTerm;
import se.swedsoft.bookkeeping.data.common.SSDeliveryWay;
import se.swedsoft.bookkeeping.data.common.SSPaymentTerm;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.company.panel.SSAdressPanel;
import se.swedsoft.bookkeeping.gui.company.panel.SSDefaultAccountPanel;
import se.swedsoft.bookkeeping.gui.purchaseorder.util.SSPurchaseOrderRowTableModel;
import se.swedsoft.bookkeeping.gui.supplier.util.SSSupplierTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSInputVerifier;
import se.swedsoft.bookkeeping.gui.util.SSSelectionListener;
import se.swedsoft.bookkeeping.gui.util.components.SSBigDecimalTextField;
import se.swedsoft.bookkeeping.gui.util.components.SSEditableTableComboBox;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.model.SSCurrencyTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSDeliveryTermTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSDeliveryWayTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSPaymentTermTableModel;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSDeleteAction;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSTraversalAction;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * User: Andreas Lago
 * Date: 2006-mar-24
 * Time: 11:21:13
 */
public class SSPurchaseOrderPanel {

    private SSPurchaseOrder iPurchaseOrder;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JFormattedTextField iNumber;

    private SSDateChooser iDate;

    private SSTableComboBox<SSSupplier> iSupplier;

    private JTextField iSupplierName;

    private SSAdressPanel iSupplierAddress;

    private SSAdressPanel iDeliveryAddress;

    private JTextArea iText;

    private JTextField iYourContactPerson;

    private JTextField iOurContactPerson;

    private SSDateChooser iEstimatedDelivery;



    private SSEditableTableComboBox<SSDeliveryWay > iDeliveryWay;

    private SSEditableTableComboBox<SSDeliveryTerm> iDeliveryTerm;

    private SSEditableTableComboBox<SSPaymentTerm > iPaymentTerm;

    private SSEditableTableComboBox<SSCurrency    > iCurrency;

    private SSTable iTable;

    private SSDefaultAccountPanel iDefaultAccounts;


    private SSBigDecimalTextField iTotalSum;


    private SSPurchaseOrderRowTableModel  iModel;

    private SSInputVerifier iInputVerifier;
    private SSBigDecimalTextField iCurrencyRate;

    /**
     *
     * @param iOwner
     */
    public SSPurchaseOrderPanel(final SSDialog iOwner) {
        iNumber.setFormatterFactory( new DefaultFormatterFactory(new NumberFormatter( new DecimalFormat("0") ) ) );
        iNumber.setHorizontalAlignment(JTextField.RIGHT);

        iTable.setColorReadOnly(true);
        iTable.setColumnSortingEnabled(false);
        iTable.setSingleSelect();

        iModel = new SSPurchaseOrderRowTableModel();
        iModel.addColumn(SSPurchaseOrderRowTableModel.COLUMN_PRODUCT              , true);
        iModel.addColumn(SSPurchaseOrderRowTableModel.COLUMN_DESCRIPTION          , true);
        iModel.addColumn(SSPurchaseOrderRowTableModel.COLUMN_SUPPLIER_ARTICLE_NR  , true);
        iModel.addColumn(SSPurchaseOrderRowTableModel.COLUMN_UNITPRICE            , true);
        iModel.addColumn(SSPurchaseOrderRowTableModel.COLUMN_QUANTITY             , true);
        iModel.addColumn(SSPurchaseOrderRowTableModel.COLUMN_UNIT                 , true);
        iModel.addColumn(SSPurchaseOrderRowTableModel.COLUMN_SUM                 , false);


        iModel.setupTable(iTable);

        iModel.addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                updateSumFields();
            }
        });

        iSupplier.setModel( SSSupplierTableModel.getDropDownModel() );
        iSupplier.setSearchColumns(0,1);
        iSupplier.setAllowCustomValues(true);

        iSupplier.addSelectionListener(new SSSelectionListener<SSSupplier>() {
            public void selected(SSSupplier selected) {
                if(selected != null){
                    iPurchaseOrder.setSupplier(selected);
                    iPurchaseOrder.setCurrencyRate(selected.getCurrency() == null ? new BigDecimal(1) : selected.getCurrency().getExchangeRate());
                    setOrder(iPurchaseOrder);
                }
            }
        });

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
                SSPurchaseOrderRow iSelected = iModel.getSelectedRow(iTable);

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


        iInputVerifier = new SSInputVerifier();

        iInputVerifier.add(iSupplier);
        iInputVerifier.add(iSupplierName);
        iInputVerifier.addListener(new SSInputVerifier.SSVerifierListener() {
            public void updated(SSInputVerifier iVerifier, boolean iValid) {
                JComponent iCurrent = iVerifier.getCurrentComponent();

                iButtonPanel.getOkButton().setEnabled(iValid);
            }
        });

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
     * @param iPurchaseOrder
     */
    public void setOrder(SSPurchaseOrder iPurchaseOrder) {
        this.iPurchaseOrder = iPurchaseOrder;

        iModel.setObjects(iPurchaseOrder.getRows());

        // Offfertnummer
        iNumber.setValue(iPurchaseOrder.getNumber());
        // Offertdatum
        iDate.setDate(iPurchaseOrder.getDate());
        // Leveransdatum
        iEstimatedDelivery.setDate(iPurchaseOrder.getEstimatedDelivery());

        // Kund nummer
        iSupplier.setSelected(iPurchaseOrder.getSupplier(SSDB.getInstance().getSuppliers()));
        // Kund namn
        iSupplierName.setText(iPurchaseOrder.getSupplierName());
        // Vår kontaktperson:
        iOurContactPerson.setText(iPurchaseOrder.getOurContact());
        // Er kontaktperson:
        iYourContactPerson.setText(iPurchaseOrder.getYourContact());

        // Valuta
        iCurrency.setSelected(iPurchaseOrder.getCurrency());
        //Valutakurs
        iCurrencyRate.setValue(iPurchaseOrder.getCurrencyRate());
        // Betalningsvilkor
        iPaymentTerm.setSelected(iPurchaseOrder.getPaymentTerm());
        // Leveransvilkor
        iDeliveryTerm.setSelected(iPurchaseOrder.getDeliveryTerm());
        // Leveranssätt
        iDeliveryWay.setSelected(iPurchaseOrder.getDeliveryWay());


        // Offerttext
        iText.setText(iPurchaseOrder.getText());

        // Leverantörens adress
        iSupplierAddress.setAdress(iPurchaseOrder.getSupplierAddress());
        // Leveransadress
        iDeliveryAddress.setAdress(iPurchaseOrder.getDeliveryAddress());
        // Standard konton
        iDefaultAccounts.setDefaultAccounts( iPurchaseOrder.getDefaultAccounts());

        updateSumFields();

        iInputVerifier.update();
    }

    /**
     * @return
     */
    public SSPurchaseOrder getOrder() {
        // Offertdatum
        iPurchaseOrder.setDate(iDate.getDate());

        // Leveransdatum
        iPurchaseOrder.setEstimatedDelivery(iEstimatedDelivery.getDate());


        // Kund nummer
        iPurchaseOrder.setSupplier(iSupplier.getSelected());
        // Kund namn
        iPurchaseOrder.setSupplierName(iSupplierName.getText());
        // Vår kontaktperson:
        iPurchaseOrder.setOurContact(iOurContactPerson.getText());
        // Er kontaktperson:
        iPurchaseOrder.setYourContact(iYourContactPerson.getText());

        // Valuta
        iPurchaseOrder.setCurrency(iCurrency.getSelected());
        //Valutakurs
        iPurchaseOrder.setCurrencyRate(iCurrencyRate.getValue());
        // Betalningsvilkor
        iPurchaseOrder.setPaymentTerm(iPaymentTerm.getSelected());
        // Leveransvilkor
        iPurchaseOrder.setDeliveryTerm(iDeliveryTerm.getSelected());
        // Leveranssätt
        iPurchaseOrder.setDeliveryWay(iDeliveryWay.getSelected());


        // Offerttext
        iPurchaseOrder.setText(iText.getText());

        // Fakturaadress
        iPurchaseOrder.setSupplierAddress(iSupplierAddress.getAddress());
        // Leveransadress
        iPurchaseOrder.setDeliveryAddress(iDeliveryAddress.getAddress());

        return iPurchaseOrder;

    }


    /**
     *
     */
    private void updateSumFields(){
        BigDecimal iTotalSum = iPurchaseOrder.getSum();

        this.iTotalSum .setValue(iTotalSum);
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
        iSupplierAddress=null;
        iSupplierName=null;
        iDeliveryAddress=null;
        iText.removeAll();
        iText=null;
        iYourContactPerson.removeAll();
        iYourContactPerson=null;
        iOurContactPerson.removeAll();
        iOurContactPerson=null;
        iEstimatedDelivery=null;
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
        iTotalSum.removeAll();
        iTotalSum=null;
        iModel=null;
        iInputVerifier=null;
        iCurrencyRate.removeAll();
        iCurrencyRate=null;
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
                            iSupplierName.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iSupplierName.addKeyListener(new KeyAdapter(){
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
                            iEstimatedDelivery.getEditor().getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iEstimatedDelivery.getEditor().getComponent(0).addKeyListener(new KeyAdapter(){
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
                        iSupplier.getComponent(0).requestFocusInWindow();
                    }
                });
            }
        });

        iSupplierAddress.addKeyListeners();
        iDeliveryAddress.addKeyListeners();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.purchaseorder.panel.SSPurchaseOrderPanel");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iCurrency=").append(iCurrency);
        sb.append(", iCurrencyRate=").append(iCurrencyRate);
        sb.append(", iDate=").append(iDate);
        sb.append(", iDefaultAccounts=").append(iDefaultAccounts);
        sb.append(", iDeliveryAddress=").append(iDeliveryAddress);
        sb.append(", iDeliveryTerm=").append(iDeliveryTerm);
        sb.append(", iDeliveryWay=").append(iDeliveryWay);
        sb.append(", iEstimatedDelivery=").append(iEstimatedDelivery);
        sb.append(", iInputVerifier=").append(iInputVerifier);
        sb.append(", iModel=").append(iModel);
        sb.append(", iNumber=").append(iNumber);
        sb.append(", iOurContactPerson=").append(iOurContactPerson);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iPaymentTerm=").append(iPaymentTerm);
        sb.append(", iPurchaseOrder=").append(iPurchaseOrder);
        sb.append(", iSupplier=").append(iSupplier);
        sb.append(", iSupplierAddress=").append(iSupplierAddress);
        sb.append(", iSupplierName=").append(iSupplierName);
        sb.append(", iTable=").append(iTable);
        sb.append(", iText=").append(iText);
        sb.append(", iTotalSum=").append(iTotalSum);
        sb.append(", iYourContactPerson=").append(iYourContactPerson);
        sb.append('}');
        return sb.toString();
    }
}
