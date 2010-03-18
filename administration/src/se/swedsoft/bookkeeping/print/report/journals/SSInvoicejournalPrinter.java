package se.swedsoft.bookkeeping.print.report.journals;

import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.SSInvoice;
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
public class SSInvoicejournalPrinter extends SSPrinter {

    private Integer iNumber;

    private List<SSInvoice> iInvoices;

    private SSVoucherPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    /**
     *
     * @param iInvoices
     * @param iNumber
     * @param iDate
     */
    public SSInvoicejournalPrinter( List<SSInvoice> iInvoices, Integer iNumber, Date iDate){
        this.iInvoices = iInvoices;
        this.iNumber   = iNumber;

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("journals/invoicejournal.jrxml");
        setDetail      ("journals/invoicejournal.jrxml");
        setSummary     ("journals/invoicejournal.jrxml");

        addParameter("periodTitle", iBundle.getString("invoicejournal.periodtitle") );
        addParameter("periodText" , iDate );
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return String.format(iBundle.getString("invoicejournal.title"), iNumber);
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
        iPrinter = new SSVoucherPrinter();
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());


        // Sort the invoices
        Collections.sort(iInvoices, new Comparator<SSInvoice>() {
            public int compare(SSInvoice o1, SSInvoice o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });


        SSDefaultTableModel<SSInvoice> iModel = new SSDefaultTableModel<SSInvoice>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            @Override
            public Class getType() {
                return SSInvoice.class;
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
                        value = iInvoice.getDate()     == null ? null : iFormat.format( iInvoice.getDate() );
                        break;
                    case 4:
                        value = iInvoice.getCurrency() == null ? null : iInvoice.getCurrency().getName();
                        break;
                    case 5:
                        value = iInvoice.getCurrencyRate();
                        break;
                    case 6:
                        value = SSInvoiceMath.getTotalSum(iInvoice);
                        break;
                    case 7:
                        value = SSInvoiceMath.getTotalTaxSum(iInvoice);
                        break;
                    case 8:
                        BigDecimal iTotalSum = SSInvoiceMath.getTotalSum(iInvoice);

                        value = SSInvoiceMath.convertToLocal(iInvoice,  iTotalSum);
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

        iModel.addColumn("invoice.number");
        iModel.addColumn("invoice.customernr");
        iModel.addColumn("invoice.customername");
        iModel.addColumn("invoice.date");
        iModel.addColumn("invoice.currency");
        iModel.addColumn("invoice.currencyrate");
        iModel.addColumn("invoice.sum");
        iModel.addColumn("invoice.tax");
        iModel.addColumn("invoice.localsum");

        iModel.addColumn("journal.rows");

        iModel.setObjects(iInvoices);

        return iModel;
    }




    private class SSVoucherPrinter extends SSPrinter {

        private SSDefaultTableModel<SSVoucherRow> iModel;

        /**
         *
         */
        public SSVoucherPrinter( ){
            setMargins(0,0,0,0);

            setDetail ("journals/invoicejournal.rows.jrxml");

            iModel = new SSDefaultTableModel<SSVoucherRow>(  ) {

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
                            value = iRow.getAccount() == null ? null : iRow.getAccount().getNumber();
                            break;
                        case 1:
                            value = iRow.getAccount() == null ? null : iRow.getAccount().getDescription();
                            break;
                        case 2:
                            value = iRow.getDebet();
                            break;
                        case 3:
                            value = iRow.getCredit();
                            break;
                        case 4:
                            value = iRow.getProject() == null ? null : iRow.getProject().getNumber();
                            break;
                        case 5:
                            value = iRow.getResultUnit() == null ? null : iRow.getResultUnit().getNumber();
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
        public void setInvoice(SSInvoice iInvoice) {

            iModel.setObjects( iInvoice.getVoucher().getRows() );
        }
    }



}
