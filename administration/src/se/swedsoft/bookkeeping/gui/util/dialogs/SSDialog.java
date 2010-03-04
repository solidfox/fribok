package se.swedsoft.bookkeeping.gui.util.dialogs;

import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.inpayment.SSInpaymentDialog;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Date: 2006-jan-25
 * Time: 11:04:32
 */
public class SSDialog extends JDialog implements KeyEventDispatcher,ActionListener {

    private int iModalResult;



    /**
     *
     * @param iFrame
     * @param title
     */
    public SSDialog(JFrame iFrame, String title) {
        this(iFrame, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     *
     * @param iFrame
     * @param title
     * @param modal
     */
    public SSDialog(JFrame iFrame, String title, boolean modal) {
        super(iFrame, title, modal);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        iModalResult = JOptionPane.CLOSED_OPTION;

        // Add the esc-
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);

    }


    /**
     *
     * @param iDialog
     * @param title
     */
    public SSDialog(JDialog iDialog, String title) {
        this(iDialog, title, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     *
     * @param iDialog
     * @param title
     * @param modal
     */
    public SSDialog(JDialog iDialog, String title, boolean modal) {
        super(iDialog, title, modal);

        //Make sure memory is freed when dialog is closed.
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        iModalResult = JOptionPane.CLOSED_OPTION;

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
    }



    /*
    *
    */
    public void setVisible(){
        setVisible(true);
    }

    /*
    *
    */
    public void closeDialog(){
        setVisible(false);
        //this.dispose();
        //System.gc();
    }

    @Override
    public void dispose()
    {
        this.removeAll();
        this.getContentPane().removeAll();
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
        //System.gc();
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

                if ((iEvent.equals(JOptionPane.VALUE_PROPERTY) || iEvent.equals(JOptionPane.INPUT_VALUE_PROPERTY))) {

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

}

