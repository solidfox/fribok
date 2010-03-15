package se.swedsoft.bookkeeping.calc;

import se.swedsoft.bookkeeping.data.SSInvoice;

/**
 * Generates unique OCR numbers from invoice numbers.
 *
 * User: Andreas Lago
 * Date: 2006-aug-24
 * Time: 11:16:16
 */
public class SSOCRNumber {

    /**
     * Generate an OCR number from an invoice
     *
     * This is actually made from the invoice numer, and two checksum digits,
     * one of which is based on the length of the invoice number.
     *
     * @param iInvoice the invoice for which to generate an OCR number
     * @return the OCR number
     */
    public static String getOCRNumber(SSInvoice iInvoice){
        String strNumber = Integer.toString( iInvoice.getNumber() );

        int  iNumber    = iInvoice.getNumber();
        char iLengthSum = getLengthSum(iNumber);
        char iChecksum  = getCheckSum(strNumber + iLengthSum);

        return Integer.toString(iNumber) + iLengthSum + iChecksum;
    }

    /**
     * Generate a checksum digit for an arbitrary invoice number
     *
     * @param iText the invoice number for which to generate a checksum
     * @return the checksum digit
     */
    public static char getCheckSum(Integer iText){
        return getCheckSum( Integer.toString(iText) );
    }

    /**
     * Generate a checksum digit for a specified invoice
     *
     * @param iText the invoice for which to generate a checksum
     * @return the checksum digit
     */
    public static char getCheckSum(String iText){
        int iChecksum = 0;
        int iWeight   = 2;

        for(int i = iText.length() -1 ; i >=0; i--){
            char iChar = iText.charAt(i);

            int iValue = 0;
            try {
                iValue = Integer.decode(String.valueOf(iChar));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            int iSum =(iValue * iWeight);

            if( iSum > 9){
                iChecksum += iSum - 9;
            } else{
                iChecksum += iSum;
            }

            iWeight = (iWeight == 2) ? 1: 2;

        }
        iChecksum = (iChecksum / 10 + 1) * 10 - iChecksum;

        if(iChecksum == 10){
            return '0';
        } else {
            return Integer.toString( iChecksum ).charAt(0);
        }
    }

    /**
     * Generate a number based on the length of the invoice number
     *
     * The formula is (number of digits in number + 2) % 10
     *
     * @param iNumber the invoice number
     * @return a number 0-9
     */
    private static char getLengthSum(int iNumber) {
        String iString = Integer.toString(iNumber);

        return Integer.toString((iString.length() + 2) % 10).charAt(0);
    }
}
