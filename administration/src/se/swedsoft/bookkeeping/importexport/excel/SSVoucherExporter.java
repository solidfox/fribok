package se.swedsoft.bookkeeping.importexport.excel;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.*;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.data.SSVoucherRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.importexport.excel.util.SSWritableExcelRow;
import se.swedsoft.bookkeeping.importexport.excel.util.SSWritableExcelSheet;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * User: Andreas Lago
 * Date: 2006-aug-01
 * Time: 11:32:25
 */
public class SSVoucherExporter {
    // Column names
    public static final String NUMMER       = "Nummer";
    public static final String BESKRIVNING  = "Beskrivning";

    public static final String DATUM        = "Datum";

    public static final String KONTO        = "Konto";
    public static final String DEBET        = "Debet";
    public static final String KREDIT       = "Kredit";
    public static final String PROJEKT      = "Projekt";
    public static final String RESULTATENHET = "Resultatenhet";


    private File iFile;
    private List<SSVoucher> iVouchers;


    /**
     *
     * @param iFile
     */
    public SSVoucherExporter(File iFile){
      this.iFile = iFile;
        iVouchers = SSDB.getInstance().getVouchers();
    }

    /**
     *
     * @param iFile
     * @param iVouchers
     */
    public SSVoucherExporter(File iFile, List<SSVoucher> iVouchers) {
        this.iFile     = iFile;
        this.iVouchers = iVouchers;
    }

    /**
     *
     * @throws IOException
     * @throws SSImportException
     * @throws SSExportException
     */
    public void export()  throws IOException, SSExportException {
        WorkbookSettings iSettings = new WorkbookSettings();

        iSettings.setLocale               (new Locale("sv", "SE"));
        iSettings.setEncoding             ("windows-1252");
        iSettings.setExcelDisplayLanguage ("SE");
        iSettings.setExcelRegionalSettings("SE");

        try{
            WritableWorkbook iWorkbook = Workbook.createWorkbook(iFile, iSettings);

            WritableSheet iSheet = iWorkbook.createSheet("Verifikationer", 0);

            writeVouchers(new SSWritableExcelSheet(iSheet)  );

            iWorkbook.write();
            iWorkbook.close();

        }catch( WriteException e){
            throw new SSExportException( e.getLocalizedMessage() );
        }

    }

    /**
     *
     * @param iVouchers
     * @return
     */
    private int getNumRows(List<SSVoucher> iVouchers){
        int count = 0;
        for (SSVoucher iVoucher : iVouchers) {
            count = count + iVoucher.getRows().size() + 1;
        }
        return count;
    }

    /**
     *
     * @param pSheet
     * @throws WriteException
     */
    private void writeVouchers(SSWritableExcelSheet pSheet ) throws WriteException {
        List<SSWritableExcelRow> iRows = pSheet.getRows( getNumRows(iVouchers) + 4  );

        WritableCellFormat iCellFormat = new WritableCellFormat();

        iCellFormat.setBackground( Colour.GRAY_25 );

        iRows.get(0).setString(0, NUMMER            , iCellFormat);
        iRows.get(0).setString(1, BESKRIVNING       , iCellFormat);
        iRows.get(0).setString(2, DATUM             , iCellFormat);
        iRows.get(0).setString(3, KONTO             , iCellFormat);
        iRows.get(0).setString(4, DEBET             , iCellFormat);
        iRows.get(0).setString(5, KREDIT            , iCellFormat);
        iRows.get(0).setString(6, PROJEKT           , iCellFormat);
        iRows.get(0).setString(7, RESULTATENHET     , iCellFormat);

        iCellFormat = new WritableCellFormat();
        WritableFont iFont = new WritableFont( WritableFont.ARIAL, WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD);

        iCellFormat.setFont( iFont );

        int iRowIndex = 1;
        SSWritableExcelRow iRow;
        for (SSVoucher iVoucher : iVouchers) {
            iRow =  iRows.get(iRowIndex++);
            iRow.setNumber(0,  iVoucher.getNumber()      , iCellFormat);
            iRow.setString(1,  iVoucher.getDescription() , iCellFormat);
            iRow.setDate  (2,  iVoucher.getDate()        , iCellFormat);

            for (SSVoucherRow iVoucherRow : iVoucher.getRows()) {

                if(iVoucherRow.isCrossed()) continue;

                iRow =  iRows.get(iRowIndex++);
                iRow.setNumber(3,  iVoucherRow.getAccountNr() );
                iRow.setNumber(4,  iVoucherRow.getDebet() );
                iRow.setNumber(5,  iVoucherRow.getCredit() );
                iRow.setString(6,  iVoucherRow.getProjectNr() );
                iRow.setString(7,  iVoucherRow.getResultUnitNr() );
            }
        }


    }



}
