package se.swedsoft.bookkeeping.importexport.xml;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import se.swedsoft.bookkeeping.data.SSOrder;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2007-mar-29
 * Time: 15:16:52
 */
public class SSOrderExporter {

    private List<SSOrder> iItems;

    private File iFile;

    public SSOrderExporter(File pFile, List<SSOrder> pOrders) {

        iItems = pOrders;
        iFile = pFile;
    }


    public void doExport() {
        Element iElement;
        Element iSubElement;
        Element iRoot2;
        Element iSubElement2;
        Node iNode;

        Document iXmlDoc= new DocumentImpl();
        Element iRoot = iXmlDoc.createElement("Orders");

        for (SSOrder iOrder : iItems) {
            iElement = iXmlDoc.createElementNS(null, "Order");

            iSubElement = iXmlDoc.createElementNS(null,"SellerOrderNo");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getNumber() == null ? "" : iOrder.getNumber().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"OrderDate");
            iElement.appendChild(iSubElement);
            SimpleDateFormat iFormat = new SimpleDateFormat("yyyy-MM-dd");
            iNode = iXmlDoc.createTextNode(iOrder.getDate() == null ? "" : iFormat.format(iOrder.getDate()));
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"CustomerNumber");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getCustomerNr());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"CustomerName");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getCustomerName());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"OurContactPerson");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getOurContactPerson());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"YourContactPerson");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getYourContactPerson());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"DelayInterest");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getDelayInterest() == null ? "": iOrder.getDelayInterest().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"CurrencyCode");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getCurrency() == null ? "" : iOrder.getCurrency().getName());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"PaymentTerms");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getPaymentTerm() == null ? "" : iOrder.getPaymentTerm().getName());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"DeliveryTerms");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getDeliveryTerm() == null ? "" : iOrder.getDeliveryTerm().getName());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"DeliveryMethod");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getDeliveryWay() == null ? "" : iOrder.getDeliveryWay().getName());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"TaxFree");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(Boolean.toString(iOrder.getTaxFree()));
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"Text");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getText() == null ? "" : iOrder.getText());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"TaxRate1");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getTaxRate1() == null ? "" : iOrder.getTaxRate1().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"TaxRate2");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getTaxRate2() == null ? "" : iOrder.getTaxRate2().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"TaxRate3");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getTaxRate3() == null ? "" : iOrder.getTaxRate3().toString());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"EuSaleCommodity");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(Boolean.toString(iOrder.getEuSaleCommodity()));
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"EuSaleThirdPartCommodity");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(Boolean.toString(iOrder.getEuSaleThirdPartCommodity()));
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"VATRegNo");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getCustomer() == null ? "" : iOrder.getCustomer().getVATNumber());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"Email");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getCustomer() == null ? "" : iOrder.getCustomer().getEMail());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"CompanyNo");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getCustomer() == null ? "" : iOrder.getCustomer().getRegistrationNumber());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"Telefax");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getCustomer() == null ? "" : iOrder.getCustomer().getTelefax());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"Telephone");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getCustomer() == null ? "" : iOrder.getCustomer().getPhone1());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"Telephone2");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getCustomer() == null ? "" : iOrder.getCustomer().getPhone2());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"InvoiceName");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getInvoiceAddress() == null ? "" : iOrder.getInvoiceAddress().getName());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"InvoiceAddress1");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getInvoiceAddress() == null ? "" : iOrder.getInvoiceAddress().getAddress1());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"InvoiceAddress2");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getInvoiceAddress() == null ? "" : iOrder.getInvoiceAddress().getAddress2());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"InvoicePostCode");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getInvoiceAddress() == null ? "" : iOrder.getInvoiceAddress().getZipCode());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"InvoicePostOffice");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getInvoiceAddress() == null ? "" : iOrder.getInvoiceAddress().getCity());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"InvoiceCountry");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getInvoiceAddress() == null ? "" : iOrder.getInvoiceAddress().getCountry());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"DeliveryName");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getDeliveryAddress() == null ? "" : iOrder.getDeliveryAddress().getName());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"DeliveryAddress1");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getDeliveryAddress() == null ? "" : iOrder.getDeliveryAddress().getAddress1());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"DeliveryAddress2");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getDeliveryAddress() == null ? "" : iOrder.getDeliveryAddress().getAddress2());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"DeliveryPostCode");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getDeliveryAddress() == null ? "" : iOrder.getDeliveryAddress().getZipCode());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"DeliveryPostOffice");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getDeliveryAddress() == null ? "" : iOrder.getDeliveryAddress().getCity());
            iSubElement.appendChild(iNode);

            iSubElement = iXmlDoc.createElementNS(null,"DeliveryCountry");
            iElement.appendChild(iSubElement);
            iNode = iXmlDoc.createTextNode(iOrder.getDeliveryAddress() == null ? "" : iOrder.getDeliveryAddress().getCountry());
            iSubElement.appendChild(iNode);

            iRoot2 = iXmlDoc.createElement("Detail");

            for (SSSaleRow iRow : iOrder.getRows()) {
                iSubElement = iXmlDoc.createElementNS(null,"ArticleRow");

                iSubElement2 = iXmlDoc.createElementNS(null,"SellerArticleNo");
                iNode = iXmlDoc.createTextNode(iRow.getProductNr());
                iSubElement2.appendChild(iNode);
                iSubElement.appendChild(iSubElement2);

                iSubElement2 = iXmlDoc.createElementNS(null,"ArticleDescription");
                iNode = iXmlDoc.createTextNode(iRow.getDescription());
                iSubElement2.appendChild(iNode);
                iSubElement.appendChild(iSubElement2);

                iSubElement2 = iXmlDoc.createElementNS(null,"UnitPrice");
                iNode = iXmlDoc.createTextNode(iRow.getUnitprice() == null ? "" : iRow.getUnitprice().toString());
                iSubElement2.appendChild(iNode);
                iSubElement.appendChild(iSubElement2);

                iSubElement2 = iXmlDoc.createElementNS(null,"QuantityOrdered");
                iNode = iXmlDoc.createTextNode(iRow.getQuantity() == null ? "" : iRow.getQuantity().toString());
                iSubElement2.appendChild(iNode);
                iSubElement.appendChild(iSubElement2);

                iSubElement2 = iXmlDoc.createElementNS(null,"Unit");
                iNode = iXmlDoc.createTextNode(iRow.getUnit() == null ? "" : iRow.getUnit().toString());
                iSubElement2.appendChild(iNode);
                iSubElement.appendChild(iSubElement2);

                iSubElement2 = iXmlDoc.createElementNS(null,"TotalLineDiscountPercent");
                iNode = iXmlDoc.createTextNode(iRow.getDiscount() == null ? "" : iRow.getDiscount().toString());
                iRow.getNormalizedDiscount();
                iSubElement2.appendChild(iNode);
                iSubElement.appendChild(iSubElement2);

                iSubElement2 = iXmlDoc.createElementNS(null,"SumOfLine");
                iNode = iXmlDoc.createTextNode(iRow.getSum() == null ? "" : iRow.getSum().toString());
                iSubElement2.appendChild(iNode);
                iSubElement.appendChild(iSubElement2);

                iSubElement2 = iXmlDoc.createElementNS(null,"VATPercentage");
                iNode = iXmlDoc.createTextNode(iRow.getTaxCode() == null ? "" : iRow.getTaxCode().toString());
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
