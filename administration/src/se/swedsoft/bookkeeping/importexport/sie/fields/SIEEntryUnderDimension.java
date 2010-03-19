package se.swedsoft.bookkeeping.importexport.sie.fields;

import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.gui.util.SSBundleString;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.types.SIEDimension;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import java.util.List;

import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.INT;
import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.STRING;

/**
 * Date: 2006-feb-23
 * Time: 12:18:47
 */
public class SIEEntryUnderDimension implements SIEEntry {


    /**
     * Imports the entry
     *
     * @param iImporter
     * @param iReader
     * @return If anything was imported
     * @throws SSImportException
     *
     */
    @Override
    public boolean importEntry(SSSIEImporter iImporter, SIEReader iReader, SSNewAccountingYear iYearData) throws SSImportException {
        List<SIEDimension> iDimensions = iImporter.getDimensions();

        if(!iReader.hasFields(STRING, INT, STRING, INT )) {
            throw new SSImportException(SSBundleString.getString("sieimport.fielderror", iReader.peekLine()) );
        }

        SIEDimension iDimension = new SIEDimension();
        iDimension.setNumber        ( iReader.nextInteger() );
        iDimension.setName          ( iReader.nextString() );
        iDimension.setSuperDimension( iReader.nextInteger() );

        if(iDimensions.contains(iDimension)){
            System.out.println("Duplicate dimension:" + iDimension);
            return false;
        }

        iDimensions.add( iDimension );

        return true;
    }

    /**
     * Exports the entry
     *
     * @param iExporter
     * @param iWriter
     * @return If anything was exported
     * @throws SSExportException
     *
     */
    @Override
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter, SSNewAccountingYear iCurrentYearData) throws SSExportException {
        // We only need to use the standard dimensions,
        // 1 for resultunit and
        // 6 for project

        return false;
    }
}

