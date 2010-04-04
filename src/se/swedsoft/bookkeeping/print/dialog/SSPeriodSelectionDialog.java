package se.swedsoft.bookkeeping.print.dialog;

import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
     * @param iMainFrame
     * @param iTitle
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.print.dialog.SSPeriodSelectionDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iFrom=").append(iFrom);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iTo=").append(iTo);
        sb.append('}');
        return sb.toString();
    }
}
