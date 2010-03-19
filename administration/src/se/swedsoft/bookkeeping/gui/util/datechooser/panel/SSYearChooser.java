package se.swedsoft.bookkeeping.gui.util.datechooser.panel;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-apr-04
 * Time: 12:40:37
 */
public class SSYearChooser extends JPanel implements ChangeListener, CaretListener, ActionListener {


    private JPanel iPanel;

    private JSpinner iSpinner;

    private JTextField iTextField;

    private SpinnerNumberModel iModel ;

    // The selected date
    private Date iDate;
    // the change listeners
    private List<ActionListener> iChangeListeners;




    public SSYearChooser() {
        setLayout(new BorderLayout());

        add(iPanel, BorderLayout.CENTER);

        Calendar iCalendar = Calendar.getInstance();

        iChangeListeners = new LinkedList<ActionListener>();

        iTextField = new JTextField();
        iTextField.setBorder(BorderFactory.createEmptyBorder());
        iTextField.setHorizontalAlignment(SwingConstants.RIGHT);
        iTextField.addCaretListener(this);
        iTextField.addActionListener(this);

        iModel = new SpinnerNumberModel();
        iModel.setMinimum(iCalendar.getMinimum(Calendar.YEAR));
        iModel.setMaximum(iCalendar.getMaximum(Calendar.YEAR));
        iModel.addChangeListener(this);

        iSpinner.setEditor( iTextField);
        iSpinner.setModel ( iModel );
        //  iSpinner.setBorder(new EmptyBorder(0, 0, 0, 0));

        setDate(new Date() );
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

        Calendar iCalendar = Calendar.getInstance();

        iCalendar.setTime(iDate);

        iSpinner.setValue( iCalendar.get(Calendar.YEAR) );
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
        ActionEvent iEvent = new ActionEvent(this, 0, "year");

        for (ActionListener iActionListener : iChangeListeners) {
            iActionListener.actionPerformed(iEvent);
        }
    }

    /**
     * Invoked when the target of the listener has changed its state.
     *
     * @param e a ChangeEvent object
     */
    public void stateChanged(ChangeEvent e) {
        Calendar iCalendar = Calendar.getInstance();

        Number iNumber = iModel.getNumber();

        iTextField.setText( iNumber.toString() );

        iCalendar.setTime(iDate);
        iCalendar.set(Calendar.YEAR, iNumber.intValue());

        iDate = iCalendar.getTime();

        notifyChangeListeners();
    }

    /**
     * After any user input, the value of the textfield is proofed. Depending on
     * being an integer, the value is colored green or red.
     *
     * @param e Description of the Parameter
     */
    public void caretUpdate(CaretEvent e) {
        Calendar iCalendar = Calendar.getInstance();

        int iValue;

        try {
            iValue = Integer.decode( iTextField.getText() );
        } catch (NumberFormatException e1) {
            iTextField.setForeground(Color.RED);

            return;
        }

        int iMin = iCalendar.getMinimum(Calendar.YEAR);
        int iMax = iCalendar.getMaximum(Calendar.YEAR);

        if(iValue < iMin || iValue > iMax){
            iTextField.setForeground(Color.RED);
        } else {
            iTextField.setForeground(Color.BLACK);
        }

        iTextField.repaint();
    }



    /**
     * After any user input, the value of the textfield is proofed. Depending on
     * being an integer, the value is colored green or red. If the textfield is
     * green, the enter key is accepted and the new value is set.
     *
     * @param e
     *            Description of the Parameter
     */
    public void actionPerformed(ActionEvent e) {
        try {
            int iValue = Integer.decode( iTextField.getText() );

            iModel.setValue(iValue);

        } catch (NumberFormatException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Used to clean up references making sure the garbage collector
     * is able to clean up the object.
     */
    public void dispose() {

        iPanel.removeAll();
        iPanel=null;
        iSpinner.removeAll();
        iSpinner=null;

        ActionListener[] iActionListeners = iTextField.getActionListeners();
        CaretListener[] iCaretListeners = iTextField.getCaretListeners();

        for (ActionListener iActionListener : iActionListeners) {
            iTextField.removeActionListener(iActionListener);
        }

        for (CaretListener iCaretListener : iCaretListeners) {
            iTextField.removeCaretListener(iCaretListener);
        }

        iTextField.removeAll();
        iTextField=null;

        ChangeListener[] iChangeListenerss = iModel.getChangeListeners();
        for(ChangeListener iChangeListener : iChangeListenerss){
            iModel.removeChangeListener(iChangeListener);
        }
        iModel=null;

        iDate=null;

        iChangeListeners.removeAll(iChangeListeners);
        iChangeListeners=null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.util.datechooser.panel.SSYearChooser");
        sb.append("{iChangeListeners=").append(iChangeListeners);
        sb.append(", iDate=").append(iDate);
        sb.append(", iModel=").append(iModel);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iSpinner=").append(iSpinner);
        sb.append(", iTextField=").append(iTextField);
        sb.append('}');
        return sb.toString();
    }
}
