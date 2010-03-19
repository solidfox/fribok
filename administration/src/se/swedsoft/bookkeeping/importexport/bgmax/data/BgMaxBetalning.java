package se.swedsoft.bookkeeping.importexport.bgmax.data;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-aug-23
 * Time: 12:03:14
 */
public class BgMaxBetalning {
    public BgMaxAvsnitt iAvsnitt;

    public String iBankgiroNummer;
    public String iReferens;
    public String iBelopp;
    public String iReferensKod;
    public String iBetalningsKanalKod;
    public String iBGCLopnummer;
    public String iAvibildmarkering;


    public List<BgMaxReferens> iReferenser;

    public String iInformationsText;

    public String  iBetalarensNamn;
    public String  iExtraNamnfalt;
    public String  iBetalarensAdress;
    public String  iBetalarensPostnummer;
    public String  iBetalarensOrt;
    public String  iBetalarensLand;
    public String  iLandKod;
    public String  iBetalarensOrganisationsnr;



    /**
     *
     */
    public BgMaxBetalning() {
        iReferenser = new LinkedList<BgMaxReferens>();
    }

    /**
     *
     * @return
     */
    public BigDecimal getBelopp() {
        return new BigDecimal(iBelopp).scaleByPowerOfTen(-2);
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxBetalning");
        sb.append("{iAvibildmarkering='").append(iAvibildmarkering).append('\'');
        sb.append(", iAvsnitt=").append(iAvsnitt);
        sb.append(", iBankgiroNummer='").append(iBankgiroNummer).append('\'');
        sb.append(", iBelopp='").append(iBelopp).append('\'');
        sb.append(", iBetalarensAdress='").append(iBetalarensAdress).append('\'');
        sb.append(", iBetalarensLand='").append(iBetalarensLand).append('\'');
        sb.append(", iBetalarensNamn='").append(iBetalarensNamn).append('\'');
        sb.append(", iBetalarensOrganisationsnr='").append(iBetalarensOrganisationsnr).append('\'');
        sb.append(", iBetalarensOrt='").append(iBetalarensOrt).append('\'');
        sb.append(", iBetalarensPostnummer='").append(iBetalarensPostnummer).append('\'');
        sb.append(", iBetalningsKanalKod='").append(iBetalningsKanalKod).append('\'');
        sb.append(", iBGCLopnummer='").append(iBGCLopnummer).append('\'');
        sb.append(", iExtraNamnfalt='").append(iExtraNamnfalt).append('\'');
        sb.append(", iInformationsText='").append(iInformationsText).append('\'');
        sb.append(", iLandKod='").append(iLandKod).append('\'');
        sb.append(", iReferens='").append(iReferens).append('\'');
        sb.append(", iReferenser=").append(iReferenser);
        sb.append(", iReferensKod='").append(iReferensKod).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
