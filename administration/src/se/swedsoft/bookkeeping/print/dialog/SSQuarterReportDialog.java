package se.swedsoft.bookkeeping.print.dialog;

import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.datechooser.panel.SSQuaterChooser;
import se.swedsoft.bookkeeping.gui.util.datechooser.panel.SSYearChooser;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * User: Andreas Lago
 * Date: 2006-apr-24
 * Time: 16:16:46
 */
public class SSQuarterReportDialog extends SSDialog {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private SSQuaterChooser iQuaterChooser;

    private SSYearChooser iYearChooser;


    /**
     * @param iFrame
     */
    public SSQuarterReportDialog(SSMainFrame iFrame) {
        super(iFrame, SSBundle.getBundle().getString("quarterreport.dialog.title"));

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

        iYearChooser.addChangeListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iQuaterChooser.setDate(iYearChooser.getDate());
            }
        });
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return iQuaterChooser.getDate();
    }

    /**
     *
     * @return
     */
    public Date getEndDate() {
        return iQuaterChooser.getEndDate();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.print.dialog.SSQuarterReportDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iQuaterChooser=").append(iQuaterChooser);
        sb.append(", iYearChooser=").append(iYearChooser);
        sb.append('}');
        return sb.toString();
    }
}
