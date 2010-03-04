package se.swedsoft.bookkeeping.gui.util.model;

import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSNameDescriptionDialog;
import se.swedsoft.bookkeeping.gui.util.components.SSEditableTableComboBox;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 12:13:59
 */
public class SSUnitTableModel  extends SSTableModel<SSUnit> {


    /**
     * Default constructor.
     */
    public SSUnitTableModel() {
        super(SSDB.getInstance().getUnits() );
    }

    /**
     * Default constructor.
     */
    public SSUnitTableModel(List<SSUnit> iPaymentTerms) {
        super(iPaymentTerms);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSUnit.class;
    }


    @Override
    public SSUnitTableModel getDropdownmodel() {
        return getDropDownModel();
    }
    /**
     *
     * @return
     */
    public static SSUnitTableModel getDropDownModel(){
        SSUnitTableModel iModel = new SSUnitTableModel();

        iModel.addColumn( SSUnitTableModel.COLUMN_NAME );
        iModel.addColumn( SSUnitTableModel.COLUMN_DESCRIPTION   );

        return iModel;

    }



    /**
     *  Name
     */
    public static SSTableColumn<SSUnit> COLUMN_NAME = new SSTableColumn<SSUnit>(SSBundle.getBundle().getString("currencytable.column.1")) {
        @Override
        public Object getValue(SSUnit iCurrency) {
            return iCurrency.getName();
        }

        @Override
        public void setValue(SSUnit iCurrency, Object iValue) {
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
    public static SSTableColumn<SSUnit> COLUMN_DESCRIPTION = new SSTableColumn<SSUnit>(SSBundle.getBundle().getString("currencytable.column.2")) {
        @Override
        public Object getValue(SSUnit iCurrency) {
            return iCurrency.getDescription();
        }

        @Override
        public void setValue(SSUnit iCurrency, Object iValue) {
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
     *
     * @param iOwner
     * @return
     */
    public static SSEditableTableComboBox.EditingFactory<SSUnit> getEditingFactory(final JDialog iOwner){
        return new SSEditableTableComboBox.EditingFactory<SSUnit>() {
            public SSUnit newAction() {
                SSNameDescriptionDialog iDialog = new SSNameDescriptionDialog(iOwner, SSBundle.getBundle().getString("unittable.title"));

                if(iDialog.showDialog() == JOptionPane.OK_OPTION){
                    SSUnit iUnit = new SSUnit();

                    iUnit.setName(iDialog.getName());
                    iUnit.setDescription(iDialog.getDescription());

                    SSDB.getInstance().addUnit(iUnit);
                    return iUnit;
                }
                return null;

            }

            public void editAction(SSUnit iSelected) {
                SSNameDescriptionDialog iDialog = new SSNameDescriptionDialog(iOwner, SSBundle.getBundle().getString("unittable.title"));
                iDialog.setName       ( iSelected.getName()       );
                iDialog.setDescription( iSelected.getDescription());
                if(iDialog.showDialog() == JOptionPane.OK_OPTION){
                    iSelected.setName(iDialog.getName());
                    iSelected.setDescription(iDialog.getDescription());
                    SSDB.getInstance().updateUnit(iSelected);
                }
            }

            public void deleteAction(SSUnit iSelected) {
                SSDB.getInstance().deleteUnit(iSelected);
            }
        } ;
    }



}
