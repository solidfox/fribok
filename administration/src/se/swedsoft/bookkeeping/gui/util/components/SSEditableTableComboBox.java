package se.swedsoft.bookkeeping.gui.util.components;

import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;
import se.swedsoft.bookkeeping.gui.util.table.SSTableSearchable;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * User: Andreas Lago
 * Date: 2006-mar-23
 * Time: 09:25:01
 */
public class SSEditableTableComboBox<T extends SSTableSearchable> extends JPanel {


    public static interface EditingFactory<T extends SSTableSearchable> {

        public T newAction();

        public void editAction(T iSelected);

        public void deleteAction(T iSelected);
    }


    private JButton iEditButton;

    private JPopupMenu iPopup;

    private EditingFactory<T> iFactory;

    private SSTableComboBox<T> iComboBox;



    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public SSEditableTableComboBox() {
        super();
        iComboBox = new SSTableComboBox<T>();

        iEditButton  = new JButton( "..." );
        iEditButton.setPreferredSize(new Dimension(20, 20));
        iEditButton.setMaximumSize  (new Dimension(20, 20));
        iEditButton.setMinimumSize  (new Dimension(20, 20));

        createLayout();


        JMenuItem iMenuItem;

        iPopup = new JPopupMenu();

        iMenuItem = new JMenuItem("Lägg till...");
        iMenuItem.setIcon        ( SSIcon.getIcon("ICON_NEW16", SSIcon.IconState.NORMAL ) );
        iMenuItem.setDisabledIcon( SSIcon.getIcon("ICON_NEW16", SSIcon.IconState.DISABLED ) );
        iMenuItem.setRolloverIcon( SSIcon.getIcon("ICON_NEW16", SSIcon.IconState.HIGHLIGHTED ) );
        iMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if(iFactory != null) {
                    T iSelected = iFactory.newAction();

                    if(iSelected != null){
                        if(iComboBox.getModel() != null) iComboBox.getModel().fireTableDataChanged();
                        iComboBox.setModel(iComboBox.getModel().getDropdownmodel());
                        iComboBox.setSelected(iSelected, true);
                    }
                }
            }
        });
        iPopup.add(iMenuItem);

        iMenuItem = new JMenuItem("Ändra...");
        iMenuItem.setIcon        ( SSIcon.getIcon("ICON_EDIT16", SSIcon.IconState.NORMAL ) );
        iMenuItem.setDisabledIcon( SSIcon.getIcon("ICON_EDIT16", SSIcon.IconState.DISABLED ) );
        iMenuItem.setRolloverIcon( SSIcon.getIcon("ICON_EDIT16", SSIcon.IconState.HIGHLIGHTED ) );
        iMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                T iSelected = iComboBox.getSelected();

                if(iFactory != null && iSelected != null) {
                    iFactory.editAction(iSelected);

                    if(iComboBox.getModel() != null) iComboBox.getModel().fireTableDataChanged();
                    iComboBox.setModel(iComboBox.getModel().getDropdownmodel());
                    iComboBox.setSelected(iSelected, true);
                }
            }
        });
        iPopup.add(iMenuItem);

        iMenuItem = new JMenuItem("Ta bort...");
        iMenuItem.setIcon        ( SSIcon.getIcon("ICON_DELETE16", SSIcon.IconState.NORMAL ) );
        iMenuItem.setDisabledIcon( SSIcon.getIcon("ICON_DELETE16", SSIcon.IconState.DISABLED ) );
        iMenuItem.setRolloverIcon( SSIcon.getIcon("ICON_DELETE16", SSIcon.IconState.HIGHLIGHTED ) );
        iMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                T iSelected = iComboBox.getSelected();

                if(iFactory != null && iSelected != null) {
                    iFactory.deleteAction(iSelected);

                    if(iComboBox.getModel() != null) iComboBox.getModel().fireTableDataChanged();
                    iComboBox.setModel(iComboBox.getModel().getDropdownmodel());
                    iComboBox.setSelected(null, true);
                }

            }
        });
        iPopup.add(iMenuItem);


        iEditButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iPopup.show(iEditButton, 0, iEditButton.getHeight());
            }
        });
    }

    public void dispose()
    {
        ActionListener[] iActionListeners = iEditButton.getActionListeners();
        for (ActionListener iActionListener : iActionListeners) {
            iEditButton.removeActionListener(iActionListener);
        }
        iEditButton.removeAll();
        iEditButton=null;

        iPopup.removeAll();
        iPopup=null;
        iFactory=null;
        iComboBox.dispose();
        iComboBox=null;
        
    }
    /**
     *
     */
    protected void createLayout() {
        GridBagLayout      iLayout      = new GridBagLayout();
        GridBagConstraints iConstraints = new GridBagConstraints();

        setLayout(iLayout);

        iConstraints.insets  = new Insets(0,0,0,0);
        iConstraints.fill    = GridBagConstraints.BOTH;
        iConstraints.weightx = 1.0;
        iConstraints.weighty = 1.0;

        iLayout.setConstraints(iComboBox, iConstraints);
        add(iComboBox);

        iConstraints.fill    = GridBagConstraints.NONE;
        iConstraints.weightx = 0.0;

        iLayout.setConstraints(iEditButton, iConstraints);
        add(iEditButton);
    }


    /**
     *
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        iComboBox     .setEnabled(enabled);
        iEditButton    .setEnabled (enabled);

        super.setEnabled(enabled);
    }




    /**
     *
     * @param pFactory
     */
    public void setEditingFactory(EditingFactory<T> pFactory){
        iFactory = pFactory;
    }

    /**
     *
     * @return
     */
    public SSTableComboBox<T> getComboBox() {
        return iComboBox;
    }

    /**
     *
     * @return
     */
    public T getSelected() {
        return iComboBox.getSelected();
    }

    /**
     *
     * @param pSelected
     */
    public void setSelected(T pSelected) {
        iComboBox.setSelected(pSelected);
    }

    /**
     *
     * @param pSelected
     * @param pNotifyListeners
     */
    public void setSelected(T pSelected, boolean pNotifyListeners){
        iComboBox.setSelected(pSelected, pNotifyListeners);
    }



}


