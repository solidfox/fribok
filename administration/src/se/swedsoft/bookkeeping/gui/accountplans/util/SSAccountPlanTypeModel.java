package se.swedsoft.bookkeeping.gui.accountplans.util;

import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.data.SSAccountPlanType;

import java.util.List;

/**
 * Date: 2006-feb-14
 * Time: 09:48:40
 */
public class SSAccountPlanTypeModel extends SSTableModel<SSAccountPlanType> {

    /**
     * Default constructor.
     */
    public SSAccountPlanTypeModel() {
        super( SSAccountPlanType.getAccountPlanTypes() );
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSAccountPlanType.class;
    }

    /**
     *
     * @return
     */
    public static SSTableModel<SSAccountPlanType> getDropDownModel() {
        SSAccountPlanTypeModel iModel = new SSAccountPlanTypeModel();

        iModel.addColumn(COLUMN_NAME);

        return iModel;
    }

     /**
     * Namn
     */
    public static SSTableColumn<SSAccountPlanType> COLUMN_NAME = new SSTableColumn<SSAccountPlanType>("") {
        @Override
        public Object getValue(SSAccountPlanType iAccountPlanType) {
            return iAccountPlanType.getName();
        }

        @Override
        public void setValue(SSAccountPlanType iAccountPlanType, Object iValue) {
            iAccountPlanType.setName((String)iValue);
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 300;
        }
    };



}
