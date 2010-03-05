package se.swedsoft.bookkeeping.util;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;

/**
 * Class to launch browsers.
 *  Supports: Mac OS X, GNU/Linux, Unix, Windows XP
 *  Example Usage:
 *     String url = "http://www.centerkey.com/";
 *     SSBrowserLaunch.openURL(url);
 * @author Andreas Lago
 * @version $Id$
 */
public class SSBrowserLaunch {

    private static final String errMsg = "Error attempting to launch web browser";

    /**
     * Launch browser
     * @param url
     */
    public static void openURL(URL url) {
        openURL( url.toString() );
    }

    /**
     * Launch browser
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
            else { // assume GNU/Linux or other Unix-like OS
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

    private static String getUnixBrowser() throws IOException, InterruptedException {
        ArrayList<String> browsers = new ArrayList<String>();
        // respect the user's preferences
        String env = System.getenv("BROWSER");
        if (env != null)
            for (String b : env.split(":"))
                browsers.add(b);
        // fall back to trying reasonable defaults.  free-as-in-freedom browsers
        // in front please :-)
        browsers.add("firefox");
        browsers.add("epiphany");
        browsers.add("konqueror");
        browsers.add("mozilla");
        browsers.add("conkeror");
        browsers.add("opera");
        browsers.add("netscape");
        for (String browser : browsers) {
            Process process = Runtime.getRuntime().exec(new String[] {"which", browser});
            if (process.waitFor() == 0)
                return browser;
        }   
        return null;
    }
}
