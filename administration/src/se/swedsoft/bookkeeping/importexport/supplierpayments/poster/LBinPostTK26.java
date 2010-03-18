package se.swedsoft.bookkeeping.importexport.supplierpayments.poster;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.importexport.supplierpayments.data.SupplierPayment;
import se.swedsoft.bookkeeping.importexport.supplierpayments.util.LBinLine;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;

/**
 * User: Andreas Lago
 * Date: 2006-aug-30
 * Time: 14:18:56
 *
 * Namn, kontantutbetalning,
 */
public class LBinPostTK26 extends LBinPost {

    private Integer  iOutpaymentNumber;
    private String   iName;

    /**
     *
     */
    public LBinPostTK26() {
    }

    /**
     *
     * @param iPayment
     */
    public LBinPostTK26(SupplierPayment iPayment){
        iOutpaymentNumber = iPayment.getOutpaymentNumber();
        iName             = iPayment.getAddress().getName();

        if(iName == null)
            throw new SSExportException(SSBundle.getBundle(), "supplierpaymentframe.error.supplieraddress", iPayment.getSupplier().getName() );
    }

    /**
     *
     * @param iLine
     */
    @Override
    public void write(LBinLine iLine){
        iLine.append("26");
        iLine.append("0000"             , 4, '0' ); // 3 ==> 6: Text
        iLine.append(iOutpaymentNumber  , 5      ); // 7 ==> 11: Utbetalningsnummer
        iLine.append(" "                         ); // 12      : Checksiffra
        iLine.append(iName.toUpperCase(), 35     ); // 13 ==> 47: Mottagarens namn i versaler
        iLine.append(""                 , 33     ); // 48 ==> 80: Extra namnfält
    }

    /**
     *
     * @param iLine
     */
    @Override
    public void read(LBinLine iLine){
        iOutpaymentNumber  = iLine.readInteger(3 , 12); // 3 ==> 12: Utbetalningsnummer
        iName              = iLine.readString (13, 47); // 13 ==> 47: Mottagarens namn i versaler

    }

}
