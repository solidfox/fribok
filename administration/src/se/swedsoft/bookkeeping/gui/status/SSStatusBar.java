package se.swedsoft.bookkeeping.gui.status;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import java.util.List;
import java.util.LinkedList;
import java.awt.*;

/**
 * User: Andreas Lago
 * Date: 2006-mar-22
 * Time: 09:18:27
 */
public class SSStatusBar extends JPanel {

    public static class SSStatusBarPanel{
        int        fill;
        JComponent component;
        double     weightx;
    }

    private List<SSStatusBarPanel> iPanels;


    //private static int width

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public SSStatusBar() {
        iPanels = new LinkedList<SSStatusBarPanel>();

        Border iBorder = new StatusBorder();

        setMinimumSize  (new Dimension(-1, 24));
        setMaximumSize  (new Dimension(-1, 24));
        setPreferredSize(new Dimension(-1, 24));


        setBorder(iBorder);
        setLayout(null);
        setBackground( new Color(236, 233, 216));
    }

    /**
     *
     * @param iComponent
     */
    public void addPanel( JComponent iComponent ){
        SSStatusBarPanel iPanel = new SSStatusBarPanel();

        iPanel.component   = iComponent;
        iPanel.fill        = GridBagConstraints.NONE;
        iPanel.weightx     = 0;

        iPanels.add(iPanel);

        layoutPanels();
    }

    /**
     *
     */
    public void addSeperator(){
        SSStatusBarPanel iPanel = new SSStatusBarPanel();

        iPanel.component  = new SeperatorPanel();
        iPanel.fill       = GridBagConstraints.NONE;
        iPanel.weightx    = 0;

        iPanels.add(iPanel);

        layoutPanels();
    }

    /**
     *
     * @param
     */
    public void addSpacer() {
        SSStatusBarPanel iPanel = new SSStatusBarPanel();

        JPanel iSpacer = new JPanel();
        iSpacer.setBackground( new Color(236, 233, 216));

        iPanel.component  = iSpacer;
        iPanel.fill       = GridBagConstraints.BOTH;
        iPanel.weightx     = 1;

        iPanels.add(iPanel);

        layoutPanels();
    }


    /**
     *
     */
    private void layoutPanels(){
        int x = 0;
        int w = getWidth();

        removeAll();
        GridBagLayout      iLayout = new GridBagLayout();
        GridBagConstraints iConstraints = new GridBagConstraints();

        setLayout(iLayout);
        for(SSStatusBarPanel iPanel: iPanels){
            iConstraints.fill    = iPanel.fill;
            iConstraints.weightx = iPanel.weightx;

            iLayout.setConstraints(iPanel.component, iConstraints);

            add(iPanel.component);
        }
    }


    /**
     * Invoked by Swing to draw components.
     *
     * @param g the <code>Graphics</code> context in which to paint
     */
    public void paint(Graphics g) {
        super.paint(g);

        int w = getWidth () - 12;
        int h = getHeight() - 14;

        for(int x = 0; x < 12; x = x + 4){

            for(int y = 0; y < 12; y = y + 4){

                if(x + y > 4){
                    g.setColor(new Color(255, 255, 255));
                    g.fillRect(w+x+1, h+y+1, 2, 2);

                    g.setColor(new Color(184, 180, 168));
                    g.fillRect(w+x  , h+y  , 2, 2);
                }
            }
        }
    }


    /**
     *
     */
    private static class SeperatorPanel extends JPanel{
        private Color cColor1 = new Color(175, 173, 160);
        private Color cColor2 = new Color(243, 241, 231);

        /**
         * Creates a new <code>JPanel</code> with a double buffer
         * and a flow layout.
         */
        public SeperatorPanel() {
            setPreferredSize(new Dimension(8, 18));
            setMinimumSize  (new Dimension(8, 18));
        }

        /**
         * Invoked by Swing to draw components.
         *
         * @param g the <code>Graphics</code> context in which to paint
         */
        public void paint(Graphics g) {
            g.setColor(cColor1);
            g.drawLine(4, 0 , 4, getHeight() );

            g.setColor(cColor2);
            g.drawLine(5, 0 , 5, getHeight() );
        }
    }


    /**
     * The top and bottom border of the status bar
     */
    private static class StatusBorder extends AbstractBorder {

        private Color cOuterColor = new Color(165, 163, 151);
        private Color cInnerColor = new Color(200, 198, 183);

        /**
         * This default implementation does no painting.
         *
         * @param c      the component for which this border is being painted
         * @param g      the paint graphics
         * @param x      the x position of the painted border
         * @param y      the y position of the painted border
         * @param width  the width of the painted border
         * @param height the height of the painted border
         */
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Color oldColor = g.getColor();

            g.setColor(cOuterColor);
            g.drawLine(x, y       , width, y     );
            g.drawLine(x, height-1, width, height-1);

            g.setColor(cInnerColor);
            g.drawLine(x, y     +1, width, y     +1);
            g.drawLine(x, height-2, width, height-2);

            g.setColor(oldColor);
        }

        /**
         * This default implementation returns a new <code>Insets</code>
         * instance where the <code>top</code>, <code>left</code>,
         * <code>bottom</code>, and
         * <code>right</code> fields are set to <code>0</code>.
         *
         * @param c the component for which this border insets value applies
         * @return the new <code>Insets</code> object initialized to 0
         */
        public Insets getBorderInsets(Component c) {
            return new Insets(2,4,2,12);
        }

        /**
         * Reinitializes the insets parameter with this Border's current Insets.
         *
         * @param c      the component for which this border insets value applies
         * @param insets the object to be reinitialized
         * @return the <code>insets</code> object
         */
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left   = 4;
            insets.top    = 2;
            insets.right  = 12;
            insets.bottom = 2;

            return insets;
        }

    }
}
