package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.gui.product.util.SSProductTableModel;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;

import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 11:37:41
 */
public class SSProductCellEditor extends SSTableComboBox.CellEditor<SSProduct> {

    /**
     *
     * @param iAllowCustomValues
     */
    public SSProductCellEditor(boolean iAllowCustomValues) {
        setModel( SSProductTableModel.getDropDownModel() );

        setSearchColumns(0, 1);
        setAllowCustomValues(iAllowCustomValues);
    }

    /**
     *
     * @param iProducts
     * @param iAllowCustomValues
     */
    public SSProductCellEditor(List<SSProduct> iProducts, boolean iAllowCustomValues) {
        setModel( SSProductTableModel.getDropDownModel(iProducts) );
        setSearchColumns(0, 1);
        setAllowCustomValues(iAllowCustomValues);
    }

    /**
     *
     */
    public SSProductCellEditor() {
        this(true);
    }

    /**
     *
     * @param iProducts
     */
    public SSProductCellEditor(List<SSProduct> iProducts) {
        this(iProducts, true);
    }
}
