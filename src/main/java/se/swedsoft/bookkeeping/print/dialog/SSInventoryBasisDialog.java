package se.swedsoft.bookkeeping.print.dialog;


import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;


/**
 * $Id$
 *
 */
public class SSInventoryBasisDialog extends SSDialog {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JRadioButton iRadioAll;
    private JRadioButton iRadioDate;

    private SSDateChooser iDate;

    /**
     *
     * @param iMainFrame
     */
    public SSInventoryBasisDialog(SSMainFrame iMainFrame) {
        super(iMainFrame,
                SSBundle.getBundle().getString("inventorybasisreport.dialog.title"));

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

        ChangeListener iChangeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {

                iDate.setEnabled(iRadioDate.isSelected());
            }
        };

        iRadioDate.addChangeListener(iChangeListener);
        iRadioAll.addChangeListener(iChangeListener);

        ButtonGroup iGroup = new ButtonGroup();

        iGroup.add(iRadioAll);
        iGroup.add(iRadioDate);

        iChangeListener.stateChanged(null);
    }

    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }

    /**
     *
     * @return
     */
    public boolean isDateSelected() {
        return iRadioDate.isSelected();
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return iDate.getDate();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.dialog.SSInventoryBasisDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iDate=").append(iDate);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iRadioAll=").append(iRadioAll);
        sb.append(", iRadioDate=").append(iRadioDate);
        sb.append('}');
        return sb.toString();
    }
}
