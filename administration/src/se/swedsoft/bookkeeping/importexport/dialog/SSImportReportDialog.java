package se.swedsoft.bookkeeping.importexport.dialog;

import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * User: Andreas Lago
 * Date: 2006-okt-05
 * Time: 09:33:49
 */
public class SSImportReportDialog extends SSDialog {

    private JPanel iPanel;


    private JTextPane iTextPane;
    private SSButtonPanel iButtonPanel;

    /**
     * @param iFrame
     * @param title
     */
    public SSImportReportDialog(JFrame iFrame, String title) {
        super(iFrame, title);

        setPanel(iPanel);

        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(JOptionPane.OK_OPTION);
            }
        });

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(JOptionPane.CANCEL_OPTION);
            }
        });
    }

    /**
     *
     * @param t
     */
    public void setText(String t) {
        iTextPane.setText(t);
    }

}
