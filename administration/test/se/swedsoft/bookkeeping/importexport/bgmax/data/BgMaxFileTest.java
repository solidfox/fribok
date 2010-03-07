package se.swedsoft.bookkeeping.importexport.bgmax.data;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxAvsnitt;
import se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxBetalning;
import se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxFile;
import se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxReferens;

/**
 * Tests for BGMax.
 *
 * @author jensli
 *
 * $Id$
 *
 */
public class BgMaxFileTest {
 
    public BgMaxFile File4;
 
    public final String FILE_NAME = "BgMaxTestFile", 
        FILE_ENDING = "ut", 
        DIR = "data";

    public final int NR_FILES = 4;

    public List<TestFile> fileList = new ArrayList<TestFile>();
 
    @Before
        public void setUp() throws Exception {
  
  
        for ( int i = 0; i < NR_FILES; i++ ) {
            TestFile file = new TestFile( FILE_NAME + i + 1 + "." + FILE_ENDING, DIR );
            fileList.add( file );
   
            BufferedReader reader = new BufferedReader( new FileReader( file.dir + File.separator + file.name ) );
            file.lines = new ArrayList<String>();
   
            while ( true ) {
                String line = reader.readLine();
    
                if ( line == null ) break;
    
                file.lines.add( line );
            }
   
        }
    }

 
    @After
        public void tearDown() throws Exception {}

    @Test
        public void testParse()
    {
        testParseFile4();
    }
 
    public void testParseFile4( )
    {
        TestFile testFile = fileList.get( 3 );  
        BgMaxFile bgMaxFile = new BgMaxFile();
  
        bgMaxFile.parse(  testFile.lines );
  
        testParsedFile4( bgMaxFile, testFile );
    }
 
 
    /**
     * Tests if the right data has been read to bgFile. Is supposed to
     * be called with a bgFile where .parse has already been called.
     */
    public void testParsedFile4( BgMaxFile bgFile, TestFile testFile )
    {
        assertEquals( "layout name after reading ", "BGMAX", bgFile.iLayoutnamn );
        assertEquals( "iVersion", "01", bgFile.iVersion );
        assertEquals( "iTidsstampel", "20040525173035010331", bgFile.iTidsstampel );
        
        assertEquals( "iAntalBetalningsPoster", "00000009", bgFile.iAntalBetalningsPoster );
        assertEquals( "iAntalAvdragsPoster", "00000000", bgFile.iAntalAvdragsPoster );
        assertEquals( "iAntalExtraReferensPoster", "00000013", bgFile.iAntalExtraReferensPoster );
        assertEquals( "iAntalInsattningsPoster", "00000004", bgFile.iAntalInsattningsPoster );

        BgMaxAvsnitt avs1 = bgFile.iAvsnitts.get( 0 );
        assertEquals( "iBankgiroNummer", "0009912346", avs1.iBankgiroNummer );
        assertEquals( "iValuta", "SEK", avs1.iValuta );
        
        BgMaxBetalning bet1 = avs1.iBetalningar.get( 0 );
        assertEquals( "iBankgiroNummer", "0003783511", bet1.iBankgiroNummer );
        assertEquals( "iReferens", "", bet1.iReferens );
        assertEquals( "iBetalningsBelopp", "000000000000180000", bet1.iBelopp );
        assertEquals( "iReferensKod", "0", bet1.iReferensKod );
        assertEquals( "iBetalningsKanalKod", "2", bet1.iBetalningsKanalKod );
        assertEquals( "iBGCLopnummer", "000120000018", bet1.iBGCLopnummer );
        assertEquals( "iAvibildmarkering", "0", bet1.iAvibildmarkering );
        assertEquals( "iInformationsText", "Betalning med extra refnr 665869 657775 665661665760", bet1.iInformationsText );
        assertEquals( "iBetalarensNamn", "Kalles Pl", bet1.iBetalarensNamn.substring( 0, 9 ) );  // String trunked because of char code issues
        assertEquals( "iBetalarensAdress", "Storgatan 2", bet1.iBetalarensAdress );
        assertEquals( "iBetalarensPostnummer", "12345", bet1.iBetalarensPostnummer );
        assertEquals( "iBetalarensOrt", "Stor", bet1.iBetalarensOrt.substring( 0, 4 ) );
        assertEquals( "iBetalarensOrganisationsnr", "005500001234", bet1.iBetalarensOrganisationsnr );
        
        assertEquals( "iBankKontoNummer", "00000000000000000005841000001009823", avs1.iBankKontoNummer );
        assertEquals( "iBetalningsdag", "20040525", avs1.iBetalningsdag );
        assertEquals( "iLopnummer", "00056", avs1.iLopnummer );
        
        assertEquals( "iBelopp", "000000000000370000", avs1.iBelopp );
        assertEquals( "iValuta", "SEK", avs1.iValuta );
        assertEquals( "iAntal", "00000002", avs1.iAntal );
        
        BgMaxReferens ref1 = bet1.iReferenser.get( 0 );
        assertEquals( "iBankgiroNummer", "0003783511", ref1.iBankgiroNummer );
        assertEquals( "iReferens", "665760", ref1.iReferens );
        assertEquals( "iBelopp", "000000000000000000", ref1.iBelopp );
        assertEquals( "iReferensKod", "2", ref1.iReferensKod );
        assertEquals( "iBetalningsKanalKod", "2", ref1.iBetalningsKanalKod );
        assertEquals( "iBGCLopnummer", "000120000018", ref1.iBGCLopnummer );
        assertEquals( "iAvibildmarkering", "0", ref1.iAvibildmarkering );
    }
 
 
    public class TestFile
    {
        public String name, dir;
        public File file;

        public List<String> lines = new LinkedList<String>();

        public TestFile( String name, String dir ) {
            this.name = name;
            this.dir = dir;
            this.file = new File( dir + File.pathSeparator + this.name );
        }
    }

}
