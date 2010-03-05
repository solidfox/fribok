package se.swedsoft.bookkeeping.print.dialog;

import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.voucher.util.SSVoucherTableModel;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.calc.util.SSFilterFactory;
import se.swedsoft.bookkeeping.calc.util.SSFilter;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.util.List;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Date: 2006-feb-17
 * Time: 15:54:16
 */
public class SSVoucherListDialog extends SSDialog {

    public enum SSVoucherRange{
        ALL_VOUCHERS,
        BETWEEN_NUMBER,
        BETWEEN_DATE
    }

    private JPanel iPanel;

    private SSDateChooser iFromDate;

    private SSDateChooser iToDate;

    private SSTableComboBox<SSVoucher> iToVoucher;

    private SSTableComboBox<SSVoucher> iFromVoucher;

    private JRadioButton iRadioNumber;

    private JRadioButton iRadioDate;

    private JRadioButton iRadioAll;

    private SSButtonPanel iButtonPanel;

    /**
     *
     */
    public SSVoucherListDialog(SSMainFrame iMainFrame){
        super(iMainFrame, SSBundle.getBundle().getString("voucherlistreport.dialog.title"));

        List<SSVoucher> iVouchers = SSDB.getInstance().getCurrentYear().getVouchers();

        setPanel(iPanel);

        ButtonGroup iButtonGroup = new ButtonGroup();
        iButtonGroup.add(iRadioNumber);
        iButtonGroup.add(iRadioDate);
        iButtonGroup.add(iRadioAll);


        ChangeListener iChangeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iFromVoucher.setEnabled( iRadioNumber.isSelected() );
                iToVoucher  .setEnabled( iRadioNumber.isSelected() );

                iFromDate.setEnabled( iRadioDate.isSelected() );
                iToDate  .setEnabled( iRadioDate.isSelected() );
            }
        };

        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(JOptionPane.OK_OPTION);
            }
        });

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(JOptionPane.CANCEL_OPTION);
            }
        });

        iRadioAll   .addChangeListener(iChangeListener);
        iRadioDate  .addChangeListener(iChangeListener);
        iRadioNumber.addChangeListener(iChangeListener);

        iFromVoucher.setModel( SSVoucherTableModel.getDropDownModel() );
        iToVoucher  .setModel( SSVoucherTableModel.getDropDownModel() );

        iFromVoucher.setSelected( SSVoucherMath.getFirst(iVouchers) );
        iToVoucher  .setSelected( SSVoucherMath.getLast(iVouchers) );

        iFromVoucher.setSearchColumns(0);
        iToVoucher.setSearchColumns(0);

        iRadioAll.setSelected(true);
    }


    /**
     *
     * @return
     */
    public JPanel getPanel(){
        return iPanel;
    }


    /**
     *
     * @return
     */
    public SSVoucherRange getVoucherRange(){
        if( iRadioNumber.isSelected() ) return SSVoucherRange.BETWEEN_NUMBER;
        if( iRadioDate  .isSelected() ) return SSVoucherRange.BETWEEN_DATE;

        return SSVoucherRange.ALL_VOUCHERS;
    }

    /**
     *
     * @param pDateFrom
     */
    public void setDateFrom(Date pDateFrom){
        iFromDate.setDate(pDateFrom);
    }

    /**
     *
     * @param pDateTo
     */
    public void setDateTo(Date pDateTo){
        iToDate.setDate(pDateTo);
    }



    /**
     *
     * @return
     */
    public Integer getNumberFrom(){
        SSVoucher iSelected = iFromVoucher.getSelected();

        return iSelected == null ? 0 : iSelected.getNumber();
    }

    /**
     *
     * @return
     */
    public Integer getNumberTo(){
        SSVoucher iSelected = iToVoucher.getSelected();

        return iSelected == null ? 0 : iSelected.getNumber();
    }

    /**
     *
     * @return
     */
    public Date getDateFrom(){
        return iFromDate.getDate();
    }

    /**
     *
     * @return
     */
    public Date getDateTo(){
        return iToDate.getDate();
    }


    /**
     *
     * @return
     */
    public boolean isNumberSelected() {
        return iRadioNumber.isSelected();
    }

    /**
     *
     * @return
     */
    public boolean isDateSelected() {
        return iRadioDate.isSelected();
    }

    /**
     * Returns the invoices to print depending on the user selections
     *
     * @return
     */
    public List<SSVoucher> getElementsToPrint(){

        List<SSVoucher> iVouchers = SSDB.getInstance().getVouchers();

        // Filter by non payed invoices
        if( iRadioNumber.isSelected() ){
            final Integer iNumberFrom = this.iFromVoucher.getSelected().getNumber();
            final Integer iNumberTo   = this.iToVoucher.getSelected().getNumber();

            iVouchers = SSFilterFactory.doFilter(iVouchers, new SSFilter<SSVoucher>() {
                public boolean applyFilter(SSVoucher iObject) {
                    Integer iNumber = iObject.getNumber();

                    return iNumber >= iNumberFrom && iNumber <= iNumberTo;
                }
            });

        }
        // Filter by date
        if(iRadioDate.isSelected() ){
            final Date iDateFrom = this.iFromDate.getDate();
            final Date iDateTo   = this.iToDate  .getDate();


            iVouchers = SSFilterFactory.doFilter(iVouchers, new SSFilter<SSVoucher>() {
                public boolean applyFilter(SSVoucher iInvoice) {
                    return SSVoucherMath.inPeriod(iInvoice, iDateFrom, iDateTo);
                }
            });
        }

        return iVouchers;
    }

}
