package se.swedsoft.bookkeeping.gui.util.model;

import se.swedsoft.bookkeeping.data.common.SSPaymentTerm;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSEditableTableComboBox;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSNameDescriptionDialog;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import javax.swing.*;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 12:13:59
 */
public class SSPaymentTermTableModel extends SSTableModel<SSPaymentTerm> {


    /**
     * Default constructor.
     */
    public SSPaymentTermTableModel() {
        super(SSDB.getInstance().getPaymentTerms());
    }

    /**
     * Default constructor.
     * @param iPaymentTerms
     */
    public SSPaymentTermTableModel(List<SSPaymentTerm> iPaymentTerms) {
        super(iPaymentTerms);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSPaymentTerm.class;
    }


    @Override
    public SSPaymentTermTableModel getDropdownmodel() {
        return getDropDownModel();
    }
    /**
     *
     * @return
     */
    public static SSPaymentTermTableModel getDropDownModel(){
        SSPaymentTermTableModel iModel = new SSPaymentTermTableModel();

        iModel.addColumn( COLUMN_NAME );
        iModel.addColumn( COLUMN_DESCRIPTION   );

        return iModel;

    }



    /**
     *  Name
     */
    public static SSTableColumn<SSPaymentTerm> COLUMN_NAME = new SSTableColumn<SSPaymentTerm>(SSBundle.getBundle().getString("currencytable.column.1")) {
        @Override
        public Object getValue(SSPaymentTerm iCurrency) {
            return iCurrency.getName();
        }

        @Override
        public void setValue(SSPaymentTerm iCurrency, Object iValue) {
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
    public static SSTableColumn<SSPaymentTerm> COLUMN_DESCRIPTION = new SSTableColumn<SSPaymentTerm>(SSBundle.getBundle().getString("currencytable.column.2")) {
        @Override
        public Object getValue(SSPaymentTerm iCurrency) {
            return iCurrency.getDescription();
        }

        @Override
        public void setValue(SSPaymentTerm iCurrency, Object iValue) {
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
    public static SSEditableTableComboBox.EditingFactory<SSPaymentTerm> getEditingFactory(final JDialog iOwner){
        return new SSEditableTableComboBox.EditingFactory<SSPaymentTerm>() {
            public SSPaymentTerm newAction() {
                SSNameDescriptionDialog iDialog = new SSNameDescriptionDialog(iOwner, SSBundle.getBundle().getString("paymenttermtable.title"));

                if(iDialog.showDialog() == JOptionPane.OK_OPTION){
                    SSPaymentTerm iPaymentTerm = new SSPaymentTerm();

                    iPaymentTerm.setName(iDialog.getName());
                    iPaymentTerm.setDescription(iDialog.getDescription());

                    SSDB.getInstance().addPaymentTerm(iPaymentTerm);
                    return iPaymentTerm;
                }
                return null;
            }

            public void editAction(SSPaymentTerm iSelected) {
                SSNameDescriptionDialog iDialog = new SSNameDescriptionDialog(iOwner, SSBundle.getBundle().getString("paymenttermtable.title"));
                iDialog.setName       ( iSelected.getName()       );
                iDialog.setDescription( iSelected.getDescription());

                if(iDialog.showDialog() == JOptionPane.OK_OPTION){
                    iSelected.setName(iDialog.getName());
                    iSelected.setDescription(iDialog.getDescription());
                    SSDB.getInstance().updatePaymentTerm(iSelected);
                }
            }

            public void deleteAction(SSPaymentTerm iSelected) {
                SSDB.getInstance().deletePaymentTerm(iSelected);
            }
        };

    }
}
