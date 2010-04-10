package se.swedsoft.bookkeeping.gui.util.components;

import se.swedsoft.bookkeeping.gui.util.SSSelectionListener;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableColumn;
import se.swedsoft.bookkeeping.gui.util.table.model.SSTableModel;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
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
public class SSTableComboBox<T extends SSTableSearchable> extends JPanel {

    // Our textfield
    protected SSTextField iTextField;

    // Dropdown button
    protected JButton iDropdownButton;

    // Popup menu
    protected JPopupMenu iPopup;

    // The table in the popup menu
    protected SSTable  iTable;

    // The table model for the dropdown
    protected SSTableModel<T> iModel;

    // The original objects
    private List<T> iObjects;

    // The columns to search
    private int[] iSearchColumns;

    // Allow to select values not in the dropdown
    private boolean iAllowCustomValues;

    // The selected value
    private T       iSelected;


    private List<SSSelectionListener> iSelectionListeners;
    // Our current editor
    protected CellEditor iEditor;


    private static final int[] EMPTY_INT_ARRAY = {};

    /**
     * Creates a new {@code JPanel} with a double buffer
     * and a flow layout.
     */
    public SSTableComboBox() {
        iSelectionListeners = new LinkedList<SSSelectionListener>();
        iSelected           = null;
        iSearchColumns      = EMPTY_INT_ARRAY;
        iAllowCustomValues  = false;
        iModel              = createDefaultModel();

        iTextField = new SSTextField();

        iDropdownButton = new SSButton( "ICON_DROPDOWN16" );
        iDropdownButton.setMinimumSize  (new Dimension(20, 20));
        iDropdownButton.setMaximumSize  (new Dimension(20, 20));
        iDropdownButton.setPreferredSize(new Dimension(20, 20));

        createLayout();

        // Create the popup here.
        iTable = new SSTable();
        iTable.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );

        iPopup = new JPopupMenu();
        iPopup.add(new JScrollPane(iTable));
        iPopup.setPreferredSize( new Dimension(350,200) );

        // Listener for pressing the button
        iDropdownButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if(! iPopup.isVisible()) {
                    iPopup.show(iTextField, 0, getHeight());
                }

