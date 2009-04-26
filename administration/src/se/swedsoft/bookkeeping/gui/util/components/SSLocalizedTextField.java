package se.swedsoft.bookkeeping.gui.util.components;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.util.*;
import java.util.List;
import java.awt.*;

/**
 * User: Andreas Lago
 * Date: 2006-okt-16
 * Time: 10:35:49
 */
public class SSLocalizedTextField extends JPanel {


    private List<Locale> iLocales;

    private Map<Locale, String> iDescriptions;

    private Map<Locale, String> iValues;


    private Map<Locale, JTextField> iTextFields;



    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public SSLocalizedTextField() {
        iLocales      = new LinkedList<Locale>();
        iDescriptions = new HashMap<Locale, String>();
        iValues       = new HashMap<Locale, String>();

        iTextFields = new HashMap<Locale, JTextField>();
    }

    /**
     *
     */
    private void createLayout() {
        GridBagLayout      iLayout      = new GridBagLayout();
        GridBagConstraints iConstraints = new GridBagConstraints();

        removeAll();

        setLayout(iLayout);

        iConstraints.insets   = new Insets(1,2,1,2);
        for (Locale iLocale : iLocales) {
            String iDescription = iDescriptions.get(iLocale);
            String iValue       = iValues      .get(iLocale);

            JLabel     iLabel     = new JLabel    ( iDescription );
            JTextField iTextField = new JTextField( iValue );

            iTextField.setPreferredSize( new Dimension(380, iTextField.getPreferredSize().height) );
            iTextField.getDocument().addDocumentListener(new MyDocumentListener(iTextField, iLocale));

            iConstraints.fill      = GridBagConstraints.NONE;
            iConstraints.weightx   = 0.0;
            iConstraints.gridwidth = GridBagConstraints.RELATIVE;

            iLayout.setConstraints(iLabel, iConstraints); add(iLabel);

            iConstraints.fill      = GridBagConstraints.HORIZONTAL;
            iConstraints.weightx   = 1.0;
            iConstraints.gridwidth = GridBagConstraints.REMAINDER;

            iLayout.setConstraints(iTextField, iConstraints); add(iTextField);

            iTextFields.put(iLocale, iTextField);
        }
    }

    /**
     *
     * @param iLocale
     */
    public void addLocale(Locale iLocale, String iDescription){
        addLocale(iLocale,  iDescription,  null);
    }

    /**
     *
     * @param iLocale
     */
    public void addLocale(Locale iLocale, String iDescription, String iValue){
        iLocales.add(iLocale);
        iDescriptions.put(iLocale, iDescription);
        iValues      .put(iLocale, iValue);

        createLayout();
    }


    /**
     *
     * @return
     */
    public Map<Locale, String> getValues() {

         for (Locale iLocale : iLocales) {
             iValues.put(iLocale,  iTextFields.get(iLocale).getText() );
        }

        return iValues;
    }

    /**
     *
     * @param iValues
     */
    public void setValues(Map<Locale, String> iValues) {
        this.iValues = new HashMap<Locale, String>();
        this.iValues.putAll(iValues);

        for (Locale iLocale : iLocales) {
            iTextFields.get(iLocale).setText( iValues.get(iLocale) );
        }
    }




    /**
     *
     * @param iLocale
     * @return
     */
    public String getValue(Locale iLocale) {
        return iValues.get(iLocale);
    }

    /**
     * 
     * @param iLocale
     * @param iLocale
     */
    public void setValue(Locale iLocale, String value) {
        iValues.put(iLocale, value);

        setValues(iValues);
    }


    /**
     *
     */
    private class MyDocumentListener implements DocumentListener {

        private Locale iLocale;

        private JTextField iTextField;

        /**
         *
         * @param iTextField
         * @param iLocale
         */
        public MyDocumentListener(JTextField iTextField, Locale iLocale) {
            this.iLocale    = iLocale;
            this.iTextField = iTextField;
        }

        /**
         *
         * @param e
         */
        public void insertUpdate(DocumentEvent e) {
        }

        /**
         *
         * @param e
         */
        public void removeUpdate(DocumentEvent e) {
        }

        /**
         *
         * @param e
         */
        public void changedUpdate(DocumentEvent e) {
            String iValue = iTextField.getText();

            iValues.put(iLocale, iValue);

        }
    }
}



