package se.swedsoft.bookkeeping.gui.util.filechooser.util;

import se.swedsoft.bookkeeping.gui.util.SSBundle;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ResourceBundle;

/**
 * Date: 2006-feb-13
 * Time: 14:48:14
 */
public class SSFilterALL extends FileFilter {

    public static ResourceBundle bundle = SSBundle.getBundle();

    /**
     * Whether the given file is accepted by this filter.
     */
    @Override
    public boolean accept(File f) {
        return true;    }

    /**
     * @return The description of this filter
     */
    @Override
    public String getDescription() {
        return SSFilterALL.bundle.getString("filechooser.all.filter");
    }

}
