package se.swedsoft.bookkeeping.gui.util.filechooser;

import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.filechooser.util.SSFilterSIE;

import java.io.File;

/**
 * Date: 2006-feb-13
 * Time: 14:47:02
 */
public class SSSIEFileChooser extends SSFileChooser  {

    private static SSSIEFileChooser cInstance;

    public static SSSIEFileChooser getInstance(){
        if(cInstance == null){
            cInstance = new SSSIEFileChooser();
        }
        return cInstance;
    }
    /**
     *
     */
    private SSSIEFileChooser(){
        super();
        addChoosableFileFilter(getAcceptAllFileFilter());
        addChoosableFileFilter(new SSFilterSIE());
    }

    /**
     *
     */
    public void setDefaultFileName(){
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        if(iCompany != null){
            File iParent = getSelectedFile();

            File iFile = new File(iParent, iCompany.getName() + ".se");

            setSelectedFile(iFile);
        }
    }

}