package se.swedsoft.bookkeeping.calc.math;


import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


/**
 * User: Andreas Lago
 * Date: 2006-jun-14
 * Time: 09:13:54
 */
public class SSSupplierInvoiceMath {
    private SSSupplierInvoiceMath() {}

    /**
     *
     * @param iSupplierInvoice
     * @param pFrom
     * @param pTo
     * @return
     */
    public static boolean inPeriod(SSSupplierInvoice iSupplierInvoice, Date pFrom, Date pTo) {
        Date iDate = iSupplierInvoice.getDate();
        Date iFrom = SSDateMath.floor(pFrom);
        Date iTo = SSDateMath.ceil(pTo);

        return (iFrom.getTime() <= iDate.getTime()) && (iDate.getTime() <= iTo.getTime());
    }

    /**
     *
     * @param iSupplierInvoice
     * @param pTo
     * @return
     */
    public static boolean inPeriod(SSSupplierInvoice iSupplierInvoice, Date pTo) {
        Date iDate = iSupplierInvoice.getDate();
        Date iTo = SSDateMath.ceil(pTo);

        return iDate.getTime() <= iTo.getTime();
    }

    /**
     * Convers a value from a sales currency to the company currency
     *
     * @param iSupplierInvoice the supplier invoice
     * @param iValue
     * @return the converted value
     */
    public static BigDecimal convertToLocal(SSSupplierInvoice iSupplierInvoice, BigDecimal iValue) {
        BigDecimal iCurrencyRate = iSupplierInvoice.getCurrencyRate();

        if (iCurrencyRate != null) {
            iValue = iValue.multiply(iCurrencyRate);
        }

        return iValue;
    }

    public static BigDecimal convertToLocal(Integer iSupplierInvoiceNr, BigDecimal iValue) {
        SSSupplierInvoice iSupplierInvoice = new SSSupplierInvoice();

        iSupplierInvoice.setNumber(iSupplierInvoiceNr);
        iSupplierInvoice = SSDB.getInstance().getSupplierInvoice(iSupplierInvoice);

        BigDecimal iCurrencyRate = iSupplierInvoice.getCurrencyRate();

        if (iCurrencyRate != null) {
            iValue = iValue.multiply(iCurrencyRate);
        }

        return iValue;
    }

    /**
     *
     * @param iInvoice
     * @return
     */
    public static boolean expired(SSSupplierInvoice iInvoice) {
        Date iNow = new Date();
        Date iPaymentDay = iInvoice.getDueDate();

        iNow = SSDateMath.ceil(iNow);
        iPaymentDay = SSDateMath.floor(iPaymentDay);

        return iNow.after(iPaymentDay);

    }

    /**
     * Get the total sum for the supplier invoice.
     *
     * @param iSupplierInvoice
     * @return the total sum
     */
    public static BigDecimal getNetSum(SSSupplierInvoice iSupplierInvoice) {
        BigDecimal iTotalSum = new BigDecimal(0);

        for (SSSupplierInvoiceRow iRow : iSupplierInvoice.getRows()) {
            BigDecimal iRowSum = iRow.getSum();

            if (iRowSum != null) {
                iTotalSum = iTotalSum.add(iRowSum);
            }

        }
        return iTotalSum;
    }

    /**
     * Get the total sum for the supplier invoice.
     *
     * @param iSupplierInvoice
     * @return the total sum
     */
    public static BigDecimal getTotalSum(SSSupplierInvoice iSupplierInvoice) {
        BigDecimal iNetSum = getNetSum(iSupplierInvoice);
        BigDecimal iTaxSum = iSupplierInvoice.getTaxSum();
        BigDecimal iRoundingSum = iSupplierInvoice.getRoundingSum();

        return iTaxSum == null
                ? (iRoundingSum == null ? iNetSum : iNetSum.add(iRoundingSum))
                : (iRoundingSum == null
                        ? iNetSum.add(iTaxSum)
                        : iNetSum.add(iTaxSum).add(iRoundingSum));
    }

