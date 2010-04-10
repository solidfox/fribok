package se.swedsoft.bookkeeping.print.dialog;


import se.swedsoft.bookkeeping.calc.math.SSOrderMath;
import se.swedsoft.bookkeeping.calc.util.SSFilter;
import se.swedsoft.bookkeeping.calc.util.SSFilterFactory;
import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSOrder;
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
public class SSOrderListDialog extends SSDialog {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JRadioButton iRadioAll;
    private JRadioButton iRadioInvoice;
    private JCheckBox iCheckDate;
    private JCheckBox iCheckCustomer;

    private SSTableComboBoxOld<SSCustomer> iCustomer;
    private SSDateChooser iToDate;
    private SSDateChooser iFromDate;

    /**
     *
     * @param iMainFrame
     */
    public SSOrderListDialog(SSMainFrame iMainFrame) {
        super(iMainFrame, SSBundle.getBundle().getString("orderlistreport.dialog.title"));

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

        iCustomer.setModel(new SSCustomerDropdownModel());
        iCustomer.setSearchColumns(0);
        iCustomer.setColumnWidths(60, 200);
        iCustomer.setPopupSize(250, 150);

        ChangeListener iChangeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iCustomer.setEnabled(iCheckCustomer.isSelected());

                iFromDate.setEnabled(iCheckDate.isSelected());
                iToDate.setEnabled(iCheckDate.isSelected());
            }
        };

        iCheckDate.addChangeListener(iChangeListener);
        iCheckCustomer.addChangeListener(iChangeListener);

        ButtonGroup iGroup = new ButtonGroup();

        iGroup.add(iRadioAll);
        iGroup.add(iRadioInvoice);

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
     * Returns the orders to print depending on the user selections
     *
     * @return
     */
    public List<SSOrder> getOrdersToPrint() {

        final List<SSInvoice>  iInvoices = SSDB.getInstance().getInvoices();
        final List<SSCustomer> iCustomers = SSDB.getInstance().getCustomers();

        List<SSOrder> iOrders = SSDB.getInstance().getOrders();

        SSFilterFactory<SSOrder> iFactory = new SSFilterFactory<SSOrder>(iOrders);

        // Filter by orders with invoices
        if (iRadioInvoice.isSelected()) {

            iFactory.applyFilter(new SSFilter<SSOrder>() {
                public boolean applyFilter(SSOrder iOrder) {
                    return iOrder.getInvoice(iInvoices) == null;
                }
            });
        }

        // Filter by a customer
        if (iCheckCustomer.isSelected() && iCustomer.hasSelected()) {
            final SSCustomer iCustomer = this.iCustomer.getSelected();

            iFactory.applyFilter(new SSFilter<SSOrder>() {
                public boolean applyFilter(SSOrder iOrder) {
                    return iCustomer.equals(iOrder.getCustomer(iCustomers));
                }
            });

        }
        // Filter by date
        if (iCheckDate.isSelected()) {
            final Date iDateFrom = iFromDate.getDate();
            final Date iDateTo = iToDate.getDate();

            iFactory.applyFilter(new SSFilter<SSOrder>() {
                public boolean applyFilter(SSOrder iOrder) {
                    return SSOrderMath.inPeriod(iOrder, iDateFrom, iDateTo);
                }
            });
        }

        return iFactory.getObjects();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.dialog.SSOrderListDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iCheckCustomer=").append(iCheckCustomer);
        sb.append(", iCheckDate=").append(iCheckDate);
        sb.append(", iCustomer=").append(iCustomer);
        sb.append(", iFromDate=").append(iFromDate);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iRadioAll=").append(iRadioAll);
        sb.append(", iRadioInvoice=").append(iRadioInvoice);
        sb.append(", iToDate=").append(iToDate);
        sb.append('}');
        return sb.toString();
    }
}
