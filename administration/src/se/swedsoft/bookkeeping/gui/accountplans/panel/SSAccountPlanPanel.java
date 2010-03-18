package se.swedsoft.bookkeeping.gui.accountplans.panel;

import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSAccountPlan;
import se.swedsoft.bookkeeping.data.SSAccountPlanType;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.accountplans.dialog.SSImportAccountplanDialog;
import se.swedsoft.bookkeeping.gui.accountplans.util.SSAccountPlanRowTableModel;
import se.swedsoft.bookkeeping.gui.accountplans.util.SSAccountPlanTypeModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSInputVerifier;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Date: 2006-feb-13
 * Time: 13:50:23
 */
public class SSAccountPlanPanel {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    protected JTextField iName;

    protected JTextField iBase;

    protected JLabel iBaseLabel;

    protected JTextField iAssessementYear;

    protected SSTableComboBox<SSAccountPlanType> iType;

    protected JButton iImportFields;


    private SSTable iTable;

    private SSAccountPlan iAccountPlan;

    private SSAccountPlanRowTableModel iModel;


    private JButton iDeleteRowButton;


    private SSInputVerifier iInputVerifier;

    /**
     *
     * @param iMainFrame
     */
    public SSAccountPlanPanel(final SSMainFrame iMainFrame){
        iModel = new SSAccountPlanRowTableModel();
        iModel.addColumn( SSAccountPlanRowTableModel.COLUMN_ACTIVE     , true);
        iModel.addColumn( SSAccountPlanRowTableModel.COLUMN_NUMBER     , true);
        iModel.addColumn( SSAccountPlanRowTableModel.COLUMN_DESCRIPTION, true );
        iModel.addColumn( SSAccountPlanRowTableModel.COLUMN_VATCODE    , true);
        iModel.addColumn( SSAccountPlanRowTableModel.COLUMN_SRUCODE    , true);
        iModel.addColumn( SSAccountPlanRowTableModel.COLUMN_REPORTCODE , true);
        iModel.addColumn( SSAccountPlanRowTableModel.COLUMN_PROJECT    , true);
        iModel.addColumn( SSAccountPlanRowTableModel.COLUMN_RESULTUNIT , true);

        iModel.setupTable(iTable);

        iDeleteRowButton  .setIcon        ( SSIcon.getIcon("ICON_DELETEVOUCHERROW", SSIcon.IconState.NORMAL ) );
        iDeleteRowButton  .setDisabledIcon( SSIcon.getIcon("ICON_DELETEVOUCHERROW", SSIcon.IconState.DISABLED ) );
        iDeleteRowButton  .setRolloverIcon( SSIcon.getIcon("ICON_DELETEVOUCHERROW", SSIcon.IconState.HIGHLIGHTED ) );


        // The traversal action.
        Action iTraversal = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int col = iTable.getSelectedColumn();
                int row = iTable.getSelectedRow();

                if ( iTable.isEditing() ) {
                    col = iTable.getEditingColumn();
                    row = iTable.getEditingRow();

                    if( !iTable.getCellEditor(row, col).stopCellEditing() ){
                        return;
                    }
                }

                if(col >= 0 && col <= 4) {
                    iTable.changeSelection(row, col + 1, false, false);
                }
                if(col == 5){
                    iTable.changeSelection(row+1, 1, false, false);
                }
            }
        };
        // The delete action.
        Action iDelete = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if ( iTable.isEditing() )  return;

                List<SSAccount> iSelected = iModel.getSelectedRows(iTable);

                if(iSelected != null) {
                    int col = iTable.getSelectedColumn();
                    int row = iTable.getSelectedRow();

                    if(SSQueryDialog.showDialog(iMainFrame, SSBundle.getBundle(), "accountplanpanel.delete") != JOptionPane.YES_OPTION ) return;

                    iModel.delete(iSelected );

                    iTable.changeSelection(row-1, col, false, false);
                }
            }
        };

        iTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER , 0), "ENTER_TRAVERSAL");
        iTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "DELETE_ROW");

        iTable.getActionMap().put("ENTER_TRAVERSAL", iTraversal);
        iTable.getActionMap().put("DELETE_ROW"     , iDelete);


        iModel.addTableModelListener( new TableModelListener(){
            public void tableChanged(TableModelEvent e) {
                if( iTable.getSelectedColumn() == -1 )
                    iTable.setColumnSelectionInterval(1,1);
            }
        });

        iDeleteRowButton.setEnabled(false);
        iTable.addSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                iDeleteRowButton.setEnabled( iTable.getSelectedRowCount() > 0);
            }
        });

        iDeleteRowButton.addActionListener(iDelete);


        iType.setModel( SSAccountPlanTypeModel.getDropDownModel() );


        iImportFields.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSImportAccountplanDialog iDialog = new SSImportAccountplanDialog(iMainFrame);

                iDialog.setLocationRelativeTo(iMainFrame);
                iDialog.setVisible(true);

                if(iDialog.getModalResult() != JOptionPane.OK_OPTION ) return;

                SSAccountPlan iSelected = iDialog.geAccountPlan();

                if(iSelected != null) {
                    getAccountPlan();

                    if(iDialog.isSRUSelected()) iAccountPlan.importSRUCodesFrom( iSelected );
                    if(iDialog.isVATSelected()) iAccountPlan.importVATCodesFrom( iSelected );
                    if(iDialog.isRECSelected()) iAccountPlan.importReportCodesFrom( iSelected );

                    setAccountPlan(iAccountPlan);
                }
            }
        });



        iInputVerifier = new SSInputVerifier();
        iInputVerifier.add(iName);
        iInputVerifier.addListener(new SSInputVerifier.SSVerifierListener() {
            public void updated(SSInputVerifier iVerifier, boolean iValid) {
                iButtonPanel.getOkButton().setEnabled(iValid);
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                iName.requestFocusInWindow();
            }
        });

        iName.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                     SwingUtilities.invokeLater(new Runnable(){
                        public void run() {
                            iAssessementYear.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iAssessementYear.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iType.getComponent(0).requestFocusInWindow();
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
                            iButtonPanel.getCancelButton().requestFocusInWindow();
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
    }

    /**
     *
     * @return The selected row
     */
    protected SSAccount getSelectedRow(){
        int selected = iTable.getSelectedRow();

        return selected < 0 ? null : iModel.getObject(selected);
    }


    /**
     *
     * @param pAccountPlan
     */
    public void setAccountPlan(SSAccountPlan pAccountPlan) {
        iAccountPlan = pAccountPlan;

        iModel.setObjects( pAccountPlan.getAccounts() );

        iName           .setText    ( pAccountPlan.getName    () );
        iBase           .setText    ( pAccountPlan.getBaseName() );
        iType           .setSelected( pAccountPlan.getType    () );
        iAssessementYear.setText    ( pAccountPlan.getAssessementYear    () );
    }

    /**
     *
     * @return The current accountplan
     */
    public SSAccountPlan getAccountPlan() {
        iAccountPlan.setName           ( iName           .getText    () );
        iAccountPlan.setType           ( iType           .getSelected() );
        iAccountPlan.setAssessementYear( iAssessementYear.getText()  );

        Set<SSAccount> iAccounts = new HashSet<SSAccount>( iAccountPlan.getAccounts() );

        iAccountPlan.setAccounts( new LinkedList<SSAccount>(iAccounts));

        return iAccountPlan;
    }

    /**
     *
     * @return  The main panel
     */
    public JPanel getPanel() {
        return iPanel;
    }


    /**
     *
     * @param e
     */
    public void addOkAction(ActionListener e){
        iButtonPanel.addOkActionListener(e);
    }

    /**
     *
     * @param e
     */
    public void addCancelAction(ActionListener e){
        iButtonPanel.addCancelActionListener(e);
    }


    /**
     *
     * @param iShowBase
     */
    public void setShowBase(boolean iShowBase){
        iBase           .setVisible(iShowBase);
        iBaseLabel      .setVisible(iShowBase);
    }

    public boolean isValid() {
        return !iName.getText().equals("");
    }

}
