package se.swedsoft.bookkeeping.gui.util.components;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Date: 2006-feb-21
 * Time: 09:55:32
 */
public class SSTextField extends JTextField {
    /**
     * Invoked to process the key bindings for <code>ks</code> as the result
     * of the <code>KeyEvent</code> <code>e</code>. This obtains
     * the appropriate <code>InputMap</code>,
     * gets the binding, gets the action from the <code>ActionMap</code>,
     * and then (if the action is found and the component
     * is enabled) invokes <code>notifyAction</code> to notify the action.
     *
     * @param ks        the <code>KeyStroke</code> queried
     * @param e         the <code>KeyEvent</code>
     * @param condition one of the following values:
     *                  <ul>
     *                  <li>JComponent.WHEN_FOCUSED
     *                  <li>JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
     *                  <li>JComponent.WHEN_IN_FOCUSED_WINDOW
     *                  </ul>
     * @param pressed   true if the key is pressed
     * @return true if there was a binding to an action, and the action
     *         was enabled
     * @since 1.3
     */
    @Override
    public boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        return super.processKeyBinding(ks, e, condition, pressed);
    }
}
