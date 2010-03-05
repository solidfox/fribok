package se.swedsoft.bookkeeping;

public interface SSVersion {

    public static final String app_title   = "JFS Fakturering";

    public static final String app_package = "Fakturering";

    public static final String app_version = "1.1-dev-$Rev$";

    public static final String app_build   = "$Date$";

    // Toggles if the user can remove vouchers
    public static final boolean iAllowVoucherDeletion = false;
}