package se.swedsoft.bookkeeping.print.dialog;

import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * Date: 2006-jan-30
 * Time: 15:53:24
 */
public class SSResultPrinterSetupPanel {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private SSDateChooser iTo;

    private SSDateChooser iFrom;


    private JCheckBox iBudgetCheckbox;

    private JCheckBox iLastyearCheckBox;


    /**
     * Default constructor.
     */
    public SSResultPrinterSetupPanel() {


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
    public Date getTo() {
        return iTo.getDate();
    }

    /**
     *
     * @param to
     */
    public void setTo(Date to) {
        iTo.setDate(to);
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
     * @param from
     */
    public void setFrom(Date from) {
        iFrom.setDate(from);
    }

    /**
     *
     * @param pPrintBudget
     */
    public void setPrintBudget(Boolean pPrintBudget){
        iBudgetCheckbox.setSelected(pPrintBudget);
    }

    /**
     *
     * @return
     */
    public Boolean getPrintBudget(){
        return iBudgetCheckbox.isSelected();
    }

    /**
     *
     * @param pLastyear
     */
    public void setPrintLastyear(Boolean pLastyear){
        iLastyearCheckBox.setSelected(pLastyear);
    }

    /**
     *
     * @return
     */
    public Boolean getPrintLastyear(){
        return iLastyearCheckBox.isSelected();
    }

    /**
     *
     * @param pLastyearEnabled
     */
    public void setPrintLastyearEnabled(Boolean pLastyearEnabled){
        iLastyearCheckBox.setEnabled(pLastyearEnabled);
    }

    /**
     *
     * @param iListener
     */
    public void addCancelActionListener(ActionListener iListener) {
        iButtonPanel.addCancelActionListener(iListener);
    }

    /**
     *
     * @param iListener
     */
    public void addOkActionListener(ActionListener iListener) {
        iButtonPanel.addOkActionListener(iListener);
    }
}
