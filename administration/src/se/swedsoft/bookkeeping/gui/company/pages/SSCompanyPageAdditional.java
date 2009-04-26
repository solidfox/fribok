package se.swedsoft.bookkeeping.gui.company.pages;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSIntegerTextField;
import se.swedsoft.bookkeeping.data.SSNewCompany;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * User: Andreas Lago
 * Date: 2006-aug-25
 * Time: 10:14:40
 */
public class SSCompanyPageAdditional extends SSCompanyPage{

    private SSNewCompany iCompany;

    private JPanel iPanel;
    private JTextField iContactPerson;
    private JTextField iPhone;
    private JTextField iPhone2;
    private JTextField iTelefax;
    private JTextField iEMail;
    private JTextField iWebAddress;
    private JTextField iBank;
    private JTextField iBankGiroNumber;
    private JTextField iPlusGiroNumber;
    private JTextField iSwiftCode;
    private JTextField iIBAN;
    private JTextField iSMTPAddress;
    private JCheckBox iRoundingOff;
    private SSIntegerTextField iVatPeriod;



    /**
     * @param iDialog
     */
    public SSCompanyPageAdditional(JDialog iDialog) {
        super(iDialog);
        addKeyListeners();
    }


    /**
     *
     * @return the name and title
     */
    public String getName() {
        return SSBundle.getBundle().getString("companyframe.pages.additional");
    }

    /**
     *
     * @return the panel
     */
    public JPanel getPanel() {
        return iPanel;
    }

    /**
     * Set the company to edit
     *
     * @param iCompany
     */
    public void setCompany(SSNewCompany iCompany) {
        this.iCompany = iCompany;

        iPhone.setText(iCompany.getPhone());
        iPhone2.setText(iCompany.getPhone2());
        iTelefax.setText(iCompany.getTelefax());
        iEMail.setText(iCompany.getEMail());
        iWebAddress.setText(iCompany.getHomepage());
        iContactPerson.setText(iCompany.getContactPerson());
        iBank.setText(iCompany.getBank());
        iBankGiroNumber.setText(iCompany.getBankGiroNumber());
        iPlusGiroNumber.setText(iCompany.getPlusGiroNumber());
        iSwiftCode.setText(iCompany.getBIC());
        iIBAN.setText(iCompany.getIBAN());
        iSMTPAddress.setText(iCompany.getSMTP());
        iRoundingOff.setSelected(iCompany.isRoundingOff());
        iVatPeriod.setValue(iCompany.getVatPeriod());
    }

    /**
     * Get the edited company
     *
     * @return the company
     */
    public SSNewCompany getCompany() {
        iCompany.setPhone( iPhone.getText() );
        iCompany.setPhone2( iPhone2.getText() );
        iCompany.setTelefax( iTelefax.getText() );
        iCompany.setHomepage( iWebAddress.getText() );
        iCompany.setEMail( iEMail.getText() );
        iCompany.setContactPerson( iContactPerson.getText() );
        iCompany.setBank( iBank.getText() );
        iCompany.setBankGiroNumber( iBankGiroNumber.getText() );
        iCompany.setPlusGiroNumber( iPlusGiroNumber.getText() );
        iCompany.setBIC( iSwiftCode.getText() );
        iCompany.setIBAN( iIBAN.getText() );
        iCompany.setSMTP(iSMTPAddress.getText());
        iCompany.setRoundingOff(iRoundingOff.isSelected());
        iCompany.setVatPeriod(iVatPeriod.getValue());

        return iCompany;
    }

    public void addKeyListeners(){

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                iContactPerson.requestFocusInWindow();
            }
        });

        iContactPerson.addKeyListener(new KeyAdapter(){
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
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iWebAddress.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iWebAddress.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iSMTPAddress.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iSMTPAddress.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iBank.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iBank.addKeyListener(new KeyAdapter(){
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
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iSwiftCode.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iSwiftCode.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iIBAN.requestFocusInWindow();
                        }
                    });
                }
            }
        });

    }

}
