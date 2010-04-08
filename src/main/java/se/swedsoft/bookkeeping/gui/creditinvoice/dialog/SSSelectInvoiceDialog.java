package se.swedsoft.bookkeeping.gui.creditinvoice.dialog;

import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.invoice.util.SSInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: Andreas Lago
 * Date: 2006-apr-21
 * Time: 11:04:30
 */
public class SSSelectInvoiceDialog extends SSDialog {

    private SSTableComboBox<SSInvoice> iInvoice;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;


    /**
     *
     * @param iMainFrame
     */
    public SSSelectInvoiceDialog(SSMainFrame iMainFrame) {
        super(iMainFrame, SSBundle.getBundle().getString("creditinvoiceframe.selectinvoice.title"));

        setLayout(new BorderLayout() );
        add(iPanel, BorderLayout.CENTER);

        pack();

        iButtonPanel.addCancelActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.CANCEL_OPTION);
                setVisible(false);
            }
        });

        iButtonPanel.addOkActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setModalResult(JOptionPane.OK_OPTION);
                setVisible(false);
            }
        });

        iInvoice.setModel(SSInvoiceTableModel.getDropDownModel());
    }



    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }

    /**
     *
     * @param l
     */
    public void addOkActionListener(ActionListener l) {
        iButtonPanel.addOkActionListener(l);
    }

    /**
     *
     * @param l
     */
    public void addCancelActionListener(ActionListener l) {
        iButtonPanel.addCancelActionListener(l);
    }


    /**
     *
     * @param iMainFrame
     * @return
     */
    public static SSInvoice showDialog(SSMainFrame iMainFrame){
        SSSelectInvoiceDialog iDialog = new SSSelectInvoiceDialog(iMainFrame);
        iDialog.setLocationRelativeTo(iMainFrame);
        iDialog.setVisible(true);
        int iResult = iDialog.getModalResult();

        if(iResult != JOptionPane.OK_OPTION ){
            SSInvoice iTemp = new SSInvoice();
            iTemp.setNumber(-1);
            return iTemp;
        }

        SSInvoice selected = iDialog.iInvoice.getSelected();

        return SSDB.getInstance().getInvoice(selected);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.creditinvoice.dialog.SSSelectInvoiceDialog");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iInvoice=").append(iInvoice);
        sb.append(", iPanel=").append(iPanel);
        sb.append('}');
        return sb.toString();
    }
}
