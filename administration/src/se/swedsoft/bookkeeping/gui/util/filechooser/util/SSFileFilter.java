package se.swedsoft.bookkeeping.gui.util.filechooser.util;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.List;
import java.util.LinkedList;

/**
 * Date: 2006-feb-13
 * Time: 14:49:08
 */
public abstract class SSFileFilter extends FileFilter {

    private List<String> iExtensions;

    public SSFileFilter(){
        iExtensions = new LinkedList<String>();
    }

    /**
     *
     * @param pExtension
     */
    protected void addExtension(String pExtension){
        iExtensions.add( pExtension.toLowerCase() );
    }

    /**
     *
     * @param pFile
     * @return Accept all directories and all gif, jpg, tiff, or png files.
     */
    @Override
    public final boolean accept(File pFile) {
        if (pFile.isDirectory()) {
            return true;
        }

        String iExtension = getExtension(pFile);

        if( iExtension == null )  return false;

        for(String iCurrent : iExtensions ){
            if( iCurrent.equals( iExtension ) ) return true;
        }

        return false;
    }


    /*
    * Get the lowercase extension of a file.
    */
    private String getExtension(File pFile) {
        String ext = null;
        String s   = pFile.getName();

        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }

        return ext == null ? null : ext.toLowerCase();
    }


    /**
     * @return The description of this filter
     */
    @Override
    public abstract String getDescription();

}
