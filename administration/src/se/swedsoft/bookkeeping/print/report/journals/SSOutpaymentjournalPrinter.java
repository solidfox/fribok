package se.swedsoft.bookkeeping.print.report.journals;

import se.swedsoft.bookkeeping.calc.math.SSOutpaymentMath;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSOutpayment;
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
public class SSOutpaymentjournalPrinter extends SSPrinter {

    private Integer iNumber;

    private List<SSOutpayment> iOutpayments;

    private SSVoucherPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    /**
     *
     */
    public SSOutpaymentjournalPrinter( List<SSOutpayment> iOutpayments, Integer iNumber, Date iDate){
        super();
        this.iOutpayments = iOutpayments;
        this.iNumber      = iNumber;

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("journals/outpaymentjournal.jrxml");
        setDetail      ("journals/outpaymentjournal.jrxml");
        setSummary     ("journals/outpaymentjournal.jrxml");

        addParameter("periodTitle", iBundle.getString("outpaymentjournal.periodtitle") );
        addParameter("periodText" , iDate );
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return String.format(iBundle.getString("outpaymentjournal.title"), iNumber);
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
        iPrinter = new SSOutpaymentjournalPrinter.SSVoucherPrinter();
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());


        // Sort the invoices
        Collections.sort(iOutpayments, new Comparator<SSOutpayment>() {
            public int compare(SSOutpayment o1, SSOutpayment o2) {
                return o1.getNumber() - o2.getNumber();
            }
        });

        SSDefaultTableModel<SSOutpayment> iModel = new SSDefaultTableModel<SSOutpayment>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            @Override
            public Class getType() {
                return SSInvoice.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSOutpayment iOutpayment = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iOutpayment.getNumber();
                        break;
                    case 1:
                        value = iOutpayment.getText();
                        break;
                    case 2:
                        value = iOutpayment.getDate();
                        break;
                    case 3:
                        value = SSOutpaymentMath.getSum(iOutpayment);
                        break;
                    case 4:
                        iPrinter.setOutpayment(iOutpayment);

                        iDataSource.reset();

                        value = iDataSource;
                        break;
                }


                return value;
            }
        };

        iModel.addColumn("outpayment.number");
        iModel.addColumn("outpayment.text");
        iModel.addColumn("outpayment.date");
        iModel.addColumn("outpayment.sum");

        iModel.addColumn("journal.rows");

        iModel.setObjects(iOutpayments);

        return iModel;
    }


    private class SSVoucherPrinter extends SSPrinter {

        private SSDefaultTableModel<SSVoucherRow> iModel;

        /**
         *
         */
        public SSVoucherPrinter( ){
            setMargins(0,0,0,0);

            setDetail ("journals/outpaymentjournal.rows.jrxml");


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
         * @param iOutpayment
         */
        public void setOutpayment(SSOutpayment iOutpayment) {

            iModel.setObjects( iOutpayment.getVoucher().getRows() );
        }
    }




}
