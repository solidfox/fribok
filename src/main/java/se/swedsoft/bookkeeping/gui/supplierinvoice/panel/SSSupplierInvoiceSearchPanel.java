package se.swedsoft.bookkeeping.gui.supplierinvoice.panel;


import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.supplierinvoice.SSSupplierInvoiceFrame;
import se.swedsoft.bookkeeping.gui.supplierinvoice.util.SSSupplierInvoiceTableModel;

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
public class SSSupplierInvoiceSearchPanel extends JPanel {
    private JPanel iPanel;

    private JTextField iTextField;

    private SSSupplierInvoiceTableModel iModel;

    public SSSupplierInvoiceSearchPanel(SSSupplierInvoiceTableModel iModel) {
        this.iModel = iModel;

        setLayout(new BorderLayout());
        setVisible(true);
        add(iPanel, BorderLayout.CENTER);

        iTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ApplyFilter(SSDB.getInstance().getSupplierInvoices());
            }
        });
    }

    public void ApplyFilter(List<SSSupplierInvoice> iList) {
        List<SSSupplierInvoice> iFiltered = new LinkedList<SSSupplierInvoice>();

        String iText = iTextField.getText();

        if (iText == null) {
            iText = "";
        }

        iText = iText.toLowerCase();

        for (SSSupplierInvoice iSupplierInvoice : iList) {
            String iNumber = iSupplierInvoice.getNumber().toString();
            String iDescription = iSupplierInvoice.getSupplierName();
            String iSupplierNumber = iSupplierInvoice.getSupplierNr();

            if ((iText.length() == 0)
                    || (iNumber != null && iNumber.toLowerCase().startsWith(iText))
                    || (iDescription != null
                            && iDescription.toLowerCase().startsWith(iText))
                            || (iSupplierNumber != null
                                    && iSupplierNumber.toLowerCase().startsWith(iText))) {
                iFiltered.add(iSupplierInvoice);
            }
        }
        SSSupplierInvoiceFrame.getInstance().setFilterIndex(
                SSSupplierInvoiceFrame.getInstance().getTabbedPane().getSelectedIndex(),
                iFiltered);
        // iModel.setObjects(iFiltered);

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(
                "se.swedsoft.bookkeeping.gui.supplierinvoice.panel.SSSupplierInvoiceSearchPanel");
        sb.append("{iModel=").append(iModel);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iTextField=").append(iTextField);
        sb.append('}');
        return sb.toString();
    }
}
