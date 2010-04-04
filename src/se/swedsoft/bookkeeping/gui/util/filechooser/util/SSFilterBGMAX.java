package se.swedsoft.bookkeeping.gui.util.filechooser.util;

import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.util.ResourceBundle;

/**
 * Date: 2006-feb-13
 * Time: 14:48:14
 */
public class SSFilterBGMAX extends SSFileFilter {

    public static ResourceBundle bundle = SSBundle.getBundle();

    public SSFilterBGMAX(){
        addExtension("out");
        addExtension("ut");
    }
    /**
     * @return The description of this filter
     */
    @Override
    public String getDescription() {
        return SSFilterBGMAX.bundle.getString("filechooser.bgmax.filter");
    }



}
