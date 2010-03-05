package se.swedsoft.bookkeeping.gui.util;

import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBoxOld;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-apr-03
 * Time: 09:18:12
 */
public class SSInputVerifier implements DocumentListener, ActionListener {

    //public enum

    private List<JComponent> iComponents;

    private List<SSVerifierListener> iListeners;

    // The component that failed the validation
    private JComponent iCurrentComponent = null;

    /**
     *
     */
    public SSInputVerifier(){
        iComponents = new LinkedList<JComponent>();
        iListeners  = new LinkedList<SSVerifierListener>();

    }


    /**
     *
     *
     * @param iTextField
     */
    public void add(JTextField iTextField){
        iTextField.getDocument().addDocumentListener(this);

        iComponents.add(iTextField);
    }

    /**
     *
     * @param iFormattedTextField
     */
    public void add(JFormattedTextField iFormattedTextField){
        iFormattedTextField.getDocument().addDocumentListener(this);

        iComponents.add(iFormattedTextField);
    }

    /**
     *
     * @param iTableComboBox
     */
    public void add(SSTableComboBoxOld iTableComboBox) {
        iTableComboBox.addChangeListener(this);
        iTableComboBox.getDocument().addDocumentListener(this);

        iComponents.add(iTableComboBox);
    }



    /**
     *
     * @param iTableComboBox
     */
    public void add(SSTableComboBox iTableComboBox) {
        iTableComboBox.addChangeListener(this);
        iTableComboBox.getDocument().addDocumentListener(this);

        iComponents.add(iTableComboBox);
    }

    /**
     * 
     */
    public void update(){
        notifyListeners();
    }



    /**
     *
     * @return
     */
    public boolean isValid(){
        for(JComponent iComponent: iComponents){
            iCurrentComponent = iComponent;

            // Textfields
            if(iComponent instanceof JTextField){
                JTextField iTextField = (JTextField)iComponent;

                if( isNullOrEmpty(iTextField.getText() ) ) return false;
            }

            // JFormattedTextField
            if(iComponent instanceof JFormattedTextField){
                JFormattedTextField iFormattedTextField = (JFormattedTextField) iComponent;

                if( iFormattedTextField.getValue() == null) return false;
            }

            // SSTablecombobox
            if(iComponent instanceof SSTableComboBoxOld){
                SSTableComboBoxOld iTableComboBox = (SSTableComboBoxOld)iComponent;

                if(iTableComboBox.doAllowCustomValues()){
                    if( isNullOrEmpty(iTableComboBox.getText()) ) return false;
                } else{
                    if(iTableComboBox.getSelected() == null) return false;
                }

            }

            // SSTablecombobox
            if(iComponent instanceof SSTableComboBox){
                SSTableComboBox iTableComboBox = (SSTableComboBox)iComponent;

                if(iTableComboBox.getValue() == null) return false;
           
            }

        }
        iCurrentComponent = null;

        return true;
    }

    /**
     * Return the component that failed the verifikation, null if isValid() returned true
     *
     * @return the component
     */
    public JComponent getCurrentComponent() {
        return iCurrentComponent;
    }

    /**
     *
     * @param iListener
     */
    public void addListener(SSVerifierListener iListener){
        iListeners.add(iListener);
        // Call the action to initialize the state
        iListener.updated(this, isValid());
    }

    /**
     *
     */
    protected void notifyListeners(){
        boolean iValid = isValid();

        for(SSVerifierListener iListener: iListeners){
            iListener.updated(this, iValid);
        }
    }




    /**
     * Gives notification that there was an insert into the document.  The
     * range given by the DocumentEvent bounds the freshly inserted region.
     *
     * @param e the document event
     */
    public void insertUpdate(DocumentEvent e) {
        notifyListeners();
    }

    /**
     * Gives notification that a portion of the document has been
     * removed.  The range is given in terms of what the view last
     * saw (that is, before updating sticky positions).
     *
     * @param e the document event
     */
    public void removeUpdate(DocumentEvent e) {
        notifyListeners();
    }

    /**
     * Gives notification that an attribute or set of attributes changed.
     *
     * @param e the document event
     */
    public void changedUpdate(DocumentEvent e) {
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        notifyListeners();
    }



    private static boolean isNullOrEmpty(String iText){
        return iText == null || iText.length() == 0;
    }


    /**
     * Interface for the listeners
     */
    public static interface SSVerifierListener{
        public void updated(SSInputVerifier iVerifier, boolean iValid);


    }

}
