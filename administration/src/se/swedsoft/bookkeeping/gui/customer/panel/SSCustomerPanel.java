package se.swedsoft.bookkeeping.gui.customer.panel;

import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.common.*;
import se.swedsoft.bookkeeping.gui.util.components.SSBigDecimalTextField;
import se.swedsoft.bookkeeping.gui.util.components.SSEditableTableComboBox;
import se.swedsoft.bookkeeping.gui.util.model.SSDeliveryWayTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSCurrencyTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSDeliveryTermTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSPaymentTermTableModel;
import se.swedsoft.bookkeeping.gui.util.SSButtonGroup;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSInputVerifier;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.company.panel.SSAdressPanel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * User: Andreas Lago
 * Date: 2006-mar-22
 * Time: 16:36:34
 */
public class SSCustomerPanel {

    private SSCustomer iCustomer;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private SSBigDecimalTextField iDiscount;
    private JCheckBox iEuSaleCommodity;
    private JTextField iYourContactPerson;
    private JTextField iOurContactPerson;
    private JTextField iOrginisationNumber;
    private JTextField iEMail;
    private JTextField iTelefax;
    private JTextField iPhone;
    private JTextField iPhone2;
    private JTextField iName;
    private JTextField iCustomerNr;
    private JCheckBox iVatFreeSale;
    private JCheckBox iEuSaleYhirdPartCommodity;
    private SSAdressPanel iInvoiceAddress;
    private SSAdressPanel iDeliveryAddress;
    private SSBigDecimalTextField iCreditLimit;
    private JTextField iVATNumber;
    private JTextField iBankAccountNumber;
    private JTextField iPlusAccountNumber;

    private SSEditableTableComboBox<SSDeliveryWay > iDeliveryWay;
    private SSEditableTableComboBox<SSDeliveryTerm> iDeliveryTerm;
    private SSEditableTableComboBox<SSPaymentTerm > iPaymentTerm;
    private SSEditableTableComboBox<SSCurrency    > iInvoiceCurrency;


    private JCheckBox iUseInvoiceForDelivery;

    private JLabel iCreditLimitCurrency;

    private SSInputVerifier iInputVerifier;
    private JCheckBox iHideUnitPrice;
    private JTextPane iComment;


