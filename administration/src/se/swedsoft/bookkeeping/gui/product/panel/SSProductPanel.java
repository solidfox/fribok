package se.swedsoft.bookkeeping.gui.product.panel;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;
import se.swedsoft.bookkeeping.data.common.SSDefaultAccount;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSProductCellEditor;
import se.swedsoft.bookkeeping.gui.util.components.*;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSAccountTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSUnitTableModel;
import se.swedsoft.bookkeeping.gui.util.model.SSTaxCodeTableModel;
import se.swedsoft.bookkeeping.gui.util.SSSelectionListener;
import se.swedsoft.bookkeeping.gui.util.SSInputVerifier;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.product.util.SSProductRowTableModel;
import se.swedsoft.bookkeeping.gui.supplier.util.SSSupplierTableModel;
import se.swedsoft.bookkeeping.gui.project.util.SSProjectTableModel;
import se.swedsoft.bookkeeping.gui.resultunit.util.SSResultUnitTableModel;
import se.swedsoft.bookkeeping.calc.math.SSProductMath;
import se.swedsoft.bookkeeping.SSBookkeeping;
import se.swedsoft.bookkeeping.SSVersion;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Locale;
import java.text.NumberFormat;

/**
 * User: Andreas Lago
 * Date: 2006-mar-21
 * Time: 10:55:12
 */
public class SSProductPanel {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;


    private SSProduct iProduct;

    private JTextField iProductNr;

    private JTextField iDescription;

    private SSCurrencyTextField iUnitprice;

    private SSTableComboBox<SSTaxCode> iTax;

    private SSCurrencyTextField iPurchasePrice;

    private SSBigDecimalTextField iWeight;

    private SSBigDecimalTextField iVolume;

    private SSEditableTableComboBox<SSUnit> iUnit;

    private SSTableComboBox<SSAccount> iPurchaseAccount;

    private SSTableComboBox<SSAccount> iSellingAccount;

    private SSTableComboBox<SSNewProject> iProject;

    private SSTableComboBox<SSNewResultUnit> iResultUnit;

    private JCheckBox iExpired;

    private JCheckBox iStockGoods;

    private SSIntegerTextField iOrderpoint;

    private SSIntegerTextField iOrdercount;


    private JTextField iWarehouseLocation;

    private SSTableComboBox<SSSupplier> iSupplier;

    private JTextField iSupplierProductNr;



    private SSCurrencyTextField iFreight;


    private JButton iCalculateButton;

    private JTextField iSellingAccountDescription;

    private JTextField iPurchaseAccountDescription;

    private JTextField iProjectDescription;

    private JTextField iResultUnitDescription;

    private SSLocalizedTextField iDescriptions;


    private SSTable iParcel;

    private SSDefaultTableModel<SSProductRow> iParcelModel;


    private JLabel iVolumeUnit;

    private JLabel iWeightUnit;

    private JButton iDeleteParcelButton;

    private JButton iUpdateContributionButton;

    private SSCurrencyTextField iInprice;

    private SSCurrencyTextField iContribution;

    private SSBigDecimalTextField iContributionRate;

    private SSInputVerifier iInputVerifier;
    private SSBigDecimalTextField iStockPrice;


