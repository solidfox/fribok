package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.calc.math.SSAccountMath;
import se.swedsoft.bookkeeping.calc.math.SSDateMath;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSCreditInvoiceMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.util.*;
import java.math.BigDecimal;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSProductRevenuePrinter extends SSPrinter {



    private SSMonthlyDistributionPrinter iPrinter;


    private SSDefaultJasperDataSource iDataSource;

    private List<SSProduct> iProducts;

    private Date  iDateFrom;

    private Date  iDateTo;

    Map<String, Map<SSMonth, BigDecimal>> iProductRevenue;

    /**
     *
     * @param
     */
    public SSProductRevenuePrinter(List<SSProduct> pProducts, Date pFrom, Date pTo ){
        super();
        iProducts = pProducts;
        iDateFrom       = SSDateMath.floor(pFrom);
        iDateTo         = SSDateMath.ceil(pTo);
        calculate();
        setPageHeader  ("header_period.jrxml");
        setColumnHeader("productrevenue.jrxml");
        setDetail      ("productrevenue.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    public String getTitle() {
        return SSBundle.getBundle().getString("productrevenue.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    protected SSDefaultTableModel getModel() {
        addParameter("dateFrom", iDateFrom );
        addParameter("dateTo"  , iDateTo    );

        iPrinter = new SSMonthlyDistributionPrinter(iDateFrom, iDateTo);
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

        SSDefaultTableModel<SSProduct> iModel = new SSDefaultTableModel<SSProduct>() {
            public Class getType() {
                return SSProduct.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSProduct iProduct = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iProduct.getNumber();
                        break;
                    case 1:
                        value = iProduct.getDescription();
                        break;
                    case 2:
                        iPrinter.setProduct(iProduct, iProductRevenue.get(iProduct.getNumber()));
                        iDataSource.reset();
                        value = iDataSource;
                        break;
                }

                return value;
            }
        };

        iModel.addColumn("product.number");
        iModel.addColumn("product.description");
        iModel.addColumn("month.data");

        iModel.setObjects(iProducts);


        return iModel;
    }

    private void calculate(){
        iProductRevenue = new HashMap<String, Map<SSMonth,BigDecimal>>();
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
                    if(iRow.getProductNr() != null && iRow.getSum() != null){
                        BigDecimal iSum = SSInvoiceMath.convertToLocal(iInvoice, iRow.getSum());
                        if(iProductRevenue.containsKey(iRow.getProductNr())){
                            iRevenueInMonth = iProductRevenue.get(iRow.getProductNr());
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
                        iProductRevenue.put(iRow.getProductNr(), iRevenueInMonth);
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
                    if(iRow.getProductNr() != null && iRow.getSum() != null){
                        BigDecimal iSum = SSCreditInvoiceMath.convertToLocal(iCreditInvoice, iRow.getSum());
                        if(iProductRevenue.containsKey(iRow.getProductNr())){
                            iRevenueInMonth = iProductRevenue.get(iRow.getProductNr());
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
                        iProductRevenue.put(iRow.getProductNr(), iRevenueInMonth);
                    }
                }
            }
        }
    }

    private class SSMonthlyDistributionPrinter extends SSPrinter {

        private SSDefaultTableModel<SSMonth> iModel;

        private SSProduct iProduct;
        private Date  iFrom;
        private Date  iTo;

        Map<SSMonth, BigDecimal> iMonthRevenue;
        /**
         *
         * @param pFrom
         * @param pTo
         */
        public SSMonthlyDistributionPrinter(Date pFrom, Date pTo ){
            iFrom = pFrom;
            iTo   = pTo;
            setMargins(0,0,0,0);

            setDetail ("productrevenue.monthly.jrxml");
            setSummary("productrevenue.monthly.jrxml");

            iModel = new SSDefaultTableModel<SSMonth>( SSMonth.splitYearIntoMonths(iFrom,iTo) ) {
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
                            if(iProduct != null && iMonthRevenue.containsKey(iMonth)){
                                value = iMonthRevenue.get(iMonth);
                            }
                            else{
                                value = new BigDecimal(0.00);
                            }
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
        protected SSDefaultTableModel getModel() {
            return iModel;
        }

        /**
         * Gets the title for this report
         *
         * @return The title
         */
        public String getTitle() {
            return null;
        }

        /**
         *
         * @param pProduct
         */
        public void setProduct(SSProduct pProduct, Map<SSMonth, BigDecimal> iMap) {
            iProduct = pProduct;
            iMonthRevenue = iMap;
            if(iMonthRevenue == null)
                iMonthRevenue = new HashMap<SSMonth, BigDecimal>();
        }
    }
}
