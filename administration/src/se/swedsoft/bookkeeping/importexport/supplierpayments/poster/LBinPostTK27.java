package se.swedsoft.bookkeeping.importexport.supplierpayments.poster;

import se.swedsoft.bookkeeping.data.SSAddress;
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
public class LBinPostTK27 extends LBinPost {

    private Integer iOutpaymentNumber;
    private String  iAddress1;
    private String  iZipCode;
    private String  iCity;

    public LBinPostTK27() {
    }

    /**
     *
     */
    public LBinPostTK27(SupplierPayment iPayment){
        super();
        SSAddress  iAddress = iPayment.getAddress();

        iOutpaymentNumber = iPayment.getOutpaymentNumber();
        iAddress1         = iAddress.getAddress1();
        iZipCode          = iAddress.getZipCode();
        iCity             = iAddress.getCity();

        if(iAddress1 == null || iZipCode == null || iCity == null) {
            throw new SSExportException(SSBundle.getBundle(), "supplierpaymentframe.error.supplieraddress", iPayment.getSupplier().getName() );
        }
        iZipCode = iZipCode.replace(" ", "");
    }


    /**
     *
     * @param iLine
     */
    @Override
    public void write(LBinLine iLine){
        iLine.append("27");
        iLine.append("0000"                 , 4, '0' ); // 3 ==> 6: Text
        iLine.append(iOutpaymentNumber      , 5      ); // 7 ==> 11: Utbetalningsnummer
        iLine.append(" "                             ); // 12      : Checksiffra
        iLine.append(iAddress1.toUpperCase(), 35     ); // 13 ==> 47: Mottagarens gatuaddress i versaler
        iLine.append(iZipCode .toUpperCase(), 5      ); // 48 ==> 52: Mottagarens postnmmer
        iLine.append(iCity    .toUpperCase(), 20     ); // 53 => 72: Mottagarens stad
        iLine.append(""                     , 8      ); // 73 => 80: Blanka
    }

    /**
     *
     * @param iLine
     */
    @Override
    public void read(LBinLine iLine){
        iOutpaymentNumber = iLine.readInteger(3 , 11); // 7 ==> 11: Utbetalningsnummer
        iAddress1         = iLine.readString (13, 47); // 13 ==> 47: Mottagarens gatuaddress i versaler
        iZipCode          = iLine.readString (48, 52); // 48 ==> 52: Mottagarens postnmmer
        iCity             = iLine.readString (53, 72); // 53 => 72: Mottagarens stad

    }
}
