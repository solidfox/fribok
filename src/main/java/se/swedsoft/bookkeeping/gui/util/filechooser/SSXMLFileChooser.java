package se.swedsoft.bookkeeping.gui.util.filechooser;


import se.swedsoft.bookkeeping.gui.util.filechooser.util.SSFilterXML;


/**
 * Date: 2006-feb-13
 * Time: 14:47:02
 */
public class SSXMLFileChooser extends SSFileChooser {

    private static SSXMLFileChooser cInstance;

    /**
     *
     * @return
     */
    public static SSXMLFileChooser getInstance() {
        if (cInstance == null) {
            cInstance = new SSXMLFileChooser();
        }
        return cInstance;
    }

    /**
     *
     */
    private SSXMLFileChooser() {
        // Add a custom file filter
        addChoosableFileFilter(new SSFilterXML());
        // Disable the default (Accept All) file filter.
        setAcceptAllFileFilterUsed(false);
    }

}
