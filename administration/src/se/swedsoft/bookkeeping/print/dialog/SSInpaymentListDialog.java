package se.swedsoft.bookkeeping.print.dialog;

import se.swedsoft.bookkeeping.calc.math.SSInpaymentMath;
import se.swedsoft.bookkeeping.calc.util.SSFilter;
import se.swedsoft.bookkeeping.calc.util.SSFilterFactory;
import se.swedsoft.bookkeeping.data.SSInpayment;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.invoice.util.SSInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
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
 * User: Andreas Lago
 * Date: 2006-apr-19
 * Time: 14:28:34
 */
public class SSInpaymentListDialog extends SSDialog {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private JCheckBox iCheckDate;
    private JCheckBox iCheckInvoice;

    private SSTableComboBox<SSInvoice> iInvoice;
    private SSDateChooser iToDate;
    private SSDateChooser iFromDate;

    /**
     *
     * @param iMainFrame
     */
    public SSInpaymentListDialog(SSMainFrame iMainFrame) {
        super(iMainFrame, SSBundle.getBundle().getString("inpaymentlistreport.dialog.title") );

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


        iInvoice.setModel( SSInvoiceTableModel.getDropDownModel() );
        iInvoice.setSearchColumns(0);

        ChangeListener iChangeListener = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iInvoice.setEnabled( iCheckInvoice.isSelected() );

                iFromDate.setEnabled( iCheckDate.isSelected() );
                iToDate  .setEnabled( iCheckDate.isSelected() );
            }
        };

        iCheckDate     .addChangeListener(iChangeListener);
        iCheckInvoice .addChangeListener(iChangeListener);

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
    public List<SSInpayment> getElementsToPrint(){
        List<SSInpayment> iInpayments = SSDB.getInstance().getInpayments();

        SSFilterFactory<SSInpayment> iFactory = new SSFilterFactory<SSInpayment>(iInpayments);


        // Filter by a customer
        if(iCheckInvoice.isSelected() && iInvoice.getSelected() != null ){
            final SSInvoice iInvoice = this.iInvoice.getSelected();

            iFactory.applyFilter(new SSFilter<SSInpayment>() {
                public boolean applyFilter(SSInpayment iInpayment) {
                    return SSInpaymentMath.hasInvoice(iInpayment, iInvoice );
                }
            });

        }
        // Filter by date
        if(iCheckDate.isSelected() ){
            final Date iDateFrom = iFromDate.getDate();
            final Date iDateTo   = iToDate.getDate();

            iFactory.applyFilter(new SSFilter<SSInpayment>() {
                public boolean applyFilter(SSInpayment iInpayment) {
                    return SSInpaymentMath.inPeriod(iInpayment, iDateFrom, iDateTo);
                }
            });
        }

        return iFactory.getObjects();
    }


    /**
     *
     * @return
     */
    public boolean isDateSelected() {
        return iCheckDate.isSelected();
    }

    /**
     *
     * @return
     */
    public boolean isInvoiceSelected() {
        return iCheckInvoice.isSelected();
    }

    /**
     *
     * @return
     */
    public Date getDateFrom() {
        return iFromDate.getDate();
    }

    /**
     *
     * @return
     */
    public Date getDateTo() {
        return iToDate.getDate();
    }

    /**
     *
     * @return
     */
    public SSInvoice getInvoice() {
        return iInvoice.getSelected();
    }
}
