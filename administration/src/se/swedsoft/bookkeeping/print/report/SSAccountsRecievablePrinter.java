package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;
import se.swedsoft.bookkeeping.data.SSCustomer;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.common.SSInvoiceType;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.calc.math.*;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.util.*;
import java.text.DateFormat;
import java.math.BigDecimal;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSAccountsRecievablePrinter extends SSPrinter {


    private SSInvoicePrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    private List<SSCustomer> iCustomers;

    private Date iDate;

    Map<String, List<SSInvoice>> iCustomerInvoicesMap;

    HashMap<Integer,BigDecimal> iInpaymentSum;

    HashMap<Integer, BigDecimal> iCreditInvoiceSum;
    /**
     *
     */
    public SSAccountsRecievablePrinter(Date iDate) {
        this(iDate, SSDB.getInstance().getCustomers() );
    }

    /**
     *
     */
    public SSAccountsRecievablePrinter(Date iDate, List<SSCustomer> iCustomers){
        super();
        // Get all customers
        this.iCustomers = iCustomers;
        this.iDate      = iDate;

        Date iCeiledDate = SSDateMath.ceil(this.iDate);

        iInpaymentSum = SSInpaymentMath.getSumsForInvoices(iCeiledDate);

        iCreditInvoiceSum = SSCreditInvoiceMath.getSumsForInvoices(iCeiledDate);

        iCustomerInvoicesMap = new HashMap<String, List<SSInvoice>>();

        for(String iCustomerNumber : SSCustomerMath.iInvoicesForCustomers.keySet()){
            List<SSInvoice> iInvoicesForCustomer = new LinkedList<SSInvoice>();
            for(SSInvoice iInvoice : SSCustomerMath.iInvoicesForCustomers.get(iCustomerNumber)){
                if(iInvoice.getDate().before(iCeiledDate) && iInvoice.getType() != SSInvoiceType.CASH){
                    iInvoicesForCustomer.add(iInvoice);
                }
            }
            iCustomerInvoicesMap.put(iCustomerNumber,iInvoicesForCustomer);
        }

        addParameter("periodTitle", iBundle.getString("accountsrecievablereport.periodtitle") );
        addParameter("periodText" , iDate);


        setPageHeader  ("header_period.jrxml");
        setColumnHeader("accountsrecievable.jrxml");
        setDetail      ("accountsrecievable.jrxml");
        setSummary     ("accountsrecievable.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    public String getTitle() {
        return SSBundle.getBundle().getString("accountsrecievablereport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    protected SSDefaultTableModel getModel() {



        iPrinter = new SSAccountsRecievablePrinter.SSInvoicePrinter();
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

        SSDefaultTableModel<SSCustomer> iModel = new SSDefaultTableModel<SSCustomer>() {

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
                        List<SSInvoice> iInvoices = iCustomerInvoicesMap.get(iCustomer.getNumber());//SSInvoiceMath.getInvoicesForCustomer(iCustomer, iDate);

                        BigDecimal iSum = new BigDecimal(0);
                        for (SSInvoice iInvoice : iInvoices) {
                            BigDecimal iSaldo = SSInvoiceMath.getTotalSum(iInvoice);//SSInvoiceMath.getSaldo(iInvoice, iDate);
                            if(iInpaymentSum.containsKey(iInvoice.getNumber()))
                                iSaldo = iSaldo.subtract(iInpaymentSum.get(iInvoice.getNumber()));
                            if(iCreditInvoiceSum.containsKey(iInvoice.getNumber()))
                                iSaldo = iSaldo.subtract(iCreditInvoiceSum.get(iInvoice.getNumber()));

                            iSum = iSum.add(SSInvoiceMath.convertToLocal(iInvoice,  iSaldo));
                        }
                        value = iSum;
                        break;
                    case 3:
                        iPrinter.setCustomer(iCustomer);

                        iDataSource.reset();

                        value = iDataSource;
                        break;
                }

                return value;
            }
        };

        iModel.addColumn("customer.number");
        iModel.addColumn("customer.name");
        iModel.addColumn("customer.saldosum");
        iModel.addColumn("customer.invoices");


        Collections.sort(iCustomers, new Comparator<SSCustomer>() {
            public int compare(SSCustomer o1, SSCustomer o2) {
                return o1.getNumber().compareTo( o2.getNumber() );
            }
        });

        iModel.setObjects(iCustomers);


        return iModel;
    }





    private class SSInvoicePrinter extends SSPrinter {

        private SSDefaultTableModel<SSInvoice> iModel;



        /**
         *
         */
        public SSInvoicePrinter( ){
            setMargins(0,0,0,0);

            setDetail ("accountsrecievable.row.jrxml");
            setSummary("accountsrecievable.row.jrxml");


            iModel = new SSDefaultTableModel<SSInvoice>(  ) {

                DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

                public Class getType() {
                    return SSSaleRow.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    SSInvoice iInvoice = getObject(rowIndex);

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
                            value = SSInvoiceMath.getTotalSum(iInvoice);
                            break;
                        case 4:
                            value = iCreditInvoiceSum.get(iInvoice.getNumber()) == null ? new BigDecimal(0.00) : iCreditInvoiceSum.get(iInvoice.getNumber());//SSCreditInvoiceMath.getSumForInvoice(iInvoice, iDate);
                            break;
                        case 5:
                            value = iInpaymentSum.get(iInvoice.getNumber()) == null ? new BigDecimal(0.00) : iInpaymentSum.get(iInvoice.getNumber());//SSInpaymentMath.getSumForInvoice(iInvoice, iDate);
                            break;
                        case 6:
                            value = iInvoice.getCurrencyRate();
                            break;
                        case 7:
                            BigDecimal iSaldo = SSInvoiceMath.getTotalSum(iInvoice);//SSInvoiceMath.getSaldo(iInvoice, iDate);
                            if(iInpaymentSum.containsKey(iInvoice.getNumber()))
                                iSaldo = iSaldo.subtract(iInpaymentSum.get(iInvoice.getNumber()));
                            if(iCreditInvoiceSum.containsKey(iInvoice.getNumber()))
                                iSaldo = iSaldo.subtract(iCreditInvoiceSum.get(iInvoice.getNumber()));

                            value = SSInvoiceMath.convertToLocal(iInvoice, iSaldo);
                            break;
                    }

                    return value;
                }
            };

            iModel.addColumn("invoice.number");
            iModel.addColumn("invoice.date");
            iModel.addColumn("invoice.currency");
            iModel.addColumn("invoice.value");
            iModel.addColumn("invoice.credited");
            iModel.addColumn("invoice.payed");
            iModel.addColumn("invoice.currencyrate");
            iModel.addColumn("invoice.localsaldo");
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
         * @param iCustomer
         */
        public void setCustomer(SSCustomer iCustomer) {
            List<SSInvoice> iInvoices = iCustomerInvoicesMap.get(iCustomer.getNumber());//SSInvoiceMath.getInvoicesForCustomer(iCustomer, iDate);

            iModel.setObjects( iInvoices );
        }
    }




}
