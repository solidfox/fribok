package se.swedsoft.bookkeeping.importexport.bgmax.data;

import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-aug-23
 * Time: 11:21:49
 */
public class BgMaxAvsnitt {

    public String iBankgiroNummer;

    public String iPlusgiroNummer;

    //public String iValuta;

    public String iBankKontoNummer;
    public String iBetalningsdag;
    public String iLopnummer;
    public String iBelopp;
    public String iValuta;
    public String iAntal;
    public String iTyp;



    public List<BgMaxBetalning> iBetalningar;

    /**
     *
     */
    public BgMaxAvsnitt() {
        iBetalningar = new LinkedList<BgMaxBetalning>();
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
        sb.append("  Avsnitt:\n");
        sb.append("  {\n");
        sb.append("    iBankgiroNummer  : ").append(iBankgiroNummer).append('\n');
        sb.append("    iPlusgiroNummer  : ").append(iPlusgiroNummer).append('\n');
        sb.append("    iValuta          : ").append(iValuta        ).append('\n');

        for (BgMaxBetalning iBetalning : iBetalningar) {
            sb.append(iBetalning);
        }

        sb.append("    iBankKontoNummer : ").append(iBankKontoNummer).append('\n');
        sb.append("    iBetalningsdag   : ").append(iBetalningsdag  ).append('\n');
        sb.append("    iLopnummer       : ").append(iLopnummer      ).append('\n');
        sb.append("    iBelopp          : ").append(iBelopp         ).append('\n');
        sb.append("    iValuta          : ").append(iValuta         ).append('\n');
        sb.append("    iAntal           : ").append(iAntal          ).append('\n');
        sb.append("    iTyp             : ").append(iTyp            ).append('\n');
        sb.append("  }\n");

        return sb.toString();
    }

}


