package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.common.SSHeadingType;

import javax.swing.table.DefaultTableCellRenderer;

public class SSHeadingTypeCellRenderer extends DefaultTableCellRenderer {
    /**
     *
     */
    public SSHeadingTypeCellRenderer() {
        super();

        setValue(SSHeadingType.HEADING1);
        setValue(SSHeadingType.HEADING2);
        setValue(SSHeadingType.HEADING3);
    }
    /**
     *
     * @param value
     */
    @Override
    public void setValue(Object value) {
        if(value instanceof SSHeadingType){
            SSHeadingType iType = (SSHeadingType) value;

            setText( "" + iType.toString()  );
        }  else {
            setText( "" );
        }
    }


}
