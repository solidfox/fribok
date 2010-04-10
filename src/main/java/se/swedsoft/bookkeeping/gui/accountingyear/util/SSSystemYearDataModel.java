package se.swedsoft.bookkeeping.gui.accountingyear.util;


import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.system.SSSystemYear;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;

import java.util.Date;
import java.util.ResourceBundle;


/**
 * Date: 2006-feb-15
 * Time: 11:57:14
 */
public class SSSystemYearDataModel extends SSDefaultTableModel<SSNewAccountingYear> {

    private static ResourceBundle cBunbdle = SSBundle.getBundle();

    /**
     * Default constructor.
     */
    public SSSystemYearDataModel() {
        addColumn(cBunbdle.getString("accountingyeartable.column.1"));
        addColumn(cBunbdle.getString("accountingyeartable.column.2"));
        addColumn(cBunbdle.getString("accountingyeartable.column.3"));
        addColumn(cBunbdle.getString("accountingyeartable.column.4"));
    }

    @Override
    public Class getType() {
        return SSSystemYear.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SSNewAccountingYear year = getObject(rowIndex);

        Object value = null;

        switch (columnIndex) {
        case 0:
            value = year.equals(SSDB.getInstance().getCurrentYear());
            break;

        case 1:
            value = year.getFrom();
            break;

        case 2:
            value = year.getTo();
            break;

        case 3:
            value = year.getAccountPlan().getName();
            break;
        }

        return value;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {

        switch (columnIndex) {
        case 0:
            return Boolean.class;

        case 1:
            return Date.class;

        case 2:
            return Date.class;
        }

        return super.getColumnClass(columnIndex);

    }
}

