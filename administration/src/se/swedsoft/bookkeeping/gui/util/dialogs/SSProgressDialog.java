package se.swedsoft.bookkeeping.gui.util.dialogs;

import javax.swing.*;
import java.awt.*;

import com.sun.java.help.impl.SwingWorker;

/**
 * User: Fredrik Stigsson
 * Date: 2006-feb-01
 * Time: 16:32:05
 */
public class SSProgressDialog extends SSDialog {

    private JPanel iPanel;

    /**
     *
     * @param iFrame
     */
    public SSProgressDialog(JFrame iFrame, String iTitle){
        super(iFrame, iTitle, false);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setPanel(iPanel);

        pack();
        setLocationRelativeTo(iFrame);
    }

    /**
     *
     * @param iDialog
     */
    public SSProgressDialog(JDialog iDialog, String iTitle){
        super(iDialog, iTitle, false);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setPanel(iPanel);

        pack();
        setLocationRelativeTo(iDialog);
    }

    /**
     *
     * @param iFrame
     * @param iAction
     */
    public static void runProgress(JFrame iFrame, final Runnable iAction){
        runProgress(iFrame, "", iAction);
    }

    /**
     * 
     * @param iFrame
     * @param iTitle
     * @param iAction
     */
    public static void runProgress(JFrame iFrame, String iTitle, final Runnable iAction){
        final SSProgressDialog dialog  = new SSProgressDialog(iFrame, iTitle);
        dialog.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        dialog.setVisible();

        new SwingWorker() {
            @Override
            public Object construct() {
                try{
                    iAction.run();
                } finally{
                    dialog.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    dialog.setVisible(false);
                    dialog.dispose();
                }
                return null;
            }
        }.start();
    }


    /**
     *
     * @param iDialog
     * @param iAction
     */
    public static void runProgress(JDialog iDialog, final Runnable iAction){
        runProgress(iDialog, "", iAction);
    }

    /**
     *
     * @param iDialog
     * @param iTitle
     * @param iAction
     */
    public static void runProgress(JDialog iDialog, String iTitle, final Runnable iAction){
        final SSProgressDialog dialog  = new SSProgressDialog(iDialog, iTitle);
        dialog.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        dialog.setVisible();

        new SwingWorker() {
            @Override
            public Object construct() {
                try{
                    iAction.run();
                } finally{
                    dialog.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    dialog.setVisible(false);
                    dialog.dispose();
                }
                return null;
            }
        }.start();
    }



}
