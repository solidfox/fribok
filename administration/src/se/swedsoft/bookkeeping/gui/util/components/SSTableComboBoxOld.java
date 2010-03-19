package se.swedsoft.bookkeeping.gui.util.components;

import se.swedsoft.bookkeeping.gui.util.SSSelectionListener;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 2006-jan-30
 * Time: 10:59:12
 */
public class SSTableComboBoxOld<T extends SSTableSearchable> extends JPanel {

    // Our textfield
    protected SSTextField iTextField;

    // Dropdown button
    protected JButton    iDropdownButton;

    // Popup menu
    protected JPopupMenu iPopup;

    // The table in the popup menu
    protected SSTable    iTable;

    // The table model for the dropdown
    protected SSDefaultTableModel<T> iModel;

    // The original objects
    private List<T> iObjects;

    // The columns to search
    private int[] iSearchColumns;

    // Allow to select values not in the dropdown
    private boolean iAllowCustomValues;

    // The selectef value
    private T       iSelected;


    private List<SSSelectionListener> iSelectionListeners;
    // Our current editor
    protected CellEditor iEditor;



    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public SSTableComboBoxOld() {
        iSelectionListeners = new LinkedList<SSSelectionListener>();
        iSelected           = null;
        iSearchColumns      = new int[] {};
        iAllowCustomValues  = false;
        iModel              = createDefaultModel();

        iTextField = new SSTextField();

        iDropdownButton = new SSButton( "ICON_DROPDOWN16" );
        iDropdownButton.setPreferredSize(new Dimension(20, 20));
        iDropdownButton.setMaximumSize  (new Dimension(20, 20));
        iDropdownButton.setMinimumSize  (new Dimension(20, 20));

        createLayout();

        // Create the popup here.
        iTable = new SSTable();
        iTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

        iPopup = new JPopupMenu();
        iPopup.add(new JScrollPane(iTable));
        iPopup.setPreferredSize( new Dimension(350,200) );
        iPopup.setLightWeightPopupEnabled(true);

        // Listener for pressing the button
        iDropdownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startEdit();
                searchTable();
            }
        });

        // Select the null value
        Action iSelectNull = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                setSelected(null);

                notifySelectionListeners(iSelected);

                if(iEditor != null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iEditor.stopCellEditing();
                            iEditor.transferFocusToTable();
                        }
                    });
                }
                stopEdit();
            }
        };

        // Select an item and close.
        final Action iSelectAndClose = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                int selected = iTable.getSelectedRow();

                if (selected >= 0 && iModel.getRowCount() > selected) {
                    T value = iModel.getObject( selected );

                    setSelected(value);

                    notifySelectionListeners(iSelected);
                }

                if(iEditor != null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iEditor.stopCellEditing();
                            iEditor.transferFocusToTable();
                        }
                    });
                }
                stopEdit();
            }
        };
        // The action for transferring the focus to the search table.
        Action iFocusTable =  new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                iTable.requestFocus();
            }
        };
        // Listener for textfield
        iTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Dont do anything if disabled
                if(!isEnabled()) return;

                // Dont process enter or escape keys
                if(e.getKeyCode() == KeyEvent.VK_ENTER  || e.getKeyCode() ==  KeyEvent.VK_ESCAPE) return;

                // Search the model after the text
                if ( searchTable() ) {

                    // The text matches one item, select the fist one
                    if(  iModel != null && iModel.getRowCount() > 0){
                        iSelected =  iModel.getObject(0);

                        iTable.setRowSelectionInterval(0, 0);
                    } else {
                        // Nothing to select
                        iSelected = null;
                    }

                } else {
                    // The text doesnt match the selected
                    iSelected = null;
                }
                // If we dont allow custom values color the textfield according to the selected value
                if(! iAllowCustomValues){
                    iTextField.setForeground(iSelected == null ? Color.RED : Color.BLACK);
                }

                // Show the dropdown
                startEdit();
                iTextField.requestFocus();
            }
        });

        iTable.addDblClickListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iSelectAndClose.actionPerformed(null);
            }
        });

        iTextField.getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0                        ), "SELECT_NULL");

        iTextField.getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0                         ), "SELECT_ROW_AND_CLOSE_DROP_DOWN");
        iTextField.getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP   , 0                         ), "FOCUS_DROP_DOWN_TABLE");
        iTextField.getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN , 0                         ), "FOCUS_DROP_DOWN_TABLE");

        iTable    .getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0                         ), "SELECT_ROW_AND_CLOSE_DROP_DOWN");
        iTable    .getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB  , 0                         ), "SELECT_ROW_AND_CLOSE_DROP_DOWN");
        iTable    .getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB  , KeyEvent.SHIFT_DOWN_MASK  ), "SELECT_ROW_AND_CLOSE_DROP_DOWN");


        iTextField.getActionMap().put("SELECT_NULL"                   , iSelectNull);
        iTextField.getActionMap().put("FOCUS_DROP_DOWN_TABLE"         , iFocusTable);
        iTextField.getActionMap().put("SELECT_ROW_AND_CLOSE_DROP_DOWN", iSelectAndClose);
        iTable    .getActionMap().put("SELECT_ROW_AND_CLOSE_DROP_DOWN", iSelectAndClose);
    }

    /**
     *
     * @return the model
     */
    private SSDefaultTableModel<T> createDefaultModel() {
        return new SSDefaultTableModel<T>() {
            @Override
            public Class getType() {
                return null;
            }
            public Object getValueAt(int rowIndex, int columnIndex) {
                return null;
            }
        };
    }

    /**
     *
     */
    protected void createLayout(){
        setLayout( new BorderLayout() );

        JPanel iPanel = new JPanel();
        iPanel.setBorder( new EmptyBorder(1,1,1,1));
        iPanel.setLayout( new BorderLayout() );
        iPanel.add(iDropdownButton     , BorderLayout.CENTER);

        add(iTextField, BorderLayout.CENTER);
        add(iPanel    , BorderLayout.EAST);
    }


    /**
     *
     * @param iModel
     */
    public void setModel(SSDefaultTableModel<T> iModel){
        this.iModel = iModel;

        iObjects = iModel.getObjects();

        iTable.setModel(iModel);
    }

    /**
     *
     * @param pSearchColumns
     */
    public void setSearchColumns(int ... pSearchColumns){
        iSearchColumns = pSearchColumns;
    }
    /**
     *
     * @param pColumnWidths
     */
    public void setColumnWidths(int ... pColumnWidths){
        int index = 0;
        for(int pColumnWidth: pColumnWidths){
            iTable.getColumnModel().getColumn(index).setPreferredWidth(pColumnWidth);
            index++;
        }
    }

    /**
     *
     * @param pPopupSize
     */
    public void setPopupSize(Dimension pPopupSize) {
        iPopup.setPreferredSize( pPopupSize );
    }

    /**
     *
     * @param pWidth
     * @param pHeight
     */
    public void setPopupSize(int pWidth, int pHeight) {
        setPopupSize( new Dimension(pWidth, pHeight) );
    }

    /**
     * Set if the user can select items not in the dropdown
     *
     * @param iAllowCustomValues
     */
    public void setAllowCustomValues(boolean iAllowCustomValues) {
        this.iAllowCustomValues = iAllowCustomValues;

        if( iAllowCustomValues){
            iTextField.setForeground(Color.BLACK);
        } else {
            iTextField.setForeground(iSelected == null ? Color.RED : Color.BLACK);
        }

    }

    /**
     *
     * @return if the user can select a not existing items
     */
    public boolean doAllowCustomValues() {
        return iAllowCustomValues;
    }


    /**
     * Set the selected value
     *
     * @param pSelected the value
     * @param pNotifyListeners if the listeners shall be notifyed
     */
    public void setSelected(T pSelected, boolean pNotifyListeners){
        iSelected = pSelected;

        if(iSelected != null){
            iTextField.setText( iSelected.toRenderString() );
        } else {
            iTextField.setText( "" );
        }

        if( iAllowCustomValues){
            iTextField.setForeground(Color.BLACK);
        } else {
            iTextField.setForeground(iSelected == null ? Color.RED : Color.BLACK);
        }

        if(pNotifyListeners) notifySelectionListeners(pSelected);
    }

    /**
     *
     * @param pSelected
     */
    public void setSelected(T pSelected){
        setSelected(pSelected, false);
    }



    /**
     *
     * @return The selected object
     */
    public T getSelected(){
        return iSelected;
    }

    /**
     *
     * @return If the combobox has a selected value
     */
    public boolean hasSelected(){
        return iSelected != null;
    }



    /**
     *
     * @return The first object
     */
    public T getFirst(){
        if(!iObjects.isEmpty()){
            return iObjects.get(0);
        } else {
            return null;
        }
    }


    /**
     *
     * @return The text
     */
    public String getText(){
        if(iAllowCustomValues){
            return iTextField.getText();
        } else {
            return iSelected == null ? null : iSelected.toRenderString();
        }

    }

    /**
     *
     * @param pText
     */
    public void setText(String pText){
        iTextField.setText(pText);

        searchTable();
    }




    /**
     *
     * @return
     */
    private boolean searchTable() {
        String iText = iTextField.getText();

        if( iSearchColumns.length == 0 ){
            return true;
        }
        List<T> iVisible = new LinkedList<T>();

        // Restore the models objects
        iModel.setObjects(iObjects);

        for (int i = 0, size = iObjects.size(); i < size; i++) {

            for (int c : iSearchColumns) {
                Object value = iModel.getValueAt(i, c);

                if(compareTo(c, iText, value)) {
                    iVisible.add(iModel.getObject(i));
                    break;
                }
            }
        }

        iModel.setObjects(iVisible);
        iModel.fireTableDataChanged();

        return !iVisible.isEmpty();
    }

    /**
     *
     * @param column
     * @param text
     * @param value
     *
     * @return boolean
     */
    protected boolean compareTo(int column, String text, Object value) {
        if(value == null || text == null) return true;

        String iString = value.toString();

        return iString.length() == 0 || iString.toLowerCase().startsWith(text.toLowerCase());
    }


    /**
     * Start the editing.
     */
    public void startEdit() {
        iPopup.show(this, 0, getHeight());

        //    iTextField.requestFocusInWindow();
    }

    /**
     * Stop the editing.
     */
    public void stopEdit() {
        iModel.setObjects(iObjects);
        iModel.fireTableDataChanged();

        iPopup.setVisible(false);
    }

    /**
     * Tells the editor to cancel editing and not accept any partially
     * edited value.
     */
    public void cancelCellEditing() {
        stopEdit();
    }


    /**
     * Invoked to process the key bindings for <code>ks</code> as the result
     * of the <code>KeyEvent</code> <code>e</code>. This obtains
     * the appropriate <code>InputMap</code>,
     * gets the binding, gets the action from the <code>ActionMap</code>,
     * and then (if the action is found and the component
     * is enabled) invokes <code>notifyAction</code> to notify the action.
     *
     * @param ks        the <code>KeyStroke</code> queried
     * @param e         the <code>KeyEvent</code>
     * @param condition one of the following values:
     *                  <ul>
     *                  <li>JComponent.WHEN_FOCUSED
     *                  <li>JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
     *                  <li>JComponent.WHEN_IN_FOCUSED_WINDOW
     *                  </ul>
     * @param pressed   true if the key is pressed
     * @return true if there was a binding to an action, and the action
     *         was enabled
     * @since 1.3
     */
    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        super.processKeyBinding(ks, e, condition, pressed);

        return iTextField.processKeyBinding(ks, e, condition, pressed);
    }




    /**
     *
     * @param pAction
     */
    public void addChangeListener(final ActionListener pAction) {
        iTextField.addKeyListener( new KeyListener(){
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
                pAction.actionPerformed(null);
            }
        });
    }


    /**
     *
     * @param pAction
     */
    public void addSelectionListener(SSSelectionListener<T> pAction) {
        iSelectionListeners.add(pAction);
    }

    /**
     *
     * @param pSelected
     */
    private void notifySelectionListeners(SSTableSearchable pSelected) {

        for (SSSelectionListener<SSTableSearchable> iSelectionListener : iSelectionListeners) {
            iSelectionListener.selected(pSelected);
        }
    }

    /**
     *
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        iTextField     .setEditable(enabled);
        iDropdownButton.setEnabled (enabled);

        super.setEnabled(enabled);
    }

    /**
     * Fetches the model associated with the editor.  This is
     * primarily for the UI to get at the minimal amount of
     * state required to be a text editor.  Subclasses will
     * return the actual type of the model which will typically
     * be something that extends Document.
     *
     * @return the model
     */
    public Document getDocument() {
        return iTextField.getDocument();
    }

    /**
     *
     * @return
     */
    public SSDefaultTableModel<T> getModel() {
        return iModel;
    }


    /**
     *
     * @param iModel
     * @return The tablecell editor
     */
    public static CellEditor<?> createAsCellEditor(SSDefaultTableModel<?> iModel){
        return new CellEditor(iModel);
    }



    /**
     *
     */
    public static class CellEditor<T extends SSTableSearchable> extends DefaultCellEditor {
        // The combobox were using
        private SSTableComboBoxOld<T> iComboBox;

        // The current table
        private JTable  iCurrentTable;
        // The current row
        private int     iCurrentRow;
        // The current colunb
        private int     iCurrentColumn;

        /**
         *
         */
        public CellEditor(){
            super(new JTextField());
            iComboBox        = new SSTableComboBoxOld<T>();
            iComboBox.iEditor = this;
        }

        /**
         *
         * @param pModel
         */
        public CellEditor(SSDefaultTableModel<T> pModel){
            this();
            setModel( pModel );
        }

        /**
         *
         * @param pModel
         */
        protected void setModel(SSDefaultTableModel<T> pModel){
            iComboBox.setModel( pModel );
        }
        /**
         * Sets an initial <code>value</code> for the editor.  This will cause
         * the editor to <code>stopEditing</code> and lose any partially
         * edited value if the editor is editing when this method is called. <p>
         * <p/>
         * Returns the component that should be added to the client's
         * <code>Component</code> hierarchy.  Once installed in the client's
         * hierarchy this component will then be able to draw and receive
         * user input.
         *
         * @param    table        the <code>JTable</code> that is asking the
         * editor to edit; can be <code>null</code>
         * @param    value        the value of the cell to be edited; it is
         * up to the specific editor to interpret
         * and draw the value.  For example, if value is
         * the string "true", it could be rendered as a
         * string or it could be rendered as a check
         * box that is checked.  <code>null</code>
         * is a isValid value
         * @param    isSelected    true if the cell is to be rendered with
         * highlighting
         * @param    row the row of the cell being edited
         * @param    column the column of the cell being edited
         * @return the component for editing
         */
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            iCurrentTable = table;
            iCurrentRow          = row;
            iCurrentColumn       = column;

            if (value != null && value instanceof SSTableSearchable ){
                iComboBox.iSelected = (T)value;
                iComboBox.iTextField.setText( ((SSTableSearchable)value).toRenderString() );

                if ( iComboBox.searchTable()) {
                    iComboBox.iTable.setRowSelectionInterval(0, 0);
                }
            } else {
                if(value == null){
                    iComboBox.iTextField.setText( "" );
                } else{
                    iComboBox.iTextField.setText( value.toString() );
                }
                iComboBox.searchTable();
            }


            // Request focus to receive input.
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    iComboBox.iTextField.requestFocus();
                }
            });

            return iComboBox;
        }


        /**
         * Returns a reference to the editor component.
         *
         * @return the editor <code>Component</code>
         */
        @Override
        public Component getComponent() {
            return  iComboBox.iTextField;
        }

        /**
         * Returns the value contained in the editor.
         *
         * @return the value contained in the editor
         */
        @Override
        public Object getCellEditorValue() {
            if(iComboBox.getSelected() == null && iComboBox.doAllowCustomValues()){
                return iComboBox.getText();
            }
            return iComboBox.getSelected();
        }

        /**
         * Asks the editor if it can start editing using <code>anEvent</code>.
         * <code>anEvent</code> is in the invoking component coordinate system.
         * The editor can not assume the Component returned by
         * <code>getCellEditorComponent</code> is installed.  This method
         * is intended for the use of client to avoid the cost of setting up
         * and installing the editor component if editing is not possible.
         * If editing can be started this method returns true.
         *
         * @param    anEvent        the event the editor should use to consider
         * whether to begin editing or not
         * @return true if editing can be started
         * @see #shouldSelectCell
         */
        @Override
        public boolean isCellEditable(EventObject anEvent) {

            if (anEvent == null) {
                return true;
            }
            if (anEvent instanceof MouseEvent) {
                MouseEvent e = (MouseEvent)anEvent;
                if (e.getClickCount() != 2) {
                    return false;
                }
            }
            JTable table = (JTable)anEvent.getSource();
            TableModel model = table.getModel();
            int row = table.getSelectedRow();
            int col = table.getSelectedColumn();

            return row == -1 || model.isCellEditable(row, col);
        }

        /**
         * Returns true if the editing cell should be selected, false otherwise.
         * Typically, the return value is true, because is most cases the editing
         * cell should be selected.  However, it is useful to return false to
         * keep the selection from changing for some types of edits.
         * eg. A table that contains a column of check boxes, the user might
         * want to be able to change those checkboxes without altering the
         * selection.  (See Netscape Communicator for just such an example)
         * Of course, it is up to the client of the editor to use the return
         * value, but it doesn't need to if it doesn't want to.
         *
         * @param    anEvent        the event the editor should use to start
         * editing
         * @return true if the editor would like the editing cell to be selected;
         * otherwise returns false
         * @see #isCellEditable
         */
        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }
        /**
         *
         * @return
         */
        @Override
        public boolean stopCellEditing() {
            iComboBox.iPopup.setVisible(false);
            return super.stopCellEditing();
        }

        /**
         *
         */
        @Override
        public void cancelCellEditing() {
            iComboBox.iPopup.setVisible(false);
            super.fireEditingCanceled();
        }

        /**
         *
         */
        private void transferFocusToTable(){
            iCurrentTable.changeSelection(iCurrentRow, iCurrentColumn, false, false);
            iCurrentTable.requestFocusInWindow();
        }

        /**
         *
         * @param pSearchColumns
         */
        public void setSearchColumns(int ... pSearchColumns){
            iComboBox.setSearchColumns(pSearchColumns);

        }

        /**
         *
         * @param pColumnWidths
         */
        public void setColumnWidths(int ... pColumnWidths){
            iComboBox.setColumnWidths(pColumnWidths);
        }

        /**
         *
         * @param pPopupSize
         */
        public void setPopupSize(Dimension pPopupSize) {
            iComboBox.setPopupSize(pPopupSize);
        }

        /**
         *
         * @param pWidth
         * @param pHeight
         */
        public void setPopupSize(int pWidth, int pHeight) {
            iComboBox.setPopupSize(pWidth, pHeight );
        }

        /**
         *
         * @param iAllowCustomValues
         */
        public void setAllowCustomValues(boolean iAllowCustomValues) {
            iComboBox.setAllowCustomValues(iAllowCustomValues);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("se.swedsoft.bookkeeping.gui.util.components.SSTableComboBoxOld.CellEditor");
            sb.append("{iComboBox=").append(iComboBox);
            sb.append(", iCurrentColumn=").append(iCurrentColumn);
            sb.append(", iCurrentRow=").append(iCurrentRow);
            sb.append(", iCurrentTable=").append(iCurrentTable);
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.util.components.SSTableComboBoxOld");
        sb.append("{iAllowCustomValues=").append(iAllowCustomValues);
        sb.append(", iDropdownButton=").append(iDropdownButton);
        sb.append(", iEditor=").append(iEditor);
        sb.append(", iModel=").append(iModel);
        sb.append(", iObjects=").append(iObjects);
        sb.append(", iPopup=").append(iPopup);
        sb.append(", iSearchColumns=").append(iSearchColumns == null ? "null" : "");
        for (int i = 0; iSearchColumns != null && i < iSearchColumns.length; ++i)
            sb.append(i == 0 ? "" : ", ").append(iSearchColumns[i]);
        sb.append(", iSelected=").append(iSelected);
        sb.append(", iSelectionListeners=").append(iSelectionListeners);
        sb.append(", iTable=").append(iTable);
        sb.append(", iTextField=").append(iTextField);
        sb.append('}');
        return sb.toString();
    }
}



