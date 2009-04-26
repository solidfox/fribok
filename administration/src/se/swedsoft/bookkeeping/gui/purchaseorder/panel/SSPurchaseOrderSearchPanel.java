package se.swedsoft.bookkeeping.gui.purchaseorder.panel;

import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.purchaseorder.SSPurchaseOrderFrame;
import se.swedsoft.bookkeeping.gui.purchaseorder.util.SSPurchaseOrderTableModel;
import se.swedsoft.bookkeeping.data.SSPurchaseOrder;
import se.swedsoft.bookkeeping.data.SSPurchaseOrder;
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
public class SSPurchaseOrderSearchPanel extends JPanel {
    private JPanel iPanel;

    private JTextField iTextField;

    private SSPurchaseOrderTableModel iModel;


    public SSPurchaseOrderSearchPanel(SSPurchaseOrderTableModel iModel) {
        this.iModel = iModel;

        setLayout(new BorderLayout());
        setVisible(true);
        add(iPanel, BorderLayout.CENTER);


        iTextField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                ApplyFilter(SSDB.getInstance().getPurchaseOrders());
            }
        });
    }


    public void ApplyFilter(List<SSPurchaseOrder> iList) {
        List<SSPurchaseOrder> iFiltered = new LinkedList<SSPurchaseOrder>();

        String iText = iTextField.getText();

        if(iText == null) iText = "";

        iText = iText.toLowerCase();

        for (SSPurchaseOrder iPurchaseOrder : iList) {
            String iNumber      = iPurchaseOrder.getNumber().toString();

            String iDescription = "";
            String iSupplierNumber = "";
            if (iPurchaseOrder.getSupplier() != null) {
                iDescription = iPurchaseOrder.getSupplierName();
                iSupplierNumber = iPurchaseOrder.getSupplierNr();
            }


            if( (iText.length() == 0) || (iNumber != null && iNumber.toLowerCase().startsWith(iText)) || (iDescription != null && iDescription.toLowerCase().startsWith(iText) ) || (iSupplierNumber != null && iSupplierNumber.toLowerCase().startsWith(iText) ) ){
                iFiltered.add(iPurchaseOrder);
            }
        }
        SSPurchaseOrderFrame.getInstance().setFilterIndex(SSPurchaseOrderFrame.getInstance().getTabbedPane().getSelectedIndex(),iFiltered);
        //iModel.setObjects(iFiltered);

    }
}
