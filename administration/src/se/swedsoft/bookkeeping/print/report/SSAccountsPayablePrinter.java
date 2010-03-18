package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.calc.math.*;
import se.swedsoft.bookkeeping.data.SSSupplier;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.*;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSAccountsPayablePrinter extends SSPrinter {


    private SSAccountsPayablePrinter.SSInvoicePrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    private List<SSSupplier> iSuppliers;

    private Date iDate;

    Map<String, List<SSSupplierInvoice>> iSupplierInvoicesMap;

    HashMap<Integer,BigDecimal> iOutpaymentSum;

    HashMap<Integer, BigDecimal> iSupplierCreditInvoiceSum;

    /**
     *
     * @param iDate
     */
    public SSAccountsPayablePrinter(Date iDate) {
        this(iDate, SSDB.getInstance().getSuppliers() );
    }

    /**
     *
     * @param iDate
     * @param iSuppliers
     */
    public SSAccountsPayablePrinter(Date iDate, List<SSSupplier> iSuppliers){
        // Get all customers
        this.iSuppliers = iSuppliers;
        this.iDate      = iDate;

        Date iCeiledDate = SSDateMath.ceil(this.iDate);

        iOutpaymentSum = SSOutpaymentMath.getSumsForSupplierInvoices(iCeiledDate);

        iSupplierCreditInvoiceSum = SSSupplierCreditInvoiceMath.getSumsForSupplierInvoices(iCeiledDate);

        iSupplierInvoicesMap = new HashMap<String, List<SSSupplierInvoice>>();

        for(String iSupplierNumber : SSSupplierMath.iInvoicesForSuppliers.keySet()){
            List<SSSupplierInvoice> iInvoicesForCustomer = new LinkedList<SSSupplierInvoice>();
            for(SSSupplierInvoice iInvoice : SSSupplierMath.iInvoicesForSuppliers.get(iSupplierNumber)){
                if(iInvoice.getDate().before(iCeiledDate)){
                    iInvoicesForCustomer.add(iInvoice);
                }
            }
            iSupplierInvoicesMap.put(iSupplierNumber,iInvoicesForCustomer);
        }
        addParameter("periodTitle", iBundle.getString("accountspayablereport.periodtitle") );
        addParameter("periodText" , iDate);


        setPageHeader  ("header_period.jrxml");
        setColumnHeader("accountspayable.jrxml");
        setDetail      ("accountspayable.jrxml");
        setSummary     ("accountspayable.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("accountspayablereport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {

        iPrinter = new SSAccountsPayablePrinter.SSInvoicePrinter();
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
                          List<SSSupplierInvoice> iInvoices = iSupplierInvoicesMap.get(iSupplier.getNumber());

                        BigDecimal iSum = new BigDecimal(0);
                        for (SSSupplierInvoice iInvoice : iInvoices) {
                            BigDecimal iSaldo = SSSupplierInvoiceMath.getTotalSum(iInvoice);
                            if(iOutpaymentSum.containsKey(iInvoice.getNumber()))
                                iSaldo = iSaldo.subtract(iOutpaymentSum.get(iInvoice.getNumber()));
                            if(iSupplierCreditInvoiceSum.containsKey(iInvoice.getNumber()))
                                iSaldo = iSaldo.subtract(iSupplierCreditInvoiceSum.get(iInvoice.getNumber()));

                            iSum = iSum.add(SSSupplierInvoiceMath.convertToLocal(iInvoice,  iSaldo));
                        }
                        value = iSum;
                        break;

                          //List<SSSupplierInvoice> iInvoices = SSSupplierInvoiceMath.getInvoicesForSupplier(iSupplier, iDate);

                         //value = SSSupplierInvoiceMath.getSaldoSum(iInvoices, iDate);
                    case 3:
                        iPrinter.setSupplier(iSupplier);

                        iDataSource.reset();

                        value = iDataSource;
                        break;
                }

                return value;
            }
        };

        iModel.addColumn("supplier.number");
        iModel.addColumn("supplier.name");
        iModel.addColumn("supplier.saldosum");
        iModel.addColumn("supplier.invoices");


        Collections.sort(iSuppliers, new Comparator<SSSupplier>() {
            public int compare(SSSupplier o1, SSSupplier o2) {
                return o1.getNumber().compareTo( o2.getNumber() );
            }
        });

        iModel.setObjects(iSuppliers);


        return iModel;
    }





    private class SSInvoicePrinter extends SSPrinter {

        private SSDefaultTableModel<SSSupplierInvoice> iModel;



        /**
         *
         */
        public SSInvoicePrinter( ){
            setMargins(0,0,0,0);

            setDetail ("accountspayable.row.jrxml");
            setSummary("accountspayable.row.jrxml");


            iModel = new SSDefaultTableModel<SSSupplierInvoice>(  ) {

                DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

                @Override
                public Class getType() {
                    return SSSaleRow.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    SSSupplierInvoice iInvoice = getObject(rowIndex);

                    switch (columnIndex) {
                        case 0  :
                            value = iInvoice.getNumber();
                            break;
                        case 1:
                            value = iFormat.format(iInvoice.getDate());
                            break;
                        case 2:
                            value = iInvoice.getCurrency() == null ? null : iInvoice.getCurrency().getName();
                            break;
                        case 3:
                            value = SSSupplierInvoiceMath.getTotalSum(iInvoice);
                            break;
                        case 4:
                            value = iSupplierCreditInvoiceSum.get(iInvoice.getNumber()) == null ? new BigDecimal(0.00) : iSupplierCreditInvoiceSum.get(iInvoice.getNumber());//value = SSSupplierCreditInvoiceMath.getSumForInvoice(iInvoice, iDate);
                            break;
                        case 5:
                            value = iOutpaymentSum.get(iInvoice.getNumber()) == null ? new BigDecimal(0.00) : iOutpaymentSum.get(iInvoice.getNumber());//value = SSOutpaymentMath    .getSumForInvoice(iInvoice, iDate);
                            break;
                        case 6:
                            value = iInvoice.getCurrencyRate();
                            break;
                        case 7:
                            //BigDecimal iSaldo = SSSupplierInvoiceMath.getSaldo(iInvoice, iDate);

                            //value = SSSupplierInvoiceMath.convertToLocal(iInvoice, iSaldo);
                            //break;
                            BigDecimal iSaldo = SSSupplierInvoiceMath.getTotalSum(iInvoice);//SSInvoiceMath.getSaldo(iInvoice, iDate);
                            if(iOutpaymentSum.containsKey(iInvoice.getNumber()))
                                iSaldo = iSaldo.subtract(iOutpaymentSum.get(iInvoice.getNumber()));
                            if(iSupplierCreditInvoiceSum.containsKey(iInvoice.getNumber()))
                                iSaldo = iSaldo.subtract(iSupplierCreditInvoiceSum.get(iInvoice.getNumber()));

                            value = SSSupplierInvoiceMath.convertToLocal(iInvoice, iSaldo);
                    }

                    return value;
                }
            };

            iModel.addColumn("supplierinvoice.number");
            iModel.addColumn("supplierinvoice.date");
            iModel.addColumn("supplierinvoice.currency");
            iModel.addColumn("supplierinvoice.value");
            iModel.addColumn("supplierinvoice.credited");
            iModel.addColumn("supplierinvoice.payed");
            iModel.addColumn("supplierinvoice.currencyrate");
            iModel.addColumn("supplierinvoice.localsaldo");
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
         * @param iSupplier
         */
        public void setSupplier(SSSupplier iSupplier) {
            List<SSSupplierInvoice> iInvoices = iSupplierInvoicesMap.get(iSupplier.getNumber());//SSSupplierInvoiceMath.getInvoicesForSupplier(iSupplier, iDate);

            iModel.setObjects( iInvoices );
        }
    }




}
