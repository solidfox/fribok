package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.data.SSMonth;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;

import java.util.Date;
import java.util.List;

/**
 * Date: 2007-mar-21
 * Time: 15:32:42
 */
public class SSPurchasevaluePrinter extends SSPrinter {

    private Date        iFrom;
    private Date        iTo;


    private List<SSMonth> iMonths;


    /**
     *
     * @param iFrom
     * @param iTo

     */
    public SSPurchasevaluePrinter(Date iFrom, Date iTo) {
        this.iFrom       = iFrom;
        this.iTo          = iTo;

        iMonths = SSMonth.splitYearIntoMonths(iFrom,iTo);

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("purchasevalues.jrxml");
        setDetail      ("purchasevalues.jrxml");
        setSummary     ("purchasevalues.jrxml");
    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("purchasevalues.title");
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


                switch (columnIndex) {
                    case 0  :
                        value = iMonth.toString();
                        break;
                    case 1:
                        value = SSDB.getInstance().getCurrentCompany().getPurchaseOrderValueForMonth(iMonth);
                        break;
                    case 2:
                        value = SSDB.getInstance().getCurrentCompany().getSupplierInvoiceValueForMonth(iMonth);
                        break;
                }

                return value;
            }
        };
        iModel.addColumn("month.name");
        iModel.addColumn("month.purchaseorder");
        iModel.addColumn("month.supplierinvoice");

        iModel.setObjects(iMonths);

        return iModel;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.print.report.SSPurchasevaluePrinter");
        sb.append("{iFrom=").append(iFrom);
        sb.append(", iMonths=").append(iMonths);
        sb.append(", iTo=").append(iTo);
        sb.append('}');
        return sb.toString();
    }
}