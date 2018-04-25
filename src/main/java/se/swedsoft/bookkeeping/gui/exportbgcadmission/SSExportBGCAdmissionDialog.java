package se.swedsoft.bookkeeping.gui.exportbgcadmission;


import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.exportbgcadmission.util.SSExportBGCAdmissionTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;


/**
 * $Id$
 *
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
     * @param iCustomers
     */
    public SSExportBGCAdmissionDialog(final SSMainFrame iMainFrame, List<SSCustomer> iCustomers) {
        super(iMainFrame,
                SSBundle.getBundle().getString("customerframe.bgcadmission.title"));
        setPanel(iPanel);
        iTable.setColumnSortingEnabled(false);
        iTable.setColorReadOnly(true);

        iModel = new SSExportBGCAdmissionTableModel(iCustomers);
        iModel.addColumn(SSExportBGCAdmissionTableModel.COLUMN_NUMBER);
        iModel.addColumn(SSExportBGCAdmissionTableModel.COLUMN_NAME);
        iModel.addColumn(SSExportBGCAdmissionTableModel.COLUMN_BANKGIRO);
        iModel.addColumn(SSExportBGCAdmissionTableModel.COLUMN_CLEARINGNUMBER);
        iModel.addColumn(SSExportBGCAdmissionTableModel.COLUMN_ACCOUNTNR);

        iModel.setupTable(iTable);

        iOurBankGiroNumber.setText(
                SSDB.getInstance().getCurrentCompany().getBankGiroNumber());

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

	getRootPane().setDefaultButton(iButtonPanel.getOkButton());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {}
        });

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(
                "se.swedsoft.bookkeeping.gui.exportbgcadmission.SSExportBGCAdmissionDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iMessage=").append(iMessage);
        sb.append(", iModel=").append(iModel);
        sb.append(", iOurBankGiroNumber=").append(iOurBankGiroNumber);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iTable=").append(iTable);
        sb.append('}');
        return sb.toString();
    }
}
