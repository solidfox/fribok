package se.swedsoft.bookkeeping.gui.accountplans.dialog;


import se.swedsoft.bookkeeping.data.SSAccountPlan;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.accountplans.util.SSAccountPlanTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Date: 2006-feb-15
 * Time: 09:27:34
 */
public class SSImportAccountplanDialog extends SSDialog {

    private SSTableComboBox<SSAccountPlan> iAccountPlan;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JCheckBox iCheckSRU;
    private JCheckBox iCheckREC;
    private JCheckBox iCheckVAT;

    /**
     *
     * @param iMainFrame
     */
    public SSImportAccountplanDialog(SSMainFrame iMainFrame) {
        super(iMainFrame,
                SSBundle.getBundle().getString("accountplanframe.importfield.title"));

        setLayout(new BorderLayout());
        add(iPanel, BorderLayout.CENTER);

        pack();

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.CANCEL_OPTION);
                setVisible(false);
            }
        });

        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.OK_OPTION);
                setVisible(false);
            }
        });
        iAccountPlan.setModel(SSAccountPlanTableModel.getDropDownModel());
        iAccountPlan.setSelected(iAccountPlan.getFirst());
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
     * @param l
     */
    public void addOkActionListener(ActionListener l) {
        iButtonPanel.addOkActionListener(l);
    }

    /**
     *
     * @param l
     */
    public void addCancelActionListener(ActionListener l) {
        iButtonPanel.addCancelActionListener(l);
    }

    /**
     *
     * @return
     */
    public SSAccountPlan geAccountPlan() {
        return iAccountPlan.getSelected();
    }

    /**
     *
     * @return
     */
    public boolean isSRUSelected() {
        return iCheckSRU.isSelected();
    }

    /**
     *
     * @return
     */
    public boolean isRECSelected() {
        return iCheckREC.isSelected();
    }

    /**
     *
     * @return
     */
    public boolean isVATSelected() {
        return iCheckVAT.isSelected();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(
                "se.swedsoft.bookkeeping.gui.accountplans.dialog.SSImportAccountplanDialog");
        sb.append("{iAccountPlan=").append(iAccountPlan);
        sb.append(", iButtonPanel=").append(iButtonPanel);
        sb.append(", iCheckREC=").append(iCheckREC);
        sb.append(", iCheckSRU=").append(iCheckSRU);
        sb.append(", iCheckVAT=").append(iCheckVAT);
        sb.append(", iPanel=").append(iPanel);
        sb.append('}');
        return sb.toString();
    }
}
