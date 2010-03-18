package se.swedsoft.bookkeeping.gui.util.filechooser;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.filechooser.util.SSFileFilter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ResourceBundle;




/**
 * Date: 2006-feb-13
 * Time: 14:41:28
 */
public class SSFileChooser extends JFileChooser  {

    public static ResourceBundle bundle = SSBundle.getBundle();



    /**
     *
     */
    public SSFileChooser(){
        // Disable the default (Accept All) file filter.
        setAcceptAllFileFilterUsed(false);
    }


    /**
     *
     * @param iFilters
     */
    public SSFileChooser(SSFileFilter ... iFilters){
        // Disable the default (Accept All) file filter.
        setAcceptAllFileFilterUsed(false);
        for (SSFileFilter iFilter : iFilters) {
            addChoosableFileFilter (iFilter );
        }
    }




    /**
     * Pops up a "Save File" file chooser dialog. Note that the
     * text that appears in the approve button is determined by
     * the L&F.
     *
     * @param parent the parent component of the dialog,
     *               can be <code>null</code>;
     *               see <code>editDialog</code> for details
     * @return the return state of the file chooser on popdown:
     *         <ul>
     *         <li>JFileChooser.CANCEL_OPTION
     *         <li>JFileChooser.APPROVE_OPTION
     *         <li>JFileCHooser.ERROR_OPTION if an error occurs or the
     *         dialog is dismissed
     *         </ul>
     * @throws java.awt.HeadlessException if GraphicsEnvironment.isHeadless()
     *                                    returns true.
     * @see java.awt.GraphicsEnvironment#isHeadless
     * @see #showDialog
     */
    @Override
    public int showSaveDialog(Component parent) throws HeadlessException {
        int iResult =  super.showSaveDialog(parent);

        if(iResult != APPROVE_OPTION) return iResult;

        File iFile = getSelectedFile();
        if( iFile.exists()) {
            String iTitle   = getUI().getDialogTitle(this);
            String iMessage = String.format( bundle.getString("filechooser.fileexists"), iFile.getAbsolutePath() );

            if( JOptionPane.showConfirmDialog(null, iMessage, iTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION){
                return CANCEL_OPTION;
            }
        }
        return iResult;
    }

    /**
     *
     * @param pParent

     * @return int
     */
    public final int showDialog(Component pParent){
        int iResult = super.showDialog(pParent, null);

        if(iResult != APPROVE_OPTION) return iResult;

        if(getDialogType() == SAVE_DIALOG ){
            File iFile = getSelectedFile();
            if( iFile.exists()) {
                String iTitle   = getUI().getDialogTitle(this);
                String iMessage = String.format( bundle.getString("filechooser.fileexists"), iFile.getAbsolutePath() );

                if( JOptionPane.showConfirmDialog(null, iMessage, iTitle, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION){
                    return CANCEL_OPTION;
                }
            }

        }
        return iResult;
    }





}


