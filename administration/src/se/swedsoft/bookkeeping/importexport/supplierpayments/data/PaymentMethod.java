package se.swedsoft.bookkeeping.importexport.supplierpayments.data;

import se.swedsoft.bookkeeping.gui.util.SSBundle;

/**
 * User: Andreas Lago
 * Date: 2006-aug-28
 * Time: 16:08:03
 */
public enum PaymentMethod {

    BANKGIRO ("supplierpaymentframe.method.1"),
    PLUSGIRO ("supplierpaymentframe.method.2"),
    CASH     ("supplierpaymentframe.method.3");

    private String iBundleName;

    /**
     *
     * @param iBundleName
     */
    PaymentMethod(String iBundleName) {
        this.iBundleName = iBundleName;
    }


    /**
     *
     * @return
     */
    public String getDescription(){
        return SSBundle.getBundle().getString(iBundleName);
    }

}
