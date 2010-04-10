package se.swedsoft.bookkeeping.gui.voucher.dialogs;


import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * User: Fredrik Stigsson
 * Date: 2006-feb-07
 * Time: 10:49:10
 */
public class SSCopyReversedVoucherDialog extends SSDialog {

    private JPanel iPanel;
    private JCheckBox iCopyReverse;

    private JTextField iVerificationName;
    private SSButtonPanel iButtonPanel;

    public SSCopyReversedVoucherDialog(SSMainFrame iDialog) {
        super(iDialog, SSBundle.getBundle().getString("voucherframe.copy.title"));

        add(iPanel, BorderLayout.CENTER);
        pack();
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
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }

    /**
     *
     * @return
     */
    public boolean getCopyInverse() {
        return iCopyReverse.isSelected();
    }

    /**
     *
     * @param iMainFrame
     * @param iVoucher
     * @return
     */
    public static Boolean showDialog(final SSMainFrame iMainFrame, SSVoucher iVoucher) {
        SSCopyReversedVoucherDialog iDialog = new SSCopyReversedVoucherDialog(iMainFrame);

        iDialog.iVerificationName.setText(iVoucher.getDescription());

        iDialog.setLocationRelativeTo(iMainFrame);

        if (iDialog.showDialog() == JOptionPane.OK_OPTION) {
            return iDialog.getCopyInverse();
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(
                "se.swedsoft.bookkeeping.gui.voucher.dialogs.SSCopyReversedVoucherDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iCopyReverse=").append(iCopyReverse);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iVerificationName=").append(iVerificationName);
        sb.append('}');
        return sb.toString();
    }
}

