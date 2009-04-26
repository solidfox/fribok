package se.swedsoft.bookkeeping.gui.util.filechooser.util;

import se.swedsoft.bookkeeping.gui.util.SSBundle;

import javax.swing.filechooser.FileFilter;
import java.util.ResourceBundle;
import java.io.File;

/**
 * Date: 2006-feb-13
 * Time: 13:58:06
 */
public class SSDefaultFilter extends FileFilter {

    public static ResourceBundle bundle = SSBundle.getBundle();


    /**
     * @param f
     * @return Accept all files.
     */
    public boolean accept(File f) {
        return true;

    }

    /**
     * @return The description of this filter
     */
    public String getDescription() {
        return bundle.getString("filechooser.default.filter");
    }

}


