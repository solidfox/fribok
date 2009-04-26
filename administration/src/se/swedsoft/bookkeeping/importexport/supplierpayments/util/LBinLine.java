package se.swedsoft.bookkeeping.importexport.supplierpayments.util;

import javax.swing.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.math.BigDecimal;

/**
 * User: Andreas Lago
 * Date: 2006-sep-04
 * Time: 09:47:45
 */
public class LBinLine {


    private char [] iChars;

    private int iPosition;


    /**
     *
     */
    public LBinLine(int iLength) {
        iChars    = new char[iLength];
        iPosition = 0;
    }

    /**
     *
     * @param iChars
     */
    public LBinLine(char[] iChars) {
        this.iChars    = iChars;
        this.iPosition = 0;
    }

    /**
     *
     * @param iLine
     */
    public LBinLine(String iLine) {
        this(iLine.length() );
        iLine.getChars(0, iLine.length(), iChars, 0);
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Resets the position to the first id
     */
    public void reset(){
        iPosition = 0;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public int getPosition() {
        return iPosition;
    }

    /**
     *
     * @return
     */
    public int getLength() {
        return iChars.length;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Append a string
     *
     * @param iString The text to append
     * @return the new position
     */
    public int append(String iString) {

        if( iString == null ) return iPosition;

        for(int i = 0; i < iString.length(); i++){
            iChars[iPosition] = iString.charAt(i);

            iPosition++ ;
        }
        return iPosition;
    }


    /**
     * Append a string
     *
     * @param iString The text to append
     * @param iLength The lengt of the field
     * @return the new position
     */
    public int append(String iString, int iLength, char iBlank) {

        if( iString == null ) iString = "";

        // Fill the string with blanks
        for(int i = iString.length(); i < iLength; i++ ){
            iString = iBlank + iString;
        }

        for(int i = 0; i < iLength; i++){
            iChars[iPosition] = iString.charAt(i);

            iPosition++ ;
        }

        return iPosition;
    }


    /**
     * Append a string
     *
     * @param iLength
     * @return the new position
     */
    public int append(String iString, int iLength) {
        return append(iString, iLength, ' ');
    }


    /**
     * Append a date
     *
     * @param iDate
     * @param iLength
     * @return the new position
     */
    public int append(Date iDate, int iLength, String iFormat) {
        DateFormat iDateFormat = new SimpleDateFormat(iFormat);

        return append( iDateFormat.format(iDate), iLength, ' ' );
    }

    /**
     *
     * @param iValue
     * @param iLength
     */
    public int append(BigDecimal iValue, int iLength) {
        String iText = Long.toString( Math.round(iValue.doubleValue() * 100) );

        return append(iText, iLength, '0');
    }

    /**
     *
     * @param iValue
     * @param iLength
     */
    public int append(int iValue, int iLength) {
        String iText = Integer.toString(iValue);

        return append(iText, iLength, '0');
    }



    // Read



    /**
     *
     * @param iStart
     * @return
     */
    public String readString(int iStart){
        return "" + iChars[iStart - 1];

    }

    /**
     *
     * @param iStart
     * @param iEnd
     * @return
     */
    public String readString(int iStart, int iEnd){
        String iField = "";

        for(int i = iStart; i <= iEnd; i++){
            if(i - 1 < iChars.length) iField = iField + iChars[i - 1];
        }
        return iField.trim();
    }

    /**
     *
     * @param iStart
     * @param iEnd
     * @param iFormat
     * @return
     */
    public Date readDate(int iStart, int iEnd, String iFormat){
        DateFormat iDateFormat = new SimpleDateFormat(iFormat);

        String iValue = readString(iStart, iEnd);

        try {
            return iDateFormat.parse(iValue);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     *
     * @param iStart
     * @param iEnd
     * @return
     */
    public BigDecimal readBigDecimal(int iStart, int iEnd){
        String iText = readString(iStart, iEnd);

        try {
            Long iValue = Long.valueOf(iText, 10);

            return new BigDecimal(iValue / 100.0);
        } catch (Exception e) {
            return new BigDecimal(0);
        }

    }



    /**
     *
     * @param iStart
     * @param iEnd
     * @return
     */
    public Integer readInteger(int iStart, int iEnd){
        String iValue = readString(iStart, iEnd);

        try {
            return new Integer(iValue);
        } catch (Exception e) {
            return 0;
        }

    }









    /**
     *
     * @return
     */
    public String toString() {
        return new String(iChars);
    }





}
