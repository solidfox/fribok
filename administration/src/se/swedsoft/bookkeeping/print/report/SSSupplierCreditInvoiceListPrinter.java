package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSSupplierCreditInvoice;
import se.swedsoft.bookkeeping.data.SSSupplierInvoiceRow;
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
public class SSSupplierCreditInvoiceListPrinter extends SSPrinter {

    private SSInvoiceRowPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    private List<SSSupplierCreditInvoice> iInvoices;

    /**
     *
     */
    public SSSupplierCreditInvoiceListPrinter() {
        this(SSDB.getInstance().getSupplierCreditInvoices() );
    }

    /**
     *
     */
    public SSSupplierCreditInvoiceListPrinter( List<SSSupplierCreditInvoice> iInvoices){
        super();
        // Get all orders
        this.iInvoices = iInvoices;

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("suppliercreditinvoicelist.jrxml");
        setDetail      ("suppliercreditinvoicelist.jrxml");
        setSummary     ("suppliercreditinvoicelist.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("suppliercreditinvoicelistreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {

        iPrinter = new SSSupplierCreditInvoiceListPrinter.SSInvoiceRowPrinter();
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

        SSDefaultTableModel<SSSupplierCreditInvoice> iModel = new SSDefaultTableModel<SSSupplierCreditInvoice>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            @Override
            public Class getType() {
                return SSAccount.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSSupplierCreditInvoice iInvoice = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iInvoice.getNumber();
                        break;
                    case 1:
                        value = iInvoice.getSupplierNr();
                        break;
                    case 2:
                        value = iInvoice.getSupplierName();
                        break;
                    case 3:
                        value = iFormat.format(iInvoice.getDate());
                        break;
                    case 4:
                        value = iInvoice.getCurrency() == null ? null : iInvoice.getCurrency().getName();
                        break;
                    case 5:
                        value = SSSupplierInvoiceMath.getTotalSum(iInvoice);
                        break;
                    case 6:
                        value = iInvoice.getTaxSum();
                        break;
                    case 7:
                        iPrinter.setInvoice(iInvoice);

                        iDataSource.reset();

                        value = iDataSource;
                        break;
                    case 8:
                        BigDecimal iSum = new BigDecimal(0);
                        iSum = iSum.add(SSSupplierInvoiceMath.getTotalSum(iInvoice).multiply(iInvoice.getCurrencyRate()));
                        value = iSum;
                        break;
                    case 9:
                        BigDecimal iTaxSum = new BigDecimal(0);
                        iTaxSum = iTaxSum.add(iInvoice.getTaxSum().multiply(iInvoice.getCurrencyRate()));
                        value = iTaxSum;
                        break;
                }

                return value;
            }
        };

        iModel.addColumn("suppliercreditinvoice.number");
        iModel.addColumn("suppliercreditinvoice.suppliernr");
        iModel.addColumn("suppliercreditinvoice.suppliername");
        iModel.addColumn("suppliercreditinvoice.date");
        iModel.addColumn("suppliercreditinvoice.currency");
        iModel.addColumn("suppliercreditinvoice.sum");
        iModel.addColumn("suppliercreditinvoice.tax");
        iModel.addColumn("suppliercreditinvoice.rows");
        iModel.addColumn("invoice.totalsum");
        iModel.addColumn("invoice.totaltax");




        Collections.sort(iInvoices, new Comparator<SSSupplierCreditInvoice>() {
            public int compare(SSSupplierCreditInvoice o1, SSSupplierCreditInvoice o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });

        iModel.setObjects(iInvoices);


        return iModel;
    }


    private class SSInvoiceRowPrinter extends SSPrinter {

        private SSDefaultTableModel<SSSupplierInvoiceRow> iModel;

        /**
         *
         */
        public SSInvoiceRowPrinter( ){
            setMargins(0,0,0,0);

            setDetail ("suppliercreditinvoicelist.row.jrxml");
            setSummary("suppliercreditinvoicelist.row.jrxml");



            iModel = new SSDefaultTableModel<SSSupplierInvoiceRow>(  ) {

                @Override
                public Class getType() {
                    return SSSaleRow.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    SSSupplierInvoiceRow iRow = getObject(rowIndex);

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
        public void setInvoice(SSSupplierCreditInvoice iInvoice) {
            iModel.setObjects( iInvoice.getRows() );
        }
    }




}