                iModel.setObjects(iObjects);
            }
        });

        // Select the null value
        Action iSelectNull = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                setSelected(null, true);

                if(iEditor != null) {
                    iEditor.stopCellEditing();
                    iEditor.transferFocusToTable();
                }
                stopEdit();
            }
        };

        // Select an item and close.
        Action iSelectAndClose = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                int iIndex = iTable.getSelectedRow();

                if (iIndex >= 0 && iIndex < iModel.getRowCount() ) {
                    T iObject = iModel.getObject( iIndex );

                    setSelected(iObject, true);
                } else{
                    //iSelected = null;
                    return;
                }

                if(iEditor != null) {
                    iEditor.stopCellEditing();
                    iEditor.transferFocusToTable();
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
                if(e.getKeyCode() == KeyEvent.VK_ENTER  || e.getKeyCode() ==  KeyEvent.VK_ESCAPE|| e.getKeyCode() ==  KeyEvent.VK_DELETE) return;

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
            }
        });



        iTable.addDblClickListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //iSelectAndClose.actionPerformed(null);
                iTextField.getActionMap().get("SELECT_ROW_AND_CLOSE_DROP_DOWN").actionPerformed(null);
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
    private SSTableModel<T> createDefaultModel() {
        return new SSTableModel<T>() {
            @Override
            public Class getType() {
                return null;
            }
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return null;
            }
        };
    }

    /**
     *
     */
    protected void createLayout(){
        GridBagLayout      iLayout      = new GridBagLayout();
        GridBagConstraints iConstraints = new GridBagConstraints();

        setLayout(iLayout);

        iConstraints.insets  = new Insets(0,0,0,0);
        iConstraints.fill    = GridBagConstraints.BOTH;
        iConstraints.weightx = 1.0;
        iConstraints.weighty = 1.0;

        iLayout.setConstraints(iTextField, iConstraints);  add(iTextField);

        iConstraints.fill    = GridBagConstraints.NONE;
        iConstraints.weightx = 0.0;

        iLayout.setConstraints(iDropdownButton, iConstraints);  add(iDropdownButton);
    }


    /**
     *
     * @param iModel
     */
    public void setModel(SSTableModel<T> iModel){
        this.iModel = iModel;

        iObjects = iModel.getObjects();

        iTable.setModel(iModel);

        int iTotalWidth  = 0;
        int iIndex       = 0;
        for (SSTableColumn<T> iColumn : iModel.getColumns() ) {
            int iWidth                  = iColumn.getDefaultWidth();
            TableCellEditor   iEditor   = iColumn.getCellEditor();
            TableCellRenderer iRenderer = iColumn.getCellRenderer();

            iTable.getColumnModel().getColumn(iIndex).setPreferredWidth(iWidth);
            iTable.getColumnModel().getColumn(iIndex).setCellEditor(iEditor);
            iTable.getColumnModel().getColumn(iIndex).setCellRenderer(iRenderer);

            iIndex++;

            iTotalWidth += iWidth;

        }
        iPopup.setPreferredSize( new Dimension(iTotalWidth+20, 150) );
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
     * @param pSearchColumns
     */
    public void setSearchColumns(int ... pSearchColumns){
        iSearchColumns = pSearchColumns;
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
        if(iSelected==null && ! iAllowCustomValues && iModel.getRowCount() > 0) return iModel.getObject(0);
        return iSelected;
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
    public Object getValue(){
        if(iSelected != null){
            return iSelected;
        }
        if(iAllowCustomValues){

            return iTextField.getText();
        }
        return null;
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
        if(text == null || text.length() == 0) return true;

        if(value == null) return false;

        String iString = value.toString();

        return iString.toLowerCase().startsWith(text.toLowerCase());
    }


    /**
     * Start the editing.
     */
    public void startEdit() {
        iPopup.show(iTextField, 0, getHeight());

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                iTextField.requestFocusInWindow();
            }
        });
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
    protected void notifySelectionListeners(SSTableSearchable pSelected) {
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
    public SSTable getTable() {
        return iTable;
    }

    /**
     * Used to clean up references making sure the garbage collector
     * is able to clean up the object.
     */
    public void dispose() {

        KeyListener[] iKeyListeners = iTextField.getKeyListeners();
        for (KeyListener iKeyListener : iKeyListeners) {
            iTextField.removeKeyListener(iKeyListener);
        }
        iTextField.removeAll();
        iTextField=null;

        ActionListener[] iActionListeners = iDropdownButton.getActionListeners();
        for(ActionListener iActionListener : iActionListeners){
            iDropdownButton.removeActionListener(iActionListener);
        }
        iDropdownButton.removeAll();
        iDropdownButton=null;

        iPopup.removeAll();
        iPopup=null;

        iTable.dispose();
        iTable.removeAll();
        iTable=null;

        iModel=null;
        iSelected=null;

        iSelectionListeners.removeAll(iSelectionListeners);
        iSelectionListeners=null;

        if(iEditor!=null)
            iEditor.dispose();

        iEditor=null;
    }

    //   private static Border EDITOR_BORDER = new CompoundBorder(new EmptyBorder(1,1,1,1),  new LineBorder(Color.BLACK, 1, true) );

    /**
     *
     */
    public static class CellEditor<T extends SSTableSearchable> extends AbstractCellEditor implements TableCellEditor {
        // The combobox were using
        private SSTableComboBox<T> iComboBox;

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
            iComboBox         = new SSTableComboBox<T>();
            iComboBox.iEditor = this;
        }

        /**
         *
         * @param pModel
         */
        public CellEditor(SSTableModel<T> pModel){
            this();
            setModel( pModel );
        }

        public void dispose()
        {
            if(iCurrentTable!=null)
                iCurrentTable.removeAll();

            iCurrentTable=null;
            iComboBox=null;
        }

        /**
         *
         * @param pModel
         */
        protected void setModel(SSTableModel<T> pModel){
            iComboBox.setModel( pModel );
        }
        /**
         * Sets an initial <code>value</code> for the editor.  This will cause
         * the editor to <code>stopEditing</code> and lose any partially
         * edited value if the editor is editing when this method is called.
         * 
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
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            iCurrentTable  = table;
            iCurrentRow    = row;
            iCurrentColumn = column;

            if ( value instanceof SSTableSearchable ){
                iComboBox.setSelected((T)value, false);
            } else {

                if(value == null){
                    iComboBox.iTextField.setText( "" );
                    iComboBox.setSelected(null);
                } else{
                    iComboBox.iTextField.setText( value.toString() );
                    iComboBox.searchTable();
                }
            }
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    iComboBox.iTextField.requestFocusInWindow();
                }
            });
            iComboBox.iTextField.selectAll();

            return iComboBox;
        }


        /**
         * Returns a reference to the editor component.
         *
         * @return the editor {@code Component}
         */
        public Component getComponent() {
            return iComboBox.iTextField;
        }

        /**
         * Returns the value contained in the editor.
         *
         * @return the value contained in the editor
         */
        public Object getCellEditorValue() {
            /*
    T iSelected = iComboBox.getSelected();

    if(iSelected == null && iComboBox.doAllowCustomValues()){
    return iComboBox.getText();
    }
    return iSelected;
            */
            return iComboBox.getValue();
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
            if (anEvent instanceof MouseEvent) {
                return ((MouseEvent)anEvent).getClickCount() >= 2;
            }
            return true;
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
         * Returns true to indicate that editing has begun.
         *
         * @param anEvent          the event
         * @return
         */
        public boolean startCellEditing(EventObject anEvent) {
            return true;
        }

        /**
         *
         * @return
         */
        @Override
        public boolean stopCellEditing() {
            iComboBox.iPopup.setVisible(false);
            fireEditingStopped();
            return true;
        }

        /**
         *
         */
        @Override
        public void cancelCellEditing() {
            iComboBox.iPopup.setVisible(false);
            fireEditingCanceled();
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
         * @param iAllowCustomValues
         */
        public void setAllowCustomValues(boolean iAllowCustomValues) {
            iComboBox.setAllowCustomValues(iAllowCustomValues);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox.CellEditor");
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
        sb.append("se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox");
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
