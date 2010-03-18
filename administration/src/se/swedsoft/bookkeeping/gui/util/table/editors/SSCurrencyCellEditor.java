package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.model.SSCurrencyTableModel;

import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 11:37:41
 */
public class SSCurrencyCellEditor extends SSTableComboBox.CellEditor<SSCurrency> {

    /**
     *
     */
    public SSCurrencyCellEditor() {
        setModel( SSCurrencyTableModel.getDropDownModel() );
        setSearchColumns(0);
        setAllowCustomValues(false);
    }

    /**
     *
     * @param iCurrencies
     */
    public SSCurrencyCellEditor(List<SSCurrency> iCurrencies) {
        setModel( SSCurrencyTableModel.getDropDownModel(iCurrencies) );
        setSearchColumns(0);
        setAllowCustomValues(false);
    }
}
