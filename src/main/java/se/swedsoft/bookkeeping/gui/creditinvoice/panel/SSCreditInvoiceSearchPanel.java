package se.swedsoft.bookkeeping.gui.creditinvoice.panel;


import se.swedsoft.bookkeeping.data.SSCreditInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.creditinvoice.util.SSCreditInvoiceTableModel;

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
public class SSCreditInvoiceSearchPanel extends JPanel {
    private JPanel iPanel;

    private JTextField iTextField;

    private SSCreditInvoiceTableModel iModel;

    public SSCreditInvoiceSearchPanel(SSCreditInvoiceTableModel iModel) {
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
        List<SSCreditInvoice> iCreditInvoices = SSDB.getInstance().getCreditInvoices();
        List<SSCreditInvoice> iFiltered = new LinkedList<SSCreditInvoice>();

        String iText = iTextField.getText();

        if (iText == null) {
            iText = "";
        }

        iText = iText.toLowerCase();

        for (SSCreditInvoice iCreditInvoice : iCreditInvoices) {
            String iNumber = iCreditInvoice.getNumber().toString();
            String iDescription = iCreditInvoice.getCustomerName();
            String iCustomerNumber = iCreditInvoice.getCustomerNr();

            if ((iText.length() == 0)
                    || (iNumber != null && iNumber.toLowerCase().startsWith(iText))
                    || (iDescription != null
                            && iDescription.toLowerCase().startsWith(iText))
                            || (iCustomerNumber != null
                                    && iCustomerNumber.toLowerCase().startsWith(iText))) {
                iFiltered.add(iCreditInvoice);
            }
        }
        iModel.setObjects(iFiltered);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(
                "se.swedsoft.bookkeeping.gui.creditinvoice.panel.SSCreditInvoiceSearchPanel");
        sb.append("{iModel=").append(iModel);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iTextField=").append(iTextField);
        sb.append('}');
        return sb.toString();
    }
}
