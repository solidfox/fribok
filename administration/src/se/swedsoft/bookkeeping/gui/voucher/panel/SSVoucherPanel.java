package se.swedsoft.bookkeeping.gui.voucher.panel;

import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.util.SSConfig;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSSelectionListener;
import se.swedsoft.bookkeeping.gui.util.components.SSBigDecimalTextField;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSDeleteAction;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSTraversalAction;
import se.swedsoft.bookkeeping.gui.voucher.util.SSVoucherRowTableModel;
import se.swedsoft.bookkeeping.gui.voucher.util.SSVoucherVerifier;
import se.swedsoft.bookkeeping.gui.vouchertemplate.util.SSVoucherTemplateTableModel;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Date: 2006-feb-03
 * Time: 14:01:30
 */
public class SSVoucherPanel implements TableModelListener, ListSelectionListener {

    private JPanel iPanel;

    protected SSTable iTable;

    protected JFormattedTextField iNumber;

    protected SSTableComboBox<SSVoucherTemplate> iDescription;

    protected JCheckBox iStoreAsTemplate;

    protected SSDateChooser iDate;


    protected JButton iMarkRowButton;



    protected JButton iDeleteRowButton;



    protected JFormattedTextField iDebetSum;

    protected JFormattedTextField iCreditSum;

    protected JFormattedTextField iDifference;

    protected JLabel iErrorLabel;

    protected SSButton iOkButton;

    protected SSButton iCancelButton;


    // The current voucher
    protected SSVoucher iVoucher;

    //protected SSVoucherRowTableModelOld iModel;


    protected SSVoucherRowTableModel iModel;

    protected SSVoucherVerifier iVerifier;

    private JCheckBox iReopenDialog;

    public static boolean iAccountChanged;
    public static boolean iDebetChanged;
    public static boolean iCreditChanged;
    private SSBigDecimalTextField iSaldo;
    private JTextField textField1;
    private SSBigDecimalTextField SSBigDecimalTextField1;
    private SSButton iAddAccountButton;

