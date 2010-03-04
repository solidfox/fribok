package se.swedsoft.bookkeeping.gui.outpayment.panel;

import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.outpayment.util.SSOutpaymentTableModel;
import se.swedsoft.bookkeeping.data.SSOutpayment;
import se.swedsoft.bookkeeping.data.SSOutpayment;
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
public class SSOutpaymentSearchPanel extends JPanel {
    private JPanel iPanel;

    private JTextField iTextField;


    private SSOutpaymentTableModel iModel;


    public SSOutpaymentSearchPanel(SSOutpaymentTableModel iModel) {
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
        List<SSOutpayment> iOutpayments = SSDB.getInstance().getOutpayments();
        List<SSOutpayment> iFiltered = new LinkedList<SSOutpayment>();

        String iText = iTextField.getText();

        if(iText == null) iText = "";

        iText = iText.toLowerCase();

        for (SSOutpayment iOutpayment : iOutpayments) {
            String iNumber      = iOutpayment.getNumber().toString();
            String iDescription = iOutpayment.getText();

            if( (iText.length() == 0) || (iNumber != null && iNumber.toLowerCase().startsWith(iText)) || (iDescription != null && iDescription.toLowerCase().startsWith(iText) ) ){
                iFiltered.add(iOutpayment);
            }
        }
        iModel.setObjects(iFiltered);
    }
}
