/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.util.table.editors;

import javax.swing.table.DefaultTableCellRenderer;
import java.text.NumberFormat;


/**
 */
public class SSBigDecimalCellRenderer extends DefaultTableCellRenderer {

    /**
     *
     */
    private int iFractionDigits;

    private boolean iShowNullValues;

    /**
     *
     * @param pFractionDigits
     */
    public SSBigDecimalCellRenderer(int pFractionDigits) {
        this(pFractionDigits, false);
   }
    /**
     *
     * @param pFractionDigits
     * @param pShowNullValues
     */
    public SSBigDecimalCellRenderer(int pFractionDigits, boolean pShowNullValues) {
        iFractionDigits = pFractionDigits;
        iShowNullValues = pShowNullValues;

        setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
    }

    /**
     *
     * @param value
     */
    @Override
    protected void setValue(Object value) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMinimumFractionDigits(iFractionDigits);
        format.setMaximumFractionDigits(iFractionDigits);
        format.setGroupingUsed(true);

        if(value != null){
            setText( format.format(value) );
        } else{

            if(iShowNullValues){
                setText(format.format(0.00));
            } else {
                setText("");
            }
        }


    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.util.table.editors.SSBigDecimalCellRenderer");
        sb.append("{iFractionDigits=").append(iFractionDigits);
        sb.append(", iShowNullValues=").append(iShowNullValues);
        sb.append('}');
        return sb.toString();
    }
}
