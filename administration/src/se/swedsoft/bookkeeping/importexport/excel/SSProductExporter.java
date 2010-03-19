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
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSProductRow;
import se.swedsoft.bookkeeping.data.SSStock;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
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
 */
public class SSProductExporter {
    public static final String PRODUKTNUMMER = "Produktnummer";
    public static final String BESKRIVNING = "Beskrivning";
    public static final String FORSALJNINGSPRIS = "F�rs�ljningspris";
    public static final String INKOPSPRIS = "Ink�pspris";
    public static final String ENHETSFRAKT = "Enhetsfrakt";
    public static final String MOMS = "Moms";
    public static final String ENHET = "Enhet";
    public static final String VIKT = "Vikt";
    public static final String VOLYM = "Volym";
    public static final String LEVERANTOR = "Leverant�r";
    public static final String LEVERANTORENS_ARTIKEL_NUMMER = "Leverant�rens artikel nummer";
    public static final String BESTALLNINGSPUNKT = "Best�llningspunkt";
    public static final String LAGERPLATS = "Lagerplats";
    public static final String LAGERANTAL = "Lagerantal";
    public static final String DISPONIBELT = "Disponibelt";
    public static final String LAGERPRIS = "Lagerpris";

    private File iFile;
    private List<SSProduct> iProducts;

    /**
     *
     * @param iFile
     */
    public SSProductExporter(File iFile) {
        this.iFile = iFile;
        iProducts = SSDB.getInstance().getProducts();
    }

    /**
     * 
     * @param iFile
     * @param iProducts
     */
    public SSProductExporter(File iFile, List<SSProduct> iProducts) {
        this.iFile = iFile;
        this.iProducts = iProducts;
    }


    /**
     *
     * @throws IOException
     * @throws SSImportException
     */
    public void doExport()  throws IOException, SSImportException {
        WorkbookSettings iSettings = new WorkbookSettings();

        iSettings.setLocale               (new Locale("sv", "SE"));
        iSettings.setEncoding             ("windows-1252");
        iSettings.setExcelDisplayLanguage ("SE");
        iSettings.setExcelRegionalSettings("SE");

        try{
            WritableWorkbook iWorkbook = Workbook.createWorkbook(iFile, iSettings);

            WritableSheet iSheet = iWorkbook.createSheet("Produkter", 0);

            writeSheet(new SSWritableExcelSheet(iSheet)  );

            iWorkbook.write();
            iWorkbook.close();

        }catch( WriteException e){
            throw new SSExportException( e.getLocalizedMessage() );
        }

    }

    /**
     *
     * @param pSheet
     * @throws WriteException
     */
    private void writeSheet(SSWritableExcelSheet pSheet ) throws WriteException {
        List<SSWritableExcelRow> iRows = pSheet.getRows( iProducts.size() + 1  );

        // Write the column names
        SSWritableExcelRow iColumns = iRows.get(0);

        WritableCellFormat iCellFormat = new WritableCellFormat();

        iCellFormat.setBackground( Colour.GRAY_25 );


        iColumns.setString( 0, PRODUKTNUMMER                , iCellFormat);
        iColumns.setString( 1, BESKRIVNING                  , iCellFormat);
        iColumns.setString( 2, FORSALJNINGSPRIS             , iCellFormat);
        iColumns.setString( 3, INKOPSPRIS                   , iCellFormat);
        iColumns.setString( 4, ENHETSFRAKT                  , iCellFormat);
        iColumns.setString( 5, MOMS                         , iCellFormat);
        iColumns.setString( 6, ENHET                        , iCellFormat);
        iColumns.setString( 7, VIKT                         , iCellFormat);
        iColumns.setString( 8, VOLYM                        , iCellFormat);
        iColumns.setString( 9, LEVERANTOR                   , iCellFormat);
        iColumns.setString(10, LEVERANTORENS_ARTIKEL_NUMMER , iCellFormat);
        iColumns.setString(11, BESTALLNINGSPUNKT            , iCellFormat);
        iColumns.setString(12, LAGERPLATS                   , iCellFormat);
        iColumns.setString(13, LAGERANTAL                   , iCellFormat);
        iColumns.setString(14, DISPONIBELT                  , iCellFormat);
        iColumns.setString(15, LAGERPRIS                    , iCellFormat);

        SSStock iStock = new SSStock(true);

        int iRowIndex = 1;
        for (SSProduct iProduct: iProducts) {
            SSSupplier iMainsupplier = iProduct.getSupplier(SSDB.getInstance().getSuppliers());


            SSWritableExcelRow iRow =  iRows.get(iRowIndex);
            iRow.setString(0,  iProduct.getNumber() );
            iRow.setString(1,  iProduct.getDescription() );
            iRow.setNumber(2,  iProduct.getSellingPrice() );
            iRow.setNumber(3,  iProduct.getPurchasePrice() );
            iRow.setNumber(4,  iProduct.getUnitFreight() );
            iRow.setNumber(5,  iProduct.getTaxRate()     );
            iRow.setString(6,  iProduct.getUnit() == null ? "" : iProduct.getUnit().getName()     );
            iRow.setNumber(7,  iProduct.getWeight()     );
            iRow.setNumber(8,  iProduct.getVolume()     );

            iRow.setString(9 ,  iMainsupplier == null ? "" : iMainsupplier.getNumber()    );
            iRow.setString(10,  iProduct.getSupplierProductNr()     );
            iRow.setNumber(11,  iProduct.getOrderpoint() == null ? 0 : iProduct.getOrderpoint()     );

            iRow.setString(12,  iProduct.getWarehouseLocation() );
            iRow.setNumber(13,  iStock.getQuantity(iProduct) );
            iRow.setNumber(14,  iStock.getAvaiable(iProduct) );
            iRow.setNumber(15,  iProduct.getStockPrice() == null ? 0 : iProduct.getStockPrice()     );
            iRowIndex++;
        }


    }

