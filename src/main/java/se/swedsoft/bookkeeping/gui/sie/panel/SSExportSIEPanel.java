package se.swedsoft.bookkeeping.gui.sie.panel;


import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.importexport.sie.util.SIEType;

import javax.swing.*;
import java.awt.event.ActionListener;


/**
 * Date: 2006-feb-22
 * Time: 13:11:54
 */
public class SSExportSIEPanel {

    private JPanel iPanel;

    protected JPanel iSetupPanel;

    private SSButtonPanel iButtonPanel;

    protected JTextField iComment;

    protected JLabel iCommentLabel;

    private JRadioButton iSIERadio1;
    private JRadioButton iSIERadio2;
    private JRadioButton iSIERadio3;
    private JRadioButton iSIERadio4;

    public SSExportSIEPanel() {
        ButtonGroup iButtonGroup = new ButtonGroup();

        iButtonGroup.add(iSIERadio1);
        iButtonGroup.add(iSIERadio2);
        iButtonGroup.add(iSIERadio3);
        iButtonGroup.add(iSIERadio4);
    }

    /**
     *
     * @return
     */
    public String getComment() {
        String text = iComment.getText();

        if (text == null) {
            return null;
        }

        return text.length() > 0 ? text : null;
    }

    /**
     *
     * @return
     */
    public SIEType getType() {
        if (iSIERadio1.isSelected()) {
            return SIEType.SIE_1;
        }
        if (iSIERadio2.isSelected()) {
            return SIEType.SIE_2;
        }
        if (iSIERadio3.isSelected()) {
            return SIEType.SIE_3;
        }
        if (iSIERadio4.isSelected()) {
            return SIEType.SIE_4E;
        }

        return SIEType.SIE_NULL;
    }

    /**
     *
     * @param e
     */
    public void addOkAction(ActionListener e) {
        iButtonPanel.addOkActionListener(e);
    }

    /**
     *
     * @param e
     */
    public void addCancelAction(ActionListener e) {
        iButtonPanel.addCancelActionListener(e);
    }

    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.sie.panel.SSExportSIEPanel");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iComment=").append(iComment);
        sb.append(", iCommentLabel=").append(iCommentLabel);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iSetupPanel=").append(iSetupPanel);
        sb.append(", iSIERadio1=").append(iSIERadio1);
        sb.append(", iSIERadio2=").append(iSIERadio2);
        sb.append(", iSIERadio3=").append(iSIERadio3);
        sb.append(", iSIERadio4=").append(iSIERadio4);
        sb.append('}');
        return sb.toString();
    }
}
