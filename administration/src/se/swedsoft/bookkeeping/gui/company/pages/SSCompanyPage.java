package se.swedsoft.bookkeeping.gui.company.pages;

import se.swedsoft.bookkeeping.data.SSNewCompany;

import javax.swing.*;

/**
 * User: Andreas Lago
 * Date: 2006-aug-25
 * Time: 10:10:40
 */
public abstract class SSCompanyPage {

    protected JDialog iDialog;

    /**
     *
     * @param iDialog
     */
    public SSCompanyPage(JDialog iDialog) {
        this.iDialog = iDialog;
    }

    /**
     *
     * @return the name and title
     */
    public abstract String getName();

    /**
     *
     * @return the panel
     */
    public abstract JPanel getPanel();

    /**
     * Set the company to edit
     *
     * @param iCompany
     */
    public abstract void setCompany(SSNewCompany iCompany);

    /**
     * Get the edited company
     *
     * @return the company
     */
    public abstract SSNewCompany getCompany();

    /**
     *
     * @param iDialog
     */
    public void setDialog(JDialog iDialog){
        this.iDialog = iDialog;
    }


    //public static SSNewCompanyPage getInstance();
}
