package se.swedsoft.bookkeeping.gui.supplierpayments;

import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSFileChooser;
import se.swedsoft.bookkeeping.gui.util.filechooser.util.SSFilterTXT;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.supplierpayments.util.SSSupplierPaymentTableModel;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSPostLock;
import se.swedsoft.bookkeeping.importexport.supplierpayments.SSSupplierPaymentExporter;
import se.swedsoft.bookkeeping.importexport.supplierpayments.data.SupplierPaymentConfig;
import se.swedsoft.bookkeeping.importexport.supplierpayments.data.SupplierPayment;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import java.util.List;
import java.util.Date;
import java.util.LinkedList;
import java.util.Collections;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * User: Andreas Lago
 * Date: 2006-aug-28
 * Time: 11:30:33
 */
public class SSSupplierPaymentDialog extends SSDialog {

    private JPanel iPanel;

    private SSTable iTable;

    private SSSupplierPaymentTableModel iModel;


    private SSButtonPanel iButtonPanel;

    private JTextField iOurBankGiroNumber;

    private JTextField iMessage;

    /**
     *
     * @param iMainFrame
     * @param iSupplierInvoices
     */
    public SSSupplierPaymentDialog(final SSMainFrame iMainFrame, List<SSSupplierInvoice> iSupplierInvoices) {
        super(iMainFrame, SSBundle.getBundle().getString("supplierpaymentframe.title"));
        setPanel(iPanel );
        iTable.setColumnSortingEnabled(false);
        iTable.setColorReadOnly(true);

        iModel = new SSSupplierPaymentTableModel(iSupplierInvoices);
        iModel.addColumn( SSSupplierPaymentTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSSupplierPaymentTableModel.COLUMN_SUPPLIER_NUMBER  );
        iModel.addColumn( SSSupplierPaymentTableModel.COLUMN_SUPPLIER_NAME    );
        iModel.addColumn( SSSupplierPaymentTableModel.COLUMN_DATE           , true);
        iModel.addColumn( SSSupplierPaymentTableModel.COLUMN_VALUE          , true);
        iModel.addColumn( SSSupplierPaymentTableModel.COLUMN_CURRENCY );
        iModel.addColumn( SSSupplierPaymentTableModel.COLUMN_PAYMENT_METHOD, true );
        iModel.addColumn( SSSupplierPaymentTableModel.COLUMN_ACCOUNT       , true);

        iModel.setupTable(iTable);

        iOurBankGiroNumber.setText( SSDB.getInstance().getCurrentCompany().getBankGiroNumber() );

        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<SupplierPayment> iSupplierPayments = iModel.getObjects();

                for(SupplierPayment iPayment : iSupplierPayments) {
                    SSSupplierInvoice pSupplierInvoice = iPayment.getSupplierInvoice();
                    if(SSDB.getInstance().getSupplierInvoice(pSupplierInvoice) == null){
                        iSupplierPayments.remove(iPayment);
                        new SSErrorDialog( iMainFrame, "supplierinvoiceframe.supplierinvoicegone",pSupplierInvoice.getNumber());
                    }

                }
                iModel.setObjects(iSupplierPayments);
                if(iSupplierPayments.size() == 0){
                    new SSErrorDialog( iMainFrame, "supplierinvoiceframe.nosupplierpayments");
                    return;
                }

                SSFileChooser iFileChooser = new SSFileChooser( new SSFilterTXT() );

                iFileChooser.setSelectedFile( new File("Leverantörsbetalning.txt"));
                int iResponce = iFileChooser.showSaveDialog(iMainFrame);
                if( iResponce != SSFileChooser.APPROVE_OPTION){
                    return;
                }

                for(SupplierPayment iPayment : iSupplierPayments) {
                    SSSupplierInvoice pSupplierInvoice = iPayment.getSupplierInvoice();
                    if(SSDB.getInstance().getSupplierInvoice(pSupplierInvoice) == null){
                        iSupplierPayments.remove(iPayment);
                        new SSErrorDialog( iMainFrame, "supplierinvoiceframe.supplierinvoicegone",pSupplierInvoice.getNumber());
                    }

                }
                iModel.setObjects(iSupplierPayments);
                if(iSupplierPayments.size() == 0){
                    new SSErrorDialog( iMainFrame, "supplierinvoiceframe.nosupplierpayments");
                    return;
                }
                Date iDate = new Date();
                for(SupplierPayment iSupplierPayment : iSupplierPayments){
                    if (iSupplierPayment.getDate().after(iDate)) {
                        iDate = iSupplierPayment.getDate();
                    }
                    iSupplierPayment.getSupplierInvoice().setBGCEntered();
                    SSDB.getInstance().updateSupplierInvoice(iSupplierPayment.getSupplierInvoice());
                }
                SupplierPaymentConfig.setOurBankGiroAccount( iOurBankGiroNumber.getText() );
                SupplierPaymentConfig.setMessage           ( iMessage.getText()           );
                SupplierPaymentConfig.setMessageDate       ( iDate );

                try {
                    SSSupplierPaymentExporter.Export(iFileChooser.getSelectedFile(), iSupplierPayments);
                } catch (SSExportException e1) {
                    SSErrorDialog.showDialog(iMainFrame, SSBundle.getBundle().getString("supplierpaymentframe.error"), e1.getMessage() );

                    e1.printStackTrace();

                    return;
                }

                SSPostLock.removeLock("supplierpayment"+SSDB.getInstance().getCurrentCompany().getId());
                closeDialog(JOptionPane.OK_OPTION);
            }
        });

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSPostLock.removeLock("supplierpayment"+SSDB.getInstance().getCurrentCompany().getId());
                closeDialog();
            }
        });

        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                SSPostLock.removeLock("supplierpayment"+SSDB.getInstance().getCurrentCompany().getId());
            }
        });

    }




}
