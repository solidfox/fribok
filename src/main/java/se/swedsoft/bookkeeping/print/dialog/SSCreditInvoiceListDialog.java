package se.swedsoft.bookkeeping.print.dialog;

import se.swedsoft.bookkeeping.calc.math.SSCreditInvoiceMath;
import se.swedsoft.bookkeeping.calc.util.SSFilter;
import se.swedsoft.bookkeeping.calc.util.SSFilterFactory;
import se.swedsoft.bookkeeping.data.SSCreditInvoice;
import se.swedsoft.bookkeeping.data.SSCustomer;
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
public class SSCreditInvoiceListDialog extends SSDialog {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JCheckBox iCheckDate;
    private JCheckBox iCheckCustomer;

    private SSTableComboBoxOld<SSCustomer> iCustomer;
    private SSDateChooser iToDate;
    private SSDateChooser iFromDate;

    /**
     *
     * @param iMainFrame
     */
    public SSCreditInvoiceListDialog(SSMainFrame iMainFrame) {
        super(iMainFrame, SSBundle.getBundle().getString("creditinvoicelistreport.dialog.title") );

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
    public List<SSCreditInvoice> getInvoicesToPrint(){

        //    final List<SSInvoice>  iInvoices  = SSDB.getInstance().getInvoices();
        final List<SSCustomer> iCustomers = SSDB.getInstance().getCustomers();

        List<SSCreditInvoice> iInvoices = SSDB.getInstance().getCreditInvoices();

        SSFilterFactory<SSCreditInvoice> iFactory = new SSFilterFactory<SSCreditInvoice>(iInvoices);

                // Filter by a customer
        if(iCheckCustomer.isSelected() && iCustomer.hasSelected() ){
            final SSCustomer iCustomer = this.iCustomer.getSelected();

            iFactory.applyFilter(new SSFilter<SSCreditInvoice>() {
                public boolean applyFilter(SSCreditInvoice iInvoice) {
                    return iCustomer.equals( iInvoice.getCustomer(iCustomers) );
                }
            });

        }
        // Filter by date
        if(iCheckDate.isSelected() ){
            final Date iDateFrom = iFromDate.getDate();
            final Date iDateTo   = iToDate.getDate();

            iFactory.applyFilter(new SSFilter<SSCreditInvoice>() {
                public boolean applyFilter(SSCreditInvoice iInvoice) {
                    return SSCreditInvoiceMath.inPeriod(iInvoice, iDateFrom, iDateTo);
                }
            });
        }

        return iFactory.getObjects();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.print.dialog.SSCreditInvoiceListDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iCheckCustomer=").append(iCheckCustomer);
        sb.append(", iCheckDate=").append(iCheckDate);
        sb.append(", iCustomer=").append(iCustomer);
        sb.append(", iFromDate=").append(iFromDate);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iToDate=").append(iToDate);
        sb.append('}');
        return sb.toString();
    }
}
