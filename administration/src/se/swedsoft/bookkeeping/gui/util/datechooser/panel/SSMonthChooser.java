package se.swedsoft.bookkeeping.gui.util.datechooser.panel;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Andreas Lago
 * Date: 2006-apr-04
 * Time: 12:01:53
 */
public class SSMonthChooser implements ItemListener {

    private JPanel iPanel;
    // The combobox
    private JComboBox iComboBox;
    // the spinner
    private JSpinner iSpinner;

    // The selected date
    private Date iDate;
    // the change listeners
    private List<ActionListener> iChangeListeners;


    /**
     *
     */
    public SSMonthChooser() {
        iChangeListeners = new LinkedList<ActionListener>();

        iComboBox = new JComboBox();
        iComboBox.addItemListener(this);
        iComboBox.setBorder(BorderFactory.createEmptyBorder());
        iComboBox.setLightWeightPopupEnabled(true);


        //iSpinner.setBorder(new EmptyBorder(0, 0, 0, 0));

        iSpinner.setEditor(iComboBox);
        iSpinner.setModel( new MonthSpinnerModel() );

        updateMonthNames();

        setDate( new Date() );
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

        ComboBoxModel iComboBoxModel = iComboBox.getModel();

        int iIndex = iCalendar.get(Calendar.MONTH);

        iComboBoxModel.setSelectedItem(iComboBoxModel.getElementAt(iIndex));
    }




    /**
     *
     * @param e
     */
    public void itemStateChanged(ItemEvent e) {

        int iMonth = iComboBox.getSelectedIndex();

        if(iDate != null && iMonth >= 0){
            Calendar iCalendar = Calendar.getInstance();

            iCalendar.setTime( iDate );

            // Get the selected day
            int iDay = iCalendar.get(Calendar.DAY_OF_MONTH);

            iCalendar.set(Calendar.DAY_OF_MONTH, 1);
            iCalendar.set(Calendar.MONTH       , iMonth);

            if(iDay > iCalendar.getActualMaximum(Calendar.DAY_OF_MONTH) ){
                iCalendar.set(Calendar.DAY_OF_MONTH, iCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            } else {
                iCalendar.set(Calendar.DAY_OF_MONTH, iDay);

            }


            iDate = iCalendar.getTime();

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
        ActionEvent iEvent = new ActionEvent(this, 0, "month");

        for (ActionListener iActionListener : iChangeListeners) {
            iActionListener.actionPerformed(iEvent);
        }
    }

    /**
     * Initializes the locale specific month names.
     */
    private void updateMonthNames() {
        String [] iMonths = new DateFormatSymbols().getMonths();

        iComboBox.removeAllItems();
        for (int i = 0; i < 12; i++) {
            iComboBox.addItem(iMonths[i]);
        }

    }


    /**
     * Used to clean up references making sure the garbage collector
     * is able to clean up the object.
     */
    public void dispose() {

        iPanel.removeAll();
        iPanel=null;

        ItemListener[] iItemListeners=iComboBox.getItemListeners();
        for (ItemListener iItemListener : iItemListeners) {
            iComboBox.removeItemListener(iItemListener);
        }
        iComboBox.removeAllItems();
        iComboBox=null;
        iSpinner.removeAll();
        iSpinner=null;
        iDate = null;

        iChangeListeners.removeAll(iChangeListeners);
        iChangeListeners=null;
    }



    /**
     * The model to use for the spinner
     */
    private class MonthSpinnerModel extends AbstractSpinnerModel{

        /**
         * The <i>current element</i> of the sequence.  This element is usually
         * displayed by the <code>editor</code> part of a <code>JSpinner</code>.
         *
         * @return the current spinner value.
         * @see #setValue
         */
        public Object getValue() {
            return iComboBox.getSelectedItem();

        }

        /**
         * Changes current value of the model, typically this value is displayed
         * by the <code>editor</code> part of a  <code>JSpinner</code>.
         * If the <code>SpinnerModel</code> implementation doesn't support
         * the specified value then an <code>IllegalArgumentException</code>
         * is thrown.  For example a <code>SpinnerModel</code> for numbers might
         * only support values that are integer multiples of ten. In
         * that case, <code>model.setInvoiceValue(new Number(11))</code>
         * would throw an exception.
         *
         * @throws IllegalArgumentException if <code>value</code> isn't allowed
         * @see #getValue
         */
        public void setValue(Object value) {
            iComboBox.setSelectedItem( value );
        }

        /**
         * Return the object in the sequence that comes after the object returned
         * by <code>getInvoiceValue()</code>. If the end of the sequence has been reached
         * then return null.  Calling this method does not effect <code>value</code>.
         *
         * @return the next legal value or null if one doesn't exist
         * @see #getValue
         * @see #getPreviousValue
         */
        public Object getNextValue() {
            int iIndex = iComboBox.getSelectedIndex();

            if(iIndex < 11){
                return iComboBox.getItemAt(iIndex+1);
            } else {
                return iComboBox.getItemAt(0);
            }
        }

        /**
         * Return the object in the sequence that comes before the object returned
         * by <code>getInvoiceValue()</code>.  If the end of the sequence has been reached then
         * return null. Calling this method does not effect <code>value</code>.
         *
         * @return the previous legal value or null if one doesn't exist
         * @see #getValue
         * @see #getNextValue
         */
        public Object getPreviousValue() {
            int iIndex = iComboBox.getSelectedIndex();

            if(iIndex > 0){
                return iComboBox.getItemAt(iIndex-1);
            } else {
                return iComboBox.getItemAt(11);
            }

        }
    }

}
