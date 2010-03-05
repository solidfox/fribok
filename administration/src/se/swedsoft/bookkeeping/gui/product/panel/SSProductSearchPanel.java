package se.swedsoft.bookkeeping.gui.product.panel;

import se.swedsoft.bookkeeping.gui.product.util.SSProductTableModel;
import se.swedsoft.bookkeeping.data.SSProduct;
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
public class SSProductSearchPanel extends JPanel {
    private JPanel iPanel;

    private JTextField iTextField;


    private SSProductTableModel iModel;


    public SSProductSearchPanel(SSProductTableModel iModel) {
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
        List<SSProduct> iProducts = SSDB.getInstance().getProducts();
        List<SSProduct> iFiltered = new LinkedList<SSProduct>();

        String iText = iTextField.getText();

        if(iText == null) iText = "";

        iText = iText.toLowerCase();

        for (SSProduct iProduct : iProducts) {
            String iNumber      = iProduct.getNumber();
            String iDescription = iProduct.getDescription();

            if( (iText.length() == 0) || (iNumber != null && iNumber.toLowerCase().startsWith(iText)) || (iDescription != null && iDescription.toLowerCase().startsWith(iText) ) ){
                iFiltered.add(iProduct);
            }
        }
        iModel.setObjects(iFiltered);
    }
}
