package se.swedsoft.bookkeeping.importexport.excel;


import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import se.swedsoft.bookkeeping.data.SSSupplier;
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
public class SSSupplierExporter {
    // Column names
    public static final String LEVERANTORSNUMMER = "Leverant�rsnummer";
    public static final String NAMN = "Namn";
    public static final String TELEFON1 = "Telefon1";
    public static final String TELEFON2 = "Telefon2";
    public static final String FAX = "Fax";
    public static final String EPOST = "EPost";
    public static final String HEMSIDA = "Hemsida";
    public static final String KONTAKTPERSON = "Kontaktperson";
    public static final String ORGANISATIONSNUMMER = "Organisationsnummer";
    public static final String VART_KUNDNUMMER = "V�rt kundnummer";
    public static final String BANKGIRO = "Bankgiro";
    public static final String PLUSGIRO = "Plusgiro";
    public static final String ADRESS_NAMN = "Adress.Namn";
    public static final String ADRESS_ADRESS1 = "Adress.Adress1";
    public static final String ADRESS_ADRESS2 = "Adress.Adress2";
    public static final String ADRESS_POSTNUMMER = "Adress.Postnummer";
    public static final String ADRESS_POSTORT = "Adress.Postort";
    public static final String ADRESS_LAND = "Adress.Land";

    private File iFile;
    private List<SSSupplier> iSuppliers;

    /**
     *
     * @param iFile
     */
    public SSSupplierExporter(File iFile) {
        this.iFile = iFile;
        iSuppliers = SSDB.getInstance().getSuppliers();
    }

    /**
     *
     * @param iFile
     * @param iSuppliers
     */
    public SSSupplierExporter(File iFile, List<SSSupplier> iSuppliers) {
        this.iFile = iFile;
        this.iSuppliers = iSuppliers;
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

            WritableSheet iSheet = iWorkbook.createSheet("Leverant�rer", 0);

            writeSuppliers(new SSWritableExcelSheet(iSheet));

            iWorkbook.write();
            iWorkbook.close();

        } catch (WriteException e) {
            throw new SSExportException(e.getLocalizedMessage());
        }

    }

    /**
     *
     * @param pSheet
     * @throws WriteException
     */
    private void writeSuppliers(SSWritableExcelSheet pSheet) throws WriteException {

        List<SSWritableExcelRow> iRows = pSheet.getRows(iSuppliers.size() + 1);

        // Write the column names
        SSWritableExcelRow iColumns = iRows.get(0);

        WritableCellFormat iCellFormat = new WritableCellFormat();

        iCellFormat.setBackground(Colour.GRAY_25);

        iColumns.setString(0, LEVERANTORSNUMMER, iCellFormat);
        iColumns.setString(1, NAMN, iCellFormat);
        iColumns.setString(2, TELEFON1, iCellFormat);
        iColumns.setString(3, TELEFON2, iCellFormat);
        iColumns.setString(4, FAX, iCellFormat);
        iColumns.setString(5, EPOST, iCellFormat);
        iColumns.setString(6, HEMSIDA, iCellFormat);
        iColumns.setString(7, KONTAKTPERSON, iCellFormat);
        iColumns.setString(8, ORGANISATIONSNUMMER, iCellFormat);
        iColumns.setString(9, VART_KUNDNUMMER, iCellFormat);
        iColumns.setString(10, BANKGIRO, iCellFormat);
        iColumns.setString(11, PLUSGIRO, iCellFormat);
        iColumns.setString(12, ADRESS_NAMN, iCellFormat);
        iColumns.setString(13, ADRESS_ADRESS1, iCellFormat);
        iColumns.setString(14, ADRESS_ADRESS2, iCellFormat);
        iColumns.setString(15, ADRESS_POSTNUMMER, iCellFormat);
        iColumns.setString(16, ADRESS_POSTORT, iCellFormat);
        iColumns.setString(17, ADRESS_LAND, iCellFormat);

        int iRowIndex = 1;

        for (SSSupplier iSupplier : iSuppliers) {
            SSWritableExcelRow iRow = iRows.get(iRowIndex);

            iRow.setString(0, iSupplier.getNumber());
            iRow.setString(1, iSupplier.getName());
            iRow.setString(2, iSupplier.getPhone1());
            iRow.setString(3, iSupplier.getPhone2());
            iRow.setString(4, iSupplier.getTelefax());
            iRow.setString(5, iSupplier.getEMail());
            iRow.setString(6, iSupplier.getHomepage());
            iRow.setString(7, iSupplier.getYourContact());
            iRow.setString(8, iSupplier.getRegistrationNumber());
            iRow.setString(9, iSupplier.getOurCustomerNr());
            iRow.setString(10, iSupplier.getBankgiro());
            iRow.setString(11, iSupplier.getPlusgiro());

            iRow.setString(12, iSupplier.getAddress().getName());
            iRow.setString(13, iSupplier.getAddress().getAddress1());
            iRow.setString(14, iSupplier.getAddress().getAddress2());
            iRow.setString(15, iSupplier.getAddress().getZipCode());
            iRow.setString(16, iSupplier.getAddress().getCity());
            iRow.setString(17, iSupplier.getAddress().getCountry());

            iRowIndex++;
        }

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.importexport.excel.SSSupplierExporter");
        sb.append("{iFile=").append(iFile);
        sb.append(", iSuppliers=").append(iSuppliers);
        sb.append('}');
        return sb.toString();
    }
}
