package se.swedsoft.bookkeeping.gui.util.filechooser;

import se.swedsoft.bookkeeping.gui.util.filechooser.util.SSFilterXLS;

/**
 * Date: 2006-feb-13
 * Time: 14:47:02
 */
public class SSExcelFileChooser extends SSFileChooser  {

    private static SSExcelFileChooser cInstance;

    /**
     *
     * @return
     */
    public static SSExcelFileChooser getInstance(){
        if(cInstance == null){
            cInstance = new SSExcelFileChooser();
        }
        return cInstance;
    }
    
    /**
     *
     */
    private SSExcelFileChooser(){
        super();
        // Add a custom file filter
        addChoosableFileFilter    (new SSFilterXLS());
        // Disable the default (Accept All) file filter.
        setAcceptAllFileFilterUsed(false);
    }

}