package se.swedsoft.bookkeeping.gui.util.dialogs;

import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

/**
 * User: Andreas Lago
 * Date: 2006-apr-24
 * Time: 16:16:46
 */
public class SSClearTransactionsDialog extends SSDialog {

    private JPanel iPanel;

    private SSDateChooser iDate;

    private SSButtonPanel iButtonPanel;


    /**
     * @param iFrame
     */
    public SSClearTransactionsDialog(SSMainFrame iFrame) {
        super(iFrame, "Rensa transaktioner");

        setPanel(iPanel);

        Calendar iCal = Calendar.getInstance();
        iCal.setTime(SSDB.getInstance().getCurrentYear().getFrom());
        iCal.add(Calendar.DATE, -1);

        iDate.setDate(iCal.getTime());
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
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return iDate.getDate();
    }
}
