package se.swedsoft.bookkeeping.util;

/**
 * Small convenience methods.
 * 
 * @author jensli
 *
 * $Id$
 * 
 */
public class SSUtil
{
    private SSUtil() {} // Can not be instantiated
    
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0;
    }
    
    public static void verifyArgument(String message, boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException("Illegal arguments: " + message);
        }
    }
    
    public static void verifyNotNull(String message, Object... objs) {
        for (Object o : objs) {
            if (o == null) {
                throw new NullPointerException("Illegal null ref: " + message);
            }
        }
    }
    
    public static String convertNullToEmpty(String s) {
        return s == null ? "" : s ;
    }
    
    public static boolean isInRage(int i, int low, int high) {
        SSUtil.verifyArgument("low must be lower or equal to high", low <= high);
        return low <= i && i <= high;
    }
}
