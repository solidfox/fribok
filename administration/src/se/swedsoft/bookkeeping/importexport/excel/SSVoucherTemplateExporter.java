package se.swedsoft.bookkeeping.importexport.excel;

import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.excel.util.SSWritableExcelSheet;
import se.swedsoft.bookkeeping.importexport.excel.util.SSWritableExcelRow;
import se.swedsoft.bookkeeping.data.SSVoucherTemplate;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.List;

import jxl.WorkbookSettings;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.*;

import static se.swedsoft.bookkeeping.data.SSVoucherTemplate.*;

/**
 * User: Andreas Lago
 * Date: 2006-aug-01
 * Time: 11:32:25
 */
public class SSVoucherTemplateExporter {
    // Column names
    public static final String BESKRIVNING   = "Beskrivning";

    public static final String KONTO         = "Konto";
    public static final String DEBET         = "Debet";
    public static final String KREDIT        = "Kredit";


    private File iFile;
    private List<SSVoucherTemplate> iVouchers;


    /**
     *
     * @param iFile
     */
    public SSVoucherTemplateExporter(File iFile){
      this.iFile = iFile;
      this.iVouchers = SSDB.getInstance().getVoucherTemplates();
    }

    /**
     *
     * @param iFile
     * @param iVouchers
     */
    public SSVoucherTemplateExporter(File iFile, List<SSVoucherTemplate> iVouchers) {
        this.iFile = iFile;
        this.iVouchers = iVouchers;
    }

    /**
     *
     * @throws java.io.IOException
     * @throws se.swedsoft.bookkeeping.importexport.util.SSImportException
     */
    public void export()  throws IOException, SSExportException {
        WorkbookSettings iSettings = new WorkbookSettings();

        iSettings.setLocale               (new Locale("sv", "SE"));
        iSettings.setEncoding             ("windows-1252");
        iSettings.setExcelDisplayLanguage ("SE");
        iSettings.setExcelRegionalSettings("SE");
        WritableWorkbook iWorkbook = null;
        try{
            iWorkbook = Workbook.createWorkbook(iFile, iSettings);

            WritableSheet iSheet = iWorkbook.createSheet("Konteringmallar", 0);

            writeVoucherTemplates(new SSWritableExcelSheet(iSheet)  );

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
    private int getNumRows(List<SSVoucherTemplate> iVouchers){
        int count = 0;
        for (SSVoucherTemplate iVoucher : iVouchers) {
            count = count + iVoucher.getRows ().size() + 1;
        }
        return count;
    }

    /**
     *
     * @param pSheet
     */
    private void writeVoucherTemplates(SSWritableExcelSheet pSheet ) throws WriteException {

        List<SSWritableExcelRow> iRows = pSheet.getRows( getNumRows(iVouchers) + 4  );

        WritableCellFormat iCellFormat = new WritableCellFormat();

        iCellFormat.setBackground( Colour.GRAY_25 );

        iRows.get(0).setString(0, SSVoucherTemplateExporter.BESKRIVNING       , iCellFormat);
        iRows.get(0).setString(1, SSVoucherTemplateExporter.KONTO             , iCellFormat);
        iRows.get(0).setString(2, SSVoucherTemplateExporter.DEBET             , iCellFormat);
        iRows.get(0).setString(3, SSVoucherTemplateExporter.KREDIT            , iCellFormat);

        iCellFormat = new WritableCellFormat();
        WritableFont iFont = new WritableFont( WritableFont.ARIAL, WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD);

        iCellFormat.setFont( iFont );

        int iRowIndex = 1;
        SSWritableExcelRow iRow;
        for (SSVoucherTemplate iVoucher : iVouchers) {
            iRow =  iRows.get(iRowIndex++);
            iRow.setString(0,  iVoucher.getDescription() , iCellFormat);

            for (SSVoucherTemplateRow iVoucherRow : iVoucher.getRows()) {
                iRow =  iRows.get(iRowIndex++);
                iRow.setNumber(1,  iVoucherRow.getAccountNr() );
                iRow.setNumber(2,  iVoucherRow.getDebet() );
                iRow.setNumber(3,  iVoucherRow.getCredit() );
            }
        }


    }



}