    /**
     *
     * @param iSupplierInvoice
     * @return
     */
    public static BigDecimal getSaldo(SSSupplierInvoice iSupplierInvoice) {

        BigDecimal iTotalSum = getTotalSum(iSupplierInvoice);
        BigDecimal iCredited = SSSupplierCreditInvoiceMath.getSumForInvoice(
                iSupplierInvoice);
        BigDecimal iPayed = SSOutpaymentMath.getSumForInvoice(iSupplierInvoice);

        return iTotalSum.subtract(iCredited).subtract(iPayed);
    }

    public static BigDecimal getSaldo(Integer iSupplierInvoiceNumber) {
        if (iSaldoMap.containsKey(iSupplierInvoiceNumber)) {
            return iSaldoMap.get(iSupplierInvoiceNumber);
        } else {
            return new BigDecimal(0);
        }
    }

    /*
     * Returns the partial saldo for the sales, in the sales currency up
     * to and including the selected date
     *
     * @param iInvoice
     * @param iDate The end date to calculate up to
     *
     * @return  the saldo
     */
    public static BigDecimal getSaldo(SSSupplierInvoice iInvoice, Date iDate) {

        BigDecimal iTotalSum = getTotalSum(iInvoice);

        BigDecimal iCreditingSum = SSSupplierCreditInvoiceMath.getSumForInvoice(iInvoice,
                iDate);
        BigDecimal iInpaymentSum = SSOutpaymentMath.getSumForInvoice(iInvoice, iDate);

        iTotalSum = iTotalSum.subtract(iCreditingSum);
        iTotalSum = iTotalSum.subtract(iInpaymentSum);

        return iTotalSum;
    }

    public static HashMap<Integer, BigDecimal> iSaldoMap;

    public static void calculateSaldos() {
        if (iSaldoMap == null) {
            iSaldoMap = new HashMap<Integer, BigDecimal>();
        }
        HashMap<Integer, BigDecimal> iOutpaymentSum = SSOutpaymentMath.getSumsForSupplierInvoices();

        HashMap<Integer, BigDecimal> iSupplierCreditInvoiceSum = SSSupplierCreditInvoiceMath.getSumsForSupplierInvoices();

        List<SSSupplierInvoice> iSupplierInvoices = SSDB.getInstance().getSupplierInvoices();

        for (SSSupplierInvoice iSupplierInvoice : iSupplierInvoices) {
            BigDecimal iTotalSum = getTotalSum(iSupplierInvoice);

            if (iOutpaymentSum.containsKey(iSupplierInvoice.getNumber())) {
                iTotalSum = iTotalSum.subtract(
                        iOutpaymentSum.get(iSupplierInvoice.getNumber()));
            }

            if (iSupplierCreditInvoiceSum.containsKey(iSupplierInvoice.getNumber())) {
                iTotalSum = iTotalSum.subtract(
                        iSupplierCreditInvoiceSum.get(iSupplierInvoice.getNumber()));
            }

            iSaldoMap.put(iSupplierInvoice.getNumber(), iTotalSum);
        }
    }

    public static Map<Integer, BigDecimal> getSaldos(Date iDate) {
        Map<Integer, BigDecimal> iSaldos = new HashMap<Integer, BigDecimal>();

        HashMap<Integer, BigDecimal> iOutpaymentSum = SSOutpaymentMath.getSumsForSupplierInvoices(
                iDate);

        HashMap<Integer, BigDecimal> iSupplierCreditInvoiceSum = SSSupplierCreditInvoiceMath.getSumsForSupplierInvoices(
                iDate);

        List<SSSupplierInvoice> iSupplierInvoices = SSDB.getInstance().getSupplierInvoices();

        for (SSSupplierInvoice iSupplierInvoice : iSupplierInvoices) {

            BigDecimal iTotalSum = getTotalSum(iSupplierInvoice);

            if (iOutpaymentSum.containsKey(iSupplierInvoice.getNumber())) {
                iTotalSum = iTotalSum.subtract(
                        iOutpaymentSum.get(iSupplierInvoice.getNumber()));
            }

            if (iSupplierCreditInvoiceSum.containsKey(iSupplierInvoice.getNumber())) {
                iTotalSum = iTotalSum.subtract(
                        iSupplierCreditInvoiceSum.get(iSupplierInvoice.getNumber()));
            }

            iSaldos.put(iSupplierInvoice.getNumber(), iTotalSum);
        }
        return iSaldos;
    }

