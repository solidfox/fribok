package se.swedsoft.bookkeeping.gui.util.filechooser;

import se.swedsoft.bookkeeping.gui.util.filechooser.util.SSDefaultFilter;
/**
 * Date: 2006-feb-13
 * Time: 13:57:23
 */
public class SSDefaultFileChooser extends SSFileChooser  {

    /**
     *
     */
    public SSDefaultFileChooser(){
        // Add a custom file filter
        addChoosableFileFilter (new SSDefaultFilter());
    }


}


