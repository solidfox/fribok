package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.SSNewResultUnit;
import se.swedsoft.bookkeeping.data.SSNewProject;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 13:26:29
 */
public class SSProjectCellRenderer extends DefaultTableCellRenderer {

    /**
     *
     * @param value
     */
    @Override
    public void setValue(Object value) {
        if(value instanceof SSNewProject){
            SSNewProject iProject = (SSNewProject) value;

            setText( "" + iProject.getNumber()  );
        }  else {
            setText( "" );
        }
    }


}
