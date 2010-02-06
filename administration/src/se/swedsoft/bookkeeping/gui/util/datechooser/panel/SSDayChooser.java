package se.swedsoft.bookkeeping.gui.util.datechooser.panel;

//import com.incors.plaf.alloy.AlloyCommonUtilities;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DateFormatSymbols;
import java.text.DateFormat;

import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;
//import com.incors.plaf.alloy.AlloyCommonUtilities;


/**
 * User: Andreas Lago
 * Date: 2006-apr-04
 * Time: 09:58:55
 */
public class SSDayChooser implements ActionListener {

       private static Color WEEK_COLOR = new Color(100, 100, 100);

    private static Color BACKGROUND_COLOR = new Color(210, 228, 238);

    private static Color sundayForeground  = new Color(164, 0, 0);
    private static Color weekdayForeground = new Color(0, 90, 164);

    private static Color SELECTED_COLOR = new Color(160, 160, 160);//new Color(119, 137, 162);


    // The main panel
    private JPanel iPanel;
    // The panel that contains the day buttons
    private JPanel iDayPanel;

    private JPanel iDayNamePanel;

    private JPanel iWeekNamePanel;


    // The buttons for the days
    private List<DayButton> iDayButtons;
    // The label for the day names
    private List<JLabel> iDayNames;
    // The labels for the weeks
    private List<JLabel> iWeekNames;


    // The selected date
    private Date iDate;
    // the change listeners
    private List<ActionListener> iChangeListeners;

    /**
     * 
     */
    public SSDayChooser() {
        iChangeListeners = new LinkedList<ActionListener>();

        iPanel.setBackground(BACKGROUND_COLOR);

        iDayPanel.setLayout(new GridLayout(6, 7));
        iDayPanel.setBorder( BorderFactory.createEmptyBorder(2,2,2,2));

        iDayNamePanel.setLayout(new GridLayout(1, 7));
        iDayNamePanel.setOpaque(false);
        iDayNamePanel.setBorder( BorderFactory.createEmptyBorder(2,2,2,2));

        iWeekNamePanel.setLayout(new GridLayout(6, 1));
        iWeekNamePanel.setOpaque(false);
        iWeekNamePanel.setBorder( BorderFactory.createEmptyBorder(2,2,2,2));


        // Initializate all the day buttons
        iDayButtons = new LinkedList<DayButton>();
        for(int i=0; i < 42; i++){
            DayButton iButton = new DayButton();

            iButton.setText( Integer.toString(i+1) );
            iButton.addActionListener(this);
            iButton.setContentAreaFilled(true);

            iDayButtons.add(iButton);
            iDayPanel  .add(iButton);
        }

        // Initialize the day names
        iDayNames = new LinkedList<JLabel>();
        for(int i=0; i < 7; i++){
            JLabel iLabel = new JLabel();
            iLabel.setOpaque(false);
            iLabel.setHorizontalAlignment(SwingConstants.CENTER);
            iLabel.setVerticalAlignment  (SwingConstants.CENTER);

            iDayNames    .add(iLabel);
            iDayNamePanel.add(iLabel);
        }

        // Initialize the week names
        iWeekNames = new LinkedList<JLabel>();
        for(int i=0; i < 6; i++){
            JLabel iLabel = new JLabel();
            iLabel.setOpaque(false);
            iLabel.setHorizontalAlignment(SwingConstants.CENTER);
            iLabel.setVerticalAlignment  (SwingConstants.CENTER);
            iLabel.setForeground(WEEK_COLOR);

            iWeekNames    .add(iLabel);
            iWeekNamePanel.add(iLabel);
        }

        setDate( new Date() );
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return iDate;
    }

    /**
     * Set the selected date
     *
     * @param iDate
     */
    public void setDate(Date iDate){
        this.iDate = iDate;

        updateDayColumns();
        updateDays();
        updateWeeks();
    }



    /**
     * Returns the panel for the date chooser
     *
     * @return the panel
     */
    public JPanel getPanel() {
        return iPanel;
    }



    /**
     *
     */
    private void updateDayColumns() {
        Calendar iCalendar = Calendar.getInstance();

        String[] iDayNames =  new DateFormatSymbols().getShortWeekdays();

        int day = iCalendar.getFirstDayOfWeek();

        for(JLabel iLabel: this.iDayNames){
            iLabel.setText(iDayNames[day]);

            if (day == Calendar.SUNDAY) {
                iLabel.setForeground(sundayForeground);
            } else {
                iLabel.setForeground(weekdayForeground);
            }


            if (day < 7) {
                day++;
            } else {
                day -= 6;
            }
        }
    }

