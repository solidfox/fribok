package se.swedsoft.bookkeeping.gui.util.table.editors;


import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.model.SSAccountTableModel;


/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 11:37:41
 */
public class SSAccountCellEditor extends SSTableComboBox.CellEditor<SSAccount> {

    /**
     *
     */
    public SSAccountCellEditor() {
        setModel(SSAccountTableModel.getDropDownModel());

        setSearchColumns(0, 1);
        setAllowCustomValues(false);
    }

}
