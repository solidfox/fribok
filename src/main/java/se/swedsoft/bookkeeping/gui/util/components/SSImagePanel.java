package se.swedsoft.bookkeeping.gui.util.components;


import se.swedsoft.bookkeeping.gui.util.graphics.SSImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * User: Andreas Lago
 * Date: 2006-mar-14
 * Time: 15:16:54
 */
public class SSImagePanel extends JPanel {

    private String iImageName;

    private Paint iPaint;

    private Integer iImageWidth;
    private Integer iImageHeight;

    /**
     *
     * @return
     */
    public String getImageName() {
        return iImageName;
    }

    /**
     *
     * @param pImageName
     */
    public void setImageName(String pImageName) {
        iImageName = pImageName;

        if (SSImage.hasImage(iImageName)) {
            BufferedImage iImage = SSImage.getImage(iImageName);

            iImageWidth = iImage.getWidth();
            iImageHeight = iImage.getHeight();

            iPaint = new TexturePaint(iImage,
                    new Rectangle(0, 0, iImageWidth, iImageHeight));

            setPreferredSize(new Dimension(iImageWidth, iImageHeight));

        } else {
            iPaint = null;
        }

    }

    /**
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g) {

        if (iPaint != null) {
            Graphics2D g2 = (Graphics2D) g;

            g2.setPaint(iPaint);

            int w = getWidth();
            int h = getHeight();
            int x = (w - iImageWidth) / 2;
            int y = (h - iImageHeight) / 2;

            g2.fillRect(x, y, w, h);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.util.components.SSImagePanel");
        sb.append("{iImageHeight=").append(iImageHeight);
        sb.append(", iImageName='").append(iImageName).append('\'');
        sb.append(", iImageWidth=").append(iImageWidth);
        sb.append(", iPaint=").append(iPaint);
        sb.append('}');
        return sb.toString();
    }
}
