package se.swedsoft.bookkeeping.gui.util;

import javax.swing.*;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * User: Andreas Lago
 * Date: 2006-apr-25
 * Time: 16:37:09
 */
public class SSButtonGroup implements ActionListener {

    // the list of buttons participating in this group
    protected List<AbstractButton> iButtons = new LinkedList<AbstractButton>();

    protected boolean iAllowNoneSelected;

    // The currently selected button
    protected AbstractButton iSelected;


    /**
     *
     */
    public SSButtonGroup() {
        this(false);
    }

    /**
     *
     * @param iAllowNoneSelected
     */
    public SSButtonGroup(boolean iAllowNoneSelected) {
        this.iAllowNoneSelected = iAllowNoneSelected;
    }

    /**
     *
     * @return
     */
    public boolean isAllowNoneSelected() {
        return iAllowNoneSelected;
    }

    /**
     *
     * @param iAllowNoneSelected
     */
    public void setAllowNoneSelected(boolean iAllowNoneSelected) {
        this.iAllowNoneSelected = iAllowNoneSelected;
    }


    /**
     * Adds the button to the group.
     * @param iButton the button to be added
     */
    public void add(AbstractButton iButton) {
        if(iButton == null || iButtons.contains(iButton) ) return;

        iButtons.add(iButton);

        if(iButton.isSelected()) {
            setSelected(iButton);
        }
        iButton.addActionListener(this);
    }

    /**
     * Removes the button from the group.
     * @param iButton the button to be removed
     */
    public void remove(AbstractButton iButton) {
        if(iButton == null) return;

        iButtons.remove(iButton);

        if(iButton == iSelected) {
            iSelected = null;
        }
        iButton.addActionListener(this);
    }

    /**
     * Returns the number of buttons in the group.
     * @return the button count
     * @since 1.3
     */
    public int getButtonCount() {
        return iButtons == null ? 0 : iButtons.size();
    }

    /**
     * Returns the buttons in the group
     * @return the buttons
     */
    public List<AbstractButton> getButtons() {
        return Collections.unmodifiableList(iButtons);
    }


    /**
     * Returns the elected button.
     * @return the selected button
     */
    public AbstractButton getSelected() {
        return iSelected;
    }

   // private boolean iUpdating = false;

    /**
     *
     * @param iButton
     */
    private void setSelected(AbstractButton iButton){
        iSelected = iButton;

//        iUpdating = true;
        for (AbstractButton iCurrent : iButtons) {
            iCurrent.removeActionListener(this);
            iCurrent.setSelected(iCurrent == iSelected);
            iCurrent.addActionListener(this);
        }
        //   setUpdating(false);
    }






    boolean iUpdating = false;

    /**
     *
     * @return if the locking failed
     */
    private boolean setUpdating(boolean iNewUpdating){

        if( iUpdating && iNewUpdating ) return true;

        this.iUpdating = iNewUpdating;

        return false;
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        AbstractButton iButton = (AbstractButton)e.getSource();

        if( iButton.isSelected() ){
            setSelected(iButton);
        } else {

            if(iAllowNoneSelected) setSelected(null);

        }
    }
}
