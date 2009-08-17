package se.swedsoft.bookkeeping.importexport.supplierpayments.poster;

import se.swedsoft.bookkeeping.importexport.supplierpayments.data.SupplierPayment;
import se.swedsoft.bookkeeping.importexport.supplierpayments.data.SupplierPaymentConfig;
import se.swedsoft.bookkeeping.importexport.supplierpayments.util.LBinLine;

import java.util.Date;

/**
 * User: Andreas Lago
 * Date: 2006-aug-30
 * Time: 14:18:56
 *
 * Öppningspost
 */
public class LBinPostTK13 extends LBinPost {


    private String iReferenceText;
    private String iValueText;

    /**
     *
     */
    public LBinPostTK13(){
        super();
        iReferenceText = "REFERENS                                             ";
        iValueText     = "BELOPP                                               ";
    }

    /**
     *
     * @param iLine
     */
    public void write(LBinLine iLine){
        iLine.append("13");
        iLine.append(iReferenceText   , 25 );  //  3 => 27: Rubrik för betalningspecifikation
        iLine.append(iValueText       , 12 );  // 28 => 39: Rubrik för belopp
        iLine.append(""               , 41 );  // 40 => 80: Blanka
    }

    /**
     *
     * @param iLine
     */
    public void read(LBinLine iLine){
        iReferenceText      = iLine.readString( 3, 27); //  3 => 27: Rubrik för betalningspecifikation
        iValueText          = iLine.readString(28, 39); // 28 => 39: Rubrik för belopp

    }
}
