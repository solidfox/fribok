package se.swedsoft.bookkeeping.gui.accountingyear.panel;


import se.swedsoft.bookkeeping.data.SSAccountPlan;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.accountplans.util.SSAccountPlanTableModel;
import se.swedsoft.bookkeeping.gui.util.SSButtonPanel;
import se.swedsoft.bookkeeping.gui.util.components.SSTableComboBox;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

/**
 * Date: 2006-feb-15
 * Time: 12:20:25
 */
public class SSAccountingYearPanel {

    private JPanel iPanel;

    private SSButtonPanel iButtonPanel;


    protected SSDateChooser iFrom;

    protected SSDateChooser iTo;

    protected JPanel iAccountPlanPanel;

    protected SSTableComboBox<SSAccountPlan> iAccountPlan;

    protected JRadioButton iRadioUseLast;

    protected JRadioButton iRadioAccountPlan;


    private SSNewAccountingYear iAccountingYear;


    /**
     *
     */
    public SSAccountingYearPanel(){
        ButtonGroup iGroup = new ButtonGroup();

        iGroup.add(iRadioUseLast   );
        iGroup.add(iRadioAccountPlan);

        iRadioAccountPlan.addChangeListener( new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                iAccountPlan.setEnabled( iRadioAccountPlan.isSelected() );
            }
        });

        iAccountPlan.setModel   ( SSAccountPlanTableModel.getDropDownModel() );
        iAccountPlan.setSelected( iAccountPlan.getFirst() );
    }


    /**
     *
     * @param pAccountingYear
     */
    public void setAccountingYear( SSNewAccountingYear pAccountingYear  ){
        iAccountingYear = pAccountingYear;


        iFrom       .setDate( iAccountingYear.getFrom() );
        iTo         .setDate( iAccountingYear.getTo  () );
        iAccountPlan.setSelected( iAccountingYear.getAccountPlan() );
    }


    /**
     *
     * @return
     */
    public SSNewAccountingYear getAccountingYear(){
        iAccountingYear.setFrom (iFrom       .getDate()  );
        iAccountingYear.setTo   (iTo         .getDate() );

        if(iAccountPlanPanel.isVisible()){
            SSAccountPlan iAccountPlan = getAccountPlan();

            iAccountingYear.setAccountPlan( new SSAccountPlan(iAccountPlan, true) );
        }
        return iAccountingYear;
    }

    /**
     *
     * @return
     */
    public SSAccountPlan getAccountPlan(){
        SSNewAccountingYear iLast = SSDB.getInstance().getLastYear();

        if(iRadioUseLast.isSelected() && iLast != null && iLast.getAccountPlan() != null) {
            return iLast.getAccountPlan() ;
        } else{
            return iAccountPlan.getSelected();
        }

    }







    /**
     *
     */
    public void setYearFromAndTo(  ){
        SSNewAccountingYear iLast = SSDB.getInstance().getLastYear();

        iRadioUseLast    .setEnabled ( iLast                    != null );
        iRadioAccountPlan.setEnabled ( iAccountPlan.getFirst()  != null );

        if(iLast == null && iAccountPlan.getFirst() == null){
            iButtonPanel.getOkButton().setEnabled(false);
            return;
        }

        if(iLast != null){
            iRadioUseLast.setSelected( true );

            Date iFrom = iLast.getFrom();
            Date iTo   = iLast.getTo();

            Calendar calendarFrom = Calendar.getInstance();
            Calendar calendarTo   = Calendar.getInstance();

            calendarFrom.setTime(iFrom);
            calendarTo  .setTime(iTo);
            calendarTo  .add(Calendar.DAY_OF_MONTH, 1);

            // Get the length for the last year
            int diffYear  = calendarTo.get(Calendar.YEAR        ) - calendarFrom.get(Calendar.YEAR        );
            int diffMonth = calendarTo.get(Calendar.MONTH       ) - calendarFrom.get(Calendar.MONTH       );

            calendarTo  .add(Calendar.DAY_OF_MONTH, -1);

            // Add the length of the year to the to table
            calendarFrom.add(Calendar.YEAR        , diffYear );
            calendarFrom.add(Calendar.MONTH       , diffMonth);
            calendarFrom.set(Calendar.DAY_OF_MONTH, calendarTo.getActualMinimum(Calendar.DAY_OF_MONTH));

            // Add the length of the year to the to table
            calendarTo.add(Calendar.YEAR        , diffYear );
            calendarTo.add(Calendar.MONTH       , diffMonth);
            calendarTo.set(Calendar.DAY_OF_MONTH, calendarTo.getActualMaximum(Calendar.DAY_OF_MONTH));

            this.iFrom.setDate(calendarFrom.getTime() );
            this.iTo.setDate  (calendarTo  .getTime() );
        } else {
            Calendar iCalendar = Calendar.getInstance();

            int year = iCalendar.get(Calendar.YEAR);

            iCalendar.clear();
            iCalendar.set(Calendar.YEAR, year);
            iCalendar.set(Calendar.MONTH, 0);
            iCalendar.set(Calendar.DAY_OF_MONTH, 1);

            iFrom.setDate( iCalendar.getTime() );

            iCalendar.set(Calendar.MONTH, 11);
            iCalendar.set(Calendar.DAY_OF_MONTH, 31);
            iTo.setDate(iCalendar.getTime());

            iAccountPlan.setSelected( iAccountPlan.getFirst() );
        }
    }



    /**
     *
     * @return  The main panel
     */
    public JPanel getPanel() {
        return iPanel;
    }


    /**
     *
     * @param e
     */
    public void addOkAction(ActionListener e){
        iButtonPanel.addOkActionListener(e);
    }

    /**
     *
     * @param e
     */
    public void addCancelAction(ActionListener e){
        iButtonPanel.addCancelActionListener(e);
    }

    /**
     *
     * @param iShow
     */
    public void setShowAccountPlanPanel(boolean iShow) {
        iAccountPlanPanel.setVisible(iShow);
    }

}
