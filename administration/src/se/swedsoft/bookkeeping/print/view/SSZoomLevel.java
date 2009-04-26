package se.swedsoft.bookkeeping.print.view;

import java.util.List;
import java.util.LinkedList;

/**
 * Date: 2006-feb-20
 * Time: 09:43:16
 */
public enum SSZoomLevel {
    ZOOM_350 (500, "500%"),
    ZOOM_225 (400, "400%"),
    ZOOM_175 (300, "300%"),
    ZOOM_150 (200, "200%"),
    ZOOM_100 (100, "100%"),
    ZOOM_75  (75 , "75%"),
    ZOOM_50  (50 , "50%"),
    ZOOM_25  (25 , "25%"),
    ZOOM_10  (10 , "10%");


    private int iZoom;

    private String iName;

    private SSZoomLevel(int pZoom, String pName){
        iZoom = pZoom;
        iName = pName;
    }


    /**
     *
     */
    public int getZoom(){
        return iZoom;
    }



    /**
     * Returns the name of this enum constant, as contained in the
     * declaration.  This method may be overridden, though it typically
     * isn't necessary or desirable.  An enum type should override this
     * method when a more "programmer-friendly" string form exists.
     *
     * @return the name of this enum constant
     */
    public String toString() {
        return iName;

    }




}
