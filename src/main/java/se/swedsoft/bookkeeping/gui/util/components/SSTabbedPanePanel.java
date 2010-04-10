package se.swedsoft.bookkeeping.gui.util.components;

import javax.swing.*;
import java.awt.*;

/**
 * User: Andreas Lago
 * Date: 2006-mar-31
 * Time: 12:29:20
 */
public class SSTabbedPanePanel extends JPanel {

    /**
     * Creates a new {@code JPanel} with a double buffer
     * and a flow layout.
     */
    public SSTabbedPanePanel() {
        setLayout(new BorderLayout());

        setBorder( BorderFactory.createEmptyBorder(2,2,4,2));
    }


}
