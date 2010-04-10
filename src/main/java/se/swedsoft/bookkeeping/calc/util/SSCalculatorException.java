package se.swedsoft.bookkeeping.calc.util;


import java.util.ResourceBundle;


/**
 * Date: 2006-feb-15
 * Time: 17:14:33
 */
public class SSCalculatorException extends RuntimeException {

    private final String iMesssage;

    /**
     *
     *
     * @param pMesssage The message
     */
    public SSCalculatorException(String pMesssage) {

        iMesssage = pMesssage;
    }

    /**
     *
     * @param pBundle The bundle
     * @param pKey Name of the bundle key
     */

    public SSCalculatorException(ResourceBundle pBundle, String pKey) {

        iMesssage = pBundle.getString(pKey);
    }

    /**
     *
     * @param pBundle The bundle
     * @param pKey Name of the bundle key
     * @param pFormat Format args for String.format
     */
    public SSCalculatorException(ResourceBundle pBundle, String pKey, Object... pFormat) {

        iMesssage = String.format(pBundle.getString(pKey), pFormat);
    }

    /**
     *
     * @return a string representation of this throwable.
     */
    public String toString() {
        return iMesssage;
    }

}
