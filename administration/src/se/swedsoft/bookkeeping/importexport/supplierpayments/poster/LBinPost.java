package se.swedsoft.bookkeeping.importexport.supplierpayments.poster;

import se.swedsoft.bookkeeping.importexport.supplierpayments.util.LBinLine;
import se.swedsoft.bookkeeping.importexport.supplierpayments.data.SupplierPayment;

import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-aug-30
 * Time: 14:32:02
 */
public abstract class LBinPost {


    /**
     *
     */
    protected LBinPost() {
    }

    /**
     *
     * @return
     */
    public boolean isEmpty(){
        return false;
    }


    /**
     *
     * @param iLine
     */
    public abstract void write(LBinLine iLine);

    /**
     *
     * @param iLine
     */
    public abstract void read(LBinLine iLine);


}
