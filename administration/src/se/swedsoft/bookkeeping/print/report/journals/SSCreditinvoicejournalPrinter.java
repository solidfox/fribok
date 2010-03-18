package se.swedsoft.bookkeeping.print.report.journals;

import se.swedsoft.bookkeeping.calc.math.SSInvoiceMath;
import se.swedsoft.bookkeeping.data.SSCreditInvoice;
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
public class SSCreditinvoicejournalPrinter extends SSPrinter {

    private Integer iNumber;

    private List<SSCreditInvoice> iCreditInvoices;

    private SSCreditinvoicejournalPrinter.SSVoucherPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    /**
     *
     * @param iCreditInvoices
     * @param iNumber
     * @param iDate
     */
    public SSCreditinvoicejournalPrinter( List<SSCreditInvoice> iCreditInvoices, Integer iNumber, Date iDate){
        this.iCreditInvoices = iCreditInvoices;
        this.iNumber         = iNumber;

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("journals/creditinvoicejournal.jrxml");
        setDetail      ("journals/creditinvoicejournal.jrxml");
        setSummary     ("journals/creditinvoicejournal.jrxml");

        addParameter("periodTitle", iBundle.getString("creditinvoicejournal.periodtitle") );
        addParameter("periodText" , iDate );
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return String.format(iBundle.getString("creditinvoicejournal.title"), iNumber);
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {

        iPrinter = new SSCreditinvoicejournalPrinter.SSVoucherPrinter();
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());


        // Sort the invoices
        Collections.sort(iCreditInvoices, new Comparator<SSInvoice>() {
            public int compare(SSInvoice o1, SSInvoice o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });


        SSDefaultTableModel<SSCreditInvoice> iModel = new SSDefaultTableModel<SSCreditInvoice>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            @Override
            public Class getType() {
                return SSInvoice.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSCreditInvoice iCreditInvoice = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iCreditInvoice.getNumber();
                        break;
                    case 1:
                        value = iCreditInvoice.getCustomerNr();
                        break;
                    case 2:
                        value = iCreditInvoice.getCustomerName();
                        break;
                    case 3:
                        value = iCreditInvoice.getDate()     == null ? null : iFormat.format( iCreditInvoice.getDate() );
                        break;
                    case 4:
                        value = iCreditInvoice.getCurrency() == null ? null : iCreditInvoice.getCurrency().getName();
                        break;
                    case 5:
                        value = iCreditInvoice.getCurrencyRate();
                        break;
                    case 6:
                        value = SSInvoiceMath.getTotalSum(iCreditInvoice);
                        break;
                    case 7:
                        value = SSInvoiceMath.getTotalTaxSum(iCreditInvoice);
                        break;
                    case 8:
                        BigDecimal iTotalSum = SSInvoiceMath.getTotalSum(iCreditInvoice);

                        value = SSInvoiceMath.convertToLocal(iCreditInvoice,  iTotalSum);
                        break;
                    case 9:
                        iPrinter.setInvoice(iCreditInvoice);

                        iDataSource.reset();

                        value = iDataSource;
                        break;
                }


                return value;
            }
        };

        iModel.addColumn("creditinvoice.number");
        iModel.addColumn("creditinvoice.customernr");
        iModel.addColumn("creditinvoice.customername");
        iModel.addColumn("creditinvoice.date");
        iModel.addColumn("creditinvoice.currency");
        iModel.addColumn("creditinvoice.currencyrate");
        iModel.addColumn("creditinvoice.sum");
        iModel.addColumn("creditinvoice.tax");
        iModel.addColumn("creditinvoice.localsum");

        iModel.addColumn("journal.rows");

        iModel.setObjects(iCreditInvoices);

        return iModel;
    }




    private class SSVoucherPrinter extends SSPrinter {

        private SSDefaultTableModel<SSVoucherRow> iModel;

        /**
         *
         */
        public SSVoucherPrinter( ){
            setMargins(0,0,0,0);

            setDetail ("journals/creditinvoicejournal.rows.jrxml");
           // setSummary("journals/invoicejournal.rows.jrxml");


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
         * @param iCreditInvoice
         */
        public void setInvoice(SSCreditInvoice iCreditInvoice) {

            iModel.setObjects( iCreditInvoice.getVoucher().getRows() );
        }
    }



}