    /**
     * Returns all invoices and saldos up to and including the specified date
     *
     * @param iInvoices The invoices
     * @param iDate The end date
     *
     * @return map of the invoices and their saldo
     */
    
    /* public static Map<SSSupplierInvoice, BigDecimal> getSaldo(List<SSSupplierInvoice> iInvoices, Date iDate) {
     Map<SSSupplierInvoice, BigDecimal> iSaldos = new HashMap<SSSupplierInvoice, BigDecimal>();

     // Ceil the date so the before and after comparisions will be correct
     iDate = SSDateMath.ceil(iDate);

     // Loop through the invoices
     for (SSSupplierInvoice iInvoice : iInvoices) {
     Date iCurrent = iInvoice.getDate();

     // Only put invoices that is added before the specified date
     if( iCurrent.before(iDate)){
     BigDecimal iSaldo = getSaldo(iInvoice, iDate);

     iSaldos.put(iInvoice, iSaldo);
     }
     }
     return iSaldos;
     } */

    public static Map<SSSupplierInvoice, BigDecimal> getSaldo(List<SSSupplierInvoice> iInvoices, Date iDate) {
        Map<SSSupplierInvoice, BigDecimal> iSaldos = new HashMap<SSSupplierInvoice, BigDecimal>();

        // Ceil the date so the before and after comparisions will be correct
        iDate = SSDateMath.ceil(iDate);

        HashMap<Integer, BigDecimal> iOutpaymentSum = SSOutpaymentMath.getSumsForSupplierInvoices(
                iDate);

        HashMap<Integer, BigDecimal> iSupplierCreditInvoiceSum = SSSupplierCreditInvoiceMath.getSumsForSupplierInvoices(
                iDate);

        // Loop through the invoices
        for (SSSupplierInvoice iInvoice : iInvoices) {
            Date iCurrent = iInvoice.getDate();

            // Only put invoices that is added before the specified date
            if (iCurrent.before(iDate)) {
                BigDecimal iSum = getTotalSum(iInvoice);

                if (iOutpaymentSum.containsKey(iInvoice.getNumber())) {
                    iSum = iSum.subtract(iOutpaymentSum.get(iInvoice.getNumber()));
                }

                if (iSupplierCreditInvoiceSum.containsKey(iInvoice.getNumber())) {
                    iSum = iSum.subtract(
                            iSupplierCreditInvoiceSum.get(iInvoice.getNumber()));
                }

                iSaldos.put(iInvoice, iSum.setScale(2, RoundingMode.HALF_UP));
                // BigDecimal iSaldo = getSaldo(iInvoice, iDate);
                // iSaldos.put(iInvoice, iSaldo.setScale(2, RoundingMode.HALF_UP));
            }
        }
        return iSaldos;
    }

    /**
     * Returns the sum of all saldos up to and including the specified date
     *
     * @param iInvoices The invoices
     * @param iDate The end date
     *
     * @return the saldo sum
     */
    public static BigDecimal getSaldoSum(List<SSSupplierInvoice> iInvoices, Date iDate) {
        Map<SSSupplierInvoice, BigDecimal> iSaldos = getSaldo(iInvoices, iDate);

        BigDecimal iSum = new BigDecimal(0);

        for (SSSupplierInvoice iInvoice : iInvoices) {
            iSum = iSum.add(iSaldos.get(iInvoice));
        }

        return iSum;
    }

