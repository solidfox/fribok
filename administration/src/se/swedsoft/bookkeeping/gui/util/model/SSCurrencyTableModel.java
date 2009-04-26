package se.swedsoft.bookkeeping.gui.util.model;


import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellRenderer;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSNameDescriptionDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSCurrencyDialog;
import se.swedsoft.bookkeeping.gui.util.components.SSEditableTableComboBox;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.util.List;
import java.math.BigDecimal;

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
     */
    public SSCurrencyTableModel(List<SSCurrency> iCurrencies) {
        super(iCurrencies);
    }

    /**
     * Returns the type of data in this model.
     *
     * @return The current data type.
     */
    public Class getType() {
        return SSCurrency.class;
    }


    public SSCurrencyTableModel getDropdownmodel() {
        return getDropDownModel(SSDB.getInstance().getCurrencies());
    }
    /**
     *
     * @return
     */
    public static SSCurrencyTableModel getDropDownModel(){
        return getDropDownModel(SSDB.getInstance().getCurrencies());
    }

    /**
     *
     * @return
     */
    public static SSCurrencyTableModel getDropDownModel(List<SSCurrency> iCurrencies){
        SSCurrencyTableModel iModel = new SSCurrencyTableModel(iCurrencies);

        iModel.addColumn( SSCurrencyTableModel.COLUMN_NAME );
        iModel.addColumn( SSCurrencyTableModel.COLUMN_DESCRIPTION   );
        iModel.addColumn( SSCurrencyTableModel.COLUMN_EXCHANGERATE   );

        return iModel;
    }


    /**
     *  Name
     */
    public static SSTableColumn<SSCurrency> COLUMN_NAME = new SSTableColumn<SSCurrency>(SSBundle.getBundle().getString("currencytable.column.1")) {
        public Object getValue(SSCurrency iCurrency) {
            return iCurrency.getName();
        }

        public void setValue(SSCurrency iCurrency, Object iValue) {
            iCurrency.setName((String)iValue);
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 100;
        }
    };


    /**
     *  Description
     */
    public static SSTableColumn<SSCurrency> COLUMN_DESCRIPTION = new SSTableColumn<SSCurrency>(SSBundle.getBundle().getString("currencytable.column.2")) {
        public Object getValue(SSCurrency iCurrency) {
            return iCurrency.getDescription();
        }

        public void setValue(SSCurrency iCurrency, Object iValue) {
            iCurrency.setDescription((String)iValue);
        }

        public Class getColumnClass() {
            return String.class;
        }

        public int getDefaultWidth() {
            return 200;
        }
    };



    /**
     *  Description
     */
    public static SSTableColumn<SSCurrency> COLUMN_EXCHANGERATE = new SSTableColumn<SSCurrency>(SSBundle.getBundle().getString("currencytable.column.3")) {
        public Object getValue(SSCurrency iCurrency) {
            return iCurrency.getExchangeRate();
        }

        public void setValue(SSCurrency iCurrency, Object iValue) {
            iCurrency.setExchangeRate((BigDecimal)iValue);
        }

        public Class getColumnClass() {
            return BigDecimal.class;
        }

        public int getDefaultWidth() {
            return 80;
        }

        /**
         * @return
         */
        public TableCellRenderer getCellRenderer() {
            return new SSBigDecimalCellRenderer(8);
        }
    };


    /**
     *
     * @param iOwner
     * @return
     */
    public static SSEditableTableComboBox.EditingFactory<SSCurrency> getEditingFactory(final JDialog iOwner){

        return new SSEditableTableComboBox.EditingFactory<SSCurrency>() {
            public SSCurrency newAction() {
                SSCurrencyDialog iDialog = new SSCurrencyDialog(iOwner);
                    if(iDialog.showDialog() == JOptionPane.OK_OPTION){
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
                iDialog.setName        ( iSelected.getName()       );
                iDialog.setDescription ( iSelected.getDescription());
                iDialog.setExchangeRate( iSelected.getExchangeRate());

                if(iDialog.showDialog() == JOptionPane.OK_OPTION){
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
