package se.swedsoft.bookkeeping.print.dialog;

import se.swedsoft.bookkeeping.data.SSNewResultUnit;
import se.swedsoft.bookkeeping.gui.resultunit.util.SSResultUnitTableModel;
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

/**
 * Date: 2006-feb-08
 * Time: 12:01:51
 */
public class SSResultUnitResultSetupDialog extends SSDialog {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JRadioButton iRadioAll;

    private JRadioButton iRadioSingle;

    private SSDateChooser iFrom;

    private SSDateChooser iTo;


    private SSTableComboBox<SSNewResultUnit> iResultUnit;

    /**
     *
     * @param iFrame
     * @param title
     */
    public SSResultUnitResultSetupDialog(JFrame iFrame, String title) {
        super(iFrame, title);

        setPanel(iPanel);

        iRadioSingle.addChangeListener( new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                iResultUnit.setEnabled( iRadioSingle.isSelected() );
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

        ButtonGroup iGroup = new ButtonGroup();


        iGroup.add(iRadioAll   );
        iGroup.add(iRadioSingle);

        iResultUnit.setModel(SSResultUnitTableModel.getDropDownModel());
        iResultUnit.setSelected(iResultUnit.getFirst());

    }



    /**
     *
     * @param pDate
     */
    public void setFrom(Date pDate){
        iFrom.setDate(pDate);
    }

    /**
     *
     * @param pDate
     */
    public void setTo(Date pDate){
        iTo.setDate(pDate);
    }

    /**
     *
     * @return
     */
    public Date getFrom(){
        return iFrom.getDate();
    }

    /**
     *
     * @return
     */
    public Date getTo(){
        return iTo.getDate();
    }

    /**
     *
     * @return
     */
    public SSNewResultUnit getSelectedResultUnit(){
        if( iRadioSingle.isSelected() ){
            return iResultUnit.getSelected();
        } else {
            return null;
        }
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.print.dialog.SSResultUnitResultSetupDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iFrom=").append(iFrom);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iRadioAll=").append(iRadioAll);
        sb.append(", iRadioSingle=").append(iRadioSingle);
        sb.append(", iResultUnit=").append(iResultUnit);
        sb.append(", iTo=").append(iTo);
        sb.append('}');
        return sb.toString();
    }
}
