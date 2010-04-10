package se.swedsoft.bookkeeping.gui.util.frame;


import se.swedsoft.bookkeeping.data.util.SSConfig;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


/**
 * User: Andreas Lago
 * Date: 2006-apr-03
 * Time: 10:51:34
 */
public class SSFrameManager {

    // The offset between frames.
    private static final int FRAME_OFFSET = 20;
    // The maximum number of frames before we shall reset the counter
    private static final int FRAME_RESET = 20;

    // The instance of the frame manager
    private static SSFrameManager cInstance;

    /**
     * Return the frame manager for the application
     *
     * @return the frame manager
     */
    public static SSFrameManager getInstance() {
        if (cInstance == null) {
            cInstance = new SSFrameManager();
        }
        return cInstance;
    }

    // ///////////////////////////////////////////////////

    // All frames
    private Set<SSInternalFrame> iFrames;

    private List<ActionListener> iFrameListeners;

    /**
     *
     */
    private SSFrameManager() {
        iFrames = new HashSet<SSInternalFrame>();
        iFrameListeners = new LinkedList<ActionListener>();
    }

    /**
     *
     * @return all frames
     */
    public Set<SSInternalFrame> getFrames() {
        return Collections.unmodifiableSet(iFrames);
    }

    /**
     * Add a frame to the frame manager
     *
     * @param iFrame
     */
    public synchronized void addFrame(SSInternalFrame iFrame) {

        iFrames.add(iFrame);

        iFrame.addInternalFrameListener(new FrameListener(iFrame));

        notifyFrameListeners();
    }

    /**
     * Remove a frame from the frame manager
     *
     * @param iFrame
     */
    public synchronized void removeFrame(SSInternalFrame iFrame) {
        iFrames.remove(iFrame);

        notifyFrameListeners();
    }

    /**
     *
     * @param iActionListener
     */
    public void addFrameListener(ActionListener iActionListener) {
        iFrameListeners.add(iActionListener);
    }

    /**
     *
     */
    private void notifyFrameListeners() {
        ActionEvent iAction = new ActionEvent(this, 0, "");

        for (ActionListener iActionListener: iFrameListeners) {
            iActionListener.actionPerformed(iAction);
        }
    }

    // ////////////////////////////////////////////

    /**
     * Cascades all  frames
     */
    public synchronized void cascade() {
        List<SSInternalFrame> iFramesToCascade = new LinkedList<SSInternalFrame>(iFrames);

        Collections.sort(iFramesToCascade, new Comparator<SSInternalFrame>() {
            public int compare(SSInternalFrame o1, SSInternalFrame o2) {
                String iTitle1 = o1.getTitle();
                String iTitle2 = o2.getTitle();

                if (iTitle1 == null || iTitle2 == null) {
                    return 0;
                }

                return iTitle1.compareTo(iTitle2);
            }
        });

        int iFrameCounter = 1;

        for (SSInternalFrame iFrame : iFramesToCascade) {
            int x = iFrameCounter * FRAME_OFFSET;
            int y = iFrameCounter * FRAME_OFFSET;

            if (iFrameCounter == FRAME_RESET) {
                iFrameCounter = 0;
            }

            iFrameCounter++;

            iFrame.setLocation(x, y);
            iFrame.deIconize();

            storeFrame(iFrame);
        }
    }

    /**
     * Close all frames
     */
    public synchronized void close() {
        Set<SSInternalFrame> iFramesToClose = new HashSet<SSInternalFrame>(iFrames);

        iFrames.clear();

        for (SSInternalFrame iFrame : iFramesToClose) {
            storeFrame(iFrame);

            iFrame.setVisible(false);
            iFrame.dispose();
        }
        notifyFrameListeners();
    }

    /**
     *
     * @param iFrame
     */
    public void storeFrame(SSInternalFrame iFrame) {
        String    iName = iFrame.getClass().getName();
        Point     iLocation = iFrame.getLocation();
        Dimension iSize = iFrame.getSize();

        if (iLocation != null) {
            SSConfig.getInstance().set(iName + ".location", iLocation);
        }
        if (iSize != null) {
            SSConfig.getInstance().set(iName + ".size", iSize);
        }
    }

    /**
     *
     * @param iFrame
     */
    public void restoreFrame(SSInternalFrame iFrame) {
        String    iName = iFrame.getClass().getName();
        Point     iLocation = (Point) SSConfig.getInstance().get(iName + ".location");
        Dimension iSize = (Dimension) SSConfig.getInstance().get(iName + ".size");

        if (iSize != null) {
            if (iSize.width < 80) {
                iSize.width = 80;
            }
            if (iSize.height < 60) {
                iSize.height = 60;
            }

            iFrame.setSize(iSize);
        }
        if (iLocation != null) {
            if (iLocation.x < 0) {
                iLocation.x = FRAME_OFFSET * (iFrames.size() % FRAME_RESET);
            }
            if (iLocation.y < 0) {
                iLocation.y = FRAME_OFFSET * (iFrames.size() % FRAME_RESET);
            }

            iFrame.setLocation(iLocation);
        }
    }

    /**
     *
     */
    public void storeAllFrames() {
        Set<SSInternalFrame> iFramesToStore = new HashSet<SSInternalFrame>(iFrames);

        for (SSInternalFrame iFrame : iFramesToStore) {
            storeFrame(iFrame);
        }
    }

    /**
     *
     */
    private class FrameListener extends InternalFrameAdapter {
        // The frame
        private SSInternalFrame iFrame;

        /**
         *
         * @param iFrame
         */
        public FrameListener(SSInternalFrame iFrame) {
            this.iFrame = iFrame;
        }

        /**
         * Invoked when an internal frame has been opened.
         */
        @Override
        public void internalFrameOpened(InternalFrameEvent e) {
            restoreFrame(iFrame);
        }

        /**
         * Invoked when an internal frame is in the process of being closed.
         * The close operation can be overridden at this point.
         */
        @Override
        public void internalFrameClosing(InternalFrameEvent e) {
            storeFrame(iFrame);
        }

        /**
         * Invoked when an internal frame has been closed.
         */
        @Override
        public void internalFrameClosed(InternalFrameEvent e) {
            removeFrame(iFrame);

        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();

            sb.append(
                    "se.swedsoft.bookkeeping.gui.util.frame.SSFrameManager.FrameListener");
            sb.append("{iFrame=").append(iFrame);
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.util.frame.SSFrameManager");
        sb.append("{iFrameListeners=").append(iFrameListeners);
        sb.append(", iFrames=").append(iFrames);
        sb.append('}');
        return sb.toString();
    }
}
