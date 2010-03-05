package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.gui.supplierinvoice.util.SSSupplierInvoiceTableModel;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;

import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 11:37:41
 */
public class SSSupplierInvoiceCellEditor extends SSTableComboBox.CellEditor<SSSupplierInvoice> {


    /**
     *
     */
    public SSSupplierInvoiceCellEditor(List<SSSupplierInvoice> iInvoices) {
        super();

        setModel( SSSupplierInvoiceTableModel.getDropDownModel(iInvoices) );
        setSearchColumns(0);
        setAllowCustomValues(false);

    }

    /**
     *
     */
    public SSSupplierInvoiceCellEditor() {
        super();

        setModel( SSSupplierInvoiceTableModel.getDropDownModel() );

        setSearchColumns(0);
        setAllowCustomValues(false);

    }



}
