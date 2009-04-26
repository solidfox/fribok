package se.swedsoft.bookkeeping.gui.util.table.model;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.math.BigDecimal;

/**
 * User: Andreas Lago
 * Date: 2006-jun-15
 * Time: 10:12:46
 */
public abstract class SSTableColumn<T> {

    private String iName;

    private SSTableModel<T> iModel;

    /**
     *
     * @param iName name of the column
     */
    public SSTableColumn(String iName) {
        this.iName = iName;
    }


    /**
     * Gets the value of the column at a specific row
     *
     * @param iObject
     * @return the value
     */
    public abstract Object getValue(T iObject);

    /**
     * Sets the value of the column at a specific row
     *
     * @param iObject the object
     * @param iValue the value
     */
    public abstract void setValue(T iObject, Object iValue);


    /**
     * Get the class the getInvoiceValue returns for this table column
     *
     * @return the class
     */
    public abstract Class<?> getColumnClass();


    /**
     * Returns the default width for this column
     *
     * @return the default width
     */
    public abstract int getDefaultWidth();


    /**
     * Returns the name of this colunn
     *
     * @return the name
     */
    public String getName() {
        return iName;
    }

    /**
     * Set the name of this column
     *
     * @param iName the name
     */
    public void setName(String iName) {
        this.iName = iName;
    }

    /**
     *
     * @return
     */
    public SSTableModel<T> getModel() {
        return iModel;
    }

    /**
     *
     * @param iModel
     */
    public void setModel(SSTableModel<T> iModel) {
        this.iModel = iModel;
    }

    /**
     *
     * @return
     */
    public TableCellEditor getCellEditor() {
        return null;
    }

    /**
     * 
     * @return
     */
    public TableCellRenderer getCellRenderer() {
        return null;
    }

    /**
     * Returns true
     *
     * @param iRow
     * @return if this column is editable at the row
     */
    public boolean isEditable(int iRow){
        return true;
    }



}
