package se.swedsoft.bookkeeping.print.dialog;


import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.project.util.SSProjectTableModel;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;


/**
 * $Id$
 *
 */
public class SSProjectResultSetupDialog extends SSDialog {

    private SSButtonPanel iButtonPanel;

    private JRadioButton iRadioAll;

    private JRadioButton iRadioSingle;

    private SSDateChooser iFrom;

    private SSDateChooser iTo;

    private SSTableComboBox<SSNewProject> iProjects;

    private JPanel iPanel;

    /**
     *
     * @param iFrame
     * @param title
     */
    public SSProjectResultSetupDialog(JFrame iFrame, String title) {
        super(iFrame, title);

        setPanel(iPanel);

        iRadioSingle.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iProjects.setEnabled(iRadioSingle.isSelected());
            }
        });

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

        iGroup.add(iRadioAll);
        iGroup.add(iRadioSingle);

        iProjects.setModel(SSProjectTableModel.getDropDownModel());
        iProjects.setSelected(iProjects.getFirst());

        Date iFrom = new Date();
        Date iTo = new Date();

        List<SSNewAccountingYear> iYears = SSDB.getInstance().getYears();

        for (SSNewAccountingYear iYear : iYears) {
            if (iFrom.after(iYear.getFrom())) {
                iFrom = iYear.getFrom();
            }
            if (iTo.before(iYear.getTo())) {
                iTo = iYear.getTo();
            }
        }
        this.iFrom.setDate(iFrom);
        this.iTo.setDate(iTo);
    }

    /**
     *
     * @param pDate
     */
    public void setFrom(Date pDate) {
        iFrom.setDate(pDate);
    }

    /**
     *
     * @param pDate
     */
    public void setTo(Date pDate) {
        iTo.setDate(pDate);
    }

    /**
     *
     * @return
     */
    public Date getFrom() {
        return iFrom.getDate();
    }

    /**
     *
     * @return
     */
    public Date getTo() {
        return iTo.getDate();
    }

    /**
     *
     * @return
     */
    public SSNewProject getProject() {
        if (iRadioSingle.isSelected()) {
            return iProjects.getSelected();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.dialog.SSProjectResultSetupDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iFrom=").append(iFrom);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iProjects=").append(iProjects);
        sb.append(", iRadioAll=").append(iRadioAll);
        sb.append(", iRadioSingle=").append(iRadioSingle);
        sb.append(", iTo=").append(iTo);
        sb.append('}');
        return sb.toString();
    }
}
