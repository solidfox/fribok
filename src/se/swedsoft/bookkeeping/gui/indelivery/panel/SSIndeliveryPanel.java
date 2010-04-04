package se.swedsoft.bookkeeping.gui.indelivery.panel;

import se.swedsoft.bookkeeping.calc.math.SSProductMath;
import se.swedsoft.bookkeeping.data.SSIndelivery;
import se.swedsoft.bookkeeping.data.SSIndeliveryRow;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.indelivery.util.SSIndeliveryRowTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.actions.SSDeleteAction;
import se.swedsoft.bookkeeping.gui.util.table.editors.SSProductCellEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * User: Andreas Lago
 * Date: 2006-sep-26
 * Time: 11:49:51
 */
public class SSIndeliveryPanel {

    private  SSIndelivery iIndelivery;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private SSTable iTable;



    private JTextField iText;

    private SSDateChooser iDate;

    private JFormattedTextField iNumber;


    private SSIndeliveryRowTableModel iModel;

    /**
     *
     * @param iDialog
     */
    public SSIndeliveryPanel(SSDialog iDialog) {
        iTable.setColorReadOnly(true);
        iTable.setColumnSortingEnabled(false);
        iTable.setSingleSelect();

        iModel = new SSIndeliveryRowTableModel();
        iModel.addColumn(SSIndeliveryRowTableModel.COLUMN_PRODUCT, true);
        iModel.addColumn(SSIndeliveryRowTableModel.COLUMN_DESCRIPTION);
        iModel.addColumn(SSIndeliveryRowTableModel.COLUMN_CHANGE, true);

        iModel.setupTable(iTable);

        iTable.setSingleSelect();
        iTable.setDefaultEditor(SSProduct.class, new SSProductCellEditor( SSProductMath.getNormalProducts(), false) );

        new SSDeleteAction(iTable){
            @Override
            protected Point doDelete(Point iPosition) {
                SSIndeliveryRow iSelected = iModel.getSelectedRow(iTable);

                if(iSelected != null) {

                    SSQueryDialog dialog = new SSQueryDialog(SSMainFrame.getInstance(), SSBundle.getBundle(), "tenderframe.deleterow", iSelected.toString() );

                    if( dialog.getResponce() != JOptionPane.YES_OPTION ) return null;

                    iModel.deleteRow(iSelected );
                }
                return iPosition;
            }
        };

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                iDate.getEditor().getComponent(0).requestFocusInWindow();
            }
        });

        iDate.getEditor().getComponent(0).addKeyListener(new KeyAdapter(){
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
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
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
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }

    /**
     *
     * @return
     */
    public SSIndelivery getIndelivery() {
        // Text
        iIndelivery.setText( iText.getText() );
        // Datum
        iIndelivery.setDate( iDate.getDate() );

        return iIndelivery;
    }

    /**
     *
     * @param iInventory
     */
    public void setIndelivery(SSIndelivery iInventory) {
        iIndelivery = iInventory;

        iModel.setObjects(iInventory.getRows());

        // Number
        iNumber.setValue( iInventory.getNumber() );
        // Text
        iText.setText( iInventory.getText() );
        // Datum
        iDate.setDate( iInventory.getDate());
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
        sb.append("se.swedsoft.bookkeeping.gui.indelivery.panel.SSIndeliveryPanel");
        sb.append("{iButtonPanel=").append(iButtonPanel);
        sb.append(", iDate=").append(iDate);
        sb.append(", iIndelivery=").append(iIndelivery);
        sb.append(", iModel=").append(iModel);
        sb.append(", iNumber=").append(iNumber);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iTable=").append(iTable);
        sb.append(", iText=").append(iText);
        sb.append('}');
        return sb.toString();
    }
}
