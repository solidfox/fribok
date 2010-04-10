package se.swedsoft.bookkeeping.gui.company.panel;


import se.swedsoft.bookkeeping.data.SSStandardText;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * Date: 2006-feb-10
 * Time: 13:35:43
 */
public class SSStandardTextPanel extends JPanel {

    public static ResourceBundle bundle = SSBundle.getBundle();

    private JPanel iPanel;

    private JComboBox iComboBox;

    private JTextPane iTextPane;

    private Map<SSStandardText, String> iTexts;

    private SSStandardText iSelected;

    /**
     *
     */
    public SSStandardTextPanel() {
        setLayout(new BorderLayout());
        add(iPanel, BorderLayout.CENTER);

        DefaultComboBoxModel iModel = new DefaultComboBoxModel();

        iModel.addElement(
                new StandardText(bundle.getString("companypanel.standardtext.1"),
                SSStandardText.Tender));
        iModel.addElement(
                new StandardText(bundle.getString("companypanel.standardtext.2"),
                SSStandardText.Saleorder));
        iModel.addElement(
                new StandardText(bundle.getString("companypanel.standardtext.3"),
                SSStandardText.Customerinvoice));
        iModel.addElement(
                new StandardText(bundle.getString("companypanel.standardtext.4"),
                SSStandardText.Creditinvoice));
        iModel.addElement(
                new StandardText(bundle.getString("companypanel.standardtext.5"),
                SSStandardText.Purchaseorder));
        iModel.addElement(
                new StandardText(bundle.getString("companypanel.standardtext.6"),
                SSStandardText.Reminder));
        iModel.addElement(
                new StandardText(bundle.getString("companypanel.standardtext.7"),
                SSStandardText.Email));

        iSelected = null;
        iTexts = new HashMap<SSStandardText, String>();

        iComboBox.setModel(iModel);

        iComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (iSelected != null) {
                    iTexts.put(iSelected, iTextPane.getText());
                }
                iSelected = ((StandardText) iComboBox.getSelectedItem()).getField();

                if (iSelected != null) {
                    iTextPane.setText(iTexts.get(iSelected));
                }
            }

        });
        iComboBox.setSelectedIndex(0);
    }

    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }

    /**
     *
     * @param pData
     */
    public void setData(Map<SSStandardText, String> pData) {
        iTexts.put(SSStandardText.Tender, pData.get(SSStandardText.Tender));
        iTexts.put(SSStandardText.Saleorder, pData.get(SSStandardText.Saleorder));
        iTexts.put(SSStandardText.Customerinvoice,
                pData.get(SSStandardText.Customerinvoice));
        iTexts.put(SSStandardText.Creditinvoice, pData.get(SSStandardText.Creditinvoice));
        iTexts.put(SSStandardText.Purchaseorder, pData.get(SSStandardText.Purchaseorder));
        iTexts.put(SSStandardText.Reminder, pData.get(SSStandardText.Reminder));
        iTexts.put(SSStandardText.Email, pData.get(SSStandardText.Email));

        if (iSelected != null) {
            iTextPane.setText(iTexts.get(iSelected));
        }
    }

    /**
     *
     * @param pData
     */
    public void getData(Map<SSStandardText, String> pData) {
        if (iSelected != null) {
            iTexts.put(iSelected, iTextPane.getText());
        }
        pData.put(SSStandardText.Tender, iTexts.get(SSStandardText.Tender));
        pData.put(SSStandardText.Saleorder, iTexts.get(SSStandardText.Saleorder));
        pData.put(SSStandardText.Customerinvoice,
                iTexts.get(SSStandardText.Customerinvoice));
        pData.put(SSStandardText.Creditinvoice, iTexts.get(SSStandardText.Creditinvoice));
        pData.put(SSStandardText.Purchaseorder, iTexts.get(SSStandardText.Purchaseorder));
        pData.put(SSStandardText.Reminder, iTexts.get(SSStandardText.Reminder));
        pData.put(SSStandardText.Email, iTexts.get(SSStandardText.Email));

    }

    private class StandardText {

        private String         iDescription;
        private SSStandardText iField;

        public StandardText(String pDescription, SSStandardText pField) {
            iDescription = pDescription;
            iField = pField;
        }

        public String toString() {
            return iDescription;
        }

        public SSStandardText getField() {
            return iField;
        }
    }

}
