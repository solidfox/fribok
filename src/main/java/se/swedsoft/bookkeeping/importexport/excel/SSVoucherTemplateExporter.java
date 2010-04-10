package se.swedsoft.bookkeeping.importexport.excel;


import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.*;
import se.swedsoft.bookkeeping.data.SSVoucherTemplate;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.importexport.excel.util.SSWritableExcelRow;
import se.swedsoft.bookkeeping.importexport.excel.util.SSWritableExcelSheet;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static se.swedsoft.bookkeeping.data.SSVoucherTemplate.SSVoucherTemplateRow;


/**
 * User: Andreas Lago
 * Date: 2006-aug-01
 * Time: 11:32:25
 */
public class SSVoucherTemplateExporter {
    // Column names
    public static final String BESKRIVNING = "Beskrivning";

    public static final String KONTO = "Konto";
    public static final String DEBET = "Debet";
    public static final String KREDIT = "Kredit";

    private File iFile;
    private List<SSVoucherTemplate> iVouchers;

    /**
     *
     * @param iFile
     */
    public SSVoucherTemplateExporter(File iFile) {
        this.iFile = iFile;
        iVouchers = SSDB.getInstance().getVoucherTemplates();
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
     * @throws IOException
     * @throws SSImportException
     * @throws SSExportException
     */
    public void export()  throws IOException, SSExportException {
        WorkbookSettings iSettings = new WorkbookSettings();

        iSettings.setLocale(new Locale("sv", "SE"));
        iSettings.setEncoding("windows-1252");
        iSettings.setExcelDisplayLanguage("SE");
        iSettings.setExcelRegionalSettings("SE");
        try {
            WritableWorkbook iWorkbook = Workbook.createWorkbook(iFile, iSettings);

            WritableSheet iSheet = iWorkbook.createSheet("Konteringmallar", 0);

            writeVoucherTemplates(new SSWritableExcelSheet(iSheet));

            iWorkbook.write();
            iWorkbook.close();

        } catch (WriteException e) {
            throw new SSExportException(e.getLocalizedMessage());
        }

    }

    /**
     *
     * @param iVouchers
     * @return
     */
    private int getNumRows(List<SSVoucherTemplate> iVouchers) {
        int count = 0;

        for (SSVoucherTemplate iVoucher : iVouchers) {
            count = count + iVoucher.getRows().size() + 1;
        }
        return count;
    }

    /**
     *
     * @param pSheet
     * @throws WriteException
     */
    private void writeVoucherTemplates(SSWritableExcelSheet pSheet) throws WriteException {

        List<SSWritableExcelRow> iRows = pSheet.getRows(getNumRows(iVouchers) + 4);

        WritableCellFormat iCellFormat = new WritableCellFormat();

        iCellFormat.setBackground(Colour.GRAY_25);

        iRows.get(0).setString(0, BESKRIVNING, iCellFormat);
        iRows.get(0).setString(1, KONTO, iCellFormat);
        iRows.get(0).setString(2, DEBET, iCellFormat);
        iRows.get(0).setString(3, KREDIT, iCellFormat);

        iCellFormat = new WritableCellFormat();
        WritableFont iFont = new WritableFont(WritableFont.ARIAL,
                WritableFont.DEFAULT_POINT_SIZE, WritableFont.BOLD);

        iCellFormat.setFont(iFont);

        int iRowIndex = 1;

        for (SSVoucherTemplate iVoucher : iVouchers) {
            iRowIndex++;
            SSWritableExcelRow iRow = iRows.get(iRowIndex);

            iRow.setString(0, iVoucher.getDescription(), iCellFormat);

            for (SSVoucherTemplateRow iVoucherRow : iVoucher.getRows()) {
                iRowIndex++;
                iRow = iRows.get(iRowIndex);
                iRow.setNumber(1, iVoucherRow.getAccountNr());
                iRow.setNumber(2, iVoucherRow.getDebet());
                iRow.setNumber(3, iVoucherRow.getCredit());
            }
        }

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.importexport.excel.SSVoucherTemplateExporter");
        sb.append("{iFile=").append(iFile);
        sb.append(", iVouchers=").append(iVouchers);
        sb.append('}');
        return sb.toString();
    }
}
