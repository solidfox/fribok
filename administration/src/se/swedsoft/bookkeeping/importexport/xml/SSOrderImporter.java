package se.swedsoft.bookkeeping.importexport.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import se.swedsoft.bookkeeping.app.SSPath;
import se.swedsoft.bookkeeping.data.SSAddress;
import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSOrder;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.common.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSInformationDialog;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SSOrderImporter {

    private File iFile;

    private List<SSCustomer> iCustomers;
    private List<SSProduct> iProducts;

    public SSOrderImporter(File pFile) {
        iCustomers = new LinkedList<SSCustomer>();
        iProducts = new LinkedList<SSProduct>();
        iFile = pFile;
    }

    public void doImport() throws SSImportException{
        SSOrder iOrder;
        List<SSOrder> iOrders = new LinkedList<SSOrder>();

        try {
            DocumentBuilderFactory iDocBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder iDocBuilder = iDocBuilderFactory.newDocumentBuilder();
            Document iDoc = iDocBuilder.parse (iFile.getAbsolutePath());


            //System.out.println(iDoc.getXmlEncoding());
            iDoc.getDocumentElement().normalize ();

            if (!iDoc.getDocumentElement().getNodeName().equals("Orders")) {
                throw new SSImportException("Filen innehåller inga ordrar");
            }

            NodeList iOrderList = iDoc.getElementsByTagName("Order");
            if (iOrderList.getLength() == 0) {
                throw new SSImportException("Filen innehåller inga ordrar");
            }

            for (int i = 0; i < iOrderList.getLength() ; i++) {
                /** Nummren nedan har inget att göra med i, utan bara visar ordningen dom
                 *  fälten förekommer i filen.
                 * 0 = ORDERNR, 1 = ORDERDATE, 2 = CUSTOMERNR, 3 = CUSTOMERNAME
                 * 4 = OURCONTACTPERSON, 5 = YOURCONTACTPERSON, 6 = DELAYINTEREST
                 * 7 = CURRENCY, 8 = PAYMENTTERM, 9 = DELIVERYTERM, 10 = DELIVERYWAY
                 * 11 = TAXFREE, 12 = ORDERTEXT, 13 = TAXRATE1, 14 = TAXRATE2
                 * 15 = TAXRATE3, 16 = EUSALECOMMODITY, 17 = EUSALETHIRDPARTCOMMODITY
                 * 17 = INVOICENAME, 18 = INVOICEADDRESS1, 19 = INVOICEADDRESS2
                 * 20 = INVOICEZIPCODE, 21 = INVOICECITY, 22 = INVOICECOUNTRY
                 * 23 = DELIVERYNAME, 24 = DELIVERYADDRESS1, 25 = DELIVERYADDRESS2
                 * 26 = DELIVERYZIPCODE, 27 = DELIVERYCITY, 28 = ROWS
                **/
                iOrder = new SSOrder();

                Node iOrderNode = iOrderList.item(i);
                if(iOrderNode.getNodeType() == Node.ELEMENT_NODE){

                    NodeList iTextOrderAttList = null;
                    String iValue = null;
                    Element iOrderElement = (Element)iOrderNode;
                    // Ordernummer (Kommer ändras innan ordern sparas)
                    NodeList iOrderAttList = iOrderElement.getElementsByTagName("SellerOrderNo");
                    Element iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                    if (iFirstOrderAttElement != null) {
                        iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                        iValue = iTextOrderAttList.item(0).getNodeValue() == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                        iOrder.setNumber(iValue == null ? 0 : Integer.parseInt(iValue));
                    }

                    // Orderdatum
                    iOrderAttList = iOrderElement.getElementsByTagName("OrderDate");
                    iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                    if (iFirstOrderAttElement != null) {
                        iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                        iValue = iTextOrderAttList.item(0).getNodeValue() == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                        SimpleDateFormat iFormat = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            iOrder.setDate(iValue == null ? new Date() : iFormat.parse(iValue));
                        } catch (ParseException e) {
                            iOrder.setDate(new Date());
                        }
                    }

                    // Dröjsmålsränta
                    iOrderAttList = iOrderElement.getElementsByTagName("DelayInterest");
                    iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                    if (iFirstOrderAttElement != null) {
                        iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                        iValue = iTextOrderAttList.item(0) == null ? "0.0" : iTextOrderAttList.item(0).getNodeValue().trim();
                        iOrder.setDelayInterest(new BigDecimal(iValue));
                    }

                    // Ordertext
                    iOrderAttList = iOrderElement.getElementsByTagName("Text");
                    iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                    if (iFirstOrderAttElement != null) {
                        iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                        iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                        iOrder.setText(iValue);
                    }

                    // Momskod 1
                    iOrderAttList = iOrderElement.getElementsByTagName("TaxRate1");
                    iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                    if (iFirstOrderAttElement != null) {
                        iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                        iValue = iTextOrderAttList.item(0) == null ? "0.0" : iTextOrderAttList.item(0).getNodeValue().trim();
                        iOrder.setTaxRate1(new BigDecimal(iValue));
                    }

                    // Momskod 2
                    iOrderAttList = iOrderElement.getElementsByTagName("TaxRate2");
                    iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                    if (iFirstOrderAttElement != null) {
                        iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                        iValue = iTextOrderAttList.item(0) == null ? "0.0" : iTextOrderAttList.item(0).getNodeValue().trim();
                        iOrder.setTaxRate2(new BigDecimal(iValue));
                    }

                    // Momskod 3
                    iOrderAttList = iOrderElement.getElementsByTagName("TaxRate3");
                    iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                    if (iFirstOrderAttElement != null) {
                        iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                        iValue = iTextOrderAttList.item(0) == null ? "0.0" : iTextOrderAttList.item(0).getNodeValue().trim();
                        iOrder.setTaxRate3(new BigDecimal(iValue));
                    }

                    // Kundnummer
                    SSCustomer iCustomer = null;
                    iValue = null;
                    iOrderAttList = iOrderElement.getElementsByTagName("CustomerNumber");
                    iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                    if (iFirstOrderAttElement != null) {
                        iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                        iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                        iCustomer = getCustomer(iValue);
                    }
                    if (iCustomer != null) {
                        // Kunden finns. använd kunddatan från databasen
                        iOrder.setCustomer(iCustomer);

                        // Valuta
                        iOrderAttList = iOrderElement.getElementsByTagName("CurrencyCode");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            SSCurrency iCurrency = getCurrency(iValue);
                            iOrder.setCurrency(iCurrency);
                            iOrder.setCurrencyRate(iCurrency.getExchangeRate());
                        }

                        //Betalningsvillkor
                        iOrderAttList = iOrderElement.getElementsByTagName("PaymentTerms");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iOrder.setPaymentTerm(getPaymentTerm(iValue));
                        }

                        // Leveransvillkor
                        iOrderAttList = iOrderElement.getElementsByTagName("DeliveryTerms");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iOrder.setDeliveryTerm(getDeliveryTerm(iValue));
                        }

                        // Leveranssätt
                        iOrderAttList = iOrderElement.getElementsByTagName("DeliveryMethod");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iOrder.setDeliveryWay(getDeliveryWay(iValue));
                        }

                    } else {
                        // Kunden finns inte. Skapa en ny med data från filen
                        iCustomer = new SSCustomer();
                        iCustomer.setNumber(iValue);

                        // Kundnamn
                        iOrderAttList = iOrderElement.getElementsByTagName("CustomerName");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iCustomer.setName(iValue);
                        }

                        // Vår kontaktperson
                        iOrderAttList = iOrderElement.getElementsByTagName("OurContactPerson");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iCustomer.setOurContactPerson(iValue);
                        }

                        // Er kontaktperson
                        iOrderAttList = iOrderElement.getElementsByTagName("YourContactPerson");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iCustomer.setYourContactPerson(iValue);
                        }

                        // Valuta
                        iOrderAttList = iOrderElement.getElementsByTagName("CurrencyCode");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            SSCurrency iCurrency = getCurrency(iValue);
                            iCustomer.setInvoiceCurrency(iCurrency);
                        }

                        // Betalningsvillkor
                        iOrderAttList = iOrderElement.getElementsByTagName("PaymentTerms");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iCustomer.setPaymentTerm(getPaymentTerm(iValue));
                        }

                        // Leveransvillkor
                        iOrderAttList = iOrderElement.getElementsByTagName("DeliveryTerms");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iCustomer.setDeliveryTerm(getDeliveryTerm(iValue));
                        }

                        // Leveranssätt
                        iOrderAttList = iOrderElement.getElementsByTagName("DeliveryMethod");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iCustomer.setDeliveryWay(getDeliveryWay(iValue));
                        }

                        // Momsfri
                        iOrderAttList = iOrderElement.getElementsByTagName("TaxFree");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iCustomer.setTaxFree(Boolean.valueOf(iValue));
                        }

                        // EU-försäljning
                        iOrderAttList = iOrderElement.getElementsByTagName("EuSaleCommodity");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iCustomer.setEuSaleCommodity(Boolean.valueOf(iValue));
                        }

                        // EU-försäljning 3e-part
                        iOrderAttList = iOrderElement.getElementsByTagName("EuSaleThirdPartCommodity");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iCustomer.setEuSaleYhirdPartCommodity(Boolean.valueOf(iValue));
                        }

                        // Vat. nr
                        iOrderAttList = iOrderElement.getElementsByTagName("VATRegNo");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iCustomer.setVATNumber(iValue);
                        }

                        //  E-post
                        iOrderAttList = iOrderElement.getElementsByTagName("Email");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iCustomer.setEMail(iValue);
                        }

                        // Org. nr
                        iOrderAttList = iOrderElement.getElementsByTagName("CompanyNo");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iCustomer.setRegistrationNumber(iValue);
                        }

                        // Faxnummer
                        iOrderAttList = iOrderElement.getElementsByTagName("Telefax");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if(iFirstOrderAttElement != null){
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iCustomer.setTelefax(iValue);
                        }

                        // Telefon
                        iOrderAttList = iOrderElement.getElementsByTagName("Telephone");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iCustomer.setPhone1(iValue);
                        }

                        // Telefon2
                        iOrderAttList = iOrderElement.getElementsByTagName("Telephone2");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iCustomer.setPhone2(iValue);
                        }

                        // Fakturaadress namn
                        SSAddress iInvoiceAddress = new SSAddress();
                        iOrderAttList = iOrderElement.getElementsByTagName("InvoiceName");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iInvoiceAddress.setName(iValue);
                        }

                        // Fakturaadress adress1
                        iOrderAttList = iOrderElement.getElementsByTagName("InvoiceAddress1");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iInvoiceAddress.setAddress1(iValue);
                        }

                        // Fakturaadress adress2
                        iOrderAttList = iOrderElement.getElementsByTagName("InvoiceAddress2");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iInvoiceAddress.setAddress2(iValue);
                        }

                        // Fakturaadress postnummer
                        iOrderAttList = iOrderElement.getElementsByTagName("InvoicePostCode");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iInvoiceAddress.setZipCode(iValue);
                        }

                        // Fakturaadress stad
                        iOrderAttList = iOrderElement.getElementsByTagName("InvoicePostOffice");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iInvoiceAddress.setCity(iValue);
                        }

                        // Fakturaadress land
                        iOrderAttList = iOrderElement.getElementsByTagName("InvoiceCountry");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iInvoiceAddress.setCountry(iValue);
                        }

                        iCustomer.setInvoiceAddress(iInvoiceAddress);

                        // Leveransadress namn
                        SSAddress iDeliveryAddress = new SSAddress();
                        iOrderAttList = iOrderElement.getElementsByTagName("DeliveryName");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iDeliveryAddress.setName(iValue);
                        }

                        // Leveransadress adress1
                        iOrderAttList = iOrderElement.getElementsByTagName("DeliveryAddress1");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iDeliveryAddress.setAddress1(iValue);
                        }

                        // Leveransadress adress2
                        iOrderAttList = iOrderElement.getElementsByTagName("DeliveryAddress2");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iDeliveryAddress.setAddress2(iValue);
                        }

                        // Leveransadress postnummer
                        iOrderAttList = iOrderElement.getElementsByTagName("DeliveryPostCode");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iDeliveryAddress.setZipCode(iValue);
                        }

                        // Leveransadress stad
                        iOrderAttList = iOrderElement.getElementsByTagName("DeliveryPostOffice");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iDeliveryAddress.setCity(iValue);
                        }

                        // Leveransadress land
                        iOrderAttList = iOrderElement.getElementsByTagName("DeliveryCountry");
                        iFirstOrderAttElement = (Element)iOrderAttList.item(0);
                        if (iFirstOrderAttElement != null) {
                            iTextOrderAttList = iFirstOrderAttElement.getChildNodes();
                            iValue = iTextOrderAttList.item(0) == null ? "" : iTextOrderAttList.item(0).getNodeValue().trim();
                            iDeliveryAddress.setCountry(iValue);
                        }

                        iCustomer.setDeliveryAddress(iDeliveryAddress);

                        iOrder.setCustomer(iCustomer);

                        if(iCustomer.getNumber() != null)
                            iCustomers.add(iCustomer);
                    }

                    iOrderAttList = iOrderElement.getElementsByTagName("Detail");
                    iFirstOrderAttElement = (Element)iOrderAttList.item(0);

                    NodeList iRowList = iFirstOrderAttElement.getElementsByTagName("ArticleRow");

                    for (int j = 0; j < iRowList.getLength(); j++) {
                        /**
                         * 0 = PRODUCTNR, 1 = PRODUCTDESCRIPTION, 2 = UNITPRICE
                         * 3 = QUANTITY, 4 = UNIT, 5 = DISCOUNT, 6 = SUM, 7 = TAX
                         */
                        SSSaleRow iRow = new SSSaleRow();
                        Node iRowNode = iRowList.item(j);
                        if(iRowNode.getNodeType() == Node.ELEMENT_NODE){
                            Element iRowElement = (Element)iRowNode;
                            // Antal
                            NodeList iTextRowAttList = null;
                            NodeList iRowAttList = iRowElement.getElementsByTagName("QuantityOrdered");
                            Element iFirstRowAttElement = (Element)iRowAttList.item(0);
                            if (iFirstOrderAttElement != null) {
                                iTextRowAttList = iFirstRowAttElement.getChildNodes();
                                iValue = iTextRowAttList.item(0) == null ? "0" : iTextRowAttList.item(0).getNodeValue().trim();
                                iRow.setQuantity(Integer.parseInt(iValue));
                            }

                            // Rabatt
                            iRowAttList = iRowElement.getElementsByTagName("TotalLineDiscountPercent");
                            iFirstRowAttElement = (Element)iRowAttList.item(0);
                            if (iFirstOrderAttElement != null) {
                                iTextRowAttList = iFirstRowAttElement.getChildNodes();
                                iValue = iTextRowAttList.item(0) == null ? null : iTextRowAttList.item(0).getNodeValue().trim();
                                iRow.setDiscount(iValue == null ? null : new BigDecimal(iValue));
                            }

                            // Produktnr
                            SSProduct iProduct = null;
                            iRowAttList = iRowElement.getElementsByTagName("SellerArticleNo");
                            iFirstRowAttElement = (Element)iRowAttList.item(0);
                            if (iFirstOrderAttElement != null) {
                                iTextRowAttList = iFirstRowAttElement.getChildNodes();
                                iValue = iTextRowAttList.item(0) == null ? "" : iTextRowAttList.item(0).getNodeValue().trim();
                                iProduct = getProduct(iValue);
                            }

                            if (iProduct != null) {
                                // Produkten fanns i databasen, använd produktdata för raden
                                iRow.setProductOnly(iProduct);

                                // Enhetspris
                                iRowAttList = iRowElement.getElementsByTagName("UnitPrice");
                                iFirstRowAttElement = (Element)iRowAttList.item(0);
                                if (iFirstOrderAttElement != null) {
                                    iTextRowAttList = iFirstRowAttElement.getChildNodes();
                                    iValue = iTextRowAttList.item(0) == null ? "0.0" : iTextRowAttList.item(0).getNodeValue().trim();
                                    iRow.setUnitprice(new BigDecimal(iValue));
                                }

                            } else {
                                iProduct = new SSProduct();
                                iProduct.setNumber(iValue);

                                // Produktbeskrivning
                                iRowAttList = iRowElement.getElementsByTagName("ArticleDescription");
                                iFirstRowAttElement = (Element)iRowAttList.item(0);
                                if (iFirstOrderAttElement != null) {
                                    iTextRowAttList = iFirstRowAttElement.getChildNodes();
                                    iValue = iTextRowAttList.item(0) == null ? "" : iTextRowAttList.item(0).getNodeValue().trim();
                                    iProduct.setDescription(iValue);
                                }

                                // Enhetspris
                                iRowAttList = iRowElement.getElementsByTagName("UnitPrice");
                                iFirstRowAttElement = (Element)iRowAttList.item(0);
                                if (iFirstOrderAttElement != null) {
                                    iTextRowAttList = iFirstRowAttElement.getChildNodes();
                                    iValue = iTextRowAttList.item(0) == null ? "0.0" : iTextRowAttList.item(0).getNodeValue().trim();
                                    iProduct.setSellingPrice(new BigDecimal(iValue));
                                }

                                // Enhet
                                iRowAttList = iRowElement.getElementsByTagName("Unit");
                                iFirstRowAttElement = (Element)iRowAttList.item(0);
                                if (iFirstOrderAttElement != null) {
                                    iTextRowAttList = iFirstRowAttElement.getChildNodes();
                                    iValue = iTextRowAttList.item(0) == null ? "" : iTextRowAttList.item(0).getNodeValue().trim();
                                    iProduct.setUnit(getUnit(iValue));
                                }

                                // Moms
                                iRowAttList = iRowElement.getElementsByTagName("VATPercentage");
                                iFirstRowAttElement = (Element)iRowAttList.item(0);
                                if (iFirstOrderAttElement != null) {
                                    iTextRowAttList = iFirstRowAttElement.getChildNodes();
                                    iValue = iTextRowAttList.item(0) == null ? "" : iTextRowAttList.item(0).getNodeValue().trim();
                                    iProduct.setTaxCode(SSTaxCode.decode(iValue));
                                }
                                if(iProduct.getNumber() != null)
                                    iProducts.add(iProduct);

                                iRow.setProductOnly(iProduct);
                            }
                            if(iRow.getProductNr() != null)
                                iOrder.getRows().add(iRow);
                        }
                    }
                    if(iOrder.getCustomerNr() != null){
                        iOrders.add(iOrder);
                    }

                }
            }

            for (SSProduct iProduct : iProducts) {
                SSDB.getInstance().addProduct(iProduct);
            }
            for (SSCustomer iCustomer : iCustomers) {
                SSDB.getInstance().addCustomer(iCustomer);
            }

            for (SSOrder iNewOrder : iOrders) {
                SSDB.getInstance().addOrder(iNewOrder);
            }



        } catch (ParserConfigurationException e) {
            throw new SSImportException(e.getMessage());
        } catch (SAXException e) {
            throw new SSImportException(e.getMessage());
        } catch (IOException e) {
            throw new SSImportException(e.getMessage());
        }
    }

    private SSCustomer getCustomer(String iNumber) {
        List<SSCustomer> pCustomers = SSDB.getInstance().getCustomers();
        for (SSCustomer iCustomer : pCustomers) {
            if (iCustomer.getNumber().equals(iNumber)) {
                return iCustomer;
            }
        }

        for (SSCustomer iCustomer : iCustomers) {
            if (iCustomer.getNumber().equals(iNumber)) {
                return iCustomer;
            }
        }
        return null;
    }

    private SSCurrency getCurrency(String iName) {
        List<SSCurrency> iCurrencies = SSDB.getInstance().getCurrencies();
        for (SSCurrency iCurrency : iCurrencies) {
            if (iCurrency.getName().equals(iName)) {
                return iCurrency;
            }
        }
        return null;
    }

    private SSPaymentTerm getPaymentTerm(String iName) {
        List<SSPaymentTerm> iPaymentTerms = SSDB.getInstance().getPaymentTerms();
        for (SSPaymentTerm iPaymentTerm : iPaymentTerms) {
            if (iPaymentTerm.getName().equals(iName)) {
                return iPaymentTerm;
            }
        }
        return null;
    }

    private SSDeliveryTerm getDeliveryTerm(String iName) {
        List<SSDeliveryTerm> iDeliveryTerms = SSDB.getInstance().getDeliveryTerms();
        for (SSDeliveryTerm iDeliveryTerm : iDeliveryTerms) {
            if (iDeliveryTerm.getName().equals(iName)) {
                return iDeliveryTerm;
            }
        }
        return null;
    }

    private SSDeliveryWay getDeliveryWay(String iName) {
        List<SSDeliveryWay> iDeliveryWays = SSDB.getInstance().getDeliveryWays();
        for (SSDeliveryWay iDeliveryWay : iDeliveryWays) {
            if (iDeliveryWay.getName().equals(iName)) {
                return iDeliveryWay;
            }
        }
        return null;
    }

    private SSProduct getProduct(String iName) {
        List<SSProduct> iProducts = SSDB.getInstance().getProducts();
        for (SSProduct iProduct : iProducts) {
            if (iProduct.getNumber().equals(iName)) {
                return iProduct;
            }
        }
        for (SSProduct iProduct : this.iProducts) {
            if (iProduct.getNumber().equals(iName)) {
                return iProduct;
            }
        }
        return null;
    }

    private SSUnit getUnit(String iName) {
        List<SSUnit> iUnits = SSDB.getInstance().getUnits();
        for (SSUnit iUnit : iUnits) {
            if (iUnit.getName().equals(iName)) {
                return iUnit;
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
            HashMap<Integer,ArrayList<String>> iResult = new HashMap<Integer,ArrayList<String>>();
            while((text = br.readLine()) != null) {
                al.add(text);
            }
            br.close();

            for(String iLine : al){
                if(iLine.equals(""))
                    continue;
                
                String[] iFields = iLine.split("\t",2);
                Integer iOrderNumber = Integer.parseInt(iFields[0]);
                if(iResult.get(iOrderNumber) == null){
                    ArrayList<String> iLineData = new ArrayList<String>();
                    iLineData.add(iFields[1]);
                    iResult.put(iOrderNumber, iLineData);
                }
                else{
                    iResult.get(iOrderNumber).add(iFields[1]);
                }
            }

            LinkedList<String> iBadOrders = new LinkedList<String>();
            Integer iOrderCount = 0;

            if(SSDB.getInstance().getProduct("frakt") == null/* && SSDB.getInstance().getProduct("frakt") == null && SSDB.getInstance().getProduct("FRAKT") == null*/){
                SSErrorDialog.showDialog(SSMainFrame.getInstance(), "", "Du måste skapa en produkt med nummer \"Frakt\" innan du kan importera.");
                return;
            }

            if(SSDB.getInstance().getProduct("avgift") == null){
                SSErrorDialog.showDialog(SSMainFrame.getInstance(), "", "Du måste skapa en produkt med nummer \"Avgift\" innan du kan importera.");
                return;
            }
            ArrayList<Integer> iOrderNumbers = new ArrayList<Integer>(iResult.keySet());
            Collections.sort(iOrderNumbers);
            order:for(Integer iOrderNumber : iOrderNumbers){
                SSOrder iOrder = new SSOrder();
                ArrayList<String> iAl = iResult.get(iOrderNumber);
                String iFeeString = "";
                String iFreightString = "";
                String iPaymentString = "";
                for(String iLine : iAl){
                    String[] iFields = iLine.split("\t");
                    if(iOrder.getCustomer() == null && iFields[0] != null){
                        SSCustomer iCustomer = SSDB.getInstance().getCustomer(iFields[0]);
                        if(iCustomer == null){
                            iBadOrders.add(iOrderNumber + " - Kunden " + iFields[0] + " existerar inte.");
                            continue order;
                        }
                        iOrder.setCustomer(iCustomer);
                    }
                    else if(iFields[0] == null){
                        iBadOrders.add(iOrderNumber + " - Kundnummer saknas");
                        continue order;
                    }

                    if(iFields[1] != null){
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            iOrder.setDate(df.parse(iFields[1]));
                        } catch (ParseException e) {
                            iBadOrders.add(iOrderNumber + " - Orderdatum är i fel format");
                            continue order;
                        }
                    }

                    SSSaleRow iRow = new SSSaleRow();
                    if(iFields[2] != null){
                        SSProduct iProduct = SSDB.getInstance().getProduct(iFields[2]);
                        if(iProduct != null){
                            iRow.setProduct(iProduct);
                            iRow.setDescription(iFields[3]);
                            if(iFields[6] != null){
                                try {
                                    iFields[6] = iFields[6].replace(",", ".");
                                    iRow.setUnitprice(new BigDecimal(iFields[6]));
                                } catch (Exception e) {
                                    iRow.setUnitprice(new BigDecimal(0.0));
                                }
                            }
                        }
                        else{
                            iProduct = new SSProduct();
                            iProduct.setNumber(iFields[2]);
                            if(iFields[3] != null){
                                iProduct.setDescription(iFields[3]);
                            }
                            try {
                                iFields[6] = iFields[6].replace(",", ".");
                                iProduct.setSellingPrice(new BigDecimal(iFields[6]));
                            } catch (Exception e) {
                                iProduct.setSellingPrice(new BigDecimal(0.0));
                            }
                            try {
                                iFields[6] = iFields[6].replace(",", ".");
                                iFields[8] = iFields[8].replace(",", ".");
                                BigDecimal iGrossSum = new BigDecimal(iFields[6]);
                                BigDecimal iTaxSum = new BigDecimal(iFields[8]);

                                BigDecimal iTaxPercent = iTaxSum.divide(iGrossSum);
                                iTaxPercent = iTaxPercent.multiply(new BigDecimal(100));
                                iTaxPercent = iTaxPercent.setScale(0);
                                iProduct.setTaxCode(SSTaxCode.decode2(iTaxPercent.toString()));
                            } catch (Exception e) {
                                iProduct.setTaxCode(SSTaxCode.TAXRATE_1);
                            }
                            try {
                                iProduct.setWeight(new BigDecimal(iFields[9]));
                            } catch (Exception e) {}
                            iRow.setProduct(iProduct);
                            SSDB.getInstance().addProduct(iProduct);
                        }
                    }
                    else{
                        iBadOrders.add(iOrderNumber + " - Artikelnummer saknas.");
                        continue order;
                    }

                    try {
                        iRow.setQuantity(Integer.parseInt(iFields[5]));
                    } catch (Exception e) {
                        iRow.setQuantity(0);
                    }

                    iOrder.getRows().add(iRow);

                    iFeeString = iFields[13];
                    iFreightString = iFields[15];
                    iPaymentString = iFields[14];
                }

                if(iFeeString != null && !iFeeString.equals("") && !iFeeString.equals("0")){
                    try {
                        iFeeString = iFeeString.replace(",", ".");
                        SSSaleRow iRow = new SSSaleRow(SSDB.getInstance().getProduct("Avgift"));
                        BigDecimal iFee = new BigDecimal(iFeeString);
                        iFee = iFee.multiply(new BigDecimal(0.8));
                        iRow.setUnitprice(iFee);
                        iRow.setQuantity(1);
                        iOrder.getRows().add(iRow);
                    } catch (Exception e) {
                        iBadOrders.add(iOrderNumber + " - Kunde inte skapa produktrad för avgift");
                        continue;
                    }
                }
                if(iFreightString != null && !iFreightString.equals("") && !iFreightString.equals("0")){
                    try {
                        iFreightString = iFreightString.replace(",", ".");
                        SSSaleRow iRow = new SSSaleRow(SSDB.getInstance().getProduct("Frakt"));
                        BigDecimal iFreight = new BigDecimal(iFreightString);
                        iFreight = iFreight.multiply(new BigDecimal(0.8));
                        iRow.setUnitprice(iFreight);
                        iRow.setQuantity(1);
                        iOrder.getRows().add(iRow);
                    } catch (Exception e) {
                        iBadOrders.add(iOrderNumber + " - Kunde inte skapa produktrad för frakt");
                        continue;
                    }
                }
                if(iPaymentString != null && !iPaymentString.equals("")){
                    boolean set = false;
                    for(SSPaymentTerm iPaymentTerm : SSDB.getInstance().getPaymentTerms()){
                        if(iPaymentTerm.getDescription().equals(iPaymentString)){
                            iOrder.setPaymentTerm(iPaymentTerm);
                            set = true;
                        }
                    }
                    if(!set) iOrder.setPaymentTerm(SSDB.getInstance().getCurrentCompany().getPaymentTerm());
                }
                iOrderCount++;
                SSDB.getInstance().addOrder(iOrder);
            }
            if(!iBadOrders.isEmpty()){
                BufferedWriter bw = new BufferedWriter(new PrintWriter(new File(SSPath.get(SSPath.APP_BASE), "orderimport.txt")));
                for(String iBadOrder:iBadOrders){
                    bw.write(iBadOrder);
                    bw.newLine();
                }
                bw.close();
                new SSInformationDialog(SSMainFrame.getInstance(), "orderframe.import.errors",iOrderCount.toString(), String.valueOf(iBadOrders.size()));
            }
            else{
                new SSInformationDialog(SSMainFrame.getInstance(), "orderframe.import.noerrors",iOrderCount.toString());
            }
        }
        catch (IOException e) {
            throw new SSImportException(e.getMessage());
        }
    }
}
