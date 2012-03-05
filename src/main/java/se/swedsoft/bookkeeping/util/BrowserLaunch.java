/*
 * Copyright © 2010 Stefan Kangas <skangas@skangas.se>
 *
 * This file is part of Fribok.
 *
 * Fribok is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * Fribok is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Fribok.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.swedsoft.bookkeeping.util;


import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Class to launch web browsers.
 * Should support GNU/Linux, Mac OS X, other Unix-variants and Windows
 * @author Stefan Kangas
 * @version $Id$
 */
public class BrowserLaunch {

    private static final String osname = System.getProperty("os.name").toLowerCase();
    private static final boolean MAC_OS_X = osname.startsWith("mac os x");
    private static final boolean WINDOWS = osname.startsWith("windows");

    private BrowserLaunch() {// private constructor to enforce non-instantiability
    }

    /**
     * Launch browser
     * @param url
     */
    public static void openURL(URL url) {
        openURL(url.toString());
    }

    /**
     * Launch browser
     * @param url
     */
    public static void openURL(String url) {
        String error = null;

        try {
            if (WINDOWS) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else { // assume Unix
                // hone BROWSER variable
                List<String> browsers = new ArrayList<String>(10);
                String env = System.getenv("BROWSER");

                if (env != null) {
                    browsers.addAll(Arrays.asList(env.split(":")));
                }

                // add browsers to list
                if (MAC_OS_X) {
                    browsers.addAll(
                            Arrays.asList(
                                    new String[] {
                        "firefox", "chrome", "safari",
                        "mozilla", "opera", "netscape" }));
                } else {
                    browsers.addAll(
                            Arrays.asList(
                                    new String[] {
                        "firefox", "epiphany", "konqueror",
                        "mozilla", "opera", "netscape" }));
                }
                // find browser in path
                String browser = null;

                for (String br : browsers) {
                    Process process = Runtime.getRuntime().exec(
                            new String[] { "which", br});

                    try {
                        if (process.waitFor() == 0) {
                            browser = br;
                        }
                    } catch (InterruptedException ignored) {}
                }

                // launch browser
                if (browser != null) {
                    String[] commandLine = { browser, url};
                    Process process = Runtime.getRuntime().exec(commandLine);
                } else {
                    error = "Could not find web browser";
                }
            }
        } catch (IOException e) {
            error = e.getMessage();
        }
        if (error != null) {
            JOptionPane.showMessageDialog(null,
                    "Error attempting to launch web browser:\n" + error);
        }
    }
}
