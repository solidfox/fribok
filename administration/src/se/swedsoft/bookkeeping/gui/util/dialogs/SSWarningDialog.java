package se.swedsoft.bookkeeping.gui.util.dialogs;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;

import javax.swing.*;

/**
 * User: Fredrik Stigsson
 * Date: 2006-jan-26
 * Time: 09:09:26
 */
public class SSWarningDialog {

    /**
     *  Opens a warning dialog and reads the tile and message from the bundle<br>
     *  <br>
     *  Message = bundleName.message<br>
     *  Tle     = bundleName.title<br>
     *
     * @param iFrame
     * @param bundleName
     */
    public SSWarningDialog(JFrame iFrame, String bundleName){
        String iMessage = SSBundle.getBundle().getString(bundleName + ".message");
        String iTitle   = SSBundle.getBundle().getString(bundleName + ".title");

        openDialog(iFrame, iTitle, iMessage);

    }
    /**
     *  Opens a warning dialog and reads the tile and message from the bundle<br>
     *  <br>
     *  Message = bundleName.message<br>
     *  Tle     = bundleName.title<br>
     *
     * @param iFrame
     * @param pBundleName
     */
    public SSWarningDialog(JFrame iFrame, String pBundleName, String ... pMessageFormat){
        String title   =               SSBundle.getBundle().getString(pBundleName + ".title");
        String message = String.format(SSBundle.getBundle().getString(pBundleName + ".message"), (Object [])pMessageFormat);

        openDialog(iFrame, title, message);
    }

    
    /**
     *
     * @param iFrame
     * @param iTitle
     * @param iMessage
     */
    private void openDialog(JFrame iFrame, String iTitle, String iMessage){
        Icon iIcon = SSIcon.getIcon("ICON_DIALOG_INFORMATION");

        // Manually construct an warning popup
        JOptionPane optionPane = new JOptionPane(iMessage, JOptionPane.WARNING_MESSAGE, JOptionPane.PLAIN_MESSAGE, iIcon);
        optionPane.setValue( JOptionPane.DEFAULT_OPTION );

        // Construct a message internal frame popup
        SSDialog dialog = new SSDialog(iFrame, iTitle);
        dialog.setOptionPane(optionPane);
        dialog.pack();
        dialog.setLocationRelativeTo(iFrame);
        dialog.setVisible();
    }


}
