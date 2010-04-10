package se.swedsoft.bookkeeping.gui.util.graphics;


import se.swedsoft.bookkeeping.app.Path;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.net.URL;


/**
 * Date: 2006-feb-08
 * Time: 10:51:02
 */
public class SSImage {

    // Library of all graphics
    private static Map<String, BufferedImage> graphics = new HashMap<String, BufferedImage>();

    // Ensure non-instantiability
    private SSImage() {
        throw new AssertionError("Don't instantiate this class");
    }

    // Standard graphics
    static {
        loadImage("BACKGROUND", "Background.png");
        loadImage("LOGO", "logo.png");
        loadImage("OCRAVI", "OCRAvi.png");
        loadImage("OCRBackground", "OCRBackground.png");
        loadImage("CHECK", "Check.png");
        loadImage("SERVER", "Server.png");
        loadImage("ICON_LOGO", "Logo_Icon16.png");
    }

    /**
     * Load an image from disk
     * @param name     unique name of the image
     * @param filename the name of the image file
     */
    private static void loadImage(String name, String filename) {
        if (graphics.containsKey(name)) {
            System.out.println("(SSImage): Already loaded image: " + name);
            return;
        }

        URL url = Path.class.getResource("/graphics/" + filename);

        BufferedImage image = null;

        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (image != null) {
            graphics.put(name, image);
        } else {
            System.out.println("(SSImage): Failed to load image: " + filename);
        }
    }

    /**
     * Check if an image with the specified name exists
     * @param name the unique name of the image
     * @return true if image exists
     */
    public static boolean hasImage(String name) {
        return graphics.containsKey(name);
    }

    /**
     * Gets the image by the specified name
     * @param name the unique name of the image
     * @return the image
     */
    public static BufferedImage getImage(String name) {
        if (!graphics.containsKey(name)) {
            System.out.println("(SSImage): Image not found: " + name);
        }
        return graphics.get(name);
    }
}
