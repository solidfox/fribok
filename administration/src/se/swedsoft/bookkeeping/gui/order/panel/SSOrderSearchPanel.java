package se.swedsoft.bookkeeping.gui.order.panel;

import se.swedsoft.bookkeeping.gui.order.util.SSOrderTableModel;
import se.swedsoft.bookkeeping.gui.order.SSOrderFrame;
import se.swedsoft.bookkeeping.gui.order.util.SSOrderTableModel;
import se.swedsoft.bookkeeping.data.SSOrder;
import se.swedsoft.bookkeeping.data.SSOrder;
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
public class SSOrderSearchPanel extends JPanel {
    private JPanel iPanel;

    private JTextField iTextField;

    private SSOrderTableModel iModel;


    public SSOrderSearchPanel(SSOrderTableModel iModel) {
        this.iModel = iModel;

        setLayout(new BorderLayout());
        setVisible(true);
        add(iPanel, BorderLayout.CENTER);


        iTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ApplyFilter(SSDB.getInstance().getOrders());
            }
        });
    }


    public void ApplyFilter(List<SSOrder> iList) {
        List<SSOrder> iFiltered = new LinkedList<SSOrder>();

        String iText = iTextField.getText();

        if(iText == null) iText = "";

        iText = iText.toLowerCase();

        for (SSOrder iOrder : iList) {
            String iNumber      = iOrder.getNumber() == null ? "" : iOrder.getNumber().toString();
            String iDescription = iOrder.getCustomerName();
            String iCustomerNumber = iOrder.getCustomerNr();

            if( (iText.length() == 0) || (iNumber != null && iNumber.toLowerCase().startsWith(iText)) || (iDescription != null && iDescription.toLowerCase().startsWith(iText) ) || (iCustomerNumber != null && iCustomerNumber.toLowerCase().startsWith(iText) ) ){
                iFiltered.add(iOrder);
            }
        }
        SSOrderFrame.getInstance().setFilterIndex(SSOrderFrame.getInstance().getTabbedPane().getSelectedIndex(),iFiltered);
        //iModel.setObjects(iFiltered);

    }
}
