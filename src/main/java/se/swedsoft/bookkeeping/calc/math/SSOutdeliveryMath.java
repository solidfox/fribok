package se.swedsoft.bookkeeping.calc.math;


import se.swedsoft.bookkeeping.data.SSOutdelivery;
import se.swedsoft.bookkeeping.data.SSOutdeliveryRow;
import se.swedsoft.bookkeeping.data.SSProduct;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * User: Andreas Lago
 * Date: 2006-sep-22
 * Time: 16:34:16
 */
public class SSOutdeliveryMath {
    private SSOutdeliveryMath() {}

    /**
     *
     * @param iInventory
     * @param pTo
     * @return
     */
    public static boolean inPeriod(SSOutdelivery iInventory, Date pTo) {
        Date iDate = iInventory.getDate();
        Date iTo = SSDateMath.ceil(pTo);

        return iDate.getTime() <= iTo.getTime();

    }

    /**
     *
     * @param iInventory
     * @param pFrom
     * @param pTo
     * @return
     */
    public static boolean inPeriod(SSOutdelivery iInventory, Date pFrom, Date pTo) {
        Date iDate = iInventory.getDate();
        Date iFrom = SSDateMath.floor(pFrom);
        Date iTo = SSDateMath.ceil(pTo);

        return (iFrom.getTime() <= iDate.getTime()) && (iDate.getTime() <= iTo.getTime());
    }

    /**
     *
     * @param iOutdelivery
     * @return
     */
    public static Integer getTotalCount(SSOutdelivery iOutdelivery) {
        Integer iCount = 0;

        for (SSOutdeliveryRow iRow : iOutdelivery.getRows()) {
            if (iRow.getChange() != null) {
                iCount = iCount + iRow.getChange();
            }

        }
        return iCount;
    }

    /**
     *
     * @param iOutdelivery
     * @param iProduct
     * @return
     */
    public static boolean hasProduct(SSOutdelivery iOutdelivery, SSProduct iProduct) {

        for (SSOutdeliveryRow iRow : iOutdelivery.getRows()) {
            if (iRow.hasProduct(iProduct)) {
                return true;
            }
        }
        return false;
    }

    public static Map<String, Integer> getStockInfluencing(List<SSOutdelivery> iOutdeliveries) {
        Map<String, Integer> iOutdeliveryCount = new HashMap<String, Integer>();

        for (SSOutdelivery iOutdelivery : iOutdeliveries) {
            for (SSOutdeliveryRow iRow : iOutdelivery.getRows()) {
                if (iRow.getChange() == null) {
                    continue;
                }
                Integer iReserved = iOutdeliveryCount.get(iRow.getProductNr()) == null
                        ? iRow.getChange()
                        : iOutdeliveryCount.get(iRow.getProductNr()) + iRow.getChange();

                iOutdeliveryCount.put(iRow.getProductNr(), iReserved);
            }
        }
        return iOutdeliveryCount;
    }
}
