package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.supplier.util.SSSupplierTableModel;
import se.swedsoft.bookkeeping.data.SSSupplier;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 11:37:41
 */
public class SSSupplierCellEditor extends SSTableComboBox.CellEditor<SSSupplier> {
    /**
     *
     */
    public SSSupplierCellEditor() {
        super();

        setModel( SSSupplierTableModel.getDropDownModel() );

        setSearchColumns(0);
        setAllowCustomValues(false);
    }


}
