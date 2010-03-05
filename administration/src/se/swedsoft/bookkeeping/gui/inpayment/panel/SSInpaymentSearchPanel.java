package se.swedsoft.bookkeeping.gui.inpayment.panel;

import se.swedsoft.bookkeeping.gui.inpayment.util.SSInpaymentTableModel;
import se.swedsoft.bookkeeping.data.SSInpayment;
import se.swedsoft.bookkeeping.data.SSInpayment;
import se.swedsoft.bookkeeping.data.system.SSDB;

import javax.swing.*;
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
public class SSInpaymentSearchPanel extends JPanel {
    private JPanel iPanel;

    private JTextField iTextField;


    private SSInpaymentTableModel iModel;


    public SSInpaymentSearchPanel(SSInpaymentTableModel iModel) {
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
        List<SSInpayment> iInpayments = SSDB.getInstance().getInpayments();
        List<SSInpayment> iFiltered = new LinkedList<SSInpayment>();

        String iText = iTextField.getText();

        if(iText == null) iText = "";

        iText = iText.toLowerCase();

        for (SSInpayment iInpayment : iInpayments) {
            String iNumber      = iInpayment.getNumber().toString();
            String iDescription = iInpayment.getText();

            if( (iText.length() == 0) || (iNumber != null && iNumber.toLowerCase().startsWith(iText)) || (iDescription != null && iDescription.toLowerCase().startsWith(iText) ) ){
                iFiltered.add(iInpayment);
            }
        }
        iModel.setObjects(iFiltered);
    }
}
