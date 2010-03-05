package se.swedsoft.bookkeeping.gui.util.menu;

import se.swedsoft.bookkeeping.gui.util.SSBundle;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Date: 2006-jan-25
 * Time: 09:12:19
 */
public class SSMenuUtils {

    private static SSBundle iBundle = SSBundle.getBundle();


    /**
     * @param name The name of the resouce
     *
     * @return The menu
     */
    public static JMenu createMenu(String name){
        // Get the title and mnemonic from the bundle
        String title       = iBundle.getString(name + ".title");
        String mnemonic    = iBundle.getString(name + ".mnemonic");


        JMenu menu = new JMenu(title);

        if( iBundle.hasKey(name + ".mnemonic") ) {
            KeyStroke ksMnemonic = KeyStroke.getKeyStroke( mnemonic.charAt(0) );

            if( ksMnemonic != null ) menu.setMnemonic( (int)ksMnemonic.getKeyChar() );
        }
        return menu;
    }

    /**
     * @param name The name of the resouce
     *
     * @return The menu item
     */

    public static JMenuItem createMenuItem(String name){
        // Get the title, accellerator and mnemonic from the bundle
        String title       = iBundle.getString(name  + ".title");
        String accellerator= iBundle.getString(name  + ".accellerator");
        String mnemonic    = iBundle.getString(name  + ".mnemonic");

        JMenuItem menuItem =  new JMenuItem(title);

        if( iBundle.hasKey(name + ".mnemonic") ) {
            KeyStroke ksMnemonic = KeyStroke.getKeyStroke( mnemonic.charAt(0) );

            if( ksMnemonic != null ) menuItem.setMnemonic( (int)ksMnemonic.getKeyChar() );
        }

        if( iBundle.hasKey(name + ".accellerator") ) {
            KeyStroke ksAccellerator = KeyStroke.getKeyStroke(accellerator);

            if( ksAccellerator != null )  menuItem.setAccelerator(ksAccellerator);
        }

        return menuItem;
    }

    /**
     * @param name The name of the resouce
     * @param action The default actionlistener
     *
     * @return The menu item
     */

    public static JMenuItem createMenuItem(String name, ActionListener action){
        JMenuItem menuItem = createMenuItem(name);

        menuItem.addActionListener(action);

        return menuItem;
    }


    /**
     *
     * @param pMenu
     * @param pName
     */
    public static void setupMenu(JMenu pMenu, String pName){
        // Get the title and mnemonic from the bundle
        String title       = iBundle.getString(pName + ".title");
        String mnemonic    = iBundle.getString(pName + ".mnemonic");

        pMenu.setText(title);

        if( iBundle.hasKey(pName + ".mnemonic") ) {
            KeyStroke ksMnemonic = KeyStroke.getKeyStroke( mnemonic.charAt(0) );

            if( ksMnemonic != null ) pMenu.setMnemonic( (int)ksMnemonic.getKeyChar() );
        }
    }


    /**
     *
     * @param pMenuItem
     * @param pName
     */
    public static void setupMenuItem(JMenuItem pMenuItem, String pName){
        // Get the title, accellerator and mnemonic from the bundle
        String title       = iBundle.getString(pName  + ".title");
        String accellerator= iBundle.getString(pName  + ".accellerator");
        String mnemonic    = iBundle.getString(pName  + ".mnemonic");

        pMenuItem.setText(title);

        if( iBundle.hasKey(pName + ".mnemonic") ) {
            KeyStroke ksMnemonic = KeyStroke.getKeyStroke( mnemonic.charAt(0) );

            if( ksMnemonic != null ) pMenuItem.setMnemonic( (int)ksMnemonic.getKeyChar() );
        }

        if( iBundle.hasKey(pName + ".accellerator") ) {
            KeyStroke ksAccellerator = KeyStroke.getKeyStroke(accellerator);

            if( ksAccellerator != null ) pMenuItem.setAccelerator(ksAccellerator);
        }
    }

}
