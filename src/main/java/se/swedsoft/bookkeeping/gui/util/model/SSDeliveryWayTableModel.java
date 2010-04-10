package se.swedsoft.bookkeeping.gui.util.model;


import se.swedsoft.bookkeeping.data.common.SSDeliveryWay;
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
public class SSDeliveryWayTableModel extends SSTableModel<SSDeliveryWay> {

    /**
     * Default constructor.
     */
    public SSDeliveryWayTableModel() {
        super(SSDB.getInstance().getDeliveryWays());
    }

    /**
     * Default constructor.
     * @param iPaymentTerms
     */
    public SSDeliveryWayTableModel(List<SSDeliveryWay> iPaymentTerms) {
        super(iPaymentTerms);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSDeliveryWay.class;
    }

    @Override
    public SSDeliveryWayTableModel getDropdownmodel() {
        return getDropDownModel();
    }

    /**
     *
     * @return
     */
    public static SSDeliveryWayTableModel getDropDownModel() {
        SSDeliveryWayTableModel iModel = new SSDeliveryWayTableModel();

        iModel.addColumn(COLUMN_NAME);
        iModel.addColumn(COLUMN_DESCRIPTION);

        return iModel;

    }

    /**
     *  Name
     */
    public static SSTableColumn<SSDeliveryWay> COLUMN_NAME = new SSTableColumn<SSDeliveryWay>(
            SSBundle.getBundle().getString("currencytable.column.1")) {
        @Override
        public Object getValue(SSDeliveryWay iCurrency) {
            return iCurrency.getName();
        }

        @Override
        public void setValue(SSDeliveryWay iCurrency, Object iValue) {}

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
    public static SSTableColumn<SSDeliveryWay> COLUMN_DESCRIPTION = new SSTableColumn<SSDeliveryWay>(
            SSBundle.getBundle().getString("currencytable.column.2")) {
        @Override
        public Object getValue(SSDeliveryWay iCurrency) {
            return iCurrency.getDescription();
        }

        @Override
        public void setValue(SSDeliveryWay iCurrency, Object iValue) {}

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
    public static SSEditableTableComboBox.EditingFactory<SSDeliveryWay> getEditingFactory(final JDialog iOwner) {
        return new SSEditableTableComboBox.EditingFactory<SSDeliveryWay>() {
            public SSDeliveryWay newAction() {
                SSNameDescriptionDialog iDialog = new SSNameDescriptionDialog(iOwner,
                        SSBundle.getBundle().getString("deliverywaytable.title"));

                if (iDialog.showDialog() == JOptionPane.OK_OPTION) {
                    SSDeliveryWay iDeliveryWay = new SSDeliveryWay();

                    iDeliveryWay.setName(iDialog.getName());
                    iDeliveryWay.setDescription(iDialog.getDescription());

                    SSDB.getInstance().addDeliveryWay(iDeliveryWay);
                    return iDeliveryWay;
                }
                return null;
            }

            public void editAction(SSDeliveryWay iSelected) {
                SSNameDescriptionDialog iDialog = new SSNameDescriptionDialog(iOwner,
                        SSBundle.getBundle().getString("deliverywaytable.title"));

                iDialog.setName(iSelected.getName());
                iDialog.setDescription(iSelected.getDescription());

                if (iDialog.showDialog() == JOptionPane.OK_OPTION) {
                    iSelected.setName(iDialog.getName());
                    iSelected.setDescription(iDialog.getDescription());
                    SSDB.getInstance().updateDeliveryWay(iSelected);
                }
            }

            public void deleteAction(SSDeliveryWay iSelected) {
                SSDB.getInstance().deleteDeliveryWay(iSelected);
            }
        };

    }
}
