package se.swedsoft.bookkeeping.gui.supplier.panel;

import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.common.SSDeliveryTerm;
import se.swedsoft.bookkeeping.data.common.SSDeliveryWay;
import se.swedsoft.bookkeeping.data.common.SSPaymentTerm;
import se.swedsoft.bookkeeping.gui.company.panel.SSAdressPanel;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSInputVerifier;
import se.swedsoft.bookkeeping.gui.util.components.SSEditableTableComboBox;
import se.swedsoft.bookkeeping.gui.util.components.SSIntegerTextField;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.model.SSCurrencyTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSDeliveryTermTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSDeliveryWayTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSPaymentTermTableModel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * User: Andreas Lago
 * Date: 2006-mar-24
 * Time: 10:00:21
 */
public class SSSupplierPanel {

    private SSSupplier iSupplier;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JTextField iHomepage;
    private JTextField iOurContact;
    private JTextField iYourContact;
    private JTextField iRegistrationNumber;
    private JTextField iEmail;
    private JTextField iTeleFax;
    private JTextField iPhone;
    private JTextField iPhone2;
    private JTextField iName;
    private JTextField iSupplierNr;
    private SSAdressPanel iAddress;

    private SSEditableTableComboBox<SSDeliveryWay > iDeliveryWay;
    private SSEditableTableComboBox<SSDeliveryTerm> iDeliveryTerm;
    private SSEditableTableComboBox<SSPaymentTerm > iPaymentTerm;
    private SSEditableTableComboBox<SSCurrency    > iCurrency;

    private JTextField iOurCustomerNr;

    private JTextField iPlusGiroNumber;

    private JTextField iBankGiroNumber;

    private SSIntegerTextField iOutpaymentNumber;

    private SSInputVerifier iInputVerifier;
    
    private JTextPane iComment;

