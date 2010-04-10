package se.swedsoft.bookkeeping.print.report;


import se.swedsoft.bookkeeping.calc.math.SSProductMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSStock;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
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
public class SSStockAccountPrinter extends SSPrinter {

    private SSDefaultJasperDataSource iDataSource;

    private List<SSProduct> iProducts;

    private SSStock iStock;

    private Date iDate;

    /**
     *
     */
    public SSStockAccountPrinter() {
        // Get all orders
        iProducts = SSProductMath.getStockProducts(SSDB.getInstance().getProducts());
        iStock = new SSStock();
        iDate = null;

        iStock.update();

        setPageHeader("header_period.jrxml");
        setColumnHeader("stockaccount.jrxml");
        setDetail("stockaccount.jrxml");
    }

    /**
     *
     * @param iDate
     */
    public SSStockAccountPrinter(Date iDate) {
        // Get all orders
        iProducts = SSProductMath.getStockProducts(SSDB.getInstance().getProducts());
        iStock = new SSStock();
        this.iDate = iDate;

        iStock.update(iDate);

        addParameter("periodTitle",
                SSBundle.getBundle().getString("stockvaluereport.periodtitle"));
        addParameter("periodText", iDate);

        setPageHeader("header_period.jrxml");
        setColumnHeader("stockaccount.jrxml");
        setDetail("stockaccount.jrxml");
    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("stockaccountreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {

        SSDefaultTableModel<SSProduct> iModel = new SSDefaultTableModel<SSProduct>() {

            DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);

            @Override
            public Class getType() {
                return SSAccount.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;

                SSProduct iProduct = getObject(rowIndex);

                switch (columnIndex) {
                case 0:
                    value = iProduct.getNumber();
                    break;

                case 1:
                    value = iProduct.getDescription();
                    break;

                case 2:
                    value = iProduct.getOrderpoint();
                    break;

                case 3:
                    value = iProduct.getOrdercount();
                    break;

                case 4:
                    value = iStock.getQuantity(iProduct);
                    break;

                case 5:
                    value = iStock.getReserved(iProduct);
                    break;

                case 6:
                    value = iStock.getOrdered(iProduct);
                    break;
                }

                return value;
            }
        };

        iModel.addColumn("product.number");
        iModel.addColumn("product.description");
        iModel.addColumn("product.orderpoint");
        iModel.addColumn("product.ordercount");
        iModel.addColumn("product.quantity");
        iModel.addColumn("product.reserved");
        iModel.addColumn("product.ordered");

        Collections.sort(iProducts, new Comparator<SSProduct>() {
            public int compare(SSProduct o1, SSProduct o2) {
                return o1.getNumber().compareTo(o2.getNumber());
            }
        });

        iModel.setObjects(iProducts);

        return iModel;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.report.SSStockAccountPrinter");
        sb.append("{iDataSource=").append(iDataSource);
        sb.append(", iDate=").append(iDate);
        sb.append(", iProducts=").append(iProducts);
        sb.append(", iStock=").append(iStock);
        sb.append('}');
        return sb.toString();
    }
}
