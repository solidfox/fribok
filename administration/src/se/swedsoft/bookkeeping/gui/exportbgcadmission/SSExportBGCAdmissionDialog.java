package se.swedsoft.bookkeeping.gui.exportbgcadmission;

import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSFileChooser;
import se.swedsoft.bookkeeping.gui.util.filechooser.util.SSFilterTXT;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.exportbgcadmission.util.SSExportBGCAdmissionTableModel;
import se.swedsoft.bookkeeping.gui.supplierpayments.util.SSSupplierPaymentTableModel;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.SSCustomer;
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
public class SSExportBGCAdmissionDialog extends SSDialog {

    private JPanel iPanel;

    private SSTable iTable;

    private SSExportBGCAdmissionTableModel iModel;


    private SSButtonPanel iButtonPanel;

    private JTextField iOurBankGiroNumber;

    private JTextField iMessage;

    /**
     *
     * @param iMainFrame
     */
    public SSExportBGCAdmissionDialog(final SSMainFrame iMainFrame, List<SSCustomer> iCustomers) {
        super(iMainFrame, SSBundle.getBundle().getString("customerframe.bgcadmission.title"));
        setPanel(iPanel );
        iTable.setColumnSortingEnabled(false);
        iTable.setColorReadOnly(true);

        iModel = new SSExportBGCAdmissionTableModel(iCustomers);
        iModel.addColumn( SSExportBGCAdmissionTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSExportBGCAdmissionTableModel.COLUMN_NAME  );
        iModel.addColumn( SSExportBGCAdmissionTableModel.COLUMN_BANKGIRO);
        iModel.addColumn( SSExportBGCAdmissionTableModel.COLUMN_CLEARINGNUMBER);
        iModel.addColumn( SSExportBGCAdmissionTableModel.COLUMN_ACCOUNTNR);

        iModel.setupTable(iTable);

        iOurBankGiroNumber.setText( SSDB.getInstance().getCurrentCompany().getBankGiroNumber() );

        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog(JOptionPane.OK_OPTION);
            }
        });

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                closeDialog();
            }
        });

        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
            }
        });

    }




}
