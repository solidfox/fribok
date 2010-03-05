package se.swedsoft.bookkeeping.calc.math;

import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSPeriodicInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.util.*;

/**
 * User: Andreas Lago
 * Date: 2006-aug-14
 * Time: 09:53:50
 */
public class SSPeriodicInvoiceMath {


    /**
     * Returns a list of all new periodic invoices.
     * @return
     */
    public static Map<SSPeriodicInvoice, List<SSInvoice>> getPeriodicInvoices() {
        List<SSPeriodicInvoice> iPeriodicInvoices = SSDB.getInstance().getPeriodicInvoices();

        Map<SSPeriodicInvoice, List<SSInvoice>> iResult = new HashMap<SSPeriodicInvoice, List<SSInvoice>>();

        Date iNow = new Date();
        for (SSPeriodicInvoice iPeriodicInvoice : iPeriodicInvoices) {
            List<SSInvoice> iInvoices = iPeriodicInvoice.getInvoices(iNow);

            iResult.put(iPeriodicInvoice,  iInvoices);
        }
        return iResult;

    }

    /**
     *
     * @return
     */
    public static boolean hasPendingPeriodicInvoices(){
        Map<SSPeriodicInvoice, List<SSInvoice>> iPeriodicInvoices = getPeriodicInvoices();

        for (SSPeriodicInvoice iPeriodicInvoice : iPeriodicInvoices.keySet()) {
            List<SSInvoice> iInvoices = iPeriodicInvoices.get(iPeriodicInvoice);

            if(!iInvoices.isEmpty()) return true;
        }
        return false;
    }







}
