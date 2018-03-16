package se.swedsoft.bookkeeping.importexport.supplierpayments.poster;


import se.swedsoft.bookkeeping.importexport.supplierpayments.data.SupplierPayment;
import se.swedsoft.bookkeeping.importexport.supplierpayments.util.LBinLine;

import java.math.BigDecimal;
import java.util.Date;


/**
 * Kontoinsättning
 */
public class LBinPostTK40 extends LBinPost {

    private String     iNumber;
    private String     iClearing;
    private String     iKonto;
    private String     iReference;
    private String     iLon;

    public LBinPostTK40() {}

    /**
     *
     * @param iPayment
     */
    public LBinPostTK40(SupplierPayment iPayment) {
        this.iNumber = iNumber.replaceAll("-", "");
        iKonto = iPayment.getKonto().replaceAll("-", "").replaceAll(" ", "").substring(4);
        //iClearing = iPayment.getClearing().replaceAll("-", "").replaceAll(" ", "");
	iClearing = iPayment.getKonto().replaceAll("-", "").replaceAll(" ", "").substring(0, 4);
        iReference = iPayment.getReference();
        iLon = " "; //iPayment.getLon();
    }

    /**
     *
     * @param iLine
     */
    @Override
    public void write(LBinLine iLine) {
        iLine.append("40");
        iLine.append('0', 4); // 3 => 6  : Reservfält
        iLine.append(iNumber, 6, '0'); // 7 => 12 : Utbetalningsnummer
        iLine.append(iClearing, 4); // 13 => 16  : Clearingnummer
        iLine.append(iKonto, 12, '0'); // 17 => 28  : Kontonummer
        iLine.append(iReference, 12); // 29 => 40: Referens
        iLine.append(iLon, 1); // 41 => 41: Kod för lön
        iLine.append("", 39); // 42 => 80: Reservfält Blanka

    }

    /**
     *
     * @param iLine
     */
    @Override
    public void read(LBinLine iLine) {
        iNumber = iLine.readString(7, 12); // 7 => 12  : Utbetalningsnummer
        iClearing = iLine.readString(13, 16); // 13 => 16  : Clearingnummer
        iKonto = iLine.readString(17, 28); // 17 => 28  : Kontonummer
        iReference = iLine.readString(29, 40); // 29 => 40: Referens
        iLon = iLine.readString(41, 41); // 41 => 41: Lön
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
    public String getClearing() {
        return iClearing;
    }

    /**
     *
     * @param iClearing
     */
    public void setClearing(String iClearing) {
        this.iClearing = iClearing;
    }

    /**
     *
     * @return
     */
    public String getKonto() {
        return iKonto;
    }

    /**
     *
     * @param iKonto
     */
    public void setKonto(String iKonto) {
        this.iKonto = iKonto;
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
    public String getLon() {
        return iLon;
    }

    /**
     *
     * @param iLon
     */
    public void setLon(String iLon) {
        this.iLon = iLon;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(
                "se.swedsoft.bookkeeping.importexport.supplierpayments.poster.LBinPostTK40");
        sb.append(", iNumber='").append(iNumber).append('\'');
        sb.append(", iClearing=").append(iClearing).append('\'');
        sb.append(", iKonto='").append(iKonto).append('\'');
        sb.append(", iReference='").append(iReference).append('\'');
        sb.append(", iLon=").append(iLon);
        sb.append('}');
        return sb.toString();
    }
}
