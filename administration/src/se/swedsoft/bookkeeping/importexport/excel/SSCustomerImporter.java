package se.swedsoft.bookkeeping.importexport.excel;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import se.swedsoft.bookkeeping.data.SSAddress;
import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.common.SSDeliveryTerm;
import se.swedsoft.bookkeeping.data.common.SSDeliveryWay;
import se.swedsoft.bookkeeping.data.common.SSPaymentTerm;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.util.SSFileSystem;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSInformationDialog;
import se.swedsoft.bookkeeping.importexport.dialog.SSImportReportDialog;
import se.swedsoft.bookkeeping.importexport.excel.util.SSExcelCell;
import se.swedsoft.bookkeeping.importexport.excel.util.SSExcelRow;
import se.swedsoft.bookkeeping.importexport.excel.util.SSExcelSheet;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Date: 2006-feb-13
 * Time: 16:43:09
 */
public class SSCustomerImporter {

    private File iFile;

    private Map<String, Integer> iColumns;

    /**
     *
     * @param iFile
     */
    public SSCustomerImporter(File iFile) {
        this.iFile = iFile;
        this.iColumns = new HashMap<String, Integer>();
    }


    /**
     *
     */
    public void Import()  throws SSImportException {
        WorkbookSettings iSettings = new WorkbookSettings();

        iSettings.setLocale               (new Locale("sv", "SE"));
        iSettings.setEncoding             ("windows-1252");
        iSettings.setExcelDisplayLanguage ("SE");
        iSettings.setExcelRegionalSettings("SE");

        List<SSCustomer> iCustomers = null;

        try{
            Workbook iWorkbook = Workbook.getWorkbook(iFile, iSettings);

            // Empty workbook, ie nothing to import
            if(iWorkbook.getNumberOfSheets() == 0){
                throw new SSImportException(SSBundle.getBundle(), "customerframe.import.nosheets");
            }

            Sheet iSheet = iWorkbook.getSheet(0);

            iCustomers = importCustomers(new SSExcelSheet(iSheet));

            iWorkbook.close();

        }catch( BiffException e){
            throw new SSImportException( e.getLocalizedMessage() );
        } catch (IOException e1){
            throw new SSImportException( e1.getLocalizedMessage() );
        }
        boolean iResult = showImportReport(iCustomers);

        if( iCustomers != null &&  iResult){
            for (SSCustomer iCustomer : iCustomers) {
                if( ! SSDB.getInstance().getCustomers().contains(iCustomer) ){
                    SSDB.getInstance().addCustomer(iCustomer);
                }

            }
        }
    }



