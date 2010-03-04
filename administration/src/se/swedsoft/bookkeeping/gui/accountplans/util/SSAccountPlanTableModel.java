package se.swedsoft.bookkeeping.gui.accountplans.util;

import se.swedsoft.bookkeeping.data.SSAccountPlan;
import se.swedsoft.bookkeeping.data.SSAccountPlanType;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;


/**
 * Date: 2006-feb-13
 * Time: 12:13:27
 */
public class SSAccountPlanTableModel extends SSTableModel<SSAccountPlan> {

    /**
     * Default constructor.
     */
    public SSAccountPlanTableModel() {
        super( SSDB.getInstance().getAccountPlans() );
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSAccountPlan.class;
    }

    /**
     *
     * @return
     */
    public static SSTableModel<SSAccountPlan> getDropDownModel() {
        SSAccountPlanTableModel iModel = new SSAccountPlanTableModel();

        iModel.addColumn(COLUMN_NAME);

        return iModel;
    }

     /**
     * Namn
     */
    public static SSTableColumn<SSAccountPlan> COLUMN_NAME = new SSTableColumn<SSAccountPlan>(SSBundle.getBundle().getString("accountplantable.column.1")) {
        @Override
        public Object getValue(SSAccountPlan iAccountPlan) {
            return iAccountPlan.getName();
        }

        @Override
        public void setValue(SSAccountPlan iAccountPlan, Object iValue) {
            iAccountPlan.setName((String)iValue);
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


    /**
    * Namn
    */
   public static SSTableColumn<SSAccountPlan> COLUMN_TYPE = new SSTableColumn<SSAccountPlan>(SSBundle.getBundle().getString("accountplantable.column.2")) {
       @Override
       public Object getValue(SSAccountPlan iAccountPlan) {
           return iAccountPlan.getType();
       }

       @Override
       public void setValue(SSAccountPlan iAccountPlan, Object iValue) {
           iAccountPlan.setType((SSAccountPlanType)iValue);
       }

       @Override
       public Class getColumnClass() {
           return SSAccountPlanType.class;
       }

       @Override
       public int getDefaultWidth() {
           return 85;
       }
   };


    /**
    * Namn
    */
   public static SSTableColumn<SSAccountPlan> COLUMN_ASSESSMENTYEAR = new SSTableColumn<SSAccountPlan>(SSBundle.getBundle().getString("accountplantable.column.3")) {
       @Override
       public Object getValue(SSAccountPlan iAccountPlan) {
           return iAccountPlan.getAssessementYear();
       }

       @Override
       public void setValue(SSAccountPlan iAccountPlan, Object iValue) {
           iAccountPlan.setAssessementYear((String)iValue);
       }

       @Override
       public Class getColumnClass() {
           return String.class;
       }

       @Override
       public int getDefaultWidth() {
           return 85;
       }
   };


}

