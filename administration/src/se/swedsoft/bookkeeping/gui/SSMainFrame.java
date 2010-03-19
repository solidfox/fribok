/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui;

import se.swedsoft.bookkeeping.SSVersion;
import se.swedsoft.bookkeeping.data.util.SSConfig;
import se.swedsoft.bookkeeping.gui.company.SSCompanyFrame;
import se.swedsoft.bookkeeping.gui.status.SSMainStatusBar;
import se.swedsoft.bookkeeping.gui.status.SSStatusBar;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.graphics.SSImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.util.ResourceBundle;

/**
 * This class implements the main frame as well as the main program.
 *
 * $Id$
 */
public class SSMainFrame extends JFrame {

    private static SSMainFrame cInstance;

    /**
     * Returns the instance of the main frame
     * @return
     */
    public static SSMainFrame getInstance(){
        if(cInstance == null){
            cInstance = new SSMainFrame();
        }
        return cInstance;
    }

    private static ResourceBundle bundle = SSBundle.getBundle();


    // The desktop pane.
    private JDesktopPane iDesktop;

    // The status bar.
    private JPanel iStatusBar;


    /**
     * Default constructor. <P>
     * Creates and displays the main window of the application.
     */
    private SSMainFrame() {
        super( SSVersion.app_title );

        // Exit the program when the main frame is closed.
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        Dimension iScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Get the last know size of the frame from the config
        Integer iExtendedsState = (Integer)SSConfig.getInstance().get("mainframe.extendedstate", JFrame.NORMAL);
        Integer iWidth          = (Integer)SSConfig.getInstance().get("mainframe.width"        , 1024);
        Integer iHeight         = (Integer)SSConfig.getInstance().get("mainframe.height"       , 750);

        int x = Math.max((int)(iScreenSize.getWidth()  - iWidth ) / 2, 0);
        int y = Math.max((int)(iScreenSize.getHeight() - iHeight) / 2, 0);
        int w = Math.min(iWidth , (int)iScreenSize.getWidth () );
        int h = Math.min(iHeight, (int)iScreenSize.getHeight() );

        setBounds(x, y, w, h);

        setExtendedState(iExtendedsState);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveSizeAndLocation();
            }
        });



        // Load and add a menu.
        SSMainMenu iMainMenu = new SSMainMenu(this);

        setIconImage(SSImage.getImage("ICON_LOGO"));
        setJMenuBar( iMainMenu.getMenuBar() );

        // Add the desktop pane.
        getContentPane().setLayout(new BorderLayout());

        iDesktop   = new JDesktopPane();//new SSDesktopPane( SSImage.getImage("BACKGROUND") );
        iStatusBar = getStatusBar();

        add(iDesktop  , BorderLayout.CENTER);
        add(iStatusBar, BorderLayout.SOUTH);

      
    }

    /**
     * Creates the status bar for the mainframe
     * @return The statusbar
     */
    private JPanel getStatusBar(){

        SSMainStatusBar iPanels = new SSMainStatusBar();

        SSStatusBar iStatusBar = new SSStatusBar();
        iStatusBar.addPanel( iPanels.getNameLabel() );
        iStatusBar.addSpacer();
        iStatusBar.addPanel(iPanels.getMemLabel());
        iStatusBar.addPanel( iPanels.getReadonlyLabel() );
         iStatusBar.addSeperator();
        iStatusBar.addPanel( iPanels.getCompanyLabel() );
        iStatusBar.addSeperator();
        iStatusBar.addPanel( iPanels.getYearLabel());

        return iStatusBar;
    }

    /**
     *
     */
    public void saveSizeAndLocation(){
        Integer iExtendedsState = getExtendedState();
        Integer iWidth          = getWidth();
        Integer iHeight         = getHeight();

        SSConfig.getInstance().set("mainframe.extendedstate", iExtendedsState);
        SSConfig.getInstance().set("mainframe.width"        , iWidth);
        SSConfig.getInstance().set("mainframe.height"       , iHeight);
    }


    /**
     * Shows the startup company frame
     */
    public void showCompanyFrame() {
        // Create the startup company frame.
        SSCompanyFrame.showFrame(this, 500, 300);

        try {
            SSCompanyFrame.getInstance().setSelected(true);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the desktop pane for the application. <P>
     *
     * @return The JDesktopPane.
     */
    public JDesktopPane getDesktopPane() {
        return iDesktop;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.SSMainFrame");
        sb.append("{iDesktop=").append(iDesktop);
        sb.append(", iStatusBar=").append(iStatusBar);
        sb.append('}');
        return sb.toString();
    }
}
