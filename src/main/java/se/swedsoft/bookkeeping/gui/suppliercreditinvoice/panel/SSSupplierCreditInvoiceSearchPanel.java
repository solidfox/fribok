package se.swedsoft.bookkeeping.gui.suppliercreditinvoice.panel;


import se.swedsoft.bookkeeping.data.SSSupplierCreditInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;


/**
 * User: Andreas Lago
 * Date: 2006-nov-15
 * Time: 14:57:36
 */
public class SSSupplierCreditInvoiceSearchPanel extends JPanel {
    private JPanel iPanel;

    private JTextField iTextField;

    private SSTableModel<SSSupplierCreditInvoice> iModel;

    public SSSupplierCreditInvoiceSearchPanel(SSTableModel<SSSupplierCreditInvoice> iModel) {
        this.iModel = iModel;

        setLayout(new BorderLayout());
        setVisible(true);
        add(iPanel, BorderLayout.CENTER);

        iTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ApplyFilter();
            }
        });
    }

    public void ApplyFilter() {
        List<SSSupplierCreditInvoice> iFiltered = new LinkedList<SSSupplierCreditInvoice>();
        List<SSSupplierCreditInvoice> iInvoices = SSDB.getInstance().getSupplierCreditInvoices();
        String iText = iTextField.getText();

        if (iText == null) {
            iText = "";
        }

        iText = iText.toLowerCase();

        for (SSSupplierCreditInvoice iSupplierCreditInvoice : iInvoices) {
            String iNumber = iSupplierCreditInvoice.getNumber().toString();
            String iDescription = iSupplierCreditInvoice.getSupplierName();
            String iCustomerNumber = iSupplierCreditInvoice.getSupplierNr();

            if ((iText.length() == 0)
                    || (iNumber != null && iNumber.toLowerCase().startsWith(iText))
                    || (iDescription != null
                            && iDescription.toLowerCase().startsWith(iText))
                            || (iCustomerNumber != null
                                    && iCustomerNumber.toLowerCase().startsWith(iText))) {
                iFiltered.add(iSupplierCreditInvoice);
            }
        }
        iModel.setObjects(iFiltered);

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(
                "se.swedsoft.bookkeeping.gui.suppliercreditinvoice.panel.SSSupplierCreditInvoiceSearchPanel");
        sb.append("{iModel=").append(iModel);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iTextField=").append(iTextField);
        sb.append('}');
        return sb.toString();
    }
}
