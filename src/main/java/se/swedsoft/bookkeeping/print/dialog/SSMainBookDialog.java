package se.swedsoft.bookkeeping.print.dialog;

import se.swedsoft.bookkeeping.calc.math.SSAccountMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.SSNewResultUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.project.util.SSProjectTableModel;
import se.swedsoft.bookkeeping.gui.resultunit.util.SSResultUnitTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.model.SSAccountTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * Date: 2006-feb-07
 * Time: 16:14:10
 */
public class SSMainBookDialog extends SSDialog {

    private JPanel iPanel;

    private SSDateChooser iFromDate;

    private SSDateChooser iToDate;

    private SSTableComboBox<SSAccount> iToAccount;

    private SSTableComboBox<SSAccount> iFromAccount;

    private SSButtonPanel iButtonPanel;

    private JCheckBox iCheckProject;
    private JCheckBox iCheckResultunit;

    private SSTableComboBox<SSNewProject> iProject;
    private SSTableComboBox<SSNewResultUnit> iResultunit;

    /**
     *
     * @param iMainFrame
     */
    public SSMainBookDialog(SSMainFrame iMainFrame){
        super(iMainFrame, SSBundle.getBundle().getString("mainbookreport.dialog.title") );

        setPanel(iPanel);

        iProject   .setModel( SSProjectTableModel   .getDropDownModel() );
        iResultunit.setModel( SSResultUnitTableModel.getDropDownModel() );

        iFromAccount.setModel( SSAccountTableModel.getDropDownModel());
        iFromAccount.setSearchColumns(0);

        iToAccount.setModel( SSAccountTableModel.getDropDownModel());
        iToAccount.setSearchColumns(0);

        iFromAccount.setSelected( SSAccountMath.getFirstAccount( SSDB.getInstance().getAccounts() ));
        iToAccount  .setSelected( SSAccountMath.getLastAccount ( SSDB.getInstance().getAccounts() ));



        iCheckProject   .addActionListener(this);
        iCheckResultunit.addActionListener(this);

        actionPerformed(null);
    }



    /**
     *
     * @param pDateFrom
     */
    public void setDateFrom(Date pDateFrom){
        iFromDate.setDate(pDateFrom);
    }

    /**
     *
     * @param pDateTo
     */
    public void setDateTo(Date pDateTo){
        iToDate.setDate(pDateTo);
    }



    /**
     *
     * @return
     */
    public SSAccount getAccountFrom(){
        return iFromAccount.getSelected();
    }

    /**
     *
     * @return
     */
    public SSAccount getAccountTo(){

        return iToAccount.getSelected();
    }

    /**
     *
     * @return
     */
    public Date getDateFrom(){
        return iFromDate.getDate();
    }

    /**
     *
     * @return
     */
    public Date getDateTo(){
        return iToDate.getDate();
    }

    /**
     *
     * @return
     */
    public SSNewProject getProject() {
        if( iCheckProject.isSelected()){
            return iProject.getSelected();
        } else {
            return null;
        }
    }

    /**
     *
     * @return
     */
    public SSNewResultUnit getResultUnit() {
        if( iCheckResultunit.isSelected()){
            return iResultunit.getSelected();
        } else {
            return null;
        }
    }


    /**
     *
     * @return
     */
    public boolean isProjectSelected() {
        return iCheckProject.isSelected();
    }

    /**
     *
     * @return
     */
    public boolean isResultUnitSelected() {
        return iCheckResultunit.isSelected();
    }


    /**
     *
     * @param l
     */
    public void addOkActionListener(ActionListener l) {
        iButtonPanel.addOkActionListener(l);
    }

    /**
     *
     * @param l
     */
    public void addCancelActionListener(ActionListener l) {
        iButtonPanel.addCancelActionListener(l);
    }

    /**
     * Invoked when an action occurs.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        iProject   .setEnabled( iCheckProject   .isSelected() );
        iResultunit.setEnabled( iCheckResultunit.isSelected() );
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.print.dialog.SSMainBookDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iCheckProject=").append(iCheckProject);
        sb.append(", iCheckResultunit=").append(iCheckResultunit);
        sb.append(", iFromAccount=").append(iFromAccount);
        sb.append(", iFromDate=").append(iFromDate);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iProject=").append(iProject);
        sb.append(", iResultunit=").append(iResultunit);
        sb.append(", iToAccount=").append(iToAccount);
        sb.append(", iToDate=").append(iToDate);
        sb.append('}');
        return sb.toString();
    }
}


