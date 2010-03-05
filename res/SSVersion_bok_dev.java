package se.swedsoft.bookkeeping;

/**
 * Date: 2006-mar-14
 * Time: 14:21:50
 *
 * $Id$
 *
 */
public interface SSVersion {

    public static final String app_title   = "JFS Bokföring";

    public static final String app_package = "Bookkeeping";

    public static final String app_version = "2.1-dev-$Rev$";

    public static final String app_build   = "$Date$";

    // Toggles if the user can remove vouchers
    public static final boolean iAllowVoucherDeletion = false;
}