    /**
     *
     * @param iOwner
     * @param iEdit
     */
    public SSSupplierPanel(final SSDialog iOwner, boolean iEdit) {

        iSupplierNr.setEnabled(!iEdit);

        iCurrency.getComboBox().setModel( SSCurrencyTableModel.getDropDownModel() );
        iCurrency.getComboBox().setSearchColumns();
        iCurrency.setEditingFactory( SSCurrencyTableModel.getEditingFactory(iOwner) );

        iDeliveryWay.getComboBox().setModel( SSDeliveryWayTableModel.getDropDownModel() );
        iDeliveryWay.getComboBox().setSearchColumns();
        iDeliveryWay.setEditingFactory( SSDeliveryWayTableModel.getEditingFactory(iOwner) );

        iDeliveryTerm.getComboBox().setModel( SSDeliveryTermTableModel.getDropDownModel() );
        iDeliveryTerm.getComboBox().setSearchColumns();
        iDeliveryTerm.setEditingFactory( SSDeliveryTermTableModel.getEditingFactory(iOwner) );

        iPaymentTerm.getComboBox().setModel( SSPaymentTermTableModel.getDropDownModel() );
        iPaymentTerm.getComboBox().setSearchColumns();
        iPaymentTerm.setEditingFactory( SSPaymentTermTableModel.getEditingFactory(iOwner) );

         iInputVerifier = new SSInputVerifier();

        iInputVerifier.add(iSupplierNr);
        iInputVerifier.addListener(new SSInputVerifier.SSVerifierListener() {
            public void updated(SSInputVerifier iVerifier, boolean iValid) {
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
     * @return
     */
    public SSSupplier getSupplier() {
        // Leverantörsnummer
        iSupplier.setNumber(iSupplierNr.getText());
        // Namn
        iSupplier.setName(iName.getText());
        // Telefon
        iSupplier.setPhone1(iPhone.getText());
        // Telefon 2
        iSupplier.setPhone2(iPhone2.getText());
        // Fax nummer
        iSupplier.setTelefax(iTeleFax.getText());
        // E-Post
        iSupplier.setEMail(iEmail.getText());
        // Hemsida
        iSupplier.setHomepage(iHomepage.getText());
        // Orginisationsnummer
        iSupplier.setRegistrationNumber(iRegistrationNumber.getText());
        // Er kontaktperson
        iSupplier.setYourContact(iYourContact.getText());
        // Vår kontaktperson
        iSupplier.setOurContact(iOurContact.getText());
        // Vårt kund nummer
        iSupplier.setOurCustomerNr(iOurCustomerNr.getText());
        // Bankgiro nummer:
        iSupplier.setBankGiro(iBankGiroNumber.getText());
        // Plusgiro nummer:
        iSupplier.setPlusGiro(iPlusGiroNumber.getText());
        // Utbetalningsnummer
        iSupplier.setOutpaymentNumber(iOutpaymentNumber.getValue() );
        // Valuta
        iSupplier.setCurrency(iCurrency.getSelected());
        // Betalningsvilkor
        iSupplier.setPaymentTerm(iPaymentTerm.getSelected());
        // Leveransvilkor
        iSupplier.setDeliveryTerm(iDeliveryTerm.getSelected());
        // Leveranssätt
        iSupplier.setDeliveryWay(iDeliveryWay.getSelected());
        // Address
        iSupplier.setAddress(iAddress.getAddress());

        iSupplier.setComment(iComment.getText());

        if(iSupplier.getAddress().getName() == null || iSupplier.getAddress().getName().length() == 0) iSupplier.getAddress().setName( iSupplier.getName() );


        return iSupplier;


    }

    /**
     * @param iSupplier
     * @param newSupplier
     */
    public void setSupplier(SSSupplier iSupplier,boolean newSupplier) {
        this.iSupplier = iSupplier;

        // Leverantörsnummer
        iSupplierNr.setText(iSupplier.getNumber());
        // Namn
        iName.setText(iSupplier.getName());
        // Telefon
        iPhone.setText(iSupplier.getPhone1());
        // Telefon 2
        iPhone2.setText(iSupplier.getPhone2());
        // Fax nummer
        iTeleFax.setText(iSupplier.getTelefax());
        // E-Post
        iEmail.setText(iSupplier.getEMail());
        // Hemsida
        iHomepage.setText(iSupplier.getHomepage());
        // Orginisationsnummer
        iRegistrationNumber.setText(iSupplier.getRegistrationNumber());
        // Er kontaktperson
        iYourContact.setText(iSupplier.getYourContact());
        // Vår kontaktperson
        iOurContact.setText(iSupplier.getOurContact());
        // Vårt kund nummer
        iOurCustomerNr.setText(iSupplier.getOurCustomerNr());
        // Bankgiro nummer:
        iBankGiroNumber.setText(iSupplier.getBankgiro());
        // Plusgiro nummer:
        iPlusGiroNumber.setText(iSupplier.getPlusgiro());
        // Utbetalningsnummer
        if (newSupplier) {
            iOutpaymentNumber.setValue(null);
        }else{
            iOutpaymentNumber.setValue( iSupplier.getOutpaymentNumber() );
        }
        // Valuta
        iCurrency.setSelected(iSupplier.getCurrency());
        // Betalningsvilkor
        iPaymentTerm.setSelected(iSupplier.getPaymentTerm());
        // Leveransvilkor
        iDeliveryTerm.setSelected(iSupplier.getDeliveryTerm());
        // Leveranssätt
        iDeliveryWay.setSelected(iSupplier.getDeliveryWay());
        // Address
        iAddress.setAdress(iSupplier.getAddress());

        iComment.setText(iSupplier.getComment());
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

    public void setEditPanel(boolean iEdit) {
        iSupplierNr.setEditable(!iEdit);
    }

    public void addKeyListeners() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if(iSupplierNr.isEnabled())
                    iSupplierNr.requestFocusInWindow();
                else
                    iName.requestFocusInWindow();
            }
        });

        iSupplierNr.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iName.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iName.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iHomepage.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iHomepage.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iEmail.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iEmail.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iRegistrationNumber.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iRegistrationNumber.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iOurContact.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iOurContact.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iYourContact.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iYourContact.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iPhone.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iPhone.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iPhone2.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iPhone2.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iTeleFax.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iTeleFax.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iButtonPanel.getOkButton().requestFocusInWindow();
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

        iCurrency.getComboBox().getComponent(0).addKeyListener(new KeyAdapter(){
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
                            iOurCustomerNr.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iOurCustomerNr.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iOutpaymentNumber.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iOutpaymentNumber.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iBankGiroNumber.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iBankGiroNumber.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iPlusGiroNumber.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iPlusGiroNumber.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iComment.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iAddress.addKeyListeners();
    }

}