    /**
     *
     * @param iDialog
     */
    public SSVoucherPanel(final SSDialog  iDialog){
        iVoucher   = null;
        // iModel     =  new SSVoucherRowTableModelOld(false, false);
        iModel  = new SSVoucherRowTableModel();
        iModel.addColumn( SSVoucherRowTableModel.COLUMN_ACCOUNT    , true);
        iModel.addColumn( SSVoucherRowTableModel.COLUMN_DESCRIPTION, true);
        iModel.addColumn( SSVoucherRowTableModel.COLUMN_DEBET      , true);
        iModel.addColumn( SSVoucherRowTableModel.COLUMN_CREDIT     , true);
        iModel.addColumn( SSVoucherRowTableModel.COLUMN_PROJECT    , true);
        iModel.addColumn( SSVoucherRowTableModel.COLUMN_RESULTUNIT , true);

        iTable.setColorReadOnly(true);
        iTable.setSingleSelect();
        iTable.setColumnSortingEnabled(false);
        iTable.addSelectionListener(this);
        iTable.setFocusable(true);

        iModel.setupTable(iTable, true);
        iModel.addTableModelListener(this);


        iMarkRowButton  .setIcon        ( SSIcon.getIcon("ICON_MARKVOUCHERROW", SSIcon.IconState.NORMAL ) );
        iMarkRowButton  .setDisabledIcon( SSIcon.getIcon("ICON_MARKVOUCHERROW", SSIcon.IconState.DISABLED ) );
        iMarkRowButton  .setRolloverIcon( SSIcon.getIcon("ICON_MARKVOUCHERROW", SSIcon.IconState.HIGHLIGHTED ) );

        iDeleteRowButton  .setIcon        ( SSIcon.getIcon("ICON_DELETEVOUCHERROW", SSIcon.IconState.NORMAL ) );
        iDeleteRowButton  .setDisabledIcon( SSIcon.getIcon("ICON_DELETEVOUCHERROW", SSIcon.IconState.DISABLED ) );
        iDeleteRowButton  .setRolloverIcon( SSIcon.getIcon("ICON_DELETEVOUCHERROW", SSIcon.IconState.HIGHLIGHTED ) );

        iDescription.setModel( SSVoucherTemplateTableModel.getDropDownModel() );
        iDescription.setSearchColumns(0);
        iDescription.setAllowCustomValues( true);

        // Event for selecting a template
        iDescription.addSelectionListener(new SSSelectionListener<SSVoucherTemplate>() {
            public void selected(SSVoucherTemplate template) {
                if (template != null) {
                    // Remove empty rows.
                    //    iVoucher.trim();

                    template.addToVoucher(iVoucher);

                    iModel.fireTableDataChanged();

                    iStoreAsTemplate.setEnabled (false);
                    iStoreAsTemplate.setSelected(false);
                }
            }
        });

        iDescription.getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    iDescription.cancelCellEditing();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iTable.requestFocusInWindow();
                            iTable.changeSelection(0,0,false,false);
                        }
                    });
                }
            }
        });


        new SSTraversalAction(iTable){
            @Override
            protected Point doTraversal(Point iPosition) {

                if (iPosition.x == 0) {
                    iPosition.x = iPosition.x + 2;
                } else
                if (iPosition.x < 3) {
                    iPosition.x = iPosition.x + 1;
                } else
                if (iPosition.x == 3) {
                    SSVoucherRow iVoucherRow = iModel.getObject(iPosition.y);

                    iPosition.y = iPosition.y + 1;
                    iPosition.x = 0;

                    if(iPosition.y == iModel.getRowCount()) {
                        iOkButton.requestFocus();
                        return null;
                    } else {
                        setDifferenceToZero( iVoucherRow);
                    }
                }  else {
                    iPosition.y = iPosition.y + 1;
                    iPosition.x = 0;
                }

                return iPosition;
            }
        };

        new SSDeleteAction(iTable){
            @Override
            protected Point doDelete(Point iPosition) {
                SSVoucherRow iSelected = iModel.getSelectedRow(iTable);

                if(iSelected != null && iModel.allowDeletion(iSelected)) {

                    if( SSQueryDialog.showDialog(SSMainFrame.getInstance(), SSBundle.getBundle(),"voucherframe.deleterow", iSelected.toString() ) != JOptionPane.YES_OPTION ) return null;

                    iModel.deleteRow(iSelected );
                }
                return iPosition;
            }
        };

        iTable.addSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e){
                if(iTable.getSelectedRow() != -1){
                    SSVoucherRow iRow = iModel.getObject(iTable.getSelectedRow());
                    if(iRow.getAccount() != null){
                        List<SSVoucher> iVouchers = SSDB.getInstance().getVouchers();
                        SSNewAccountingYear iYear = SSDB.getInstance().getCurrentYear();
                        BigDecimal iInbalanceSum = iYear.getInBalance(iRow.getAccount());
                        BigDecimal iDebetSum = new BigDecimal(0.0);
                        BigDecimal iCreditSum = new BigDecimal(0.0);

                        for(SSVoucher iTempVoucher : iVouchers){
                            for(SSVoucherRow iTempRow : iTempVoucher.getRows()){
                                if(iTempRow.getAccountNr() != null){
                                    if(iTempRow.getAccountNr().equals(iRow.getAccountNr()) && !iTempRow.isCrossed()){
                                        if(iTempRow.isDebet())
                                            iDebetSum = iDebetSum.add(iTempRow.getDebet());
                                        else
                                            iCreditSum = iCreditSum.add(iTempRow.getCredit());
                                    }
                                }
                            }
                        }
                        BigDecimal iTotalSum = iInbalanceSum.add((iDebetSum.subtract(iCreditSum)));
                        iTotalSum = iTotalSum.setScale(2, RoundingMode.HALF_UP);
                        iSaldo.setValue(iTotalSum);
                    }
                    else{
                        iSaldo.setValue(new BigDecimal(0.00));
                    }
                }
            }
        });

        iDescription.addChangeListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                iVoucher.setDescription( iDescription.getText()  );
            }
        });

        iDate.addChangeListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                iVoucher.setDate( iDate.getDate()  );
            }
        });

        iDate.getEditor().getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iDescription.getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iAddAccountButton.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iOkButton.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iOkButton.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iCancelButton.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iCancelButton.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iOkButton.requestFocusInWindow();
                        }
                    });
                }
            }
        });


        // iMarkRowButton.setVisible( ! iNewVoucher   );
        iMarkRowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSVoucherRow iSelected = iModel.getSelectedRow(iTable);

                String iSignature = getSignature();

                if (iSignature == null) return;

                iSelected.setCrossed(iSignature);

                iModel.fireTableDataChanged();
            }
        });

        //     iDeleteRowButton.setVisible(  iNewVoucher   );
        iDeleteRowButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSVoucherRow iSelected = iModel.getSelectedRow(iTable);

                if(iSelected != null && iModel.allowDeletion(iSelected)) {

                    if( SSQueryDialog.showDialog(SSMainFrame.getInstance() , SSBundle.getBundle(),"voucherframe.deleterow", iSelected.toString() ) != JOptionPane.YES_OPTION ) return;

                    iModel.deleteRow(iSelected );
                }
            }
        });

        iErrorLabel.setVisible(false);
        iErrorLabel.setForeground(Color.RED);

        iVerifier = new SSVoucherVerifier(iOkButton);
        iVerifier.setOnUpdate( new SSVoucherVerifier.OnUpdate(){
            public void update(boolean valid, String Error) {
                iErrorLabel.setVisible( !valid);
                iErrorLabel.setText   (Error);
            }
        });

        iDifference .addPropertyChangeListener(iVerifier);
        iDescription.addPropertyChangeListener(iVerifier);
        iDescription.addChangeListener        (iVerifier);


        iReopenDialog.setSelected( (Boolean)SSConfig.getInstance().get("reopen_voucher_dialog" , true) );
        iReopenDialog.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                SSConfig.getInstance().set("reopen_voucher_dialog" , iReopenDialog.isSelected() );
            }
        });

        iModel.addTableModelListener(new TableModelListener(){
            public void tableChanged(TableModelEvent l) {

                int iRow = l.getFirstRow();
                SSVoucherRow iVoucherRow = iModel.getObject(iRow);
                if (iAccountChanged) {
                    iAccountChanged = false;
                    SSAutoDist iAutoDist = getAutoDistForAccount(iVoucherRow);
                    if(iAutoDist != null){
                        if(!iAutoDist.getAmount().equals(new BigDecimal(0.0))){
                            if(SSQueryDialog.showDialog(SSMainFrame.getInstance(),SSBundle.getBundle(), "voucherframe.doautodist") == JOptionPane.YES_OPTION){
                                doAutoDistributionForAmount(iAutoDist, iVoucherRow);
                            }
                        }
                    }
                }
                else if (iDebetChanged) {
                    iDebetChanged = false;
                    SSAutoDist iAutoDist = getAutoDistForAccount(iVoucherRow);
                    if(iAutoDist != null){
                        if(SSQueryDialog.showDialog(SSMainFrame.getInstance(),SSBundle.getBundle(), "voucherframe.doautodist") == JOptionPane.YES_OPTION){
                            doAutoDistributionForRows(iAutoDist, iVoucherRow.getDebet());
                        }
                    }
                }
                else if (iCreditChanged) {
                    iCreditChanged = false;
                    SSAutoDist iAutoDist = getAutoDistForAccount(iVoucherRow);
                    if(iAutoDist != null){
                        if(SSQueryDialog.showDialog(SSMainFrame.getInstance(),SSBundle.getBundle(), "voucherframe.doautodist") == JOptionPane.YES_OPTION){
                            doAutoDistributionForRows(iAutoDist, iVoucherRow.getCredit());
                        }
                    }
                }
            }
        });

    }
    /**
     *
     * @return
     */
    public boolean doReopen(){
        return iReopenDialog.isSelected();
    }

    private SSAutoDist getAutoDistForAccount(SSVoucherRow iVoucherRow) {
        if (iVoucherRow.getAccountNr() != null) {
            for (SSAutoDist iAutoDist : SSDB.getInstance().getAutoDists()) {
                if (iAutoDist.getNumber().equals(iVoucherRow.getAccountNr())) {
                    return iAutoDist;
                }
            }
        }
        return null;
    }

    private void doAutoDistributionForRows(SSAutoDist iAutoDist, BigDecimal iValue) {
        SSVoucherRow iVoucherRow;
        BigDecimal iDebet = null;
        BigDecimal iCredit = null;
        SSNewProject iProject = null;
        SSNewResultUnit iResultUnit = null;
        for (SSAutoDistRow iAutoDistRow : iAutoDist.getRows()) {
            iVoucherRow = null;
            iProject = iAutoDistRow.getProject();
            iResultUnit = iAutoDistRow.getResultUnit();
            if (iAutoDistRow.getPercentage() != null && iValue != null) {
                if (iAutoDistRow.getPercentage().doubleValue() < 0.0) {
                    BigDecimal iPercent = iAutoDistRow.getPercentage().multiply(new BigDecimal(-1.0));
                    iCredit = (iPercent.divide(new BigDecimal(100.0))).multiply(iValue);
                    iDebet = null;
                } else if (iAutoDistRow.getPercentage().doubleValue() > 0.0) {
                    iDebet = (iAutoDistRow.getPercentage().divide(new BigDecimal(100.0))).multiply(iValue);
                    iCredit = null;
                }
            }
            else if (iAutoDistRow.getDebet() != null) {
                iDebet = iAutoDistRow.getDebet();
                iCredit = null;
            }
            else if (iAutoDistRow.getCredit() != null) {
                iCredit = iAutoDistRow.getCredit();
                iDebet = null;
            }
            iVoucherRow = new SSVoucherRow(iAutoDistRow.getAccount(),iDebet,iCredit,iProject,iResultUnit);
            iVoucher.addVoucherRow(iVoucherRow);
        }
    }

    private void doAutoDistributionForAmount(SSAutoDist iAutoDist, SSVoucherRow iRow) {
        BigDecimal iValue = new BigDecimal(0.0);
        iVoucher.getRows().remove(iRow);
        if (iAutoDist.getAmount().doubleValue() < 0.0) {
            iValue = iAutoDist.getAmount().multiply(new BigDecimal(-1.0));
            iRow.setCredit(iValue);
            iRow.setDebet(null);
        } else if (iAutoDist.getAmount().doubleValue() > 0.0) {
            iValue = iAutoDist.getAmount();
            iRow.setDebet(iValue);
            iRow.setCredit(null);
        }
        iVoucher.getRows().add(iRow);

        SSVoucherRow iVoucherRow;
        BigDecimal iDebet;
        BigDecimal iCredit;
        SSNewProject iProject;
        SSNewResultUnit iResultUnit;

        java.util.List<SSVoucherRow> iRows = iVoucher.getRows();
        for (SSAutoDistRow iAutoDistRow : iAutoDist.getRows()) {
            iDebet = null;
            iCredit = null;
            iVoucherRow = null;
            iProject = iAutoDistRow.getProject();
            iResultUnit = iAutoDistRow.getResultUnit();
            if (iAutoDistRow.getPercentage() != null && iAutoDist.getAmount() != null) {
                if (iAutoDistRow.getPercentage().doubleValue() < 0.0) {
                    BigDecimal iPercent = iAutoDistRow.getPercentage().multiply(new BigDecimal(-1.0));
                    if (iAutoDist.getAmount().doubleValue() < 0.0) {
                        iCredit = (iPercent.divide(new BigDecimal(100.0))).multiply(iAutoDist.getAmount().multiply(new BigDecimal(-1.0)));
                        iDebet = null;
                    } else {
                        iCredit = (iPercent.divide(new BigDecimal(100.0))).multiply(iAutoDist.getAmount());
                        iDebet = null;
                    }
                } else if (iAutoDistRow.getPercentage().doubleValue() > 0.0) {
                    if (iAutoDist.getAmount().doubleValue() < 0.0) {
                        iDebet = (iAutoDistRow.getPercentage().divide(new BigDecimal(100.0))).multiply(iAutoDist.getAmount().multiply(new BigDecimal(-1.0)));
                        iCredit = null;
                    } else {
                        iDebet = (iAutoDistRow.getPercentage().divide(new BigDecimal(100.0))).multiply(iAutoDist.getAmount());
                        iCredit = null;
                    }
                }
            }
            else if (iAutoDistRow.getDebet() != null) {
                iDebet = iAutoDistRow.getDebet();
                iCredit = null;
            }
            else if (iAutoDistRow.getCredit() != null) {
                iCredit = iAutoDistRow.getCredit();
                iDebet = null;
            }
            iVoucherRow = new SSVoucherRow(iAutoDistRow.getAccount(),iDebet,iCredit,iProject,iResultUnit);
            iRows.add(iVoucherRow);
        }
        iVoucher.setVoucherRows(iRows);
        iModel.setObjects(iVoucher.getRows());
        //doAutoDistributionForRows(iAutoDist);
    }

    /**
     *
     * @param pVoucher
     * @param iEditing
     * @param repop
     */
    public void setVoucher(SSVoucher pVoucher, boolean iEditing,boolean repop){
        iVoucher = pVoucher;

        if(iEditing){
            if(!repop)
            {
                iModel.addColumn(SSVoucherRowTableModel.COLUMN_EDITED_DATE, true);
                iModel.addColumn(SSVoucherRowTableModel.COLUMN_EDITED_SIGNATURE, true);
            }
        } else {
            iModel.removeColumn(SSVoucherRowTableModel.COLUMN_EDITED_DATE);
            iModel.removeColumn(SSVoucherRowTableModel.COLUMN_EDITED_SIGNATURE);
        }

        iModel.setObjects(iVoucher.getRows(), iEditing);
        iModel.setupTable(iTable, true);



//    SSVoucherRowTableModelOld.setupTable(iTable, iModel);

        iDescription     .setText    ( pVoucher.getDescription() );
        iNumber          .setValue   ( pVoucher.getNumber()      );
        iDate            .setDate    ( pVoucher.getDate()        );
        iStoreAsTemplate.setSelected (false);

        iVerifier.setVoucher(iVoucher);
        iVerifier.update();
    }

    /**
     *
     * @return
     */
    public SSVoucher getVoucher(){
        return iVoucher;
    }

    /**
     *
     * @return
     */
    public boolean isValid(){
        return iVerifier.isValid();
    }


    /**
     *
     * @return The panel
     */
    public JPanel getPanel(){
        return iPanel;
    }

    /**
     *
     * @param e
     */
    public void addOkAction(ActionListener e){
        iOkButton.addActionListener(e);
    }

    /**
     *
     * @param e
     */
    public void addCancelAction(ActionListener e){
        iCancelButton.addActionListener(e);
    }

    public void addAddAccountAction(ActionListener e){
        iAddAccountButton.addActionListener(e);
    }

    /**
     *
     * @param aFlag
     */
    public void setMarkRowButtonVisible(boolean aFlag) {
        iMarkRowButton.setVisible(aFlag);


    }

    /**
     *
     * @param aFlag
     */
    public void setDeleteRowButtonVisible(boolean aFlag) {
        iDeleteRowButton.setVisible(aFlag);
    }

    /**
     *
     * @return
     */
    public boolean isStoreAsTemplate() {
        return iStoreAsTemplate.isSelected();
    }

    public SSDateChooser getDate()
    {
        return iDate;
    }


    /**
     *
     * @param iVoucherRow
     */
    private void setDifferenceToZero(SSVoucherRow iVoucherRow){
        BigDecimal iDebet = iVoucherRow.getDebet();
        BigDecimal iCredit = iVoucherRow.getCredit();
        if((iDebet == null || iDebet.equals(new BigDecimal(0.0))) && (iCredit == null || iCredit.equals(new BigDecimal(0.0)))){
            BigDecimal iDifference = getDifference();

            if (iDifference.signum() < 0) {
                iVoucherRow.setDebet(iDifference.abs());
                iVoucherRow.setCredit(null);
            } else {
                iVoucherRow.setCredit(iDifference.abs());
                iVoucherRow.setDebet(null);
            }
        }
        iModel.fireTableDataChanged();
    }

    /**
     *
     * @return The debet - credit
     */
    private BigDecimal getDifference(){
        if( iVoucher != null ){
            return SSVoucherMath.getDebetMinusCreditSum(iVoucher);
        } else {
            return new BigDecimal(0.0);
        }
    }

