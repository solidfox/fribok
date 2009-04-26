package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.invoice.util.SSInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.order.util.SSOrderTableModel;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSOrder;

import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 11:37:41
 */
public class SSOrderCellEditor extends SSTableComboBox.CellEditor<SSOrder> {

    /**
     *
     */
    public SSOrderCellEditor() {
        super();

        setModel( SSOrderTableModel.getDropdownModel() );
        setSearchColumns(0);
        setAllowCustomValues(false);
    }

    /**
     *
     */
    public SSOrderCellEditor(List<SSInvoice> iInvoices) {
        super();

        setModel( SSOrderTableModel.getDropdownModel() );
        setSearchColumns(0);
        setAllowCustomValues(false);

    }


}
