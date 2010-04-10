package se.swedsoft.bookkeeping.calc.util;


import java.io.Serializable;
import java.util.HashMap;


/**
 * User: Andreas Lago
 * Date: 2006-jun-05
 * Time: 11:39:37
 */
public class SSAutoIncrement implements Serializable {

    private static final long serialVersionUID = 8382640246746989054L;

    private HashMap<String, Integer> iNumbers;

    /**
     *
     */
    public SSAutoIncrement() {
        iNumbers = new HashMap<String, Integer>();
    }

    /**
     *
     * @param iKey
     */
    public void doAutoIncrement(String iKey) {
        Integer iNumber = iNumbers.get(iKey);

        if (iNumber == null) {
            iNumbers.put(iKey, 1);
        } else {
            iNumbers.put(iKey, iNumber + 1);
        }

    }

    /**
     *
     * @param iKey
     * @return
     */
    public int getNumber(String iKey) {
        Integer iNumber = iNumbers.get(iKey);

        return iNumber == null ? 0 : iNumber;
    }

    /**
     *
     * @param iKey
     * @param iNumber
     */
    public void setNumber(String iKey, int iNumber) {
        iNumbers.put(iKey, iNumber);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String iName : iNumbers.keySet()) {
            sb.append(iName).append(' ').append(iNumbers.get(iName)).append('\n');
        }
        return sb.toString();
    }

}
