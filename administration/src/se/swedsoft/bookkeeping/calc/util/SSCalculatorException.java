package se.swedsoft.bookkeeping.calc.util;

import java.util.ResourceBundle;

/**
 * Date: 2006-feb-15
 * Time: 17:14:33
 */
public class SSCalculatorException  extends RuntimeException{

    private String iMesssage;

    /**
     *
     *
     * @param pMesssage The message
     */
    public SSCalculatorException(String pMesssage) {
        super();

        iMesssage = pMesssage;
    }

    /**
     *
     * @param pBundle The bundle
     * @param pKey Name of the bundle key
     */

    public SSCalculatorException(ResourceBundle pBundle, String pKey) {
        super();

        iMesssage = pBundle.getString(pKey);
    }

    /**
     *
     * @param pBundle The bundle
     * @param pKey Name of the bundle key
     * @param pFormat Format args for String.format
     */
    public SSCalculatorException(ResourceBundle pBundle, String pKey, Object ... pFormat) {
        super();

        iMesssage = String.format( pBundle.getString(pKey), pFormat);
    }

    /**
     *
     * @return a string representation of this throwable.
     */
    public String toString() {
        return iMesssage;
    }

}
