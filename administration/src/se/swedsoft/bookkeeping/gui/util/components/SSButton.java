package se.swedsoft.bookkeeping.gui.util.components;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Date: 2006-feb-02
 * Time: 10:40:47
 */
public class SSButton extends JButton {

    protected static SSBundle iBundle = SSBundle.getBundle();


    private String iIconName;

    /**
     *
     */
    public SSButton(){
        getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ENTER_PRESSED");

        getActionMap().put("ENTER_PRESSED", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                fireActionPerformed(e);
            }
        });
    }
    /**
     *
     * @param pIcon
     */
    public SSButton(String pIcon){
        this();

        setIconName(pIcon);
    }

    /**
     *
     * @param pIcon
     * @param pBundleName
     */
    public SSButton(String pIcon, String pBundleName){
        this();

        if( iBundle.hasKey( pBundleName + ".title" )  ){
            setText(iBundle.getString(pBundleName + ".title") );
        }

        if( iBundle.hasKey( pBundleName + ".tooltip" )  ){
            setToolTipText(iBundle.getString(pBundleName + ".tooltip") );
        }
        setIconName(pIcon);
    }

    /**
     *
     * @param pIcon
     * @param pBundleName
     * @param pAction
     */
    public SSButton(String pIcon, String pBundleName, ActionListener pAction){
        this(pIcon, pBundleName);

        addActionListener(pAction);
    }


    /**
     *
     * @param pIcon
     */
    public void setIconName(String pIcon){
        iIconName = pIcon;

        if(iIconName != null && iIconName.length() > 0){
            setIcon        ( SSIcon.getIcon(pIcon, SSIcon.IconState.NORMAL ) );
            setDisabledIcon( SSIcon.getIcon(pIcon, SSIcon.IconState.DISABLED ) );
            setRolloverIcon( SSIcon.getIcon(pIcon, SSIcon.IconState.HIGHLIGHTED ) );
        }
    }

    /**
     * 
     * @return
     */
    public String getIconName(){
        return iIconName;
    }


    /**
     *
     */
    public void setDefaultSize(){
        setMaximumSize  ( new Dimension(40,40) );
        setPreferredSize( new Dimension(40,40) );
    }

    /**
     * 
     * @param iTable
     */
    public void setSelectionDependent(SSTable iTable) {
        iTable.addSelectionDependentComponent(this);
    }

    /**
     * Used to clean up references making sure the garbage collector
     * is able to clean up the object.
     */
    public void dispose() {
        ActionListener[] iActionListeners = getActionListeners();
        for (ActionListener iActionListener : iActionListeners) {
            removeActionListener(iActionListener);
        }
        //this.removeAll();
    }
}