    /**
     *
     */
    public SSProductPanel(final SSDialog iOwner, boolean iEdit) {
        iProductNr.setEnabled(!iEdit);

        iParcelModel = new SSProductRowTableModel();
        iParcelModel.addDeleteAction(iParcel);

        iParcel.setModel(iParcelModel);
        iParcel.setColorReadOnly(true);

        iParcel.setDefaultEditor(SSProduct.class, new SSProductCellEditor( SSProductMath.getNormalProducts(), false) );

        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();
        if (iCompany != null) {
            iVolumeUnit.setText(iCompany.getVolumeUnit());
            iWeightUnit.setText(iCompany.getWeightUnit());
        }

        iSupplier.setModel(SSSupplierTableModel.getDropDownModel() );
        iSupplier.setSearchColumns(0);


        iOrderpoint.setValue(0);
        iOrdercount.setValue(0);

        iSellingAccount.setModel(SSAccountTableModel.getDropDownModel());
        iSellingAccount.setSearchColumns(0);
        iSellingAccount.addSelectionListener(new SSSelectionListener() {
            public void selected(SSTableSearchable selected) {
                SSAccount iSelected = iSellingAccount.getSelected();

                iSellingAccountDescription.setText(iSelected == null ? "" : iSelected.getDescription());
            }
        });
        iSellingAccount.setSelected(SSDB.getInstance().getCurrentAccountPlan().getAccount(3041), true);

        iPurchaseAccount.setModel(SSAccountTableModel.getDropDownModel());
        iPurchaseAccount.setSearchColumns(0);
        iPurchaseAccount.addSelectionListener(new SSSelectionListener() {
            public void selected(SSTableSearchable selected) {
                SSAccount iSelected = iPurchaseAccount.getSelected();

                iPurchaseAccountDescription.setText(iSelected == null ? "" : iSelected.getDescription());
            }
        });
        iPurchaseAccount.setSelected(SSDB.getInstance().getCurrentAccountPlan().getAccount(4010), true);


        iProject.setModel(SSProjectTableModel.getDropDownModel());
        iProject.setSearchColumns(0);
        iProject.addSelectionListener(new SSSelectionListener() {
            public void selected(SSTableSearchable selected) {
                SSNewProject iSelected = iProject.getSelected();
                if (iProject.getText() != null && !iProject.getText().equals("")) {
                    iProjectDescription.setText(iSelected == null ? "" : iSelected.getName());
                } else {
                    iProjectDescription.setText(null);
                }
            }
        });

        iProject.getComponent(0).addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    iProject.cancelCellEditing();
                    iProject.setSelected(null);

                    iProjectDescription.setText(null);
                }
            }
        });
        iResultUnit.setModel(SSResultUnitTableModel.getDropDownModel());
        iResultUnit.setSearchColumns(0);
        iResultUnit.addSelectionListener(new SSSelectionListener() {
            public void selected(SSTableSearchable selected) {
                SSNewResultUnit iSelected = iResultUnit.getSelected();
                if (iResultUnit.getText() != null && !iResultUnit.getText().equals("")) {
                    iResultUnitDescription.setText(iSelected == null ? "" : iSelected.getName());
                } else {
                    iResultUnitDescription.setText(null);
                }
            }
        });
        iResultUnit.getComponent(0).addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    iResultUnit.cancelCellEditing();
                    iResultUnit.setSelected(null);
                    iResultUnitDescription.setText(null);
                }
            }
        });

        iTax.setModel( SSTaxCodeTableModel.getDropDownModel() );
        iTax.setSearchColumns(0);

        iUnit.getComboBox().setModel( SSUnitTableModel.getDropDownModel() );
        iUnit.getComboBox().setSearchColumns(0);
        iUnit.setEditingFactory(SSUnitTableModel.getEditingFactory(iOwner));


        iCalculateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculateFields();
            }
        });

        iDeleteParcelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iProduct.getParcelRows().clear();

                iParcelModel.fireTableDataChanged();
            }
        });

        iUpdateContributionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getProduct();

                updateContribution();
            }

        });

        iInputVerifier = new SSInputVerifier();

        iInputVerifier.add(iProductNr);
        iInputVerifier.add(iUnitprice);
        iInputVerifier.addListener(new SSInputVerifier.SSVerifierListener() {
            public void updated(SSInputVerifier iVerifier, boolean iValid) {
                iButtonPanel.getOkButton().setEnabled(iValid);
            }
        });


        iDescriptions.addLocale( new Locale("se"), "Svenska:" );
        iDescriptions.addLocale( new Locale("en"), "Engelska:" );


        NumberFormat iFormat = NumberFormat.getNumberInstance();
        iFormat.setMinimumFractionDigits(2);
        iFormat.setMaximumFractionDigits(2);
        iFormat.setGroupingUsed(false);

        NumberFormatter iFormatter = new NumberFormatter(iFormat);
        iFormatter.setFormat(iFormat);

        iContributionRate.setFormatterFactory(new DefaultFormatterFactory(iFormatter));

        if (SSVersion.app_title.contains("JFS Fakturering")) {
            iSupplier.setEnabled(false);
        }

        addKeyListeners();

    }

    /**
     * 
     * @return
     */
    public boolean isValid() {
        return iInputVerifier.isValid();
    }

    /**
     *
     */
    private void updateContribution() {
        BigDecimal iInprice          = SSProductMath.getInprice(iProduct);
        BigDecimal iContribution     = SSProductMath.getContribution(iProduct);
        BigDecimal iContributionRate = SSProductMath.getContributionRate(iProduct);


        if(iInprice != null) {
            this.iInprice         .setValue(iInprice);
            this.iContribution    .setValue(iContribution);
            this.iContributionRate.setValue(iContributionRate);
        } else {
            this.iInprice         .setValue( new BigDecimal(0) );
            this.iContribution.    setValue( iProduct.getSellingPrice() );
            this.iContributionRate.setValue( new BigDecimal(100));
        }
    }


    /**
     * @param pProduct
     */
    public void setProduct(SSProduct pProduct) {
        iProduct = pProduct;

        iProductNr        .setText(iProduct.getNumber());
        iDescription      .setText(iProduct.getDescription());
        iUnitprice        .setValue(iProduct.getSellingPrice());
        iTax              .setSelected(iProduct.getTaxCode());
        iPurchasePrice    .setValue(iProduct.getPurchasePrice());
        iStockPrice       .setValue(iProduct.getStockPrice());
        iFreight          .setValue(iProduct.getUnitFreight());
        iWeight           .setValue(iProduct.getWeight());
        iVolume           .setValue(iProduct.getVolume());
        iUnit             .setSelected(iProduct.getUnit());
        iExpired          .setSelected(iProduct.isExpired());
        iStockGoods       .setSelected(iProduct.isStockProduct());
        iOrderpoint       .setValue(iProduct.getOrderpoint());
        iOrdercount       .setValue(iProduct.getOrdercount());
        iWarehouseLocation.setText(iProduct.getWarehouseLocation());
        iSupplierProductNr.setText(iProduct.getSupplierProductNr());
        iSupplier         .setSelected(iProduct.getSupplier(SSDB.getInstance().getSuppliers()));

        // Beskrivningar på alternativa språk
        iDescriptions.setValues( iProduct.getDescriptions() );

        SSAccountPlan iAccountPlan = SSDB.getInstance().getCurrentAccountPlan();
        // SSNewCompany     iCompany     = SSDB.getInstance().getCurrentCompany();

        //Integer iSalesAccountNumber     = iProduct.getDefaultAccount(SSDefaultAccount.Sales    );//, iCompany.getDefaultAccount(SSDefaultAccount.Sales    ));
        //Integer iPurchasesAccountNumber = iProduct.getDefaultAccount(SSDefaultAccount.Purchases);// iCompany.getDefaultAccount(SSDefaultAccount.Purchases));

        if(iAccountPlan != null){
            iSellingAccount .setSelected(iAccountPlan.getAccount(iProduct.getDefaultAccount(SSDefaultAccount.Sales    )), true);
            iPurchaseAccount.setSelected(iAccountPlan.getAccount(iProduct.getDefaultAccount(SSDefaultAccount.Purchases)), true);
        }
        if (iProduct.getProjectNr() != null) {
            for(SSNewProject pProject : SSDB.getInstance().getProjects()){
                if(pProject.getNumber().equals(iProduct.getProjectNr())){
                    iProject.setSelected(pProject);
                    iProjectDescription.setText(pProject.getName());
                }
            }
        } else {
            iProject.setSelected(null);
            iProject.setText(null);
        }
        if (iProduct.getResultUnitNr() != null) {
            for(SSNewResultUnit pResultUnit : SSDB.getInstance().getResultUnits()){
                if(pResultUnit.getNumber().equals(iProduct.getResultUnitNr())){
                    iResultUnit.setSelected(pResultUnit);
                    iResultUnitDescription.setText(pResultUnit.getName());
                }
            }
        } else {
            iResultUnit.setSelected(null);
            iResultUnit.setText(null);
        }

        SSProductRowTableModel.setupTable(iParcel, iProduct);

        iParcelModel.setObjects(iProduct.getParcelRows());

        updateContribution();
    }


    /**
     * @return
     */
    public SSProduct getProduct() {
        iProduct.setNumber(iProductNr        .getText());
        iProduct.setDescription(iDescription      .getText());
        iProduct.setSellingPrice(iUnitprice        .getValue());
        iProduct.setTaxCode(iTax              .getSelected());
        iProduct.setPurchasePrice(iPurchasePrice    .getValue());
        iProduct.setStockPrice(iStockPrice.getValue());
        iProduct.setUnitFreight(iFreight          .getValue());
        iProduct.setWeight(iWeight           .getValue());
        iProduct.setVolume(iVolume           .getValue());
        iProduct.setUnit(iUnit             .getSelected());
        iProduct.setExpired(iExpired          .isSelected());
        iProduct.setStockProduct(iStockGoods       .isSelected());
        // beställningsantal
        iProduct.setOrdercount(iOrdercount.getValue());
        // Beställningspunkt
        iProduct.setOrderpoint(iOrderpoint.getValue() );
        iProduct.setWarehouseLocation(iWarehouseLocation .getText());
        iProduct.setSupplierProductNr(iSupplierProductNr.getText());
        iProduct.setSupplier(iSupplier.getSelected());

        // Beskrivningar på alternativa språk
        iProduct.setDescriptions( iDescriptions.getValues() );

        iProduct.setDefaultAccount(SSDefaultAccount.Purchases, iPurchaseAccount.getSelected());
        iProduct.setDefaultAccount(SSDefaultAccount.Sales    , iSellingAccount .getSelected());

        if (iProject.getText() != null && !iProject.getText().equals("")) {
            iProduct.setProject(iProject.getSelected());
        } else {
            iProduct.setProject(null);
        }
        if (iResultUnit.getText() != null && !iResultUnit.getText().equals("")) {
            iProduct.setResultUnit(iResultUnit.getSelected());
        } else {
            iProduct.setResultUnit(null);
        }

        iProduct.setParcelRows(iParcelModel.getObjects());
        return iProduct;
    }


    /**
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }


    /**
     * @param pActionListener
     */

    public void addOkAction(ActionListener pActionListener) {
        iButtonPanel.addOkActionListener(pActionListener);
    }

    /**
     * @param pActionListener
     */
    public void addCancelAction(ActionListener pActionListener) {
        iButtonPanel.addCancelActionListener(pActionListener);
    }


    /**
     *
     */
    private void calculateFields() {
        BigDecimal iUnitpriceSum     = new BigDecimal(0.0);
        BigDecimal iVolumeSum        = new BigDecimal(0.0);
        BigDecimal iWeightSum        = new BigDecimal(0.0);
        BigDecimal iPurchasePriceSum = new BigDecimal(0.0);
        BigDecimal iFreightSum       = new BigDecimal(0.0);

        for (SSProductRow iRow : iProduct.getParcelRows()) {
            SSProduct iProduct  = iRow.getProduct();
            Integer   iCount    = iRow.getQuantity();

            if(iCount == null || iProduct == null ) continue;

            BigDecimal iUnitprice     = iProduct.getSellingPrice();
            BigDecimal iVolume        = iProduct.getVolume();
            BigDecimal iWeight        = iProduct.getWeight();
            BigDecimal iPurchasePrice = iProduct.getPurchasePrice();
            BigDecimal iFreightPrice  = iProduct.getUnitFreight();

            BigDecimal dCount = new BigDecimal(iCount);
            if (iUnitprice     != null) iUnitpriceSum     = iUnitpriceSum    .add(iUnitprice    .multiply(dCount));
            if (iVolume        != null) iVolumeSum        = iVolumeSum       .add(iVolume       .multiply(dCount));
            if (iWeight        != null) iWeightSum        = iWeightSum       .add(iWeight       .multiply(dCount));
            if (iPurchasePrice != null) iPurchasePriceSum = iPurchasePriceSum.add(iPurchasePrice.multiply(dCount));
            if (iFreightPrice  != null) iFreightSum       = iFreightSum      .add(iFreightPrice .multiply(dCount));

        }

        iUnitprice    .setValue(iUnitpriceSum);
        iVolume       .setValue(iVolumeSum);
        iWeight       .setValue(iWeightSum);
        iPurchasePrice.setValue(iPurchasePriceSum);
        iFreight      .setValue(iFreightSum);

    }

    public void setEditPanel() {
        iProductNr.setEditable(false);
    }

    public void addKeyListeners(){

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if(iProductNr.isEnabled())
                    iProductNr.requestFocusInWindow();
                else
                    iDescription.requestFocusInWindow();
            }
        });

        iProductNr.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iDescription.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iDescription.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iUnitprice.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iUnitprice.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iTax.getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iTax.getComponent(0).addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iPurchasePrice.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iPurchasePrice.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iFreight.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iFreight.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iUnit.getComboBox().getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iUnit.getComboBox().getComponent(0).addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iWeight.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iWeight.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iVolume.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iVolume.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iSellingAccount.getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iSellingAccount.getComponent(0).addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iPurchaseAccount.getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iPurchaseAccount.getComponent(0).addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iProject.getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iProject.getComponent(0).addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iResultUnit.getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iResultUnit.getComponent(0).addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iExpired.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iExpired.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iStockGoods.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iStockGoods.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iButtonPanel.getOkButton().requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iButtonPanel.getOkButton().addKeyListener(new KeyAdapter(){
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

        iWarehouseLocation.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iOrderpoint.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iOrderpoint.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iOrdercount.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iOrdercount.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iSupplier.getComponent(0).requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iSupplier.getComponent(0).addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iSupplierProductNr.requestFocusInWindow();
                        }
                    });
                }
            }
        });
    }
}




