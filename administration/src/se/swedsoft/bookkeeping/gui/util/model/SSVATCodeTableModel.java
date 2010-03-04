package se.swedsoft.bookkeeping.gui.util.model;

import se.swedsoft.bookkeeping.data.common.SSVATCode;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.SSBundle;

import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 12:13:59
 */
public class SSVATCodeTableModel extends SSTableModel<SSVATCode> {


    /**
     * Default constructor.
     */
    public SSVATCodeTableModel() {
        this( SSVATCode.getValues() );
    }

    /**
     * Default constructor.
     */
    public SSVATCodeTableModel(List<SSVATCode> iVATCodes) {
        super();
        add(SSVATCode.VAT_NULL);
        addAll(iVATCodes);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSVATCode.class;
    }

    /**
     *
     * @return
     */
    public static SSVATCodeTableModel getDropDownModel(){
        return SSVATCodeTableModel.getDropDownModel( SSVATCode.getValues() );
    }

    /**
     *
     * @return
     */
    public static SSVATCodeTableModel getDropDownModel(List<SSVATCode> iVATCodes){
        SSVATCodeTableModel iModel = new SSVATCodeTableModel(iVATCodes);

        iModel.addColumn( SSVATCodeTableModel.COLUMN_NAME );
        iModel.addColumn( SSVATCodeTableModel.COLUMN_DESCRIPTION   );

        return iModel;
    }



    /**
     *  Name
     */
    public static SSTableColumn<SSVATCode> COLUMN_NAME = new SSTableColumn<SSVATCode>(SSBundle.getBundle().getString("vatcodetable.column.1")) {
        @Override
        public Object getValue(SSVATCode iVATCode) {
            return iVATCode.getName();
        }

        @Override
        public void setValue(SSVATCode iVATCode, Object iValue) {
            iVATCode.setName((String)iValue);
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 100;
        }
    };


    /**
     *  Description
     */
    public static SSTableColumn<SSVATCode> COLUMN_DESCRIPTION = new SSTableColumn<SSVATCode>(SSBundle.getBundle().getString("vatcodetable.column.1")) {
        @Override
        public Object getValue(SSVATCode iVATCode) {
            return iVATCode.getDescription();
        }

        @Override
        public void setValue(SSVATCode iVATCode, Object iValue) {
            iVATCode.setDescription((String)iValue);
        }

        @Override
        public Class getColumnClass() {
            return String.class;
        }

        @Override
        public int getDefaultWidth() {
            return 350;
        }
    };



}
