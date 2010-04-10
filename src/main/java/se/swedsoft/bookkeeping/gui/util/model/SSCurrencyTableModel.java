package se.swedsoft.bookkeeping.gui.util.model;


import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSEditableTableComboBox;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSCurrencyDialog;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellRenderer;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.math.BigDecimal;
import java.util.List;


/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 12:13:59
 */
public class SSCurrencyTableModel extends SSTableModel<SSCurrency> {

    /**
     * Default constructor.
     */
    public SSCurrencyTableModel() {
        super(SSDB.getInstance().getCurrencies());
    }

    /**
     * Default constructor.
     * @param iCurrencies
     */
    public SSCurrencyTableModel(List<SSCurrency> iCurrencies) {
        super(iCurrencies);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    @Override
    public Class getType() {
        return SSCurrency.class;
    }

    @Override
    public SSCurrencyTableModel getDropdownmodel() {
        return getDropDownModel(SSDB.getInstance().getCurrencies());
    }

    /**
     *
     * @return
     */
    public static SSCurrencyTableModel getDropDownModel() {
        return getDropDownModel(SSDB.getInstance().getCurrencies());
    }

    /**
     *
     * @param iCurrencies
     * @return
     */
    public static SSCurrencyTableModel getDropDownModel(List<SSCurrency> iCurrencies) {
        SSCurrencyTableModel iModel = new SSCurrencyTableModel(iCurrencies);

        iModel.addColumn(COLUMN_NAME);
        iModel.addColumn(COLUMN_DESCRIPTION);
        iModel.addColumn(COLUMN_EXCHANGERATE);

        return iModel;
    }

    /**
     *  Name
     */
    public static SSTableColumn<SSCurrency> COLUMN_NAME = new SSTableColumn<SSCurrency>(
            SSBundle.getBundle().getString("currencytable.column.1")) {
        @Override
        public Object getValue(SSCurrency iCurrency) {
            return iCurrency.getName();
        }

        @Override
        public void setValue(SSCurrency iCurrency, Object iValue) {
            iCurrency.setName((String) iValue);
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
    public static SSTableColumn<SSCurrency> COLUMN_DESCRIPTION = new SSTableColumn<SSCurrency>(
            SSBundle.getBundle().getString("currencytable.column.2")) {
        @Override
        public Object getValue(SSCurrency iCurrency) {
            return iCurrency.getDescription();
        }

        @Override
        public void setValue(SSCurrency iCurrency, Object iValue) {
            iCurrency.setDescription((String) iValue);
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
     *  Description
     */
    public static SSTableColumn<SSCurrency> COLUMN_EXCHANGERATE = new SSTableColumn<SSCurrency>(
            SSBundle.getBundle().getString("currencytable.column.3")) {
        @Override
        public Object getValue(SSCurrency iCurrency) {
            return iCurrency.getExchangeRate();
        }

        @Override
        public void setValue(SSCurrency iCurrency, Object iValue) {
            iCurrency.setExchangeRate((BigDecimal) iValue);
        }

        @Override
        public Class getColumnClass() {
            return BigDecimal.class;
        }

        @Override
        public int getDefaultWidth() {
            return 80;
        }

        /**
         * @return
         */
        @Override
        public TableCellRenderer getCellRenderer() {
            return new SSBigDecimalCellRenderer(8);
        }
    };

    /**
     *
     * @param iOwner
     * @return
     */
    public static SSEditableTableComboBox.EditingFactory<SSCurrency> getEditingFactory(final JDialog iOwner) {

        return new SSEditableTableComboBox.EditingFactory<SSCurrency>() {
            public SSCurrency newAction() {
                SSCurrencyDialog iDialog = new SSCurrencyDialog(iOwner);

                if (iDialog.showDialog() == JOptionPane.OK_OPTION) {
                    SSCurrency iCurrency = new SSCurrency();

                    iCurrency.setName(iDialog.getName());
                    iCurrency.setDescription(iDialog.getDescription());
                    iCurrency.setExchangeRate(iDialog.getExchangeRate());

                    SSDB.getInstance().addCurrency(iCurrency);

                    return iCurrency;
                }
                return null;
            }

            public void editAction(SSCurrency iSelected) {
                SSCurrencyDialog iDialog = new SSCurrencyDialog(iOwner);

                iDialog.setName(iSelected.getName());
                iDialog.setDescription(iSelected.getDescription());
                iDialog.setExchangeRate(iSelected.getExchangeRate());

                if (iDialog.showDialog() == JOptionPane.OK_OPTION) {
                    iSelected.setName(iDialog.getName());
                    iSelected.setDescription(iDialog.getDescription());
                    iSelected.setExchangeRate(iDialog.getExchangeRate());
                    SSDB.getInstance().updateCurrency(iSelected);
                }
            }

            public void deleteAction(SSCurrency iSelected) {
                SSDB.getInstance().deleteCurrency(iSelected);
            }

        };
    }

}