    /**
     * Returns all invoices for the current supplier
     *
     * @param iSupplier
     * @return the invoices for the supplier
     */
    public static List<SSSupplierInvoice> getInvoicesForSupplier(SSSupplier iSupplier) {
        return getInvoicesForSupplier(SSDB.getInstance().getSupplierInvoices(), iSupplier);
    }

    /**
     * Returns all invoices for the current supplier
     *
     * @param iInvoices
     * @param iSupplier
     * @return the invoices for the customer
     */
    public static List<SSSupplierInvoice> getInvoicesForSupplier(List<SSSupplierInvoice> iInvoices, SSSupplier iSupplier) {
        List<SSSupplierInvoice> iFiltered = new LinkedList<SSSupplierInvoice>();

        for (SSSupplierInvoice iInvoice : iInvoices) {
            if (iInvoice.hasSupplier(iSupplier)) {
                iFiltered.add(iInvoice);
            }

        }

        return iFiltered;
    }

    /**
     * Returns all invoices for the current supplier
     *
     * @param iSupplier
     * @param iDate
     * @return the invoices for the supplier
     */
    public static List<SSSupplierInvoice> getInvoicesForSupplier(SSSupplier iSupplier, Date iDate) {
        return getInvoicesForSupplier(SSDB.getInstance().getSupplierInvoices(), iSupplier,
                iDate);
    }

    /**
     * Returns all invoices for the current supplier
     *
     * @param iInvoices
     * @param iSupplier
     * @param iDate
     * @return the invoices for the customer
     */
    public static List<SSSupplierInvoice> getInvoicesForSupplier(List<SSSupplierInvoice> iInvoices, SSSupplier iSupplier, Date iDate) {
        List<SSSupplierInvoice> iFiltered = new LinkedList<SSSupplierInvoice>();

        for (SSSupplierInvoice iInvoice : iInvoices) {
            if (iInvoice.hasSupplier(iSupplier) && inPeriod(iInvoice, iDate)) {
                iFiltered.add(iInvoice);
            }

        }

        return iFiltered;
    }

    /**
     *
     * @param iSupplierInvoice
     * @param iProduct
     * @return
     */
    public static Integer getProductCount(SSSupplierInvoice iSupplierInvoice, SSProduct iProduct) {

        Integer iCount = 0;

        for (SSSupplierInvoiceRow iRow : iSupplierInvoice.getRows()) {
            SSProduct iRowProduct = iRow.getProduct();

            // Skip if no product or no quantity
            if (iRowProduct == null || iRow.getQuantity() == null) {
                continue;
            }

            // This is the product we want to get the quantity for
            if (iRowProduct.equals(iProduct)) {
                iCount = iCount + iRow.getQuantity();
            }
            // Get the quantity if this is a parcel product
            if (iRowProduct.isParcel()) {
                Integer iQuantity = SSProductMath.getProductCount(iRowProduct, iProduct);

                iCount = iCount + iRow.getQuantity() * iQuantity;
            }
        }
        return iCount;
    }

    /**
     *
     * @param iSupplierInvoices
     * @param iNumber
     * @return
     */
    public static SSSupplierInvoice getSupplierInvoice(List<SSSupplierInvoice> iSupplierInvoices, int iNumber) {

        for (SSSupplierInvoice iSupplierInvoice : iSupplierInvoices) {

            if (iSupplierInvoice.getNumber() == iNumber) {
                return iSupplierInvoice;
            }
        }
        return null;
    }

    /**
     *
     * @param iSupplierInvoices
     * @param iNumber
     * @return
     */
    public static SSSupplierInvoice getSupplierInvoiceByNumber(List<SSSupplierInvoice> iSupplierInvoices, Integer iNumber) {

        for (SSSupplierInvoice iSupplierInvoice : iSupplierInvoices) {

            if (iSupplierInvoice.getNumber().equals(iNumber)) {
                return iSupplierInvoice;
            }
        }
        return null;
    }

