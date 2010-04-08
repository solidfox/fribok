package se.swedsoft.bookkeeping.gui.help;

import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.help.panel.SSHelpPanel;
import se.swedsoft.bookkeeping.gui.help.util.SSHelpHistory;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;

import javax.help.HelpSet;
import javax.help.InvalidHelpSetContextException;
import javax.help.JHelpContentViewer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * Date: 2006-mar-06
 * Time: 09:43:04
 */
public class SSHelpFrame extends JFrame {

    private static SSHelpFrame cInstance;

    /**
     *
     * @return
     */
    public static SSHelpFrame getInstance(){
        return cInstance;
    }

    /**
     *
     * @param iMainFrame
     * @param pWidth
     * @param pHeight
     */
    public static void showFrame(SSMainFrame iMainFrame, int pWidth, int pHeight){
        if(cInstance == null){
            cInstance = new SSHelpFrame(iMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
    }

    /**
     *
     * @param iMainFrame
     * @param pWidth
     * @param pHeight
     * @param iHelpClass
     */
    public static void showFrame(SSMainFrame iMainFrame, int pWidth, int pHeight, Class iHelpClass) {
        if(cInstance == null){
            cInstance = new SSHelpFrame(iMainFrame, pWidth, pHeight);
        }
        cInstance.setVisible(true);
        cInstance.setHelpClass(iHelpClass);
    }


    //////////////////////////////////////////////////////////////////////////////////////////

    private HelpSet iHelpSet;

    private JHelpContentViewer iViewer;

    private SSHelpHistory iHistory;

    private  SSButton iBackButton;

    private  SSButton iForwardButton;


    private SSHelpFrame(SSMainFrame iMainFrame, int pWidth, int pHeight){
        super(SSBundle.getBundle().getString("helpframe.title"));

        createHelpSet();

        setLayout(new BorderLayout());
        add(getMainContent(), BorderLayout.CENTER);
        add(getToolBar()    , BorderLayout.NORTH);

        setSize    (pWidth, pHeight);
        setLocationRelativeTo(iMainFrame);
    }




    /**
     * Gets the helpset for the application
     *
     */
    private void createHelpSet(){
        if(iHelpSet == null){
            ClassLoader iClassLoader = SSHelpFrame.class.getClassLoader();

            try {
                URL iHelpSetUrl = HelpSet.findHelpSet(iClassLoader, "jfs.hs");

                iHelpSet = new HelpSet(null, iHelpSetUrl);
            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }

        setTitle( iHelpSet.getTitle() );

        iViewer = new JHelpContentViewer(iHelpSet);

        iHistory = new SSHelpHistory(iViewer);


    }




    /**
     * This method should return a toolbar if the sub-class wants one. <P>
     * Otherwise, it may return null.
     *
     * @return A JToolBar or null.
     */
    public JToolBar getToolBar() {
        JToolBar toolBar = new JToolBar();

        // Back
        // ***************************
        SSButton iButton = new SSButton("ICON_HOME", "helpframe.homebutton", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    iViewer.setCurrentID(iHelpSet.getHomeID());
                } catch (InvalidHelpSetContextException ex) {
                    ex.printStackTrace();
                }
            }
        });
        toolBar.add(iButton);
        toolBar.addSeparator();

        // Back
        // ***************************
        iBackButton = new SSButton("ICON_BACK", "helpframe.prevbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                iHistory.back();
            }
        });
        toolBar.add(iBackButton);


        // Forward
        // ***************************
        iForwardButton = new SSButton("ICON_FORWARD", "helpframe.nextbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                iHistory.forward();
            }
        });
        toolBar.add(iForwardButton);
        toolBar.addSeparator();
        /*
 // Print
 // ***************************
 iButton = new SSButton("ICON_PRINT", "helpframe.printbutton", new PrintAction(iViewer) );
 toolBar.add(iButton);
        */


        iHistory.addHistoryListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iBackButton   .setEnabled( iHistory.hasPrevious() );
                iForwardButton.setEnabled( iHistory.hasNext() );
            }
        });
        iBackButton   .setEnabled( iHistory.hasPrevious() );
        iForwardButton.setEnabled( iHistory.hasNext() );

        toolBar.setFloatable(false);
        return toolBar;
    }



    /**
     * This method should return the main content for the frame. <P>
     * Such as an object table.
     *
     * @return The main content for this frame.
     */
    public JComponent getMainContent() {

        try {
            iViewer.setCurrentID(  iHelpSet.getHomeID() );
        } catch (InvalidHelpSetContextException e) {
            e.printStackTrace();
        }


        SSHelpPanel iHelpPanel = new SSHelpPanel(iHelpSet, iViewer, iViewer.getModel());

        JPanel iPanel = new JPanel();


        iPanel.setLayout(new BorderLayout());
        iPanel.add(iHelpPanel.getPanel(), BorderLayout.CENTER);

        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,2,2));

        return iPanel;
    }



    /**
     *
     * @param pHelpClass
     */
    private void setHelpClass(Class pHelpClass) {
        // @todo Restructure help file to be able to connect a frame to a help topic.
        System.out.println(pHelpClass);
        //  iViewer.setCurrentID();
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.help.SSHelpFrame");
        sb.append("{iBackButton=").append(iBackButton);
        sb.append(", iForwardButton=").append(iForwardButton);
        sb.append(", iHelpSet=").append(iHelpSet);
        sb.append(", iHistory=").append(iHistory);
        sb.append(", iViewer=").append(iViewer);
        sb.append('}');
        return sb.toString();
    }
}
