package se.swedsoft.bookkeeping.calc;

import se.swedsoft.bookkeeping.data.SSInvoice;

/**
 * User: Andreas Lago
 * Date: 2006-aug-24
 * Time: 11:16:16
 */
public class SSOCRNumber {


    /**
     *
     * @param iInvoice
     * @return
     */
    public static String getOCRNumber(SSInvoice iInvoice){
        String strNumber = Integer.toString( iInvoice.getNumber() );

        int  iNumber    = iInvoice.getNumber();
        char iLengthSum = getLengthSum(iNumber);
        char iChecksum  = getCheckSum(strNumber + iLengthSum);

        return Integer.toString(iNumber) + iLengthSum + "" + iChecksum;
    }



    /**
     *
     * @param iText
     * @return
     */
    public static char getCheckSum(Integer iText){
        return getCheckSum( Integer.toString(iText) );
    }
    /**
     *
     * @param iText
     * @return
     */
    public static char getCheckSum(String iText){
        int iChecksum = 0;
        int iWeight   = 2;
       // 1212
      //  /21212

      //  char[] iChars = new char[iText.length()];

     //   iText.getChars(0, 0, iChars, iChars.length);

        for(int i = iText.length() -1 ; i >=0; i--){
            char iChar = iText.charAt(i);

            int iValue = 0;
            try {
                iValue = Integer.decode("" + iChar);
            } catch (NumberFormatException e) {

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
     *
     * @param iNumber
     * @return
     */
    private static char getLengthSum(int iNumber) {
        String iString = Integer.toString(iNumber);

        return Integer.toString((iString.length() + 2) % 10).charAt(0);
    }

}
