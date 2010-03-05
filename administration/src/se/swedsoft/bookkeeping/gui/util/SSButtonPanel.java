package se.swedsoft.bookkeeping.gui.util;

import se.swedsoft.bookkeeping.gui.util.components.SSButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * User: Andreas Lago
 * Date: 2006-apr-21
 * Time: 11:15:56
 */
public class SSButtonPanel extends JPanel {
    private JPanel iPanel;

    private SSButton iCancelButton;

    private SSButton iOkButton;

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public SSButtonPanel() {
        setLayout(new BorderLayout());

        add(iPanel, BorderLayout.CENTER);
    }

    /**
     * Add a action thats invoked when the ok button is pressed.
     *
     * @param iListener the action listener
     */
    public void addOkActionListener(ActionListener iListener) {
        iOkButton.addActionListener(iListener);
    }

    /**
     *  Add a action thats invoked when the cancel button is pressed.
     *
     * @param iListener the action listener
     */
    public void addCancelActionListener(ActionListener iListener) {
        iCancelButton.addActionListener(iListener);
    }

    /**
     *
     * @return the cancel button
     */
    public SSButton getCancelButton() {
        return iCancelButton;
    }

    /**
     *
     * @return the ok button
     */
    public SSButton getOkButton() {
        return iOkButton;
    }


    /**
     * Used to clean up references making sure the garbage collector
     * is able to clean up the object.
     */
    public void dispose() {
        iPanel.removeAll();
        iPanel=null;
        iCancelButton.dispose();
        iCancelButton=null;
        iOkButton.dispose();
        iOkButton=null;
    }
}
