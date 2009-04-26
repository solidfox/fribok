package se.swedsoft.bookkeeping.importexport.supplierpayments.poster;

import se.swedsoft.bookkeeping.importexport.supplierpayments.data.SupplierPaymentConfig;
import se.swedsoft.bookkeeping.importexport.supplierpayments.util.LBinLine;

import java.math.BigDecimal;

/**
 * User: Andreas Lago
 * Date: 2006-sep-04
 * Time: 15:24:40
 */
public class LBinPostTK29 extends LBinPost {

    private String     iBankGiroNr;
    private int        iSize;
    private BigDecimal iSum;

    /**
     *
     */
    public LBinPostTK29() {
    }

    /**
     *
     * @param iSize
     * @param iSum
     */
    public LBinPostTK29(int iSize, BigDecimal iSum) {
        super();

        this.iBankGiroNr = SupplierPaymentConfig.getOurBankGiroAccount().replaceAll("-", "");
        this.iSize       = iSize;
        this.iSum        = iSum;
    }



    /**
     *
     * @param iLine
     */
    public void write(LBinLine iLine){
        iLine.append("29");
        iLine.append( iBankGiroNr           , 10, '0'      ); // 3 => 12 : Avsändarens bankgiro nr
        iLine.append( iSize                 ,  8           ); // 13 => 20: Antal poster
        iLine.append( iSum                  ,  12          ); // 21 => 32: Totalbelopp
        iLine.append( " "                   ,  1           ); // 33      : Minustecken om negativt
        iLine.append( " "                   ,  47          ); // 34 => 80: Blanka
    }

    /**
     *
     * @param iLine
     */
    public void read(LBinLine iLine){
        iBankGiroNr = iLine.readString    (3 , 12); // 3 => 12 : Avsändarens bankgiro nr
        iSize       = iLine.readInteger   (13, 20); // 13 => 20: Antal poster
        iSum        = iLine.readBigDecimal(21, 32); // 21 => 32: Totalbelopp
    }

}
