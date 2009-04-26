package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.project.util.SSProjectTableModel;
import se.swedsoft.bookkeeping.data.SSNewProject;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 11:37:41
 */
public class SSProjectCellEditor extends SSTableComboBox.CellEditor<SSNewProject> {
    /**
     *
     */
    public SSProjectCellEditor() {
        super();

        setModel( SSProjectTableModel.getDropDownModel() );

        setSearchColumns(0);
    }




}
