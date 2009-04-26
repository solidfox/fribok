package se.swedsoft.bookkeeping.data;

import se.swedsoft.bookkeeping.data.SSNewAccountingYear;

import java.io.Serializable;
import java.util.*;
import java.text.DateFormat;
import java.text.DateFormatSymbols;

/**
 * Date: 2006-jan-27
 * Time: 11:48:46
 */
public class SSMonth  implements Serializable {

    private static Calendar cCalendar = Calendar.getInstance();

    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    private Date iFrom;

    private Date iTo;

    /**
     *
     * @param pFrom
     */
    public SSMonth(Date pFrom){
        iFrom = pFrom;
        iTo   = null;
    }
    /**
     *
     * @param pFrom
     */
    public SSMonth(Date pFrom, Date pTo){
        iFrom = pFrom;
        iTo   = pTo;
    }

    /**
     *
     * @return The date
     */
    public Date getDate(){
        return iFrom;
    }

    /**
     *
     * @return The date
     */
    public Date getFrom(){
        return iFrom;
    }


    /**
     *
     * @return The date
     */
    public Date getTo(){
        return iTo;
    }

    /**
     *
     * @param pFrom
     * @param pTo
     * @return boolean
     */
    public boolean isBetween(Date pFrom, Date pTo){
        return (pFrom.compareTo(iFrom) <= 0 && pTo.compareTo(iFrom) >= 0);
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hashtables such as those provided by
     *
     * @return a hash code value for this object.
     */
    public int hashCode() {
        if(iFrom == null) return super.hashCode();

        cCalendar.setTime(iFrom);

        return  cCalendar.get(Calendar.YEAR) * 12 + cCalendar.get(Calendar.MONTH);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     * @see #hashCode()
     * @see java.util.Hashtable
     */
    public boolean equals(Object obj) {
        if(obj instanceof SSMonth){
            return ((SSMonth)obj).hashCode() == hashCode();
        }
        return super.equals(obj);
    }

    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     *
     * @return a string representation of the object.
     */
    public String toString() {
        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);

        return format.format(iFrom).substring(0,7);
    }

    /**
     * 
     * @return
     */
    public String getName() {
        Calendar iCalendar = Calendar.getInstance();

        iCalendar.setTime( iFrom );

        int iMonth = iCalendar.get(Calendar.MONTH);
        int iYear  = iCalendar.get(Calendar.YEAR);

        DateFormatSymbols iSymbols = new DateFormatSymbols();


        return iSymbols.getMonths()[iMonth] + ", " + iYear;
    }


    /**
     *  Breaks a year into it's months
     */
    public static List<SSMonth> splitYearIntoMonths(SSNewAccountingYear pYearData){
        Date iFrom = pYearData.getFrom();
        Date iTo   = pYearData.getTo  ();

        return splitYearIntoMonths(iFrom, iTo);
    }

    public boolean isDateInMonth(Date iDate) {
        Calendar iCheckDate = Calendar.getInstance();
        Calendar iMonthDate = Calendar.getInstance();

        iCheckDate.setTime(iDate);
        iMonthDate.setTime(getDate());

        return (iCheckDate.get(Calendar.MONTH) == iMonthDate.get(Calendar.MONTH)) && (iCheckDate.get(Calendar.YEAR) == iMonthDate.get(Calendar.YEAR));
    }
    /**
     *
     * Breaks a year into it's months
     *
     * @param iFrom
     * @param iTo
     *
     * @return List of months
     */
    public static List<SSMonth> splitYearIntoMonths(Date iFrom, Date iTo){
        List<SSMonth> iMonths = new LinkedList<SSMonth>();

        Calendar iCalendar = Calendar.getInstance();
        // Set the time
        iCalendar.setTime(iFrom);
        // Reset the date to the first day of the month
        iCalendar.set(Calendar.DAY_OF_MONTH, 1);

        // Loop throught all the months between the from date and the to date
        while(iCalendar.getTime().compareTo(iTo) < 0  ){
            Date mForm = iCalendar.getTime();

            iCalendar.set(Calendar.DAY_OF_MONTH, iCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) );

            Date mTo  = iCalendar.getTime();

            SSMonth iMonth = new SSMonth( mForm, mTo);

            iMonths.add(iMonth);

            iCalendar.set(Calendar.DAY_OF_MONTH, 1);
            iCalendar.add(Calendar.MONTH, 1);
        }
        return iMonths;
    }


}
