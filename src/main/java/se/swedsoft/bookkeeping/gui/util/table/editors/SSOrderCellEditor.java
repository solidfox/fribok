package se.swedsoft.bookkeeping.gui.util.table.editors;


import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSOrder;
import se.swedsoft.bookkeeping.gui.order.util.SSOrderTableModel;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;

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
        setModel(SSOrderTableModel.getDropdownModel());
        setSearchColumns(0);
        setAllowCustomValues(false);
    }

    /**
     *
     * @param iInvoices
     */
    public SSOrderCellEditor(List<SSInvoice> iInvoices) {
        setModel(SSOrderTableModel.getDropdownModel());
        setSearchColumns(0);
        setAllowCustomValues(false);
    }
}
