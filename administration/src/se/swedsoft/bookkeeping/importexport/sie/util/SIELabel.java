package se.swedsoft.bookkeeping.importexport.sie.util;

import se.swedsoft.bookkeeping.importexport.sie.fields.*;

import java.util.LinkedList;
import java.util.List;

import static se.swedsoft.bookkeeping.importexport.sie.util.SIEType.*;

/**
 *
 * $Id$
 */
public enum SIELabel {
    // * = Frivillig
    // - = Får ej förekomma


    // Identifikationsposter
    //**************************************************************************************************

    /**
     * Flaggpost som anger om filen tagits emot av mottagaren
     *
     * #FLAGGA 0
     *
     * Typer: 1, 2, 3, 4I, 4E
     */
    SIE_FLAGGA ("#FLAGGA", new SIEEntryFlagga(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E ),

    /**
     * Vilket program som genererat filen
     *
     * #PROGRAM namn version
     *
     * Typer: 1, 2, 3, 4I, 4E
     */
    SIE_PROGRAM("#PROGRAM", new SIEEntryProgram(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),

    /**
     * Formatet för filen
     *
     * #FORMAT PC8
     *
     * Typer: 1, 2, 3, 4I, 4E
     */
    SIE_FORMAT("#FORMAT", new SIEEntryFormat(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),

    /**
     * När och av vem som som filen genererats.
     *
     * #GEN datum sign
     *
     * Typer: 1, 2, 3, 4I, 4E
     */
    SIE_GEN("#GEN", new SIEEntryGenererat(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),

    /**
     * Anger vilken filtyp innom SIE-formatet som filen följer
     *
     * #SIETYP 1
     *
     * Typer: 1*, 2, 3, 4I, 4E
     */
    SIE_SIETYP("#SIETYP", new SIEEntryTyp(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),

    /**
     * Fri kommentartext kring filens innehåll
     *
     * #PROSA text
     *
     * Typer: 1*, 2*, 3*, 4I*, 4E*
     */
    SIE_PROSA("#PROSA", new SIEEntryProsa(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),

    /**
     * Redovisiningsprogrammets internkod för exporterat företag
     *
     * #FNR företagsid
     *
     * Typer: 1*, 2*, 3*, 4I*, 4E*
     */
    SIE_FNR("#FNR", new SIEEntryForetagsid(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),

    /**
     * Orginisationsnummret för det företag som exporterats
     *
     * #ORGNR orgnr förvnr verknr
     *
     * Typer: 1*, 2*, 3*, 4I*, 4E*
     */
    SIE_ORGNR("#ORGNR", new SIEEntryOrgnummer(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),

    /**
     * Branchtillhörighet för det exporteradr företaget
     *
     * #BKOD SNI-kod
     *
     * Typer: 1*, 2*, 3*, ,4I-, 4E*
     */
    SIE_BKOD("#BKOD", new SIEEntryBranschkod(), SIE_1, SIE_2, SIE_3, SIE_4E),

    /**
     * Adressuppgifter för det exporterade företaget

     * #ADRESS kontakt utdelningsaddr postadress tel
     *
     * Typer: 1*, 2*, 3*, 4I*, 4E*
     */
    SIE_ADRESS("#ADRESS", new SIEEntryForetagsAdress(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),

    /**
     * Fullständigt namn för det företag som exporteras
     *
     * #FNAMN företagsnamn
     *
     * Typer: 1, 2, 3, 4I, 4E
     */
    SIE_FNAMN("#FNAMN", new SIEEntryFNamn(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),


    /**
     * Räkenskapsår för vilket exporterade data hämtas
     *
     * #RAR årsnr start slut
     *
     * Typer: 1, 2, 3, 4I*, 4E
     *
     */
    SIE_RAR("#RAR", new SIEEntryRAR(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),

    /**
     * Taxeringsår för deklarationsinformation (SRU-Koder)
     *
     * #TAXAR år uppstform
     *
     * Typer: 1*, 2*, 3*, 4I*, 4E*
     */
    SIE_TAXAR("#TAXAR", new SIEEntryTaxar(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),

    /**
     * Datum för periodsaldons omfattning
     *
     * #OMFATTN datum
     *
     * Typer: 1-, 2*, 3*, 41-, 4E
     */
    SIE_OMFATTN("#OMFATTN", new SIEEntryOmfattn(), SIE_2, SIE_3),


    /**
     * Kontoplanstyp
     *
     * #KPTYP typ
     *
     * Typer: 1*, 2*, 3*, 4I*, 4E*
     */
    SIE_KPTYP("#KPTYP", new SIEEntryKontoplanTyp(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),


    // Kontoplansuppgifter
    //**************************************************************************************************

    /**
     * Kontouppgifter
     *
     * #KONTO kontonr kontonamn
     *
     * Typer: 1, 2, 3, 4I*, 4E
     */
    SIE_KONTO("#KONTO", new SIEEntryKonto(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),


    /**
     * Kontotyp
     *
     * #KTYP kontonr kontotyp
     *
     * Typer: 1*, 2*, 3*, 4I*, 4E*
     */
    SIE_KTYP("#KTYP", new SIEEntryKontoTyp(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),


    /**
     * Enhet vid kvantitetsredovisning
     *
     * #ENHET kontonr enhet
     *
     * Typer: 1*, 2*, 3*, 4I*, 4E*
     */
    SIE_ENHET("#ENHET", new SIEEntryEnhet(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),

    /**
     * RSV-Kod för standardiserat räkenskapsutdrag
     *
     * #SRU konto SRU-kod
     *
     * Typer: 1, 2, 3, 4I*, 4E*
     */
    SIE_SRU("#SRU", new SIEEntrySRU(), SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E),


    /**
     * Dimension
     *
     * #DIM dimensionsnr namn
     *
     * Typer: 1-, 2-, 3!, 4I*, 4E*
     */
    SIE_DIM("#DIM", new SIEEntryDimension(), SIE_3, SIE_4I, SIE_4E),

    /**
     * Underdimension
     *
     * #UNDERDIM dimensionsnummer namn superdimension
     *
     * Typer: 1-, 2-, 3!, 4I*, 4E*
     */
    SIE_UNDERDIM("#UNDERDIM", new SIEEntryUnderDimension(), SIE_3, SIE_4I, SIE_4E),


    /**
     * Objekt
     *
     * #OBJEKT dimensionsnr objektkod objektnamn
     *
     * Typer: 1-, 2-, 3!, 4I*, 4E*
     */
    SIE_OBJEKT("#OBJEKT", new SIEEntryObjekt(), SIE_3, SIE_4I, SIE_4E),


    // Saldoposter / Verifikationsposter
    //**************************************************************************************************



    /**
     * Ingående balans för balanskonto
     *
     * #IB årsnr konto saldo kvantitet
     *
     * Typer: 1!, 2!, 3!, 4I-, 4E!
     */
    SIE_IB("#IB", new SIEEntryInBalance(),  SIE_1, SIE_2, SIE_3,  SIE_4E),


    /**
     * Utgående balans för balanskonto
     *
     * #UB årsnr konto saldo kvantitet
     *
     * Typer: 1!, 2!, 3!, 4I-, 4E!
     */
    SIE_UB("#UB", new SIEEntryOutBalance(),  SIE_1, SIE_2, SIE_3,  SIE_4E),


    /**
     * Ingående balans för object
     *
     * #OIB årsnr konto {dimensionsnr objectnr} saldo kvantitet
     *
     * Typer: 1-, 2-, 3!, 4I-, 4E*
     */
    // Vi stödjer inte balansräkningar för projelt eller resultatenhet för tillfället
    //SIE_OIB("#OIB", new SIEEntryObjectInBalance(),  SIE_3,  SIE_4E),


    /**
     * Utgående balans för object
     *
     * #UIB årsnr konto {dimensionsnr objectnr} saldo kvantitet
     *
     * Typer: 1-, 2-, 3!, 4I-, 4E*
     */
    // Vi stödjer inte balansräkningar för projelt eller resultatenhet för tillfället
    //SIE_OUB("#OUB", new SIEEntryObjectOutBalance(),  SIE_3,  SIE_4E),


    /**
     * Saldo för resultatkonto
     *
     * #RES årsnr konto saldo kvantitet
     *
     * Typer: 1!, 2!, 3!, 4I-, 4E!
     */
    SIE_RES("#RES", new SIEEntryResult(),  SIE_1, SIE_2, SIE_3, SIE_4E),


    /**
     * Periodens saldo för ett visst konto.
     *
     * #PSALDO årsnr period konto {dimensionsnr objectkod saldo kvantitet
     *
     * Typer: 1-, 2!, 3!, 4I-, 4E*
     */
    SIE_PSALDO("#PSALDO", new SIEEntryPeriodSaldo(), SIE_2, SIE_3),


    /**
     * Periodens saldo för ett visst konto.
     *
     * #PBUDGET årsnr period konto {dimensionsnr objectkod saldo kvantitet
     *
     * Typer: 1-, 2!, 3!, 4I-, 4E*
     */
    SIE_PBUDGET("#PBUDGET", new SIEEntryPeriodBudget(), SIE_2, SIE_3, SIE_4E),





    /**
     * Verifikationspost
     *
     * #VER serie vernrr verdatum vertext regdatum
     *
     * Typer: 1-, 2-, 3-, 4I*, 4E*
     */
    SIE_VER("#VER", new SIEEntryVerifikation(), SIE_4I, SIE_4E),

    /**
     * Transaktionspost
     *
     * #TRANS kontonr {objectlista} belopp transtext kvantitet
     *
     * Typer: 1-, 2-, 3-, 4I*, 4E*
     */
    SIE_TRANS("#TRANS", new SIEEntryTransaktion() ),



    //***************************************************************************
    SIE_NULL("", null, SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E);

    private String iName;

    private SIEEntry iEntry;

    private SIEType [] iFormats;

    /**
     *
     * @param pName
     * @param pEntry
     * @param pFormats
     */
    private SIELabel(String pName, SIEEntry pEntry, SIEType ... pFormats){
        iName    = pName;
        iEntry   = pEntry;
        iFormats = pFormats;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return iName;
    }

    /**
     *
     * @return
     */
    public SIEEntry getEntry() {
        return iEntry;
    }

    /**
     *
     * @return
     */
    public SIEType[] getFormats() {
        return iFormats;
    }

    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum type should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    public String toString() {
        return iName;
    }

    /**
     *
     * @param pFormat
     * @return
     */
    public static List<SIELabel> values(SIEType pFormat){
        List<SIELabel> iValues = new LinkedList<SIELabel>();
        for(SIELabel iLabel : SIELabel.values() ){
            for(SIEType iFormat: iLabel.iFormats){
                if(pFormat == iFormat){
                    iValues.add(iLabel);
                    break;
                }
            }
        }
        return iValues;
    }
}
