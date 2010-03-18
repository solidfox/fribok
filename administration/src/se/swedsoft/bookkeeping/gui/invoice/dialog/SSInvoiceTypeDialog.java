package se.swedsoft.bookkeeping.gui.invoice.dialog;

import se.swedsoft.bookkeeping.data.common.SSInvoiceType;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Andreas Lago
 * Date: 2006-apr-05
 * Time: 09:23:41
 */
public class SSInvoiceTypeDialog extends SSDialog {

    private SSInvoiceType iInvoiceType;

    private JButton iType1Button;
    private JButton iType2Button;

    private JPanel iPanel;

    private JButton iCancelButton;

    /**
     *
     * @param iMainFrame
     */
    public SSInvoiceTypeDialog(SSMainFrame iMainFrame) {
        super(iMainFrame, SSBundle.getBundle().getString("invoiceframe.type.title") );

        add(iPanel, BorderLayout.CENTER);
        pack();

        iInvoiceType = null;

        iType1Button .addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iInvoiceType = iInvoiceType = SSInvoiceType.NORMAL;
                setModalResult(JOptionPane.OK_OPTION, true);
            }
        });
        iType2Button .addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iInvoiceType = iInvoiceType = SSInvoiceType.CASH;
                setModalResult(JOptionPane.OK_OPTION, true);
            }
        });
        iCancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.CANCEL_OPTION, true);
            }
        });
    }


    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }

    /**
     *
     * @return
     */
    public SSInvoiceType getInvoiceType() {
        return iInvoiceType;
    }


    /**
     *
     * @param iMainFrame
     * @return
     */
    public static SSInvoiceType showDialog(final SSMainFrame iMainFrame) {
        SSInvoiceTypeDialog iDialog = new SSInvoiceTypeDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if( iDialog.showDialog() == JOptionPane.OK_OPTION ) {
            return iDialog.iInvoiceType;
        }
        return null;
    }
}
