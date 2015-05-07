package se.swedsoft.bookkeeping.calc.math;


import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.common.SSInvoiceType;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 15:42:39
 */
public class SSInvoiceMath extends SSSaleMath {

    /**
     *
     * @param iInvoice
     * @return
     */
    public static boolean expired(SSInvoice iInvoice) {
        Date iNow = new Date();
        Date iPaymentDay = iInvoice.getDueDate();

        iNow = SSDateMath.ceil(iNow);
        iPaymentDay = SSDateMath.floor(iPaymentDay);

        return iNow.after(iPaymentDay);

    }

    /**
     * Convers a value from a sales currency to the company currency
     *
     * @param iInvoice
     * @param iValue
     * @return the converted value
     */
    public static BigDecimal convertToLocal(SSInvoice iInvoice, BigDecimal iValue) {
        BigDecimal iCurrencyRate = iInvoice.getCurrencyRate();

        if (iCurrencyRate != null) {
            iValue = iValue.multiply(iCurrencyRate);
        }

        return iValue;
    }

    public static BigDecimal convertToLocal(Integer iInvoiceNr, BigDecimal iValue) {
        SSInvoice iInvoice = new SSInvoice();

        iInvoice.setNumber(iInvoiceNr);
        iInvoice = SSDB.getInstance().getInvoice(iInvoice);

        BigDecimal iCurrencyRate = iInvoice.getCurrencyRate();

        if (iCurrencyRate != null) {
            iValue = iValue.multiply(iCurrencyRate);
        }

        return iValue;
    }

