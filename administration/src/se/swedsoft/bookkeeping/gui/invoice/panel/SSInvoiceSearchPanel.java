package se.swedsoft.bookkeeping.gui.invoice.panel;

import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.invoice.SSInvoiceFrame;
import se.swedsoft.bookkeeping.gui.invoice.util.SSInvoiceTableModel;

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
public class SSInvoiceSearchPanel extends JPanel {
    private JPanel iPanel;

    private JTextField iTextField;

    private SSInvoiceTableModel iModel;


    public SSInvoiceSearchPanel(SSInvoiceTableModel iModel) {
        this.iModel = iModel;

        setLayout(new BorderLayout());
        setVisible(true);
        add(iPanel, BorderLayout.CENTER);


        iTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ApplyFilter(SSDB.getInstance().getInvoices());
            }
        });
    }


    public void ApplyFilter(List<SSInvoice> iList) {
        List<SSInvoice> iFiltered = new LinkedList<SSInvoice>();

        String iText = iTextField.getText();

        if(iText == null) iText = "";

        iText = iText.toLowerCase();

        for (SSInvoice iInvoice : iList) {
            String iNumber      = iInvoice.getNumber().toString();
            String iDescription = iInvoice.getCustomerName();
            String iCustomerNumber = iInvoice.getCustomerNr();

            if( (iText.length() == 0) || (iNumber != null && iNumber.toLowerCase().startsWith(iText)) || (iDescription != null && iDescription.toLowerCase().startsWith(iText) ) || (iCustomerNumber != null && iCustomerNumber.toLowerCase().startsWith(iText) ) ){
                iFiltered.add(iInvoice);
            }
        }
        SSInvoiceFrame.getInstance().setFilterIndex(SSInvoiceFrame.getInstance().getTabbedPane().getSelectedIndex(),iFiltered);
        //iModel.setObjects(iFiltered);

    }
}
