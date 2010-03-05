package se.swedsoft.bookkeeping.importexport.bgmax.data;

import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 * Class for storing the contents of a bgmax file read from disk, and responsible 
 * for parsing the text from a read file.
 * 
 * Has fields for data about the whole bgmax file, and stors data about the 
 * transactions in the file in the iAvsnitts collection.
 * 
 * User: Andreas Lago
 * Date: 2006-aug-23
 * Time: 09:09:20
 */
public class BgMaxFile {

    public String iLayoutnamn;
    public String iVersion;
    public String iTidsstampel;
    public String iTestmarkering;

    public String iAntalBetalningsPoster ;
    public String iAntalExtraReferensPoster;
    public String iAntalAvdragsPoster;
    public String iAntalInsattningsPoster;



    public List<BgMaxAvsnitt> iAvsnitts;

    /**
     *
     */
    public BgMaxFile() {
        iAvsnitts = new LinkedList<BgMaxAvsnitt>();
    }

    /**
     * The entry point method for the b 
     * Parses the lines of the BgMax-file, filling in the data in its own fields and
     * creating BgMaxAvsnitt object in the iAvsnitts collection with parsed data.
     *  
     * @param iLines The raw lines of a BgMax-file read from disk
     */
    public void parse(List<String> iLines) throws SSImportException{
        iAvsnitts = new LinkedList<BgMaxAvsnitt>();
        
        boolean isEmptyLineRead = false;

        if( iLines.size() < 1 || ! isValid(iLines.get(0)) ) {
             throw new SSImportException(SSBundle.getBundle(), "bgmaximport.error.invalidfile");
        }

        Iterator<String> itr = iLines.iterator();
        
        while (itr.hasNext()) {
        	
        	String iLine = itr.next();
        	
            try {
            	
            	if ( iLine.length() == 0) {
            		
            		// If an empty line is found, all the rest of the lines have
            		// to be empty too
            		if ( !isAllEmpty( itr ) ) {
            			// Throw exc to bee caught a few lines down
            			throw new RuntimeException( "Empty line found in other place than last in the file" );
            		}
            		
            		break;  // Exit success
            	}
            	
                BgMaxLine iBgMaxLine = new BgMaxLine(iLine);
                parseLine(iBgMaxLine);
            	
            } catch (RuntimeException exc) {
                exc.printStackTrace();

                throw new SSImportException(SSBundle.getBundle(), "bgmaximport.error.parseerror");
            }


        }
    }
    
    private static boolean isAllEmpty(Iterator<String> itr)
    {
    	if (itr == null) {
    		throw new IllegalArgumentException( "Method can not take null argument" );
    	}
    	
    	while ( itr.hasNext() ) {
    		if ( itr.next().length() != 0 ) return false;
    	}
    	
    	return true;
    }

    /**
     * Returns true if this is a valid bgmax file
     * @param iFirstLine
     * @return
     */
    private boolean isValid(String iFirstLine) throws RuntimeException {
        return iFirstLine.length() == 80 && iFirstLine.startsWith("01BGMAX");
    }


