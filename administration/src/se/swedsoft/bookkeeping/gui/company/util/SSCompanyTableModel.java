package se.swedsoft.bookkeeping.gui.company.util;

import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;

/**
 * User: Fredrik Stigsson
 * Date: 2006-feb-02
 * Time: 15:17:22
 */
public class SSCompanyTableModel extends SSDefaultTableModel<SSNewCompany>{

    public SSCompanyTableModel(){
        addColumn(getBundle().getString("companytable.column.1"));
        addColumn(getBundle().getString("companytable.column.2"));
    }


    @Override
    public Class getType() {
        return SSNewCompany.class;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        SSNewCompany iCompany = getObject(rowIndex);

        switch (columnIndex) {
            case 0:
                return iCompany.equals(SSDB.getInstance().getCurrentCompany());
            case 1:
                return iCompany.getName();

        }

        return "";
    }

    /**
     * Returns <code>Object.class</code> regardless of <code>columnIndex</code>.
     *
     * @param columnIndex the column being queried
     *
     * @return the Object.class
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {

        switch (columnIndex) {

            case 0:
                return Boolean.class;
            case 1:
                return String.class;
        }

        return super.getColumnClass(columnIndex);
    }
}

