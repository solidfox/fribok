package se.swedsoft.bookkeeping.gui.license.dialog;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSInformationDialog;
import se.swedsoft.bookkeeping.gui.license.panel.SSLicensePanel;
import se.swedsoft.bookkeeping.license.SSLicenseFactory;
import se.swedsoft.bookkeeping.license.SSLicenseType;
import se.swedsoft.bookkeeping.util.SSLicenseInfo;
import se.swedsoft.bookkeeping.data.system.SSDBConfig;

import javax.swing.*;
import java.util.ResourceBundle;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.*;
import java.text.DateFormat;
import java.sql.*;
import java.io.IOException;

/**
 * Date: 2006-mar-16
 * Time: 16:39:45
 */
public class SSLicenseDialog {

    private static ResourceBundle bundle = SSBundle.getBundle();

    /**
     *
     * @param iMainFrame
     */
    public static void showDialog(final SSMainFrame iMainFrame) {
        final JDialog        iDialog = new JDialog();
        final SSLicensePanel iPanel  = new SSLicensePanel();

        iDialog.setTitle(bundle.getString("licenseframe.title"));
        iDialog.setModal(true);
        iDialog.setResizable(false);

        iDialog.add(iPanel.getPanel(), BorderLayout.CENTER);

        iPanel.addOkListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String iName    = iPanel.getName();
                String iKey     = iPanel.getKey();
                String iCompany = iPanel.getCompany();

                if(!SSLicenseFactory.isValid(iName, iKey)){
                    new SSInformationDialog(iMainFrame, "licenseframe.invalidkey");
                    return;
                }

                SSLicenseType iType    = SSLicenseFactory.getType   (iKey);
                Date          iExpires = SSLicenseFactory.getExpires(iKey);

                SSLicenseInfo iLicense = new SSLicenseInfo(iType, iName);

                iLicense.setCompany(iCompany);
                iLicense.setExpires(iExpires);

                SSLicenseInfo.setLicense(iLicense);

                if(SSLicenseFactory.getType(iKey) == SSLicenseType.Evaluation){
                    DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

                    String iExpiresText = iFormat.format( SSLicenseFactory.getExpires(iKey) );

                    new SSInformationDialog(iMainFrame, "licenseframe.evaluation", iExpiresText);
                }
                SSDBConfig.setLicenseKey(iKey);

                try {
                    JEditorPane iEditorPane = new JEditorPane("http://support.jfsbokforing.se/regkey.php?key="+iKey);
                    iEditorPane = null;
                } catch (Exception e1) {
                    iDialog.setVisible(false);
                }


                iDialog.setVisible(false);
            }
        });

        iPanel.addCancelListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        iDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        iDialog.pack();

        Dimension iScreenSize = Toolkit.getDefaultToolkit().getScreenSize();


        int x = Math.max((int)(iScreenSize.getWidth()  - iDialog.getWidth() ) / 2, 0);
        int y = Math.max((int)(iScreenSize.getHeight() - iDialog.getHeight()) / 2, 0);

        int w = Math.min(iDialog.getWidth() , (int)iScreenSize.getWidth () );
        int h = Math.min(iDialog.getHeight(), (int)iScreenSize.getHeight() );

        iDialog.setBounds(x, y, w, h);
        iDialog.setVisible(true);
    }
}
