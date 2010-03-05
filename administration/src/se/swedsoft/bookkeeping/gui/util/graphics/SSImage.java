package se.swedsoft.bookkeeping.gui.util.graphics;

import se.swedsoft.bookkeeping.data.util.SSFileSystem;

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

    // Standard location for graphics
    public static final String cImageDirectory = SSFileSystem.getImageDirectory();

    // Library of all graphics
    private static Map<String, BufferedImage> iGraphics = new HashMap<String, BufferedImage>();

    // Standard graphics
    static{
        SSImage.loadImage("BACKGROUND" , "Background.gif" );
        SSImage.loadImage("LOGO"       , "logo.png" );
        SSImage.loadImage("OCRAVI"     , "OCRAvi.png" );
        SSImage.loadImage("OCRBackground" , "OCRBackground.png" );
        SSImage.loadImage("Check.png"     , "Check.png" );
        SSImage.loadImage("SERVER"  , "Server.png");
        SSImage.loadImage("ICON_LOGO","Logo_Icon16.png");
    }


    /**
     * Loads an icon from the disk
     *
     * @param pName     Unique name of the image
     * @param pImageFile The name of the image file
     */
    public static void loadImage(String pName, String pImageFile){

        if(iGraphics.containsKey(pName) ){
            System.out.println("(SSImage): Duplicate image: "+ pName );
            return;
        }

        BufferedImage iImage = loadImage(cImageDirectory + pImageFile);

        if(iImage == null){
            System.out.println("(SSImage): Failed to load image: "+ cImageDirectory + pImageFile );
            return;
        }
        iGraphics.put(pName, iImage);
    }

    /**
     *   Gets the icon by the specified name
     *
     * @param pName The unique name of the image
     * @return if image exists
     */
    public static boolean hasImage(String pName){
        return iGraphics.containsKey(pName);
    }

    /**
     *   Gets the icon by the specified name
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
     * @param pImageFile
     * @return The Image
     */
    private static BufferedImage loadImage(String pImageFile) {
        File iImageFile = new File(pImageFile);

        if( !iImageFile.exists() ) return null;

        try {
            return ImageIO.read(iImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
