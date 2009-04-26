package se.swedsoft.bookkeeping.gui.util.components;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.util.graphics.SSIcon;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

/**
 * Date: 2006-feb-02
 * Time: 10:40:47
 */
public class SSMenuButton<T extends JButton> extends SSButton {

    private JPopupMenu iPopup;


    /**
     *
     */
    public SSMenuButton(){
        super();
        createPopup();
    }




    /**
     * @param pIcon
     */
    public SSMenuButton(String pIcon) {
        super(pIcon);
        createPopup();
    }

    /**
     * @param pIcon
     * @param pBundleName
     */
    public SSMenuButton(String pIcon, String pBundleName) {
        super(pIcon, pBundleName);
        createPopup();
    }

    /**
     *
     */
    private void createPopup() {
        iPopup = new JPopupMenu();

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                iPopup.show(SSMenuButton.this, 0, SSMenuButton.this.getHeight() );
            }
        });
    }

    /**
     *
     * @param iComponent
     * @return  the newly added item
     */
    public T add(T iComponent) {
        iPopup.add(iComponent);

        return iComponent;
    }
        

    /**
     *
     * @param pBundleName
     * @param iListener
     * @return the newly added menuitem
     */
    public JMenuItem add(String pBundleName, ActionListener iListener){
        JMenuItem iMenuItem = new JMenuItem();

        if( iBundle.hasKey( pBundleName + ".title" )  ){
            iMenuItem.setText(SSMenuButton.iBundle.getString(pBundleName + ".title") );
        }

        if( iBundle.hasKey( pBundleName + ".tooltip" )  ){
            iMenuItem.setToolTipText(SSMenuButton.iBundle.getString(pBundleName + ".tooltip") );
        }
        iMenuItem.addActionListener(iListener);

        iPopup.add(iMenuItem);
        iPopup.pack();

        return iMenuItem;
    }

    /**
     * 
     */
    public void addSeparator() {
        iPopup.addSeparator();

    }
}
