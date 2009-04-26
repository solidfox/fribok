package se.swedsoft.bookkeeping.calc.math;

import se.swedsoft.bookkeeping.data.*;

import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

/**
 * User: Andreas Lago
 * Date: 2006-sep-22
 * Time: 16:34:16
 */
public class SSIndeliveryMath {

    /**
     *
     * @param iInventory
     * @param pTo
     * @return
     */
    public static boolean inPeriod(SSIndelivery iInventory, Date pTo) {
        Date iDate = iInventory.getDate();
        Date iTo   = SSDateMath.ceil (pTo);

        return  (iDate.getTime() <= iTo.getTime());

    }

    /**
     *
     * @param iInventory
     * @param pFrom
     * @param pTo
     * @return
     */
    public static boolean inPeriod( SSIndelivery iInventory, Date pFrom, Date pTo){
        Date iDate = iInventory.getDate();
        Date iFrom = SSDateMath.floor(pFrom);
        Date iTo   = SSDateMath.ceil (pTo);

        return (iFrom.getTime() <= iDate.getTime()) && (iDate.getTime() <= iTo.getTime());
    }

    /**
     *
     * @param iIndelivery
     * @return
     */
    public static Integer getTotalCount(SSIndelivery iIndelivery) {
        Integer iCount = 0;
        for (SSIndeliveryRow iRow : iIndelivery.getRows()) {
            if( iRow.getChange() != null){
                iCount = iCount + iRow.getChange();
            }


        }
        return iCount;
    }

    /**
     *
     * @param iIndelivery
     * @param iProduct
     * @return
     */
    public static boolean hasProduct(SSIndelivery iIndelivery, SSProduct iProduct) {

        for (SSIndeliveryRow iRow : iIndelivery.getRows()) {
            if(iRow.hasProduct(iProduct)) return true;
        }
        return false;
    }

    public static Map<String, Integer> getStockInfluencing(List<SSIndelivery> iIndeliveries) {
        Map<String, Integer> iIndeliveryCount = new HashMap<String, Integer>();
        for (SSIndelivery iIndelivery : iIndeliveries) {
            for (SSIndeliveryRow iRow : iIndelivery.getRows()) {
                if(iRow.getChange() == null) continue;
                Integer iReserved = iIndeliveryCount.get(iRow.getProductNr()) == null ? iRow.getChange() : iIndeliveryCount.get(iRow.getProductNr()) + iRow.getChange();
                iIndeliveryCount.put(iRow.getProductNr(), iReserved);
            }
        }
        return iIndeliveryCount;
    }

}
