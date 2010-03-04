package se.swedsoft.bookkeeping.gui.periodicinvoice.panel;

import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.periodicinvoice.util.SSPeriodicInvoiceTableModel;
import se.swedsoft.bookkeeping.data.SSPeriodicInvoice;
import se.swedsoft.bookkeeping.data.SSPeriodicInvoice;
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
public class SSPeriodicInvoiceSearchPanel extends JPanel {
    private JPanel iPanel;

    private JTextField iTextField;


    private SSPeriodicInvoiceTableModel iModel;


    public SSPeriodicInvoiceSearchPanel(SSPeriodicInvoiceTableModel iModel) {
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
        List<SSPeriodicInvoice> iPeriodicInvoices = SSDB.getInstance().getPeriodicInvoices();
        List<SSPeriodicInvoice> iFiltered = new LinkedList<SSPeriodicInvoice>();

        String iText = iTextField.getText();

        if(iText == null) iText = "";

        iText = iText.toLowerCase();

        for (SSPeriodicInvoice iPeriodicInvoice : iPeriodicInvoices) {
            String iNumber      = iPeriodicInvoice.getNumber().toString();
            String iDescription = iPeriodicInvoice.getInvoices().get(0).getCustomerName();
            String iCustomerNumber = iPeriodicInvoice.getInvoices().get(0).getCustomerNr();

            if( (iText.length() == 0) || (iNumber != null && iNumber.toLowerCase().startsWith(iText)) || (iDescription != null && iDescription.toLowerCase().startsWith(iText) ) || (iCustomerNumber != null && iCustomerNumber.toLowerCase().startsWith(iText) ) ){
                iFiltered.add(iPeriodicInvoice);
            }
        }
        iModel.setObjects(iFiltered);
    }
}
