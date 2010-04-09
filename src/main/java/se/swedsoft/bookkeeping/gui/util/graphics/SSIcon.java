package se.swedsoft.bookkeeping.gui.util.graphics;

import se.swedsoft.bookkeeping.app.Path;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;

/**
 * Date: 2006-feb-02
 * Time: 10:11:34
 */
public class SSIcon {

    // Ensure non-instantiability
    private SSIcon() { throw new AssertionError("Don't instantiate this class"); }

    public enum IconState {
        NORMAL     ("NORMAL"     ),
        DISABLED   ("DISABLED"   ),
        HIGHLIGHTED("HIGHLIGHTED");

        private final String name;

        IconState(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }
    }

    // Library of all graphics
    private static Map<String, ImageIcon> icons = new HashMap<String, ImageIcon>();

    // Standard graphics
    static{
        // Export, import, print
        loadIcon("ICON_EXPORT", IconState.NORMAL     , "Export_from_Database_24_NORMAL.png" );
        loadIcon("ICON_EXPORT", IconState.DISABLED   , "Export_from_Database_24_DISABLED.png" );
        loadIcon("ICON_EXPORT", IconState.HIGHLIGHTED, "Export_from_Database_24_HIGHLIGHTED.png" );

        loadIcon("ICON_IMPORT", IconState.NORMAL     , "Import_to_Database_24_NORMAL.png" );
        loadIcon("ICON_IMPORT", IconState.DISABLED   , "Import_to_Database_24_DISABLED.png" );
        loadIcon("ICON_IMPORT", IconState.HIGHLIGHTED, "Import_to_Database_24_HIGHLIGHTED.png" );

        loadIcon("ICON_PRINT", IconState.NORMAL     , "Printer_24_NORMAL.png" );
        loadIcon("ICON_PRINT", IconState.DISABLED   , "Printer_24_DISABLED.png" );
        loadIcon("ICON_PRINT", IconState.HIGHLIGHTED, "Printer_24_HIGHLIGHTED.png" );

        // File actions
        loadIcon("ICON_NEWITEM", IconState.NORMAL     , "New_Blank_Document_24_NORMAL.png" );
        loadIcon("ICON_NEWITEM", IconState.DISABLED   , "New_Blank_Document_24_DISABLED.png" );
        loadIcon("ICON_NEWITEM", IconState.HIGHLIGHTED, "New_Blank_Document_24_HIGHLIGHTED.png" );

        loadIcon("ICON_OPENITEM", IconState.NORMAL     , "Open_File_or_Folder_24_NORMAL.png" );
        loadIcon("ICON_OPENITEM", IconState.DISABLED   , "Open_File_or_Folder_24_DISABLED.png" );
        loadIcon("ICON_OPENITEM", IconState.HIGHLIGHTED, "Open_File_or_Folder_24_HIGHLIGHTED.png" );

        loadIcon("ICON_SAVEITEM", IconState.NORMAL     , "Save_Blue_24_NORMAL.png" );
        loadIcon("ICON_SAVEITEM", IconState.DISABLED   , "Save_Blue_24_DISABLED.png" );
        loadIcon("ICON_SAVEITEM", IconState.HIGHLIGHTED, "Save_Blue_24_HIGHLIGHTED.png" );

        loadIcon("ICON_COPYITEM", IconState.NORMAL     , "Copy_24_NORMAL.png" );
        loadIcon("ICON_COPYITEM", IconState.DISABLED   , "Copy_24_DISABLED.png" );
        loadIcon("ICON_COPYITEM", IconState.HIGHLIGHTED, "Copy_24_HIGHLIGHTED.png" );

        // Delete
        loadIcon("ICON_CANCELITEM", IconState.NORMAL     , "Red_Delete_24_NORMAL.png" );
        loadIcon("ICON_CANCELITEM", IconState.DISABLED   , "Red_Delete_24_DISABLED.png" );
        loadIcon("ICON_CANCELITEM", IconState.HIGHLIGHTED, "Red_Delete_24_HIGHLIGHTED.png" );

        // Navigation
        loadIcon("ICON_EDITITEM", IconState.NORMAL     , "Edit_24_NORMAL.png" );
        loadIcon("ICON_EDITITEM", IconState.DISABLED   , "Edit_24_DISABLED.png" );
        loadIcon("ICON_EDITITEM", IconState.HIGHLIGHTED, "Edit_24_HIGHLIGHTED.png" );

        loadIcon("ICON_DELETEITEM", IconState.NORMAL     , "Delete_Document_3_24_NORMAL.png" );
        loadIcon("ICON_DELETEITEM", IconState.DISABLED   , "Delete_Document_3_24_DISABLED.png" );
        loadIcon("ICON_DELETEITEM", IconState.HIGHLIGHTED, "Delete_Document_3_24_HIGHLIGHTED.png" );

        loadIcon("ICON_HOME", IconState.NORMAL     , "Home_3_24_NORMAL.png" );
        loadIcon("ICON_HOME", IconState.DISABLED   , "Home_3_24_DISABLED.png" );
        loadIcon("ICON_HOME", IconState.HIGHLIGHTED, "Home_3_24_HIGHLIGHTED.png" );

        loadIcon("ICON_FIRST", IconState.NORMAL     , "First_24_NORMAL.png" );
        loadIcon("ICON_FIRST", IconState.DISABLED   , "First_24_DISABLED.png" );
        loadIcon("ICON_FIRST", IconState.HIGHLIGHTED, "First_24_HIGHLIGHTED.png" );

        loadIcon("ICON_BACK", IconState.NORMAL     , "Back_24_NORMAL.png" );
        loadIcon("ICON_BACK", IconState.DISABLED   , "Back_24_DISABLED.png" );
        loadIcon("ICON_BACK", IconState.HIGHLIGHTED, "Back_24_HIGHLIGHTED.png" );

        loadIcon("ICON_FORWARD", IconState.NORMAL     , "Forward_24_NORMAL.png" );
        loadIcon("ICON_FORWARD", IconState.DISABLED   , "Forward_24_DISABLED.png" );
        loadIcon("ICON_FORWARD", IconState.HIGHLIGHTED, "Forward_24_HIGHLIGHTED.png" );

        loadIcon("ICON_LAST", IconState.NORMAL     , "Last_24_NORMAL.png" );
        loadIcon("ICON_LAST", IconState.DISABLED   , "Last_24_DISABLED.png" );
        loadIcon("ICON_LAST", IconState.HIGHLIGHTED, "Last_24_HIGHLIGHTED.png" );

        // Zoom in/out
        loadIcon("ICON_ZOOMIN", IconState.NORMAL     , "Zoom_In_24_NORMAL.png" );
        loadIcon("ICON_ZOOMIN", IconState.DISABLED   , "Zoom_In_24_DISABLED.png" );
        loadIcon("ICON_ZOOMIN", IconState.HIGHLIGHTED, "Zoom_In_24_HIGHLIGHTED.png" );

        loadIcon("ICON_ZOOMOUT", IconState.NORMAL     , "Zoom_Out_24_NORMAL.png" );
        loadIcon("ICON_ZOOMOUT", IconState.DISABLED   , "Zoom_Out_24_DISABLED.png" );
        loadIcon("ICON_ZOOMOUT", IconState.HIGHLIGHTED, "Zoom_Out_24_HIGHLIGHTED.png" );

        // Redo
        loadIcon("ICON_REDO", IconState.NORMAL     , "Redo_24_NORMAL.png" );
        loadIcon("ICON_REDO", IconState.DISABLED   , "Redo_24_DISABLED.png" );
        loadIcon("ICON_REDO", IconState.HIGHLIGHTED, "Redo_24_HIGHLIGHTED.png" );

        // New document
        loadIcon("ICON_CREATECHANGE", IconState.NORMAL     , "New_Text_Document_24_NORMAL.png" );
        loadIcon("ICON_CREATECHANGE", IconState.DISABLED   , "New_Text_Document_24_DISABLED.png" );
        loadIcon("ICON_CREATECHANGE", IconState.HIGHLIGHTED, "New_Text_Document_24_HIGHLIGHTED.png" );

        loadIcon("ICON_MARKVOUCHERROW", IconState.NORMAL     , "Eraser_16_NORMAL.png" );
        loadIcon("ICON_MARKVOUCHERROW", IconState.DISABLED   , "Eraser_16_DISABLED.png" );
        loadIcon("ICON_MARKVOUCHERROW", IconState.HIGHLIGHTED, "Eraser_16_HIGHLIGHTED.png" );

        loadIcon("ICON_DELETEVOUCHERROW", IconState.NORMAL     , "Red_Delete_16_NORMAL.png" );
        loadIcon("ICON_DELETEVOUCHERROW", IconState.DISABLED   , "Red_Delete_16_DISABLED.png" );
        loadIcon("ICON_DELETEVOUCHERROW", IconState.HIGHLIGHTED, "Red_Delete_16_HIGHLIGHTED.png" );

        loadIcon("ICON_HELP_TOC", IconState.NORMAL     , "New_Text_Document_16_NORMAL.png" );
        loadIcon("ICON_HELP_TOC", IconState.DISABLED   , "New_Text_Document_16_DISABLED.png" );
        loadIcon("ICON_HELP_TOC", IconState.HIGHLIGHTED, "New_Text_Document_16_HIGHLIGHTED.png" );

        loadIcon("ICON_HELP_SEARCH", IconState.NORMAL     , "Search_16_NORMAL.png" );
        loadIcon("ICON_HELP_SEARCH", IconState.DISABLED   , "Search_16_DISABLED.png" );
        loadIcon("ICON_HELP_SEARCH", IconState.HIGHLIGHTED, "Search_16_HIGHLIGHTED.png" );

        loadIcon("ICON_SEARCH_24", IconState.NORMAL     , "Search_24_NORMAL.png" );
        loadIcon("ICON_SEARCH_24", IconState.DISABLED   , "Search_24_DISABLED.png" );
        loadIcon("ICON_SEARCH_24", IconState.HIGHLIGHTED, "Search_24_HIGHLIGHTED.png" );

        loadIcon("ICON_CLOSE_X_RED_16", IconState.NORMAL     , "Close_X_Red_16_NORMAL.png" );
        loadIcon("ICON_CLOSE_X_RED_16", IconState.DISABLED   , "Close_X_Red_16_DISABLED.png" );
        loadIcon("ICON_CLOSE_X_RED_16", IconState.HIGHLIGHTED, "Close_X_Red_16_HIGHLIGHTED.png" );

        loadIcon("ICON_CALENDAR16", IconState.NORMAL     , "Calendar_Edit_16_NORMAL.png" );
        loadIcon("ICON_CALENDAR16", IconState.DISABLED   , "Calendar_Edit_16_DISABLED.png" );
        loadIcon("ICON_CALENDAR16", IconState.HIGHLIGHTED, "Calendar_Edit_16_HIGHLIGHTED.png" );

        loadIcon("ICON_DROPDOWN16", IconState.NORMAL     , "Dropdown_16_NORMAL.png" );
        loadIcon("ICON_DROPDOWN16", IconState.DISABLED   , "Dropdown_16_DISABLED.png" );
        loadIcon("ICON_DROPDOWN16", IconState.HIGHLIGHTED, "Dropdown_16_HIGHLIGHTED.png" );

        loadIcon("ICON_DIALOG_QUESTION"   , IconState.NORMAL, "Dialog_Question.png" );
        loadIcon("ICON_DIALOG_INFORMATION", IconState.NORMAL, "Dialog_Information.png" );
        loadIcon("ICON_DIALOG_ERROR"      , IconState.NORMAL, "Dialog_Error.png" );

        loadIcon("ICON_CALCULATOR16", IconState.NORMAL     , "Calculator_16_NORMAL.png" );
        loadIcon("ICON_CALCULATOR16", IconState.DISABLED   , "Calculator_16_DISABLED.png" );
        loadIcon("ICON_CALCULATOR16", IconState.HIGHLIGHTED, "Calculator_16_HIGHLIGHTED.png" );

        loadIcon("ICON_EDIT16", IconState.NORMAL     , "Edit_16_NORMAL.png" );
        loadIcon("ICON_EDIT16", IconState.DISABLED   , "Edit_16_DISABLED.png" );
        loadIcon("ICON_EDIT16", IconState.HIGHLIGHTED, "Edit_16_HIGHLIGHTED.png" );

        loadIcon("ICON_DELETE16", IconState.NORMAL     , "Red_Delete_16_NORMAL.png" );
        loadIcon("ICON_DELETE16", IconState.DISABLED   , "Red_Delete_16_DISABLED.png" );
        loadIcon("ICON_DELETE16", IconState.HIGHLIGHTED, "Red_Delete_16_HIGHLIGHTED.png" );

        loadIcon("ICON_NEW16", IconState.NORMAL     , "New_Text_Document_16_NORMAL.png" );
        loadIcon("ICON_NEW16", IconState.DISABLED   , "New_Text_Document_16_DISABLED.png" );
        loadIcon("ICON_NEW16", IconState.HIGHLIGHTED, "New_Text_Document_16_HIGHLIGHTED.png" );

        loadIcon("ICON_PARCEL16", IconState.NORMAL     , "Parcel_16_NORMAL.png" );
        loadIcon("ICON_PARCEL16", IconState.DISABLED   , "Parcel_16_DISABLED.png" );
        loadIcon("ICON_PARCEL16", IconState.HIGHLIGHTED, "Parcel_16_HIGHLIGHTED.png" );

        loadIcon("ICON_HELP24", IconState.NORMAL     , "Help_Blue_24_NORMAL.png" );
        loadIcon("ICON_HELP24", IconState.DISABLED   , "Help_Blue_24_DISABLED.png" );
        loadIcon("ICON_HELP24", IconState.HIGHLIGHTED, "Help_Blue_24_HIGHLIGHTED.png" );

        loadIcon("ICON_INVOICE24", IconState.NORMAL     , "Invoice_24_NORMAL.png" );
        loadIcon("ICON_INVOICE24", IconState.DISABLED   , "Invoice_24_DISABLED.png" );
        loadIcon("ICON_INVOICE24", IconState.HIGHLIGHTED, "Invoice_24_HIGHLIGHTED.png" );

        loadIcon("ICON_COINS24", IconState.NORMAL     , "Coins_24_NORMAL.png" );
        loadIcon("ICON_COINS24", IconState.DISABLED   , "Coins_24_DISABLED.png" );
        loadIcon("ICON_COINS24", IconState.HIGHLIGHTED, "Coins_24_HIGHLIGHTED.png" );

        loadIcon("ICON_EXCLAMATION24", IconState.NORMAL     , "Exclamation_Red_24_NORMAL.png" );
        loadIcon("ICON_EXCLAMATION24", IconState.DISABLED   , "Exclamation_Red_24_DISABLED.png" );
        loadIcon("ICON_EXCLAMATION24", IconState.HIGHLIGHTED, "Exclamation_Red_24_HIGHLIGHTED.png" );

        loadIcon("ICON_PROPERTIES16", IconState.NORMAL     , "Properties_16_NORMAL.png" );
        loadIcon("ICON_PROPERTIES16", IconState.DISABLED   , "Properties_16_DISABLED.png" );
        loadIcon("ICON_PROPERTIES16", IconState.HIGHLIGHTED, "Properties_16_HIGHLIGHTED.png" );

        loadIcon("ICON_TASKLIST16", IconState.NORMAL     , "Task_List_16_NORMAL.png" );
        loadIcon("ICON_TASKLIST16", IconState.DISABLED   , "Task_List_16_DISABLED.png" );
        loadIcon("ICON_TASKLIST16", IconState.HIGHLIGHTED, "Task_List_16_HIGHLIGHTED.png" );

        loadIcon("ICON_FORWARDTASK16", IconState.NORMAL     , "Forward_Task_16_NORMAL.png" );
        loadIcon("ICON_FORWARDTASK16", IconState.DISABLED   , "Forward_Task_16_DISABLED.png" );
        loadIcon("ICON_FORWARDTASK16", IconState.HIGHLIGHTED, "Forward_Task_16_HIGHLIGHTED.png" );

        loadIcon("ICON_TASKLIST24", IconState.NORMAL     , "Task_List_24_NORMAL.png" );
        loadIcon("ICON_TASKLIST24", IconState.DISABLED   , "Task_List_24_DISABLED.png" );
        loadIcon("ICON_TASKLIST24", IconState.HIGHLIGHTED, "Task_List_24_HIGHLIGHTED.png" );

        loadIcon("ICON_REFRESH16", IconState.NORMAL     , "Refresh_16_NORMAL.png" );
        loadIcon("ICON_REFRESH16", IconState.DISABLED   , "Refresh_16_DISABLED.png" );
        loadIcon("ICON_REFRESH16", IconState.HIGHLIGHTED, "Refresh_16_HIGHLIGHTED.png" );

        loadIcon("ICON_BOTTOM16", IconState.NORMAL      , "Bottom_16_NORMAL.png" );
        loadIcon("ICON_BOTTOM16", IconState.DISABLED    , "Bottom_16_DISABLED.png" );
        loadIcon("ICON_BOTTOM16", IconState.HIGHLIGHTED , "Bottom_16_HIGHLIGHTED.png" );

        loadIcon("ICON_FRAME", IconState.NORMAL, "Light_White_Round_16_DISABLED.png");
    }

