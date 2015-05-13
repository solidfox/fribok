package se.swedsoft.bookkeeping.importexport.excel;


import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.importexport.excel.util.SSWritableExcelRow;
import se.swedsoft.bookkeeping.importexport.excel.util.SSWritableExcelSheet;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;


/**
 * User: Andreas Lago
 * Date: 2006-aug-01
 * Time: 11:32:25
 * $Id$
 */
public class SSCustomerExporter {
    // Column names
    public static final String KUNDNUMMER = "Kund-id";
    public static final String NAMN = "Namn";
    public static final String TELEFON1 = "Telefon1";
    public static final String TELEFON2 = "Telefon2";
    public static final String FAX = "Fax";
    public static final String EPOST = "Epost";
    public static final String KONTAKTPERSON = "Kontaktperson";
    public static final String ORGANISATIONSNUMMER = "Organisationsnummer";
    public static final String BANKGIRO = "Bankgiro";
    public static final String PLUSGIRO = "Plusgiro";
    public static final String FAKTURAADRESS_NAMN = "Fakturaadress.Namn";
    public static final String FAKTURAADRESS_ADRESS1 = "Fakturaadress.Adress1";
    public static final String FAKTURAADRESS_ADRESS2 = "Fakturaadress.Adress2";
    public static final String FAKTURAADRESS_POSTNUMMER = "Fakturaadress.Postnummer";
    public static final String FAKTURAADRESS_POSTORT = "Fakturaadress.Postort";
    public static final String FAKTURAADRESS_LAND = "Fakturaadress.Land";
    public static final String LEVERANSADRESS_NAMN = "Leveransadress.Namn";
    public static final String LEVERANSADRESS_ADRESS1 = "Leveransadress.Adress1";
    public static final String LEVERANSADRESS_ADRESS2 = "Leveransadress.Adress2";
    public static final String LEVERANSADRESS_POSTNUMMER = "Leveransadress.Postnummer";
    public static final String LEVERANSADRESS_POSTORT = "Leveransadress.Postort";
    public static final String LEVERANSADRESS_LAND = "Leveransadress.Land";

    private File iFile;
    private List<SSCustomer> iCustomers;

    /**
     *
     * @param iFile
     */
    public SSCustomerExporter(File iFile) {
        this.iFile = iFile;
        iCustomers = SSDB.getInstance().getCustomers();
    }

