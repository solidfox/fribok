package se.swedsoft.bookkeeping;

public interface SSVersion {

    String app_title   = "JFS Administration";

    String app_package = "Administration";

    String app_version = "2.1-dev-$Rev$";

    String app_build   = "$Date$";

    // Toggles if the user can remove vouchers
    boolean iAllowVoucherDeletion = false;
}