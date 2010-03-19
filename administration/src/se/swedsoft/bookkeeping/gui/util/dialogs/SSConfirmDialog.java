package se.swedsoft.bookkeeping.gui.util.dialogs;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;

import javax.swing.*;

/**
 * Date: 2006-feb-10
 * Time: 09:14:42
 */
public class SSConfirmDialog {

    private String iTitle;
    private String iMessage;

    /**
     *  Opens a error dialog and reads the tile and message from the bundle<br>
     *  <br>
     *  Message = bundleName.message<br>
     *  Tle     = bundleName.title<br>
     *
     * @param pBundleName
     */
    public SSConfirmDialog(String pBundleName){
        iMessage = SSBundle.getBundle().getString(pBundleName + ".message");
        iTitle   = SSBundle.getBundle().getString(pBundleName + ".title");
    }

    /**
     *  Opens a query dialog and reads the tile and message from the bundle<br>
     *  <br>
     *  Message = bundleName.message<br>
     *  Tle     = bundleName.title<br>
     *
     * @param pBundleName
     * @param pMessageFormat
     */
    public SSConfirmDialog(String pBundleName, Object ... pMessageFormat){
        iTitle   =               SSBundle.getBundle().getString(pBundleName + ".title");
        iMessage = String.format(SSBundle.getBundle().getString(pBundleName + ".message"), pMessageFormat);
    }




    /**
     *
     * @param iFrame
     * @return
     */
    public int openDialog(JFrame iFrame){
        Icon iIcon = SSIcon.getIcon("ICON_DIALOG_INFORMATION");

        // Manually construct an warning popup
        JOptionPane optionPane = new JOptionPane(iMessage, JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION, iIcon);
        optionPane.setValue( JOptionPane.DEFAULT_OPTION );

        // Construct a message internal frame popup
        SSDialog dialog = new SSDialog(iFrame, iTitle);
        dialog.setOptionPane(optionPane);
        dialog.pack();

        return dialog.showDialog(iFrame);
    }

    /**
     *
     * @param iDialog
     * @return
     */
    public int openDialog(JDialog iDialog){
        Icon iIcon = SSIcon.getIcon("ICON_DIALOG_INFORMATION");

        // Manually construct an warning popup
        JOptionPane optionPane = new JOptionPane(iMessage, JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION, iIcon);
        optionPane.setValue( JOptionPane.DEFAULT_OPTION );

        // Construct a message internal frame popup
        SSDialog dialog = new SSDialog(iDialog, iTitle);
        dialog.setOptionPane(optionPane);
        dialog.pack();

        return dialog.showDialog(iDialog);
    }

    public static int showDialog(JFrame iFrame, String iTitle, String iMessage){
        Icon iIcon = SSIcon.getIcon("ICON_DIALOG_INFORMATION");

        // Manually construct an warning popup
        JOptionPane optionPane = new JOptionPane(iMessage, JOptionPane.INFORMATION_MESSAGE, JOptionPane.YES_NO_OPTION, iIcon);
        optionPane.setValue( JOptionPane.DEFAULT_OPTION );

        // Construct a message internal frame popup
        SSDialog dialog = new SSDialog(iFrame, iTitle);
        dialog.setOptionPane(optionPane);
        dialog.pack();

        return dialog.showDialog(iFrame);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.util.dialogs.SSConfirmDialog");
        sb.append("{iMessage='").append(iMessage).append('\'');
        sb.append(", iTitle='").append(iTitle).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
