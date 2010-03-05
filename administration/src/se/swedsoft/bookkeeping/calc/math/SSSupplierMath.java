package se.swedsoft.bookkeeping.calc.math;

import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-aug-04
 * Time: 13:58:10
 */
public class SSSupplierMath {



    /**
     * Returns the customer claim for the specified cistpmer
     *
     * @param iSupplier
     * @return
     */
    public static BigDecimal getSupplierDebt(SSSupplier iSupplier) {
        List<SSSupplierInvoice> iInvoices = SSSupplierInvoiceMath.getInvoicesForSupplier(iSupplier);

        BigDecimal iSum = new BigDecimal(0.0);
        for (SSSupplierInvoice iInvoice : iInvoices) {
            BigDecimal iSaldo = SSSupplierInvoiceMath.getSaldo(iInvoice.getNumber());

            iSaldo = SSSupplierInvoiceMath.convertToLocal(iInvoice, iSaldo);

            iSum = iSum.add(iSaldo);
        }

        return iSum;
    }

    public static HashMap<String,List<SSSupplierInvoice>> iInvoicesForSuppliers;

    public static void getInvoicesForSuppliers(){
        if(iInvoicesForSuppliers == null) iInvoicesForSuppliers = new HashMap<String,List<SSSupplierInvoice>>();

        List<SSSupplier> iSuppliers = SSDB.getInstance().getSuppliers();

        for(SSSupplier iSupplier:iSuppliers){
            List<SSSupplierInvoice> iSupplierInvoices = SSSupplierInvoiceMath.getInvoicesForSupplier(iSupplier);
            List<SSSupplierInvoice> iInvoices = new LinkedList<SSSupplierInvoice>();
            for(SSSupplierInvoice iSupplierInvoice:iSupplierInvoices){
                iInvoices.add(iSupplierInvoice);
            }
            iInvoicesForSuppliers.put(iSupplier.getNumber(),iInvoices);
        }
    }

    public static Integer getOutpaymentNumber() {
        Integer iOutpaymentNumber = 0;

        List<SSSupplier> iSuppliers = SSDB.getInstance().getSuppliers();
        for (SSSupplier iSupplier : iSuppliers) {
            if(iSupplier.getOutpaymentNumber() != null && iSupplier.getOutpaymentNumber() > iOutpaymentNumber)
                iOutpaymentNumber = iSupplier.getOutpaymentNumber();
        }
        iOutpaymentNumber++;

        return iOutpaymentNumber;
    }
}
