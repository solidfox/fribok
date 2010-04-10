package se.swedsoft.bookkeeping.print.report;


import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSProduct;
import se.swedsoft.bookkeeping.data.SSProductRow;
import se.swedsoft.bookkeeping.data.SSStock;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Date: 2006-mar-03
 * Time: 15:32:42
 */
public class SSProductListPrinter extends SSPrinter {

    private SSParcelPrinter iPrinter;

    private SSDefaultJasperDataSource iDataSource;

    private List<SSProduct> iProducts;

    private SSStock iStock;

    /**
     *
     */
    public SSProductListPrinter() {
        this(SSDB.getInstance().getProducts());
    }

    /**
     *
     * @param iProducts
     */
    public SSProductListPrinter(List<SSProduct> iProducts) {
        // Get all orders
        this.iProducts = iProducts;

        iStock = new SSStock(true);

        setPageHeader("header_period.jrxml");
        // if (!SSVersion.app_title.contains("JFS Fakturering")) {
        setColumnHeader("productlist.jrxml");
        setDetail("productlist.jrxml");
        // } else {
        // setColumnHeader("productlist_fakt.jrxml");
        // setDetail("productlist_fakt.jrxml");
        // }
    }

    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("productlistreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {

        iPrinter = new SSParcelPrinter();
        iPrinter.generateReport();

        addParameter("Report", iPrinter.getReport());
        addParameter("Parameters", iPrinter.getParameters());

        iDataSource = new SSDefaultJasperDataSource(iPrinter.getModel());

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
                    value = iProduct.isParcel();
                    break;

                case 1:
                    value = iProduct.getNumber();
                    break;

                case 2:
                    value = iProduct.getDescription();
                    break;

                case 3:
                    value = iProduct.getUnit() == null
                            ? null
                            : iProduct.getUnit().getName();
                    break;

                case 4:
                    value = iProduct.getSellingPrice();
                    break;

                case 5:
                    value = iProduct.getWarehouseLocation() == null
                            ? ""
                            : iProduct.getWarehouseLocation();
                    break;

                case 6:
                    // if (!SSVersion.app_title.contains("JFS Fakturering")) {
                    value = iStock.getQuantity(iProduct);
                    // } else {
                    // iPrinter.setProduct(iProduct);
                    // iDataSource.reset();
                    // value = iDataSource;
                    // }
                    break;

                case 7:
                    iPrinter.setProduct(iProduct);

                    iDataSource.reset();

                    value = iDataSource;
                    break;
                }

                return value;
            }
        };

        iModel.addColumn("product.isparcel");

        iModel.addColumn("product.number");
        iModel.addColumn("product.description");
        iModel.addColumn("product.unit");
        iModel.addColumn("product.unitprice");
        iModel.addColumn("product.warehouselocation");
        // if(!SSVersion.app_title.contains("JFS Fakturering")){
        iModel.addColumn("product.warehousequantity");
        // }
        iModel.addColumn("product.rows");

        Collections.sort(iProducts, new Comparator<SSProduct>() {
            public int compare(SSProduct o1, SSProduct o2) {
                return o1.getNumber().compareTo(o2.getNumber());
            }
        });

        iModel.setObjects(iProducts);

        return iModel;
    }

    private class SSParcelPrinter extends SSPrinter {

        private SSDefaultTableModel<SSProductRow> iModel;

        /**
         *
         */
        public SSParcelPrinter() {
            setMargins(0, 0, 0, 0);

            setDetail("productlist.parcel.jrxml");
            // setSummary("productlist.parcel.jrxml");

            iModel = new SSDefaultTableModel<SSProductRow>() {

                @Override
                public Class getType() {
                    return SSProductRow.class;
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    Object value = null;

                    SSProductRow iProductRow = getObject(rowIndex);

                    SSProduct iProduct = iProductRow.getProduct(
                            SSDB.getInstance().getProducts());

                    switch (columnIndex) {
                    case 0:
                        value = iProductRow.getProductNr();
                        break;

                    case 1:
                        value = iProductRow.getDescription();
                        break;

                    case 2:
                        value = iProductRow.getQuantity();
                        break;

                    case 3:
                        value = iProduct == null || iProduct.getUnit() == null
                                ? null
                                : iProduct.getUnit().getName();
                        break;

                    }

                    return value;
                }
            };

            iModel.addColumn("product.number");
            iModel.addColumn("product.description");
            iModel.addColumn("product.count");
            iModel.addColumn("product.unit");
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
         * @param iProduct
         */
        public void setProduct(SSProduct iProduct) {
            iModel.setObjects(iProduct.getParcelRows());
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();

            sb.append(
                    "se.swedsoft.bookkeeping.print.report.SSProductListPrinter.SSParcelPrinter");
            sb.append("{iModel=").append(iModel);
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append("se.swedsoft.bookkeeping.print.report.SSProductListPrinter");
        sb.append("{iDataSource=").append(iDataSource);
        sb.append(", iPrinter=").append(iPrinter);
        sb.append(", iProducts=").append(iProducts);
        sb.append(", iStock=").append(iStock);
        sb.append('}');
        return sb.toString();
    }
}