    /**
     * TransaktionsKod 1 - 2
     *
     * @param iLine
     */
    private void parseLine( BgMaxLine iLine ) {
        String iTransaktionsKod = iLine.getTransaktionsKod();

        BgMaxBetalning iBetalning;
        BgMaxAvsnitt   iAvsnitt;

        if( iTransaktionsKod.equals("05") ) {
            iAvsnitt = new BgMaxAvsnitt();

            iAvsnitts.add(iAvsnitt);
        } else {

            if(iAvsnitts.size() > 0){
                iAvsnitt = iAvsnitts.get( iAvsnitts.size()-1 );
            } else {
                iAvsnitt = null;
            }
        }

        if(iAvsnitt != null){

            if( iTransaktionsKod.equals("20") || iTransaktionsKod.equals("21") ) {
                iBetalning = new BgMaxBetalning();
                iBetalning.iAvsnitt = iAvsnitt;

                iAvsnitt.iBetalningar.add(iBetalning);
            } else {

                if( iAvsnitt.iBetalningar.size() > 0){
                    iBetalning = iAvsnitt.iBetalningar.get(  iAvsnitt.iBetalningar.size()-1 );
                } else {
                    iBetalning = null;
                }

            }
        } else {
            iBetalning = null;
        }

        // B
        if( iTransaktionsKod.equals("01") ) readStartPost       (iLine, this); else
        if( iTransaktionsKod.equals("70") ) readSlutPost        (iLine, this); else

        if( iTransaktionsKod.equals("05") ) readOppningsPost    (iLine, iAvsnitt); else
        if( iTransaktionsKod.equals("15") ) readInsattningsPost (iLine, iAvsnitt); else

        if( iTransaktionsKod.equals("20") ) readBetalningsPost   (iLine, iBetalning); else
        if( iTransaktionsKod.equals("21") ) readAvdragsPost      (iLine, iBetalning); else
        if( iTransaktionsKod.equals("22") ) readExtraReferensPost(iLine, iBetalning, false); else
        if( iTransaktionsKod.equals("23") ) readExtraReferensPost(iLine, iBetalning, true ); else
        if( iTransaktionsKod.equals("25") ) readInformationsPost (iLine, iBetalning); else
        if( iTransaktionsKod.equals("26") ) readNamnPost         (iLine, iBetalning); else
        if( iTransaktionsKod.equals("27") ) readAddressPost1     (iLine, iBetalning); else
        if( iTransaktionsKod.equals("28") ) readAddressPost2     (iLine, iBetalning); else
        if( iTransaktionsKod.equals("29") ) readOrgnummerPost    (iLine, iBetalning); else
            System.out.println("No reader for: " + iTransaktionsKod);
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
        sb.append("BgMaxFil: {\n");
        sb.append("  Layoutnamn    : ").append(iLayoutnamn   ).append("\n");
        sb.append("  Version       : ").append(iVersion      ).append("\n");
        sb.append("  Tidsstämpel   : ").append(iTidsstampel  ).append("\n");
        sb.append("  Testmarkering : ").append(iTestmarkering).append("\n");

        for (BgMaxAvsnitt iCurrent : iAvsnitts) {
            sb.append(iCurrent);
        }

        sb.append("  iAntalBetalningsPoster    : ").append(iAntalBetalningsPoster   ).append("\n");
        sb.append("  iAntalAvdragsPoster       : ").append(iAntalAvdragsPoster      ).append("\n");
        sb.append("  iAntalExtraReferensPoster : ").append(iAntalExtraReferensPoster).append("\n");
        sb.append("  iAntalInsattningsPoster   : ").append(iAntalInsattningsPoster  ).append("\n");
        sb.append("}\n");

        return sb.toString();
    }



    /**
     * 1.3 Start post (01)
     *
     *
     * @param iLine
     */
    public static void readStartPost(BgMaxLine iLine, BgMaxFile iFile){
        iFile.iLayoutnamn     = iLine.getField( 3, 22);
        iFile.iVersion        = iLine.getField(23, 24);
        iFile.iTidsstampel   =  iLine.getField(25, 44);
        iFile.iTestmarkering =  iLine.getField(45);
    }

    /**
     * 1.14 Slutpost (70)
     *
     * @param iLine
     */
    private static void readSlutPost(BgMaxLine iLine, BgMaxFile iFile) {
        iFile.iAntalBetalningsPoster    = iLine.getField(3, 10);
        iFile.iAntalExtraReferensPoster = iLine.getField(19, 26);
        iFile.iAntalAvdragsPoster       = iLine.getField(11, 18);
        iFile.iAntalInsattningsPoster   = iLine.getField(27, 34);
    }


    /**
     * 1.4 Öppningsport (05)
     *
     * @param iLine
     */
    public static void readOppningsPost(BgMaxLine iLine, BgMaxAvsnitt  iAvsnitt){
        iAvsnitt.iPlusgiroNummer = iLine.getField(13, 22);
        iAvsnitt.iBankgiroNummer = iLine.getField( 3, 12);
        iAvsnitt.iValuta         = iLine.getField(23, 25);
    }

    /**
     * 1.13 Insättningspost (15)
     *
     * @param iLine
     */
    private static  void readInsattningsPost(BgMaxLine iLine, BgMaxAvsnitt iAvsnitt) {
        iAvsnitt.iBankKontoNummer = iLine.getField(3, 37);
        iAvsnitt.iBetalningsdag   = iLine.getField(38, 45);
        iAvsnitt.iLopnummer       = iLine.getField(46, 50);
        iAvsnitt.iBelopp          = iLine.getField(51, 65);
        iAvsnitt.iValuta          = iLine.getField(69, 71);
        iAvsnitt.iAntal           = iLine.getField(72, 79);
        iAvsnitt.iTyp             = iLine.getField(80);
    }








