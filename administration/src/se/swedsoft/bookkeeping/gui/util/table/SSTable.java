/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.util.table;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * This should be the deafult table for most applications in this application.
 *
 *
 */
public class SSTable extends JTable{
    /**
     *
     */
    public interface SSCustomPainter{
        void update(JTable iTable, Component c, int row, int col, boolean selected, boolean editable);
    }

    private List<ActionListener> iDblClickListeners;


    private List<JComponent> iSelectionDependentComponents;

    private SSCustomPainter iCustomPainter;

    private boolean iColumnSortingEnabled;

    private boolean iColorReadOnly;


    /**
     * Default constructor.
     */
    public SSTable() {
        iDblClickListeners            = new LinkedList<ActionListener>();
        iSelectionDependentComponents = new LinkedList<JComponent>();

        // Show the lines.
        setShowHorizontalLines(true);
        setShowVerticalLines  (true);
        setRowHeight(18);
        setGridColor( new Color(192, 192, 192));

        // Disallow the reordering of the table headers.
        getTableHeader().setReorderingAllowed(false);

        iColumnSortingEnabled = true;
        iColorReadOnly        = false;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2)  notifyDblClickListeners();
            }
        });
        addSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                updateSelectionDependentComponents();
            }

        });


    }




    /**
     * Constructs a <code>JTable</code> that is initialized with
     * <code>dm</code> as the data model, a default column model,
     * and a default selection model.
     *
     * @param dm the data model for the table
     *
     * @see #createDefaultColumnModel
     * @see #createDefaultSelectionModel
     */
    public SSTable(TableModel dm) {
        this();

        setModel(dm);
    }


    /**
     * Sets the data model for this table to <code>newModel</code> and registers
     * with it for listener notifications from the new data model.
     *
     * @param dataModel the new data source for this table
     *
     * @throws IllegalArgumentException if <code>newModel</code> is <code>null</code>
     *
     * @see #getModel
     */
    @Override
    public void setModel(TableModel dataModel) {
        if(iColumnSortingEnabled){
            SSTableSorter sorter = new SSTableSorter(dataModel);
            super.setModel(sorter);
            sorter.setTableHeader(getTableHeader());
        } else {
            super.setModel(dataModel);
        }
    }


    /**
     * Returns the index of the first selected row, -1 if no row is selected.
     *
     * @return the index of the first selected row
     */
    @Override
    public int getSelectedRow() {
        if(iColumnSortingEnabled){
            int selectedRow = super.getSelectedRow();

            return selectedRow >= 0 ?
                    ((SSTableSorter)getModel()).modelIndex(selectedRow) :
                    selectedRow;
        } else {
            return super.getSelectedRow();
        }
    }

    /**
     * Returns the indices of all selected rows.
     *
     * @return an array of integers containing the indices of all selected rows,
     *         or an empty array if no row is selected
     *
     * @see #getSelectedRow
     */
    @Override
    public int[] getSelectedRows() {
        if(iColumnSortingEnabled){
            int[] viewSelected = super.getSelectedRows();
            int[] modelSelected = new int[viewSelected.length];
            SSTableSorter sorter = (SSTableSorter)getModel();

            for (int i = 0; i < viewSelected.length; i++) {
                modelSelected[i] = sorter.modelIndex(viewSelected[i]);
            }
            // Must sort them in ascending order.
            Arrays.sort(modelSelected);

            return modelSelected;
        } else {
            return super.getSelectedRows();
        }
    }

    /**
     * Makes sure that only one row can be selected at the time. <P>
     */
    public void setSingleSelect() {
        // Only allow one row to be selected at a time.
        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Make sure that the table fills the entire parent component area. <P>
     *
     * @return A boolean.
     */
    @Override
    public boolean getScrollableTracksViewportHeight() {
        if (getParent() instanceof JViewport) {
            return getParent().getHeight() > getPreferredSize().height;
        }
        return false;
    }



    final Color GRID_COLOR             = new Color(80, 80, 80);
    //final Color CELL_READONLY          = new Color(241, 240, 227);
    final Color CELL_READONLY          = new Color(245, 245, 245);
    final Color CELL_EDITABLE          = new Color(255, 255, 255);
    final Color CELL_READONLY_SELECTED = new Color(249, 224, 137);
    final Color CELL_EDITABLE_SELECTED = new Color(251, 232, 175);

    /**
     *
     * @param renderer
     * @param row
     * @param col
     *
     * @return Component
     */
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
        Component c = super.prepareRenderer(renderer, row, col);

        boolean isCellEditable    = isCellEditable(row, col);
        boolean isCellSelected    = isCellSelected(row, col);


        if(iColorReadOnly){
         //   setGridColor(GRID_COLOR);

            if(isCellEditable){

                if(isCellSelected){
                    c.setBackground( CELL_EDITABLE_SELECTED );
                } else {
                    c.setBackground( CELL_EDITABLE);
                }

            } else {
                if(isCellSelected){
                    c.setBackground( CELL_READONLY_SELECTED );
                } else {
                    c.setBackground( CELL_READONLY);
                }
            }

        }

        if(iCustomPainter != null) iCustomPainter.update(this, c, row, col, isCellSelected, isCellEditable);

        return c;
    }


    /**
     * @param pListener
     */
    public void addDblClickListener(ActionListener pListener){
        iDblClickListeners.add(pListener);
    }

    /**
     *
     */
    private void notifyDblClickListeners(){
        for(ActionListener iListener : iDblClickListeners){
            iListener.actionPerformed(null);
        }
    }

    /**
     *
     * @param pListener
     */
    public void addSelectionListener(ListSelectionListener pListener){
        getSelectionModel().addListSelectionListener(pListener);
    }

    /**
     *
     * @return
     */
    public boolean isColumnSortingEnabled() {
        return iColumnSortingEnabled;
    }

    /**
     *
     * @param iColumnSortingEnabled
     */
    public void setColumnSortingEnabled(boolean iColumnSortingEnabled) {
        this.iColumnSortingEnabled = iColumnSortingEnabled;

        if( getModel() instanceof SSTableSorter){
            SSTableSorter iModel = (SSTableSorter)getModel();

            super.setModel( iModel.getTableModel() );
        }
    }

    /**
     *
     * @return
     */
    public boolean isColorReadOnly() {
        return iColorReadOnly;
    }

    /**
     *
     * @param iColorReadOnly
     */
    public void setColorReadOnly(boolean iColorReadOnly) {
        this.iColorReadOnly = iColorReadOnly;
    }


    /**
     *
     * @return
     */
    public SSCustomPainter getCustomPainter() {
        return iCustomPainter;
    }

    /**
     *
     * @param iCustomPainter
     */
    public void setCustomPainter(SSCustomPainter iCustomPainter) {
        this.iCustomPainter = iCustomPainter;
    }


    /**
     *
     * @param iComponent
     */
    public void addSelectionDependentComponent(JComponent iComponent) {
        iSelectionDependentComponents.add(iComponent);

        iComponent.setEnabled(getSelectedRowCount() > 0);
    }

    /**
     *
     */
    private void updateSelectionDependentComponents() {
        boolean iEnabled = getSelectedRowCount() > 0;
        for (JComponent iComponent : iSelectionDependentComponents) {
            iComponent.setEnabled(iEnabled);
        }
    }

    /**
     * Used to clean up references making sure the garbage collector
     * is able to clean up the object.
     */
    public void dispose() {
        MouseListener[] iMouseListeners = getMouseListeners();
        for (MouseListener iMouseListener : iMouseListeners) {
            removeMouseListener(iMouseListener);
        }
        DefaultListSelectionModel iSelectionModel=(DefaultListSelectionModel) getSelectionModel();
        ListSelectionListener[] iSelectionListeners = iSelectionModel.getListSelectionListeners();
        for (ListSelectionListener iSelectionListener : iSelectionListeners) {
            getSelectionModel().removeListSelectionListener(iSelectionListener);
        }
        if(iDblClickListeners!=null)
            iDblClickListeners.removeAll(iDblClickListeners);

        iDblClickListeners=null;
        if(iSelectionDependentComponents!=null)
            iSelectionDependentComponents.removeAll(iSelectionDependentComponents);

        iSelectionDependentComponents=null;
        iCustomPainter=null;
    }


}

