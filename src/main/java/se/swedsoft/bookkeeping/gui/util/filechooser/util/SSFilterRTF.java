package se.swedsoft.bookkeeping.gui.util.filechooser.util;


import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.util.ResourceBundle;


/**
 * Date: 2006-feb-13
 * Time: 14:48:14
 */
public class SSFilterRTF extends SSFileFilter {

    public static ResourceBundle bundle = SSBundle.getBundle();

    public SSFilterRTF() {
        addExtension("rff");
    }

    /**
     * @return The description of this filter
     */
    @Override
    public String getDescription() {
        return bundle.getString("filechooser.rtf.filter");
    }

}