    /**
     * 1.5 Betalningspost (20)
     *
     * @param iLine
     */
    public static void readBetalningsPost(BgMaxLine iLine, BgMaxBetalning iBetalning){
        iBetalning.iBankgiroNummer     = iLine.getField( 3, 12);
        iBetalning.iReferens           = iLine.getField(13, 37);
        iBetalning.iBelopp             = iLine.getField(38, 55);
        iBetalning.iReferensKod        = iLine.getField(56);
        iBetalning.iBetalningsKanalKod = iLine.getField(57);
        iBetalning.iBGCLopnummer       = iLine.getField(58, 69);
        iBetalning.iAvibildmarkering   = iLine.getField(70);
    }

    /**
     * 1.6 Avdragspost (21)
     *
     * @param iLine
     */
    public static void readAvdragsPost(BgMaxLine iLine, BgMaxBetalning iBetalning){
        iBetalning.iBankgiroNummer     = iLine.getField( 3, 12);
        iBetalning.iReferens           = iLine.getField(13, 37);
        iBetalning.iBelopp             = "-" + iLine.getField(38, 55);
        iBetalning.iReferensKod        = iLine.getField(56);
        iBetalning.iBetalningsKanalKod = iLine.getField(57);
        iBetalning.iBGCLopnummer       = iLine.getField(58, 69);
        iBetalning.iAvibildmarkering   = iLine.getField(70);
    }



    /**
     * 1.7 Extra referensnummerpost (22 / 23)
     *
     * @param iLine
     * @param iNegative
     */
    private static void readExtraReferensPost(BgMaxLine iLine, BgMaxBetalning iBetalning, boolean iNegative) {
        String iBankgiroNummer     = iLine.getField( 3, 12);
        String iReferens           = iLine.getField(13, 37);
        String iBelopp             = iLine.getField(38, 55);
        String iReferensKod        = iLine.getField(56);
        String iBetalningsKanalKod = iLine.getField(57);
        String iBGCLopnummer       = iLine.getField(58, 69);
        String iAvibildmarkering   = iLine.getField(70);

        BgMaxReferens iBgMaxReferens = new BgMaxReferens();
        iBgMaxReferens.iBankgiroNummer     = iBankgiroNummer;
        iBgMaxReferens.iReferens           = iReferens;
        iBgMaxReferens.iBelopp             = iBelopp;
        iBgMaxReferens.iReferensKod        = iReferensKod;
        iBgMaxReferens.iBetalningsKanalKod = iBetalningsKanalKod;
        iBgMaxReferens.iBGCLopnummer       = iBGCLopnummer;
        iBgMaxReferens.iAvibildmarkering   = iAvibildmarkering;

        iBetalning.iReferenser.add(iBgMaxReferens);

    }

    /**
     * 1.8 Informationspost
     *
     * @param iLine
     */
    private static void readInformationsPost(BgMaxLine iLine, BgMaxBetalning iBetalning) {
        String iInformationsText = iLine.getField(3, 52);

        iBetalning.iInformationsText = iBetalning.iInformationsText == null ? iInformationsText : iBetalning.iInformationsText + iInformationsText;
    }

    /**
     * 1.9 Namnpost (26)
     *
     * @param iLine
     */
    private static void readNamnPost(BgMaxLine iLine, BgMaxBetalning iBetalning) {
        iBetalning.iBetalarensNamn = iLine.getField( 3, 27);
        iBetalning. iExtraNamnfalt  = iLine.getField(38, 72);
    }

    /**
     * 1.10 Adresspost 1 (27)
     *
     * @param iLine
     */
    private static void readAddressPost1(BgMaxLine iLine, BgMaxBetalning iBetalning){
        iBetalning.iBetalarensAdress     = iLine.getField(3, 37);
        iBetalning.iBetalarensPostnummer = iLine.getField(38, 46);
    }

    /**
     * 1.11 Adresspost 1 (28)
     *
     * @param iLine
     */
    private static void readAddressPost2(BgMaxLine iLine, BgMaxBetalning iBetalning){
        iBetalning. iBetalarensOrt = iLine.getField(3, 37);
        iBetalning. iBetalarensLand       = iLine.getField(38, 72);
        iBetalning. iLandKod    = iLine.getField(73, 74);
    }

    /**
     * 1,12. Orginisationsnummerpost (29)
     *
     * @param iLine
     */
    private static void readOrgnummerPost(BgMaxLine iLine, BgMaxBetalning iBetalning) {
        iBetalning. iBetalarensOrganisationsnr  = iLine.getField(3, 14);

    }



}
