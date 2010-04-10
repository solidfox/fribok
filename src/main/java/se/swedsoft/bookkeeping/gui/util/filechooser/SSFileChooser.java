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
public class SSFileChooser extends JFileChooser {

    public static ResourceBundle bundle = SSBundle.getBundle();

    /**
     *
     */
    public SSFileChooser() {
        setAcceptAllFileFilterUsed(false);
    }

    /**
     *
     * @param iFilters
     */
    public SSFileChooser(SSFileFilter... iFilters) {
        setAcceptAllFileFilterUsed(false);
        for (SSFileFilter iFilter : iFilters) {
            addChoosableFileFilter(iFilter);
        }
    }

    @Override
    public int showSaveDialog(Component parent) throws HeadlessException {
        int iResult = super.showSaveDialog(parent);

        if (iResult != APPROVE_OPTION) {
            return iResult;
        }

        File iFile = getSelectedFile();

        if (iFile.exists()) {
            String iTitle = getUI().getDialogTitle(this);
            String iMessage = String.format(bundle.getString("filechooser.fileexists"),
                    iFile.getAbsolutePath());

            if (JOptionPane.showConfirmDialog(null, iMessage, iTitle,
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
                    != JOptionPane.YES_OPTION) {
                return CANCEL_OPTION;
            }
        }
        return iResult;
    }

    /**
     *
     * @param pParent
     *
     * @return int
     */
    public final int showDialog(Component pParent) {
        int iResult = super.showDialog(pParent, null);

        if (iResult != APPROVE_OPTION) {
            return iResult;
        }

        if (getDialogType() == SAVE_DIALOG) {
            File iFile = getSelectedFile();

            if (iFile.exists()) {
                String iTitle = getUI().getDialogTitle(this);
                String iMessage = String.format(bundle.getString("filechooser.fileexists"),
                        iFile.getAbsolutePath());

                if (JOptionPane.showConfirmDialog(null, iMessage, iTitle,
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)
                        != JOptionPane.YES_OPTION) {
                    return CANCEL_OPTION;
                }
            }

        }
        return iResult;
    }

}
