package se.swedsoft.bookkeeping.gui.util.model;

import se.swedsoft.bookkeeping.data.common.SSDeliveryWay;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.common.SSDeliveryTerm;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
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
public class SSDeliveryTermTableModel extends SSTableModel<SSDeliveryTerm> {


    /**
     * Default constructor.
     */
    public SSDeliveryTermTableModel() {
        super(SSDB.getInstance().getDeliveryTerms());
    }

    /**
     * Default constructor.
     */
    public SSDeliveryTermTableModel(List<SSDeliveryTerm> iPaymentTerms) {
        super(iPaymentTerms);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSDeliveryTerm.class;
    }


    @Override
    public SSDeliveryTermTableModel getDropdownmodel() {
        return getDropDownModel();
    }

    /**
     *
     * @return
     */
    public static SSDeliveryTermTableModel getDropDownModel(){
        SSDeliveryTermTableModel iModel = new SSDeliveryTermTableModel();

        iModel.addColumn( SSDeliveryTermTableModel.COLUMN_NAME );
        iModel.addColumn( SSDeliveryTermTableModel.COLUMN_DESCRIPTION   );

        return iModel;

    }



    /**
     *  Name
     */
    public static SSTableColumn<SSDeliveryTerm> COLUMN_NAME = new SSTableColumn<SSDeliveryTerm>(SSBundle.getBundle().getString("currencytable.column.1")) {
        @Override
        public Object getValue(SSDeliveryTerm iCurrency) {
            return iCurrency.getName();
        }

        @Override
        public void setValue(SSDeliveryTerm iCurrency, Object iValue) {
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
    public static SSTableColumn<SSDeliveryTerm> COLUMN_DESCRIPTION = new SSTableColumn<SSDeliveryTerm>(SSBundle.getBundle().getString("currencytable.column.2")) {
        @Override
        public Object getValue(SSDeliveryTerm iCurrency) {
            return iCurrency.getDescription();
        }

        @Override
        public void setValue(SSDeliveryTerm iCurrency, Object iValue) {
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
    public static SSEditableTableComboBox.EditingFactory<SSDeliveryTerm> getEditingFactory(final JDialog iOwner){
        return new SSEditableTableComboBox.EditingFactory<SSDeliveryTerm>() {
            public SSDeliveryTerm newAction() {
                SSNameDescriptionDialog iDialog = new SSNameDescriptionDialog(iOwner, SSBundle.getBundle().getString("deliverytermtable.title"));

                if(iDialog.showDialog() == JOptionPane.OK_OPTION){
                    SSDeliveryTerm iDeliveryTerm = new SSDeliveryTerm();

                    iDeliveryTerm.setName(iDialog.getName());
                    iDeliveryTerm.setDescription(iDialog.getDescription());

                    SSDB.getInstance().addDeliveryTerm(iDeliveryTerm);
                    return iDeliveryTerm;
                }
                return null;
            }

            public void editAction(SSDeliveryTerm iSelected) {
                SSNameDescriptionDialog iDialog = new SSNameDescriptionDialog(iOwner, SSBundle.getBundle().getString("deliverytermtable.title"));
                iDialog.setName       ( iSelected.getName()       );
                iDialog.setDescription( iSelected.getDescription());

                if(iDialog.showDialog() == JOptionPane.OK_OPTION){
                    iSelected.setName       ( iDialog.getName() );
                    iSelected.setDescription( iDialog.getDescription() );
                    SSDB.getInstance().updateDeliveryTerm(iSelected);
                }
            }

            public void deleteAction(SSDeliveryTerm iSelected) {
                SSDB.getInstance().deleteDeliveryTerm(iSelected);
            }
        };
    }

}
