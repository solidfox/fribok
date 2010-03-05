package se.swedsoft.bookkeeping.importexport.util;

import se.swedsoft.bookkeeping.gui.util.SSBundleString;

import java.util.ResourceBundle;

/**
 * Date: 2006-feb-14
 * Time: 10:51:37
 */
public class SSImportException  extends RuntimeException{


    /**
     *
     *
     * @param pMesssage The message
     */
    public SSImportException(String pMesssage) {
        super(pMesssage);
    }



    /**
     *
     * @param pMesssage The message
     * @param pFormat Format args for String.format
     */
    public SSImportException( String pMesssage, Object ... pFormat) {
        super(String.format( pMesssage, pFormat));
    }

    /**
     *
     * @param pBundleString The bundle
     */

    public SSImportException(SSBundleString pBundleString) {
        super(pBundleString.getString());
    }

    /**
     *
     * @param pBundle The bundle
     * @param pKey Name of the bundle key
     */

    public SSImportException(ResourceBundle pBundle, String pKey) {
        super(pBundle.getString(pKey));
    }

    /**
     *
     * @param pBundle The bundle
     * @param pKey Name of the bundle key
     * @param pFormat Format args for String.format
     */
    public SSImportException(ResourceBundle pBundle, String pKey, Object ... pFormat) {
        super(String.format( pBundle.getString(pKey), pFormat));
    }


}
