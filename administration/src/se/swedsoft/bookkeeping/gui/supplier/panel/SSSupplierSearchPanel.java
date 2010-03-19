package se.swedsoft.bookkeeping.gui.supplier.panel;

import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.supplier.util.SSSupplierTableModel;

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
            @Override
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.supplier.panel.SSSupplierSearchPanel");
        sb.append("{iModel=").append(iModel);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iTextField=").append(iTextField);
        sb.append('}');
        return sb.toString();
    }
}