    /**
     *
     * @param iSupplierInvoices
     * @param iNumber
     * @return
     */
    public static SSSupplierInvoice getSupplierInvoiceByReference(List<SSSupplierInvoice> iSupplierInvoices, String iNumber) {

        for (SSSupplierInvoice iSupplierInvoice : iSupplierInvoices) {

            String iReferencenumber = iSupplierInvoice.getReferencenumber();

            if (iReferencenumber.length() > 0 && iReferencenumber.equals(iNumber)) {
                return iSupplierInvoice;
            }
        }
        return null;
    }

    /**
     *
     * @param iSupplierInvoice
     * @param iRow
     * @return
     */
    public static SSSupplierInvoiceRow getMatchingRow(SSSupplierInvoice iSupplierInvoice, SSPurchaseOrderRow iRow) {

        String iProductNr = iRow.getProductNr();

        if (iProductNr == null) {
            return null;
        }

        for (SSSupplierInvoiceRow iCurrent : iSupplierInvoice.getRows()) {

            if (iProductNr.equals(iCurrent.getProductNr())) {
                return iCurrent;
            }
        }
        return null;

    }

    /**
     * Returns the invoices where the saldo different from zero
     *
     * @return list of invoices
     */
    public static List<SSSupplierInvoice> getNonPayedOrCreditedInvoices() {
        return getNonPayedOrCreditedInvoices(SSDB.getInstance().getSupplierInvoices());
    }

    /**
     * Returns the invoices where the saldo is different from zero
     *
     * @param iInvoices
     * @return list of invoices
     */
    public static List<SSSupplierInvoice> getNonPayedOrCreditedInvoices(List<SSSupplierInvoice> iInvoices) {
        List<SSSupplierInvoice> iFiltered = new LinkedList<SSSupplierInvoice>();

        for (SSSupplierInvoice iInvoice : iInvoices) {
            BigDecimal iSaldo = getSaldo(iInvoice.getNumber());

            if (iSaldo.signum() != 0) {
                iFiltered.add(iInvoice);
            }
        }
        return iFiltered;
    }

    public static Map<String, Integer> getStockInfluencing(List<SSSupplierInvoice> iSupplierInvoices) {
        Map<String, Integer> iSupplierInvoiceCount = new HashMap<String, Integer>();
        List<String> iParcelProducts = new LinkedList<String>();
        List<SSProduct> iProducts = new LinkedList<SSProduct>(
                SSDB.getInstance().getProducts());

        for (SSProduct iProduct : iProducts) {
            if (iProduct.isParcel() && iProduct.getNumber() != null) {
                iParcelProducts.add(iProduct.getNumber());
            }
        }
        for (SSSupplierInvoice iSupplierInvoice : iSupplierInvoices) {
            for (SSSupplierInvoiceRow iRow : iSupplierInvoice.getRows()) {
                if (iRow.getQuantity() == null) {
                    continue;
                }
                Integer iReserved;

                if (iParcelProducts.contains(iRow.getProductNr())) {
                    SSProduct iProduct = iRow.getProduct();

                    if (iProduct != null) {
                        for (SSProductRow iProductRow : iProduct.getParcelRows()) {
                            iReserved = iSupplierInvoiceCount.get(
                                    iProductRow.getProductNr())
                                            == null
                                                    ? iProductRow.getQuantity()
                                                            * iRow.getQuantity()
                                                            : iSupplierInvoiceCount.get(
                                                                    iProductRow.getProductNr())
                                                                            + (iProductRow.getQuantity()
                                                                                    * iRow.getQuantity());
                            iSupplierInvoiceCount.put(iProductRow.getProductNr(),
                                    iReserved);
                        }
                    }
                } else {
                    iReserved = iSupplierInvoiceCount.get(iRow.getProductNr()) == null
                            ? iRow.getQuantity()
                            : iSupplierInvoiceCount.get(iRow.getProductNr())
                                    + iRow.getQuantity();
                    iSupplierInvoiceCount.put(iRow.getProductNr(), iReserved);
                }
            }
        }
        return iSupplierInvoiceCount;
    }

}

