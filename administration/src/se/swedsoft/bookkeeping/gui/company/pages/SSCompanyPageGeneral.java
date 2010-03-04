package se.swedsoft.bookkeeping.gui.company.pages;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.*;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSDefaultFileChooser;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSImageFileChooser;
import se.swedsoft.bookkeeping.gui.util.components.SSEditableTableComboBox;
import se.swedsoft.bookkeeping.gui.util.components.SSCurrencyTextField;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.common.*;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;

/**
 * User: Andreas Lago
 * Date: 2006-aug-25
 * Time: 10:14:40
 */
public class SSCompanyPageGeneral extends SSCompanyPage{

    private SSNewCompany iCompany;

    private JPanel iPanel;

    private JButton iBrowseForLogoButton;

    private JTextField iLogotype;

    private SSEditableTableComboBox<SSCurrency> iCurrency;
    private JCheckBox iTaxRegistered;
    private JTextField iWeightUnit;
    private JTextField iVolumeUnit;
    private JTextField iEstimatedDelivery;
    private SSCurrencyTextField iReminderfee;
    private SSCurrencyTextField iDelayintrest;
    private JTextField iVATNumber;
    private JTextField iResidence;
    private JTextField iCorporateID;
    private JTextField iName;
    private SSEditableTableComboBox<SSPaymentTerm> iPaymentTerm;
    private SSEditableTableComboBox<SSDeliveryTerm> iDeliveryTerm;
    private SSEditableTableComboBox<SSDeliveryWay> iDeliveryWay;
    private SSEditableTableComboBox<SSUnit> iStandardUnit;

    /**
     *
     */
    public SSCompanyPageGeneral(JDialog iDialog) {
        super(iDialog);

        iCurrency.getComboBox().setModel( SSCurrencyTableModel.getDropDownModel() );
        iCurrency.getComboBox().setSearchColumns(0);
        iCurrency.setEditingFactory( SSCurrencyTableModel.getEditingFactory(iDialog)  );

        iPaymentTerm.getComboBox().setModel( SSPaymentTermTableModel.getDropDownModel() );
        iPaymentTerm.getComboBox().setSearchColumns(0);
        iPaymentTerm.setEditingFactory( SSPaymentTermTableModel.getEditingFactory(iDialog)  );

        iDeliveryTerm.getComboBox().setModel( SSDeliveryTermTableModel.getDropDownModel() );
        iDeliveryTerm.getComboBox().setSearchColumns(0);
        iDeliveryTerm.setEditingFactory( SSDeliveryTermTableModel.getEditingFactory(iDialog)  );

        iDeliveryWay.getComboBox().setModel( SSDeliveryWayTableModel.getDropDownModel() );
        iDeliveryWay.getComboBox().setSearchColumns(0);
        iDeliveryWay.setEditingFactory( SSDeliveryWayTableModel.getEditingFactory(iDialog)  );

        iStandardUnit.getComboBox().setModel( SSUnitTableModel.getDropDownModel());
        iStandardUnit.getComboBox().setSearchColumns(0);
        iStandardUnit.setEditingFactory( SSUnitTableModel.getEditingFactory(iDialog));

        iBrowseForLogoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSImageFileChooser iFileChooser = SSImageFileChooser.getInstance();

                if( iFileChooser.showDialog(iBrowseForLogoButton) != JFileChooser.APPROVE_OPTION) return;

                iLogotype.setText( iFileChooser.getSelectedFile().getAbsolutePath()  );
            }
        });

        addKeyListeners();
    }

    /**
     *
     * @return the name and title
     */
    @Override
    public String getName() {
        return SSBundle.getBundle().getString("companyframe.pages.general");
    }

    /**
     *
     * @return the panel
     */
    @Override
    public JPanel getPanel() {
        return iPanel;
    }

    /**
     * Set the company to edit
     *
     * @param iCompany
     */
    @Override
    public void setCompany(SSNewCompany iCompany) {
        this.iCompany = iCompany;
        iName             .setText    ( iCompany.getName());
        iResidence        .setText    ( iCompany.getResidence());
        iTaxRegistered    .setSelected( iCompany.getTaxRegistered());
        iCorporateID      .setText    ( iCompany.getCorporateID());
        iLogotype         .setText    ( iCompany.getLogotype());
        iVATNumber        .setText    ( iCompany.getVATNumber());
        iDelayintrest     .setValue   ( iCompany.getDelayInterest()   );
        iReminderfee      .setValue   ( iCompany.getReminderfee()   );
        iCurrency         .setSelected( iCompany.getCurrency()   );
        iEstimatedDelivery.setText    ( iCompany.getEstimatedDelivery());
        iVolumeUnit       .setText    ( iCompany.getVolumeUnit()  );
        iWeightUnit       .setText    ( iCompany.getWeightUnit() );
        iStandardUnit     .setSelected( iCompany.getStandardUnit());
        iPaymentTerm      .setSelected( iCompany.getPaymentTerm() );
        iDeliveryTerm     .setSelected( iCompany.getDeliveryTerm());
        iDeliveryWay      .setSelected( iCompany.getDeliveryWay());
    }

    /**
     * Get the edited company
     *
     * @return the company
     */
    @Override
    public SSNewCompany getCompany() {
        iCompany.setName             ( iName                  .getText());
        iCompany.setResidence        ( iResidence             .getText());
        iCompany.setTaxRegistered    ( iTaxRegistered         .isSelected());
        iCompany.setCorporateID      ( iCorporateID           .getText());
        iCompany.setLogotype         ( iLogotype              .getText());
        iCompany.setVATNumber        ( iVATNumber             .getText());
        iCompany.setDelayInterest    ( iDelayintrest          .getValue() ) ;
        iCompany.setReminderfee      ( iReminderfee           .getValue());
        iCompany.setCurrency         ( iCurrency              .getSelected());
        iCompany.setEstimatedDelivery( iEstimatedDelivery     .getText());
        iCompany.setVolumeUnit       ( iVolumeUnit.getText( ) );
        iCompany.setWeightUnit       ( iWeightUnit.getText( ) );
        iCompany.setStandardUnit     ( iStandardUnit           .getSelected() );
        iCompany.setPaymentTerm      ( iPaymentTerm            .getSelected());
        iCompany.setDeliveryTerm     ( iDeliveryTerm           .getSelected());
        iCompany.setDeliveryWay      ( iDeliveryWay            .getSelected());


        return iCompany;
    }

    public void addKeyListeners(){

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                iName.requestFocusInWindow();
            }
        });

        iName.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iResidence.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iResidence.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iCorporateID.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iCorporateID.addKeyListener(new KeyAdapter(){
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
                            iDelayintrest.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iDelayintrest.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iReminderfee.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iReminderfee.addKeyListener(new KeyAdapter(){
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
                            iEstimatedDelivery.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iEstimatedDelivery.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iVolumeUnit.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iVolumeUnit.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iWeightUnit.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iWeightUnit.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iStandardUnit.getComboBox().getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iStandardUnit.getComboBox().getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iTaxRegistered.requestFocusInWindow();
                        }
                    });
                }
            }
        });  
    }

}
