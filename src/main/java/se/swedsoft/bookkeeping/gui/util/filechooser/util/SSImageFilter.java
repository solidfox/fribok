package se.swedsoft.bookkeeping.gui.util.filechooser.util;


import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.util.ResourceBundle;


/**
 * Date: 2006-feb-10
 * Time: 15:07:37
 */
public class SSImageFilter extends SSFileFilter {

    public static ResourceBundle bundle = SSBundle.getBundle();

    public static final String jpeg = "jpeg";
    public static final String jpg = "jpg";
    public static final String gif = "gif";
    public static final String png = "png";

    public SSImageFilter() {
        addExtension(gif);
        addExtension(jpeg);
        addExtension(jpg);
        addExtension(png);
    }

    /**
     * @return The description of this filter
     */
    @Override
    public String getDescription() {
        return bundle.getString("filechooser.image.filter");
    }

}

