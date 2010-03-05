package se.swedsoft.bookkeeping.gui.about.panel;

import se.swedsoft.bookkeeping.SSVersion;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.util.SSBrowserLaunch;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Date: 2006-mar-14
 * Time: 15:15:51
 */
public class SSAboutPanel {

    private JPanel iPanel;

    private JButton iCloseButton;

    private JEditorPane iEditorPane;

    /**
     *
     */
    public SSAboutPanel() {
        String iText    = SSBundle.getBundle().getString("aboutframe.abouttext");
        String iLicence = "";

        iEditorPane.setBackground( iPanel.getBackground() );

        iText = iText.replace("{TITLE}"  , SSVersion.app_title);
        iText = iText.replace("{VERSION}", SSVersion.app_version);
        iText = iText.replace("{BUILD}"  , SSVersion.app_build);
        iText = iText.replace("{LICENCE}", iLicence);


        iEditorPane.setText(iText);

        iEditorPane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                String iEventName = e.getEventType() == null ? "" : e.getEventType().toString();

                if(iEventName.equals("ACTIVATED")){
                    SSBrowserLaunch.openURL(e.getURL());
                }
                if(iEventName.equals("ENTERED")){
                    iEditorPane.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
                if(iEventName.equals("EXITED")){
                    iEditorPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }

            }
        });
    }

    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }

    /**
     *
     * @param iListener
     */
    public void addCloseButtonListener(ActionListener iListener){
        iCloseButton.addActionListener(iListener);
    }
/*

    // Used to identify the windows platform.
    private static final String WIN_ID = "Windows";
    // The default system browser under windows.
    private static final String WIN_PATH = "rundll32";
    // The flag to display a url.
    private static final String WIN_FLAG = "url.dll,FileProtocolHandler";
    // The default browser under unix.
    private static final String UNIX_PATH = "netscape";
    // The flag to display a url.
    private static final String UNIX_FLAG = "-remote openURL";


     * Display a file in the sy
     * stem browser.  If you want to display a
     * file, you must include the absolute path name.
     *
     * @param url the file's url (the url must start with either "http://" or "file://").

    public static void displayURL(URL url){

        boolean windows = isWindowsPlatform();
        String cmd = null;
        try{
            if (windows){
                // cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
                cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
                Process p = Runtime.getRuntime().exec(cmd);
            }  else  {
                // Under Unix, Netscape has to be running for the "-remote"
                // command to work.  So, we try sending the command and
                // check for an exit value.  If the exit command is 0,
                // it worked, otherwise we need to start the browser.
                // cmd = 'netscape -remote openURL(http://www.javaworld.com)'
                cmd = UNIX_PATH + " " + UNIX_FLAG + "(" + url + ")";
                Process p = Runtime.getRuntime().exec(cmd);
                try
                {
                    // wait for exit code -- if it's 0, command worked,
                    // otherwise we need to start the browser up.
                    int exitCode = p.waitFor();
                    if (exitCode != 0) {
                        // Command failed, start up the browser
                        // cmd = 'netscape http://www.javaworld.com'
                        cmd = UNIX_PATH + " "  + url;

                        Runtime.getRuntime().exec(cmd);
                    }
                }
                catch(InterruptedException x)  {
                    System.err.println("Error bringing up browser, cmd='" + cmd + "'");
                    System.err.println("Caught: " + x);
                }
            }
        }
        catch(IOException x) {
            // couldn't exec browser
            System.err.println("Could not invoke browser, command=" + cmd);
            System.err.println("Caught: " + x);
        }
    }


     * Try to determine whether this application is running under Windows
     * or some other platform by examing the "os.name" property.
     *
     * @return true if this application is running under a Windows OS

    public static boolean isWindowsPlatform()
    {
        String os = System.getProperty("os.name");
        return os != null && os.startsWith(WIN_ID);


    }    */

}
