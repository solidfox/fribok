package se.swedsoft.bookkeeping.gui.util.datechooser.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-apr-04
 * Time: 12:07:38
 */
public class SSCalendar implements ActionListener {

    private JPanel iPanel;

    private JPanel iYearPanel;

    private JPanel iMonthPanel;

    private JPanel iDayPanel;

    private SSDayChooser iDayChooser;

    private SSMonthChooser iMonthChooser;

    private SSYearChooser iYearChooser;
    // The selected date
    private Date iDate;
    // the change listeners
    private List<ActionListener> iChangeListeners;


    /**
     *
     */
    public SSCalendar() {
        iChangeListeners = new LinkedList<ActionListener>();

        iYearPanel.setLayout ( new BorderLayout() );
        iMonthPanel.setLayout( new BorderLayout() );
        iDayPanel .setLayout ( new BorderLayout() );

        iDayChooser   = new SSDayChooser  ();
        iMonthChooser = new SSMonthChooser();
        iYearChooser  = new SSYearChooser ();

        iDayChooser  .addChangeListener( this);
        iMonthChooser.addChangeListener( this);
        iYearChooser .addChangeListener( this);


        iYearPanel .add(iYearChooser .getPanel(), BorderLayout.CENTER );
        iMonthPanel.add(iMonthChooser.getPanel(), BorderLayout.CENTER );
        iDayPanel  .add(iDayChooser  .getPanel(), BorderLayout.CENTER );
    }


    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return iDate;
    }

    /**
     *
     * @param iDate
     */
    public void setDate(Date iDate) {
        this.iDate = iDate;

        iDayChooser  .setDate(iDate );
        iMonthChooser.setDate(iDate );
        iYearChooser .setDate(iDate );
    }

    /**
     *
     * @return
     */
    public SSYearChooser getYearChooser() {
        return iYearChooser;
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
     * @return
     */
    public SSMonthChooser getMonthChooser() {
        return iMonthChooser;
    }

    /**
     *
     * @return
     */
    public SSDayChooser getDayChooser() {
        return iDayChooser;
    }


    /**
     *
     */
    private void notifyChangeListeners(ActionEvent iEvent) {

        for (ActionListener iActionListener : iChangeListeners) {
            iActionListener.actionPerformed(iEvent);
        }
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        ActionEvent iEvent = new ActionEvent(this, 0, e.getActionCommand() );

        if(e.getSource() == iDayChooser  ) {
            iDate = iDayChooser.getDate();

            notifyChangeListeners(iEvent);
        }
        if(e.getSource() == iYearChooser ) {
            iDate = iYearChooser .getDate();
        }
        if(e.getSource() == iMonthChooser) {
            iDate = iMonthChooser.getDate();
        }

        iDayChooser  .setDate(iDate );
        iMonthChooser.setDate(iDate );
        iYearChooser .setDate(iDate );


    }

    /**
     * Used to clean up references making sure the garbage collector
     * is able to clean up the object.
     */
    public void dispose() {
        iPanel.removeAll();
        iPanel=null;

        iDayPanel.removeAll();
        iDayPanel=null;

        iYearPanel.removeAll();
        iYearPanel=null;

        iMonthPanel.removeAll();
        iMonthPanel=null;

        iDayChooser.dispose();
        iDayChooser=null;

        iMonthChooser.dispose();
        iMonthChooser=null;

        iYearChooser.dispose();
        iYearChooser=null;

        iDate=null;

        iChangeListeners.removeAll(iChangeListeners);
        iChangeListeners=null;
    }

}
