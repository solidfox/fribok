package se.swedsoft.bookkeeping.importexport.supplierpayments.data;

import java.util.Date;

/**
 * User: Andreas Lago
 * Date: 2006-sep-04
 * Time: 14:49:28
 */
public class SupplierPaymentConfig {

    // Avsändarens (vårt) bankgironummer
    private static String iOurBankGiroAccount;

    private static String iMessage;

    private static Date iMessageDate;

    /////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     *
     * @return
     */
    public static String getOurBankGiroAccount() {
        return iOurBankGiroAccount;
    }

    /**
     *
     * @param iOurBankGiroAccount
     */
    public static void setOurBankGiroAccount(String iOurBankGiroAccount) {
        SupplierPaymentConfig.iOurBankGiroAccount = iOurBankGiroAccount;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public static String getMessage() {
        return iMessage;
    }

    /**
     *
     * @param iMessage
     */
    public static void setMessage(String iMessage) {
        SupplierPaymentConfig.iMessage = iMessage;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return
     */
    public static Date getMessageDate() {
        return iMessageDate;
    }

    /**
     *
     * @param iMessageDate
     */
    public static void setMessageDate(Date iMessageDate) {
        SupplierPaymentConfig.iMessageDate = iMessageDate;
    }
}
