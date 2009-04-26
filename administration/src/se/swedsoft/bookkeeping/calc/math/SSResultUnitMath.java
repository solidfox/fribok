package se.swedsoft.bookkeeping.calc.math;

import se.swedsoft.bookkeeping.data.SSNewResultUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2007-jan-17
 * Time: 10:54:27
 */
public class SSResultUnitMath {
    /**
     * Returns one resultunit for the current company.
     *
     * @return The resultunit or null
     */
    public static SSNewResultUnit getResultUnit(String pNumber) {

        List<SSNewResultUnit> iResultUnits = SSDB.getInstance().getResultUnits();
        for(SSNewResultUnit iResultUnit: iResultUnits){
            if(iResultUnit.getNumber().equals(pNumber)){
                return iResultUnit;
            }
        }
        return null;
    }
}
