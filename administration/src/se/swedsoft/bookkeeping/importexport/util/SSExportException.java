package se.swedsoft.bookkeeping.importexport.util;

import se.swedsoft.bookkeeping.gui.util.SSBundleString;

import java.util.ResourceBundle;

/**
 * Date: 2006-feb-14
 * Time: 10:50:49
 */
public class SSExportException extends RuntimeException{


    /**
     *
     *
     * @param pMesssage The message
     */
    public SSExportException(String pMesssage) {
        super(pMesssage);
    }

    /**
     *
     * @param pBundleString The bundle
     */

    public SSExportException(SSBundleString pBundleString) {
        super(pBundleString.getString());
    }


    /**
     *
     * @param pBundle The bundle
     * @param pKey Name of the bundle key
     */

    public SSExportException(ResourceBundle pBundle, String pKey) {
        super(pBundle.getString(pKey));
    }

    /**
     *
     * @param pBundle The bundle
     * @param pKey Name of the bundle key
     * @param pFormat Format args for String.format
     */
    public SSExportException(ResourceBundle pBundle, String pKey, Object ... pFormat) {
        super(String.format( pBundle.getString(pKey), pFormat));
    }



}
