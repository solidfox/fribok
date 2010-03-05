package se.swedsoft.bookkeeping.gui.company.pages;

import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.gui.company.panel.SSAdressPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import javax.swing.*;

/**
 * User: Andreas Lago
 * Date: 2006-aug-25
 * Time: 10:14:40
 */
public class SSCompanyPageAddress extends SSCompanyPage{

    private SSNewCompany iCompany;
    private JCheckBox iUseAdressForDelivery;
    private SSAdressPanel iDelivery;
    private SSAdressPanel iAdress;
    private JPanel iPanel;

    /**
     * @param iDialog
     */
    public SSCompanyPageAddress(JDialog iDialog) {
        super(iDialog);
        iAdress.addKeyListeners();
        iDelivery.addKeyListeners();
    }


    /**
     *
     * @return the name and title
     */
    @Override
    public String getName() {
        return SSBundle.getBundle().getString("companyframe.pages.addresse");
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

        iAdress           .setAdress( iCompany.getAddress()         );
        iDelivery         .setAdress( iCompany.getDeliveryAddress() );

        iUseAdressForDelivery.setSelected(  iCompany.getDeliveryAddress().equals(  iCompany.getAddress() ) );

    }

    /**
     * Get the edited company
     *
     * @return the company
     */
    @Override
    public SSNewCompany getCompany() {
         if( iUseAdressForDelivery.isSelected() ) {
            iCompany.setDeliveryAddress(  iAdress.getAddressCloned() );
        } else {
            iCompany.setDeliveryAddress(  iDelivery.getAddressCloned() );
        }

        if(iCompany.getDeliveryAddress().getName() == null || iCompany.getDeliveryAddress().getName().length() == 0) iCompany.getDeliveryAddress().setName( iCompany.getName() );
        if(iCompany.getAddress        ().getName() == null || iCompany.getAddress        ().getName().length() == 0) iCompany.getAddress ()       .setName( iCompany.getName() );

        return iCompany;
    }

}
