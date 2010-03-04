package se.swedsoft.bookkeeping.calc.math;

import se.swedsoft.bookkeeping.data.*;
import se.swedsoft.bookkeeping.data.base.SSSaleRow;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.SSBookkeeping;

import java.util.*;

/**
 * User: Andreas Lago
 * Date: 2006-mar-27
 * Time: 15:42:39
 */
public class SSOrderMath extends SSTenderMath{



    /**
     * Returns all the customers for the selected orders
     *
     * @param iOrders
     * @return
     */
    public static List<SSOrder> getOrdersByCustomerNr(List<SSOrder> iOrders, String iCustomerNr) {

        List<SSOrder> iFiltered = new LinkedList<SSOrder>();

        for (SSOrder iOrder : iOrders) {
            if(iCustomerNr.equals(iOrder.getCustomerNr() )){
                iFiltered.add(iOrder);
            }

        }
        return iFiltered;
    }


    /**
     * Returns all the orders without any invoice
     *
     * @param iOrders
     * @return
     */
    public static List<SSOrder> getOrdersWithoutInvoice(List<SSOrder> iOrders) {

        List<SSOrder> iFiltered = new LinkedList<SSOrder>();

        for (SSOrder iOrder : iOrders) {
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
    public static void removeReference(SSInvoice iInvoice) {
        removeInvoice(SSDB.getInstance().getOrders(), iInvoice);
    }

    /**
     * Removes all references to the selected sales from the sales list
     *
     * @param iOrders
     * @param iInvoice
     */
    public static void removeInvoice(List<SSOrder> iOrders, SSInvoice iInvoice) {

        for (SSOrder iOrder : iOrders) {
            if (iOrder.hasInvoice(iInvoice)) {
                iOrder.setInvoice(null);
                SSDB.getInstance().updateOrder(iOrder);
            }
        }
    }







    /**
     *
     * @param iOrder
     * @return
     */
    public static Map<SSProduct, Integer> getProductCount(SSOrder iOrder){
        List<SSProduct> iProducts = SSDB.getInstance().getProducts();

        Map<SSProduct, Integer> iProductCount = new HashMap<SSProduct, Integer>();

        for (SSSaleRow iRow : iOrder.getRows()) {
            // Get the product for the row
            SSProduct iProduct = iRow.getProduct(iProducts);

            if (iProduct != null){
                Integer iCount = iRow.getQuantity();

                Integer iTotal = iProductCount.get(iProduct);

                if( iTotal == null){
                    iProductCount.put(iProduct, iCount );
                } else {
                    iProductCount.put(iProduct, iCount + iTotal);
                }

            }
        }

        return iProductCount;
    }

    /**
     *
     * @param iOrders
     * @return
     */
    public static Map<SSProduct, Integer> getProductCount(List<SSOrder> iOrders){

        Map<SSProduct, Integer> iProductCount = new HashMap<SSProduct, Integer>();

        for (SSOrder iOrder : iOrders) {
            Map<SSProduct, Integer> iCounts = getProductCount(iOrder);

            for (Map.Entry<SSProduct, Integer> iEntry : iCounts.entrySet()) {
                SSProduct iProduct = iEntry.getKey();
                Integer   iValue   = iEntry.getValue();

                Integer iTotal = iProductCount.containsKey(iProduct) ? iProductCount.get(iProduct) : 0;

                iProductCount.put(iProduct, iTotal + iValue);
            }

        }
        return iProductCount;
    }

    public static Map<String, Integer> getStockInfluencing(List<SSOrder> iOrders) {
        Map<String, Integer> iOrderCount = new HashMap<String, Integer>();
        List<String> iParcelProducts = new LinkedList<String>();
        List<SSProduct> iProducts = new LinkedList<SSProduct>(SSDB.getInstance().getProducts());
        for (SSProduct iProduct : iProducts) {
            if (iProduct.isParcel() && iProduct.getNumber() != null) {
                iParcelProducts.add(iProduct.getNumber());
            }
        }
        for (SSOrder iOrder : iOrders) {
            for (SSSaleRow iRow : iOrder.getRows()) {
                if(iRow.getQuantity() == null) continue;
                Integer iReserved;
                if (iParcelProducts.contains(iRow.getProductNr())) {
                    SSProduct iProduct = iRow.getProduct();
                    if (iProduct != null) {
                        for (SSProductRow iProductRow : iProduct.getParcelRows()) {
                            iReserved = iOrderCount.get(iProductRow.getProductNr()) == null ? iProductRow.getQuantity()*iRow.getQuantity() : iOrderCount.get(iProductRow.getProductNr()) + (iProductRow.getQuantity()*iRow.getQuantity());
                            iOrderCount.put(iProductRow.getProductNr(), iReserved);
                        }
                    }
                } else {
                    iReserved = iOrderCount.get(iRow.getProductNr()) == null ? iRow.getQuantity() : iOrderCount.get(iRow.getProductNr()) + iRow.getQuantity();
                    iOrderCount.put(iRow.getProductNr(), iReserved);
                }
            }
        }
        return iOrderCount;
    }

    public static HashMap<Integer,String> iInvoiceForOrders;

    public static void setInvoiceForOrders(){
        /*if(iInvoiceForOrders == null) iInvoiceForOrders = new HashMap<Integer,String>();

        List<SSOrder> iOrders = SSDB.getInstance().getOrders();

        for(SSOrder iOrder : iOrders){
            SSInvoice iInvoice = iOrder.getInvoice();
            if(iInvoice != null){
                iInvoiceForOrders.put(iOrder.getNumber(), iInvoice.getNumber().toString());
                continue;
            }

            SSPeriodicInvoice iPeriodicInvoice = iOrder.getPeriodicInvoice();
            if(iPeriodicInvoice != null){
                iInvoiceForOrders.put(iOrder.getNumber(), "P" + iPeriodicInvoice.getNumber().toString());
            }
        } */
    }

}
