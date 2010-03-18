package se.swedsoft.bookkeeping.gui.util.graphics;

import se.swedsoft.bookkeeping.app.SSPath;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2006-feb-08
 * Time: 10:51:02
 */
public class SSImage {

    public static final File cImageDirectory = SSPath.get(SSPath.APP_IMAGES);

    // Library of all graphics
    private static Map<String, BufferedImage> iGraphics = new HashMap<String, BufferedImage>();

    // Standard graphics
    static{
        loadImage("BACKGROUND"    , "Background.png" );
        loadImage("LOGO"          , "logo.png" );
        loadImage("OCRAVI"        , "OCRAvi.png" );
        loadImage("OCRBackground" , "OCRBackground.png" );
        loadImage("CHECK"         , "Check.png" );
        loadImage("SERVER"        , "Server.png");
        loadImage("ICON_LOGO"     , "Logo_Icon16.png");
    }

    /**
     * Loads an image from the disk
     *
     * @param pName     Unique name of the image
     * @param pImageFile The name of the image file
     */
    public static void loadImage(String pName, String pImageFile){

        if(iGraphics.containsKey(pName) ){
            System.out.println("(SSImage): Duplicate image: "+ pName );
            return;
        }

        BufferedImage iImage = loadImage(new File(cImageDirectory, pImageFile));

        if(iImage == null){
            System.out.println("(SSImage): Failed to load image: "+ cImageDirectory + pImageFile );
            return;
        }
        iGraphics.put(pName, iImage);
    }

    /**
     *   Gets the image by the specified name
     *
     * @param pName The unique name of the image
     * @return if image exists
     */
    public static boolean hasImage(String pName){
        return iGraphics.containsKey(pName);
    }

    /**
     *   Gets the image by the specified name
     *
     * @param pName The unique name of the image
     * @return The image
     */
    public static BufferedImage getImage(String pName){
        if(!iGraphics.containsKey(pName) ){
            System.out.println("(SSImage): Image not found: "+ pName );
        }
        return iGraphics.get(pName);
    }

    /**
     *
     * @param iImageFile
     * @return The Image
     */
    private static BufferedImage loadImage(File iImageFile) {
        if( !iImageFile.exists() ) return null;

        try {
            return ImageIO.read(iImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
