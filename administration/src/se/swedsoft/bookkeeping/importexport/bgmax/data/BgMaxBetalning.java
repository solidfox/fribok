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


    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     *
     * @return a string representation of the object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    Betalning: \n");
        sb.append("    {\n");
        sb.append("      iBankgiroNummer      : ").append(iBankgiroNummer    ).append('\n');
        sb.append("      iReferens            : ").append(iReferens          ).append('\n');
        sb.append("      iBelopp              : ").append(iBelopp            ).append('\n');
        sb.append("      iReferensKod         : ").append(iReferensKod       ).append('\n');
        sb.append("      iBetalningsKanalKod  : ").append(iBetalningsKanalKod).append('\n');
        sb.append("      iBGCLopnummer        : ").append(iBGCLopnummer      ).append('\n');
        sb.append("      iBankgiroNummer      : ").append(iBankgiroNummer    ).append('\n');
        sb.append("      iAvibildmarkering    : ").append(iAvibildmarkering  ).append('\n');

        for (BgMaxReferens iReferens : iReferenser) {
            sb.append(iReferens);
        }
        sb.append("      iBetalarensNamn      : ").append(iBetalarensNamn     ).append('\n');
        sb.append("      iExtraNamnfalt       : ").append(iExtraNamnfalt      ).append('\n');
        sb.append("      iBetalarensAdress              : ").append(iBetalarensAdress             ).append('\n');
        sb.append("      iBetalarensPostnummer          : ").append(iBetalarensPostnummer         ).append('\n');
        sb.append("      iBetalarensOrt          : ").append(iBetalarensOrt         ).append('\n');
        sb.append("      iBetalarensLand                : ").append(iBetalarensLand               ).append('\n');
        sb.append("      iLandKod             : ").append(iLandKod            ).append('\n');
        sb.append("      iBetalarensOrganisationsnr : ").append(iBetalarensOrganisationsnr).append('\n');
        sb.append("    }\n");

        return sb.toString();
    }


}
