package se.swedsoft.bookkeeping.print.report.journals;

import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSVoucherRow;
import se.swedsoft.bookkeeping.data.SSSupplierInvoice;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.calc.math.SSSupplierInvoiceMath;

import java.util.List;
import java.util.Date;
import java.util.Collections;
import java.util.Comparator;
import java.text.DateFormat;
import java.math.BigDecimal;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSSupplierInvoicejournalPrinter extends SSPrinter {

    private Integer iNumber;

    private List<SSSupplierInvoice> iInvoices;

    private SSSupplierInvoicejournalPrinter.SSVoucherPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    /**
     *
     */
    public SSSupplierInvoicejournalPrinter( List<SSSupplierInvoice> iInvoices, Integer iNumber, Date iDate){
        super();
        this.iInvoices = iInvoices;
        this.iNumber   = iNumber;

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("journals/supplierinvoicejournal.jrxml");
        setDetail      ("journals/supplierinvoicejournal.jrxml");
        setSummary     ("journals/supplierinvoicejournal.jrxml");

        addParameter("periodTitle", iBundle.getString("supplierinvoicejournal.periodtitle") );
        addParameter("periodText" , iDate );
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    public String getTitle() {
        return String.format(iBundle.getString("supplierinvoicejournal.title"), iNumber);
    }

    /**
     * @return SSDefaultTableModel
     */
    protected SSDefaultTableModel getModel() {

        iPrinter = new SSSupplierInvoicejournalPrinter.SSVoucherPrinter();
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());


        // Sort the invoices
        Collections.sort(iInvoices, new Comparator<SSSupplierInvoice>() {
            public int compare(SSSupplierInvoice o1, SSSupplierInvoice o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });


        SSDefaultTableModel<SSSupplierInvoice> iModel = new SSDefaultTableModel<SSSupplierInvoice>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            public Class getType() {
                return SSInvoice.class;
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
                        value = iInvoice.getDate()     == null ? null : iFormat.format( iInvoice.getDate() );
                        break;
                    case 4:
                        value = iInvoice.getCurrency() == null ? null : iInvoice.getCurrency().getName();
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

                        value = SSSupplierInvoiceMath.convertToLocal(iInvoice,  iTotalSum);
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

        iModel.addColumn("supplierinvoice.number");
        iModel.addColumn("supplierinvoice.suppliernr");
        iModel.addColumn("supplierinvoice.suppliername");
        iModel.addColumn("supplierinvoice.date");
        iModel.addColumn("supplierinvoice.currency");
        iModel.addColumn("supplierinvoice.currencyrate");
        iModel.addColumn("supplierinvoice.sum");
        iModel.addColumn("supplierinvoice.tax");
        iModel.addColumn("supplierinvoice.localsum");

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
           // setSummary("journals/invoicejournal.rows.jrxml");


            iModel = new SSDefaultTableModel<SSVoucherRow>(  ) {

                DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

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

            iModel.setObjects( iInvoice.getVoucher().getRows() );
        }
    }



}
