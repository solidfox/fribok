package se.swedsoft.bookkeeping.gui.company.pages;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSBigDecimalTextField;
import se.swedsoft.bookkeeping.data.SSNewCompany;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

/**
 * User: Andreas Lago
 * Date: 2006-aug-25
 * Time: 10:14:40
 */
public class SSCompanyPageTax extends SSCompanyPage{

    private SSNewCompany iCompany;

    private JPanel iPanel;

    private SSBigDecimalTextField iTaxRate1;
    private SSBigDecimalTextField iTaxRate2;
    private SSBigDecimalTextField iTaxRate3;

    /**
     * @param iDialog
     */
    public SSCompanyPageTax(JDialog iDialog) {
        super(iDialog);
        addKeyListeners();
    }

    /**
     *
     * @return the name and title
     */
    public String getName() {
        return SSBundle.getBundle().getString("companyframe.pages.tax");
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

        iTaxRate1.setValue( iCompany.getTaxRate1() );
        iTaxRate2.setValue( iCompany.getTaxRate2() );
        iTaxRate3.setValue( iCompany.getTaxRate3() );
    }

    /**
     * Get the edited company
     *
     * @return the company
     */
    public SSNewCompany getCompany() {
        iCompany.setTaxrate1( iTaxRate1.getValue() );
        iCompany.setTaxrate2( iTaxRate2.getValue() );
        iCompany.setTaxrate3( iTaxRate3.getValue() );

        return iCompany;
    }

    public void addKeyListeners(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                iTaxRate1.requestFocusInWindow();
            }
        });

        iTaxRate1.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iTaxRate2.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iTaxRate2.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iTaxRate3.requestFocusInWindow();
                        }
                    });
                }
            }
        });
    }

}
