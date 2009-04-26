package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.model.SSAccountTableModel;
import se.swedsoft.bookkeeping.gui.ownreport.util.SSHeadingTypeTableModel;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.common.SSHeadingType;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 11:37:41
 */
public class SSHeadingTypeCellEditor extends SSTableComboBox.CellEditor<SSHeadingType> {

    /**
     *
     */
    public SSHeadingTypeCellEditor() {
        super();

        setModel( SSHeadingTypeTableModel.getDropDownModel() );

        setSearchColumns(0);
        setAllowCustomValues(false);
    }
}
