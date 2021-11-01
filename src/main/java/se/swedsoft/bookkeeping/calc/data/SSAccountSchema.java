package se.swedsoft.bookkeeping.calc.data;


import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.fribok.bookkeeping.app.Path;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;


/**
 * Date: 2006-feb-27
 * Time: 12:48:13
 */
public class SSAccountSchema implements Serializable {

    private static String cParserClass = "org.apache.xerces.parsers.SAXParser";

    private List<SSAccountGroup> iResultGroups;

    private List<SSAccountGroup> iBalanceGroups;

    /**
     *
     */
    private SSAccountSchema() {
        iResultGroups = new LinkedList<SSAccountGroup>();
        iBalanceGroups = new LinkedList<SSAccountGroup>();
    }

    /**
     * Returns the result groups
     * @return
     */
    public List<SSAccountGroup> getResultGroups() {
        return iResultGroups;
    }

    /**
     * Returns the balance groups
     * @return
     */
    public List<SSAccountGroup> getBalanceGroups() {
        return iBalanceGroups;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ResultGroups: {\n");
        for (SSAccountGroup iLevelOne : iResultGroups) {
            sb.append(iLevelOne);
        }
        sb.append("}\n");

        sb.append("BalanceGroups: {\n");
        for (SSAccountGroup iLevelOne : iBalanceGroups) {
            sb.append(iLevelOne);
        }
        sb.append("}\n");

        return sb.toString();
    }

    private static Map<String, SSAccountSchema> iSchemaCache = new HashMap<String, SSAccountSchema>();

    /**
     *
     * @param iSchema
     * @return
     */
    public static SSAccountSchema getAccountSchema(String iSchema) {
        SSAccountSchema iAccountSchema;

        if (iSchemaCache.containsKey(iSchema)) {
            iAccountSchema = iSchemaCache.get(iSchema);
        } else {
            InputStream is = SSAccountSchema.class.getResourceAsStream(
                    "/account/" + iSchema);

            iAccountSchema = createAccountSchema(is);
            iSchemaCache.put(iSchema, iAccountSchema);
        }

        return iAccountSchema;
    }

    /**
     *
     * @param pYearData
     * @return
     */
    public static SSAccountSchema getAccountSchema(SSNewAccountingYear pYearData) {
        String iSchema = pYearData.getAccountPlan().getType().getSchema();

        return getAccountSchema(iSchema);
    }

    /**
     *
     * @param iFile
     * @return
     */
    private static SSAccountSchema createAccountSchema(InputStream is) {
        SSAccountSchema iSchema = new SSAccountSchema();

        XMLReader iReader;

        try {
            iReader = XMLReaderFactory.createXMLReader(cParserClass);
        } catch (SAXException e) {
            e.printStackTrace();
            return iSchema;
        }

        iReader.setContentHandler(new AccountGroupLoader(iSchema));

        try {
            iReader.parse(new InputSource(is));
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return iSchema;
    }

    /**
     *
     */
    private static class AccountGroupLoader extends DefaultHandler {

        private SSAccountSchema iSchema;

        private List<SSAccountGroup> iLevelOne;

        private Stack<SSAccountGroup> iLevelTwo;

        /**
         *
         * @param pSchema
         */
        public AccountGroupLoader(SSAccountSchema pSchema) {
            iSchema = pSchema;
            iLevelOne = null;
            iLevelTwo = new Stack<SSAccountGroup>();
        }

        /**
         *
         * @param iAttributes
         * @return
         */
        private SSAccountGroup createGroup(Attributes iAttributes) {
            String iId = iAttributes.getValue("id");
            String iBundle = iAttributes.getValue("bundle");
            String iFromAccount = iAttributes.getValue("fromAccount");
            String iToAccount = iAttributes.getValue("toAccount");

            SSAccountGroup iAccountGroup = new SSAccountGroup();

            iAccountGroup.setBundle(iBundle);
            iAccountGroup.setId(Integer.decode(iId));

            if (iFromAccount != null) {
                iAccountGroup.setFromAccount(Integer.decode(iFromAccount));
            }
            if (iToAccount != null) {
                iAccountGroup.setToAccount(Integer.decode(iToAccount));
            }

            add(iAccountGroup);

            return iAccountGroup;
        }

        /**
         *
         * @param pGroup
         */
        private void add(SSAccountGroup pGroup) {
            if (iLevelTwo.isEmpty()) {
                iLevelOne.add(pGroup);
            } else {
                iLevelTwo.peek().addAccountGroup(pGroup);
            }
        }

        /**
         *
         * @param uri
         * @param localName
         * @param qName
         * @param iAttributes
         * @throws SAXException
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes iAttributes) throws SAXException {
            if (localName.equalsIgnoreCase("result")) {
                iLevelOne = iSchema.iResultGroups;
            }
            if (localName.equalsIgnoreCase("balance")) {
                iLevelOne = iSchema.iBalanceGroups;
            }

            if (localName.equalsIgnoreCase("group")) {
                iLevelTwo.push(createGroup(iAttributes));
            }

        }

        /**
         *
         * @param uri
         * @param localName
         * @param qName
         * @throws SAXException
         */
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (localName.equalsIgnoreCase("result")) {
                iLevelOne = null;
            }
            if (localName.equalsIgnoreCase("balance")) {
                iLevelOne = null;
            }
            if (localName.equalsIgnoreCase("group")) {
                iLevelTwo.pop();
            }

        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();

            sb.append(
                    "se.swedsoft.bookkeeping.calc.data.SSAccountSchema.AccountGroupLoader");
            sb.append("{iLevelOne=").append(iLevelOne);
            sb.append(", iLevelTwo=").append(iLevelTwo);
            sb.append(", iSchema=").append(iSchema);
            sb.append('}');
            return sb.toString();
        }
    }
}
