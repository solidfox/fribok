package se.swedsoft.bookkeeping.gui.periodicinvoice.dialog;


import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSPeriodicInvoice;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.periodicinvoice.util.SSPendingInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;


/**
 * User: Andreas Lago
 * Date: 2006-aug-14
 * Time: 12:19:27
 */
public class SSPendingPeriodicInvoiceDialog extends SSDialog {

    private SSPendingInvoiceTableModel iModel;

    private SSButtonPanel iButtonPanel;

    private JPanel iPanel;

    private SSTable iTable;

    // The periodic invoices and the new invoices
    private Map<SSPeriodicInvoice, List<SSInvoice>> iInvoices;

    /**
     *
     * @param iMainFrame
     * @param iInvoices
     */
    public SSPendingPeriodicInvoiceDialog(SSMainFrame iMainFrame, Map<SSPeriodicInvoice, List<SSInvoice>> iInvoices) {
        super(iMainFrame,
                SSBundle.getBundle().getString("periodicinvoiceframe.invoice.title"));
        this.iInvoices = iInvoices;

        iModel = new SSPendingInvoiceTableModel(iInvoices);
        iModel.addColumn(iModel.getSelectionColumn(), true);
        iModel.addColumn(SSPendingInvoiceTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSPendingInvoiceTableModel.COLUMN_DESCRIPTION);
        iModel.addColumn(SSPendingInvoiceTableModel.COLUMN_CUSTOMER_NR);
        iModel.addColumn(SSPendingInvoiceTableModel.COLUMN_CUSTOMER_NAME);
        iModel.addColumn(SSPendingInvoiceTableModel.COLUMN_DATE);

        iModel.setupTable(iTable);
        iModel.selectAll();

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
        iButtonPanel.getOkButton().setEnabled(!iInvoices.isEmpty());

        setPanel(iPanel);
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
     * @param iInvoices
     * @return
     */
    public static Map<SSPeriodicInvoice, List<SSInvoice>> showDialog(SSMainFrame iMainFrame, Map<SSPeriodicInvoice, List<SSInvoice>> iInvoices) {
        SSPendingPeriodicInvoiceDialog iDialog = new SSPendingPeriodicInvoiceDialog(
                iMainFrame, iInvoices);

        iDialog.setSize(800, 600);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible(true);

        if (iDialog.getModalResult() != JOptionPane.OK_OPTION) {
            return null;
        }

        return iDialog.iModel.getSelected();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(
                "se.swedsoft.bookkeeping.gui.periodicinvoice.dialog.SSPendingPeriodicInvoiceDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iInvoices=").append(iInvoices);
        sb.append(", iModel=").append(iModel);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iTable=").append(iTable);
        sb.append('}');
        return sb.toString();
    }
}
