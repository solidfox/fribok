package se.swedsoft.bookkeeping.print.report.sales;

import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.SSStandardText;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;

import java.math.BigDecimal;
import java.util.*;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSReminderPrinter extends SSPrinter {

    private List<SSInvoice> iInvoices;

    private SSCustomer iCustomer;

    private Locale iLocale;

    private Integer iMaxReminders;

    /**
     *
     * @param iInvoices
     * @param iCustomer
     * @param iLocale
     */
    public SSReminderPrinter(List<SSInvoice> iInvoices, SSCustomer iCustomer, Locale iLocale){
        this.iInvoices = iInvoices;
        this.iCustomer = iCustomer;
        this.iLocale   = iLocale;

        ResourceBundle iBundle = ResourceBundle.getBundle("reports.reminderreport", iLocale);

        setBundle( iBundle );
        setLocale( iLocale );

        setMargins(0,0,0,0);

        setPageHeader  ("sales/sale.header.jrxml");
        setPageFooter  ("sales/sale.footer.jrxml");

        setDetail      ("sales/reminder.jrxml");
        setColumnHeader("sales/reminder.jrxml");

        addParameters();

    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        addParameter("title.number"   , iBundle.getString("reminderreport.title.date") );

        return iBundle.getString("reminderreport.title");
    }



    /**
     *
     */
    private void addParameters(){
        SSNewCompany iCompany = SSDB.getInstance().getCurrentCompany();

        SSSalePrinterUtils.addParametersForCompany(iCompany, this);

        // Sale parameters
        addParameter("date"     , new Date() );
        addParameter("text"     , iCompany.getStandardText(SSStandardText.Reminder) );

        if(iCustomer != null){

            addParameter("customer.invoiceadress.name"    , iCustomer.getInvoiceAddress().getName() );
            addParameter("customer.invoiceadress.address1", iCustomer.getInvoiceAddress().getAddress1() );
            addParameter("customer.invoiceadress.address2", iCustomer.getInvoiceAddress().getAddress2() );
            addParameter("customer.invoiceadress.zipcode" , iCustomer.getInvoiceAddress().getZipCode() );
            addParameter("customer.invoiceadress.city"    , iCustomer.getInvoiceAddress().getCity() );
            addParameter("customer.invoiceadress.country" , iCustomer.getInvoiceAddress().getCountry() );

            addParameter("customer.customernr"  , iCustomer.getNumber() );
            addParameter("customer.currency"    , iCustomer.getInvoiceCurrency() , true);
            addParameter("customer.ourcontact"  , iCustomer.getOurContactPerson() );
            addParameter("customer.yourcontact" , iCustomer.getYourContactPerson() );
        }
        iMaxReminders = 0;
        for (SSInvoice iInvoice : iInvoices) {
            Integer iNumReminders = iInvoice.getNumReminders();

            if(iNumReminders > iMaxReminders) iMaxReminders = iNumReminders;
        }
        iMaxReminders = iMaxReminders + 1;

        BigDecimal iReminderfee = iCompany.getReminderfee();

        if(iReminderfee != null) {
            addParameter("reminder.reminderfee", iReminderfee.multiply( new BigDecimal(iMaxReminders)) );
        } else {
            addParameter("reminder.reminderfee", new BigDecimal(0) );
        }



    }


    /**
     *
     * @return
     */
    @Override
    protected SSDefaultTableModel getModel() {
        final SSPrinter iPrinter = new SSRowReport(  );

        iPrinter.setBundle(iBundle);
        iPrinter.setLocale(iLocale);
        iPrinter.generateReport();

        addParameter("subreport.report"      , iPrinter.getReport());
        addParameter("subreport.parameters"  , iPrinter.getParameters() );
        addParameter("subreport.datasource"  , iPrinter.getDataSource() );

        SSDefaultTableModel<SSInvoice> iModel = new SSDefaultTableModel<SSInvoice>(  ) {

            @Override
            public Class getType() {
                return SSInvoice.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                iPrinter.getDataSource().reset();

                return iPrinter.getDataSource();
            }
        };

        iModel.addColumn("subreport.datasource");

        iModel.setObjects(iInvoices.get(0));

        return iModel;
    }




    /**
     *
     */
    private class SSRowReport extends SSPrinter {

        /**
         *
         */
        public SSRowReport( ){
            setMargins(0,0,0,0);

            setColumnHeader("sales/reminder.rows.jrxml");
            setDetail      ("sales/reminder.rows.jrxml");
        }

        /**
         * Gets the data model for this report
         *
         * @return SSDefaultTableModel
         */
        @Override
        protected SSDefaultTableModel getModel() {
            SSDefaultTableModel<SSInvoice> iModel = new SSDefaultTableModel<SSInvoice>(  ) {

                @Override
                public Class getType() {
                    return SSSaleRow.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    SSInvoice iInvoice = getObject(rowIndex);

                    switch (columnIndex) {
                        case 0  :
                            if(iInvoice.getOCRNumber() != null){
                                value =  iInvoice.getOCRNumber();
                            } else {
                                value = iInvoice.getNumber().toString();
                            }
                            break;
                        case 1:
                            value = iInvoice.getDate();
                            break;
                        case 2:
                            value = iInvoice.getDueDate();
                            break;
                        case 3:
                            value = getNumDelayedDays(iInvoice);
                            break;
                        case 4:
                            value = iInvoice.getNumReminders() + 1;
                            break;
                        case 5:
                            value = SSInvoiceMath.getSaldo(iInvoice.getNumber());
                            break;
                        case 6:
                            value = iInvoice.getDelayInterest() ;
                            break;
                        case 7:
                            value = SSInvoiceMath.getInterestSum(iInvoice, SSInvoiceMath.getSaldo(iInvoice.getNumber()), getNumDelayedDays(iInvoice));
                            break;
                    }

                    return value;
                }
            };


            iModel.addColumn("invoice.number");
            iModel.addColumn("invoice.date");
            iModel.addColumn("invoice.duedate");
            iModel.addColumn("invoice.duedays");
            iModel.addColumn("invoice.numreminders");
            iModel.addColumn("invoice.saldo");
            iModel.addColumn("invoice.delayinterest");
            iModel.addColumn("invoice.delayfee");


            iModel.setObjects(iInvoices);

            return iModel;
        }

        /**
         * Gets the title for this report
         *
         * @return The title
         */
        @Override
        public String getTitle() {
            return null;
        }


    }



    /**
     *
     * @param iInvoice
     * @return
     */
    private static int getNumDelayedDays(SSInvoice iInvoice){
        Date iDueDate = iInvoice.getDueDate();
        Date iNowDate  = new Date();

        if(iDueDate == null ) return 0;

        Calendar iCalendar = Calendar.getInstance();

        iCalendar.setTimeInMillis(  iNowDate.getTime() - iDueDate.getTime() );

        int iYear = iCalendar.get(Calendar.YEAR       ) - 1970 ;
        int iDay  = iCalendar.get(Calendar.DAY_OF_YEAR) ;

        return iYear * iCalendar.getActualMaximum(Calendar.DAY_OF_YEAR) + iDay;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.print.report.sales.SSReminderPrinter");
        sb.append("{iCustomer=").append(iCustomer);
        sb.append(", iInvoices=").append(iInvoices);
        sb.append(", iLocale=").append(iLocale);
        sb.append(", iMaxReminders=").append(iMaxReminders);
        sb.append('}');
        return sb.toString();
    }
}