    public SSCustomerPanel(final SSDialog iOwner, boolean iEdit) {
        iCustomerNr.setEnabled(!iEdit);

        iUseInvoiceForDelivery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDeliveryAddress.setEnabled( !iUseInvoiceForDelivery.isSelected());

                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        if(!iUseInvoiceForDelivery.isSelected())
                            iDeliveryAddress.setFocus();
                        else
                            iInvoiceAddress.setFocus();
                    }
                });
            }
        });

        iInvoiceCurrency.getComboBox().setModel( SSCurrencyTableModel.getDropDownModel() );
        iInvoiceCurrency.getComboBox().setSearchColumns(0);
        iInvoiceCurrency.setEditingFactory( SSCurrencyTableModel.getEditingFactory(iOwner) );

        iDeliveryWay.getComboBox().setModel( SSDeliveryWayTableModel.getDropDownModel() );
        iDeliveryWay.getComboBox().setSearchColumns(0);
        iDeliveryWay.setEditingFactory( SSDeliveryWayTableModel.getEditingFactory(iOwner) );

        iDeliveryTerm.getComboBox().setModel( SSDeliveryTermTableModel.getDropDownModel() );
        iDeliveryTerm.getComboBox().setSearchColumns(0);
        iDeliveryTerm.setEditingFactory( SSDeliveryTermTableModel.getEditingFactory(iOwner) );

        iPaymentTerm.getComboBox().setModel( SSPaymentTermTableModel.getDropDownModel() );
        iPaymentTerm.getComboBox().setSearchColumns(0);
        iPaymentTerm.setEditingFactory( SSPaymentTermTableModel.getEditingFactory(iOwner) );


        SSCurrency iCurrency = SSDB.getInstance().getCurrentCompany().getCurrency();

        iCreditLimitCurrency.setText( iCurrency == null ? "" : iCurrency.getName() );

        SSButtonGroup iGroup = new SSButtonGroup(true);
        iGroup.add( iEuSaleCommodity );
        iGroup.add( iEuSaleYhirdPartCommodity );

        iInputVerifier = new SSInputVerifier();

        iInputVerifier.add(iCustomerNr);
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
    public SSCustomer getCustomer() {
        // Kund nr:
        iCustomer.setNumber(iCustomerNr.getText());
        // Namn
        iCustomer.setName(iName.getText());
        // E-post
        iCustomer.setEMail(iEMail.getText());
        // Telefon
        iCustomer.setPhone1(iPhone.getText());
        // Telefon 2
        iCustomer.setPhone2(iPhone2.getText());
        // Fax
        iCustomer.setTelefax(iTelefax.getText());
        // Orginisationsnummer
        iCustomer.setRegistrationNumber(iOrginisationNumber.getText());
        // Vår kontaktperson:
        iCustomer.setOurContactPerson(iOurContactPerson.getText());
        // Er kontaktperson:
        iCustomer.setYourContactPerson(iYourContactPerson.getText());

        // VAT nr:
        iCustomer.setVATNumber(iVATNumber.getText());
        // Bankgiro nummer:
        iCustomer.setBankgiro(iBankAccountNumber.getText());
        // Plusgiro nummer:
        iCustomer.setPlusgiro(iPlusAccountNumber.getText());
        /*// Kontonummer:
        iCustomer.setAccountNumber(iAccountNumber.getText());
        // Clearingnummer:
        iCustomer.setClearingNumber(iClearingNumber.getText());*/
        // EU-försäljning varor
        iCustomer.setEuSaleCommodity(iEuSaleCommodity.isSelected());
        // EU-försäljning trepart varor
        iCustomer.setEuSaleYhirdPartCommodity(iEuSaleYhirdPartCommodity.isSelected());
        // Momsfri försäljning
        iCustomer.setTaxFree(iVatFreeSale.isSelected());
        //Göm enhetspris på följesedel
        iCustomer.setHideUnitprice(iHideUnitPrice.isSelected());

        // Fakureringsvaluta
        iCustomer.setInvoiceCurrency(iInvoiceCurrency.getSelected());
        // Betalningsvilkor
        iCustomer.setPaymentTerm(iPaymentTerm.getSelected());
        // Leveransvilkor
        iCustomer.setDeliveryTerm(iDeliveryTerm.getSelected());
        // Leveranssätt
        iCustomer.setDeliveryWay(iDeliveryWay.getSelected());

        // Kreditgräns
        iCustomer.setCreditLimit(iCreditLimit.getValue());
        // Rabatt
        iCustomer.setDiscount(iDiscount.getValue());

        // Fakturaadress
        iCustomer.setInvoiceAddress(iInvoiceAddress.getAddress());

        if (iUseInvoiceForDelivery.isSelected()) {
            // Leveransadress
            iCustomer.setDeliveryAddress(iInvoiceAddress.getAddressCloned());
        } else {
            // Leveransadress
            iCustomer.setDeliveryAddress(iDeliveryAddress.getAddress());
        }

        iCustomer.setComment(iComment.getText());


        if(iCustomer.getDeliveryAddress().getName() == null || iCustomer.getDeliveryAddress().getName().length() == 0) iCustomer.getDeliveryAddress().setName( iCustomer.getName() );
        if(iCustomer.getInvoiceAddress ().getName() == null || iCustomer.getInvoiceAddress ().getName().length() == 0) iCustomer.getInvoiceAddress ().setName( iCustomer.getName() );


        return iCustomer;
    }

    /**
     * @param iCustomer
     */
    public void setCustomer(SSCustomer iCustomer) {
        // Kund nr:
        iCustomerNr.setText(iCustomer.getNumber());
        // Namn
        iName.setText(iCustomer.getName());
        // E-post
        iEMail.setText(iCustomer.getEMail());
        // Telefon
        iPhone.setText(iCustomer.getPhone1());
        // Telefon 2
        iPhone2.setText(iCustomer.getPhone2());
        // Fax
        iTelefax.setText(iCustomer.getTelefax());
        // Orginisationsnummer
        iOrginisationNumber.setText(iCustomer.getRegistrationNumber());
        // Vår kontaktperson:
        iOurContactPerson.setText(iCustomer.getOurContactPerson());
        // Er kontaktperson:
        iYourContactPerson.setText(iCustomer.getYourContactPerson());

        // VAT nr:
        iVATNumber.setText(iCustomer.getVATNumber());
        // Bankgiro nummer:
        iBankAccountNumber.setText(iCustomer.getBankgiro());
        // Plusgiro nummer:
        iPlusAccountNumber.setText(iCustomer.getPlusgiro());
        /*// Clearingnummer:
        iClearingNumber.setText(iCustomer.getClearingNumber());
        // Kontonummer:
        iAccountNumber.setText(iCustomer.getAccountNumber());*/
        // EU-försäljning varor
        iEuSaleCommodity.setSelected(iCustomer.getEuSaleCommodity());
        // EU-försäljning trepart varor
        iEuSaleYhirdPartCommodity.setSelected(iCustomer.getEuSaleYhirdPartCommodity());
        // Momsfri försäljning
        iVatFreeSale.setSelected(iCustomer.getTaxFree());
        //Göm enhetspris på följesedel
        iHideUnitPrice.setSelected(iCustomer.getHideUnitprice());
        // Fakureringsvaluta
        iInvoiceCurrency.setSelected(iCustomer.getInvoiceCurrency());
        // Betalningsvilkor
        iPaymentTerm.setSelected(iCustomer.getPaymentTerm());
        // Leveransvilkor
        iDeliveryTerm.setSelected(iCustomer.getDeliveryTerm());
        // Leveranssätt
        iDeliveryWay.setSelected(iCustomer.getDeliveryWay());

        // Kreditgräns
        iCreditLimit.setValue(iCustomer.getCreditLimit());
        // Rabatt
        iDiscount.setValue(iCustomer.getDiscount());

        // Fakturaadress
        iInvoiceAddress.setAdress(iCustomer.getInvoiceAddress());
        // Leveransadress
        iDeliveryAddress.setAdress(iCustomer.getDeliveryAddress());

        iUseInvoiceForDelivery.setSelected(  iCustomer.getDeliveryAddress().equals(  iCustomer.getInvoiceAddress() ) );

        iDeliveryAddress.setEnabled( !iUseInvoiceForDelivery.isSelected());

        iComment.setText(iCustomer.getComment());

        this.iCustomer = iCustomer;
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
        iCustomerNr.setEditable(!iEdit);
    }

    public void addKeyListeners() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if(iCustomerNr.isEnabled())
                    iCustomerNr.requestFocusInWindow();
                else
                    iName.requestFocusInWindow();
            }
        });

        iCustomerNr.addKeyListener(new KeyAdapter(){
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
                            iEMail.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iEMail.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iOrginisationNumber.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iOrginisationNumber.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iVATNumber.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iVATNumber.addKeyListener(new KeyAdapter(){
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
                            iTelefax.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iTelefax.addKeyListener(new KeyAdapter(){
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
                            iEuSaleCommodity.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iEuSaleCommodity.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iEuSaleYhirdPartCommodity.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iEuSaleYhirdPartCommodity.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iVatFreeSale.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iVatFreeSale.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iHideUnitPrice.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iHideUnitPrice.addKeyListener(new KeyAdapter(){
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

        iInvoiceCurrency.getComboBox().getComponent(0).addKeyListener(new KeyAdapter(){
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
                            iCreditLimit.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iCreditLimit.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iDiscount.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iDiscount.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iBankAccountNumber.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iBankAccountNumber.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iPlusAccountNumber.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iPlusAccountNumber.addKeyListener(new KeyAdapter(){
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

        iInvoiceAddress.addKeyListeners();
        iDeliveryAddress.addKeyListeners();
    }

}
