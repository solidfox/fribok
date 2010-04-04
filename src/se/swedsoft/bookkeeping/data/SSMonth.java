package se.swedsoft.bookkeeping.data;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

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
     * @param pTo
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
        return pFrom.compareTo(iFrom) <= 0 && pTo.compareTo(iFrom) >= 0;
    }

    
    public int hashCode() {
        if(iFrom == null) return super.hashCode();

        cCalendar.setTime(iFrom);

        return  cCalendar.get(Calendar.YEAR) * 12 + cCalendar.get(Calendar.MONTH);
    }

    
    public boolean equals(Object obj) {
        if(obj instanceof SSMonth){
            return obj.hashCode() == hashCode();
        }
        return super.equals(obj);
    }

    
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
     * @param pYearData
     * @return
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
        iMonthDate.setTime(iFrom);

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
