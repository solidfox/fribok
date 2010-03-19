package se.swedsoft.bookkeeping.calc.math;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.math.BigDecimal;
import java.util.*;

/**
 * User: Andreas Lago
 * Date: 2006-jun-14
 * Time: 09:13:54
 */
public class SSSupplierCreditInvoiceMath {
    private SSSupplierCreditInvoiceMath() {
    }


    /**
     *
     * @param iSupplierInvoice
     * @param pFrom
     * @param pTo
     * @return
     */
    public static boolean inPeriod( SSSupplierCreditInvoice iSupplierInvoice, Date pFrom, Date pTo){
        Date iDate = iSupplierInvoice.getDate();
        Date iFrom = SSDateMath.floor(pFrom);
        Date iTo   = SSDateMath.ceil (pTo);

        return (iFrom.getTime() <= iDate.getTime()) && (iDate.getTime() <= iTo.getTime());
    }

    /**
     * Get the sum for the credit sales in the sales currency
     *
     * @param iInvoice
     * @return the sum
     */
    public static BigDecimal getSumForInvoice(SSSupplierInvoice iInvoice){
        // Get all credit invoices from the db
        List<SSSupplierCreditInvoice> iCreditInvoices = SSDB.getInstance().getSupplierCreditInvoices();

        BigDecimal iSum = new BigDecimal(0.0);

        for (SSSupplierCreditInvoice iCreditInvoice : iCreditInvoices) {
            BigDecimal iRowSum = SSSupplierInvoiceMath.getTotalSum(iCreditInvoice);

            if(iRowSum != null && iCreditInvoice.isCrediting(iInvoice) ) {
                iSum = iSum.add(iRowSum);
            }
        }
        return iSum;
    }

    /**
     * Get the sum for the credit sales in the sales currency up and including to the selected date
     *
     * @param iInvoice
     * @param iDate
     * @return the sum
     */
    public static BigDecimal getSumForInvoice(SSSupplierInvoice iInvoice, Date iDate) {
        // Get all credit invoices from the db
        List<SSSupplierCreditInvoice> iCreditInvoices = SSDB.getInstance().getSupplierCreditInvoices();

        iDate = SSDateMath.ceil(iDate);
        BigDecimal iSum = new BigDecimal(0.0);
        for (SSSupplierCreditInvoice iCreditInvoice : iCreditInvoices) {
            Date iCurrent = SSDateMath.floor(  iCreditInvoice.getDate() );

            BigDecimal iRowSum = SSSupplierInvoiceMath.getTotalSum(iCreditInvoice);

            if(iRowSum != null && iCreditInvoice.isCrediting(iInvoice) && iCurrent.before(iDate)) {
                iSum = iSum.add(iRowSum);
            }
        }
        return iSum;
    }

    public static HashMap<Integer,BigDecimal> getSumsForSupplierInvoices(){
        HashMap<Integer,BigDecimal> iSums = new HashMap<Integer,BigDecimal>();

        List<SSSupplierCreditInvoice> iSupplierCreditInvoices = SSDB.getInstance().getSupplierCreditInvoices();

        for (SSSupplierCreditInvoice iSupplierCreditInvoice : iSupplierCreditInvoices) {
            BigDecimal iRowSum = SSSupplierInvoiceMath.getTotalSum(iSupplierCreditInvoice);

            if(iRowSum != null && iSupplierCreditInvoice.getCreditingNr() != null){
                if(iSums.containsKey(iSupplierCreditInvoice.getCreditingNr())){
                    iSums.put(iSupplierCreditInvoice.getCreditingNr(),iSums.get(iSupplierCreditInvoice.getCreditingNr()).add(iRowSum));
                }
                else{
                    iSums.put(iSupplierCreditInvoice.getCreditingNr(),iRowSum);
                }
            }
        }
        return iSums;
    }

    public static HashMap<Integer,BigDecimal> getSumsForSupplierInvoices(Date iDate){
        HashMap<Integer,BigDecimal> iSums = new HashMap<Integer,BigDecimal>();

        List<SSSupplierCreditInvoice> iSupplierCreditInvoices = SSDB.getInstance().getSupplierCreditInvoices();

        for (SSSupplierCreditInvoice iSupplierCreditInvoice : iSupplierCreditInvoices) {
            if(iSupplierCreditInvoice.getDate().before(iDate)){
                BigDecimal iRowSum = SSSupplierInvoiceMath.getTotalSum(iSupplierCreditInvoice);

                if(iRowSum != null && iSupplierCreditInvoice.getCreditingNr() != null){
                    if(iSums.containsKey(iSupplierCreditInvoice.getCreditingNr())){
                        iSums.put(iSupplierCreditInvoice.getCreditingNr(),iSums.get(iSupplierCreditInvoice.getCreditingNr()).add(iRowSum));
                    }
                    else{
                        iSums.put(iSupplierCreditInvoice.getCreditingNr(),iRowSum);
                    }
                }
            }
        }
        return iSums;
    }

    public static Map<String, Integer> getStockInfluencing(List<SSSupplierCreditInvoice> iSupplierCreditInvoices) {
        Map<String, Integer> iSupplierCreditInvoiceCount = new HashMap<String, Integer>();
        List<String> iParcelProducts = new LinkedList<String>();
        List<SSProduct> iProducts = new LinkedList<SSProduct>(SSDB.getInstance().getProducts());
        for (SSProduct iProduct : iProducts) {
            if (iProduct.isParcel() && iProduct.getNumber() != null) {
                iParcelProducts.add(iProduct.getNumber());
            }
        }
        for (SSSupplierCreditInvoice iSupplierCreditInvoice : iSupplierCreditInvoices) {
            for (SSSupplierInvoiceRow iRow : iSupplierCreditInvoice.getRows()) {
                if(iRow.getQuantity() == null) continue;
                Integer iReserved;
                if (iParcelProducts.contains(iRow.getProductNr())) {
                    SSProduct iProduct = iRow.getProduct();
                    if (iProduct != null) {
                        for (SSProductRow iProductRow : iProduct.getParcelRows()) {
                            iReserved = iSupplierCreditInvoiceCount.get(iProductRow.getProductNr()) == null ? iProductRow.getQuantity()*iRow.getQuantity() : iSupplierCreditInvoiceCount.get(iProductRow.getProductNr()) + (iProductRow.getQuantity()*iRow.getQuantity());
                            iSupplierCreditInvoiceCount.put(iProductRow.getProductNr(), iReserved);
                        }
                    }
                } else {
                    iReserved = iSupplierCreditInvoiceCount.get(iRow.getProductNr()) == null ? iRow.getQuantity() : iSupplierCreditInvoiceCount.get(iRow.getProductNr()) + iRow.getQuantity();
                    iSupplierCreditInvoiceCount.put(iRow.getProductNr(), iReserved);
                }
            }
        }
        return iSupplierCreditInvoiceCount;
    }



}
