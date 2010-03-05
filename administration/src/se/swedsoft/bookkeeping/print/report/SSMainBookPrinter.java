package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.calc.SSMainBookCalculator;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.data.SSNewResultUnit;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.*;

import static se.swedsoft.bookkeeping.calc.SSMainBookCalculator.SSMainBookRow;

/**
 * Date: 2006-feb-17
 * Time: 09:25:44
 */
public class SSMainBookPrinter extends SSPrinter {


    SSNewAccountingYear iYearData;

    SSAccount iAccountFrom;
    SSAccount iAccountTo;

    Date iDateFrom;
    Date iDateTo;
    private SSNewProject iProject;
    private SSNewResultUnit iResultUnit;


    /**
     *
     */
    public SSMainBookPrinter(SSAccount pAccountFrom, SSAccount pAccountTo, Date pDateFrom, Date pDateTo, SSNewProject iProject, SSNewResultUnit iResultUnit){
        this( SSDB.getInstance().getCurrentYear(), pAccountFrom, pAccountTo, pDateFrom, pDateTo, iProject, iResultUnit );
    }

    /**
     *
     * @param pYearData The year
     */
    public SSMainBookPrinter(SSNewAccountingYear pYearData, SSAccount pAccountFrom, SSAccount pAccountTo, Date pDateFrom, Date pDateTo, SSNewProject iProject, SSNewResultUnit iResultUnit){
        super();
        iYearData       = pYearData;
        iAccountFrom    = pAccountFrom;
        iAccountTo      = pAccountTo;
        iDateFrom       = pDateFrom;
        iDateTo         = pDateTo;
        this.iProject    = iProject;
        this.iResultUnit = iResultUnit;

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("mainbook.jrxml");
        setDetail      ("mainbook.jrxml");
    }



    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("mainbookreport.title");
    }


    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
       final DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

        addParameter("dateFrom", iDateFrom );
        addParameter("dateTo"  , iDateTo);

        String iPeriodText = String.format(SSBundle.getBundle().getString("mainbookreport.period.account"),  iAccountFrom.getNumber(), iAccountTo.getNumber() );


        if( iProject != null){
            iPeriodText = iPeriodText + String.format(SSBundle.getBundle().getString("mainbookreport.period.project"), iProject.getNumber());
        }
        if( iResultUnit != null){
            iPeriodText = iPeriodText + String.format(SSBundle.getBundle().getString("mainbookreport.period.resultunit"), iResultUnit.getNumber());
        }


        addParameter("periodTitle", iPeriodText) ;
        addParameter("periodText",  "" );

        SSMainBookCalculator iCalculator = new SSMainBookCalculator(iYearData, iAccountFrom, iAccountTo, iDateFrom, iDateTo, iProject, iResultUnit);

        // Calculate all fields
        iCalculator.calculate();

        final List<SSMainBookRow>        iRows      = iCalculator.getRows();

        final Map<SSAccount, BigDecimal> iInBalance = iCalculator.getInBalance();
        final Map<SSAccount, BigDecimal> iInSaldo   = iCalculator.getInSaldo();


        SSDefaultTableModel<SSMainBookRow> iModel = new SSDefaultTableModel<SSMainBookRow>(){

            @Override
            public Class getType() {
                return SSAccount.class;

            }
            public Object getValueAt(int rowIndex, int columnIndex) {
                SSMainBookRow iRow = getObject(rowIndex);

                SSAccount    iAccount = iRow.getAccount();


                Object value = null;

                switch(columnIndex){
                    case 0:
                        // account.number
                        value = iAccount.getNumber();
                        break;
                    case 1:
                        // account.description
                        value = iAccount.getDescription();
                        break;
                    case 2:
                        // account.insaldo
                        value = iInSaldo.get(iAccount);
                        break;
                    case 3:
                        // account.inbalance
                        value = iInBalance.get(iAccount);
                        break;

                    case 4:
                        // isrow
                        value = iRow.getHasdata();
                        break;
                    case 5:
                        // voucher.number
                        value = iRow.getNumber();
                        break;
                    case 6:
                        // voucher.date
                        value =  iRow.getDate();
                        if(value != null) value = iFormat.format(iRow.getDate());
                        break;
                    case 7:
                        // voucher.description
                        value = iRow.getDescription();
                        break;
                    case 8:
                        // voucherrow.added
                        value = iRow.isAdded();
                        break;

                    case 9:
                        // voucherrow.crossed
                        value = iRow.isCrossed();
                        break;

                    case 10:
                        // voucherrow.debet
                        value = iRow.getDebet();
                        break;
                    case 11:
                        // voucherrow.credit
                        value = iRow.getCredit();
                        break;
                    case 12:
                        // voucherrow.saldo
                        value = iRow.getSum();
                        break;
                }

                return value;
            }

        };

        iModel.addColumn( "account.number" );
        iModel.addColumn( "account.description" );
        iModel.addColumn( "account.insaldo" );
        iModel.addColumn( "account.inbalance");

        iModel.addColumn( "isrow");
        iModel.addColumn( "voucher.number");
        iModel.addColumn( "voucher.date");
        iModel.addColumn( "voucher.description");

        iModel.addColumn( "voucherrow.added");
        iModel.addColumn( "voucherrow.crossed");
        iModel.addColumn( "voucherrow.debet");
        iModel.addColumn( "voucherrow.credit");
        iModel.addColumn( "voucherrow.sum");


        Collections.sort(iRows, new Comparator<SSMainBookRow>() {
            public int compare(SSMainBookRow o1, SSMainBookRow o2) {
                return o1.getAccount().getNumber().compareTo(o2.getAccount().getNumber());
            }
        });

        iModel.setObjects( iRows );


        return iModel;
    }





}
