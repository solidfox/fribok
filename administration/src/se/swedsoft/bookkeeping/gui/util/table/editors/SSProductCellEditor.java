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
     */
    public SSProductCellEditor(boolean iAllowCustomValues) {
        super();

        setModel( SSProductTableModel.getDropDownModel() );

        setSearchColumns(0, 1);
        setAllowCustomValues(iAllowCustomValues);
    }


    /**
     *
     */
    public SSProductCellEditor(List<SSProduct> iProducts, boolean iAllowCustomValues) {
        super();

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
     */
    public SSProductCellEditor(List<SSProduct> iProducts) {
        this(iProducts, true);
    }




}
