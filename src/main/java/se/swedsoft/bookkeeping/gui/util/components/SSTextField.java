package se.swedsoft.bookkeeping.gui.util.components;


import javax.swing.*;
import java.awt.event.KeyEvent;


/**
 * Date: 2006-feb-21
 * Time: 09:55:32
 */
public class SSTextField extends JTextField {
    @Override
    public boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        return super.processKeyBinding(ks, e, condition, pressed);
    }
}
