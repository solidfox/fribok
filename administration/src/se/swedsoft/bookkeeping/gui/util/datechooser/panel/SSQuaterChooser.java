package se.swedsoft.bookkeeping.gui.util.datechooser.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.*;
import java.util.List;
import java.text.DateFormatSymbols;

/**
 * User: Andreas Lago
 * Date: 2006-maj-31
 * Time: 15:18:50
 */
public class SSQuaterChooser extends JPanel implements ItemListener {


    private JComboBox iComboBox;

    private JPanel iPanel;

    // The selected date
    private Date iDate;
    // the change listeners
    private List<ActionListener> iChangeListeners;


    /**
     *
     */
    public SSQuaterChooser() {
        super();
        iChangeListeners = new LinkedList<ActionListener>();

        setLayout(new BorderLayout());

        add(iPanel, BorderLayout.CENTER);
        /*
        iComboBox.addItem("1");
        iComboBox.addItem("2");
        iComboBox.addItem("3");
        iComboBox.addItem("4"); */

        updateQuaterNames();


        iComboBox.addItemListener(this);
        setDate(new Date());
    }


    /**
     * 
     */
    private void updateQuaterNames() {
        String [] iMonths = new DateFormatSymbols().getMonths();

        iComboBox.removeAllItems();
//        iComboBox.addItem("1  (" + iMonths[0] + " - " + iMonths[2]);
//        iComboBox.addItem("2  (" + iMonths[3] + " - " + iMonths[5]);
//        iComboBox.addItem("3  (" + iMonths[6] + " - " + iMonths[8]);
//        iComboBox.addItem("4  (" + iMonths[9] + " - " + iMonths[11]);
        iComboBox.addItem("1");
        iComboBox.addItem("2");
        iComboBox.addItem("3");
        iComboBox.addItem("4");
    }


    /**
     * Invoked when the date changes
     *
     * @param iActionListener
     */

    public void addChangeListener(ActionListener iActionListener) {
        iChangeListeners.add(iActionListener);
    }

    /**
     *
     */
    private void notifyChangeListeners() {
        ActionEvent iEvent = new ActionEvent(this, 0, "quater");

        for (ActionListener iActionListener : iChangeListeners) {
            iActionListener.actionPerformed(iEvent);
        }
    }


    /**
     * Invoked when an item has been selected or deselected by the user.
     * The code written for this method performs the operations
     * that need to occur when an item is selected (or deselected).
     */
    public void itemStateChanged(ItemEvent e) {
        notifyChangeListeners();
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        int iIndex = iComboBox.getSelectedIndex();

        Calendar iCalendar = Calendar.getInstance();

        iCalendar.setTime(iDate);

        iCalendar.set(Calendar.MONTH       , iIndex * 3);
        iCalendar.set(Calendar.DAY_OF_MONTH, 1);
        iCalendar.set(Calendar.HOUR_OF_DAY , 0);
        iCalendar.set(Calendar.MINUTE      , 0);
        iCalendar.set(Calendar.SECOND      , 0);

        iDate = iCalendar.getTime();

        return iDate;
    }

    /**
     *
     * @return
     */
    public Date getEndDate(){

        int iIndex = iComboBox.getSelectedIndex();

        Calendar iCalendar = Calendar.getInstance();

        iCalendar.setTime(iDate);

        iCalendar.set(Calendar.MONTH       , (iIndex+1) * 3);
        iCalendar.set(Calendar.DAY_OF_MONTH, 1);
        iCalendar.set(Calendar.HOUR_OF_DAY , 23);
        iCalendar.set(Calendar.MINUTE      , 59);
        iCalendar.set(Calendar.SECOND      , 59);
        iCalendar.add(Calendar.DAY_OF_MONTH, -1);

        return iCalendar.getTime();
    }


    /**
     *
     * @param iDate
     */
    public void setDate(Date iDate) {
        this.iDate = iDate;

        Calendar iCalendar = Calendar.getInstance();

        iCalendar.setTime(iDate);

        int iIndex = iCalendar.get(Calendar.MONTH) % 3;

        iComboBox.setSelectedIndex(iIndex);
    }
}
