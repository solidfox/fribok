package se.swedsoft.bookkeeping.gui.invoice.dialog;

import se.swedsoft.bookkeeping.data.common.SSInvoiceType;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.SSMainFrame;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

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
     */
    public SSInvoiceTypeDialog(SSMainFrame iMainFrame) {
        super(iMainFrame, SSBundle.getBundle().getString("invoiceframe.type.title") );

        add(iPanel, BorderLayout.CENTER);
        pack();

        this.iInvoiceType = null;

        iType1Button .addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSInvoiceTypeDialog.this.iInvoiceType = iInvoiceType = SSInvoiceType.NORMAL;
                SSInvoiceTypeDialog.this.setModalResult(JOptionPane.OK_OPTION, true);
            }
        });
        iType2Button .addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSInvoiceTypeDialog.this.iInvoiceType = iInvoiceType = SSInvoiceType.CASH;
                SSInvoiceTypeDialog.this.setModalResult(JOptionPane.OK_OPTION, true);
            }
        });
        iCancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSInvoiceTypeDialog.this.setModalResult(JOptionPane.CANCEL_OPTION, true);
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
     */
    public static SSInvoiceType showDialog(final SSMainFrame iMainFrame) {
        SSInvoiceTypeDialog iDialog = new SSInvoiceTypeDialog(iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);

        if( iDialog.showDialog() == JOptionPane.OK_OPTION ) {
            return iDialog.getInvoiceType();
        }
        return null;
    }
}
