package se.swedsoft.bookkeeping.print.dialog;

import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import static se.swedsoft.bookkeeping.print.report.SSSaleReportPrinter.SortingMode;

/**
 * User: Andreas Lago
 * Date: 2006-apr-19
 * Time: 14:28:34
 */
public class SSSaleReportDialog extends SSDialog {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;


    private SSDateChooser iToDate;
    private SSDateChooser iFromDate;

    private JRadioButton iSortAscending;
    private JRadioButton iSortDescending;

    private JComboBox iSort;


    /**
     *
     * @param iMainFrame
     */
    public SSSaleReportDialog(SSMainFrame iMainFrame) {
        super(iMainFrame, SSBundle.getBundle().getString("salereport.dialog.title") );

        SSNewAccountingYear iCurrentYear = SSDB.getInstance().getCurrentYear();
        if(iCurrentYear != null){
            iFromDate.setDate( iCurrentYear.getFrom() );
            iToDate  .setDate( iCurrentYear.getTo() );
        }

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

        iSort.setModel(new DefaultComboBoxModel( SortingMode.values() ));
        iSort.setSelectedItem( SortingMode.Product);

        ButtonGroup iGroup = new ButtonGroup();

        iGroup.add(iSortAscending);
        iGroup.add(iSortDescending);

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
    public SortingMode getSortingMode(){
        return (SortingMode)iSort.getSelectedItem();
    }

    /**
     *
     * @return
     */
    public boolean getAscending(){
        return iSortAscending.isSelected();
    }

    /**
     *
     * @return
     */
    public Date getFrom(){
        return iFromDate.getDate();
    }

    /**
     *
     * @return
     */
    public Date getTo(){
        return iToDate.getDate();
    }


}
