package se.swedsoft.bookkeeping.importexport.supplierpayments.poster;

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
public class LBinPostTK12 extends LBinPost {
    private String iText;
    private Date   iDate;

    /**
     *
     */
    public LBinPostTK12(){
        iText  = SupplierPaymentConfig.getMessage();
        iDate  = SupplierPaymentConfig.getMessageDate();

        if(iText != null) iText = iText  + "                                                 ";
    }


    /**
     * @return
     */
    @Override
    public boolean isEmpty() {
        return SupplierPaymentConfig.getMessage() == null || SupplierPaymentConfig.getMessage().length() == 0;
    }


    /**
     *
     * @param iLine
     */
    @Override
    public void write(LBinLine iLine){
        iLine.append("12");
        iLine.append(iText       , 50            );  //  3 => 52: Informationstext
        iLine.append(iDate       ,  6  , "yyMMdd");  // 53 => 58: Datum som anger hur länge informationen skall visas
        iLine.append(""          , 22            );  // 59 => 80: Blanka
    }

    /**
     *
     * @param iLine
     */
    @Override
    public void read(LBinLine iLine){
        iText        = iLine.readString( 3, 52          ); //  3 => 52: Informationstext
        iDate        = iLine.readDate  (53, 58, "yyMMdd"); // 53 => 58: Datum som anger hur länge informationen skall visas
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.importexport.supplierpayments.poster.LBinPostTK12");
        sb.append("{iDate=").append(iDate);
        sb.append(", iText='").append(iText).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
