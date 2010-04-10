package se.swedsoft.bookkeeping.gui.util.table.editors;


import javax.swing.table.DefaultTableCellRenderer;
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

        if (value != null) {
            setText(format.format(value) + '%');
        } else {
            if (iShowNullValues) {
                setText(format.format(0) + '%');
            } else {
                setText("");
            }

        }

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.util.table.editors.SSPercentCellRenderer");
        sb.append("{iFractionDigits=").append(iFractionDigits);
        sb.append(", iShowNullValues=").append(iShowNullValues);
        sb.append('}');
        return sb.toString();
    }
}
