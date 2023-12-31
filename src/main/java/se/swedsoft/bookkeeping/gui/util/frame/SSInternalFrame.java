/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.util.frame;


import se.swedsoft.bookkeeping.gui.SSMainFrame;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;


// Trade Extensions specific imports

// Java specific imports

/**
 * This class implements a basic internal frame.
 *
 * @author Roger Björnstedt
 */
public abstract class SSInternalFrame extends JInternalFrame {
    // The main frame.
    private SSMainFrame iMainFrame;

    /**
     * Constructor.
     *
     * @param pMainFrame The main frame of the program.
     * @param pTitle     The title to set for this frame.
     * @param pWidth     The width of the frame.
     * @param pHeight    The height of the frame.
     */
    public SSInternalFrame(SSMainFrame pMainFrame, String pTitle, int pWidth, int pHeight) {
        super(pTitle, true, true, true, true);

        iMainFrame = pMainFrame;
        iMainFrame.getDesktopPane().add(this);

        setLayout(new BorderLayout());

        setSize(pWidth, pHeight);
        setInCenter(pMainFrame);

        SSFrameManager.getInstance().addFrame(this);
    }

    /**
     * Makes the component visible or invisible.
     * Overrides {@code Component.setVisible}.
     *
     * @param aFlag true to make the component visible; false to
     *              make it invisible
     */
    @Override
    public void setVisible(boolean aFlag) {
        if (!aFlag) {
            SSFrameManager.getInstance().removeFrame(this);
        }
        super.setVisible(aFlag);
    }

    /**
     *
     */
    public void deIconize() {
        try {
            setIcon(false);
        } catch (PropertyVetoException ignored) {}
        toFront();

        requestFocus();
    }

    /**
     *
     * @param iListener
     */
    public void addCloseListener(final ActionListener iListener) {
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                iListener.actionPerformed(new ActionEvent(this, e.getID(), null));

                iMainFrame = null;
                // System.gc();
            }
        });
    }

    /**
     * Returns the main frmae of the application.
     *
     * @return A SSMainFrame.
     */
    public SSMainFrame getMainFrame() {
        return iMainFrame;
    }

    /**
     * Indicates whether this frame is a company data related frame.
     *
     * @return A boolean value.
     */
    public abstract boolean isCompanyFrame();

    /**
     * Indicates whether this frame is a year data related frame.
     *
     * @return A boolean value.
     */
    public abstract boolean isYearDataFrame();

    /**
     * @param iFrame
     */
    public void setInCenter(JFrame iFrame) {
        int x = (iFrame.getWidth() - getWidth()) / 2;
        int y = (iFrame.getHeight() - getHeight()) / 2;

        setBounds(x, y, getWidth(), getHeight());
    }

    /**
     * @param iFrame
     */
    public void setInCenter(JDialog iDialog) {
        int x = (iDialog.getWidth() - getWidth()) / 2;
        int y = (iDialog.getHeight() - getHeight()) / 2;

        setBounds(x, y, getWidth(), getHeight());
    }

    /**
     * @param iMainFrame
     */
    public void setInCenter(SSMainFrame iMainFrame) {
        int x = (iMainFrame.getDesktopPane().getWidth() - getWidth()) / 2;
        int y = (iMainFrame.getDesktopPane().getHeight() - getHeight()) / 2;

        setBounds(x, y, getWidth(), getHeight());
    }

    /**
     *
     */
    public static void closeAllFrames() {
        SSFrameManager.getInstance().close();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.util.frame.SSInternalFrame");
        sb.append("{iMainFrame=").append(iMainFrame);
        sb.append('}');
        return sb.toString();
    }
}
