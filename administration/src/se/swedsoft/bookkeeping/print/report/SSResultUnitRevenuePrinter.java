package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.calc.math.SSCreditInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSDateMath;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.SSCreditInvoice;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSMonth;
import se.swedsoft.bookkeeping.data.SSNewResultUnit;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;

import java.math.BigDecimal;
import java.util.*;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSResultUnitRevenuePrinter extends SSPrinter {



    private SSMonthlyDistributionPrinter iPrinter;


    private SSDefaultJasperDataSource iDataSource;

    private List<SSNewResultUnit> iResultUnits;

    private Date  iDateFrom;

    private Date  iDateTo;

    private Map<String, Map<SSMonth,BigDecimal>> iResultUnitRevenue;
    /**
     * 
     * @param pResultUnits
     * @param pFrom
     * @param pTo
     */
    public SSResultUnitRevenuePrinter(List<SSNewResultUnit> pResultUnits, Date pFrom, Date pTo ){
        iResultUnits = pResultUnits;
        iDateFrom       = SSDateMath.floor(pFrom);
        iDateTo         = SSDateMath.ceil(pTo);
        calculate();
        setPageHeader  ("header_period.jrxml");
        setColumnHeader("resultunitrevenue.jrxml");
        setDetail      ("resultunitrevenue.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("resultunitrevenue.title");
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

        SSDefaultTableModel<SSNewResultUnit> iModel = new SSDefaultTableModel<SSNewResultUnit>() {
            @Override
            public Class getType() {
                return SSNewResultUnit.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSNewResultUnit iResultUnit = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iResultUnit.getNumber();
                        break;
                    case 1:
                        value = iResultUnit.getName();
                        break;
                    case 2:
                        iPrinter.setResultUnit(iResultUnit, iResultUnitRevenue.get(iResultUnit.getNumber()));
                        iDataSource.reset();
                        value = iDataSource;
                        break;
                }

                return value;
            }
        };

        iModel.addColumn("resultunit.number");
        iModel.addColumn("resultunit.description");
        iModel.addColumn("month.data");

        iModel.setObjects(iResultUnits);


        return iModel;
    }


    private void calculate(){
        iResultUnitRevenue = new HashMap<String, Map<SSMonth,BigDecimal>>();
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
                for(SSSaleRow iRow : iInvoice.getRows()){
                    if(iRow.getResultUnitNr() != null && iRow.getSum() != null){
                        BigDecimal iSum = SSInvoiceMath.convertToLocal(iInvoice, iRow.getSum());
                        if(iResultUnitRevenue.containsKey(iRow.getResultUnitNr())){
                            iRevenueInMonth = iResultUnitRevenue.get(iRow.getResultUnitNr());
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
                        iResultUnitRevenue.put(iRow.getResultUnitNr(), iRevenueInMonth);
                    }
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
                for(SSSaleRow iRow : iCreditInvoice.getRows()){
                    if(iRow.getResultUnitNr() != null && iRow.getSum() != null){
                        BigDecimal iSum = SSCreditInvoiceMath.convertToLocal(iCreditInvoice, iRow.getSum());
                        if(iResultUnitRevenue.containsKey(iRow.getResultUnitNr())){
                            iRevenueInMonth = iResultUnitRevenue.get(iRow.getResultUnitNr());
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
                        iResultUnitRevenue.put(iRow.getResultUnitNr(), iRevenueInMonth);
                    }
                }
            }
        }
    }


    private class SSMonthlyDistributionPrinter extends SSPrinter {

        private SSDefaultTableModel<SSMonth> iModel;

        private SSNewResultUnit iResultUnit;
        private Date  iFrom;

        private Date  iTo;

        private Map<SSMonth, BigDecimal> iRevenue;
        /**
         *
         * @param pFrom
         * @param pTo
         */
        public SSMonthlyDistributionPrinter(Date pFrom, Date pTo ){
            iFrom = pFrom;
            iTo   = pTo;
            setMargins(0,0,0,0);

            setDetail ("resultunitrevenue.monthly.jrxml");
            setSummary("resultunitrevenue.monthly.jrxml");



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
                            if(iResultUnit != null && iRevenue.containsKey(iMonth))
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
         * @param pResultUnit
         * @param iMap
         */
        public void setResultUnit(SSNewResultUnit pResultUnit, Map<SSMonth, BigDecimal> iMap) {
            iResultUnit = pResultUnit;
            iRevenue = iMap;
            if(iRevenue == null)
                iRevenue = new HashMap<SSMonth, BigDecimal>();
        }
    }




}
