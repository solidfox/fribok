package se.swedsoft.bookkeeping.gui.util.dialogs;


import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;

import javax.swing.*;


/**
 * User: Fredrik Stigsson
 * Date: 2006-feb-01
 * Time: 14:51:39
 */
public class SSErrorDialog {

    /**
     *  Opens a error dialog and reads the tile and message from the bundle
     *
     *  Message = bundleName.message
     *  Tle     = bundleName.title
     *
     * @param iFrame
     * @param bundleName
     */
    public SSErrorDialog(JFrame iFrame, String bundleName) {
        String message = SSBundle.getBundle().getString(bundleName + ".message");
        String title = SSBundle.getBundle().getString(bundleName + ".title");

        openDialog(iFrame, title, message);
    }

    /**
     *  Opens a query dialog and reads the tile and message from the bundle
     *
     *  Message = bundleName.message
     *  Tle     = bundleName.title
     *
     * @param iFrame
     * @param pBundleName
     * @param pMessageFormat
     */
    public SSErrorDialog(JFrame iFrame, String pBundleName, Object... pMessageFormat) {
        String title = SSBundle.getBundle().getString(pBundleName + ".title");
        String message = String.format(
                SSBundle.getBundle().getString(pBundleName + ".message"), pMessageFormat);

        openDialog(iFrame, title, message);
    }

    /**
     *
     * @param iFrame
     * @param iTitle
     * @param iMessage
     */
    private void openDialog(JFrame iFrame, String iTitle, String iMessage) {
        Icon iIcon = SSIcon.getIcon("ICON_DIALOG_ERROR");

        // Manually construct an warning popup
        JOptionPane optionPane = new JOptionPane(iMessage, JOptionPane.ERROR_MESSAGE,
                JOptionPane.PLAIN_MESSAGE, iIcon);

        optionPane.setValue(JOptionPane.DEFAULT_OPTION);

        // Construct a message internal frame popup
        SSDialog dialog = new SSDialog(iFrame, iTitle);

        dialog.setOptionPane(optionPane);
        dialog.pack();
        dialog.setLocationRelativeTo(iFrame);
        dialog.setVisible();
    }

    /**
     *
     * @param iFrame
     * @param iTitle
     * @param iMessage
     */
    public static void showDialog(JFrame iFrame, String iTitle, String iMessage) {
        Icon iIcon = SSIcon.getIcon("ICON_DIALOG_ERROR");

        // Manually construct an warning popup
        JOptionPane optionPane = new JOptionPane(iMessage, JOptionPane.ERROR_MESSAGE,
                JOptionPane.PLAIN_MESSAGE, iIcon);

        optionPane.setValue(JOptionPane.DEFAULT_OPTION);

        // Construct a message internal frame popup
        SSDialog dialog = new SSDialog(iFrame, iTitle);

        dialog.setOptionPane(optionPane);
        dialog.pack();
        dialog.setLocationRelativeTo(iFrame);
        dialog.setVisible();
    }
}
