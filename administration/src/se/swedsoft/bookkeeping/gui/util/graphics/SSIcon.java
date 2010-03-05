package se.swedsoft.bookkeeping.gui.util.graphics;

import se.swedsoft.bookkeeping.data.util.SSFileSystem;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2006-feb-02
 * Time: 10:11:34
 */
public class SSIcon {

    // Standard location for graphics
    public static final String cIconDirectory = SSFileSystem.getIconDirectory();


    public enum IconState {
        NORMAL     ("NORMAL"     ),
        DISABLED   ("DISABLED"   ),
        HIGHLIGHTED("HIGHLIGHTED");

        private final String iName;

        IconState(String pName) {
            this.iName = pName;
        }
        public String getName() {
            return iName;
        }
    }

    // Library of all graphics
    private static Map<String, ImageIcon> iIcons = new HashMap<String, ImageIcon>();

    // Standard graphics
    static{
        SSIcon.loadIcon("ICON_EXPORT", IconState.NORMAL     , "Export from Database 24 n p.png" );
        SSIcon.loadIcon("ICON_EXPORT", IconState.DISABLED   , "Export from Database 24 d p.png" );
        SSIcon.loadIcon("ICON_EXPORT", IconState.HIGHLIGHTED, "Export from Database 24 h p.png" );

        SSIcon.loadIcon("ICON_IMPORT", IconState.NORMAL     , "Import to Database 24 n p.png" );
        SSIcon.loadIcon("ICON_IMPORT", IconState.DISABLED   , "Import to Database 24 d p.png" );
        SSIcon.loadIcon("ICON_IMPORT", IconState.HIGHLIGHTED, "Import to Database 24 h p.png" );

        SSIcon.loadIcon("ICON_PRINT", IconState.NORMAL     , "Printer 24 n p.png" );
        SSIcon.loadIcon("ICON_PRINT", IconState.DISABLED   , "Printer 24 d p.png" );
        SSIcon.loadIcon("ICON_PRINT", IconState.HIGHLIGHTED, "Printer 24 h p.png" );


        SSIcon.loadIcon("ICON_NEWITEM", IconState.NORMAL     , "New Blank Document 24 n p.png" );
        SSIcon.loadIcon("ICON_NEWITEM", IconState.DISABLED   , "New Blank Document 24 d p.png" );
        SSIcon.loadIcon("ICON_NEWITEM", IconState.HIGHLIGHTED, "New Blank Document 24 h p.png" );

        SSIcon.loadIcon("ICON_OPENITEM", IconState.NORMAL     , "Open File or Folder 24 n p.png" );
        SSIcon.loadIcon("ICON_OPENITEM", IconState.DISABLED   , "Open File or Folder 24 d p.png" );
        SSIcon.loadIcon("ICON_OPENITEM", IconState.HIGHLIGHTED, "Open File or Folder 24 h p.png" );

        SSIcon.loadIcon("ICON_SAVEITEM", IconState.NORMAL     , "Save Blue 24 n p.png" );
        SSIcon.loadIcon("ICON_SAVEITEM", IconState.DISABLED   , "Save Blue 24 d p.png" );
        SSIcon.loadIcon("ICON_SAVEITEM", IconState.HIGHLIGHTED, "Save Blue 24 h p.png" );

        SSIcon.loadIcon("ICON_COPYITEM", IconState.NORMAL     , "Copy 24 n p.png" );
        SSIcon.loadIcon("ICON_COPYITEM", IconState.DISABLED   , "Copy 24 d p.png" );
        SSIcon.loadIcon("ICON_COPYITEM", IconState.HIGHLIGHTED, "Copy 24 h p.png" );


        SSIcon.loadIcon("ICON_CANCELITEM", IconState.NORMAL     , "Red Delete 24 n p.png" );
        SSIcon.loadIcon("ICON_CANCELITEM", IconState.DISABLED   , "Red Delete 24 d p.png" );
        SSIcon.loadIcon("ICON_CANCELITEM", IconState.HIGHLIGHTED, "Red Delete 24 h p.png" );


        SSIcon.loadIcon("ICON_EDITITEM", IconState.NORMAL     , "Edit 24 n p.png" );
        SSIcon.loadIcon("ICON_EDITITEM", IconState.DISABLED   , "Edit 24 d p.png" );
        SSIcon.loadIcon("ICON_EDITITEM", IconState.HIGHLIGHTED, "Edit 24 h p.png" );

        SSIcon.loadIcon("ICON_DELETEITEM", IconState.NORMAL     , "Delete Document 3 24 n p.png" );
        SSIcon.loadIcon("ICON_DELETEITEM", IconState.DISABLED   , "Delete Document 3 24 d p.png" );
        SSIcon.loadIcon("ICON_DELETEITEM", IconState.HIGHLIGHTED, "Delete Document 3 24 h p.png" );

        SSIcon.loadIcon("ICON_HOME", IconState.NORMAL     , "Home 3 24 n p.png" );
        SSIcon.loadIcon("ICON_HOME", IconState.DISABLED   , "Home 3 24 d p.png" );
        SSIcon.loadIcon("ICON_HOME", IconState.HIGHLIGHTED, "Home 3 24 h p.png" );

        SSIcon.loadIcon("ICON_FIRST", IconState.NORMAL     , "First 24 n p.png" );
        SSIcon.loadIcon("ICON_FIRST", IconState.DISABLED   , "First 24 d p.png" );
        SSIcon.loadIcon("ICON_FIRST", IconState.HIGHLIGHTED, "First 24 h p.png" );

        SSIcon.loadIcon("ICON_BACK", IconState.NORMAL     , "Back 24 n p.png" );
        SSIcon.loadIcon("ICON_BACK", IconState.DISABLED   , "Back 24 d p.png" );
        SSIcon.loadIcon("ICON_BACK", IconState.HIGHLIGHTED, "Back 24 h p.png" );

        SSIcon.loadIcon("ICON_FORWARD", IconState.NORMAL     , "Forward 24 n p.png" );
        SSIcon.loadIcon("ICON_FORWARD", IconState.DISABLED   , "Forward 24 d p.png" );
        SSIcon.loadIcon("ICON_FORWARD", IconState.HIGHLIGHTED, "Forward 24 h p.png" );

        SSIcon.loadIcon("ICON_LAST", IconState.NORMAL     , "Last 24 n p.png" );
        SSIcon.loadIcon("ICON_LAST", IconState.DISABLED   , "Last 24 d p.png" );
        SSIcon.loadIcon("ICON_LAST", IconState.HIGHLIGHTED, "Last 24 h p.png" );




        SSIcon.loadIcon("ICON_ZOOMIN", IconState.NORMAL     , "Zoom In 24 n p.png" );
        SSIcon.loadIcon("ICON_ZOOMIN", IconState.DISABLED   , "Zoom In 24 d p.png" );
        SSIcon.loadIcon("ICON_ZOOMIN", IconState.HIGHLIGHTED, "Zoom In 24 h p.png" );

        SSIcon.loadIcon("ICON_ZOOMOUT", IconState.NORMAL     , "Zoom Out 24 n p.png" );
        SSIcon.loadIcon("ICON_ZOOMOUT", IconState.DISABLED   , "Zoom Out 24 d p.png" );
        SSIcon.loadIcon("ICON_ZOOMOUT", IconState.HIGHLIGHTED, "Zoom Out 24 h p.png" );



        SSIcon.loadIcon("ICON_REDO", IconState.NORMAL     , "Redo 24 n p.png" );
        SSIcon.loadIcon("ICON_REDO", IconState.DISABLED   , "Redo 24 d p.png" );
        SSIcon.loadIcon("ICON_REDO", IconState.HIGHLIGHTED, "Redo 24 h p.png" );


        SSIcon.loadIcon("ICON_CREATECHANGE", IconState.NORMAL     , "New Text Document 24 n p.png" );
        SSIcon.loadIcon("ICON_CREATECHANGE", IconState.DISABLED   , "New Text Document 24 d p.png" );
        SSIcon.loadIcon("ICON_CREATECHANGE", IconState.HIGHLIGHTED, "New Text Document 24 h p.png" );



        SSIcon.loadIcon("ICON_MARKVOUCHERROW", IconState.NORMAL     , "Eraser 16 n p.png" );
        SSIcon.loadIcon("ICON_MARKVOUCHERROW", IconState.DISABLED   , "Eraser 16 d p.png" );
        SSIcon.loadIcon("ICON_MARKVOUCHERROW", IconState.HIGHLIGHTED, "Eraser 16 h p.png" );

        SSIcon.loadIcon("ICON_DELETEVOUCHERROW", IconState.NORMAL     , "Red Delete 16 n p.png" );
        SSIcon.loadIcon("ICON_DELETEVOUCHERROW", IconState.DISABLED   , "Red Delete 16 d p.png" );
        SSIcon.loadIcon("ICON_DELETEVOUCHERROW", IconState.HIGHLIGHTED, "Red Delete 16 h p.png" );

        SSIcon.loadIcon("ICON_HELP_TOC", IconState.NORMAL     , "New Text Document 16 n p.png" );
        SSIcon.loadIcon("ICON_HELP_TOC", IconState.DISABLED   , "New Text Document 16 d p.png" );
        SSIcon.loadIcon("ICON_HELP_TOC", IconState.HIGHLIGHTED, "New Text Document 16 h p.png" );

        SSIcon.loadIcon("ICON_HELP_SEARCH", IconState.NORMAL     , "Search 16 n p.png" );
        SSIcon.loadIcon("ICON_HELP_SEARCH", IconState.DISABLED   , "Search 16 d p.png" );
        SSIcon.loadIcon("ICON_HELP_SEARCH", IconState.HIGHLIGHTED, "Search 16 h p.png" );

        SSIcon.loadIcon("ICON_SEARCH_24", IconState.NORMAL     , "Search 24 n p.png" );
        SSIcon.loadIcon("ICON_SEARCH_24", IconState.DISABLED   , "Search 24 d p.png" );
        SSIcon.loadIcon("ICON_SEARCH_24", IconState.HIGHLIGHTED, "Search 24 h p.png" );

        SSIcon.loadIcon("ICON_CLOSE_X_RED_16", IconState.NORMAL     , "Close X Red 16 n p.png" );
        SSIcon.loadIcon("ICON_CLOSE_X_RED_16", IconState.DISABLED   , "Close X Red 16 d p.png" );
        SSIcon.loadIcon("ICON_CLOSE_X_RED_16", IconState.HIGHLIGHTED, "Close X Red 16 h p.png" );


        SSIcon.loadIcon("ICON_CALENDAR16", IconState.NORMAL     , "Calendar Edit 16 n p.png" );
        SSIcon.loadIcon("ICON_CALENDAR16", IconState.DISABLED   , "Calendar Edit 16 d p.png" );
        SSIcon.loadIcon("ICON_CALENDAR16", IconState.HIGHLIGHTED, "Calendar Edit 16 h p.png" );

        SSIcon.loadIcon("ICON_DROPDOWN16", IconState.NORMAL     , "Dropdown 16 n p.png" );
        SSIcon.loadIcon("ICON_DROPDOWN16", IconState.DISABLED   , "Dropdown 16 d p.png" );
        SSIcon.loadIcon("ICON_DROPDOWN16", IconState.HIGHLIGHTED, "Dropdown 16 h p.png" );


        SSIcon.loadIcon("ICON_DIALOG_QUESTION"   , IconState.NORMAL, "Dialog Question.png" );
        SSIcon.loadIcon("ICON_DIALOG_INFORMATION", IconState.NORMAL, "Dialog Information.png" );
        SSIcon.loadIcon("ICON_DIALOG_ERROR"      , IconState.NORMAL, "Dialog Error.png" );

        SSIcon.loadIcon("ICON_CALCULATOR16", IconState.NORMAL     , "Calculator 16 n p.png" );
        SSIcon.loadIcon("ICON_CALCULATOR16", IconState.DISABLED   , "Calculator 16 d p.png" );
        SSIcon.loadIcon("ICON_CALCULATOR16", IconState.HIGHLIGHTED, "Calculator 16 h p.png" );

        SSIcon.loadIcon("ICON_EDIT16", IconState.NORMAL     , "Edit 16 n p.png" );
        SSIcon.loadIcon("ICON_EDIT16", IconState.DISABLED   , "Edit 16 d p.png" );
        SSIcon.loadIcon("ICON_EDIT16", IconState.HIGHLIGHTED, "Edit 16 h p.png" );

        SSIcon.loadIcon("ICON_DELETE16", IconState.NORMAL     , "Red Delete 16 n p.png" );
        SSIcon.loadIcon("ICON_DELETE16", IconState.DISABLED   , "Red Delete 16 d p.png" );
        SSIcon.loadIcon("ICON_DELETE16", IconState.HIGHLIGHTED, "Red Delete 16 h p.png" );

        SSIcon.loadIcon("ICON_NEW16", IconState.NORMAL     , "New Text Document 16 n p.png" );
        SSIcon.loadIcon("ICON_NEW16", IconState.DISABLED   , "New Text Document 16 d p.png" );
        SSIcon.loadIcon("ICON_NEW16", IconState.HIGHLIGHTED, "New Text Document 16 h p.png" );

        SSIcon.loadIcon("ICON_PARCEL16", IconState.NORMAL     , "Parcel 16 n p.png" );
        SSIcon.loadIcon("ICON_PARCEL16", IconState.DISABLED   , "Parcel 16 d p.png" );
        SSIcon.loadIcon("ICON_PARCEL16", IconState.HIGHLIGHTED, "Parcel 16 h p.png" );


        SSIcon.loadIcon("ICON_HELP24", IconState.NORMAL     , "Help Blue 24 n p.png" );
        SSIcon.loadIcon("ICON_HELP24", IconState.DISABLED   , "Help Blue 24 d p.png" );
        SSIcon.loadIcon("ICON_HELP24", IconState.HIGHLIGHTED, "Help Blue 24 h p.png" );

        SSIcon.loadIcon("ICON_INVOICE24", IconState.NORMAL     , "Invoice 24 n p.png" );
        SSIcon.loadIcon("ICON_INVOICE24", IconState.DISABLED   , "Invoice 24 d p.png" );
        SSIcon.loadIcon("ICON_INVOICE24", IconState.HIGHLIGHTED, "Invoice 24 h p.png" );

        SSIcon.loadIcon("ICON_COINS24", IconState.NORMAL     , "Coins 24 n p.png" );
        SSIcon.loadIcon("ICON_COINS24", IconState.DISABLED   , "Coins 24 d p.png" );
        SSIcon.loadIcon("ICON_COINS24", IconState.HIGHLIGHTED, "Coins 24 h p.png" );

        SSIcon.loadIcon("ICON_EXCLAMATION24", IconState.NORMAL     , "Exclamation Red 24 n p.png" );
        SSIcon.loadIcon("ICON_EXCLAMATION24", IconState.DISABLED   , "Exclamation Red 24 d p.png" );
        SSIcon.loadIcon("ICON_EXCLAMATION24", IconState.HIGHLIGHTED, "Exclamation Red 24 h p.png" );


        SSIcon.loadIcon("ICON_PROPERTIES16", IconState.NORMAL     , "Properties 16 n p.png" );
        SSIcon.loadIcon("ICON_PROPERTIES16", IconState.DISABLED   , "Properties 16 d p.png" );
        SSIcon.loadIcon("ICON_PROPERTIES16", IconState.HIGHLIGHTED, "Properties 16 h p.png" );


        SSIcon.loadIcon("ICON_TASKLIST16", IconState.NORMAL     , "Task List 16 n p.png" );
        SSIcon.loadIcon("ICON_TASKLIST16", IconState.DISABLED   , "Task List 16 d p.png" );
        SSIcon.loadIcon("ICON_TASKLIST16", IconState.HIGHLIGHTED, "Task List 16 h p.png" );

        SSIcon.loadIcon("ICON_FORWARDTASK16", IconState.NORMAL     , "Forward Task 16 n p.png" );
        SSIcon.loadIcon("ICON_FORWARDTASK16", IconState.DISABLED   , "Forward Task 16 d p.png" );
        SSIcon.loadIcon("ICON_FORWARDTASK16", IconState.HIGHLIGHTED, "Forward Task 16 h p.png" );

        SSIcon.loadIcon("Task List 24", IconState.NORMAL     , "Task List 24 n p.png" );
        SSIcon.loadIcon("Task List 24", IconState.DISABLED   , "Task List 24 d p.png" );
        SSIcon.loadIcon("Task List 24", IconState.HIGHLIGHTED, "Task List 24 h p.png" );

        SSIcon.loadIcon("ICON_REFRESH16", IconState.NORMAL     , "Refresh 16 n p.png" );
        SSIcon.loadIcon("ICON_REFRESH16", IconState.DISABLED   , "Refresh 16 d p.png" );
        SSIcon.loadIcon("ICON_REFRESH16", IconState.HIGHLIGHTED, "Refresh 16 h p.png" );

        SSIcon.loadIcon("ICON_BOTTOM16", IconState.NORMAL      , "Bottom 16 n p.png" );
        SSIcon.loadIcon("ICON_BOTTOM16", IconState.DISABLED    , "Bottom 16 d p.png" );
        SSIcon.loadIcon("ICON_BOTTOM16", IconState.HIGHLIGHTED , "Bottom 16 h p.png" );

        SSIcon.loadIcon("ICON_FRAME", IconState.NORMAL, "Light White Round 16 d p.png");
    }


