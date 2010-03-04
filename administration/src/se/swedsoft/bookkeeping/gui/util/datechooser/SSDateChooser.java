package se.swedsoft.bookkeeping.gui.util.datechooser;

import se.swedsoft.bookkeeping.gui.util.datechooser.panel.SSCalendar;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.SSBookkeeping;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.text.SimpleDateFormat;

/**
 * User: Andreas Lago
 * Date: 2006-apr-04
 * Time: 09:58:31
 */
public class SSDateChooser extends JPanel implements ActionListener, ChangeListener {



    private JSpinner iSpinner;

    private JSpinner.DateEditor iEditor;

    private JButton iCalendarButton;

    private JDialog iPopup;

    // the change listeners
    private List<ActionListener> iChangeListeners;

    private SSCalendar iCalendar;

    private SpinnerDateModel iModel;

    // The date format string
    private String iDateFormatString;
    // The calendar field
    private int iCalendarField;

    protected boolean isDateSelected;

    private JPanel iPanel;


    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public SSDateChooser() {
        iChangeListeners = new LinkedList<ActionListener>();

        iDateFormatString = "yyyy-MM-dd";
        iCalendarField    = Calendar.DAY_OF_MONTH;

        iModel = new SpinnerDateModel(){
            @Override
            public void setCalendarField(int calendarField) {
                // Always use the prefered calendar field
                super.setCalendarField(iCalendarField);
            }
        };
        iModel.setCalendarField( Calendar.MONTH );
        iModel.addChangeListener(this);

        iSpinner = new JSpinner();
        iSpinner.setModel ( iModel );
        iSpinner.setPreferredSize(new Dimension(-1, 20));
        iSpinner.setMaximumSize  (new Dimension(-1, 20));
        iSpinner.setMinimumSize  (new Dimension(-1, 20));

        iEditor = new JSpinner.DateEditor(iSpinner, iDateFormatString);
        iSpinner.setEditor( iEditor);

        iCalendar = new SSCalendar();
        iCalendar.addChangeListener(this);

        iPanel = iCalendar.getPanel();
        iPanel.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder(new Color(102,101,84),1), BorderFactory.createLineBorder(new Color(247,236,249),1)));

        iCalendarButton = new SSButton("ICON_CALENDAR16");
        iCalendarButton.setToolTipText( SSBundle.getBundle().getString("date.tooltip"));
        iCalendarButton.setPreferredSize(new Dimension(20, 20));
        iCalendarButton.setMaximumSize  (new Dimension(20, 20));
        iCalendarButton.setMinimumSize  (new Dimension(20, 20));

        iCalendarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if( iPopup == null ) createPopup(SSDateChooser.this);

                int x = iCalendarButton.getWidth() - iPopup.getPreferredSize().width;
                int y = iCalendarButton.getY()     + iCalendarButton.getHeight();

                isDateSelected = false;

                iCalendar.setDate( getDate() );

                // iPopup
                show(iCalendarButton, x, y);
            }
        });

        setLayout(new BorderLayout());
        add(iSpinner       , BorderLayout.CENTER );
        add(iCalendarButton, BorderLayout.EAST );
        setDate(new Date());
    }

    /**
     *
     * @param invoker
     * @param x
     * @param y
     */
    public void show(Component invoker, int x, int y) {
        if( iPopup == null ) createPopup(invoker);

        if (invoker != null) {
            Point invokerOrigin = invoker.getLocationOnScreen();

            iPopup.setLocation(invokerOrigin.x + x, invokerOrigin.y + y);
        } else {
            iPopup.setLocation(x, y);
        }
        //  iPopup.set  getWindow
        iPopup.setVisible(true);
        iPopup.requestFocusInWindow();

    }

    /**
     *
     */
    private void createPopup(Component invoker){
        JDialog iDialog = getDialog(invoker);

        if( iDialog != null){
            iPopup = new JDialog(iDialog);
        } else  {
            iPopup = new JDialog();
        }

        iPopup.setUndecorated(true);
        iPopup.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        iPopup.setAlwaysOnTop(true);
        iPopup.addWindowListener( new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                iPopup.setVisible(false);
            }
        });
        iPopup.add(iPanel);
        iPopup.pack();
    }


    /**
     * Get the parent dialog for this component, or null
     *
     * @param c
     * @return the owning dialog
     */
    private JDialog getDialog(Component c) {
        Component w = c;
        while( w != null ) {

            if(w instanceof JDialog) return (JDialog)w;

            w = w.getParent();
        }
        return null;
    }


    /**
     *
     * @return
     */
    public Date getDate() {
        return iModel.getDate();
    }

    /**
     * Set the selected date, if the date is null, the current date is selected
     *
     * @param iDate the date
     */
    public void setDate(Date iDate) {
        if(iDate != null){
            iModel.setValue(iDate);
        } else {
            iModel.setValue(new Date());
        }
    }

    /**
     * Get the date format string to use for the editor
     *
     * @return the dateformat string
     * @see java.text.DateFormat
     */
    public String getDateFormatString() {
        return iDateFormatString;
    }

    /**
     * Set the date format string to use for the editor
     *
     * @param iDateFormatString the date format string
     * @see java.text.DateFormat
     */
    public void setDateFormatString(String iDateFormatString) {
        this.iDateFormatString = iDateFormatString;

        iEditor.getFormat().applyPattern(iDateFormatString);
        iEditor.getTextField().setValue(iModel.getDate() );

        invalidate();
    }

    /**
     * Set the calendar field the updown shall edit
     *
     * @param iCalendarField the field, as specified in Calendar
     * @see Calendar
     */
    public void setCalendarField(int iCalendarField) {
        this.iCalendarField = iCalendarField;

    }

    /**
     * Get the calendar field the updown are editing
     *
     * @return the calendar field
     * @see Calendar
     */
    public int getCalendarField() {
        return iCalendarField;
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
        ActionEvent iEvent = new ActionEvent(this, 0, "date");

        for (ActionListener iActionListener : iChangeListeners) {
            iActionListener.actionPerformed(iEvent);
        }

    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e) {
        Date iDate = iCalendar.getDate();

        iModel.setValue(iDate);

        if(e.getActionCommand().equals("day")) {
            isDateSelected = true;

            iPopup.setVisible(false);
        }
    }


    public JComponent getEditor() {
        return iSpinner.getEditor();
    }
    public boolean isInCurrentAccountYear()
    {
        Date iAccountYearTo= SSDB.getInstance().getCurrentYear().getTo();
        Date iAccountYearFrom=SSDB.getInstance().getCurrentYear().getFrom();

        Calendar iCalTo = Calendar.getInstance();
        iCalTo.setTime(iAccountYearTo);
        iCalTo.add(Calendar.HOUR, 23);
        iCalTo.add(Calendar.MINUTE, 59);
        iAccountYearTo = iCalTo.getTime();

        Date iCurrent=iModel.getDate();
        if(iCurrent.before(iAccountYearFrom)||iCurrent.after(iAccountYearTo))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Invoked when the target of the listener has changed its state.
     *
     * @param e a ChangeEvent object
     */
    public void stateChanged(ChangeEvent e) {
        notifyChangeListeners();
    }


    /**
     *
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        iCalendarButton.setEnabled(enabled);
        iSpinner       .setEnabled(enabled);
        iEditor        .setEnabled(enabled);

    }

    public static void main(String[] args) {
        try {
            JDialog.setDefaultLookAndFeelDecorated(true);
            JFrame .setDefaultLookAndFeelDecorated(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final SSDateChooser iDateChooser = new SSDateChooser();

        JFrame iFrame = new JFrame();

        JButton iButton = new JButton();
        iButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(iDateChooser.getDate());



            }
        });

        iFrame.setLayout(new BorderLayout());
//   iFrame.add( new SSDayChooser  ().getPanel(), BorderLayout.CENTER );
//  iFrame.add( new SSMonthChooser().getPanel(), BorderLayout.NORTH );
// iFrame.add( new SSYearChooser ().getPanel(), BorderLayout.SOUTH );

//   iFrame.add( new SSCalendar  ().getPanel(), BorderLayout.CENTER );

        iFrame.add(iDateChooser, BorderLayout.NORTH );
        iFrame.add(iButton     , BorderLayout.SOUTH );


        iFrame.pack();
        iFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        iFrame.setLocationRelativeTo(null);
        iFrame.setVisible(true);

    }


    /**
     * Used to clean up references making sure the garbage collector
     * is able to clean up the object.
     */
    public void dispose() {
        iSpinner.removeAll();
        iSpinner=null;
        iEditor.removeAll();
        iEditor=null;

        ActionListener[] iActionListeners=iCalendarButton.getActionListeners();
        for (ActionListener iActionListener : iActionListeners) {
            iCalendarButton.removeActionListener(iActionListener);
        }
        iCalendarButton.removeAll();
        iCalendarButton=null;

        if(iPopup!=null)
        {
            WindowListener[] iWindowListeners= iPopup.getWindowListeners();
            for (WindowListener iWindowListener : iWindowListeners) {
                iPopup.removeWindowListener(iWindowListener);
            }

            iPopup.removeAll();
            iPopup.getContentPane().removeAll();
            iPopup.dispose();
        }
        iChangeListeners.removeAll(iChangeListeners);
        iChangeListeners=null;
        iCalendar.dispose();
        iCalendar=null;
        ChangeListener[] iChangeListeners=iModel.getChangeListeners();
        for (ChangeListener iChangeListener : iChangeListeners) {
            iModel.removeChangeListener(iChangeListener);
        }
        iModel=null;
        iDateFormatString=null;
        iPanel.removeAll();
        iPanel=null;
    }
}
