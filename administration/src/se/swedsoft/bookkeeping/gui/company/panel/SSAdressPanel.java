package se.swedsoft.bookkeeping.gui.company.panel;

import se.swedsoft.bookkeeping.data.SSAddress;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Date: 2006-feb-09
 * Time: 16:35:53
 */
public class SSAdressPanel extends JPanel{

    private JLabel iTitle;

    private JPanel iPanel;

    private SSAddress iAdress;

    private JTextField iName;

    private JTextField iAddress;

    private JTextField iStreet;

    private JTextField iCountry;

    private JTextField iZipCode;

    private JTextField iCity;


    ///////////////////////////////////////////////////////////


    /**
     *
     */
    public SSAdressPanel(){
        setLayout(new BorderLayout() );

        add(iPanel, BorderLayout.CENTER);
    }

    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }


    ///////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getTitle() {
        return iTitle.getText();
    }

    /**
     *
     * @param iTitle
     */
    public void setTitle(String iTitle) {
        this.iTitle.setText( iTitle);
    }


    ///////////////////////////////////////////////////////////



    /**
     *
     * @return
     */
    public String getAdress(){
        return iAddress.getText();
    }


    /**
     *
     * @param pAdress
     */
    public void setAdress(String pAdress){
        iAddress.setText(pAdress);
    }

    ///////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getStreet(){
        return iStreet.getText();
    }


    /**
     *
     * @param pStreet
     */
    public void setStreet(String pStreet){
        iStreet.setText(pStreet);
    }

    ///////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getCountry(){
        return iCountry.getText();
    }



    /**
     *
     * @param pCountry
     */
    public void setCountry(String pCountry){
        iCountry.setText(pCountry);
    }

    ///////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getZipCode(){
        return iZipCode.getText();
    }

    /**
     *
     * @param pZipCode
     */
    public void setZipCode(String pZipCode){
        iZipCode.setText(pZipCode);
    }

    ///////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public String getCity(){
        return iCity.getText();
    }

    ///////////////////////////////////////////////////////////

    /**
     *
     * @param pCity
     */
    public void setCity(String pCity){
        iCity.setText(pCity);
    }

    ///////////////////////////////////////////////////////////

    /**
     *
     * @param iAddress
     */
    public void setAdress(SSAddress iAddress) {
        iAdress = iAddress;

        iName         .setText(iAddress.getName());
        this.iAddress .setText(iAddress.getAddress1());
        iStreet       .setText(iAddress.getAddress2());
        iCountry      .setText(iAddress.getCountry());
        iCity         .setText(iAddress.getCity());
        iZipCode      .setText(iAddress.getZipCode());
    }

    /**
     *
     * @return
     */
    public SSAddress getAddress() {
        iAdress.setName   (iName   .getText());
        iAdress.setAddress1(iAddress.getText());
        iAdress.setAddress2 (iStreet .getText());
        iAdress.setCountry(iCountry.getText());
        iAdress.setCity   (iCity   .getText());
        iAdress.setZipCode(iZipCode.getText());

        return iAdress;
    }

    /**
     *
     * @return
     */
    public SSAddress getAddressCloned() {
        return new SSAddress(getAddress());

    }


    /**
     *
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        iName   .setEditable(enabled);
        iAddress.setEditable(enabled);
        iStreet .setEditable(enabled);
        iCountry.setEditable(enabled);
        iCity   .setEditable(enabled);
        iZipCode.setEditable(enabled);
    }

    public void addKeyListeners(){
        iName.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iAddress.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iAddress.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iStreet.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iStreet.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iZipCode.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iZipCode.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iCity.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iCity.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iCountry.requestFocusInWindow();
                        }
                    });
                }
            }
        });
    }

    public void setFocus(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                iName.requestFocusInWindow();
            }
        });
    }


}
