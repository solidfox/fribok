package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSInvoiceListPrinter extends SSPrinter {

    private SSInvoiceRowPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    private List<SSInvoice> iInvoices;

    /**
     *
     */
    public SSInvoiceListPrinter() {
        this(SSDB.getInstance().getInvoices() );
    }

    /**
     *
     * @param iInvoices
     */
    public SSInvoiceListPrinter( List<SSInvoice> iInvoices){
        super();
        // Get all orders
        this.iInvoices = iInvoices;

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("invoicelist.jrxml");
        setDetail      ("invoicelist.jrxml");
        setSummary     ("invoicelist.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("invoicelistreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {

        iPrinter = new SSInvoiceRowPrinter();
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

        SSDefaultTableModel<SSInvoice> iModel = new SSDefaultTableModel<SSInvoice>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            @Override
            public Class getType() {
                return SSAccount.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSInvoice iInvoice = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iInvoice.getNumber();
                        break;
                    case 1:
                        value = iInvoice.getCustomerNr();
                        break;
                    case 2:
                        value = iInvoice.getCustomerName();
                        break;
                    case 3:
                        value = iFormat.format(iInvoice.getDate());
                        break;
                    case 4:
                        iPrinter.setInvoice(iInvoice);

                        iDataSource.reset();

                        value = iDataSource;
                        break;
                    case 5:
                        value = iInvoice.getCurrency() == null ? null : iInvoice.getCurrency().getName();
                        break;
                    case 6:
                        value = SSInvoiceMath.getTotalSum(iInvoice);
                        break;
                    case 7:
                        value = SSInvoiceMath.getTotalTaxSum(iInvoice);
                        break;
                    case 8:
                        BigDecimal iSum = new BigDecimal(0);
                        iSum = iSum.add(SSInvoiceMath.getTotalSum(iInvoice).multiply(iInvoice.getCurrencyRate()));
                        value = iSum;
                        break;
                    case 9:
                        BigDecimal iTaxSum = new BigDecimal(0);
                        iTaxSum = iTaxSum.add(SSInvoiceMath.getTotalTaxSum(iInvoice).multiply(iInvoice.getCurrencyRate()));
                        value = iTaxSum;
                        break;
                }

                return value;
            }
        };

        iModel.addColumn("invoice.number");
        iModel.addColumn("invoice.customernr");
        iModel.addColumn("invoice.customername");
        iModel.addColumn("invoice.date");
        iModel.addColumn("invoice.rows");
        iModel.addColumn("invoice.currency");
        iModel.addColumn("invoice.sum");
        iModel.addColumn("invoice.tax");
        iModel.addColumn("invoice.totalsum");
        iModel.addColumn("invoice.totaltax");



        Collections.sort(iInvoices, new Comparator<SSInvoice>() {
            public int compare(SSInvoice o1, SSInvoice o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });

        iModel.setObjects(iInvoices);


        return iModel;
    }


    private class SSInvoiceRowPrinter extends SSPrinter {

        private SSDefaultTableModel<SSSaleRow> iModel;

        /**
         *
         */
        public SSInvoiceRowPrinter( ){
            setMargins(0,0,0,0);

            setDetail ("invoicelist.row.jrxml");
            setSummary("invoicelist.row.jrxml");



            iModel = new SSDefaultTableModel<SSSaleRow>(  ) {

                @Override
                public Class getType() {
                    return SSSaleRow.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    SSSaleRow iRow = getObject(rowIndex);

                    switch (columnIndex) {
                        case 0  :
                            value = iRow.getProductNr();
                            break;
                        case 1:
                            value = iRow.getDescription();
                            break;
                        case 2:
                            value = iRow.getQuantity();
                            break;
                        case 3:
                            value = iRow.getUnit() == null ? null : iRow.getUnit().getName();
                            break;
                        case 4:
                            value = iRow.getUnitprice();
                            break;
                        case 5:
                            value = iRow.getDiscount();
                            break;
                        case 6:
                            value = iRow.getSum();
                            break;
                    }

                    return value;
                }
            };

            iModel.addColumn("row.number");
            iModel.addColumn("row.description");
            iModel.addColumn("row.count");
            iModel.addColumn("row.unit");
            iModel.addColumn("row.unitprice");
            iModel.addColumn("row.discount");
            iModel.addColumn("row.sum");
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
         * @param iInvoice
         */
        public void setInvoice(SSInvoice iInvoice) {
            iModel.setObjects( iInvoice.getRows() );
        }
    }




}