    /**
     * Loads an icon from the disk
     *
     * @param pName     Unique name of the icon
     * @param pIconFile The name of the icon file
     */
    private static void loadIcon(String pName, String pIconFile){

        if(iIcons.containsKey(pName) ){
            System.out.println("(SSIcon): Duplicate icon: "+ pName );
            return;
        }

        ImageIcon iIcon = loadIcon(cIconDirectory + pIconFile);

        if(iIcon == null){
            System.out.println("(SSIcon): Failed to load icon: "+ cIconDirectory + pIconFile );
            return;
        }
        iIcons.put(pName, iIcon);
    }

    /**
     * Loads an icon from the disk
     *
     * @param pName     Unique name of the icon
     * @param pIconFile The name of the icon file
     */
    private static void loadIcon(String pName, IconState pState, String pIconFile){
        loadIcon(pName + "_" + pState.getName(), pIconFile);
    }

    /**
     *   Gets the icon by the specified name
     *
     * @param pName The unique name of the icon
     * @return The icon
     */
    public static ImageIcon getIcon(String pName){
        return getIcon(pName, IconState.NORMAL);
    }

    /**
     *   Gets the icon by the specified name
     *
     * @param pName The unique name of the icon
     * @return The icon
     */
    public static boolean hasIcon(String pName){
        return hasIcon(pName, IconState.NORMAL);
    }
    /**
     *
     * @param pName The unique name of the icon
     * @param pState The state of the icon
     * @return The icon
     */
    public static boolean hasIcon(String pName, IconState pState){
        String iIconName = pName + "_" + pState.getName();

        return iIcons.containsKey(iIconName);
    }

    /**
     *
     * @param pName The unique name of the icon
     * @param pState The state of the icon
     * @return The icon
     */
    public static ImageIcon getIcon(String pName, IconState pState){
        String iIconName = pName + "_" + pState.getName();

        if(!iIcons.containsKey(iIconName) ){
            System.out.println("(SSIcon): Icon not found: "+ iIconName );
        }
        return iIcons.get(iIconName);
    }





    /**
     *
     * @param pIconFile
     * @return The icon
     */
    private static ImageIcon loadIcon(String pIconFile) {
        File iImageFile = new File(pIconFile);

        if( !iImageFile.exists() ) return null;

        try {
            return new ImageIcon(ImageIO.read(iImageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