    /**
     * Loads an icon from the disk
     *
     * @param name     Unique name of the icon
     * @param filename The name of the icon file
     */
    private static void loadIcon(String name, String filename) {
        if (icons.containsKey(name)) {
            System.out.println("(SSIcon): Already loaded icon: " + name);
            return;
        }

        URL url = Path.class.getResource("/graphics/icons/" + filename);

        ImageIcon icon = null;
        try {
            icon = new ImageIcon(ImageIO.read(url));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (icon != null) {
            icons.put(name, icon);
        } else {
            System.out.println("(SSIcon): Failed to load icon: " + filename);
        }
    }

    /**
     * Loads an icon from the disk
     *
     * @param name     Unique name of the icon
     * @param pState
     * @param filename The name of the icon file
     */
    private static void loadIcon(String name, IconState pState, String filename){
        loadIcon(name + '_' + pState.getName(), filename);
    }

    /**
     *   Gets the icon by the specified name
     *
     * @param name The unique name of the icon
     * @return The icon
     */
    public static ImageIcon getIcon(String name){
        return getIcon(name, IconState.NORMAL);
    }

    /**
     *   Gets the icon by the specified name
     *
     * @param name The unique name of the icon
     * @return The icon
     */
    public static boolean hasIcon(String name){
        return hasIcon(name, IconState.NORMAL);
    }

    /**
     *
     * @param name The unique name of the icon
     * @param pState The state of the icon
     * @return The icon
     */
    public static boolean hasIcon(String name, IconState pState){
        String iIconName = name + '_' + pState.getName();

        return icons.containsKey(iIconName);
    }

    /**
     *
     * @param name The unique name of the icon
     * @param pState The state of the icon
     * @return The icon
     */
    public static ImageIcon getIcon(String name, IconState pState){
        String iIconName = name + '_' + pState.getName();

        if(!icons.containsKey(iIconName) ){
            System.out.println("(SSIcon): Icon not found: "+ iIconName );
        }
        return icons.get(iIconName);
    }

}
