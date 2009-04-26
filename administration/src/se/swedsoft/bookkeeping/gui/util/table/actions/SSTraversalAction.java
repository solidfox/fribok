package se.swedsoft.bookkeeping.gui.util.table.actions;

import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.SSMainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.*;

import com.lowagie.text.Table;

/**
 * User: Andreas Lago
 * Date: 2006-mar-30
 * Time: 16:40:39
 */
public abstract class SSTraversalAction {

    private SSTable iTable;

    /**
     *
     */
    public SSTraversalAction(SSTable iTable) {
        this.iTable = iTable;

        Action iTraversal = new TraversalAction();

        iTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER , 0), "ENTER_TRAVERSAL");
        iTable.getActionMap().put("ENTER_TRAVERSAL", iTraversal);
    }

    /**
     *
     * @param iPosition
     * @return
     */
    protected abstract Point doTraversal(Point iPosition);

    /**
     *
     */
    private class  TraversalAction extends AbstractAction {
        /**
         *
         * @param e
         */
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
            Point iTraversalPoint = doTraversal( new Point(col, row) );

            if(iTraversalPoint != null) iTable.changeSelection(iTraversalPoint.y, iTraversalPoint.x, false, false);
        }
    }


}
