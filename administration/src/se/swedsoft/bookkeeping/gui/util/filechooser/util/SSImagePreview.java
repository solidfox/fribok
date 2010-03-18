package se.swedsoft.bookkeeping.gui.util.filechooser.util;


import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

/**
 * Date: 2006-feb-10
 * Time: 15:15:47
 */
public class SSImagePreview extends JComponent implements PropertyChangeListener {

    private ImageIcon iThumbnail;
    private File      iFile;

    /**
     *
     * @param pFileChooser
     */
    public SSImagePreview(JFileChooser pFileChooser) {
        setPreferredSize(new Dimension(100, 50));

        pFileChooser.addPropertyChangeListener(this);
    }

    /**
     *
     */
    public void loadImage() {
        if (iFile == null) {
            iThumbnail = null;
            return;
        }

        //Don't use createImageIcon (which is a wrapper for getResource)
        //because the image we're trying to load is probably not one
        //of this program's own resources.
        ImageIcon tmpIcon = new ImageIcon(iFile.getPath());
        if (tmpIcon != null) {
            if (tmpIcon.getIconWidth() > 90) {
                iThumbnail = new ImageIcon(tmpIcon.getImage().
                        getScaledInstance(90, -1,
                        Image.SCALE_DEFAULT));
            } else { //no need to miniaturize
                iThumbnail = tmpIcon;
            }
        }
    }

    /**
     *
     * @param e
     */
    public void propertyChange(PropertyChangeEvent e) {
        boolean update = false;
        String prop = e.getPropertyName();

        //If the directory changed, don't show an image.
        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
            iFile = null;
            update = true;

            //If a iFile became selected, find out which one.
        } else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
            iFile = (File) e.getNewValue();
            update = true;
        }

        //Update the preview accordingly.
        if (update) {
            iThumbnail = null;
            if (isShowing()) {
                loadImage();
                repaint();
            }
        }
    }

    /**
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (iThumbnail == null) {
            loadImage();
        }
        if (iThumbnail != null) {
            int x = getWidth()/2 - iThumbnail.getIconWidth()/2;
            int y = 0;// getHeight()/2 - iThumbnail.getIconHeight()/2;

            if (y < 0) {
                y = 0;
            }

            if (x < 5) {
                x = 5;
            }
            iThumbnail.paintIcon(this, g, x, y);
        }
    }
}



