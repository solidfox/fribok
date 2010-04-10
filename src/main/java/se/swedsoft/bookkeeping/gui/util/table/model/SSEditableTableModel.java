package se.swedsoft.bookkeeping.gui.util.table.model;


import java.util.List;


/**
 * User: Andreas Lago
 * Date: 2006-jul-25
 * Time: 15:26:59
 */
public abstract class SSEditableTableModel<T> extends SSTableModel<T> {

    private T iEditing;

    private static final String EMPTY_STRING = "";

    /**
     * Default constructor.
     */
    public SSEditableTableModel() {
        iEditing = newObject();
    }

    /**
     * Constructor.
     *
     * @param pObjects The data for the table model.
     */
    public SSEditableTableModel(List<T> pObjects) {
        super(pObjects);
        iEditing = newObject();
    }

    /**
     * Constructor.
     *
     * @param pObjects The data for the table model.
     */
    public SSEditableTableModel(T... pObjects) {
        super(pObjects);
        iEditing = newObject();
    }

    @Override
    public int getRowCount() {
        return super.getRowCount() + 1;
    }

    @Override
    public T getObject(int row) {

        if (row == super.getRowCount()) {
            return iEditing;
        }
        return super.getObject(row);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        super.setValueAt(aValue, rowIndex, columnIndex);

        // If we're editing the last row and the value is set add a new row
        if ((rowIndex == super.getRowCount()) && (aValue != null)
                && !EMPTY_STRING.equals(aValue)) {
            add(iEditing);

            iEditing = newObject();
        }

    }

    @Override
    public void setObjects(List<T> pObjects) {
        super.setObjects(pObjects);
        iEditing = newObject();
    }

    @Override
    public void setObjects(T... pObjects) {
        super.setObjects(pObjects);
        iEditing = newObject();
    }

    /**
     *
     * @return
     */
    public abstract T newObject();

    /**
     *
     * @return
     */
    public T getEditObject() {
        return iEditing;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.gui.util.table.model.SSEditableTableModel");
        sb.append("{EMPTY_STRING='").append(EMPTY_STRING).append('\'');
        sb.append(", iEditing=").append(iEditing);
        sb.append('}');
        return sb.toString();
    }
}
