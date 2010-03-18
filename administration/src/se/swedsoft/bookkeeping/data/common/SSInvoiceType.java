package se.swedsoft.bookkeeping.data.common;

import se.swedsoft.bookkeeping.gui.util.SSBundle;

/**
 * User: Andreas Lago
 * Date: 2006-apr-06
 * Time: 09:52:04
 */
public enum SSInvoiceType {
    NORMAL("invoiceframe.typename.1"),
    CASH  ("invoiceframe.typename.2")
    ;


    // Constant for serialization versioning.
    static final long serialVersionUID = 1L;


    private String iResouceName;

    /**
     *
     * @param iResouceName
     */
    SSInvoiceType(String iResouceName) {
       this.iResouceName = iResouceName;
   }

    /**
     *
     * @return
     */
    public String getResouceName() {
        return iResouceName;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return SSBundle.getBundle().getString(iResouceName);
    }

}
