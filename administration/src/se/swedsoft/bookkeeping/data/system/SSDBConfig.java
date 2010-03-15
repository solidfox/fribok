package se.swedsoft.bookkeeping.data.system;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import se.swedsoft.bookkeeping.app.SSPath;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * $Id$
 */
public class SSDBConfig {

    private final static File CONFIG_FILE = new File(SSPath.get(SSPath.APP_BASE), "database.config");

    private static String iClientKey;

    private static String iServerAddress;

    private static Integer iCompanyId;

    private static Integer iYearId;


    public static String getServerAddress() {
        return iServerAddress == null ? "" : iServerAddress;
    }

    public static Integer getCompanyId() {
        return iCompanyId;
    }

    public static void setCompanyId(Integer iId) {
        iCompanyId = iId;
        DOMParser iParser = new DOMParser();
        try {
            iParser.parse( new InputSource(new FileInputStream( CONFIG_FILE )) );
            iParser.getDocument().getDocumentElement().setAttribute("company",iCompanyId == null ? "" : iCompanyId.toString());

            //Write back the database path to the config file.
            OutputFormat iFormat = new OutputFormat(iParser.getDocument());
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(CONFIG_FILE), iFormat);
            serializer.serialize(iParser.getDocument());
        } catch (Exception ex) {
            ex.printStackTrace();                    
        }
    }

    public static Integer getYearId() {
        return iYearId;
    }

    public static void setYearId(Integer pCompanyId,Integer iId) {
        iYearId = iId;
        DOMParser iParser = new DOMParser();
        try {
            iParser.parse( new InputSource(new FileInputStream( CONFIG_FILE )) );
            iParser.getDocument().getDocumentElement().setAttribute("year",iYearId == null ? "" : iYearId.toString());

            boolean iExists = false;
            NodeList iCompanyElements = iParser.getDocument().getDocumentElement().getElementsByTagName("company");
            for (int i = 0; i < iCompanyElements.getLength(); i++) {
                Node iCompanyNode = iCompanyElements.item(i);
                if(iCompanyNode.getNodeType() == Node.ELEMENT_NODE){
                    Element iCompanyElement = (Element)iCompanyNode;
                    Integer iCompanyElementId = Integer.parseInt(iCompanyElement.getAttribute("id"));
                    if(iCompanyElementId.equals(pCompanyId)){
                        iCompanyElement.setAttribute("yearid",iId == null ? "" : iId.toString());
                        iExists = true;
                    }
                }
            }
            if(!iExists){
                Element iCompanyElement = iParser.getDocument().createElement("company");
                iCompanyElement.setAttribute("id", pCompanyId.toString());
                iCompanyElement.setAttribute("yearid", iId == null ? "" : iId.toString());
                iParser.getDocument().getDocumentElement().appendChild(iCompanyElement);
            }

            //Write back the database path to the config file.
            OutputFormat iFormat = new OutputFormat(iParser.getDocument());
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(CONFIG_FILE), iFormat);
            serializer.serialize(iParser.getDocument());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static SSNewAccountingYear loadCompanySetting(Integer pCompanyId) {
        if(pCompanyId == null) return null;
        DOMParser iParser = new DOMParser();
        try {
            iParser.parse( new InputSource(new FileInputStream( CONFIG_FILE )) );
            NodeList iCompanyElements = iParser.getDocument().getDocumentElement().getElementsByTagName("company");
            for (int i = 0; i < iCompanyElements.getLength(); i++) {
                Node iCompanyNode = iCompanyElements.item(i);
                if(iCompanyNode.getNodeType() == Node.ELEMENT_NODE){
                    Element iCompanyElement = (Element)iCompanyNode;
                    Integer iCompanyElementId = Integer.parseInt(iCompanyElement.getAttribute("id"));
                    if(iCompanyElementId.equals(pCompanyId)){
                        String iResult = iCompanyElement.getAttribute("yearid");
                        if (iResult == null || iResult.equals("")) {
                            return null;
                        }
                        SSNewAccountingYear iYear = new SSNewAccountingYear();
                        iYear.setId(Integer.parseInt(iResult));
                        return SSDB.getInstance().getAccountingYear(iYear);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void setServerAddress(String iAddress) {
        iServerAddress = iAddress;

        DOMParser iParser = new DOMParser();
        try {
            iParser.parse( new InputSource(new FileInputStream( CONFIG_FILE )) );

            iParser.getDocument().getDocumentElement().setAttribute("server", iServerAddress == null ? "" : iServerAddress);
            iParser.getDocument().getDocumentElement().setAttribute("company","");
            iParser.getDocument().getDocumentElement().setAttribute("year","");

            NodeList iCompanyElements = iParser.getDocument().getDocumentElement().getElementsByTagName("company");
            for (int i = 0; i < iCompanyElements.getLength(); i++) {
                Node iCompanyNode = iCompanyElements.item(i);
                iParser.getDocument().getDocumentElement().removeChild(iCompanyNode);
            }



            //Write back the database path to the config file.
            OutputFormat iFormat = new OutputFormat(iParser.getDocument());
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(CONFIG_FILE), iFormat);
            serializer.serialize(iParser.getDocument());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void setClientKey(String iKey) {
        iClientKey = iKey;
        DOMParser iParser = new DOMParser();
        try {
            iParser.parse( new InputSource(new FileInputStream( CONFIG_FILE )) );
            iParser.getDocument().getDocumentElement().setAttribute("clientkey", iClientKey == null ? "" : iClientKey);

            //Write back the database path to the config file.
            OutputFormat iFormat = new OutputFormat(iParser.getDocument());
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(CONFIG_FILE), iFormat);
            serializer.serialize(iParser.getDocument());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static String getClientkey() {
        return iClientKey == null ? "" : iClientKey;
    }
    static{
        load();
    }

    /**
     *
     */
    public static void load(){

        DOMParser iParser = new DOMParser();
        try {
            //  parser.set(false)
            iParser.parse( new InputSource(new FileInputStream( CONFIG_FILE )) );

            String iServer = null, iCompany = null, iYear = null, iKey = null;

            if(iParser.getDocument().getDocumentElement().hasAttribute("server")){
                iServer  =  iParser.getDocument().getDocumentElement().getAttribute("server");
            }

            if(iParser.getDocument().getDocumentElement().hasAttribute("company")){
                iCompany  =  iParser.getDocument().getDocumentElement().getAttribute("company");
            }
            if (iCompany != null && !iCompany.equals("")) {
                iCompanyId = Integer.parseInt(iCompany);
            }

            if(iParser.getDocument().getDocumentElement().hasAttribute("year")){
                iYear  =  iParser.getDocument().getDocumentElement().getAttribute("year");
            }
            if (iYear != null && !iYear.equals("")) {
                iYearId = Integer.parseInt(iYear);
            }

            if (iParser.getDocument().getDocumentElement().hasAttribute("clientkey")) {
                iKey = iParser.getDocument().getDocumentElement().getAttribute("clientkey");
            } else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH.mm z");
                setClientKey(dateFormat.format(new Date()));
            }

            SSDBConfig.iClientKey = iKey;

            SSDBConfig.iServerAddress = iServer;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
