package se.swedsoft.bookkeeping.print.dialog;

import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.SSMainFrame;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Date;

/**
 * User: Andreas Lago
 * Date: 2006-apr-24
 * Time: 16:16:46
 */
public class SSStockValueDialog extends SSDialog {

    private JPanel iPanel;

    private SSDateChooser iDate;

    private SSButtonPanel iButtonPanel;
    private JRadioButton iRadioAll;
    private JRadioButton iRadioDate;


    /**
     * @param iFrame
     */
    public SSStockValueDialog(SSMainFrame iFrame) {
        super(iFrame, SSBundle.getBundle().getString("stockvaluereport.dialog.title"));

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


        ChangeListener iChangeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iDate.setEnabled( iRadioDate.isSelected() );
            }
        };
        iRadioAll  .addChangeListener(iChangeListener);
        iRadioDate.addChangeListener(iChangeListener);

        ButtonGroup iGroup = new ButtonGroup();
        iGroup.add(iRadioAll);
        iGroup.add(iRadioDate);

        iChangeListener.stateChanged(null);
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return iDate.getDate();
    }

    /**
     * 
     * @return
     */
    public boolean isDateSelected() {
        return iRadioDate.isSelected();
    }


}
