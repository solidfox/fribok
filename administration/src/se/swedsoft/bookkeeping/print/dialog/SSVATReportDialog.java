package se.swedsoft.bookkeeping.print.dialog;

import se.swedsoft.bookkeeping.calc.math.SSAccountMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSSelectionListener;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.model.SSAccountTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

/**
 * User: Andreas Lago
 * Date: 2006-jun-02
 * Time: 15:48:53
 */
public class SSVATReportDialog extends SSDialog  {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private SSDateChooser iTo;

    private SSDateChooser iFrom;



    private SSTableComboBox<SSAccount> iAccountA;
    private SSTableComboBox<SSAccount> iAccountR1;
    private SSTableComboBox<SSAccount> iAccountR2;

    private JTextField txtAccountR2;
    private JTextField txtAccountR1;
    private JTextField txtAccountA;

    /**
     *
     */
    public SSVATReportDialog(SSMainFrame iMainFrame) {
        super(iMainFrame, SSBundle.getBundle().getString("vatreport2007.dialog.title") );

        setPanel(iPanel);

        iAccountA .setModel( SSAccountTableModel.getDropDownModel() );
        iAccountA.setSearchColumns(0);
        iAccountA.setAllowCustomValues(false);

        iAccountR1.setModel( SSAccountTableModel.getDropDownModel() );
        iAccountR1.setSearchColumns(0);
        iAccountR1.setAllowCustomValues(false);

        iAccountR2.setModel( SSAccountTableModel.getDropDownModel() );
        iAccountR2.setSearchColumns(0);
        iAccountR2.setAllowCustomValues(false);

        iAccountR1.addSelectionListener(new SSSelectionListener<SSAccount>() {
            public void selected(SSAccount selected) {
                txtAccountR1.setText(selected == null ? "" : selected.getDescription() );
            }
        });
        iAccountR2.addSelectionListener(new SSSelectionListener<SSAccount>() {
            public void selected(SSAccount selected) {
                txtAccountR2.setText(selected == null ? "" : selected.getDescription() );
            }
        });
        iAccountA .addSelectionListener(new SSSelectionListener<SSAccount>() {
            public void selected(SSAccount selected) {
                txtAccountA.setText(selected == null ? "" : selected.getDescription() );
            }
        });

        iAccountR1.setSelected( SSAccountMath.getAccountWithVATCode(SSDB.getInstance().getAccounts(), "R1", new SSAccount(1650) ) , true);
        iAccountR2.setSelected( SSAccountMath.getAccountWithVATCode(SSDB.getInstance().getAccounts(), "R2", new SSAccount(2650) ) , true);
        iAccountA .setSelected( SSAccountMath.getAccountWithVATCode(SSDB.getInstance().getAccounts(), "A" , new SSAccount(3740) ) , true);


        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.CANCEL_OPTION, true);
            }
        });
        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.OK_OPTION, true);
            }
        });

        SSNewCompany iCurrentCompany = SSDB.getInstance().getCurrentCompany();

        Calendar iCalendar = Calendar.getInstance();
        if(iCurrentCompany.getVatPeriod() != null && iCurrentCompany.getVatPeriod() != 0){
            iCalendar.add(Calendar.MONTH, (iCurrentCompany.getVatPeriod()*-1));
        }
        else{
            iCalendar.add(Calendar.MONTH, -1);
        }
        iCalendar.set(Calendar.DAY_OF_MONTH, iCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        iFrom.setDate( iCalendar.getTime() );

        iCalendar = Calendar.getInstance();
        iCalendar.add(Calendar.MONTH, -1);
        iCalendar.set(Calendar.DAY_OF_MONTH, iCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        iTo.setDate( iCalendar.getTime() );

    }

    /**
     *
     * @return
     */
    public Date getTo() {
        return iTo.getDate();
    }

    /**
     *
     * @param to
     */
    public void setTo(Date to) {
        iTo.setDate(to);
    }

    /**
     *
     * @return
     */
    public Date getFrom() {
        return iFrom.getDate();
    }

    /**
     *
     * @param from
     */
    public void setFrom(Date from) {
        iFrom.setDate(from);
    }

    /**
     *
     * @return
     */
    public SSAccount getAccountR1() {
        return iAccountR1.getSelected();
    }

    /**
     *
     * @return
     */
    public SSAccount getAccountR2() {
        return iAccountR2.getSelected();
    }

    /**
     *
     * @return
     */
    public SSAccount getAccountA() {
        return iAccountA.getSelected();
    }


}
