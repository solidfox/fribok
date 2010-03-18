package se.swedsoft.bookkeeping.calc.math;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.system.SSDB;

import java.util.*;

/**
 * User: Andreas Lago
 * Date: 2006-aug-03
 * Time: 17:11:57
 */
public class SSPurchaseOrderMath {

    /*
    * @param iSale
    * @param pFrom
    * @param pTo
    * @return
    */
    public static boolean inPeriod( SSPurchaseOrder iPurchaseOrder, Date pFrom, Date pTo){
        Date iDate = iPurchaseOrder.getDate();
        Date iFrom = SSDateMath.floor(pFrom);
        Date iTo   = SSDateMath.ceil (pTo);

        return (iFrom.getTime() <= iDate.getTime()) && (iDate.getTime() <= iTo.getTime());
    }

    /**
     *
     * @param iPurchaseOrder
     * @param pTo
     * @return
     */
    public static boolean inPeriod(SSPurchaseOrder iPurchaseOrder, Date pTo) {
        Date iDate = iPurchaseOrder.getDate();
        Date iTo   = SSDateMath.ceil (pTo);

        return iDate.getTime() <= iTo.getTime();
    }


    /**
     * Returns the order connected to this sales, null if none is found
     *
     * @param iInvoice
     * @return the order or null
     */
    public static List<SSPurchaseOrder> getOrdersForInvoice(SSSupplierInvoice iInvoice) {
        return getOrdersForInvoice( SSDB.getInstance().getPurchaseOrders(), iInvoice );
    }

    /**
     * Returns the order connected to this sales from the list of orders, null if none is found
     *
     * @param iOrders
     * @param iInvoice
     * @return the order or null
     */
    public static List<SSPurchaseOrder> getOrdersForInvoice(List<SSPurchaseOrder> iOrders, SSSupplierInvoice iInvoice) {
        List<SSPurchaseOrder> iFiltered = new LinkedList<SSPurchaseOrder>();

        for (SSPurchaseOrder iOrder : iOrders)
            if (iOrder.hasInvoice(iInvoice)) {
                iFiltered.add(iOrder);
            }
        return iFiltered;
    }


    /**
     * Returns all the supplier for the selected orders
     *
     * @param iOrders
     * @param iSupplierNr
     * @return
     */
    public static List<SSPurchaseOrder> getOrdersBySupplierNr(List<SSPurchaseOrder> iOrders, String iSupplierNr) {

        List<SSPurchaseOrder> iFiltered = new LinkedList<SSPurchaseOrder>();

        for (SSPurchaseOrder iOrder : iOrders) {
            if(iSupplierNr.equals(iOrder.getSupplierNr() )){
                iFiltered.add(iOrder);
            }

        }
        return iFiltered;
    }



    /**
     *
     * @param iSale
     * @param iProduct
     * @return
     */
    public static Integer getProductCount(SSPurchaseOrder iSale, SSProduct iProduct){
        Integer iCount = 0;

        for (SSPurchaseOrderRow iRow : iSale.getRows()) {
            SSProduct iRowProduct = iRow.getProduct();

            // Skip if no product or no quantity
            if(iRowProduct == null || iRow.getQuantity() == null) continue;

            // This is the product we want to get the quantity for
            if(iRowProduct.equals(iProduct)){
                iCount = iCount + iRow.getQuantity();
            }
            // Get the quantity if this is a parcel product
            if(iRowProduct.isParcel()){
                Integer iQuantity = SSProductMath.getProductCount(iRowProduct, iProduct);

                iCount = iCount + iRow.getQuantity() * iQuantity;
            }
        }
        return iCount;
    }

    /**
     * Returns all the orders without any invoice
     *
     * @param iPurchaseOrders
     * @return
     */
    public static List<SSPurchaseOrder> getOrdersWithoutInvoice(List<SSPurchaseOrder> iPurchaseOrders) {

        List<SSPurchaseOrder> iFiltered = new LinkedList<SSPurchaseOrder>();

        for (SSPurchaseOrder iOrder : iPurchaseOrders) {
            if( ! iOrder.hasInvoice() ){
                iFiltered.add(iOrder);
            }

        }
        return iFiltered;
    }




    /**
     * Removes all references to the selected sales from the sales list
     * @param iInvoice
     */
    public static void removeReference(SSSupplierInvoice iInvoice) {
        removeInvoice(SSDB.getInstance().getPurchaseOrders(), iInvoice);
    }

    /**
     * Removes all references to the selected sales from the sales list
     *
     * @param iPurchaseOrders
     * @param iInvoice
     */
    public static void removeInvoice(List<SSPurchaseOrder> iPurchaseOrders, SSSupplierInvoice iInvoice) {

        for (SSPurchaseOrder iOrder : iPurchaseOrders) {
            if(iOrder.hasInvoice(iInvoice) ){
                iOrder.setInvoice(null);
            }
        }
    }

    public static Integer getNumberOfIncommingProducts(SSProduct iProduct) {
        Integer iSum = 0;
        for (SSPurchaseOrder iPurchaseOrder : SSDB.getInstance().getPurchaseOrders()) {
            if (!iPurchaseOrder.hasInvoice()) {
                for (SSPurchaseOrderRow iRow : iPurchaseOrder.getRows()) {
                    if (iProduct.getNumber().equals(iRow.getProductNr())) {
                        iSum+=iRow.getQuantity();
                    }
                }
            }
        }
        return iSum;
    }

    public static Map<String, Integer> getStockInfluencing(List<SSPurchaseOrder> iPurchaseOrders) {
        Map<String, Integer> iPurchaseOrderCount = new HashMap<String, Integer>();
        List<String> iParcelProducts = new LinkedList<String>();
        List<SSProduct> iProducts = new LinkedList<SSProduct>(SSDB.getInstance().getProducts());
        for (SSProduct iProduct : iProducts) {
            if (iProduct.isParcel() && iProduct.getNumber() != null) {
                iParcelProducts.add(iProduct.getNumber());
            }
        }
        for (SSPurchaseOrder iPurchaseOrder : iPurchaseOrders) {
            for (SSPurchaseOrderRow iRow : iPurchaseOrder.getRows()) {
                if(iRow.getQuantity() == null) continue;
                Integer iReserved;
                if (iParcelProducts.contains(iRow.getProductNr())) {
                    SSProduct iProduct = iRow.getProduct();
                    if (iProduct != null) {
                        for (SSProductRow iProductRow : iProduct.getParcelRows()) {
                            iReserved = iPurchaseOrderCount.get(iProductRow.getProductNr()) == null ? iProductRow.getQuantity()*iRow.getQuantity() : iPurchaseOrderCount.get(iProductRow.getProductNr()) + (iProductRow.getQuantity()*iRow.getQuantity());
                            iPurchaseOrderCount.put(iProductRow.getProductNr(), iReserved);
                        }
                    }
                } else {
                    iReserved = iPurchaseOrderCount.get(iRow.getProductNr()) == null ? iRow.getQuantity() : iPurchaseOrderCount.get(iRow.getProductNr()) + iRow.getQuantity();
                    iPurchaseOrderCount.put(iRow.getProductNr(), iReserved);
                }
            }
        }
        return iPurchaseOrderCount;
    }

}
