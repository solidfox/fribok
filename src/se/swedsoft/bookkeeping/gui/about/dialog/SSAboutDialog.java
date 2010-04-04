package se.swedsoft.bookkeeping.gui.about.dialog;

import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.about.panel.SSAboutPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * Date: 2006-mar-14
 * Time: 15:57:57
 */
public class SSAboutDialog {


    private static ResourceBundle bundle = SSBundle.getBundle();

    private SSAboutDialog() {
    }

    /**
     *
     * @param iMainFrame
     */
    public static void showDialog(final SSMainFrame iMainFrame) {

        final SSDialog       iDialog = new SSDialog(iMainFrame, bundle.getString("aboutframe.title"));
        final SSAboutPanel   iPanel  = new SSAboutPanel();

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);
        iDialog.setResizable(false);

        iPanel.addCloseButtonListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iDialog.closeDialog();
            }
        });

        iDialog.pack();
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible();
    }
}
