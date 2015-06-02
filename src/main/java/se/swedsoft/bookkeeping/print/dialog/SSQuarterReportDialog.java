package se.swedsoft.bookkeeping.print.dialog;


import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.datechooser.panel.SSQuarterChooser;
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

    private SSQuarterChooser iQuarterChooser;

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
                iQuarterChooser.setDate(iYearChooser.getDate());
            }
        });
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return iQuarterChooser.getDate();
    }

    /**
     *
     * @return
     */
    public Date getEndDate() {
        return iQuarterChooser.getEndDate();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.dialog.SSQuarterReportDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iQuarterChooser=").append(iQuarterChooser);
        sb.append(", iYearChooser=").append(iYearChooser);
        sb.append('}');
        return sb.toString();
    }
}
