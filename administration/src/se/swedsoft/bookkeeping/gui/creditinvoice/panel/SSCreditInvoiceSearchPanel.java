package se.swedsoft.bookkeeping.gui.creditinvoice.panel;

import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.creditinvoice.util.SSCreditInvoiceTableModel;
import se.swedsoft.bookkeeping.data.SSCreditInvoice;
import se.swedsoft.bookkeeping.data.SSCreditInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.util.*;
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

        if(iText == null) iText = "";

        iText = iText.toLowerCase();

        for (SSCreditInvoice iCreditInvoice : iCreditInvoices) {
            String iNumber      = iCreditInvoice.getNumber().toString();
            String iDescription = iCreditInvoice.getCustomerName();
            String iCustomerNumber = iCreditInvoice.getCustomerNr();

            if( (iText.length() == 0) || (iNumber != null && iNumber.toLowerCase().startsWith(iText)) || (iDescription != null && iDescription.toLowerCase().startsWith(iText) ) || (iCustomerNumber != null && iCustomerNumber.toLowerCase().startsWith(iText) ) ){
                iFiltered.add(iCreditInvoice);
            }
        }
        iModel.setObjects(iFiltered);
    }
}
