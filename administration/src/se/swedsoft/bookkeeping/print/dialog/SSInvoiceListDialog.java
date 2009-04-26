package se.swedsoft.bookkeeping.print.dialog;

import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBoxOld;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.customer.util.SSCustomerDropdownModel;
import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.calc.util.SSFilterFactory;
import se.swedsoft.bookkeeping.calc.util.SSFilter;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Date;

/**
 * User: Andreas Lago
 * Date: 2006-apr-19
 * Time: 14:28:34
 */
public class SSInvoiceListDialog extends SSDialog {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JRadioButton iRadioAll;
    private JRadioButton iRadioNotpayed;
    private JRadioButton iRadioExpired;

    private JCheckBox iCheckDate;
    private JCheckBox iCheckCustomer;

    private SSTableComboBoxOld<SSCustomer> iCustomer;
    private SSDateChooser iToDate;
    private SSDateChooser iFromDate;

    /**
     *
     */
    public SSInvoiceListDialog(SSMainFrame iMainFrame) {
        super(iMainFrame, SSBundle.getBundle().getString("invoicelistreport.dialog.title") );

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
        iGroup.add(iRadioNotpayed);
        iGroup.add(iRadioExpired);

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
    public List<SSInvoice> getInvoicesToPrint(){

        //    final List<SSInvoice>  iInvoices  = SSDB.getInstance().getInvoices();
        final List<SSCustomer> iCustomers = SSDB.getInstance().getCustomers();

        List<SSInvoice> iInvoices = SSDB.getInstance().getInvoices();

        SSFilterFactory<SSInvoice> iFactory = new SSFilterFactory<SSInvoice>(iInvoices);

        // Filter by non payed invoices
        if( iRadioNotpayed.isSelected() ){

            iFactory.applyFilter(new SSFilter<SSInvoice>() {
                public boolean applyFilter(SSInvoice iInvoice) {
                    return SSInvoiceMath.getSaldo(iInvoice.getNumber()).signum() != 0;
                }
            });
        }

        // Filter by expired
        if( iRadioExpired.isSelected() ){

            iFactory.applyFilter(new SSFilter<SSInvoice>() {
                public boolean applyFilter(SSInvoice iInvoice) {
                    return SSInvoiceMath.getSaldo(iInvoice.getNumber()).signum() != 0 && SSInvoiceMath.expired(iInvoice);
                }
            });
        }

        // Filter by a customer
        if(iCheckCustomer.isSelected() && iCustomer.hasSelected() ){
            final SSCustomer iCustomer = this.iCustomer.getSelected();

            iFactory.applyFilter(new SSFilter<SSInvoice>() {
                public boolean applyFilter(SSInvoice iInvoice) {
                    return iCustomer.equals( iInvoice.getCustomer(iCustomers) );
                }
            });

        }
        // Filter by date
        if(iCheckDate.isSelected() ){
            final Date iDateFrom = this.iFromDate.getDate();
            final Date iDateTo   = this.iToDate  .getDate();

            iFactory.applyFilter(new SSFilter<SSInvoice>() {
                public boolean applyFilter(SSInvoice iInvoice) {
                    return SSInvoiceMath.inPeriod(iInvoice, iDateFrom, iDateTo);
                }
            });
        }

        return iFactory.getObjects();
    }
}
