package se.swedsoft.bookkeeping.gui.license.panel;

import javax.swing.*;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.PlainDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import java.awt.event.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.*;
import java.util.List;
import java.util.LinkedList;

import se.swedsoft.bookkeeping.gui.about.panel.SSAboutPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.util.SSBrowserLaunch;

/**
 * Date: 2006-mar-16
 * Time: 16:37:30
 */
public class SSLicensePanel {

    private JPanel iPanel;
    private JTextField iName;
    private JTextField iCompany;

    private JTextField iKeyField1;
    private JTextField iKeyField2;
    private JTextField iKeyField3;
    private JTextField iKeyField4;
    private JTextField iKeyField5;
    private JTextField iKeyField6;
    private JButton iCancelButton;
    private JButton iOkButton;

    private JEditorPane iEditorPane;

    private List<JTextField> iKeyFields;

    public SSLicensePanel() {
        String iText = SSBundle.getBundle().getString("licenseframe.description");

        iEditorPane.setBackground( iPanel.getBackground() );
        iEditorPane.setText(iText);

        iEditorPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                String iEventName = e.getEventType() == null ? "" : e.getEventType().toString();

                if(iEventName.equals("ACTIVATED")){
                    SSBrowserLaunch.openURL(e.getURL());
                }
                if(iEventName.equals("ENTERED")){
                    iEditorPane.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                }
                if(iEventName.equals("EXITED")){
                    iEditorPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });


        iKeyFields = new LinkedList<JTextField>();
        iKeyFields.add(iKeyField1);
        iKeyFields.add(iKeyField2);
        iKeyFields.add(iKeyField3);
        iKeyFields.add(iKeyField4);
        iKeyFields.add(iKeyField5);
        iKeyFields.add(iKeyField6);
        iKeyField1.setDocument(new KeyFieldDocument(5));
        iKeyField2.setDocument(new KeyFieldDocument(5));
        iKeyField3.setDocument(new KeyFieldDocument(5));
        iKeyField4.setDocument(new KeyFieldDocument(5));
        iKeyField5.setDocument(new KeyFieldDocument(5));
        iKeyField6.setDocument(new KeyFieldDocument(3));

        // The traversal action.
        Action iTraversal = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JTextField iCurrent = (JTextField)e.getSource();

                int iIndex = iKeyFields.indexOf( iCurrent )+1;

                iKeyFields.get(iIndex).requestFocus();

            }
        };
        // The paste action.
        Action iPaste = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Clipboard iClipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();

                Transferable iTransferable = iClipBoard.getContents(null);

                if( iTransferable != null){
                    try {
                        String iData = (String)iTransferable.getTransferData(DataFlavor.stringFlavor);

                        if(iData != null){

                            String [] iFields = iData.split("-");

                            for(int i =0 ; i < iFields.length; i++){

                                if(i >= iKeyFields.size()) return;

                                iKeyFields.get(i).setText(iFields[i]);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            }
        };





        iKeyField1.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER , 0), "TRAVERSAL");
        iKeyField2.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER , 0), "TRAVERSAL");
        iKeyField3.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER , 0), "TRAVERSAL");
        iKeyField4.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER , 0), "TRAVERSAL");
        iKeyField5.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER , 0), "TRAVERSAL");

        iKeyField1.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V  , InputEvent.CTRL_MASK), "PASTE");



        iKeyField1.getActionMap().put("TRAVERSAL", iTraversal);
        iKeyField2.getActionMap().put("TRAVERSAL", iTraversal);
        iKeyField3.getActionMap().put("TRAVERSAL", iTraversal);
        iKeyField4.getActionMap().put("TRAVERSAL", iTraversal);
        iKeyField5.getActionMap().put("TRAVERSAL", iTraversal);

        iKeyField1.getActionMap().put("PASTE", iPaste);
        iKeyField2.getActionMap().put("PASTE", iPaste);
        iKeyField3.getActionMap().put("PASTE", iPaste);
        iKeyField4.getActionMap().put("PASTE", iPaste);
        iKeyField5.getActionMap().put("PASTE", iPaste);

        // The traversal action.
        KeyAdapter iKeyAdapter = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                JTextField iCurrent = (JTextField)e.getSource();

                String iText = iCurrent.getText();

                if(iText.length() == 5) {
                    int iIndex = iKeyFields.indexOf( iCurrent )+1;

                    iKeyFields.get(iIndex).requestFocus();
                }
            }
        };

        iKeyField1.addKeyListener(iKeyAdapter);
        iKeyField2.addKeyListener(iKeyAdapter);
        iKeyField3.addKeyListener(iKeyAdapter);
        iKeyField4.addKeyListener(iKeyAdapter);
        iKeyField5.addKeyListener(iKeyAdapter);


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
     * @return
     */
    public String getName() {
        return iName.getText();
    }

    /**
     *
     * @return
     */
    public String getKey(){
        StringBuilder sb = new StringBuilder();

        for(JTextField iTextField: iKeyFields){
            sb.append( iTextField.getText() );
            if(iTextField !=  iKeyField6) sb.append("-");
        }
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public String getCompany() {
        return iCompany.getText();
    }

    /**
     *
     * @param iListener
     */
    public void addOkListener(ActionListener iListener){
        iOkButton.addActionListener(iListener);
    }


    /**
     *
     * @param iListener
     */
    public void addCancelListener(ActionListener iListener){
        iCancelButton.addActionListener(iListener);
    }




    private class KeyFieldDocument extends PlainDocument {

        private int limit;

        public KeyFieldDocument(int limit) {
            super();
            this.limit = limit;
        }



        public void insertString(int offset, String  str, AttributeSet attr) throws BadLocationException {
            if (str == null) return;

            str =  str.trim().toUpperCase();

            if ((getLength() + str.length()) <= limit) {
                super.insertString(offset, str, attr);
            }
        }
    }

}
