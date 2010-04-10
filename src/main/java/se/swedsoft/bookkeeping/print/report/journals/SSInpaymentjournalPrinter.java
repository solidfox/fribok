package se.swedsoft.bookkeeping.print.report.journals;


import se.swedsoft.bookkeeping.calc.math.SSInpaymentMath;
import se.swedsoft.bookkeeping.data.SSInpayment;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSVoucherRow;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSInpaymentjournalPrinter extends SSPrinter {

    private Integer iNumber;

    private List<SSInpayment> iInpayments;

    private SSVoucherPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    /**
     *
     * @param iInpayments
     * @param iNumber
     * @param iDate
     */
    public SSInpaymentjournalPrinter(List<SSInpayment> iInpayments, Integer iNumber, Date iDate) {
        this.iInpayments = iInpayments;
        this.iNumber = iNumber;

        setPageHeader("header_period.jrxml");
        setColumnHeader("journals/inpaymentjournal.jrxml");
        setDetail("journals/inpaymentjournal.jrxml");
        setSummary("journals/inpaymentjournal.jrxml");

        addParameter("periodTitle", iBundle.getString("inpaymentjournal.periodtitle"));
        addParameter("periodText", iDate);
    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return String.format(iBundle.getString("inpaymentjournal.title"), iNumber);
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
        Collections.sort(iInpayments, new Comparator<SSInpayment>() {
            public int compare(SSInpayment o1, SSInpayment o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });

        SSDefaultTableModel<SSInpayment> iModel = new SSDefaultTableModel<SSInpayment>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            @Override
            public Class getType() {
                return SSInvoice.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSInpayment iInpayment = getObject(rowIndex);

                switch (columnIndex) {
                case 0:
                    value = iInpayment.getNumber();
                    break;

                case 1:
                    value = iInpayment.getText();
                    break;

                case 2:
                    value = iInpayment.getDate();
                    break;

                case 3:
                    value = SSInpaymentMath.getSum(iInpayment);
                    break;

                case 4:
                    iPrinter.setInpayment(iInpayment);

                    iDataSource.reset();

                    value = iDataSource;
                    break;
                }

                return value;
            }
        };

        iModel.addColumn("inpayment.number");
        iModel.addColumn("inpayment.text");
        iModel.addColumn("inpayment.date");
        iModel.addColumn("inpayment.sum");

        iModel.addColumn("journal.rows");

        iModel.setObjects(iInpayments);

        return iModel;
    }

    private class SSVoucherPrinter extends SSPrinter {

        private SSDefaultTableModel<SSVoucherRow> iModel;

        /**
         *
         */
        public SSVoucherPrinter() {
            setMargins(0, 0, 0, 0);

            setDetail("journals/inpaymentjournal.rows.jrxml");

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
         * @param iInpayment
         */
        public void setInpayment(SSInpayment iInpayment) {

            iModel.setObjects(iInpayment.getVoucher().getRows());
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();

            sb.append(
                    "se.swedsoft.bookkeeping.print.report.journals.SSInpaymentjournalPrinter.SSVoucherPrinter");
            sb.append("{iModel=").append(iModel);
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(
                "se.swedsoft.bookkeeping.print.report.journals.SSInpaymentjournalPrinter");
        sb.append("{iDataSource=").append(iDataSource);
        sb.append(", iInpayments=").append(iInpayments);
        sb.append(", iNumber=").append(iNumber);
        sb.append(", iPrinter=").append(iPrinter);
        sb.append('}');
        return sb.toString();
    }
}
