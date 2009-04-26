package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.data.common.SSUnit;

import javax.swing.table.DefaultTableCellRenderer;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 13:26:29
 */
public class SSPercentCellRenderer extends DefaultTableCellRenderer {

    int     iFractionDigits;

    boolean iShowNullValues;

    /**
     * 
     * @param pFractionDigits
     */
    public SSPercentCellRenderer(int pFractionDigits) {
        this(pFractionDigits, false);
    }

    /**
     *
     * @param pFractionDigits
     * @param pShowNullValues
     */
    public SSPercentCellRenderer(int pFractionDigits, boolean pShowNullValues) {
        super();
        iFractionDigits = pFractionDigits;
        iShowNullValues = pShowNullValues;

        setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
    }

    /**
     *
     * @param value
     */
    protected void setValue(Object value) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumFractionDigits(iFractionDigits);
        format.setMaximumFractionDigits(iFractionDigits);
        format.setGroupingUsed(true);

        if(value != null){
            setText( format.format(value) + "%" );
        } else{
            if(iShowNullValues){
                setText( format.format(0) + "%" );
            } else {
                setText("");
            }

        }


    }
}
