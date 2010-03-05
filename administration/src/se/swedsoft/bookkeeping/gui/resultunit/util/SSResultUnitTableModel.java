package se.swedsoft.bookkeeping.gui.resultunit.util;

import se.swedsoft.bookkeeping.data.SSNewResultUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.util.List;


public class SSResultUnitTableModel  extends SSTableModel<SSNewResultUnit> {


    /**
     * Default constructor.
     */
    public SSResultUnitTableModel() {
        super(SSDB.getInstance().getResultUnits());
    }

    /**
     * Default constructor.
     */
    public SSResultUnitTableModel(List<SSNewResultUnit> iResultUnits) {
        super(iResultUnits);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSNewResultUnit.class;
    }

    /**
     *
     * @return
     */
    public static SSResultUnitTableModel getDropDownModel(){
        SSResultUnitTableModel iModel = new SSResultUnitTableModel();


        iModel.addColumn( SSResultUnitTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSResultUnitTableModel.COLUMN_NAME   );
        iModel.addColumn( SSResultUnitTableModel.COLUMN_DESCRIPTION   );
        return iModel;
    }

    /**
     *
     * @return
     */
    public static SSResultUnitTableModel getDropDownModel(List<SSNewResultUnit> iResultUnits){
        SSResultUnitTableModel iModel = new SSResultUnitTableModel(iResultUnits);

        iModel.addColumn( SSResultUnitTableModel.COLUMN_NUMBER );
        iModel.addColumn( SSResultUnitTableModel.COLUMN_NAME   );
        iModel.addColumn( SSResultUnitTableModel.COLUMN_DESCRIPTION   );

        return iModel;
    }



    /**
     *  Nummer
     */
    public static SSTableColumn<SSNewResultUnit> COLUMN_NUMBER = new SSTableColumn<SSNewResultUnit>(SSBundle.getBundle().getString("resultunittable.column.1")) {
        @Override
        public Object getValue(SSNewResultUnit iResultUnit) {
            return iResultUnit.getNumber();
        }

        @Override
        public void setValue(SSNewResultUnit iResultUnit, Object iValue) {
            iResultUnit.setNumber((String)iValue);

        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 80;
        }
    };

    /**
     *  Nummer
     */
    public static SSTableColumn<SSNewResultUnit> COLUMN_NAME = new SSTableColumn<SSNewResultUnit>(SSBundle.getBundle().getString("resultunittable.column.2")) {
        @Override
        public Object getValue(SSNewResultUnit iResultUnit) {
            return iResultUnit.getName();
        }

        @Override
        public void setValue(SSNewResultUnit iResultUnit, Object iValue) {
            iResultUnit.setName((String)iValue);

        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 200;
        }
    };

    /**
     *  Beskrivning
     */
    public static SSTableColumn<SSNewResultUnit> COLUMN_DESCRIPTION = new SSTableColumn<SSNewResultUnit>(SSBundle.getBundle().getString("resultunittable.column.3")) {
        @Override
        public Object getValue(SSNewResultUnit iResultUnit) {
            return iResultUnit.getDescription();
        }

        @Override
        public void setValue(SSNewResultUnit iResultUnit, Object iValue) {
            iResultUnit.setDescription((String)iValue);

        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 400;
        }
    };

}