    /**
     *
     * @param iFile
     * @param iCustomers
     */
    public SSCustomerExporter(File iFile, List<SSCustomer> iCustomers) {
        this.iFile = iFile;
        this.iCustomers = iCustomers;
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

            WritableSheet iSheet = iWorkbook.createSheet("Kunder", 0);

            writeCustomers(new SSWritableExcelSheet(iSheet));

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
    private void writeCustomers(SSWritableExcelSheet pSheet) throws WriteException {

        List<SSWritableExcelRow> iRows = pSheet.getRows(iCustomers.size() + 1);

        // Write the column names
        SSWritableExcelRow iColumns = iRows.get(0);

        WritableCellFormat iCellFormat = new WritableCellFormat();

        iCellFormat.setBackground(Colour.GRAY_25);

        iColumns.setString(0, KUNDNUMMER, iCellFormat);
        iColumns.setString(1, NAMN, iCellFormat);
        iColumns.setString(2, TELEFON1, iCellFormat);
        iColumns.setString(3, TELEFON2, iCellFormat);
        iColumns.setString(4, FAX, iCellFormat);
        iColumns.setString(5, EPOST, iCellFormat);
        iColumns.setString(6, KONTAKTPERSON, iCellFormat);
        iColumns.setString(7, ORGANISATIONSNUMMER, iCellFormat);
        iColumns.setString(8, BANKGIRO, iCellFormat);
        iColumns.setString(9, PLUSGIRO, iCellFormat);

        iColumns.setString(10, FAKTURAADRESS_NAMN, iCellFormat);
        iColumns.setString(11, FAKTURAADRESS_ADRESS1, iCellFormat);
        iColumns.setString(12, FAKTURAADRESS_ADRESS2, iCellFormat);
        iColumns.setString(13, FAKTURAADRESS_POSTNUMMER, iCellFormat);
        iColumns.setString(14, FAKTURAADRESS_POSTORT, iCellFormat);
        iColumns.setString(15, FAKTURAADRESS_LAND, iCellFormat);

        iColumns.setString(16, LEVERANSADRESS_NAMN, iCellFormat);
        iColumns.setString(17, LEVERANSADRESS_ADRESS1, iCellFormat);
        iColumns.setString(18, LEVERANSADRESS_ADRESS2, iCellFormat);
        iColumns.setString(19, LEVERANSADRESS_POSTNUMMER, iCellFormat);
        iColumns.setString(20, LEVERANSADRESS_POSTORT, iCellFormat);
        iColumns.setString(21, LEVERANSADRESS_LAND, iCellFormat);

        int iRowIndex = 1;

        for (SSCustomer iCustomer : iCustomers) {
            SSWritableExcelRow iRow = iRows.get(iRowIndex);

            iRow.setString(0, iCustomer.getNumber());
            iRow.setString(1, iCustomer.getName());
            iRow.setString(2, iCustomer.getPhone1());
            iRow.setString(3, iCustomer.getPhone2());
            iRow.setString(4, iCustomer.getTelefax());
            iRow.setString(5, iCustomer.getEMail());
            iRow.setString(6, iCustomer.getYourContactPerson());
            iRow.setString(7, iCustomer.getRegistrationNumber());
            iRow.setString(8, iCustomer.getBankgiro());
            iRow.setString(9, iCustomer.getPlusgiro());

            iRow.setString(1, iCustomer.getInvoiceAddress().getName());
            iRow.setString(11, iCustomer.getInvoiceAddress().getAddress1());
            iRow.setString(12, iCustomer.getInvoiceAddress().getAddress2());
            iRow.setString(13, iCustomer.getInvoiceAddress().getZipCode());
            iRow.setString(14, iCustomer.getInvoiceAddress().getCity());
            iRow.setString(15, iCustomer.getInvoiceAddress().getCountry());

            iRow.setString(16, iCustomer.getDeliveryAddress().getName());
            iRow.setString(17, iCustomer.getDeliveryAddress().getAddress1());
            iRow.setString(18, iCustomer.getDeliveryAddress().getAddress2());
            iRow.setString(19, iCustomer.getDeliveryAddress().getZipCode());
            iRow.setString(20, iCustomer.getDeliveryAddress().getCity());
            iRow.setString(21, iCustomer.getDeliveryAddress().getCountry());
            iRowIndex++;
        }

    }

    public void doXMLExport() {

        Document iXmlDoc = new DocumentImpl();
        Element iRoot = iXmlDoc.createElement("Customers");

        for (SSCustomer iCustomer : iCustomers) {
            Element iElement = iXmlDoc.createElementNS(null, "Customer");

            Element iSubElement = iXmlDoc.createElementNS(null, "CustomerNo");

            iElement.appendChild(iSubElement);
            Node iNode = iXmlDoc.createTextNode(
                    iCustomer.getNumber() == null ? "" : iCustomer.getNumber());

            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "CustomerName");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iCustomer.getName());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "OurContactPerson");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iCustomer.getOurContactPerson());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "YourContactPerson");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iCustomer.getYourContactPerson());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "CurrencyCode");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getInvoiceCurrency() == null
                            ? ""
                            : iCustomer.getInvoiceCurrency().getName());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "PaymentTerms");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getPaymentTerm() == null
                            ? ""
                            : iCustomer.getPaymentTerm().getName());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "DeliveryTerms");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getDeliveryTerm() == null
                            ? ""
                            : iCustomer.getDeliveryTerm().getName());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "DeliveryMethod");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getDeliveryWay() == null
                            ? ""
                            : iCustomer.getDeliveryWay().getName());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "TaxFree");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(Boolean.toString(iCustomer.getTaxFree()));
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "EuSaleCommodity");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    Boolean.toString(iCustomer.getEuSaleCommodity()));
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "EuSaleThirdPartCommodity");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    Boolean.toString(iCustomer.getEuSaleYhirdPartCommodity()));
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "HideUnitPrice");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(Boolean.toString(iCustomer.getHideUnitprice()));
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "VATRegNo");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getVATNumber() == null ? "" : iCustomer.getVATNumber());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "Email");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getEMail() == null ? "" : iCustomer.getEMail());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "CompanyNo");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getRegistrationNumber() == null
                            ? ""
                            : iCustomer.getRegistrationNumber());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "Telefax");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getTelefax() == null ? "" : iCustomer.getTelefax());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "Telephone");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getPhone1() == null ? "" : iCustomer.getPhone1());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "Telephone2");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getPhone2() == null ? "" : iCustomer.getPhone2());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "CreditLimit");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getCreditLimit() == null
                            ? ""
                            : iCustomer.getCreditLimit().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "Discount");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getDiscount() == null
                            ? ""
                            : iCustomer.getDiscount().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "BgNo");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getBankgiro() == null ? "" : iCustomer.getBankgiro());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "PgNo");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getPlusgiro() == null ? "" : iCustomer.getPlusgiro());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "CreditLimit");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getCreditLimit() == null
                            ? ""
                            : iCustomer.getCreditLimit().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "InvoiceName");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getInvoiceAddress() == null
                            ? ""
                            : iCustomer.getInvoiceAddress().getName());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "InvoiceAddress1");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getInvoiceAddress() == null
                            ? ""
                            : iCustomer.getInvoiceAddress().getAddress1());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "InvoiceAddress2");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getInvoiceAddress() == null
                            ? ""
                            : iCustomer.getInvoiceAddress().getAddress2());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "InvoicePostCode");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getInvoiceAddress() == null
                            ? ""
                            : iCustomer.getInvoiceAddress().getZipCode());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "InvoicePostOffice");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getInvoiceAddress() == null
                            ? ""
                            : iCustomer.getInvoiceAddress().getCity());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "InvoiceCountry");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getInvoiceAddress() == null
                            ? ""
                            : iCustomer.getInvoiceAddress().getCountry());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "DeliveryName");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getDeliveryAddress() == null
                            ? ""
                            : iCustomer.getDeliveryAddress().getName());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "DeliveryAddress1");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getDeliveryAddress() == null
                            ? ""
                            : iCustomer.getDeliveryAddress().getAddress1());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "DeliveryAddress2");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getDeliveryAddress() == null
                            ? ""
                            : iCustomer.getDeliveryAddress().getAddress2());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "DeliveryPostCode");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getDeliveryAddress() == null
                            ? ""
                            : iCustomer.getDeliveryAddress().getZipCode());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "DeliveryPostOffice");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getDeliveryAddress() == null
                            ? ""
                            : iCustomer.getDeliveryAddress().getCity());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null, "DeliveryCountry");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(
                    iCustomer.getDeliveryAddress() == null
                            ? ""
                            : iCustomer.getDeliveryAddress().getCountry());
            iSubElement.appendChild(iNode);

            iRoot.appendChild(iElement);
        }
        iXmlDoc.appendChild(iRoot);
        try {
            FileOutputStream fos = new FileOutputStream(iFile.getAbsolutePath());
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            OutputFormat of = new OutputFormat("XML", "UTF-8", true);

            of.setIndent(1);
            of.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(osw, of);

            serializer.asDOMSerializer();
            serializer.serialize(iXmlDoc.getDocumentElement());

            fos.close();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.importexport.excel.SSCustomerExporter");
        sb.append("{iCustomers=").append(iCustomers);
        sb.append(", iFile=").append(iFile);
        sb.append('}');
        return sb.toString();
    }
}
