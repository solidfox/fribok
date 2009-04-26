package se.swedsoft.bookkeeping.print.dialog;

import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.SSMainFrame;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;

/**
 * User: Andreas Lago
 * Date: 2006-jun-02
 * Time: 15:48:53
 */
public class SSPeriodSelectionDialog extends SSDialog {

    private SSDateChooser iTo;

    private SSDateChooser iFrom;

    private SSButtonPanel iButtonPanel;

    private JPanel iPanel;

    /**
     *
     */
    public SSPeriodSelectionDialog(SSMainFrame iMainFrame, String iTitle) {
        super(iMainFrame, iTitle );

        setPanel(iPanel);

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
}
