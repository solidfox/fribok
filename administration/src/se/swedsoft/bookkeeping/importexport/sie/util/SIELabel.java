package se.swedsoft.bookkeeping.importexport.sie.util;

import se.swedsoft.bookkeeping.importexport.sie.fields.*;

import static se.swedsoft.bookkeeping.importexport.sie.util.SIEType.*;

import java.util.List;
import java.util.LinkedList;

/**
 * Date: 2006-feb-20
 * Time: 12:24:18
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








    //**************************************************************************************************
    SIE_NULL("", null, SIE_1, SIE_2, SIE_3, SIE_4I, SIE_4E);




    /*
#FLAGGA 0
#FORMAT PC8
#SIETYP 1
#PROGRAM "SPCS Administration 2000 N„t" 3.2a
#GEN 20060217
#FNAMN "Lars Axelsson"
#FNR D:\program\Spcs\Ovnbol
#ADRESS "Karin Svensson" "Cirkelv„gen 2" "571 75 Fredriksdal" 0380-26217
#RAR 0 20050101 20051231
#RAR -1 20040101 20041231
#TAXAR 2001 ÅRL
#KPTYP EUBAS97
#KONTO 1010 "Balanserade utgifter"
#KTYP 1010 T
#SRU 1010 234
#KONTO 1030 Patent
#KTYP 1030 T
#SRU 1030 234
#KONTO 1039 "Ack avskrivn patent"
#KTYP 1039 T
#SRU 1039 234
#KONTO 1060 Hyresr„tt
#KTYP 1060 T
#SRU 1060 234
#KONTO 1069 "Ack avskrivn hyresr„tt"
#KTYP 1069 T
#SRU 1069 234
#KONTO 1070 Goodwill
#KTYP 1070 T
#SRU 1070 234
#KONTO 1079 "Ack avskrivn goodwill"
#KTYP 1079 T
#SRU 1079 234
#KONTO 1110 Byggnader
#KTYP 1110 T
#SRU 1110 237
#KONTO 1119 "Ack avskrivn byggnader"
#KTYP 1119 T
#SRU 1119 237
#KONTO 1130 Mark
#KTYP 1130 T
#SRU 1130 235
#KONTO 1140 "Tomter, markomr obebyggda"
#KTYP 1140 T
#SRU 1140 235
#KONTO 1150 Markanl„ggningar
#KTYP 1150 T
#SRU 1150 237
#KONTO 1159 "Ack avskrivn markanl„ggn"
#KTYP 1159 T
#SRU 1159 237
#KONTO 1211 Arbetsmaskiner
#KTYP 1211 T
#SRU 1211 236
#KONTO 1219 "Ack avskrivn arb.maskiner"
#KTYP 1219 T
#SRU 1219 236
#KONTO 1220 "Inventarier och verktyg"
#KTYP 1220 T
#SRU 1220 236
#KONTO 1221 Inventarier
#KTYP 1221 T
#SRU 1221 236
#KONTO 1222 Byggnadsinventarier
#KTYP 1222 T
#SRU 1222 236
#KONTO 1223 Markinventarier
#KTYP 1223 T
#SRU 1223 236
#KONTO 1225 Verktyg
#KTYP 1225 T
#SRU 1225 236
#KONTO 1229 "Ack avskrivn inv/verktyg"
#KTYP 1229 T
#SRU 1229 236
#KONTO 1240 "Bilar och andra transportmedel"
#KTYP 1240 T
#SRU 1240 236
#KONTO 1241 Personbilar
#KTYP 1241 T
#SRU 1241 236
#KONTO 1242 Lastbilar
#KTYP 1242 T
#SRU 1242 236
#KONTO 1243 Truckar
#KTYP 1243 T
#SRU 1243 236
#KONTO 1244 Arbetsmaskiner
#KTYP 1244 T
#SRU 1244 236
#KONTO 1245 Traktorer
#KTYP 1245 T
#SRU 1245 236
#KONTO 1246 "Motorcyklar, mopeder och skotrar"
#KTYP 1246 T
#SRU 1246 236
#KONTO 1247 B†tar
#KTYP 1247 T
#SRU 1247 236
#KONTO 1248 "Flygplan och helikoptrar"
#KTYP 1248 T
#SRU 1248 236
#KONTO 1249 "Ack avskr bilar/transportmedel"
#KTYP 1249 T
#SRU 1249 236
#KONTO 1250 Datorer
#KTYP 1250 T
#SRU 1250 236
#KONTO 1251 "Datorer, f”retaget"
#KTYP 1251 T
#SRU 1251 236
#KONTO 1257 "Datorer, personal"
#KTYP 1257 T
#SRU 1257 236
#KONTO 1259 "Ack avskrivn datorer"
#KTYP 1259 T
#SRU 1259 236
#KONTO 1291 "Konst o ej avskrivn invent"
#KTYP 1291 T
#SRU 1291 235
#KONTO 1311 "Aktier sv dotterf”retag"
#KTYP 1311 T
#KONTO 1312 "Aktier utl dotterf”retag"
#KTYP 1312 T
#KONTO 1314 "Aktier/and ”vr utl konc.ftg"
#KTYP 1314 T
#KONTO 1330 "Aktier, andra sv f”retag"
#KTYP 1330 T
#KONTO 1350 "Aktier, andra utl f”retag"
#KTYP 1350 T
#SRU 1350 233
#KONTO 1354 "Obligationer, v„rdepapper"
#KTYP 1354 T
#SRU 1354 233
#KONTO 1360 "Finansiella instrument"
#KTYP 1360 T
#KONTO 1369 "V„rdereg finans instrument"
#KTYP 1369 T
#KONTO 1380 "L†ngfristiga fordringar"
#KTYP 1380 T
#SRU 1380 233
#KONTO 1382 "Fordringar hos anst„llda"
#KTYP 1382 T
#SRU 1382 233
#KONTO 1383 Depositioner
#KTYP 1383 T
#SRU 1383 233
#KONTO 1390 "V„rderegl l†ngfr fordr"
#KTYP 1390 T
#SRU 1390 233
#KONTO 1400 Lager
#KTYP 1400 T
#SRU 1400 219
#KONTO 1402 "Varor p† v„g"
#KTYP 1402 T
#SRU 1402 219
#KONTO 1440 "Produkter i arbete"
#KTYP 1440 T
#SRU 1440 219
#KONTO 1470 "P†g†ende arbete"
#KTYP 1470 T
#SRU 1470 219
#KONTO 1480 "F”rskott till leverant”r"
#KTYP 1480 T
#SRU 1480 219
#KONTO 1510 Kundfordringar
#KTYP 1510 T
#SRU 1510 204
#KONTO 1512 "Bel†nade fordringar"
#KTYP 1512 T
#SRU 1512 204
#KONTO 1515 "Os„kra kundfordringar"
#KTYP 1515 T
#SRU 1515 204
#KONTO 1580 "Kontokort och kuponger"
#KTYP 1580 T
#SRU 1580 204
#KONTO 1610 F”rskott
#KTYP 1610 T
#SRU 1610 220
#KONTO 1611 Resef”rskott
#KTYP 1611 T
#SRU 1611 220
#KONTO 1613 "™vriga f”rskott"
#KTYP 1613 T
#SRU 1613 220
#KONTO 1619 "™vr fordringar hos anst„llda"
#KTYP 1619 T
#SRU 1619 220
#KONTO 1630 "Avr„kn skatter och avgifter"
#KTYP 1630 T
#SRU 1630 220
#KONTO 1640 Skattefordringar
#KTYP 1640 T
#KONTO 1650 "Fordran moms"
#KTYP 1650 T
#SRU 1650 207
#KONTO 1680 "™vr kortfristiga fordr"
#KTYP 1680 T
#SRU 1680 220
#KONTO 1684 "Fordring hos leverant”r"
#KTYP 1684 T
#SRU 1684 220
#KONTO 1685 "Fordring hos n„rst†ende"
#KTYP 1685 T
#SRU 1685 220
#KONTO 1710 "F”rutbetalda hyror"
#KTYP 1710 T
#SRU 1710 205
#KONTO 1740 "F”rutbet r„nteutgifter"
#KTYP 1740 T
#SRU 1740 205
#KONTO 1750 "Upplupna hyresinkomster"
#KTYP 1750 T
#SRU 1750 205
#KONTO 1760 "Upplupna inkomstr„ntor"
#KTYP 1760 T
#SRU 1760 205
#KONTO 1790 "™vr interimsfordringar"
#KTYP 1790 T
#SRU 1790 205
#KONTO 1820 "Obligationer, v„rdepapper"
#KTYP 1820 T
#SRU 1820 203
#KONTO 1860 "Finansiella instrument"
#KTYP 1860 T
#SRU 1860 203
#KONTO 1869 "V„rderegl finans instr"
#KTYP 1869 T
#SRU 1869 203
#KONTO 1910 Kassa
#KTYP 1910 T
#SRU 1910 200
#KONTO 1920 Postgiro
#KTYP 1920 T
#SRU 1920 200
#KONTO 1930 Checkr„kningskonto
#KTYP 1930 T
#SRU 1930 200
#KONTO 1940 "™vriga bankkonto"
#KTYP 1940 T
#SRU 1940 200
#KONTO 2081 Aktiekapital
#KTYP 2081 S
#KONTO 2086 Reservfond
#KTYP 2086 S
#KONTO 2091 "Balanserad vinst/f”rlust"
#KTYP 2091 S
#KONTO 2094 "Egna aktier"
#KTYP 2094 S
#KONTO 2098 "Vinst/f”rlust f”reg †r"
#KTYP 2098 S
#KONTO 2099 "Redovisat resultat"
#KTYP 2099 S
#KONTO 2116 "Periodiseringsfond TAX 96"
#KTYP 2116 S
#SRU 2116 339
#KONTO 2117 "Periodiseringsfond TAX 97"
#KTYP 2117 S
#SRU 2117 339
#KONTO 2118 "Periodiseringsfond TAX 98"
#KTYP 2118 S
#SRU 2118 339
#KONTO 2119 "Periodiseringsfond TAX 99"
#KTYP 2119 S
#SRU 2119 339
#KONTO 2120 "Periodiseringsfond TAX 2000"
#KTYP 2120 S
#SRU 2120 339
#KONTO 2121 "Periodiseringsfond TAX 2001"
#KTYP 2121 S
#SRU 2121 339
#KONTO 2122 "Periodiseringsfond TAX 2002"
#KTYP 2122 S
#SRU 2122 339
#KONTO 2150 "Ack ”veravskrivningar"
#KTYP 2150 S
#SRU 2150 330
#KONTO 2151 "Ack ”veravskr imm anl.tillg"
#KTYP 2151 S
#SRU 2151 330
#KONTO 2152 "Ack ”veravskr p† byggn/markanl"
#KTYP 2152 S
#SRU 2152 330
#KONTO 2153 "Ack ”veravskr maskiner/inv"
#KTYP 2153 S
#SRU 2153 330
#KONTO 2190 "™vr obeskattade reserver"
#KTYP 2190 S
#SRU 2190 339
#KONTO 2195 Valutakursreserv
#KTYP 2195 S
#SRU 2195 337
#KONTO 2196 Lagerreserv
#KTYP 2196 S
#SRU 2196 339
#KONTO 2210 "Avsatt till pensioner"
#KTYP 2210 S
#SRU 2210 320
#KONTO 2211 "Avs„ttningar f”r PRI-pensioner"
#KTYP 2211 S
#SRU 2211 320
#KONTO 2219 "Avs„ttningar f”r ”vr pensioner"
#KTYP 2219 S
#SRU 2219 320
#KONTO 2220 Garantiskulder
#KTYP 2220 S
#SRU 2220 304
#KONTO 2330 Checkr„kningskredit
#KTYP 2330 S
#SRU 2330 329
#KONTO 2350 Reversl†n
#KTYP 2350 S
#SRU 2350 329
#KONTO 2355 "L†n i utl„ndsk valuta"
#KTYP 2355 S
#SRU 2355 329
#KONTO 2393 "L†n fr†n aktie„gare"
#KTYP 2393 S
#KONTO 2395 "Andra l†n i utl valuta"
#KTYP 2395 S
#SRU 2395 329
#KONTO 2399 "™vr l†ngfristiga skulder"
#KTYP 2399 S
#SRU 2399 329
#KONTO 2420 "F”rskott fr†n kunder"
#KTYP 2420 S
#SRU 2420 310
#KONTO 2440 Leverant”rsskulder
#KTYP 2440 S
#SRU 2440 300
#KONTO 2510 Skatteskulder
#KTYP 2510 S
#SRU 2510 301
#KONTO 2611 "Utg moms f”rs„ljning 25% sv"
#KTYP 2611 S
#SRU 2611 307
#KONTO 2612 "Utg moms egna uttag 25%"
#KTYP 2612 S
#SRU 2612 307
#KONTO 2613 "Utg moms uthyrning ored"
#KTYP 2613 S
#SRU 2613 307
#KONTO 2614 "Ber moms tjf”rv 25% utl"
#KTYP 2614 S
#SRU 2614 307
#KONTO 2615 "Ber moms varuf”rv 25% EU"
#KTYP 2615 S
#SRU 2615 307
#KONTO 2621 "Utg moms f”rs„ljning 12% Sv"
#KTYP 2621 S
#SRU 2621 307
#KONTO 2622 "Utg moms egna uttag 12%"
#KTYP 2622 S
#SRU 2622 307
#KONTO 2624 "Ber moms tjf”rv 12% utl"
#KTYP 2624 S
#SRU 2624 307
#KONTO 2631 "Utg moms f”rs„ljning 6% Sv"
#KTYP 2631 S
#SRU 2631 307
#KONTO 2632 "Utg moms egna uttag 6%"
#KTYP 2632 S
#SRU 2632 307
#KONTO 2634 "Ber moms tjf”rv 6% utl"
#KTYP 2634 S
#SRU 2634 307
#KONTO 2641 "Ing†ende moms"
#KTYP 2641 S
#SRU 2641 307
#KONTO 2645 "Ing†ende moms utland"
#KTYP 2645 S
#SRU 2645 307
#KONTO 2650 "Moms redovisningskonto"
#KTYP 2650 S
#SRU 2650 307
#KONTO 2660 "S„rskilda punktskatter"
#KTYP 2660 S
#SRU 2660 301
#KONTO 2710 "Personalens k„llskatt"
#KTYP 2710 S
#SRU 2710 301
#KONTO 2731 "Avr„kn sociala avgifter"
#KTYP 2731 S
#SRU 2731 301
#KONTO 2732 "Avr„kn s„rsk l”nesk pens"
#KTYP 2732 S
#SRU 2732 301
#KONTO 2750 "Utm„tning i l”n mm"
#KTYP 2750 S
#SRU 2750 319
#KONTO 2790 "™vriga l”neavdrag"
#KTYP 2790 S
#SRU 2790 319
#KONTO 2792 L”nsparande
#KTYP 2792 S
#SRU 2792 319
#KONTO 2793 "Kollektivf”rs„kring mm"
#KTYP 2793 S
#SRU 2793 319
#KONTO 2794 Fackf”reningsavgifter
#KTYP 2794 S
#SRU 2794 319
#KONTO 2795 Granskningsavgifter
#KTYP 2795 S
#SRU 2795 319
#KONTO 2840 L†neskulder
#KTYP 2840 S
#SRU 2840 319
#KONTO 2850 "Avr„kn skatter/avgifter"
#KTYP 2850 S
#SRU 2850 319
#KONTO 2890 "™vr kortfr skulder"
#KTYP 2890 S
#SRU 2890 319
#KONTO 2893 "Skulder n„rst†ende personer"
#KTYP 2893 S
#KONTO 2910 "Upplupna l”ner"
#KTYP 2910 S
#SRU 2910 305
#KONTO 2920 "Upplupna seml”ner"
#KTYP 2920 S
#SRU 2920 305
#KONTO 2921 "Upplupna soc.avg sem.l”n"
#KTYP 2921 S
#SRU 2921 305
#KONTO 2941 "Upplupna sociala avgifter"
#KTYP 2941 S
#SRU 2941 305
#KONTO 2950 "Upplupna avtalade soc avg"
#KTYP 2950 S
#SRU 2950 305
#KONTO 2960 "Upplupna utgiftsr„ntor"
#KTYP 2960 S
#SRU 2960 305
#KONTO 2970 "F”rutbetalda hyresinkomster"
#KTYP 2970 S
#SRU 2970 305
#KONTO 2990 "™vr interimsskulder"
#KTYP 2990 S
#SRU 2990 305
#KONTO 2995 "Skuld inkomna f”ljesedlar"
#KTYP 2995 S
#SRU 2995 305
#KONTO 2999 "™vr uppl kostn/f”rutbet int„kter"
#KTYP 2999 S
#SRU 2999 305
#KONTO 3020 "F”rs„ljning VMB varor"
#KTYP 3020 I
#SRU 3020 400
#KONTO 3028 "Positiv VM omf”ringskonto"
#KTYP 3028 I
#SRU 3028 400
#KONTO 3030 "Positiv VM"
#KTYP 3030 I
#SRU 3030 400
#KONTO 3041 "F”rs„ljn tj„nst 25% sv"
#KTYP 3041 I
#SRU 3041 400
#KONTO 3042 "F”rs„ljn tj„nst 12% sv"
#KTYP 3042 I
#SRU 3042 400
#KONTO 3043 "F”rs„ljn tj„nst 6% sv"
#KTYP 3043 I
#SRU 3043 400
#KONTO 3044 "F”rs„ljn tj„nst sv momsfri"
#KTYP 3044 I
#SRU 3044 400
#KONTO 3045 "F”rs„lj tj„nst utanf”r EU"
#KTYP 3045 I
#SRU 3045 400
#KONTO 3046 "F”rs„ljn tj„nst till EU"
#KTYP 3046 I
#SRU 3046 400
#KONTO 3048 "F”rs„ljn tj„nst EU momsfri"
#KTYP 3048 I
#SRU 3048 400
#KONTO 3051 "F”rs„ljn varor 25% sv"
#KTYP 3051 I
#SRU 3051 400
#KONTO 3052 "F”rs„ljn varor 12% sv"
#KTYP 3052 I
#SRU 3052 400
#KONTO 3053 "F”rs„ljn varor 6% sv"
#KTYP 3053 I
#SRU 3053 400
#KONTO 3054 "F”rs„ljn varor sv momsfri"
#KTYP 3054 I
#SRU 3054 400
#KONTO 3055 "F”rs„ljn varor utanf”r EU"
#KTYP 3055 I
#SRU 3055 400
#KONTO 3056 "F”rs„ljn varor till EU"
#KTYP 3056 I
#SRU 3056 400
#KONTO 3058 "F”rs„ljn varor EU momsfri"
#KTYP 3058 I
#SRU 3058 400
#KONTO 3059 "Momspl uttag av fast.tj"
#KTYP 3059 I
#SRU 3059 400
#KONTO 3062 "F”rs varor n„rst f”retag"
#KTYP 3062 I
#SRU 3062 400
#KONTO 3100 "Hyresint„kt skattepl"
#KTYP 3100 I
#SRU 3100 400
#KONTO 3510 Emballage
#KTYP 3510 I
#SRU 3510 400
#KONTO 3520 Frakter
#KTYP 3520 I
#SRU 3520 400
#KONTO 3521 "Frakter export"
#KTYP 3521 I
#SRU 3521 400
#KONTO 3522 "Frakter EU"
#KTYP 3522 I
#SRU 3522 400
#KONTO 3530 "Fakt tull och spedition"
#KTYP 3530 I
#SRU 3530 400
#KONTO 3540 Faktureringsavgifter
#KTYP 3540 I
#SRU 3540 400
#KONTO 3541 "Faktureringsavgifter export"
#KTYP 3541 I
#SRU 3541 400
#KONTO 3542 "Faktureringsavgifter EU"
#KTYP 3542 I
#SRU 3542 400
#KONTO 3550 "Fakt resekostnader"
#KTYP 3550 I
#SRU 3550 400
#KONTO 3590 "™vr fakt kostnader"
#KTYP 3590 I
#SRU 3590 400
#KONTO 3610 "F”rs„ljning material"
#KTYP 3610 I
#SRU 3610 400
#KONTO 3690 "™vriga sidoint„ker"
#KTYP 3690 I
#SRU 3690 400
#KONTO 3731 Kassarabatter
#KTYP 3731 I
#SRU 3731 400
#KONTO 3736 "Kassarabatter export"
#KTYP 3736 I
#SRU 3736 400
#KONTO 3737 "Kassarabatter EU"
#KTYP 3737 I
#SRU 3737 400
#KONTO 3740 ™resutj„mning
#KTYP 3740 I
#SRU 3740 400
#KONTO 3840 "Aktiverat arbete (material)"
#KTYP 3840 I
#SRU 3840 402
#KONTO 3850 "Aktiverat arbete (omkostnader)"
#KTYP 3850 I
#SRU 3850 402
#KONTO 3870 "Aktiverat arbete (l”ner)"
#KTYP 3870 I
#SRU 3870 402
#KONTO 3911 Hyresint„kter
#KTYP 3911 I
#SRU 3911 401
#KONTO 3921 Provisionsint„kter
#KTYP 3921 I
#SRU 3921 401
#KONTO 3950 "?tervunna kundf”rluster"
#KTYP 3950 I
#SRU 3950 401
#KONTO 3960 "Kursvinst r”relsen"
#KTYP 3960 I
#SRU 3960 401
#KONTO 3970 "Vinst avyttr imm/mat anl.tillg"
#KTYP 3970 I
#SRU 3970 552
#KONTO 3972 "Vinst avyttr fastigheter"
#KTYP 3972 I
#SRU 3972 552
#KONTO 3973 "Vinst avyttr maskiner/inv"
#KTYP 3973 I
#SRU 3973 552
#KONTO 3985 "Erh†llna statliga bidrag"
#KTYP 3985 I
#SRU 3985 401
#KONTO 3987 "Erh†llna kommunala bidrag"
#KTYP 3987 I
#SRU 3987 401
#KONTO 3989 "™vriga erh†llna bidrag"
#KTYP 3989 I
#SRU 3989 401
#KONTO 3990 "™vr ers„ttn och int„ker"
#KTYP 3990 I
#SRU 3990 401
#KONTO 3994 F”rs„kringsers„ttningar
#KTYP 3994 I
#SRU 3994 401
#KONTO 4010 "Ink”p materiel och varor"
#KTYP 4010 K
#SRU 4010 500
#KONTO 4020 "Ink”p VMB varor"
#KTYP 4020 K
#SRU 4020 500
#KONTO 4028 "Negativ VM omf”ringskonto"
#KTYP 4028 K
#SRU 4028 500
#KONTO 4030 "Negativ VM"
#KTYP 4030 K
#SRU 4030 500
#KONTO 4055 "Trepartsf”rv varor fr EU"
#KTYP 4055 K
#SRU 4055 500
#KONTO 4056 "Ink”p varor 25% EU"
#KTYP 4056 K
#SRU 4056 500
#KONTO 4057 "Ink”p varor 12% EU"
#KTYP 4057 K
#SRU 4057 500
#KONTO 4058 "Ink”p varor EU momsfri"
#KTYP 4058 K
#SRU 4058 500
#KONTO 4100 "Kostn uthyrn lokal sk.pl"
#KTYP 4100 K
#SRU 4100 500
#KONTO 4600 "Legoarbeten, underentrepr"
#KTYP 4600 K
#SRU 4600 501
#KONTO 4731 "Erh†llna kassarabatter"
#KTYP 4731 K
#SRU 4731 500
#KONTO 4733 "Erh†llet aktivitetsst”d"
#KTYP 4733 K
#SRU 4733 500
#KONTO 4940 "F”r„ndr produkter i arbete"
#KTYP 4940 K
#SRU 4940 509
#KONTO 4950 "F”r„ndr lager f„rdigvaror"
#KTYP 4950 K
#SRU 4950 509
#KONTO 4970 "F”r„ndr p†g arbete"
#KTYP 4970 K
#SRU 4970 509
#KONTO 4990 Lagerf”r„ndring
#KTYP 4990 K
#SRU 4990 509
#KONTO 5010 Lokalhyra
#KTYP 5010 K
#SRU 5010 528
#KONTO 5020 "El hyrd lokal"
#KTYP 5020 K
#SRU 5020 528
#KONTO 5030 "V„rme hyrd lokal"
#KTYP 5030 K
#SRU 5030 528
#KONTO 5050 "Lokaltillbeh”r hyrd lokal"
#KTYP 5050 K
#SRU 5050 528
#KONTO 5060 "St„dning, renh†llning hyrd lokal"
#KTYP 5060 K
#SRU 5060 528
#KONTO 5070 "Reparationer hyrd lokal"
#KTYP 5070 K
#SRU 5070 528
#KONTO 5090 "™vr kostnader hyrd lokal"
#KTYP 5090 K
#SRU 5090 528
#KONTO 5120 Belysning
#KTYP 5120 K
#SRU 5120 528
#KONTO 5130 V„rme
#KTYP 5130 K
#SRU 5130 528
#KONTO 5132 Sotning
#KTYP 5132 K
#SRU 5132 528
#KONTO 5140 "Vatten och avlopp"
#KTYP 5140 K
#SRU 5140 528
#KONTO 5160 "Renh†lln och st„dning"
#KTYP 5160 K
#SRU 5160 528
#KONTO 5170 "Rep och underh†ll fastighet"
#KTYP 5170 K
#SRU 5170 526
#KONTO 5191 Fastighetsskatt
#KTYP 5191 K
#SRU 5191 528
#KONTO 5192 "F”rs„kringsprem fastighet"
#KTYP 5192 K
#SRU 5192 528
#KONTO 5199 "™vr fastighetskostnader"
#KTYP 5199 K
#SRU 5199 528
#KONTO 5210 "Hyra arbetsmaskiner"
#KTYP 5210 K
#SRU 5210 529
#KONTO 5220 "Hyra inventarier"
#KTYP 5220 K
#SRU 5220 529
#KONTO 5290 "™vr hyreskostn anl tillg"
#KTYP 5290 K
#SRU 5290 529
#KONTO 5310 "El f”r drift"
#KTYP 5310 K
#SRU 5310 530
#KONTO 5380 Vatten
#KTYP 5380 K
#SRU 5380 530
#KONTO 5390 "™vriga br„nslen"
#KTYP 5390 K
#SRU 5390 530
#KONTO 5410 F”rbrukningsinventarier
#KTYP 5410 K
#SRU 5410 531
#KONTO 5460 F”rbrukningsmaterial
#KTYP 5460 K
#SRU 5460 531
#KONTO 5480 "Arbetskl„der o skyddsmtrl"
#KTYP 5480 K
#SRU 5480 531
#KONTO 5500 "Reparation och underh†ll"
#KTYP 5500 K
#SRU 5500 530
#KONTO 5611 "Drivmedel personbilar"
#KTYP 5611 K
#SRU 5611 536
#KONTO 5612 "Skatt f”rs„kr personbilar"
#KTYP 5612 K
#SRU 5612 536
#KONTO 5613 "Reparation personbilar"
#KTYP 5613 K
#SRU 5613 536
#KONTO 5615 "Leasingavg personbilar"
#KTYP 5615 K
#SRU 5615 536
#KONTO 5690 "™vr transportmedel"
#KTYP 5690 K
#SRU 5690 538
#KONTO 5710 "Frakt och transport"
#KTYP 5710 K
#SRU 5710 538
#KONTO 5720 "Tull- och speditionskost"
#KTYP 5720 K
#SRU 5720 538
#KONTO 5800 Resekostnader
#KTYP 5800 K
#SRU 5800 538
#KONTO 5910 Annonsering
#KTYP 5910 K
#SRU 5910 541
#KONTO 5930 Reklamtrycksaker
#KTYP 5930 K
#SRU 5930 541
#KONTO 5940 "Utst„llning och m„ssor"
#KTYP 5940 K
#SRU 5940 541
#KONTO 5990 "™vrig reklam"
#KTYP 5990 K
#SRU 5990 541
#KONTO 6010 "Kataloger och prislistor"
#KTYP 6010 K
#SRU 6010 530
#KONTO 6040 Kontokortsavgifter
#KTYP 6040 K
#SRU 6040 530
#KONTO 6050 F”rs„ljningsprovision
#KTYP 6050 K
#SRU 6050 530
#KONTO 6060 Kreditf”rs„ljningskostnad
#KTYP 6060 K
#SRU 6060 530
#KONTO 6071 "Repr avdr.gill"
#KTYP 6071 K
#SRU 6071 530
#KONTO 6072 "Repr ej avdr.gill"
#KTYP 6072 K
#SRU 6072 530
#KONTO 6080 Bankgarantier
#KTYP 6080 K
#SRU 6080 530
#KONTO 6090 "™vr f”rs„ljningskostnader"
#KTYP 6090 K
#SRU 6090 530
#KONTO 6110 Kontorsmaterial
#KTYP 6110 K
#SRU 6110 530
#KONTO 6150 Trycksaker
#KTYP 6150 K
#SRU 6150 530
#KONTO 6211 Telefon
#KTYP 6211 K
#SRU 6211 530
#KONTO 6212 Mobiltelefon
#KTYP 6212 K
#SRU 6212 530
#KONTO 6213 Mobils”kning
#KTYP 6213 K
#SRU 6213 530
#KONTO 6214 Fax
#KTYP 6214 K
#SRU 6214 530
#KONTO 6250 Porto
#KTYP 6250 K
#SRU 6250 530
#KONTO 6310 F”retagsf”rs„kringar
#KTYP 6310 K
#SRU 6310 530
#KONTO 6320 Sj„lvrisker
#KTYP 6320 K
#SRU 6320 530
#KONTO 6350 Kundf”rluster
#KTYP 6350 K
#SRU 6350 530
#KONTO 6390 "™vriga riskkostnader"
#KTYP 6390 K
#SRU 6390 530
#KONTO 6410 Styrelsearvode
#KTYP 6410 K
#SRU 6410 530
#KONTO 6420 Revisionsarvode
#KTYP 6420 K
#SRU 6420 530
#KONTO 6430 "Management fees"
#KTYP 6430 K
#SRU 6430 530
#KONTO 6440 "?rsredovisn, del†rsrapporter"
#KTYP 6440 K
#SRU 6440 530
#KONTO 6450 Bolagsst„mma
#KTYP 6450 K
#SRU 6450 530
#KONTO 6490 "™vr f”rvaltningskostnader"
#KTYP 6490 K
#SRU 6490 530
#KONTO 6530 Redovisningstj„nster
#KTYP 6530 K
#SRU 6530 530
#KONTO 6540 IT-tj„nster
#KTYP 6540 K
#SRU 6540 530
#KONTO 6550 Konsultarvoden
#KTYP 6550 K
#SRU 6550 530
#KONTO 6570 Bankkostnader
#KTYP 6570 K
#SRU 6570 530
#KONTO 6580 Advokatkostnader
#KTYP 6580 K
#SRU 6580 530
#KONTO 6590 "™vr fr„mmande tj„nster"
#KTYP 6590 K
#SRU 6590 530
#KONTO 6800 "Inhyrd personal"
#KTYP 6800 K
#SRU 6800 530
#KONTO 6970 "Tidningar, facklitteratur"
#KTYP 6970 K
#SRU 6970 530
#KONTO 6981 "F”reningsavg avdr gill"
#KTYP 6981 K
#SRU 6981 530
#KONTO 6982 "F”reningsavg ej avdr gill"
#KTYP 6982 K
#SRU 6982 530
#KONTO 6991 "™vr avdr gill kostn"
#KTYP 6991 K
#SRU 6991 530
#KONTO 6992 "™vr ej avdr gill kostn"
#KTYP 6992 K
#SRU 6992 530
#KONTO 6993 "L„mnade bidrag och g†vor"
#KTYP 6993 K
#SRU 6993 530
#KONTO 7010 "L”n kollektivanst„llda"
#KTYP 7010 K
#SRU 7010 512
#KONTO 7081 "Sjukl”n kollektivanst„llda"
#KTYP 7081 K
#SRU 7081 512
#KONTO 7082 Semesterl”n
#KTYP 7082 K
#SRU 7082 512
#KONTO 7090 "F”r„ndr sem l”neskuld"
#KTYP 7090 K
#SRU 7090 512
#KONTO 7210 "L”n tj„nstem„n"
#KTYP 7210 K
#SRU 7210 512
#KONTO 7211 "L”n Extra s„ljare"
#KTYP 7211 K
#SRU 7211 512
#KONTO 7220 "L”n f”retagsledare"
#KTYP 7220 K
#SRU 7220 512
#KONTO 7281 "Sjukl”n tj„nstem„n"
#KTYP 7281 K
#SRU 7281 512
#KONTO 7282 "Sjukl”ner till ftgledare"
#KTYP 7282 K
#SRU 7282 512
#KONTO 7285 "Semesterl”n tj„nstem„n"
#KTYP 7285 K
#SRU 7285 512
#KONTO 7286 "Semesterl”n ftgledare"
#KTYP 7286 K
#SRU 7286 512
#KONTO 7290 "F”r„ndr sem l”neskuld"
#KTYP 7290 K
#SRU 7290 512
#KONTO 7310 "Kontanta extra ers„ttn"
#KTYP 7310 K
#SRU 7310 514
#KONTO 7321 "Skattefria trakt Sverige"
#KTYP 7321 K
#SRU 7321 514
#KONTO 7322 "Skattepl trakt Sverige"
#KTYP 7322 K
#SRU 7322 514
#KONTO 7323 "Skattefria trakt utland"
#KTYP 7323 K
#SRU 7323 514
#KONTO 7324 "Skattepl trakt utland"
#KTYP 7324 K
#SRU 7324 514
#KONTO 7331 "Skattefri bilers„ttning"
#KTYP 7331 K
#SRU 7331 514
#KONTO 7332 "Skattepl bilers„ttning"
#KTYP 7332 K
#SRU 7332 514
#KONTO 7381 "Kostn f”r fri bostad"
#KTYP 7381 K
#SRU 7381 514
#KONTO 7382 "Kostn fria/subv m†ltider"
#KTYP 7382 K
#SRU 7382 514
#KONTO 7385 "Kostn f”r fri bil"
#KTYP 7385 K
#SRU 7385 514
#KONTO 7389 "™vriga kostn naturaf”rm"
#KTYP 7389 K
#SRU 7389 514
#KONTO 7390 "™vr kostnadsers„ttningar"
#KTYP 7390 K
#SRU 7390 514
#KONTO 7411 "Kollektiv pensionsf”rs„kr"
#KTYP 7411 K
#SRU 7411 524
#KONTO 7412 "Indiv pensionsf”rs„kring"
#KTYP 7412 K
#SRU 7412 524
#KONTO 7490 "™vriga pensionskostnader"
#KTYP 7490 K
#SRU 7490 524
#KONTO 7510 Arbetsgivaravgifter
#KTYP 7510 K
#SRU 7510 520
#KONTO 7519 "Soc.avgifter semesterl”n"
#KTYP 7519 K
#SRU 7519 520
#KONTO 7531 L”neskatt
#KTYP 7531 K
#SRU 7531 520
#KONTO 7533 "S„rsk l”neskatt pensionsk"
#KTYP 7533 K
#SRU 7533 520
#KONTO 7570 "AMF enl avtal"
#KTYP 7570 K
#SRU 7570 511
#KONTO 7610 Utbildning
#KTYP 7610 K
#SRU 7610 527
#KONTO 7620 "Sjuk- och h„lsov†rd"
#KTYP 7620 K
#SRU 7620 527
#KONTO 7650 Sjukl”nef”rs„kring
#KTYP 7650 K
#SRU 7650 527
#KONTO 7690 "™vr personalkostnader"
#KTYP 7690 K
#SRU 7690 527
#KONTO 7813 "Avskrivningar patent"
#KTYP 7813 K
#SRU 7813 561
#KONTO 7816 "Avskrivningar hyresr„tt"
#KTYP 7816 K
#SRU 7816 561
#KONTO 7817 "Avskrivningar goodwill"
#KTYP 7817 K
#SRU 7817 561
#KONTO 7821 "Avskrivn byggnader"
#KTYP 7821 K
#SRU 7821 560
#KONTO 7822 "Avskrivn byggnadsinv"
#KTYP 7822 K
#SRU 7822 560
#KONTO 7824 "Avskrivn markanl„ggn"
#KTYP 7824 K
#SRU 7824 560
#KONTO 7831 "Avskrivn arbetsmaskiner"
#KTYP 7831 K
#SRU 7831 559
#KONTO 7832 "Avskrivn inventarier"
#KTYP 7832 K
#SRU 7832 559
#KONTO 7834 "Avskrivn bilar"
#KTYP 7834 K
#SRU 7834 559
#KONTO 7960 Kursf”rluster
#KTYP 7960 K
#SRU 7960 530
#KONTO 7972 "F”rlust avyttr fastigheter"
#KTYP 7972 K
#SRU 7972 556
#KONTO 7973 "F”rlust avyttr maskiner/inv"
#KTYP 7973 K
#SRU 7973 556
#KONTO 7974 "F”rlust avyttr aktier/andel"
#KTYP 7974 K
#SRU 7974 556
#KONTO 8012 "Utdeln sv/utl dotterbolag"
#KTYP 8012 I
#SRU 8012 564
#KONTO 8210 "Utdeln andra sv/utl f”retag"
#KTYP 8210 I
#SRU 8210 565
#KONTO 8230 "Valutakursdiff l†ngfr fordr"
#KTYP 8230 I
#SRU 8230 566
#KONTO 8270 "Nedskr andel/lf fordr ”vr ftg"
#KTYP 8270 I
#SRU 8270 553
#KONTO 8280 "?terf nedskr and/lf ford ” ftg"
#KTYP 8280 I
#SRU 8280 571
#KONTO 8300 R„nteint„kter
#KTYP 8300 I
#SRU 8300 568
#KONTO 8313 "R„nteint„kter kundfordringar"
#KTYP 8313 I
#SRU 8313 568
#KONTO 8331 Valutakursvinster
#KTYP 8331 I
#SRU 8331 566
#KONTO 8336 Valutakursf”rluster
#KTYP 8336 K
#SRU 8336 567
#KONTO 8390 "™vr finansiella int„kter"
#KTYP 8390 I
#SRU 8390 568
#KONTO 8400 R„ntekostnader
#KTYP 8400 K
#SRU 8400 569
#KONTO 8422 "R„ntekostnader levskulder"
#KTYP 8422 K
#SRU 8422 569
#KONTO 8431 "Valutakursvinst skulder"
#KTYP 8431 I
#SRU 8431 566
#KONTO 8436 "Valutakursf”rlust skulder"
#KTYP 8436 K
#SRU 8436 567
#KONTO 8490 "™vr finansiella kostnader"
#KTYP 8490 K
#SRU 8490 569
#KONTO 8811 "Avs„ttning per fond"
#KTYP 8811 K
#SRU 8811 594
#KONTO 8819 "?terf”ring av per fond"
#KTYP 8819 K
#SRU 8819 593
#KONTO 8850 "Avskrivn ”ver/under plan"
#KTYP 8850 K
#SRU 8850 578
#KONTO 8896 "F”r„ndring lagerreserv"
#KTYP 8896 K
#SRU 8896 593
#KONTO 8897 "?terf”ring av SURV"
#KTYP 8897 K
#SRU 8897 591
#KONTO 8910 "?rets skattekostnad"
#KTYP 8910 K
#KONTO 8999 "Redovisat resultat"
#KTYP 8999 K
#SRU 8999 596
#IB 0 1030 20000.00
#UB 0 1030 20000.00
#IB 0 1039 10000.00
#UB 0 1039 10000.00
#IB 0 1060 50000.00
#UB 0 1060 50000.00
#IB 0 1220 60000.00
#UB 0 1220 60000.00
#IB 0 1221 42785.41
#UB 0 1221 42785.41
#IB 0 1229 -16705.00
#UB 0 1229 -16705.00
#IB 0 1240 244700.00
#UB 0 1240 244700.00
#IB 0 1400 78785.13
#UB 0 1400 78785.13
#IB 0 1510 138272.77
#UB 0 1510 96994597.77
#IB 0 1512 12500.00
#UB 0 1512 12500.00
#IB 0 1790 4500.00
#UB 0 1790 4500.00
#IB 0 1910 -89629.00
#UB 0 1910 -89629.00
#IB 0 1920 15479.00
#UB 0 1920 15479.00
#IB 0 1930 130960.87
#UB 0 1930 130960.87
#IB 0 2081 -100000.00
#UB 0 2081 -100000.00
#IB 0 2099 -30700.59
#UB 0 2099 -30700.59
#IB 0 2122 -13000.00
#UB 0 2122 -13000.00
#IB 0 2150 -14130.00
#UB 0 2150 -14130.00
#IB 0 2350 -180000.00
#UB 0 2350 -180000.00
#IB 0 2440 -255108.99
#UB 0 2440 -255108.99
#IB 0 2510 -7893.00
#UB 0 2510 -7893.00
#IB 0 2611 -2500.00
#UB 0 2611 -11505.00
#IB 0 2641 12500.00
#UB 0 2641 12500.00
#IB 0 2650 -11449.00
#UB 0 2650 -11449.00
#IB 0 2710 -6882.00
#UB 0 2710 -6882.00
#IB 0 2732 -970.40
#UB 0 2732 -970.40
#IB 0 2920 -22570.80
#UB 0 2920 -22570.80
#IB 0 2921 -7407.72
#UB 0 2921 -7407.72
#IB 0 2941 -7448.83
#UB 0 2941 -7448.83
#IB 0 2990 -13500.00
#UB 0 2990 -13500.00
#RES 0 3041 -40500.00
#RES 0 3048 -96000000.00
#RES 0 3051 -28680.00
#RES 0 3055 -778064.00
#RES 0 3540 -40.00
#RES 0 3541 -35.28
#RES 0 3740 -0.72
#IB -1 1030 20000.00
#UB -1 1030 20000.00
#IB -1 1039 10000.00
#UB -1 1039 10000.00
#IB -1 1060 50000.00
#UB -1 1060 50000.00
#IB -1 1220 60000.00
#UB -1 1220 60000.00
#IB -1 1221 42785.41
#UB -1 1221 42785.41
#IB -1 1229 -16705.00
#UB -1 1229 -16705.00
#IB -1 1240 244700.00
#UB -1 1240 244700.00
#IB -1 1400 78785.13
#UB -1 1400 78785.13
#IB -1 1510 138272.77
#UB -1 1510 138272.77
#IB -1 1512 12500.00
#UB -1 1512 12500.00
#IB -1 1790 4500.00
#UB -1 1790 4500.00
#IB -1 1910 -89629.00
#UB -1 1910 -89629.00
#IB -1 1920 15479.00
#UB -1 1920 15479.00
#IB -1 1930 130960.87
#UB -1 1930 130960.87
#IB -1 2081 -100000.00
#UB -1 2081 -100000.00
#IB -1 2099 -30700.59
#UB -1 2099 -30700.59
#IB -1 2122 -13000.00
#UB -1 2122 -13000.00
#IB -1 2150 -14130.00
#UB -1 2150 -14130.00
#IB -1 2350 -180000.00
#UB -1 2350 -180000.00
#IB -1 2440 -255108.99
#UB -1 2440 -255108.99
#IB -1 2510 -7893.00
#UB -1 2510 -7893.00
#IB -1 2611 -2500.00
#UB -1 2611 -2500.00
#IB -1 2641 12500.00
#UB -1 2641 12500.00
#IB -1 2650 -11449.00
#UB -1 2650 -11449.00
#IB -1 2710 -6882.00
#UB -1 2710 -6882.00
#IB -1 2732 -970.40
#UB -1 2732 -970.40
#IB -1 2920 -22570.80
#UB -1 2920 -22570.80
#IB -1 2921 -7407.72
#UB -1 2921 -7407.72
#IB -1 2941 -7448.83
#UB -1 2941 -7448.83
#IB -1 2990 -13500.00
#UB -1 2990 -13500.00
    */





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
