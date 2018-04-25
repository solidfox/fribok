package se.swedsoft.bookkeeping.print.dialog;


import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;


/**
 * $Id$
 *
 */
public class SSLanguageDialog extends SSDialog {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;
    private JRadioButton iLanguageRadioSwedish;
    private JRadioButton iLanguageRadioEnglish;

    /**
     *
     * @param iMainFrame
     * @param iTitle
     */
    public SSLanguageDialog(SSMainFrame iMainFrame, String iTitle) {
        super(iMainFrame, iTitle);

        setPanel(iPanel);

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.CANCEL_OPTION, true);
            }
        });
        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.OK_OPTION, true);
            }
        });

	getRootPane().setDefaultButton(iButtonPanel.getOkButton());

        ButtonGroup iGroup = new ButtonGroup();

        iGroup.add(iLanguageRadioSwedish);
        iGroup.add(iLanguageRadioEnglish);

    }

    /**
     *
     * @param iOwner
     * @param iTitle
     */
    public SSLanguageDialog(SSDialog iOwner, String iTitle) {
        super(iOwner, iTitle);

        setPanel(iPanel);

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.CANCEL_OPTION, true);
            }
        });
        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.OK_OPTION, true);
            }
        });

        ButtonGroup iGroup = new ButtonGroup();

        iGroup.add(iLanguageRadioSwedish);
        iGroup.add(iLanguageRadioEnglish);
    }

    /**
     *
     * @return
     */
    public Locale getLanguage() {
        if (iLanguageRadioSwedish.isSelected()) {
            return new Locale("sv", "", "");
        }
        if (iLanguageRadioEnglish.isSelected()) {
            return new Locale("en", "", "");
        }

        return Locale.getDefault();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.dialog.SSLanguageDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iLanguageRadioEnglish=").append(iLanguageRadioEnglish);
        sb.append(", iLanguageRadioSwedish=").append(iLanguageRadioSwedish);
        sb.append(", iPanel=").append(iPanel);
        sb.append('}');
        return sb.toString();
    }
}
