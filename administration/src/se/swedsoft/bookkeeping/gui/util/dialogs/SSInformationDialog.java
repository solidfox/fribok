package se.swedsoft.bookkeeping.gui.util.dialogs;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;

import javax.swing.*;

/**
 * Date: 2006-feb-10
 * Time: 09:14:42
 */
public class SSInformationDialog {

    /**
     *  Opens a error dialog and reads the tile and message from the bundle<br>
     *  <br>
     *  Message = bundleName.message<br>
     *  Tle     = bundleName.title<br>
     *
     * @param iFrame
     * @param pBundleName
     */
    public SSInformationDialog(JFrame iFrame, String pBundleName){
        String message = SSBundle.getBundle().getString(pBundleName + ".message");
        String title   = SSBundle.getBundle().getString(pBundleName + ".title");

        openDialog(iFrame, title, message);
    }

    /**
     *  Opens a query dialog and reads the tile and message from the bundle<br>
     *  <br>
     *  Message = bundleName.message<br>
     *  Tle     = bundleName.title<br>
     *
     * @param iFrame
     * @param pBundleName
     * @param pMessageFormat
     */
    public SSInformationDialog(JFrame iFrame, String pBundleName, Object ... pMessageFormat){
        String title   =               SSBundle.getBundle().getString(pBundleName + ".title");
        String message = String.format(SSBundle.getBundle().getString(pBundleName + ".message"), pMessageFormat);

        openDialog(iFrame, title, message);
    }

    /**
     *  Opens a error dialog and reads the tile and message from the bundle<br>
     *  <br>
     *  Message = bundleName.message<br>
     *  Tle     = bundleName.title<br>
     *
     * @param iFrame
     * @param pBundleName
     */
    public SSInformationDialog(JDialog iFrame, String pBundleName){
        String message = SSBundle.getBundle().getString(pBundleName + ".message");
        String title   = SSBundle.getBundle().getString(pBundleName + ".title");

        openDialog(iFrame, title, message);
    }

    /**
     *  Opens a query dialog and reads the tile and message from the bundle<br>
     *  <br>
     *  Message = bundleName.message<br>
     *  Tle     = bundleName.title<br>
     *
     * @param iFrame
     * @param pBundleName
     * @param pMessageFormat
     */
    public SSInformationDialog(JDialog iFrame, String pBundleName, Object ... pMessageFormat){
        String title   =               SSBundle.getBundle().getString(pBundleName + ".title");
        String message = String.format(SSBundle.getBundle().getString(pBundleName + ".message"), pMessageFormat);

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
        JOptionPane optionPane = new JOptionPane(iMessage, JOptionPane.INFORMATION_MESSAGE, JOptionPane.PLAIN_MESSAGE, iIcon);
        optionPane.setValue( JOptionPane.DEFAULT_OPTION );

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
    private void openDialog(JDialog iFrame, String iTitle, String iMessage){
        Icon iIcon = SSIcon.getIcon("ICON_DIALOG_INFORMATION");

        // Manually construct an warning popup
        JOptionPane optionPane = new JOptionPane(iMessage, JOptionPane.INFORMATION_MESSAGE, JOptionPane.PLAIN_MESSAGE, iIcon);
        optionPane.setValue( JOptionPane.DEFAULT_OPTION );

        // Construct a message internal frame popup
        SSDialog dialog = new SSDialog(iFrame, iTitle);
        dialog.setOptionPane(optionPane);
        dialog.pack();
        dialog.setLocationRelativeTo(iFrame);
        dialog.setVisible();
    }



     /**
     *  Opens a error dialog and reads the tile and message from the bundle<br>
     *  <br>
     *  Message = bundleName.message<br>
     *  Tle     = bundleName.title<br>
     *
     * @param iFrame
     * @param pBundleName
     */
    public static void showDialog(JFrame iFrame, String pBundleName){
        new SSInformationDialog(iFrame, pBundleName);
    }

    /**
     *  Opens a query dialog and reads the tile and message from the bundle<br>
     *  <br>
     *  Message = bundleName.message<br>
     *  Tle     = bundleName.title<br>
     *
     * @param iFrame
     * @param pBundleName
     * @param pMessageFormat
     */
    public static void showDialog(JFrame iFrame, String pBundleName, Object ... pMessageFormat){
        new SSInformationDialog(iFrame, pBundleName, pMessageFormat);
    }

}


