package se.swedsoft.bookkeeping.gui.customer.panel;

import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.customer.util.SSCustomerTableModel;

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
public class SSCustomerSearchPanel extends JPanel {
    private JPanel iPanel;

    private JTextField iTextField;
    private SSCustomerTableModel iModel;


    public SSCustomerSearchPanel(SSCustomerTableModel iModel) {
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
        List<SSCustomer> iCustomers = SSDB.getInstance().getCustomers();
        List<SSCustomer> iFiltered = new LinkedList<SSCustomer>();

        String iText = iTextField.getText();

        if(iText == null) iText = "";

        iText = iText.toLowerCase();

        for (SSCustomer iCustomer : iCustomers) {
            String iNumber = iCustomer.getNumber();
            String iName   = iCustomer.getName();

            if( (iText.length() == 0) || (iNumber != null && iNumber.toLowerCase().startsWith(iText)) || (iName != null && iName.toLowerCase().startsWith(iText) ) ){
                iFiltered.add(iCustomer);
            }
        }
        iModel.setObjects(iFiltered);
    }
}
