package se.swedsoft.bookkeeping.calc.math;

import se.swedsoft.bookkeeping.data.SSOutpayment;
import se.swedsoft.bookkeeping.data.SSOutpaymentRow;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-apr-07
 * Time: 15:18:22
 */
public class SSOutpaymentMath {

    /**
     *
     * @param iOutpayment
     * @param pFrom
     * @param pTo
     * @return
     */
    public static boolean inPeriod( SSOutpayment iOutpayment, Date pFrom, Date pTo){
        Date iDate = iOutpayment.getDate();
        Date iFrom = SSDateMath.floor(pFrom);
        Date iTo   = SSDateMath.ceil (pTo);

        return (iFrom.getTime() <= iDate.getTime()) && (iDate.getTime() <= iTo.getTime());
    }

    /**
     * Convers a value from a row currency to the company currency
     *
     * @param iRow
     * @param iValue
     * @return the converted value
     */
    public static BigDecimal convertToLocal(SSOutpaymentRow iRow, BigDecimal iValue){
        BigDecimal iCurrencyRate = iRow.getCurrencyRate();

        if(iCurrencyRate != null){
            iValue = iValue.multiply( iCurrencyRate );
        }

        return iValue;
    }


    /**
     * Get the sum for the outpayment in the payment currency
     *
     * @param iOutpayment
     * @return the sum
     */
    public static BigDecimal getSum(SSOutpayment iOutpayment){

        BigDecimal iSum = new BigDecimal(0.0);

        for( SSOutpaymentRow iRow: iOutpayment.getRows()){
            BigDecimal iValue = iRow.getValue();

            if(iValue != null) {
                // Convert to the local currency
                iValue = convertToLocal(iRow, iValue);

                iSum = iSum.add(iValue);
            }
        }

        return iSum;

    }



    /**
     * Get the difference between the value of the sales in the sales currency and the payed value, in the company currency such as
     *
     * return (Value of row) * (payment currency rate - sales currency rate)
     *
     * @param iOutpaymentRow
     * @return the currency rate difference
     */
    public static BigDecimal getCurrencyRateDifference(SSOutpaymentRow iOutpaymentRow){
        BigDecimal iPaymentRate   = iOutpaymentRow.getCurrencyRate();
        BigDecimal iCurrencyRate  = iOutpaymentRow.getInvoiceCurrencyRate();
        BigDecimal iValue         = iOutpaymentRow.getValue();

        if(iPaymentRate == null || iCurrencyRate == null || iValue == null) return null;

        return iValue.multiply(  iCurrencyRate.subtract( iPaymentRate ) );
    }


    /**
     * Get the currency rate difference for the whole inpayment, in the company currency
     *
     * @param iOutpayment
     * @return the currency rate difference
     */
    public static BigDecimal getCurrencyRateDifference(SSOutpayment iOutpayment){
        BigDecimal iSum = new BigDecimal(0.0);

        for( SSOutpaymentRow iRow: iOutpayment.getRows()){
            BigDecimal iRowSum = getCurrencyRateDifference(iRow);

            if(iRowSum != null) {
                iSum = iSum.add(iRowSum);
            }
        }

        return iSum;

    }


    /**
     * Get the sum of the inpayments for the supplied sales in the sales currency
     *
     * @param iOutpayment
     * @param iInvoice
     * @return the sum
     */
    public static BigDecimal getSumForInvoice(SSOutpayment iOutpayment, SSSupplierInvoice iInvoice){

        BigDecimal iSum = new BigDecimal(0.0);

        for( SSOutpaymentRow iRow: iOutpayment.getRows()){
            BigDecimal iRowValue   = iRow.getValue();

            if(iRowValue != null && iRow.isPaying(iInvoice) ) {
                iSum = iSum.add(iRowValue);
            }
        }

        return iSum;
    }

    /**
     * Get the sum for the credit sales in the sales currency
     *
     * @param iInvoice
     * @return the sum
     */
    public static BigDecimal getSumForInvoice(SSSupplierInvoice iInvoice){
        // Get all credit invoices from the db
        List<SSOutpayment> iOutpayments = SSDB.getInstance().getOutpayments();

        BigDecimal iSum = new BigDecimal(0.0);
        for (SSOutpayment iOutpayment : iOutpayments) {
            BigDecimal iRowSum = getSumForInvoice(iOutpayment, iInvoice);

            iSum = iSum.add(iRowSum);
        }

        return iSum;
    }

    public static HashMap<Integer,BigDecimal> getSumsForSupplierInvoices(){
        HashMap<Integer,BigDecimal> iSums = new HashMap<Integer,BigDecimal>();

        List<SSOutpayment> iOutpayments = SSDB.getInstance().getOutpayments();
        for (SSOutpayment iOutpayment : iOutpayments) {
            for(SSOutpaymentRow iRow : iOutpayment.getRows()){
                if(iRow.getValue() != null){
                    if(iSums.containsKey(iRow.getInvoiceNr())){
                        iSums.put(iRow.getInvoiceNr(), iSums.get(iRow.getInvoiceNr()).add(iRow.getValue()));
                    }
                    else{
                        iSums.put(iRow.getInvoiceNr(),iRow.getValue());
                    }
                }
            }
        }
        return iSums;
    }

    public static HashMap<Integer,BigDecimal> getSumsForSupplierInvoices(Date iDate){
        HashMap<Integer,BigDecimal> iSums = new HashMap<Integer,BigDecimal>();

        List<SSOutpayment> iOutpayments = SSDB.getInstance().getOutpayments();
        for (SSOutpayment iOutpayment : iOutpayments) {
            if(iOutpayment.getDate().before(iDate)){
                for(SSOutpaymentRow iRow : iOutpayment.getRows()){
                    if(iRow.getValue() != null){
                        if(iSums.containsKey(iRow.getInvoiceNr())){
                            iSums.put(iRow.getInvoiceNr(), iSums.get(iRow.getInvoiceNr()).add(iRow.getValue()));
                        }
                        else{
                            iSums.put(iRow.getInvoiceNr(),iRow.getValue());
                        }
                    }
                }
            }
        }
        return iSums;
    }

    /**
     * Get the sum of all outpayments for the supplied supplier invoice in the invoice currency up and including to the selected date
     *
     * @param iInvoice
     * @param iDate
     * @return the sum
     */
    public static BigDecimal getSumForInvoice(SSSupplierInvoice iInvoice, Date iDate) {
        List<SSOutpayment> iOutpayments = SSDB.getInstance().getOutpayments();

        BigDecimal iSum = new BigDecimal(0.0);

        iDate = SSDateMath.ceil(iDate);
        for (SSOutpayment iOutpayment : iOutpayments) {
            Date iCurrent = SSDateMath.floor(  iOutpayment.getDate() );

            BigDecimal iRowSum = getSumForInvoice(iOutpayment, iInvoice);

            if(iCurrent.before(iDate)){
                iSum = iSum.add(iRowSum);
            }
        }

        return iSum;
    }


      /**
     *
     * @param iOutpayment
     * @param iInvoice
     * @return
     */
    public static boolean hasInvoice(SSOutpayment iOutpayment, SSSupplierInvoice iInvoice) {

        for (SSOutpaymentRow iRow : iOutpayment.getRows()) {
            if(iRow.isPaying(iInvoice)) return true;
        }
        return false;
    }

}
