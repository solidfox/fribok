package se.swedsoft.bookkeeping.print.dialog;

import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.calc.util.SSFilter;
import se.swedsoft.bookkeeping.calc.util.SSFilterFactory;
import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSTender;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.customer.util.SSCustomerDropdownModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBoxOld;
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
 * User: Andreas Lago
 * Date: 2006-apr-19
 * Time: 14:28:34
 */
public class SSTenderListDialog extends SSDialog {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JRadioButton iRadioAll;
    private JRadioButton iRadioActive;

    private JCheckBox iCheckDate;
    private JCheckBox iCheckCustomer;

    private SSTableComboBoxOld<SSCustomer> iCustomer;
    private SSDateChooser iToDate;
    private SSDateChooser iFromDate;

    /**
     *
     */
    public SSTenderListDialog(SSMainFrame iMainFrame) {
        super(iMainFrame, SSBundle.getBundle().getString("tenderlistreport.dialog.title") );

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


        iCustomer.setModel( new SSCustomerDropdownModel() );
        iCustomer.setSearchColumns(0);
        iCustomer.setColumnWidths(60 , 200);
        iCustomer.setPopupSize   (250, 150);


        ChangeListener iChangeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iCustomer.setEnabled( iCheckCustomer.isSelected() );

                iFromDate.setEnabled( iCheckDate.isSelected() );
                iToDate  .setEnabled( iCheckDate.isSelected() );
            }
        };

        iCheckDate     .addChangeListener(iChangeListener);
        iCheckCustomer .addChangeListener(iChangeListener);

        ButtonGroup iGroup = new ButtonGroup();

        iGroup.add(iRadioAll);
        iGroup.add(iRadioActive);

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
     * Returns the invoices to print depending on the user selections
     *
     * @return
     */
    public List<SSTender> getTendersToPrint(){
        final List<SSCustomer> iCustomers = SSDB.getInstance().getCustomers();

        List<SSTender> iTenders = SSDB.getInstance().getTenders();

        SSFilterFactory<SSTender> iFactory = new SSFilterFactory<SSTender>(iTenders);

        // Filter by non payed invoices
        if( iRadioActive.isSelected() ){

            iFactory.applyFilter(new SSFilter<SSTender>() {
                public boolean applyFilter(SSTender iTender) {
                    return ! iTender.isExpired();
                }
            });
        }


        // Filter by a customer
        if(iCheckCustomer.isSelected() && iCustomer.hasSelected() ){
            final SSCustomer iCustomer = this.iCustomer.getSelected();

            iFactory.applyFilter(new SSFilter<SSTender>() {
                public boolean applyFilter(SSTender iTender) {
                    return iCustomer.equals( iTender.getCustomer(iCustomers) );
                }
            });

        }
        // Filter by date
        if(iCheckDate.isSelected() ){
            final Date iDateFrom = this.iFromDate.getDate();
            final Date iDateTo   = this.iToDate  .getDate();

            iFactory.applyFilter(new SSFilter<SSTender>() {
                public boolean applyFilter(SSTender iTender) {
                    return SSInvoiceMath.inPeriod(iTender, iDateFrom, iDateTo);
                }
            });
        }

        return iFactory.getObjects();
    }
}