    /**
     *
     */
    private void updateWeeks(){
        Calendar iCalendar = Calendar.getInstance();

        iCalendar.setTime(iDate);

        iCalendar.set(Calendar.DAY_OF_MONTH, 1);

        for(JLabel iLabel: iWeekNames){
            int iWeekNumber = iCalendar.get(Calendar.WEEK_OF_YEAR);

            if (iWeekNumber < 10) {
                iLabel.setText( "0" +  Integer.toString(iWeekNumber) );
            } else {
                iLabel.setText( Integer.toString(iWeekNumber) );
            }

            iCalendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

    }

    /**
     *
     */
    private void updateDays() {
        Calendar iCalendar = Calendar.getInstance();

        iCalendar.setTime(iDate);

        // Get the current month
        int iMonth  = iCalendar.get(Calendar.MONTH);

        // Move to the fist day of the month
        iCalendar.set(Calendar.DAY_OF_MONTH, 1);

        //int iStartDay =  ;
        // Reset to the first day in the fist week of the month
       // iCalendar.add(Calendar.DAY_OF_YEAR, iCalendar.getFirstDayOfWeek() - iCalendar.get(Calendar.DAY_OF_WEEK) );
        int iIndex = 0;
        int iStart  = iCalendar.get(Calendar.DAY_OF_WEEK) - iCalendar.getFirstDayOfWeek() ;

        if(iStart < 0) iStart = iStart + 7;

        int iStop   = iStart + iCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);


        DateFormat iFormat = DateFormat.getDateInstance(DateFormat.LONG);
        for(DayButton iButton: iDayButtons){
            int iDay = iCalendar.get(Calendar.DAY_OF_MONTH);

            // Only show the button if the day is in the current month
            if( iIndex >= iStart && iIndex < iStop){
                Date iCurrentDate =  iCalendar.getTime();

                iButton.setVisible    ( true );
                iButton.setText       ( Integer.toString( iDay  ) );
                iButton.setToolTipText( iFormat.format( iCurrentDate ) );
                iButton.setDate       ( iCurrentDate );

                if( iCurrentDate.equals( iDate )){
                    //AlloyCommonUtilities.set3DBackground(iButton, SELECTED_COLOR);
                    iButton.setBackground(SELECTED_COLOR);
                } else {
                    iButton.setBackground(  new JButton().getBackground() );
                }

                iCalendar.add(Calendar.DAY_OF_MONTH, 1);
            } else {
                iButton.setVisible( false );
                iButton.setDate(null);
            }

            iIndex++;
        }
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() instanceof DayButton){
            DayButton iButton = (DayButton) e.getSource();

            iDate = iButton.getDate();

            notifyChangeListeners();
        }

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
        ActionEvent iEvent = new ActionEvent(this, 0, "day");

        for (ActionListener iActionListener : iChangeListeners) {
            iActionListener.actionPerformed(iEvent);
        }
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

        iDayNamePanel.removeAll();
        iDayNamePanel=null;

        iWeekNamePanel.removeAll();
        iWeekNamePanel=null;
        for(DayButton iDayButton:iDayButtons)
        {
            ActionListener[] iActionListeners = iDayButton.getActionListeners();
            for(int i=0;i<iActionListeners.length;i++)
            {
                iDayButton.removeActionListener(iActionListeners[i]);
            }
        }

        iDayButtons.removeAll(iDayButtons);
        iDayButtons=null;
        iDayNames.removeAll(iDayNames);
        iDayNames=null;
        iWeekNames.removeAll(iWeekNames);
        iWeekNames=null;

        iDate=null;

        iChangeListeners.removeAll(iChangeListeners);
        iChangeListeners=null;
    }


    /**
     * The button to use for the day selecting
     */
    private class DayButton extends JButton{

        // The date to select if the user presses this button
        private Date iDate;

        /**
         * Creates a button with no set text or icon.
         */
        public DayButton() {
            setMinimumSize  ( new Dimension(27, 21));
            setMaximumSize  ( new Dimension(27, 21));
            setPreferredSize( new Dimension(27, 21));

            setMargin(new Insets(0, 0, 0, 0));

            setFocusPainted(false);
            setOpaque(false);
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
        }
    }

}


