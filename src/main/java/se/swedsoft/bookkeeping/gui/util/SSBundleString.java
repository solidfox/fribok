package se.swedsoft.bookkeeping.gui.util;


import java.util.ResourceBundle;


/**
 * Date: 2006-feb-15
 * Time: 09:19:09
 */
public class SSBundleString {

    private static ResourceBundle cBundle = SSBundle.getBundle();

    private String iString;

    /**
     *
     * @param pBundleName
     */
    public SSBundleString(String pBundleName) {
        this(cBundle, pBundleName);
    }

    /**
     *
     * @param pBundleName
     * @param pFormat
     */
    public SSBundleString(String pBundleName, Object... pFormat) {
        this(cBundle, pBundleName, pFormat);
    }

    /**
     *
     * @param pBundle
     * @param pBundleName
     */
    public SSBundleString(ResourceBundle pBundle, String pBundleName) {
        iString = pBundle.getString(pBundleName);
    }

    /**
     *
     * @param pBundle
     * @param pBundleName
     * @param pFormat
     */
    public SSBundleString(ResourceBundle pBundle, String pBundleName, Object... pFormat) {
        iString = String.format(pBundle.getString(pBundleName), pFormat);
    }

    /**
     *
     * @return
     */
    public String getString() {
        return iString;
    }

    public String toString() {
        return iString;
    }

    /**
     *
     * @param pBundleName
     * @return
     */
    public static SSBundleString getString(String pBundleName) {
        return new SSBundleString(pBundleName);
    }

    /**
     *
     * @param pBundleName
     * @param pFromat
     * @return
     */
    public static SSBundleString getString(String pBundleName, Object... pFromat) {
        return new SSBundleString(pBundleName, pFromat);
    }

}
