package se.swedsoft.bookkeeping.print.view;


import javax.swing.*;
import java.awt.*;


/**
 * Date: 2006-feb-16
 * Time: 14:25:25
 */
public class SSDocumentPanel {

    private JPanel iPanel;

    private JPanel iPage;

    private ImagePanel iImagePanel;

    private Image iImage;

    /**
     *
     */
    public SSDocumentPanel() {
        iPanel.setBackground(Color.gray);

        iImagePanel = new ImagePanel();
        iImagePanel.setOpaque(true);
        iImagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        iPage.setLayout(new BorderLayout());
        iPage.add(iImagePanel, BorderLayout.CENTER);
    }

    /**
     *
     * @param iImage
     * @param iSize
     */
    public void setDocument(Image iImage, Dimension iSize) {
        this.iImage = iImage;

        Dimension iPanelSize = new Dimension(iSize.width + 4, iSize.height + 4);

        iPage.setMaximumSize(iSize);
        iPage.setMinimumSize(iSize);
        iPage.setPreferredSize(iSize);

        iPanel.setMaximumSize(iPanelSize);
        iPanel.setMinimumSize(iPanelSize);
        iPanel.setPreferredSize(iPanelSize);

        iImagePanel.revalidate();
        iImagePanel.repaint();

    }

    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }

    private class ImagePanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(iImage, 1, 1, this);
        }

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.view.SSDocumentPanel");
        sb.append("{iImage=").append(iImage);
        sb.append(", iImagePanel=").append(iImagePanel);
        sb.append(", iPage=").append(iPage);
        sb.append(", iPanel=").append(iPanel);
        sb.append('}');
        return sb.toString();
    }
}

