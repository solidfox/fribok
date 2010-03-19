package se.swedsoft.bookkeeping.calc.math;

import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-aug-04
 * Time: 13:55:43
 */
public class SSCustomerMath {
    private SSCustomerMath() {
    }

    /**
     * Returns the customer claim for the specified cistpmer
     *
     * @param iCustomer
     * @return
     */
    public static BigDecimal getCustomerClain(SSCustomer iCustomer) {
        List<SSInvoice> iInvoices = SSInvoiceMath.getInvoicesForCustomer(iCustomer);

        BigDecimal iCustomerClaim = new BigDecimal(0.0);
        for (SSInvoice iInvoice : iInvoices) {
            BigDecimal iSaldo = SSInvoiceMath.getSaldo(iInvoice.getNumber());

            iSaldo = SSInvoiceMath.convertToLocal(iInvoice, iSaldo);

            iCustomerClaim = iCustomerClaim.add(iSaldo);
        }

        return iCustomerClaim;
    }

    public static HashMap<String,List<SSInvoice>> iInvoicesForCustomers;

    public static void getInvoicesForCustomers(){
        if(iInvoicesForCustomers == null) iInvoicesForCustomers = new HashMap<String,List<SSInvoice>>();

        List<SSCustomer> iCustomers = SSDB.getInstance().getCustomers();

        for(SSCustomer iCustomer:iCustomers){
            List<SSInvoice> iInvoices = SSInvoiceMath.getInvoicesForCustomer(iCustomer);
            List<SSInvoice> iInvoiceNumbers = new LinkedList<SSInvoice>();
            for(SSInvoice iInvoice:iInvoices){
                iInvoiceNumbers.add(iInvoice);
            }
            iInvoicesForCustomers.put(iCustomer.getNumber(),iInvoiceNumbers);
        }
    }
}
