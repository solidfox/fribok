package se.swedsoft.bookkeeping.gui.util.table.actions;

import se.swedsoft.bookkeeping.gui.util.table.SSTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * User: Andreas Lago
 * Date: 2006-mar-30
 * Time: 16:40:39
 */
public abstract class SSTraversalAction {

    private SSTable iTable;

    /**
     *
     * @param iTable
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


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.util.table.actions.SSTraversalAction");
        sb.append("{iTable=").append(iTable);
        sb.append('}');
        return sb.toString();
    }
}
