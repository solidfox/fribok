package se.swedsoft.bookkeeping.gui.tender.panel;

import se.swedsoft.bookkeeping.data.SSTender;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.tender.SSTenderFrame;
import se.swedsoft.bookkeeping.gui.tender.util.SSTenderTableModel;

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
public class SSTenderSearchPanel extends JPanel {
    private JPanel iPanel;

    private JTextField iTextField;

    private SSTenderTableModel iModel;


    public SSTenderSearchPanel(SSTenderTableModel iModel) {
        this.iModel = iModel;

        setLayout(new BorderLayout());
        setVisible(true);
        add(iPanel, BorderLayout.CENTER);


        iTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ApplyFilter(SSDB.getInstance().getTenders());
            }
        });
    }


    public void ApplyFilter(List<SSTender> iList) {
        List<SSTender> iFiltered = new LinkedList<SSTender>();

        String iText = iTextField.getText();

        if(iText == null) iText = "";

        iText = iText.toLowerCase();

        for (SSTender iTender : iList) {
            String iNumber      = iTender.getNumber().toString();
            String iDescription = iTender.getCustomerName();
            String iCustomerNumber = iTender.getCustomerNr();

            if( (iText.length() == 0) || (iNumber != null && iNumber.toLowerCase().startsWith(iText)) || (iDescription != null && iDescription.toLowerCase().startsWith(iText) ) || (iCustomerNumber != null && iCustomerNumber.toLowerCase().startsWith(iText) ) ){
                iFiltered.add(iTender);
            }
        }
        SSTenderFrame.getInstance().setFilterIndex(SSTenderFrame.getInstance().getTabbedPane().getSelectedIndex(),iFiltered);
        //iModel.setObjects(iFiltered);

    }
}
