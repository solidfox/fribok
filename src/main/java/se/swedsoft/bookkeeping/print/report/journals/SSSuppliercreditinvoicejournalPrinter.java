package se.swedsoft.bookkeeping.print.report.journals;


import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSSupplierCreditInvoice;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.data.SSVoucherRow;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSSuppliercreditinvoicejournalPrinter extends SSPrinter {

    private Integer iNumber;

    private List<SSSupplierCreditInvoice> iInvoices;

    private SSVoucherPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    /**
     *
     * @param iInvoices
     * @param iNumber
     * @param iDate
     */
    public SSSuppliercreditinvoicejournalPrinter(List<SSSupplierCreditInvoice> iInvoices, Integer iNumber, Date iDate) {
        this.iInvoices = iInvoices;
        this.iNumber = iNumber;

        setPageHeader("header_period.jrxml");
        setColumnHeader("journals/suppliercreditinvoicejournal.jrxml");
        setDetail("journals/suppliercreditinvoicejournal.jrxml");
        setSummary("journals/suppliercreditinvoicejournal.jrxml");

        addParameter("periodTitle",
                iBundle.getString("suppliercreditinvoicejournal.periodtitle"));
        addParameter("periodText", iDate);
    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return String.format(iBundle.getString("suppliercreditinvoicejournal.title"),
                iNumber);
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
        // Get all invoices
        // List<SSInvoice> iInvoices = new LinkedList<SSInvoice>( iSaldos.keySet() );

        iPrinter = new SSVoucherPrinter();
        iPrinter.generateReport();

        addParameter("Report", iPrinter.getReport());
        addParameter("Parameters", iPrinter.getParameters());

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

        // Sort the invoices
        Collections.sort(iInvoices, new Comparator<SSSupplierCreditInvoice>() {
            public int compare(SSSupplierCreditInvoice o1, SSSupplierCreditInvoice o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });

        SSDefaultTableModel<SSSupplierCreditInvoice> iModel = new SSDefaultTableModel<SSSupplierCreditInvoice>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            @Override
            public Class getType() {
                return SSInvoice.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSSupplierInvoice iInvoice = getObject(rowIndex);

                switch (columnIndex) {
                case 0:
                    value = iInvoice.getNumber();
                    break;

                case 1:
                    value = iInvoice.getSupplierNr();
                    break;

                case 2:
                    value = iInvoice.getSupplierName();
                    break;

                case 3:
                    value = iInvoice.getDate() == null
                            ? null
                            : iFormat.format(iInvoice.getDate());
                    break;

                case 4:
                    value = iInvoice.getCurrency() == null
                            ? null
                            : iInvoice.getCurrency().getName();
                    break;

                case 5:
                    value = iInvoice.getCurrencyRate();
                    break;

                case 6:
                    value = SSSupplierInvoiceMath.getTotalSum(iInvoice);
                    break;

                case 7:
                    value = iInvoice.getTaxSum();
                    break;

                case 8:
                    BigDecimal iTotalSum = SSSupplierInvoiceMath.getTotalSum(iInvoice);

                    value = SSSupplierInvoiceMath.convertToLocal(iInvoice, iTotalSum);
                    break;

                case 9:
                    iPrinter.setInvoice(iInvoice);

                    iDataSource.reset();

                    value = iDataSource;
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
        iModel.addColumn("suppliercreditinvoice.currencyrate");
        iModel.addColumn("suppliercreditinvoice.sum");
        iModel.addColumn("suppliercreditinvoice.tax");
        iModel.addColumn("suppliercreditinvoice.localsum");

        iModel.addColumn("journal.rows");

        iModel.setObjects(iInvoices);

        return iModel;
    }

    private class SSVoucherPrinter extends SSPrinter {

        private SSDefaultTableModel<SSVoucherRow> iModel;

        /**
         *
         */
        public SSVoucherPrinter() {
            setMargins(0, 0, 0, 0);

            setDetail("journals/suppliercreditinvoicejournal.rows.jrxml");
            // setSummary("journals/invoicejournal.rows.jrxml");

            iModel = new SSDefaultTableModel<SSVoucherRow>() {

                DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

                @Override
                public Class getType() {
                    return SSVoucherRow.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    SSVoucherRow iRow = getObject(rowIndex);

                    switch (columnIndex) {
                    case 0:
                        value = iRow.getAccount() == null
                                ? null
                                : iRow.getAccount().getNumber();
                        break;

                    case 1:
                        value = iRow.getAccount() == null
                                ? null
                                : iRow.getAccount().getDescription();
                        break;

                    case 2:
                        value = iRow.getDebet();
                        break;

                    case 3:
                        value = iRow.getCredit();
                        break;

                    case 4:
                        value = iRow.getProject() == null
                                ? null
                                : iRow.getProject().getNumber();
                        break;

                    case 5:
                        value = iRow.getResultUnit() == null
                                ? null
                                : iRow.getResultUnit().getNumber();
                        break;

                    }

                    return value;
                }
            };

            iModel.addColumn("row.account");
            iModel.addColumn("row.description");
            iModel.addColumn("row.debet");
            iModel.addColumn("row.credet");
            iModel.addColumn("row.project");
            iModel.addColumn("row.resultunit");
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
        public void setInvoice(SSSupplierInvoice iInvoice) {

            iModel.setObjects(iInvoice.getVoucher().getRows());
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();

            sb.append(
                    "se.swedsoft.bookkeeping.print.report.journals.SSSuppliercreditinvoicejournalPrinter.SSVoucherPrinter");
            sb.append("{iModel=").append(iModel);
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(
                "se.swedsoft.bookkeeping.print.report.journals.SSSuppliercreditinvoicejournalPrinter");
        sb.append("{iDataSource=").append(iDataSource);
        sb.append(", iInvoices=").append(iInvoices);
        sb.append(", iNumber=").append(iNumber);
        sb.append(", iPrinter=").append(iPrinter);
        sb.append('}');
        return sb.toString();
    }
}
