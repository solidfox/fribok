package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.SSNewResultUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBoxOld;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 11:37:41
 */
public class SSResultUnitCellEditor extends SSTableComboBoxOld.CellEditor<SSNewResultUnit> {
    /**
     *
     */
    public SSResultUnitCellEditor() {
        SSDefaultTableModel<SSNewResultUnit> iModel = new SSDefaultTableModel<SSNewResultUnit>(SSDB.getInstance().getResultUnits()) {
            @Override
            public Class getType() {
                return SSNewResultUnit.class;
            }
            public Object getValueAt(int rowIndex, int columnIndex) {
                SSNewResultUnit iResultUnit = getObject(rowIndex);

                Object value = null;
                switch (columnIndex) {
                    case 0:
                        value = iResultUnit.getNumber();
                        break;
                    case 1:
                        value= iResultUnit.getName();
                        break;
                }

                return value;
            }
        };
        iModel.addColumn(SSBundle.getBundle().getString("resultunittable.column.1"));
        iModel.addColumn(SSBundle.getBundle().getString("resultunittable.column.2"));

        setModel( iModel );

        setSearchColumns(0);
        setPopupSize   (360,200);
        setColumnWidths(60,300);

    }




}