    public void doXMLExport() {

        Document iXmlDoc= new DocumentImpl();
        Element iRoot = iXmlDoc.createElement("Products");

        for (SSProduct iProduct : iProducts) {
            Element iElement = iXmlDoc.createElementNS(null, "Product");

            Element iSubElement = iXmlDoc.createElementNS(null, "ProductNo");
            iElement.appendChild(iSubElement);
            Node iNode = iXmlDoc.createTextNode(iProduct.getNumber() == null ? "" : iProduct.getNumber());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"ProductDescription");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getDescription());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"UnitPrice");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getSellingPrice() == null ? "" : iProduct.getSellingPrice().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"TaxRate");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getTaxRate() == null ? "": iProduct.getTaxRate().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"PurchasePrice");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getPurchasePrice() == null ? "" : iProduct.getPurchasePrice().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"UnitFreight");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getUnitFreight() == null ? "" : iProduct.getUnitFreight().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"Unit");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getUnit() == null ? "" : iProduct.getUnit().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"Weight");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getWeight() == null ? "" : iProduct.getWeight().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"Volume");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getVolume() == null ? "" : iProduct.getVolume().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"SaleAccount");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getDefaultAccount(SSDefaultAccount.Sales) == null ? "" : iProduct.getDefaultAccount(SSDefaultAccount.Sales).toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"PurchaseAccount");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getDefaultAccount(SSDefaultAccount.Purchases) == null ? "" : iProduct.getDefaultAccount(SSDefaultAccount.Purchases).toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"Expired");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(Boolean.toString(iProduct.isExpired()));
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"StockProduct");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(Boolean.toString(iProduct.isStockProduct()));
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"WarehouseLocation");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getWarehouseLocation());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"OrderPoint");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getOrderpoint() == null ? "" : iProduct.getOrderpoint().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"OrderCount");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getOrdercount() == null ? "" : iProduct.getOrdercount().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"Supplier");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getSupplierNr());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"SupplierProductNo");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getSupplierProductNr());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"EnProductDescription");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iProduct.getDescription(new Locale("en")));
            iSubElement.appendChild(iNode);

            Element iRoot2 = iXmlDoc.createElement("Detail");

            for (SSProductRow iRow : iProduct.getParcelRows()) {
                iSubElement = iXmlDoc.createElementNS(null,"ParcelRow");

                Element iSubElement2 = iXmlDoc.createElementNS(null,"IncludedProductNo");
                iNode = iXmlDoc.createTextNode(iRow.getProductNr());
                iSubElement2.appendChild(iNode);
                iSubElement.appendChild(iSubElement2);

                iSubElement2 = iXmlDoc.createElementNS(null,"RowQuantity");
                iNode = iXmlDoc.createTextNode(iRow.getQuantity() == null ? "" : iRow.getQuantity().toString());
                iSubElement2.appendChild(iNode);
                iSubElement.appendChild(iSubElement2);

                iRoot2.appendChild(iSubElement);
            }
            iElement.appendChild(iRoot2);

            iRoot.appendChild(iElement);
        }
        iXmlDoc.appendChild(iRoot);
        try {
            FileOutputStream fos = new FileOutputStream(iFile.getAbsolutePath());
            OutputStreamWriter osw = new OutputStreamWriter(fos,"windows-1252");
            OutputFormat of = new OutputFormat("XML","windows-1252",true);
            of.setIndent(1);
            of.setIndenting(true);
            XMLSerializer serializer = new XMLSerializer(osw,of);
            serializer.asDOMSerializer();
            serializer.serialize( iXmlDoc.getDocumentElement() );

            fos.close();
            osw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
