package se.swedsoft.bookkeeping.gui.ownreport.util;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.base.SSSale;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;
import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.model.SSEditableTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.editors.*;
import se.swedsoft.bookkeeping.calc.math.SSProductMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:34:35
 */
public class SSOwnReportAccountTableModel extends SSEditableTableModel<SSOwnReportAccountRow> {
    /**
     *
     * @return
     */
    public SSOwnReportAccountRow newObject() {
        return  new SSOwnReportAccountRow();
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSOwnReportAccountRow.class;
    }


    /**
     * Account column
     */
    public static SSTableColumn<SSOwnReportAccountRow> COLUMN_ACCOUNT = new SSTableColumn<SSOwnReportAccountRow>(SSBundle.getBundle().getString("ownreport.accounttable.column.1")) {
        public Object getValue(SSOwnReportAccountRow iObject) {
            return iObject.getAccount() == null ? "" : (iObject.getAccount().getNumber() == null ? "" : iObject.getAccount().getNumber().toString());
        }

        public void setValue(SSOwnReportAccountRow iObject, Object iValue) {
            iObject.setAccount((SSAccount)iValue);
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 75;
        }

        public TableCellEditor getCellEditor() {
            return new SSAccountCellEditor();
        }
    };

    /**
     * Description column
     */
    public static SSTableColumn<SSOwnReportAccountRow> COLUMN_DESCRIPTION = new SSTableColumn<SSOwnReportAccountRow>(SSBundle.getBundle().getString("ownreport.accounttable.column.2")) {
        public Object getValue(SSOwnReportAccountRow iObject) {
            return iObject.getAccount() == null ? null : iObject.getAccount().getDescription();
        }

        public void setValue(SSOwnReportAccountRow iObject, Object iValue) {
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 300;
        }
    };


    /**
     * Unit price
     */
    public static SSTableColumn<SSOwnReportAccountRow> COLUMN_BUDGET = new SSTableColumn<SSOwnReportAccountRow>(SSBundle.getBundle().getString("ownreport.accounttable.column.3")) {
        public Object getValue(SSOwnReportAccountRow iObject) {
            BigDecimal iSum = new BigDecimal(0);

            for(SSMonth iMonth : iObject.getBudget().keySet()){
                BigDecimal iMonthly = iObject.getBudget().get(iMonth);
                if(iMonthly != null) iSum = iSum.add(iMonthly);
            }
            return iSum.setScale(2, RoundingMode.HALF_UP);

        }

        public void setValue(SSOwnReportAccountRow iObject, Object iValue) {
            iObject.setYearBudget((BigDecimal) iValue);
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };
}

