package se.swedsoft.bookkeeping.gui.util.filechooser;

import se.swedsoft.bookkeeping.gui.util.filechooser.util.SSFilterZIP;

/**
 * Date: 2006-feb-13
 * Time: 14:47:02
 */
public class SSBackupFileChooser extends SSFileChooser  {

    private static SSBackupFileChooser cInstance;

    public static SSBackupFileChooser getInstance(){
        if(cInstance == null){
            cInstance = new SSBackupFileChooser();
        }
        return cInstance;
    }
    /**
     *
     */
    private SSBackupFileChooser(){
        super();
        addChoosableFileFilter(getAcceptAllFileFilter());
        addChoosableFileFilter(new SSFilterZIP());

        //setFileFilter( getChoosableFileFilters()[0]);
    }


}