package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSInvoice;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.data.SSVoucherRow;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 2006-mar-02
 * Time: 16:06:12
 */
public class SSVoucherPrinter extends SSPrinter {


    private String iTitle;

    private SSVoucher iVoucher;

    private List<SSAccount> iMarkedAccounts;

    private SSVoucherRowPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;


    /**
     *
     * @param pVoucher
     * @param pTitle
     */
    public SSVoucherPrinter(SSVoucher pVoucher, String pTitle ){
        iVoucher        = pVoucher;
        iTitle          = pTitle;
        iMarkedAccounts = Collections.emptyList();

        setPageHeader  ("header.jrxml");
        setDetail      ("voucher.jrxml");
        setColumnHeader("voucher.jrxml");
    }

    /**
     *
     * @param pVoucher
     * @param pTitle
     * @param pMarkedAccounts
     */
    public SSVoucherPrinter(SSVoucher pVoucher, String pTitle, SSAccount ... pMarkedAccounts ){
        this(pVoucher, pTitle);

        iMarkedAccounts = new LinkedList<SSAccount>();
        iMarkedAccounts.addAll(Arrays.asList(pMarkedAccounts));
   }

    /**
     *
     * @param pVoucher
     * @param pTitle
     * @param pMarkedAccounts
     */
    public SSVoucherPrinter(SSVoucher pVoucher, String pTitle, List<SSAccount> pMarkedAccounts){
        this(pVoucher, pTitle);
        iMarkedAccounts = pMarkedAccounts;
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return iTitle;
    }




    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
        iPrinter = new SSVoucherRowPrinter();
        iPrinter.generateReport();

        addParameter("Report"      , iPrinter.getReport());
        addParameter("Parameters"  , iPrinter.getParameters() );

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());
        SSDefaultTableModel<SSVoucher> iModel = new SSDefaultTableModel<SSVoucher>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            @Override
            public Class getType() {
                return SSInvoice.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSVoucher iVoucher = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :
                        value = iVoucher.getNumber();
                        break;
                    case 1:
                        value = iVoucher.getDate()== null ? null : iFormat.format( iVoucher.getDate() );
                        break;
                    case 2:
                        value = iVoucher.getDescription();
                        break;
                    case 3:
                        value = SSVoucherMath.getDebetSum(iVoucher);
                        break;
                    case 4:
                        value = SSVoucherMath.getCreditSum(iVoucher);
                        break;

                    case 5:
                        iPrinter.setRows(iVoucher.getRows());

                        iDataSource.reset();

                        value = iDataSource;
                        break;
                }


                return value;
            }
        };

        iModel.addColumn( "voucher.number" );
        iModel.addColumn( "voucher.date" );
        iModel.addColumn( "voucher.description" );
        iModel.addColumn( "voucher.debet" );
        iModel.addColumn( "voucher.credet" );

        iModel.addColumn( "voucher.rows" );

        iModel.setObjects( iVoucher );


        return iModel;

//        return iModel;
    }




    private class SSVoucherRowPrinter extends SSPrinter {

        private SSDefaultTableModel<SSVoucherRow> iModel;

        /**
         *
         */
        public SSVoucherRowPrinter( ){
            setMargins(0,0,0,0);

            setDetail ("voucher.rows.jrxml");
            setSummary("voucher.rows.jrxml");

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
                        case 6:
                            value = iRow.isAdded();
                            break;
                        case 7:
                            value = iRow.isCrossed();
                            break;
                        case 8:
                            value = iMarkedAccounts.contains(iRow.getAccount());
                            break;

                    }

                    return value;
                }
            };

            iModel.addColumn("voucherrow.account");
            iModel.addColumn("voucherrow.description");
            iModel.addColumn("voucherrow.debet");
            iModel.addColumn("voucherrow.credet");
            iModel.addColumn("voucherrow.project");
            iModel.addColumn("voucherrow.resultunit");
            iModel.addColumn("voucherrow.isadded");
            iModel.addColumn("voucherrow.iscrossed");
            iModel.addColumn("voucherrow.ismarked" );
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
         * @param iRows
         */
        public void setRows(List<SSVoucherRow> iRows) {

            iModel.setObjects( iRows );
        }
    }
}


