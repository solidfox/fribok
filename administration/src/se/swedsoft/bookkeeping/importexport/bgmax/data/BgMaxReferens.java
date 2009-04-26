package se.swedsoft.bookkeeping.importexport.bgmax.data;

/**
 * User: Andreas Lago
 * Date: 2006-aug-24
 * Time: 10:28:30
 */
public class BgMaxReferens {
    
    public String iBankgiroNummer;
    public String iReferens;
    public String iBelopp;
    public String iReferensKod;
    public String iBetalningsKanalKod;
    public String iBGCLopnummer;
    public String iAvibildmarkering;

    /**
     *
     */
    public BgMaxReferens() {
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

        sb.append("    Referens:\n");
        sb.append("    {\n");
        sb.append("        iBankgiroNummer     : ").append(iBankgiroNummer).append("\n");
        sb.append("        iReferens           : ").append(iReferens).append("\n");
        sb.append("        iBelopp             : ").append(iBelopp).append("\n");
        sb.append("        iReferensKod        : ").append(iReferensKod).append("\n");
        sb.append("        iBetalningsKanalKod : ").append(iBetalningsKanalKod).append("\n");
        sb.append("        iBGCLopnummer       : ").append(iBGCLopnummer).append("\n");
        sb.append("        iBankgiroNummer     : ").append(iBankgiroNummer).append("\n");
        sb.append("        iAvibildmarkering   : ").append(iAvibildmarkering).append("\n");
        sb.append("    }\n");

        return sb.toString();
    }
}
