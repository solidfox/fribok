package se.swedsoft.bookkeeping;

public interface SSVersion {

    public static final String app_title   = "JFS Fakturering";

    public static final String app_package = "Fakturering";

    public static final String app_version = "1.1a1";

    public static final String app_build   = "2009-08-17";


    // Togles if a isValid license is required
    public static final boolean LicenseRequired = false;

    // Toggles if the user can remove vouchers
    public static final boolean iAllowVoucherDeletion = false;
}