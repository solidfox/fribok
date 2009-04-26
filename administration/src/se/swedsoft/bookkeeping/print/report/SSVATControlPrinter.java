package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.calc.SSSalesTaxCalculator;

import java.util.List;
import java.util.Date;
import java.math.BigDecimal;

import static se.swedsoft.bookkeeping.calc.SSSalesTaxCalculator.SSVATControlGroup;

/**
 * Date: 2006-mar-02
 * Time: 16:45:18
 */
public class SSVATControlPrinter extends SSPrinter {

    private SSNewAccountingYear iAccountingYear;

    private Date iDateFrom;

    private Date iDateTo;


    public SSVATControlPrinter(SSNewAccountingYear pAccountingYear, Date pDateFrom, Date pDateTo ){
        super();
        iAccountingYear = pAccountingYear;
        iDateFrom       = pDateFrom;
        iDateTo         = pDateTo;

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("vatcontrol.jrxml");
        setDetail      ("vatcontrol.jrxml");
        setSummary     ("vatcontrol.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    public String getTitle() {
        return SSBundle.getBundle().getString("vatcontrolreport.title");
    }




    /**
     * @return SSDefaultTableModel
     */
    protected SSDefaultTableModel getModel() {
        addParameter("dateFrom", iDateFrom );
        addParameter("dateTo"  , iDateTo);

        SSSalesTaxCalculator iCalculator = new SSSalesTaxCalculator(iAccountingYear, iDateFrom, iDateTo);

        iCalculator.calculate();

        List<SSVATControlGroup> iControlGroups = iCalculator.getControlGroups();


        SSDefaultTableModel<SSVATControlGroup> iModel = new SSDefaultTableModel<SSVATControlGroup>() {

            public Class getType() {
                return SSVATControlGroup.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                SSVATControlGroup iControlGroup = getObject(rowIndex);

                Object value = null;

                switch (columnIndex) {
                    // controlGroup.title
                    case 0:
                        value = iControlGroup.getDescription();
                        break;
                        // controlGroup.value.1
                    case 1:
                        value = iControlGroup.getSum();
                        /*
             switch( iControlGroup.getNumber() ){
                 case 1: value = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "MP1", "MPFF"); break;
                 case 2: value = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "MP2"); break;
                 case 3: value = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "MP3"); break;
             }           */
                        break;
                        // controlGroup.value.2
                    case 2:
                        BigDecimal d = iControlGroup.getSum();/*
                        switch( iControlGroup.getNumber() ){
                            case 1: d = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "MP1", "MPFF"); break;
                            case 2: d = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "MP2"); break;
                            case 3: d = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "MP3"); break;
                        }                                       */
                        switch( iControlGroup.getGroup1() ){
                            case 1: value = d.multiply( new BigDecimal(0.25)); break;
                            case 2: value = d.multiply( new BigDecimal(0.12)); break;
                            case 3: value = d.multiply( new BigDecimal(0.06)); break;
                        }

                        break;
                        // controlGroup.value.3
                    case 3:
                        value = iControlGroup.getReported();   /*
                        switch( iControlGroup.getNumber() ){
                            case 1: value = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "U1", "UVL"); break;
                            case 2: value = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "U2"); break;
                            case 3: value = SSAccountMath.getSumByVATCodeForAccounts(creditMinusDebetSum, "U3"); break;
                        }    */
                        break;
                }

                return value;
            }
        };

        iModel.addColumn("group.title");
        iModel.addColumn("group.turnover");
        iModel.addColumn("group.calculated");
        iModel.addColumn("group.reported");

        iModel.setObjects( iControlGroups );

        return iModel;
    }




}