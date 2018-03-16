package se.swedsoft.bookkeeping.importexport.supplierpayments.poster;


import se.swedsoft.bookkeeping.importexport.supplierpayments.data.SupplierPayment;
import se.swedsoft.bookkeeping.importexport.supplierpayments.util.LBinLine;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Girering till Plusgironummer
 */
public class LBinPostTK54 extends LBinPost {

    private String     iPlusGiro;
    private String     iReference;
    private BigDecimal iValue;
    private Date       iDate;
    private Integer   iInvoiceNr;

    public LBinPostTK54() {}

    /**
     *
     * @param iPayment
     */
    public LBinPostTK54(SupplierPayment iPayment) {
        iPlusGiro = iPayment.getPlusGiro().replaceAll("-", "").replaceAll(" ", "");
        iReference = iPayment.getReference();
        iValue = iPayment.getValue();
        iDate = iPayment.getDate();
        iInvoiceNr = iPayment.getNumber();

    }

    /**
     *
     * @param iLine
     */
    @Override
    public void write(LBinLine iLine) {
        iLine.append("54");
        iLine.append(iPlusGiro, 10, '0'); // 3 => 12  : Plusgiro
        iLine.append(iReference, 25); // 13 => 37: Referens
        iLine.append(iValue, 12); // 38 => 49: Belopp
        iLine.append(iDate, 6, "yyMMdd"); // 50 => 55: Betalningsdatum
        iLine.append("", 5); // 56 => 60: Blanka
        iLine.append(iInvoiceNr, 20); // 61 => 80: Användarinformation
    }

    /**
     *
     * @param iLine
     */
    @Override
    public void read(LBinLine iLine) {
        iPlusGiro = iLine.readString(3, 12); // 3 => 12  : Plusgiro
        iReference = iLine.readString(13, 37); // 13 => 37: Referens
        iValue = iLine.readBigDecimal(38, 49); // 38 => 49: Belopp
        // iDate      = iLine.readDate      (50, 55, "yyMMdd"); // 50 => 55: Betalningsdatum
        iInvoiceNr = iLine.readInteger(61, 80); // 61 => 80: Användarinformation
    }

    /**
     *
     * @return
     */
    public String getPlusGiro() {
        return iPlusGiro;
    }

    /**
     *
     * @param iPlusGiro
     */
    public void setPlusGiro(String iPlusGiro) {
        this.iPlusGiro = iPlusGiro;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(
                "se.swedsoft.bookkeeping.importexport.supplierpayments.poster.LBinPostTK54");
        sb.append("{iDate=").append(iDate);
        sb.append(", iInvoiceNr=").append(iInvoiceNr);
        sb.append(", iPlusGiro='").append(iPlusGiro).append('\'');
        sb.append(", iReference='").append(iReference).append('\'');
        sb.append(", iValue=").append(iValue);
        sb.append('}');
        return sb.toString();
    }
}
