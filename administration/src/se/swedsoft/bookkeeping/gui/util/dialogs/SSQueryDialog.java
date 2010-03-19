package se.swedsoft.bookkeeping.gui.util.dialogs;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;

import javax.swing.*;
import java.util.ResourceBundle;

/**
 * User: Fredrik Stigsson
 * Date: 2006-jan-27
 * Time: 09:24:10
 */
public class SSQueryDialog {

    private JOptionPane iOptionPane;

    /**
     *  Opens a query dialog and reads the tile and message from the bundle<br>
     *  <br>
     *  Message = bundleName.message<br>
     *  Tle     = bundleName.title<br>
     *
     * @param iFrame
     * @param pBundleName
     */
    public SSQueryDialog(JFrame iFrame, String pBundleName){
        this(iFrame, JOptionPane.YES_NO_OPTION, pBundleName );
    }


    /**
     *  Opens a query dialog and reads the tile and message from the bundle<br>
     *  <br>
     *  Message = bundleName.message<br>
     *  Tle     = bundleName.title<br>
     *
     * @param iFrame
     * @param iBundle
     * @param pBundleName
     * @param pMessageFormat
     */
    public SSQueryDialog(JFrame iFrame, ResourceBundle iBundle, String pBundleName, Object ... pMessageFormat){
        String title   =               iBundle.getString(pBundleName + ".title");
        String message = String.format(iBundle.getString(pBundleName + ".message"), pMessageFormat);

        openDialog(iFrame, JOptionPane.YES_NO_OPTION, title, message);
    }

    /**
     *
     * @param iFrame
     * @param pOptionType
     * @param pBundleName
     */
    public SSQueryDialog(JFrame iFrame, int pOptionType, String pBundleName){
        String title   = SSBundle.getBundle().getString(pBundleName + ".title");
        String message = SSBundle.getBundle().getString(pBundleName + ".message");

        openDialog(iFrame, pOptionType, title, message);
    }


    /**
     *
     * @param iFrame
     * @param pOptionType
     * @param iTitle
     * @param iMessage
     */
    public SSQueryDialog(JFrame iFrame, int pOptionType, String iTitle, String iMessage){
        openDialog(iFrame, pOptionType, iTitle, iMessage);
    }


    /**
     *
     * @param iFrame
     * @param pOptionType
     * @param pTitle
     * @param pMessage
     * @return
     */
    private int openDialog(JFrame iFrame, int pOptionType, String pTitle, String pMessage){
        Icon iIcon =   SSIcon.getIcon("ICON_DIALOG_INFORMATION");

        // Manually construct an warning popup
        iOptionPane = new JOptionPane(pMessage, JOptionPane.QUESTION_MESSAGE, pOptionType , iIcon );
        iOptionPane.setValue( JOptionPane.DEFAULT_OPTION );

        // Construct a message internal frame popup
        SSDialog dialog = new SSDialog(iFrame, pTitle);
        dialog.setOptionPane(iOptionPane);
        dialog.pack();
        dialog.setLocationRelativeTo(iFrame);
        dialog.setVisible();

        return (Integer)iOptionPane.getValue();
    }

    /**
     *
     * @return The responce from the JOptionPane
     */
    public int getResponce() {
        return (iOptionPane == null) ?  JOptionPane.DEFAULT_OPTION : (Integer)iOptionPane.getValue();
    }


    /**
     * 
     * @param iFrame
     * @param pBundleName
     * @return
     */
    public static int showDialog(JFrame iFrame, String pBundleName){
        SSQueryDialog iDialog = new SSQueryDialog(iFrame, pBundleName);

        return iDialog.getResponce();
    }



    /**
     *
     * @param iFrame
     * @param iBundle
     * @param pBundleName
     * @param pMessageFormat
     * @return
     */
    public static int showDialog(JFrame iFrame,  ResourceBundle iBundle,String pBundleName, Object ... pMessageFormat){
        SSQueryDialog iDialog = new SSQueryDialog(iFrame, iBundle, pBundleName, pMessageFormat);

        return iDialog.getResponce();
    }

    /**
     *
     * @param iFrame
     * @param pOptionType
     * @param pBundleName
     * @return
     */
    public static int showDialog(JFrame iFrame, int pOptionType, String pBundleName){
        SSQueryDialog iDialog = new SSQueryDialog(iFrame, pOptionType, pBundleName);

        return iDialog.getResponce();
    }

    /**
     * 
     * @param iFrame
     * @param pOptionType
     * @param pTitle
     * @param iMessage
     * @return
     */
    public static int showDialog(JFrame iFrame, int pOptionType, String pTitle, String iMessage){
        SSQueryDialog iDialog = new SSQueryDialog(iFrame, pOptionType, pTitle, iMessage);

        return iDialog.getResponce();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog");
        sb.append("{iOptionPane=").append(iOptionPane);
        sb.append('}');
        return sb.toString();
    }
}