    /**
     *
     * @param iColumns
     */
    private void getColumnIndexes(SSExcelRow iColumns) {
        int iIndex = 0;

        this.iColumns.clear();
        for (SSExcelCell iColumn : iColumns.getCells()) {
            String iName = iColumn.getString();

            if(iName != null && iName.length() > 0) {

                if( iName.equalsIgnoreCase( SSCustomerExporter.KUNDNUMMER               ) ) this.iColumns.put(SSCustomerExporter.KUNDNUMMER                 , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.NAMN                     ) ) this.iColumns.put(SSCustomerExporter.NAMN                       , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.TELEFON1                 ) ) this.iColumns.put(SSCustomerExporter.TELEFON1                   , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.TELEFON2                 ) ) this.iColumns.put(SSCustomerExporter.TELEFON2                   , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.FAX                      ) ) this.iColumns.put(SSCustomerExporter.FAX                        , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.EPOST                    ) ) this.iColumns.put(SSCustomerExporter.EPOST                      , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.KONTAKTPERSON            ) ) this.iColumns.put(SSCustomerExporter.KONTAKTPERSON              , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.ORGANISATIONSNUMMER      ) ) this.iColumns.put(SSCustomerExporter.ORGANISATIONSNUMMER        , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.BANKGIRO                 ) ) this.iColumns.put(SSCustomerExporter.BANKGIRO                   , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.PLUSGIRO                 ) ) this.iColumns.put(SSCustomerExporter.PLUSGIRO                   , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.FAKTURAADRESS_NAMN       ) ) this.iColumns.put(SSCustomerExporter.FAKTURAADRESS_NAMN         , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.FAKTURAADRESS_ADRESS1    ) ) this.iColumns.put(SSCustomerExporter.FAKTURAADRESS_ADRESS1      , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.FAKTURAADRESS_ADRESS2    ) ) this.iColumns.put(SSCustomerExporter.FAKTURAADRESS_ADRESS2      , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.FAKTURAADRESS_POSTNUMMER ) ) this.iColumns.put(SSCustomerExporter.FAKTURAADRESS_POSTNUMMER   , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.FAKTURAADRESS_POSTORT    ) ) this.iColumns.put(SSCustomerExporter.FAKTURAADRESS_POSTORT      , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.FAKTURAADRESS_LAND       ) ) this.iColumns.put(SSCustomerExporter.FAKTURAADRESS_LAND         , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.LEVERANSADRESS_NAMN      ) ) this.iColumns.put(SSCustomerExporter.LEVERANSADRESS_NAMN        , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.LEVERANSADRESS_ADRESS1   ) ) this.iColumns.put(SSCustomerExporter.LEVERANSADRESS_ADRESS1     , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.LEVERANSADRESS_ADRESS2   ) ) this.iColumns.put(SSCustomerExporter.LEVERANSADRESS_ADRESS2     , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.LEVERANSADRESS_POSTNUMMER) ) this.iColumns.put(SSCustomerExporter.LEVERANSADRESS_POSTNUMMER  , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.LEVERANSADRESS_POSTORT   ) ) this.iColumns.put(SSCustomerExporter.LEVERANSADRESS_POSTORT     , iIndex); else
                if( iName.equalsIgnoreCase( SSCustomerExporter.LEVERANSADRESS_LAND      ) ) this.iColumns.put(SSCustomerExporter.LEVERANSADRESS_LAND        , iIndex); else
                 throw new SSImportException( "Ogiltigt kolumnnamn i importfilen: %s", iName);

            }
            iIndex++;
        }
    }

    /**
     *
     * @param pSheet
     */
    private List<SSCustomer> importCustomers(SSExcelSheet pSheet ){

        List<SSExcelRow> iRows = pSheet.getRows( );

        if( iRows.size() < 2 ) throw new SSImportException(SSBundle.getBundle(), "customerframe.import.norows");

        getColumnIndexes(iRows.get(0));

        List<SSCustomer> iCustomers = new LinkedList<SSCustomer>();

        for(int row = 1; row < iRows.size(); row++){
            SSExcelRow iRow = iRows.get(row);
            // Skip empty rows
            if( iRow.empty() ) continue;

            List<SSExcelCell> iCells = iRow.getCells();

            SSCustomer iCustomer = new SSCustomer();

            // Get the cell
            for(int col = 0; col < iCells.size(); col++){
                SSExcelCell iCell = iCells.get(col);

                String iValue = iCell.getString();

                if( iColumns.containsKey(SSCustomerExporter.KUNDNUMMER                               ) && iColumns.get(SSCustomerExporter.KUNDNUMMER                              ) == col ) iCustomer.setNumber ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.NAMN                                     ) && iColumns.get(SSCustomerExporter.NAMN                                    ) == col ) iCustomer.setName ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.TELEFON1                                 ) && iColumns.get(SSCustomerExporter.TELEFON1                                ) == col ) iCustomer.setPhone1 ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.TELEFON2                                 ) && iColumns.get(SSCustomerExporter.TELEFON2                                ) == col ) iCustomer.setPhone2 ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.FAX                                      ) && iColumns.get(SSCustomerExporter.FAX                                     ) == col ) iCustomer.setTelefax ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.EPOST                                    ) && iColumns.get(SSCustomerExporter.EPOST                                   ) == col ) iCustomer.setEMail ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.KONTAKTPERSON                            ) && iColumns.get(SSCustomerExporter.KONTAKTPERSON                           ) == col ) iCustomer.setYourContactPerson ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.ORGANISATIONSNUMMER                      ) && iColumns.get(SSCustomerExporter.ORGANISATIONSNUMMER                     ) == col ) iCustomer.setRegistrationNumber( iValue );
                if( iColumns.containsKey(SSCustomerExporter.BANKGIRO                                 ) && iColumns.get(SSCustomerExporter.BANKGIRO                                ) == col ) iCustomer.setBankgiro ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.PLUSGIRO                                 ) && iColumns.get(SSCustomerExporter.PLUSGIRO                                ) == col ) iCustomer.setPlusgiro ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.FAKTURAADRESS_NAMN                       ) && iColumns.get(SSCustomerExporter.FAKTURAADRESS_NAMN                      ) == col ) iCustomer.getInvoiceAddress().setName ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.FAKTURAADRESS_ADRESS1                    ) && iColumns.get(SSCustomerExporter.FAKTURAADRESS_ADRESS1                   ) == col ) iCustomer.getInvoiceAddress().setAddress1 ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.FAKTURAADRESS_ADRESS2                    ) && iColumns.get(SSCustomerExporter.FAKTURAADRESS_ADRESS2                   ) == col ) iCustomer.getInvoiceAddress().setAddress2 ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.FAKTURAADRESS_POSTNUMMER                 ) && iColumns.get(SSCustomerExporter.FAKTURAADRESS_POSTNUMMER                ) == col ) iCustomer.getInvoiceAddress().setZipCode ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.FAKTURAADRESS_POSTORT                    ) && iColumns.get(SSCustomerExporter.FAKTURAADRESS_POSTORT                   ) == col ) iCustomer.getInvoiceAddress().setCity ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.FAKTURAADRESS_LAND                       ) && iColumns.get(SSCustomerExporter.FAKTURAADRESS_LAND                      ) == col ) iCustomer.getInvoiceAddress().setCountry ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.LEVERANSADRESS_NAMN                      ) && iColumns.get(SSCustomerExporter.LEVERANSADRESS_NAMN                     ) == col ) iCustomer.getDeliveryAddress().setName ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.LEVERANSADRESS_ADRESS1                   ) && iColumns.get(SSCustomerExporter.LEVERANSADRESS_ADRESS1                  ) == col ) iCustomer.getDeliveryAddress().setAddress1 ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.LEVERANSADRESS_ADRESS2                   ) && iColumns.get(SSCustomerExporter.LEVERANSADRESS_ADRESS2                  ) == col ) iCustomer.getDeliveryAddress().setAddress2 ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.LEVERANSADRESS_POSTNUMMER                ) && iColumns.get(SSCustomerExporter.LEVERANSADRESS_POSTNUMMER               ) == col ) iCustomer.getDeliveryAddress().setZipCode ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.LEVERANSADRESS_POSTORT                   ) && iColumns.get(SSCustomerExporter.LEVERANSADRESS_POSTORT                  ) == col ) iCustomer.getDeliveryAddress().setCity ( iValue );
                if( iColumns.containsKey(SSCustomerExporter.LEVERANSADRESS_LAND                      ) && iColumns.get(SSCustomerExporter.LEVERANSADRESS_LAND                     ) == col ) iCustomer.getDeliveryAddress().setCountry ( iValue );
            }
            if(iCustomer.getNumber() != null && iCustomer.getNumber().length() > 0 )iCustomers.add(iCustomer);
        }
        return iCustomers;
    }


    /**
     *
     * @param iCustomers
     */
    private boolean showImportReport(List<SSCustomer> iCustomers){
        SSImportReportDialog iDialog = new SSImportReportDialog(SSMainFrame.getInstance(), SSBundle.getBundle().getString("customerframe.import.report"));
        // Generate the import dialog
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("Följande kolumner har importerats från kundfilen:<br>");
        sb.append("<ul>");
        if(iColumns.containsKey(SSCustomerExporter.KUNDNUMMER               )) sb.append("<li>").append(SSCustomerExporter.KUNDNUMMER ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.NAMN                     )) sb.append("<li>").append(SSCustomerExporter.NAMN ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.TELEFON1                 )) sb.append("<li>").append(SSCustomerExporter.TELEFON1 ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.TELEFON2                 )) sb.append("<li>").append(SSCustomerExporter.TELEFON2 ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.FAX                      )) sb.append("<li>").append(SSCustomerExporter.FAX ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.EPOST                    )) sb.append("<li>").append(SSCustomerExporter.EPOST ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.KONTAKTPERSON            )) sb.append("<li>").append(SSCustomerExporter.KONTAKTPERSON ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.ORGANISATIONSNUMMER      )) sb.append("<li>").append(SSCustomerExporter.ORGANISATIONSNUMMER ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.BANKGIRO                 )) sb.append("<li>").append(SSCustomerExporter.BANKGIRO ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.PLUSGIRO                 )) sb.append("<li>").append(SSCustomerExporter.PLUSGIRO ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.FAKTURAADRESS_NAMN       )) sb.append("<li>").append(SSCustomerExporter.FAKTURAADRESS_NAMN ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.FAKTURAADRESS_ADRESS1    )) sb.append("<li>").append(SSCustomerExporter.FAKTURAADRESS_ADRESS1 ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.FAKTURAADRESS_ADRESS2    )) sb.append("<li>").append(SSCustomerExporter.FAKTURAADRESS_ADRESS2 ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.FAKTURAADRESS_POSTNUMMER )) sb.append("<li>").append(SSCustomerExporter.FAKTURAADRESS_POSTNUMMER ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.FAKTURAADRESS_POSTORT    )) sb.append("<li>").append(SSCustomerExporter.FAKTURAADRESS_POSTORT ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.FAKTURAADRESS_LAND       )) sb.append("<li>").append(SSCustomerExporter.FAKTURAADRESS_LAND ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.LEVERANSADRESS_NAMN      )) sb.append("<li>").append(SSCustomerExporter.LEVERANSADRESS_NAMN ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.LEVERANSADRESS_ADRESS1   )) sb.append("<li>").append(SSCustomerExporter.LEVERANSADRESS_ADRESS1 ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.LEVERANSADRESS_ADRESS2   )) sb.append("<li>").append(SSCustomerExporter.LEVERANSADRESS_ADRESS2 ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.LEVERANSADRESS_POSTNUMMER)) sb.append("<li>").append(SSCustomerExporter.LEVERANSADRESS_POSTNUMMER ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.LEVERANSADRESS_POSTORT   )) sb.append("<li>").append(SSCustomerExporter.LEVERANSADRESS_POSTORT ).append("</li>");
        if(iColumns.containsKey(SSCustomerExporter.LEVERANSADRESS_LAND      )) sb.append("<li>").append(SSCustomerExporter.LEVERANSADRESS_LAND ).append("</li>");

        sb.append("</ul>");
        sb.append("Följande kunder kommer att importeras:<br>");

        sb.append("<ul>");
        for (SSCustomer iCustomer : iCustomers) {
            sb.append("<li>");
            sb.append(iCustomer);
            sb.append("</li>");
        }
        sb.append("</ul>");

        sb.append("Fortsätt med importeringen ?");
        sb.append("</html>");


        iDialog.setText( sb.toString() );
        iDialog.setSize( 640, 480);
        iDialog.setLocationRelativeTo(SSMainFrame.getInstance());

        return iDialog.showDialog() == JOptionPane.OK_OPTION;
    }


    public void doImport() throws SSImportException{
        SSCustomer iCustomer;
        List<SSCustomer> iCustomers = new LinkedList<SSCustomer>();
        try {
            DocumentBuilderFactory iDocBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder iDocBuilder = iDocBuilderFactory.newDocumentBuilder();
            Document iDoc = iDocBuilder.parse (iFile.getAbsolutePath());

            iDoc.getDocumentElement().normalize ();

            if (!iDoc.getDocumentElement().getNodeName().equals("Customers")) {
                throw new SSImportException("Filen innehåller inga kunder");
            }

            NodeList iCustomerList = iDoc.getElementsByTagName("Customer");
            if (iCustomerList.getLength() == 0) {
                throw new SSImportException("Filen innehåller inga kunder");
            }

            for (int i = 0; i < iCustomerList.getLength() ; i++) {
                iCustomer = new SSCustomer();

                Node iCustomerNode = iCustomerList.item(i);
                if(iCustomerNode.getNodeType() == Node.ELEMENT_NODE){

                    NodeList iTextCustomerAttList = null;
                    String iValue = null;
                    Element iCustomerElement = (Element)iCustomerNode;

                    // Kundnummer
                    NodeList iCustomerAttList = iCustomerElement.getElementsByTagName("CustomerNo");
                    Element iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setNumber(iValue == null ? "" : iValue);
                    }

                    iCustomerAttList = iCustomerElement.getElementsByTagName("CustomerName");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setName(iValue == null ? "" : iValue);
                    }

                    // Valuta
                    iCustomerAttList = iCustomerElement.getElementsByTagName("CurrencyCode");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        SSCurrency iCurrency = getCurrency(iValue);
                        iCustomer.setInvoiceCurrency(iCurrency);
                    }


                    // Vår kontaktperson
                    iCustomerAttList = iCustomerElement.getElementsByTagName("OurContactPerson");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setOurContactPerson(iValue);
                    }

                    // Er kontaktperson
                    iCustomerAttList = iCustomerElement.getElementsByTagName("YourContactPerson");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setYourContactPerson(iValue);
                    }

                    // Betalningsvillkor
                    iCustomerAttList = iCustomerElement.getElementsByTagName("PaymentTerms");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setPaymentTerm(getPaymentTerm(iValue));
                    }

                    // Leveransvillkor
                    iCustomerAttList = iCustomerElement.getElementsByTagName("DeliveryTerms");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setDeliveryTerm(getDeliveryTerm(iValue));
                    }

                    // Leveranssätt
                    iCustomerAttList = iCustomerElement.getElementsByTagName("DeliveryMethod");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setDeliveryWay(getDeliveryWay(iValue));
                    }

                    // Momsfri
                    iCustomerAttList = iCustomerElement.getElementsByTagName("TaxFree");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setTaxFree(Boolean.valueOf(iValue));
                    }

                    // EU-försäljning
                    iCustomerAttList = iCustomerElement.getElementsByTagName("EuSaleCommodity");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setEuSaleCommodity(Boolean.valueOf(iValue));
                    }

                    // EU-försäljning 3e-part
                    iCustomerAttList = iCustomerElement.getElementsByTagName("EuSaleThirdPartCommodity");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setEuSaleYhirdPartCommodity(Boolean.valueOf(iValue));
                    }

                    // Vat. nr
                    iCustomerAttList = iCustomerElement.getElementsByTagName("VATRegNo");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setVATNumber(iValue);
                    }

                    //  E-post
                    iCustomerAttList = iCustomerElement.getElementsByTagName("Email");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setEMail(iValue);
                    }

                    // Org. nr
                    iCustomerAttList = iCustomerElement.getElementsByTagName("CompanyNo");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setRegistrationNumber(iValue);
                    }

                    // Faxnummer
                    iCustomerAttList = iCustomerElement.getElementsByTagName("Telefax");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if(iFirstCustomerAttElement != null){
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setTelefax(iValue);
                    }

                    // Telefon
                    iCustomerAttList = iCustomerElement.getElementsByTagName("Telephone");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setPhone1(iValue);
                    }

                    // Telefon2
                    iCustomerAttList = iCustomerElement.getElementsByTagName("Telephone2");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setPhone2(iValue);
                    }

                    // Fakturaadress namn
                    SSAddress iInvoiceAddress = new SSAddress();
                    iCustomerAttList = iCustomerElement.getElementsByTagName("InvoiceName");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iInvoiceAddress.setName(iValue);
                    }

                    // Fakturaadress adress1
                    iCustomerAttList = iCustomerElement.getElementsByTagName("InvoiceAddress1");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iInvoiceAddress.setAddress1(iValue);
                    }

                    // Fakturaadress adress2
                    iCustomerAttList = iCustomerElement.getElementsByTagName("InvoiceAddress2");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iInvoiceAddress.setAddress2(iValue);
                    }

                    // Fakturaadress postnummer
                    iCustomerAttList = iCustomerElement.getElementsByTagName("InvoicePostCode");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iInvoiceAddress.setZipCode(iValue);
                    }

                    // Fakturaadress stad
                    iCustomerAttList = iCustomerElement.getElementsByTagName("InvoicePostOffice");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iInvoiceAddress.setCity(iValue);
                    }

                    // Fakturaadress land
                    iCustomerAttList = iCustomerElement.getElementsByTagName("InvoiceCountry");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iInvoiceAddress.setCountry(iValue);
                    }

                    iCustomer.setInvoiceAddress(iInvoiceAddress);

                    // Leveransadress namn
                    SSAddress iDeliveryAddress = new SSAddress();
                    iCustomerAttList = iCustomerElement.getElementsByTagName("DeliveryName");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iDeliveryAddress.setName(iValue);
                    }

                    // Leveransadress adress1
                    iCustomerAttList = iCustomerElement.getElementsByTagName("DeliveryAddress1");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iDeliveryAddress.setAddress1(iValue);
                    }

                    // Leveransadress adress2
                    iCustomerAttList = iCustomerElement.getElementsByTagName("DeliveryAddress2");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iDeliveryAddress.setAddress2(iValue);
                    }

                    // Leveransadress postnummer
                    iCustomerAttList = iCustomerElement.getElementsByTagName("DeliveryPostCode");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iDeliveryAddress.setZipCode(iValue);
                    }

                    // Leveransadress stad
                    iCustomerAttList = iCustomerElement.getElementsByTagName("DeliveryPostOffice");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iDeliveryAddress.setCity(iValue);
                    }

                    // Leveransadress land
                    iCustomerAttList = iCustomerElement.getElementsByTagName("DeliveryCountry");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iDeliveryAddress.setCountry(iValue);
                    }

                    iCustomer.setDeliveryAddress(iDeliveryAddress);

                    // Dölj enhetspris på följesedel
                    iCustomerAttList = iCustomerElement.getElementsByTagName("HideUnitPrice");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "false" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setHideUnitprice(Boolean.valueOf(iValue));
                    }

                    // Kreditgräns
                    iCustomerAttList = iCustomerElement.getElementsByTagName("CreditLimit");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "0.0" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setCreditLimit(new BigDecimal(iValue));
                    }

                    // Rabatt
                    iCustomerAttList = iCustomerElement.getElementsByTagName("Discount");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "0.0" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setDiscount(new BigDecimal(iValue));
                    }

                    // Bankgiro
                    iCustomerAttList = iCustomerElement.getElementsByTagName("BgNo");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setBankgiro(iValue);
                    }

                    // Plusgiro
                    iCustomerAttList = iCustomerElement.getElementsByTagName("PgNo");
                    iFirstCustomerAttElement = (Element)iCustomerAttList.item(0);
                    if (iFirstCustomerAttElement != null) {
                        iTextCustomerAttList = iFirstCustomerAttElement.getChildNodes();
                        iValue = iTextCustomerAttList.item(0) == null ? "" : iTextCustomerAttList.item(0).getNodeValue().trim();
                        iCustomer.setPlusgiro(iValue);
                    }

                    iCustomers.add(iCustomer);
                }
            }

            for (SSCustomer pCustomer : iCustomers) {
                if (SSDB.getInstance().getCustomers().contains(pCustomer)) {
                    SSDB.getInstance().updateCustomer(pCustomer);
                } else {
                    SSDB.getInstance().addCustomer(pCustomer);
                }
            }

        } catch (ParserConfigurationException e) {
            throw new SSImportException(e.getMessage());
        } catch (SAXException e) {
            throw new SSImportException(e.getMessage());
        } catch (IOException e) {
            throw new SSImportException(e.getMessage());
        }
    }

    private SSCurrency getCurrency(String iName) {
        for (SSCurrency iCurrency : SSDB.getInstance().getCurrencies()) {
            if (iCurrency.getName().equals(iName)) {
                return iCurrency;
            }
        }
        return null;
    }

     private SSPaymentTerm getPaymentTerm(String iName) {
        for (SSPaymentTerm iPaymentTerm : SSDB.getInstance().getPaymentTerms()) {
            if (iPaymentTerm.getName().equals(iName)) {
                return iPaymentTerm;
            }
        }
        return null;
    }

    private SSDeliveryTerm getDeliveryTerm(String iName) {
        for (SSDeliveryTerm iDeliveryTerm : SSDB.getInstance().getDeliveryTerms()) {
            if (iDeliveryTerm.getName().equals(iName)) {
                return iDeliveryTerm;
            }
        }
        return null;
    }

    private SSDeliveryWay getDeliveryWay(String iName) {
        for (SSDeliveryWay iDeliveryWay : SSDB.getInstance().getDeliveryWays()) {
            if (iDeliveryWay.getName().equals(iName)) {
                return iDeliveryWay;
            }
        }
        return null;
    }



    public void doEbutikImport(){
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(iFile),"Windows-1252"));
            String text = null;
            ArrayList<String> al = new ArrayList<String>();
            ArrayList<String> iBadCustomers = new ArrayList<String>();
            Integer iCustomerCount = 0;
            while((text = br.readLine()) != null) {
                al.add(text);
            }
            br.close();

            for(String iLine : al){
                boolean iNewCustomer = false;
                String[] iFields = iLine.split("\t",-1);
                SSCustomer iCustomer = SSDB.getInstance().getCustomer(iFields[0]);
                if(iCustomer == null){
                    iCustomer = new SSCustomer();
                    iCustomer.setNumber(iFields[0]);
                    //iCustomer.setCreditLimit(new BigDecimal(0.0));
                    iNewCustomer = true;
                }

                if(iFields.length == 20){
                    if(iFields[2] == null || iFields[2].equals("")){
                        iCustomer.getInvoiceAddress().setName(iFields[3] + ' ' + iFields[4]);
                        iCustomer.setName(iFields[3] + ' ' + iFields[4]);
                        iCustomer.getInvoiceAddress().setAddress1(iFields[5]);
                    }
                    else{
                        iCustomer.getInvoiceAddress().setName(iFields[2]);
                        iCustomer.getInvoiceAddress().setAddress1(iFields[3] + ' ' + iFields[4]);
                        iCustomer.getInvoiceAddress().setAddress2(iFields[5]);
                        iCustomer.setName(iFields[2]);
                    }
                    iCustomer.setYourContactPerson(iFields[3] + ' ' + iFields[4]);
                    iCustomer.getInvoiceAddress().setZipCode(iFields[6]);
                    iCustomer.getInvoiceAddress().setCity(iFields[7]);
                    iCustomer.getInvoiceAddress().setCountry(iFields[8]);

                    if(iFields[9] == null || (iFields[9].equals("") && iFields[2].equals(""))){
                        iCustomer.getDeliveryAddress().setName((iFields[10] + ' ' + iFields[11]).equals(" ") ? iCustomer.getInvoiceAddress().getName() : iFields[10] + ' ' + iFields[11]);
                        iCustomer.getDeliveryAddress().setAddress1(iFields[12].equals("") ? iCustomer.getInvoiceAddress().getAddress1() : iFields[12]);
                    }
                    else{
                        iCustomer.getDeliveryAddress().setName(iFields[9].equals("") ? iCustomer.getInvoiceAddress().getName() : iFields[9]);
                        iCustomer.getDeliveryAddress().setAddress1((iFields[10] + ' ' + iFields[11]).equals(" ") ? iCustomer.getInvoiceAddress().getAddress1() : iFields[10] + ' ' + iFields[11]);
                        iCustomer.getDeliveryAddress().setAddress2(iFields[12].equals("") ? iCustomer.getInvoiceAddress().getAddress2() : iFields[12]);
                    }
                    iCustomer.getDeliveryAddress().setZipCode(iFields[13].equals("") ? iCustomer.getInvoiceAddress().getZipCode() : iFields[13]);
                    iCustomer.getDeliveryAddress().setCity(iFields[14].equals("") ? iCustomer.getInvoiceAddress().getCity() : iFields[14]);
                    iCustomer.getDeliveryAddress().setCountry(iFields[15].equals("") ? iCustomer.getInvoiceAddress().getCountry() : iFields[15]);

                    iCustomer.setPhone1(iFields[16]);
                    iCustomer.setTelefax(iFields[17]);
                    iCustomer.setEMail(iFields[18]);
                    iCustomer.setRegistrationNumber(iFields[19]);
                    if(iNewCustomer) SSDB.getInstance().addCustomer(iCustomer);
                    else SSDB.getInstance().updateCustomer(iCustomer);
                    iCustomerCount++;
                }
                else if(iFields.length == 19){
                    if(iFields[1] == null || iFields[1].equals("")){
                        iCustomer.getInvoiceAddress().setName(iFields[2] + ' ' + iFields[3]);
                        iCustomer.setName(iFields[2] + ' ' + iFields[3]);
                        iCustomer.getInvoiceAddress().setAddress1(iFields[4]);
                    }
                    else{
                        iCustomer.getInvoiceAddress().setName(iFields[1]);
                        iCustomer.getInvoiceAddress().setAddress1(iFields[2] + ' ' + iFields[3]);
                        iCustomer.getInvoiceAddress().setAddress2(iFields[4]);
                        iCustomer.setName(iFields[1]);
                    }
                    iCustomer.setYourContactPerson(iFields[2] + ' ' + iFields[3]);
                    iCustomer.getInvoiceAddress().setZipCode(iFields[5]);
                    iCustomer.getInvoiceAddress().setCity(iFields[6]);
                    iCustomer.getInvoiceAddress().setCountry(iFields[7]);

                    if(iFields[8] == null || (iFields[8].equals("") && iFields[1].equals(""))){
                        iCustomer.getDeliveryAddress().setName((iFields[9] + ' ' + iFields[10]).equals(" ") ? iCustomer.getInvoiceAddress().getName() : iFields[9] + ' ' + iFields[10]);
                        iCustomer.getDeliveryAddress().setAddress1(iFields[11].equals("") ? iCustomer.getInvoiceAddress().getAddress1() : iFields[11]);
                    }
                    else{
                        iCustomer.getDeliveryAddress().setName(iFields[8].equals("") ? iCustomer.getInvoiceAddress().getName() : iFields[8]);
                        iCustomer.getDeliveryAddress().setAddress1((iFields[9] + ' ' + iFields[10]).equals(" ") ? iCustomer.getInvoiceAddress().getAddress1() : iFields[9] + ' ' + iFields[10]);
                        iCustomer.getDeliveryAddress().setAddress2(iFields[11].equals("") ? iCustomer.getInvoiceAddress().getAddress2() : iFields[11]);
                    }
                    iCustomer.getDeliveryAddress().setZipCode(iFields[12].equals("") ? iCustomer.getInvoiceAddress().getZipCode() : iFields[12]);
                    iCustomer.getDeliveryAddress().setCity(iFields[13].equals("") ? iCustomer.getInvoiceAddress().getCity() : iFields[13]);
                    iCustomer.getDeliveryAddress().setCountry(iFields[14].equals("") ? iCustomer.getInvoiceAddress().getCountry() : iFields[14]);

                    iCustomer.setPhone1(iFields[15]);
                    iCustomer.setTelefax(iFields[16]);
                    iCustomer.setEMail(iFields[17]);
                    iCustomer.setRegistrationNumber(iFields[18]);
                    if(iNewCustomer) SSDB.getInstance().addCustomer(iCustomer);
                    else SSDB.getInstance().updateCustomer(iCustomer);
                    iCustomerCount++;
                }
                else{
                    iBadCustomers.add(iFields[0] + " - Fel antal fält");
                }
            }


            if(!iBadCustomers.isEmpty()){
                BufferedWriter bw = new BufferedWriter(new PrintWriter(SSFileSystem.getApplicationDirectory() + "kundimport.txt"));
                for(String iBadOrder:iBadCustomers){
                    bw.write(iBadOrder);
                    bw.newLine();
                }
                bw.close();
                new SSInformationDialog(SSMainFrame.getInstance(), "customerframe.import.errors",iCustomerCount.toString(), "" + iBadCustomers.size());
            }
            else{
                new SSInformationDialog(SSMainFrame.getInstance(), "customerframe.import.noerrors",iCustomerCount.toString());
            }
        }
        catch (IOException e) {
            throw new SSImportException(e.getMessage());
        }
    }

}
