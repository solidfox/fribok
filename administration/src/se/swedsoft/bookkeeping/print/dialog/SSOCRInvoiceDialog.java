package se.swedsoft.bookkeeping.print.dialog;

import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

/**
 * User: Andreas Lago
 * Date: 2006-maj-05
 * Time: 09:41:08
 */
public class SSOCRInvoiceDialog extends SSDialog {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;
    private JRadioButton iLanguageRadioSwedish;
    private JRadioButton iLanguageRadioEnglish;

    /**
     *
     * @param iMainFrame
     * @param iTitle
     */
    public SSOCRInvoiceDialog(SSMainFrame iMainFrame, String iTitle) {
        super(iMainFrame, iTitle );

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
    public Locale getLanguage(){
        if(iLanguageRadioSwedish.isSelected()) return new Locale("se","","");
        if(iLanguageRadioEnglish.isSelected()) return new Locale("en","","");

        return Locale.getDefault() ;
    }

    public boolean doShowBackground(){
        return false;
    }

}
