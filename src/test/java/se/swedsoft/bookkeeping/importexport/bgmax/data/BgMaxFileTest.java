package se.swedsoft.bookkeeping.importexport.bgmax.data;


import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxAvsnitt;
import se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxBetalning;
import se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxFile;
import se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxReferens;
import java.net.URL;


/**
 * Tests for BGMax.
 * @author jensli
 * @version $Id$
 */
public class BgMaxFileTest {
    private final int NUM_FILES = 4;

    private final String FILE_NAME = "BgMaxTestFile";
    private final String FILE_ENDING = "ut";

    private final List<List<String>> files = new ArrayList<List<String>>();

    @Before
    public void setUp() throws Exception {
        // Load file lines to a list for easy access in tests
        for (int i = 0; i < NUM_FILES; i++) {
            String filename = FILE_NAME + (i + 1) + "." + FILE_ENDING;
            URL url = getClass().getResource(filename);
            File file = new File(url.toURI());
            BufferedReader reader = new BufferedReader(new FileReader(file));

            List<String> lines = new ArrayList<String>();
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            files.add(lines);
        }
    }

    @Test
    public void testParseFile3() {
        BgMaxFile bgMaxFile = new BgMaxFile();

        bgMaxFile.parse(files.get(3));

        assertEquals("layout name after reading ", "BGMAX", bgMaxFile.iLayoutnamn);
        assertEquals("iVersion", "01", bgMaxFile.iVersion);
        assertEquals("iTidsstampel", "20040525173035010331", bgMaxFile.iTidsstampel);

        assertEquals("iAntalBetalningsPoster", "00000009",
                bgMaxFile.iAntalBetalningsPoster);
        assertEquals("iAntalAvdragsPoster", "00000000", bgMaxFile.iAntalAvdragsPoster);
        assertEquals("iAntalExtraReferensPoster", "00000013",
                bgMaxFile.iAntalExtraReferensPoster);
        assertEquals("iAntalInsattningsPoster", "00000004",
                bgMaxFile.iAntalInsattningsPoster);

        BgMaxAvsnitt avs1 = bgMaxFile.iAvsnitts.get(0);

        assertEquals("iBankgiroNummer", "0009912346", avs1.iBankgiroNummer);
        assertEquals("iValuta", "SEK", avs1.iValuta);

        BgMaxBetalning bet1 = avs1.iBetalningar.get(0);

        assertEquals("iBankgiroNummer", "0003783511", bet1.iBankgiroNummer);
        assertEquals("iReferens", "", bet1.iReferens);
        assertEquals("iBetalningsBelopp", "000000000000180000", bet1.iBelopp);
        assertEquals("iReferensKod", "0", bet1.iReferensKod);
        assertEquals("iBetalningsKanalKod", "2", bet1.iBetalningsKanalKod);
        assertEquals("iBGCLopnummer", "000120000018", bet1.iBGCLopnummer);
        assertEquals("iAvibildmarkering", "0", bet1.iAvibildmarkering);
        assertEquals("iInformationsText",
                "Betalning med extra refnr 665869 657775 665661665760",
                bet1.iInformationsText);
        assertEquals("iBetalarensNamn", "Kalles Pl", bet1.iBetalarensNamn.substring(0, 9)); // String trunked because of char code issues
        assertEquals(
                "iBetalarensAdress", "Storgatan 2", bet1.iBetalarensAdress);
        assertEquals("iBetalarensPostnummer", "12345", bet1.iBetalarensPostnummer);
        assertEquals("iBetalarensOrt", "Stor", bet1.iBetalarensOrt.substring(0, 4));
        assertEquals("iBetalarensOrganisationsnr", "005500001234",
                bet1.iBetalarensOrganisationsnr);

        assertEquals("iBankKontoNummer", "00000000000000000005841000001009823",
                avs1.iBankKontoNummer);
        assertEquals("iBetalningsdag", "20040525", avs1.iBetalningsdag);
        assertEquals("iLopnummer", "00056", avs1.iLopnummer);

        assertEquals("iBelopp", "000000000000370", avs1.iBelopp);
        assertEquals("iValuta", "SEK", avs1.iValuta);
        assertEquals("iAntal", "00000002", avs1.iAntal);

        BgMaxReferens ref1 = bet1.iReferenser.get(0);

        assertEquals("iBankgiroNummer", "0003783511", ref1.iBankgiroNummer);
        assertEquals("iReferens", "665760", ref1.iReferens);
        assertEquals("iBelopp", "000000000000000000", ref1.iBelopp);
        assertEquals("iReferensKod", "2", ref1.iReferensKod);
        assertEquals("iBetalningsKanalKod", "2", ref1.iBetalningsKanalKod);
        assertEquals("iBGCLopnummer", "000120000018", ref1.iBGCLopnummer);
        assertEquals("iAvibildmarkering", "0", ref1.iAvibildmarkering);
    }
}