/**
 *
 * @return The selected row

protected SSVoucherRow getSelectedRow(){
int selected = iTable.getSelectedRow();

return selected < 0 ? null : iModel.getObject(selected);
}
 */
    /**
     *
     * @return The signature
     */
    public String getSignature(){
        return JOptionPane.showInputDialog(iPanel,
                SSBundle.getBundle().getString("voucherframe.markvoucherrow.message"),
                SSBundle.getBundle().getString("voucherframe.markvoucherrow.title"),
                JOptionPane.QUESTION_MESSAGE);
    }



    /**
     *  Deletes the selected voucher row

     private void deleteSelectedRow(){
     SSVoucherRow iSelected = getSelectedRow();

     SSQueryDialog dialog = new SSQueryDialog( SSMainFrame.getInstance(), "voucherframe.deleterow", iSelected.toString() );

     if(  dialog.getResponce() != JOptionPane.YES_OPTION ) return;

     if(! iModel.canDeleteRow(iSelected) ){
     new SSErrorDialog(SSMainFrame.getInstance(), "voucherframe.cannotdeleterow");
     } else {
     iModel.deleteRow(iSelected);
     iModel.fireTableDataChanged();
     }
     }
     */




    public void tableChanged(TableModelEvent e) {
        if( iVoucher == null ) return;

        iVerifier.update();

        BigDecimal debetSum   = SSVoucherMath.getDebetSum(iVoucher);
        BigDecimal creditSum  = SSVoucherMath.getCreditSum(iVoucher);
        BigDecimal difference = debetSum.subtract(creditSum);

        iDebetSum  .setValue( debetSum  );
        iCreditSum .setValue( creditSum  );
        iDifference.setValue( difference );


        iDifference.setForeground(difference.setScale(2, RoundingMode.HALF_UP).signum() == 0 ? Color.BLACK : Color.RED );

        if( iTable.getSelectedColumn() == 0 )
            iTable.setColumnSelectionInterval(2,2);

    }

    /**
     *
     * @param e
     */
    public void valueChanged(ListSelectionEvent e) {
        SSVoucherRow iSelected = iModel.getSelectedRow(iTable);

        if(iSelected == null){
            iMarkRowButton  .setEnabled(false);
            iDeleteRowButton.setEnabled(false);

            return;
        }

        boolean allowDeletion = iModel.allowDeletion(iSelected );
        boolean allowMarking  = iModel.allowMarking (iSelected);

        iMarkRowButton  .setEnabled(allowMarking );
        iDeleteRowButton.setEnabled(allowDeletion);

    }

    public void updateAccounts(){
        iModel.setupTable(iTable);
    }



}
