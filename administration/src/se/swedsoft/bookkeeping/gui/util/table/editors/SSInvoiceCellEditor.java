package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.gui.invoice.util.SSInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;

import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 11:37:41
 */
public class SSInvoiceCellEditor extends SSTableComboBox.CellEditor<SSInvoice> {

    /**
     *
     */
    public SSInvoiceCellEditor() {
        super();

        setModel( SSInvoiceTableModel.getDropDownModel() );
        setSearchColumns(0);
        setAllowCustomValues(false);
    }

    /**
     *
     */
    public SSInvoiceCellEditor(List<SSInvoice> iInvoices) {
        super();

        setModel( SSInvoiceTableModel.getDropDownModel(iInvoices) );
        setSearchColumns(0);
        setAllowCustomValues(false);

    }


}
