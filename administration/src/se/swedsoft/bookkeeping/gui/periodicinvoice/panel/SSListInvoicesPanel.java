package se.swedsoft.bookkeeping.gui.periodicinvoice.panel;

import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSPeriodicInvoice;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.invoice.util.SSInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSIntegerTextField;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import javax.swing.*;

/**
 * User: Andreas Lago
 * Date: 2006-okt-17
 * Time: 09:59:28
 */
public class SSListInvoicesPanel {

    private JPanel iPanel;


    private SSPeriodicInvoice iPeriodicInvoice;

    private SSTable iTable;


    private SSIntegerTextField iNumber;

    private SSIntegerTextField iPeriod;

    private JTextField iDescription;

    private SSDateChooser iDate;

    private SSDateChooser iNext;

    private SSIntegerTextField iCount;


    private SSTableModel<SSInvoice> iModel;


    /**
     *
     * @param iPeriodicInvoice
     */
    public SSListInvoicesPanel(SSPeriodicInvoice iPeriodicInvoice) {
        this.iPeriodicInvoice = iPeriodicInvoice;

        iModel = new SSInvoiceTableModel();
        iModel.addColumn( new AddedColumn() );
        iModel.addColumn( SSInvoiceTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSInvoiceTableModel.COLUMN_DATE );
        iModel.addColumn( SSInvoiceTableModel.COLUMN_DUEDATE );
        iModel.setupTable(iTable);
        iModel.setObjects( iPeriodicInvoice.getInvoices() );

        // Nummer
        iNumber.setValue( iPeriodicInvoice.getNumber() );
        // Första faktueringsdatum
        iDate.setDate( iPeriodicInvoice.getDate() );
        // Nästa fakturadatum
        iNext.setDate( iPeriodicInvoice.getNextDate() );
        // Antal fakturor
        iCount.setValue( iPeriodicInvoice.getCount() );
        // Periodtid i månader
        iPeriod.setValue( iPeriodicInvoice.getPeriod() );
        // Beskrivning
        iDescription.setText( iPeriodicInvoice.getDescription() );
    }

    /**
     *
     * @param iMainFrame
     */
    public static void showDialog(SSMainFrame iMainFrame, SSPeriodicInvoice iPeriodicInvoice){
        SSListInvoicesPanel iPanel = new SSListInvoicesPanel(iPeriodicInvoice);
        JOptionPane.showMessageDialog(iMainFrame, iPanel.iPanel, SSBundle.getBundle().getString("periodicinvoiceframe.invoicelist.title"), JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Datum
     */
    private class AddedColumn extends SSTableColumn<SSInvoice>{

        public AddedColumn(){
            super(SSBundle.getBundle().getString("periodicinvoicetable.column.6"));
        }
        @Override
        public Object getValue(SSInvoice iInvoice) {
            return iPeriodicInvoice.isAdded( iInvoice );
        }

        @Override
        public void setValue(SSInvoice iInvoice, Object iValue) {
        }

        @Override
        public Class getColumnClass() {
            return Boolean.class;
        }

        @Override
        public int getDefaultWidth() {
            return 80;
        }
    }

}
