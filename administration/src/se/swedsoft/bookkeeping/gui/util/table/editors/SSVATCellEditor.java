package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.common.SSVATCode;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.model.SSVATCodeTableModel;

import java.util.List;


/**
 * Date: 2006-mar-01
 * Time: 09:48:11
 */
public class SSVATCellEditor extends SSTableComboBox.CellEditor<SSVATCode> {


       /**
     *
     */
    public SSVATCellEditor() {
        super();

        setModel( SSVATCodeTableModel.getDropDownModel() );
        setSearchColumns(0);
        setAllowCustomValues(true);
    }

    /**
     *
     * @param iCurrencies
     */
    public SSVATCellEditor(List<SSVATCode> iCurrencies) {
        super();

        setModel( SSVATCodeTableModel.getDropDownModel(iCurrencies) );
        setSearchColumns(0);
        setAllowCustomValues(true);

    }


}
