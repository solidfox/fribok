package se.swedsoft.bookkeeping.gui.autodist.panel;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSAutoDist;
import se.swedsoft.bookkeeping.data.SSAutoDistRow;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.autodist.util.SSAutoDistRowTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSSelectionListener;
import se.swedsoft.bookkeeping.gui.util.components.SSBigDecimalTextField;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.model.SSAccountTableModel;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSDeleteAction;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSTraversalAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * User: Andreas Lago
 * Date: 2006-sep-26
 * Time: 11:49:51
 */
public class SSAutoDistPanel {

    private  SSAutoDist iAutoDist;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private SSTable iTable;



    private JTextField iText;

    private SSTableComboBox<SSAccount> iAccount;
    private SSAutoDistRowTableModel iModel;
    private JTextField iAccountDescription;
    private SSBigDecimalTextField iAmount;


    /**
     *
     * @param iDialog
     * @param iEdit
     */
    public SSAutoDistPanel(SSDialog iDialog, boolean iEdit) {
        iAccount.setEnabled(!iEdit);
        iTable.setColorReadOnly(true);
        iTable.setColumnSortingEnabled(false);
        iTable.setSingleSelect();
        iButtonPanel.getOkButton().setEnabled(false);
        iModel = new SSAutoDistRowTableModel();
        iModel.addColumn(SSAutoDistRowTableModel.COLUMN_ACCOUNT, true);
        iModel.addColumn(SSAutoDistRowTableModel.COLUMN_DESCRIPTION);
        iModel.addColumn(SSAutoDistRowTableModel.COLUMN_PERCENTAGE,true);
        iModel.addColumn(SSAutoDistRowTableModel.COLUMN_DEBET,true);
        iModel.addColumn(SSAutoDistRowTableModel.COLUMN_CREDIT,true);
        iModel.addColumn(SSAutoDistRowTableModel.COLUMN_PROJECT,true);
        iModel.addColumn(SSAutoDistRowTableModel.COLUMN_RESULTUNIT,true);

        iModel.setupTable(iTable);

        iTable.setSingleSelect();
        //iTable.setDefaultEditor(SSProduct.class, new SSProductCellEditor( SSProductMath.getNormalProducts(), false) );

        new SSDeleteAction(iTable){
            @Override
            protected Point doDelete(Point iPosition) {
                SSAutoDistRow iSelected = iModel.getSelectedRow(iTable);

                if(iSelected != null) {

                    SSQueryDialog dialog = new SSQueryDialog(SSMainFrame.getInstance(), SSBundle.getBundle(), "autodistframe.deleterow", iSelected.toString() );

                    if( dialog.getResponce() != JOptionPane.YES_OPTION ) return null;

                    iModel.deleteRow(iSelected );
                }
                return iPosition;
            }
        };

        new SSTraversalAction(iTable){
            @Override
            protected Point doTraversal(Point iPosition) {
                if (iPosition.x <= 6) {
                    iPosition.x = iPosition.x + 1;
                }
                if (iPosition.x == 7) {

                    iPosition.y = iPosition.y + 1;
                    iPosition.x = 0;

                    if(iPosition.y == iModel.getRowCount()) {
                        iButtonPanel.getOkButton().requestFocus();
                        return null;
                    }

                }
                return iPosition;
            }
        };

        iAccount.setModel(SSAccountTableModel.getDropDownModel());
        iAccount.setSearchColumns(0);
        iAccount.setAllowCustomValues(true);
        iAccount.addSelectionListener(new SSSelectionListener<SSAccount>(){
            public void selected(SSAccount iAccount) {
                iAccountDescription.setText(iAccount.getDescription());
                iButtonPanel.getOkButton().setEnabled(true);
            }
        });
        iAccount.addChangeListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(iAccount.getSelected() == null){
                    iButtonPanel.getOkButton().setEnabled(false);
                    iAccountDescription.setText(null);
                }
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if(iAccount.isEnabled())
                    iAccount.getComponent(0).requestFocusInWindow();
                else
                    iText.requestFocusInWindow();
            }
        });

        iAccount.getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iText.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iText.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iAmount.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iAmount.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iTable.requestFocusInWindow();
                            iTable.changeSelection(0,0,false,false);
                        }
                    });
                }
            }
        });

        iButtonPanel.getOkButton().addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iButtonPanel.getOkButton().requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iButtonPanel.getCancelButton().addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iButtonPanel.getOkButton().requestFocusInWindow();
                        }
                    });
                }
            }
        });

        //iInputVerifier = new SSInputVerifier();
        //iInputVerifier.add(iAccount);
    }

    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }

    /**
     *
     * @return
     */
    public SSAutoDist getAutoDist() {
        // Text
        iAutoDist.setDescrition( iText.getText() );
        iAutoDist.setAccount(iAccount.getSelected());
        iAutoDist.setAmount(iAmount.getValue());
        iAutoDist.setRows(iModel.getObjects());
        return iAutoDist;
    }

    public void setAutoDist(SSAutoDist iAutoDist) {
        this.iAutoDist = iAutoDist;
        iModel.setObjects(iAutoDist.getRows());

        iAccount.setSelected(iAutoDist.getAccount());
        if(iAccount.getSelected() != null){
            iAccountDescription.setText(iAccount.getSelected().getDescription());
            iButtonPanel.getOkButton().setEnabled(true);
        }
        iText.setText(iAutoDist.getDescription());
        iAmount.setValue(iAutoDist.getAmount());

    }

    /**
     *
     * @param l
     */
    public void addOkActionListener(ActionListener l) {
        iButtonPanel.addOkActionListener(l);
    }

    /**
     *
     * @param l
     */
    public void addCancelActionListener(ActionListener l) {
        iButtonPanel.addCancelActionListener(l);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.autodist.panel.SSAutoDistPanel");
        sb.append("{iAccount=").append(iAccount);
        sb.append(", iAccountDescription=").append(iAccountDescription);
        sb.append(", iAmount=").append(iAmount);
        sb.append(", iAutoDist=").append(iAutoDist);
        sb.append(", iButtonPanel=").append(iButtonPanel);
        sb.append(", iModel=").append(iModel);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iTable=").append(iTable);
        sb.append(", iText=").append(iText);
        sb.append('}');
        return sb.toString();
    }
}
