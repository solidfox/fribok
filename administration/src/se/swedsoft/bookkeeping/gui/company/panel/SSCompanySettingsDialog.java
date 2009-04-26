package se.swedsoft.bookkeeping.gui.company.panel;

import se.swedsoft.bookkeeping.gui.company.pages.*;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSInputVerifier;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.data.SSNewCompany;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.List;
import java.util.LinkedList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * User: Andreas Lago
 * Date: 2006-aug-25
 * Time: 10:19:35
 */
public class SSCompanySettingsDialog extends SSDialog implements ListSelectionListener {


    private List<SSCompanyPage> iPages;

    private SSNewCompany iCompany;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JPanel iContainer;

    private JList iNavigator;


  


    /**
     *
     */
    public SSCompanySettingsDialog(SSMainFrame iMainFrame, String iDialogTitle) {
        super(iMainFrame, iDialogTitle);

        iPages = new LinkedList<SSCompanyPage>();
        iPages.add( new SSCompanyPageGeneral(this) );
        iPages.add( new SSCompanyPageAddress(this) );
        iPages.add( new SSCompanyPageAdditional(this) );
        iPages.add( new SSCompanyPageStandardText(this) );
        iPages.add( new SSCompanyPageDefaultAccount(this) );
        iPages.add( new SSCompanyPageTax(this) );
        iPages.add( new SSCompanyPageAutoIncrement(this) );

        setPanel(iPanel);

        iButtonPanel.addOkActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog( JOptionPane.OK_OPTION );
            }
        });

        iButtonPanel.addCancelActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog( JOptionPane.CANCEL_OPTION );
            }
        });

        iContainer.setLayout(new BorderLayout() );

        iNavigator.setModel(new DefaultComboBoxModel(iPages.toArray()) );
        iNavigator.addListSelectionListener(this);
        iNavigator.setFixedCellHeight(24);

        iNavigator.setCellRenderer(new NavigatorCellRenderer());
        iNavigator.setSelectedIndex(0);


    }


    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    public void valueChanged(ListSelectionEvent e) {
        int iSelected = iNavigator.getSelectedIndex();

        SSCompanyPage iPage = iPages.get(iSelected);

        iPage.setDialog(this);

        iContainer.removeAll();
        iContainer.add( iPage.getPanel() , BorderLayout.CENTER );
        //iContainer.setPreferredSize( iPage.getPanel().getPreferredSize() );
        iContainer.validate();
     //   iContainer.resize();

        repaint();
    }



    /**
     * Set the company to edit
     *
     * @param iCompany
     */
    public void setCompany(SSNewCompany iCompany) {
        this.iCompany = iCompany;

        for (SSCompanyPage iPage : iPages) {
            iPage.setCompany(iCompany);
        }
    }

    /**
     * Get the edited company
     *
     * @return the company
     */
    public SSNewCompany getCompany() {
        for (SSCompanyPage iPage : iPages) {
            iPage.getCompany();
        }
        return iCompany;
    }


    /**
     *
     */
    private class NavigatorCellRenderer extends JLabel implements ListCellRenderer {

        public Component getListCellRendererComponent(JList list, Object value,  int index,  boolean isSelected, boolean cellHasFocus) {
            String s = Integer.toString(index + 1) + ". " + ((SSCompanyPage)value).getName();

            setText(s);

            setIcon( cellHasFocus ? SSIcon.getIcon("ICON_TASKLIST16", SSIcon.IconState.HIGHLIGHTED) : SSIcon.getIcon("ICON_TASKLIST16", SSIcon.IconState.NORMAL) );

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
            else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            setBorder( new EmptyBorder(0, 3, 0, 0));
            return this;
        }
    }

}
