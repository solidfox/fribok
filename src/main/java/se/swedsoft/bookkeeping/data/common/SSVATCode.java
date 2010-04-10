package se.swedsoft.bookkeeping.data.common;


import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import se.swedsoft.bookkeeping.app.Path;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.io.InputStream;


/**
 * Date: 2006-mar-01
 * Time: 09:36:34
 */
public class SSVATCode implements SSTableSearchable {

    private String iName;

    private String iDescription;

    /**
     *
     */
    private SSVATCode() {
        iName = null;
        iDescription = null;
    }

    // //////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return the name
     */
    public String getName() {
        return iName;
    }

    /**
     *
     * @param iName
     */
    public void setName(String iName) {
        this.iName = iName;
    }

    // //////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return the description
     */
    public String getDescription() {
        return iDescription;
    }

    /**
     *
     * @param iDescription
     */
    public void setDescription(String iDescription) {
        this.iDescription = iDescription;
    }

    // //////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @param iBundle
     */
    public void setBundle(String iBundle) {
        iDescription = SSBundle.getBundle().getString(iBundle);
    }

    // //////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the render string to be shown in the tables
     *
     * @return The searchable string
     */
    public String toRenderString() {
        return iName;
    }

    public String toString() {
        return iName;
    }

    public boolean equals(Object obj) {
        if (obj instanceof SSVATCode) {
            SSVATCode iVATCode = (SSVATCode) obj;

            return iName != null && iName.equalsIgnoreCase(iVATCode.iName);
        }
        if (obj instanceof String) {
            String iVATCode = (String) obj;

            return iName != null && iName.equalsIgnoreCase(iVATCode);
        }

        return false;
    }

    public static SSVATCode VAT_NULL = new SSVATCode();

    private static List<SSVATCode> iValues;

    /**
     *
     * @return the vat codes
     */
    public static List<SSVATCode> getValues() {
        if (iValues == null) {
            iValues = new LinkedList<SSVATCode>();

            DOMParser iParser = new DOMParser();

            try {
                // parser.set(false)
                InputStream istream = SSVATCode.class.getResourceAsStream("/vatcodes.xml");

                iParser.parse(new InputSource(istream));
                NodeList iNodes = iParser.getDocument().getDocumentElement().getElementsByTagName(
                        "vatcode");

                for (int i = 0; i < iNodes.getLength(); i++) {
                    Node iNode = iNodes.item(i);

                    String iName = iNode.getAttributes().getNamedItem("name").getNodeValue();
                    String iDescription = iNode.getAttributes().getNamedItem("description").getNodeValue();

                    SSVATCode iVatCode = new SSVATCode();

                    iVatCode.iName = iName;
                    iVatCode.iDescription = iDescription;

                    iValues.add(iVatCode);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return iValues;
    }

    /**
     * Decodes a string to a vatcode
     *
     * @param iVATCode
     * @return the vatcode
     */
    public static SSVATCode decode(String iVATCode) {

        for (SSVATCode iVatCode: getValues()) {

            if (iVatCode.equals(iVATCode)) {
                return iVatCode;
            }
        }
        return null;
    }

}

