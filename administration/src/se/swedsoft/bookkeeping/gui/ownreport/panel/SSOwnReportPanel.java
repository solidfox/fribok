/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.ownreport.panel;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.common.SSHeadingType;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.ownreport.util.SSOwnReportAccountRow;
import se.swedsoft.bookkeeping.gui.ownreport.util.SSOwnReportAccountTableModel;
import se.swedsoft.bookkeeping.gui.ownreport.util.SSOwnReportMonthlyTableModel;
import se.swedsoft.bookkeeping.gui.ownreport.util.SSOwnReportRowTableModel;
import se.swedsoft.bookkeeping.gui.project.util.SSProjectTableModel;
import se.swedsoft.bookkeeping.gui.resultunit.util.SSResultUnitTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSInputVerifier;
import se.swedsoft.bookkeeping.gui.util.SSSelectionListener;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.model.SSAccountTableModel;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSDeleteAction;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellEditor;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellRenderer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class SSOwnReportPanel {



    private JPanel iPanel;


    private SSTable iHeadingTable;
    private SSOwnReportRowTableModel iHeadingTableModel;

    private SSTable iAccountTable;
    private SSOwnReportAccountTableModel iAccountTableModel;

    private SSTable iMonthlyTable;
    private SSOwnReportMonthlyTableModel iMonthlyTableModel;

    private JTabbedPane iTabbedPane;

    private JTextField iName;

    private SSButton iAddRowButton;
    private SSTableComboBox<SSOwnReportRow> iHeadingComboBox;
    private SSTableComboBox<SSAccount> iAccountComboBox;
    private SSTableComboBox<SSNewProject> iProjectComboBox;
    private SSTableComboBox<SSNewResultUnit> iResultUnitComboBox;
    SSOwnReport iOwnReport;

    SSInputVerifier iInputVerifier;

    private SSButtonPanel iButtonPanel;

    List<SSOwnReportRow> iOwnReportRows;
    /**
     * Default constructor.
     * @param pOwnReport
     */
    public SSOwnReportPanel(SSOwnReport pOwnReport) {
        iOwnReport = pOwnReport;
        iOwnReportRows = iOwnReport.getHeadings();

        iName.setText(iOwnReport.getName());
        
        iInputVerifier = new SSInputVerifier();
        iInputVerifier.add(iName);
        iInputVerifier.addListener(new SSInputVerifier.SSVerifierListener() {
            public void updated(SSInputVerifier iVerifier, boolean iValid) {
                iButtonPanel.getOkButton().setEnabled(iValid);
            }
        });

        iHeadingTable.setColorReadOnly(true);
        iHeadingTable.setColumnSortingEnabled(false);
        iHeadingTable.setSingleSelect();

        iHeadingTableModel = new SSOwnReportRowTableModel();
        iHeadingTableModel.addColumn(SSOwnReportRowTableModel.COLUMN_TYPE, true);
        iHeadingTableModel.addColumn(SSOwnReportRowTableModel.COLUMN_HEADING, true);
        iHeadingTableModel.setObjects(iOwnReportRows);
        iHeadingTableModel.setupTable(iHeadingTable);

        iHeadingComboBox.setModel(SSOwnReportRowTableModel.getDropDownModel());
        iHeadingComboBox.setSearchColumns(0);
        iHeadingComboBox.setAllowCustomValues(false);

        iAccountComboBox.setModel(SSAccountTableModel.getDropDownModel(getAccounts()));
        iAccountComboBox.setSearchColumns(0,1);
        iAccountComboBox.setAllowCustomValues(false);

        iProjectComboBox.setModel(SSProjectTableModel.getDropDownModel());
        iProjectComboBox.setSearchColumns(0,1);
        iProjectComboBox.setAllowCustomValues(false);
        iProjectComboBox.addSelectionListener(new SSSelectionListener<SSNewProject>(){
            public void selected(SSNewProject iProject ){
                if(iProject != null){
                    iResultUnitComboBox.setSelected(null);
                }
            }
        });
        iProjectComboBox.setSelected(SSDB.getInstance().getProject(iOwnReport.getProjectNr()));
        iProjectComboBox.getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                    iProjectComboBox.setSelected(null);
                }
            }
        });

        iResultUnitComboBox.setModel(SSResultUnitTableModel.getDropDownModel());
        iResultUnitComboBox.setSearchColumns(0,1);
        iResultUnitComboBox.setAllowCustomValues(false);
        iResultUnitComboBox.addSelectionListener(new SSSelectionListener<SSNewResultUnit>(){
            public void selected(SSNewResultUnit iResultUnit ){
                if(iResultUnit != null ){
                    iProjectComboBox.setSelected(null);
                }
            }
        });
        iResultUnitComboBox.setSelected(SSDB.getInstance().getResultUnit(iOwnReport.getResultUnitNr()));
        iResultUnitComboBox.getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                    iResultUnitComboBox.setSelected(null);
                }
            }
        });

        iHeadingTable.addSelectionDependentComponent(iAddRowButton);

        iAccountTable.setColorReadOnly(true);
        iAccountTable.setColumnSortingEnabled(false);
        iAccountTable.setSingleSelect();

        iAccountTableModel = new SSOwnReportAccountTableModel();
        iAccountTableModel.addColumn(SSOwnReportAccountTableModel.COLUMN_ACCOUNT,true);
        iAccountTableModel.addColumn(SSOwnReportAccountTableModel.COLUMN_DESCRIPTION,false);
        iAccountTableModel.addColumn(SSOwnReportAccountTableModel.COLUMN_BUDGET,true);
        iAccountTableModel.setupTable(iAccountTable);
        iAccountTable.setEnabled(false);

        iMonthlyTable.setColorReadOnly(true);
        iMonthlyTable.setColumnSortingEnabled(false);
        iMonthlyTable.setSingleSelect();

        iMonthlyTableModel = new SSOwnReportMonthlyTableModel(null, iOwnReportRows);

        List<SSMonth> iMonths = SSMonth.splitYearIntoMonths(SSDB.getInstance().getCurrentYear());
        iMonthlyTableModel = new SSOwnReportMonthlyTableModel(null,iOwnReportRows);
        iMonthlyTableModel.addColumn(SSBundle.getBundle().getString("ownreport.monthtable.column.1"));
        iMonthlyTableModel.addColumn(SSBundle.getBundle().getString("ownreport.monthtable.column.2"));

        Collections.sort(iMonths, new Comparator<SSMonth>() {
            public int compare(SSMonth o1, SSMonth o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
        iMonthlyTableModel.setObjects(iMonths);
        iMonthlyTable.setModel(iMonthlyTableModel);

        iMonthlyTable.getColumnModel().getColumn(0).setMaxWidth(75);
        iMonthlyTable.getColumnModel().getColumn(1).setMaxWidth(100);
        iMonthlyTable.getColumnModel().getColumn(1).setCellRenderer(new SSBigDecimalCellRenderer  (2, true));
        iMonthlyTable.getColumnModel().getColumn(1).setCellEditor  (new SSBigDecimalCellEditor(2));


        new SSDeleteAction(iHeadingTable){
            @Override
            protected Point doDelete(Point iPosition) {
                SSOwnReportRow iSelected = iHeadingTableModel.getSelectedRow(iHeadingTable);

                if(iSelected != null) {

                    SSQueryDialog dialog = new SSQueryDialog(SSMainFrame.getInstance(), SSBundle.getBundle(), "tenderframe.deleterow", iSelected.toString() );

                    if( dialog.getResponce() != JOptionPane.YES_OPTION ) return null;

                    iHeadingTableModel.deleteRow(iSelected );
                }
                return iPosition;
            }
        };

        new SSDeleteAction(iAccountTable){
            @Override
            protected Point doDelete(Point iPosition) {
                SSOwnReportAccountRow iSelected = iAccountTableModel.getSelectedRow(iAccountTable);

                if(iSelected != null) {

                    SSQueryDialog dialog = new SSQueryDialog(SSMainFrame.getInstance(), SSBundle.getBundle(), "tenderframe.deleterow", iSelected.toString() );

                    if( dialog.getResponce() != JOptionPane.YES_OPTION ) return null;

                    iAccountTableModel.deleteRow(iSelected );
                }
                return iPosition;
            }
        };

        iAddRowButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int selected = iHeadingTable.getSelectedRow();
                List<SSOwnReportRow> iObjects = iHeadingTableModel.getObjects();
                iObjects.add(selected, new SSOwnReportRow());
                iHeadingTableModel.fireTableDataChanged();
            }
        });

        iTabbedPane.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e){
                switch (iTabbedPane.getSelectedIndex()){
                    case 0:
                        break;
                    case 1:
                        for(SSOwnReportRow iRow : iOwnReportRows){
                            if(!iHeadingComboBox.getModel().getObjects().contains(iRow) && iRow.getType() == SSHeadingType.HEADING2)
                                iHeadingComboBox.getModel().add(iRow);
                        }
                        iHeadingComboBox.getModel().fireTableDataChanged();

                        break;
                    case 2:
                        iAccountComboBox.setModel(SSAccountTableModel.getDropDownModel(getAccounts()));
                        break;
                }
            }
        });

        iHeadingComboBox.addSelectionListener(new SSSelectionListener<SSOwnReportRow>() {
            public void selected(SSOwnReportRow selected) {
                if(selected != null){
                    if(selected.getHeading() != null && selected.getHeading().length() != 0) iAccountTable.setEnabled(true);
                    else iAccountTable.setEnabled(false);

                    iAccountTableModel.setObjects(selected.getAccountRows());
                }
                else{
                    iAccountTable.setEnabled(false);
                }
            }
        });

        iAccountComboBox.addSelectionListener(new SSSelectionListener<SSAccount>(){
            public void selected(SSAccount iAccount){
                iMonthlyTableModel.setAccount(iAccount);
            }
        });
    }

    public SSOwnReport getOwnReport(){
        iOwnReport.setName(iName.getText());

        if(iProjectComboBox.getText() != null && iProjectComboBox.getText().length() != 0) {
            iOwnReport.setProjectNr(iProjectComboBox.getText());
        }
        else{
            iOwnReport.setProjectNr(null);
        }

        if(iResultUnitComboBox.getText() != null && iResultUnitComboBox.getText().length() != 0){
            iOwnReport.setResultUnitNr(iResultUnitComboBox.getText());
        }
        else{
            iOwnReport.setResultUnitNr(null);
        }

        iOwnReport.setHeadings(iOwnReportRows);

        return iOwnReport;
    }

    public boolean isValid(){
        return iName.getText().length() != 0;
    }
    /**
     * Returns the panel.
     *
     * @return The main panel.
     */
    public JPanel getPanel() {
        return iPanel;
    }

    public void addOkAction(ActionListener al){
        iButtonPanel.addOkActionListener(al);
    }

    public void addCancelAction(ActionListener al){
        iButtonPanel.addCancelActionListener(al);
    }

    public List<SSAccount> getAccounts(){
        List<SSAccount> iAccounts = new LinkedList<SSAccount>();
        for(SSOwnReportRow iRow : iOwnReportRows){
            for(SSOwnReportAccountRow iAccountRow : iRow.getAccountRows()){
                if(iAccountRow.getAccount() != null) iAccounts.add(iAccountRow.getAccount());
            }
        }
        return iAccounts;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.ownreport.panel.SSOwnReportPanel");
        sb.append("{iAccountComboBox=").append(iAccountComboBox);
        sb.append(", iAccountTable=").append(iAccountTable);
        sb.append(", iAccountTableModel=").append(iAccountTableModel);
        sb.append(", iAddRowButton=").append(iAddRowButton);
        sb.append(", iButtonPanel=").append(iButtonPanel);
        sb.append(", iHeadingComboBox=").append(iHeadingComboBox);
        sb.append(", iHeadingTable=").append(iHeadingTable);
        sb.append(", iHeadingTableModel=").append(iHeadingTableModel);
        sb.append(", iInputVerifier=").append(iInputVerifier);
        sb.append(", iMonthlyTable=").append(iMonthlyTable);
        sb.append(", iMonthlyTableModel=").append(iMonthlyTableModel);
        sb.append(", iName=").append(iName);
        sb.append(", iOwnReport=").append(iOwnReport);
        sb.append(", iOwnReportRows=").append(iOwnReportRows);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iProjectComboBox=").append(iProjectComboBox);
        sb.append(", iResultUnitComboBox=").append(iResultUnitComboBox);
        sb.append(", iTabbedPane=").append(iTabbedPane);
        sb.append('}');
        return sb.toString();
    }
}
