package se.swedsoft.bookkeeping.importexport.supplierpayments.poster;

import se.swedsoft.bookkeeping.importexport.supplierpayments.data.SupplierPayment;
import se.swedsoft.bookkeeping.importexport.supplierpayments.util.LBinLine;

import java.math.BigDecimal;
import java.util.Date;

/**
 * User: Andreas Lago
 * Date: 2006-sep-04
 * Time: 11:04:23
 *
 * Betalningspost
 */
public class LBinPostTK14 extends LBinPost {

    private String     iNumber;
    private String     iReference;
    private BigDecimal iValue    ;
    private Date       iDate     ;
    private Integer    iInvoiceNr;

    /**
     *
     */
    public LBinPostTK14() {
    }

    /**
     *
     */
    public LBinPostTK14(SupplierPayment iPayment, String iNumber) {
        super();
        this.iNumber    = iNumber.replaceAll("-", "");
        this.iReference = iPayment.getReference();
        this.iValue     = iPayment.getValue();
        this.iDate      = iPayment.getDate();
        this.iInvoiceNr = iPayment.getNumber();
    }

    /**
     *
     * @param iLine
     */
    @Override
    public void write(LBinLine iLine){
        iLine.append("14");
        iLine.append( iNumber   , 10, '0'     ); // 3 => 12 : Postgiro eller utbet nr
        iLine.append( iReference, 25          ); // 13 => 37: Referens
        iLine.append( iValue    , 12          ); // 38 => 49: Belopp
        iLine.append( iDate     , 6, "yyMMdd" ); // 50 => 55: Betalningsdatum
        iLine.append( ""        , 5           ); // 56 => 60: Blanka
        iLine.append( iInvoiceNr, 20          ); // 61 => 80: Användarinformation
    }

    /**
     *
     * @param iLine
     */
    @Override
    public void read(LBinLine iLine){
        iNumber    = iLine.readString    (3 , 12);              // 3 => 12 : Postgiro eller utbet nr
        iReference = iLine.readString    (13, 37);              // 13 => 37: Referens
        iValue     = iLine.readBigDecimal(38, 49);              // 38 => 49: Belopp
        iInvoiceNr = iLine.readInteger   (61, 80);              // 61 => 80: Användarinformation

      //  iDate      = iLine.readDate      (50, 55, "yyMMdd");    // 50 => 55: Betalningsdatum

    }

    /**
     *
     * @return
     */
    public String getNumber() {
        return iNumber;
    }

    /**
     *
     * @param iNumber
     */
    public void setNumber(String iNumber) {
        this.iNumber = iNumber;
    }

    /**
     *
     * @return
     */
    public String getReference() {
        return iReference;
    }

    /**
     *
     * @param iReference
     */
    public void setReference(String iReference) {
        this.iReference = iReference;
    }

    /**
     *
     * @return
     */
    public BigDecimal getValue() {
        return iValue;
    }

    /**
     *
     * @param iValue
     */
    public void setValue(BigDecimal iValue) {
        this.iValue = iValue;
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return iDate;
    }

    /**
     *
     * @param iDate
     */
    public void setDate(Date iDate) {
        this.iDate = iDate;
    }

    /**
     *
     * @return
     */
    public Integer getInvoiceNr() {
        return iInvoiceNr;
    }

    /**
     *
     * @param iInvoiceNr
     */
    public void setInvoiceNr(Integer iInvoiceNr) {
        this.iInvoiceNr = iInvoiceNr;
    }

}
