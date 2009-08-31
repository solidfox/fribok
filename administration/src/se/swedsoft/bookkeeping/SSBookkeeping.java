package se.swedsoft.bookkeeping;

import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.company.SSCompanyFrame;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.frame.SSFrameManager;
import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;
import se.swedsoft.bookkeeping.data.system.*;
import se.swedsoft.bookkeeping.data.util.SSConfig;
import se.swedsoft.bookkeeping.data.util.SSFileSystem;

import javax.swing.*;
import java.sql.*;
import java.io.File;

//import com.incors.plaf.alloy.AlloyLookAndFeel;


/**
 * 
 * $Id$
 */
public class SSBookkeeping {

    public static boolean iRunning;

    /**
     *
     */
    private static void printDataBaseInfo(){

    }

    /**
     *
     */
    private static void startupDatabase(){
        try {
        Class.forName("org.hsqldb.jdbcDriver" );
        } catch (Exception e) {
            System.out.println("ERROR: failed to load HSQLDB JDBC driver.");
            e.printStackTrace();
            return;
        }

        try {
            String iServerAddress = SSDBConfig.getServerAddress();
            if (iServerAddress.equals("")) {
                //K�r mot lokal databas! Ingen l�sning m.m.
                Connection iConnection = DriverManager.getConnection("jdbc:hsqldb:file:db"+File.separator+"JFSDB", "sa", "");
                SSDB.getInstance().startupLocal(iConnection);
            } else {
                //K�r mot en JFSServer specificerad i serveradressen.
                Connection iConnection = DriverManager.getConnection("jdbc:hsqldb:hsql://"+iServerAddress+"/JFSDB", "sa", "");
                SSDB.getInstance().startupRemote(iConnection, iServerAddress);
            }

        } catch (SQLException e) {
            SSQueryDialog iDialog = new SSQueryDialog(SSMainFrame.getInstance(),"noserverfoundstartup");
            if (iDialog.getResponce() == JOptionPane.YES_OPTION) {
                if(SSDB.getInstance().getLocking()){
                    SSCompanyLock.removeLock(SSDB.getInstance().getCurrentCompany());
                    SSYearLock.removeLock(SSDB.getInstance().getCurrentYear());
                }
                SSDBConfig.setServerAddress(null);
                SSDB.getInstance().setCurrentCompany(null);
                SSDB.getInstance().setCurrentYear(null);
                SSFrameManager.getInstance().close();
                SSDB.getInstance().loadLocalDatabase();
                SSCompanyFrame.showFrame(SSMainFrame.getInstance(), 500, 300);
            }
        }
    }

    /**
     * The main method of the program.
     *
     * @param args The arguments to the program.
     */
    public static void main(String[] args) {

        //AlloyLookAndFeel.setProperty("alloy.licenseCode"           , "4#JFS_Bokforing#m4zvft#5lkl8w");
        //AlloyLookAndFeel.setProperty("alloy.titlePaneTextAlignment", "left");

        try {
            //AlloyLookAndFeel iAlloyLookAndFeel =  new AlloyLookAndFeel();
            //UIManager.setLookAndFeel( iAlloyLookAndFeel );

            JDialog.setDefaultLookAndFeelDecorated(true);
            JFrame .setDefaultLookAndFeelDecorated(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        iRunning = true;
        System.out.println("Starting up...");
        System.out.println("Title     : " + SSVersion.app_title);
        System.out.println("Version   : " + SSVersion.app_version);
        System.out.println("Build     : " + SSVersion.app_build);
        System.out.println("Directory : " + SSFileSystem.getApplicationDirectory());
        System.out.println("");
        System.out.println("Operating system: " + System.getProperty("os.name"));
        System.out.println("Architecture    : " + System.getProperty("os.arch"));
        System.out.println("Java version    : " + System.getProperty("java.version"));
        System.out.println("");



        // Create and display the main iMainFrame.

        SSMainFrame iMainFrame = SSMainFrame.getInstance();
        UIManager.put("InternalFrame.icon", SSIcon.getIcon("ICON_FRAME"));
        UIManager.put("InternalFrame.inactiveIcon", SSIcon.getIcon("ICON_FRAME"));
        startupDatabase();
        
        // Display the main frame.
        iMainFrame.setVisible(true);

        SSDB.getInstance().readOldDatabase();

        // Only display the company iMainFrame if there are no companies defined.
        // I would prefer to only open the select company iMainFrame if there are no companies.
        // But Fredrik and Joakim wants it to displayed every time.
        if( (Boolean)SSConfig.getInstance().get("companyframe.showatstart", true) ){
            iMainFrame.showCompanyFrame();
        }

        SSDB.getInstance().init(true);

        // Perhaps add some type of shut down hook.
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                SSFrameManager.getInstance().storeAllFrames();

                if (SSDBConfig.getClientkey() != null && !SSDBConfig.getClientkey().equals("")) {
                    SSDB.getInstance().removeClient();
                }

                try {
                    iRunning = false;
                    SSDB.getInstance().shutdown();
                    // Shut down the database.
                    if(SSDB.getInstance().getLocking() && SSDB.getInstance().getSocket() != null){
                        SSDB.getInstance().getWriter().println("disconnect");
                        SSDB.getInstance().getWriter().flush();
                        SSDB.getInstance().getWriter().close();

                        SSDB.getInstance().getReader().close();
                        SSDB.getInstance().getSocket().close();
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }



}
