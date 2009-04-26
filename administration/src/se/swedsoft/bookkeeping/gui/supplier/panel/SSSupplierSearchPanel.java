package se.swedsoft.bookkeeping.gui.supplier.panel;

import se.swedsoft.bookkeeping.gui.customer.util.SSCustomerTableModel;
import se.swedsoft.bookkeeping.gui.supplier.util.SSSupplierTableModel;
import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.system.SSDB;

import javax.swing.*;
import java.util.List;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;


/**
 * User: Andreas Lago
 * Date: 2006-nov-15
 * Time: 14:57:36
 */
public class SSSupplierSearchPanel extends JPanel {
    private JPanel iPanel;

    private JTextField iTextField;

    private SSSupplierTableModel iModel;


    public SSSupplierSearchPanel(SSSupplierTableModel iModel) {
        this.iModel = iModel;

        setLayout(new BorderLayout());
        setVisible(true);
        add(iPanel, BorderLayout.CENTER);


        iTextField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                ApplyFilter();
            }
        });
    }


    public void ApplyFilter() {
        List<SSSupplier> iSuppliers = SSDB.getInstance().getSuppliers();
        List<SSSupplier> iFiltered  = new LinkedList<SSSupplier>();

        String iText = iTextField.getText();

        if(iText == null) iText = "";

        iText = iText.toLowerCase();

        for (SSSupplier iSupplier : iSuppliers) {
            String iNumber = iSupplier.getNumber();
            String iName   = iSupplier.getName();

            if( (iText.length() == 0) || (iNumber != null && iNumber.toLowerCase().startsWith(iText)) || (iName != null && iName.toLowerCase().startsWith(iText) ) ){
                iFiltered.add(iSupplier);
            }
        }
        iModel.setObjects(iFiltered);
    }
}
