package se.swedsoft.bookkeeping.gui.accountingyear.panel;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.gui.accountingyear.util.SSStartingAmountTableModel;
import se.swedsoft.bookkeeping.gui.util.components.SSCurrencyTextField;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellEditor;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellRenderer;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date: 2006-feb-15
 * Time: 15:59:52
 */
public class SSStartingAmountPanel {

    private JPanel iPanel;


    private SSTable iTable;


    private Map<SSAccount, BigDecimal> iInBalance;

    private SSDefaultTableModel<SSAccount> iModel;

    private SSCurrencyTextField iSumField;


    public SSStartingAmountPanel(){
        iInBalance = new HashMap<SSAccount, BigDecimal>();

        iModel = new SSStartingAmountTableModel(iInBalance);

        iTable.setModel(iModel);

        // Set some basic table widths.
        iTable.getColumnModel().getColumn(0).setPreferredWidth(55);
        iTable.getColumnModel().getColumn(0).setMaxWidth(70);
        iTable.getColumnModel().getColumn(0).setMinWidth(50);

        iTable.getColumnModel().getColumn(1).setPreferredWidth(250);

        iTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        iTable.getColumnModel().getColumn(2).setMinWidth(60);
        //iTable.getColumnModel().getColumn(2).setMaxWidth(150);

        iTable.setDefaultEditor  (BigDecimal.class, new SSBigDecimalCellEditor(2));
        iTable.setDefaultRenderer(BigDecimal.class, new SSBigDecimalCellRenderer(2));

        // Update the sum if a change occurs in the second column.
        iModel.addTableModelListener( new TableModelListener(){
            public void tableChanged(TableModelEvent e) {
                if( e.getColumn() == 2){
                    updateSumField();
                }
            }
        });


    }


    /**
     *
     * @param pInBalance
     * @param pBalanceAccounts
     */
    public void setInBalance( Map<SSAccount, BigDecimal> pInBalance, List<SSAccount> pBalanceAccounts){
        iInBalance.clear();

        // Copy all ammounts
        for(Map.Entry<SSAccount, BigDecimal> ssAccountBigDecimalEntry : pInBalance.entrySet()){
            iInBalance.put(ssAccountBigDecimalEntry.getKey(), new BigDecimal(ssAccountBigDecimalEntry.getValue().doubleValue() ) );
        }

        iModel.setObjects( pBalanceAccounts );
        updateSumField();
    }

    /**
     *
     * @param pInBalance
     */
    public void setInBalance( Map<SSAccount, BigDecimal> pInBalance){
        iInBalance.clear();

        // Copy all ammounts
        for(Map.Entry<SSAccount, BigDecimal> ssAccountBigDecimalEntry : pInBalance.entrySet()){
            iInBalance.put(ssAccountBigDecimalEntry.getKey(), new BigDecimal(ssAccountBigDecimalEntry.getValue().doubleValue() ) );
        }
        updateSumField();
    }

    /**
     *
     * @return
     */
    public Map<SSAccount, BigDecimal> getInBalance( ){
        Map<SSAccount, BigDecimal> iResult = new HashMap<SSAccount, BigDecimal>();

        for(Map.Entry<SSAccount, BigDecimal> ssAccountBigDecimalEntry : iInBalance.entrySet()){
            BigDecimal iValue = ssAccountBigDecimalEntry.getValue();
            if((iValue != null ) && (iValue.signum() != 0)){
                iResult.put(ssAccountBigDecimalEntry.getKey(), iValue);
            }
        }


        return iResult;
    }


    /**
     *
     */
    private void updateSumField(){
        BigDecimal iSum = new BigDecimal(0);

        // Copy all ammounts
        for(Map.Entry<SSAccount, BigDecimal> ssAccountBigDecimalEntry : iInBalance.entrySet()){
            BigDecimal iVal = ssAccountBigDecimalEntry.getValue();
            if(iVal == null) {
                iVal = new BigDecimal(0);
                iInBalance.put(ssAccountBigDecimalEntry.getKey(), iVal);
            }
            iSum = iSum.add(  iVal  );
        }
        iSumField.setValue(iSum);

        if (iSum.signum() == 0) {
            iSumField.setForeground(Color.BLACK);
        } else {
            iSumField.setForeground(Color.RED);
        }
    }





    /**
     *
     * @return
     */
    public JPanel getPanel(){
        return iPanel;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.accountingyear.panel.SSStartingAmountPanel");
        sb.append("{iInBalance=").append(iInBalance);
        sb.append(", iModel=").append(iModel);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iSumField=").append(iSumField);
        sb.append(", iTable=").append(iTable);
        sb.append('}');
        return sb.toString();
    }
}
