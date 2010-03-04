package se.swedsoft.bookkeeping.gui.util.filechooser.util;

import se.swedsoft.bookkeeping.gui.util.SSBundle;

import javax.swing.filechooser.FileFilter;
import java.util.ResourceBundle;
import java.io.File;

/**
 * Date: 2006-feb-13
 * Time: 14:48:14
 */
public class SSFilterSIE extends SSFileFilter {

    public static ResourceBundle bundle = SSBundle.getBundle();

    public SSFilterSIE(){
        super();
        addExtention("sie");
        addExtention("si");
        addExtention("se");
    }
    /**
     * @return The description of this filter
     */
    @Override
    public String getDescription() {
        return bundle.getString("filechooser.sie.filter");
    }

}
