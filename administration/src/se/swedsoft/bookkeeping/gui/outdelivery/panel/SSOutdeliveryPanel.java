package se.swedsoft.bookkeeping.gui.outdelivery.panel;

import se.swedsoft.bookkeeping.calc.math.SSProductMath;
import se.swedsoft.bookkeeping.data.SSOutdelivery;
import se.swedsoft.bookkeeping.data.SSOutdeliveryRow;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.outdelivery.util.SSOutdeliveryRowTableModel;
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
public class SSOutdeliveryPanel {

    private SSOutdelivery iOutdelivery;

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;

    private SSTable iTable;



    private JTextField iText;

    private SSDateChooser iDate;

    private JFormattedTextField iNumber;


    private SSOutdeliveryRowTableModel iModel;

    /**
     *
     * @param iDialog
     */
    public SSOutdeliveryPanel(SSDialog iDialog) {
        iTable.setColorReadOnly(true);
        iTable.setColumnSortingEnabled(false);
        iTable.setSingleSelect();

        iModel = new SSOutdeliveryRowTableModel();
        iModel.addColumn(SSOutdeliveryRowTableModel.COLUMN_PRODUCT, true);
        iModel.addColumn(SSOutdeliveryRowTableModel.COLUMN_DESCRIPTION);
        iModel.addColumn(SSOutdeliveryRowTableModel.COLUMN_CHANGE, true);
        iModel.setupTable(iTable);

        iTable.setDefaultEditor(SSProduct.class, new SSProductCellEditor( SSProductMath.getNormalProducts(), false) );

         new SSDeleteAction(iTable){
            @Override
            protected Point doDelete(Point iPosition) {
                SSOutdeliveryRow iSelected = iModel.getSelectedRow(iTable);

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
    public SSOutdelivery getoutdelivery() {
        // Text
        iOutdelivery.setText( iText.getText() );
        // Datum
        iOutdelivery.setDate( iDate.getDate() );

        return iOutdelivery;
    }

    /**
     *
     * @param iInventory
     */
    public void setOutdelivery(SSOutdelivery iInventory) {
        iOutdelivery = iInventory;

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


}
