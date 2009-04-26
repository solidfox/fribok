package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.common.SSHeadingType;
import se.swedsoft.bookkeeping.data.common.SSTaxCode;

import javax.swing.table.DefaultTableCellRenderer;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;

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
    public void setValue(Object value) {
        if(value instanceof SSHeadingType){
            SSHeadingType iType = (SSHeadingType) value;

            setText( "" + iType.toString()  );
        }  else {
            setText( "" );
        }
    }


}
