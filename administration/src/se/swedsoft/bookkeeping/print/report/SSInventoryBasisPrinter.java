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

import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSInventoryBasisPrinter extends SSPrinter {


    private List<SSProduct> iProducts;

    private SSStock iStock;

    /**
     *
     */
    public SSInventoryBasisPrinter() {
        super();
        // Get all stock products
        this.iProducts = SSProductMath.getStockProducts( SSDB.getInstance().getProducts() );
        this.iStock    = new SSStock();

        iStock.update();

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("inventorybasis.jrxml");
        setDetail      ("inventorybasis.jrxml");
    }

    /**
     *
     * @param iDate
     */
    public SSInventoryBasisPrinter( Date iDate){
        super();
        // Get all stock products
        this.iProducts = SSProductMath.getStockProducts( SSDB.getInstance().getProducts() );
        this.iStock    = new SSStock();

        iStock.update(iDate);

        addParameter("periodTitle", SSBundle.getBundle().getString("inventorybasisreport.periodtitle"));
        addParameter("periodText", iDate);

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("inventorybasis.jrxml");
        setDetail      ("inventorybasis.jrxml");
    }


    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("inventorybasisreport.title");
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
                        value = iProduct.getUnit()  == null ? null : iProduct.getUnit().getName() ;
                        break;
                    case 4:
                        value = iStock.getQuantity(iProduct);
                        break;
                    case 5:
                        value = iProduct.getWarehouseLocation() == null ? "" : iProduct.getWarehouseLocation();
                        break;

                }

                return value;
            }
        };
        iModel.addColumn("product.number");
        iModel.addColumn("product.description");
        iModel.addColumn("product.expired");
        iModel.addColumn("product.unit");
        iModel.addColumn("product.stockquantity");
        iModel.addColumn("product.warehouselocation");

        /*Collections.sort(iProducts, new Comparator<SSProduct>() {
            public int compare(SSProduct o1, SSProduct o2) {
                return o1.getNumber().compareTo( o2.getNumber() );
            }
        });*/

        Collections.sort(iProducts, new Comparator<SSProduct>(){
            public int compare(SSProduct iProduct1, SSProduct iProduct2){
                if(iProduct1.getWarehouseLocation() == null && iProduct2.getWarehouseLocation() == null) return 0;
                else if(iProduct1.getWarehouseLocation() != null && iProduct2.getWarehouseLocation() == null) return 1;
                else if(iProduct1.getWarehouseLocation() == null && iProduct2.getWarehouseLocation() != null) return -1;
                else return iProduct1.getWarehouseLocation().compareTo(iProduct2.getWarehouseLocation());
            }
        });

        iModel.setObjects(iProducts);

        return iModel;
    }






}
