package se.swedsoft.bookkeeping.util;

import se.swedsoft.bookkeeping.gui.util.SSBundleString;

import java.util.ResourceBundle;

/**
 * Date: 2006-feb-22
 * Time: 16:40:54
 */
public class SSException extends RuntimeException{


    /**
     *
     *
     * @param pMesssage The message
     */
    public SSException(String pMesssage) {
        super(pMesssage);
    }

    /**
     *
     * @param pBundleString The bundle
     */

    public SSException(SSBundleString pBundleString) {
        super(pBundleString.getString());
    }


    /**
     *
     * @param pBundle The bundle
     * @param pKey Name of the bundle key
     */

    public SSException(ResourceBundle pBundle, String pKey) {
        super(pBundle.getString(pKey));
    }

    /**
     *
     * @param pBundle The bundle
     * @param pKey Name of the bundle key
     * @param pFormat Format args for String.format
     */
    public SSException(ResourceBundle pBundle, String pKey, Object ... pFormat) {
        super(String.format( pBundle.getString(pKey), pFormat));
    }


}
