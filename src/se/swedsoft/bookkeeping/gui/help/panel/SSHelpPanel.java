package se.swedsoft.bookkeeping.gui.help.panel;

import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;

import javax.help.*;
import javax.swing.*;
import java.awt.*;

/**
 * Date: 2006-mar-06
 * Time: 16:21:43
 */
public class SSHelpPanel {

    private HelpSet iHelpSet;

    private JPanel iPanel;

    private JTabbedPane tabbedPane1;



    private JPanel iPane11;
    private JPanel iPane12;
    private JPanel iPanel3;


    public SSHelpPanel(HelpSet pHelpSet, JHelpContentViewer iViewer, TextHelpModel pModel){
        iHelpSet = pHelpSet;

        TOCView    iTOCView    = (TOCView   )iHelpSet.getNavigatorView("Table of Contents");
        SearchView iSearchView = (SearchView)iHelpSet.getNavigatorView("Search");
             //  iTOCView.get

        JHelpSearchNavigator iSearchNavigator = (JHelpSearchNavigator) iSearchView.createNavigator(pModel);
        JHelpNavigator       iHelpNavigator   = (JHelpNavigator      ) iTOCView   .createNavigator(pModel);
              

        tabbedPane1.setTitleAt(0, iTOCView   .getLabel());
        tabbedPane1.setTitleAt(1, iSearchView.getLabel());

        tabbedPane1.setIconAt(0, SSIcon.getIcon("ICON_HELP_TOC"   ,  SSIcon.IconState.NORMAL));
        tabbedPane1.setIconAt(1, SSIcon.getIcon("ICON_HELP_SEARCH",  SSIcon.IconState.NORMAL));

        iPane11.setLayout(new BorderLayout());
        iPane11.add(iSearchNavigator, BorderLayout.CENTER);

        iPane12.setLayout(new BorderLayout());
        iPane12.add(iHelpNavigator, BorderLayout.CENTER);

        iPanel3.setLayout(new BorderLayout());
        iPanel3.add(iViewer, BorderLayout.CENTER);
    }


    public JPanel getPanel() {
        return iPanel;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.help.panel.SSHelpPanel");
        sb.append("{iHelpSet=").append(iHelpSet);
        sb.append(", iPane11=").append(iPane11);
        sb.append(", iPane12=").append(iPane12);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iPanel3=").append(iPanel3);
        sb.append(", tabbedPane1=").append(tabbedPane1);
        sb.append('}');
        return sb.toString();
    }
}
