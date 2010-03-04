package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.SSAccount;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 13:26:29
 */
public class SSAccountCellRenderer extends DefaultTableCellRenderer {

    /**
     *
     * @param value
     */
    @Override
    public void setValue(Object value) {
        if(value instanceof SSAccount){
            SSAccount iAccount = (SSAccount) value;

            setText( "" + iAccount.getNumber()  );
        }  else {
            setText( "" );
        }
    }
}
