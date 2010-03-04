package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.calc.math.SSAccountMath;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSCreditInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSDateMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.util.*;
import java.math.BigDecimal;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSCustomerRevenuePrinter extends SSPrinter {



    private SSMonthlyDistributionPrinter iPrinter;


    private SSDefaultJasperDataSource iDataSource;

    private List<SSCustomer> iCustomers;

    private Date  iDateFrom;

    private Date  iDateTo;

    Map<String, Map<SSMonth, BigDecimal>> iCustomerRevenue;
    /**
     *
     * @param
     */
    public SSCustomerRevenuePrinter(List<SSCustomer> pCustomers, Date pFrom, Date pTo ){
        super();
        iCustomers = pCustomers;
        iDateFrom       = SSDateMath.floor(pFrom);
        iDateTo         = SSDateMath.ceil(pTo);
        calculate();
        setPageHeader  ("header_period.jrxml");
        setColumnHeader("customerrevenue.jrxml");
        setDetail      ("customerrevenue.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("customerrevenue.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
        addParameter("dateFrom", iDateFrom );
        addParameter("dateTo"  , iDateTo    );

        iPrinter = new SSMonthlyDistributionPrinter(iDateFrom, iDateTo);
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

        SSDefaultTableModel<SSCustomer> iModel = new SSDefaultTableModel<SSCustomer>() {
            @Override
            public Class getType() {
                return SSCustomer.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSCustomer iCustomer = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iCustomer.getNumber();
                        break;
                    case 1:
                        value = iCustomer.getName();
                        break;
                    case 2:
                        iPrinter.setCustomer(iCustomer, iCustomerRevenue.get(iCustomer.getNumber()));
                        iDataSource.reset();
                        value = iDataSource;
                        break;
                }

                return value;
            }
        };

        iModel.addColumn("customer.number");
        iModel.addColumn("customer.description");
        iModel.addColumn("month.data");

        iModel.setObjects(iCustomers);


        return iModel;
    }

    private void calculate(){
        iCustomerRevenue = new HashMap<String, Map<SSMonth,BigDecimal>>();
        List<SSInvoice> iInvoices = SSDB.getInstance().getInvoices();
        for(SSInvoice iInvoice : iInvoices){
            if(iInvoice.getDate().after(iDateFrom) && iInvoice.getDate().before(iDateTo)){
                Calendar iCal = Calendar.getInstance();
                iCal.setTime(iInvoice.getDate());
                iCal.set(Calendar.DAY_OF_MONTH, 1);
                Date iFrom = iCal.getTime();
                iCal.set(Calendar.DAY_OF_MONTH, iCal.getActualMaximum(Calendar.DAY_OF_MONTH) );
                Date iTo = iCal.getTime();
                SSMonth iMonth = new SSMonth(iFrom, iTo);

                Map<SSMonth,BigDecimal> iRevenueInMonth;
                if(iInvoice.getCustomerNr() != null && SSInvoiceMath.getNetSum(iInvoice) != null){
                    BigDecimal iSum = SSInvoiceMath.convertToLocal(iInvoice, SSInvoiceMath.getNetSum(iInvoice));
                    if(iCustomerRevenue.containsKey(iInvoice.getCustomerNr())){
                        iRevenueInMonth = iCustomerRevenue.get(iInvoice.getCustomerNr());
                        if(iRevenueInMonth.containsKey(iMonth)){
                            iRevenueInMonth.put(iMonth,iRevenueInMonth.get(iMonth).add(iSum));
                        }
                        else{
                            iRevenueInMonth.put(iMonth, iSum);
                        }
                    }
                    else{
                        iRevenueInMonth = new HashMap<SSMonth, BigDecimal>();
                        iRevenueInMonth.put(iMonth, iSum);
                    }
                    iCustomerRevenue.put(iInvoice.getCustomerNr(), iRevenueInMonth);
                }
            }
        }

        List<SSCreditInvoice> iCreditInvoices = SSDB.getInstance().getCreditInvoices();
        for(SSCreditInvoice iCreditInvoice : iCreditInvoices){
            if(iCreditInvoice.getDate().after(iDateFrom) && iCreditInvoice.getDate().before(iDateTo)){
                Calendar iCal = Calendar.getInstance();
                iCal.setTime(iCreditInvoice.getDate());
                iCal.set(Calendar.DAY_OF_MONTH, 1);
                Date iFrom = iCal.getTime();
                iCal.set(Calendar.DAY_OF_MONTH, iCal.getActualMaximum(Calendar.DAY_OF_MONTH) );
                Date iTo = iCal.getTime();
                SSMonth iMonth = new SSMonth(iFrom, iTo);

                Map<SSMonth,BigDecimal> iRevenueInMonth;
                if(iCreditInvoice.getCustomerNr() != null && SSCreditInvoiceMath.getNetSum(iCreditInvoice) != null){
                    BigDecimal iSum = SSCreditInvoiceMath.convertToLocal(iCreditInvoice, SSCreditInvoiceMath.getNetSum(iCreditInvoice));
                    if(iCustomerRevenue.containsKey(iCreditInvoice.getCustomerNr())){
                        iRevenueInMonth = iCustomerRevenue.get(iCreditInvoice.getCustomerNr());
                        if(iRevenueInMonth.containsKey(iMonth)){
                            iRevenueInMonth.put(iMonth,iRevenueInMonth.get(iMonth).subtract(iSum));
                        }
                        else{
                            iRevenueInMonth.put(iMonth, iSum.negate());
                        }
                    }
                    else{
                        iRevenueInMonth = new HashMap<SSMonth, BigDecimal>();
                        iRevenueInMonth.put(iMonth, iSum.negate());
                    }
                    iCustomerRevenue.put(iCreditInvoice.getCustomerNr(), iRevenueInMonth);
                }
            }
        }
    }



    private class SSMonthlyDistributionPrinter extends SSPrinter {

        private SSDefaultTableModel<SSMonth> iModel;

        private SSCustomer iCustomer;
        private Date  iFrom;

        private Date  iTo;

        Map<SSMonth, BigDecimal> iRevenue;
        /**
         *
         * @param pFrom
         * @param pTo
         */
        public SSMonthlyDistributionPrinter(Date pFrom, Date pTo ){
            iFrom = pFrom;
            iTo   = pTo;
            setMargins(0,0,0,0);

            setDetail ("customerrevenue.monthly.jrxml");
            setSummary("customerrevenue.monthly.jrxml");



            iModel = new SSDefaultTableModel<SSMonth>( SSMonth.splitYearIntoMonths(iFrom,iTo) ) {

                @Override
                public Class getType() {
                    return SSMonth.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    SSMonth iMonth = getObject(rowIndex);

                    switch (columnIndex) {
                        case 0  :
                            value = iMonth.toString();
                            break;
                        case 1  :
                            value = iMonth.getName();
                            break;
                        case 2:
                            if(iCustomer != null && iRevenue.containsKey(iMonth))
                                value = iRevenue.get(iMonth);
                            else
                                value = new BigDecimal(0.00);
                            break;
                        case 3  :
                            value = iMonth.isBetween(iFrom, iTo);
                            break;
                    }

                    return value;
                }
            };

            iModel.addColumn("month.date");
            iModel.addColumn("month.description");
            iModel.addColumn("month.value");
            iModel.addColumn("month.visible");
        }

        /**
         * Gets the data model for this report
         *
         * @return SSDefaultTableModel
         */
        @Override
        protected SSDefaultTableModel getModel() {
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

        /**
         *
         * @param pCustomer
         */
        public void setCustomer(SSCustomer pCustomer, Map<SSMonth, BigDecimal> iMap) {
            iCustomer = pCustomer;
            iRevenue = iMap;
            if(iRevenue == null)
                iRevenue = new HashMap<SSMonth, BigDecimal>();
        }
    }




}
