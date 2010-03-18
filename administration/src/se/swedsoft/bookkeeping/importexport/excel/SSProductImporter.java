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
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSProductRow;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSInitDialog;
import se.swedsoft.bookkeeping.importexport.dialog.SSImportReportDialog;
import se.swedsoft.bookkeeping.importexport.excel.util.SSExcelCell;
import se.swedsoft.bookkeeping.importexport.excel.util.SSExcelRow;
import se.swedsoft.bookkeeping.importexport.excel.util.SSExcelSheet;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

/**
 * User: Andreas Lago
 * Date: 2006-aug-01
 * Time: 11:32:25
 */
public class SSProductImporter {

    private File iFile;

    private Map<String, Integer> iColumns;

    /**
     *
     * @param iFile
     */
    public SSProductImporter(File iFile) {
        this.iFile = iFile;
        this.iColumns = new HashMap<String, Integer>();
    }

    /**
     *
     * @throws IOException
     * @throws SSImportException
     */
    public void doImport() throws IOException, SSImportException  {
        WorkbookSettings iSettings = new WorkbookSettings();

        iSettings.setLocale               (new Locale("sv", "SE"));
        iSettings.setEncoding             ("windows-1252");
        iSettings.setExcelDisplayLanguage ("SE");
        iSettings.setExcelRegionalSettings("SE");

        List<SSProduct> iProducts = null;
        try{
            Workbook iWorkbook = Workbook.getWorkbook(iFile, iSettings);

            Sheet iSheet = iWorkbook.getSheet(0);

            iProducts = importProducts(new SSExcelSheet(iSheet));

            //  iWorkbook.write();
            iWorkbook.close();

        }catch( BiffException e){
            throw new SSImportException( e.getLocalizedMessage() );
        }
        final List<SSProduct> iNewProducts = new LinkedList<SSProduct>(iProducts);
        final boolean iResult = showImportReport(iProducts);
        SSInitDialog.runProgress(SSMainFrame.getInstance(), "Importerar produkter", new Runnable(){
            public void run(){
                if( iNewProducts != null && iResult ){
                    List<SSProduct> iExistingProducts = SSDB.getInstance().getProducts();
                    for (SSProduct iProduct : iNewProducts) {
                        if(!iExistingProducts.contains(iProduct)){
                            SSDB.getInstance().addProduct(iProduct);
                        }
                    }
                }
            }
        });

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
                if( iName.equalsIgnoreCase( SSProductExporter.BESKRIVNING                  )) this.iColumns.put(SSProductExporter.BESKRIVNING, iIndex); else
                if( iName.equalsIgnoreCase( SSProductExporter.PRODUKTNUMMER                )) this.iColumns.put(SSProductExporter.PRODUKTNUMMER, iIndex); else
                if( iName.equalsIgnoreCase( SSProductExporter.FORSALJNINGSPRIS             )) this.iColumns.put(SSProductExporter.FORSALJNINGSPRIS, iIndex); else
                if( iName.equalsIgnoreCase( SSProductExporter.INKOPSPRIS                   )) this.iColumns.put(SSProductExporter.INKOPSPRIS, iIndex); else
                if( iName.equalsIgnoreCase( SSProductExporter.ENHETSFRAKT                  )) this.iColumns.put(SSProductExporter.ENHETSFRAKT, iIndex); else
                if( iName.equalsIgnoreCase( SSProductExporter.MOMS                         )) this.iColumns.put(SSProductExporter.MOMS, iIndex); else
                if( iName.equalsIgnoreCase( SSProductExporter.ENHET                        )) this.iColumns.put(SSProductExporter.ENHET, iIndex); else
                if( iName.equalsIgnoreCase( SSProductExporter.VIKT                         )) this.iColumns.put(SSProductExporter.VIKT, iIndex); else
                if( iName.equalsIgnoreCase( SSProductExporter.VOLYM                        )) this.iColumns.put(SSProductExporter.VOLYM, iIndex); else
                if( iName.equalsIgnoreCase( SSProductExporter.LEVERANTOR                   )) this.iColumns.put(SSProductExporter.LEVERANTOR, iIndex); else
                if( iName.equalsIgnoreCase( SSProductExporter.LEVERANTORENS_ARTIKEL_NUMMER )) this.iColumns.put(SSProductExporter.LEVERANTORENS_ARTIKEL_NUMMER, iIndex); else
                if( iName.equalsIgnoreCase( SSProductExporter.BESTALLNINGSPUNKT            )) this.iColumns.put(SSProductExporter.BESTALLNINGSPUNKT, iIndex); else
                if( iName.equalsIgnoreCase( SSProductExporter.LAGERPLATS                   )) this.iColumns.put(SSProductExporter.LAGERPLATS, iIndex); else
                if( iName.equalsIgnoreCase( SSProductExporter.LAGERANTAL                   )) this.iColumns.put(SSProductExporter.LAGERANTAL, iIndex); else
                if( iName.equalsIgnoreCase( SSProductExporter.DISPONIBELT                  )) this.iColumns.put(SSProductExporter.DISPONIBELT, iIndex); else
                if( iName.equalsIgnoreCase( SSProductExporter.LAGERPRIS                    )) this.iColumns.put(SSProductExporter.LAGERPRIS, iIndex); else
                    throw new SSImportException("Ogiltigt kolumnnamn i importfilen: %s", iName);
            }
            iIndex++;
        }
    }

    /**
     *
     * @param iSheet
     * @return
     */
    private List<SSProduct> importProducts(SSExcelSheet iSheet){


        List<SSExcelRow> iRows = iSheet.getRows( );

        if( iRows.size() < 2 ) throw new SSImportException(SSBundle.getBundle(), "productframe.import.norows");

        getColumnIndexes(iRows.get(0));

        List<SSProduct> iProducts = new LinkedList<SSProduct>();

        for(int row = 1; row < iRows.size(); row++){
            SSExcelRow iRow = iRows.get(row);
            // Skip empty rows
            if( iRow.empty() ) continue;

            List<SSExcelCell> iCells = iRow.getCells();

            SSProduct iProduct = new SSProduct();

            // Get the cell
            for(int col = 0; col < iCells.size(); col++){
                SSExcelCell iCell = iCells.get(col);

                // Get the column name
                String iValue = iCell.getString();

                DecimalFormat iFormat = new DecimalFormat("0,0000");
                iFormat.setParseBigDecimal(true);


                try {
                    if( iColumns.containsKey(SSProductExporter.PRODUKTNUMMER                ) && iColumns.get(SSProductExporter.PRODUKTNUMMER               ) == col) iProduct.setNumber(iValue);
                    if( iColumns.containsKey(SSProductExporter.BESKRIVNING                  ) && iColumns.get(SSProductExporter.BESKRIVNING                 ) == col) iProduct.setDescription(iValue);
                    if( iColumns.containsKey(SSProductExporter.FORSALJNINGSPRIS             ) && iColumns.get(SSProductExporter.FORSALJNINGSPRIS            ) == col) iProduct.setSellingPrice  ( (BigDecimal)iFormat.parse(iValue));
                    if( iColumns.containsKey(SSProductExporter.INKOPSPRIS                   ) && iColumns.get(SSProductExporter.INKOPSPRIS                  ) == col) iProduct.setPurchasePrice ( (BigDecimal)iFormat.parse(iValue));
                    if( iColumns.containsKey(SSProductExporter.ENHETSFRAKT                  ) && iColumns.get(SSProductExporter.ENHETSFRAKT                 ) == col) iProduct.setUnitFreight   ( (BigDecimal)iFormat.parse(iValue));
                    if( iColumns.containsKey(SSProductExporter.MOMS                         ) && iColumns.get(SSProductExporter.MOMS                        ) == col) iProduct.setTaxCode(SSTaxCode.decode(iValue));
                    if( iColumns.containsKey(SSProductExporter.ENHET                        ) && iColumns.get(SSProductExporter.ENHET                       ) == col) iProduct.setUnit(SSUnit.decode(iValue));
                    if( iColumns.containsKey(SSProductExporter.VIKT                         ) && iColumns.get(SSProductExporter.VIKT                        ) == col) iProduct.setWeight( (BigDecimal)iFormat.parse(iValue) );
                    if( iColumns.containsKey(SSProductExporter.VOLYM                        ) && iColumns.get(SSProductExporter.VOLYM                       ) == col) iProduct.setVolume( (BigDecimal)iFormat.parse(iValue));
                    if( iColumns.containsKey(SSProductExporter.LEVERANTOR                   ) && iColumns.get(SSProductExporter.LEVERANTOR                  ) == col) iProduct.setSupplierNr(iValue);
                    if( iColumns.containsKey(SSProductExporter.LEVERANTORENS_ARTIKEL_NUMMER ) && iColumns.get(SSProductExporter.LEVERANTORENS_ARTIKEL_NUMMER) == col) iProduct.setSupplierProductNr(iValue);
                    if( iColumns.containsKey(SSProductExporter.BESTALLNINGSPUNKT            ) && iColumns.get(SSProductExporter.BESTALLNINGSPUNKT           ) == col) iProduct.setOrderpoint(Integer.valueOf(iValue));
                    if( iColumns.containsKey(SSProductExporter.LAGERPLATS                   ) && iColumns.get(SSProductExporter.LAGERPLATS                  ) == col) iProduct.setWarehouseLocation(iValue);
                    if( iColumns.containsKey(SSProductExporter.LAGERPRIS                    ) && iColumns.get(SSProductExporter.LAGERPRIS                   ) == col) iProduct.setStockPrice( (BigDecimal) iFormat.parse(iValue) );

                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new SSImportException(e.getLocalizedMessage());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    throw new SSImportException(e.getLocalizedMessage());
                }

            }
            if(iProduct.getNumber() != null && iProduct.getNumber().length() > 0 ) iProducts.add(iProduct);
        }
        return iProducts;
    }


    /**
     *
     * @param iProducts
     * @return
     */
    private boolean showImportReport(List<SSProduct> iProducts){
        SSImportReportDialog iDialog = new SSImportReportDialog(SSMainFrame.getInstance(), SSBundle.getBundle().getString("productframe.import.report"));
        // Generate the import dialog
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("F�ljande kolumner har importerats fr�n produktfilen:<br>");
        sb.append("<ul>");
        if(iColumns.containsKey(SSProductExporter.PRODUKTNUMMER                 ) ) sb.append("<li>").append(SSProductExporter.PRODUKTNUMMER ).append("</li>");
        if(iColumns.containsKey(SSProductExporter.BESKRIVNING                   ) ) sb.append("<li>").append(SSProductExporter.BESKRIVNING ).append("</li>");
        if(iColumns.containsKey(SSProductExporter.FORSALJNINGSPRIS              ) ) sb.append("<li>").append(SSProductExporter.FORSALJNINGSPRIS ).append("</li>");
        if(iColumns.containsKey(SSProductExporter.INKOPSPRIS                    ) ) sb.append("<li>").append(SSProductExporter.INKOPSPRIS ).append("</li>");
        if(iColumns.containsKey(SSProductExporter.ENHETSFRAKT                   ) ) sb.append("<li>").append(SSProductExporter.ENHETSFRAKT ).append("</li>");
        if(iColumns.containsKey(SSProductExporter.MOMS                          ) ) sb.append("<li>").append(SSProductExporter.MOMS ).append("</li>");
        if(iColumns.containsKey(SSProductExporter.ENHET                         ) ) sb.append("<li>").append(SSProductExporter.ENHET ).append("</li>");
        if(iColumns.containsKey(SSProductExporter.VIKT                          ) ) sb.append("<li>").append(SSProductExporter.VIKT ).append("</li>");
        if(iColumns.containsKey(SSProductExporter.VOLYM                         ) ) sb.append("<li>").append(SSProductExporter.VOLYM ).append("</li>");
        if(iColumns.containsKey(SSProductExporter.LEVERANTOR                    ) ) sb.append("<li>").append(SSProductExporter.LEVERANTOR ).append("</li>");
        if(iColumns.containsKey(SSProductExporter.LEVERANTORENS_ARTIKEL_NUMMER  ) ) sb.append("<li>").append(SSProductExporter.LEVERANTORENS_ARTIKEL_NUMMER ).append("</li>");
        if(iColumns.containsKey(SSProductExporter.BESTALLNINGSPUNKT             ) ) sb.append("<li>").append(SSProductExporter.BESTALLNINGSPUNKT ).append("</li>");
        if(iColumns.containsKey(SSProductExporter.LAGERPLATS                    ) ) sb.append("<li>").append(SSProductExporter.LAGERPLATS ).append("</li>");
        if(iColumns.containsKey(SSProductExporter.LAGERPRIS                     ) ) sb.append("<li>").append(SSProductExporter.LAGERPRIS ).append("</li>");

        sb.append("</ul>");
        sb.append("F�ljande produkter kommer att importeras:<br>");

        sb.append("<ul>");
        for (SSProduct iProduct : iProducts) {
            sb.append("<li>");
            sb.append(iProduct);
            sb.append("</li>");
        }
        sb.append("</ul>");
        sb.append("Forts�tt med importeringen ?");
        sb.append("</html>");


        iDialog.setText( sb.toString() );
        iDialog.setSize( 640, 480);
        iDialog.setLocationRelativeTo(SSMainFrame.getInstance());

        return iDialog.showDialog() == JOptionPane.OK_OPTION;
    }


    public void doXMLImport() throws SSImportException{
        SSProduct iProduct;
        List<SSProduct> iProducts = new LinkedList<SSProduct>();
        try {
            DocumentBuilderFactory iDocBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder iDocBuilder = iDocBuilderFactory.newDocumentBuilder();
            Document iDoc = iDocBuilder.parse (iFile.getAbsolutePath());

            iDoc.getDocumentElement().normalize ();

            if (!iDoc.getDocumentElement().getNodeName().equals("Products")) {
                throw new SSImportException("Filen inneh�ller inga produkter");
            }

            NodeList iProductList = iDoc.getElementsByTagName("Product");
            if (iProductList.getLength() == 0) {
                throw new SSImportException("Filen inneh�ller inga produkter");
            }

            for (int i = 0; i < iProductList.getLength() ; i++) {
                iProduct = new SSProduct();

                Node iProductNode = iProductList.item(i);
                if(iProductNode.getNodeType() == Node.ELEMENT_NODE){

                    NodeList iTextProductAttList = null;
                    String iValue = null;
                    Element iProductElement = (Element)iProductNode;

                    NodeList iProductAttList = iProductElement.getElementsByTagName("ProductNo");
                    Element iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setNumber(iValue == null ? "" : iValue);
                    }

                    iProductAttList = iProductElement.getElementsByTagName("ProductDescription");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setDescription(iValue == null ? "" : iValue);
                    }

                    iProductAttList = iProductElement.getElementsByTagName("UnitPrice");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setSellingPrice(iValue == null ? new BigDecimal("0.0") : new BigDecimal(iValue));
                    }

                    iProductAttList = iProductElement.getElementsByTagName("TaxRate");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setTaxCode(iValue == null ? SSTaxCode.decode("0") : SSTaxCode.decode(iValue));
                    }

                    iProductAttList = iProductElement.getElementsByTagName("PurchasePrice");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setPurchasePrice((iValue == null ? new BigDecimal("0.0") : new BigDecimal(iValue)));
                    }

                    iProductAttList = iProductElement.getElementsByTagName("UnitFreight");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setUnitFreight((iValue == null ? new BigDecimal("0.0") : new BigDecimal(iValue)));
                    }

                    iProductAttList = iProductElement.getElementsByTagName("Unit");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setUnit(iValue == null ? null : SSUnit.decode(iValue));
                    }

                    iProductAttList = iProductElement.getElementsByTagName("Weight");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setWeight((iValue == null ? new BigDecimal("0.0") : new BigDecimal(iValue)));
                    }

                    iProductAttList = iProductElement.getElementsByTagName("Volume");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setVolume((iValue == null ? new BigDecimal("0.0") : new BigDecimal(iValue)));
                    }

                    iProductAttList = iProductElement.getElementsByTagName("SaleAccount");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setDefaultAccount(SSDefaultAccount.Sales, iValue == null ? null : Integer.parseInt(iValue));
                    }

                    iProductAttList = iProductElement.getElementsByTagName("PurchaseAccount");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setDefaultAccount(SSDefaultAccount.Purchases, iValue == null ? null : Integer.parseInt(iValue));
                    }

                    iProductAttList = iProductElement.getElementsByTagName("Expired");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setExpired(iValue != null && Boolean.valueOf(iValue));
                    }

                    iProductAttList = iProductElement.getElementsByTagName("StockProduct");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setStockProduct(iValue != null && Boolean.valueOf(iValue));
                    }

                    iProductAttList = iProductElement.getElementsByTagName("WarehouseLocation");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setWarehouseLocation(iValue);
                    }

                    iProductAttList = iProductElement.getElementsByTagName("OrderPoint");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setOrderpoint(iValue == null ? null : Integer.parseInt(iValue));
                    }

                    iProductAttList = iProductElement.getElementsByTagName("OrderCount");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setOrdercount(iValue == null ? null : Integer.parseInt(iValue));
                    }

                    iProductAttList = iProductElement.getElementsByTagName("SupplierProductNo");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setSupplierProductNr(iValue);
                    }

                    iProductAttList = iProductElement.getElementsByTagName("Supplier");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setSupplier(getSupplier(iValue));
                    }

                    iProductAttList = iProductElement.getElementsByTagName("EnProductDescription");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);
                    if (iFirstProductAttElement != null) {
                        iTextProductAttList = iFirstProductAttElement.getChildNodes();
                        iValue = iTextProductAttList.item(0) == null ? null : iTextProductAttList.item(0).getNodeValue().trim();
                        iProduct.setDescription(new Locale("en"), iValue);
                    }

                    iProductAttList = iProductElement.getElementsByTagName("Detail");
                    iFirstProductAttElement = (Element)iProductAttList.item(0);

                    NodeList iRowList = iFirstProductAttElement.getElementsByTagName("ParcelRow");
                    List<SSProductRow> iRows = new LinkedList<SSProductRow>();
                    for (int j = 0; j < iRowList.getLength(); j++) {
                        SSProductRow iRow = new SSProductRow();
                        Node iRowNode = iRowList.item(j);
                        if(iRowNode.getNodeType() == Node.ELEMENT_NODE){
                            Element iRowElement = (Element)iRowNode;

                            NodeList iTextRowAttList = null;
                            NodeList iRowAttList = iRowElement.getElementsByTagName("IncludedProductNo");
                            Element iFirstRowAttElement = (Element)iRowAttList.item(0);
                            if (iFirstProductAttElement != null) {
                                iTextRowAttList = iFirstRowAttElement.getChildNodes();
                                iValue = iTextRowAttList.item(0) == null ? null : iTextRowAttList.item(0).getNodeValue().trim();
                                iRow.setProduct(getProduct(iValue));
                            }

                            iRowAttList = iRowElement.getElementsByTagName("RowQuantity");
                            iFirstRowAttElement = (Element)iRowAttList.item(0);
                            if (iFirstProductAttElement != null) {
                                iTextRowAttList = iFirstRowAttElement.getChildNodes();
                                iValue = iTextRowAttList.item(0) == null ? null : iTextRowAttList.item(0).getNodeValue().trim();
                                iRow.setQuantity(iValue == null ? null : Integer.parseInt(iValue));
                            }

                            if(iRow.getProductNr() != null)
                                iRows.add(iRow);
                        }

                    }
                    if(!iRows.isEmpty())
                        iProduct.setParcelRows(iRows);
                    
                    iProducts.add(iProduct);
                }
            }
            List<SSProduct> iExistingProducts = SSDB.getInstance().getProducts();
            for (SSProduct pProduct : iProducts) {
                if(iExistingProducts.contains(pProduct)) {
                    SSDB.getInstance().updateProduct(pProduct);
                } else {
                    SSDB.getInstance().addProduct(pProduct);
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

    private SSProduct getProduct(String iNumber) {
        List<SSProduct> iProducts = SSDB.getInstance().getProducts();
        for (SSProduct iProduct : iProducts) {
            if (iProduct.getNumber().equals(iNumber)) {
                return iProduct;
            }
        }
        return null;
    }

    private SSSupplier getSupplier(String iNumber) {
        for (SSSupplier iSupplier : SSDB.getInstance().getSuppliers()) {
            if (iSupplier.getNumber().equals(iNumber)) {
                return iSupplier;
            }
        }
        return null;
    }


}
