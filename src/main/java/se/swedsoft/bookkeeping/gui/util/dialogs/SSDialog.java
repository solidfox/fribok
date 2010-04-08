package se.swedsoft.bookkeeping.gui.util.dialogs;

import se.swedsoft.bookkeeping.gui.SSMainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Date: 2006-jan-25
 * Time: 11:04:32
 */
public class SSDialog extends JDialog implements KeyEventDispatcher,ActionListener {

    private int iModalResult;

    {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     *
     * @param owner
     * @param title
     */
    public SSDialog(JFrame owner, String title) {
        this(owner, title, true);
    }

    /**
     *
     * @param owner
     * @param title
     * @param modal
     */
    public SSDialog(JFrame owner, String title, boolean modal) {
        super(owner, title, modal);

        setLayout(new BorderLayout());

        iModalResult = JOptionPane.CLOSED_OPTION;

        // Add the esc-
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    /**
     *
     * @param owner
     * @param title
     */
    public SSDialog(JDialog owner, String title) {
        this(owner, title, true);
    }

    /**
     *
     * @param owner
     * @param title
     * @param modal
     */
    public SSDialog(JDialog owner, String title, boolean modal) {
        super(owner, title, modal);

        setLayout(new BorderLayout());

        iModalResult = JOptionPane.CLOSED_OPTION;

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }

    /**
     *
     */
    public void setVisible(){
        setVisible(true);
    }

    /**
     *
     */
    public void closeDialog(){
        setVisible(false);
        //dispose();
    }

    @Override
    public void dispose()
    {
        removeAll();
        getContentPane().removeAll();
        super.dispose();
    }

    /**
     *
     * @param iModalResult the JOptionPane modal result
     */
    public void closeDialog(int iModalResult){
        this.iModalResult = iModalResult;

        setVisible(false);
        //dispose();
    }

    /*
     * Sets the option pane to use

     * @param The option pane
     */
    public void setOptionPane(final JOptionPane pOptionPane){
        // Add in option pane
        add(pOptionPane, BorderLayout.CENTER);

        // make the OptionPane close the window
        pOptionPane.addPropertyChangeListener( new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                String iEvent = event.getPropertyName();

                if (iEvent.equals(JOptionPane.VALUE_PROPERTY) || iEvent.equals(JOptionPane.INPUT_VALUE_PROPERTY)) {

                    if( pOptionPane.getValue() instanceof Integer ){
                        Integer iModalresult = (Integer) pOptionPane.getValue();

                        setModalResult(iModalresult);
                    }

                    closeDialog();
                }
            }
        });
    }

    /**
     *
     * @param iPanel
     */
    public void setPanel(JPanel iPanel){
        add(iPanel, BorderLayout.CENTER);
        pack();
    }

    /*
      * @param iMainFrame
    */
    public void setLocationRelativeTo(SSMainFrame iMainFrame){
        setLocationRelativeTo(iMainFrame.getDesktopPane());
    }


    /**
     * Sets the modal result for the dialog
     *
     * @param iModalResult
     */
    public void setModalResult(int iModalResult) {
        setModalResult(iModalResult, false);
    }

    /**
     * Sets the modal result for the dialog
     *
     * @param iModalResult
     * @param doHide hide the dialog ?
     */
    public void setModalResult(int iModalResult, boolean doHide) {
        this.iModalResult = iModalResult;

        if(doHide) setVisible(false);
    }

    /**
     * Return the modal result if invisible, <code>JOptionPane.NO_OPTION</code> if visible
     *
     * @return the modal result
     */
    public int getModalResult() {
        return isVisible() ? JOptionPane.NO_OPTION : iModalResult;
    }

    /**
     *
     * @return the modal result
     * @see JOptionPane
     */
    public int showDialog(){
        iModalResult = JOptionPane.CLOSED_OPTION;

        setVisible(true);

        return iModalResult;
    }


    /**
     *
     * @param iFrame
     * @return the modal result
     * @see JOptionPane
     */
    public int showDialog(JFrame iFrame){
        iModalResult = JOptionPane.CLOSED_OPTION;

        setLocationRelativeTo(iFrame);
        setVisible(true);

        return iModalResult;
    }


    /**
     *
     * @param iDialog
     * @return the modal result
     * @see JOptionPane
     */
    public int showDialog(JDialog iDialog){
        iModalResult = JOptionPane.CLOSED_OPTION;

        setLocationRelativeTo(iDialog);
        setVisible(true);

        return iModalResult;
    }

    /**
     *
     * @param iOwner
     * @param iTitle
     * @return the modal result
     */
    public static int showDialog(JFrame iOwner, String iTitle){
        SSDialog iDialog = new SSDialog(iOwner, iTitle);

        return iDialog.showDialog();
    }

    /**
     *
     * @param iOwner
     * @param iTitle
     * @return the modal result
     */
    public static int showDialog(JDialog iOwner, String iTitle){
        SSDialog iDialog = new SSDialog(iOwner, iTitle);

        return iDialog.showDialog();
    }


    /**
     * This method is called by the current KeyboardFocusManager requesting
     * that this KeyEventDispatcher dispatch the specified event on its behalf.
     * @param e the KeyEvent to dispatch
     * @return <code>true</code> if the KeyboardFocusManager should take no
     *         further action with regard to the KeyEvent; <code>false</code>
     *         otherwise
     */
    public boolean dispatchKeyEvent(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            iModalResult = JOptionPane.CLOSED_OPTION;

            if(KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow() != this) return false;

            if(e.getID() == KeyEvent.KEY_PRESSED) {
                processWindowEvent( new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }

            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);

            return true;
        }
        return false;
    }

    public void actionPerformed(ActionEvent e)
    {

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog");
        sb.append("{iModalResult=").append(iModalResult);
        sb.append('}');
        return sb.toString();
    }
}
