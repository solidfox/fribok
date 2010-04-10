package se.swedsoft.bookkeeping.importexport.sie.util;


/**
 * Date: 2006-feb-22
 * Time: 12:19:05
 */
public enum SIEType {

    SIE_1  ("1"), SIE_2  ("2"), SIE_3  ("3"), SIE_4I ("4"), SIE_4E ("4"), SIE_NULL(null);

    /**
     *
     */
    private String iValue;

    /**
     *
     * @param pValue
     */
    SIEType(String pValue) {
        iValue = pValue;
    }

    public String toString() {
        return iValue;
    }

    /**
     *
     * @param s
     * @return
     */
    public static SIEType decode(String s) {
        if (s.equals("1")) {
            return SIE_1;
        }
        if (s.equals("2")) {
            return SIE_2;
        }
        if (s.equals("3")) {
            return SIE_3;
        }
        if (s.equals("4")) {
            return SIE_4E;
        }
        return SIE_NULL;
    }

}
