package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.SSSupplierInvoiceRow;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.text.DateFormat;
import java.math.BigDecimal;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSSupplierInvoiceListPrinter extends SSPrinter {

    private SSSupplierInvoiceListPrinter.SSInvoiceRowPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    private List<SSSupplierInvoice> iInvoices;

    /**
     *
     */
    public SSSupplierInvoiceListPrinter() {
        this(SSDB.getInstance().getSupplierInvoices() );
    }

    /**
     *
     */
    public SSSupplierInvoiceListPrinter( List<SSSupplierInvoice> iInvoices){
        super();
        // Get all orders
        this.iInvoices = iInvoices;

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("supplierinvoicelist.jrxml");
        setDetail      ("supplierinvoicelist.jrxml");
        setSummary     ("supplierinvoicelist.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    public String getTitle() {
        return SSBundle.getBundle().getString("supplierinvoicelistreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    protected SSDefaultTableModel getModel() {

        iPrinter = new SSSupplierInvoiceListPrinter.SSInvoiceRowPrinter();
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

        SSDefaultTableModel<SSSupplierInvoice> iModel = new SSDefaultTableModel<SSSupplierInvoice>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            public Class getType() {
                return SSAccount.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSSupplierInvoice iInvoice = getObject(rowIndex);

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

        iModel.addColumn("supplierinvoice.number");
        iModel.addColumn("supplierinvoice.suppliernr");
        iModel.addColumn("supplierinvoice.suppliername");
        iModel.addColumn("supplierinvoice.date");
        iModel.addColumn("supplierinvoice.currency");
        iModel.addColumn("supplierinvoice.sum");
        iModel.addColumn("supplierinvoice.tax");

        iModel.addColumn("supplierinvoice.rows");
        iModel.addColumn("invoice.totalsum");
        iModel.addColumn("invoice.totaltax");


        Collections.sort(iInvoices, new Comparator<SSSupplierInvoice>() {
            public int compare(SSSupplierInvoice o1, SSSupplierInvoice o2) {
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

            setDetail ("supplierinvoicelist.row.jrxml");
            setSummary("supplierinvoicelist.row.jrxml");



            iModel = new SSDefaultTableModel<SSSupplierInvoiceRow>(  ) {

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
         * @param iInvoice
         */
        public void setInvoice(SSSupplierInvoice iInvoice) {
            iModel.setObjects( iInvoice.getRows() );
        }
    }




}