    /**
     * Returns the saldo for the sales, in the sales currency
     *
     * @param iInvoice
     *
     * @return  the saldo
     */
    public static BigDecimal getSaldo(SSInvoice iInvoice) {
        // a cash sales cant have any saldo
        if (iInvoice.getType() == SSInvoiceType.CASH) {
            return new BigDecimal(0);
        }

        BigDecimal iTotalSum = getTotalSum(iInvoice);

        BigDecimal iCreditingSum = SSCreditInvoiceMath.getSumForInvoice(iInvoice);
        BigDecimal iInpaymentSum = SSInpaymentMath.getSumForInvoice(iInvoice);

        iTotalSum = iTotalSum.subtract(iCreditingSum);
        iTotalSum = iTotalSum.subtract(iInpaymentSum);

        return iTotalSum.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal getSaldo(Integer iInvoiceNumber) {
        if (iSaldoMap.containsKey(iInvoiceNumber)) {
            return iSaldoMap.get(iInvoiceNumber);
        } else {
            return new BigDecimal(0);
        }
    }

    public static HashMap<Integer, BigDecimal> iSaldoMap;

    public static void calculateSaldos() {
        if (iSaldoMap == null) {
            iSaldoMap = new HashMap<Integer, BigDecimal>();
        }
        HashMap<Integer, BigDecimal> iInpaymentSum = SSInpaymentMath.getSumsForInvoices();

        HashMap<Integer, BigDecimal> iCreditInvoiceSum = SSCreditInvoiceMath.getSumsForInvoices();

        List<SSInvoice> iInvoices = SSDB.getInstance().getInvoices();

        for (SSInvoice iInvoice : iInvoices) {
            if (iInvoice.getType() == SSInvoiceType.CASH) {
                continue;
            }

            BigDecimal iTotalSum = getTotalSum(iInvoice);

            if (iInpaymentSum.containsKey(iInvoice.getNumber())) {
                iTotalSum = iTotalSum.subtract(iInpaymentSum.get(iInvoice.getNumber()));
            }

            if (iCreditInvoiceSum.containsKey(iInvoice.getNumber())) {
                iTotalSum = iTotalSum.subtract(iCreditInvoiceSum.get(iInvoice.getNumber()));
            }

            iSaldoMap.put(iInvoice.getNumber(), iTotalSum);
        }
    }

    public static Map<Integer, BigDecimal> getSaldos(Date iDate) {
        Map<Integer, BigDecimal> iSaldos = new HashMap<Integer, BigDecimal>();

        HashMap<Integer, BigDecimal> iInpaymentSum = SSInpaymentMath.getSumsForInvoices(
                iDate);

        HashMap<Integer, BigDecimal> iCreditInvoiceSum = SSCreditInvoiceMath.getSumsForInvoices(
                iDate);

        List<SSInvoice> iInvoices = SSDB.getInstance().getInvoices();

        for (SSInvoice iInvoice : iInvoices) {
            if (iInvoice.getType() == SSInvoiceType.CASH) {
                continue;
            }

            BigDecimal iTotalSum = getTotalSum(iInvoice);

            if (iInpaymentSum.containsKey(iInvoice.getNumber())) {
                iTotalSum = iTotalSum.subtract(iInpaymentSum.get(iInvoice.getNumber()));
            }

            if (iCreditInvoiceSum.containsKey(iInvoice.getNumber())) {
                iTotalSum = iTotalSum.subtract(iCreditInvoiceSum.get(iInvoice.getNumber()));
            }

            iSaldos.put(iInvoice.getNumber(), iTotalSum);
        }
        return iSaldos;
    }

    /**
     * Returns the partial saldo for the sales, in the sales currency up
     * to and including the selected date
     *
     * @param iInvoice
     * @param iDate The end date to calculate up to
     *
     * @return  the saldo
     */
    public static BigDecimal getSaldo(SSInvoice iInvoice, Date iDate) {
        // a cash sales cant have any saldo
        if (iInvoice.getType() == SSInvoiceType.CASH) {
            return new BigDecimal(0);
        }

        BigDecimal iTotalSum = getTotalSum(iInvoice);

        BigDecimal iCreditingSum = SSCreditInvoiceMath.getSumForInvoice(iInvoice, iDate);
        BigDecimal iInpaymentSum = SSInpaymentMath.getSumForInvoice(iInvoice, iDate);

        iTotalSum = iTotalSum.subtract(iCreditingSum);
        iTotalSum = iTotalSum.subtract(iInpaymentSum);

        return iTotalSum.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Returns all invoices and saldos up to and including the specified date
     *
     * @param iInvoices The invoices
     * @param iDate The end date
     *
     * @return map of the invoices and their saldo
     */
    public static Map<SSInvoice, BigDecimal> getSaldo(List<SSInvoice> iInvoices, Date iDate) {
        Map<SSInvoice, BigDecimal> iSaldos = new HashMap<SSInvoice, BigDecimal>();

        // Ceil the date so the before and after comparisions will be correct
        iDate = SSDateMath.ceil(iDate);

        HashMap<Integer, BigDecimal> iInpaymentSum = SSInpaymentMath.getSumsForInvoices(
                iDate);

        HashMap<Integer, BigDecimal> iCreditInvoiceSum = SSCreditInvoiceMath.getSumsForInvoices(
                iDate);

        // Loop through the invoices
        for (SSInvoice iInvoice : iInvoices) {
            Date iCurrent = iInvoice.getDate();

            // Only put invoices that is added before the specified date
            if (iCurrent.before(iDate) && iInvoice.getType() != SSInvoiceType.CASH) {
                BigDecimal iSum = getTotalSum(iInvoice);

                if (iInpaymentSum.containsKey(iInvoice.getNumber())) {
                    iSum = iSum.subtract(iInpaymentSum.get(iInvoice.getNumber()));
                }

                if (iCreditInvoiceSum.containsKey(iInvoice.getNumber())) {
                    iSum = iSum.subtract(iCreditInvoiceSum.get(iInvoice.getNumber()));
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
    public static BigDecimal getSaldoSum(List<SSInvoice> iInvoices, Date iDate) {
        Map<SSInvoice, BigDecimal> iSaldos = getSaldo(iInvoices, iDate);

        BigDecimal iSum = new BigDecimal(0);

        for (SSInvoice iInvoice : iInvoices) {
            iSum = iSum.add(iSaldos.get(iInvoice));
        }

        return iSum;
    }

    /**
     * Returns the saldo for the sales, in the sales currency
     *
     * @param iInvoice
     *
     * @param iDate
     * @return  the saldo
     */
    public static BigDecimal getSumMinusCredited(SSInvoice iInvoice, Date iDate) {
        // a cash sales cant have any saldo
        if (iInvoice.getType() == SSInvoiceType.CASH) {
            return new BigDecimal(0);
        }

        BigDecimal iTotalSum = getTotalSum(iInvoice);

        BigDecimal iCreditingSum = SSCreditInvoiceMath.getSumForInvoice(iInvoice, iDate);

        iTotalSum = iTotalSum.subtract(iCreditingSum);

        return iTotalSum;
    }

    /**
     * Returns the order connected to this sales, null if none is found
     *
     * @param iInvoice
     * @return the order or null
     */
    public static List<SSOrder> getOrdersForInvoice(SSInvoice iInvoice) {
        return getOrdersForInvoice(SSDB.getInstance().getOrders(), iInvoice);
    }

    /**
     * Returns the order connected to this sales from the list of orders, null if none is found
     *
     * @param iOrders
     * @param iInvoice
     * @return the order or null
     */
    public static List<SSOrder> getOrdersForInvoice(List<SSOrder> iOrders, SSInvoice iInvoice) {
        List<SSOrder> iFiltered = new LinkedList<SSOrder>();

        for (SSOrder iOrder : iOrders) {
            if (iOrder.hasInvoice(iInvoice)) {
                iFiltered.add(iOrder);
            }
        }
        return iFiltered;
    }

    /**
     * Returns all invoices for the current customer
     *
     * @param iCustomer
     * @return the invoices for the customer
     */
    public static List<SSInvoice> getInvoicesForCustomer(SSCustomer iCustomer) {
        return getInvoicesForCustomer(SSDB.getInstance().getInvoices(), iCustomer);
    }

    /**
     * Returns all invoices for the current customer
     *
     * @param iInvoices
     * @param iCustomer
     * @return the invoices for the customer
     */
    public static List<SSInvoice> getInvoicesForCustomer(List<SSInvoice> iInvoices, SSCustomer iCustomer) {
        List<SSInvoice> iFiltered = new LinkedList<SSInvoice>();

        for (SSInvoice iInvoice : iInvoices) {
            if (iInvoice.hasCustomer(iCustomer)) {
                iFiltered.add(iInvoice);
            }

        }

        return iFiltered;
    }

    public static Map<String, List<SSInvoice>> getInvoicesforCustomers() {
        List<SSInvoice> iInvoices = SSDB.getInstance().getInvoices();
        Map<String, List<SSInvoice>> iMap = new HashMap<String, List<SSInvoice>>();

        for (SSInvoice iInvoice : iInvoices) {
            if (iInvoice.getCustomerNr() != null) {
                if (iMap.containsKey(iInvoice.getCustomerNr())) {
                    iMap.get(iInvoice.getCustomerNr()).add(iInvoice);
                } else {
                    List<SSInvoice> iTemp = new LinkedList<SSInvoice>();

                    iTemp.add(iInvoice);
                    iMap.put(iInvoice.getCustomerNr(), iTemp);
                }
            }
        }
        return iMap;
    }

    /**
     * Returns all invoices for the current customer
     *
     * @param iCustomer
     * @param iDate
     * @return the invoices for the customer
     */
    public static List<SSInvoice> getInvoicesForCustomer(SSCustomer iCustomer, Date iDate) {
        return getInvoicesForCustomer(SSDB.getInstance().getInvoices(), iCustomer, iDate);
    }

    /**
     * Returns all invoices for the current customer
     *
     * @param iInvoices
     * @param iCustomer
     * @param iDate
     * @return the invoices for the customer
     */
    public static List<SSInvoice> getInvoicesForCustomer(List<SSInvoice> iInvoices, SSCustomer iCustomer, Date iDate) {
        List<SSInvoice> iFiltered = new LinkedList<SSInvoice>();

        for (SSInvoice iInvoice : iInvoices) {
            if (iInvoice.hasCustomer(iCustomer) && inPeriod(iInvoice, iDate)) {
                iFiltered.add(iInvoice);
            }

        }

        return iFiltered;
    }

    /**
     * Returns the invoices where the saldo is zero
     *
     * @return list of invoices
     */
    public static List<SSInvoice> getPayedOrCreditedInvoices() {
        return getPayedOrCreditedInvoices(SSDB.getInstance().getInvoices());
    }

    /**
     * Returns the invoices where the saldo is zero
     *
     * @param iInvoices
     * @return list of invoices
     */
    public static List<SSInvoice> getPayedOrCreditedInvoices(List<SSInvoice> iInvoices) {
        List<SSInvoice> iFiltered = new LinkedList<SSInvoice>();

        for (SSInvoice iInvoice : iInvoices) {
            BigDecimal iSaldo = getSaldo(iInvoice.getNumber());

            if (iSaldo.signum() == 0) {
                iFiltered.add(iInvoice);
            }
        }
        return iFiltered;
    }

    /**
     * Returns the invoices where the saldo different from zero
     *
     * @return list of invoices
     */
    public static List<SSInvoice> getNonPayedOrCreditedInvoices() {
        return getNonPayedOrCreditedInvoices(SSDB.getInstance().getInvoices());
    }

    /**
     * Returns the invoices where the saldo is different from zero
     *
     * @param iInvoices
     * @return list of invoices
     */
    public static List<SSInvoice> getNonPayedOrCreditedInvoices(List<SSInvoice> iInvoices) {
        List<SSInvoice> iFiltered = new LinkedList<SSInvoice>();

        for (SSInvoice iInvoice : iInvoices) {
            BigDecimal iSaldo = getSaldo(iInvoice.getNumber());

            if (iSaldo.signum() != 0) {
                iFiltered.add(iInvoice);
            }
        }
        return iFiltered;
    }

    /**
     *
     * @param iInvoice
     * @return
     */
    public static int getNumDelayedDays(SSInvoice iInvoice) {
        Date iPaymentDay = iInvoice.getDueDate();

        Date iLastPayment = SSInpaymentMath.getLastInpaymentForInvoice(iInvoice);

        if (iLastPayment == null || iPaymentDay == null) {
            return 0;
        }

        Calendar iCalendar = Calendar.getInstance();

        iCalendar.setTimeInMillis(iLastPayment.getTime() - iPaymentDay.getTime());

        int iYear = iCalendar.get(Calendar.YEAR) - 1970;
        int iDay = iCalendar.get(Calendar.DAY_OF_YEAR);

        return iYear * iCalendar.getActualMaximum(Calendar.DAY_OF_YEAR) + iDay;
    }

    /**
     *
     * @param iInvoice
     * @return
     */
    public static BigDecimal getInterestSaldo(SSInvoice iInvoice) {
        BigDecimal iTotalSum = getTotalSum(iInvoice);

        BigDecimal iCredited = SSCreditInvoiceMath.getSumForInvoice(iInvoice);

        return iTotalSum.subtract(iCredited);

    }

    /**
     *
     * @param iInvoice
     * @param iSaldo
     * @param iNumDays
     * @return
     */
    public static BigDecimal getInterestSum(SSInvoice iInvoice, BigDecimal iSaldo, int iNumDays) {
        BigDecimal iInterest = iInvoice.getDelayInterest();

        // System.out.println(iInterest);
        BigDecimal iNormalisedInterest = iInterest.scaleByPowerOfTen(-2);

        BigDecimal iDay = new BigDecimal(iNumDays).divide(new BigDecimal(365), 16,
                RoundingMode.HALF_UP);

        return iSaldo.multiply(iNormalisedInterest).multiply(iDay);
    }

    /**
     *
     * @param iReferensNumber
     * @return
     */
    public static SSInvoice getInvoiceByReference(String iReferensNumber) {
        return getInvoiceByReference(SSDB.getInstance().getInvoices(), iReferensNumber);
    }

    /**
     *
     * @param iInvoices
     * @param iReferensNumber
     * @return
     */
    public static SSInvoice getInvoiceByReference(List<SSInvoice> iInvoices, String iReferensNumber) {
        for (SSInvoice iInvoice : iInvoices) {
            String iNumber = iInvoice.getNumber().toString();
            String iOCRNumber = iInvoice.getOCRNumber();

            if (iReferensNumber.equals(iOCRNumber) || iReferensNumber.equals(iNumber)) {
                return iInvoice;
            }
        }
        return null;
    }

    public static Map<String, Integer> getStockInfluencing(List<? extends SSInvoice> iInvoices) {
        Map<String, Integer> iInvoiceCount = new HashMap<String, Integer>();
        List<String> iParcelProducts = new LinkedList<String>();
        List<SSProduct> iProducts = new LinkedList<SSProduct>(
                SSDB.getInstance().getProducts());

        for (SSProduct iProduct : iProducts) {
            if (iProduct.isParcel() && iProduct.getNumber() != null) {
                iParcelProducts.add(iProduct.getNumber());
            }
        }
        for (SSInvoice iInvoice : iInvoices) {
            for (SSSaleRow iRow : iInvoice.getRows()) {
                if (iRow.getQuantity() == null) {
                    continue;
                }
                Integer iReserved;

                if (iParcelProducts.contains(iRow.getProductNr())) {
                    SSProduct iProduct = iRow.getProduct();

                    if (iProduct != null) {
                        for (SSProductRow iProductRow : iProduct.getParcelRows()) {
                            iReserved = iInvoiceCount.get(iProductRow.getProductNr())
                                    == null
                                            ? iProductRow.getQuantity()
                                                    * iRow.getQuantity()
                                                    : iInvoiceCount.get(
                                                            iProductRow.getProductNr())
                                                                    + (iProductRow.getQuantity()
                                                                            * iRow.getQuantity());
                            iInvoiceCount.put(iProductRow.getProductNr(), iReserved);
                        }
                    }
                } else {
                    iReserved = iInvoiceCount.get(iRow.getProductNr()) == null
                            ? iRow.getQuantity()
                            : iInvoiceCount.get(iRow.getProductNr()) + iRow.getQuantity();
                    iInvoiceCount.put(iRow.getProductNr(), iReserved);
                }
            }
        }
        return iInvoiceCount;
    }

}

