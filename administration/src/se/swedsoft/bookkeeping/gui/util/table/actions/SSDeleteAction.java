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
public abstract class SSDeleteAction {


    private SSTable iTable;

    /**
     *
     * @param iTable
     */
    public SSDeleteAction(SSTable iTable) {
        this.iTable = iTable;

        Action iDelete = new DeleteAction();

        iTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE , 0), "DELETE_ROW");
        iTable.getActionMap().put("DELETE_ROW"     , iDelete);
    }


    /**
     *
     * @param iPosition
     * @return
     */
    protected abstract Point doDelete(Point iPosition);

    /**
     *
     */
    private class  DeleteAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            int col = iTable.getSelectedColumn();
            int row = iTable.getSelectedRow();

            if ( iTable.isEditing() ) {
                return;
            }
            Point iTraversalPoint = doDelete( new Point(col, row) );


            if(iTraversalPoint != null) iTable.changeSelection(iTraversalPoint.y, iTraversalPoint.x, false, false);
        }
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.util.table.actions.SSDeleteAction");
        sb.append("{iTable=").append(iTable);
        sb.append('}');
        return sb.toString();
    }
}
