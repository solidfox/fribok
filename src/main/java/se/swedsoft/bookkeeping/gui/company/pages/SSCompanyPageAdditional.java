package se.swedsoft.bookkeeping.gui.company.pages;

import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.util.SSMailServer;
import se.swedsoft.bookkeeping.gui.company.panel.SSMailServerDialog;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSIntegerTextField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 * User: Andreas Lago
 * Date: 2006-aug-25
 * Time: 10:14:40
 *
 * $Id$
 *
 */
public class SSCompanyPageAdditional extends SSCompanyPage {

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
    private JButton iEditMailServerButton;
    private JTextField severField;

    private SSMailServer iMailServer;

    private SSMailServerDialog iMailDialog;

    /**
     * @param iDialog
     */
    public SSCompanyPageAdditional(JDialog iDialog) {
        super(iDialog);

        iMailDialog = new SSMailServerDialog(iDialog);

        addKeyListeners();
    }


    /**
     * @return the name and title
     */
    @Override
    public String getName() {
        return SSBundle.getBundle().getString("companyframe.pages.additional");
    }

    /**
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
//        iSMTPAddress.setText(iCompany.getSMTP());
        iRoundingOff.setSelected(iCompany.isRoundingOff());
        iVatPeriod.setValue(iCompany.getVatPeriod());

        setMailServer(iCompany.getMailServer());
    }

    private void setMailServer(SSMailServer server) {
        iMailServer = server;
        if (iMailServer != null) {
            severField.setText(iMailServer.getURI().getHost());
        } else {
            severField.setText("");
        }
    }

    /**
     * Get the edited company
     *
     * @return the company
     */
    @Override
    public SSNewCompany getCompany() {
        iCompany.setPhone(iPhone.getText());
        iCompany.setPhone2(iPhone2.getText());
        iCompany.setTelefax(iTelefax.getText());
        iCompany.setHomepage(iWebAddress.getText());
        iCompany.setEMail(iEMail.getText());
        iCompany.setContactPerson(iContactPerson.getText());
        iCompany.setBank(iBank.getText());
        iCompany.setBankGiroNumber(iBankGiroNumber.getText());
        iCompany.setPlusGiroNumber(iPlusGiroNumber.getText());
        iCompany.setBIC(iSwiftCode.getText());
        iCompany.setIBAN(iIBAN.getText());
//        iCompany.setSMTP(iSMTPAddress.getText());
        iCompany.setRoundingOff(iRoundingOff.isSelected());
        iCompany.setVatPeriod(iVatPeriod.getValue());

        iCompany.setMailServer(iMailServer);

        return iCompany;
    }

    /**
     * Adds listeners for going to next field when enter key is pressed.
     */
    public void addKeyListeners() {


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                iContactPerson.requestFocusInWindow();
            }
        });

        iEditMailServerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setMailServer(new SSMailServerDialog(null).showServerQuery(iMailServer));
            }
        });


        iContactPerson.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iPhone.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iPhone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iPhone2.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iPhone2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iTelefax.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iTelefax.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iEMail.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iEMail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iWebAddress.requestFocusInWindow();
                        }
                    });
                }
            }
        });


        iWebAddress.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iEditMailServerButton.requestFocusInWindow();
                        }
                    });
                }
            }
        });


        // iSMTPAddress removed and replaced by the editMailServer button.
//        iWebAddress.addKeyListener(new KeyAdapter() {
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                    SwingUtilities.invokeLater(new Runnable() {
//                        public void run() {
//                            iSMTPAddress.requestFocusInWindow();
//                        }
//                    });
//                }
//            }
//        });

        // iSMTPAddress removed and replaced by the editMailServer button.
//        iSMTPAddress.addKeyListener(new KeyAdapter() {
//            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                    SwingUtilities.invokeLater(new Runnable() {
//                        public void run() {
//                            iBank.requestFocusInWindow();
//                        }
//                    });
//                }
//            }
//        });

        iBank.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iBankGiroNumber.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iBankGiroNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iPlusGiroNumber.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iPlusGiroNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iSwiftCode.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iSwiftCode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iIBAN.requestFocusInWindow();
                        }
                    });
                }
            }
        });

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.company.pages.SSCompanyPageAdditional");
        sb.append("{iBank=").append(iBank);
        sb.append(", iBankGiroNumber=").append(iBankGiroNumber);
        sb.append(", iCompany=").append(iCompany);
        sb.append(", iContactPerson=").append(iContactPerson);
        sb.append(", iEditMailServerButton=").append(iEditMailServerButton);
        sb.append(", iEMail=").append(iEMail);
        sb.append(", iIBAN=").append(iIBAN);
        sb.append(", iMailDialog=").append(iMailDialog);
        sb.append(", iMailServer=").append(iMailServer);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iPhone=").append(iPhone);
        sb.append(", iPhone2=").append(iPhone2);
        sb.append(", iPlusGiroNumber=").append(iPlusGiroNumber);
        sb.append(", iRoundingOff=").append(iRoundingOff);
        sb.append(", iSMTPAddress=").append(iSMTPAddress);
        sb.append(", iSwiftCode=").append(iSwiftCode);
        sb.append(", iTelefax=").append(iTelefax);
        sb.append(", iVatPeriod=").append(iVatPeriod);
        sb.append(", iWebAddress=").append(iWebAddress);
        sb.append(", severField=").append(severField);
        sb.append('}');
        return sb.toString();
    }
}