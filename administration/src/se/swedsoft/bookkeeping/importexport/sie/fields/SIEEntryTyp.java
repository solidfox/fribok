package se.swedsoft.bookkeeping.importexport.sie.fields;

import se.swedsoft.bookkeeping.importexport.sie.util.SIELabel;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEReader;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEWriter;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEType;
import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.STRING;
import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.INT;
import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.ARRAY;
import static se.swedsoft.bookkeeping.importexport.sie.util.SIEReader.SIEDataType.FLOAT;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEImporter;
import se.swedsoft.bookkeeping.importexport.sie.SSSIEExporter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;
import se.swedsoft.bookkeeping.gui.util.SSBundleString;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;

/**
 * Date: 2006-feb-20
 * Time: 15:46:40
 */
public class SIEEntryTyp extends SIEEntry {

    /**
     * Imports the field
     *
     * @param iReader
     */
    @Override
    public boolean importEntry(SSSIEImporter iImporter, SIEReader iReader, SSNewAccountingYear iYearData) throws SSImportException {

        // #SIETYP typ
        if(!iReader.hasFields(STRING, INT )) {
            throw new SSImportException(SSBundleString.getString("sieimport.fielderror", iReader.peekLine()) );
        }
        SIEType iType = SIEType.decode( iReader.next() );

        iImporter.setType( iType );

        return true;
    }

    /**
     * Exports the field
     *
     * @param iWriter
     */
    @Override
    public boolean exportEntry(SSSIEExporter iExporter, SIEWriter iWriter , SSNewAccountingYear iCurrentYearData) throws SSExportException {
        iWriter.append( SIELabel.SIE_SIETYP   );
        iWriter.append( iExporter.getType() );
        iWriter.newLine();

        return true;
    }

}
