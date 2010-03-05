package se.swedsoft.bookkeeping.util;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

/**
 * User: Andreas Lago
 * Date: 2006-maj-09
 * Time: 12:03:25
 *  Supports: Mac OS X, GNU/Linux, Unix, Windows XP
 *  Example Usage:
 *     String url = "http://www.centerkey.com/";
 *     SSBrowserLaunch.openURL(url);
 */
public class SSBrowserLaunch {

    private static final String errMsg = "Error attempting to launch web browser";

    /**
     *
     * @param url
     */
    public static void openURL(URL url) {
        openURL( url.toString() );
    }

    /**
     *
     * @param url
     */
    public static void openURL(String url) {
        String osName = System.getProperty("os.name");
        try {
            if (osName.startsWith("Mac OS")) {
                Class fileMgr = Class.forName("com.apple.eio.FileManager");

                Method openURL = fileMgr.getDeclaredMethod("openURL",  String.class);

                openURL.invoke(null, url);
            }
            else if (osName.startsWith("Windows"))
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            else { //assume Unix or Linux

                String iBrowser = getUnixBrowser();

                if (iBrowser != null){
                    Runtime.getRuntime().exec(new String[] {iBrowser, url});
                }  else {
                    throw new Exception("Could not find web browser");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, errMsg + ":\n" + e.getLocalizedMessage());
        }
    }



    private static String getUnixBrowser() throws IOException, InterruptedException{
        String[] iBrowsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };

        for (String iBrowser : iBrowsers) {
            Process iProcess = Runtime.getRuntime().exec(new String[] {"which", iBrowser});

            if (iProcess.waitFor() == 0) return iBrowser;
        }

        return null;
    }


}
