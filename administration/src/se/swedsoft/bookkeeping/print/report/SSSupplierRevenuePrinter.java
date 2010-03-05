package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.calc.math.SSDateMath;
import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;

import java.util.*;
import java.math.BigDecimal;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSSupplierRevenuePrinter extends SSPrinter {



    private SSMonthlyDistributionPrinter iPrinter;


    private SSDefaultJasperDataSource iDataSource;

    private List<SSSupplier> iSuppliers;

    private Date  iDateFrom;

    private Date  iDateTo;

    private Map<String, Map<SSMonth, BigDecimal>> iSupplierRevenue;
    /**
     *
     * @param
     */
    public SSSupplierRevenuePrinter(List<SSSupplier> pSuppliers, Date pFrom, Date pTo ){
        super();
        iSuppliers = pSuppliers;
        iDateFrom       = SSDateMath.floor(pFrom);
        iDateTo         = SSDateMath.ceil(pTo);
        calculate();
        setPageHeader  ("header_period.jrxml");
        setColumnHeader("supplierrevenue.jrxml");
        setDetail      ("supplierrevenue.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("supplierrevenue.title");
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

        SSDefaultTableModel<SSSupplier> iModel = new SSDefaultTableModel<SSSupplier>() {
            @Override
            public Class getType() {
                return SSSupplier.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSSupplier iSupplier = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iSupplier.getNumber();
                        break;
                    case 1:
                        value = iSupplier.getName();
                        break;
                    case 2:
                        iPrinter.setSupplier(iSupplier, iSupplierRevenue.get(iSupplier.getNumber()));
                        iDataSource.reset();
                        value = iDataSource;
                        break;
                }

                return value;
            }
        };

        iModel.addColumn("supplier.number");
        iModel.addColumn("supplier.description");
        iModel.addColumn("month.data");

        iModel.setObjects(iSuppliers);


        return iModel;
    }


    private void calculate(){
        iSupplierRevenue = new HashMap<String, Map<SSMonth,BigDecimal>>();
        List<SSSupplierInvoice> iSupplierInvoices = SSDB.getInstance().getSupplierInvoices();
        for(SSSupplierInvoice iSupplierInvoice : iSupplierInvoices){
            if(iSupplierInvoice.getDate().after(iDateFrom) && iSupplierInvoice.getDate().before(iDateTo)){
                Calendar iCal = Calendar.getInstance();
                iCal.setTime(iSupplierInvoice.getDate());
                iCal.set(Calendar.DAY_OF_MONTH, 1);
                Date iFrom = iCal.getTime();
                iCal.set(Calendar.DAY_OF_MONTH, iCal.getActualMaximum(Calendar.DAY_OF_MONTH) );
                Date iTo = iCal.getTime();
                SSMonth iMonth = new SSMonth(iFrom, iTo);

                Map<SSMonth,BigDecimal> iRevenueInMonth;
                if(iSupplierInvoice.getSupplierNr() != null && SSSupplierInvoiceMath.getNetSum(iSupplierInvoice) != null){
                    BigDecimal iSum = SSSupplierInvoiceMath.convertToLocal(iSupplierInvoice, SSSupplierInvoiceMath.getNetSum(iSupplierInvoice));
                    if(iSupplierRevenue.containsKey(iSupplierInvoice.getSupplierNr())){
                        iRevenueInMonth = iSupplierRevenue.get(iSupplierInvoice.getSupplierNr());
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
                    iSupplierRevenue.put(iSupplierInvoice.getSupplierNr(), iRevenueInMonth);
                }
            }
        }

        List<SSSupplierCreditInvoice> iSupplierCreditInvoices = SSDB.getInstance().getSupplierCreditInvoices();
        for(SSSupplierCreditInvoice iSupplierCreditInvoice : iSupplierCreditInvoices){
            if(iSupplierCreditInvoice.getDate().after(iDateFrom) && iSupplierCreditInvoice.getDate().before(iDateTo)){
                Calendar iCal = Calendar.getInstance();
                iCal.setTime(iSupplierCreditInvoice.getDate());
                iCal.set(Calendar.DAY_OF_MONTH, 1);
                Date iFrom = iCal.getTime();
                iCal.set(Calendar.DAY_OF_MONTH, iCal.getActualMaximum(Calendar.DAY_OF_MONTH) );
                Date iTo = iCal.getTime();
                SSMonth iMonth = new SSMonth(iFrom, iTo);

                Map<SSMonth,BigDecimal> iRevenueInMonth;
                if(iSupplierCreditInvoice.getSupplierNr() != null && SSSupplierInvoiceMath.getNetSum(iSupplierCreditInvoice) != null){
                    BigDecimal iSum = SSSupplierInvoiceMath.convertToLocal(iSupplierCreditInvoice, SSSupplierInvoiceMath.getNetSum(iSupplierCreditInvoice));
                    if(iSupplierRevenue.containsKey(iSupplierCreditInvoice.getSupplierNr())){
                        iRevenueInMonth = iSupplierRevenue.get(iSupplierCreditInvoice.getSupplierNr());
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
                    iSupplierRevenue.put(iSupplierCreditInvoice.getSupplierNr(), iRevenueInMonth);
                }
            }
        }
    }


    private class SSMonthlyDistributionPrinter extends SSPrinter {

        private SSDefaultTableModel<SSMonth> iModel;

        private SSSupplier iSupplier;
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

            setDetail ("supplierrevenue.monthly.jrxml");
            setSummary("supplierrevenue.monthly.jrxml");



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
                            if(iSupplier != null && iRevenue.containsKey(iMonth))
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
         * @param pSupplier
         */
        public void setSupplier(SSSupplier pSupplier, Map<SSMonth, BigDecimal> iMap) {
            iSupplier = pSupplier;
            iRevenue = iMap;
            if(iRevenue == null)
                iRevenue = new HashMap<SSMonth, BigDecimal>();
        }
    }




}
