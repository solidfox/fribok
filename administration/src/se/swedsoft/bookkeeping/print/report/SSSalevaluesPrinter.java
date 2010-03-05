package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSMonth;
import se.swedsoft.bookkeeping.data.SSNewCompany;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;

import java.util.*;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSSalevaluesPrinter extends SSPrinter {

    private Date        iFrom;
    private Date        iTo;


    private List<SSMonth> iMonths;


    /**
     *
     * @param iFrom
     * @param iTo

     */
    public SSSalevaluesPrinter(Date iFrom, Date iTo) {
        super();
        this.iFrom       = iFrom;
        this.iTo          = iTo;

        this.iMonths    = SSMonth.splitYearIntoMonths(iFrom,iTo);

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("salevalues.jrxml");
        setDetail      ("salevalues.jrxml");
        setSummary     ("salevalues.jrxml");
    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("salevalues.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
        addParameter("dateFrom", iFrom );
        addParameter("dateTo"  , iTo);

        SSDefaultTableModel<SSMonth> iModel = new SSDefaultTableModel<SSMonth>() {

            @Override
            public Class getType() {
                return SSProduct.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSMonth iMonth = getObject(rowIndex);
                SSNewCompany iCurrentCompany = SSDB.getInstance().getCurrentCompany();

                switch (columnIndex) {
                    case 0  :
                        value = iMonth.toString();
                        break;
                    case 1:
                        value = iCurrentCompany.getTenderValueForMonth(iMonth);
                        break;
                    case 2:
                        value = iCurrentCompany.getOrderValueForMonth(iMonth);
                        break;
                    case 3:
                        value = iCurrentCompany.getInvoiceValueForMonth(iMonth);
                        break;
                }

                return value;
            }
        };
        iModel.addColumn("month.name");
        iModel.addColumn("month.tender");
        iModel.addColumn("month.order");
        iModel.addColumn("month.invoice");

        iModel.setObjects(iMonths);

        return iModel;
    }


}
