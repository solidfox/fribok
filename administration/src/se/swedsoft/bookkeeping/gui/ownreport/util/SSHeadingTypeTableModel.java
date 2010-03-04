package se.swedsoft.bookkeeping.gui.ownreport.util;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.base.SSSale;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;
import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.data.common.SSHeadingType;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.model.SSEditableTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.editors.*;
import se.swedsoft.bookkeeping.calc.math.SSProductMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSHeadingTypeTableModel extends SSTableModel<SSHeadingType> {

    public SSHeadingTypeTableModel(){
        super( SSHeadingType.values() );
    }


    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSHeadingType.class;
    }

    public static SSHeadingTypeTableModel getDropDownModel(){
        SSHeadingTypeTableModel iModel = new SSHeadingTypeTableModel();

        iModel.addColumn( COLUMN_TYPE );
        //iModel.sort();

        return iModel;
    }
    /**
     * Moms
     */
    public static SSTableColumn<SSHeadingType> COLUMN_TYPE = new SSTableColumn<SSHeadingType>(SSBundle.getBundle().getString("ownreport.headingtable.column.1")) {
        @Override
        public Object getValue(SSHeadingType iObject) {
            return iObject.toString();
        }

        @Override
        public void setValue(SSHeadingType iObject, Object iValue) {
            //iObject.setType((SSHeadingType)iValue);
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 60;
        }
    };
}
