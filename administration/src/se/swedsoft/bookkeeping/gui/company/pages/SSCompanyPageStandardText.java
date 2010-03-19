package se.swedsoft.bookkeeping.gui.company.pages;

import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.gui.company.panel.SSStandardTextPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import javax.swing.*;

/**
 * User: Andreas Lago
 * Date: 2006-aug-25
 * Time: 10:14:40
 */
public class SSCompanyPageStandardText extends SSCompanyPage{

    private SSNewCompany iCompany;

    private JPanel iPanel;

    private SSStandardTextPanel iStandardTextPanel;

    /**
     * @param iDialog
     */
    public SSCompanyPageStandardText(JDialog iDialog) {
        super(iDialog);
    }

    /**
     *
     * @return the name and title
     */
    @Override
    public String getName() {
        return SSBundle.getBundle().getString("companyframe.pages.standardtexts");
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

        iStandardTextPanel.setData  ( iCompany.getStandardTexts() );
    }

    /**
     * Get the edited company
     *
     * @return the company
     */
    @Override
    public SSNewCompany getCompany() {
        iStandardTextPanel.getData( iCompany.getStandardTexts() );

        return iCompany;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.company.pages.SSCompanyPageStandardText");
        sb.append("{iCompany=").append(iCompany);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iStandardTextPanel=").append(iStandardTextPanel);
        sb.append('}');
        return sb.toString();
    }
}
