package se.swedsoft.bookkeeping.gui.inpayment.util;


import se.swedsoft.bookkeeping.calc.math.SSInpaymentMath;
import se.swedsoft.bookkeeping.data.SSInpayment;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellRenderer;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSDateCellRenderer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSInpaymentTableModel extends SSDefaultTableModel<SSInpayment> {

    /**
     * Default constructor.
     */
    public SSInpaymentTableModel() {
        this(SSDB.getInstance().getInpayments());
    }

    /**
     * Constructor.
     *
     * @param pObjects The data for the table model.
     */
    public SSInpaymentTableModel(List<SSInpayment> pObjects) {
        super(pObjects);
        addColumn(SSBundle.getBundle().getString("inpaymenttable.column.1"));
        addColumn(SSBundle.getBundle().getString("inpaymenttable.column.2"));
        addColumn(SSBundle.getBundle().getString("inpaymenttable.column.3"));
        addColumn(SSBundle.getBundle().getString("inpaymenttable.column.4"));
    }

    @Override
    public Class getType() {
        return SSProduct.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SSInpayment iInpayment = getObject(rowIndex);

        Object value = null;

        switch (columnIndex) {
        case 0:
            value = iInpayment.getNumber();
            break;

        case 1:
            value = iInpayment.getDate();
            break;

        case 2:
            value = iInpayment.getText();
            break;

        case 3:
            value = SSInpaymentMath.getSum(iInpayment);
            break;
        }

        return value;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
        case 0:
            return String.class;

        case 1:
            return Date.class;

        case 2:
            return String.class;

        case 3:
            return BigDecimal.class;
        }
        return super.getColumnClass(columnIndex);
    }

    /**
     *
     * @param iTable
     */
    public static void setupTable(SSTable iTable) {
        iTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        iTable.getColumnModel().getColumn(1).setPreferredWidth(110);
        iTable.getColumnModel().getColumn(2).setPreferredWidth(470);
        iTable.getColumnModel().getColumn(3).setPreferredWidth(95);

        iTable.setDefaultRenderer(Date.class, new SSDateCellRenderer());

        iTable.setDefaultRenderer(BigDecimal.class, new SSBigDecimalCellRenderer(2));
    }
}
