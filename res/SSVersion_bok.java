package se.swedsoft.bookkeeping;

/**
 * Date: 2006-mar-14
 * Time: 14:21:50
 */
public interface SSVersion {

    public static final String app_title   = "JFS Bokföring";

    public static final String app_package = "Bookkeeping";

    public static final String app_version = "2.0.5";

    public static final String app_build   = "2008-01-16";


    // Toggles demo version
    public static final boolean DemoVersion = false;

    // Togles if a isValid license is required
    public static final boolean LicenseRequired = true;

    // Toggles if the user can remove vouchers
    public static final boolean iAllowVoucherDeletion = false;
}