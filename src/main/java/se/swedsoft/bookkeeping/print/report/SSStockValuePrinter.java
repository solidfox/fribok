package se.swedsoft.bookkeeping.print.report;


import se.swedsoft.bookkeeping.calc.math.SSProductMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSStock;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.graphics.SSImage;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.*;


/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSStockValuePrinter extends SSPrinter {

    private List<SSProduct> iProducts;

    private SSStock iStock;

    private Date iDate;

    private Map<SSProduct, BigDecimal> iInprices;

    /**
     *
     */
    public SSStockValuePrinter() {
        // Get all orders
        iProducts = SSProductMath.getStockProducts(SSDB.getInstance().getProducts());
        iStock = new SSStock();
        iDate = null;
        iInprices = SSProductMath.getInprices(iProducts);

        iStock.update();

        setPageHeader("header_period.jrxml");
        setColumnHeader("stockvalue.jrxml");
        setDetail("stockvalue.jrxml");
        setSummary("stockvalue.jrxml");
    }

    /**
     *
     * @param iDate
     */
    public SSStockValuePrinter(Date iDate) {
        // Get all orders
        iProducts = SSProductMath.getStockProducts(SSDB.getInstance().getProducts());
        iStock = new SSStock();
        this.iDate = iDate;
        iInprices = SSProductMath.getInprices(iProducts, iDate);

        iStock.update(iDate);

        addParameter("periodTitle",
                SSBundle.getBundle().getString("stockvaluereport.periodtitle"));
        addParameter("periodText", iDate);

        setPageHeader("header_period.jrxml");
        setColumnHeader("stockvalue.jrxml");
        setDetail("stockvalue.jrxml");
        setSummary("stockvalue.jrxml");
    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("stockvaluereport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
        addParameter("image.check", SSImage.getImage("CHECK"));

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
                    value = iProduct.isExpired();
                    break;

                case 3:
                    value = iStock.getQuantity(iProduct);
                    break;

                case 4:
                    value = iProduct.getUnit() == null
                            ? null
                            : iProduct.getUnit().getName();
                    break;

                case 5:
                    value = iInprices.get(iProduct);
                    break;
                }

                return value;
            }
        };

        iModel.addColumn("product.number");
        iModel.addColumn("product.description");
        iModel.addColumn("product.expired");
        iModel.addColumn("product.stockquantity");
        iModel.addColumn("product.unit");
        iModel.addColumn("product.inprice");

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

        sb.append("se.swedsoft.bookkeeping.print.report.SSStockValuePrinter");
        sb.append("{iDate=").append(iDate);
        sb.append(", iInprices=").append(iInprices);
        sb.append(", iProducts=").append(iProducts);
        sb.append(", iStock=").append(iStock);
        sb.append('}');
        return sb.toString();
    }
}
