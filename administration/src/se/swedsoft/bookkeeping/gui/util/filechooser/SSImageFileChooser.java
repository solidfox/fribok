package se.swedsoft.bookkeeping.gui.util.filechooser;

import se.swedsoft.bookkeeping.gui.util.filechooser.util.SSImagePreview;
import se.swedsoft.bookkeeping.gui.util.filechooser.util.SSImageFilter;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Date: 2006-feb-10
 * Time: 15:05:52
 */
public class SSImageFileChooser extends SSFileChooser  {

    private static SSImageFileChooser cInstance;

    public static SSImageFileChooser getInstance(){
        if(cInstance == null){
            cInstance = new SSImageFileChooser();
        }
        return cInstance;
    }

    /**
     *
     */
    private SSImageFileChooser(){
        super();
        // Add a custom file filter
        addChoosableFileFilter    (new SSImageFilter());
        //Add the preview pane.
        setAccessory(new SSImagePreview(this));
    }




}


