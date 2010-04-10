package se.swedsoft.bookkeeping.gui.util.table.editors;


import javax.swing.table.DefaultTableCellRenderer;
import java.text.NumberFormat;


/**
 * User: Andreas Lago
 * Date: 2006-jul-27
 * Time: 09:20:46
 */
public class SSIntegerRenderer extends DefaultTableCellRenderer {

    /**
     *
     */

    private boolean iShowNullValues;

    /**
     *
     */
    public SSIntegerRenderer() {
        this(false);
    }

    /**
     *
     * @param pShowNullValues
     */
    public SSIntegerRenderer(boolean pShowNullValues) {
        iShowNullValues = pShowNullValues;

        setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
    }

    /**
     *
     * @param value
     */
    @Override
    protected void setValue(Object value) {
        NumberFormat iFormat = NumberFormat.getNumberInstance();

        iFormat.setMinimumFractionDigits(0);
        iFormat.setMaximumFractionDigits(0);
        iFormat.setGroupingUsed(false);

        if (value != null) {
            setText(iFormat.format(value));
        } else {

            if (iShowNullValues) {
                setText(iFormat.format(0.00));
            } else {
                setText("");
            }
        }

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.util.table.editors.SSIntegerRenderer");
        sb.append("{iShowNullValues=").append(iShowNullValues);
        sb.append('}');
        return sb.toString();
    }
}
