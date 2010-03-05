package se.swedsoft.bookkeeping.gui.util.components;

import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.help.SSHelpFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Andreas Lago
 * Date: 2006-mar-24
 * Time: 14:41:29
 */
public class SSHelpButton extends SSButton implements ActionListener {

    private Class iHelpClass;

    /**
     *
     */
    public SSHelpButton(Class pHelpClass) {
        super("ICON_HELP24", "toolbar.helpbutton");

        iHelpClass = pHelpClass;

        addActionListener(this);
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        SSHelpFrame.showFrame(SSMainFrame.getInstance(), 980, 720, iHelpClass);
    }


    /**
     *
      * @param iToolBar
     * @param pHelpClass
     */
    public static void addButton(JToolBar iToolBar, Class pHelpClass){
        iToolBar.addSeparator();
        iToolBar.add( new SSHelpButton(pHelpClass) );
    }

}
