package se.swedsoft.bookkeeping.calc.math;

import java.util.Calendar;
import java.util.Date;

/**
 * Date: 2006-mar-09
 * Time: 12:34:59
 */
public class SSDateMath {

    private static Calendar cCalendar = Calendar.getInstance();

    /**
     * Sets the time parts of a date to 00:00:00
     *
     * @param pDate
     * @return
     */
    public static Date floor(Date pDate){
        cCalendar.setTime(pDate);
        cCalendar.set(Calendar.HOUR_OF_DAY, cCalendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        cCalendar.set(Calendar.MINUTE     , cCalendar.getActualMinimum(Calendar.MINUTE));
        cCalendar.set(Calendar.SECOND     , cCalendar.getActualMinimum(Calendar.SECOND));
        cCalendar.set(Calendar.MILLISECOND, cCalendar.getActualMinimum(Calendar.MILLISECOND));

        return cCalendar.getTime();
    }

    /**
     * Sets the time parts of a date to 23:59:59
     *
     * @param pDate
     * @return the
     */
    public static Date ceil(Date pDate){
        cCalendar.setTime(pDate);
        cCalendar.set(Calendar.HOUR_OF_DAY, cCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        cCalendar.set(Calendar.MINUTE     , cCalendar.getActualMaximum(Calendar.MINUTE));
        cCalendar.set(Calendar.SECOND     , cCalendar.getActualMaximum(Calendar.SECOND));
        cCalendar.set(Calendar.MILLISECOND, cCalendar.getActualMaximum(Calendar.MILLISECOND));

        return cCalendar.getTime();
    }

    /**
     *
     * @param pDate
     * @return
     */
    public static Date getFirstDayInMonth(Date pDate) {
        cCalendar.setTime(pDate);

        cCalendar.set(Calendar.DAY_OF_MONTH, cCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        cCalendar.set(Calendar.HOUR_OF_DAY , cCalendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        cCalendar.set(Calendar.MINUTE      , cCalendar.getActualMinimum(Calendar.MINUTE));
        cCalendar.set(Calendar.SECOND      , cCalendar.getActualMinimum(Calendar.SECOND));
        cCalendar.set(Calendar.MILLISECOND , cCalendar.getActualMinimum(Calendar.MILLISECOND));

        return cCalendar.getTime();

    }

    /**
     *
     * @param pDate
     * @return
     */
    public static Date getLastDayMonth(Date pDate) {
        cCalendar.setTime(pDate);

        cCalendar.set(Calendar.DAY_OF_MONTH, cCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        cCalendar.set(Calendar.HOUR_OF_DAY, cCalendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        cCalendar.set(Calendar.MINUTE     , cCalendar.getActualMaximum(Calendar.MINUTE));
        cCalendar.set(Calendar.SECOND     , cCalendar.getActualMaximum(Calendar.SECOND));
        cCalendar.set(Calendar.MILLISECOND, cCalendar.getActualMaximum(Calendar.MILLISECOND));

        return cCalendar.getTime();
    }

    /**
     *
     * @param iFrom
     * @param iTo
     * @return
     */
    public static int getMonthsBetween(Date iFrom, Date iTo) {
        Calendar iCalendar = Calendar.getInstance();

        if( iFrom.before(iTo) ) return 0;

        iCalendar.setTime(iFrom);

        int iMonths = 0;
        while( iCalendar.getTime().before(iTo) ) {
            iCalendar.add(Calendar.MONTH, 1);
            iMonths++;
        }
        return iMonths;
    }

    /**
     *
     * @param iFrom
     * @param iTo
     * @return
     */
    public static int getDaysBetween(Date iFrom, Date iTo) {
        Calendar iCalendar = Calendar.getInstance();

        if( iTo.before(iFrom) ) return 0;

        iCalendar.setTime(iFrom);

        int iDays = 0;
        while( iCalendar.getTime().before(iTo) ) {
            iCalendar.add(Calendar.DAY_OF_MONTH, 1);
            iDays++;
        }
        return iDays;
    }




    /**
     * Add the specified number of months to the specified date 
     *
     * @param iDate
     * @param iCount
     * @return the new date
     */
    public static Date addMonths(Date iDate, Integer iCount) {
        Calendar iCalendar = Calendar.getInstance();

        iCalendar.setTime(iDate);

        iCalendar.add(Calendar.MONTH, iCount);

        return iCalendar.getTime();
    }
}
