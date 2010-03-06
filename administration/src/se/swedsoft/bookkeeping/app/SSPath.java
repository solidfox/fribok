/*
 * Copyright © 2010 Stefan Kangas <skangas@skangas.se>
 * 
 * This file is part of JFS Accounting.
 *
 * JFS Accounting is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * JFS Accounting is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * JFS Accounting.  If not, see <http://www.gnu.org/licenses/>.
 */ 
package se.swedsoft.bookkeeping.app;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;
import org.freedesktop.xdg.BaseDirs;

/**
 * Return paths to directories depending on the running OS.  This class makes NO
 * GUARANTEES at all that these directories are readable or writable by the
 * current user; this class is nothing more than a set of File objects available
 * for use in different parts of the program.
 *
 * @author Stefan Kangas
 * @version $Id$
 */
public enum SSPath {
    /** The application base directory */
    APP_BASE,
    /** The application data directory */
    APP_DATA,
    /** The application icon directory */
    APP_ICONS,
    /** The application image directory */
    APP_IMAGES,
    /** The user report directory */
    USER_REPORTS,
    /** The user configuration directory */
    USER_CONF,
    /** The user data directory */
    USER_DATA;
    
    private static final String JFS_SUBDIR = "jfsaccounting";
    private static final Map<SSPath, File> path = new EnumMap<SSPath, File>(SSPath.class);

    static {
        File base = new File(new File("").getAbsolutePath());
        path.put(APP_BASE,   base);
        path.put(APP_DATA,   new File(base, "data"));
        path.put(APP_IMAGES, new File(base, "graphic"));
        path.put(APP_ICONS,  new File(base, "graphic/icons"));

        String os = System.getProperty("os.name");
        if (os.startsWith("Mac OS") || os.startsWith("Windows")) {
            // TODO: Decide locations for MacOSX and Windows. This should
            // probably be done by someone with access to a MacOSX/Windows box.
            path.put(USER_DATA, base);
            path.put(USER_CONF, base);
        }
        else { // assume freedesktop.org compliance
            BaseDirs iBaseDirs = new BaseDirs();
            String userData = iBaseDirs.getUserPath(BaseDirs.Resource.DATA);
            String userConf = iBaseDirs.getUserPath(BaseDirs.Resource.CONFIG);
            path.put(USER_DATA, new File(userData, JFS_SUBDIR));
            path.put(USER_CONF, new File(userConf, JFS_SUBDIR));
        }
        path.put(USER_REPORTS, new File(path.get(USER_DATA), "report"));
    }

    /**
     * Return path.
     *
     * @param name a path you want
     * @return the path
     */
    public static File get(SSPath name) {
        return path.get(name);
    }
}
