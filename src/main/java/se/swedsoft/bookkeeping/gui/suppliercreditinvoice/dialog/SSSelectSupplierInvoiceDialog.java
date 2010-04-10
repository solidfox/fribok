package se.swedsoft.bookkeeping.gui.suppliercreditinvoice.dialog;


import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.supplierinvoice.util.SSSupplierInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * User: Andreas Lago
 * Date: 2006-apr-21
 * Time: 11:04:30
 */
public class SSSelectSupplierInvoiceDialog extends SSDialog {

    private SSTableComboBox<SSSupplierInvoice> iInvoices;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    /**
     *
     * @param iMainFrame
     */
    public SSSelectSupplierInvoiceDialog(SSMainFrame iMainFrame) {
        super(iMainFrame,
                SSBundle.getBundle().getString(
                "suppliercreditinvoiceframe.selectinvoice.title"));

        setLayout(new BorderLayout());
        add(iPanel, BorderLayout.CENTER);

        pack();

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.CANCEL_OPTION);
                setVisible(false);
            }
        });

        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.OK_OPTION);
                setVisible(false);
            }
        });

        iInvoices.setModel(SSSupplierInvoiceTableModel.getDropDownModel());
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
     * @param l
     */
    public void addOkActionListener(ActionListener l) {
        iButtonPanel.addOkActionListener(l);
    }

    /**
     *
     * @param l
     */
    public void addCancelActionListener(ActionListener l) {
        iButtonPanel.addCancelActionListener(l);
    }

    /**
     *
     * @param iMainFrame
     * @return
     */
    public static SSSupplierInvoice showDialog(SSMainFrame iMainFrame) {
        SSSelectSupplierInvoiceDialog iDialog = new SSSelectSupplierInvoiceDialog(
                iMainFrame);

        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible(true);

        if (iDialog.getModalResult() != JOptionPane.OK_OPTION) {
            SSSupplierInvoice iTemp = new SSSupplierInvoice();

            iTemp.setNumber(-1);
            return iTemp;
        }

        SSSupplierInvoice selected = iDialog.iInvoices.getSelected();

        return SSDB.getInstance().getSupplierInvoice(selected);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(
                "se.swedsoft.bookkeeping.gui.suppliercreditinvoice.dialog.SSSelectSupplierInvoiceDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iInvoices=").append(iInvoices);
        sb.append(", iPanel=").append(iPanel);
        sb.append('}');
        return sb.toString();
    }
}
