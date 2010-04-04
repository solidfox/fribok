package se.swedsoft.bookkeeping.print.view;

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

    SSZoomLevel(int pZoom, String pName){
        iZoom = pZoom;
        iName = pName;
    }

    /**
     *
     * @return
     */
    public int getZoom(){
        return iZoom;
    }

    public String toString() {
        return iName;

    }
}
